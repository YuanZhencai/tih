package com.wcs.tih.feedback.controller.vo;

import java.io.Serializable;
import java.util.Date;

public class SearchAntiVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String companyName;
	private Date missionStartDatetime;
	private Date missionEndDatetime;
	private Date investStartDatetime;
	private Date investEndDatetime;
	private String investType;
	private String defunctInd;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getMissionStartDatetime() {
		return missionStartDatetime;
	}

	public void setMissionStartDatetime(Date missionStartDatetime) {
		this.missionStartDatetime = missionStartDatetime;
	}

	public Date getMissionEndDatetime() {
		return missionEndDatetime;
	}

	public void setMissionEndDatetime(Date missionEndDatetime) {
		this.missionEndDatetime = missionEndDatetime;
	}

	public Date getInvestStartDatetime() {
		return investStartDatetime;
	}

	public void setInvestStartDatetime(Date investStartDatetime) {
		this.investStartDatetime = investStartDatetime;
	}

	public Date getInvestEndDatetime() {
		return investEndDatetime;
	}

	public void setInvestEndDatetime(Date investEndDatetime) {
		this.investEndDatetime = investEndDatetime;
	}

	public String getDefunctInd() {
		return defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public String getInvestType() {
		return investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

}
