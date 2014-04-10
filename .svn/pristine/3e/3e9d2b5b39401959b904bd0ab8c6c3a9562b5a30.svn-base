package com.wcs.tih.transaction.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.tih.consts.TaskConsts;
import com.wcs.tih.interaction.controller.ApplyQuestionBean;
import com.wcs.tih.interaction.controller.SendReportBean;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.transaction.controller.vo.AuthorizmstrSearchVo;
import com.wcs.tih.transaction.controller.vo.AuthorizmstrVo;
import com.wcs.tih.transaction.controller.vo.EmailInfo;
import com.wcs.tih.transaction.controller.vo.MailConfigVo;
import com.wcs.tih.transaction.controller.vo.TaskSearchVo;
import com.wcs.tih.transaction.controller.vo.TaskTreeNodeVo;
import com.wcs.tih.transaction.controller.vo.WfAuthorizmstrVo;
import com.wcs.tih.transaction.controller.vo.WfInstancemstrVo;
import com.wcs.tih.transaction.service.TaskManagementService;
import com.wcs.tih.util.DateUtil;
import com.wcs.tih.util.ValidateUtil;

import filenet.vw.api.VWException;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 任务管理
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "taskManagementBean")
@ViewScoped
public class TaskManagementBean {
	private static final String INSERT = "insert";

	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private TaskManagementService taskManagementService;
	// 树节点
	private TreeNode taskTreeRoot;
	private TreeNode selectedTaskTreeNode;
	private WfInstancemstrVo selectedWfInstancemstrVo;
	private LazyDataModel<WfInstancemstrVo> lazyWfInstancemstrVoModel;
	private WfInstancemstr wfInstancemstr;
	private TaskSearchVo taskSearchVo;
	private String tableHeader = null;
	// 邮件配置
	private List<MailConfigVo> mailConfigVoList;
	// 我的授权
	private LazyDataModel<WfAuthorizmstrVo> lazyWfAuthorizmstrVoModel;
	private WfAuthorizmstrVo selectedWfAuthorizmstrVo;
	private AuthorizmstrVo authorizmstrVo;
	private AuthorizmstrSearchVo authorizmstrSearchVo;
	private String authorizedTo;
	private String showAuthorizedTo;
	private String authorizedBy;
	private String showAuthorizedBy;
	// 代理公共用户
	@ManagedProperty(value = "#{userCommonBean}")
	private UserCommonBean userCommonBean;
	@ManagedProperty(value = "#{sendReportBean}")
	private SendReportBean sendReportBean;
	@ManagedProperty(value = "#{applyQuestionBean}")
	private ApplyQuestionBean applyQuestionBean;
	
	@EJB
	private LoginService loginService;
	// 角色属性文件
	private static ResourceBundle rolesRb = ResourceBundle.getBundle("roles");
	private boolean taskadminsFlag;// 是否是任务管理员
	private String excuteMethod;
	private String currentAdAccount;
	private boolean showCreateTaskFlag;
	private boolean showMyTaskFlag;
	private boolean showMyAuthorized;
	private boolean showMailConfig;
	private boolean showStopWorkFlowButton;
	private boolean showOverTimeMailConfig;

	private Date authorizeStartDatetime;
	private Date authorizeEndDatetime;

	private List<Long> companys = new ArrayList<Long>();
	private Map<String, String> companyMap = new HashMap<String, String>();
	private List<CompanyManagerModel> companyItems = new ArrayList<CompanyManagerModel>();
	private LazyDataModel<EmailInfo> emailHistory = null;

	private Map<String, Object> filter = new HashMap<String, Object>();
	private String nodeName = null;

	private static final String TASK_NODE_CREATE_TASK = TaskConsts.TASK_NODE_CREATE_TASK;
	private static final String TASK_NODE_MY_AUTHORIZED = TaskConsts.TASK_NODE_MY_AUTHORIZED;
	private static final String TASK_NODE_MAIL_SET = TaskConsts.TASK_NODE_MAIL_SET;
	private static final String TASK_NODE_MAIL_TIMED = TaskConsts.TASK_NODE_MAIL_TIMED;
	private static final String TASK_NODE_MAIL_TIMEOUT = TaskConsts.TASK_NODE_MAIL_TIMEOUT;
	private static final String TASK_NODE_MAIL_INFO = TaskConsts.TASK_NODE_MAIL_INFO;

	/**
	 * <p>
	 * Description: 初始化
	 * </p>
	 */
	@PostConstruct
	public void init() {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		currentAdAccount = taskManagementService.getCurrentUserName();
		excuteMethod = INSERT;
		showCreateTaskFlag = false;
		showMyTaskFlag = true;
		showMyAuthorized = false;
		showMailConfig = false;
		showStopWorkFlowButton = false;
		showOverTimeMailConfig = false;

		taskSearchVo = new TaskSearchVo();
		authorizmstrVo = new AuthorizmstrVo();
		authorizmstrSearchVo = new AuthorizmstrSearchVo();
		TaskTreeNodeVo nodeVo = null;

		// 判断当前用户是不是任务管理员
		String taskadmins = rolesRb.getString("taskadmins");
		Boolean isAdmin = taskManagementService.getCurrentRoles(taskadmins);

		// 设置任务号
		String taskNumber = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("taskNumber");
		String documentType = "Word Document";
		if (!"".equals(taskNumber) && taskNumber != null) {
			showCreateTaskFlag = false;
			showMyTaskFlag = true;
			showMyAuthorized = false;
			showMailConfig = false;
			showStopWorkFlowButton = false;
			WfInstancemstrVo wfVo = taskManagementService.findWfInstancemstrByNo(taskNumber);
			List<WfInstancemstrVo> taskList = new ArrayList<WfInstancemstrVo>();
			if (wfVo != null && wfVo.getWfInstancemstr() != null) {
				taskList.add(wfVo);
				String chargedBy = "";
				WfInstancemstr wf = wfVo.getWfInstancemstr();
				List<WfStepmstr> wfStepmstrs = wf.getWfStepmstrs();
				if (wfStepmstrs != null) {
					chargedBy = wfStepmstrs.get(wfStepmstrs.size() - 1).getChargedBy();
				}
				if (!DictConsts.TIH_TAX_WORKFLOWSTATUS_1.equals(wf.getStatus())
						&& taskManagementService.ifContainIndexOf(chargedBy, loginService.getCurrentUserName())) {
					// 我的待处理任务
					nodeVo = new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_WAIT_DEALWITH_TASK, documentType, 0, "");
					tableHeader = "待处理任务列表";
				} else if (taskManagementService.ifContainIndexOf(chargedBy, loginService.getCurrentUserName())
						&& !DictConsts.TIH_TAX_WORKFLOWSTATUS_1.equals(wf.getStatus())) { // 如果流程创建人和当前用户相同
					// 我的申请
					nodeVo = new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_APPLICATION, documentType, 0, "");
					tableHeader = "申请列表";
				} else if ((!DictConsts.TIH_TAX_WORKFLOWSTATUS_1.equals(wf.getStatus()))) {
					// 我的已处理任务
					nodeVo = new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_HADBEEN_DEALWITH_TASK, documentType, 0, "");
					tableHeader = "已处理任务列表";
				}
			}
			lazyWfInstancemstrVoModel = new PageModel<WfInstancemstrVo>(taskList, false);
		} else {
			nodeVo = new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_WAIT_DEALWITH_TASK, documentType, 0, "");
			List<WfInstancemstrVo> myWaitTaskList = taskManagementService.getMyWaitTask(browserLang.toString(), null);
			lazyWfInstancemstrVoModel = new PageModel<WfInstancemstrVo>(myWaitTaskList, false);
		}
		taskadminsFlag = isAdmin;
		taskTreeRoot = new DefaultTreeNode("root", null);
		selectedTaskTreeNode = new DefaultTreeNode(nodeVo, taskTreeRoot);
		taskTreeRoot = taskManagementService.createTree(browserLang.toString(), taskadminsFlag);
		selectNodeBySelectedNode(taskTreeRoot, selectedTaskTreeNode);
	}

	public void selectNodeBySelectedNode(TreeNode treeRoot, TreeNode selectedNode) {
		try {
			TaskTreeNodeVo selectedNodeVo = (TaskTreeNodeVo) selectedNode.getData();
			List<TreeNode> children = treeRoot.getChildren();
			for (TreeNode child : children) {
				TaskTreeNodeVo childNodeVo = (TaskTreeNodeVo) child.getData();
				if (childNodeVo.getTaskNodeName().equals(selectedNodeVo.getTaskNodeName())) {
					child.setSelected(true);
					selectedNode = child;
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 初始化，各流程执行完成后调用该方法将重新生成相应的菜单和主页数据
	 * </p>
	 */
	public void initTask() {
		if (selectedTaskTreeNode != null) {
			TaskTreeNodeVo ttnv = (TaskTreeNodeVo) (selectedTaskTreeNode.getData());
			Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			// 生成树
			taskTreeRoot = taskManagementService.createTree(browserLang.toString(), taskadminsFlag);
			if (taskTreeRoot != null) {
				selectNodeBySelectedNode(taskTreeRoot, selectedTaskTreeNode);
			}
			// 生成数据和选中菜单
			getMyTask(ttnv.getTaskNodeName(), ttnv.getRequestFormType());
		}
	}

	/**
	 * <p>
	 * Description: 查询任务
	 * </p>
	 */
	public void queryTask() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (taskSearchVo.getStartSubmitDatetime() != null && taskSearchVo.getEndSubmitDatetime() != null) {
			if (taskSearchVo.getStartSubmitDatetime().getTime() > taskSearchVo.getEndSubmitDatetime().getTime()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "提交时间选择不正确", "前一个时间不能大于后一个时间，请重新选择！"));
				return;
			}
		}
		if (selectedTaskTreeNode != null) {
			TaskTreeNodeVo ttnv = (TaskTreeNodeVo) (selectedTaskTreeNode.getData());
			setQueryTsakData(ttnv.getTaskNodeName());
		} else {
			setQueryTsakData(TaskConsts.TASK_NODE_MY_WAIT_DEALWITH_TASK);
		}
	}

	/**
	 * <p>
	 * Description: 设置查询任务数据
	 * </p>
	 * 
	 * @param taskNodeName
	 *            选中的任务树节点
	 */
	public void setQueryTsakData(String taskNodeName) {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		List<WfInstancemstrVo> wfInstancemstrVos = null;
		if (TaskConsts.TASK_NODE_MY_DRAFT.equals(taskNodeName)) {
			wfInstancemstrVos = taskManagementService.getMyDraft(browserLang.toString(), taskSearchVo);
			tableHeader = "草稿列表";
		} else if (TaskConsts.TASK_NODE_MY_APPLICATION.equals(taskNodeName)) {
			wfInstancemstrVos = taskManagementService.getMyApplication(browserLang.toString(), taskSearchVo);
			tableHeader = "申请列表";
		} else if (TaskConsts.TASK_NODE_MY_WAIT_DEALWITH_TASK.equals(taskNodeName)) {
			wfInstancemstrVos = taskManagementService.getMyWaitTask(browserLang.toString(), taskSearchVo);
			tableHeader = "待处理任务列表";
		} else if (TaskConsts.TASK_NODE_MY_HADBEEN_DEALWITH_TASK.equals(taskNodeName)) {
			wfInstancemstrVos = taskManagementService.getMyHandledTask(browserLang.toString(), taskSearchVo);
			tableHeader = "已处理任务列表";
		} else if (TaskConsts.TASK_NODE_ALL_TASK.equals(taskNodeName)) {
			wfInstancemstrVos = taskManagementService.getAllTask(browserLang.toString(), taskSearchVo);
			tableHeader = "任务列表";
		} else {
			wfInstancemstrVos = null;
			lazyWfInstancemstrVoModel = null;
		}
		if (null != wfInstancemstrVos && wfInstancemstrVos.size() != 0) {
			lazyWfInstancemstrVoModel = new PageModel<WfInstancemstrVo>(wfInstancemstrVos, false);
		} else {
			lazyWfInstancemstrVoModel = null;
		}
	}

	/**
	 * <p>
	 * Description: 取得我的任务信息
	 * </p>
	 * 
	 * @param taskNodeName
	 *            选中的任务树节点
	 * @param requestFormType
	 *            申请单类型
	 */
	public void getMyTask(String taskNodeName, String requestFormType) {
		nodeName = taskNodeName;
		resetTaskSearchVo();
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (TaskConsts.TASK_NODE_CREATE_TASK.equals(taskNodeName)) {
			// 创建任务
			showMyTaskFlag = false;
			showStopWorkFlowButton = false;
		} else if (TaskConsts.TASK_NODE_MY_AUTHORIZED.equals(taskNodeName)) {
			showMyTaskFlag = false;
			showStopWorkFlowButton = false;
			authorizmstrSearchVo = new AuthorizmstrSearchVo();
			queryWarrant();
		} else if (TaskConsts.TASK_NODE_MAIL_SET.equals(taskNodeName) || TaskConsts.TASK_NODE_MAIL_TIMED.equals(taskNodeName)) {
			showMyTaskFlag = false;
			showStopWorkFlowButton = false;
			// 初始化邮件设置信息
			taskManagementService.initMailConfig(browserLang.toString());
			mailConfigVoList = taskManagementService.getAllWfMailConfig();
		} else if (TaskConsts.TASK_NODE_MAIL_TIMEOUT.equals(taskNodeName)) {
			// 超时邮件
			showMyTaskFlag = false;
			showStopWorkFlowButton = false;
			// 初始化超时邮件邮件设置信息

		} else if (TaskConsts.TASK_NODE_MAIL_INFO.equals(taskNodeName)) {
			// 超时邮件
			// 初始化超时邮件邮件设置信息
			showMyTaskFlag = false;
			showStopWorkFlowButton = false;

		} else {
			if (requestFormType != null && !"".equals(requestFormType)) {
				nodeName = TaskConsts.TASK_NODE_CREATE_TASK;
				showMyTaskFlag = false;
				showStopWorkFlowButton = false;
				String showRequestFormDialog = "";
				if (DictConsts.TIH_TAX_REQUESTFORM_3.equals(requestFormType)) {
					showRequestFormDialog = TaskConsts.SHOW_TIH_TAX_REQUESTFORM_3;
					applyQuestionBean.initAddApplyQuestion();
				} else if (DictConsts.TIH_TAX_REQUESTFORM_4.equals(requestFormType)) {
					showRequestFormDialog = TaskConsts.SHOW_TIH_TAX_REQUESTFORM_4;
					sendReportBean.initAddReport();
				} else if (DictConsts.TIH_TAX_REQUESTFORM_5.equals(requestFormType)) {
					showRequestFormDialog = TaskConsts.SHOW_TIH_TAX_REQUESTFORM_5;
				} else {
					showRequestFormDialog = "";
				}
				RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
				RequestContext.getCurrentInstance().addCallbackParam("showRequestFormDialog", showRequestFormDialog);
				RequestContext.getCurrentInstance().addCallbackParam("authorized", "no");
			} else {
				showMyTaskFlag = true;
				taskSearchVo = new TaskSearchVo();
				setQueryTsakData(taskNodeName);
				if (TaskConsts.TASK_NODE_ALL_TASK.equals(taskNodeName)) {
					showStopWorkFlowButton = true;
				} else {
					showStopWorkFlowButton = false;
				}
				RequestContext.getCurrentInstance().addCallbackParam("issucc", "no");
			}
		}
	}

	/**
	 * <p>
	 * Description: 终止流程
	 * </p>
	 */
	public void stopWorkFlow() {
		FacesContext context = FacesContext.getCurrentInstance();
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		try {
			taskManagementService.stopWorkFlow(wfInstancemstr);
			List<WfInstancemstrVo> wfInstancemstrVos = taskManagementService.getAllTask(browserLang.toString(), taskSearchVo);
			lazyWfInstancemstrVoModel = new PageModel<WfInstancemstrVo>(wfInstancemstrVos, false);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "终止流程单号'" + wfInstancemstr.getNo() + "'的流程成功，请查看！"));
			this.initTask();
		} catch (VWException e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "FileNet出现异常，终止流程单号'" + wfInstancemstr.getNo() + "'的流程失败！"));
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 保存邮件配置信息
	 * </p>
	 */
	public void saveMailConfig() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean b = taskManagementService.updateWfMailConfig(mailConfigVoList);
		if (b) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "邮件配置成功，请查看！"));
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "邮件配置失败！"));
		}
	}

	/**
	 * <p>
	 * Description: 取得选中的被授权人
	 * </p>
	 */
	public void getNeedAuthorizedTo() {
		authorizedTo = userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
		showAuthorizedTo = userCommonBean.getSelectedUsermstrVo().getP().getNachn();
	}

	/**
	 * <p>
	 * Description: 创建授权
	 * </p>
	 */
	public void createWarrant() {
		authorizmstrVo = new AuthorizmstrVo();
		this.authorizedTo = "";
		this.showAuthorizedTo = "";
		try {
			UsermstrVo uv = taskManagementService.getUsermstrVo(taskManagementService.getCurrentUserName());
			authorizedBy = uv.getUsermstr().getAdAccount();
			showAuthorizedBy = uv.getP().getNachn();
		} catch (Exception e) {
			authorizedBy = "";
			showAuthorizedBy = "";
		}
	}

	/**
	 * <p>
	 * Description: 查询授权
	 * </p>
	 */
	public void queryWarrant() {
		List<WfAuthorizmstrVo> wfAuthorizmstrVos = taskManagementService.getAllWfAuthorizmstrVo(taskadminsFlag, authorizmstrSearchVo);
		if (null != wfAuthorizmstrVos && wfAuthorizmstrVos.size() != 0) {
			lazyWfAuthorizmstrVoModel = new PageModel<WfAuthorizmstrVo>(wfAuthorizmstrVos, false);
		} else {
			lazyWfAuthorizmstrVoModel = null;
		}
	}

	/**
	 * <p>
	 * Description: 保存授权
	 * </p>
	 */
	public void saveWarrant() {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		FacesContext context = FacesContext.getCurrentInstance();
		RequestContext.getCurrentInstance().addCallbackParam("showRequestFormDialog", "");
		RequestContext.getCurrentInstance().addCallbackParam("issucc", "no");
		RequestContext.getCurrentInstance().addCallbackParam("authorized", "no");
		if (!(ValidateUtil.validateRequired(context, authorizedTo, "被授权人：")
				& ValidateUtil.validateRequired(context, this.authorizmstrVo.getType(), "授权任务：")
				& ValidateUtil.validateRequired(context, this.authorizmstrVo.getStartDatetime(), "生效时间：") & ValidateUtil.validateRequired(context,
				this.authorizmstrVo.getEndDatetime(), "失效时间："))) {
			return;
		}
		boolean b = true;
		if (excuteMethod != null && INSERT.equals(excuteMethod) && (DateUtil.daysOfTwo(this.authorizmstrVo.getStartDatetime(), new Date())) > 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "生效日期不能早于当前日期", ""));
			b = false;
		}
		if (excuteMethod != null && INSERT.equals(excuteMethod) && (DateUtil.daysOfTwo(this.authorizmstrVo.getEndDatetime(), new Date())) > 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失效日期不能早于当前日期", ""));
			b = false;
		}
		if (this.authorizmstrVo.getStartDatetime().getTime() > this.authorizmstrVo.getEndDatetime().getTime()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失效日期不能早于生效日期", ""));
			b = false;
		}
		authorizmstrVo.setAuthorizedBy(authorizedBy);
		authorizmstrVo.setAuthorizedTo(authorizedTo);
		int num = 0;
		if (excuteMethod != null && "update".equals(excuteMethod)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (null != authorizeStartDatetime && null != authorizeEndDatetime) {
				if (!sdf.format(authorizmstrVo.getStartDatetime()).equals(sdf.format(authorizeStartDatetime))
						|| !sdf.format(authorizmstrVo.getEndDatetime()).equals(sdf.format(authorizeEndDatetime))) {
					num = taskManagementService.getAllAuthorizmstrVo(authorizmstrVo.getAuthorizedBy(), authorizmstrVo.getType(),
							authorizmstrVo.getStartDatetime(), authorizmstrVo.getEndDatetime());
				}
			}
		}
		if (excuteMethod != null && INSERT.equals(excuteMethod)) {
			num = taskManagementService.getAllAuthorizmstrVo(authorizmstrVo.getAuthorizedBy(), authorizmstrVo.getType(),
					authorizmstrVo.getStartDatetime(), authorizmstrVo.getEndDatetime());
		}
		if (num != 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "同一个授权人，同一任务类型的授权记录只允许存在一条,生效日期和失效日期不能有交集！", ""));
			b = false;
		}
		if (null != this.authorizmstrVo.getRemarks() && "".equals(this.authorizmstrVo.getRemarks().trim())) {
			if (!ValidateUtil.validateMaxlength(context, this.authorizmstrVo.getRemarks(), "备注：", 500)) {
				b = false;
			}
		}
		if (!b) {
			return;
		}
		boolean bb = false;
		if (INSERT.equals(excuteMethod)) {
			bb = this.taskManagementService.saveAuthorizmstr(browserLang.toString(), authorizmstrVo);
			if (bb) {
				RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
				RequestContext.getCurrentInstance().addCallbackParam("authorized", "yes");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "新增授权信息成功，请查看并确认！"));
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "新增授权信息失败！"));
			}
		}
		if ("update".equals(excuteMethod)) {
			bb = this.taskManagementService.updateAuthorizmstr(browserLang.toString(), this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getId(),
					authorizmstrVo);
			if (bb) {
				RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
				RequestContext.getCurrentInstance().addCallbackParam("authorized", "yes");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "编辑授权信息成功，请查看并确认！"));
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "编辑授权信息失败！"));
			}
			queryWarrant();
		}
	}

	/**
	 * <p>
	 * Description: 删除授权
	 * </p>
	 */
	public void deleteWarrant() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean b = taskManagementService.deleteAuthorizmstr(selectedWfAuthorizmstrVo.getWfAuthorizmstr().getId());
		if (b) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "删除授权信息成功，请查看！"));
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "删除授权信息失败！"));
		}
		queryWarrant();
	}

	/**
	 * <p>
	 * Description: 更新授权
	 * </p>
	 */
	public void updateWarrant() {
		authorizmstrVo = new AuthorizmstrVo();
		authorizmstrVo.setAuthorizedBy(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getAuthorizedBy());
		this.authorizedBy = this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getAuthorizedBy();
		this.authorizedTo = this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getAuthorizedTo();
		try {
			this.showAuthorizedBy = this.taskManagementService.getUsermstrVo(authorizedBy).getP().getNachn();
			this.showAuthorizedTo = this.taskManagementService.getUsermstrVo(authorizedTo).getP().getNachn();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.showAuthorizedBy = "";
			this.showAuthorizedTo = "";
		}
		authorizmstrVo.setAuthorizedTo(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getAuthorizedTo());
		authorizmstrVo.setStartDatetime(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getStartDatetime());
		authorizmstrVo.setEndDatetime(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getEndDatetime());
		this.authorizeStartDatetime = this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getStartDatetime();
		this.authorizeEndDatetime = this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getEndDatetime();
		authorizmstrVo.setRemarks(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getRemarks());
		authorizmstrVo.setType(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getType());
		if (this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getMailInd() != null
				&& "N".equals(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getMailInd())) {
			authorizmstrVo.setEmailFlag(true);
		} else {
			authorizmstrVo.setEmailFlag(false);
		}
		if (this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getSysNoticeInd() != null
				&& "N".equals(this.selectedWfAuthorizmstrVo.getWfAuthorizmstr().getSysNoticeInd())) {
			authorizmstrVo.setSysNoticeFlag(true);
		} else {
			authorizmstrVo.setSysNoticeFlag(false);
		}
	}

	/**
	 * <p>
	 * Description: 重置查询条件
	 * </p>
	 */
	public void resetTaskSearchVo() {
		taskSearchVo = new TaskSearchVo();
		companyMap.clear();
		companyItems.clear();
	}

	public void changeType() {
		companyMap.clear();
		companyItems.clear();
	}

	public void selectQuestionCompanys(CompanyManagerModel[] com) {
		for (CompanyManagerModel vo : com) {
			String company = vo.getStext();
			String companyId = vo.getOid();
			taskSearchVo.getSelectedQuestionCompanys().add(companyId);
			companyMap.put(company, companyId);
		}
	}

	public void selectedFeebBackCompanys(CompanyManagerModel[] com) {
		for (CompanyManagerModel vo : com) {
			taskSearchVo.getSelectedFeebBackCompanys().add(vo.getId());
			companyItems.add(vo);
		}
	}

	public void searchEmailHistory() {
		emailHistory = findEmailHistory(filter);
	}

	public void resetEmailQuery() {
		filter = new HashMap<String, Object>();
	}

	public String getUserName(String adAccount) {
		return userCommonBean.getUserRealName(adAccount);
	}

	public TreeNode getTaskTreeRoot() {
		return taskTreeRoot;
	}

	public void setTaskTreeRoot(TreeNode taskTreeRoot) {
		this.taskTreeRoot = taskTreeRoot;
	}

	public TreeNode getSelectedTaskTreeNode() {
		return selectedTaskTreeNode;
	}

	public void setSelectedTaskTreeNode(TreeNode selectedTaskTreeNode) {
		this.selectedTaskTreeNode = selectedTaskTreeNode;
	}

	public WfInstancemstrVo getSelectedWfInstancemstrVo() {
		return selectedWfInstancemstrVo;
	}

	public void setSelectedWfInstancemstrVo(WfInstancemstrVo selectedWfInstancemstrVo) {
		this.selectedWfInstancemstrVo = selectedWfInstancemstrVo;
	}

	public LazyDataModel<WfInstancemstrVo> getLazyWfInstancemstrVoModel() {
		return lazyWfInstancemstrVoModel;
	}

	public void setLazyWfInstancemstrVoModel(LazyDataModel<WfInstancemstrVo> lazyWfInstancemstrVoModel) {
		this.lazyWfInstancemstrVoModel = lazyWfInstancemstrVoModel;
	}

	public TaskSearchVo getTaskSearchVo() {
		return taskSearchVo;
	}

	public void setTaskSearchVo(TaskSearchVo taskSearchVo) {
		this.taskSearchVo = taskSearchVo;
	}

	public WfInstancemstr getWfInstancemstr() {
		return wfInstancemstr;
	}

	public void setWfInstancemstr(WfInstancemstr wfInstancemstr) {
		this.wfInstancemstr = wfInstancemstr;
	}

	public String getAuthorizedTo() {
		return authorizedTo;
	}

	public void setAuthorizedTo(String authorizedTo) {
		this.authorizedTo = authorizedTo;
	}

	public String getShowAuthorizedTo() {
		return showAuthorizedTo;
	}

	public void setShowAuthorizedTo(String showAuthorizedTo) {
		this.showAuthorizedTo = showAuthorizedTo;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public String getShowAuthorizedBy() {
		return showAuthorizedBy;
	}

	public void setShowAuthorizedBy(String showAuthorizedBy) {
		this.showAuthorizedBy = showAuthorizedBy;
	}

	public UserCommonBean getUserCommonBean() {
		return userCommonBean;
	}

	public void setUserCommonBean(UserCommonBean userCommonBean) {
		this.userCommonBean = userCommonBean;
	}

	public LazyDataModel<WfAuthorizmstrVo> getLazyWfAuthorizmstrVoModel() {
		return lazyWfAuthorizmstrVoModel;
	}

	public void setLazyWfAuthorizmstrVoModel(LazyDataModel<WfAuthorizmstrVo> lazyWfAuthorizmstrVoModel) {
		this.lazyWfAuthorizmstrVoModel = lazyWfAuthorizmstrVoModel;
	}

	public WfAuthorizmstrVo getSelectedWfAuthorizmstrVo() {
		return selectedWfAuthorizmstrVo;
	}

	public void setSelectedWfAuthorizmstrVo(WfAuthorizmstrVo selectedWfAuthorizmstrVo) {
		this.selectedWfAuthorizmstrVo = selectedWfAuthorizmstrVo;
	}

	public String getExcuteMethod() {
		return excuteMethod;
	}

	public void setExcuteMethod(String excuteMethod) {
		this.excuteMethod = excuteMethod;
	}

	public boolean isTaskadminsFlag() {
		return taskadminsFlag;
	}

	public void setTaskadminsFlag(boolean taskadminsFlag) {
		this.taskadminsFlag = taskadminsFlag;
	}

	public String getCurrentAdAccount() {
		return currentAdAccount;
	}

	public void setCurrentAdAccount(String currentAdAccount) {
		this.currentAdAccount = currentAdAccount;
	}

	public List<MailConfigVo> getMailConfigVoList() {
		return mailConfigVoList;
	}

	public void setMailConfigVoList(List<MailConfigVo> mailConfigVoList) {
		this.mailConfigVoList = mailConfigVoList;
	}

	public boolean isShowCreateTaskFlag() {
		return showCreateTaskFlag;
	}

	public void setShowCreateTaskFlag(boolean showCreateTaskFlag) {
		this.showCreateTaskFlag = showCreateTaskFlag;
	}

	public boolean isShowMyTaskFlag() {
		return showMyTaskFlag;
	}

	public void setShowMyTaskFlag(boolean showMyTaskFlag) {
		this.showMyTaskFlag = showMyTaskFlag;
	}

	public boolean isShowMyAuthorized() {
		return showMyAuthorized;
	}

	public void setShowMyAuthorized(boolean showMyAuthorized) {
		this.showMyAuthorized = showMyAuthorized;
	}

	public boolean isShowMailConfig() {
		return showMailConfig;
	}

	public void setShowMailConfig(boolean showMailConfig) {
		this.showMailConfig = showMailConfig;
	}

	public boolean isShowStopWorkFlowButton() {
		return showStopWorkFlowButton;
	}

	public void setShowStopWorkFlowButton(boolean showStopWorkFlowButton) {
		this.showStopWorkFlowButton = showStopWorkFlowButton;
	}

	public Date getAuthorizeStartDatetime() {
		return authorizeStartDatetime;
	}

	public void setAuthorizeStartDatetime(Date authorizeStartDatetime) {
		this.authorizeStartDatetime = authorizeStartDatetime;
	}

	public Date getAuthorizeEndDatetime() {
		return authorizeEndDatetime;
	}

	public void setAuthorizeEndDatetime(Date authorizeEndDatetime) {
		this.authorizeEndDatetime = authorizeEndDatetime;
	}

	public AuthorizmstrVo getAuthorizmstrVo() {
		return authorizmstrVo;
	}

	public void setAuthorizmstrVo(AuthorizmstrVo authorizmstrVo) {
		this.authorizmstrVo = authorizmstrVo;
	}

	public AuthorizmstrSearchVo getAuthorizmstrSearchVo() {
		return authorizmstrSearchVo;
	}

	public void setAuthorizmstrSearchVo(AuthorizmstrSearchVo authorizmstrSearchVo) {
		this.authorizmstrSearchVo = authorizmstrSearchVo;
	}

	public String getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}

	public List<Long> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Long> companys) {
		this.companys = companys;
	}

	public Map<String, String> getCompanyMap() {
		return companyMap;
	}

	public void setCompanyMap(Map<String, String> companyMap) {
		this.companyMap = companyMap;
	}

	public List<CompanyManagerModel> getCompanyItems() {
		return companyItems;
	}

	public void setCompanyItems(List<CompanyManagerModel> companyItems) {
		this.companyItems = companyItems;
	}

	public boolean isShowOverTimeMailConfig() {
		return showOverTimeMailConfig;
	}

	public void setShowOverTimeMailConfig(boolean showOverTimeMailConfig) {
		this.showOverTimeMailConfig = showOverTimeMailConfig;
	}

	public LazyDataModel<EmailInfo> getEmailHistory() {
		return emailHistory;
	}

	public void setEmailHistory(LazyDataModel<EmailInfo> emailHistory) {
		this.emailHistory = emailHistory;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getTaskNodeMailInfo() {
		return TASK_NODE_MAIL_INFO;
	}

	public String getTaskNodeCreateTask() {
		return TASK_NODE_CREATE_TASK;
	}

	public String getTaskNodeMyAuthorized() {
		return TASK_NODE_MY_AUTHORIZED;
	}

	public String getTaskNodeMailSet() {
		return TASK_NODE_MAIL_SET;
	}

	public String getTaskNodeMailTimed() {
		return TASK_NODE_MAIL_TIMED;
	}

	public String getTaskNodeMailTimeout() {
		return TASK_NODE_MAIL_TIMEOUT;
	}

	public LazyDataModel<EmailInfo> findEmailHistory(final Map<String, Object> filter) {
		LazyDataModel<EmailInfo> notices = new LazyDataModel<EmailInfo>() {

			@Override
			public List<EmailInfo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				Integer rowCount = taskManagementService.findCountFromEmailHistory(filter);
				setRowCount(rowCount);
				return taskManagementService.findEmailHistoryBy(filter, first, pageSize);
			}
		};

		return notices;
	}

	public SendReportBean getSendReportBean() {
		return sendReportBean;
	}

	public void setSendReportBean(SendReportBean sendReportBean) {
		this.sendReportBean = sendReportBean;
	}

	public ApplyQuestionBean getApplyQuestionBean() {
		return applyQuestionBean;
	}

	public void setApplyQuestionBean(ApplyQuestionBean applyQuestionBean) {
		this.applyQuestionBean = applyQuestionBean;
	}
	
}
