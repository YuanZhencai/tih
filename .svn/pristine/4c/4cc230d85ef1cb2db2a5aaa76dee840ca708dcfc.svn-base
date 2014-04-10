package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the PROJECT_PROBLEMMSTR database table.
 * 
 */
@Entity
@Table(name="PROJECT_PROBLEMMSTR")
public class ProjectProblemmstr extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String desc;

	private String proposal;

	@Column(name="SOLVED_BY")
	private String solvedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="SOLVED_DATE")
	private Date solvedDate;

	private String status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to ProjectMissionmstr
    @ManyToOne
	@JoinColumn(name="PROJECT_MISSIONMSTR_ID")
	private ProjectMissionmstr projectMissionmstr;

    public ProjectProblemmstr() {
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

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getProposal() {
		return this.proposal;
	}

	public void setProposal(String proposal) {
		this.proposal = proposal;
	}

	public String getSolvedBy() {
		return this.solvedBy;
	}

	public void setSolvedBy(String solvedBy) {
		this.solvedBy = solvedBy;
	}

	public Date getSolvedDate() {
		return this.solvedDate;
	}

	public void setSolvedDate(Date solvedDate) {
		this.solvedDate = solvedDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public ProjectMissionmstr getProjectMissionmstr() {
		return this.projectMissionmstr;
	}

	public void setProjectMissionmstr(ProjectMissionmstr projectMissionmstr) {
		this.projectMissionmstr = projectMissionmstr;
	}
	
}