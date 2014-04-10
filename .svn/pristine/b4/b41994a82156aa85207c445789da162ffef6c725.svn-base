package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the REPORT_VAT_IPT_DEDUCTION_DETAIL database table.
 * 
 */
@Entity
@Table(name="REPORT_VAT_IPT_DEDUCTION_DETAIL")
public class ReportVatIptDeductionDetail extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private double ammount;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="INVOICE_TYPE_CODE")
	private String invoiceTypeCode;

	@Column(name="INVOICE_TYPE_NAME")
	private String invoiceTypeName;

	@Column(name="MONEY_SUM")
	private BigDecimal moneySum;

	@Column(name="TAX_AMMOUNT")
	private BigDecimal taxAmmount;

	@Column(name="TAX_RATE")
	private double taxRate;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="VARIETY_CODE")
	private String varietyCode;

	@Column(name="VARIETY_NAME")
	private String varietyName;

	//bi-directional many-to-one association to ReportVatIptDeduction
    @ManyToOne
	@JoinColumn(name="REPORT_VAT_IPT_DEDUCTION_ID")
	private ReportVatIptDeduction reportVatIptDeduction;

    public ReportVatIptDeductionDetail() {
    }

	public double getAmmount() {
		return this.ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
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

	public String getInvoiceTypeCode() {
		return this.invoiceTypeCode;
	}

	public void setInvoiceTypeCode(String invoiceTypeCode) {
		this.invoiceTypeCode = invoiceTypeCode;
	}

	public String getInvoiceTypeName() {
		return this.invoiceTypeName;
	}

	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}

	public BigDecimal getMoneySum() {
		return this.moneySum;
	}

	public void setMoneySum(BigDecimal moneySum) {
		this.moneySum = moneySum;
	}

	public BigDecimal getTaxAmmount() {
		return this.taxAmmount;
	}

	public void setTaxAmmount(BigDecimal taxAmmount) {
		this.taxAmmount = taxAmmount;
	}

	public double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
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

	public String getVarietyCode() {
		return this.varietyCode;
	}

	public void setVarietyCode(String varietyCode) {
		this.varietyCode = varietyCode;
	}

	public String getVarietyName() {
		return this.varietyName;
	}

	public void setVarietyName(String varietyName) {
		this.varietyName = varietyName;
	}

	public ReportVatIptDeduction getReportVatIptDeduction() {
		return this.reportVatIptDeduction;
	}

	public void setReportVatIptDeduction(ReportVatIptDeduction reportVatIptDeduction) {
		this.reportVatIptDeduction = reportVatIptDeduction;
	}
	
}