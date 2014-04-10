package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wcs.common.model.Companymstr;

/**
 * The persistent class for the COMPANY_MAIN_ASSETS database table.
 * 
 */
@Entity
@Table(name = "COMPANY_MAIN_ASSETS")
public class CompanyMainAsset extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double account;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "DEPRECIATION_TIMES")
    private Double depreciationTimes;

    private String item;

    private String unit;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to CompanyDepreciation
    @OneToMany(mappedBy = "companyMainAsset", fetch = FetchType.EAGER)
    private List<CompanyDepreciation> companyDepreciations;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyMainAsset() {
    }

    public Double getAccount() {
        return this.account;
    }

    public void setAccount(Double account) {
        this.account = account;
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

    public Double getDepreciationTimes() {
        return this.depreciationTimes;
    }

    public void setDepreciationTimes(Double depreciationTimes) {
        this.depreciationTimes = depreciationTimes;
    }

    public String getItem() {
        return this.item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public List<CompanyDepreciation> getCompanyDepreciations() {
        return this.companyDepreciations;
    }

    public void setCompanyDepreciations(List<CompanyDepreciation> companyDepreciations) {
        this.companyDepreciations = companyDepreciations;
    }

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

}