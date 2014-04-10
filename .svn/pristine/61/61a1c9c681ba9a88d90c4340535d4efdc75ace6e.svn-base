package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the REPORT_PAYABLE_TAX_INCOME database table.
 * 
 */
@Entity
@Table(name = "REPORT_PAYABLE_TAX_INCOME")
public class ReportPayableTaxIncome extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "EVALUATE_AMMOUNT")
    private BigDecimal evaluateAmmount;

    @Column(name = "FINAL_SET_DECLARE")
    private BigDecimal finalSetDeclare;

    @Column(name = "FIRST_SEASON")
    private BigDecimal firstSeason;

    @Column(name = "FOURTH_SEASON")
    private BigDecimal fourthSeason;

    @Column(name = "RETURN_AMMOUNT")
    private BigDecimal returnAmmount;

    @Column(name = "SECOND_SEASON")
    private BigDecimal secondSeason;

    @Column(name = "SHOULD_ADD_NOR_RETRUN")
    private BigDecimal shouldAddNorRetrun;

    @Column(name = "\"SUM\"")
    private BigDecimal sum;

    @Column(name = "THIRD_SEASON")
    private BigDecimal thirdSeason;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to ReportPayableTax
    @ManyToOne
    @JoinColumn(name = "REPORT_PAYABLE_TAX_ID")
    private ReportPayableTax reportPayableTax;

    public ReportPayableTaxIncome() {
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

    public BigDecimal getEvaluateAmmount() {
        return this.evaluateAmmount;
    }

    public void setEvaluateAmmount(BigDecimal evaluateAmmount) {
        this.evaluateAmmount = evaluateAmmount;
    }

    public BigDecimal getFinalSetDeclare() {
        return this.finalSetDeclare;
    }

    public void setFinalSetDeclare(BigDecimal finalSetDeclare) {
        this.finalSetDeclare = finalSetDeclare;
    }

    public BigDecimal getFirstSeason() {
        return this.firstSeason;
    }

    public void setFirstSeason(BigDecimal firstSeason) {
        this.firstSeason = firstSeason;
    }

    public BigDecimal getFourthSeason() {
        return this.fourthSeason;
    }

    public void setFourthSeason(BigDecimal fourthSeason) {
        this.fourthSeason = fourthSeason;
    }

    public BigDecimal getReturnAmmount() {
        return this.returnAmmount;
    }

    public void setReturnAmmount(BigDecimal returnAmmount) {
        this.returnAmmount = returnAmmount;
    }

    public BigDecimal getSecondSeason() {
        return this.secondSeason;
    }

    public void setSecondSeason(BigDecimal secondSeason) {
        this.secondSeason = secondSeason;
    }

    public BigDecimal getShouldAddNorRetrun() {
        return this.shouldAddNorRetrun;
    }

    public void setShouldAddNorRetrun(BigDecimal shouldAddNorRetrun) {
        this.shouldAddNorRetrun = shouldAddNorRetrun;
    }

    public BigDecimal getSum() {
        return this.sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getThirdSeason() {
        return this.thirdSeason;
    }

    public void setThirdSeason(BigDecimal thirdSeason) {
        this.thirdSeason = thirdSeason;
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