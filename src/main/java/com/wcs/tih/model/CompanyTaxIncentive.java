package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wcs.common.model.Companymstr;

/**
 * The persistent class for the COMPANY_TAX_INCENTIVES database table.税收优惠
 * 
 */
@Entity
@Table(name = "COMPANY_TAX_INCENTIVES")
public class CompanyTaxIncentive extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private String policy;

    @Column(name = "PREFERENTIAL_END_DATETIME")
    private Date preferentialEndDatetime;

    @Column(name = "PREFERENTIAL_ITEM")
    private String preferentialItem;

    @Column(name = "PREFERENTIAL_START_DATETIME")
    private Date preferentialStartDatetime;

    @Column(name = "SITUATION_REMARKS")
    private String situationRemarks;

    @Column(name = "STATISTIC_DATETIME")
    private Date statisticDatetime;

    @Column(name = "TAX_TYPE")
    private String taxType;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "APPROVAL_ORGAN")
    private String approvalOrgan;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyTaxIncentive() {
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

    public String getPolicy() {
        return this.policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Date getPreferentialEndDatetime() {
        return this.preferentialEndDatetime;
    }

    public void setPreferentialEndDatetime(Date preferentialEndDatetime) {
        this.preferentialEndDatetime = preferentialEndDatetime;
    }

    public String getPreferentialItem() {
        return this.preferentialItem;
    }

    public void setPreferentialItem(String preferentialItem) {
        this.preferentialItem = preferentialItem;
    }

    public Date getPreferentialStartDatetime() {
        return this.preferentialStartDatetime;
    }

    public void setPreferentialStartDatetime(Date preferentialStartDatetime) {
        this.preferentialStartDatetime = preferentialStartDatetime;
    }

    public String getSituationRemarks() {
        return this.situationRemarks;
    }

    public void setSituationRemarks(String situationRemarks) {
        this.situationRemarks = situationRemarks;
    }

    public Date getStatisticDatetime() {
        return this.statisticDatetime;
    }

    public void setStatisticDatetime(Date statisticDatetime) {
        this.statisticDatetime = statisticDatetime;
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

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

    public String getApprovalOrgan() {
        return approvalOrgan;
    }

    public void setApprovalOrgan(String approvalOrgan) {
        this.approvalOrgan = approvalOrgan;
    }

}