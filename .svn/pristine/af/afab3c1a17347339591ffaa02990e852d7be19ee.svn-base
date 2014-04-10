package com.wcs.tih.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the WF_TIMEOUT_CONFIG database table.
 * 
 */
@Entity
@Table(name="WF_TIMEOUT_CONFIG")
@NamedQuery(name="WfTimeoutConfig.findAll", query="SELECT w FROM WfTimeoutConfig w")
public class WfTimeoutConfig extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="EFFECTIVE_DAYS")
	private long effectiveDays;

	@Column(name="ENABLE_IND")
	private String enableInd;

	@Column(name="JOB_ID")
	private String jobId;

	@Column(name="MAIL_IND")
	private String mailInd;

	@Column(name="POSITION_TIMEOUT_IND")
	private String positionTimeoutInd;

	private String remarks;

	@Column(name="SYS_NOTICE_IND")
	private String sysNoticeInd;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="WF_REQUESTFORM_TYPE")
	private String wfRequestformType;

	@Column(name="WF_TIMEOUT_IND")
	private String wfTimeoutInd;

	@Column(name="WF_TYPE")
	private String wfType;

	//bi-directional many-to-one association to PositionTimeoutRemind
	@OneToMany(mappedBy="wfTimeoutConfig", cascade = CascadeType.ALL)
	private List<PositionTimeoutRemind> positionTimeoutReminds;

	public WfTimeoutConfig() {
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

	public long getEffectiveDays() {
		return this.effectiveDays;
	}

	public void setEffectiveDays(long effectiveDays) {
		this.effectiveDays = effectiveDays;
	}

	public String getEnableInd() {
		return this.enableInd;
	}

	public void setEnableInd(String enableInd) {
		this.enableInd = enableInd;
	}

	public String getJobId() {
		return this.jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getMailInd() {
		return this.mailInd;
	}

	public void setMailInd(String mailInd) {
		this.mailInd = mailInd;
	}

	public String getPositionTimeoutInd() {
		return this.positionTimeoutInd;
	}

	public void setPositionTimeoutInd(String positionTimeoutInd) {
		this.positionTimeoutInd = positionTimeoutInd;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSysNoticeInd() {
		return this.sysNoticeInd;
	}

	public void setSysNoticeInd(String sysNoticeInd) {
		this.sysNoticeInd = sysNoticeInd;
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

	public String getWfRequestformType() {
		return this.wfRequestformType;
	}

	public void setWfRequestformType(String wfRequestformType) {
		this.wfRequestformType = wfRequestformType;
	}

	public String getWfTimeoutInd() {
		return this.wfTimeoutInd;
	}

	public void setWfTimeoutInd(String wfTimeoutInd) {
		this.wfTimeoutInd = wfTimeoutInd;
	}

	public String getWfType() {
		return this.wfType;
	}

	public void setWfType(String wfType) {
		this.wfType = wfType;
	}

	public List<PositionTimeoutRemind> getPositionTimeoutReminds() {
		return this.positionTimeoutReminds;
	}

	public void setPositionTimeoutReminds(List<PositionTimeoutRemind> positionTimeoutReminds) {
		this.positionTimeoutReminds = positionTimeoutReminds;
	}

	public PositionTimeoutRemind addPositionTimeoutRemind(PositionTimeoutRemind positionTimeoutRemind) {
		getPositionTimeoutReminds().add(positionTimeoutRemind);
		positionTimeoutRemind.setWfTimeoutConfig(this);

		return positionTimeoutRemind;
	}

	public PositionTimeoutRemind removePositionTimeoutRemind(PositionTimeoutRemind positionTimeoutRemind) {
		getPositionTimeoutReminds().remove(positionTimeoutRemind);
		positionTimeoutRemind.setWfTimeoutConfig(null);

		return positionTimeoutRemind;
	}

}