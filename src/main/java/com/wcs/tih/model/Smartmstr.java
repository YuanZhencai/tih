package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonManagedReference;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the SMARTMSTR database table.
 * 
 */
@Entity
public class Smartmstr extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String answer;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String question;

	private String region;

	@Column(name="TAX_TYPE")
	private String taxType;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to SmartAttachmentmstr
	@OneToMany(mappedBy="smartmstr", fetch=FetchType.EAGER)
	private List<SmartAttachmentmstr> smartAttachmentmstrs;

	//bi-directional many-to-one association to SmartStatisticsmstr
	@OneToMany(mappedBy="smartmstr", fetch=FetchType.EAGER)
	private List<SmartStatisticsmstr> smartStatisticsmstrs;
	
	@Column(name="WF_INSTANCEMSTR_ID")
	private long wfInstancemstrId;
	
    public Smartmstr() {
    }

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
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

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public List<SmartAttachmentmstr> getSmartAttachmentmstrs() {
		return this.smartAttachmentmstrs;
	}

	public void setSmartAttachmentmstrs(List<SmartAttachmentmstr> smartAttachmentmstrs) {
		this.smartAttachmentmstrs = smartAttachmentmstrs;
	}
	
	@JsonManagedReference
	public List<SmartStatisticsmstr> getSmartStatisticsmstrs() {
		return this.smartStatisticsmstrs;
	}

	public void setSmartStatisticsmstrs(List<SmartStatisticsmstr> smartStatisticsmstrs) {
		this.smartStatisticsmstrs = smartStatisticsmstrs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getWfInstancemstrId() {
		return wfInstancemstrId;
	}

	public void setWfInstancemstrId(long wfInstancemstrId) {
		this.wfInstancemstrId = wfInstancemstrId;
	}

}