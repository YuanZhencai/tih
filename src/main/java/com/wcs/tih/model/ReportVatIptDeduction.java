package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the REPORT_VAT_IPT_DEDUCTION database table.
 * 
 */
@Entity
@Table(name="REPORT_VAT_IPT_DEDUCTION")
public class ReportVatIptDeduction extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String bukrs;

	@Column(name="COMPANY_NAME")
	private String companyName;

	@Column(name="COMPANYMSTR_ID")
	private long companymstrId;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="STATISTIC_DATETIME")
	private Date statisticDatetime;

	private String status;

	@Column(name="TAX_RATE")
	private double taxRate;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to ReportVatIptDeductionDetail
	@OneToMany(mappedBy="reportVatIptDeduction", fetch=FetchType.EAGER)
	private List<ReportVatIptDeductionDetail> reportVatIptDeductionDetails;

    public ReportVatIptDeduction() {
    }

	public String getBukrs() {
		return this.bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
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

	public Date getStatisticDatetime() {
		return this.statisticDatetime;
	}

	public void setStatisticDatetime(Date statisticDatetime) {
		this.statisticDatetime = statisticDatetime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
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

	public List<ReportVatIptDeductionDetail> getReportVatIptDeductionDetails() {
		return this.reportVatIptDeductionDetails;
	}

	public void setReportVatIptDeductionDetails(List<ReportVatIptDeductionDetail> reportVatIptDeductionDetails) {
		this.reportVatIptDeductionDetails = reportVatIptDeductionDetails;
	}
	
}