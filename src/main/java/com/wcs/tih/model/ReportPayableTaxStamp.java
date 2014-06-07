package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the REPORT_PAYABLE_TAX_STAMP database table.
 * 
 */
@Entity
@Table(name = "REPORT_PAYABLE_TAX_STAMP")
public class ReportPayableTaxStamp extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CONTRACT_AMOUNT")
    private BigDecimal contractAmount;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "TAX_RATE")
    private double taxRate;

    @Column(name = "TAX_RATING")
    private String taxRating;

    @Column(name = "TAX_RATING_CODE")
    private String taxRatingCode;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    @Column(name = "YEAR_ACCUM_DECLARE")
    private BigDecimal yearAccumDeclare;

    // bi-directional many-to-one association to ReportPayableTax
    @ManyToOne
    @JoinColumn(name = "REPORT_PAYABLE_TAX_ID")
    private ReportPayableTax reportPayableTax;

    public ReportPayableTaxStamp() {
    }

    public BigDecimal getContractAmount() {
        return this.contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
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

    public double getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxRating() {
        return this.taxRating;
    }

    public void setTaxRating(String taxRating) {
        this.taxRating = taxRating;
    }

    public String getTaxRatingCode() {
        return this.taxRatingCode;
    }

    public void setTaxRatingCode(String taxRatingCode) {
        this.taxRatingCode = taxRatingCode;
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

    public BigDecimal getYearAccumDeclare() {
        return this.yearAccumDeclare;
    }

    public void setYearAccumDeclare(BigDecimal yearAccumDeclare) {
        this.yearAccumDeclare = yearAccumDeclare;
    }

    public ReportPayableTax getReportPayableTax() {
        return this.reportPayableTax;
    }

    public void setReportPayableTax(ReportPayableTax reportPayableTax) {
        this.reportPayableTax = reportPayableTax;
    }

}