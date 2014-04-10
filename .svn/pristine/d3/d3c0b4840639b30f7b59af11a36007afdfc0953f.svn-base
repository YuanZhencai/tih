package com.wcs.tih.document.controller;

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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.exception.EngineRuntimeException;
import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.CommonBean;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.document.controller.vo.UploadWorkflowVo;
import com.wcs.tih.document.service.DocumentCheckinService;
import com.wcs.tih.feedback.controller.vo.DictPictureVO;
import com.wcs.tih.filenet.ce.util.DownloadIdNotFoundException;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.transaction.controller.helper.TaskRefreshHelper;

import filenet.vw.api.VWException;

@ManagedBean
@ViewScoped
public class DocumentCheckinBean {
    private static final String AUDITER = "auditer";
    private static final String SUPERVISOR = "supervisor";
    private static final String HELPER = "helper";
    private static final String ASSIGNER = "assigner";
    private static final String NOT_PASS = "NOTPASS";
    private static final String PASS = "PASS";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @EJB  
    private DocumentCheckinService documentCheckinService;
    @EJB
    private CommonService commonService;
    @EJB
    private TimeoutEmailService timeoutEmailService;
    
    private static Map<String, String> stepMap = new HashMap<String, String>();
    private static Map<String, String> statusMap = new HashMap<String, String>();
    @ManagedProperty(value = "#{currentUser}")
    private CurrentUserBean currentUser;
    private UploadWorkflowVo uploadWorkflowVo = new UploadWorkflowVo();

    //selectOneMenu中文字和图片一起显示
    private List<DictPictureVO> photos;
    private DictPictureVO selectedhoto;
    
    private List<DictPictureVO> photosUrgent;
    private DictPictureVO selectedUrgent;
    
    public UploadWorkflowVo getUploadWorkflowVo() {
        return uploadWorkflowVo;
    }

    @ManagedProperty(value = "#{userCommonBean}")
    private UserCommonBean userCommonBean;
    @ManagedProperty(value = "#{commonBean}")
    private CommonBean commonBean;
    static {
        statusMap.put(PASS, DictConsts.TIH_TAX_APPROACH_8);
        statusMap.put(NOT_PASS, DictConsts.TIH_TAX_APPROACH_11);
        statusMap.put(ASSIGNER, DictConsts.TIH_TAX_APPROACH_4);
        statusMap.put("reject", DictConsts.TIH_TAX_APPROACH_7);
        statusMap.put("complete", DictConsts.TIH_TAX_APPROACH_10);
        statusMap.put(HELPER, DictConsts.TIH_TAX_APPROACH_3);
        statusMap.put("feedbackToAudit", DictConsts.TIH_TAX_APPROACH_5);
        statusMap.put("feedbackToAssigner", DictConsts.TIH_TAX_APPROACH_5);
        statusMap.put("refuse", DictConsts.TIH_TAX_APPROACH_6);
        statusMap.put("passOrNotPass", DictConsts.TIH_TAX_APPROACH_10);

        stepMap.put("LaunchStep", "0");
        stepMap.put("文档转签岗", ASSIGNER);
        stepMap.put("检入人主管", SUPERVISOR);
        stepMap.put("文档审核岗", AUDITER);
        stepMap.put("文档协助岗", HELPER);
    }
    private static String dnStr = ResourceBundle.getBundle("filenet").getString("tds.users.dn");
    private String showHelper;
    private String showAssigner;
    private String aggigner;
    private WfStepmstr lastStep;
    private List<WfStepmstrProperty> workflowDetail;
    private WfStepmstr firestStep;
    private String status;
    private FnDocument doc = new FnDocument();
    private String helper;
    private WfInstancemstr wfInstancemstr;
    private String lastChangeBy;
    private String auditOpinion;
    private String curStepName;
    //多种税收
    private List<String>taxTypeList =new ArrayList<String>();
    //文档分类
    private List<String>docTypeList =new ArrayList<String>();
    
    private Dict selectedPublishNo = new Dict();
    private Dict selectedPublishOrg = new Dict();
    private String lang = "zh_CN";
    
    
    
    private Map<String, String> cateMap = new HashMap<String, String>();
    private List<Dict> cateList = new ArrayList<Dict>();
    private TreeNode cateNode;
    private List<Dict> cateListNoSpace = new ArrayList<Dict>();
	private WfRemindVo wfRemindVo;
    
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
        this.uploadWorkflowVo = this.documentCheckinService.getUploadWorkflowVo(wfInstancemstr.getId());
        this.auditOpinion = "";
        this.helper="";
        this.showHelper=null;
        this.aggigner="";
        this.showAssigner=null;
        String tmpStepName="";
        cateNode = new DefaultTreeNode(new Dict(), null);
        this.buildCateTree(cateNode);
        buildCateList();
        doc = this.documentCheckinService.getDocuments(this.documentCheckinService.getWfInstancemstrPropertyByKeyAndValue(wfInstancemstr.getId(), WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID));
        this.lastStep = this.documentCheckinService.getLastStep(wfInstancemstr);
        this.firestStep = documentCheckinService.getFirstStep(this.wfInstancemstr.getNo());
        
        //重要程度
        selectedhoto = new DictPictureVO();
        String importance = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1;
        if(this.wfInstancemstr.getImportance() != null){
        	importance = this.wfInstancemstr.getImportance();
        }
        selectedhoto.setCode(importance);
        //紧急程度
        selectedUrgent = new DictPictureVO();
        String urgency = DictConsts.TIH_TAX_WORKFLOWURGENCY_1;
        if(this.wfInstancemstr.getImportance() != null){
        	urgency = this.wfInstancemstr.getUrgency();
        }
        selectedUrgent.setCode(urgency);
        
        if(this.wfInstancemstr.getType().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_4)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "流程已经被终止", ""));
            return ;
        }
        this.workflowDetail = this.documentCheckinService.getWorkflowDetail(this.wfInstancemstr.getNo());
   
        try {
        	String status = this.wfInstancemstr.getStatus();
        	if(!DictConsts.TIH_TAX_WORKFLOWSTATUS_3.equals(status) && !DictConsts.TIH_TAX_WORKFLOWSTATUS_4.equals(status)){
        	    tmpStepName = documentCheckinService.getWorkStepNameById(wfInstancemstr.getNo());
        	}else{
        	    tmpStepName = "";
        	}
            this.curStepName = this.stepMap.get(tmpStepName);
        } catch (VWException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "连接pe失败，请检查账号和filenet", ""));
            logger.error(e.getMessage(), e);
        }
        this.lastChangeBy = tmpStepName + " " + this.documentCheckinService.getUserName(lastStep.getChargedBy()) + " 正在处理";
        
        wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_3, DictConsts.TIH_TAX_REQUESTFORM_3, wfInstancemstr.getId());
        
        
    }
    
    public void AssignerHelper(){
    	showHelper=null;
        showAssigner=null;
    }
  
    public void lunchWorkflow() {
        if(this.getAuditOpinion()!=null &&!this.getAuditOpinion().equals("")){
            if(this.getAuditOpinion().getBytes().length>999){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "意见请不要超过1000个字符", ""));
                return;
            }
            }
        List<UsermstrVo> list = documentCheckinService.getuserList(this.doc.getCategory());
        try {
            logger.info("=====================" + this.documentCheckinService.getWorkStepNameById(this.wfInstancemstr.getNo()));
        } catch (VWException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "filenet连接失败", ""));
            logger.error("", e1);
            return;
        }
        String userName = " ";
        String roleName = "";
        HashMap<String, Object> hh = new HashMap<String, Object>();
        if (curStepName.equals(SUPERVISOR)) {
            if (this.status.equals(PASS)) {
                if (list == null) {
                    logger.info("return!!!!!");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
                userName = list.get(0).getUsermstr().getAdAccount();
            } else if (this.status.equals(NOT_PASS)) {
                this.wfInstancemstr.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.documentCheckinService.editWfInstance(this.wfInstancemstr);
                this.documentCheckinService.checkInNotPass(this.wfInstancemstr,this.doc);
            }
        }
        if (curStepName.equals(AUDITER)) {
            if (this.status.equals(HELPER)) {
                if (this.helper == null || "".equals(this.helper)) {
                    logger.info("in............................................");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "协助人:", "不能为空"));
                    return;
                }
                userName = this.helper;
                roleName = WorkflowConsts.WORK_FLOW_PARAM_HELPER;
            } else if (this.status.equals(ASSIGNER)) {
                if (this.aggigner == null || "".equals(this.aggigner)) {
                    logger.info("in............................................");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "转签人:", "不能为空"));
                    return;
                }
                userName = this.aggigner;
                roleName = WorkflowConsts.WORK_FLOW_PARAM_ASSIGNER;
            } else if (this.status.equals(PASS)) {
                try {
                    this.documentCheckinService.checkInPass(this.wfInstancemstr,this.doc.getCreatedBy());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "捡入失败", ""));
                    return;
                }
            } else if (this.status.equals(NOT_PASS)) {
                this.documentCheckinService.checkInNotPass(this.wfInstancemstr,this.doc);
            }
            if (this.status.equals(PASS) || this.status.equals(NOT_PASS)) {
                status = "passOrNotPass";
                this.wfInstancemstr.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.documentCheckinService.editWfInstance(this.wfInstancemstr);
            }
        }

        if (curStepName.equals(ASSIGNER)) {
            if (this.status.equals(PASS)) {
                try {
                    this.documentCheckinService.checkInPass(this.wfInstancemstr,doc.getCreatedBy());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "捡入失败", ""));
                    return;
                }
            } else if (this.status.equals(NOT_PASS)) {
                this.documentCheckinService.checkInNotPass(this.wfInstancemstr,this.doc);
                this.wfInstancemstr.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.documentCheckinService.editWfInstance(this.wfInstancemstr);
            } else if (this.status.equals("refuse")) {
                if (list == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                userName = list.get(0).getUsermstr().getAdAccount();
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
            } else if (this.status.equals(HELPER)) {
                if (this.helper == null || "".equals(this.helper)) {
                    logger.info("in............................................");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "协助人:", "不能为空"));
                    return;
                }
                userName = this.helper;
                roleName = WorkflowConsts.WORK_FLOW_PARAM_HELPER;
            }
            if (this.status.equals(PASS) || this.status.equals(NOT_PASS)) {
                this.wfInstancemstr.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
                this.documentCheckinService.editWfInstance(this.wfInstancemstr);
                status = "passOrNotPass";
            }
        }
        if (curStepName.equals(HELPER)) {
            if (this.status.equals("feedbackToAssigner")) {
                roleName = WorkflowConsts.WORK_FLOW_PARAM_ASSIGNER;
                userName = this.lastStep.getCreatedBy();
            } else if (this.status.equals("feedbackToAudit")) {
                if (list == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档审核岗没有人员", ""));
                    return;
                }
                if (list.size() > 1) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, new StringBuilder().append("一种文档类别审核岗位只能分配给一个人,你现在分配的岗位为：").append(this.printlnErroByList(list)).toString(), ""));
                    return;
                }
                userName = list.get(0).getUsermstr().getAdAccount();
                roleName = WorkflowConsts.WORK_FLOW_PARAM_AUDITER;
            }
        }
        if (!roleName.equals("")) {
            hh.put(roleName, new String []{new StringBuilder().append("uid=").append("userName").append(",").append(dnStr).toString()});
        }
        
        logger.info("status===" + status);
        logger.info("username===" + userName);
        logger.info("roleName==" + roleName);
        hh.put("status", this.status);
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
        
        int j = 0;
        String name = this.currentUser.getCurrentUsermstr().getAdAccount();
        if(!userName.equals(" ")){
	        try {
	        	logger.info("DocumentCheckinBean.lunchWorkflow()"+"        remover"+name+"  binduser"+userName);
	        	List<WfInstancemstrProperty> wfips = null;
	        	if(this.wfInstancemstr.getWfInstancemstrProperties() != null){
	        		wfips = this.wfInstancemstr.getWfInstancemstrProperties();
	        	}else{
	        		wfips = this.documentCheckinService.getwfips(this.wfInstancemstr.getId());
	        	}
	        	for(WfInstancemstrProperty  wfip: wfips){
	        		if(WorkflowConsts.TIH_DCOUMENT_CHECKIN_PERMISSION.equals(wfip.getName())){
	        			String[] str = wfip.getValue().split(",");
	        			for(int i = 0; i<str.length; i++){
	        			    String authorizer = this.documentCheckinService.getAuthorizer(userName, DictConsts.TIH_TAX_REQUESTFORM_2);
	        				if(!name.equals(str[i]) && !authorizer.equals(str[i])){
	        					j = 1;
	        					break;
	        				}else if (name.equals(str[i]) && !authorizer.equals(str[i])) {
	        					j = 2;
	        					break;
							}else if (!name.equals(str[i]) && authorizer.equals(str[i])) {
								j = 3;
	        					break;
							}
	        			}
	        		}
	        	}
	        	switch (j) {
					case 1:
						this.documentCheckinService.bindAndRemoveRole(this.doc.getId(), name, userName);
						j = 0;
						break;
					case 2:
						this.documentCheckinService.bindUserRoleToDoc(this.doc.getId(), userName);
						j = 0;
						break;
					case 3:
						this.documentCheckinService.removeUserRoleFromDoc(this.doc.getId(), name);
						j = 0;
						break;
					default:
						j = 0;
						break;
				}
	        	
	            this.documentCheckinService.bindAndRemoveRole (this.documentCheckinService.getCheckInId(this.wfInstancemstr),name, userName);//新文档
	            this.documentCheckinService.updateWfInstance(this.wfInstancemstr.getNo(), selectedhoto.getCode(), selectedUrgent.getCode());
	        } catch (Exception e) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "给"+userName+"添加权限失败."));
	            logger.error(e.getMessage(), e);
	            return;
	        }
        }
        
        
        try {
            documentCheckinService.doDispath(hh, step, h, this.wfInstancemstr.getNo());
        }catch (VWException e) {
            logger.error(e.getMessage(), e);
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        
        try {
            TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "刷新页面失败"));
            logger.error(e.getMessage(), e);
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
    public boolean isCurrenUser() {
        if (this.lastStep == null){
        	return false;
        }
        return (this.currentUser.getCurrentUsermstr().getAdAccount().equals(this.lastStep.getChargedBy())&&this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_2));
    }
    public boolean isLastPanel(){
        if (this.lastStep == null){
        	return false;
        }
       return (!this.currentUser.getCurrentUsermstr().getAdAccount().equals(this.lastStep.getChargedBy())&&this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_2));
    }
    public List<WfStepmstrProperty> getWorkflowDetail() {
        return workflowDetail;
    }

    public WfStepmstr getLastStep() {
        return lastStep;
    }

    public void setLastStep(WfStepmstr lastStep) {
        this.lastStep = lastStep;
    }

    public WfStepmstr getFirestStep() {
        return firestStep;
    }

    public void setFirestStep(WfStepmstr firestStep) {
        this.firestStep = firestStep;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserCommonBean getUserCommonBean() {
        return userCommonBean;
    }

    public void setUserCommonBean(UserCommonBean userCommonBean) {
        this.userCommonBean = userCommonBean;
    }

    public FnDocument getDoc() {
        return doc;
    }

    public void setDoc(FnDocument doc) {
        this.doc = doc;
    }
    
    public DefaultStreamedContent getFile() {
        try {
            return (DefaultStreamedContent) this.documentCheckinService.getFile(this.documentCheckinService.getCheckInId(this.wfInstancemstr));
        } catch (EngineRuntimeException e) {
            logger.error(e.getMessage(), e);
        } catch (DownloadIdNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
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

    public void setAggigner(String aggigner) {
        this.aggigner = aggigner;
    }

    public void getNeedhelper() {
        this.helper = this.userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
        this.showHelper = this.userCommonBean.getSelectedUsermstrVo().getP().getNachn();
    }

    public WfInstancemstr getWfInstancemstr() {
        return wfInstancemstr;
    }

    public void setWfInstancemstr(WfInstancemstr wfInstancemstr) {
        this.wfInstancemstr = wfInstancemstr;
    }

    public String getLastChangeBy() {
        return lastChangeBy;
    }

    public void setLastChangeBy(String lastChangeBy) {
        this.lastChangeBy = lastChangeBy;
    }



    public boolean isPassButton() {
        if (this.curStepName == null){
        	return false;
        }
        return (this.curStepName.equals(SUPERVISOR) || this.curStepName.equals(AUDITER) || this.curStepName.equals(ASSIGNER));
    }

    public boolean isNotPassButton() {
        if (this.curStepName == null){
        	return false;
        }
        return isPassButton();
    }

    public boolean isRejectButton() {
        if (this.curStepName == null){
        	return false;
        }
            if(this.curStepName.equals(ASSIGNER)){
                if(this.stepMap.get(this.lastStep.getName()).equals("creator")){
                    return false;
                }
                if(this.stepMap.get(this.lastStep.getName()).equals(HELPER)){
                    return false;
                }
                return true;
            }
        return false;
    }

    public boolean isAssignerButton() {
        if (this.curStepName == null){
        	return false;
        }
        return (this.curStepName.equals(AUDITER));
    }

    public boolean isHelperButton() {
        if (curStepName == null){
        	return false;
        }
        return (this.curStepName.equals(AUDITER) || this.curStepName.equals(ASSIGNER));
    }

    public boolean isFeedbackToAuditButton() {
        if (curStepName == null){
        	return false;
        }
        if (this.lastStep == null){
        	return false;
        }
        return (this.curStepName.equals(HELPER) && (this.stepMap.get(this.lastStep.getName()).equals(AUDITER)));
    }

    public boolean isFeedbackToAssigner() {
        if (curStepName == null){
        	return false;
        }
        if (this.lastStep == null){
        	return false;
        }
        return (this.curStepName.equals(HELPER) && (this.stepMap.get(this.lastStep.getName()).equals(ASSIGNER)));
    }
 
    public String getfirstDetail() {
        if (this.getFirestStep() == null){
        	return "";
        }
        return "提交人 "+this.getUserName(this.getFirestStep().getCreatedBy()) +" "+ commonBean.getValueByDictCatKey(this.getFirestStep().getDealMethod()) +" "+ this.formatDateFime(this.getFirestStep().getCreatedDatetime());
    }

    public String formatDateFime(Date time) {
        if (time != null) { 
        	return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time); 
        }
        return "";
    }

    public String getUserName(String str) {
        return documentCheckinService.getUserName(str);
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public boolean isSupervisor() {
        return this.curStepName.equals(SUPERVISOR);
    }

    public String getCurStepName() {
        return curStepName;
    }

 

    public void getNeedAssigner() {
        this.aggigner = this.userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
        this.showAssigner = this.userCommonBean.getSelectedUsermstrVo().getP().getNachn();
    }

 


    
    public CurrentUserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUserBean currentUser) {
        this.currentUser = currentUser;
    }
   

    public Map<String, String> getCateMap() {
        return cateMap;
    }

    public void setCateMap(Map<String, String> cateMap) {
        this.cateMap = cateMap;
    }

    public CommonBean getCommonBean() {
        return commonBean;
    }

    public void setCommonBean(CommonBean commonBean) {
        this.commonBean = commonBean;
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
		this.docTypeList = docTypeList;
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

    public Dict getSelectedPublishNo() {
        selectedPublishNo = new Dict();
        if (doc != null && doc.getPublishNo() != null) {
            selectedPublishNo = commonService.findDictByKey(doc.getPublishNo(), lang);
        }
        return selectedPublishNo;
    }

    public void setSelectedPublishNo(Dict selectedPublishNo) {
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
        this.selectedPublishOrg = selectedPublishOrg;
    }

	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}

	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}
	
}
