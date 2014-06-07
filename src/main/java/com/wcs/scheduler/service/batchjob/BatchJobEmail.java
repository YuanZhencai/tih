package com.wcs.scheduler.service.batchjob;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import com.wcs.common.service.NoticeService;
import com.wcs.scheduler.service.JobManagementService;
import com.wcs.tih.model.JobInfo;
/**
 *
 * @author Christopher Lam
 */
@Stateless
@Local(BatchJobInterface.class)
public class BatchJobEmail implements BatchJobInterface{
	@EJB 
	private NoticeService noticeService;
	@EJB 
	private JobManagementService jobManagementService;
    private static Logger logger = Logger.getLogger(BatchJobEmail.class.getName());

    @Lock(LockType.WRITE)
    public void executeJob(Timer timer){
        logger.info("Start of BatchJobA at " + new Date() + "...");
        JobInfo jobInfo = (JobInfo) timer.getInfo();
        JobInfo job = jobManagementService.findJobInfoByJobId(jobInfo.getJobId()).get(0);
        try{
            logger.info("Running job: " + jobInfo.getJobName());
            sendTimingEmail(jobInfo);
        }catch (Exception e){
        	logger.log(Level.SEVERE, null, e);
        }finally{
        	job.setNextTimeout(timer.getNextTimeout());
        	jobManagementService.updateNextTime(job);
        	jobManagementService.deleteJobFromUselessDatabase();//删除生命周期结束的定时器数据
        }
        logger.info("End of BatchJobA at " + new Date());
    }
    
    public void sendTimingEmail(JobInfo jobInfo) throws Exception{
    	noticeService.timingEmail(jobInfo);
    }
}
