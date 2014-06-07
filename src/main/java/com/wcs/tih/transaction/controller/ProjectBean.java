package com.wcs.tih.transaction.controller;

import java.io.Serializable;
import java.text.ParseException;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.math.RandomUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.Rolemstr;
import com.wcs.tih.model.ProjectAttachment;
import com.wcs.tih.model.ProjectMembermstr;
import com.wcs.tih.model.ProjectMissionmstr;
import com.wcs.tih.model.ProjectProblemmstr;
import com.wcs.tih.model.Projectmstr;
import com.wcs.tih.transaction.controller.vo.AttachmentVO;
import com.wcs.tih.transaction.controller.vo.ProblemVO;
import com.wcs.tih.transaction.controller.vo.ProjectMemberVO;
import com.wcs.tih.transaction.controller.vo.ProjectVO;
import com.wcs.tih.transaction.service.ProjectService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class ProjectBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ManagedProperty(value = "#{currentUser}")
    private CurrentUserBean currentUserBean;
    
    @ManagedProperty(value = "#{userCommonBean}")
    private UserCommonBean userCommonBean;
    
    @EJB private ProjectService projectService;
    // query conditions
    private Map<String, Object> query;
    // query results
    private List<ProjectVO> projectmstrses;
    private String projectIds;
    // operation instance
    private ProjectVO project;
    // add,edit project flag
    private String projectModel = "";
    
    private UsermstrVo[] uservo;
    private String projectUserName;
    private List<String> projectUserNameList = new ArrayList<String>();
    private Map<String,String> projectUserNameMap = new HashMap<String, String>();
    
    // Member. membermstres,membermstr成员变量将同时用于任务责任人、问题解决人选择处
    private LazyDataModel<ProjectMemberVO> membermstres;
    private ProjectMemberVO membermstr;
    private String memberModel = "";
    private ProjectMemberVO[] selectedMembers;
    
    // Task
    private ProjectMissionmstr task;
    private String taskModel = "";

    // Question
    private LazyDataModel<ProblemVO> problems;
    private ProblemVO problem;
    private ProblemVO[] selectedProblem;
    private String problemModel = "";
    
    // 删除标识
    private String deleteModel = "";
    
    private final String projectState = DictConsts.TIH_TAX_PROJECT_STAGE;
    private final String taskState = DictConsts.TIH_TAX_PROJECT_TASK_STATUS;
    private final String questionState = DictConsts.TIH_TAX_PROJECT_TASK_QUESTION_STATUS;
    
    private boolean proadmin = false;
    private final String rolesFileName = "roles";
    private String projectAdmin;
    
    private String memberflag;
    
 // 附件管理
    private AttachmentVO[] selectedAttachments;   // 当前操作的附件
    private LazyDataModel<AttachmentVO> attachments;
    private AttachmentVO attachment;
    private String attachmentModel;
    private final String projectAttachType = DictConsts.TIH_TAX_ATTACH_TYPE_1;   //TIH.TAX.ATTACH.TYPE 1   项目管理-项目
    private final String taskAttachType = DictConsts.TIH_TAX_ATTACH_TYPE_2;      //TIH.TAX.ATTACH.TYPE 2   项目管理-任务
    private final String questionAttachType = DictConsts.TIH_TAX_ATTACH_TYPE_3;  //TIH.TAX.ATTACH.TYPE 3   项目管理-问题

    private String operation;
    
    private String currentOpAccount;
    private String chargedBy;
    
    private List<ProjectMemberVO> ls;
    
    public List<ProjectMemberVO> getLs() {
		return ls;
	}

	public void setLs(List<ProjectMemberVO> ls) {
		this.ls = ls;
	}

	// 语言
    private final String lang = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
    
    @PostConstruct public void initBean() {
        query = new HashMap<String, Object>(8);
        resetQuery();
        String no = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("proNumber");
        query.put("qcode", no);
        searchProjects();
        no = "";
        initAddProject();
        initTask();
        initProblem();
        
        // 取项目管理员
        ResourceBundle rb = ResourceBundle.getBundle(rolesFileName);
        projectAdmin = rb.getString("projectadmins");
        
        proadmin = isUserAdmin();
    }
    
    public boolean isUserAdmin() {
        List<Rolemstr> roles = currentUserBean.getCurrentRoles();
        for(Rolemstr r : roles) {
            if(projectAdmin.equals(r.getCode())) {
                return true;
            }
        }
        return false;
    }
    /**
     * <p>Description: 选择干系人</p>
     */
    public void selectMember() {
        if(membermstr == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "请选择一个干系人", ""));
            return;
        }
        chargedBy = getUsernameByAccount(membermstr.getM().getMember());
        addSuccessParam();
    }

    private void addSuccessParam() {
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
    }
    
    public void initAddProject() {
        project = new ProjectVO(new Projectmstr());
        // 算法code
        project.getP().setCode(getRandomCode());
        project.getP().setDefunctInd("N");
    }
    // 生成Code, "P" + 4位随机数 + 8位日期(yyyyMMdd) + 当前人员当天所建项目数量
    public String getRandomCode() {
        int r = 0;
        while(r < 1000) {
            r = RandomUtils.nextInt(9999);
        }
        long number = projectService.getProjectsNumByDay();
        String currentMemberCreateProjectsNum = String.valueOf(number + 1);
        switch(currentMemberCreateProjectsNum.length()) {
            case 1: 
            	currentMemberCreateProjectsNum = "00" + currentMemberCreateProjectsNum; 
            	break;
            case 2: 
            	currentMemberCreateProjectsNum = "0" + currentMemberCreateProjectsNum; 
            	break;
            // 超过3位不再限制
            default: break;
        }
        return "P" + new SimpleDateFormat("yyyyMMdd").format(new Date())
                + currentMemberCreateProjectsNum + r;
    }
    public void nullAction(){
    }
    // 重置查询条件
    public void resetQuery() {
        query.clear();
    }
    
    public Date DateFormatByPattern(Date date, String pattern){
        Date d = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(pattern !=null && !"".equals(pattern)){
            try {
                SimpleDateFormat df = new SimpleDateFormat(pattern);
                d = simpleDateFormat.parse(df.format(date));
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return d;
    }
    // 查询工程
    public void searchProjects() {
        if(query.get("qstartDate") != null && query.get("qcloseDate") != null) {
            if(((Date) query.get("qstartDate")).after((Date) query.get("qcloseDate"))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "查询项目：", "开始日期不能大于结束日期"));
                return ;
            }
        }
        // projectmstrses
        projectmstrses = projectService.searchProjects(query);
    }
    // 新增、修改工程
    public void modelProject() {
    	searchProjects();
        FacesContext context = FacesContext.getCurrentInstance();
        Projectmstr pro = project.getP();
        if(!(ValidateUtil.validateRequiredAndMax(context, pro.getName(), "项目名称：", 50)
                & ValidateUtil.validateRequired(context, pro.getStatus(), "项目阶段：")
                & (ValidateUtil.validateRegex(context, project.getProgress(), "项目进度：", "^[0-9]+.?[0-9]?$", "请填写1-100之间的整数")
                		&& ValidateUtil.validateRequired(context, project.getProgress(), "项目进度：")
                        && ValidateUtil.validateNumericLessthan(context, project.getProgress(), "项目进度：", 100d))
                & ValidateUtil.validateRequired(context, pro.getStartDate(), "开始日期：")
                & ValidateUtil.validateRequired(context, pro.getCloseDate(), "结束日期：")
                & ValidateUtil.validateTwoDate(context, pro.getStartDate(), pro.getCloseDate(), "项目开始日期不能够大于结束日期")
                & ValidateUtil.validateMaxlength(context, pro.getDesc(), "项目描述：", 200))){
        	return;
        }
        
        pro.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        pro.setUpdatedDatetime(new Date());
        pro.setProgress(Float.valueOf(project.getProgress()) / 100);

        if("add".equals(projectModel)) {
            pro.setCreatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
            pro.setCreatedDatetime(new Date());
            // 注入pmId
            String pmId = currentUserBean == null ? "" : currentUserBean.getCurrentUsermstr().getAdAccount();
            if("".equals(pmId)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "您已超时，请重新登录系统操作", ""));
                return;
            }
            pro.setPmId(pmId);
            projectService.addProject(pro);
            // 同时把项目经理添加为干系人
            ProjectMembermstr m = new ProjectMembermstr();
            m.setId(null);
            m.setProjectmstr(pro);
            m.setMember(currentUserBean.getCurrentUsermstr().getAdAccount());
            m.setRemarks("");
            m.setRole("项目经理");
            m.setDefunctInd("N");
            m.setCreatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
            m.setCreatedDatetime(new Date());
            m.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
            m.setUpdatedDatetime(new Date());
            try {
                projectService.addMember(m);
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增项目：", "添加项目经理出错."));
            }
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "新增项目：", "操作成功."));
        } else if("edit".equals(projectModel)) {
            projectService.editProject(pro, lang);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改项目：", "操作成功."));
        }
        searchProjects();
        addSuccessParam();
        RequestContext.getCurrentInstance().addCallbackParam("pid", pro.getId());
    }
    
    public String getUsernameByAccount(String account) {
        if(account == null || "".equals(account)) {
            return "";
        }
        return projectService.getUsernameByAccount(account);
    }
    
    
    // 查询干系人列
    public void searchMembers() {
        ls = projectService.getMembers(project.getP());
        setMembermstres(new PageModel<ProjectMemberVO>(ls, false));
    }
    public void initMember() {
    	projectUserNameList = new ArrayList<String>();
        projectUserNameMap = new HashMap<String, String>();
    	ProjectMembermstr projectMembermstr = new ProjectMembermstr();
    	projectMembermstr.setRole("干系人");
        membermstr = new ProjectMemberVO(projectMembermstr);
        memberModel = "add";
        setUservo(null);
        setProjectUserName("");
    }
    public void selectedUser() {
    	FacesContext context = FacesContext.getCurrentInstance();
        uservo = userCommonBean.getSelectedUsermstrVos();
        for(UsermstrVo vo : uservo){
        	projectUserNameMap.put(vo.getP().getNachn(),vo.getUsermstr().getAdAccount());
        	projectUserNameList.add(vo.getUsermstr().getAdAccount());
        }
        
        if(ls.size()>0){
        	for(ProjectMemberVO vo : ls){
        		if(projectUserNameList.contains(vo.getM().getMember())){
        			projectUserNameMap.remove(getUsernameByAccount(vo.getM().getMember()));
        			projectUserNameList.remove(vo.getM().getMember());
        			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增干系人：",getUsernameByAccount(vo.getM().getMember())+ "  已经存在，不可重复添加。"));
        		}
        	}
        }
        
    }
    // 新增、修改干系人
    public void modelMember() {
        FacesContext context = FacesContext.getCurrentInstance();
        ProjectMembermstr m = membermstr.getM();
        
        if("add".equals(memberModel)) { 
            // new member
        	if(!(ValidateUtil.validateMaxlength(context, m.getRemarks(), "备　注：", 200)
            		& ValidateUtil.validateRepeatWf(context, projectUserNameList.size(), "干系人：", 0))) {
                return;
            }
        	for(String account : projectUserNameList){
	        	ProjectMembermstr menbermstr = new ProjectMembermstr();
	        	menbermstr.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
	        	menbermstr.setUpdatedDatetime(new Date());
	        	menbermstr.setCreatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
	        	menbermstr.setCreatedDatetime(new Date());
	        	menbermstr.setDefunctInd("N");
	        	menbermstr.setProjectmstr(project.getP());
	        	menbermstr.setRole(m.getRole());
	        	menbermstr.setRemarks("");
	        	menbermstr.setMember(account);
	        	projectService.addMember(menbermstr);
        	}
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "新增干系人：", "操作完成."));
        } else if("edit".equals(memberModel)) {
        	if(!ValidateUtil.validateMaxlength(context, m.getRemarks(), "备　注：", 200)) {
                membermstr.setM(projectService.findMemberById(m.getId()));
                return;
            }
        	m.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        	m.setUpdatedDatetime(new Date());
            projectService.editMember(m);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改干系人：", "操作完成."));
        }
        searchMembers();
        addSuccessParam();
    }
    public void deleteMembers() {
        if(selectedMembers == null || selectedMembers.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "删除干系人：", "没有选中任何要删除的记录，无法删除"));
            return;
        }
        try {
            projectService.deleteMembers(selectedMembers);
        } catch(Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除干系人：", "操作失败，请重试；如果仍然无法成功，请联系系统管理员"));
            return;
        }
        searchMembers();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "删除干系人：", "操作成功."));
    }
    
    /**
     * <p>Description: Task</p>
     */
    public void initTask() {
        task = new ProjectMissionmstr();
        task.setDefunctInd("N");
        task.setCreatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        task.setCreatedDatetime(new Date());
        setTaskModel("add");
    }
    // 保存、编辑、删除任务
    public void modelTask() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if(!(ValidateUtil.validateRequiredAndMax(context, task.getName(), "任务名称：", 50)
                & ValidateUtil.validateRequired(context, task.getStatus(), "任务状态：")
                & ValidateUtil.validateRequired(context, chargedBy, "任务负责人：")
                & ValidateUtil.validateMaxlength(context, task.getDesc(), "任务描述：", 200))){
        	return;
        }
        
        if(task.getStartDate() != null && task.getCloseDate() != null && task.getStartDate().after(task.getCloseDate())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "开始日期不能大于结束日期", ""));
            return;
        }
        task.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        task.setUpdatedDatetime(new Date());
        try{
            if("add".equals(taskModel)) {
                task.setChargedBy(membermstr.getM().getMember());
                task.setProjectmstr(project.getP());
                projectService.addTask(task, lang);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "新增任务：", "操作成功."));
            } else if("edit".equals(taskModel)) {
                task.setChargedBy(currentOpAccount);
                projectService.editTask(task, lang);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改任务：", "操作成功."));
            }
        } catch(Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
            return;
        }
        searchProjects();
        addSuccessParam();
        RequestContext.getCurrentInstance().addCallbackParam("pid", project.getP().getId());
    }
    public void deleteTask() {
        try {
            projectService.deleteTask(task);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
            return;
        }
        searchProjects();
    }
    
    // 问题
    public void searchProblems() {
        problems = new PageModel<ProblemVO>(projectService.searchProblems(task), false);
    }
    public void initProblem() {
        ProjectProblemmstr pb = new ProjectProblemmstr();
        pb.setDefunctInd("N");
        pb.setProjectMissionmstr(task);
        pb.setCreatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        pb.setCreatedDatetime(new Date());
        problem = new ProblemVO(pb);
        problemModel = "add";
    }
    public void modelProblem() {
        ProjectProblemmstr pb = problem.getProblem();
        FacesContext context = FacesContext.getCurrentInstance();
        if(!(ValidateUtil.validateRequired(context, pb.getStatus(), "问题状态：")
                & ValidateUtil.validateRequiredAndMax(context, pb.getDesc(), "问题描述：", 200)
                & ValidateUtil.validateMaxlength(context, pb.getProposal(), "解决方案：", 200)
                & ValidateUtil.validateRequired(context, chargedBy, "解决人员："))){
        	return;
        }
        
        pb.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        pb.setUpdatedDatetime(new Date());
        if("add".equals(problemModel)) {
            pb.setSolvedBy(membermstr.getM().getMember());
            projectService.addProblem(pb, lang);
        } else if("edit".equals(problemModel)) {
            pb.setSolvedBy(currentOpAccount);
            projectService.editProblem(pb, lang);
        }
        searchProblems();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "新增问题：", "操作成功"));
        addSuccessParam();
    }
    public void deleteProblems() {
        if(selectedProblem == null || selectedProblem.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "删除问题：", "您没有选中任何要删除的记录"));
            return;
        }
        try {
            projectService.deleteProblems(selectedProblem);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除问题：", "操作失败，请重新尝试"));
            return;
        }
        searchProblems();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "删除问题：", "操作成功"));
    }
    
    // validate
    public void validProject(FacesContext context, UIComponent component, java.lang.Object value)
            throws ValidatorException {
    }
    
    /** Attachments */
    public void searchProjectAttachments() {
        if(operation.equals("project")) {
            attachments = new PageModel<AttachmentVO>(
                    projectService.searchProjectAttachments(project.getP().getId(), projectAttachType), false);
        } else if(operation.equals("task")) {
            attachments = new PageModel<AttachmentVO>(
                    projectService.searchProjectAttachments(task.getId(), taskAttachType), false);
        } else if(operation.equals("question")) {
            attachments = new PageModel<AttachmentVO>(
                    projectService.searchProjectAttachments(problem.getProblem().getId(), questionAttachType), false);
        }
    }
    public void initProjectAttachment() {
        attachmentModel = "add";
    }
    public boolean hasName(String name) {
        if(operation.equals("project")) {
            return projectService.isAttachNameExist(project.getP().getId(), projectAttachType, name);
        } else if(operation.equals("task")) {
            return projectService.isAttachNameExist(task.getId(), taskAttachType, name);
        } else if(operation.equals("question")) {
            return projectService.isAttachNameExist(problem.getProblem().getId(), questionAttachType, name);
        }
        return false;
    }
    public void attachFileUpload(FileUploadEvent event) {
        UploadedFile upFile = event.getFile();
        if(upFile == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传：", "附件不能为空"));
            return;
        }

        ProjectAttachment pa = new ProjectAttachment();
        pa.setCreatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        pa.setCreatedDatetime(new Date());
        pa.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        pa.setUpdatedDatetime(new Date());
        pa.setDefunctInd("N");
        if(operation.equals("project")) {
            pa.setType(projectAttachType);
            pa.setTypeId(project.getP().getId());
        } else if(operation.equals("task")) {
            pa.setType(taskAttachType);
            pa.setTypeId(task.getId());
        } else if(operation.equals("question")) {
            pa.setType(questionAttachType);
            pa.setTypeId(problem.getProblem().getId());
        }
        
        String fileName = upFile.getFileName();
        if(fileName.length() > 225){
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件名称不能超过225个字符!", ""));
            return;
        }
        if(fileName.lastIndexOf('.') == -1) {
            pa.setName(fileName);
        } else {
            pa.setName(fileName.substring(0, fileName.lastIndexOf('.')));
        }
        if(hasName(pa.getName())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "附件上传：", "名称重复"));
            return;
        }
        try {
            String fileid = projectService.uploadAttachments(upFile.getInputstream(), fileName);
            // 权限
            pa.setFilemstrId(fileid);
            projectService.addAttach(pa);
        } catch (Exception e) {
            // 上传失败
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传：", e.getMessage()));
            return;
        }
        
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "附件上传：", "操作成功"));
        searchProjectAttachments();
    }
    
    public void modelProjectAttachment() {
        ProjectAttachment pa = attachment.getAttachment();
        pa.setUpdatedBy(currentUserBean.getCurrentUsermstr().getAdAccount());
        pa.setUpdatedDatetime(new Date());
        
        if(attachmentModel.equals("edit")) {
            if(pa.getName() == null || "".equals(pa.getName())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件修改：", "名称不能为空"));
                return;
            }
            projectService.editAttach(pa);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "附件修改：", "操作成功"));
        }
        searchProjectAttachments();
    }
    // 下载
    public StreamedContent getFileById(String fileId) {
        try {
            return projectService.downloadAttachment(fileId);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载附件：", e.getMessage()));
            return null;
        }
    }
    /**
     * <p>Description: 删除是否有选中要删除的文件</p>
     */
    public void hasSelectedAtts() {
        if(selectedAttachments != null && selectedAttachments.length != 0) {
            addSuccessParam();
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, "请选择要删除的附件。", ""));
    }
    public void deleteAttachments() {
        // delete ce
        try {
            projectService.deleteAtts(selectedAttachments);
        } catch (Exception e) {
            return;
        }
        searchProjectAttachments();
    }
    public void deleteAttachment() {
        try {
            projectService.deleteAtt(attachment);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
        searchProjectAttachments();
    }
    
    public void searchProjectMissions(ToggleEvent event){
        try {
            if("VISIBLE".equals(event.getVisibility().name())){
                String clientId = event.getComponent().getClientId();
                String index = clientId.substring(clientId.indexOf(':')+1, clientId.lastIndexOf(':'));
                ProjectVO projectVO = projectmstrses.get(Integer.parseInt(index));
                projectService.findProjectMissions(projectVO);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    // attachment getter & setter
    public LazyDataModel<AttachmentVO> getAttachments() {
        return attachments;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public AttachmentVO getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentVO attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentModel() {
        return attachmentModel;
    }

    public void setAttachmentModel(String attachmentModel) {
        this.attachmentModel = attachmentModel;
    }

    public AttachmentVO[] getSelectedAttachments() {
        return selectedAttachments;
    }

    public void setSelectedAttachments(AttachmentVO[] selectedAttachments) {
        this.selectedAttachments = selectedAttachments;
    }

    // Getter and Setter
    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public List<ProjectVO> getProjectmstrses() {
        return projectmstrses;
    }

    public void setProjectmstrses(List<ProjectVO> projectmstrses) {
        this.projectmstrses = projectmstrses;
    }

    public ProjectVO getProject() {
        return project;
    }

    public void setProject(ProjectVO project) {
        this.project = project;
    }

    public String getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(String projectModel) {
        this.projectModel = projectModel;
    }

    public CurrentUserBean getCurrentUserBean() {
        return currentUserBean;
    }

    public void setCurrentUserBean(CurrentUserBean currentUserBean) {
        this.currentUserBean = currentUserBean;
    }

    public String getMemberModel() {
        return memberModel;
    }

    public void setMemberModel(String memberModel) {
        this.memberModel = memberModel;
    }

    public ProjectMissionmstr getTask() {
        return task;
    }

    public void setTask(ProjectMissionmstr task) {
        this.task = task;
    }

    public String getTaskModel() {
        return taskModel;
    }

    public void setTaskModel(String taskModel) {
        this.taskModel = taskModel;
    }

    public String getDeleteModel() {
        return deleteModel;
    }

    public void setDeleteModel(String deleteModel) {
        this.deleteModel = deleteModel;
    }

    public LazyDataModel<ProblemVO> getProblems() {
        return problems;
    }

    public void setProblems(LazyDataModel<ProblemVO> problems) {
        this.problems = problems;
    }

    public ProblemVO getProblem() {
        return problem;
    }

    public void setProblem(ProblemVO problem) {
        this.problem = problem;
    }

    public String getProblemModel() {
        return problemModel;
    }

    public void setProblemModel(String problemModel) {
        this.problemModel = problemModel;
    }

    public ProblemVO[] getSelectedProblem() {
        return selectedProblem;
    }

    public void setSelectedProblem(ProblemVO[] selectedProblem) {
        this.selectedProblem = selectedProblem;
    }

    public String getProjectState() {
        return projectState;
    }

    public String getTaskState() {
        return taskState;
    }

    public String getQuestionState() {
        return questionState;
    }

    public boolean isProadmin() {
        return proadmin;
    }

    public UserCommonBean getUserCommonBean() {
        return userCommonBean;
    }

    public void setUserCommonBean(UserCommonBean userCommonBean) {
        this.userCommonBean = userCommonBean;
    }

    public String getProjectUserName() {
        return projectUserName;
    }

    public void setProjectUserName(String projectUserName) {
        this.projectUserName = projectUserName;
    }

    public LazyDataModel<ProjectMemberVO> getMembermstres() {
        return membermstres;
    }

    public void setMembermstres(LazyDataModel<ProjectMemberVO> membermstres) {
        this.membermstres = membermstres;
    }

    public ProjectMemberVO getMembermstr() {
        return membermstr;
    }

    public void setMembermstr(ProjectMemberVO membermstr) {
        this.membermstr = membermstr;
    }

    public ProjectMemberVO[] getSelectedMembers() {
        return selectedMembers;
    }

    public void setSelectedMembers(ProjectMemberVO[] selectedMembers) {
        this.selectedMembers = selectedMembers;
    }

    public String getMemberflag() {
        return memberflag;
    }

    public void setMemberflag(String memberflag) {
        this.memberflag = memberflag;
    }

    public String getCurrentOpAccount() {
        return currentOpAccount;
    }

    public void setCurrentOpAccount(String currentOpAccount) {
        this.currentOpAccount = currentOpAccount;
    }

    public String getChargedBy() {
        return chargedBy;
    }

    public void setChargedBy(String chargedBy) {
        this.chargedBy = chargedBy;
    }

	public UsermstrVo[] getUservo() {
		return uservo;
	}

	public void setUservo(UsermstrVo[] uservo) {
		this.uservo = uservo;
	}

	public List<String> getProjectUserNameList() {
		return projectUserNameList;
	}

	public void setProjectUserNameList(List<String> projectUserNameList) {
		this.projectUserNameList = projectUserNameList;
	}

	public Map<String,String> getProjectUserNameMap() {
		return projectUserNameMap;
	}

	public void setProjectUserNameMap(Map<String,String> projectUserNameMap) {
		this.projectUserNameMap = projectUserNameMap;
	}

}
