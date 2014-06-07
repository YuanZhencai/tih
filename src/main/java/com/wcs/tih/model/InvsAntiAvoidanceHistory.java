package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the INVS_ANTI_AVOIDANCE_HISTORY database table.
 * 
 */
@Entity
@Table(name="INVS_ANTI_AVOIDANCE_HISTORY")
public class InvsAntiAvoidanceHistory extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cause;

	@Column(name="COMPANY_NAME")
	private String companyName;

	@Column(name="COMPANYMSTR_ID")
	private long companymstrId;

	private String conclusion;

	private String contact;

	@Column(name="CONTACT_NUM")
	private String contactNum;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEAL_WITH")
	private String dealWith;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String doubt;

	@Column(name="IMPLEMENT_ORG")
	private String implementOrg;

	@Column(name="INVEST_END_DATETIME")
	private Date investEndDatetime;

	@Column(name="INVEST_START_DATETIME")
	private Date investStartDatetime;

	@Column(name="INVEST_TYPE")
	private String investType;

	@Column(name="\"METHOD\"")
	private String method;

	@Column(name="MISSION_END_DATETIME")
	private Date missionEndDatetime;

	@Column(name="MISSION_START_DATETIME")
	private Date missionStartDatetime;

	@Column(name="OPERATE_IND")
	private String operateInd;

	@Column(name="PHASE_REMARKS")
	private String phaseRemarks;

	@Column(name="RISK_ACCOUNT")
	private BigDecimal riskAccount;

	@Column(name="SPONSOR_ORG")
	private String sponsorOrg;

	@Column(name="TAX_TYPES")
	private String taxTypes;

	@Column(name="TRACE_END_DATETIME")
	private Date traceEndDatetime;

	@Column(name="TRACE_START_DATETIME")
	private Date traceStartDatetime;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="WF_ID")
	private long wfId;

	//bi-directional many-to-one association to InvsAntiAvoidance
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVS_ANTI_AVOIDANCE_ID")
	private InvsAntiAvoidance invsAntiAvoidance;

	//bi-directional many-to-one association to InvsAntiResultHistory
	@OneToMany(mappedBy="invsAntiAvoidanceHistory")
	private List<InvsAntiResultHistory> invsAntiResultHistories;

    public InvsAntiAvoidanceHistory() {
    }

	public String getCause() {
		return this.cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getCompanymstrId() {
		return this.companymstrId;
	}

	public void setCompanymstrId(long companymstrId) {
		this.companymstrId = companymstrId;
	}

	public String getConclusion() {
		return this.conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactNum() {
		return this.contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
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

	public String getDealWith() {
		return this.dealWith;
	}

	public void setDealWith(String dealWith) {
		this.dealWith = dealWith;
	}

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public String getDoubt() {
		return this.doubt;
	}

	public void setDoubt(String doubt) {
		this.doubt = doubt;
	}

	public String getImplementOrg() {
		return this.implementOrg;
	}

	public void setImplementOrg(String implementOrg) {
		this.implementOrg = implementOrg;
	}

	public Date getInvestEndDatetime() {
		return this.investEndDatetime;
	}

	public void setInvestEndDatetime(Date investEndDatetime) {
		this.investEndDatetime = investEndDatetime;
	}

	public Date getInvestStartDatetime() {
		return this.investStartDatetime;
	}

	public void setInvestStartDatetime(Date investStartDatetime) {
		this.investStartDatetime = investStartDatetime;
	}

	public String getInvestType() {
		return this.investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Date getMissionEndDatetime() {
		return this.missionEndDatetime;
	}

	public void setMissionEndDatetime(Date missionEndDatetime) {
		this.missionEndDatetime = missionEndDatetime;
	}

	public Date getMissionStartDatetime() {
		return this.missionStartDatetime;
	}

	public void setMissionStartDatetime(Date missionStartDatetime) {
		this.missionStartDatetime = missionStartDatetime;
	}

	public String getOperateInd() {
		return this.operateInd;
	}

	public void setOperateInd(String operateInd) {
		this.operateInd = operateInd;
	}

	public String getPhaseRemarks() {
		return this.phaseRemarks;
	}

	public void setPhaseRemarks(String phaseRemarks) {
		this.phaseRemarks = phaseRemarks;
	}

	public BigDecimal getRiskAccount() {
		return this.riskAccount;
	}

	public void setRiskAccount(BigDecimal riskAccount) {
		this.riskAccount = riskAccount;
	}

	public String getSponsorOrg() {
		return this.sponsorOrg;
	}

	public void setSponsorOrg(String sponsorOrg) {
		this.sponsorOrg = sponsorOrg;
	}

	public String getTaxTypes() {
		return this.taxTypes;
	}

	public void setTaxTypes(String taxTypes) {
		this.taxTypes = taxTypes;
	}

	public Date getTraceEndDatetime() {
		return this.traceEndDatetime;
	}

	public void setTraceEndDatetime(Date traceEndDatetime) {
		this.traceEndDatetime = traceEndDatetime;
	}

	public Date getTraceStartDatetime() {
		return this.traceStartDatetime;
	}

	public void setTraceStartDatetime(Date traceStartDatetime) {
		this.traceStartDatetime = traceStartDatetime;
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

	public long getWfId() {
		return this.wfId;
	}

	public void setWfId(long wfId) {
		this.wfId = wfId;
	}

	public InvsAntiAvoidance getInvsAntiAvoidance() {
		return this.invsAntiAvoidance;
	}

	public void setInvsAntiAvoidance(InvsAntiAvoidance invsAntiAvoidance) {
		this.invsAntiAvoidance = invsAntiAvoidance;
	}
	
	public List<InvsAntiResultHistory> getInvsAntiResultHistories() {
		return this.invsAntiResultHistories;
	}

	public void setInvsAntiResultHistories(List<InvsAntiResultHistory> invsAntiResultHistories) {
		this.invsAntiResultHistories = invsAntiResultHistories;
	}
	
}