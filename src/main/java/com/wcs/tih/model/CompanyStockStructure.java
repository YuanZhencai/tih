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

import com.wcs.common.model.Companymstr;

/**
 * The persistent class for the COMPANY_STOCK_STRUCTURE database table.
 * 
 */
@Entity
@Table(name = "COMPANY_STOCK_STRUCTURE")
public class CompanyStockStructure extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    


    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    private String currency;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private double ratio;

    @Column(name = "REGISTERED_CAPITAL")
    private BigDecimal registeredCapital;

    private String shareholder;

    @Column(name = "\"TYPE\"")
    private String type;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name ="STATISTICS_DATETIME")
    private Date  statisticsDatetime;
    public Date getStatisticsDatetime() {
        return statisticsDatetime;
    }

    public void setStatisticsDatetime(Date statisticsDatetime) {
        this.statisticsDatetime = statisticsDatetime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    @JoinColumn(name="COMPANYMSTR_ID")
    private Companymstr companymstr;

    public CompanyStockStructure() {
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

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDefunctInd() {
        return this.defunctInd;
    }

    public void setDefunctInd(String defunctInd) {
        this.defunctInd = defunctInd;
    }

    public double getRatio() {
        return this.ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getRegisteredCapital() {
        return this.registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getShareholder() {
        return this.shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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