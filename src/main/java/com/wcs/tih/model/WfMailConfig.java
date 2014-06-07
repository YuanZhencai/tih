package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the WF_MAIL_CONFIG database table.
 * 
 */
@Entity
@Table(name="WF_MAIL_CONFIG")
public class WfMailConfig extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="MAIL_IND")
	private String mailInd;

	@Column(name="SYS_NOTICE_IND")
	private String sysNoticeInd;

	private String type;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;
    
    @Column(name="JOB_IND")
    private String jobInd;
    
    @Column(name="JOB_ID")
    private String jobId;

    public WfMailConfig() {
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

	public String getMailInd() {
		return this.mailInd;
	}

	public void setMailInd(String mailInd) {
		this.mailInd = mailInd;
	}

	public String getSysNoticeInd() {
		return this.sysNoticeInd;
	}

	public void setSysNoticeInd(String sysNoticeInd) {
		this.sysNoticeInd = sysNoticeInd;
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

	public String getJobInd() {
		return jobInd;
	}

	public void setJobInd(String jobInd) {
		this.jobInd = jobInd;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}