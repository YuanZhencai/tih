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
 * The persistent class for the COMPANY_ANNUAL_RETURN database table.年返还
 * 
 */
@Entity
@Table(name = "COMPANY_ANNUAL_RETURN")
public class CompanyAnnualReturn extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ACTUAL_RETURN_ACCOUNT")
	private BigDecimal actualReturnAccount;

	@Column(name = "BASE_RETURN_ACCOUNT")
	private BigDecimal baseReturnAccount;
	
	@Column(name = "SHOULD_RETURN_ACCOUNT")
	private BigDecimal shouldReturnAccount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PAYMENT_DATETIME")
	private Date paymentDatetime;

	@Column(name = "RETURN_PURPOSE")
	private String returnPurpose;
	
	@Column(name = "REMARK")
	private String remark;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name = "DEFUNCT_IND")
	private String defunctInd;

	@Column(name = "RETURN_ACCOUNT")
	private BigDecimal returnAccount;

	@Column(name = "RETURN_YEAR")
	private Date returnYear;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATETIME")
	private Date updatedDatetime;

	// bi-directional many-to-one association to CompanyFinancialReturn
	@ManyToOne
	@JoinColumn(name = "COMPANY_FINANCIAL_RETURN_ID")
	private CompanyFinancialReturn companyFinancialReturn;

	public CompanyAnnualReturn() {
	}

	public BigDecimal getActualReturnAccount() {
		return actualReturnAccount;
	}

	public void setActualReturnAccount(BigDecimal actualReturnAccount) {
		this.actualReturnAccount = actualReturnAccount;
	}

	public BigDecimal getBaseReturnAccount() {
		return baseReturnAccount;
	}

	public void setBaseReturnAccount(BigDecimal baseReturnAccount) {
		this.baseReturnAccount = baseReturnAccount;
	}

	public BigDecimal getShouldReturnAccount() {
		return shouldReturnAccount;
	}

	public void setShouldReturnAccount(BigDecimal shouldReturnAccount) {
		this.shouldReturnAccount = shouldReturnAccount;
	}

	public Date getPaymentDatetime() {
		return paymentDatetime;
	}

	public void setPaymentDatetime(Date paymentDatetime) {
		this.paymentDatetime = paymentDatetime;
	}

	public String getReturnPurpose() {
		return returnPurpose;
	}

	public void setReturnPurpose(String returnPurpose) {
		this.returnPurpose = returnPurpose;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public BigDecimal getReturnAccount() {
		return this.returnAccount;
	}

	public void setReturnAccount(BigDecimal returnAccount) {
		this.returnAccount = returnAccount;
	}

	public Date getReturnYear() {
		return this.returnYear;
	}

	public void setReturnYear(Date returnYear) {
		this.returnYear = returnYear;
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

	public CompanyFinancialReturn getCompanyFinancialReturn() {
		return this.companyFinancialReturn;
	}

	public void setCompanyFinancialReturn(CompanyFinancialReturn companyFinancialReturn) {
		this.companyFinancialReturn = companyFinancialReturn;
	}

}