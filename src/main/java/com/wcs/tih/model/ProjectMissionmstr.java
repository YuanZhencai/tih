package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the PROJECT_MISSIONMSTR database table.
 * 
 */
@Entity
@Table(name="PROJECT_MISSIONMSTR")
public class ProjectMissionmstr extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CHARGED_BY")
	private String chargedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CLOSE_DATE")
	private Date closeDate;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	private String desc;

	private String name;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="START_DATE")
	private Date startDate;

	private String status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to Projectmstr
    @ManyToOne
	private Projectmstr projectmstr;

	//bi-directional many-to-one association to ProjectProblemmstr
	@OneToMany(mappedBy="projectMissionmstr")
	private List<ProjectProblemmstr> projectProblemmstrs;

    public ProjectMissionmstr() {
    }

	public String getChargedBy() {
		return this.chargedBy;
	}

	public void setChargedBy(String chargedBy) {
		this.chargedBy = chargedBy;
	}

	public Date getCloseDate() {
		return this.closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
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

	public Projectmstr getProjectmstr() {
		return this.projectmstr;
	}

	public void setProjectmstr(Projectmstr projectmstr) {
		this.projectmstr = projectmstr;
	}
	
	public List<ProjectProblemmstr> getProjectProblemmstrs() {
		return this.projectProblemmstrs;
	}

	public void setProjectProblemmstrs(List<ProjectProblemmstr> projectProblemmstrs) {
		this.projectProblemmstrs = projectProblemmstrs;
	}
	
}