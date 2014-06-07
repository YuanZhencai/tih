package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the REPORT_PAYABLE_TAX_STAYED database table.
 * 
 */
@Entity
@Table(name = "REPORT_PAYABLE_TAX_STAYED")
public class ReportPayableTaxStayed extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "\"MONTH\"")
    private int month;

    @Column(name = "MONTHLY_AMMOUNT")
    private BigDecimal monthlyAmmount;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to ReportPayableTax
    @ManyToOne
    @JoinColumn(name = "REPORT_PAYABLE_TAX_ID")
    private ReportPayableTax reportPayableTax;

    public ReportPayableTaxStayed() {
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

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getMonthlyAmmount() {
        return this.monthlyAmmount;
    }

    public void setMonthlyAmmount(BigDecimal monthlyAmmount) {
        this.monthlyAmmount = monthlyAmmount;
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

    public ReportPayableTax getReportPayableTax() {
        return this.reportPayableTax;
    }

    public void setReportPayableTax(ReportPayableTax reportPayableTax) {
        this.reportPayableTax = reportPayableTax;
    }

}