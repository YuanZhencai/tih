package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the INVS_INSPECTATION database table.
 * 
 */
@Entity
@Table(name="INVS_INSPECTATION")
public class InvsInspectation extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="COMPANY_NAME")
	private String companyName;

	@Column(name="COMPANYMSTR_ID")
	private long companymstrId;

	private String contact;

	@Column(name="CONTACT_NUM")
	private String contactNum;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="INSPECT_END_DATETIME")
	private Date inspectEndDatetime;

	@Column(name="INSPECT_ORG")
	private String inspectOrg;

	@Column(name="INSPECT_START_DATETIME")
	private Date inspectStartDatetime;

	@Column(name="INSPECT_TYPE")
	private String inspectType;

	@Column(name="MAIN_PROBLEM_DESC")
	private String mainProblemDesc;

	@Column(name="MISSION_END_DATETIME")
	private Date missionEndDatetime;

	@Column(name="MISSION_START_DATETIME")
	private Date missionStartDatetime;

	@Column(name="RECTIFICATION_PLAN")
	private String rectificationPlan;

	@Column(name="RECTIFICATION_RESULT")
	private String rectificationResult;

	@Column(name="TAX_TYPES")
	private String taxTypes;

	@Column(name="TOTAL_FINE")
	private BigDecimal totalFine;

	@Column(name="TOTAL_PENALTY")
	private BigDecimal totalPenalty;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	@Column(name="WF_ID")
	private long wfId;

	//bi-directional many-to-one association to InvsInspectationHistory
	@OneToMany(mappedBy="invsInspectation")
	private List<InvsInspectationHistory> invsInspectationHistories;

	//bi-directional many-to-one association to InvsInspectationResult
	@OneToMany(mappedBy="invsInspectation")
	private List<InvsInspectationResult> invsInspectationResults;

    public InvsInspectation() {
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

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public Date getInspectEndDatetime() {
		return this.inspectEndDatetime;
	}

	public void setInspectEndDatetime(Date inspectEndDatetime) {
		this.inspectEndDatetime = inspectEndDatetime;
	}

	public String getInspectOrg() {
		return this.inspectOrg;
	}

	public void setInspectOrg(String inspectOrg) {
		this.inspectOrg = inspectOrg;
	}

	public Date getInspectStartDatetime() {
		return this.inspectStartDatetime;
	}

	public void setInspectStartDatetime(Date inspectStartDatetime) {
		this.inspectStartDatetime = inspectStartDatetime;
	}

	public String getInspectType() {
		return this.inspectType;
	}

	public void setInspectType(String inspectType) {
		this.inspectType = inspectType;
	}

	public String getMainProblemDesc() {
		return this.mainProblemDesc;
	}

	public void setMainProblemDesc(String mainProblemDesc) {
		this.mainProblemDesc = mainProblemDesc;
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

	public String getRectificationPlan() {
		return this.rectificationPlan;
	}

	public void setRectificationPlan(String rectificationPlan) {
		this.rectificationPlan = rectificationPlan;
	}

	public String getRectificationResult() {
		return this.rectificationResult;
	}

	public void setRectificationResult(String rectificationResult) {
		this.rectificationResult = rectificationResult;
	}

	public String getTaxTypes() {
		return this.taxTypes;
	}

	public void setTaxTypes(String taxTypes) {
		this.taxTypes = taxTypes;
	}

	public BigDecimal getTotalFine() {
		return this.totalFine;
	}

	public void setTotalFine(BigDecimal totalFine) {
		this.totalFine = totalFine;
	}

	public BigDecimal getTotalPenalty() {
		return this.totalPenalty;
	}

	public void setTotalPenalty(BigDecimal totalPenalty) {
		this.totalPenalty = totalPenalty;
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

	public List<InvsInspectationHistory> getInvsInspectationHistories() {
		return this.invsInspectationHistories;
	}

	public void setInvsInspectationHistories(List<InvsInspectationHistory> invsInspectationHistories) {
		this.invsInspectationHistories = invsInspectationHistories;
	}
	
	public List<InvsInspectationResult> getInvsInspectationResults() {
		return this.invsInspectationResults;
	}

	public void setInvsInspectationResults(List<InvsInspectationResult> invsInspectationResults) {
		this.invsInspectationResults = invsInspectationResults;
	}
	
}