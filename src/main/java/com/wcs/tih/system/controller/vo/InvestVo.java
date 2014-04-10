/** * InvestVo.java 
* Created on 2014年3月20日 上午9:33:23 
*/

package com.wcs.tih.system.controller.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyInvestment;

/** 
 * <p>Project: tih</p> 
 * <p>Title: InvestVo.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */

public class InvestVo {
	private CompanyInvestment invest;
    private String investee;
    
    private String investAddress;
    
    private String investmentRatio;
    
    private String currency;

    private String defunctInd;

    private Date endDatetime;

    private String investAccount;

    private String phase;

    private Date startDatetime;

    private Companymstr companymstr;

    private String createdBy;

    private Date createdDatetime;
    private String updatedBy;

    private Date updatedDatetime;

	public CompanyInvestment getInvest() {
		return invest;
	}

	public void setInvest(CompanyInvestment invest) {
		this.invest = invest;
	}

	public String getInvestee() {
		return investee;
	}

	public void setInvestee(String investee) {
		this.investee = investee;
	}

	public String getInvestAddress() {
		return investAddress;
	}

	public void setInvestAddress(String investAddress) {
		this.investAddress = investAddress;
	}

	public String getInvestmentRatio() {
		return investmentRatio;
	}

	public void setInvestmentRatio(String investmentRatio) {
		this.investmentRatio = investmentRatio;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDefunctInd() {
		return defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getInvestAccount() {
		return investAccount;
	}

	public void setInvestAccount(String investAccount) {
		this.investAccount = investAccount;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public Date getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Companymstr getCompanymstr() {
		return companymstr;
	}

	public void setCompanymstr(Companymstr companymstr) {
		this.companymstr = companymstr;
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
    
	public InvestVo getInvestVo(CompanyInvestment invest) {
		InvestVo investVo = new InvestVo();
		investVo.setCompanymstr(invest.getCompanymstr());
		investVo.setCreatedBy(invest.getCreatedBy());
		investVo.setCreatedDatetime(invest.getCreatedDatetime());
		investVo.setCurrency(invest.getCurrency());
		investVo.setDefunctInd(invest.getDefunctInd());
		investVo.setEndDatetime(invest.getEndDatetime());
		investVo.setInvest(invest);
		investVo.setInvestAccount(invest.getInvestAccount() + "");
		investVo.setInvestmentRatio(invest.getInvestmentRatio());
		investVo.setPhase(invest.getPhase());
		investVo.setStartDatetime(invest.getStartDatetime());
		investVo.setUpdatedBy(invest.getUpdatedBy());
		investVo.setUpdatedDatetime(invest.getUpdatedDatetime());
		return investVo;
	}
    
}
