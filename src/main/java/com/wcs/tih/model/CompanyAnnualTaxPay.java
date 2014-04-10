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
 * The persistent class for the COMPANY_ANNUAL_TAX_PAY database table.
 * 
 */
@Entity
@Table(name = "COMPANY_ANNUAL_TAX_PAY")
public class CompanyAnnualTaxPay extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "TAX_PAY_ACCOUNT")
    private BigDecimal taxPayAccount;

    @Column(name = "TAX_PAY_YEAR")
    private Date taxPayYear;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    @Column(name="NEW_TAX_RATE")
	private String newTaxRate;

    private String remarks;
    
    // bi-directional many-to-one association to CompanyTaxTypeRatio
    @ManyToOne
    @JoinColumn(name = "COMPANY_TAX_TYPE_RATIO_ID")
    private CompanyTaxTypeRatio companyTaxTypeRatio;

    public CompanyAnnualTaxPay() {
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

    public BigDecimal getTaxPayAccount() {
        return this.taxPayAccount;
    }

    public void setTaxPayAccount(BigDecimal taxPayAccount) {
        this.taxPayAccount = taxPayAccount;
    }

    public Date getTaxPayYear() {
        return this.taxPayYear;
    }

    public void setTaxPayYear(Date taxPayYear) {
        this.taxPayYear = taxPayYear;
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

    public CompanyTaxTypeRatio getCompanyTaxTypeRatio() {
        return this.companyTaxTypeRatio;
    }

    public void setCompanyTaxTypeRatio(CompanyTaxTypeRatio companyTaxTypeRatio) {
        this.companyTaxTypeRatio = companyTaxTypeRatio;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	public String getNewTaxRate() {
		return newTaxRate;
	}

	public void setNewTaxRate(String newTaxRate) {
		this.newTaxRate = newTaxRate;
	}

}