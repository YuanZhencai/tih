package com.wcs.scheduler.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.util.DateUtils;
import com.wcs.scheduler.vo.JobInfoVo;
import com.wcs.scheduler.vo.TimeoutConfigVo;
import com.wcs.scheduler.vo.TimerTreeVo;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.JobInfo;
import com.wcs.tih.model.PositionTimeoutRemind;
import com.wcs.tih.model.WfTimeoutConfig;
import com.wcs.tih.model.WfTimeoutRemind;
import com.wcs.tih.util.ValidateUtil;

@Stateless
public class TimeoutEmailService {

	private static Logger logger = LoggerFactory.getLogger(TimeoutEmailService.class);

	private static final String TIMEOUT_EMAIL_JOBCLASS_NAME = "java:module/BatchJobTimeoutEmail";
	private static final ResourceBundle REGEX_BUNDLE = ResourceBundle.getBundle("regex");

	@PersistenceContext
	private EntityManager em;

	@EJB
	private JobService jobService;
	@EJB
	private LoginService loginService;
	@EJB
	private CommonService commonService;

	private Map<String, String> workflowTypeMap = null;

	@PostConstruct
	public void init() {
		workflowTypeMap = commonService.getDictMapByCat(DictConsts.TIH_TAX_REQUESTFORM, "zh_CN");
	}
	
	
	public void createTimeoutEmailJob(JobInfoVo jobInfoVo, List<TimeoutConfigVo> configVos) throws DuplicateKeyException {

		// 1.create jobInfo
		JobInfo jobInfo = saveEmailJob(jobInfoVo, configVos);
		// 2.create timeout email timer
		JobInfo createJob = jobService.createJob(jobInfo);
		jobInfo.setNextTimeout(createJob.getNextTimeout());
		// 3.update nextTimeout
		updateJob(jobInfo);
	}
	
	public void updateTimeoutEmailJob(JobInfoVo jobInfoVo, List<TimeoutConfigVo> configVos) throws DuplicateKeyException {
		// 1.update jobInfo
		JobInfo jobInfo = updateEmailJob(jobInfoVo, configVos);
		// 2.update timeout email timer
		JobInfo updateJob = jobService.updateJob(jobInfo);
		jobInfo.setNextTimeout(updateJob.getNextTimeout());
		// 3.update timeout jobInfo
		updateJob(jobInfo);
	}
	
	public void deleteTimeoutEmailJob(JobInfoVo jobInfoVo) throws DuplicateKeyException {
		JobInfo jobInfo = jobInfoVo.getJobInfo();
		// 1.delete job
		deleteEmailJob(jobInfo);
		// 2. delete timer
		jobService.deleteJob(jobInfo);
	}

	public JobInfo saveEmailJob(JobInfoVo jobInfoVo, List<TimeoutConfigVo> configVos) {
		// 1.create jobInfo
		JobInfo jobInfo = getJobInfoByJobInfoVo(jobInfoVo);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
		String jobId = sdf.format(new Date()).replace(" ", "");
		jobInfo.setJobId(jobId);
		// set default value
		jobInfo.setYear("*");
		jobInfo.setMonth("*");
		jobInfo.setDayOfMonth("*");
		jobInfo.setDayOfWeek("*");
		jobInfo.setSecond("0");

		em.persist(jobInfo);
		for (TimeoutConfigVo configVo : configVos) {
			// 2.create config
			WfTimeoutConfig config = getConfigByConfigVo(configVo);
			config.setJobId(jobInfo.getJobId());
			em.persist(config);
		}
		return jobInfo;
	}

	public JobInfo updateEmailJob(JobInfoVo jobInfoVo, List<TimeoutConfigVo> configVos) {
		// 1.get jobInfo
		JobInfo jobInfo = getJobInfoByJobInfoVo(jobInfoVo);
		for (TimeoutConfigVo configVo : configVos) {
			// 2.merge config
			WfTimeoutConfig config = getConfigByConfigVo(configVo);
			config.setJobId(jobInfo.getJobId());
			if (config.getId() != null) {
				config.setDefunctInd(configVo.getDefunctInd());
				em.merge(config);
			} else if (!"Y".equals(configVo.getDefunctInd())) {
				em.persist(config);
			}
		}
		return jobInfo;
	}

	public void updateJob(JobInfo jobInfo) {
		em.merge(jobInfo);
	}

	public void deleteEmailJob(JobInfo jobInfo) {
		// 1.delete reminds by job
		deleteRemindsByJobInfo(jobInfo);
		// 2.remove jobInfo
		em.remove(em.find(JobInfo.class, jobInfo.getId()));
	}

	public JobInfo findTimeoutEmailTimer(JobInfo jobInfo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select job from JobInfo job");
		jpql.append(" where job.jobClassName = '" + TIMEOUT_EMAIL_JOBCLASS_NAME + "'");
		jpql.append(" and job.defunctInd = 'N'");
		jpql.append(" and job.jobId = '" + jobInfo.getJobId() + "'");
		return (JobInfo) em.createQuery(jpql.toString()).getSingleResult();
	}

	public TreeNode createTimeoutEmailTree() {
		// 1.find Timeout JobInfos
		TreeNode rootNode = new DefaultTreeNode("root", null);
		try {

			List<JobInfo> timeoutEmailJobInfos = findTimeoutEmailJobInfos();
			TimerTreeVo timerTreeVo = null;
			for (JobInfo jobInfo : timeoutEmailJobInfos) {
				timerTreeVo = new TimerTreeVo();
				timerTreeVo.setJobInfoVo(getJobInfoVoByJobInfo(jobInfo));
				timerTreeVo.setType("job");
				// jobNode
				new DefaultTreeNode("job", timerTreeVo, rootNode);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return rootNode;
	}

	@SuppressWarnings("unchecked")
	public List<JobInfo> findTimeoutEmailJobInfos() {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select job from JobInfo job");
		jpql.append(" where job.jobClassName = '" + TIMEOUT_EMAIL_JOBCLASS_NAME + "'");
		jpql.append(" and job.defunctInd = 'N'");
		return em.createQuery(jpql.toString()).getResultList();
	}

	public List<WfTimeoutConfig> findConfigsByJobInfo(JobInfo jobInfo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select config from WfTimeoutConfig config");
		jpql.append(" left join fetch config.positionTimeoutReminds");
		jpql.append(" where config.jobId = '").append(jobInfo.getJobId()).append("'");
		jpql.append(" and config.defunctInd = 'N'");
		jpql.append(" order by config.wfType");
		return em.createQuery(jpql.toString()).getResultList();
	}

	public void deleteRemindsByJobInfo(JobInfo jobInfo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" update WfTimeoutConfig c set c.defunctInd = 'Y'");
		jpql.append(" where c.jobId = '" + jobInfo.getJobId() + "'");
		em.createQuery(jpql.toString()).executeUpdate();
	}

	public JobInfo getJobInfoByJobInfoVo(JobInfoVo jobInfoVo) {
		String userName = loginService.getCurrentUserName();
		Date currentDate = new Date();
		JobInfo jobInfo = jobInfoVo.getJobInfo();
		if (jobInfo == null) {
			jobInfo = new JobInfo();
		}
		jobInfo.setJobName(jobInfoVo.getJobName());
		jobInfo.setDayOfMonth(jobInfoVo.getDayOfMonth());
		jobInfo.setDayOfWeek(jobInfoVo.getDayOfWeek());
		jobInfo.setHour(jobInfoVo.getHour());
		jobInfo.setDescription(jobInfoVo.getDescription());
		jobInfo.setEndDate(jobInfoVo.getEndDate());
		jobInfo.setJobClassName(TIMEOUT_EMAIL_JOBCLASS_NAME);
		jobInfo.setMinute(jobInfoVo.getMinute());
		jobInfo.setMonth(jobInfoVo.getMonth());
		jobInfo.setNextTimeout(jobInfoVo.getNextTimeout());
		jobInfo.setSecond(jobInfoVo.getSecond());
		jobInfo.setStartDate(jobInfoVo.getStartDate());
		jobInfo.setYear(jobInfoVo.getYear());

		jobInfo.setCreatedBy(userName);
		jobInfo.setCreatedDatetime(currentDate);
		jobInfo.setUpdatedBy(userName);
		jobInfo.setUpdatedDatetime(currentDate);
		jobInfo.setDefunctInd("N");

		return jobInfo;
	}

	public JobInfoVo getJobInfoVoByJobInfo(JobInfo jobInfo) {

		JobInfoVo jobInfoVo = new JobInfoVo();
		jobInfoVo.setJobInfo(jobInfo);
		jobInfoVo.setJobName(jobInfo.getJobName());
		jobInfoVo.setDayOfMonth(jobInfo.getDayOfMonth());
		jobInfoVo.setDayOfWeek(jobInfo.getDayOfWeek());
		jobInfoVo.setHour(jobInfo.getHour());
		jobInfoVo.setDescription(jobInfo.getDescription());
		jobInfoVo.setEndDate(jobInfo.getEndDate());
		jobInfoVo.setJobClassName(jobInfo.getJobClassName());
		jobInfoVo.setMinute(jobInfo.getMinute());
		jobInfoVo.setMonth(jobInfo.getMonth());
		jobInfoVo.setNextTimeout(jobInfo.getNextTimeout());
		jobInfoVo.setSecond(jobInfo.getSecond());
		jobInfoVo.setStartDate(jobInfo.getStartDate());
		jobInfoVo.setYear(jobInfo.getYear());
		return jobInfoVo;
	}

	public WfTimeoutConfig getConfigByConfigVo(TimeoutConfigVo configVo) {
		String userName = loginService.getCurrentUserName();
		Date currentDate = new Date();
		WfTimeoutConfig config = configVo.getConfig();
		if (config == null) {
			config = new WfTimeoutConfig();
		}
		config.setEffectiveDays(Long.valueOf(configVo.getEffectiveDays()));
		config.setEnableInd(configVo.getEnableInd());
		config.setMailInd(configVo.isMail() ? "Y" : "N");
		config.setSysNoticeInd(configVo.isNotice() ? "Y" : "N");
		config.setRemarks(configVo.getRemarks());
		String requestformType = "";
		for (String requestform : configVo.getRequestforms()) {
			requestformType = requestformType + requestform + ",";
		}
		config.setWfRequestformType(requestformType);
		config.setWfType(configVo.getWfType());

		String timeoutType = configVo.getTimeoutType();
		if (DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_2.equals(timeoutType)) {
			config.setPositionTimeoutInd("Y");
			config.setWfTimeoutInd("N");
			PositionTimeoutRemind remind = null;

			List<PositionTimeoutRemind> reminds = config.getPositionTimeoutReminds();
			if (reminds != null && reminds.size() > 0) {
				remind = reminds.get(0);
			} else {
				reminds = new ArrayList<PositionTimeoutRemind>();
				remind = new PositionTimeoutRemind();
				reminds.add(remind);
			}
			remind.setEnableInd("Y");
			remind.setWfTimeoutConfig(config);
			String wpIntervalDays = configVo.getWpIntervalDays();
			remind.setWpIntervalDays("" == wpIntervalDays ? 0 : Long.valueOf(wpIntervalDays));
			String wpTimeoutDays = configVo.getWpTimeoutDays();
			remind.setWpTimeoutDays("" == wpTimeoutDays ? 0 : Long.valueOf(wpTimeoutDays));
			String wpUrgeDays = configVo.getWpUrgeDays();
			remind.setWpUrgeDays("" == wpUrgeDays ? 0 : Long.valueOf(wpUrgeDays));

			remind.setCreatedBy(userName);
			remind.setCreatedDatetime(currentDate);
			remind.setUpdatedBy(userName);
			remind.setUpdatedDatetime(currentDate);
			remind.setDefunctInd("N");

			config.setPositionTimeoutReminds(reminds);
		}
		if (DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_1.equals(timeoutType)) {
			config.setWfTimeoutInd("Y");
			config.setPositionTimeoutInd("N");
		}

		config.setCreatedBy(userName);
		config.setCreatedDatetime(currentDate);
		config.setUpdatedBy(userName);
		config.setUpdatedDatetime(currentDate);
		config.setDefunctInd("N");
		return config;
	}

	public TimeoutConfigVo getConfigVoByConfig(WfTimeoutConfig config) {
		TimeoutConfigVo configVo = new TimeoutConfigVo();
		configVo.setConfig(config);

		if ("Y".equals(config.getWfTimeoutInd())) {
			configVo.setTimeoutType(DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_1);
		}
		List<String> requestforms = new ArrayList<String>();
		for (String requestform : config.getWfRequestformType().split(",")) {
			requestforms.add(requestform);
		}
		configVo.setRequestforms(requestforms);
		if ("Y".equals(config.getPositionTimeoutInd())) {
			configVo.setTimeoutType(DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_2);
			PositionTimeoutRemind remind = config.getPositionTimeoutReminds().get(0);
			configVo.setWpIntervalDays(remind.getWpIntervalDays() + "");
			configVo.setWpTimeoutDays(remind.getWpTimeoutDays() + "");
			configVo.setWpUrgeDays(remind.getWpUrgeDays() + "");
		}

		configVo.setEffectiveDays(config.getEffectiveDays() + "");
		configVo.setEnableInd(config.getEnableInd());
		configVo.setMail("Y".equals(config.getMailInd()));
		configVo.setNotice("Y".equals(config.getSysNoticeInd()));
		configVo.setRemarks(config.getRemarks());
		configVo.setWfTimeoutInd(config.getWfTimeoutInd());
		configVo.setWfType(config.getWfType());
		configVo.setDefunctInd(config.getDefunctInd());

		return configVo;

	}

	public boolean hasConfig(TimeoutConfigVo configVo) {
		String wfTimeoutInd = "N";
		String positionTimeoutInd = "N";
		if (DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_1.equals(configVo.getTimeoutType())) {
			wfTimeoutInd = "Y";
		}
		if (DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_2.equals(configVo.getTimeoutType())) {
			positionTimeoutInd = "Y";
		}
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select config from WfTimeoutConfig config");
		jpql.append(" where config.wfType = '").append(configVo.getWfType()).append("'");
		jpql.append(" and config.wfType = '").append(configVo.getWfType()).append("'");
		jpql.append(" and config.wfTimeoutInd = '").append(wfTimeoutInd).append("'");
		jpql.append(" and config.positionTimeoutInd = '").append(positionTimeoutInd).append("'");

		jpql.append(" and ( 1=1");
		for (String rs : configVo.getRequestforms()) {
			jpql.append(" or config.wfRequestformType like '%").append(rs).append("%'");
		}
		jpql.append(" )");
		WfTimeoutConfig config = configVo.getConfig();
		if (config != null) {
			jpql.append(" and config.id <> ").append(config.getId());
		}
		jpql.append(" and config.defunctInd <> 'Y'");

		List<WfTimeoutConfig> configs = em.createQuery(jpql.toString()).getResultList();

		return configs.size() > 0;
	}

	public WfTimeoutConfig findTimeoutTypeByRequestform(String wfType, String requestform) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select config from WfTimeoutConfig config");
		jpql.append(" where config.defunctInd <> 'Y'");
		jpql.append(" and config.enableInd = 'Y'");
		jpql.append(" and config.wfTimeoutInd = 'Y'");
		jpql.append(" and config.wfType = '").append(wfType).append("'");
		jpql.append(" and config.wfRequestformType like '%").append(requestform).append("%'");
		List<WfTimeoutConfig> configs = em.createQuery(jpql.toString()).getResultList();
		if (configs.size() > 0) {
			return configs.get(0);
		} else {
			return null;
		}
	}

	public boolean validateWfRemindVo(WfRemindVo wfRemindVo) {
		boolean validate = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if ("Y".equals(wfRemindVo.getWfTimeoutInd())) {
			if (!ValidateUtil.validateRequired(context, wfRemindVo.getWfCompleteDate(), "完成日期：")) {
				validate = false;
			}
			if (!ValidateUtil.validateRequired(context, wfRemindVo.getWfIntervalDays(), "提醒频率（天）：")) {
				validate = false;
			}
			if (!ValidateUtil
					.validateRegex(context, wfRemindVo.getWfIntervalDays(), "提醒频率（天）：", REGEX_BUNDLE.getString("POSITIVEINTTEGER"), "必须是正整数")) {
				validate = false;
			}
			if (!ValidateUtil.validateRequired(context, wfRemindVo.getWfUrgeDate(), "催促日期：")) {
				validate = false;
			}
			if (!ValidateUtil.validateStartTimeAndEndTime(context, wfRemindVo.getWfUrgeDate(), wfRemindVo.getWfCompleteDate(), "完成日期：", "必须晚于催促日期")) {
				validate = false;
			}
			if (!ValidateUtil.validateNewTimeAndEndTime(context, new Date(), wfRemindVo.getWfUrgeDate(), "催促日期：", "必须早于当前时间")) {
				validate = false;
			}
		}
		return validate;
	}

	public boolean validateConfigVo(TimeoutConfigVo configVo) {
		boolean validate = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if (!ValidateUtil.validateRequired(context, configVo.getWfType(), "任务类型：")) {
			validate = false;
		}
		if (configVo.getRequestforms() == null || configVo.getRequestforms().size() == 0) {
			validate = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "申请单类型：", "不能为空。"));
		}
		if (!ValidateUtil.validateRequired(context, configVo.getTimeoutType(), "超时类型：")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequiredAndRegex(context, configVo.getEffectiveDays(), "有效时间：", REGEX_BUNDLE.getString("ONLYNUM"), "必须是整数类型")) {
			validate = false;
		}
		if (DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_2.equals(configVo.getTimeoutType())) {
			if (!ValidateUtil.validateRequiredAndRegex(context, configVo.getWpTimeoutDays(), "超时天数：", REGEX_BUNDLE.getString("ONLYNUM"), "必须是整数类型")) {
				validate = false;
			}
			if (!ValidateUtil.validateRequiredAndRegex(context, configVo.getWpIntervalDays(), "提醒频率（天）：", REGEX_BUNDLE.getString("POSITIVEINTTEGER"),
					"必须是正整数")) {
				validate = false;
			}
			if (!ValidateUtil.validateRequiredAndRegex(context, configVo.getWpUrgeDays(), "催促天数：", REGEX_BUNDLE.getString("ONLYNUM"), "必须是整数类型")) {
				validate = false;
			}
		}
		return validate;
	}

	public void saveWfTimeoutRemind(WfRemindVo wfRemindVo) {
		try {
			if (wfRemindVo != null && "Y".equals(wfRemindVo.getWfTimeoutInd())) {
				String userName = loginService.getCurrentUserName();
				Date currentDate = new Date();
				WfTimeoutRemind remind = wfRemindVo.getWfTimeoutRemind();
				if (remind == null) {
					remind = new WfTimeoutRemind();
					remind.setWfId(wfRemindVo.getWfId());
					remind.setCreatedBy(userName);
					remind.setCreatedDatetime(currentDate);
				}
				remind.setWfCompleteDate(wfRemindVo.getWfCompleteDate());
				remind.setWfIntervalDays(Long.valueOf(wfRemindVo.getWfIntervalDays()));
				remind.setWfUrgeDate(wfRemindVo.getWfUrgeDate());
				remind.setDefunctInd("N");
				remind.setUpdatedBy(userName);
				remind.setUpdatedDatetime(currentDate);
				em.merge(remind);
			}

		} catch (Exception e) {
			logger.info("流程 [" + wfRemindVo.getWfId() + "]" + " 流程超时规则保存失败", e);
		}

	}

	public WfRemindVo findWfRemindVo(String wfType, String requestform, Long wfId) {
		WfRemindVo wfRemindVo = new WfRemindVo();
		try {
			WfTimeoutConfig config = findTimeoutTypeByRequestform(wfType, requestform);
			if (config != null) {
				wfRemindVo.setPositionTimeoutInd(config.getPositionTimeoutInd());
				wfRemindVo.setWfTimeoutInd(config.getWfTimeoutInd());
			}
			if (wfId != null) {
				List<WfTimeoutRemind> resultList = em.createQuery("select r from WfTimeoutRemind r where r.defunctInd <> 'Y' and r.wfId = " + wfId)
						.getResultList();
				if (resultList.size() > 0) {
					WfTimeoutRemind remind = resultList.get(0);
					wfRemindVo.setWfTimeoutRemind(remind);
					wfRemindVo.setWfCompleteDate(remind.getWfCompleteDate());
					wfRemindVo.setWfIntervalDays(remind.getWfIntervalDays() + "");
					wfRemindVo.setWfUrgeDate(remind.getWfUrgeDate());
				}
			}
		} catch (Exception e) {
			logger.info("查找超时规则失败", e);
		}
		return wfRemindVo;
	}

	public WfRemindVo findWfRemindCompleteDateBy(String workflowNumber) {
		WfRemindVo wfRemindVo = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT wr.WF_COMPLETE_DATE FROM WF_INSTANCEMSTR w");
			sql.append(" JOIN WF_INSTANCEMSTR_PROPERTY wp ON w.ID = wp.WF_INSTANCEMSTR_ID");
			sql.append(" AND wp.NAME = '" + WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE + "'");
			// 规则表
			sql.append(" JOIN WF_TIMEOUT_CONFIG wc ON locate(wp.VALUE,wc.WF_REQUESTFORM_TYPE)>0");
			sql.append(" AND wc.DEFUNCT_IND = 'N' AND wc.ENABLE_IND = 'Y' AND wc.WF_TIMEOUT_IND = 'Y'");
			sql.append(" JOIN WF_TIMEOUT_REMIND wr ON w.ID = wr.WF_ID");
			sql.append(" WHERE w.DEFUNCT_IND = 'N' AND w.STATUS = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
			sql.append(" AND w.NO = '" + workflowNumber + "'");
			List resultList = em.createNativeQuery(sql.toString()).getResultList();
			if (resultList.size() > 0) {
				Object object = resultList.get(0);
				wfRemindVo = new WfRemindVo();
				wfRemindVo.setWfCompleteDate(object == null ? null : (Date) object);
			}
		} catch (Exception e) {
			logger.info("查找超时规则失败", e);
		}
		return wfRemindVo;
	}

	/**
	 * @return 岗位超时邮件的流程
	 */
	private List<NotificationVo> findPositionTimeoutWorkflows(JobInfo jobInfo) {
		String currDateStr = DateUtils.getCurrDateStr();

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT w.TYPE,w.NO,ws.CHARGED_BY,wc.MAIL_IND,wc.SYS_NOTICE_IND");
		// 任务完成日期
		sql.append(" ,ws.COMPLETED_DATETIME + pr.WP_TIMEOUT_DAYS DAY                            AS COMPLETED_DATETIME");
		// 已经超期时间
		sql.append(" ,days('" + currDateStr + "') - days(ws.COMPLETED_DATETIME)-pr.WP_TIMEOUT_DAYS AS OVERTIMEDAYS");

		sql.append(" FROM WF_INSTANCEMSTR w");
		sql.append(" JOIN (");
		// 当前执行的节点
		sql.append(" SELECT");
		sql.append(" step.WF_INSTANCEMSTR_ID AS WF_INSTANCEMSTR_ID,");
		sql.append(" step.CHARGED_BY         AS CHARGED_BY,");
		sql.append(" step.COMPLETED_DATETIME AS COMPLETED_DATETIME");
		sql.append(" FROM WF_STEPMSTR step WHERE step.ID IN");
		sql.append(" (SELECT MAX(s.id) FROM WF_STEPMSTR s GROUP BY s.WF_INSTANCEMSTR_ID)");

		sql.append(" ) AS ws ON w.ID = ws.WF_INSTANCEMSTR_ID");
		// 超时邮件流程申请单类型
		sql.append(" JOIN WF_INSTANCEMSTR_PROPERTY wp ON w.ID = wp.WF_INSTANCEMSTR_ID");
		sql.append(" AND wp.NAME = '" + WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE + "'");
		// 规则表
		sql.append(" JOIN WF_TIMEOUT_CONFIG wc ON locate(wp.VALUE,wc.WF_REQUESTFORM_TYPE)>0");
		sql.append(" AND wc.DEFUNCT_IND = 'N' AND wc.ENABLE_IND = 'Y' AND wc.POSITION_TIMEOUT_IND = 'Y'");
		sql.append(" AND wc.JOB_ID = '" + jobInfo.getJobId() + "'");
		sql.append(" JOIN POSITION_TIMEOUT_REMIND pr ON wc.ID = pr.WF_TIMEOUT_CONFIG_ID");
		sql.append(" WHERE w.DEFUNCT_IND = 'N' AND w.STATUS = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
		// 滿足的規則
		sql.append(" AND days('" + currDateStr + "')-days(ws.COMPLETED_DATETIME) BETWEEN pr.WP_TIMEOUT_DAYS AND wc.EFFECTIVE_DAYS");
		sql.append(" AND mod (days('" + currDateStr + "') - days(ws.COMPLETED_DATETIME)-pr.WP_TIMEOUT_DAYS,pr.WP_INTERVAL_DAYS) =0");

		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		try {
			List resultList = em.createNativeQuery(sql.toString()).getResultList();
			noticeVos = getTimeoutNoticeVos(resultList);
		} catch (Exception e) {
			logger.error("超找岗位超时流程失败", e);
		}
		logger.info("[Position Timeout NoticeVos] " + noticeVos.size());
		return noticeVos;
	}

	/**
	 * @return 岗位提醒邮件的流程
	 */
	private List<NotificationVo> findPositionUrgeWorkflows(JobInfo jobInfo) {
		String currDateStr = DateUtils.getCurrDateStr();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT w.TYPE,w.NO,ws.CHARGED_BY,wc.MAIL_IND,wc.SYS_NOTICE_IND");
		// 任务完成日期
		sql.append(" ,ws.COMPLETED_DATETIME + pr.WP_TIMEOUT_DAYS DAY                            AS COMPLETED_DATETIME");
		// 已经超期时间
		sql.append(" ,days('" + currDateStr + "') - days(ws.COMPLETED_DATETIME)-pr.WP_TIMEOUT_DAYS AS OVERTIMEDAYS");

		sql.append(" FROM WF_INSTANCEMSTR w");
		sql.append(" JOIN (");
		// 当前执行的节点
		sql.append(" SELECT");
		sql.append(" step.WF_INSTANCEMSTR_ID AS WF_INSTANCEMSTR_ID,");
		sql.append(" step.CHARGED_BY         AS CHARGED_BY,");
		sql.append(" step.COMPLETED_DATETIME AS COMPLETED_DATETIME");
		sql.append(" FROM WF_STEPMSTR step WHERE step.ID IN");
		sql.append(" (SELECT MAX(s.id) FROM WF_STEPMSTR s GROUP BY s.WF_INSTANCEMSTR_ID)");

		sql.append(" ) AS ws ON w.ID = ws.WF_INSTANCEMSTR_ID");
		// 超时邮件流程申请单类型
		sql.append(" JOIN WF_INSTANCEMSTR_PROPERTY wp ON w.ID = wp.WF_INSTANCEMSTR_ID");
		sql.append(" AND wp.NAME = '" + WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE + "'");
		// 规则表
		sql.append(" JOIN WF_TIMEOUT_CONFIG wc ON locate(wp.VALUE,wc.WF_REQUESTFORM_TYPE)>0");
		sql.append(" AND wc.DEFUNCT_IND = 'N' AND wc.ENABLE_IND = 'Y' AND wc.POSITION_TIMEOUT_IND = 'Y'");
		sql.append(" AND wc.JOB_ID = '" + jobInfo.getJobId() + "'");
		sql.append(" JOIN POSITION_TIMEOUT_REMIND pr ON wc.ID = pr.WF_TIMEOUT_CONFIG_ID");
		sql.append(" WHERE w.DEFUNCT_IND = 'N' AND w.STATUS = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
		// 滿足的規則
		sql.append(" AND days('" + currDateStr + "')-days(ws.COMPLETED_DATETIME) BETWEEN 0 AND wc.EFFECTIVE_DAYS");
		sql.append(" AND pr.WP_URGE_DAYS <> 0 AND days('" + currDateStr + "')-days(ws.COMPLETED_DATETIME) = pr.WP_URGE_DAYS");
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		try {
			List resultList = em.createNativeQuery(sql.toString()).getResultList();
			noticeVos = getTimeoutNoticeVos(resultList);
		} catch (Exception e) {
			logger.error("超找岗位提醒流程失败", e);
		}
		logger.info("[Position Urge NoticeVos] " + noticeVos.size());
		return noticeVos;
	}

	/**
	 * @return 流程超时邮件的流程
	 */
	private List<NotificationVo> findWfTimeoutWorkflows(JobInfo jobInfo) {
		String currDateStr = DateUtils.getCurrDateStr();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT w.TYPE,w.NO,ws.CHARGED_BY,wc.MAIL_IND,wc.SYS_NOTICE_IND");
		// 任务完成日期
		sql.append(" ,wr.WF_COMPLETE_DATE                                    AS COMPLETED_DATETIME");
		// 已经超期时间
		sql.append(" ,days('" + currDateStr + "') - days(wr.WF_COMPLETE_DATE) AS OVERTIMEDAYS");

		sql.append(" FROM WF_INSTANCEMSTR w");
		sql.append(" JOIN (");
		// 当前执行的节点
		sql.append(" SELECT");
		sql.append(" step.WF_INSTANCEMSTR_ID AS WF_INSTANCEMSTR_ID,");
		sql.append(" step.CHARGED_BY         AS CHARGED_BY,");
		sql.append(" step.COMPLETED_DATETIME AS COMPLETED_DATETIME");
		sql.append(" FROM WF_STEPMSTR step WHERE step.ID IN");
		sql.append(" (SELECT MAX(s.id) FROM WF_STEPMSTR s GROUP BY s.WF_INSTANCEMSTR_ID)");

		sql.append(" ) AS ws ON w.ID = ws.WF_INSTANCEMSTR_ID");
		// 超时邮件流程申请单类型
		sql.append(" JOIN WF_INSTANCEMSTR_PROPERTY wp ON w.ID = wp.WF_INSTANCEMSTR_ID");
		sql.append(" AND wp.NAME = '" + WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE + "'");
		// 规则表
		sql.append(" JOIN WF_TIMEOUT_CONFIG wc ON locate(wp.VALUE,wc.WF_REQUESTFORM_TYPE)>0");
		sql.append(" AND wc.DEFUNCT_IND = 'N' AND wc.ENABLE_IND = 'Y' AND wc.WF_TIMEOUT_IND = 'Y'");
		sql.append(" AND wc.JOB_ID = '" + jobInfo.getJobId() + "'");
		sql.append(" JOIN WF_TIMEOUT_REMIND wr ON w.ID = wr.WF_ID");
		sql.append(" WHERE w.DEFUNCT_IND = 'N' AND w.STATUS = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
		// 滿足的規則
		sql.append(" AND days('" + currDateStr + "')-days(wr.WF_COMPLETE_DATE) BETWEEN 0 AND wc.EFFECTIVE_DAYS");
		sql.append(" AND mod(days('" + currDateStr + "') - days(wr.WF_COMPLETE_DATE),wr.WF_INTERVAL_DAYS) = 0");
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		try {
			List resultList = em.createNativeQuery(sql.toString()).getResultList();
			noticeVos = getTimeoutNoticeVos(resultList);
		} catch (Exception e) {
			logger.error("超找流程超时邮件流程失败", e);
		}
		logger.info("[WF Timeout NoticeVos] " + noticeVos.size());
		return noticeVos;
	}

	/**
	 * @return 流程提醒邮件的流程
	 */
	private List<NotificationVo> findWfUrgeWorkflows(JobInfo jobInfo) {
		String currDateStr = DateUtils.getCurrDateStr();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT w.TYPE,w.NO,ws.CHARGED_BY,wc.MAIL_IND,wc.SYS_NOTICE_IND");
		// 任务完成日期
		sql.append(" ,wr.WF_COMPLETE_DATE                                    AS COMPLETED_DATETIME");
		// 已经超期时间
		sql.append(" ,days('" + currDateStr + "') - days(wr.WF_COMPLETE_DATE) AS OVERTIMEDAYS");

		sql.append(" FROM WF_INSTANCEMSTR w");
		sql.append(" JOIN (");
		// 当前执行的节点
		sql.append(" SELECT");
		sql.append(" step.WF_INSTANCEMSTR_ID AS WF_INSTANCEMSTR_ID,");
		sql.append(" step.CHARGED_BY         AS CHARGED_BY,");
		sql.append(" step.COMPLETED_DATETIME AS COMPLETED_DATETIME");
		sql.append(" FROM WF_STEPMSTR step WHERE step.ID IN");
		sql.append(" (SELECT MAX(s.id) FROM WF_STEPMSTR s GROUP BY s.WF_INSTANCEMSTR_ID)");

		sql.append(" ) AS ws ON w.ID = ws.WF_INSTANCEMSTR_ID");
		// 超时邮件流程申请单类型
		sql.append(" JOIN WF_INSTANCEMSTR_PROPERTY wp ON w.ID = wp.WF_INSTANCEMSTR_ID");
		sql.append(" AND wp.NAME = '" + WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE + "'");
		// 规则表
		sql.append(" JOIN WF_TIMEOUT_CONFIG wc ON locate(wp.VALUE,wc.WF_REQUESTFORM_TYPE)>0");
		sql.append(" AND wc.DEFUNCT_IND = 'N' AND wc.ENABLE_IND = 'Y' AND wc.WF_TIMEOUT_IND = 'Y'");
		sql.append(" AND wc.JOB_ID = '" + jobInfo.getJobId() + "'");
		sql.append(" JOIN WF_TIMEOUT_REMIND wr ON w.ID = wr.WF_ID");
		sql.append(" WHERE w.DEFUNCT_IND = 'N' AND w.STATUS = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
		// 滿足的規則
		sql.append(" AND days('" + currDateStr + "')-days(ws.COMPLETED_DATETIME) BETWEEN 0 AND wc.EFFECTIVE_DAYS");
		sql.append(" AND days('" + currDateStr + "') = days(wr.WF_URGE_DATE)");
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		try {
			List resultList = em.createNativeQuery(sql.toString()).getResultList();
			noticeVos = getTimeoutNoticeVos(resultList);
		} catch (Exception e) {
			logger.error("超找流程提醒邮件流程失败", e);
		}
		logger.info("[WF Urge NoticeVos] " + noticeVos.size());
		return noticeVos;
	}

	private List<NotificationVo> getTimeoutNoticeVos(List resultList) {
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		String flowType = null;
		String subject = null;
		ArrayList<String> receivers = null;
		NotificationVo noticeVo = null;
		for (int i = 0; i < resultList.size(); i++) {
			Object[] result = (Object[]) resultList.get(i);
			String chargedBy = result[2] == null ? null : result[2].toString();
			if (chargedBy != null && !"".equals(chargedBy)) {
				flowType = result[0] == null ? "" : workflowTypeMap.get(result[0]);
				subject = " [益海嘉里]税务信息平台-" + flowType + "流程提示通知";
				String[] chargedBys = chargedBy.split(",");
				receivers = new ArrayList<String>();
				for (String c : chargedBys) {
					receivers.add(c);
				}
				noticeVo = new NotificationVo();
				noticeVo.setReceiverList(receivers);
				noticeVo.setTypeId(result[1] == null ? null : result[1].toString());
				noticeVo.setTitle(subject);
				noticeVo.setMailInd(result[3] == null ? null : result[3].toString());
				noticeVo.setSysNoticeInd(result[4] == null ? null : result[4].toString());
				Date completeDate = result[5] == null ? null : (Date) result[5];
				Long overtimeDays = result[6] == null ? null : Long.valueOf(result[6].toString());
				String content = "此流程要求在" + DateUtils.format(completeDate) + "之前完成，";
				if (overtimeDays != null && overtimeDays >= 0) {
					content = content + (overtimeDays == 0 ? "任务已到期但未处理，" : "现在已经超期" + overtimeDays + "天，");
				}
				content = content + "请尽快处理此流程。";
				noticeVo.setContent(content);
				noticeVos.add(noticeVo);
			}
		}
		return noticeVos;
	}

	/**
	 * @param jobInfo
	 * @return 超找超时邮件
	 */
	public List<NotificationVo> findTimeoutNoticeVosByJobInfo(JobInfo jobInfo) {
		List<NotificationVo> positionTimeoutWorkflows = findPositionTimeoutWorkflows(jobInfo);
		List<NotificationVo> positionUrgeWorkflows = findPositionUrgeWorkflows(jobInfo);
		List<NotificationVo> wfTimeoutWorkflows = findWfTimeoutWorkflows(jobInfo);
		List<NotificationVo> wfUrgeWorkflows = findWfUrgeWorkflows(jobInfo);
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		noticeVos.addAll(positionTimeoutWorkflows);
		noticeVos.addAll(positionUrgeWorkflows);
		noticeVos.addAll(wfTimeoutWorkflows);
		noticeVos.addAll(wfUrgeWorkflows);
		return noticeVos;
	}
}
