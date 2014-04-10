package com.wcs.scheduler.vo;

import java.util.Date;

import com.wcs.tih.model.JobInfo;

public class JobInfoVo {

    private JobInfo jobInfo;
    private String dayOfMonth;

    private String dayOfWeek;

    private String description;

    private Date endDate;

    private String hour;

    private String jobClassName;

    private String jobId;

    private String jobName;

    private String minute;

    private String month;

    private Date nextTimeout;

    private String second;

    private Date startDate;

    private String year;

    public JobInfoVo() {
    }

    public JobInfoVo(String jobId, String jobName, String description, Date startDate, Date endDate, Date nextTimeout) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextTimeout = nextTimeout;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getNextTimeout() {
        return nextTimeout;
    }

    public void setNextTimeout(Date nextTimeout) {
        this.nextTimeout = nextTimeout;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }

}
