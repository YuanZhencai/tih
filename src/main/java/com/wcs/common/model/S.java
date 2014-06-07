package com.wcs.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The persistent class for the S database table.
 * 
 */
@Entity
public class S implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "DEFUNCT_IND")
	private String defunctInd;

	private String kostl;

	private String oid;

	private String stext;

	private String zhrtxxlid;

	private String zhrtxxlms;

	public S() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public String getKostl() {
		return this.kostl;
	}

	public void setKostl(String kostl) {
		this.kostl = kostl;
	}

	public String getOid() {
		return this.oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getStext() {
		return this.stext;
	}

	public void setStext(String stext) {
		this.stext = stext;
	}

	public String getZhrtxxlid() {
		return this.zhrtxxlid;
	}

	public void setZhrtxxlid(String zhrtxxlid) {
		this.zhrtxxlid = zhrtxxlid;
	}

	public String getZhrtxxlms() {
		return this.zhrtxxlms;
	}

	public void setZhrtxxlms(String zhrtxxlms) {
		this.zhrtxxlms = zhrtxxlms;
	}

}