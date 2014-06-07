package com.wcs.common.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.O;
import com.wcs.common.model.P;

public class PVo extends IdModel implements Serializable {
	private static final long serialVersionUID = 8542392663971505965L;
	private CasUsrP cup;
	private P p;
	private O o;

	public CasUsrP getCup() {
		return cup;
	}

	public void setCup(CasUsrP cup) {
		this.cup = cup;
	}

	public P getP() {
		return p;
	}

	public void setP(P p) {
		this.p = p;
	}

	public O getO() {
		return o;
	}

	public void setO(O o) {
		this.o = o;
	}
}
