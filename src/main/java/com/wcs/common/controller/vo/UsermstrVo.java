package com.wcs.common.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.O;
import com.wcs.common.model.P;
import com.wcs.common.model.Usermstr;

public class UsermstrVo extends IdModel implements Serializable {
	private static final long serialVersionUID = 7922530371478759792L;
	private CasUsrP cup;
	private P p;
	private Usermstr usermstr;
	private O o;
	private String lowerAdAccount;

	public CasUsrP getCup() {
		return cup;
	}

	public void setCup(CasUsrP cup) {
		this.cup = cup;
	}

	public Usermstr getUsermstr() {
		return usermstr;
	}

	public void setUsermstr(Usermstr usermstr) {
		this.usermstr = usermstr;
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

    public String getLowerAdAccount() {
        return lowerAdAccount;
    }

    public void setLowerAdAccount(String lowerAdAccount) {
        this.lowerAdAccount = lowerAdAccount;
    }

}
