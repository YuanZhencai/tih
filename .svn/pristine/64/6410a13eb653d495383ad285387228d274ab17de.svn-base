package com.wcs.tih.document.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.primefaces.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.PageIterator;
import com.filenet.api.collection.PageMark;
import com.filenet.api.core.Document;
import com.wcs.base.controller.CurrentUserBean;
import com.wcs.base.tds.ConfigManager;
import com.wcs.base.tds.TdsUtilImpl;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.CommonBean;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.model.Dict;
import com.wcs.common.model.P;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.document.controller.vo.FolderVO;
import com.wcs.tih.document.controller.vo.PermissionVO;
import com.wcs.tih.document.controller.vo.UserGroupVO;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih Description: 文档管理 Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * 
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class DocumentBean extends FolderBean {
	private static final String MODIFY_PROPERTY = "modifyProperty";
	private static final String ADD = "add";
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String newNotAudited = "ce.folder.newNotAudited"; // 新建未审核
	private final String auditedPass = "ce.folder.auditPassed"; // 审核通过
	private final String checkinNotAudit = "ce.folder.checkinNotAudit"; // 检入未审核

	@ManagedProperty(value = "#{currentUser}")
	private CurrentUserBean currentUserBean;
	@ManagedProperty(value = "#{commonBean}")
	private CommonBean commonBean;

	@EJB
	private CommonService commonService;
	@EJB
	private TimeoutEmailService timeoutEmailService;

	private FnDocument query; // 查询条件map
	private Map<String, Object> mapQuery = new HashMap<String, Object>(16);

	private Map<String, String> mimeTypes; // 资源类型下拉列表

	private LazyDataModel<FnDocument> documents; // 查询结果集
	private FnDocument doc; // 当前操作的文档
	private String docModel; // 文档操作模式
	private String currentCate = ""; // 控制联动属性

	// 控制文档新增是否成功
	private String opModel;
	// 上传文件流
	private UploadedFile upFile;

	// 分类树
	private TreeNode cateNode;
	private TreeNode selectedCate;
	// 文件分类,
	private Map<String, String> cateMap = new HashMap<String, String>();
	private List<Dict> cateList = new ArrayList<Dict>();
	private List<Dict> cateListNoSpace = new ArrayList<Dict>();

	// 版本LazyDataTable
	private LazyDataModel<FnDocument> versions;
	private FnDocument version;
	private FnDocument selectedVersions[];

	// 权限
	private LazyDataModel<PermissionVO> lazyPermisses;
	private PermissionVO permiss;
	private PermissionVO[] permisses;
	private List<PermissionVO> listPerms = new ArrayList<PermissionVO>();
	private final String usual = "#AUTHENTICATED-USERS";
	// 复制权限
	private LazyDataModel<FnDocument> copyDocs;
	private FnDocument cDoc;
	private Map<String, Object> queryCopyDoc = new HashMap<String, Object>();

	// filenet人员检索
	private Map<String, String> userGroupQuery;
	private Map<String, String> userGroupType;
	private LazyDataModel<UserGroupVO> userGroups;
	private UserGroupVO currentUserGroup;

	// 多种税收
	private List<String> taxTypeList = new ArrayList<String>();
	// 文档分类
	private List<String> docTypeList = new ArrayList<String>();

	private String tabModel;

	private boolean docadmin = false;

	private boolean updatePro = false;

	private boolean updatePermission = false;

	private final String charIso = "iso-8859-1";
	private final String charUtf8 = "utf-8";
	private final String group = "GROUP";
	private final String perfix = ConfigManager.getConfigValue("app_prefix_testapp");
	private final String perfixs = ConfigManager.getConfigValue("app_prefix_testapp") + TdsUtilImpl.APP_CONNECTOR;

	// 公司查询（所属公司）
	private CompanyManagerModel company;

	private Dict selectedPublishNo = new Dict();
	private Dict selectedPublishOrg = new Dict();
	private String lang = "zh_CN";
	private WfRemindVo wfRemindVo = null;

	public DocumentBean() {
	}

	/**
	 * <p>
	 * Description: 文档管理初始化
	 * </p>
	 */
	@PostConstruct
	public void initDocumentBean() {
		lang = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
		cateNode = new DefaultTreeNode(new Dict(), null);
		buildCateTree(cateNode);
		buildCateList();
		query = new FnDocument();
		query.setMap(new HashMap<String, Object>());

		doc = new FnDocument();
		doc.setMap(new HashMap<String, Object>());
		documents = new DocSearchResultModel<FnDocument>(null, null);
		versions = new DocumentModel<FnDocument>(new ArrayList<FnDocument>());
		version = new FnDocument();
		// permission
		permiss = new PermissionVO();
		lazyPermisses = new PageModel<PermissionVO>(listPerms, false);
		// user group
		userGroupQuery = new HashMap<String, String>(2);
		userGroupType = new HashMap<String, String>(2);
		userGroupType.put("用户", "USER");
		userGroupType.put("组", "GROUP");
		userGroups = new PageModel<UserGroupVO>(new ArrayList<UserGroupVO>(), false);

		docadmin = isAdmin();
		tabModel = docadmin ? "folder" : "cate";
		copyDocs = new DocSearchResultModel<FnDocument>(null, null);

	}

	/**
	 * <p>
	 * Description: 当前用户是否为文档管理员
	 * </p>
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		return documentService.isAdmin();
	}

	/**
	 * <p>
	 * Description: 检入文档初始化
	 * </p>
	 */
	public void initCheckinDocument() {
		taxTypeList.clear();
		docTypeList.clear();
		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_2, DictConsts.TIH_TAX_REQUESTFORM_2, null);
	}

	/**
	 * <p>
	 * Description: 新增文档初始化
	 * </p>
	 */
	public void initAddDocument() {
		taxTypeList.clear();
		docTypeList.clear();
		clearFolderSelected(); // 清除目标选中项
		doc = new FnDocument();
		doc.setMap(new HashMap<String, Object>());
		doc.setAuditStatus(DictConsts.TIH_DOC_STATUS_1); // 新建未审核
		docModel = ADD;
		currentCate = "";
		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_1, DictConsts.TIH_TAX_REQUESTFORM_1, null);
	}

	/**
	 * <p>
	 * Description: 构建文档分类树
	 * </p>
	 * 
	 * @param node
	 */
	public void buildCateTree(TreeNode node) {
		Dict d = (Dict) node.getData();
		List<Dict> list;
		if (d.getId() == null) { // 第一次
			list = commonBean.getDictByCat(DictConsts.TIH_TAX_CAT);
		} else {
			list = commonBean.getDictByCat(d.getCodeCat() + "." + d.getCodeKey());
		}
		if (list.isEmpty()) { // 如果node不存在子结点
			String val = d.getCodeVal();
			cateMap.put(val, d.getCodeCat() + "." + d.getCodeKey());
		}
		for (Dict dict : list) {
			TreeNode n = new DefaultTreeNode(dict, node);
			buildCateTree(n);
		}
	}

	/**
	 * <p>
	 * Description: 构建文档类结构map
	 * </p>
	 */
	public void buildCateList() {
		List<TreeNode> list = cateNode.getChildren();
		for (TreeNode node : list) {
			Dict d = (Dict) node.getData();
			cateListNoSpace.add(d);
			cateList.add(d);
			createSunsOfCateMap(node, 1);
		}
	}

	public void createSunsOfCateMap(TreeNode node, int i) {
		List<TreeNode> list = node.getChildren();
		for (TreeNode n : list) {
			Dict d = (Dict) n.getData();
			cateListNoSpace.add(d);
			String val = "";
			for (int k = 0; k < i; k++) {
				val += "　　";
			}
			val += "|_" + d.getCodeVal();
			Dict dict = new Dict();
			dict.setId(d.getId());
			dict.setCodeVal(val);
			dict.setCodeCat(d.getCodeCat());
			dict.setCodeKey(d.getCodeKey());
			cateList.add(dict);
			createSunsOfCateMap(n, i + 1);
		}
	}

	/**
	 * <p>
	 * Description: 添加文档
	 * </p>
	 */
	public void addDocument() {
		opModel = "";
		FacesContext context = FacesContext.getCurrentInstance();
		String filename = doc.getDocumentTitle();
		String desc = doc.getDesc();

		String publishSeqNo = doc.getPublishSeqNo();
		try {
			if (filename != null && !"".equals(filename)) {
				filename = new String(filename.getBytes(charIso), charUtf8);
			}
			if (desc != null && !"".equals(desc)) {
				desc = new String(desc.getBytes(charIso), charUtf8);
			}
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			logger.error(e2.getMessage(), e2);
		}
		doc.setDocumentTitle(filename);
		doc.setDesc(desc);

		if (!(ValidateUtil.validateRequired(context, upFile, "文件：") & ValidateUtil.validateRequiredAndMax(context, filename, "文档名称：", 225)
				& ValidateUtil.validateRequired(context, doc.getCategory(), "文档类别：")
				& ValidateUtil.validateRequired(context, doc.getDocType(), "文档分类：")
				& ValidateUtil.validateRequired(context, doc.getBelongtoCompany(), "所属公司：")
				& ValidateUtil.validateMaxlength(context, desc, "简单描述：", 200) & ValidateUtil.validateMaxlength(context, publishSeqNo, "发文序号：", 50)
				& ValidateUtil.validateRegex(context, doc.getPublishYear(), "发文年度：", "^[1-2][0-9]{3}$", "请填写4位有效的年") & timeoutEmailService
					.validateWfRemindVo(wfRemindVo))) {
			return;
		}

		try {
			if (publishSeqNo != null && !"".equals(publishSeqNo)) {
				publishSeqNo = new String(publishSeqNo.getBytes(charIso), charUtf8);
			}
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.getMessage(), e1);
		}
		doc.setPublishSeqNo(publishSeqNo);

		// 不可重名
		try {
			if (documentService.isNameExists(doc.getDocumentTitle(), fixedFolder.get(newNotAudited))
					|| documentService.isNameExists(doc.getDocumentTitle(), fixedFolder.get(auditedPass))) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增文档：", "操作失败，文档名称重复。"));
				return;
			}
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增文档：", e1.getMessage()));
			return;
		}
		// 是否存在上传人主管
		if (!documentService.isExistSupervisor(DictConsts.TIH_TAX_REQUESTFORM_1, company.getId())) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增文档：", "操作失败。请设置当前上传人的上级主管"));
			return;
		}
		String fname = upFile.getFileName();
		if (fname.lastIndexOf('.') != -1) {
			filename += fname.substring(fname.lastIndexOf('.'));
		}
		InputStream is;
		try {
			is = upFile.getInputstream();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增文档：", "附件获取错误，请重新尝试。"));
			return;
		}
		// 条件查询不出来
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat ndf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (doc.getPublishTime() != null) {
			try {
				doc.setPublishTime(ndf.parse(df.format(doc.getPublishTime()) + " 18:00:00"));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (doc.getSubmitYear() != null) {
			try {
				doc.setSubmitYear(ndf.parse(df.format(doc.getSubmitYear()) + " 18:00:00"));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}

		Map<String, Object> docPros = doc.getMap();
		try {
			documentService.uploadDocument(is, docPros, filename, classid, fixedFolder.get(newNotAudited), company.getId(), wfRemindVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增文档：", "操作失败，请重新尝试"));
			return;
		}

		// 如果失败，opModel="failure",成功"successful"
		opModel = "successful";
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "文档创建成功！请等待管理员审核", ""));
	}

	/**
	 * <p>
	 * Description: 查询文档
	 * </p>
	 */
	public void searchDocuments() {
		documents = null;
		// 检验查询日期
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateTwoDate(context, mapQuery.get("startCreatedDate"), mapQuery.get("endCreatedDate"), "开始创建日期不能大于结束创建日期")
				& ValidateUtil.validateTwoDate(context, mapQuery.get("startUpdatedDate"), mapQuery.get("endUpdatedDate"), "开始修改日期不能大于结束修改日期")
				& ValidateUtil.validateTwoDate(context, mapQuery.get("startSubmitYear"), mapQuery.get("endSubmitYear"), "开始提交年度不能大于结束提交年度") & ValidateUtil
					.validateTwoDate(context, mapQuery.get("startPublishTime"), mapQuery.get("endPublishTime"), "开始发文时间不能大于结束发文时间"))) {
			return;
		}

		if (tabModel.equals("cate") && selectedCate != null) {
			Dict d = (Dict) selectedCate.getData();
			query.setCategory(d.getCodeCat() + "." + d.getCodeKey());
		} else {
			query.setCategory(null);
		}

		String queryFolderPath = null;
		if (tabModel.equals("folder") && selectedFolder != null) {
			queryFolderPath = ((FolderVO) selectedFolder.getData()).getPathName();
		}
		try {
			documents = new DocSearchResultModel<FnDocument>(documentService.getDocuments(query, mapQuery, null, queryFolderPath), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "查询文档:", "查询文档失败，请联系管理员！"));
			return;
		}
	}

	/**
	 * <p>
	 * Description: 切换Tab事件
	 * </p>
	 * 
	 * @param event
	 */
	public void tabChange(TabChangeEvent event) {
		if (event.getTab().getTitle().equals("文件夹浏览")) {
			tabModel = "folder";
		} else {
			tabModel = "cate";
		}
	}

	/**
	 * <p>
	 * Description: 清空查询条件
	 * </p>
	 */
	public void clearQuery() {
		mapQuery.clear();
		query = new FnDocument();
		query.setMap(new HashMap<String, Object>());
	}

	/**
	 * <p>
	 * Description: 根据文件夹查询文件
	 * </p>
	 */
	public void searchDocumentByFolder() {
		if (selectedFolder == null) {
			return;
		}
		clearQuery();
		try {
			documents = new DocSearchResultModel<FnDocument>(
					documentService.getDocumentsByFolder(((FolderVO) selectedFolder.getData()).getPathName()), null);
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * <p>
	 * Description: 根据类型查询文件
	 * </p>
	 */
	public void searchDocumentByCate() {
		if (selectedCate == null) {
			return;
		}
		clearQuery();
		Dict dict = (Dict) selectedCate.getData();
		try {
			documents = new DocSearchResultModel(documentService.getDocumentsByCate(dict.getCodeCat() + "." + dict.getCodeKey()), null);
		} catch (Exception e) {
			return;
		}
	}

	class DocumentModel<T extends FnDocument> extends LazyDataModel<T> {
		private static final long serialVersionUID = 1L;
		private List<T> list;

		public DocumentModel(List<T> list) {
			this.list = list;
		}

		public List<T> load(int first, int pageSize, String sortField, SortOrder arg3, Map<String, String> arg4) {
			int size = list.size();
			setRowCount(size);

			if (size > pageSize) {
				try {
					return new ArrayList<T>(list.subList(first, first + pageSize));
				} catch (IndexOutOfBoundsException e) {
					return new ArrayList<T>(list.subList(first, first + (size % pageSize)));
				}
			}
			return list;
		}

		public T getRowData(String rowKey) {
			for (T fn : list) {
				if (fn.getId().equals(rowKey)) {
					return fn;
				}
			}
			return null;
		}

		public Object getRowKey(T fd) {
			return fd.getId();
		}
	}

	/**
	 * <p>
	 * Description: 下载文档通过id
	 * </p>
	 * 
	 * @param id
	 */
	public StreamedContent getFileById(String id) {
		boolean hasPermiss = documentService.isUserHasPermiss("queryContent", id);
		if (!hasPermiss) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载出错", "你无权限做下载操作。"));
			return null;
		}
		try {
			return documentService.downloadDocument(id);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载出错：", "文件下载失败"));
			return null;
		}
	}

	/**
	 * <p>
	 * Description: 检出
	 * </p>
	 */
	public void checkOut() {
		if (!checkOutDoc()) {
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "检出：", "操作成功。请刷新查看"));
	}

	public boolean checkOutDoc() {
		if (!hasPermission("modifyContent")) {
			if (docModel.equals("checkoutdownload")) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载并检出：", "你无权限做下载并检出操作。"));
				return false;
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检出：", "你无权限做检出操作。"));
				return false;
			}
		}
		try {
			documentService.checkOutDoc(doc.getId());
			changeFnDoc();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检出：", e.getMessage()));
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Description: 下载并检出
	 * </p>
	 * 
	 * @param fd
	 * @return
	 */
	public StreamedContent getFile() {
		if (!checkOutDoc()) {
			return null;
		}
		return getFileById(doc.getId());
	}

	/**
	 * <p>
	 * Description: 取消检出
	 * </p>
	 */
	public void cancelCheckOut() {
		if (!currentUserBean.getCurrentUsermstr().getAdAccount().equals(doc.getUpdatedBy())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "取消检出：", "操作失败，你不是文档检出人员"));
			return;
		}
		if (!hasPermission("modifyContent")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "取消检出：", "你无权限做取消检出操作。"));
			return;
		}
		try {
			documentService.cancelCheckOutDoc(doc.getId());
			changeFnDoc();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "取消检出：", e.getMessage()));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "取消检出：", "操作成功。请刷新查看"));
	}

	/**
	 * <p>
	 * Description: 删除文档
	 * </p>
	 * *当有文件检出时，文档不能被删除,包含此文档的文件夹也不能被复制
	 */
	public void deleteDoc() {
		if (!hasPermission("modifyContent")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除文档：", "你无权限做删除文档操作。"));
			return;
		}
		if (doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除文档：", "操作失败，文档已被检出。"));
			return;
		}
		try {
			documentService.deleteDoc(doc.getId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除文档：", e.getMessage()));
			return;
		}
		searchDocuments();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "删除文档：", "操作成功，请刷新页面"));
	}

	/**
	 * <p>
	 * Description: 移动文档
	 * </p>
	 * *当有文件检出时，文档不能被移动,包含此文档的文件夹也不能被移动 3. 移动文档时如果需移动的文档和目标文件夹中的文档同名，采用源文档标题后面加序号的方式解决。（序号添加规则：年月日+随机数） 4. 移动文档和移动文件夹后源文档和源文件夹（包括其中的文档和子文件夹）都被删除。
	 */
	public void moveDocument(boolean flag) {
		if (!hasPermission("modifyContent")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动文档：", "你无权限做移动文档操作。"));
			RequestContext.getCurrentInstance().addCallbackParam("flag", "1");
			return;
		}
		if (doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动文档：", "操作失败，文档已被检出。"));
			return;
		}
		if (folderSelected == null || folderSelected == rootFolder) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动文档：", "操作失败，请选择目标文件夹。"));
			return;
		}
		String targetPath = ((FolderVO) folderSelected.getData()).getPathName();
		if (doc.getFoldersFiledIn().equals(targetPath)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动文档：", "请重新选择，目标文件夹为文档所在文件夹"));
			RequestContext.getCurrentInstance().addCallbackParam("flag", "4");
			return;
		}
		try {
			if (!flag && documentService.isNameExists(doc.getDocumentTitle(), targetPath)) {
				RequestContext.getCurrentInstance().addCallbackParam("flag", "2");
				return;
			}
			documentService.moveDoc(doc, targetPath);
			if (selectedFolder != null) {
				selectedFolder.setSelected(true);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动文档：", e.getMessage()));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "移动文档：", "操作成功。请刷新查看"));
		if (!flag) {
			RequestContext.getCurrentInstance().addCallbackParam("flag", "1");
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("flag", "3");
		}
	}

	/**
	 * <p>
	 * Description: 复制文档
	 * </p>
	 * *当有文件检出时，文档不能被复制,包含此文档的文件夹也不能被复制 3. 复制文档时如果需复制的文档和目标文件夹中的文档同名，采用源文档标题后面加序号的方式解决。（序号添加规则：年月日+随机数）
	 */
	public void copyDocument(boolean flag) {
		if (!hasPermission("modifyContent")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "复制文档：", "你无权限做复制文档操作。"));
			return;
		}
		if (doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "复制文档：", "操作失败，文档已被检出。"));
			return;
		}
		if (folderSelected == null || folderSelected == rootFolder) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "复制文档：", "操作失败，请选择目标文件夹。"));
			return;
		}
		String targetPath = ((FolderVO) folderSelected.getData()).getPathName();
		try {
			if (!flag && documentService.isNameExists(doc.getDocumentTitle(), targetPath)) {
				RequestContext.getCurrentInstance().addCallbackParam("flag", "2");
				return;
			}
			documentService.copyDoc(doc.getId(), targetPath);
			if (selectedFolder != null) {
				selectedFolder.setSelected(true);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "复制文档：", e.getMessage()));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "复制文档：", "操作成功。请刷新查看"));
		if (!flag) {
			RequestContext.getCurrentInstance().addCallbackParam("flag", "1");
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("flag", "3");
		}
	}

	/**
	 * <p>
	 * Description: 在复制、移动文件前，清除掉选中项
	 * </p>
	 */
	public void clearSelected() {
		if (selectedFolder != null && selectedFolder.isSelected()) {
			selectedFolder.setSelected(false);
		}
		if (folderSelected != null && folderSelected.isSelected()) {
			folderSelected.setSelected(false);
		}
	}

	/**
	 * <p>
	 * Description: 在复制、移动、新增文件夹之前，清除掉目标选中项
	 * </p>
	 */
	public void clearFolderSelected() {
		if (folderSelected != null && folderSelected.isSelected()) {
			folderSelected.setSelected(false);
		}
	}

	private void changeFnDoc() {
		try {
			FnDocument fd = documentService.getFnDocumentById(doc.getId());
			doc.setUpdatedBy(fd.getUpdatedBy());
			doc.setUpdatedDate(fd.getUpdatedDate());
			doc.setIsFrozen(fd.getIsFrozen());
			doc.setIsCurrent(fd.getIsCurrent());
			doc.setMajorVersion(fd.getMajorVersion());
			doc.setMinorVersion(fd.getMinorVersion());
			doc.setCurrentState(fd.getCurrentState());
			doc.setSize(fd.getSize());
			doc.setMimeType(fd.getMimeType());
			doc.setFoldersFiledIn(fd.getFoldersFiledIn());

			doc.setCategory(fd.getCategory());
			doc.setTaxType(fd.getTaxType());
			doc.setDocType(fd.getDocType());
			doc.setPublishOrg(fd.getPublishOrg());
			doc.setPublishNo(fd.getPublishNo());
			doc.setPublishYear(fd.getPublishYear());
			doc.setPublishSeqNo(fd.getPublishSeqNo());
			doc.setPublishTime(fd.getPublishTime());
			doc.setBelongtoCompany(fd.getBelongtoCompany());
			doc.setEffectStatus(fd.getEffectStatus());
			doc.setDesc(fd.getDesc());
			doc.setRegion(fd.getRegion());
			doc.setSubmitCompany(fd.getSubmitCompany());
			doc.setSubmitYear(fd.getSubmitYear());
			doc.setSubmitStatus(fd.getSubmitStatus());
			doc.setAuditStatus(fd.getAuditStatus());
			doc.setIndustry(fd.getIndustry());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 文档属性修改
	 * </p>
	 */
	public void editDocumentProperty() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateMaxlength(context, doc.getDesc(), "简单描述：", 200)
				& ValidateUtil.validateMaxlength(context, doc.getPublishSeqNo(), "发文序号：", 50) & ValidateUtil.validateRegex(context,
				doc.getPublishYear(), "发文年度：", "^[1-2][0-9]{3}$", "请填写4位有效的年"))) {
			changeFnDoc();
			return;
		}
		if (!hasPermission(MODIFY_PROPERTY)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改属性：", "你无权限操作。"));
			return;
		}
		if (doc.getIsFrozen()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改属性：", "操作失败，文档已被检出。"));
			return;
		}
		if (!doc.getAuditStatus().equals(DictConsts.TIH_DOC_STATUS_3)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改属性：", "操作失败，文档不是审核通过状态，请检查文档属性。"));
			return;
		}

		// 条件查询不出来
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat ndf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (doc.getPublishTime() != null) {
			try {
				doc.setPublishTime(ndf.parse(df.format(doc.getPublishTime()) + " 18:00:00"));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (doc.getSubmitYear() != null) {
			try {
				doc.setSubmitYear(ndf.parse(df.format(doc.getSubmitYear()) + " 18:00:00"));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		try {
			documentService.editDocProperty(doc, company);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改属性：", "操作失败。"));
			return;
		}
		RequestContext.getCurrentInstance().addCallbackParam(MODIFY_PROPERTY, "success");
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改属性：", "操作成功。"));
	}

	/**
	 * <p>
	 * Description: 获取文档历史版本
	 * </p>
	 */
	public void getDocumentVersions() {
		if (!hasPermission("queryContent")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "历史版本：", "无权限查看文档版本。"));
			return;
		}
		try {
			versions = new DocumentModel<FnDocument>(documentService.getDocVersions(doc.getId()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		RequestContext.getCurrentInstance().addCallbackParam("queryversions", "success");
	}

	/**
	 * <p>
	 * Description: 判断是否有选中要删除的版本
	 * </p>
	 */
	public void beforeDeleteVersion() {
		String flag = "no";
		if (selectedVersions != null && selectedVersions.length != 0) {
			flag = "yes";
		}
		if (flag.equals("no")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "请选择要删除的版本", ""));
		}
		RequestContext.getCurrentInstance().addCallbackParam("hasSelectVersions", flag);
	}

	/**
	 * <p>
	 * Description: 删除版本
	 * </p>
	 */
	public void deleteDocVersion() {
		if (!hasPermission("publish")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除版本：", "你无权限做删除版本操作。"));
			return;
		}
		if (selectedVersions == null || selectedVersions.length == 0) {
			return;
		}
		for (FnDocument fn : selectedVersions) {
			if (fn.getIsFrozen()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除版本：", "操作失败，版本" + fn.getMajorVersion() + "被检出。"));
				return;
			}
		}

		try {
			for (FnDocument fn : selectedVersions) {
				documentService.deleteDoc(fn.getId());
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除版本：", e.getMessage()));
			return;
		}
		searchDocuments();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "删除版本：", "操作成功。"));
	}

	/**
	 * <p>
	 * Description: 权限赋值
	 * </p>
	 */
	public void editDocumentPermission() {
		if (!hasPermission("all")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限赋值：", "你无权限做权限赋值操作。"));
			return;
		}
		if (doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限赋值：", "操作失败，文档已被检出。"));
			return;
		}
		try {
			List<PermissionVO> defaultList = documentService.getDefaultList();
			listPerms.addAll(defaultList);
			documentService.setPermissions(listPerms, doc.getId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限赋值：", "操作失败，请重新尝试。"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "权限赋值：", "操作成功。"));
		RequestContext.getCurrentInstance().addCallbackParam("permiss", "success");
		return;
	}

	/**
	 * <p>
	 * Description: 查询权限集
	 * </p>
	 */
	public void searchPermissions() {
		try {
			listPerms = documentService.getPermissions(doc.getId());
		} catch (Exception e) {
			return;
		}
		lazyPermisses = new PageModel<PermissionVO>(listPerms, false);
		updatePermission = hasPermission("all");
	}

	/**
	 * <p>
	 * Description: 权限联动
	 * </p>
	 * 
	 * @param p
	 */
	public void permissChange(PermissionVO p, String type) {
		if (type.equals("fullControl")) {
			if (p.isFullControl()) {
				p.setPromote(true);
				p.setModifyContent(true);
				p.setModifyProperty(true);
				p.setViewContent(true);
				p.setViewProperty(true);
				p.setPublish(true);
			}
		} else if (type.equals("promote")) {
			if (p.isPromote()) {
				p.setModifyContent(true);
				p.setModifyProperty(true);
				p.setViewContent(true);
				p.setViewProperty(true);
			} else {
				p.setFullControl(false);
			}
		} else if (type.equals("modifyContent")) {
			if (p.isModifyContent()) {
				p.setModifyProperty(true);
				p.setViewContent(true);
				p.setViewProperty(true);
			} else {
				p.setFullControl(false);
				p.setPromote(false);
			}
		} else if (type.equals(MODIFY_PROPERTY)) {
			if (p.isModifyProperty()) {
				p.setViewContent(true);
				p.setViewProperty(true);
			} else {
				p.setFullControl(false);
				p.setModifyContent(false);
				p.setPromote(false);
				p.setPublish(false);
			}
		} else if (type.equals("viewContent")) {
			if (p.isViewContent()) {
				p.setViewProperty(true);
			} else {
				p.setFullControl(false);
				p.setPromote(false);
				p.setModifyContent(false);
				p.setModifyProperty(false);
				p.setPublish(false);
			}
		} else if (type.equals("viewProperty")) {
			if (!p.isViewProperty()) {
				p.setFullControl(false);
				p.setPromote(false);
				p.setModifyContent(false);
				p.setModifyProperty(false);
				p.setViewContent(false);
				p.setPublish(false);
			}
		} else if (type.equals("publish")) {
			if (p.isPublish()) {
				p.setModifyProperty(true);
				p.setViewContent(true);
				p.setViewProperty(true);
			} else {
				p.setFullControl(false);
			}
		}
	}

	/**
	 * <p>
	 * Description: 初始化查询用户信息
	 * </p>
	 */
	public void initAddUserGroup() {
		userGroupQuery.clear();
		userGroupQuery.put("type", "USER"); // 默认选中用户
		userGroupQuery.put("name", "");
		userGroups = new PageModel<UserGroupVO>(new ArrayList<UserGroupVO>(), false);
	}

	/**
	 * <p>
	 * Description: 根据条件查询用户信息
	 * </p>
	 */
	public void searchUserGroup() {
		logger.info("perfix:" + perfix);
		userGroups = new PageModel<UserGroupVO>(new ArrayList<UserGroupVO>(), false);
		try {
			userGroups = new PageModel<UserGroupVO>(documentService.getUserGroups(userGroupQuery), false);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return;
		}
	}

	/**
	 * <p>
	 * Description: 添加权限控制人、组
	 * </p>
	 */
	public void addUserGroup() {
		if (currentUserGroup == null) {
			return;
		}
		for (PermissionVO p : listPerms) {
			if (p.getName().equals(currentUserGroup.getName())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "添加权限控制：", "当前选中项已经存在权限列表"));
				return;
			}
		}
		PermissionVO per = new PermissionVO();
		per.setId((long) listPerms.size() + 1);
		per.setName(currentUserGroup.getName());
		per.setType(currentUserGroup.getType());
		if (group.equals(currentUserGroup.getType())) {
			String groupName = documentService.findRolemstrByCode(currentUserGroup.getName().replace(perfixs, ""));
			if (usual.equals(currentUserGroup.getName())) {
				per.setValue("所有人");
			} else {
				per.setValue(!"".equals(groupName) ? groupName : currentUserGroup.getName());
			}
		} else {
			String userName = documentService.findPByName2(currentUserGroup.getName().replace(perfixs, ""));
			per.setValue(!"".equals(userName) ? userName : currentUserGroup.getName());
		}

		per.setViewProperty(true); // 默认加上查看属性
		per.setViewContent(true); // 默认加上查看内容
		listPerms.add(per);
	}

	/**
	 * <p>
	 * Description: 删除权限
	 * </p>
	 */
	public void deletePermissiones() {
		if (permisses == null || permisses.length == 0) {
			return;
		}
		if (doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限删除：", "操作失败，文档已被检出。"));
			return;
		}
		for (PermissionVO p : permisses) {
			listPerms.remove(p);
		}
	}

	/**
	 * <p>
	 * Description: 初始化权限复制dialog
	 * </p>
	 */
	public void initCopyPermission() {
		copyDocs = new DocSearchResultModel<FnDocument>(null, null);
		queryCopyDoc.clear();
		queryCopyDoc.put("name", "");
		cDoc = null;
	}

	/**
	 * <p>
	 * Description: 复制权限
	 * </p>
	 */
	public void copyPermission() {
		if (cDoc == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限复制：", "文档不能为空。"));
			return;
		}

		if (doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限复制：", "文档已被检出。"));
			return;
		}
		if (!hasPermission("all")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限复制：", "你无权限做权限复制操作。"));
			return;
		}
		List<PermissionVO> ls;
		try {
			ls = documentService.copyPermissionsFromDocToDoc(doc.getId(), cDoc.getId());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "权限复制：", "操作失败，" + e.getMessage()));
			return;
		}
		RequestContext.getCurrentInstance().addCallbackParam("info", "yes");
		if (ls == null || ls.isEmpty()) {
			return;
		}
		listPerms.addAll(ls);
		long i = 1;
		for (PermissionVO p : listPerms) {
			p.setId(i++);
		}
	}

	/**
	 * <p>
	 * Description: 查询文档
	 * </p>
	 */
	public void searchDocs() {
		try {
			copyDocs = new DocSearchResultModel(documentService.getDocuments(queryCopyDoc), null);
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * <p>
	 * Description: 是否有权限
	 * </p>
	 * 
	 * @param level
	 * @return
	 */
	public boolean hasPermission(String level) {
		return documentService.isUserHasPermiss(level, doc.getId());
	}

	/**
	 * <p>
	 * Description: 选择公司
	 * </p>
	 */
	public void selectQueryCompany(CompanyManagerModel c) {
		query.setBelongtoCompany(c == null ? "" : c.getStext());
	}

	public void selectQuerySubmitCompany(CompanyManagerModel c) {
		query.setSubmitCompany(c == null ? "" : c.getStext());
	}

	public void selectBelongCompany(CompanyManagerModel c) {
		company = c;
		doc.setBelongtoCompany(c == null ? "" : c.getStext());
	}

	public void selectSubmitCompany(CompanyManagerModel c) {
		doc.setSubmitCompany(c == null ? "" : c.getStext());
	}

	/**
	 * <p>
	 * Description: 设置当前用户是否有修改属性权限
	 * </p>
	 */
	public void setPermission() {
		taxTypeList.clear();
		docTypeList.clear();
		updatePro = hasPermission(MODIFY_PROPERTY);
	}

	/**
	 * <p>
	 * Description: 检入文档
	 * </p>
	 */
	public void checkInDocument() {
		if (!doc.getIsFrozen()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检入文档：", "文档不是检出状态，请刷新重试。"));
			return;
		}

		if (!currentUserBean.getCurrentUsermstr().getAdAccount().equals(doc.getUpdatedBy())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检入文档：", "操作失败，你不是文档检出人员。"));
			return;
		}

		if (!DictConsts.TIH_DOC_STATUS_4.equals(doc.getAuditStatus())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检入文档：", "该文档已经检入，不能重复操作。"));
			return;
		}

		if (upFile == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检入文档：", "文件不允许为空"));
			return;
		}

		// 上传人主管
		String supervisor = documentService.findSupervisorBy(doc.getBelongtoCompany(), DictConsts.TIH_TAX_REQUESTFORM_2);
		if ("".equals(supervisor)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "没有上传人主管", ""));
			return;
		}

		if (!timeoutEmailService.validateWfRemindVo(wfRemindVo)) {
			return;
		}
		InputStream is;
		try {
			is = upFile.getInputstream();
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检入文档：", "附件获取错误，请重新尝试。"));
			return;
		}
		FnDocument checkDoc = new FnDocument();
		try {
			checkDoc = documentService.getFnDocumentById(doc.getId());
		} catch (Exception e2) {
			logger.error(e2.getMessage(), e2);
		}
		Map<String, Object> docPros = checkDoc.getMap();

		String fname = upFile.getFileName();
		if (fname != null && !"".equals(fname)) {
			try {
				fname = new String(fname.getBytes(charIso), charUtf8);
			} catch (UnsupportedEncodingException e1) {
				logger.error(e1.getMessage(), e1);
			}
		}
		String filename = doc.getDocumentTitle() + (fname.lastIndexOf('.') == -1 ? "" : fname.substring(fname.lastIndexOf('.')));
		try {
			String perUserName = this.permissionOfUsers(documentService.getPermissionByAllUserName(this.doc.getId()));
			documentService.checkinDocument(is, docPros, filename, classid, fixedFolder.get(checkinNotAudit), doc.getId(), perUserName, supervisor,
					wfRemindVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "检入文档：", "操作失败，请重新尝试"));
			return;
		}
		changeFnDoc();

		opModel = "successful";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "检入文档：", "操作成功！请等待管理员审核"));
	}

	private String permissionOfUsers(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(",");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	public String findUsernameByAccount(String account) {
		P p = documentService.getNameByAdAccount(account);
		return p == null ? account : p.getNachn();
	}

	public String getDocSize(Double size) {
		DecimalFormat df = new DecimalFormat("0.00");
		String kb = df.format(size / 1024);
		if (Double.valueOf(kb) < 1024) {
			return kb + " K";
		}
		String m = df.format(Double.valueOf(kb) / 1024);
		if (Double.valueOf(m) < 1024) {
			return m + " M";
		}
		String g = df.format(Double.valueOf(m) / 1024);
		if (Double.valueOf(g) < 1024) {
			return g + " G";
		}
		return df.format(Double.valueOf(g) / 1024) + " T";
	}

	public void clearData() {
		taxTypeList = new ArrayList<String>();
		docTypeList = new ArrayList<String>();
		doc = new FnDocument();
	}

	// Getter & Setter
	public FnDocument getQuery() {
		return query;
	}

	public void setQuery(FnDocument query) {
		this.query = query;
	}

	public LazyDataModel<FnDocument> getDocuments() {
		return documents;
	}

	public FnDocument getDoc() {
		if (doc == null) {
			doc = new FnDocument();
		}
		return doc;
	}

	public void setDoc(FnDocument doc) {
		this.doc = doc;
	}

	public String getCurrentCate() {
		return currentCate;
	}

	public void setDocModel(String docModel) {
		this.docModel = docModel;
	}

	public String getDocModel() {
		return docModel;
	}

	public String getOpModel() {
		return opModel;
	}

	public void setOpModel(String opModel) {
		this.opModel = opModel;
	}

	public UploadedFile getUpFile() {
		return upFile;
	}

	public void setUpFile(UploadedFile upFile) {
		this.upFile = upFile;
	}

	public CurrentUserBean getCurrentUserBean() {
		return currentUserBean;
	}

	public void setCurrentUserBean(CurrentUserBean currentUserBean) {
		this.currentUserBean = currentUserBean;
	}

	public Map<String, String> getMimeTypes() {
		return mimeTypes;
	}

	public void setMimeTypes(Map<String, String> mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public Map<String, Object> getMapQuery() {
		return mapQuery;
	}

	public void setMapQuery(Map<String, Object> mapQuery) {
		this.mapQuery = mapQuery;
	}

	public CommonBean getCommonBean() {
		return commonBean;
	}

	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}

	public TreeNode getCateNode() {
		return cateNode;
	}

	public void setCateNode(TreeNode cateNode) {
		this.cateNode = cateNode;
	}

	public Map<String, String> getCateMap() {
		return cateMap;
	}

	public LazyDataModel<FnDocument> getVersions() {
		return versions;
	}

	public void setVersions(LazyDataModel<FnDocument> versions) {
		this.versions = versions;
	}

	public FnDocument getVersion() {
		return version;
	}

	public void setVersion(FnDocument version) {
		this.version = version;
	}

	public FnDocument[] getSelectedVersions() {
		return selectedVersions;
	}

	public void setSelectedVersions(FnDocument[] selectedVersions) {
		this.selectedVersions = selectedVersions;
	}

	public PermissionVO getPermiss() {
		return permiss;
	}

	public void setPermiss(PermissionVO permiss) {
		this.permiss = permiss;
	}

	public PermissionVO[] getPermisses() {
		return permisses;
	}

	public void setPermisses(PermissionVO[] permisses) {
		this.permisses = permisses;
	}

	public Map<String, String> getUserGroupQuery() {
		return userGroupQuery;
	}

	public void setUserGroupQuery(Map<String, String> userGroupQuery) {
		this.userGroupQuery = userGroupQuery;
	}

	public Map<String, String> getUserGroupType() {
		return userGroupType;
	}

	public void setUserGroupType(Map<String, String> userGroupType) {
		this.userGroupType = userGroupType;
	}

	public LazyDataModel<UserGroupVO> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(LazyDataModel<UserGroupVO> userGroups) {
		this.userGroups = userGroups;
	}

	public UserGroupVO getCurrentUserGroup() {
		return currentUserGroup;
	}

	public void setCurrentUserGroup(UserGroupVO currentUserGroup) {
		this.currentUserGroup = currentUserGroup;
	}

	public TreeNode getSelectedCate() {
		return selectedCate;
	}

	public void setSelectedCate(TreeNode selectedCate) {
		this.selectedCate = selectedCate;
	}

	public boolean isUpdatePro() {
		return updatePro;
	}

	public void setUpdatePro(boolean updatePro) {
		this.updatePro = updatePro;
	}

	public boolean isDocadmin() {
		return docadmin;
	}

	public List<Dict> getCateList() {
		return cateList;
	}

	public void setCateList(List<Dict> cateList) {
		this.cateList = cateList;
	}

	public List<Dict> getCateListNoSpace() {
		return cateListNoSpace;
	}

	public void setCateListNoSpace(List<Dict> cateListNoSpace) {
		this.cateListNoSpace = cateListNoSpace;
	}

	public boolean isUpdatePermission() {
		return updatePermission;
	}

	public void setUpdatePermission(boolean updatePermission) {
		this.updatePermission = updatePermission;
	}

	public LazyDataModel<FnDocument> getCopyDocs() {
		return copyDocs;
	}

	public void setCopyDocs(LazyDataModel<FnDocument> copyDocs) {
		this.copyDocs = copyDocs;
	}

	public FnDocument getcDoc() {
		return cDoc;
	}

	public void setcDoc(FnDocument cDoc) {
		this.cDoc = cDoc;
	}

	public Map<String, Object> getQueryCopyDoc() {
		return queryCopyDoc;
	}

	public void setQueryCopyDoc(Map<String, Object> queryCopyDoc) {
		this.queryCopyDoc = queryCopyDoc;
	}

	public LazyDataModel<PermissionVO> getLazyPermisses() {
		return lazyPermisses;
	}

	public void setLazyPermisses(LazyDataModel<PermissionVO> lazyPermisses) {
		this.lazyPermisses = lazyPermisses;
	}

	public Dict getSelectedPublishNo() {
		selectedPublishNo = new Dict();
		if (doc != null && doc.getPublishNo() != null) {
			selectedPublishNo = commonService.findDictByKey(doc.getPublishNo(), lang);
		}
		return selectedPublishNo;
	}

	public void setSelectedPublishNo(Dict selectedPublishNo) {
		if (selectedPublishNo != null) {
			selectedPublishNo.setLang(lang);
			selectedPublishNo = commonService.saveDict(selectedPublishNo);
			if (ADD.equals(docModel) || updatePro) {
				doc.setPublishNo(selectedPublishNo.getCodeCat() + "." + selectedPublishNo.getCodeKey());
			}
		}
		this.selectedPublishNo = selectedPublishNo;
	}

	public Dict getSelectedPublishOrg() {
		selectedPublishOrg = new Dict();
		if (doc != null && doc.getPublishOrg() != null) {
			selectedPublishOrg = commonService.findDictByKey(doc.getPublishOrg(), lang);
		}
		return selectedPublishOrg;
	}

	public void setSelectedPublishOrg(Dict selectedPublishOrg) {
		if (selectedPublishOrg != null) {
			selectedPublishOrg.setLang(lang);
			selectedPublishOrg = commonService.saveDict(selectedPublishOrg);
			if (ADD.equals(docModel) || updatePro) {
				doc.setPublishOrg(selectedPublishOrg.getCodeCat() + "." + selectedPublishOrg.getCodeKey());
			}
		}
		this.selectedPublishOrg = selectedPublishOrg;
	}

	public List<String> getTaxTypeList() {
		if (this.doc.getTaxType() != null) {
			String[] tmpTaxType = this.doc.getTaxType().split(",");
			if (tmpTaxType == null) {
				taxTypeList.add(this.doc.getTaxType());
			} else {
				for (int i = 0; i < tmpTaxType.length; i++) {
					taxTypeList.add(tmpTaxType[i]);
				}
			}
		}
		return taxTypeList;
	}

	public void setTaxTypeList(List<String> taxTypeList) {
		this.taxTypeList = taxTypeList;
		if (ADD.equals(docModel) || updatePro) {
			String tmpTaxType = "";
			for (int i = 0; i < taxTypeList.size(); i++) {
				tmpTaxType = tmpTaxType + taxTypeList.get(i);
				if (i != (taxTypeList.size() - 1)) {
					tmpTaxType = tmpTaxType + ",";
				}
			}
			this.doc.setTaxType(tmpTaxType);
		}
	}

	public List<String> getDocTypeList() {
		if (this.doc.getDocType() != null) {
			String[] tmpDocType = this.doc.getDocType().split(",");
			if (tmpDocType == null) {
				docTypeList.add(this.doc.getDocType());
			} else {
				for (int i = 0; i < tmpDocType.length; i++) {
					docTypeList.add(tmpDocType[i]);
				}
			}
		}
		return docTypeList;
	}

	public void setDocTypeList(List<String> docTypeList) {
		this.docTypeList = docTypeList;
		if (ADD.equals(docModel) || updatePro) {
			String tmpDocType = "";
			for (int i = 0; i < docTypeList.size(); i++) {
				tmpDocType = tmpDocType + docTypeList.get(i);
				if (i != (docTypeList.size() - 1)) {
					tmpDocType = tmpDocType + ",";
				}
			}
			this.doc.setDocType(tmpDocType);
		}
	}

	public void resetTableById() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Object param = facesContext.getExternalContext().getRequestParameterMap().get("documentTableName");
		String tableName = null;
		if (param != null && !"".equals(param.toString().trim())) {
			tableName = param.toString().trim();
		} else {
			tableName = "right_document_list_id";
		}
		UIComponent dataTable = ComponentUtils.findComponent(facesContext.getViewRoot(), tableName);
		DataTable table = (DataTable) dataTable;
		table.reset();
	}

	public CompanyManagerModel getCompany() {
		return company;
	}

	public void setCompany(CompanyManagerModel company) {
		this.company = company;
	}

	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}

	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}

	class DocSearchResultModel<T extends FnDocument> extends LazyDataModel<T> {
		private static final long serialVersionUID = 1L;
		private List<T> list;
		private DocumentSet docSet;// 总结果集
		private PageMark pageMark;// 当前书签
		private int size;
		private int last = -1;

		public DocSearchResultModel(DocumentSet documentSet, PageMark pageMark) {
			this.list = new ArrayList<T>();
			this.docSet = documentSet;
			this.pageMark = pageMark;
			size = 0;
		}

		public List<T> load(int first, int pageSize, String sortField, SortOrder arg3, Map<String, String> arg4) {
			try {
				if (first > this.last) {
					documentService.connect();
					if (docSet != null) {
						PageIterator pageIterator = docSet.pageIterator();
						pageIterator.setPageSize(pageSize);
						if (this.pageMark != null) {
							pageIterator.reset(this.pageMark);
						} else {
							if (first != 0) {
								first = 0;
								resetTableById();
							}
						}
						int times = first == 0 ? 2 : 1;
						int currentTimes = 0;
						while (pageIterator.nextPage() && currentTimes < times) {
							Object[] docArrays = pageIterator.getCurrentPage();
							for (int i = 0; i < docArrays.length; i++) {
								FnDocument fnDocument = documentService.conversionDocument(((Document) docArrays[i]));
								list.add((T) fnDocument);
							}
							this.size = this.size + pageIterator.getElementCount();
							currentTimes++;
						}
						this.pageMark = pageIterator.getPageMark();
						this.last = first;
					}
					setRowCount(size);
				}

				if (size > pageSize) {
					try {
						return new ArrayList<T>(list.subList(first, first + pageSize));
					} catch (IndexOutOfBoundsException e) {
						return new ArrayList<T>(list.subList(first, first + (size % pageSize)));
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return list;
		}

		public T getRowData(String rowKey) {
			for (T fn : list) {
				if (fn.getId().equals(rowKey)) {
					return fn;
				}
			}
			return null;
		}

		public Object getRowKey(T fd) {
			return fd.getId();
		}
	}
}
