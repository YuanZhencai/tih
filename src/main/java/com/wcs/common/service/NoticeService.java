package com.wcs.common.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.EmailVo;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.tih.model.JobInfo;
import com.wcs.tih.model.NotificationReceiver;
import com.wcs.tih.model.Notificationmstr;
import com.wcs.tih.model.WfMailConfig;
import com.wcs.tih.transaction.service.TaskManagementService;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 发送通知
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yuanzhencai@wcs-global.com">袁振才</a>
 */
@Stateless
public class NoticeService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private NotificationService notificationService;
	@EJB
	private UNSService uNSService;
	@EJB
	private UserCommonService userCommonService;
	@EJB
	private TaskManagementService taskManagementService;

	private static final String APP_URL = ResourceBundle.getBundle("uns").getString("app_url");

	/**
	 * <p>
	 * Description: 发邮件
	 * </p>
	 * 
	 * @param isEmailFlag
	 *            是否是实时邮件
	 * @param refType
	 *            类型(任务还是项目发邮件)
	 * @param sendStatus
	 *            消息发送状态
	 * @param typeId
	 *            所关联的任务单号或者项目单号
	 * @param receiverList
	 *            接受人
	 * @param subject
	 *            主题
	 * @param content
	 *            内容(非html格式)
	 */
	private void sendEmail(boolean isEmailFlag, NotificationVo noticeVo) {
		try {
			noticeVo.setSendOption(DictConsts.TIH_TAX_MSG_TYPE_1);
			noticeVo.setStatus(DictConsts.TIH_TAX_MSG_STATUS_2);
			List<NotificationReceiver> savedReceivers = notificationService.saveNotification(noticeVo);
			if (isEmailFlag) {// 实时邮件才会调用发邮件接口
				for (NotificationReceiver r : savedReceivers) {
					EmailVo emailVo = this.convertReceiversToHtml(r);
					uNSService.sendEmails(emailVo.getSentTo(), noticeVo.getTitle(), emailVo.getHtmlContent());
				}
				notificationService.sendEmailSuccess(savedReceivers);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 发送系统消息
	 * </p>
	 * 
	 * @param refType
	 *            消息关联类型(任务或项目)
	 * @param typeId
	 *            关联类型Id
	 * @param receiverList
	 *            接收人集合
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	private void sendNotification(NotificationVo noticeVo) {
		try {
			// 发消息
			noticeVo.setSendOption(DictConsts.TIH_TAX_MSG_TYPE_2);
			noticeVo.setStatus(DictConsts.TIH_TAX_MSG_STATUS_1);
			noticeVo.setNoticeExt(null);
			notificationService.saveNotification(noticeVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 发送邮件和系统消息
	 * </p>
	 * 
	 * @param refType
	 *            关联类型(任务或项目)
	 * @param typeId
	 *            关联类型ID
	 * @param receiverList
	 *            接收人集合
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	private void sendEmailAndNotification(NotificationVo noticeVo) {
		try {
			this.sendNotification(noticeVo);
			this.sendEmail(true, noticeVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 授权发通知
	 * </p>
	 * 
	 * @param receiverList
	 *            接收人集合
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	@Asynchronous
	public void sendNoticeForWarrant(boolean isEmail, boolean isSysNotice, NotificationVo noticeVo) {
		noticeVo.setRefType(DictConsts.TIH_TAX_MSG_REFTYPE_3);
		if (isSysNotice) {
			sendNotification(noticeVo);
		}
		if (isEmail) {
			sendEmail(true, noticeVo);
		}
	}

	/**
	 * <p>
	 * Description: 项目发通知
	 * </p>
	 * 
	 * @param receiverList
	 *            接收人集合
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	@Asynchronous
	public void sendNoticeForProject(NotificationVo noticeVo) {
		noticeVo.setRefType(DictConsts.TIH_TAX_MSG_REFTYPE_2);
		sendEmailAndNotification(noticeVo);
	}

	/**
	 * <p>
	 * Description: 超时任务发送通知
	 * </p>
	 * 
	 * @param receiverList
	 * @param typeId
	 * @param subject
	 * @param content
	 */
	@Asynchronous
	public void sendOverTimeForTask(NotificationVo noticeVo) {
		noticeVo.setRefType(DictConsts.TIH_TAX_MSG_REFTYPE_1);
		if ("Y".equals(noticeVo.getMailInd())) {
			sendEmail(true, noticeVo);
		}
		if ("Y".equals(noticeVo.getSysNoticeInd())) {
			sendNotification(noticeVo);
		}

	}

	/**
	 * <p>
	 * Description: 任务发通知
	 * </p>
	 * 
	 * @param userName
	 *            接收人
	 * @param requestFormType
	 *            申请单类型
	 * @param subject
	 *            主题
	 * @param content
	 *            内容
	 */
	@Asynchronous
	public void sendNoticeForTask(String requestFormType, List<NotificationVo> noticeVos) {
		if (noticeVos == null) {
			return;
		}
		List<WfMailConfig> mcs = taskManagementService.getWfMailConfig(requestFormType);
		boolean isTimed = false;
		boolean isNotice = false;
		boolean isEmail = false;
		for (WfMailConfig mc : mcs) {// 判断该流程邮件配置的信息
			if ("N".equals(mc.getSysNoticeInd())) {// 发消息
				isNotice = true;
			}
			if ("N".equals(mc.getMailInd())) {// 实时邮件
				isEmail = true;
			}
			if ("N".equals(mc.getJobInd())) {// 定时邮件
				isTimed = true;
			}
		}
		logger.info("isNotice:" + isNotice + "~~~isEmail:" + isEmail + "~~~isTimed:" + isTimed);

		for (int i = 0; i < noticeVos.size(); i++) {
			NotificationVo noticeVo = noticeVos.get(i);
			noticeVo.setRefType(DictConsts.TIH_TAX_MSG_REFTYPE_1);
			List<String> receiverAccounts = noticeVo.getReceiverList();
			if (receiverAccounts != null && receiverAccounts.size() != 0) {

				// 发邮件
				if (isEmail || isTimed) {
					sendEmail(isEmail, noticeVo);
				}
				// 发消息
				if (isNotice) {
					sendNotification(noticeVo);
				}

			}
		}
	}

	@Asynchronous
	public void sendOnlyEmail(NotificationVo noticeVo) {
		noticeVo.setRefType(DictConsts.TIH_TAX_MSG_REFTYPE_1);
		sendEmail(true, noticeVo);
	}

	/**
	 * <p>
	 * Description: 定时发邮件
	 * </p>
	 * 
	 * @param jobInfo
	 *            定时器信息
	 * @throws NamingException
	 * @throws JMSException
	 */
	public void timingEmail(JobInfo jobInfo) throws Exception {
		// 得到所有不重复的定时消息的接受人
		List<String> distinctReceivedBy = new ArrayList<String>();
		List<NotificationReceiver> prepareReceivers = new ArrayList<NotificationReceiver>();
		EmailVo emailVo = null;
		List<NotificationReceiver> receivers = notificationService.getAllTimedReceivers(jobInfo);
		logger.info("receivers: " + receivers.size());
		for (int i = 0; i < receivers.size(); i++) {
			NotificationReceiver r = receivers.get(i);
			if (distinctReceivedBy.contains(r.getReceivedBy())) {
				prepareReceivers.add(r);
				if (i == receivers.size() - 1) {
					emailVo = this.convertReceiversToHtml(prepareReceivers);
					uNSService.sendEmails(emailVo.getSentTo(), emailVo.getEmailSubject(), emailVo.getHtmlContent());
				}
				continue;
			} else {
				if (i != 0) {
					emailVo = this.convertReceiversToHtml(prepareReceivers);
					uNSService.sendEmails(emailVo.getSentTo(), emailVo.getEmailSubject(), emailVo.getHtmlContent());
				}
				distinctReceivedBy.add(r.getReceivedBy());
				prepareReceivers = new ArrayList<NotificationReceiver>();
				prepareReceivers.add(r);
				if (i == receivers.size() - 1) {
					emailVo = this.convertReceiversToHtml(prepareReceivers);
					uNSService.sendEmails(emailVo.getSentTo(), emailVo.getEmailSubject(), emailVo.getHtmlContent());
				}
			}
		}
		// 更新已发送
		notificationService.sendEmailsSuccess(receivers);
	}

	private String formatDateFime(Date time) {
		if (time != null) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time);
		}
		return "";
	}

	private EmailVo convertReceiversToHtml(Object obj) {
		// 邮件模版转换的Servlet
		EmailVo emailVo = new EmailVo();
		Date minDate = new Date();
		UsermstrVo uv = null;
		String receiverIds = "";
		if (obj instanceof List) {
			List<NotificationReceiver> receivers = (ArrayList<NotificationReceiver>) obj;
			uv = userCommonService.getUsermstrVoByAdAccount(receivers.get(0).getReceivedBy());
			for (int i = 0; i < receivers.size(); i++) {
				Notificationmstr n = receivers.get(i).getNotificationSender().getNotificationmstr();
				// 最小时间
				if (n.getCreatedDatetime().before(minDate)) {
					minDate = n.getCreatedDatetime();
				}
				receiverIds = receiverIds + receivers.get(i).getId() + ",";
				String emailSubject = "[益海嘉里]税务信息平台 -" + formatDateFime(minDate) + "~" + formatDateFime(new Date()) + "的待处理任务邮件";
				emailVo.setEmailSubject(emailSubject);
			}
		} else {
			NotificationReceiver receiver = (NotificationReceiver) obj;
			uv = userCommonService.getUsermstrVoByAdAccount(receiver.getReceivedBy());
			receiverIds = "" + ((NotificationReceiver) obj).getId();
		}
		String url = APP_URL + "/NoticeContentServlet?receiverIds=" + receiverIds;
		logger.info("url:" + url);
		String resource = null;
		StringBuffer resources = new StringBuffer();
		URLConnection urlc;
		try {
			URL u = new URL(url);// 通过url的方式来取得邮件内容Servlet的InputStream
			urlc = u.openConnection();
			urlc.setDoOutput(false);
			urlc.setAllowUserInteraction(false);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF8"));
			while ((resource = br.readLine()) != null) {
				resources.append(resource);
			}
			br.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		String content = resources.toString();
		content = content.replace("\t", "");
		content = content.replace("\"", "'");
		emailVo.setHtmlContent(content);
		if (uv != null && uv.getP() != null) {
			P p = uv.getP();
			if (p.getEmail() == null) {
				p.setEmail(p.getNachn());
			}
			emailVo.setSentTo(uv.getP());
		} else {
			emailVo.setSentTo(new P());
		}
		return emailVo;
	}

	public void getReceiverById() throws Exception {
		JobInfo jobInfo = notificationService.getJobInfoByJobId("20130729161227");
		logger.info("jobInfo:" + jobInfo.getJobId());
		timingEmail(jobInfo);
	}
}
