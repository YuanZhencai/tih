package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the REPORT_PAYABLE_TAX_OTHERS database table.
 * 
 */
@Entity
@Table(name="REPORT_PAYABLE_TAX_OTHERS")
public class ReportPayableTaxOther extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="BEGIN_YEAR_NOT_PAY")
	private BigDecimal beginYearNotPay;

	@Column(name="BEGIN_YEAR_OVERAGE")
	private BigDecimal beginYearOverage;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="CURR_MONTH_DECLARE")
	private BigDecimal currMonthDeclare;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private BigDecimal difference;

	@Column(name="END_YEAR_OVERAGE")
	private BigDecimal endYearOverage;

	private String remarks;

	@Column(name="TAX_CODE")
	private String taxCode;

	@Column(name="TAX_NAME")
	private String taxName;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="YEAR_ACCUM_NOT_PAY")
	private BigDecimal yearAccumNotPay;

	@Column(name="YEAR_ACCUM_PAIED")
	private BigDecimal yearAccumPaied;

	@Column(name="YEAR_ACCUM_SHOULD_PAY")
	private BigDecimal yearAccumShouldPay;

	//bi-directional many-to-one association to ReportPayableTax
    @ManyToOne
	@JoinColumn(name="REPORT_PAYABLE_TAX_ID")
	private ReportPayableTax reportPayableTax;

    public ReportPayableTaxOther() {
    }

	public BigDecimal getBeginYearNotPay() {
		return this.beginYearNotPay;
	}

	public void setBeginYearNotPay(BigDecimal beginYearNotPay) {
		this.beginYearNotPay = beginYearNotPay;
	}

	public BigDecimal getBeginYearOverage() {
		return this.beginYearOverage;
	}

	public void setBeginYearOverage(BigDecimal beginYearOverage) {
		this.beginYearOverage = beginYearOverage;
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

	public BigDecimal getCurrMonthDeclare() {
		return this.currMonthDeclare;
	}

	public void setCurrMonthDeclare(BigDecimal currMonthDeclare) {
		this.currMonthDeclare = currMonthDeclare;
	}

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public BigDecimal getDifference() {
		return this.difference;
	}

	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}

	public BigDecimal getEndYearOverage() {
		return this.endYearOverage;
	}

	public void setEndYearOverage(BigDecimal endYearOverage) {
		this.endYearOverage = endYearOverage;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getTaxName() {
		return this.taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
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

	public BigDecimal getYearAccumNotPay() {
		return this.yearAccumNotPay;
	}

	public void setYearAccumNotPay(BigDecimal yearAccumNotPay) {
		this.yearAccumNotPay = yearAccumNotPay;
	}

	public BigDecimal getYearAccumPaied() {
		return this.yearAccumPaied;
	}

	public void setYearAccumPaied(BigDecimal yearAccumPaied) {
		this.yearAccumPaied = yearAccumPaied;
	}

	public BigDecimal getYearAccumShouldPay() {
		return this.yearAccumShouldPay;
	}

	public void setYearAccumShouldPay(BigDecimal yearAccumShouldPay) {
		this.yearAccumShouldPay = yearAccumShouldPay;
	}

	public ReportPayableTax getReportPayableTax() {
		return this.reportPayableTax;
	}

	public void setReportPayableTax(ReportPayableTax reportPayableTax) {
		this.reportPayableTax = reportPayableTax;
	}
	
}