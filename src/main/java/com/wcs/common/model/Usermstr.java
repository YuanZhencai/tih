package com.wcs.common.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the USERMSTR database table.
 * 
 */
@Entity
public class Usermstr extends com.wcs.base.model.IdEntity implements Serializable {

	private static final long serialVersionUID = -4138239153171601393L;

	@Column(name="AD_ACCOUNT")
	private String adAccount;

	@Column(name="BACKGROUND_INFO")
	private String backgroundInfo;

    @Temporal( TemporalType.TIMESTAMP)
	private Date birthday;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="IDENTITY_TYPE")
	private String identityType;

	@Column(name="IDTENTITY_ID")
	private String idtentityId;

	@Column(name="ONBOARD_DATE")
	private Date onboardDate;

	private String pernr;
	

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;
    
    @Column(name="POSITION_REMARK")
	private String positionRemark;

	//bi-directional many-to-one association to Usercompany
	@OneToMany(mappedBy="usermstr", fetch=FetchType.EAGER)
	private List<Usercompany> usercompanies;

	//bi-directional many-to-one association to Userpositionorg
	@OneToMany(mappedBy="usermstr", fetch=FetchType.EAGER)
	private List<Userpositionorg> userpositionorgs;

	//bi-directional many-to-one association to Userrole
	@OneToMany(mappedBy="usermstr", fetch=FetchType.EAGER)
	private List<Userrole> userroles;

    public Usermstr() {
    }

	public String getAdAccount() {
		return this.adAccount;
	}

	public void setAdAccount(String adAccount) {
		this.adAccount = adAccount;
	}

	public String getBackgroundInfo() {
		return this.backgroundInfo;
	}

	public void setBackgroundInfo(String backgroundInfo) {
		this.backgroundInfo = backgroundInfo;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	public String getIdentityType() {
		return this.identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdtentityId() {
		return this.idtentityId;
	}

	public void setIdtentityId(String idtentityId) {
		this.idtentityId = idtentityId;
	}

	public Date getOnboardDate() {
		return this.onboardDate;
	}

	public void setOnboardDate(Date onboardDate) {
		this.onboardDate = onboardDate;
	}

	public String getPernr() {
		return this.pernr;
	}

	public void setPernr(String pernr) {
		this.pernr = pernr;
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

	public List<Usercompany> getUsercompanies() {
		return this.usercompanies;
	}

	public void setUsercompanies(List<Usercompany> usercompanies) {
		this.usercompanies = usercompanies;
	}
	
	public List<Userpositionorg> getUserpositionorgs() {
		return this.userpositionorgs;
	}

	public void setUserpositionorgs(List<Userpositionorg> userpositionorgs) {
		this.userpositionorgs = userpositionorgs;
	}
	
	public List<Userrole> getUserroles() {
		return this.userroles;
	}

	public void setUserroles(List<Userrole> userroles) {
		this.userroles = userroles;
	}

	public String getPositionRemark() {
		return positionRemark;
	}

	public void setPositionRemark(String positionRemark) {
		this.positionRemark = positionRemark;
	}
	
}