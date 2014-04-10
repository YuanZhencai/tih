package com.wcs.tih.filenet.pe.util;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.P;
import com.wcs.common.service.CommonService;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.util.DateUtils;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.filenet.pe.vo.MailVo;
import com.wcs.tih.filenet.pe.vo.StepVo;
import com.wcs.tih.model.WfStepmstr;

/**
 * 
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 发送邮件util
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 */
@Stateless
public class MailUtil {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@EJB
	private CommonService commonService;
	@EJB
	private UserCommonService userservice;
	@EJB
	private TimeoutEmailService timeoutEmailService;

	@PersistenceContext
	private EntityManager em;

	public String getRequestForm(WfStepmstr wf) {
		return this.commonService.getValueByDictCatKey(wf.getWfInstancemstr().getType(), "zh_CN");
	}

	public String getUserName(String str) {
		logger.info("str" + str);
		if (str == null) {
			return "";
		}
		if (this.userservice.getUsermstrVoByAdAccount(str) == null) {
			return str;
		}
		P p = this.userservice.getUsermstrVoByAdAccount(str).getP();
		if (p == null) {
			return str;
		}
		if (p.getNachn() == null || p.getNachn().equals("")) {
			return str;
		}
		return p.getNachn();
	}

	private String getMailBody(StepVo stepVo) {
		StringBuilder dealMsg = new StringBuilder();
		WfRemindVo wfRemindVo = stepVo.getWfRemindVo();
		if (wfRemindVo != null) {
			String completeDate = DateUtils.format(wfRemindVo.getWfCompleteDate());
			dealMsg.append("此流程要求在").append(completeDate).append("之前完成，");
			long todayIntevalDays = DateUtils.getTodayIntevalDays(completeDate);
			if (todayIntevalDays == 0) {
				dealMsg.append("任务已经超期但未处理，");
			} else if (todayIntevalDays > 0) {
				dealMsg.append("现在已经超期" + todayIntevalDays + "天，");
			}
		}
		dealMsg.append("请尽快处理此流程。");
		String pleaseDealMsg = dealMsg.toString();
		WfStepmstr step = stepVo.getStep();
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_4)) {
			return pleaseDealMsg;
		} else if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_3)) {
			return pleaseDealMsg;
		} else if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_6)) {
			return pleaseDealMsg;
		} else if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_5)) {
			return pleaseDealMsg;
		} else if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_10) || step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_11)) {
			return "此流程已处理结束。";
		} else if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_12)) {
			return pleaseDealMsg;
		}
		return pleaseDealMsg;
	}

	private List<MailVo> simpleMail(StepVo stepVo) {
		WfStepmstr step = stepVo.getStep();
		List<MailVo> list = new ArrayList<MailVo>();
		MailVo mailVo = new MailVo();
		mailVo.setBody(this.getMailBody(stepVo));
		mailVo.setTitle(this.mailTitle(step.getWfInstancemstr().getType()));
		mailVo.setSendBy(step.getChargedBy());
		list.add(mailVo);
		return list;
	}

	private String getFeedback(WfStepmstr wf) {
		String sql = " select w from WfStepmstr w where w.wfInstancemstr.no ='" + wf.getWfInstancemstr().getNo() + "' order by w.id desc";
		return ((WfStepmstr) this.em.createQuery(sql).getResultList().get(0)).getChargedBy();
	}

	private String getAudit(WfStepmstr wf) {
		String sql = " select w from WfStepmstr w where w.wfInstancemstr.no ='" + wf.getWfInstancemstr().getNo()
				+ "' and w.name='文档审核岗' order by w.id desc";
		return ((WfStepmstr) this.em.createQuery(sql).getResultList().get(0)).getCreatedBy();
	}

	public List<MailVo> getTitleTypeByDealMethod(WfStepmstr step) {
		WfRemindVo wfRemindVo = timeoutEmailService.findWfRemindCompleteDateBy(step.getWfInstancemstr().getNo());
		
		StepVo stepVo = new StepVo();
		stepVo.setStep(step);
		stepVo.setWfRemindVo(wfRemindVo);
		
		List<MailVo> list = new ArrayList<MailVo>();
		MailVo mailVo = null;

		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_1)) {
			list.addAll(simpleMail(stepVo));
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_2)) {
			return list;
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_3)) {
			list.addAll(simpleMail(stepVo));
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_4)) {
			list.addAll(simpleMail(stepVo));
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_5)) {
			mailVo = new MailVo();
			mailVo.setSendBy(this.getFeedback(step));
			mailVo.setBody(this.getMailBody(stepVo));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_6)) {
			mailVo = new MailVo();
			mailVo.setSendBy(this.getAudit(step));
			mailVo.setBody(this.getMailBody(stepVo));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_7)) {
			mailVo = new MailVo();
			mailVo.setSendBy(step.getWfInstancemstr().getCreatedBy());
			if (DictConsts.TIH_TAX_REQUESTFORM_4.equals(step.getWfInstancemstr().getType())) {
				mailVo.setSendBy(step.getChargedBy());
			} else if (DictConsts.TIH_TAX_REQUESTFORM_5.equals(step.getWfInstancemstr().getType())) {
				mailVo.setSendBy(step.getChargedBy());
			}
			mailVo.setBody(this.getMailBody(stepVo));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_8)) {
			list.addAll(simpleMail(stepVo));
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_9)) {
			list.addAll(simpleMail(stepVo));
		}

		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_12)) {
			mailVo = new MailVo();
			mailVo.setSendBy(step.getChargedBy());
			mailVo.setBody(this.getMailBody(stepVo));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}

		if (!step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_1)
				&& (!step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_7) || DictConsts.TIH_TAX_REQUESTFORM_4.equals(step.getWfInstancemstr()
						.getType()))) {
			mailVo = new MailVo();
			mailVo.setSendBy(step.getWfInstancemstr().getCreatedBy());
			mailVo.setBody(this.simpleBody(step));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}

		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_10)) {
			mailVo = new MailVo();
			mailVo.setSendBy(step.getWfInstancemstr().getCreatedBy());
			mailVo.setBody(this.getMailBody(stepVo));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}
		if (step.getDealMethod().equals(DictConsts.TIH_TAX_APPROACH_11)) {
			mailVo = new MailVo();
			mailVo.setSendBy(step.getWfInstancemstr().getCreatedBy());
			mailVo.setBody(this.getMailBody(stepVo));
			mailVo.setTitle(mailTitle(step.getWfInstancemstr().getType()));
			list.add(mailVo);
		}

		return list;
	}

	public String simpleBody(WfStepmstr step) {
		return "你创建的流程已被" + userservice.getUsermstrVo(step.getUpdatedBy()).getP().getNachn() + "处理。";
	}

	private String mailTitle(String workflowType) {
		return new StringBuilder().append("[益海嘉里]税务信息平台-").append(commonService.getValueByDictCatKey(workflowType, "zh_CN")).toString();
	}

}
