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
 * The persistent class for the COMPANY_INVESTMENT database table.
 * 
 */
@Entity
@Table(name = "COMPANY_INVESTMENT")
public class CompanyInvestment extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String investee;
    
    @Column(name = "INVEST_ADDRESS")
    private String investAddress;
    
    @Column(name = "INVESTMENT_RATIO")
    private String investmentRatio;
    
    private String currency;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "END_DATETIME")
    private Date endDatetime;

    @Column(name = "INVEST_ACCOUNT")
    private BigDecimal investAccount;

    private String phase;

    @Column(name = "START_DATETIME")
    private Date startDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    @JoinColumn(name="COMPANYMSTR_ID")
    private Companymstr companymstr;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;
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

    public CompanyInvestment() {
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

    public Date getEndDatetime() {
        return this.endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public BigDecimal getInvestAccount() {
        return this.investAccount;
    }

    public void setInvestAccount(BigDecimal investAccount) {
        this.investAccount = investAccount;
    }

    public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public Date getStartDatetime() {
        return this.startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

	public String getInvestee() {
		return investee;
	}

	public void setInvestee(String investee) {
		this.investee = investee;
	}

	public String getInvestmentRatio() {
		return investmentRatio;
	}

	public void setInvestmentRatio(String investmentRatio) {
		this.investmentRatio = investmentRatio;
	}

	public String getInvestAddress() {
		return investAddress;
	}

	public void setInvestAddress(String investAddress) {
		this.investAddress = investAddress;
	}

	
}