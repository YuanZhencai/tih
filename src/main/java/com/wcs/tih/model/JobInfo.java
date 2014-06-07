package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the JOB_INFO database table.
 * 
 */
@Entity
@Table(name="JOB_INFO")
public class JobInfo extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DAY_OF_MONTH")
	private String dayOfMonth;

	@Column(name="DAY_OF_WEEK")
	private String dayOfWeek;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String description;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="END_DATE")
	private Date endDate;

	private String hour;

	@Column(name="JOB_CLASS_NAME")
	private String jobClassName;

	@Column(name="JOB_ID")
	private String jobId;

	@Column(name="JOB_NAME")
	private String jobName;

	private String minute;

	private String month;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="NEXT_TIMEOUT")
	private Date nextTimeout;

	private String second;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="START_DATE")
	private Date startDate;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	private String year;

    public JobInfo() {
    }

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDatetime() {
		return this.createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getDayOfMonth() {
		return this.dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getDayOfWeek() {
		return this.dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getHour() {
		return this.hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getJobClassName() {
		return this.jobClassName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getJobId() {
		return this.jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getMinute() {
		return this.minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Date getNextTimeout() {
		return this.nextTimeout;
	}

	public void setNextTimeout(Date nextTimeout) {
		this.nextTimeout = nextTimeout;
	}

	public String getSecond() {
		return this.second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDatetime() {
		return this.updatedDatetime;
	}

	public void setUpdatedDatetime(Date updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}