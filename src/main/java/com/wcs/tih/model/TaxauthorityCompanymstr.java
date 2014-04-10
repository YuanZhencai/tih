package com.wcs.tih.model;

import java.io.Serializable;
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
 * The persistent class for the TAXAUTHORITY_COMPANYMSTR database table.
 * 
 */
@Entity
@Table(name = "TAXAUTHORITY_COMPANYMSTR")
public class TaxauthorityCompanymstr extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "TAXPAYER_IDENTIFIER")
    private String taxpayerIdentifier;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    @JoinColumn(name="COMPANYMSTR_ID")
    private Companymstr companymstr;

    // bi-directional many-to-one association to Taxauthority
    @ManyToOne
    private Taxauthority taxauthority;

    public TaxauthorityCompanymstr() {
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

    public String getTaxpayerIdentifier() {
        return this.taxpayerIdentifier;
    }

    public void setTaxpayerIdentifier(String taxpayerIdentifier) {
        this.taxpayerIdentifier = taxpayerIdentifier;
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
        return companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

    public Taxauthority getTaxauthority() {
        return taxauthority;
    }

    public void setTaxauthority(Taxauthority taxauthority) {
        this.taxauthority = taxauthority;
    }

}