package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PROJECTMSTR database table.
 * 
 */
@Entity
public class Projectmstr extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CLOSE_DATE")
	private Date closeDate;

	private String code;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String desc;

	private String name;

	@Column(name="PM_ID")
	private String pmId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="START_DATE")
	private Date startDate;

	private String status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to ProjectMembermstr
	@OneToMany(mappedBy="projectmstr")
	private List<ProjectMembermstr> projectMembermstrs;

	//bi-directional many-to-one association to ProjectMissionmstr
	@OneToMany(mappedBy="projectmstr")
	private List<ProjectMissionmstr> projectMissionmstrs;

	@Column(name="PROGRESS")
	private float progress;
	
    public Projectmstr() {
    }

	public Date getCloseDate() {
		return this.closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPmId() {
		return this.pmId;
	}

	public void setPmId(String pmId) {
		this.pmId = pmId;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public List<ProjectMembermstr> getProjectMembermstrs() {
		return this.projectMembermstrs;
	}

	public void setProjectMembermstrs(List<ProjectMembermstr> projectMembermstrs) {
		this.projectMembermstrs = projectMembermstrs;
	}
	
	public List<ProjectMissionmstr> getProjectMissionmstrs() {
		return this.projectMissionmstrs;
	}

	public void setProjectMissionmstrs(List<ProjectMissionmstr> projectMissionmstrs) {
		this.projectMissionmstrs = projectMissionmstrs;
	}

	public float getProgress() {
		return progress;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}
	
}