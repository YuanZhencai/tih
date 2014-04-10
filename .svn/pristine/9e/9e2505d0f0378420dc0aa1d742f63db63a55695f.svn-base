package com.wcs.tih.document.service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.GroupSet;
import com.filenet.api.collection.UserSet;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.PrincipalSearchAttribute;
import com.filenet.api.constants.PrincipalSearchSortType;
import com.filenet.api.constants.PrincipalSearchType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.EntireNetwork;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.Versionable;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.security.Group;
import com.filenet.api.security.Realm;
import com.filenet.api.security.User;
import com.filenet.api.util.Id;
import com.wcs.base.service.LoginService;
import com.wcs.base.tds.ConfigManager;
import com.wcs.base.tds.TdsUtilImpl;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.document.controller.vo.FolderVO;
import com.wcs.tih.document.controller.vo.PermissionVO;
import com.wcs.tih.document.controller.vo.Persion;
import com.wcs.tih.document.controller.vo.UserGroupVO;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.common.service.ServiceFactory;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.system.service.OrganizationLevelInterface;
import com.wcs.tih.util.DateUtil;
/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
@SuppressWarnings("unchecked")
public class DocumentService {
    private static final String USER_PASSWORD = "user.password";

    private Logger logger = LoggerFactory.getLogger(DocumentService.class);
    
    @PersistenceContext 
    private EntityManager em;
    
    @EJB private CEserviceLocal ceservice;
    @EJB private LoginService loginService;
    @EJB private UserCommonService userCommonService;
    @EJB private FileNetUploadDownload fud; 
    // 上传下载组件
    @EJB private DocumentAuditService documentAuditService;
    @EJB private DocumentCheckinService checkin;
    @EJB private OrganizationLevelInterface organizationLevelInterface;
    @EJB private TDSLocal tdsLocal;
    
    private ResourceBundle rb = ResourceBundle.getBundle("filenet");
    private ResourceBundle role = ResourceBundle.getBundle("roles");
    private String documentAdmin = "fnadmins";
    private static final String TIH_DOC_STATUS_1 = DictConsts.TIH_DOC_STATUS_1;
    private static final String TIH_DOC_STATUS_3 = DictConsts.TIH_DOC_STATUS_3;  
    private static final String TIH_DOC_STATUS_4 = DictConsts.TIH_DOC_STATUS_4;
    private static final String TIH_DOC_STATUS_5 = DictConsts.TIH_DOC_STATUS_5;
    private final String uid = rb.getString("tds.users.uid");
    private final String cn = "cn=";
    private final String adminPassport = "admin.id";
    private final String adminPassword = "admin.password";
    private final Integer allControlPermission = 986583;
    private final Integer checkInPermission = 999415;
    private final String defaultGroup = "#AUTHENTICATED-USERS";
    private final String checkinFolder = rb.getString("ce.folder.auditPassed").trim();
    
    private final String perfixApp = ConfigManager.getConfigValue("app_prefix_testapp");
    private final String perfix = ConfigManager.getConfigValue("app_prefix_testapp")+TdsUtilImpl.APP_CONNECTOR;
    private final List<PermissionVO> defaultDisplay = new ArrayList<PermissionVO>();
   

    public DocumentService() {}
    /**
     * <p>Description: 根据人员帐号取名称</p>
     * @param account
     * @return
     */
    public String getUsernameByAccount(String account) {
        UsermstrVo uv = userCommonService.getUsermstrVo(account);
        if(uv == null || uv.getP() == null){
        	return account;
        }
        return uv.getP().getNachn();
    }
    /**
     * <p>Description: 当前登陆人登陆filenet CE</p>
     * @throws Exception
     */
    public void connect() throws Exception {
        ceservice.connect(tdsLocal.addPre(loginService.getCurrentUsermstr().getAdAccount()), rb.getString(USER_PASSWORD));
    }
    /**
     * <p>Description: 使用filenet管理员帐号登陆filenet CE</p>
     * @throws Exception
     */
    public void adminConnect() throws Exception {
        ceservice.connect(tdsLocal.addPre(rb.getString("admin.id")), rb.getString("admin.password"));
    }
    
    public void findFolderTree(TreeNode parent) throws Exception {
        connect();
        buildFolderTree(parent);
    }
    /**
     * <p>Description: 构建文件夹树，递归</p>
     * @param parent
     */
    private void buildFolderTree(TreeNode parent) {
        FolderVO pfvo = (FolderVO) parent.getData();
        List<Folder> sfs = ceservice.getSubFolders(pfvo.getPathName());
        List<FolderVO> subFolders = pfvo.getSubFolders();
        for(Folder f : sfs) {
            // IsHiddenContainer,判断是否为系统文件夹还是后创建文件夹
            if(!f.getProperties().getBooleanValue("IsHiddenContainer")) {
                FolderVO sfvo = new FolderVO(f);
                subFolders.add(sfvo);
                TreeNode node = new DefaultTreeNode(sfvo, parent);
                buildFolderTree(node);
            }
        }
    }
    
    /**
     * <p>Description: 创建文件夹</p>
     * 同时分配权限：
     * 普通用户有添加、修改、删除文件夹内的文件权限
     * 文档管理员，拥有全部权限
     * @param folderName
     * @param parentFolderPath
     * @return
     * @throws EngineRuntimeException
     */
    public Folder createFolder(String folderName, String parentFolderPath) throws Exception {
        adminConnect();
        try {
            Folder parent = ceservice.getFolder(parentFolderPath);
            Folder sub = parent.createSubFolder(folderName);
            // 继承父文件夹权限
            sub.set_InheritParentPermissions(true);
            sub.save(RefreshMode.REFRESH);
            
            String an = tdsLocal.addPre(role.getString(documentAdmin));
            boolean ia = false;
            
            AccessPermissionList apl = sub.get_Permissions();
            for(Iterator<AccessPermission> it = apl.iterator(); it.hasNext();) {
                AccessPermission ap = it.next();
                String n = getGrantName(ap.get_GranteeName().toString(), ap.get_GranteeType().toString());
                if(n.equals(defaultGroup)) {
                    ap.set_AccessType(AccessType.ALLOW);
                    ap.set_AccessMask(131121);
                }
                if(n.equals(an)) {
                    ia = true;
                }
            }
            // 添加文档管理员组完全控制
            if(!ia) {
                AccessPermission ap = createPermissionWithPre(an, 999415);
                apl.add(ap);
            }
            
            sub.set_Permissions(apl);
            sub.save(RefreshMode.REFRESH);
            return sub;
        } catch (EngineRuntimeException e) {
            logger.error(e.getMessage(), e);
            if(e.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE) {
                throw new Exception("不允许重名.");
            } else {
                throw new Exception("新增失败.");
            }
        }
    }
    /**
     * <p>Description: 修改文件夹</p>
     * @param folderName
     * @param folderPath
     * @return
     * @throws EngineRuntimeException
     */
    public Folder updateFolder(String folderName, String folderPath) throws Exception {
        adminConnect();
        Folder folder;
        try {
            folder = ceservice.getFolder(folderPath);
            folder.set_FolderName(folderName);
            folder.save(RefreshMode.REFRESH);
        } catch (EngineRuntimeException e) {
            logger.error(e.getMessage(), e);
            if(e.getExceptionCode() == ExceptionCode.E_NOT_UNIQUE) {
                throw new Exception("不允许重名.");
            } else {
                throw new Exception("重命名失败.");
            }
        }
        return folder;
    }
    /**
     * <p>Description: 删除文件夹</p>
     * @param folderName
     * @throws Exception
     */
    public void deleteFolder(String folderPath) throws Exception {
        adminConnect();
        Folder folder = ceservice.getFolder(folderPath);
        if(folder.get_SubFolders().iterator().hasNext()) {
            throw new Exception("文件夹非空，含有子文件夹。");
        }
        if(folder.get_Containers().iterator().hasNext()) {
            throw new Exception("文件夹非空，含有子文档内容。");
        }
        if(folder.get_Containees().iterator().hasNext()) {
            throw new Exception("文件夹非空，含有子文档内容。");
        }
        try {
            folder.delete();
            folder.save(RefreshMode.REFRESH);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("删除失败.");
        }
    }
    
    /**
     * <p>Description: 文件夹内是否有检出文档</p>
     * @param folderPath
     * @return
     * @throws Exception 
     */
    private Document isDocCheckedout(String folderPath) throws Exception {
        Folder f = ceservice.getFolder(folderPath);
        DocumentSet ds = f.get_ContainedDocuments();
        Document d;
        for(Iterator<Document> it = ds.iterator(); it.hasNext();) {
            d = it.next();
            if(d.get_IsReserved()) {
                return d;
            }
        }
        FolderSet fs = f.get_SubFolders();
        for(Iterator<Folder> it = fs.iterator(); it.hasNext();) {
            isDocCheckedout(it.next().get_PathName());
        }
        return null;
    }
    /**
     * <p>Description: 移动文件夹</p>
     * @param sourcePath
     * @param targetPath
     * @throws Exception
     */
    public void moveFolder(String sourcePath, String targetPath) throws Exception {
        adminConnect();
        Document cd = isDocCheckedout(sourcePath);
        if(cd != null) {
            throw new Exception("文件夹内存在[" + cd.get_Name() + "]文件被检出，不允许移动整个文件夹。");
        }
        Folder sourceFolder = ceservice.getFolder(sourcePath);
        Folder targetFolder = ceservice.getFolder(targetPath);
        Iterator<Folder> it = targetFolder.get_SubFolders().iterator();
        while(it.hasNext()) {
            if(it.next().get_FolderName().equals(sourceFolder.get_FolderName())) {
                throw new Exception("目标文件夹下已经有同名文件夹，不可移动");
            }
        }
        sourceFolder.set_Parent(targetFolder);
        sourceFolder.save(RefreshMode.REFRESH);
        targetFolder.save(RefreshMode.REFRESH);
    }
    
    /**
     * <p>Description: 拷贝文件夹</p>
     * @param sourcePath
     * @param targetPath
     * @throws Exception
     */
    public void copyFolder(String sourcePath, String targetPath) throws Exception {
        adminConnect();
        Document cd = isDocCheckedout(sourcePath);
        if(cd != null) {
            throw new Exception("文件夹内存在[" + cd.get_Name() + "]文件被检出，不允许复制整个文件夹。");
        }
        Folder sourceFolder = ceservice.getFolder(sourcePath);
        Folder targetFolder = ceservice.getFolder(targetPath);
        Iterator<Folder> it = targetFolder.get_SubFolders().iterator();
        while(it.hasNext()) {
            if(it.next().get_FolderName().equals(sourceFolder.get_FolderName())) {
                throw new Exception("目标文件夹下已经有同名文件夹，不可复制");
            }
        }
        
        copyFolderLoops(targetFolder, sourceFolder);
    }
    /**
     * <p>Description: 递归拷贝文件夹</p>
     * @param parentPath
     * @param copyPath
     * @throws Exception
     */
    private void copyFolderLoops(Folder parent, Folder copy) throws Exception {
        // 拷贝文件夹
        Folder newFolder = ceservice.createSubFolder(parent, copy.get_FolderName());
        newFolder.set_Permissions(copy.get_Permissions());
        //拷贝文件夹内的文档
        DocumentSet ds = copy.get_ContainedDocuments();
        if(ds != null && !ds.isEmpty()) {
            for(Iterator<Document> it = ds.iterator(); it.hasNext(); ) {
                copyDocument(it.next().get_Id().toString(), newFolder.get_PathName());
            }
        }
        //拷贝文件夹的子文件夹
        FolderSet fs = copy.get_SubFolders();
        if(fs != null && !fs.isEmpty()) {
            for(Iterator<Folder> it = fs.iterator(); it.hasNext();) {
                copyFolderLoops(newFolder, it.next());
            }
        }
    }
    /**
     * <p>Description: 实现文档拷贝</p>
     * @param sourceDoc
     * @param targetFolderPath
     * @throws Exception 
     */
    public Document copyDocument(String id, String targetFolderPath) throws Exception {
        Document sourceDoc;
        Document newDoc;
        try {
            sourceDoc = Factory.Document.fetchInstance(ceservice.getStore(), id, null);
            newDoc = Factory.Document.createInstance(ceservice.getStore(), sourceDoc.getClassName());
            // set content
            ContentElementList contentList = Factory.ContentElement.createList();
            ContentTransfer content = Factory.ContentTransfer.createInstance();
            content.setCaptureSource(sourceDoc.accessContentStream(0));
            contentList.add(content);
            newDoc.set_ContentElements(contentList);
            // check in the document
            newDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            // Set the custom properties
            Properties propMap = newDoc.getProperties();
            Properties sourceProp = sourceDoc.getProperties();
            // 复制属性值
            copyFnDocPros(sourceProp, propMap);
            // Set the MIME type
            newDoc.set_MimeType(sourceDoc.get_MimeType());
            newDoc.save(RefreshMode.REFRESH);

            AccessPermissionList apl = Factory.AccessPermission.createList();
            copyPermissions(sourceDoc.get_Permissions(), apl);
            newDoc.set_Permissions(apl);
            newDoc.save(RefreshMode.REFRESH);

            Folder folder = Factory.Folder.fetchInstance(ceservice.getStore(), targetFolderPath, null);
            ReferentialContainmentRelationship rel = folder.file(newDoc, AutoUniqueName.AUTO_UNIQUE, sourceDoc.get_Name(),
                    DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
            rel.save(RefreshMode.NO_REFRESH);
        } catch (Exception e) {
            throw new Exception("拷贝文档出错");
        }
        return newDoc;
    }
    private void copyPermissions(AccessPermissionList source, AccessPermissionList target) {
        for(Iterator<AccessPermission> it = source.iterator(); it.hasNext();) {
            AccessPermission old = it.next();
            if(old.get_GranteeType().toString().equals("UNKNOWN")){
            	continue;
            }
            AccessPermission ap = createPermissionWithPre(getGrantName(old.get_GranteeName().toString(), old.get_GranteeType().toString()), old.get_AccessMask());
            target.add(ap);
        }
    }
    /**
     * <p>Description: 是否存在上级主管</p>
     * @return
     */
    public boolean isExistSupervisor(String requestform, Long companyId) {
        if(companyId == null || "".equals(companyId)){
            return false;
        }
        UsermstrVo uv = organizationLevelInterface.getUsermstrVo(
                loginService.getCurrentUsermstr().getAdAccount(), requestform, companyId);
        if(null == uv || null == uv.getUsermstr()) {
            return false;
        }
        return true;
    }
    
    /**
     * <p>Description: 上传文档</p>
     * @param is            文件流
     * @param docPros       文件属性
     * @param filename      文件名称，带后缀
     * @param classid       filenet文件类型
     * @param foldername    文件保存到的文件夹
     * @param wfRemindVo 
     * @throws Exception    错误操作抛出异常
     */
   
    public void uploadDocument(InputStream is, Map<String, Object> docPros, String filename, String classid, String foldername, Long companyId, WfRemindVo wfRemindVo) throws Exception {
        Document dd= fud.upLoadDocument(is, docPros, filename, classid, foldername);
        bindAdimnToDoc(dd.get_Id().toString());
        this.documentAuditService.createWorkflow(dd, companyId, wfRemindVo);
    }
    /**
     * <p>Description: 根据id获取文件流</p>
     * @param id
     * @return
     * @throws Exception
     */
    public StreamedContent downloadDocument(String id) throws Exception {
        return fud.downloadDocumentEncoding(id, "utf-8", "iso-8859-1");
    }
    /**
     * <p>Description: 文档权限复制查询</p>
     * @param q
     * @return
     * @throws Exception
     */
    public DocumentSet getDocuments(Map<String, Object> q) throws Exception {
        FnDocument fd = new FnDocument();
        fd.setDocumentTitle(q.get("name") == null ? null : q.get("name").toString());
        return getDocuments(fd, new HashMap<String, Object>(), null, null);
    }
    /**
     * <p>Description: 查询文档</p>
     * @param q
     * @return
     */
    public DocumentSet getDocuments(FnDocument q, Map<String, Object> mapQuery, String floder, String subFloder) throws Exception {
				connect();
				String subfix = "T000000Z";
		        DateFormat df = new SimpleDateFormat("yyyyMMdd");
		        
		        StringBuilder sqlStr = new StringBuilder(
		                "SELECT d.Creator,d.DateCreated,d.LastModifier,d.DateLastModified,d.id,d.IsReserved,d.IsCurrentVersion,d.MajorVersionNumber,d.MinorVersionNumber,d.CurrentState,d.ContentSize,d.MimeType,d.DocumentTitle," +
		                "d.category,d.taxType,d.docType,d.publishOrg,d.publishNo,d.publishYear,d.publishSeqNo,d.publishTime,d.belongtoCompany,d.effectStatus,d.[desc],d.region,d.submitCompany,d.submitYear,d.submitStatus,d.auditStatus,d.industry" +
		                ",d.FoldersFiledIn" +
		                " from TihDoc d ");
		        Object text = mapQuery.get("text");
		        if(text != null && !text.toString().trim().equals("")) {  
		            //正文检索
		            sqlStr.append("INNER JOIN ContentSearch v ON v.QueriedObject = d.This " +
		                    "WHERE d.IsCurrentVersion=true AND CONTAINS(d.*, '" + text.toString().trim() + "') ");
		        } else {
		            sqlStr.append("WHERE d.IsCurrentVersion=true ");
		        }
		        
		        if(!loginService.hasRole(role.getString(documentAdmin))){
		            sqlStr.append("AND d.This INSUBFOLDER '" + checkinFolder + "' ");
		        }
		        
		        // 查询选中的文件夹下文件
		        if(floder != null) {
		            sqlStr.append("AND d.This INFOLDER '" + floder.trim() + "' ");
		        }
		        // 查询选中的文件夹下文件
		        if(subFloder != null) {
		            sqlStr.append("AND d.This INSUBFOLDER '" + subFloder.trim() + "' ");
		        }
		        
		        // 基本属性
		        String documentTitle = q.getDocumentTitle();
		        sqlStr.append(documentTitle == null || "".equals(documentTitle.trim()) ? "" : "AND d.DocumentTitle LIKE '%" + documentTitle.trim() + "%' ");
		        
		        String createdBy = q.getCreatedBy();
		        if(createdBy!=null && !"".equals(createdBy)){
		            List<String> adAccounts = findAdAccountBySearchName(createdBy);
		            sqlStr.append(" AND (");
		            for (int i = 0; i < adAccounts.size(); i++) {
		                sqlStr.append(" d.Creator LIKE '%" + adAccounts.get(i) + "%'");
		                sqlStr.append((i == adAccounts.size() -1 )? "":" or");
		                
                    }
		            sqlStr.append(")");
		        }
		        
		        Object startCreatedDate = mapQuery.get("startCreatedDate");
		        sqlStr.append(startCreatedDate == null ? "" : "AND d.DateCreated >= " + df.format(startCreatedDate) + subfix + " ");
		        Date endCreatedDate = (Date) mapQuery.get("endCreatedDate");
		        sqlStr.append(endCreatedDate == null ? "" : "AND d.DateCreated <= " + df.format(DateUtil.getNextDate(endCreatedDate)) + subfix + " ");

		        String updatedBy = q.getUpdatedBy();
		        if(updatedBy!=null && !"".equals(updatedBy.trim())){
		            List<String> adAccounts = findAdAccountBySearchName(updatedBy);
		            sqlStr.append(" AND (");
                    for (int i = 0; i < adAccounts.size(); i++) {
                        sqlStr.append(" d.LastModifier LIKE '%" + adAccounts.get(i) + "%'");
                        sqlStr.append((i == adAccounts.size() -1 )? "":" or");
                        
                    }
                    sqlStr.append(") ");
		        }
		        Object startUpdatedDate = mapQuery.get("startUpdatedDate");
		        sqlStr.append(startUpdatedDate == null ? "" : "AND d.DateLastModified >= " + df.format(startUpdatedDate) + subfix + " ");
		        Date endUpdatedDate = (Date) mapQuery.get("endUpdatedDate");
		        sqlStr.append(endUpdatedDate == null ? "" : "AND d.DateLastModified <= " + df.format(DateUtil.getNextDate(endUpdatedDate)) + subfix + " ");
		        /*
		         * [MimeType]、[CurrentVersion]、[IsFrozenVersion]、[DateCreated]、[DateLastModified]
		         */

		        // 业务属性
		        String category = q.getCategory();      
		        // 文档类别
		        sqlStr.append(category == null || "".equals(category.trim()) ? "" : "AND d.category LIKE '" + category.trim() + "%' ");
		        
		        String docType = q.getDocType();        
		        // 文档分类
		        sqlStr.append(docType == null || "".equals(docType.trim()) ? "" : "AND (d.docType = '"+docType.trim()+"' OR d.docType LIKE '"+docType.trim()+",%' OR d.docType LIKE '%,"+docType.trim()+"' OR d.docType LIKE '%," + docType.trim() + ",%') ");
		        
		        String taxType = q.getTaxType();       
		        // 税种
		        sqlStr.append(taxType == null || "".equals(taxType.trim()) ? "" : "AND (d.taxType = '"+taxType.trim()+"' OR d.taxType LIKE '"+taxType.trim()+",%' OR d.taxType LIKE '%,"+taxType.trim()+"' OR d.taxType LIKE '%," + taxType.trim() + ",%') ");
		        
		        String region = q.getRegion();          
		        // 地域
		        sqlStr.append(region == null || "".equals(region.trim()) ? "" : "AND d.region = '" + region.trim() + "' ");
		        
		        String industry = q.getIndustry();      
		        //行业
		        sqlStr.append(industry == null || "".equals(industry.trim()) ? "" : "AND d.industry = '" + industry.trim() + "'");
		        
		        String publishOrg = q.getPublishOrg(); 
		        // 发文机关
		        sqlStr.append(publishOrg == null || "".equals(publishOrg.trim()) ? "" : "AND d.publishOrg = '" + publishOrg.trim() + "' ");
		        
		        String publishNo = q.getPublishNo();    
		        // 发文字号
		        sqlStr.append(publishNo == null || "".equals(publishNo.trim()) ? "" : "AND d.publishNo = '" + publishNo.trim() + "' ");
		        
		        String publishYear = q.getPublishYear();   
		        // 发文年度
		        sqlStr.append(publishYear == null || "".equals(publishYear.trim()) ? "" : "AND d.publishYear LIKE '%" + publishYear.trim() + "%' ");
		        
		        String publishSeqNo = q.getPublishSeqNo();   
		        // 发文序号
		        sqlStr.append(publishSeqNo == null || "".equals(publishSeqNo) ? "" : "AND d.publishSeqNo = '" + publishSeqNo + "' ");
		        
		        Object startPublishTime = mapQuery.get("startPublishTime");
		        sqlStr.append(startPublishTime == null ? "" : "AND d.publishTime >= " + df.format(startPublishTime) + subfix + " ");
		        Date endPublishTime = (Date) mapQuery.get("endPublishTime");
		        sqlStr.append(endPublishTime == null ? "" : "AND d.publishTime <= " + df.format(DateUtil.getNextDate(endPublishTime)) + subfix + " ");
		        
		        String belongtoCompany = q.getBelongtoCompany();  
		        // 所属公司
		        sqlStr.append(belongtoCompany == null || "".equals(belongtoCompany.trim()) ? "" : "AND d.[belongtoCompany] LIKE '%" + belongtoCompany.trim() + "%' ");
		        
		        String effectStatus = q.getEffectStatus();          
		        // 有效性
		        sqlStr.append(effectStatus == null || "".equals(effectStatus.trim()) ? "" : "AND d.effectStatus = '" + effectStatus.trim() + "' ");
		        
		        String desc = q.getDesc();      
		        // 简单描述
		        sqlStr.append(desc == null || "".equals(desc.trim()) ? "" : "AND d.[desc] LIKE '%" + desc.trim() + "%' ");
		        
		        String submitCompany = q.getSubmitCompany();   
		        // 提交资料公司
		        sqlStr.append(submitCompany == null || "".equals(submitCompany) ? "" : "AND [submitCompany] LIKE '%" + submitCompany + "%' ");
		        
		        Object startSubmitYear = mapQuery.get("startSubmitYear");
		        sqlStr.append(startSubmitYear == null ? "" : "AND d.submitYear >= " + df.format(startSubmitYear) + subfix + " ");
		        Date endSubmitYear = (Date) mapQuery.get("endSubmitYear");
		        sqlStr.append(endSubmitYear == null ? "" : "AND d.submitYear <= " + df.format(DateUtil.getNextDate(endSubmitYear)) + subfix + " ");
		        
		        String submitStatus = q.getSubmitStatus();     
		        // 资料提交状态
		        sqlStr.append(submitStatus == null || "".equals(submitStatus) ? "" : "AND d.submitStatus = '" + submitStatus + "' ");
		        
		        String auditStatus = q.getAuditStatus();        
		        // 审核状态
		        sqlStr.append(auditStatus == null || "".equals(auditStatus) ? "" : "AND [auditStatus] = '" + auditStatus + "' ");
		        Boolean isFrozen = q.getIsFrozen();             
		        //是否检出
		        sqlStr.append(isFrozen == null ? "" : "AND d.IsReserved = " + isFrozen.toString() + " ");
		        // sort
		        sqlStr.append("ORDER BY d.DateCreated DESC");
		        return ceservice.searchDocuments(sqlStr.toString(), 10);
    }
    /**
     * <p>Description: 将Filenet.Document转为VO对象FnDocument</p>
     * @param doc
     * @return
     */
    public FnDocument conversionDocument(Document doc) {
        FnDocument fnd = new FnDocument();
        // 封装基本属性
        fnd.setCreatedBy(tdsLocal.removePre(doc.get_Creator()));
        fnd.setCreatedDate(doc.get_DateCreated());
        fnd.setUpdatedBy(tdsLocal.removePre(doc.get_LastModifier()));
        fnd.setUpdatedDate(doc.get_DateLastModified());
        fnd.setId(doc.get_Id().toString());
        fnd.setIsFrozen(doc.get_IsReserved());
        fnd.setIsCurrent(doc.get_IsCurrentVersion());
        fnd.setMajorVersion(doc.get_MajorVersionNumber());
        fnd.setMinorVersion(doc.get_MinorVersionNumber());
        fnd.setCurrentState(doc.get_CurrentState());
        fnd.setSize(doc.get_ContentSize());
        fnd.setMimeType(doc.get_MimeType());
        Iterator<Folder> it = doc.get_FoldersFiledIn().iterator();
        fnd.setFoldersFiledIn(it.hasNext() ? it.next().get_PathName() : "");
        
        Properties pros = doc.getProperties();
        fnd.setDocumentTitle(pros.getStringValue("DocumentTitle"));
        
        // 封装业务属性
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("category", pros.getObjectValue("category"));
        map.put("docType", pros.getObjectValue("docType"));
        map.put("taxType", pros.getObjectValue("taxType"));
        map.put("publishOrg", pros.getObjectValue("publishOrg"));
        map.put("publishNo", pros.getObjectValue("publishNo"));
        map.put("publishYear", pros.getObjectValue("publishYear"));
        map.put("publishSeqNo", pros.getObjectValue("publishSeqNo"));
        map.put("publishTime", pros.getObjectValue("publishTime"));
        map.put("belongtoCompany", pros.getObjectValue("belongtoCompany"));
        map.put("effectStatus", pros.getObjectValue("effectStatus"));
        map.put("desc", pros.getObjectValue("desc"));
        map.put("region", pros.getObjectValue("region"));
        map.put("submitCompany", pros.getObjectValue("submitCompany"));
        map.put("submitYear", pros.getObjectValue("submitYear"));
        map.put("submitStatus", pros.getObjectValue("submitStatus"));
        map.put("auditStatus", pros.getObjectValue("auditStatus"));
        map.put("industry", pros.getObjectValue("industry"));
        
        fnd.setMap(map);
        return fnd;
    }
    /**
     * <p>Description: 根据文档id转换成FnDocument POJO</p>
     * @param fileId
     * @return
     * @throws Exception
     */
    public FnDocument getFnDocumentById(String fileId) throws Exception {
        connect();
        return conversionDocument(ceservice.getDocument(new Id(fileId)));
    }
    /**
     * <p>Description: 查找相应文件夹下的文件</p>
     * @param folderPath
     * @return
     */
    public DocumentSet getDocumentsByFolder(String folderPath) throws Exception {
        return getDocuments(new FnDocument(), new HashMap<String, Object>(), folderPath, null);
    }
    /**
     * <p>Description: 查找相应分类下的文件</p>
     * @param cate
     * @return
     * @throws Exception
     */
    public DocumentSet getDocumentsByCate(String cate) throws Exception {
        FnDocument fd = new FnDocument();
        fd.setCategory(cate);
        return getDocuments(fd, new HashMap<String, Object>(), null, null);
    }
    /**
     * <p>Description: 文档检出</p>
     * @param d
     */
    public void checkOutDoc(String id) throws Exception {
    	removePermiss(id, loginService.getCurrentUsermstr().getAdAccount());
    	bindUserRoleToDoc(id, loginService.getCurrentUsermstr().getAdAccount(), true);
        connect();
        Document d = ceservice.getDocument(new Id(id));
        if(!d.getProperties().getStringValue("auditStatus").equals(TIH_DOC_STATUS_3)) {
            // 非审核通过状态，不允许检出
            throw new Exception("文档不是审核通过状态，不允许做检出动作。");
        }
        try {
            d.checkout(ReservationType.EXCLUSIVE, null, null, null);
            d.getProperties().putValue("auditStatus", TIH_DOC_STATUS_4); 
            //改为检出状态
            d.save(RefreshMode.REFRESH);
            
            return;
        } catch(EngineRuntimeException e) {
            logger.error(e.getMessage(), e);
            if(e.getExceptionCode().equals(ExceptionCode.E_READ_ONLY)) {
                throw new Exception("文档为只读，不可以检出");
            } else if(e.getExceptionCode().equals(ExceptionCode.E_RESERVATION_EXISTS)){
                throw new Exception("文档已经检出，不可重复检出");
            } else if(e.getExceptionCode().equals(ExceptionCode.E_NOT_SUPPORTED)) {
                throw new Exception("文档必须为当前版本、没有检出、版本有效，并且你应当有检出权限，方可做检出");
            } else {
                throw new Exception("文档检出失败");
            }
        }
    }

    /**
     * <p>Description: 取消检出</p>
     * @param fd
     * @throws Exception
     */
    public void cancelCheckOutDoc(String id) throws Exception {
        connect();
        Document d = ceservice.getDocument(id);
        d = (Document)d.get_CurrentVersion(); 
		if (d.get_IsReserved().booleanValue()) 
		{ 	
			try {
				d.getProperties().putValue("auditStatus", TIH_DOC_STATUS_3); 
				//改为审核通过状态
				d.save(RefreshMode.REFRESH);
				Document canlDoc = (Document)d.cancelCheckout(); 
				canlDoc.save(RefreshMode.REFRESH); 
			} catch (Exception e) {
				throw new Exception("文档检出出错");
			}
		}else{
			throw new Exception("文档不是检出状态，不允许做取消检出操作");
		}
    }

    /**
     * <p>Description: 删除文档</p>
     * @param fd
     * @throws Exception
     */
    public void deleteDoc(String id) throws Exception {
        fud.deleteDocument(id);
    }
    
    /**
     * <p>Description: 移动文档</p>
     * @param fd
     * @throws Exception
     */
    public void moveDoc(FnDocument fd, String targetPath) throws Exception {
        connect();
        Document doc = ceservice.getDocument(new Id(fd.getId()));
        if(doc.get_IsReserved()) {
            throw new Exception("文档已经被检出，不可移动 。");
        }
        if(doc.getProperties().getStringValue("auditStatus").equals(TIH_DOC_STATUS_5)) {
            throw new Exception("文档已经检入，尚未审核。");
        }
        if(doc.getProperties().getStringValue("auditStatus").equals(TIH_DOC_STATUS_1)) {
            throw new Exception("文档已经上传，尚未审核。");
        }
        
        FolderSet pfs = doc.get_FoldersFiledIn();
        Folder target = ceservice.getFolder(targetPath);
        for(Iterator<Folder> it = pfs.iterator(); it.hasNext(); ) {
            ReferentialContainmentRelationship srel = it.next().unfile(doc);
            srel.save(RefreshMode.REFRESH);
        }
        String fileName = doc.getProperties().getStringValue("DocumentTitle");
        if(isNameExists(fileName, targetPath)) {
            fileName = generateNewDocName(fileName);
            doc.getProperties().putValue("DocumentTitle", fileName);
            doc.save(RefreshMode.REFRESH);
        }
        ReferentialContainmentRelationship rel = 
                target.file(doc, AutoUniqueName.AUTO_UNIQUE, fileName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
        rel.save(RefreshMode.REFRESH);
    }
    
    /**
     * <p>Description: Author By: Yidongjun.</p>
     * @param fd
     * @param targetPath
     * @throws Exception
     */
    public void simpleMoveDoc(FnDocument fd, String targetPath) throws Exception {
        connect();
        Document doc = ceservice.getDocument(new Id(fd.getId()));
        String fileName = doc.getProperties().getStringValue("DocumentTitle");
        if(isNameExists(fileName, targetPath)) {
            fileName = generateNewDocName(fileName);
            doc.getProperties().putValue("DocumentTitle", fileName);
            doc.save(RefreshMode.REFRESH);
        }
        FolderSet pfs = doc.get_FoldersFiledIn();
        Folder target = ceservice.getFolder(targetPath);
        
        ReferentialContainmentRelationship rel = 
                target.file(doc, AutoUniqueName.AUTO_UNIQUE, fileName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
        rel.save(RefreshMode.REFRESH);
        
        for(Iterator<Folder> it = pfs.iterator(); it.hasNext(); ) {
            ReferentialContainmentRelationship srel = it.next().unfile(doc);
            srel.save(RefreshMode.REFRESH);
        }
    }
    /**
     * <p>Description: 复制文档</p>
     * @param fd
     * @throws Exception
     */
    public void copyDoc(String id, String targetPath) throws Exception {
        connect();
        Document d = ceservice.getDocument(new Id(id));
        if(d.get_IsReserved()) {
            throw new Exception("文档已经被检出，不可操作。");
        }
        if(d.getProperties().getStringValue("auditStatus").equals(TIH_DOC_STATUS_5)) {
            throw new Exception("文档已经检入，尚未审核。");
        }
        if(d.getProperties().getStringValue("auditStatus").equals(TIH_DOC_STATUS_1)) {
            throw new Exception("文档已经上传，尚未审核。");
        }
        String on = d.getProperties().getStringValue("DocumentTitle");
        String nn = null;
        if(isNameExists(on, targetPath)) {
            nn = generateNewDocName(on);
        }
        Document newDoc = copyDocument(d.get_Id().toString(), targetPath);
        if(nn != null) {
            newDoc.getProperties().putValue("DocumentTitle", nn);
            newDoc.save(RefreshMode.REFRESH);
        }
    }
    /**
     * <p>Description: 重名文档，修改文档名称</p>
     * @param docName
     * @return
     */
    private String generateNewDocName(String docName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        int i = new Random().nextInt(9999);
        String subfix = "";
        if(i < 10) {
            subfix = "000" + i;
        } else if(i < 100) {
            subfix = "00" + i;
        } else if(i < 1000) {
            subfix = "0" + i;
        } else {
            subfix = "" + i;
        }
        return docName + df.format(new Date()) + subfix;
    }
    /**
     * <p>Description: 判断文件夹内是否存在重名文件</p>
     * @param name
     * @param path
     * @return
     */
    public boolean isNameExists(String name, String path) throws Exception {
        connect();
        Folder f = ceservice.getFolder(path);
        DocumentSet ds = f.get_ContainedDocuments();
        for(Iterator<Document> it = ds.iterator(); it.hasNext();) {
            String n = it.next().get_Name();
            if(name.equals(n)) {
                return true;
            }
        }
        return false;
    }
    /**
     * <p>Description: 修改文档属性</p>
     * @param fd
     * @throws Exception
     */
    public void editDocProperty(FnDocument fd) throws Exception {
        connect();
        Document d = ceservice.getDocument(fd.getId());
        Properties pros = d.getProperties();
        Iterator<Entry<String, Object>> it = fd.getMap().entrySet().iterator();
        Entry<String, Object> entry;
        while(it.hasNext()) {
            entry = it.next();
            pros.putObjectValue(entry.getKey(), entry.getValue());
        }
        d.save(RefreshMode.REFRESH);
    }
    
    public void editDocProperty(FnDocument fd,CompanyManagerModel company) throws Exception {
        connect();
        Document d = ceservice.getDocument(fd.getId());
        Properties pros = d.getProperties();
        Iterator<Entry<String, Object>> it = fd.getMap().entrySet().iterator();
        Entry<String, Object> entry;
        while(it.hasNext()) {
            entry = it.next();
            pros.putObjectValue(entry.getKey(), entry.getValue());
        }
        d.save(RefreshMode.REFRESH);
    }
    
    /**
     * <p>Description: 查询文档版本</p>
     * @param fd
     * @return
     */
    public List<FnDocument> getDocVersions(String id) throws Exception {
        connect();
        Iterator<Versionable> it = ceservice.getDocument(new Id(id)).get_Versions().iterator();
        List<FnDocument> list = new ArrayList<FnDocument>();
        Document d;
        while(it.hasNext()) {
            d = (Document) it.next();
            if(d.get_VersionStatus().getValue() == VersionStatus.RESERVATION_AS_INT) {  
                //检出版本，内容为空，不显示
                continue;
            }
            list.add(conversionDocument(d));
        }
        return list;
    }
    /**
     * <p>Description: 查询文档权限</p>
     * @param fd
     * @return
     */
    public List<PermissionVO> getPermissions(String id) throws Exception {
    	logger.info("perfix:"+perfix);
    	defaultDisplay.clear();
        connect();
        Document d = ceservice.getDocument(new Id(id));
        List<PermissionVO> list = new ArrayList<PermissionVO>();
        long i = 1;
        AccessPermission ap;
        for(Iterator<AccessPermission> it = d.get_Permissions().iterator(); it.hasNext(); i ++) {
            ap = it.next();
            if(ap.get_GranteeType().toString().equals("UNKNOWN")){
            	continue;
            }
            PermissionVO pvo = conversionPermission(ap);
            if(defaultGroup.equals(pvo.getName())){
            	pvo.setId(i);
            	pvo.setValue("所有人");
            	list.add(pvo);
            }
            if("GROUP".equals(pvo.getType()) && !defaultGroup.equals(pvo.getName())){
            	String groupName = findRolemstrByCode(pvo.getName().replace(perfix, ""));
            	pvo.setId(i);
            	pvo.setValue(groupName);
            	if(!"".equals(groupName)){
            		list.add(pvo);
            	}else{
            		defaultDisplay.add(pvo);
            	}
            }else if(!defaultGroup.equals(pvo.getName())){
            	String userName = findPByName2(pvo.getName().replace(perfix, ""));
            	pvo.setId(i);
            	pvo.setValue(userName);
            	if(!"".equals(userName)){
            		list.add(pvo);
            	}else{
            		defaultDisplay.add(pvo);
            	}
            }
        }
        return list;
    }
    public List<PermissionVO> getDefaultList(){
    	return defaultDisplay;
    }
    private PermissionVO conversionPermission(AccessPermission ap) {
        PermissionVO pvo = new PermissionVO();
        String type = ap.get_GranteeType().toString();
        pvo.setType(type);
        String name = getGrantName(ap.get_GranteeName().toString(), type);
        pvo.setName(name);
        
        int mask = ap.get_AccessMask().intValue();
        pvo.setMask(mask);
        if(mask >= allControlPermission) {
            pvo.setFullControl(true);
            pvo.setPublish(true);
            pvo.setPromote(true);
            pvo.setModifyContent(true);
            pvo.setModifyProperty(true);
            pvo.setViewContent(true);
            pvo.setViewProperty(true);
        } else {
            switch(mask) {
                case 131073: 
                	pvo.setViewProperty(true); 
                	break;
                case 131201: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	break;
                case 132499: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	pvo.setModifyProperty(true); 
                	break;
                case 134547: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	pvo.setModifyProperty(true); 
                	pvo.setPublish(true); 
                	break;
                case 132563: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	pvo.setModifyProperty(true); 
                	pvo.setModifyContent(true); 
                	break;
                case 134611: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	pvo.setModifyProperty(true); 
                	pvo.setModifyContent(true); 
                	pvo.setPublish(true); 
                	break;
                case 132567: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	pvo.setModifyProperty(true); 
                	pvo.setModifyContent(true); 
                	pvo.setPromote(true); 
                	break;
                case 200151: 
                	pvo.setViewProperty(true); 
                	pvo.setViewContent(true); 
                	pvo.setModifyProperty(true); 
                	pvo.setModifyContent(true); 
                	pvo.setPromote(true); 
                	pvo.setPublish(true); 
                	break;
                default: break;
            }
        }
        return pvo;
    }
    
    /**
     * <p>Description: 截取用户帐号</p>
     * @param grantName
     * @param type
     * @return
     */
    private String getGrantName(String grantName, String type) {
        String name;
        if(type.equals("USER")) {
            name = grantName.indexOf(uid) == -1 ? grantName : grantName.substring(grantName.indexOf(uid), grantName.indexOf(',')).substring(4);
        } else if(type.equals("GROUP")) {
            name = grantName.indexOf(cn) == -1 ? grantName : grantName.substring(grantName.indexOf(cn), grantName.indexOf(',')).substring(3);
        } else {
            name = grantName;
        }
        return name;
    }
    /**
     * <p>Description: 设置文档权限</p>
     * @param list
     *
     * AccessLevel.getInstanceFromInt(permission.get_AccessMask().intValue())=  
     * 131073.查看属性
     * 131201.查看内容、属性
     * 132499.修改属性；查看内容、属性
     * 132563.修改内容、属性；查看内容、属性
     * 132567.升级版本；修改内容、属性；查看内容、属性
     * 986583.全部控制(fnadmin=998903)
     * 
     * 134547.发布；修改属性；查看内容、属性
     * 134611.发布；修改内容、属性；查看内容、属性
     * 134615.发布；升级版本；修改内容、属性；查看内容、属性
     * 200151.发布；升级版本；修改内容、属性；查看内容、属性
     */
    public void setPermissions(List<PermissionVO> list, String id) throws Exception {
        connect();
        Document d = ceservice.getDocument(new Id(id));
        String documentId = d.get_Id().toString();
        AccessPermissionList apl = d.get_Permissions();
        apl.clear();
        for(PermissionVO p : list) {
            int i = 0;
            if(p.isFullControl()) {
                i = checkInPermission;
            } else if(p.isPromote()) {
            	i = 200151;
            } else if(p.isModifyContent()) {
                if(p.isPublish()) {
                    i = 134611;
                } else {
                    i = 132563;
                }
            } else if(p.isModifyProperty()) {
                if(p.isPublish()) {
                    i = 134547;
                } else {
                    i = 132499;
                }
            } else if(p.isViewContent()) {
                i = 131201;
            } else if(p.isViewProperty()) {
                i = 131073;
            } else {   
                //此条记录不存在权限
                continue;
            }
            apl.add(createPermissionWithPre(p.getName(), i));
        }
        d.set_Permissions(apl);
        d.save(RefreshMode.REFRESH);
        
        // 同时将历史版本权限同步
        for(Iterator<Document> it = d.get_Versions().iterator(); it.hasNext();) {
            Document dv = it.next();
            if(!documentId.equals(dv.get_Id().toString())){
            	dv.set_Permissions(apl);
            	dv.save(RefreshMode.REFRESH);
            }
        }
    }
    /**
     * <p>Description: 权限复制</p>
     * @param source
     * @param target
     * @throws Exception
     */
    public List<PermissionVO> copyPermissionsFromDocToDoc(String source, String target) throws Exception {
        connect();
        Document sd = ceservice.getDocument(new Id(source));
        removeUNKNOWNPermissions(sd);
        if(sd.get_IsReserved().booleanValue()) {
            throw new Exception("文档已经被检出，不可做权限复制。");
        }
        AccessPermissionList sapl = sd.get_Permissions();
        
        Document td = ceservice.getDocument(new Id(target));
        removeUNKNOWNPermissions(td);
        AccessPermissionList tapl = td.get_Permissions();
        
        List<PermissionVO> lpvos = new ArrayList<PermissionVO>();
        
        for(Iterator<AccessPermission> it = tapl.iterator(); it.hasNext();) {
            AccessPermission tap = it.next();
            boolean flag = false;
            for(Iterator<AccessPermission> is = sapl.iterator(); is.hasNext();) {
                AccessPermission sap = is.next();
                if(!sap.get_GranteeType().toString().equals(tap.get_GranteeType().toString())) {
                    continue;
                }
                if (tap.get_GranteeName().toString().equals(sap.get_GranteeName().toString())) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                lpvos.add(conversionPermission(tap));
            }
        }
        return lpvos;
    }
    /**
     * <p>Description: 查询用户或组</p>
     * @param query
     * @return
     */
    public List<UserGroupVO> getUserGroups(Map<String, String> query) throws Exception {
        List<UserGroupVO> list = new ArrayList<UserGroupVO>();
        Realm realm;
        try {
            realm = getRealm(getConn());
        } catch(Exception e) {
            throw new Exception("服务器连接失败");
        }
        long id = 1;
        if(query.get("type").equals("USER")) {
        	List<Persion> pList = findPByNachnOrName2(query.get("name").replace(perfix, "").trim());
        	for(int i = 0;i < pList.size();i++){
        		String userAccount = pList.get(i).getAdAccount();
        		if(!"".equals(perfixApp)){
        			userAccount = perfix + userAccount;
        		}
        		UserSet us = getUsers(realm, userAccount);
        		String name = "";
        		for(Iterator<User> it = us.iterator(); it.hasNext(); id++) {
        			String filenetName = it.next().get_ShortName();
        			if(userAccount.equals(filenetName)){
        				name = filenetName;
        				break;
        			}
        		}
        		if(!"".equals(name)){
        			list.add(new UserGroupVO(Long.valueOf(i), name, "USER",pList.get(i).getName()));
        		}
        	}
        } else if(query.get("type").equals("GROUP")) {
        	List<Rolemstr> gList = findRolemstrByNameOrCode(query.get("name").replace(perfix, "").trim());
        	for(int i = 0;i < gList.size();i++){
        		String groupCode = gList.get(i).getCode();
        		if(!"".equals(perfixApp)){
        			groupCode = perfix + groupCode;
        		}
        		GroupSet gs = getGroups(realm, groupCode);
        		String name = "";
        		for(Iterator<Group> it = gs.iterator(); it.hasNext(); id++) {
        			String filenetName = it.next().get_ShortName();
        			if(groupCode.equals(filenetName)){
        				name = filenetName;
        				break;
        			}
        		}
        		if(!"".equals(name)){
        			list.add(new UserGroupVO(Long.valueOf(i), name, "GROUP", gList.get(i).getName()));
        		}
        		if(gList.size() - 1 == i){
        			list.add(new UserGroupVO(Long.valueOf(gList.size()), defaultGroup, "GROUP","所有人"));
        		}
        	}
        	if(list.size() == 0){
        		if(!"".equals(query.get("name").trim()) && (userCommonService.ifContainIndexOf("所有人", query.get("name").trim()) || userCommonService.ifContainIndexOf(defaultGroup, query.get("name").trim()))){
        			list.add(new UserGroupVO(Long.valueOf(0), defaultGroup, "GROUP","所有人"));
        		}
        	}
        } else {
            throw new Exception("非正常的查询");
        }
        return list;
    }
    private UserSet getUsers(Realm realm, String pattern) {
        UserSet us = realm.findUsers(
                pattern, 
                PrincipalSearchType.PREFIX_MATCH, 
                PrincipalSearchAttribute.SHORT_NAME, 
                PrincipalSearchSortType.NONE, null, null);
        return us;
    }
    private GroupSet getGroups(Realm realm, String pattern) {
        GroupSet gs = realm.findGroups(
                pattern, 
                PrincipalSearchType.PREFIX_MATCH, 
                PrincipalSearchAttribute.SHORT_NAME, 
                PrincipalSearchSortType.NONE, null, null);
        return gs;
    }
    
    @SuppressWarnings("deprecation")
    private Realm getRealm(Connection conn) throws Exception {
        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(0, null, null, PropertyNames.MY_REALM);
        EntireNetwork entireNetwork = Factory.EntireNetwork.fetchInstance(conn, pf);
        return entireNetwork.get_MyRealm();
    }
    private Connection getConn() throws Exception {
        return ServiceFactory.getConnectionManager().getConnection(tdsLocal.addPre(rb.getString(adminPassport)), rb.getString(adminPassword));
    }
    
    public boolean isUserHasPermiss(String level, String id) {
        if(id == null || "".equals(id)){
        	return false;
        }
        List<PermissionVO> pers;
        try {
            pers = getPermissions(id);
        } catch (Exception e) {
            return false;
        }
        String user = loginService.getCurrentUsermstr().getAdAccount();
        for(PermissionVO pvo : pers) {
            if(pvo.getType().equals("GROUP")){
            	continue;
            }
            if(user.equals(tdsLocal.removePre(pvo.getName()))) {
                if(hasPermiss(level, pvo.getMask())){
                	return true;
                }
            }
        }
        // 如果人员没有，再判断组人员
        Connection conn;
        try {
            conn = getConn();
        } catch (Exception e) {
            return false;
        }
        User u = Factory.User.fetchInstance(conn, tdsLocal.addPre(user), null);
        GroupSet userGS = u.get_MemberOfGroups();
        if(userGS.isEmpty() || userGS == null){
        	for(PermissionVO pvo : pers) {
                if(pvo.getType().equals("USER")){
                	continue;
                }
                if(defaultGroup.equals(pvo.getName())) {
                    if(hasPermiss(level, pvo.getMask())){
                    	return true;
                    }
                }
            }
        }else{
        	for(Iterator<Group> it = userGS.iterator(); it.hasNext();) {
        		Group g = it.next();
        		for(PermissionVO pvo : pers) {
        			if(pvo.getType().equals("USER")){
        				continue;
        			}
        			if(defaultGroup.equals(pvo.getName()) || pvo.getName().equals(g.get_ShortName())) {
        				if(hasPermiss(level, pvo.getMask())){
        					return true;
        				}
        			}
        		}
        	}
        }
        return false;
    }
    private boolean hasPermiss(String level, int mask) {
        /**
         * AccessLevel.getInstanceFromInt(permission.get_AccessMask().intValue())=  
         * 131073.查看属性
         * 131201.查看内容、属性
         * 132499.修改属性；查看内容、属性
         * 132563.修改内容、属性；查看内容、属性
         * 132567.升级版本；修改内容、属性；查看内容、属性
         * 986583.全部控制(fnadmin=998903)
         * 
         * 134547.发布；修改属性；查看内容、属性
         * 134611.发布；修改内容、属性；查看内容、属性
         * 134615.发布；升级版本；修改内容、属性；查看内容、属性
         * 200151.发布；升级版本；修改内容、属性；查看内容、属性
         * 
         * LEVEL：queryProperty,queryContent,modifyProperty,modifyContent,publish,promote,all
         */
        if(mask >= 986583){
        	return true;
        }
        switch(mask) {
            case 131073: 
            	return level.equals("queryProperty");
            case 131201: 
            	return level.equals("queryContent") || level.equals("queryProperty");
            case 132499: 
            	return level.equals("modifyProperty") || level.equals("queryContent") || level.equals("queryProperty");
            case 132563: 
            	return level.equals("modifyContent") || level.equals("modifyProperty") || level.equals("queryContent") || level.equals("queryProperty");
            case 134547: 
                return level.equals("publish") || level.equals("modifyProperty") || level.equals("queryContent") || level.equals("queryProperty");
            case 134611: 
            	return level.equals("publish") || level.equals("modifyContent")|| level.equals("modifyProperty") || level.equals("queryContent") || level.equals("queryProperty");
            case 132567: 
            	return level.equals("promote") || level.equals("modifyContent") || level.equals("modifyProperty") || level.equals("queryContent") || level.equals("queryProperty");
            case 200151: 
            	return level.equals("publish") || level.equals("promote") || level.equals("modifyContent") || level.equals("modifyProperty") || level.equals("queryContent") || level.equals("queryProperty");
            default: return false;
        }
    }
    
    /**
     * <p>Description: 文档赋权限 </p>
     * @param fileId
     * @param userName
     */
    public void bindUserRoleToDoc(String fileId, String userName, boolean flag) throws Exception {
    	if(flag){
    		adminConnect();
    	}else{
    		connect();
    	}
        Document doc;
        try {
            doc = ceservice.getDocument(new Id(fileId));
        } catch (Exception e) {
            throw new Exception("文档不存在");
        }
        removeUNKNOWNPermissions(doc);
        
        AccessPermissionList apl = doc.get_Permissions();
        AccessPermission existsUser = getPermissionByUserName(apl, userName);
        if(existsUser != null) {
            return;
        }
        
        AccessPermission ap = createPermissionWithNoPre(userName, checkInPermission);
        
        apl.add(ap);
        doc.set_Permissions(apl);
        doc.save(RefreshMode.REFRESH);
    }
    /**
     * <p>Description: 绑定文档管理员组到文档，赋予所有权限</p>
     * @param fileId
     * @throws Exception
     */
    public void bindAdimnToDoc(String fileId) throws Exception {
        connect();
        bindUserRoleToDoc(fileId, role.getString(documentAdmin),false);
        return;
    }
    
    /**
     * <p>Description: 创建权限</p>
     * @param granteeName
     * @param mask
     * @return
     */
    private AccessPermission createPermissionWithPre(String granteeName, Integer mask) {
        AccessPermission ap = Factory.AccessPermission.createInstance();
        ap.set_AccessType(AccessType.ALLOW);
        ap.set_GranteeName(granteeName);
        ap.set_AccessMask(mask);
        return ap;
    }
    private AccessPermission createPermissionWithNoPre(String granteeName, Integer mask) {
        AccessPermission ap = Factory.AccessPermission.createInstance();
        ap.set_AccessType(AccessType.ALLOW);
        ap.set_GranteeName(granteeName.equals(defaultGroup) ? granteeName : tdsLocal.addPre(granteeName));
        ap.set_AccessMask(mask);
        return ap;
    }
    /**
     * <p>Description: 从权限列表中查找当前用户的权限，并返回</p>
     * @param apl
     * @param userName
     * @return
     */
    private AccessPermission getPermissionByUserName(AccessPermissionList apl, String userName) {
        for(Iterator<AccessPermission> it = apl.iterator(); it.hasNext();) {
            AccessPermission ap = it.next();
            if(! ap.get_GranteeType().toString().equals("USER")){
            	continue;
            }
            String name = getGrantName(ap.get_GranteeName().toString(), ap.get_GranteeType().toString());
            if(tdsLocal.removePre(name).equals(userName)) {
                return ap;
            }
        }
        return null;
    }
    
    public List<String> getPermissionByAllUserName(String fileId) throws Exception{
    	List<String> users = new ArrayList<String>();
    	connect();
    	Document doc;
    	try {
			doc = ceservice.getDocument(new Id(fileId));
		} catch (Exception e) {
			throw new Exception("文档不存在");
		}
    	AccessPermissionList apl = doc.get_Permissions();
        for(Iterator<AccessPermission> it = apl.iterator(); it.hasNext();) {
            AccessPermission ap = it.next();
            if(! ap.get_GranteeType().toString().equals("USER") && ap.get_AccessMask() > 986583){
            	continue;
            }
            String name = getGrantName(ap.get_GranteeName().toString(), ap.get_GranteeType().toString());
            users.add(name);
        }
    	return users;
    }
    
    /**
     * <p>Description: 收回文档权限</p>
     * @param fileId
     * @param userName
     */
    public void removeUserRoleFromDoc(String fileId, String userName) throws Exception {
        connect();
        Document doc;
        try {
            doc = ceservice.getDocument(new Id(fileId));
        } catch (Exception e) {
            throw new Exception("文档不存在");
        }
        removeUNKNOWNPermissions(doc);
        
        AccessPermissionList apl = doc.get_Permissions();
        AccessPermission ap = getPermissionByUserName(apl, userName);
        if(ap != null) {
            apl.remove(ap);
        }
        doc.set_Permissions(apl);
        doc.save(RefreshMode.REFRESH);
    }
    /**
     * <p>Description: 移除未知权限</p>
     * @param d
     */
    private void removeUNKNOWNPermissions(Document d) {
        AccessPermissionList apl = d.get_Permissions();
        List<AccessPermission> aps = new ArrayList<AccessPermission>();
        for(Iterator<AccessPermission> it = apl.iterator(); it.hasNext(); ) {
            AccessPermission ap = it.next();
            if(ap.get_GranteeType().toString().equals("UNKNOWN")) {
                aps.add(ap);
            }
        }
        if(! aps.isEmpty()) {
            for(AccessPermission ap : aps) {
                apl.remove(ap);
            }
            d.save(RefreshMode.REFRESH);
        }
    }
    
    /**
     * <p>Description: 绑定、解绑定用户到权限</p>
     * @param fileId    文档id
     * @param removeUser要移除的人
     * @param bindUser  要绑定的人
     * @throws Exception 
     */
    public void bindAndRemoveRole(String fileId, String removeUser, String bindUser) throws Exception {
        if(removeUser.equals(bindUser)){
        	return;
        }
        try {
            ceservice.connect(tdsLocal.addPre(removeUser), rb.getString(USER_PASSWORD));
        } catch (Exception e) {
            throw new Exception("要解绑定的用户权限不存在。");
        }
        Document doc;
        try {
            doc = ceservice.getDocument(new Id(fileId));
        } catch(Exception e) {
            throw new Exception("文档不存在，请仔细审核");
        }
        removeUNKNOWNPermissions(doc);
        
        // bind
        AccessPermissionList apl = doc.get_Permissions();
        AccessPermission existsUser = getPermissionByUserName(apl, bindUser);
        if(existsUser != null) {
            apl.remove(existsUser);
        }
        AccessPermission ap = createPermissionWithNoPre(bindUser, checkInPermission);
        apl.add(ap);
        doc.set_Permissions(apl);
        doc.save(RefreshMode.REFRESH);
        
        // remove
        ceservice.connect(tdsLocal.addPre(bindUser), rb.getString(USER_PASSWORD));
        Document d = ceservice.getDocument(new Id(fileId));
        AccessPermissionList apls = d.get_Permissions();
        AccessPermission ap1 = getPermissionByUserName(apls, removeUser);
        apls.remove(ap1);
        d.set_Permissions(apls);
        d.save(RefreshMode.REFRESH);
    }
    
    /**
     * 
     * <p>Description: 移除权限</p>  
     * @param fileId 文档ID
     * @param removeUser 移除人
     */
    public void removePermiss(String fileId, String removeUser){
    	try {
			adminConnect();
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
		}
        Document d = null;
		try {
			d = ceservice.getDocument(new Id(fileId));
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
		}
        AccessPermissionList apls = d.get_Permissions();
        AccessPermission ap1 = getPermissionByUserName(apls, removeUser);
        apls.remove(ap1);
        d.set_Permissions(apls);
        d.save(RefreshMode.REFRESH);
    }
    /**
     * <p>Description: 获取组下所有用户名称</p>
     * @param groupName
     * @return
     * @throws Exception
     */
    public List<String> getAllUsersOfGroup(String groupName) throws Exception {
        Group group = Factory.Group.fetchInstance(getConn(), groupName.equals(defaultGroup) ? groupName : tdsLocal.addPre(groupName), null);
        if(group == null){
        	throw new Exception("组不存在，请重新确认");
        }
        List<String> users = new ArrayList<String>();
        for(Iterator<User> it = group.get_Users().iterator(); it.hasNext();) {
            users.add(tdsLocal.removePre(it.next().get_ShortName()));
        }
        return users;
    }

    public void checkinDocument(InputStream is, Map<String, Object> docPros, String filename, 
            String classid, String folderName, String sourceId, String perUserName, String supervisor, WfRemindVo wfRemindVo) throws Exception {
        Document dd= fud.upLoadDocument(is, docPros, filename, classid, folderName);
        modifyDocAudit(sourceId, TIH_DOC_STATUS_5);
        modifyDocAudit(dd.get_Id().toString(), TIH_DOC_STATUS_5);
        checkin.createworkflow(sourceId, dd,perUserName, supervisor, wfRemindVo);
    }
    /**
     * <p>Description: 检入后修改源文档和检入文档的审核状态属性值为检入未审核状态</p>
     * @param sourceId
     * @param checkinId
     */
    private void modifyDocAudit(String sourceId, String auditStatus) throws Exception {
        connect();
        Document sd = ceservice.getDocument(new Id(sourceId));
        sd.getProperties().putValue("auditStatus", auditStatus);
        sd.save(RefreshMode.REFRESH);
    }
    /**
     * <p>Description: 检入文档</p>
     * @param sourceId
     * @param checkinId
     * @throws Exception
     */
    public String checkinDoc(Id sourceId, Id checkinId, String creadeBy) throws Exception {
    	try {
    		ceservice.connect(tdsLocal.addPre(creadeBy), rb.getString(USER_PASSWORD));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("filenet登录不成功");
		}
        Document sourceDoc = Factory.Document.fetchInstance(ceservice.getStore(), sourceId, null);
        if(sourceDoc == null) {
            throw new Exception("源文档不存在，无法检入");
        }
        if(!sourceDoc.getProperties().getStringValue("auditStatus").equals(TIH_DOC_STATUS_5)) {
            throw new Exception("文档不是检入未审核状态，不可做检入操作");
        }
        if(!sourceDoc.get_IsReserved()) {
            throw new Exception("文档不是检出状态，不可做检入操作");
        }
        Properties properties = sourceDoc.getProperties();
        properties.putObjectValue("auditStatus", TIH_DOC_STATUS_3);
        sourceDoc.save(RefreshMode.REFRESH);
        
        
        Document reservationDoc = (Document) sourceDoc.get_Reservation();
        
        Document checkinDoc = Factory.Document.fetchInstance(ceservice.getStore(), checkinId, null);
        if(checkinDoc == null) {
            throw new Exception("检入文档不存在，无法检入");
        }
        // 内容
        ContentElementList contentElementList = Factory.ContentElement.createList();
        ContentTransfer content = Factory.ContentTransfer.createInstance();
        content.setCaptureSource(checkinDoc.accessContentStream(0));
        // 设置RetrievalName
        Iterator<ContentTransfer> rt = checkinDoc.get_ContentElements().iterator();
        String retrievalName = rt.hasNext() ? rt.next().get_RetrievalName() : "";
        content.set_RetrievalName(retrievalName);
        
        contentElementList.add(content);
        reservationDoc.set_ContentElements(contentElementList);
        
        reservationDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        reservationDoc.save(RefreshMode.REFRESH);
        
        // 属性
        Properties propMap = reservationDoc.getProperties();
        Properties sourceProp = checkinDoc.getProperties();
        // 复制属性值
        copyFnDocPros(sourceProp, propMap);
        propMap.putObjectValue("auditStatus",   TIH_DOC_STATUS_3);
        reservationDoc.save(RefreshMode.REFRESH);
        
        checkinDoc.delete();
        checkinDoc.save(RefreshMode.REFRESH);
        
        return reservationDoc.get_Id().toString();
    }
    /**
     * <p>Description: 将source属性值复制到target</p>
     * @param source
     * @param target
     */
    private void copyFnDocPros(Properties source, Properties target) {
        target.putObjectValue("DocumentTitle",  source.getObjectValue("DocumentTitle"));
        
        target.putObjectValue("category",       source.getObjectValue("category"));
        target.putObjectValue("taxType",        source.getObjectValue("taxType"));
        target.putObjectValue("docType",        source.getObjectValue("docType"));
        target.putObjectValue("publishOrg",     source.getObjectValue("publishOrg"));
        target.putObjectValue("publishNo",      source.getObjectValue("publishNo"));
        target.putObjectValue("publishYear",    source.getObjectValue("publishYear"));
        target.putObjectValue("publishSeqNo",   source.getObjectValue("publishSeqNo"));
        target.putObjectValue("publishTime",    source.getObjectValue("publishTime"));
        target.putObjectValue("belongtoCompany",source.getObjectValue("belongtoCompany"));
        target.putObjectValue("effectStatus",   source.getObjectValue("effectStatus"));
        target.putObjectValue("desc",           source.getObjectValue("desc"));
        target.putObjectValue("region",         source.getObjectValue("region"));
        target.putObjectValue("submitCompany",  source.getObjectValue("submitCompany"));
        target.putObjectValue("submitYear",     source.getObjectValue("submitYear"));
        target.putObjectValue("submitStatus",   source.getObjectValue("submitStatus"));
        target.putObjectValue("auditStatus",    source.getObjectValue("auditStatus"));
        target.putObjectValue("industry",       source.getObjectValue("industry"));
    }

    /**
     * <p>Description: 将老文档删除，新文档属性赋值</p>
     * @param oldId
     * @param newId
     * @throws Exception 
     */
    public void replaceDocument(String oldId, String newId) throws Exception {
        connect();
        Document oldDoc = Factory.Document.fetchInstance(ceservice.getStore(), oldId, null);
        Document newDoc = Factory.Document.fetchInstance(ceservice.getStore(), newId, null);
        copyFnDocPros(oldDoc.getProperties(), newDoc.getProperties());
        
        AccessPermissionList apl = Factory.AccessPermission.createList();
        copyPermissions(oldDoc.get_Permissions(), apl);
        newDoc.set_Permissions(apl);
        newDoc.save(RefreshMode.REFRESH);
        
        oldDoc.delete();
        oldDoc.save(RefreshMode.REFRESH);
    }
    
    public String removePre(String item) {
        TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
        try {
            tdsUtilImpl.connectTDS();
            String proItem = tdsUtilImpl.removePre(item);
            if (null != proItem && !"".equals(proItem)) { 
            	return proItem; 
            }
        } catch (Exception e) {
            return item;
        }
        return item;
    }
    public P getNameByAdAccount(String adAccount) {
    	String jpql="select p from Usermstr u,P p,CasUsrP c where u.adAccount=c.id and c.pernr = p.id and u.adAccount = '" + adAccount + "'";
    	try {
    	    return (P) this.em.createQuery(jpql).getSingleResult();
		} catch (Exception e) {
			return null;
		}
    }
    
    public List<String> findAdAccountBySearchName(String searchName){
        List<String> adAccounts = new ArrayList<String>();
        if(searchName == null || "".equals(searchName)){
        	return adAccounts;
        }
        try {
            StringBuffer jpql = new StringBuffer();
            jpql.append(" select distinct(u.adAccount) from Usermstr u,P p,CasUsrP c");
            jpql.append(" where u.adAccount=c.id and c.pernr = p.id");
            jpql.append(" and u.defunctInd <> 'Y'");
            jpql.append(" and c.defunctInd <> 'Y'");
            jpql.append(" and p.defunctInd <> 'Y'");
            jpql.append(" and p.nachn like '%" + searchName.trim() + "%'");
            adAccounts = (List<String>) em.createQuery(jpql.toString()).getResultList();
            logger.info("adAccounts:"+adAccounts.size());
            if(adAccounts == null || adAccounts.size() == 0){
                adAccounts = new ArrayList<String>();
                adAccounts.add(searchName);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return adAccounts;
    }
    
    public boolean isAdmin(){
        boolean hasRole = false;
        try {
            hasRole = loginService.hasRole(role.getString(documentAdmin));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return hasRole;
    }
    
    /**
     * 
     * <p>Description: 通过组CODE找到Rolemstr</p>  
     * @param code 组CODE
     * @return
     */
    public String findRolemstrByCode(String code){
    	String sql = "select rs.name from Rolemstr rs where rs.code = '" +code+ "' and rs.defunctInd <> 'Y' ";
		List<String> resultList = this.em.createQuery(sql).getResultList();
		if(resultList.size() > 0){
			return resultList.get(0);
		}
    	return "";
    }
    
    /**
     * 
     * <p>Description: 通过adcount找到P</p>  
     * @param name2
     * @return
     */
    public String findPByName2(String name2){
    	String sql = "select p.nachn from Usermstr u,P p,CasUsrP c where u.adAccount=c.id and c.pernr = p.id and u.defunctInd <> 'Y' and c.defunctInd <> 'Y' and p.defunctInd <> 'Y' and u.adAccount = '"+name2+"' ";
    	List<String> resultList = this.em.createQuery(sql).getResultList();
		if(resultList.size() > 0){
			return resultList.get(0);
		}
    	return "";
    }
    
    public List<Persion> findPByNachnOrName2(String searchName){
    	List<Persion> lists = new ArrayList<Persion>();
    	String sql = "select u.adAccount,p.nachn from Usermstr u,P p,CasUsrP c where u.adAccount=c.id and c.pernr = p.id and u.defunctInd <> 'Y' and c.defunctInd <> 'Y' and p.defunctInd <> 'Y' and (p.nachn like '%"+searchName+"%' or u.adAccount like '%"+searchName+"%') ";
    	List resultList = this.em.createQuery(sql).getResultList();
    	logger.info("resultList:"+resultList.size());
    	for(int i=0; i<resultList.size();i++){
    		Persion persion = new Persion();
    		Object[] object = (Object[]) resultList.get(i);
    		persion.setAdAccount(object[0].toString());
    		persion.setName(object[1].toString());
    		lists.add(persion);
    	}
    	return lists;
    }
    
    public List<Rolemstr> findRolemstrByNameOrCode(String searchName){
    	List<Rolemstr> lists = new ArrayList<Rolemstr>();
    	String sql = "select rs from Rolemstr rs where rs.defunctInd <> 'Y' and (rs.name like '%" + searchName + "%' or rs.code like '%" + searchName + "%')";
    	lists = this.em.createQuery(sql).getResultList();
    	return lists;
    }
    
    /**
     * 
     * <p>Description: 通过公司名称和流程类型查找上传人主管</p>  
     * @param companyName 公司名称
     * @param requestform 流程类型
     * @param adAccount 用户账号
     * @return
     */
    public String findSupervisorBy(String companyName, String requestform){
    	String sql = "select wfs.supervisor from O o, Companymstr c, WfSupervisor wfs where c.oid = o.id and c.id = wfs.companymstrId and o.stext = '"+ companyName +"' and wfs.value = '"+requestform+"' and wfs.chargedBy = '"+ loginService.getCurrentUsermstr().getAdAccount() +"' and o.defunctInd <> 'Y' and c.defunctInd <> 'Y' ";
    	List<String> list = this.em.createQuery(sql).getResultList();
    	if(list.size() == 1){
    		return list.get(0);
    	}
    	return "";
    }
    
}