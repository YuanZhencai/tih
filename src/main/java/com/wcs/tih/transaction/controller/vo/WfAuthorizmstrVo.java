package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.WfAuthorizmstr;

public class WfAuthorizmstrVo extends IdModel implements Serializable {

	private static final long serialVersionUID = -5727261308125993557L;
	private WfAuthorizmstr wfAuthorizmstr;
	private boolean emailFlag;
	private boolean sysNoticeFlag;

	public WfAuthorizmstr getWfAuthorizmstr() {
		return wfAuthorizmstr;
	}

	public void setWfAuthorizmstr(WfAuthorizmstr wfAuthorizmstr) {
		this.wfAuthorizmstr = wfAuthorizmstr;
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
