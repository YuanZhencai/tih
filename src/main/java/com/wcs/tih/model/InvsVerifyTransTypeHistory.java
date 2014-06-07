package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the INVS_VERIFY_TRANS_TYPE_HISTORY database table.
 * 
 */
@Entity
@Table(name="INVS_VERIFY_TRANS_TYPE_HISTORY")
public class InvsVerifyTransTypeHistory extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ADJUST_SPECIAL_REASON")
	private String adjustSpecialReason;

	@Column(name="AFTER_ADJUST_RATIO")
	private double afterAdjustRatio;

	@Column(name="BEFORE_ADJUST_RATIO")
	private double beforeAdjustRatio;

	@Column(name="COMPARE_COMPANY_MEDIAN")
	private double compareCompanyMedian;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="OPERATE_IND")
	private String operateInd;

	@Column(name="TRANS_TYPE")
	private String transType;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="VALIDATION_METHOD")
	private String validationMethod;

	//bi-directional many-to-one association to InvsTransferPriceHistory
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVS_TRANSFER_PRICE_HISTORY_ID")
	private InvsTransferPriceHistory invsTransferPriceHistory;

    public InvsVerifyTransTypeHistory() {
    }

	public String getAdjustSpecialReason() {
		return this.adjustSpecialReason;
	}

	public void setAdjustSpecialReason(String adjustSpecialReason) {
		this.adjustSpecialReason = adjustSpecialReason;
	}

	public double getAfterAdjustRatio() {
		return this.afterAdjustRatio;
	}

	public void setAfterAdjustRatio(double afterAdjustRatio) {
		this.afterAdjustRatio = afterAdjustRatio;
	}

	public double getBeforeAdjustRatio() {
		return this.beforeAdjustRatio;
	}

	public void setBeforeAdjustRatio(double beforeAdjustRatio) {
		this.beforeAdjustRatio = beforeAdjustRatio;
	}

	public double getCompareCompanyMedian() {
		return this.compareCompanyMedian;
	}

	public void setCompareCompanyMedian(double compareCompanyMedian) {
		this.compareCompanyMedian = compareCompanyMedian;
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

	public String getOperateInd() {
		return this.operateInd;
	}

	public void setOperateInd(String operateInd) {
		this.operateInd = operateInd;
	}

	public String getTransType() {
		return this.transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
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

	public String getValidationMethod() {
		return this.validationMethod;
	}

	public void setValidationMethod(String validationMethod) {
		this.validationMethod = validationMethod;
	}

	public InvsTransferPriceHistory getInvsTransferPriceHistory() {
		return this.invsTransferPriceHistory;
	}

	public void setInvsTransferPriceHistory(InvsTransferPriceHistory invsTransferPriceHistory) {
		this.invsTransferPriceHistory = invsTransferPriceHistory;
	}
	
}