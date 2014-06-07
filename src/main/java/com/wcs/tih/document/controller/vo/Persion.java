package com.wcs.tih.document.controller.vo;

import java.io.Serializable;

public class Persion implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String adAccount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdAccount() {
		return adAccount;
	}

	public void setAdAccount(String adAccount) {
		this.adAccount = adAccount;
	}

}
