package com.wcs.tih.system.controller.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyAnnualTaxPay;
import com.wcs.tih.model.CompanyTaxTypeRatio;

public class CompanyAnnualTaxPayVO extends IdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private CompanyAnnualTaxPay taxPay = new CompanyAnnualTaxPay();
    private String taxRate;
    private String taxPayAccount;
    
    private String createdBy;

    private Date createdDatetime;

    private String defunctInd;


    private Date taxPayYear;

    private String updatedBy;

    private Date updatedDatetime;

	private String newTaxRate;

    private String remarks;
    
    private CompanyTaxTypeRatio companyTaxTypeRatio;
    
    public CompanyAnnualTaxPayVO() {
        
    }
    
    public CompanyAnnualTaxPayVO(CompanyAnnualTaxPay taxPay) {
        this.taxPay = taxPay;
    }

    public CompanyAnnualTaxPay getTaxPay() {
        return taxPay;
    }

    public void setTaxPay(CompanyAnnualTaxPay taxPay) {
        this.taxPay = taxPay;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

	public String getTaxPayAccount() {
		return taxPayAccount;
	}

	public void setTaxPayAccount(String taxPayAccount) {
		this.taxPayAccount = taxPayAccount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getDefunctInd() {
		return defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public Date getTaxPayYear() {
		return taxPayYear;
	}

	public void setTaxPayYear(Date taxPayYear) {
		this.taxPayYear = taxPayYear;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDatetime() {
		return updatedDatetime;
	}

	public void setUpdatedDatetime(Date updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}

	public String getNewTaxRate() {
		return newTaxRate;
	}

	public void setNewTaxRate(String newTaxRate) {
		this.newTaxRate = newTaxRate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public CompanyTaxTypeRatio getCompanyTaxTypeRatio() {
		return companyTaxTypeRatio;
	}

	public void setCompanyTaxTypeRatio(CompanyTaxTypeRatio companyTaxTypeRatio) {
		this.companyTaxTypeRatio = companyTaxTypeRatio;
	}
	
}
