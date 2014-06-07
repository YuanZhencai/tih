package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the INVS_TRANSFER_PRICE_HISTORY database table.
 * 
 */
@Entity
@Table(name="INVS_TRANSFER_PRICE_HISTORY")
public class InvsTransferPriceHistory extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ASSO_DEBT_EQUITY_RATIO")
	private double assoDebtEquityRatio;

	@Column(name="COMPANY_NAME")
	private String companyName;

	@Column(name="COMPANYMSTR_ID")
	private long companymstrId;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	private Date decade;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="DOC_SUBMIT_DATETIME")
	private Date docSubmitDatetime;

	@Column(name="OPERATE_IND")
	private String operateInd;

	@Column(name="PREPARE_DOC_IND")
	private String prepareDocInd;

	private String remarks;

	@Column(name="SUBMIT_DOC_IND")
	private String submitDocInd;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to InvsTransferPrice
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INVS_TRANSFER_PRICE_ID")
	private InvsTransferPrice invsTransferPrice;

	//bi-directional many-to-one association to InvsVerifyTransTypeHistory
	@OneToMany(mappedBy="invsTransferPriceHistory", cascade = CascadeType.ALL)
	private List<InvsVerifyTransTypeHistory> invsVerifyTransTypeHistories;

    public InvsTransferPriceHistory() {
    }

	public double getAssoDebtEquityRatio() {
		return this.assoDebtEquityRatio;
	}

	public void setAssoDebtEquityRatio(double assoDebtEquityRatio) {
		this.assoDebtEquityRatio = assoDebtEquityRatio;
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

	public Date getDecade() {
		return this.decade;
	}

	public void setDecade(Date decade) {
		this.decade = decade;
	}

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public Date getDocSubmitDatetime() {
		return this.docSubmitDatetime;
	}

	public void setDocSubmitDatetime(Date docSubmitDatetime) {
		this.docSubmitDatetime = docSubmitDatetime;
	}

	public String getOperateInd() {
		return this.operateInd;
	}

	public void setOperateInd(String operateInd) {
		this.operateInd = operateInd;
	}

	public String getPrepareDocInd() {
		return this.prepareDocInd;
	}

	public void setPrepareDocInd(String prepareDocInd) {
		this.prepareDocInd = prepareDocInd;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSubmitDocInd() {
		return this.submitDocInd;
	}

	public void setSubmitDocInd(String submitDocInd) {
		this.submitDocInd = submitDocInd;
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

	public InvsTransferPrice getInvsTransferPrice() {
		return this.invsTransferPrice;
	}

	public void setInvsTransferPrice(InvsTransferPrice invsTransferPrice) {
		this.invsTransferPrice = invsTransferPrice;
	}
	
	public List<InvsVerifyTransTypeHistory> getInvsVerifyTransTypeHistories() {
		return this.invsVerifyTransTypeHistories;
	}

	public void setInvsVerifyTransTypeHistories(List<InvsVerifyTransTypeHistory> invsVerifyTransTypeHistories) {
		this.invsVerifyTransTypeHistories = invsVerifyTransTypeHistories;
	}
	
}