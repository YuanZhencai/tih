package com.wcs.scheduler.service.batchjob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.service.NoticeService;
import com.wcs.scheduler.service.JobManagementService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.tih.model.JobInfo;

@Stateless
@Local(BatchJobInterface.class)
public class BatchJobTimeoutEmail implements BatchJobInterface {

	private static Logger logger = LoggerFactory.getLogger(BatchJobTimeoutEmail.class);

	@EJB
	private TimeoutEmailService timeoutEmailService;
	@EJB
	private JobManagementService jobManagementService;
	@EJB
	private NoticeService noticeService;

	@Override
	public void executeJob(Timer timer) {
		logger.info("Start of BatchJobTimeoutEmail at " + new Date() + "...");
		JobInfo jobInfo = (JobInfo) timer.getInfo();
		try {
			List<NotificationVo> noticeVos = findTimeoutNoticeVosByJobInfo(jobInfo);
			for (NotificationVo noticeVo : noticeVos) {
				noticeService.sendOverTimeForTask(noticeVo);
			}
		} catch (Exception e) {
			logger.error("发送超时邮件失败", e);
		} finally {
			jobInfo.setNextTimeout(timer.getNextTimeout());
			jobManagementService.updateNextTime(jobInfo);
			// 删除生命周期结束的定时器数据
			jobManagementService.deleteJobFromUselessDatabase();
		}
		logger.info("end of BatchJobTimeoutEmail at " + new Date() + "...");
	}

	public List<NotificationVo> findTimeoutNoticeVosByJobInfo(JobInfo jobInfo) {
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		try {
			noticeVos = timeoutEmailService.findTimeoutNoticeVosByJobInfo(jobInfo);
		} catch (Exception e) {
			logger.error("超找超时流程失败", e);
		}
		logger.info(new Date() + " timeoutNoticeVos:" + noticeVos.size());
		return noticeVos;
	}
}
