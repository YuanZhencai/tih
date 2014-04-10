package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the WF_AUTHORIZMSTR database table.
 * 
 */
@Entity
@Table(name="WF_AUTHORIZMSTR")
public class WfAuthorizmstr extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="AUTHORIZED_BY")
	private String authorizedBy;

	@Column(name="AUTHORIZED_TO")
	private String authorizedTo;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="END_DATETIME")
	private Date endDatetime;

	private String remarks;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="START_DATETIME")
	private Date startDatetime;

	private String type;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;
    
    @Column (name="MAIL_IND")
    private String mailInd;

    @Column (name="SYS_NOTICE_IND")
    private String sysNoticeInd;

    public WfAuthorizmstr() {
    }

	public String getAuthorizedBy() {
		return this.authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public String getAuthorizedTo() {
		return this.authorizedTo;
	}

	public void setAuthorizedTo(String authorizedTo) {
		this.authorizedTo = authorizedTo;
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

	public Date getEndDatetime() {
		return this.endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getStartDatetime() {
		return this.startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
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

	public String getMailInd() {
		return mailInd;
	}

	public void setMailInd(String mailInd) {
		this.mailInd = mailInd;
	}

	public String getSysNoticeInd() {
		return sysNoticeInd;
	}

	public void setSysNoticeInd(String sysNoticeInd) {
		this.sysNoticeInd = sysNoticeInd;
	}

}