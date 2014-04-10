package com.wcs.tih.document.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.filenet.api.exception.EngineRuntimeException;
import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.CommonBean;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.document.controller.vo.CompanyVO;
import com.wcs.tih.document.controller.vo.UploadWorkflowVo;
import com.wcs.tih.document.service.DocumentAuditService;
import com.wcs.tih.feedback.controller.vo.DictPictureVO;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.ce.util.DownloadIdNotFoundException;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.transaction.controller.helper.TaskRefreshHelper;

import filenet.vw.api.VWException;

@ManagedBean
@ViewScoped
public class DocumentAuditBean {
    private static final String MANAGER = "manager";

    private static final String SUPERVISOR = "supervisor";

    private static final String HELPER = "helper";

    private static final String NOTPASS = "NOTPASS";

    private static final String PASS = "PASS";

    private static final String CREATOR = "creator";

    private static final String AUDITER = "auditer";

    private static final String ASSIGNER = "assigner";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static Map<String, String> statusMap = new HashMap<String, String>();
    private static Map<String, String> stepMap = new HashMap<String, String>();
    private CompanyVO company;
    private String auditOpinion;
    private String curStepName;
    private String status;
    private String helper;
    private String showHelper;
    private String showAssigner;
    private boolean edit;
    private String updateId;
    private List<Dict> cateList = new ArrayList<Dict>();
    private TreeNode cateNode;
    private List<Dict> cateListNoSpace = new ArrayList<Dict>();
    private WfInstancemstr wfInstancemstr;
    private Map<String, String> cateMap = new HashMap<String, String>();
    private FnDocument doc = new FnDocument();
    private UploadWorkflowVo uploadWorkflowVo = new UploadWorkflowVo();
    private WfStepmstr lastStep;
    private List<WfStepmstrProperty> workflowDetail;
    private WfStepmstr firestStep;
    private String lastChangeBy;
    private LazyDataModel<CompanyVO> lazyCompany;
    private String aggigner;
    private List<Dict> docType;
    private List<Dict> industry;
    private List<Dict> region;
    private List<Dict> taxType;
    private List<Dict> publishOrg;
    private List<Dict> publishNo;
    private List<Dict> effectStatus;
    private List<Dict> submitYear;
    
    //多种税收
    private List<String>taxTypeList =new ArrayList<String>();
    //文档分类
    private List<String>docTypeList =new ArrayList<String>();
    
    //图片和文字一起显示在selectOneMenu中
    private List<DictPictureVO> photos;
    private DictPictureVO selectedhoto;
    
    private List<DictPictureVO> photosUrgent;
    private DictPictureVO selectedUrgent;
    
    private Dict selectedPublishNo = new Dict();
    private Dict selectedPublishOrg = new Dict();
    private String lang = "zh_CN";
    
    @EJB
    private DocumentAuditService d;
    @EJB
    private FileNetUploadDownload file;
    @EJB
    private CommonService commonService;
    @EJB
    private TimeoutEmailService timeoutEmailService;
    
    @ManagedProperty(value = "#{currentUser}")
    private CurrentUserBean currentUser;

    @ManagedProperty(value = "#{commonBean}")
    private CommonBean commonBean;
    @ManagedProperty(value = "#{userCommonBean}")
    private UserCommonBean userCommonBean;

    public CompanyVO getCompany() {
        return company;
    }
    public void setCompany(CompanyVO company) {
        this.company = company;
    }
   
    private Map<String, String> companyQuery = new HashMap<String, String>();

	private  WfRemindVo wfRemindVo;
    
    public void setBelongComapny(CompanyManagerModel belongComapny) {
        this.doc.setBelongtoCompany(belongComapny.getStext());
    }
    public void setCompanyNameBySubmit(CompanyManagerModel companyNameBySubmit) {
        this.doc.setSubmitCompany(companyNameBySubmit.getStext());
    }
    
    public boolean isEdit() {
        if (curStepName == null){
        	return false;
        }
        if (this.lastStep == null){
        	return false;
        }
        edit = (curStepName.equals(ASSIGNER) || curStepName.equals(AUDITER) ||curStepName.equals(CREATOR) ) && this.lastStep.getChargedBy().equals(this.currentUser.getCurrentUsermstr().getAdAccount());
        return edit;
    }



    static {
        statusMap.put(PASS, DictConsts.TIH_TAX_APPROACH_8);
        statusMap.put(NOTPASS, DictConsts.TIH_TAX_APPROACH_11);
        statusMap.put(ASSIGNER, DictConsts.TIH_TAX_APPROACH_4);
        statusMap.put("reject", DictConsts.TIH_TAX_APPROACH_7);
        statusMap.put("complete", DictConsts.TIH_TAX_APPROACH_10);
        statusMap.put("editSubmitToAudit", DictConsts.TIH_TAX_APPROACH_12);
        statusMap.put("editSubmitToA", DictConsts.TIH_TAX_APPROACH_12);
        statusMap.put(HELPER, DictConsts.TIH_TAX_APPROACH_3);
        statusMap.put("feedbackA", DictConsts.TIH_TAX_APPROACH_5);
        statusMap.put("feedbackB", DictConsts.TIH_TAX_APPROACH_5);
        statusMap.put("refuse", DictConsts.TIH_TAX_APPROACH_6);

        stepMap.put("LaunchStep", "0");
        stepMap.put("文档转签岗", ASSIGNER);
        stepMap.put("上传人主管岗", SUPERVISOR);
        stepMap.put("文档审核岗", AUDITER);
        stepMap.put("文档管理岗", MANAGER);
        stepMap.put("上传人岗", CREATOR);
        stepMap.put("文档协助岗", HELPER);
    }
    
    public void updateCompanyName() {
        if (updateId.equals(":add_document_form:belongtoCompanyId,") || updateId.equals(":query_document_property_form:pbelongtoCompanyId,")) {
            doc.setBelongtoCompany(company.getO().getStext());
        } else if (updateId.equals(":add_document_form:submitCompanyId,") || updateId.equals(":query_document_property_form:psubmitCompanyId,")) {
            doc.setSubmitCompany(company.getO().getStext());
        }
    }
    
    @PostConstruct
    public void initCreate(){
        lang = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
    	selectedhoto = new DictPictureVO();
        photos = new ArrayList<DictPictureVO>();
        photos.add(new DictPictureVO("一般", "TIH.TAX.WORKFLOWIMPORTANCE.1", "important3.png"));
        photos.add(new DictPictureVO("重要", "TIH.TAX.WORKFLOWIMPORTANCE.2", "important2.png"));
        photos.add(new DictPictureVO("非常重要", "TIH.TAX.WORKFLOWIMPORTANCE.3", "important1.png"));
        
        selectedUrgent = new DictPictureVO();
        photosUrgent = new ArrayList<DictPictureVO>();
        photosUrgent.add(new DictPictureVO("一般", "TIH.TAX.WORKFLOWURGENCY.1", "urgent3.png"));
        photosUrgent.add(new DictPictureVO("紧急", "TIH.TAX.WORKFLOWURGENCY.2", "urgent2.png"));
        photosUrgent.add(new DictPictureVO("非常紧急", "TIH.TAX.WORKFLOWURGENCY.3", "urgent1.png"));
        
    }
    
    public void init() {
    	taxTypeList.clear();
    	docTypeList.clear();
        this.dd=null;
        String tmpStepName="";
        try {
        	String status = this.wfInstancemstr.getStatus();
        	if(!DictConsts.TIH_TAX_WORKFLOWSTATUS_3.equals(status) && !DictConsts.TIH_TAX_WORKFLOWSTATUS_4.equals(status)){
        		tmpStepName = d.getWorkStepNameById(wfInstancemstr.getNo());
        	}
            this.curStepName = this.stepMap.get(tmpStepName);
        } catch (VWException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "连接pe失败，请检查账号和filenet", ""));
            logger.error(e.getMessage(), e);
        }
        this.helper="";
        this.showHelper=null;
        this.aggigner="";
        this.showAssigner=null;
        this.auditOpinion = "";
        cateNode = new DefaultTreeNode(new Dict(), null);
        this.buildCateTree(cateNode);
        buildCateList();
        lazyCompany = new com.wcs.common.controller.helper.PageModel<CompanyVO>(new ArrayList<CompanyVO>(), false);
        try {
            doc = this.d.getDocuments(this.d.getWfInstancemstrPropertyByKeyAndValue(wfInstancemstr.getId(), WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID));
        } catch (Exception e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "没有找到你上传的文档", ""));
            logger.error(e1.getMessage(), e1);
        }
        
        List<WfStepmstr> steps = this.d.getStepmstr(this.getWfInstancemstr().getId());
        this.lastStep = steps.get(steps.size()-1);
        this.firestStep = steps.get(0);
        
        if (this.wfInstancemstr.getType().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_4)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "流程已经被终止", ""));
            return;
        }
        //文档分类
        docType = getDocumentVal(DictConsts.TIH_TAX_DOCTYPE);
        //行业
        industry = getDocumentVal(DictConsts.TIH_TAX_INDUSTRY);
        //地域
        region = getDocumentVal(DictConsts.TIH_TAX_REGION);
        //税种
        taxType = getDocumentVal(DictConsts.TIH_TAX_TYPE);
        //发文机关
        publishOrg = getDocumentVal(DictConsts.TIH_TAX_ORG);
        //发文字号
        publishNo = getDocumentVal(DictConsts.TIH_TAX_NO);
        //有效性
        effectStatus = getDocumentVal(DictConsts.TIH_TAX_DOCSTATUS);
        //资料提交状态
        submitYear = getDocumentVal(DictConsts.TIH_DOC_SUBMITSTATUS);
        //重要程度
        selectedhoto = new DictPictureVO();
        String importance = null;
        if(this.wfInstancemstr.getImportance() != null){
        	importance = this.wfInstancemstr.getImportance();
        }
        selectedhoto.setCode(importance);
        //紧急程度
        selectedUrgent = new DictPictureVO();
        String urgency = null;
        if(this.wfInstancemstr.getImportance() != null){
        	urgency = this.wfInstancemstr.getUrgency();
        }
        selectedUrgent.setCode(urgency);//紧急程度
        
        this.lastChangeBy = this.getUserName(lastStep.getChargedBy());
        if(this.lastChangeBy.indexOf(",")!=-1){
            StringBuilder sb = new StringBuilder();
            String [] str=lastChangeBy.split(",");
            for(int i=0;i<str.length;i++){
                if(str[i]!=null){
                	sb.append(this.getUserName(this.d.remove(str[i]))).append(",");
                }
            }
            lastChangeBy=sb.toString();
        }
		lastChangeBy = tmpStepName + " " + lastChangeBy + " 正在处理";
        this.workflowDetail = this.d.getWorkflowDetail(this.wfInstancemstr.getNo());
        
        wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_1, DictConsts.TIH_TAX_REQUESTFORM_1, wfInstancemstr.getId());
        
    }
    
    private List<Dict> getDocumentVal(String keycat){
    	return commonBean.getDictByCat(keycat);
    }
  
    public boolean isOpinion() {
        if(this.lastStep==null){
        	return false;
        }
        return (this.currentUser.getCurrentUsermstr().getAdAccount().equals(this.lastStep.getChargedBy())||this.lastStep.getChargedBy().indexOf(this.currentUser.getCurrentUsermstr().getAdAccount())!=-1)&&this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_2);
    }

    /**
     * 清空转签人和协助人
     */
    public void showAssignerClean(){
    	showAssigner = null;
    	showHelper = null;
    }
    
   
    public boolean isLastMessage() {
       
        if(this.lastStep==null){
        	return false;
        }
        if(this.lastStep.getWfInstancemstr().getType().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_3)||this.lastStep.getWfInstancemstr().getType().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_4)){
        	return false;
        }
        return (!this.currentUser.getCurrentUsermstr().getAdAccount().equals(this.lastStep.getChargedBy()))&&this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_2)&& this.lastStep.getChargedBy().indexOf(this.currentUser.getCurrentUsermstr().getAdAccount())==-1;
    }
    
    public void lunchWorkflow() {
        if(this.getAuditOpinion()!=null &&!this.getAuditOpinion().equals("")){
        if(this.getAuditOpinion().getBytes().length>999){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "意见请不要超过1000个字符", ""));
            return;
        }
        }
       
        List<UsermstrVo> list = d.getuserList(this.doc.getCategory());
        boolean isremove = true;
        String userName = " ";
        String roleName = "";
        if (curStepName.equals(SUPERVISOR)) {
            if (this.status.equals(PASS)) {
                if (list == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
                userName = list.get(0).getUsermstr().getAdAccount();
            } else if (this.status.equals(NOTPASS)) {
                try {
                    this.d.movNotpass(this.doc);
                    this.doc.setAuditStatus(DictConsts.TIH_DOC_STATUS_2);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动到审核未通过文件夹失败！", ""));
                    logger.error(e.getMessage(), e);
                }
                WfInstancemstr wf = new WfInstancemstr();
                wf.setId(this.wfInstancemstr.getId());
                wf.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.d.editWfInstance(wf);
            }
        }
        if (curStepName.equals(AUDITER)) {
            if (this.status.equals("reject")) {
                roleName = WorkflowConsts.WORK_FLOW_PARAM_CREATOR;
                userName = this.wfInstancemstr.getCreatedBy();
            } else if (this.status.equals(ASSIGNER)) {
                if (this.aggigner == null || "".equals(this.aggigner)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "转签人:", "不能为空"));
                    return;
                }
                userName = this.aggigner;
                roleName = WorkflowConsts.WORK_FLOW_PARAM_ASSIGNER;
            } else if (this.status.equals(HELPER)) {
                if (this.helper == null || "".equals(this.helper)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "协助人:", "不能为空"));
                    return;
                }
                userName = this.helper;
                roleName = WorkflowConsts.WORK_FLOW_PARAM_HELPER;
            } else if (this.status.equals(PASS)) {
                try {
                    userName = this.removeLast(this.d.getAllUsersOfGroup(this.d.workqueueGroup));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作fnadmins租失败，可能没找到该组用户", ""));
                    logger.error(e.getMessage(), e);
                    return;
                }
                roleName = "";
            } else if (this.status.equals(NOTPASS)) {
                try {
                    this.d.movNotpass(this.doc);
                    this.doc.setAuditStatus(DictConsts.TIH_DOC_STATUS_2);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动到审核未通过文件夹失败！", ""));
                    logger.error(e.getMessage(), e);
                    return;
                }
                WfInstancemstr wf = new WfInstancemstr();
                wf.setId(this.wfInstancemstr.getId());
                wf.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.d.editWfInstance(wf);
            }
            
        }

        if (this.curStepName.equals(CREATOR)) {
            if (this.status.equals("editSubmitToA")) {
                userName = this.lastStep.getCreatedBy();
                roleName = WorkflowConsts.WORK_FLOW_PARAM_ASSIGNER;
               
            } else if (this.status.equals("editSubmitToAudit")) {
                if (list == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
                userName = list.get(0).getUsermstr().getAdAccount();
              
            }
        }
        if (this.curStepName.equals(ASSIGNER)) {
            if (this.status.equals("refuse")) {
                if (list == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
                userName = list.get(0).getUsermstr().getAdAccount();
            } else if (this.status.equals(HELPER)) {
                if (this.helper == null || "".equals(this.helper)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "协助人:", "不能为空"));
                    return;
                }
                userName = this.helper;
                roleName = WorkflowConsts.WORK_FLOW_PARAM_HELPER;
            } else if (this.status.equals("reject")) {
                roleName = WorkflowConsts.WORK_FLOW_PARAM_CREATOR;
                userName = this.wfInstancemstr.getCreatedBy();
            } else if (this.status.equals(PASS)) {
                try {
                    userName = this.removeLast(this.d.getAllUsersOfGroup(this.d.workqueueGroup));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作fnadmins租失败，可能没找到该租用户", ""));
                    logger.error(e.getMessage(), e);
                    return;
                }
                roleName = "";
            }else if(this.status.equals(NOTPASS)){
                try {
                    this.d.movNotpass(this.doc);
                    this.doc.setAuditStatus(DictConsts.TIH_DOC_STATUS_2);
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动到审核未通过文件夹失败！", ""));
                    logger.error(e.getMessage(), e);
                    return;
                }
                WfInstancemstr wf = new WfInstancemstr();
                wf.setId(this.wfInstancemstr.getId());
                wf.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.d.editWfInstance(wf);
            }
        }
        if (this.curStepName.equals(HELPER)) {
            if (this.status.equals("feedbackA")) {
                roleName = WorkflowConsts.WORK_FLOW_PARAM_ASSIGNER;
                userName = this.lastStep.getCreatedBy();
            } else if (this.status.equals("feedbackB")) {
                if (list == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
                userName = list.get(0).getUsermstr().getAdAccount();
            }
        }
        if (this.curStepName.equals(MANAGER)) {
            if (this.status.equals("complete")) {
                this.doc.setAuditStatus(DictConsts.TIH_DOC_STATUS_3);
                isremove = false;
                roleName = "";
                try {
                    this.doc.setAuditStatus(DictConsts.TIH_DOC_STATUS_3);
                    this.d.editDocProperty(doc);
                    this.d.movPass(doc);
                    
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "移动文件夹失败", ""));
                    logger.error(e.getMessage(), e);
                    return;
                }
                
                WfInstancemstr wf = new WfInstancemstr();
                wf.setId(this.wfInstancemstr.getId());
                wf.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.d.editWfInstance(wf);
            }
        }

        HashMap<String, Object> hh = new HashMap<String, Object>();
        hh.put("status", this.getStatus());
        
        logger.info(this.getStatus());
        logger.info(this.getStatus());
        if (!roleName.equals("")) {
            hh.put(roleName, new String[] { new StringBuilder().append("uid=").append(userName).append(",").append(d.dnStr).toString() });
        }
        WfStepmstr step = new WfStepmstr();
        step.setWfInstancemstr(wfInstancemstr);
        step.setUpdatedBy(this.currentUser.getCurrentUsermstr().getAdAccount());
        step.setCreatedBy(this.currentUser.getCurrentUsermstr().getAdAccount());
        step.setDefunctInd("N");
        step.setUpdatedDatetime(new Date());
        step.setCreatedDatetime(new Date());
        step.setChargedBy(userName);
        step.setDealMethod(this.statusMap.get(this.status));
        step.setCreatedDatetime(new Date());
        step.setCompletedDatetime(new Date());
        step.setFromStepId(this.lastStep.getId());
        HashMap<String, String> h = new HashMap<String, String>();
        h.put(WorkflowConsts.TIH_AUDITE_OPIONION, this.auditOpinion);
        try {
            this.d.editDocProperty(doc);
        } catch (Exception e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改属性失败", ""));
            logger.error(e1.getMessage(), e1);
        }
        //修改流程流转的文档权限
        try {
            if (this.dd != null) {
                //新文档替换旧文档
                this.d.replaseDoc(this.doc, this.wfInstancemstr.getNo(), dd, userName);
                //给新文档添加权限
                this.d.bindUserRoleToDoc(this.dd.get_Id().toString(), userName);
            } else {
                //给下个节点处理人添加权限
                this.d.editPermissions(userName, wfInstancemstr, doc);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改文档失败", ""));
            logger.error(e.getMessage(), e);
        }
       
        try {
            d.doDispath(hh, step, h, this.wfInstancemstr.getNo());
            this.d.updateWfInstance(this.wfInstancemstr.getNo(), selectedhoto.getCode(), selectedUrgent.getCode());
        } catch (VWException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作PE失败", ""));
            logger.error(e.getMessage(), e);
            return;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "执行流程失败", ""));
            logger.error(e.getMessage(), e);
            return;
        }
        try {
            TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "刷新页面失败"));
            logger.error(e.getMessage(), e);
        }
        if (this.getStatus().equals("complete")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "流程审核完毕，请查询并确认"));
            RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
        RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
    }
    private String printlnErroByList(List<UsermstrVo> list){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<list.size();i++){
            sb.append(list.get(i).getUsermstr().getAdAccount()).append(",");
        }
        return sb.toString();
    }
    private String removeLast(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public UserCommonBean getUserCommonBean() {
        return userCommonBean;
    }

    public void setUserCommonBean(UserCommonBean userCommonBean) {
        this.userCommonBean = userCommonBean;
    }

    public CurrentUserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUserBean currentUser) {
        this.currentUser = currentUser;
    }
    public Map<String, String> getCompanyQuery() {
        return companyQuery;
    }
    public void searchCompany() {
        lazyCompany = new com.wcs.common.controller.helper.PageModel<CompanyVO>(d.getCompanys(companyQuery), false);
    }
    public void clearCompany() {
        companyQuery.clear();
        lazyCompany = new com.wcs.common.controller.helper.PageModel<CompanyVO>(new ArrayList<CompanyVO>(), false);
    }
    public void setCompanyQuery(Map<String, String> companyQuery) {
        this.companyQuery = companyQuery;
    }

    public void setDoc(FnDocument doc) {
        this.doc = doc;
    }


    public void setUploadWorkflowVo(UploadWorkflowVo uploadWorkflowVo) {
        this.uploadWorkflowVo = uploadWorkflowVo;
    }
  
    public LazyDataModel<CompanyVO> getLazyCompany() {
        return lazyCompany;
    }
    public void setLazyCompany(LazyDataModel<CompanyVO> lazyCompany) {
        this.lazyCompany = lazyCompany;
    }
    public String getLastChangeBy() {
        return lastChangeBy;
    }
    public String getUpdateId() {
        return updateId;
    }
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }
    

    public CommonBean getCommonBean() {
        return commonBean;
    }

    public void setCommonBean(CommonBean commonBean) {
        this.commonBean = commonBean;
    }

    public void setWfInstancemstr(WfInstancemstr wfInstancemstr) {
        this.wfInstancemstr = wfInstancemstr;
    }

    public Map<String, String> getCateMap() {
        return cateMap;
    }

    public void setCateMap(Map<String, String> cateMap) {
        this.cateMap = cateMap;
    }

    public FnDocument getDoc() {
        return doc;
    }

    public UploadWorkflowVo getUploadWorkflowVo() {
        return uploadWorkflowVo;
    }

    public WfStepmstr getLastStep() {
        return lastStep;
    }

    public List<WfStepmstrProperty> getWorkflowDetail() {
        return workflowDetail;
    }

    public WfStepmstr getFirestStep() {
        return firestStep;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public String getShowHelper() {
        return showHelper;
    }

    public void setShowHelper(String showHelper) {
        this.showHelper = showHelper;
    }

    public String getShowAssigner() {
        return showAssigner;
    }

    public void setShowAssigner(String showAssigner) {
        this.showAssigner = showAssigner;
    }

    public String getAggigner() {
        return aggigner;
    }

    public String getfirstDetail() {
        if (this.getFirestStep() == null){
        	return "";
        }
        return "提交人 "+this.getUserName(this.getFirestStep().getCreatedBy()) +" "+ commonBean.getValueByDictCatKey(this.getFirestStep().getDealMethod()) +" "+this.formatDateFime(this.getFirestStep().getCreatedDatetime());
    }

    public String formatDateFime(Date time) {
        if (time != null) { 
        	return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time); 
        }
        return "";
    }

    public String getUserName(String str) {
        return d.getUserName(str);
    }

    public void setAggigner(String aggigner) {
        this.aggigner = aggigner;
    }

    public WfInstancemstr getWfInstancemstr() {
        return wfInstancemstr;
    }

    public void buildCateTree(TreeNode node) {
        Dict d = (Dict) node.getData();
        List<Dict> list;
        if(d.getId() == null) { //第一次
            list = commonBean.getDictByCat(DictConsts.TIH_TAX_CAT);
        } else {
            list = commonBean.getDictByCat(d.getCodeCat()+"."+d.getCodeKey());
        }
        if(list.isEmpty()) {    //如果node不存在子结点
            String val = d.getCodeVal();
            cateMap.put(val, d.getCodeCat()+"."+d.getCodeKey());
        }
        for(Dict dict : list) {
            TreeNode n = new DefaultTreeNode(dict, node);
            buildCateTree(n);
        }
    }
  
    public List<Dict> getCateList() {
        return cateList;
    }
    public void setCateList(List<Dict> cateList) {
        this.cateList = cateList;
    }
    public TreeNode getCateNode() {
        return cateNode;
    }
    public void setCateNode(TreeNode cateNode) {
        this.cateNode = cateNode;
    }
    public List<Dict> getCateListNoSpace() {
        return cateListNoSpace;
    }
    public void setCateListNoSpace(List<Dict> cateListNoSpace) {
        this.cateListNoSpace = cateListNoSpace;
    }
    public void buildCateList() {
        List<TreeNode> list = cateNode.getChildren();
        for(TreeNode node : list) {
            Dict d = (Dict) node.getData();
            cateListNoSpace.add(d);
            cateList.add(d);
            createSunsOfCateMap(node, 1);
        }
    }
    public void createSunsOfCateMap(TreeNode node, int i) {
        List<TreeNode> list = node.getChildren();
        for(TreeNode n : list) {
            Dict d = (Dict) n.getData();
            cateListNoSpace.add(d);
            String val = "";
            for(int k = 0; k < i; k++) {
                val += "　5　";
            }
            val += "123|_" + d.getCodeVal();
            Dict dict = new Dict();
            dict.setId(d.getId());
            dict.setCodeVal(val);
            dict.setCodeCat(d.getCodeCat());
            dict.setCodeKey(d.getCodeKey());
            cateList.add(dict);
            createSunsOfCateMap(n, i + 1);
        }
    }

    public boolean isPassButton() {
        if (this.curStepName == null){
        	return false;
        }
        return this.curStepName.equals(SUPERVISOR) || this.curStepName.equals(AUDITER) || this.curStepName.equals(ASSIGNER);
    }

    public boolean isRejectbutton() {
        
        if (this.curStepName == null){
        	return false;
        }
        return this.curStepName.equals(ASSIGNER) || this.curStepName.equals(AUDITER);
    }

    public boolean isAssignerbutton() {
        if (this.curStepName == null){
        	return false;
        }
        return this.curStepName.equals(AUDITER);
    }

    public boolean isHelperbutton() {
        if (this.curStepName == null){
        	return false;
        }
        return this.curStepName.equals(ASSIGNER) || this.curStepName.equals(AUDITER);
    }

    public boolean isNotPassbutton() {
        if (this.curStepName == null){
        	return false;
        }
        return this.curStepName.equals(SUPERVISOR) || this.curStepName.equals(AUDITER)||this.curStepName.equals(ASSIGNER);
    }

    public boolean isRefusebutton() {
        if(this.lastStep==null){
        	return false;
        }
        if (this.curStepName == null){
        	return false;
        }
        
        if(this.curStepName.equals(ASSIGNER)){
            if(this.stepMap.get(this.lastStep.getName()).equals(CREATOR)){
                return false;
            }
            if(this.stepMap.get(this.lastStep.getName()).equals(HELPER)){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isCompletebutton() {
        if (this.curStepName == null){
            return false;
        }
        if (this.lastStep == null){ 
            return false;
        }
        if (currentUser == null){ 
            return false;
        }
        if (this.currentUser.getCurrentUsermstr() == null) {
            return false;
        }

        return this.curStepName.equals(MANAGER) ;
    }

    public boolean isEditSubmitAssignerbutton() {
        if(curStepName==null){
        	return false;
        }
        if (this.lastStep == null ){
        	return  false;
        }
        return this.curStepName.equals(CREATOR) && this.stepMap.get(this.lastStep.getName()).equals(ASSIGNER);
    }

    public boolean isEditSubmitAuditerbutton() {
        if(curStepName==null){
            return false;
        }
        if (this.lastStep == null){
        	return false;
        }
        return this.curStepName.equals(CREATOR) && this.stepMap.get(this.lastStep.getName()).equals(AUDITER);
    }

    public boolean isFeedbackAssignerbutton() {
        if(curStepName==null){
        	return false;
        }
        if (this.lastStep == null){
        	return false;
        }
        return this.curStepName.equals(HELPER) && this.stepMap.get(this.lastStep.getName()).equals(ASSIGNER);
    }

    public boolean isFeedbackAuditerbutton() {
        if(curStepName==null){
        	return false;
        }
        if (this.lastStep == null){
        	return false;
        }
        return this.curStepName.equals(HELPER) && this.stepMap.get(this.lastStep.getName()).equals(AUDITER);
    }

    public void getNeedAssigner() {
        this.aggigner = this.userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
        this.showAssigner = this.userCommonBean.getSelectedUsermstrVo().getP().getNachn();
    }

    public void getNeedhelper() {
        this.helper = this.userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
        this.showHelper = this.userCommonBean.getSelectedUsermstrVo().getP().getNachn();
    }

    public List<String> getTaxTypeList() {
		if(this.doc.getTaxType()!=null){
			String[] tmpTaxType = this.doc.getTaxType().split(",");
			if(tmpTaxType==null){
				taxTypeList.add(this.doc.getTaxType());
			}else{
				for (int i = 0; i < tmpTaxType.length; i++) {
					taxTypeList.add(tmpTaxType[i]);
				}
			}
		}
		return taxTypeList;
	}

	public void setTaxTypeList(List<String> taxTypeList) {
		if(edit){
			String tmpTaxType="";
			for (int i = 0; i < taxTypeList.size(); i++) {
				tmpTaxType=tmpTaxType+taxTypeList.get(i);
				if(i!=(taxTypeList.size()-1)){
					tmpTaxType=tmpTaxType+",";
				}
			}
			this.doc.setTaxType(tmpTaxType);
		}
		this.taxTypeList = taxTypeList;
	}
	
	
	public List<String> getDocTypeList() {
		if(this.doc.getDocType()!=null){
			String[] tmpDocType = this.doc.getDocType().split(",");
			if(tmpDocType==null){
				docTypeList.add(this.doc.getDocType());
			}else{
				for (int i = 0; i < tmpDocType.length; i++) {
					docTypeList.add(tmpDocType[i]);
				}
			}
		}
		return docTypeList;
	}
	public void setDocTypeList(List<String> docTypeList) {
		if(edit){
			String tmpDocType="";
			for (int i = 0; i < docTypeList.size(); i++) {
				tmpDocType=tmpDocType+docTypeList.get(i);
				if(i!=(docTypeList.size()-1)){
					tmpDocType=tmpDocType+",";
				}
			}
			this.doc.setDocType(tmpDocType);
		}
		this.docTypeList = docTypeList;
	}

    /**
     * 
     * <p>
     * Description:如果最后一个节点等于当前人或者最后一个节点indexof(当前登人)！=-1,并且流程是在处理状态
     * </p>
     * 
     * @return
     */
    public boolean isCurrenUser() {
        if (this.lastStep == null){
        	return false;
        }
        return (this.currentUser.getCurrentUsermstr().getAdAccount().equals(this.lastStep.getChargedBy()) || this.lastStep.getChargedBy().indexOf(this.currentUser.getCurrentUsermstr().getAdAccount()) != -1) && (this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_2));
    }

    public boolean isShowAssingerText() {
        if (this.lastStep == null){
        	return false;
        }
        if (this.curStepName == null){
        	return false;
        }
        return this.curStepName.equals(AUDITER);
    }

    public boolean isShowHelperText() {
        if (this.lastStep == null){
        	return false;
        }
        if (this.curStepName == null){
        	return false;
        }
        if (!this.lastStep.getChargedBy().equals(this.currentUser.getCurrentUsermstr().getAdAccount())){
        	return false;
        }
        return this.curStepName.equals(ASSIGNER) || this.curStepName.equals(AUDITER);
    }
    private UploadedFile replaceFile;  
    
    
    public UploadedFile getReplaceFile() {
        return replaceFile;
    }
    public void setReplaceFile(UploadedFile replaceFile) {
        this.replaceFile = replaceFile;
    }
    private ResourceBundle rb = ResourceBundle.getBundle("filenet");
    private Document dd=null;
    public void replaceDoc(FileUploadEvent event){
        try {
            String lastName="";
            if(event.getFile().getFileName().indexOf(".")!=-1){
            lastName=event.getFile().getFileName().substring(event.getFile().getFileName().indexOf("."),event.getFile().getFileName().length());
            }
            dd= this.file.upLoadDocument(event.getFile().getInputstream(), new HashMap(), this.doc.getDocumentTitle()+lastName,  rb.getString("ce.document.classid"), rb.getString("ce.folder.newNotAudited"));
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "IO 异常上传文档失败"));
            logger.error(e.getMessage(), e);
            return;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "未知错误，上传文档失败"));
            logger.error(e.getMessage(), e);
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
    }
    public boolean isCreator(){
        if(this.curStepName==null){
        	return false;
        }
        return curStepName.equals(CREATOR) && this.lastStep.getChargedBy().equals(this.currentUser.getCurrentUsermstr().getAdAccount());
    }
    public DefaultStreamedContent getFile(String id) {
        try {
            return (DefaultStreamedContent) this.file.downloadDocumentEncoding(id,"utf-8", "iso-8859-1");
        } catch (EngineRuntimeException e) {
            logger.error(e.getMessage(), e);
        } catch (DownloadIdNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    public void errorMessage(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "只能选择一个文件", ""));
    }
	public List<DictPictureVO> getPhotos() {
		return photos;
	}
	public void setPhotos(List<DictPictureVO> photos) {
		this.photos = photos;
	}
	public DictPictureVO getSelectedhoto() {
		return selectedhoto;
	}
	public void setSelectedhoto(DictPictureVO selectedhoto) {
		this.selectedhoto = selectedhoto;
	}
	public List<DictPictureVO> getPhotosUrgent() {
		return photosUrgent;
	}
	public void setPhotosUrgent(List<DictPictureVO> photosUrgent) {
		this.photosUrgent = photosUrgent;
	}
	public DictPictureVO getSelectedUrgent() {
		return selectedUrgent;
	}
	public void setSelectedUrgent(DictPictureVO selectedUrgent) {
		this.selectedUrgent = selectedUrgent;
	}
	public List<Dict> getDocType() {
		return docType;
	}
	public void setDocType(List<Dict> docType) {
		this.docType = docType;
	}
	public List<Dict> getIndustry() {
		return industry;
	}
	public void setIndustry(List<Dict> industry) {
		this.industry = industry;
	}
	public List<Dict> getRegion() {
		return region;
	}
	public void setRegion(List<Dict> region) {
		this.region = region;
	}
	public List<Dict> getTaxType() {
		return taxType;
	}
	public void setTaxType(List<Dict> taxType) {
		this.taxType = taxType;
	}
	public List<Dict> getPublishOrg() {
		return publishOrg;
	}
	public void setPublishOrg(List<Dict> publishOrg) {
		this.publishOrg = publishOrg;
	}
	public List<Dict> getPublishNo() {
		return publishNo;
	}
	public void setPublishNo(List<Dict> publishNo) {
		this.publishNo = publishNo;
	}
	public List<Dict> getEffectStatus() {
		return effectStatus;
	}
	public void setEffectStatus(List<Dict> effectStatus) {
		this.effectStatus = effectStatus;
	}
	public List<Dict> getSubmitYear() {
		return submitYear;
	}
	public void setSubmitYear(List<Dict> submitYear) {
		this.submitYear = submitYear;
	}
    public Dict getSelectedPublishNo() {
        selectedPublishNo = new Dict();
        if (doc != null && doc.getPublishNo() != null) {
            selectedPublishNo = commonService.findDictByKey(doc.getPublishNo(), lang);
        }
        return selectedPublishNo;
    }
    
    public void setSelectedPublishNo(Dict selectedPublishNo) {
        this.selectedPublishNo = selectedPublishNo;
        if(this.edit && selectedPublishNo != null){
            selectedPublishNo.setLang(lang);
            selectedPublishNo = commonService.saveDict(selectedPublishNo);
            doc.setPublishNo(selectedPublishNo.getCodeCat() + "." +selectedPublishNo.getCodeKey());
        }
    }
    public Dict getSelectedPublishOrg() {
        selectedPublishOrg = new Dict();
        if (doc != null && doc.getPublishOrg() != null) {
            selectedPublishOrg = commonService.findDictByKey(doc.getPublishOrg(), lang);
        }
        return selectedPublishOrg;
    }
    public void setSelectedPublishOrg(Dict selectedPublishOrg) {
        this.selectedPublishOrg = selectedPublishOrg;
        if(this.edit && selectedPublishOrg != null){
            selectedPublishOrg.setLang(lang);
            selectedPublishOrg = commonService.saveDict(selectedPublishOrg);
            doc.setPublishOrg(selectedPublishOrg.getCodeCat() + "." +selectedPublishOrg.getCodeKey());
        }
    }
    
	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}
	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}
	
    
}