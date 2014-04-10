package com.wcs.scheduler.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.scheduler.service.batchjob.BatchJobInterface;
import com.wcs.tih.model.JobInfo;

/**
 * 
 * @author Christopher Lam
 */
@Startup
@Singleton
public class JobService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private TimerService timerService;
    @EJB
    private JobManagementService jobManagementService;

    private Date nextTimeout;

    @PostConstruct
    public void initialise() {
        logger.info("###PostConstruct - calling TimerSessionBean...");
        List<JobInfo> jobInfoTimerServiceList = getJobListFromTimerService(); // 从定时器中得到job
        List<JobInfo> jobInfoDatabaseList = jobManagementService.getJobListFromDatabase(); // 从数据库中得到job
        DeleteSurplusTimer(jobInfoTimerServiceList, jobInfoDatabaseList);
        CreateDropTimer(jobInfoTimerServiceList, jobInfoDatabaseList);
        // TODO：如果，服务器中的JOB为空，则从数据库加载JOB_INFO 实体，并添加到Timer中！
    }

    @PreDestroy
    public void cleanup() {
        logger.info("###Predestroy - cleaning up...");

    }

    /*
     * 回调方法的定时器。 <p>定时器方法的声明很简单，只需在方法上面加入@Timeout 注释，另外定时器方法 必须遵守如下格式：void XXX(Timer timer)在定时事件发生时，此方法将被执行.</p>
     */
    @Timeout
    public void timeout(Timer timer) {
//        logger.info("进入@Timeout方法");
        try {
            JobInfo jobInfo = (JobInfo) timer.getInfo();// 获取与定时器相关的信息。
            BatchJobInterface batchJob = (BatchJobInterface) InitialContext.doLookup(jobInfo.getJobClassName());
            batchJob.executeJob(timer); // Asynchronous method
        } catch (NamingException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex1) {
            logger.error(ex1.getMessage(), ex1);
        }
    }

    /*
     * 返回已经存在的Timer
     */
    private Timer getTimer(JobInfo jobInfo) {
        Collection<Timer> timers = timerService.getTimers();
        for (Timer t : timers) {// 查看定时器是否存在
            JobInfo jobInfoTime = (JobInfo) t.getInfo();
            if ((jobInfo.getJobId()).equals(jobInfoTime.getJobId())) { // 判断新建定时器的ID和名称是否相等
                return t;
            }
        }
        return null;
    }

    /**
     * 创建定时器
     * 
     * @param jobInfo
     * @throws Exception
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public JobInfo createJob(JobInfo jobInfo) throws DuplicateKeyException {
        if (getTimer(jobInfo) != null) { // 检查定时器是否存在
            throw new DuplicateKeyException("Job with the ID already exist!");
        }
        TimerConfig timerAConf = new TimerConfig(jobInfo, true);// 持久计时器

        ScheduleExpression schedExp = new ScheduleExpression();
        schedExp.start(jobInfo.getStartDate());
        schedExp.end(jobInfo.getEndDate());
        schedExp.second(jobInfo.getSecond());
        schedExp.minute(jobInfo.getMinute());
        schedExp.hour(jobInfo.getHour());
        schedExp.dayOfMonth(jobInfo.getDayOfMonth());
        schedExp.month(jobInfo.getMonth());
        schedExp.year(jobInfo.getYear());
        schedExp.dayOfWeek(jobInfo.getDayOfWeek());
        Timer newTimer = timerService.createCalendarTimer(schedExp, timerAConf);
        JobInfo ji = (JobInfo) newTimer.getInfo();
        logger.info("New timer created: " + ji);
        ji.setNextTimeout(newTimer.getNextTimeout());
        nextTimeout = newTimer.getNextTimeout();
        return ji;
    }

    /*
     * 返回更新的jobinfo从定时器
     */
    public JobInfo getJobInfo(JobInfo jobInfo) {
        Timer t = getTimer(jobInfo);
        if (t != null) {
            JobInfo j = (JobInfo) t.getInfo();
            j.setNextTimeout(t.getNextTimeout());
            return j;
        }
        return null;
    }

    /**
     * 更新计时器
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public JobInfo updateJob(JobInfo jobInfo) throws DuplicateKeyException {
        Timer t = getTimer(jobInfo);
        if (t != null) {
            logger.info("Removing timer: " + t.getInfo());
            t.cancel();
            jobInfo = createJob(jobInfo);
        }
        return jobInfo;
    }

    /*
     * 删除定时器
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteJob(JobInfo jobInfo) {
        Timer t = getTimer(jobInfo);
        if (t != null) {
            t.cancel();
        }
    }

    /**
     * Timer和数据库中的数据进行比较，Timer删除Timer多余的job
     * 
     * @param jobInfoTimerServiceList 从定时器中得到job
     * @param jobInfoDatabaseList 从数据库中得到job
     */
    public void DeleteSurplusTimer(List<JobInfo> jobInfoTimerServiceList, List<JobInfo> jobInfoDatabaseList) {
        jobInfoTimerServiceList.removeAll(jobInfoDatabaseList); // 得到的job在数据库中没有与之相对应的数据
        if (jobInfoTimerServiceList.size() > 0) {
            for (JobInfo job : jobInfoTimerServiceList) {
                deleteTimer(job);
            }
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteTimer(JobInfo jobInfo) {
        Timer t = getTimer(jobInfo);
        if (t != null) {
            t.cancel();
        }
    }

    /**
     * Timer和数据库中的数据进行比较，创建缺少的Timer
     */
    public void CreateDropTimer(List<JobInfo> jobInfoTimerServiceList, List<JobInfo> jobInfoDatabaseList) {
        jobInfoDatabaseList.removeAll(jobInfoTimerServiceList); // 得到没有创建到Timer中的数据
        if (jobInfoDatabaseList.size() > 0) {
            for (JobInfo job : jobInfoDatabaseList) {
                try {
                    createJob(job);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /*
     * 返回Timer中的定时器
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<JobInfo> getJobListFromTimerService() {
        logger.info("getJobList() called!!!");
        ArrayList<JobInfo> jobList = new ArrayList<JobInfo>();
        Collection<Timer> timers = timerService.getTimers();
        for (Timer t : timers) {
            if (t.isCalendarTimer()) {// 只判断clendarTimers,用@Schedule的timer 不考虑在内
                JobInfo jobInfo = (JobInfo) t.getInfo();
                jobInfo.setNextTimeout(t.getNextTimeout());
                jobList.add(jobInfo);
            }
        }
        return jobList;
    }

    public Date getNextTimeout() {
        return nextTimeout;
    }

    public void setNextTimeout(Date nextTimeout) {
        this.nextTimeout = nextTimeout;
    }

}
