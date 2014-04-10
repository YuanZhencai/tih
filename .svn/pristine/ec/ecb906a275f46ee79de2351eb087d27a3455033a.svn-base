package com.wcs.tih.interaction.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.filenet.api.exception.EngineRuntimeException;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.filenet.ce.util.DownloadIdNotFoundException;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.model.DocVo;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.interaction.controller.vo.SmartImportVO;
import com.wcs.tih.interaction.service.SmartImportService;
import com.wcs.tih.model.Smartmstr;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:zhaoqian@wcs-global.com">Zhao Qian</a>
 */
@ManagedBean
@ViewScoped
public class SmartImportBean implements Serializable {

	private static final String REPLYFILENAME = "REPLYFILENAME";
	private static final String REPLYFILEID = "REPLYFILEID";
	private static final String ANSWER = "ANSWER";
	private static final String WFINSID = "WFINSID";
	private static final String FILENAME = "FILENAME";
	private static final String FILEID = "FILEID";
	private static final String QUESTIONMORE = "QUESTIONMORE";
	private static final String URGENCY = "URGENCY";
	private static final String IMPORTANCE = "IMPORTANCE";
	private static final String QUESTIONHEAD = "QUESTIONHEAD";
	private static final String TAXTYPE = "TAXTYPE";
	private static final String REGION = "REGION";
	private static final String NOT_NULL = "不允许为空。";

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private SmartImportService smartImportService;
	@ManagedProperty(value = "#{userCommonBean}")
	private UserCommonBean userCommonBean;
	private Map<String, Object> filters;
	private List<DocVo> compressFiles;

	// ==================================================Yuan==============================================//
	@PostConstruct
	public void init() {
		forSecondTable();
		searchCompressFiles();
	}

	public void searchCompressFiles() {
		this.compressFiles = this.smartImportService.findCompressFiles(this.filters);
	}

	public void export() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "开始打包：请耐心等待", "");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void complete() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "打包成功：", "请到精灵导出页面下载");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	// ==================================================Yuan==============================================//

	// ==================index.xhtml=====================
	// 参数---问题标题,index页面,自动导入显示数据的DataTable
	private String questionHead;
	private LazyDataModel<SmartImportVO> firstLazyModel;

	public void forFirstTable() {
		List<SmartImportVO> firstResult = smartImportService.queryApplyQuestion(questionHead);
		firstLazyModel = new PageModel<SmartImportVO>(firstResult, false);
	}

	public void forFirstTableReset() {
		this.questionHead = "";
	}

	// ===================Index===================
	// 参数--精灵,index页面第二个accordion下的datatable使用
	private Smartmstr st = new Smartmstr();
	private Date beginTime = null;
	private Date lastTime = null;
	private List<SmartImportVO> question;

	public void forSecondTable() {
		if (null != beginTime && null != lastTime && this.beginTime.after(lastTime)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "发布时间的结束时间不能小于起始时间!", ""));
			return;
		}
		this.question = (smartImportService.queryQuestion(st, beginTime, lastTime));
	}

	public void forSecondTableReset() {
		this.st = new Smartmstr();
		beginTime = null;
		lastTime = null;
	}

	// ===================AUTOdialog,自动导入!=================
	// 导入对话框autoimport_dialog.xhtml参数-第一个accordion的dataTable中操作按钮的弹出的dialog页面使用参数.
	// 附件逻辑: 取消的时候删除autoImportQuestionNewAddFile和autoImportReplyNewAddFile中的fileId对应的附件.
	// 不管用户最终是什么操作,过程中删除也好,怎么也好,这两个list中的数据是和提问流程无关的,所以是可以删除的,因此要对比最终提交数据,如果上面两个list的数据部存在lastList,就执行删除.
	private WfInstancemstr wfInsAuto = new WfInstancemstr(); // 首次进入页面是赋值给填制人,填制日期使用.
	private Map<String, String> wfInsProMapAuto = new HashMap<String, String>(); // 给左侧第一个accordionPanel使用
	private List<SmartImportVO> wfInsProFileList = new ArrayList<SmartImportVO>(); // 给左侧第一个accordionPanel使用的DataTable的数据
	private List<SmartImportVO> autoLoopImportAccQueList = new ArrayList<SmartImportVO>(); // 左侧第二个accordionPanel使用的参数,forAutoImportWestData方法中进行了多次的封装.
	private List<SmartImportVO> loopFileList = new ArrayList<SmartImportVO>(); // 临时量,第二个accordionPanel使用的DataTable的数据

	private Map<String, String> autoImportQuestionMap = new HashMap<String, String>(); // 右侧第一个accordionPanel使用的参数
	private List<SmartImportVO> autoImportQuestionList = new ArrayList<SmartImportVO>(); // 右侧第一个accordionPanel使用的DataTable的参数
	private List<String> autoImportQuestionNewAddFile = new ArrayList<String>(); // 记录在右侧第一个点击"添加附件"添加的新附件的List(only id)

	private Map<String, String> autoImportReplyMap = new HashMap<String, String>(); // 右侧第二个accordionPanel使用的参数
	private List<SmartImportVO> autoImportReplyList = new ArrayList<SmartImportVO>(); // 右侧第二个accordionPanel使用的DataTable的参数
	private List<String> autoImportReplyNewAddFile = new ArrayList<String>(); // 记录在右侧第二个点击"添加附件"添加的新附件的List(only id)

	private String autoUsedDefunct = "N"; // 右侧最下面的有效无效
	// 判断附件进行复制的时候不能重复复制,并给与提示信息
	private List<String> autoImportQuestionCopyList = new ArrayList<String>();
	private List<String> autoImportReplyCopyList = new ArrayList<String>();

	private void forAutoImportClear() {
		wfInsProMapAuto.clear();
		wfInsProFileList.clear();
		autoLoopImportAccQueList.clear();
		loopFileList.clear();
		autoImportQuestionMap.clear();
		autoImportQuestionList.clear();
		autoImportQuestionNewAddFile.clear();
		autoImportReplyMap.clear();
		autoImportReplyList.clear();
		autoImportReplyNewAddFile.clear();
	}

	/**
	 * (读取数据)导入精灵弹dialog"页面数据的加载.
	 * 
	 * @param iId
	 *            页面上的方法调用所传的一个参数,要UI方法传过来, autoImportQuestionMap 为数据库存储值
	 */
	public void forAutoImportWestData(Long wfInsId) {
		forAutoImportClear();
		// 获取一表的数据
		this.wfInsAuto = smartImportService.queryWfInsAutoById(wfInsId);
		String requestBy = userCommonBean.getUserRealName(wfInsAuto.getRequestBy());
		wfInsAuto.setRequestBy(requestBy);
		// 获取一表属性表的数据,并封装到一个MAP里
		this.wfInsProMapAuto = smartImportService.queryWfInsProData(wfInsAuto);
		// 将一表属性表的MAP里的值赋予到另一个MAP里,也就是将来要存入到精灵表的数据.这样是因为这些数据要修改.用上一个MAP会有问题.数据分离,容易理解.
		String questionRegion = wfInsProMapAuto.get(DictConsts.TIH_TAX_REGION);
		String questionType = wfInsProMapAuto.get(DictConsts.TIH_TAX_TYPE);
		String questionHead = wfInsProMapAuto.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_QUESTIONHEAD);
		this.autoImportQuestionMap.put(REGION, questionRegion);
		this.autoImportQuestionMap.put(TAXTYPE, questionType);
		this.autoImportQuestionMap.put(QUESTIONHEAD, questionHead);
		this.wfInsProMapAuto.put(IMPORTANCE, wfInsAuto.getImportance());
		this.wfInsProMapAuto.put(URGENCY, wfInsAuto.getUrgency());
		this.wfInsProMapAuto.put(REGION, questionRegion);
		this.wfInsProMapAuto.put(TAXTYPE, questionType);
		this.wfInsProMapAuto.put(QUESTIONHEAD, questionHead);
		// 得到步骤属性表里的第一条记录
		Map<String, String> firstStepProDataMap = this.smartImportService.queryStepmstrProFirstData(this.wfInsAuto.getNo());
		String questionMore = firstStepProDataMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION);
		String questionFileId = firstStepProDataMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID);
		String questionFileName = firstStepProDataMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME);
		this.wfInsProMapAuto.put(QUESTIONMORE, questionMore);
		this.wfInsProMapAuto.put(FILEID, questionFileId);
		this.wfInsProMapAuto.put(FILENAME, questionFileName);
		this.autoImportQuestionMap.put(QUESTIONMORE, questionMore);
		this.autoImportQuestionMap.put(FILEID, questionFileId);
		this.autoImportQuestionMap.put(FILENAME, questionFileName);
		this.autoImportQuestionMap.put(WFINSID, wfInsId.toString());
		// 将问题附件的ID,NAME进行去逗号分离,并返回一个LIST,用于dataTable显示,这里是右侧第一个datatable的显示.
		String faid = firstStepProDataMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID);
		String faname = firstStepProDataMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME);
		if (faid != null && !faid.equals("")) {
			this.autoImportQuestionList = this.attIdAndNameProcess(faid, faname);
			// 这里要进行的是左侧的提问流程的accordionPanl的显示,第一个是问题的单独accordionPanl的显示
			this.wfInsProFileList = this.attIdAndNameProcess(faid, faname);
		}

		// 下面要显示的是循环读取工作流步骤表的数据的集合.要取得3个数据,流程步骤名,回复信息,以及附件Name+ID的组.5.30加上时间和处理人,处理方式,首先获取最原始的数据库List,查询得到步骤表的所有数据
		List<WfStepmstr> wfsList = smartImportService.queryStepmstr(this.wfInsAuto.getNo());
		// 在这个for循环中要把数据装入到VO中给与要循环tab的accordionPanel
		for (int i = 0; i < wfsList.size(); i++) {
			List<SmartImportVO> loopFileTempList = new ArrayList<SmartImportVO>();
			Date processDate = wfsList.get(i).getUpdatedDatetime();
			String processBy = wfsList.get(i).getUpdatedBy();
			String processMethod = wfsList.get(i).getDealMethod();
			List<WfStepmstrProperty> wfspList = wfsList.get(i).getWfStepmstrProperties();
			String replyOpionion = "";
			String replyFileId = "";
			String replyFileName = "";
			for (int n = 0; n < wfspList.size(); n++) {
				if (wfspList.get(n).getName().equals(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION)) {
					replyOpionion = wfspList.get(n).getValue();
					if (!"提问人岗".equals(wfsList.get(i).getName())) {
						// 取提问人岗上面的回答信息作为右边的回答的数据.
						this.autoImportReplyMap.put(ANSWER, replyOpionion);
					}
				} else if (wfspList.get(n).getName().equals(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID)) {
					replyFileId = wfspList.get(n).getValue();
					this.autoImportReplyMap.put(REPLYFILEID, replyFileId);
				} else if (wfspList.get(n).getName().equals(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME)) {
					replyFileName = wfspList.get(n).getValue();
					this.autoImportReplyMap.put(REPLYFILENAME, replyFileName);
				}
			}
			// 转换,有就添加,没就null
			if (null != replyFileId && !("").equals(replyFileId)) {
				loopFileTempList = this.attIdAndNameProcess(replyFileId, replyFileName);
			}
			String tfid = autoImportReplyMap.get(REPLYFILEID);
			String tfname = autoImportReplyMap.get(REPLYFILENAME);
			if (tfid != null && !tfid.equals("")) {
				this.autoImportReplyList = this.attIdAndNameProcess(tfid, tfname);
			}
			this.autoLoopImportAccQueList.add(new SmartImportVO((long) i, wfsList.get(i).getName(), replyOpionion, loopFileTempList, processDate,
					processBy, processMethod));
		}
		// 对两个copyList进行赋值
		for (SmartImportVO vo : autoImportQuestionList) {
			this.autoImportQuestionCopyList.add(vo.getFileNameRF());
		}
		for (SmartImportVO vo : autoImportReplyList) {
			this.autoImportReplyCopyList.add(vo.getFileNameRF());
		}

	}

	// file的delete操作并不去物理删除.而是要在最终提交数据的时候对比autoImportQuestionNewAddFile和autoImportReplyNewAddFile进行删除,.
	public void deleteAutoImportFile(String fileId, String processPlace, String fileName) {
		if ("QUE".equals(processPlace)) {
			this.foreachRemoveWithListSmartImportVO(fileId, autoImportQuestionList);
			autoImportQuestionCopyList.remove(fileName);
		} else {
			this.foreachRemoveWithListSmartImportVO(fileId, autoImportReplyList);
			autoImportReplyCopyList.remove(fileName);
		}
	}

	/**
	 * <p>
	 * Description:复制功能:一个文本和附件都公用的复制方法.
	 * </p>
	 * 
	 * @param sendPlace
	 *            要复制到那个控件里,方向有问题的文本框,附件Table. 回答的文本框和附件Table
	 * @param strFirst
	 *            第一个String值,是txt 或者 fileId.
	 * @param strSecond
	 *            第二个String值,如果存在fileId,这里就是fileName.如果是txt,这里就无效了.
	 */
	public void copyMethod(String sendPlace, String strFirst, String strSecond) {
		if (sendPlace != null && sendPlace.trim().equals("txtQue")) { // 对应autoImportQuestionMap['QUESTIONMORE']
			String temp = this.autoImportQuestionMap.get(QUESTIONMORE);
			this.autoImportQuestionMap.put(QUESTIONMORE, temp + "   " + strFirst);
		} else if (sendPlace != null && sendPlace.trim().equals("txtAns")) { // 对应autoImportReplyMap['ANSWER']
			String temp = this.autoImportReplyMap.get(ANSWER);
			this.autoImportReplyMap.put(ANSWER, temp + "   " + strFirst);
		} else if (sendPlace != null && sendPlace.trim().equals("tableQue")) { // 对应autoImportQuestionList
			if (autoImportQuestionCopyList.contains(strSecond)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "这个附件您已经复制到右侧问题附件表里了!", ""));
				return;
			}
			// 这里执行一次处理,存的不再是strFirst,而是勇哥那面方法返回的ID.这里需要用strFirst来执行一次复制操作.
			try {
				String newId = smartImportService.copyDocument(strFirst);
				autoImportQuestionList.add(new SmartImportVO((long) autoImportQuestionList.size(), newId, strSecond));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "复制附件时出现问题,请联系管理员!", ""));
				return;
			}
			autoImportQuestionCopyList.add(strSecond);
		} else if (sendPlace != null && sendPlace.trim().equals("tableAns")) { // 对应autoImportReplyList
			if (autoImportReplyCopyList.contains(strSecond)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "这个附件您已经复制到右侧回复附件表里了!", ""));
				return;
			}
			try {
				String newId = smartImportService.copyDocument(strFirst);
				autoImportReplyList.add(new SmartImportVO((long) autoImportQuestionList.size(), newId, strSecond));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "复制附件时出现问题,请联系管理员!", ""));
				return;
			}
			autoImportReplyCopyList.add(strSecond);
		}
	}

	/**
	 * <p>
	 * Description:导入精灵页面的最终提交
	 * </p>
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public void sumbitAutoImport() throws UnsupportedEncodingException {
		boolean haveError = false;
		if (null == autoImportQuestionMap.get(REGION) || ("").equals(autoImportQuestionMap.get(REGION))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "地域：", NOT_NULL));
			haveError = true;
		}
		if (null == autoImportQuestionMap.get(TAXTYPE) || ("").equals(autoImportQuestionMap.get(TAXTYPE))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税种：", NOT_NULL));
			haveError = true;
		}
		if (null == autoImportQuestionMap.get(QUESTIONHEAD) || ("").equals(autoImportQuestionMap.get(QUESTIONHEAD))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题标题：", NOT_NULL));
			haveError = true;
		} else if (autoImportQuestionMap.get(QUESTIONHEAD).length() > 50) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题标题：", "长度不允许大于50个字符。"));
			haveError = true;
		}
		if (null == autoImportQuestionMap.get(QUESTIONMORE) || ("").equals(autoImportQuestionMap.get(QUESTIONMORE))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题描述：", NOT_NULL));
			haveError = true;
		} else if (autoImportQuestionMap.get(QUESTIONMORE).length() > 500) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题描述：", "长度不允许大于500个字符。"));
			haveError = true;
		}
		if (null == autoImportReplyMap.get(ANSWER) || ("").equals(autoImportReplyMap.get(ANSWER))) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "回答描述：", NOT_NULL));
			haveError = true;
		} else if (autoImportReplyMap.get(ANSWER).length() > 500) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "回答描述：", "长度不允许大于500个字符。"));
			haveError = true;
		}
		if (haveError) {
			return;
		}
		try {
			smartImportService.autoImportSubmit(autoImportQuestionMap, autoImportReplyMap, autoUsedDefunct, autoImportQuestionList,
					autoImportReplyList, autoImportQuestionNewAddFile, autoImportReplyNewAddFile);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 删除fileNet出现异常.请联系管理员.
		}
		RequestContext.getCurrentInstance().addCallbackParam("sumbitProcess", "yes");
		this.forFirstTable();
		this.forSecondTable();
	}

	/**
	 * <p>
	 * Description: 取消的时候删除上传的所有,删除newAddFileInfoWithQuestion和newAddFileInfoWithReply的数据.
	 * </p>
	 */
	public void autoCancelButtonEvent() {
		this.smartImportService.btnCancel(autoImportQuestionNewAddFile, autoImportReplyNewAddFile);
		RequestContext.getCurrentInstance().addCallbackParam("sumbitProcess", "yes");
	}

	// ==========================================手动导入页面,也就是添加页面!========================================
	/*
	 * 逻辑说明(以问题为例,回复同理): 1.进入此页面,从数据库读取附件数据.此时,赋值给updateQueAttListSubmit(用于页面显示)和updateQueAttListSQL(用于最后提交的对比),
	 * 2.在提交/取消之前,如果添加了新的附件,那么有两个添加.第一:添加这个fileId到updateQueAttListSubmit,第二:添加这个fileId到updteQueAttListNewAdd.
	 * 3.在提交/取消之前,如果删除dataTable中的附件,那么需要,第一:remove掉updateQueAttListSubmit中对应fileId.第二:添加这个fielId到updteQueAttListDelete.
	 * 4.在点击"取消"后,需要做的只有一条:根据updteQueAttListNewAdd和updteRepAttListNewAdd中fileId,在fileNet端删除掉所有的文件. 5.在点击"提交"后有下面几个步骤需要做: A:删除掉updteQueAttListDelete中fileId对应的fileNet端的文件.(并且是不存在与sqlList中的)
	 * B:对比updateQueAttListSubmit和 updateQueAttListSQL,Sumbit中不存在于SQL中的执行添加操作.反之不操作. C:对比updteQueAttListDelete和updateQueAttListSQL,delete中存在于SQL中的执行更新defunt="N"为"Y".反之不操作.
	 */
	// Smartmstr对象
	private Smartmstr smt = new Smartmstr();
	// 更新页面用于提交的问题附件list,下面3个没有get/set,页面不使用.
	private List<SmartImportVO> updateQueAttListSubmit = new ArrayList<SmartImportVO>();
	private List<SmartImportVO> updateQueAttListSQL = new ArrayList<SmartImportVO>();// 存一个最原始的数据库提问list.用于以后对比
	private List<String> updteQueAttListNewAdd = new ArrayList<String>(); // 每次添加新的附件,fileId都添加到这个List
	private List<String> updteQueAttListDelete = new ArrayList<String>(); // 每次执行删除操作,都放到这个deleteList里.
	// 更新页面用于提交的回复附件list
	private List<SmartImportVO> updateRepAttListSubmit = new ArrayList<SmartImportVO>();
	private List<SmartImportVO> updateRepAttListSQL = new ArrayList<SmartImportVO>();
	private List<String> updteRepAttListNewAdd = new ArrayList<String>();
	private List<String> updteRepAttListDelete = new ArrayList<String>();

	private String secondUsedDefunct = "N";

	/**
	 * 获取Smartmstr对象.
	 * 
	 * @param id
	 *            index页面传入方法的Id.
	 */
	public void querySmtById(long id) {
		smt = this.smartImportService.querySmtById(id);
		this.secondUsedDefunct = smt.getDefunctInd();
		updteQueAttListNewAdd.clear();// 清空这是个list.
		updteQueAttListDelete.clear();
		updteRepAttListNewAdd.clear();
		updteRepAttListDelete.clear();
		updateQueAttListSubmit = smartImportService.querySmtAttByIdAndType(id, DictConsts.TIH_TAX_ATTACH_TYPE_4);
		updateQueAttListSQL = smartImportService.querySmtAttByIdAndType(id, DictConsts.TIH_TAX_ATTACH_TYPE_4);
		updateRepAttListSubmit = smartImportService.querySmtAttByIdAndType(id, DictConsts.TIH_TAX_ATTACH_TYPE_5);
		updateRepAttListSQL = smartImportService.querySmtAttByIdAndType(id, DictConsts.TIH_TAX_ATTACH_TYPE_5);
	}

	/**
	 * <p>
	 * Description:update页面的dataTable删除使用的方法体
	 * </p>
	 * 
	 * @param fileId
	 *            附件ID,由页面上传递过来.
	 * @param numPlace
	 *            页面上传递过来,"1"为用于判断是提问附件还是回答附件,注意:glassFish页面传值,传数字默认为int类型,而WAS8默认类型为double.最好采用String,boolean类型扩展受限制.
	 */
	public void deleteSmtAtt(String fileId, String numPlace) {
		// 在这里首先要remove掉updateQueAttListSubmit/updateRepAttListSubmit中对应fileId.然后添加到对应的updteQueAttListDelete/updteRepAttListDelete中.
		if (numPlace.equals("1")) {
			this.foreachRemoveWithListSmartImportVO(fileId, updateQueAttListSubmit);
			updteQueAttListDelete.add(fileId);
		} else {
			this.foreachRemoveWithListSmartImportVO(fileId, updateRepAttListSubmit);
			updteRepAttListDelete.add(fileId);
		}
	}

	// 上面方法条用的foreach方法体.点击删除后删除对应的dataTable使用的list中的某条数据.update页面的dataTable不进行物理删除,为保证提问流程附件完整性.
	private List<SmartImportVO> foreachRemoveWithListSmartImportVO(String fileId, List<SmartImportVO> tempList) {
		List<SmartImportVO> deleteTempList = new ArrayList<SmartImportVO>();
		for (SmartImportVO siVO : tempList) {
			String deleteFileId = siVO.getFileIdRF();
			if (fileId != null && deleteFileId != null && fileId.equals(deleteFileId)) {
				deleteTempList.add(siVO);
			}
		}
		tempList.removeAll(deleteTempList);
		return tempList;
	}

	// 点击取消和X的时候删除掉这个过程中上传的附件.也就是完全删除updteQueAttListNewAdd和updteRepAttListNewAdd,如果是提交,不删除.
	public void btnCancel() {
	}

	/**
	 * <p>
	 * Description:updateXhtml页面提交按钮的操作.
	 * </p>
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public void sumbitHandImport() throws UnsupportedEncodingException {
		boolean haveError = this.validateData(smt);
		if (haveError) {
			return;
		}
		Map<String, List<SmartImportVO>> sqlAndSubmitMap = new HashMap<String, List<SmartImportVO>>();
		sqlAndSubmitMap.put("QUESUB", updateQueAttListSubmit);
		sqlAndSubmitMap.put("QUESQL", updateQueAttListSQL);
		sqlAndSubmitMap.put("REPSUB", updateRepAttListSubmit);
		sqlAndSubmitMap.put("REPSQL", updateRepAttListSQL);
		smartImportService.updateXhtmlSumbitData(smt, secondUsedDefunct, updteQueAttListDelete, updteRepAttListDelete, sqlAndSubmitMap);
		this.forSecondTable();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("更新成功!", ""));
		RequestContext.getCurrentInstance().addCallbackParam("sumbitHand", "yes");
	}

	// =================================添加新的精灵数据,所有都为新添==================
	private Smartmstr smtAdd = new Smartmstr();
	private String addPageDefunt = "N";
	private List<SmartImportVO> addQuestionList = new ArrayList<SmartImportVO>();
	private List<SmartImportVO> addAnswerList = new ArrayList<SmartImportVO>();

	public void clearAddPageData() {
		this.smtAdd = new Smartmstr();
		this.addPageDefunt = "N";
		this.addQuestionList.clear();
		this.addAnswerList.clear();
	}

	// 提交按钮方法体
	public void addPageImportBtn() throws UnsupportedEncodingException {
		// 这里要进行后台上逻辑判断
		boolean haveErrorInfo = this.validateData(smtAdd);
		if (haveErrorInfo) {
			return;
		}
		smartImportService.addXhtmlSubmitData(smtAdd, addPageDefunt, addQuestionList, addAnswerList);
		this.forSecondTable();
		RequestContext.getCurrentInstance().addCallbackParam("addPageImport", "yes");
	}

	// 关闭dialog需要执行的方法体
	public void addPageCancelBtn() {
	}

	/**
	 * @param fileId
	 *            附件ID
	 * @param num
	 *            识别addPage里dataTable位置,以便进行对应的list的删除
	 */
	public void addPageDeleteAtt(String fileIdDelete, String numPlace) {
		// 内存处理.
		List<SmartImportVO> tempList = new ArrayList<SmartImportVO>();
		List<SmartImportVO> deleteTempList = new ArrayList<SmartImportVO>();
		if (("1").equals(numPlace)) {
			tempList = addQuestionList;// 指向对应的内存.
		} else {
			tempList = addAnswerList;
		}
		for (SmartImportVO vo : tempList) {
			String fileId = vo.getFileIdRF();
			if (fileIdDelete != null && fileId != null && fileIdDelete.trim().equals(fileId.trim())) {
				deleteTempList.add(vo);
			}
		}
		tempList.removeAll(deleteTempList);
		// 在add页面直接进行删除
		try {
			this.smartImportService.deleteSingleFile(fileIdDelete);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	// =================================上传方法===============================
	private String upLoadPlace; // 用于判断是什么地方使用了多上传组件

	/**
	 * <p>
	 * Description:用于判断是什么地方使用了多上传组件
	 * </p>
	 * 
	 * @param place
	 *            update页面的问题dataTable:third,回复为:fourth
	 */
	public void enterPlace(String place) {
		this.upLoadPlace = place;
	}

	// 附件的删除,添加,下载操作参数-->上传的文件
	private UploadedFile upFile;

	public void addAtt(FileUploadEvent event) {
		upFile = event.getFile();
		if (upFile == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件不能为空!", ""));
			return;
		} else {
			String fileNameIndex = upFile.getFileName();
			String fileName = fileNameIndex.substring(0, fileNameIndex.lastIndexOf('.'));
			if (fileName.length() > 50) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件名称不能超过50个字符!", ""));
				return;
			}
			List<SmartImportVO> tempList = new ArrayList<SmartImportVO>();
			if (upLoadPlace != null && this.upLoadPlace.equals("first")) {
				// 自动导入添加的id,一个问题一个回答.
				tempList = this.autoImportQuestionList;
			} else if (upLoadPlace != null && this.upLoadPlace.equals("second")) {
				tempList = this.autoImportReplyList;
			} else if (upLoadPlace != null && this.upLoadPlace.equals("third")) {
				tempList = updateQueAttListSubmit;
			} else if (upLoadPlace != null && this.upLoadPlace.equals("fourth")) {
				tempList = updateRepAttListSubmit;
			} else if (upLoadPlace != null && this.upLoadPlace.equals("addPageQue")) {
				tempList = addQuestionList;
			} else if (upLoadPlace != null && this.upLoadPlace.equals("addPageAns")) {
				tempList = addAnswerList;
			}
			boolean repeatFile = false;
			for (SmartImportVO vo : tempList) {
				if (fileName.trim().equals(vo.getFileNameRF().trim())) {
					repeatFile = true;
				}
			}
			if (repeatFile) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, fileName + "文档已经被上传。", ""));
				return;
			}
			Document document;
			try {
				document = smartImportService.addFileCE(upFile);
			} catch (MimeException e) {
				logger.error(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档类型不正确。", ""));
				return;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传失败，请重新执行.", ""));
				return;
			}

			// 获取所上传文件的ID和Name
			String documentId = document.get_Id().toString();
			String documentName = document.get_Name().toString();

			if (upLoadPlace != null && this.upLoadPlace.equals("first")) {
				// 自动导入添加的id,一个问题一个回答.
				this.autoImportQuestionList.add(new SmartImportVO((long) (autoImportQuestionList.size() + 1), documentId, documentName));
				this.autoImportQuestionNewAddFile.add(documentId);
			} else if (upLoadPlace != null && this.upLoadPlace.equals("second")) {
				this.autoImportReplyList.add(new SmartImportVO((long) (autoImportReplyList.size() + 1), documentId, documentName));
				this.autoImportReplyNewAddFile.add(documentId);
			} else if (upLoadPlace != null && this.upLoadPlace.equals("third")) {
				// 手动导入精灵页面问题附件datatable,问题和回答两个
				updateQueAttListSubmit.add(new SmartImportVO((long) (updateQueAttListSubmit.size() + 1), documentId, documentName));
				updteQueAttListNewAdd.add(documentId);
			} else if (upLoadPlace != null && this.upLoadPlace.equals("fourth")) {
				updateRepAttListSubmit.add(new SmartImportVO((long) (updateRepAttListSubmit.size() + 1), documentId, documentName));
				updteRepAttListNewAdd.add(documentId);
			} else if (upLoadPlace != null && this.upLoadPlace.equals("addPageQue")) {
				addQuestionList.add(new SmartImportVO((long) (addQuestionList.size() + 1), documentId, documentName));
			} else if (upLoadPlace != null && this.upLoadPlace.equals("addPageAns")) {
				addAnswerList.add(new SmartImportVO((long) (addAnswerList.size() + 1), documentId, documentName));
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("文档上传成功,请查询!", ""));
		}
	}

	/**
	 * @param fileId
	 *            附件的ID
	 * @return 从fileNet上获取的文件下载给用户
	 */
	public StreamedContent downAtt(String fileId) {
		try {
			return smartImportService.downloadFile(fileId);
		} catch (EngineRuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载引擎异常!", ""));
			logger.error(e.getMessage(), e);
		} catch (DownloadIdNotFoundException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "无法找到文件!", ""));
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载出现异常，请联系系统管理员。", ""));
			return null;
		}
		return null;
	}

	/**
	 * 解析附件数据 解析为单个无逗号的数据并填充到一个List中,在页面上进行DataTable显示 解析从数据库拿到的附件信息,就是附件ID和附件名称都为逗号形式的信息fileId,fileName
	 * 
	 * @param fileId
	 *            附件ID,以逗号分割的
	 * @param fileName
	 *            附件Name,以逗号分割的
	 * @return List
	 */
	private List<SmartImportVO> attIdAndNameProcess(String fileId, String fileName) {
		logger.info("开始获取流程某个步骤的fileId:" + fileId);
		logger.info("开始获取流程某个步骤的fileName:" + fileName);
		String[] fileIdGroup = fileId.split(",");
		String[] fileNameGroup = fileName.split(",");
		if (fileIdGroup.length != fileNameGroup.length) {
			logger.info("数据库存储的数据存在问题!请联系系统管理员!");
		}
		List<SmartImportVO> list = new ArrayList<SmartImportVO>();
		for (int i = 0; i < fileIdGroup.length && i < fileNameGroup.length; i++) {
			String fileIdUsed = fileIdGroup[i];
			String fileNameUsed = fileNameGroup[i];
			list.add(new SmartImportVO((long) i, fileIdUsed, fileNameUsed));
		}
		return list;
	}

	private boolean validateData(Smartmstr smtAdd) {
		boolean haveError = false;
		if (null == smtAdd.getRegion() || ("").equals(smtAdd.getRegion())) {
			haveError = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "地域：", NOT_NULL));
		}
		if (null == smtAdd.getTaxType() || ("").equals(smtAdd.getTaxType())) {
			haveError = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税种：", NOT_NULL));
		}
		if (null == smtAdd.getName() || ("").equals(smtAdd.getName())) {
			haveError = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题标题：", NOT_NULL));
		} else if (smtAdd.getName().toString().length() > 50) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题标题：", "长度不允许大于50个字符。"));
			haveError = true;
		}
		if (null == smtAdd.getAnswer() || ("").equals(smtAdd.getAnswer())) {
			haveError = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "回答描述：", NOT_NULL));
		} else if (smtAdd.getAnswer().toString().length() > 500) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "回答描述：", "长度不允许大于500个字符。"));
			haveError = true;
		}
		if (null == smtAdd.getQuestion() || ("").equals(smtAdd.getQuestion())) {
			haveError = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题描述：", NOT_NULL));
		} else if (smtAdd.getQuestion().toString().length() > 500) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题描述：", "长度不允许大于500个字符。"));
			haveError = true;
		}
		return haveError;
	}

	public SmartImportBean() {
	}

	public String getQuestionHead() {
		return questionHead;
	}

	public void setQuestionHead(String questionHead) {
		this.questionHead = questionHead;
	}

	public LazyDataModel<SmartImportVO> getFirstLazyModel() {
		return firstLazyModel;
	}

	public void setFirstLazyModel(LazyDataModel<SmartImportVO> firstLazyModel) {
		this.firstLazyModel = firstLazyModel;
	}

	public Smartmstr getSt() {
		return st;
	}

	public void setSt(Smartmstr st) {
		this.st = st;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public WfInstancemstr getWfInsAuto() {
		return wfInsAuto;
	}

	public void setWfInsAuto(WfInstancemstr wfInsAuto) {
		this.wfInsAuto = wfInsAuto;
	}

	public Map<String, String> getWfInsProMapAuto() {
		return wfInsProMapAuto;
	}

	public void setWfInsProMapAuto(Map<String, String> wfInsProMapAuto) {
		this.wfInsProMapAuto = wfInsProMapAuto;
	}

	public UploadedFile getUpFile() {
		return upFile;
	}

	public void setUpFile(UploadedFile upFile) {
		this.upFile = upFile;
	}

	public Map<String, String> getAutoImportQuestionMap() {
		return autoImportQuestionMap;
	}

	public void setAutoImportQuestionMap(Map<String, String> autoImportQuestionMap) {
		this.autoImportQuestionMap = autoImportQuestionMap;
	}

	public List<SmartImportVO> getAutoImportQuestionList() {
		return autoImportQuestionList;
	}

	public void setAutoImportQuestionList(List<SmartImportVO> autoImportQuestionList) {
		this.autoImportQuestionList = autoImportQuestionList;
	}

	public List<SmartImportVO> getWfInsProFileList() {
		return wfInsProFileList;
	}

	public void setWfInsProFileList(List<SmartImportVO> wfInsProFileList) {
		this.wfInsProFileList = wfInsProFileList;
	}

	public List<SmartImportVO> getAutoLoopImportAccQueList() {
		return autoLoopImportAccQueList;
	}

	public void setAutoLoopImportAccQueList(List<SmartImportVO> autoLoopImportAccQueList) {
		this.autoLoopImportAccQueList = autoLoopImportAccQueList;
	}

	public Map<String, String> getAutoImportReplyMap() {
		return autoImportReplyMap;
	}

	public void setAutoImportReplyMap(Map<String, String> autoImportReplyMap) {
		this.autoImportReplyMap = autoImportReplyMap;
	}

	public List<SmartImportVO> getAutoImportReplyList() {
		return autoImportReplyList;
	}

	public void setAutoImportReplyList(List<SmartImportVO> autoImportReplyList) {
		this.autoImportReplyList = autoImportReplyList;
	}

	public String getUpLoadPlace() {
		return upLoadPlace;
	}

	public void setUpLoadPlace(String upLoadPlace) {
		this.upLoadPlace = upLoadPlace;
	}

	public String getAutoUsedDefunct() {
		return autoUsedDefunct;
	}

	public void setAutoUsedDefunct(String autoUsedDefunct) {
		this.autoUsedDefunct = autoUsedDefunct;
	}

	public Smartmstr getSmt() {
		return smt;
	}

	public void setSmt(Smartmstr smt) {
		this.smt = smt;
	}

	public String getSecondUsedDefunct() {
		return secondUsedDefunct;
	}

	public void setSecondUsedDefunct(String secondUsedDefunct) {
		this.secondUsedDefunct = secondUsedDefunct;
	}

	public Smartmstr getSmtAdd() {
		return smtAdd;
	}

	public void setSmtAdd(Smartmstr smtAdd) {
		this.smtAdd = smtAdd;
	}

	public List<SmartImportVO> getAddQuestionList() {
		return addQuestionList;
	}

	public void setAddQuestionList(List<SmartImportVO> addQuestionList) {
		this.addQuestionList = addQuestionList;
	}

	public List<SmartImportVO> getAddAnswerList() {
		return addAnswerList;
	}

	public void setAddAnswerList(List<SmartImportVO> addAnswerList) {
		this.addAnswerList = addAnswerList;
	}

	public String getAddPageDefunt() {
		return addPageDefunt;
	}

	public void setAddPageDefunt(String addPageDefunt) {
		this.addPageDefunt = addPageDefunt;
	}

	public List<SmartImportVO> getQuestion() {
		return question;
	}

	public void setQuestion(List<SmartImportVO> question) {
		this.question = question;
	}

	public List<SmartImportVO> getUpdateQueAttListSubmit() {
		return updateQueAttListSubmit;
	}

	public void setUpdateQueAttListSubmit(List<SmartImportVO> updateQueAttListSubmit) {
		this.updateQueAttListSubmit = updateQueAttListSubmit;
	}

	public List<SmartImportVO> getUpdateRepAttListSubmit() {
		return updateRepAttListSubmit;
	}

	public void setUpdateRepAttListSubmit(List<SmartImportVO> updateRepAttListSubmit) {
		this.updateRepAttListSubmit = updateRepAttListSubmit;
	}

	public UserCommonBean getUserCommonBean() {
		return userCommonBean;
	}

	public void setUserCommonBean(UserCommonBean userCommonBean) {
		this.userCommonBean = userCommonBean;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public List<DocVo> getCompressFiles() {
		return compressFiles;
	}

	public void setCompressFiles(List<DocVo> compressFiles) {
		this.compressFiles = compressFiles;
	}

}
