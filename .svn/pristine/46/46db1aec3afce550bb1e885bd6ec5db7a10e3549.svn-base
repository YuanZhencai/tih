package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the INVS_ANTI_RESULT_HISTORY database table.
 * 
 */
@Entity
@Table(name="INVS_ANTI_RESULT_HISTORY")
public class InvsAntiResultHistory extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ADD_FINE")
	private BigDecimal addFine;

	@Column(name="ADD_INTEREST")
	private BigDecimal addInterest;

	private BigDecimal cit;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="OPERATE_IND")
	private String operateInd;

	@Column(name="REDUCED_LOSS")
	private BigDecimal reducedLoss;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	private BigDecimal vat;

	//bi-directional many-to-one association to InvsAntiAvoidanceHistory
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVS_ANTI_AVOIDANCE_HISTORY_ID")
	private InvsAntiAvoidanceHistory invsAntiAvoidanceHistory;

    public InvsAntiResultHistory() {
    }

	public BigDecimal getAddFine() {
		return this.addFine;
	}

	public void setAddFine(BigDecimal addFine) {
		this.addFine = addFine;
	}

	public BigDecimal getAddInterest() {
		return this.addInterest;
	}

	public void setAddInterest(BigDecimal addInterest) {
		this.addInterest = addInterest;
	}

	public BigDecimal getCit() {
		return this.cit;
	}

	public void setCit(BigDecimal cit) {
		this.cit = cit;
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

	public BigDecimal getReducedLoss() {
		return this.reducedLoss;
	}

	public void setReducedLoss(BigDecimal reducedLoss) {
		this.reducedLoss = reducedLoss;
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

	public BigDecimal getVat() {
		return this.vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public InvsAntiAvoidanceHistory getInvsAntiAvoidanceHistory() {
		return this.invsAntiAvoidanceHistory;
	}

	public void setInvsAntiAvoidanceHistory(InvsAntiAvoidanceHistory invsAntiAvoidanceHistory) {
		this.invsAntiAvoidanceHistory = invsAntiAvoidanceHistory;
	}
	
}