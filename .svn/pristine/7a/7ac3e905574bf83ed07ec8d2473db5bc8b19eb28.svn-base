package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the REPORT_PAYABLE_TAX_VAT database table.
 * 
 */
@Entity
@Table(name = "REPORT_PAYABLE_TAX_VAT")
public class ReportPayableTaxVat extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "BEGIN_YEAR_OVERAGE")
    private BigDecimal beginYearOverage;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "CUR_DECLARE_NUM")
    private double curDeclareNum;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private BigDecimal difference;

    @Column(name = "END_YEAR_OVERAGE")
    private BigDecimal endYearOverage;

    @Column(name = "ITEM_CODE")
    private String itemCode;

    @Column(name = "ITEM_NAME")
    private String itemName;

    private String remarks;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    @Column(name = "YEAR_ACCUM_HAVE_PAIED")
    private BigDecimal yearAccumHavePaied;

    @Column(name = "YEAR_ACCUM_NOT_PAY")
    private BigDecimal yearAccumNotPay;

    @Column(name = "YEAR_ACCUM_SHOULD_PAY")
    private BigDecimal yearAccumShouldPay;

    // bi-directional many-to-one association to ReportPayableTax
    @ManyToOne
    @JoinColumn(name = "REPORT_PAYABLE_TAX_ID")
    private ReportPayableTax reportPayableTax;

    public ReportPayableTaxVat() {
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

    public double getCurDeclareNum() {
        return this.curDeclareNum;
    }

    public void setCurDeclareNum(double curDeclareNum) {
        this.curDeclareNum = curDeclareNum;
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

    public String getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public BigDecimal getYearAccumHavePaied() {
        return this.yearAccumHavePaied;
    }

    public void setYearAccumHavePaied(BigDecimal yearAccumHavePaied) {
        this.yearAccumHavePaied = yearAccumHavePaied;
    }

    public BigDecimal getYearAccumNotPay() {
        return this.yearAccumNotPay;
    }

    public void setYearAccumNotPay(BigDecimal yearAccumNotPay) {
        this.yearAccumNotPay = yearAccumNotPay;
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