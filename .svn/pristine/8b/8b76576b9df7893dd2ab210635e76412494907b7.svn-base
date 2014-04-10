package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the POSITION_TIMEOUT_REMIND database table.
 * 
 */
@Entity
@Table(name="POSITION_TIMEOUT_REMIND")
@NamedQuery(name="PositionTimeoutRemind.findAll", query="SELECT p FROM PositionTimeoutRemind p")
public class PositionTimeoutRemind extends com.wcs.base.model.IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="ENABLE_IND")
	private String enableInd;

	@Column(name="POSITIONE_NAME")
	private String positioneName;

	private String remarks;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="WP_INTERVAL_DAYS")
	private Long wpIntervalDays;

	@Column(name="WP_TIMEOUT_DAYS")
	private Long wpTimeoutDays;

	@Column(name="WP_URGE_DAYS")
	private Long wpUrgeDays;

	//bi-directional many-to-one association to WfTimeoutConfig
	@ManyToOne
	@JoinColumn(name="WF_TIMEOUT_CONFIG_ID")
	private WfTimeoutConfig wfTimeoutConfig;

	public PositionTimeoutRemind() {
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

	public String getEnableInd() {
		return this.enableInd;
	}

	public void setEnableInd(String enableInd) {
		this.enableInd = enableInd;
	}

	public String getPositioneName() {
		return this.positioneName;
	}

	public void setPositioneName(String positioneName) {
		this.positioneName = positioneName;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Long getWpIntervalDays() {
		return this.wpIntervalDays;
	}

	public void setWpIntervalDays(Long wpIntervalDays) {
		this.wpIntervalDays = wpIntervalDays;
	}

	public Long getWpTimeoutDays() {
		return this.wpTimeoutDays;
	}

	public void setWpTimeoutDays(Long wpTimeoutDays) {
		this.wpTimeoutDays = wpTimeoutDays;
	}

	public Long getWpUrgeDays() {
		return this.wpUrgeDays;
	}

	public void setWpUrgeDays(Long wpUrgeDays) {
		this.wpUrgeDays = wpUrgeDays;
	}

	public WfTimeoutConfig getWfTimeoutConfig() {
		return this.wfTimeoutConfig;
	}

	public void setWfTimeoutConfig(WfTimeoutConfig wfTimeoutConfig) {
		this.wfTimeoutConfig = wfTimeoutConfig;
	}

}