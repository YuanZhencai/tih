package com.wcs.scheduler.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.util.DateUtils;
import com.wcs.scheduler.vo.JobInfoVo;
import com.wcs.scheduler.vo.RemindVo;
import com.wcs.scheduler.vo.TimeoutConfigVo;
import com.wcs.tih.model.JobInfo;
import com.wcs.tih.model.WfTimeoutConfig;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean(name = "timeoutEmailBean")
@ViewScoped
public class TimeoutEmailBean implements java.io.Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(TimeoutEmailBean.class);

	private TreeNode root = null;
	private JobInfoVo jobInfoVo = new JobInfoVo();
	private JobInfoVo selectedJobInfoVo = null;
	private RemindVo remindVo = null;
	private RemindVo selectedRemindVo = null;
	private List<RemindVo> remindVos = null;
	private String remindOprateType = null;
	private String jobOprateType = null;
	private String configOprateType = null;

	private List<TimeoutConfigVo> configVos = null;
	private List<TimeoutConfigVo> updateConfigVos = null;

	private TimeoutConfigVo configVo = new TimeoutConfigVo();
	private TimeoutConfigVo selectedConfigVo = null;

	private List<SelectItem> wfTypes = null;
	private List<SelectItem> requestForms = null;
	private Map<String, List<SelectItem>> requestFormsMap = new HashMap<String, List<SelectItem>>();

	@EJB
	private TimeoutEmailService timeoutEmailService;

	public TimeoutEmailBean() {
	}

	@PostConstruct
	public void init() {
		initWorkflowItems();
		getTimeoutEmailTree();
	}

	public void getTimeoutEmailTree() {
		root = timeoutEmailService.createTimeoutEmailTree();
	}

	public void initWorkflowItems() {
		wfTypes = new ArrayList<SelectItem>();
		wfTypes.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_1, "上传文档流程"));
		wfTypes.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_2, "检入文档流程"));
		wfTypes.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_3, "提问流程"));
		wfTypes.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_4, "报送报表流程"));
		wfTypes.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_5, "情况反馈流程(工厂)"));
		wfTypes.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_6, "情况反馈流程(集团)"));

		List<SelectItem> uploadRequestForms = new ArrayList<SelectItem>();
		uploadRequestForms.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_1, "上传文档申请单"));
		requestFormsMap.put(DictConsts.TIH_TAX_REQUESTFORM_1, uploadRequestForms);
		List<SelectItem> checkinRequestForms = new ArrayList<SelectItem>();
		checkinRequestForms.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_2, "检入文档申请单"));
		requestFormsMap.put(DictConsts.TIH_TAX_REQUESTFORM_2, checkinRequestForms);
		List<SelectItem> questionRequestForms = new ArrayList<SelectItem>();
		questionRequestForms.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_3, "提问申请单"));
		requestFormsMap.put(DictConsts.TIH_TAX_REQUESTFORM_3, questionRequestForms);
		List<SelectItem> reportRequestForms = new ArrayList<SelectItem>();
		reportRequestForms.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_4, "报送报表申请单"));
		requestFormsMap.put(DictConsts.TIH_TAX_REQUESTFORM_4, reportRequestForms);
		List<SelectItem> feedbackForms1 = new ArrayList<SelectItem>();
		feedbackForms1.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_5_1, "调研与分析"));
		feedbackForms1.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_5_2, "税务稽（检）查"));
		feedbackForms1.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_5_3, "税务审批与备案"));
		feedbackForms1.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_5_4, "反避税管理"));

		requestFormsMap.put(DictConsts.TIH_TAX_REQUESTFORM_5, feedbackForms1);

		List<SelectItem> feedbackForms2 = new ArrayList<SelectItem>();
		feedbackForms2.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_6_1, "调研与分析"));
		feedbackForms2.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_6_2, "税务稽（检）查"));
		feedbackForms2.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_6_3, "税务审批与备案"));
		feedbackForms2.add(new SelectItem(DictConsts.TIH_TAX_REQUESTFORM_6_4, "反避税管理"));
		requestFormsMap.put(DictConsts.TIH_TAX_REQUESTFORM_6, feedbackForms2);

	}

	public void find() {
		List<NotificationVo> list = timeoutEmailService.findTimeoutNoticeVosByJobInfo(null);
		logger.info("[list]" + list.size());
		for (NotificationVo notificationVo : list) {
			logger.info("[content]" + notificationVo.getContent());
		}
	}

	public void initAddTimeoutConfig() {
		configVo = new TimeoutConfigVo();
		selectedConfigVo = null;
	}

	public void addTimeoutConfig() {
		if (!hasConfigVo(configVo)) {
			if (timeoutEmailService.validateConfigVo(configVo)) {
				configVos.add(configVo);
				updateConfigVos.add(configVo);
				handleDialogByWidgetVar("configDialogVar", "close");
			}
		} else {
			showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "已存在的申请单类型", ""));
		}
	}

	public void findTimeoutConfigInfo() {
		try {
			BeanUtils.copyProperties(configVo, selectedConfigVo);
			logger.info("[selectedConfigVo]" + selectedConfigVo.getConfig());
		} catch (Exception e) {
			logger.error("查找超时配置信息失败", e);
		}
	}

	public void editTimeoutConfig() {
		if (!hasConfigVo(configVo)) {
			if (timeoutEmailService.validateConfigVo(configVo)) {
				try {
					BeanUtils.copyProperties(selectedConfigVo, configVo);
					handleDialogByWidgetVar("configDialogVar", "close");
				} catch (Exception e) {
					showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "编辑失败：", "请重新操作。"));
					logger.error(e.getMessage(), e);
				}
			}
		} else {
			showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "已存在的申请单类型", ""));
		}
	}

	public void deleteTimeoutConfig() {
		selectedConfigVo.setDefunctInd("Y");
		configVos.remove(selectedConfigVo);
	}

	public void initAddTimeoutJob() {
		jobInfoVo = new JobInfoVo();
		configVos = new ArrayList<TimeoutConfigVo>();
		updateConfigVos = new ArrayList<TimeoutConfigVo>();
	}

	public void createTimeoutJob() {
		try {
			if (!validateJobInfoVo(jobInfoVo)) {
				return;
			}
			timeoutEmailService.createTimeoutEmailJob(jobInfoVo, configVos);
			handleDialogByWidgetVar("timeoutEmailDialogVar", "close");
			showMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "创建成功", ""));
		} catch (Exception e) {
			showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "创建定时器失败：", "填写的数据错误。"));
			logger.error("创建定时器失败", e);
		}
		getTimeoutEmailTree();
	}

	public void editTimeoutJob() {
		try {
			if (!validateJobInfoVo(jobInfoVo)) {
				return;
			}
			timeoutEmailService.updateTimeoutEmailJob(jobInfoVo, updateConfigVos);
			handleDialogByWidgetVar("timeoutEmailDialogVar", "close");
			showMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "更新成功", ""));
		} catch (Exception e) {
			showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "更新定时器失败：", "填写的数据错误。"));
			logger.error("更新定时器失败", e);
		}
		getTimeoutEmailTree();
	}

	public void getTimeoutEmailJobInfo() {
		jobInfoVo = new JobInfoVo();
		configVos = new ArrayList<TimeoutConfigVo>();
		updateConfigVos = new ArrayList<TimeoutConfigVo>();
		try {
			JobInfo jobInfo = timeoutEmailService.findTimeoutEmailTimer(selectedJobInfoVo.getJobInfo());
			jobInfoVo = timeoutEmailService.getJobInfoVoByJobInfo(jobInfo);
			List<WfTimeoutConfig> configs = timeoutEmailService.findConfigsByJobInfo(jobInfo);
			for (WfTimeoutConfig config : configs) {
				TimeoutConfigVo cv = timeoutEmailService.getConfigVoByConfig(config);
				configVos.add(cv);
				updateConfigVos.add(cv);
			}
		} catch (Exception e) {
			logger.error("查询定时器信息失败", e);
		}
	}

	public void initAddTimeoutEmail() {
		jobInfoVo = new JobInfoVo();
		remindVos = new ArrayList<RemindVo>();
	}

	public void deleteTimeoutJob() {
		try {
			timeoutEmailService.deleteTimeoutEmailJob(selectedJobInfoVo);
			showMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "删除成功", ""));
		} catch (Exception e) {
			showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除失败", ""));
			logger.error("删除失败", e);
		}
		getTimeoutEmailTree();
	}

	public void handleDialogByWidgetVar(String widgetVar, String option) {
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("widgetVar", widgetVar);
		context.addCallbackParam("option", option);
	}

	public void showMessage(FacesMessage message) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, message);
	}

	public boolean validateJobInfoVo(JobInfoVo jobInfoVo) {
		boolean validate = true;
		if (jobInfoVo == null) {
			return false;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		if (!ValidateUtil.validateRequired(context, jobInfoVo.getJobName(), "名称：")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequired(context, jobInfoVo.getStartDate(), "开始时间：")) {
			validate = false;
		}
		if (!ValidateUtil.validateStartTimeGTEndTime(context, jobInfoVo.getStartDate(), jobInfoVo.getEndDate(),"结束时间：", "不能早于开始时间。")) {
			validate = false;
		}
		Date endDate = jobInfoVo.getEndDate();
		
		if(endDate != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Integer.valueOf(jobInfoVo.getHour()), Integer.valueOf(jobInfoVo.getMinute()), 0);
			if(calendar.getTime().before(new Date())){
				showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "结束时间：", "不能早于当前时间。"));
				validate = false;
			} else if(jobInfoVo.getNextTimeout() != null){
				if(calendar.getTime().before(jobInfoVo.getNextTimeout())){
					showMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "结束时间：", "不能早于下一次执行时间。"));
					validate = false;
				}
			}
		}
		
		
		return validate;
	}

	public boolean hasConfigVo(TimeoutConfigVo cv) {
		for (TimeoutConfigVo cVo : configVos) {
			if (!cVo.equals(selectedConfigVo)) {
				String wfType = cVo.getWfType();
				String timeoutType = cVo.getTimeoutType();
				if (wfType.equals(cv.getWfType()) && timeoutType.equals(cv.getTimeoutType())) {
					for (String cvs : cVo.getRequestforms()) {
						for (String r : cv.getRequestforms()) {
							if (cvs.equals(r)) {
								return true;
							}
						}
					}
				}
			}
		}
		return timeoutEmailService.hasConfig(cv);
	}



	// ================================ Get && Set =======================================//
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public JobInfoVo getJobInfoVo() {
		return jobInfoVo;
	}

	public void setJobInfoVo(JobInfoVo jobInfoVo) {
		this.jobInfoVo = jobInfoVo;
	}

	public List<String> getYears() {
		List<String> years = new ArrayList<String>();
		int year = Integer.parseInt(DateUtils.getYear());
		for (int i = year; i < 18 + year; i++) {
			years.add("" + i);
		}
		return years;
	}

	public List<String> getDays() {
		List<String> days = new ArrayList<String>();
		for (int i = 1; i < 32; i++) {
			days.add("" + i);
		}
		return days;
	}

	public List<String> getHours() {
		List<String> hours = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			hours.add("" + i);
		}
		return hours;
	}

	public List<String> getMinutesOrSeconds() {
		List<String> minutesOrSeconds = new ArrayList<String>();
		for (int i = 0; i < 61; i++) {
			minutesOrSeconds.add("" + i);
		}
		return minutesOrSeconds;
	}

	public List<RemindVo> getRemindVos() {
		return remindVos;
	}

	public void setRemindVos(List<RemindVo> remindVos) {
		this.remindVos = remindVos;
	}

	public RemindVo getRemindVo() {
		return remindVo;
	}

	public void setRemindVo(RemindVo remindVo) {
		this.remindVo = remindVo;
	}

	public RemindVo getSelectedRemindVo() {
		return selectedRemindVo;
	}

	public void setSelectedRemindVo(RemindVo selectedRemindVo) {
		this.selectedRemindVo = selectedRemindVo;
	}

	public String getRemindOprateType() {
		return remindOprateType;
	}

	public void setRemindOprateType(String remindOprateType) {
		this.remindOprateType = remindOprateType;
	}

	public String getJobOprateType() {
		return jobOprateType;
	}

	public void setJobOprateType(String jobOprateType) {
		this.jobOprateType = jobOprateType;
	}

	public JobInfoVo getSelectedJobInfoVo() {
		return selectedJobInfoVo;
	}

	public void setSelectedJobInfoVo(JobInfoVo selectedJobInfoVo) {
		this.selectedJobInfoVo = selectedJobInfoVo;
	}

	public List<TimeoutConfigVo> getConfigVos() {
		return configVos;
	}

	public void setConfigVos(List<TimeoutConfigVo> configVos) {
		this.configVos = configVos;
	}

	public TimeoutConfigVo getConfigVo() {
		return configVo;
	}

	public void setConfigVo(TimeoutConfigVo configVo) {
		this.configVo = configVo;
	}

	public List<SelectItem> getWfTypes() {
		return wfTypes;
	}

	public void setWfTypes(List<SelectItem> wfTypes) {
		this.wfTypes = wfTypes;
	}

	public List<SelectItem> getRequestForms() {
		if (configVo != null) {
			requestForms = requestFormsMap.get(configVo.getWfType());
		}
		return requestForms;
	}

	public void setRequestForms(List<SelectItem> requestForms) {
		this.requestForms = requestForms;
	}

	public Map<String, List<SelectItem>> getRequestFormsMap() {
		return requestFormsMap;
	}

	public void setRequestFormsMap(Map<String, List<SelectItem>> requestFormsMap) {
		this.requestFormsMap = requestFormsMap;
	}

	public String getConfigOprateType() {
		return configOprateType;
	}

	public void setConfigOprateType(String configOprateType) {
		this.configOprateType = configOprateType;
	}

	public TimeoutConfigVo getSelectedConfigVo() {
		return selectedConfigVo;
	}

	public void setSelectedConfigVo(TimeoutConfigVo selectedConfigVo) {
		this.selectedConfigVo = selectedConfigVo;
	}

}
