package com.wcs.tih.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the COMPANY_DEPRECIATION database table.
 * 
 */
@Entity
@Table(name = "COMPANY_DEPRECIATION")
public class CompanyDepreciation extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal cost;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "NET_WORTH")
    private BigDecimal netWorth;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    @Column(name = "\"YEAR\"")
    private Date year;

    // bi-directional many-to-one association to CompanyMainAsset
    @ManyToOne
    @JoinColumn(name = "COMPANY_MAIN_ASSETS_ID")
    private CompanyMainAsset companyMainAsset;

    public CompanyDepreciation() {
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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

    public BigDecimal getNetWorth() {
        return this.netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
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

    public Date getYear() {
        return this.year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public CompanyMainAsset getCompanyMainAsset() {
        return this.companyMainAsset;
    }

    public void setCompanyMainAsset(CompanyMainAsset companyMainAsset) {
        this.companyMainAsset = companyMainAsset;
    }

}