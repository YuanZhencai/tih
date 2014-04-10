package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;

public class AuthorizmstrVo extends IdModel implements Serializable {

	private static final long serialVersionUID = -5727261308125993557L;
	private String authorizedBy;// 授权人
	private String authorizedTo;// 被授权人
	private Date startDatetime;
	private Date endDatetime;
	private String type;
	private String remarks;
	private boolean emailFlag=true;
	private boolean sysNoticeFlag=true;

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public String getAuthorizedTo() {
		return authorizedTo;
	}

	public void setAuthorizedTo(String authorizedTo) {
		this.authorizedTo = authorizedTo;
	}

	public Date getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Date getEndDatetime() {
		return endDatetime;
	}

	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean isEmailFlag() {
		return emailFlag;
	}

	public void setEmailFlag(boolean emailFlag) {
		this.emailFlag = emailFlag;
	}

	public boolean isSysNoticeFlag() {
		return sysNoticeFlag;
	}

	public void setSysNoticeFlag(boolean sysNoticeFlag) {
		this.sysNoticeFlag = sysNoticeFlag;
	}

}
