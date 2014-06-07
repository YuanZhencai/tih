package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the REPORT_PAYABLE_TAX_ESTATE database table.
 * 
 */
@Entity
@Table(name = "REPORT_PAYABLE_TAX_ESTATE")
public class ReportPayableTaxEstate extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "CURR_DECLARE")
    private BigDecimal currDeclare;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

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

    @Column(name = "YEAR_ACCUM_DECLARE")
    private BigDecimal yearAccumDeclare;

    @Column(name = "YEAR_SHOULD_DECLARE")
    private BigDecimal yearShouldDeclare;

    // bi-directional many-to-one association to ReportPayableTax
    @ManyToOne
    @JoinColumn(name = "REPORT_PAYABLE_TAX_ID")
    private ReportPayableTax reportPayableTax;

    public ReportPayableTaxEstate() {
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

    public BigDecimal getCurrDeclare() {
        return this.currDeclare;
    }

    public void setCurrDeclare(BigDecimal currDeclare) {
        this.currDeclare = currDeclare;
    }

    public String getDefunctInd() {
        return this.defunctInd;
    }

    public void setDefunctInd(String defunctInd) {
        this.defunctInd = defunctInd;
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

    public BigDecimal getYearAccumDeclare() {
        return this.yearAccumDeclare;
    }

    public void setYearAccumDeclare(BigDecimal yearAccumDeclare) {
        this.yearAccumDeclare = yearAccumDeclare;
    }

    public BigDecimal getYearShouldDeclare() {
        return this.yearShouldDeclare;
    }

    public void setYearShouldDeclare(BigDecimal yearShouldDeclare) {
        this.yearShouldDeclare = yearShouldDeclare;
    }

    public ReportPayableTax getReportPayableTax() {
        return this.reportPayableTax;
    }

    public void setReportPayableTax(ReportPayableTax reportPayableTax) {
        this.reportPayableTax = reportPayableTax;
    }

}