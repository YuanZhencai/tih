package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the INVS_INSPECTATION_RESULT database table.
 * 
 */
@Entity
@Table(name="INVS_INSPECTATION_RESULT")
public class InvsInspectationResult extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private BigDecimal fine;

	@Column(name="INPUT_TAX_TURNS_OUT")
	private BigDecimal inputTaxTurnsOut;

	@Column(name="OVERDUE_TAX")
	private BigDecimal overdueTax;

	private BigDecimal penalty;

	@Column(name="REDUCTION_PREV_LOSS")
	private BigDecimal reductionPrevLoss;

	@Column(name="SITUATION_REMARKS")
	private String situationRemarks;

	@Column(name="TAX_TYPE")
	private String taxType;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to InvsInspectation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVS_INSPECTATION_ID")
	private InvsInspectation invsInspectation;

    public InvsInspectationResult() {
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

	public BigDecimal getFine() {
		return this.fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	public BigDecimal getInputTaxTurnsOut() {
		return this.inputTaxTurnsOut;
	}

	public void setInputTaxTurnsOut(BigDecimal inputTaxTurnsOut) {
		this.inputTaxTurnsOut = inputTaxTurnsOut;
	}

	public BigDecimal getOverdueTax() {
		return this.overdueTax;
	}

	public void setOverdueTax(BigDecimal overdueTax) {
		this.overdueTax = overdueTax;
	}

	public BigDecimal getPenalty() {
		return this.penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	public BigDecimal getReductionPrevLoss() {
		return this.reductionPrevLoss;
	}

	public void setReductionPrevLoss(BigDecimal reductionPrevLoss) {
		this.reductionPrevLoss = reductionPrevLoss;
	}

	public String getSituationRemarks() {
		return this.situationRemarks;
	}

	public void setSituationRemarks(String situationRemarks) {
		this.situationRemarks = situationRemarks;
	}

	public String getTaxType() {
		return this.taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
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

	public InvsInspectation getInvsInspectation() {
		return this.invsInspectation;
	}

	public void setInvsInspectation(InvsInspectation invsInspectation) {
		this.invsInspectation = invsInspectation;
	}
	
}