package com.wcs.tih.feedback.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.CommonBean;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.feedback.controller.vo.AttachmentVO;
import com.wcs.tih.feedback.controller.vo.DictPictureVO;
import com.wcs.tih.feedback.controller.vo.FeedbackAntiVo;
import com.wcs.tih.feedback.controller.vo.FeedbackInspectionVO;
import com.wcs.tih.feedback.controller.vo.StepVO;
import com.wcs.tih.feedback.service.FeedBackAntiService;
import com.wcs.tih.feedback.service.FeedBackInspectationService;
import com.wcs.tih.feedback.service.FeedBackService;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.interaction.service.SendReportService;
import com.wcs.tih.model.InvsAntiAvoidance;
import com.wcs.tih.model.InvsAntiResult;
import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.model.InvsInspectationResult;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.transaction.controller.helper.TaskRefreshHelper;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih Description: 情况反馈Backing Bean Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * 
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class FeedBackBean implements Serializable {
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@ManagedProperty(value = "#{currentUser}")
	private CurrentUserBean currentUser;
	@ManagedProperty(value = "#{commonBean}")
	private CommonBean commonBean;
	@ManagedProperty(value = "#{userCommonBean}")
	private UserCommonBean userCommonBean;

	@EJB
	private FeedBackService feedBackService;
	@EJB
	private CommonService commonService;
	@EJB
	private SendReportService sendReportService;
	@EJB
	private FeedBackInspectationService inspectationService;
	@EJB
	private FeedBackAntiService feedBackAntiService;
	@EJB
	private TimeoutEmailService timeoutEmailService;

	// 封装流程属性信息
	private static final String WORKFLOW_NAME = "TihfeedbackWorkflow";// filenet上流程名称（工厂发起）
	private static final String SIMPLE_WORKFLOE_NAME = "TihfeedbacksimpleWorkflow";// filenet上流程名称（集团发起）
	private Map<String, Object> instanceProperty;

	private static final String TIH_WORKFLOW_FEEDBACK_DESC = "TIH.WORKFLOW.FEEDBACK.DESC"; // 情况描述
	private static final String TIH_WORKFLOW_FEEDBACK_FILEID = "TIH.WORKFLOW.FEEDBACK.FILEID"; // 附件id
	private static final String TIH_WORKFLOW_FEEDBACK_FILENAME = "TIH.WORKFLOW.FEEDBACK.FILENAME"; // 附件名称
	private static final String TIH_WORKFLOW_FEEDBACK_COMPANY = "TIH.WORKFLOW.FEEDBACK.COMPANY"; // 发起人所属公司

	private static ResourceBundle rb;
	// 岗位编码
	private static final String SITUCP; // #情况反馈流程 公司情况反馈处理岗 SITUCP
	private static final String SITUGP; // #情况反馈流程 集团情况反馈处理岗 SITUGP

	private String inspection = FALSE;
	private String antiAvoidance = FALSE;

	// 新增附件列表
	private List<AttachmentVO> atts;
	private LazyDataModel<AttachmentVO> lazyAtts;

	// 当前流程对象
	private WfInstancemstr wfInstancemstr;
	// 是否是当前人处理
	private boolean chargedByCurrentuser;
	// 流程是否完成
	private boolean finishedFeedBack;

	// 情况反馈步骤属性信息
	private Map<String, Object> stepProMap;
	// 节点与路由状态混合结构
	private static String nodeStatus[][];

	// 附件更新id
	private String updateAttachmentId;
	// 后缀
	private static String dnStr;
	// 步骤过程列表
	private List<StepVO> steps;
	// 当前选中的用户
	private UsermstrVo currentProcesser;
	// 显示当前用户名称
	private String processer;

	// 公司查询
	private CompanyManagerModel company;

	// 当前流程名称
	private String currentNodeName;

	// selectOneMenu中文字和图片一起显示
	private List<DictPictureVO> photos;
	private DictPictureVO selectedhoto;

	private List<DictPictureVO> photosUrgent;
	private DictPictureVO selectedUrgent;

	// 项目类型和工作阶段之间的联动关系
	private String type;// 项目类型
	private String stage;// 工作阶段

	// 集团和工厂
	private String requestBy;

	// selectCheckboxMenu多选框
	private List<String> selectedMovies = new ArrayList<String>();
	private Map<String, String> movies = new HashMap<String, String>();

	private int tabVew;

	private boolean draft = false;

	private FeedbackInspectionVO feedbackInspectionVO;
	private List<String> taxTypes = new ArrayList<String>();// 所选税种
	private InvsInspectationResult deleteInspectationResult;// 删除稽查明细
	private FeedbackAntiVo feedbackAntiVo;
	private InvsAntiResult deleteAntiResult;// 删除反避税明细

	private AttachmentVO[] selectAttachment;
	private AttachmentVO[] selectAttachmentsDoFlow;
	private String fnIds;
	private WfRemindVo wfRemindVo;
	private static Map<String, String> timeoutRequestForm = null;

	static {
		nodeStatus = new String[][] {
				// "SuperVisor", //主管
				// "Grouper", //集团处理岗
				// "Assigner", //集团转签岗
				// "Companyer", //公司处理岗
				// "Helper" //协助岗
				// "Auditor"//任务发起人
				{ "SuperVisor", "发起人主管", "Visor_Pass", DictConsts.TIH_TAX_APPROACH_8 }, // 主管通过
				{ "SuperVisor", "发起人主管", "Visor_NotPass", DictConsts.TIH_TAX_APPROACH_11 }, // 主管不通过

				{ "Grouper", "集团处理岗", "Grouper_Audit", DictConsts.TIH_TAX_APPROACH_9 }, // 集团处理岗批复
				{ "Grouper", "集团处理岗", "Grouper_Assign", DictConsts.TIH_TAX_APPROACH_4 }, // 集团处理岗转签
				{ "Grouper", "集团处理岗", "Grouper_Help", DictConsts.TIH_TAX_APPROACH_3 }, // 集团处理岗协助
				{ "Grouper", "集团处理岗", "Grouper_Pass", DictConsts.TIH_TAX_APPROACH_10 }, // 集团处理岗完成

				{ "Assigner", "集团转签处理岗", "Assigner_Audit", DictConsts.TIH_TAX_APPROACH_9 }, // 集团转签岗批复
				{ "Assigner", "集团转签处理岗", "Assigner_Help", DictConsts.TIH_TAX_APPROACH_3 }, // 集团转签岗协助
				{ "Assigner", "集团转签处理岗", "Assigner_Pass", DictConsts.TIH_TAX_APPROACH_10 }, // 集团转签岗完成

				{ "Companyer", "公司处理岗", "Companyer_FeedBackToGrouper", DictConsts.TIH_TAX_APPROACH_5 }, // 公司处理岗反馈给集团处理岗
				{ "Companyer", "公司处理岗", "Companyer_FeedBackToAssigner", DictConsts.TIH_TAX_APPROACH_5 }, // 公司处理岗反馈给转签岗

				{ "Helper", "任务协助岗", "Helper_FeedBackToGrouper", DictConsts.TIH_TAX_APPROACH_5 }, // 协助反馈给集团处理岗
				{ "Helper", "任务协助岗", "Helper_FeedBackToAssigner", DictConsts.TIH_TAX_APPROACH_5 }, // 协助反馈给集团转签岗
				// 集团情况反馈************************************************************************************
				{ "Companyer", "公司处理岗", "SEND", DictConsts.TIH_TAX_APPROACH_1 },// 公司处理岗提交

				{ "Supervisor", "公司处理岗主管", "Supervisor_PASS", DictConsts.TIH_TAX_APPROACH_8 },// 公司处理岗主管通过
				{ "Supervisor", "公司处理岗主管", "NOPASS", DictConsts.TIH_TAX_APPROACH_7 },// 公司处理岗主管不通过

				{ "Auditor", "任务发起人", "Auditor_NOPASS", DictConsts.TIH_TAX_APPROACH_7 },// 任务发起人驳回
				{ "Auditor", "任务发起人", "Auditor_PASS", DictConsts.TIH_TAX_APPROACH_10 } // 任务发起人结束流程
		};
		rb = ResourceBundle.getBundle("positons");
		dnStr = ResourceBundle.getBundle("filenet").getString("tds.users.dn");
		SITUCP = rb.getString("SITUCP");
		SITUGP = rb.getString("SITUGP");

		timeoutRequestForm = new HashMap<String, String>();
		timeoutRequestForm.put(DictConsts.TIH_TAX_REQUESTFORM_5_1, DictConsts.TIH_TAX_REQUESTFORM_6_1);
		timeoutRequestForm.put(DictConsts.TIH_TAX_REQUESTFORM_5_2, DictConsts.TIH_TAX_REQUESTFORM_6_2);
		timeoutRequestForm.put(DictConsts.TIH_TAX_REQUESTFORM_5_3, DictConsts.TIH_TAX_REQUESTFORM_6_3);
		timeoutRequestForm.put(DictConsts.TIH_TAX_REQUESTFORM_5_4, DictConsts.TIH_TAX_REQUESTFORM_6_4);
	}

	@PostConstruct
	public void initFeedBackBean() {
		feedbackInspectionVO = new FeedbackInspectionVO();
		feedbackAntiVo = new FeedbackAntiVo();

		instanceProperty = new HashMap<String, Object>();
		atts = new ArrayList<AttachmentVO>();

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

		lazyAtts = null;
		initAddFeedBack();
		stepProMap = new HashMap<String, Object>();
		updateAttachmentId = ":insert_feedback_form_id";

		setSteps(new ArrayList<StepVO>());
	}

	// ===================================================== Yuan ====================================//

	public void findRemindVoByRquestform() {
		Long wfId = null;
		if (wfInstancemstr != null) {
			wfId = wfInstancemstr.getId();
		}
		logger.info("[requestBy]" + requestBy + "[type]" + type);
		String wfType = DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy) ? DictConsts.TIH_TAX_REQUESTFORM_5 : DictConsts.TIH_TAX_REQUESTFORM_6;
		String requestform = wfType.equals(DictConsts.TIH_TAX_REQUESTFORM_5) ? type : timeoutRequestForm.get(type);
		wfRemindVo = timeoutEmailService.findWfRemindVo(wfType, requestform, wfId);
	}

	public boolean validate(Map<String, String> instanceMap) {
		boolean validated = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if (!ValidateUtil.validateRequired(context, selectedhoto.getCode(), "重要程度：")) {
			validated = false;
		}
		if (!ValidateUtil.validateRequired(context, selectedUrgent.getCode(), "紧急程度：")) {
			validated = false;
		}
		if (!ValidateUtil.validateRequired(context, type, "项目类型：")) {
			validated = false;
		}
		if (!ValidateUtil.validateRequired(context, stage, "工作阶段：")) {
			validated = false;
		}
		if (!ValidateUtil.validateRequired(context, instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY), "所属公司：")) {
			validated = false;
		}
		if (!ValidateUtil.validateMaxlength(context, instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC"), "情况描述：", 300)) {
			validated = false;
		}
		if (!timeoutEmailService.validateWfRemindVo(wfRemindVo)) {
			validated = false;
		}
		return validated;
	}

	// ===================================================== Yuan ====================================//

	public void processernull() {
		this.processer = null;
	}

	public void getTab() {
		if (getInspectionBoolean()) {
			inspection = TRUE;
			antiAvoidance = FALSE;
		} else if (getAntiAvoidanceBoolean()) {
			inspection = FALSE;
			antiAvoidance = TRUE;
		} else {
			inspection = FALSE;
			antiAvoidance = FALSE;
		}
		findRemindVoByRquestform();
	}

	/**
	 * <p>
	 * Description: 新增流程初始化
	 * </p>
	 */
	public void initAddFeedBack() {
		instanceProperty.clear();
		instanceProperty.put("currentDate", new Date());
		instanceProperty.put("currentUser", userCommonBean.getUserRealName(currentUser.getCurrentUserName()));
		instanceProperty.put("TIH_TAX_WORKFLOWIMPORTANCE", "");
		instanceProperty.put("TIH_TAX_WORKFLOWURGENCY", "");
		instanceProperty.put("TIH_TAX_REQUESTFORM_5", "");
		instanceProperty.put("TIH_WORKFLOW_FEEDBACK_COMPANY", "");
	}

	/**
	 * <p>
	 * Description: 选择公司
	 * </p>
	 */
	public void selectCompany(CompanyManagerModel company) {
		if (company == null) {
			instanceProperty.put("TIH_WORKFLOW_FEEDBACK_COMPANY", "");
		} else {
			this.company = company;
			instanceProperty.put("TIH_WORKFLOW_FEEDBACK_COMPANY", company.getStext());
		}
	}

	/**
	 * Description:直接获取多个公司
	 * 
	 * @param com
	 */
	public void setCompanys(CompanyManagerModel[] com) {
		for (CompanyManagerModel vo : com) {
			String company = vo.getStext();
			String companyId = String.valueOf(vo.getId());
			selectedMovies.add(companyId);
			movies.put(company, companyId);
		}
	}

	/**
	 * <p>
	 * Description: 下载附件
	 * </p>
	 * 
	 * @param fileId
	 * @return
	 */
	public StreamedContent getFileByFileid(String fileId) {
		try {
			return feedBackService.getFileByFileid(fileId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载附件：", "操作出错，请联系系统管理员。"));
			return null;
		}
	}

	/**
	 * <p>
	 * Description: 删除附件
	 * </p>
	 * 
	 * @param fileId
	 */
	public void deleteAttachment(String fileId) {
		try {
			if (!draft) {
				feedBackService.deleteDocument(fileId);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除附件：", e.getMessage()));
		}
		for (AttachmentVO att : atts) {
			if (att.getFileId().equals(fileId)) {
				atts.remove(att);
				break;
			}
		}
		lazyAtts = new PageModel<AttachmentVO>(atts, false);

	}

	/**
	 * <p>
	 * Description: 保存草稿箱
	 * </p>
	 */
	public void saveFeedBack() {
		FacesContext context = FacesContext.getCurrentInstance();

		Map<String, String> instanceMap = new HashMap<String, String>();
		instanceMap.put(DictConsts.TIH_TAX_REQUESTFORM_5, type);// 项目类型
		instanceMap.put(DictConsts.TIH_TAX_REQUESTBY, requestBy);// 流程发起：工厂&集团
		instanceMap.put(type, stage);// 工作阶段
		if (DictConsts.TIH_TAX_REQUESTBY_1.equals(requestBy)) { // 所属公司ID
			// 集团
			instanceMap.put(TIH_WORKFLOW_FEEDBACK_COMPANY, selectedMovies.toString().replace('[', ' ').replace(']', ' ').trim().toString());// 公司ID
		} else {
			// 工厂
			if (company == null) {
				instanceMap.put(TIH_WORKFLOW_FEEDBACK_COMPANY, null);// 公司ID
			} else {
				instanceMap.put(TIH_WORKFLOW_FEEDBACK_COMPANY, company.getId().toString());// 公司ID
			}
		}
		logger.info("所属公司：" + instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY));

		if (!validate(instanceMap)) {
			return;
		}
		Map<String, String> stepMap = new HashMap<String, String>();
		stepMap.put(TIH_WORKFLOW_FEEDBACK_DESC, instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim());
		String fileids = "", filenames = "";
		for (AttachmentVO att : atts) {
			filenames += att.getFileName() + ",";
			fileids += att.getFileId() + ",";
		}
		if (!atts.isEmpty()) {
			fileids = fileids.substring(0, fileids.length() - 1);
			filenames = filenames.substring(0, filenames.length() - 1);
		}
		stepMap.put(TIH_WORKFLOW_FEEDBACK_FILEID, fileids);
		stepMap.put(TIH_WORKFLOW_FEEDBACK_FILENAME, filenames);

		String remarks = "";
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim() == null
				|| instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim().isEmpty()) {
			remarks = commonService.getValueByDictCatKey(type, browserLang.toString());
		} else {
			remarks = commonService.getValueByDictCatKey(type, browserLang.toString()) + ","
					+ instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim();
		}
		// 删除附件
		if (null != wfInstancemstr && null != wfInstancemstr.getId()) {
			feedBackService.deleteFilemstr(wfInstancemstr.getId());
		}

		Long wfId = null; // 流程ID
		wfId = feedBackService.saveOrUpdateDraft(draft, instanceMap, stepMap, wfInstancemstr, remarks, selectedhoto.getCode(),
				selectedUrgent.getCode());
		wfRemindVo.setWfId(wfId);
		// 新增或编辑稽查信息
		if (getInspectionBoolean()) {
			InvsInspectation inspectation = feedbackInspectionVO.getInspectation();
			if (instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY) != null && !"".equals(instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY))) {
				inspectation.setCompanymstrId(Long.valueOf(instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY)));
			}
			inspectation.setWfId(wfId);
			inspectation.setTaxTypes(inspectationService.taxTypeToString(taxTypes));
			try {
				inspectationService.saveOrUpdatedInDraft(inspectation, feedbackInspectionVO.getResult());
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "保存流程：", "操作失败。"));
				return;
			}
		}

		// 新增或编辑反避税信息
		if (getAntiAvoidanceBoolean()) {
			InvsAntiAvoidance avoidance = feedbackAntiVo.getAntiAvoidance();
			if (instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY) != null && !"".equals(instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY))) {
				avoidance.setCompanymstrId(Long.valueOf(instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY)));
			}
			avoidance.setWfId(wfId);
			avoidance.setTaxTypes(feedBackAntiService.taxTypeToString(taxTypes));
			try {
				feedBackAntiService.saveOrUpdatedInDraft(avoidance, feedbackAntiVo.getAntiResults());
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "保存流程：", "操作失败。"));
				return;
			}
		}

		// 把附件信息保存到Filemstr表中
		saveFilemstr(wfId);
		timeoutEmailService.saveWfTimeoutRemind(wfRemindVo);

		wfId = null;

		draft = false;

		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		try {
			TaskRefreshHelper.refreshTask(context);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return;
		}
		stage = null;
		inspection = FALSE;
		antiAvoidance = FALSE;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "保存流程：", "操作成功。"));

	}

	/**
	 * 
	 * <p>
	 * Description: 把附件信息保存到Filemstr表中
	 * </p>
	 * 
	 * @param wfId
	 */
	private void saveFilemstr(Long wfId) {
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type) || DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			List<AttachmentVO> asList = Arrays.asList(selectAttachment);
			for (AttachmentVO att : atts) {
				try {
					if (asList.contains(att)) {
						feedBackService.saveFilemstr(att, type, "N", wfId);
					} else {
						feedBackService.saveFilemstr(att, type, "Y", wfId);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

	public void deleteDraft() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (wfInstancemstr != null) {
			feedBackService.deleteFeedBack(wfInstancemstr.getId());
		}

		// 删除稽查信息
		if (getInspectionBoolean()) {
			inspectationService.deleteInspectionDraft(wfInstancemstr.getId());
		}

		// 删除反避税信息
		if (getAntiAvoidanceBoolean()) {
			feedBackAntiService.deleteAntiAndResult(wfInstancemstr.getId());
		}

		draft = false;
		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		try {
			TaskRefreshHelper.refreshTask(context);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return;
		}
		inspection = FALSE;
		antiAvoidance = FALSE;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "删除草稿：", "操作成功。"));
	}

	public boolean getInspectionBoolean() {
		Boolean flag = false;
		if (DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy) && DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type)) {
			flag = true;
		}
		return flag;
	}

	public boolean getAntiAvoidanceBoolean() {
		Boolean flag = false;
		if (DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy) && DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * <p>
	 * Description: 还原草稿
	 * </p>
	 */
	public void revertFeedBack() {
		taxTypes = null;
		company = null;
		instanceProperty.clear();
		type = null;
		stage = null;
		requestBy = null;
		selectedMovies.clear();
		movies.clear();
		instanceProperty.put("currentDate", wfInstancemstr.getCreatedDatetime());// 填制日期
		instanceProperty.put("currentUser", getUsernameByAccount(wfInstancemstr.getCreatedBy()));// 填制人
		// 重要程度
		selectedhoto = new DictPictureVO();
		String importance = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1;
		if (this.wfInstancemstr.getImportance() != null) {
			importance = this.wfInstancemstr.getImportance();
		}
		selectedhoto.setCode(importance);
		// 紧急程度
		selectedUrgent = new DictPictureVO();
		String urgency = DictConsts.TIH_TAX_WORKFLOWURGENCY_1;
		if (this.wfInstancemstr.getImportance() != null) {
			urgency = this.wfInstancemstr.getUrgency();
		}
		selectedUrgent.setCode(urgency);

		type = getInstProValueByName(DictConsts.TIH_TAX_REQUESTFORM_5);// 查询项目类型
		stage = getInstProValueByName(type);// 查询工作阶段
		requestBy = getInstProValueByName(DictConsts.TIH_TAX_REQUESTBY);// 查询流程发起
		getTab();
		if (DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy)) {
			if (getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY) != null) {
				company = feedBackService.getCMM(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY)));
				instanceProperty.put("TIH_WORKFLOW_FEEDBACK_COMPANY", company.getStext());
			}
		} else {
			String idList = getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY);
			if (getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY) != null && !"".equals(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY))) {
				String[] str = idList.split(",");
				List<CompanyManagerModel> cmmList = feedBackService.getCMMList(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY).replace(" ", ""));
				for (int i = 0; i < str.length; i++) {
					movies.put(cmmList.get(i).getStext(), cmmList.get(i).getId().toString());
					selectedMovies.add(cmmList.get(i).getId().toString());
				}
			}
		}
		List<WfStepmstr> sps = wfInstancemstr.getWfStepmstrs();
		if (!sps.isEmpty()) {
			instanceProperty.put("TIH_WORKFLOW_FEEDBACK_DESC", getStepDesc(sps.get(0)));
			lazyAtts = getStepAttachments(sps.get(0));
		}

		// 还原稽查信息
		feedbackInspectionVO = new FeedbackInspectionVO();
		if (getInspectionBoolean()) {
			List<InvsInspectation> list = inspectationService.getInspectionByWfId(wfInstancemstr.getId());
			if (list.size() > 0) {
				InvsInspectation inspectation = list.get(0);
				feedbackInspectionVO.setInspectation(inspectation);
				if (inspectation.getTaxTypes() != null && !"".equals(inspectation.getTaxTypes().trim())) {
					taxTypes = inspectationService.taxTypeToList(inspectation.getTaxTypes());
				}
				if (inspectation.getInvsInspectationResults().size() > 0) {
					feedbackInspectionVO.setResult(inspectation.getInvsInspectationResults());
				}
			}
		}

		// 还原反避税信息
		feedbackAntiVo = new FeedbackAntiVo();
		if (getAntiAvoidanceBoolean()) {
			List<InvsAntiAvoidance> antiAvoidanceLists = feedBackAntiService.getAntiByWfId(wfInstancemstr.getId());
			if (antiAvoidanceLists.size() > 0) {
				InvsAntiAvoidance antiAvoidance = antiAvoidanceLists.get(0);
				feedbackAntiVo.setAntiAvoidance(antiAvoidance);
				if (antiAvoidance.getTaxTypes() != null && !"".equals(antiAvoidance.getTaxTypes().trim())) {
					taxTypes = inspectationService.taxTypeToList(antiAvoidance.getTaxTypes());
				}
				feedbackAntiVo.setAntiResults(antiAvoidance.getInvsAntiResults());
			}
		}
		// 附件Checkbox是否需要勾选上
		selectAttachment = attachmentSelectedCheckbox();
	}

	/**
	 * <p>
	 * Description: 添加附件
	 * </p>
	 * 
	 * @param event
	 */
	public void addAttachment(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		boolean repeatFile = false;
		if (file == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件不能为空", ""));
			return;
		}
		InputStream is;
		try {
			is = file.getInputstream();
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文件流获取失败", ""));
			return;
		}
		String fileName = file.getFileName();
		if (fileName.length() > 225) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件名称不能超过225个字符!", ""));
			return;
		}
		String fileId = "";
		if (!atts.isEmpty()) {
			for (AttachmentVO vo : atts) {
				if (fileName.trim().equals(vo.getFileName().trim())) {
					repeatFile = true;
				}
			}
			if (repeatFile) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, fileName + "文档已经被上传。", ""));
				return;
			}
		}
		try {
			fileId = feedBackService.uploadDocument(is, fileName);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文件上传：", e.getMessage()));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "文件上传：", "操作成功。"));
		long id = atts.isEmpty() ? 1 : atts.get(atts.size() - 1).getId() + 1;
		atts.add(new AttachmentVO(id, fileId, fileName));
		lazyAtts = new PageModel<AttachmentVO>(atts, false);
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type) || DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			// 让页面中附件的checkbox勾上
			selectAttachment = new AttachmentVO[atts.size()];
			for (int i = 0; i < atts.size(); i++) {
				selectAttachment[i] = atts.get(i);
			}
		}
	}

	public void initUpdateId() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (null == type || "".equals(type.trim())) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "上传附件：", "项目类型不允许为空."));
			return;
		}
		RequestContext.getCurrentInstance().addCallbackParam("type", "1");
	}

	public void onRowSelect(SelectEvent event) {
		AttachmentVO attachmentVO = (AttachmentVO) event.getObject();
		feedBackService.updateFilemstr(attachmentVO.getFileId(), "N");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改附件状态成功", ""));
	}

	public void onRowUnselect(UnselectEvent event) {
		AttachmentVO attachmentVO = (AttachmentVO) event.getObject();
		feedBackService.updateFilemstr(attachmentVO.getFileId(), "Y");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改附件状态成功", ""));
	}

	/**
	 * <p>
	 * Description: 提交流程
	 * </p>
	 */
	public void submitFeedBack() {
		Map<String, String> instanceMap = new HashMap<String, String>();
		if (DictConsts.TIH_TAX_REQUESTBY_1.equals(requestBy)) { // 所属公司ID
			// 集团
			instanceMap.put(TIH_WORKFLOW_FEEDBACK_COMPANY, selectedMovies.toString().replace('[', ' ').replace(']', ' ').trim().toString());// 公司ID
			// 超时邮件申请单类型
			instanceMap.put(WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, timeoutRequestForm.get(type));
		} else {
			// 超时邮件申请单类型
			instanceMap.put(WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, type);
			// 工厂
			if (company == null) {
				instanceMap.put(TIH_WORKFLOW_FEEDBACK_COMPANY, null);// 公司ID
			} else {
				instanceMap.put(TIH_WORKFLOW_FEEDBACK_COMPANY, company.getId().toString());// 公司ID
			}
		}

		logger.info("所属公司：" + instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY));
		FacesContext context = FacesContext.getCurrentInstance();
		if (!validate(instanceMap)) {
			return;
		}
		instanceMap.put(DictConsts.TIH_TAX_REQUESTFORM_5, type);// 项目类型
		instanceMap.put(type, stage);// 工作阶段
		instanceMap.put(DictConsts.TIH_TAX_REQUESTBY, requestBy);// 公司&集团

		Map<String, String> stepMap = new HashMap<String, String>();
		stepMap.put(TIH_WORKFLOW_FEEDBACK_DESC, instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim());
		String fileids = "", filenames = "";
		for (AttachmentVO att : atts) {
			filenames += att.getFileName() + ",";
			fileids += att.getFileId() + ",";
		}
		if (!atts.isEmpty()) {
			fileids = fileids.substring(0, fileids.length() - 1);
			filenames = filenames.substring(0, filenames.length() - 1);
		}
		stepMap.put(TIH_WORKFLOW_FEEDBACK_FILEID, fileids);
		stepMap.put(TIH_WORKFLOW_FEEDBACK_FILENAME, filenames);
		// 删除附件
		if (null != wfInstancemstr && null != wfInstancemstr.getId()) {
			feedBackService.deleteFilemstr(wfInstancemstr.getId());
		}

		// 工厂发起流程
		if (DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy)) {
			if (feedBackService.getSuperadvisor(company.getId()) == null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "提交流程：", "操作失败，请设置当前人的上级主管."));
				return;
			}
		}
		String remarks = "";
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim() == null
				|| instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim().isEmpty()) {
			remarks = commonService.getValueByDictCatKey(type, browserLang.toString());
		} else {
			remarks = commonService.getValueByDictCatKey(type, browserLang.toString()) + ","
					+ instanceProperty.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim();
		}
		String sendBy = "";
		// 验证稽查表单信息
		Boolean isInspectionValidate = false;// 如果稽查无数据
		if (getInspectionBoolean()) {
			try {
				isInspectionValidate = inspectationService.isValidate(context, feedbackInspectionVO.getInspectation());
			} catch (Exception e1) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e1.getMessage(), ""));
				return;
			}
		}

		// 验证反避税表单信息
		Boolean isAntiValidate = false;
		if (getAntiAvoidanceBoolean()) {
			try {
				isAntiValidate = feedBackAntiService.vaildate(context, feedbackAntiVo.getAntiAvoidance(), feedbackAntiVo.getAntiResults());
			} catch (Exception e1) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e1.getMessage(), ""));
				return;
			}
		}

		// 流程名称
		String workflowName = "";
		try {
			if (DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy)) {// 工厂发起流程（通过流程名称判断）
				selectedMovies.add(company.getId().toString());
				workflowName = WORKFLOW_NAME;
			} else { // 集团发起流程
				workflowName = SIMPLE_WORKFLOE_NAME;
			}
			// 流程
			feedBackService.submitFeedBack(instanceMap, stepMap, workflowName, sendBy, remarks, selectedhoto.getCode(), selectedUrgent.getCode(),
					selectedMovies, wfInstancemstr, wfRemindVo);// 集团提交流程

			if (DictConsts.TIH_TAX_REQUESTBY_2.equals(requestBy)) {
				// 保存稽查信息
				if (getInspectionBoolean()) {
					if (isInspectionValidate) {
						InvsInspectation inspectation = feedbackInspectionVO.getInspectation();
						inspectation.setCompanymstrId(Long.valueOf(instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY)));
						inspectation.setWfId(feedBackService.getWfId());
						inspectation.setTaxTypes(inspectationService.taxTypeToString(taxTypes));
						inspectationService.saveOrUpdatedInDraft(inspectation, feedbackInspectionVO.getResult());
					}
				}
				// 保存反避税信息
				if (getAntiAvoidanceBoolean()) {
					if (isAntiValidate) {
						InvsAntiAvoidance avoidance = feedbackAntiVo.getAntiAvoidance();
						avoidance.setCompanymstrId(Long.valueOf(instanceMap.get(TIH_WORKFLOW_FEEDBACK_COMPANY)));
						avoidance.setWfId(feedBackService.getWfId());
						avoidance.setTaxTypes(feedBackAntiService.taxTypeToString(taxTypes));
						feedBackAntiService.saveOrUpdatedInDraft(avoidance, feedbackAntiVo.getAntiResults());
					}
				}
			}

			// 把附件信息保存到Filemstr表中
			saveFilemstr(feedBackService.getWfId());
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "提交流程：", e.getMessage()));
			return;
		}

		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		try {
			TaskRefreshHelper.refreshTask(context);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return;
		}
		inspection = FALSE;
		antiAvoidance = FALSE;
		wfInstancemstr = null;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "提交流程：", "操作成功，请耐心等待审核"));
	}

	/**
	 * <p>
	 * Description: 取消
	 * </p>
	 */
	public void cancelFeedBack() {
		initAddFeedBack();
		company = null;
		tabVew = 0;
		selectedhoto = null;
		selectedUrgent = null;
		type = null;
		stage = null;
		requestBy = null;
		selectedMovies.clear();
		movies.clear();
		draft = false;
		atts.clear();
		inspection = FALSE;
		antiAvoidance = FALSE;
		wfInstancemstr = null;
		lazyAtts = null;
		feedbackInspectionVO = new FeedbackInspectionVO();
		feedbackAntiVo = new FeedbackAntiVo();
		taxTypes = null;
		wfRemindVo = new WfRemindVo();
	}

	/**
	 * <p>
	 * Description: 流程步骤初始化
	 * </p>
	 */
	public void initDonextFeedBack() {
		taxTypes = null;
		List<WfStepmstr> subSteps = null;
		subSteps = this.sendReportService.getwfstepmatrs(wfInstancemstr.getId());
		chargedByCurrentuser = currentUser.getCurrentUsermstr().getAdAccount().equals(subSteps.get(subSteps.size() - 1).getChargedBy());
		finishedFeedBack = DictConsts.TIH_TAX_WORKFLOWSTATUS_3.equals(wfInstancemstr.getStatus());

		instanceProperty.clear();
		instanceProperty.put("currentDate", wfInstancemstr.getCreatedDatetime());
		instanceProperty.put("currentUser", getUsernameByAccount(wfInstancemstr.getCreatedBy()));
		instanceProperty.put("TIH_TAX_WORKFLOWIMPORTANCE", wfInstancemstr.getImportance());
		instanceProperty.put("TIH_TAX_WORKFLOWURGENCY", wfInstancemstr.getUrgency());
		type = getInstProValueByName(DictConsts.TIH_TAX_REQUESTFORM_5);// 查询项目类型
		stage = getInstProValueByName(type);// 查询工作阶段
		requestBy = getInstProValueByName(DictConsts.TIH_TAX_REQUESTBY);// 查询流程发起
		company = feedBackService.getCMM(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY)));
		instanceProperty.put("TIH_WORKFLOW_FEEDBACK_COMPANY", company.getStext());

		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type)) {
			inspection = TRUE;
			antiAvoidance = FALSE;
		} else if (DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			inspection = FALSE;
			antiAvoidance = TRUE;
		} else {
			inspection = FALSE;
			antiAvoidance = FALSE;
		}

		// 还原稽查信息
		feedbackInspectionVO = new FeedbackInspectionVO();
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type)) {
			List<InvsInspectation> list = inspectationService.getInspectionByWfId(wfInstancemstr.getId());
			if (list.size() > 0) {
				InvsInspectation inspectation = list.get(0);
				feedbackInspectionVO.setInspectation(inspectation);
				if (inspectation.getTaxTypes() != null && !"".equals(inspectation.getTaxTypes().trim())) {
					taxTypes = inspectationService.taxTypeToList(inspectation.getTaxTypes());
				}
				if (inspectation.getInvsInspectationResults().size() > 0) {
					feedbackInspectionVO.setResult(inspectation.getInvsInspectationResults());
				}
			}
		}

		// 欢迎反避税信息
		feedbackAntiVo = new FeedbackAntiVo();
		if (DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			List<InvsAntiAvoidance> list = feedBackAntiService.getAntiByWfId(wfInstancemstr.getId());
			if (list.size() > 0) {
				InvsAntiAvoidance avoidance = list.get(0);
				feedbackAntiVo.setAntiAvoidance(avoidance);
				feedbackAntiVo.setAntiResults(avoidance.getInvsAntiResults());
				if (avoidance.getTaxTypes() != null && !"".equals(avoidance.getTaxTypes().trim())) {
					taxTypes = inspectationService.taxTypeToList(avoidance.getTaxTypes());
				}
			}
		}

		stepProMap.clear();
		lazyAtts = null;
		steps.clear();
		// Description: 获取当中流程数组，为页面循环显示
		for (int i = 0; i < subSteps.size(); i++) {
			WfStepmstr step = subSteps.get(i);
			StepVO svo = new StepVO(getStepDesc(step), getTabTitle(step), getStepAttachments(step), isUpdateFilemstr(step.getCreatedBy()));
			// 附件是否选中
			selectAttachmentsDoFlow = attachmentSelectedCheckbox();
			svo.setStepAttachments(selectAttachmentsDoFlow);
			steps.add(svo);
		}

		processer = null;
		currentProcesser = null;

		try {
			if (this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_2)
					|| this.wfInstancemstr.getStatus().equals(DictConsts.TIH_TAX_WORKFLOWSTATUS_1)) {
				currentNodeName = feedBackService.getCurrentNodeName(wfInstancemstr.getNo());
			} else {
				currentNodeName = "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			currentNodeName = "";
		}
		atts.clear();
		findRemindVoByRquestform();
	}

	private Boolean isUpdateFilemstr(String createdBy) {
		if (currentUser.getCurrentUsermstr().getAdAccount().equals(createdBy)) {
			return true;
		}
		return false;
	}

	private AttachmentVO[] attachmentSelectedCheckbox() {
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type) || DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			fnIds = "'" + fnIds.replace(",", "','") + "'";
			List<String> selectFilemstrByFnId = feedBackService.selectFilemstrByFnId(fnIds);
			List<AttachmentVO> selectAtts = new ArrayList<AttachmentVO>();
			for (AttachmentVO att : atts) {
				if (selectFilemstrByFnId.contains(att.getFileId())) {
					selectAtts.add(att);
				}
			}
			AttachmentVO[] attachmentVO = new AttachmentVO[selectAtts.size()];
			selectAtts.toArray(attachmentVO);
			return attachmentVO;
		}
		return null;
	}

	/**
	 * <p>
	 * Description: 根据帐号查找用户名称
	 * </p>
	 * 
	 * @param account
	 * @return
	 */
	public String getUsernameByAccount(String account) {
		return feedBackService.getUsernameByAdaccount(account);
	}

	/**
	 * <p>
	 * Description: 查找实例属性值
	 * </p>
	 * 
	 * @param name
	 * @return
	 */
	public String getInstProValueByName(String name) {
		if (wfInstancemstr == null) {
			return "";
		}
		List<WfInstancemstrProperty> list = null;
		if (this.wfInstancemstr.getWfInstancemstrProperties() == null) {
			list = this.sendReportService.getwfips(this.wfInstancemstr.getId());
		} else {
			list = this.wfInstancemstr.getWfInstancemstrProperties();
		}
		for (WfInstancemstrProperty p : list) {
			if (p.getName().equals(name)) {
				return p.getValue();
			}
		}
		return "";
	}

	/**
	 * <p>
	 * Description: 获取步骤的附件
	 * </p>
	 * 
	 * @param step
	 * @return
	 */
	public LazyDataModel<AttachmentVO> getStepAttachments(WfStepmstr step) {
		String fids = "", fnames = "";
		for (WfStepmstrProperty p : step.getWfStepmstrProperties()) {
			if (p.getName().equals(TIH_WORKFLOW_FEEDBACK_FILEID)) {
				fids = p.getValue();
			}
			if (p.getName().equals(TIH_WORKFLOW_FEEDBACK_FILENAME)) {
				fnames = p.getValue();
			}
		}
		fnIds = fids;
		if ("".equals(fids)) {
			return null;
		}
		String[] idlist = fids.split(",");
		String[] nameList = fnames.split(",");
		List<AttachmentVO> list = new ArrayList<AttachmentVO>();
		for (int i = 0; i < idlist.length; i++) {
			list.add(new AttachmentVO((long) (i + 1), idlist[i], nameList[i]));
			atts.add(new AttachmentVO((long) (i + 1), idlist[i], nameList[i]));
		}
		return new PageModel<AttachmentVO>(list, false);
	}

	/**
	 * <p>
	 * Description: 获取步骤的描述信息
	 * </p>
	 * 
	 * @param step
	 * @return
	 */
	public String getStepDesc(WfStepmstr step) {
		if (step == null) {
			return "";
		}
		for (WfStepmstrProperty p : step.getWfStepmstrProperties()) {
			if (p.getName().equals(TIH_WORKFLOW_FEEDBACK_DESC)) {
				return p.getValue();
			}
		}
		return "";
	}

	/**
	 * <p>
	 * Description: 获取Tab的Title显示字段
	 * </p>
	 * 
	 * @param step
	 * @return
	 */
	public String getTabTitle(WfStepmstr step) {
		String userName = getUsernameByAccount(step.getCreatedBy());
		String deal = DictConsts.TIH_TAX_APPROACH_9.equals(step.getDealMethod()) ? "批复" : commonBean.getValueByDictCatKey(step.getDealMethod());
		return (step.getName().equals("提交") ? "提交人" : step.getName()) + " " + userName + " " + deal + " "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(step.getCreatedDatetime());
	}

	/**
	 * <p>
	 * Description: 取最后一步的Title信息
	 * </p>
	 * 
	 * @return
	 */
	public String getLastTabTitle() {
		if (wfInstancemstr == null) {
			return "";
		}
		List<WfStepmstr> subSteps = this.sendReportService.getwfstepmatrs(this.wfInstancemstr.getId());
		WfStepmstr step = subSteps.get(subSteps.size() - 1);
		return currentNodeName + " " + getUsernameByAccount(step.getChargedBy()) + " 正在处理";
	}

	/**
	 * <p>
	 * Description: 判断是否显示按钮
	 * </p>
	 * 
	 * @param status
	 * @return
	 */
	public boolean showButton(String status) {
		if (wfInstancemstr == null) {
			return false;
		}
		for (String[] strs : nodeStatus) {
			if (strs[1].equals(currentNodeName) && strs[2].equals(status)) {
				return true;
			}
		}
		return false;
	}

	public boolean groupButton(String status) {
		if ("SEND".equals(status) && DictConsts.TIH_TAX_REQUESTBY_1.equals(this.requestBy)) {
			return true;
		}
		return false;
	}

	public boolean finishButton(String status) {
		if (wfInstancemstr == null) {
			return false;
		}
		List<WfStepmstr> steps = null;
		if (this.wfInstancemstr.getWfStepmstrs() != null) {
			steps = this.wfInstancemstr.getWfStepmstrs();
		} else {
			steps = this.sendReportService.getwfstepmatrs(this.wfInstancemstr.getId());
		}
		for (WfStepmstr step : steps) {
			if ("公司处理岗".equals(step.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean processerButton(String status) {
		if (wfInstancemstr == null) {
			return false;
		}
		List<WfStepmstr> steps = null;
		if (this.wfInstancemstr.getWfStepmstrs() != null) {
			steps = this.wfInstancemstr.getWfStepmstrs();
		} else {
			steps = this.sendReportService.getwfstepmatrs(this.wfInstancemstr.getId());
		}
		String nodeName = steps.get(steps.size() - 1).getName();
		if ("Helper_FeedBackToGrouper".equals(status) && "集团处理岗".equals(nodeName)) {
			return true;
		} else if ("Helper_FeedBackToAssigner".equals(status) && "集团转签处理岗".equals(nodeName)) {
			return true;
		} else if ("Companyer_FeedBackToGrouper".equals(status) && "集团处理岗".equals(nodeName)) {
			return true;
		} else if ("Companyer_FeedBackToAssigner".equals(status) && "集团转签处理岗".equals(nodeName)) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Description: 流程下一步（通过流程单号启动流程）
	 * </p>
	 * 
	 * @param status
	 */
	public void doNextStep(String status) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!ValidateUtil.validateMaxlength(context, stepProMap.get("TIH_WORKFLOW_FEEDBACK_DESC"), "情况描述：", 330)) {
			return;
		}

		List<WfInstancemstrProperty> properties = null;
		if (this.wfInstancemstr.getWfInstancemstrProperties() != null) {
			properties = this.wfInstancemstr.getWfInstancemstrProperties();
		} else {
			properties = this.sendReportService.getwfips(this.getWfInstancemstr().getId());
		}
		for (WfInstancemstrProperty property : properties) {
			if (property.getName().equals(type)) {
				feedBackService.updateStage(property.getId(), stage);// 流程中更新项目阶段
			}
		}

		List<WfStepmstr> wfsteps = null;
		if (this.wfInstancemstr.getWfStepmstrs() != null) {
			wfsteps = this.wfInstancemstr.getWfStepmstrs();
		} else {
			wfsteps = this.sendReportService.getwfstepmatrs(this.getWfInstancemstr().getId());
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("status", status);// 保存路由信息
		String chargedOption = "";
		String chargedBy = "";
		boolean flag = false;
		if (status.equals("Visor_NotPass")) { // 主管不通过
			flag = true;
		} else if (status.equals("Visor_Pass")) { // 主管通过
			try {
				chargedOption = "grouper_param";
				chargedBy = feedBackService.getGrouper(SITUGP);
			} catch (Exception e1) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e1.getMessage(), ""));
				return;
			}
		} else if (status.equals("Grouper_Audit")) { // 集团处理岗批复
			chargedOption = "companyer_param";
			try {
				chargedBy = feedBackService.getCompanyer(SITUCP, getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY));// 公司处理岗
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
				return;
			}
		} else if (status.equals("Grouper_Assign")) {// 集团处理岗转签
			chargedOption = "assigner_param";
			if (currentProcesser == null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请选择转签处理人。", ""));
				return;
			}
			chargedBy = currentProcesser.getUsermstr().getAdAccount();
		} else if (status.equals("Grouper_Help")) { // 集团处理岗协助
			chargedOption = "helper_param";
			if (currentProcesser == null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请选择协助处理人。", ""));
				return;
			}
			chargedBy = currentProcesser.getUsermstr().getAdAccount();
		} else if (status.equals("Grouper_Pass")) { // 集团处理岗完成
			flag = true;
		}

		else if (status.equals("Assigner_Audit")) { // 集团转签岗批复
			chargedOption = "companyer_param";
			try {
				chargedBy = feedBackService.getCompanyer(SITUCP, getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY));
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
				return;
			}
		} else if (status.equals("Assigner_Help")) { // 集团转签岗协助
			chargedOption = "helper_param";
			if (currentProcesser == null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请选择协助处理人。", ""));
				return;
			}
			chargedBy = currentProcesser.getUsermstr().getAdAccount();
		} else if (status.equals("Assigner_Pass")) { // 集团转签岗完成
			flag = true;
		}

		else if (status.equals("Companyer_FeedBackToGrouper")) { // 公司处理岗反馈给集团处理岗
			chargedOption = "grouper_param";
			chargedBy = getLastCreatedBy();
		} else if (status.equals("Companyer_FeedBackToAssigner")) { // 公司处理岗反馈给转签岗
			chargedOption = "assigner_param";
			chargedBy = getLastCreatedBy();
		}

		else if (status.equals("Helper_FeedBackToGrouper")) { // 协助反馈给集团处理岗
			chargedOption = "grouper_param";
			chargedBy = getLastCreatedBy();
		} else if (status.equals("Helper_FeedBackToAssigner")) { // 协助反馈给集团转签岗
			chargedOption = "assigner_param";
			chargedBy = getLastCreatedBy();
		}

		// 集团情况反馈
		if (status.equals("SEND")) { // 公司处理岗提交到主管
			chargedOption = WorkflowConsts.PASSWORD_PARAM_SUPERVISOR;
			if (feedBackService.getSuperadvisor(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY))) == null
					|| feedBackService.getSuperadvisor(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY))).isEmpty()) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "上级主管不能为空。", ""));
				return;
			}
			chargedBy = feedBackService.getSuperadvisor(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY)));
		} else if (status.equals("NOPASS")) { // 主管不通过
			chargedOption = WorkflowConsts.PASSWORD_PARAM_COMPANY;
			try {
				chargedBy = feedBackService.getCompanyer(SITUCP, getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY));
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
				return;
			}
		} else if (status.equals("Supervisor_PASS")) { // 主管通过
			chargedOption = WorkflowConsts.PASSWORD_PARAM_AUDITOR;
			chargedBy = wfInstancemstr.getCreatedBy();
		} else if (status.equals("Auditor_PASS")) { // 完成
			flag = true;
		} else if (status.equals("Auditor_NOPASS")) { // 批复
			chargedOption = WorkflowConsts.PASSWORD_PARAM_COMPANY;
			try {
				chargedBy = feedBackService.getCompanyer(SITUCP, getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY));
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
				return;
			}
		}

		String remarks = "";
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (stepProMap.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim() == null
				|| stepProMap.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim().isEmpty()) {
			remarks = commonService.getValueByDictCatKey(type, browserLang.toString());
		} else {
			remarks = commonService.getValueByDictCatKey(type, browserLang.toString()) + ","
					+ stepProMap.get("TIH_WORKFLOW_FEEDBACK_DESC").toString().trim();
		}
		if (remarks.length() > 600) { // 控制remarks的长度，数据库中只有2000个字符
			remarks = remarks.substring(0, 600);
		}
		if (!chargedOption.equals("")) {
			paramMap.put(chargedOption, new String[] { "uid=" + chargedBy + "," + dnStr });
		}
		WfStepmstr step = new WfStepmstr();
		step.setWfInstancemstr(wfInstancemstr);
		step.setUpdatedBy(currentUser.getCurrentUsermstr().getAdAccount());
		step.setCreatedBy(currentUser.getCurrentUsermstr().getAdAccount());
		step.setDefunctInd("N");
		step.setUpdatedDatetime(new Date());
		step.setCreatedDatetime(new Date());
		step.setDealMethod(getDealMethod(status));// 保存按钮信息（如：提交、通过、不通过等等）
		step.setChargedBy(chargedBy);
		step.setCreatedDatetime(new Date());
		step.setCompletedDatetime(new Date());
		step.setFromStepId(wfsteps.get(wfsteps.size() - 1).getId());

		Map<String, String> stepMap = new HashMap<String, String>();
		stepMap.put(TIH_WORKFLOW_FEEDBACK_DESC, (String) stepProMap.get("TIH_WORKFLOW_FEEDBACK_DESC"));
		String fileids = "", filenames = "";
		if (!atts.isEmpty()) {
			for (AttachmentVO att : atts) {
				filenames += att.getFileName() + ",";
				fileids += att.getFileId() + ",";
			}
			fileids = fileids.substring(0, fileids.length() - 1);
			filenames = filenames.substring(0, filenames.length() - 1);
		}
		stepMap.put(TIH_WORKFLOW_FEEDBACK_FILEID, fileids);
		stepMap.put(TIH_WORKFLOW_FEEDBACK_FILENAME, filenames);
		String workflownumber = wfInstancemstr.getNo();

		// 验证稽查表单信息
		Boolean isInspectionValidate = false;// 如果稽查无数据
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type)) {
			try {
				isInspectionValidate = inspectationService.isValidate(context, feedbackInspectionVO.getInspectation());
			} catch (Exception e1) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e1.getMessage(), ""));
				return;
			}
		}

		// 验证反避税表单信息
		Boolean isAntiValidate = false;
		if (DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			try {
				isAntiValidate = feedBackAntiService.vaildate(context, feedbackAntiVo.getAntiAvoidance(), feedbackAntiVo.getAntiResults());
			} catch (Exception e) {
				if (e.getMessage() != null) {
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
					return;
				}
			}
		}

		try {
			feedBackService.doWorkflowNext(paramMap, step, stepMap, workflownumber, remarks);// 根据流程单号启动下一步流程
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ".."));
			return;
		}
		if (flag) {
			wfInstancemstr.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
			feedBackService.doFinish(wfInstancemstr);
		}

		// 新增或编辑稽查信息
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type)) {
			if (isInspectionValidate) {
				InvsInspectation inspectation = feedbackInspectionVO.getInspectation();
				inspectation.setCompanymstrId(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY).toString().trim()));
				inspectation.setWfId(wfInstancemstr.getId());
				inspectation.setTaxTypes(inspectationService.taxTypeToString(taxTypes));
				try {
					inspectationService.saveOrUpdateInspectation(inspectation, feedbackInspectionVO.getResult());
				} catch (Exception e) {
					logger.info("操作稽查信息失败");
					logger.error(e.getMessage(), e);
				}
			}
		}

		// 新增或编辑反避税信息
		if (DictConsts.TIH_TAX_REQUESTFORM_5_4.equals(type)) {
			if (isAntiValidate) {
				InvsAntiAvoidance avoidance = feedbackAntiVo.getAntiAvoidance();
				avoidance.setCompanymstrId(Long.valueOf(getInstProValueByName(TIH_WORKFLOW_FEEDBACK_COMPANY)));
				avoidance.setWfId(wfInstancemstr.getId());
				avoidance.setTaxTypes(inspectationService.taxTypeToString(taxTypes));
				try {
					feedBackAntiService.saveOrUpdateWF(avoidance, feedbackAntiVo.getAntiResults());
				} catch (Exception e) {
					logger.info("操作反避税信息失败");
					logger.error(e.getMessage(), e);
				}
			}
		}

		// 保存附件信息
		saveFilemstr(wfInstancemstr.getId());

		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		try {
			TaskRefreshHelper.refreshTask(context);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return;
		}
		stage = null;
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "流程执行成功", ""));
	}

	/**
	 * <p>
	 * Description: 取最后一步处理人，反馈用
	 * </p>
	 * 
	 * @return
	 */
	public String getLastCreatedBy() {
		List<WfStepmstr> steps = null;
		if (this.wfInstancemstr.getWfStepmstrs() != null) {
			steps = this.wfInstancemstr.getWfStepmstrs();
		} else {
			steps = this.sendReportService.getwfstepmatrs(this.wfInstancemstr.getId());
		}
		return steps.get(steps.size() - 1).getCreatedBy();
	}

	/**
	 * <p>
	 * Description: 获取DealMethod
	 * </p>
	 * 
	 * @param status
	 * @return
	 */
	public String getDealMethod(String status) {
		for (String[] strs : nodeStatus) {
			if (status.equals(strs[2])) {
				return strs[3];
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Description: 取用户公共组件回调方法
	 * </p>
	 */
	public void processUser() {
		setCurrentProcesser(userCommonBean.getSelectedUsermstrVo());
		processer = getUsernameByAccount(currentProcesser.getUsermstr().getAdAccount());
	}

	// ######################################## 稽查 ################
	/**
	 * <p>
	 * Description:给稽查结果表添加新的一行
	 * </p>
	 */
	public void addInspectationResult() {
		List<InvsInspectationResult> results = feedbackInspectionVO.getResult();
		feedbackInspectionVO.setResult(null);
		InvsInspectationResult result = new InvsInspectationResult();
		result.setCreatedDatetime(new Date());
		feedbackInspectionVO.getResult().add(result);
		feedbackInspectionVO.getResult().addAll(results);
	}

	/**
	 * <p>
	 * Description:删除一行(稽查)
	 * </p>
	 */
	public void deleteInspectionResult() {
		int j = 0;
		List<InvsInspectationResult> results = feedbackInspectionVO.getResult();
		for (int i = 0; i < results.size(); i++) {
			if (results.get(i).getCreatedDatetime() == deleteInspectationResult.getCreatedDatetime()) {
				j = i;
			}
		}
		if (deleteInspectationResult.getId() != null) {
			inspectationService.removeResult(deleteInspectationResult);
		}
		feedbackInspectionVO.getResult().remove(j);
	}

	// ############################# 反避税 #################
	/**
	 * <p>
	 * Description:给反避税结果表添加一行
	 * </p>
	 */
	public void addAntiResult() {
		List<InvsAntiResult> list = feedbackAntiVo.getAntiResults();
		feedbackAntiVo.setAntiResults(null);
		InvsAntiResult antiResult = new InvsAntiResult();
		antiResult.setCreatedDatetime(new Date());
		feedbackAntiVo.getAntiResults().add(antiResult);
		feedbackAntiVo.getAntiResults().addAll(list);
	}

	/**
	 * <p>
	 * Description:删除一行(反避税)
	 * </p>
	 */
	public void deleteAntiResult() {
		int j = 0;
		List<InvsAntiResult> antiResults = feedbackAntiVo.getAntiResults();
		for (int i = 0; i < antiResults.size(); i++) {
			if (antiResults.get(i).getCreatedDatetime() == deleteAntiResult.getCreatedDatetime()) {
				j = i;
			}
		}
		if (deleteAntiResult.getId() != null) {
			feedBackAntiService.removeResult(deleteAntiResult);
		}
		antiResults.remove(j);
	}

	// Getter & Setter
	public Map<String, Object> getInstanceProperty() {
		return instanceProperty;
	}

	public void setInstanceProperty(Map<String, Object> instanceProperty) {
		this.instanceProperty = instanceProperty;
	}

	public CurrentUserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CurrentUserBean currentUser) {
		this.currentUser = currentUser;
	}

	public List<AttachmentVO> getAtts() {
		return atts;
	}

	public void setAtts(List<AttachmentVO> atts) {
		this.atts = atts;
	}

	public LazyDataModel<AttachmentVO> getLazyAtts() {
		return lazyAtts;
	}

	public void setLazyAtts(LazyDataModel<AttachmentVO> lazyAtts) {
		this.lazyAtts = lazyAtts;
	}

	public CommonBean getCommonBean() {
		return commonBean;
	}

	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}

	public WfInstancemstr getWfInstancemstr() {
		return wfInstancemstr;
	}

	public void setWfInstancemstr(WfInstancemstr wfInstancemstr) {
		this.wfInstancemstr = wfInstancemstr;
	}

	public boolean isChargedByCurrentuser() {
		return chargedByCurrentuser;
	}

	public void setChargedByCurrentuser(boolean chargedByCurrentuser) {
		this.chargedByCurrentuser = chargedByCurrentuser;
	}

	public String getUpdateAttachmentId() {
		return updateAttachmentId;
	}

	public void setUpdateAttachmentId(String updateAttachmentId) {
		this.updateAttachmentId = updateAttachmentId;
	}

	public Map<String, Object> getStepProMap() {
		return stepProMap;
	}

	public void setStepProMap(Map<String, Object> stepProMap) {
		this.stepProMap = stepProMap;
	}

	public List<StepVO> getSteps() {
		return steps;
	}

	public void setSteps(List<StepVO> steps) {
		this.steps = steps;
	}

	public UserCommonBean getUserCommonBean() {
		return userCommonBean;
	}

	public void setUserCommonBean(UserCommonBean userCommonBean) {
		this.userCommonBean = userCommonBean;
	}

	public UsermstrVo getCurrentProcesser() {
		return currentProcesser;
	}

	public void setCurrentProcesser(UsermstrVo currentProcesser) {
		this.currentProcesser = currentProcesser;
	}

	public String getProcesser() {
		return processer;
	}

	public void setProcesser(String processer) {
		this.processer = processer;
	}

	public boolean isFinishedFeedBack() {
		return finishedFeedBack;
	}

	public void setFinishedFeedBack(boolean finishedFeedBack) {
		this.finishedFeedBack = finishedFeedBack;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public List<String> getSelectedMovies() {
		return selectedMovies;
	}

	public void setSelectedMovies(List<String> selectedMovies) {
		this.selectedMovies = selectedMovies;
	}

	public Map<String, String> getMovies() {
		return movies;
	}

	public void setMovies(Map<String, String> movies) {
		this.movies = movies;
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}

	public String getAntiAvoidance() {
		return antiAvoidance;
	}

	public void setAntiAvoidance(String antiAvoidance) {
		this.antiAvoidance = antiAvoidance;
	}

	public int getTabVew() {
		return tabVew;
	}

	public void setTabVew(int tabVew) {
		this.tabVew = tabVew;
	}

	public FeedbackInspectionVO getFeedbackInspectionVO() {
		if (feedbackInspectionVO == null) {
			feedbackInspectionVO = new FeedbackInspectionVO();
		}
		return feedbackInspectionVO;
	}

	public void setFeedbackInspectionVO(FeedbackInspectionVO feedbackInspectionVO) {
		this.feedbackInspectionVO = feedbackInspectionVO;
	}

	public List<String> getTaxTypes() {
		return taxTypes;
	}

	public void setTaxTypes(List<String> taxTypes) {
		this.taxTypes = taxTypes;
	}

	public InvsInspectationResult getDeleteInspectationResult() {
		return deleteInspectationResult;
	}

	public void setDeleteInspectationResult(InvsInspectationResult deleteInspectationResult) {
		this.deleteInspectationResult = deleteInspectationResult;
	}

	public FeedbackAntiVo getFeedbackAntiVo() {
		return feedbackAntiVo;
	}

	public void setFeedbackAntiVo(FeedbackAntiVo feedbackAntiVo) {
		this.feedbackAntiVo = feedbackAntiVo;
	}

	public InvsAntiResult getDeleteAntiResult() {
		return deleteAntiResult;
	}

	public void setDeleteAntiResult(InvsAntiResult deleteAntiResult) {
		this.deleteAntiResult = deleteAntiResult;
	}

	public AttachmentVO[] getSelectAttachment() {
		return selectAttachment;
	}

	public void setSelectAttachment(AttachmentVO[] selectAttachment) {
		this.selectAttachment = selectAttachment;
	}

	public String getFnIds() {
		return fnIds;
	}

	public void setFnIds(String fnIds) {
		this.fnIds = fnIds;
	}

	public AttachmentVO[] getSelectAttachmentsDoFlow() {
		return selectAttachmentsDoFlow;
	}

	public void setSelectAttachmentsDoFlow(AttachmentVO[] selectAttachmentsDoFlow) {
		this.selectAttachmentsDoFlow = selectAttachmentsDoFlow;
	}

	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}

	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}

}
