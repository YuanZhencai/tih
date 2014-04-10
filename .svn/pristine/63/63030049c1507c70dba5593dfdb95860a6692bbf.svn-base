package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonManagedReference;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the WF_INSTANCEMSTR database table.
 * 
 */
@Entity
@Table(name="WF_INSTANCEMSTR")
public class WfInstancemstr extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String no;

	@Column(name="REQUEST_BY")
	private String requestBy;

	private String status;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="SUBMIT_DATETIME")
	private Date submitDatetime;

	private String type;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;
    
    private String remarks;
    
    private String importance;
    
    private String urgency;

	//bi-directional many-to-one association to WfInstancemstrProperty
	@OneToMany(mappedBy="wfInstancemstr", fetch=FetchType.LAZY)
	private List<WfInstancemstrProperty> wfInstancemstrProperties;

	//bi-directional many-to-one association to WfStepmstr
	@OneToMany(mappedBy="wfInstancemstr", fetch=FetchType.LAZY)
	private List<WfStepmstr> wfStepmstrs;

    public WfInstancemstr() {
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

	public String getNo() {
		return this.no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getRequestBy() {
		return this.requestBy;
	}

	public void setRequestBy(String requestBy) {
		this.requestBy = requestBy;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSubmitDatetime() {
		return this.submitDatetime;
	}

	public void setSubmitDatetime(Date submitDatetime) {
		this.submitDatetime = submitDatetime;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	@JsonManagedReference
	public List<WfInstancemstrProperty> getWfInstancemstrProperties() {
		return this.wfInstancemstrProperties;
	}

	public void setWfInstancemstrProperties(List<WfInstancemstrProperty> wfInstancemstrProperties) {
		this.wfInstancemstrProperties = wfInstancemstrProperties;
	}
	
	@JsonManagedReference
	public List<WfStepmstr> getWfStepmstrs() {
		return this.wfStepmstrs;
	}

	public void setWfStepmstrs(List<WfStepmstr> wfStepmstrs) {
		this.wfStepmstrs = wfStepmstrs;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
}