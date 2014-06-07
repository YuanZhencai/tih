package com.wcs.tih.system.controller.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyAnnualReturn;

/** 
* <p>Project: tih</p> 
* <p>Title: AnnualReturnVo.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
public class AnnualReturnVo extends IdModel implements Serializable {

	private static final long serialVersionUID = -7279362301944251505L;
	private CompanyAnnualReturn annualReturn;
	private Date returnYear;
	private String returnAccount;
	private String effective;
	private String actualReturnAccount;

	private String baseReturnAccount;
	
	private String shouldReturnAccount;

	private Date paymentDatetime;

	private String returnPurpose;

	private String remark;

	public CompanyAnnualReturn getAnnualReturn() {
		return annualReturn;
	}

	public void setAnnualReturn(CompanyAnnualReturn annualReturn) {
		this.annualReturn = annualReturn;
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

	public Date getReturnYear() {
		return returnYear;
	}

	public void setReturnYear(Date returnYear) {
		this.returnYear = returnYear;
	}

	public String getEffective() {
		return effective;
	}

	public void setEffective(String effective) {
		this.effective = effective;
	}

	public String getReturnAccount() {
		return returnAccount;
	}

	public void setReturnAccount(String returnAccount) {
		this.returnAccount = returnAccount;
	}

	public String getActualReturnAccount() {
		return actualReturnAccount;
	}

	public void setActualReturnAccount(String actualReturnAccount) {
		this.actualReturnAccount = actualReturnAccount;
	}

	public String getBaseReturnAccount() {
		return baseReturnAccount;
	}

	public void setBaseReturnAccount(String baseReturnAccount) {
		this.baseReturnAccount = baseReturnAccount;
	}

	public String getShouldReturnAccount() {
		return shouldReturnAccount;
	}

	public void setShouldReturnAccount(String shouldReturnAccount) {
		this.shouldReturnAccount = shouldReturnAccount;
	}


}
