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
 * The persistent class for the COMPANY_BRANCH database table.分支机构
 * 
 */
@Entity
@Table(name = "COMPANY_BRANCH")
public class CompanyBranch extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bukrs;

    @Column(name = "BUSINESS_SCOPE")
    private String businessScope;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private String location;

    private String name;

    private String remakrs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SETUP_DATETIME")
    private Date setupDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATISTIC_DATETIME")
    private Date statisticDatetime;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyBranch() {
    }

    public String getBukrs() {
        return this.bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getBusinessScope() {
        return this.businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
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

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemakrs() {
        return this.remakrs;
    }

    public void setRemakrs(String remakrs) {
        this.remakrs = remakrs;
    }

    public Date getSetupDatetime() {
        return this.setupDatetime;
    }

    public void setSetupDatetime(Date setupDatetime) {
        this.setupDatetime = setupDatetime;
    }

    public Date getStatisticDatetime() {
        return this.statisticDatetime;
    }

    public void setStatisticDatetime(Date statisticDatetime) {
        this.statisticDatetime = statisticDatetime;
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

}