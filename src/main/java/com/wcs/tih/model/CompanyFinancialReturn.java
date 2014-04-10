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
 * The persistent class for the COMPANY_FINANCIAL_RETURN database table.财政返还
 * 
 */
@Entity
@Table(name = "COMPANY_FINANCIAL_RETURN")
public class CompanyFinancialReturn extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "REGISTRATION")
    private String registration;
    
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private String itme;

    @Column(name = "RETURN_ACCORDING")
    private String returnAccording;

    @Column(name = "RETURN_BASE")
    private String returnBase;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RETURN_END_DATETIME")
    private Date returnEndDatetime;

    @Column(name = "RETURN_RATIO")
    private String returnRatio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RETURN_START_DATETIME")
    private Date returnStartDatetime;

    @Column(name = "TAX_TYPE")
    private String taxType;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to CompanyAnnualReturn
    @OneToMany(mappedBy = "companyFinancialReturn", fetch = FetchType.EAGER)
    private List<CompanyAnnualReturn> companyAnnualReturns;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyFinancialReturn() {
    }

    public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
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

    public String getItme() {
        return this.itme;
    }

    public void setItme(String itme) {
        this.itme = itme;
    }

    public String getReturnAccording() {
        return this.returnAccording;
    }

    public void setReturnAccording(String returnAccording) {
        this.returnAccording = returnAccording;
    }

    public String getReturnBase() {
        return this.returnBase;
    }

    public void setReturnBase(String returnBase) {
        this.returnBase = returnBase;
    }

    public Date getReturnEndDatetime() {
        return this.returnEndDatetime;
    }

    public void setReturnEndDatetime(Date returnEndDatetime) {
        this.returnEndDatetime = returnEndDatetime;
    }

    public String getReturnRatio() {
        return this.returnRatio;
    }

    public void setReturnRatio(String returnRatio) {
        this.returnRatio = returnRatio;
    }

    public Date getReturnStartDatetime() {
        return this.returnStartDatetime;
    }

    public void setReturnStartDatetime(Date returnStartDatetime) {
        this.returnStartDatetime = returnStartDatetime;
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

    public List<CompanyAnnualReturn> getCompanyAnnualReturns() {
        return this.companyAnnualReturns;
    }

    public void setCompanyAnnualReturns(List<CompanyAnnualReturn> companyAnnualReturns) {
        this.companyAnnualReturns = companyAnnualReturns;
    }

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

}