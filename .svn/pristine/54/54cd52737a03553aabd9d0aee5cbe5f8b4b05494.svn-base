package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the WF_TIMEOUT_REMIND database table.
 * 
 */
@Entity
@Table(name="WF_TIMEOUT_REMIND")
@NamedQuery(name="WfTimeoutRemind.findAll", query="SELECT w FROM WfTimeoutRemind w")
public class WfTimeoutRemind extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="WF_COMPLETE_DATE")
	private Date wfCompleteDate;

	@Column(name="WF_ID")
	private long wfId;

	@Column(name="WF_INTERVAL_DAYS")
	private long wfIntervalDays;

	@Column(name="WF_URGE_DATE")
	private Date wfUrgeDate;

	public WfTimeoutRemind() {
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

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
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

	public Date getWfCompleteDate() {
		return this.wfCompleteDate;
	}

	public void setWfCompleteDate(Date wfCompleteDate) {
		this.wfCompleteDate = wfCompleteDate;
	}

	public long getWfId() {
		return this.wfId;
	}

	public void setWfId(long wfId) {
		this.wfId = wfId;
	}

	public long getWfIntervalDays() {
		return this.wfIntervalDays;
	}

	public void setWfIntervalDays(long wfIntervalDays) {
		this.wfIntervalDays = wfIntervalDays;
	}

	public Date getWfUrgeDate() {
		return this.wfUrgeDate;
	}

	public void setWfUrgeDate(Date wfUrgeDate) {
		this.wfUrgeDate = wfUrgeDate;
	}

}