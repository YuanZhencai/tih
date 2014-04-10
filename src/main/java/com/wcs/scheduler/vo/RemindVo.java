package com.wcs.scheduler.vo;

import com.wcs.tih.model.WfTimeoutRemind;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
public class RemindVo {

    private WfTimeoutRemind remind;

    private String jobId;

    private String type;
    private String remarks;
    private String status;

    private Long overtimeDays;

    private Long intervalDays;

    private Long effectiveDays;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOvertimeDays() {
        return overtimeDays;
    }

    public void setOvertimeDays(Long overtimeDays) {
        this.overtimeDays = overtimeDays;
    }

    public Long getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(Long intervalDays) {
        this.intervalDays = intervalDays;
    }

    public Long getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Long effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public WfTimeoutRemind getRemind() {
        return remind;
    }

    public void setRemind(WfTimeoutRemind remind) {
        this.remind = remind;
    }

}
