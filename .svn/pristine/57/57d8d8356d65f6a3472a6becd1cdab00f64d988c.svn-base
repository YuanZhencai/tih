package com.wcs.tih.interaction.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.feedback.controller.vo.DictPictureVO;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.interaction.controller.vo.ApplyQuestionVO;
import com.wcs.tih.interaction.controller.vo.QuestionVo;
import com.wcs.tih.interaction.service.ApplyQuestionService;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;

import filenet.vw.api.VWException;

@ManagedBean
@ViewScoped
public class ApplyQuestionBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private ApplyQuestionService applyQuestionService;
	@EJB
	private TimeoutEmailService timeoutEmailService;

	@ManagedProperty(value = "#{userCommonBean}")
	private UserCommonBean userCommonBean;
	@ManagedProperty(value = "#{currentUser}")
	private CurrentUserBean currentUser;

	/**
	 * 页面上dataTable的附件操作路程: 页面使用的是createDlgTableList 添加一个createDlgTableList增.createDlgNewAddList增,并实时上传到ce !!删除一个id,createDlgTableList减,deleteList增加.不做ce删除.
	 * 如果是提交,那么删除deleteList,进行ce操作.并且提交createDlgTableList到数据库.!! 如果是取消/点X,那么删除newaddList,不做其他操作.
	 */
	// 页面使用的常量
	private final String dropDownImportance = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE;
	private final String dropDownUrgency = DictConsts.TIH_TAX_WORKFLOWURGENCY;
	private final String dropDownRegion = DictConsts.TIH_TAX_REGION;
	private final String dropDownTaxType = DictConsts.TIH_TAX_TYPE;

	// CreateDialog Param (新建和草稿箱)
	// 新建页面上使用的参数集,key为:createDate,createUser,urgency,importance,region,taxType,recipient,queHead,queMore
	private Map<String, Object> createDlgParamMap = new HashMap<String, Object>();
	private List<ApplyQuestionVO> createDlgTableList = new ArrayList<ApplyQuestionVO>();
	private List<String> createDlgNewAddList = new ArrayList<String>();// 在新建页面等同createDlgTableList,如果取消就删掉,在草稿箱等于新添加的所有文件.
	private List<String> createDlgDeleteList = new ArrayList<String>();
	private StringBuffer documentIds = new StringBuffer(); // 下面2个参数,当新建的提问单页面,添加附件的时候,会在添加的同时,赋予这2个属性值.进入草稿箱的时候,这2个值也就是附件ID,NAME的数据库存储的字段
	private StringBuffer documentNames = new StringBuffer();
	private WfInstancemstr wfIns; // 进入草稿箱的时候,喻彬峰页面传来的wfIns.在进入提问流程处理的时候也会用到这个filed.
	private String uploadPlace; // 判断是哪里调用上传,非页面使用.
	private boolean btnDelShow = false;
	private String recipientsTitle = "点击右侧按钮可选择发送多人Email信息通知"; // p:selectCheckboxMenu使用的动态lable
	private Map<String, String> recipientsMap = new HashMap<String, String>(); // p:selectCheckboxMenu使用的下拉框列表,就是f:selectItems的value值,他使用map的key.
	private List<String> recipientsSelectList = new ArrayList<String>(); // p:selectCheckboxMenu使用的Value.
	private Map<String, Object> processDlgParamMap = new HashMap<String, Object>(); // 第一个accordionPanel的数据集合
	private List<ApplyQuestionVO> processDlgStepsAndProList = new ArrayList<ApplyQuestionVO>(); // 循环accordionPanel使用
	// 流程显示页面.
	private String workShow; // 动态生成用到的属性,流程页面使用,用于显示是不是正常工作,显示流程处理项 Page Used(动态控件)
	private boolean lastChargedByShow; // 在非处理人的情况下,显示正在处理人的名字, Page Used (动态控件)
	private List<String> userActionGroup; // 用户所属工作岗位的动作指向下拉框值组 Page Used(动态控件)
	private String menuValueListen; // 动态显示指定的处理动作(如:指定是转签还是协助.),Page Used,(动态控件)
	private String lastChargedByUser; // 如果不是处理人进去,会显示这个处理人的名字给当前用于 Page Used
	private String nowWorkflowPlace; // 获取当前工作流走到的顶岗位节点,Page Used
	private String txtOpionion = ""; // 流程页面上,回复人,转签人等人的回复意见 page Used
	private List<ApplyQuestionVO> processDlgFileList = new ArrayList<ApplyQuestionVO>(); // 用户上传的附件, page Used
	private List<String> processDlgNewAddFileList = new ArrayList<String>();

	// button按钮的处理
	private boolean answerBtnShow = false;
	private boolean helperBtnShow = false;
	private boolean assignerBtnShow = false; // 转签人除拒绝按钮外的布尔值
	private boolean assignerRefuseBtnShow = false; // 拒绝按钮单独使用
	private boolean askerBtnShow = false;
	private String cancelBtnValue = "取消";

	// selectOneMenu中文字和图片一起显示
	private List<DictPictureVO> photos;
	private DictPictureVO selectedhoto;

	private List<DictPictureVO> photosUrgent;
	private DictPictureVO selectedUrgent;

	public void initAddApplyQuestion() {
		initManyEmailUserName();
		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_3, DictConsts.TIH_TAX_REQUESTFORM_3, null);
	}

	public void queryManyEmailUserName() {
		UsermstrVo[] usermstrVO = userCommonBean.getSelectedUsermstrVos();
		for (UsermstrVo vo : usermstrVO) {
			recipientsMap.put(vo.getP().getNachn(), vo.getUsermstr().getAdAccount());
			recipientsSelectList.add(vo.getUsermstr().getAdAccount());
		}
		this.recipientsTitle = "Email收件人列表,可下拉进行编辑.";
	}

	public void initManyEmailUserName() {
		List<UsermstrVo> userByEmail = applyQuestionService.getUserByEmail();
		for (UsermstrVo vo : userByEmail) {
			recipientsMap.put(vo.getP().getNachn(), vo.getUsermstr().getAdAccount());
			recipientsSelectList.add(vo.getUsermstr().getAdAccount());
		}
		this.recipientsTitle = "Email收件人列表,可下拉进行编辑.";
	}

	/**
	 * Description:清空方法,关闭窗口的时候要清空.新建,草稿页面点击取消按钮的时候,要清空数据外加删掉已经上传到fileNet的文件
	 */
	public void clearCreateDialog() {
		this.wfIns = null;// 每次关闭的时候不管有没有wfIns都清空.
		selectedhoto = null;
		selectedUrgent = null;
		initCreateDlg();
	}

	@PostConstruct
	public void init() {
		setSelectedhoto(new DictPictureVO());
		photos = new ArrayList<DictPictureVO>();
		photos.add(new DictPictureVO("一般", "TIH.TAX.WORKFLOWIMPORTANCE.1", "important3.png"));
		photos.add(new DictPictureVO("重要", "TIH.TAX.WORKFLOWIMPORTANCE.2", "important2.png"));
		photos.add(new DictPictureVO("非常重要", "TIH.TAX.WORKFLOWIMPORTANCE.3", "important1.png"));

		setSelectedUrgent(new DictPictureVO());
		photosUrgent = new ArrayList<DictPictureVO>();
		photosUrgent.add(new DictPictureVO("一般", "TIH.TAX.WORKFLOWURGENCY.1", "urgent3.png"));
		photosUrgent.add(new DictPictureVO("紧急", "TIH.TAX.WORKFLOWURGENCY.2", "urgent2.png"));
		photosUrgent.add(new DictPictureVO("非常紧急", "TIH.TAX.WORKFLOWURGENCY.3", "urgent1.png"));
	}

	private void initCreateDlg() {
		this.documentIds = new StringBuffer();
		this.documentNames = new StringBuffer();
		this.createDlgParamMap.clear();
		this.createDlgTableList.clear();
		this.createDlgNewAddList.clear();
		this.createDlgDeleteList.clear();
		this.btnDelShow = false;
		recipientsTitle = "点击右侧按钮可选择多个收件人.";
		recipientsMap.clear();
		recipientsSelectList.clear();
	}

	/**
	 * Description: 这个是进入草稿箱页面所用的方法,进行赋值,喻彬峰页面会调用这个方法
	 */
	public void addInfoToDrafts() {
		initCreateDlg();
		Map<String, Object> tempMap = this.forPageUsedMapValue();
		this.createDlgParamMap.clear();
		this.createDlgParamMap = tempMap;
		String fileId = createDlgParamMap.get("fileId").toString();
		String fileName = createDlgParamMap.get("fileName").toString();
		this.documentIds.append(fileId);
		this.documentNames.append(fileName);
		
		
		this.createDlgTableList = this.analyticalFileIdAndName(documentIds.toString(), documentNames.toString());
		this.btnDelShow = true;
		// map里key放人名,value放id
		if (createDlgParamMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT) != null) {
			String recipients = createDlgParamMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT).toString();
			if (!recipients.trim().equals("")) {
				String[] recipientArray = recipients.split(",");
				if (recipientArray.length >= 2) {
					for (int i = 0; i < recipientArray.length; i = i + 2) {
						recipientsMap.put(recipientArray[i], recipientArray[i + 1]);
						recipientsSelectList.add(recipientArray[i + 1]);
					}
					this.recipientsTitle = "Email收件人列表,可下拉进行编辑.";
				}
			}
		}
		selectedhoto = new DictPictureVO();
		String importance = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1;
		if (this.wfIns.getImportance() != null) {
			importance = this.wfIns.getImportance();
		}
		selectedhoto.setCode(importance);
		selectedUrgent = new DictPictureVO();
		String urgency = DictConsts.TIH_TAX_WORKFLOWURGENCY_1;
		if (this.wfIns.getUrgency() != null) {
			urgency = this.wfIns.getUrgency();
		}
		selectedUrgent.setCode(urgency);
		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_3, DictConsts.TIH_TAX_REQUESTFORM_3, wfIns.getId());

	}

	/**
	 * Description: 在草稿箱中删除未启动的流程
	 */
	public void deleteWorkflowFromDraft() {
		applyQuestionService.deleteWorkflowFromDraft(wfIns, documentIds);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("删除成功,请重新查询!", ""));
		RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
		// 刷新
		try {
			com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Description: 关于草稿箱和新建页面,2个保存按钮的方法,根据是否传wfIns来判断
	 */
	public void createWorkflowToSave() {
		// 收件人的处理,sb最终存入到wfinsPro中去,下次进入草稿直接读取出来.
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Entry<String, String> tempMap : recipientsMap.entrySet()) {
			for (String account : recipientsSelectList) {
				if (tempMap.getValue().equals(account)) {
					if (i == 0) {// 前面是人民,后面是id
						sb.append(tempMap.getKey() + "," + tempMap.getValue());
						i++;
					} else {
						sb.append("," + tempMap.getKey() + "," + tempMap.getValue());
					}
				}
			}
		}
		boolean haveError = this.validateCreateDlgValue();
		if (haveError) {
			return;
		}
		this.antianalyticFileIdAndNameForSaveDB(createDlgTableList);
		QuestionVo questionVo = new QuestionVo();
		questionVo.setWfIns(wfIns);
		questionVo.setCreateDlgParamMap(createDlgParamMap);
		questionVo.setDocumentIds(documentIds);
		questionVo.setDocumentNames(documentNames);
		questionVo.setRecipients(sb.toString());
		questionVo.setImportance(selectedhoto.getCode());
		questionVo.setUrgency(selectedUrgent.getCode());
		
		if (wfIns == null) {
			// wfIns为空,表明是新建的dialog,保存操作,只是保存数据到流程表,但不启动fileNet流程
			WfInstancemstr saveToDraft = applyQuestionService.saveToDraft(questionVo);
			wfRemindVo.setWfId(saveToDraft.getId());
		} else {
			// 在草稿箱中点击保存直接关闭,进行更新数据库操作,但不进行fileNet操作.这里只需要调用更新方法来更新
			applyQuestionService.updateToDraft(questionVo);
			wfRemindVo.setWfId(wfIns.getId());
		}
		// 流程超时邮件规则
		timeoutEmailService.saveWfTimeoutRemind(wfRemindVo);
		// 刷新主页面
		try {
			com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
		clearCreateDialog();
		RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("保存成功,请重新查询草稿箱!", ""));
		if (!createDlgDeleteList.isEmpty()) {
			applyQuestionService.deleteBatchFileCE(createDlgDeleteList);
		}
	}

	/**
	 * Description: 关于草稿箱和新建页面,2个提交按钮的方法,根据是否传wfIns来判断
	 */
	public void createWorkflowToSubmit() {
		boolean haveError = this.validateCreateDlgValue();
		if (haveError) {
			return;
		}
		this.antianalyticFileIdAndNameForSaveDB(createDlgTableList);
		boolean noDraft;
		if (!btnDelShow) {
			// wfIns为空,表明是新建的dialog,提交操作,按正常流程走,流程表都要保存信息.
			noDraft = true;
		} else {
			// 这里是在草稿箱中点击提交的操作,需要修改数据库中的数据为当前页面数据,并启动流程.这里首先说的并不调用this.createWorkflow();方法,而是先更新表的数据,然后启动流程
			noDraft = false;
		}
		logger.info("noDraft:" + noDraft);
		String errorInfo = null;
		try {
			QuestionVo questionVo = new QuestionVo();
			questionVo.setNoDraft(noDraft);
			questionVo.setWfIns(wfIns);
			questionVo.setCreateDlgParamMap(createDlgParamMap);
			questionVo.setDocumentIds(documentIds);
			questionVo.setDocumentNames(documentNames);
			questionVo.setCreateDlgNameWithEmail(recipientsSelectList);
			questionVo.setImportance(selectedhoto.getCode());
			questionVo.setUrgency(selectedUrgent.getCode());
			questionVo.setWfRemindVo(wfRemindVo);
			
			errorInfo = applyQuestionService.createWorkFlow(questionVo);
		} catch (VWException e1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "提交流程：", "创建流程失败，请联系系统管理员!"));
			logger.error(e1.getMessage(), e1);
		}
		if (errorInfo != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorInfo, ""));
		} else {
			// 刷新
			RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
			try {
				com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
				logger.error(e.getMessage(), e);
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("提交成功,请重新查询!", ""));
			if (!createDlgDeleteList.isEmpty()) {
				applyQuestionService.deleteBatchFileCE(createDlgDeleteList);
			}
		}
	}

	// =============================================流程页面方法方法================================================================================================
	/**
	 * 显示用.wfIns为喻彬峰传来的实例表信息.得到循环accordionPanel的所有数据,并赋值给workStepsAndProList Description:循环使用的步骤表list
	 * 
	 * @return List集合,页面直接诶调用
	 */
	private void queryStepsAndPro() {
		processDlgStepsAndProList.clear();
		List<WfStepmstr> steps = null;
		if (wfIns.getWfStepmstrs() != null) {
			steps = wfIns.getWfStepmstrs();
		} else {
			steps = this.applyQuestionService.getwfstepmatrs(this.wfIns.getId());
		}
		int i = 0;
		for (WfStepmstr wfs : steps) {
			i++;
			Map<String, String> stepProInfoTemp = this.queryStepProByStep(wfs);
			String opionion = stepProInfoTemp.get("opionion");
			// 这里需要判断
			if (stepProInfoTemp != null && null != stepProInfoTemp.get("fileId")) {
				List<ApplyQuestionVO> fileList = this.analyticalFileIdAndName(stepProInfoTemp.get("fileId"), stepProInfoTemp.get("fileName"));
				processDlgStepsAndProList.add(new ApplyQuestionVO((long) i, wfs, opionion, fileList));
			}
		}
	}

	/**
	 * Description:页面循环使用的步骤属性表信息
	 * 
	 * @param wfs
	 *            实例表对象
	 * @return 返回一个MAP.
	 */
	private Map<String, String> queryStepProByStep(WfStepmstr wfs) {
		Map<String, String> stepProInfoTemp = new HashMap<String, String>();
		List<WfStepmstrProperty> list = wfs.getWfStepmstrProperties();
		for (WfStepmstrProperty wp : list) {
			if (WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION.equals(wp.getName())) {
				stepProInfoTemp.put("opionion", wp.getValue());
			} else if (WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID.equals(wp.getName())) {
				stepProInfoTemp.put("fileId", wp.getValue());
			} else if (WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME.equals(wp.getName())) {
				stepProInfoTemp.put("fileName", wp.getValue());
			}
		}
		return stepProInfoTemp;
	}

	// 得到页面上传送的指定处理人名,显示的和后台使用的
	private boolean showDesignee = true;
	private String supervisor;
	private String showSupervisor;

	/**
	 * Description: 获取要现实的账户名称,这里有前台使用的showSupervisor和后台使用的
	 */
	public void getNeedSupervisor() {
		supervisor = userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
		showSupervisor = userCommonBean.getSelectedUsermstrVo().getP().getNachn();
	}

	public void showSupervisorClean() {
		showSupervisor = null;
	}

	/**
	 * Description: accordionPanel所需要的方法体,显示所有的流程步骤详细信息
	 */
	public void showWorkflowProcessData() {
		// 初始化
		this.secondcancelButton();
		// 第一步,先给页面第一个accordionPanel赋值
		Map<String, Object> tempMap = this.forPageUsedMapValue();
		this.processDlgParamMap.clear();
		this.processDlgParamMap = tempMap;
		
		// 第二步,再给页面的循环的accordionPanel赋值,queryStepsAndPro为循环体赋值.
		this.queryStepsAndPro();
		// 第三步
		// A:通过传进来的wfIns得到工作流唯一标识,并判断当前用户是不是流程最后一步指定的接受人.
		String workflowNumber = wfIns.getNo();
		WfStepmstr lastStep = applyQuestionService.queryLastStep(wfIns);
		lastChargedByUser = lastStep.getChargedBy();
		String currentUserName = currentUser.getCurrentUserName();
		if (lastChargedByUser != null && !lastChargedByUser.equals("") && lastChargedByUser.equals(currentUserName)) {
			this.workShow = "yes";
		} else {
			this.workShow = "no";
		}
		// B:这里要对最后一个accordingPanl进行赋值,就是下拉框的动作选择,先从fileNet查询到当前节点,根据节点判断.
		try {
			// 获取当前工作流走到的顶岗位节点
			String status = this.wfIns.getStatus();
			if (!DictConsts.TIH_TAX_WORKFLOWSTATUS_3.equals(status) && !DictConsts.TIH_TAX_WORKFLOWSTATUS_4.equals(status)) {
				nowWorkflowPlace = applyQuestionService.queryNowWorkflowPlace(workflowNumber);
			} else {
				nowWorkflowPlace = null;
			}
		} catch (VWException e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "FileNet查询出现异常,请联系管理员.", ""));
			return;
		}

		// 还原收件人
		if (processDlgParamMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT) != null
				&& !"".equals(processDlgParamMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT).toString().trim())) {
			String recipients = processDlgParamMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT).toString().trim();
			String[] strArray = recipients.split(",");
			if (strArray.length >= 1) {
				for (int i = 0; i < strArray.length; i++) {
					if (!"".equals(strArray[i].trim())) {
						recipientsMap.put(userCommonBean.getUserRealName(strArray[i]), strArray[i]);
						recipientsSelectList.add(strArray[i]);
					}
				}
			}
		}

		// 判断最后一个accordionPanel是否显示.
		if (null != lastChargedByUser && !("").equals(lastChargedByUser) && ("no").equals(workShow)) {
			lastChargedByShow = true;
			this.cancelBtnValue = "退出";
		} else if (null == lastChargedByUser || ("").equals(lastChargedByUser)) {
			lastChargedByShow = false;
			this.cancelBtnValue = "退出";
		} else {
			lastChargedByShow = false;
		}
		lastChargedByUser = nowWorkflowPlace + " " + userCommonBean.getUserRealName(lastChargedByUser);

		if (nowWorkflowPlace == null) {
			// 已经结束,从fileNet上查到的节点为null的话,这个流程就是已经结束.
			this.workShow = "no";
			return;
		} else if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_ANSWER_STEP) && !workShow.equals("no")) {
			// 当前要处理的是:回答人岗
			this.answerBtnShow = true;
		} else if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_ASKER_STEP) && !workShow.equals("no")) {
			// 当前要处理的是:提问人岗
			this.askerBtnShow = true;
			this.showDesignee = false;
		} else if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_ASSIGNER_STEP) && !workShow.equals("no")) {
			// 当前要处理的是:转签人岗
			this.assignerBtnShow = true;
			// 判断有无权限,有就给true,没有就仍然是false.
			if (this.applyQuestionService.queryAssignerHaveRefuse(wfIns.getId())) {
				this.assignerRefuseBtnShow = true;
			}
		} else if (!workShow.equals("no")) {
			// 当前要处理的是:协助回答岗
			this.helperBtnShow = true;
			this.showDesignee = false;
		}

		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_3, DictConsts.TIH_TAX_REQUESTFORM_3, wfIns.getId());

	}

	/**
	 * Description:第二个页面取消按钮使用的方法,用于删除刚才上传
	 */
	public void secondcancelButton() {
		// 用于点击取消的时候删除掉已经上传到fileNet上的附件,isSubmit=true,表示是提交,不删除.
		this.showDesignee = true;
		this.txtOpionion = "";
		this.processDlgFileList.clear();
		this.processDlgNewAddFileList.clear();
		answerBtnShow = false;
		helperBtnShow = false;
		assignerBtnShow = false; // 转签人除拒绝按钮外的布尔值
		assignerRefuseBtnShow = false; // 拒绝按钮单独使用
		askerBtnShow = false;
		recipientsMap = new HashMap<String, String>();
		recipientsSelectList = new ArrayList<String>();
	}

	// 流程处理所需要的参数.
	private String sendToDirection; // 发送方向,走流程需要,对应FileNet路由
	private String sendToName; // 发送人的账户名称,对应FileNet中下个节点的用户名
	private UploadedFile upFile;// 上传文件
	private WfRemindVo wfRemindVo = null;

	/**
	 * Description:进行提交处理,获取流程Number以及执行流程,在applyquestion_dialog.xhtml页面进行提交的时候使用.
	 */
	public void submitProcess(String sendDirection) {

		// 判断上个节点所选的人员是否包含在收件人当中
		Map<String, Object> tempMap = this.forPageUsedMapValue();
		String name = tempMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT).toString();
		String[] split = name.split(",");
		List<String> arrayList = Arrays.asList(split);
		for (String str : arrayList) {
			if (!ifContainIndexOf(recipientsSelectList.toString(), str)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "不能删除上个节点所选择的的收件人!", ""));
				return;
			}
		}

		if (null != this.txtOpionion && !("").equals(txtOpionion.trim()) && this.txtOpionion.length() > 500) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "详细信息：", "长度不允许大于500个字符!"));
			return;
		}
		// 调用下面的方法,传入喻彬峰传过来的wfIns.进入方法体.先要判断当前的岗位,再选用合适的方法调用这个方法,nowWorkflowPlace的值必然是下面4个值中的一个.
		if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_ANSWER_STEP)) {
			this.workflowMethod("answer", sendDirection);
		} else if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_HELPER_STEP)) {
			this.workflowMethod("helper", sendDirection);
		} else if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_ASKER_STEP)) {
			this.workflowMethod("asker", sendDirection);
		} else if (nowWorkflowPlace.equals(WorkflowConsts.ASKWORKFLOW_ASSIGNER_STEP)) {
			this.workflowMethod("assigner", sendDirection);
		}
	}

	/**
	 * 
	 * <p>
	 * Description: 判断一个字符串str1是否包含另一个字符串str2
	 * </p>
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean ifContainIndexOf(String str1, String str2) {
		if (str1.indexOf(str2) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Description: submitProcess()调用的方法体,提交步骤
	 * </p>
	 * 
	 * @param workflowNodalPoint
	 *            submitProcess()方法传来的值,当前节点名,必然是answer,helper,asker,assigner中的一个.
	 * @param sendDirection
	 *            页面传来的方向
	 */
	private void workflowMethod(String workflowNodalPoint, String sendDirection) {
		String workflowNumber = wfIns.getNo(); // 通过传入的实体对象一表来获取工作流标识.
		sendToName = supervisor; // 这里需要从UI上获取是人的账户!如果是协助或者转签的话.否则提问人就是定值!
		sendToDirection = ""; // 这里需要从UI上获取是那个方向!
		String taskStatus = DictConsts.TIH_TAX_WORKFLOWSTATUS_2;

		if (workflowNodalPoint.equals("answer")) {
			if (sendDirection.equals("ansAnswer")) {
				sendToDirection = "asker";
				sendToName = applyQuestionService.queryAsker(wfIns);
			} else if (sendDirection.equals("ansHelper")) {
				sendToDirection = "helper";
				if (supervisor == null || supervisor.equals("")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请在指定人员中选择协助人!", ""));
					return;
				}
			} else if (sendDirection.equals("ansAssigner")) {
				sendToDirection = "assigner";
				if (supervisor == null || supervisor.equals("")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请在指定人员中选择转签人!", ""));
					return;
				}
			}
		} else if (workflowNodalPoint.equals("helper")) {
			WfStepmstr wfs = applyQuestionService.queryLastWfsByWfIns(wfIns);
			if (wfs.getName().equals(WorkflowConsts.ASKWORKFLOW_ANSWER_STEP)) {
				// 这里是反馈的方向,如果上一部是回答人就是反馈给回答人岗
				sendToDirection = "answer";
				sendToName = wfs.getCreatedBy();
			} else {
				// 因为流程里只有2个方向,如果不是回答人就反馈给转签人岗
				sendToDirection = "assigner";
				sendToName = wfs.getCreatedBy();
			}
		} else if (workflowNodalPoint.equals("assigner")) {
			WfStepmstr wfs = applyQuestionService.queryLastWfsByWfIns(wfIns);
			if (sendDirection.equals("assAnswer")) { // 拒绝
				sendToDirection = "answer";
				sendToName = wfs.getCreatedBy();// 这里要查询回答人岗的信息.这里如果转签人第一次时有拒绝,如果进行了协助或者回答将不能再进行拒绝.
			} else if (sendDirection.equals("assHelper")) { // 协助
				if (supervisor == null || supervisor.equals("")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请在指定人员中选择协助人!", ""));
					return;
				}
				sendToDirection = "helper";
			} else {
				// 回答
				sendToDirection = "asker";
				sendToName = applyQuestionService.queryAsker(wfIns);
			}
		} else if (workflowNodalPoint.equals("asker")) {
			WfStepmstr wfs = applyQuestionService.queryLastWfsByWfIns(wfIns);
			if (sendDirection.equals("askerOver")) {
				// 如果页面传来的值是解决,就结束整个流程
				sendToDirection = "over";
				taskStatus = DictConsts.TIH_TAX_WORKFLOWSTATUS_3;
				sendToName = "";// SQL字段不允许为空
			} else {
				// 未解决,获取上一步步骤
				if (wfs.getName().equals(WorkflowConsts.ASKWORKFLOW_ANSWER_STEP)) {
					// 返回给回答人岗
					sendToDirection = "answer";
					sendToName = wfs.getCreatedBy();
				} else {
					// 返回给转签岗
					sendToDirection = "assigner";
					sendToName = wfs.getCreatedBy();
				}
			}
		}

		// 用当前dataTable使用的processDlgFileList反解析出本类中的documentIds和doucumentNames.
		this.antianalyticFileIdAndNameForSaveDB(processDlgFileList);
		try {
			QuestionVo questionVo = new QuestionVo();
			questionVo.setWfIns(wfIns);
			questionVo.setWorkflowNodalPoint(workflowNodalPoint);
			questionVo.setSendToName(sendToName);
			questionVo.setSendToDirection(sendToDirection);
			questionVo.setWorkflowNum(workflowNumber);
			questionVo.setTxtOpionion(txtOpionion);
			questionVo.setDocumentIds(documentIds);
			questionVo.setDocumentNames(documentNames);
			questionVo.setTaskStatus(taskStatus);
			questionVo.setProcessDlgParamMap(processDlgParamMap);
			questionVo.setRecipientsSelectList(recipientsSelectList);
			
			applyQuestionService.workflowExecuteAction(questionVo);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "提交失败", "执行流程出现问题，请联系管理员"));
			logger.info("执行流程出现问题!");
			logger.error(e.getMessage(), e);
			return;
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("提交成功!请重新查询!", ""));
		RequestContext.getCurrentInstance().addCallbackParam("sumbitProcess", "yes");
		// 刷新主页面
		try {
			com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Description: 附件的下载操作
	 * 
	 * @param fileId
	 *            附件FileNet上的ID
	 * @return 文件流.
	 */
	public StreamedContent downloadFile(String fileId) {
		try {
			return applyQuestionService.downloadFile(fileId);
		} catch (MimeException e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档类型不正确。", ""));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载出现异常，请联系系统管理员。", ""));
			return null;
		}
	}

	/**
	 * <p>
	 * Description: 公用方法:用于为页面上的下拉框那堆控件赋值
	 * </p>
	 * 
	 * @return 页面使用的Map<String,Object>,
	 */
	private Map<String, Object> forPageUsedMapValue() {
		List<WfInstancemstrProperty> wfInsProList = null;
		if (this.wfIns.getWfInstancemstrProperties() != null) {
			wfInsProList = wfIns.getWfInstancemstrProperties();
		} else {
			wfInsProList = this.applyQuestionService.getwfips(this.wfIns.getId());
		}
		Map<String, String> wfInsProAndStepProMap = new HashMap<String, String>();
		for (WfInstancemstrProperty wp : wfInsProList) {
			wfInsProAndStepProMap.put(wp.getName(), wp.getValue());
		}
		List<WfStepmstrProperty> wpList = null;
		if (this.wfIns.getWfStepmstrs() != null) {
			wpList = wfIns.getWfStepmstrs().get(0).getWfStepmstrProperties();
		} else {
			wpList = this.applyQuestionService.getwfstepmatrs(this.wfIns.getId()).get(0).getWfStepmstrProperties();
		}
		for (WfStepmstrProperty wp : wpList) {
			wfInsProAndStepProMap.put(wp.getName(), wp.getValue());
		}
		Map<String, Object> tempMap = new HashMap<String, Object>();
		// createDate,createUser,urgency,importance,region,taxType,queHead,queMore
		tempMap.put("createDate", wfIns.getCreatedDatetime());
		tempMap.put("createUser", userCommonBean.getUserRealName(wfIns.getRequestBy()));
		tempMap.put("importance", wfIns.getImportance());
		tempMap.put("urgency", wfIns.getUrgency());
		tempMap.put(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT, wfInsProAndStepProMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT));
		tempMap.put("region", wfInsProAndStepProMap.get(DictConsts.TIH_TAX_REGION));
		tempMap.put("taxType", wfInsProAndStepProMap.get(DictConsts.TIH_TAX_TYPE));
		tempMap.put("queHead", wfInsProAndStepProMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_QUESTIONHEAD));
		tempMap.put("queMore", wfInsProAndStepProMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION));
		tempMap.put("fileId", wfInsProAndStepProMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID));
		tempMap.put("fileName", wfInsProAndStepProMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME));
		tempMap.put("cc", wfInsProAndStepProMap.get(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_CC));
		return tempMap;
	}

	/**
	 * <p>
	 * Description: 公用方法,解析fileId和fileName,并返回一个List,List所用VO为 id,fileName,fileId
	 * </p>
	 * 
	 * @param fileIdSQL
	 *            数据库存储方式的附件ID序列
	 * @param fileNameSQL
	 *            数据库存储方式的附件name序列
	 * @return 以VO封装好的页面使用的list.
	 */
	private List<ApplyQuestionVO> analyticalFileIdAndName(String fileIdSQL, String fileNameSQL) {
		List<ApplyQuestionVO> tempList = new ArrayList<ApplyQuestionVO>();
		if (null != fileIdSQL && !"".equals(fileIdSQL)) {
			String[] fileIdArray = fileIdSQL.split(",");
			String[] fileNameArray = fileNameSQL.split(",");

			if (fileIdArray.length == fileNameArray.length) {
				for (int i = 0; i < fileIdArray.length; i++) {
					String fileId = fileIdArray[i];
					String fileName = fileNameArray[i];
					tempList.add(new ApplyQuestionVO((long) tempList.size(), fileId, fileName));
				}
			}
		}
		return tempList;
	}

	/**
	 * <p>
	 * Description:公用方法,反解析fileId和fileName,求出这个managerbean中的documentIds, documentNames
	 * </p>
	 * 
	 * @param saveDBList
	 *            要保存到数据库所使用的附件的list.也就是当前dataTable显示使用的List. 在新建页面,草稿页面,都是createDlgTableList
	 */
	private void antianalyticFileIdAndNameForSaveDB(List<ApplyQuestionVO> saveDBList) {
		StringBuffer fileIds = new StringBuffer();
		StringBuffer fileNames = new StringBuffer();
		int i = 0;
		for (ApplyQuestionVO vo : saveDBList) {
			String fileId = vo.getFileId();
			String fileName = vo.getFileName();
			if (i == 0) {
				fileIds.append(fileId);
				fileNames.append(fileName);
			} else {
				fileIds.append("," + fileId);
				fileNames.append("," + fileName);
			}
			i++;
		}
		if (fileNames.toString().length() > 300) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题或回答的所有附件的标题长度不能超过300个字符!", ""));
			return;
		}
		this.documentIds = fileIds;
		this.documentNames = fileNames;
	}

	/**
	 * Description:删除,只更新内存中dataTable显示所用的数据.但是走流程的页面是直接进行删除.
	 * 
	 * @param fileId
	 *            文件ID
	 * @param fileName
	 *            文件名称
	 * @param deletePlace
	 *            删除位置,因为页面多次调用此方法,不同页面有不同的处理方式.
	 */
	public void deleteFile(String fileId, String fileName, boolean homePageDel) {
		// 处理list.
		List<ApplyQuestionVO> tempList = new ArrayList<ApplyQuestionVO>();
		List<ApplyQuestionVO> delTempList = new ArrayList<ApplyQuestionVO>();
		if (homePageDel) {
			tempList = this.createDlgTableList;
			this.createDlgDeleteList.add(fileId); // 在删除list中添加提交后要删除的东西.
		} else {
			tempList = this.processDlgFileList;
			// 处理页面直接删除这个ID.不需要考虑其他,直接物理删除,同时也需要更新processDlgNewAddFileList,防止最后点取消的时候删除出错.
			try {
				applyQuestionService.deleteSingleFileCE(fileId);
			} catch (Exception e) {
				logger.info("删除出现问题!");
				logger.error(e.getMessage(), e);
			}
			this.processDlgNewAddFileList.remove(fileId);
		}
		// 因为是VO,所以删除起来麻烦点.更新dataTable使用的list
		for (ApplyQuestionVO tempVO : tempList) {
			if (fileId.equals(tempVO.getFileId())) {
				delTempList.add(tempVO);
			}
		}
		tempList.removeAll(delTempList);
	}

	// ====================上传文件处理========================
	/**
	 * Description: 上传文件具体的页面位置
	 * 
	 * @param place
	 *            位置,因为页面多次调用此方法,不同页面有不同的处理方式.所以此处表明位置.
	 */
	public void enterPlace(String place) {
		this.uploadPlace = place;
		RequestContext.getCurrentInstance().addCallbackParam("attShow", "yes");
	}

	/**
	 * Description:上传文件的事件,此方法再调用上传方法.
	 * 
	 * @param event
	 *            上传事件
	 */
	public void attachFileUpload(FileUploadEvent event) {
		logger.info("进入上传方法体!");
		upFile = event.getFile();
		if (upFile == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件不能为空", ""));
			return;
		}
		modelProjectAttachment();
	}

	/**
	 * Description:上传文件的方法,上传后需要更新Bean中显示所需的数据,以更新页面.
	 */
	private void modelProjectAttachment() {
		logger.info("here it is!");
		Document document;
		String fileNameIndex = upFile.getFileName();
		if (fileNameIndex.length() > 225) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件名称不能超过225个字符!", ""));
			return;
		}
		String fileName = fileNameIndex.substring(0, fileNameIndex.lastIndexOf('.'));
		List<ApplyQuestionVO> list = new ArrayList<ApplyQuestionVO>();
		if (this.uploadPlace.equals("1")) {
			list = this.createDlgTableList;
		} else {
			list = this.processDlgFileList;
		}
		boolean repeatFile = false;
		for (ApplyQuestionVO vo : list) {
			if (fileName.trim().equals(vo.getFileName().trim())) {
				repeatFile = true;
			}
		}
		if (repeatFile) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, fileName + "文档已经被上传。", ""));
			return;
		}
		try {
			document = applyQuestionService.addFileCE(upFile);
		} catch (MimeException e) {
			e.getStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档类型不正确。", ""));
			return;
		} catch (Exception e) {
			e.getStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传失败，请重新执行.", ""));
			return;
		}
		// 获取所上传文件的ID和Name
		String documentId = document.get_Id().toString();
		String documentName = document.get_Name().toString();
		List<ApplyQuestionVO> tempList = new ArrayList<ApplyQuestionVO>();
		if (this.uploadPlace.equals("1")) {
			this.createDlgTableList.add(new ApplyQuestionVO((long) (createDlgTableList.size() + 1), documentId, documentName));
			this.createDlgNewAddList.add(documentId);
			tempList = this.createDlgTableList;
		} else {
			// 在处理流程页面
			this.processDlgFileList.add(new ApplyQuestionVO((long) (processDlgFileList.size() + 1), documentId, documentName));
			this.processDlgNewAddFileList.add(documentId);
			tempList = this.createDlgTableList;
		}
		StringBuffer fileNames = new StringBuffer();
		int i = 0;
		for (ApplyQuestionVO vo : tempList) {
			String fileNameTemp = vo.getFileName();
			if (i == 0) {
				fileNames.append(fileNameTemp);
			} else {
				fileNames.append("," + fileNameTemp);
			}
			i++;
		}
		if (fileNames.toString().length() > 300) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题或回答的所有附件的标题总长度不能超过300个字符!", ""));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("文档上传成功,请查询!", ""));
	}

	private boolean validateCreateDlgValue() {
		Boolean haveErrorInfo = false;

		if (null == createDlgParamMap.get("region") || createDlgParamMap.get("region").toString().trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "地域：", "不允许为空。"));
			haveErrorInfo = true;
		}
		if (null == createDlgParamMap.get("taxType") || createDlgParamMap.get("taxType").toString().trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税种：", "不允许为空。"));
			haveErrorInfo = true;
		}
		if (null == createDlgParamMap.get("queHead") || createDlgParamMap.get("queHead").toString().trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题标题：", "不允许为空。"));
			haveErrorInfo = true;
		} else if (createDlgParamMap.get("queHead").toString().length() > 50) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题标题：", "长度不允许大于50个字符。"));
			haveErrorInfo = true;
		}
		if (null == createDlgParamMap.get("queMore") || createDlgParamMap.get("queMore").toString().trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题描述：", "不允许为空。"));
			haveErrorInfo = true;
		} else if (createDlgParamMap.get("queMore").toString().length() > 500) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "问题描述：", "长度不允许大于500个字符。"));
			haveErrorInfo = true;
		}

		if (!timeoutEmailService.validateWfRemindVo(wfRemindVo)) {
			haveErrorInfo = true;
		}
		return haveErrorInfo;
	}

	public ApplyQuestionBean() {
		// 构造方法
	}

	public UploadedFile getUpFile() {
		return upFile;
	}

	public void setUpFile(UploadedFile upFile) {
		this.upFile = upFile;
	}

	public String getSendToDirection() {
		return sendToDirection;
	}

	public void setSendToDirection(String sendToDirection) {
		this.sendToDirection = sendToDirection;
	}

	public String getSendToName() {
		return sendToName;
	}

	public void setSendToName(String sendToName) {
		this.sendToName = sendToName;
	}

	public String getTxtOpionion() {
		return txtOpionion;
	}

	public void setTxtOpionion(String txtOpionion) {
		this.txtOpionion = txtOpionion;
	}

	public String getNowWorkflowPlace() {
		return nowWorkflowPlace;
	}

	public void setNowWorkflowPlace(String nowWorkflowPlace) {
		this.nowWorkflowPlace = nowWorkflowPlace;
	}

	public List<String> getUserActionGroup() {
		return userActionGroup;
	}

	public void setUserActionGroup(List<String> userActionGroup) {
		this.userActionGroup = userActionGroup;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getShowSupervisor() {
		return showSupervisor;
	}

	public void setShowSupervisor(String showSupervisor) {
		this.showSupervisor = showSupervisor;
	}

	public String getMenuValueListen() {
		return menuValueListen;
	}

	public void setMenuValueListen(String menuValueListen) {
		this.menuValueListen = menuValueListen;
	}

	public String getWorkShow() {
		return workShow;
	}

	public void setWorkShow(String workShow) {
		this.workShow = workShow;
	}

	public WfInstancemstr getWfIns() {
		return wfIns;
	}

	public void setWfIns(WfInstancemstr wfIns) {
		this.wfIns = wfIns;
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

	public String getLastChargedByUser() {
		return lastChargedByUser;
	}

	public void setLastChargedByUser(String lastChargedByUser) {
		this.lastChargedByUser = lastChargedByUser;
	}

	public boolean isLastChargedByShow() {
		return lastChargedByShow;
	}

	public void setLastChargedByShow(boolean lastChargedByShow) {
		this.lastChargedByShow = lastChargedByShow;
	}

	public String getDropDownImportance() {
		return dropDownImportance;
	}

	public String getDropDownUrgency() {
		return dropDownUrgency;
	}

	public String getDropDownRegion() {
		return dropDownRegion;
	}

	public String getDropDownTaxType() {
		return dropDownTaxType;
	}

	public Map<String, Object> getCreateDlgParamMap() {
		if (createDlgParamMap.size() == 0) {
			initManyEmailUserName();
		}
		this.createDlgParamMap.put("createDate", new Date());
		this.createDlgParamMap.put("createUser", userCommonBean.getUserRealName(currentUser.getCurrentUserName()));
		return createDlgParamMap;
	}

	public void setCreateDlgParamMap(Map<String, Object> createDlgParamMap) {
		this.createDlgParamMap = createDlgParamMap;
	}

	public List<ApplyQuestionVO> getCreateDlgTableList() {
		return createDlgTableList;
	}

	public void setCreateDlgTableList(List<ApplyQuestionVO> createDlgTableList) {
		this.createDlgTableList = createDlgTableList;
	}

	public List<ApplyQuestionVO> getProcessDlgStepsAndProList() {
		return processDlgStepsAndProList;
	}

	public void setProcessDlgStepsAndProList(List<ApplyQuestionVO> processDlgStepsAndProList) {
		this.processDlgStepsAndProList = processDlgStepsAndProList;
	}

	public Map<String, Object> getProcessDlgParamMap() {
		return processDlgParamMap;
	}

	public void setProcessDlgParamMap(Map<String, Object> processDlgParamMap) {
		this.processDlgParamMap = processDlgParamMap;
	}

	public List<ApplyQuestionVO> getProcessDlgFileList() {
		return processDlgFileList;
	}

	public void setProcessDlgFileList(List<ApplyQuestionVO> processDlgFileList) {
		this.processDlgFileList = processDlgFileList;
	}

	public String getRecipientsTitle() {
		return recipientsTitle;
	}

	public void setRecipientsTitle(String recipientsTitle) {
		this.recipientsTitle = recipientsTitle;
	}

	public Map<String, String> getRecipientsMap() {
		return recipientsMap;
	}

	public void setRecipientsMap(Map<String, String> recipientsMap) {
		this.recipientsMap = recipientsMap;
	}

	public List<String> getRecipientsSelectList() {
		return recipientsSelectList;
	}

	public void setRecipientsSelectList(List<String> recipientsSelectList) {
		this.recipientsSelectList = recipientsSelectList;
	}

	public boolean isAnswerBtnShow() {
		return answerBtnShow;
	}

	public void setAnswerBtnShow(boolean answerBtnShow) {
		this.answerBtnShow = answerBtnShow;
	}

	public boolean isHelperBtnShow() {
		return helperBtnShow;
	}

	public void setHelperBtnShow(boolean helperBtnShow) {
		this.helperBtnShow = helperBtnShow;
	}

	public boolean isAssignerBtnShow() {
		return assignerBtnShow;
	}

	public void setAssignerBtnShow(boolean assignerBtnShow) {
		this.assignerBtnShow = assignerBtnShow;
	}

	public boolean isAskerBtnShow() {
		return askerBtnShow;
	}

	public void setAskerBtnShow(boolean askerBtnShow) {
		this.askerBtnShow = askerBtnShow;
	}

	public boolean isAssignerRefuseBtnShow() {
		return assignerRefuseBtnShow;
	}

	public void setAssignerRefuseBtnShow(boolean assignerRefuseBtnShow) {
		this.assignerRefuseBtnShow = assignerRefuseBtnShow;
	}

	public String getCancelBtnValue() {
		return cancelBtnValue;
	}

	public void setCancelBtnValue(String cancelBtnValue) {
		this.cancelBtnValue = cancelBtnValue;
	}

	public boolean isBtnDelShow() {
		return btnDelShow;
	}

	public void setBtnDelShow(boolean btnDelShow) {
		this.btnDelShow = btnDelShow;
	}

	public boolean isShowDesignee() {
		return showDesignee;
	}

	public void setShowDesignee(boolean showDesignee) {
		this.showDesignee = showDesignee;
	}

	public DictPictureVO getSelectedhoto() {
		return selectedhoto;
	}

	public void setSelectedhoto(DictPictureVO selectedhoto) {
		this.selectedhoto = selectedhoto;
	}

	public DictPictureVO getSelectedUrgent() {
		return selectedUrgent;
	}

	public void setSelectedUrgent(DictPictureVO selectedUrgent) {
		this.selectedUrgent = selectedUrgent;
	}

	public List<DictPictureVO> getPhotos() {
		return photos;
	}

	public void setPhotos(List<DictPictureVO> photos) {
		this.photos = photos;
	}

	public List<DictPictureVO> getPhotosUrgent() {
		return photosUrgent;
	}

	public void setPhotosUrgent(List<DictPictureVO> photosUrgent) {
		this.photosUrgent = photosUrgent;
	}

	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}

	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}


	
}
