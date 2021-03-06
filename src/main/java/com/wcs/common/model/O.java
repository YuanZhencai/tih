package com.wcs.common.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the O database table.
 * 
 */
@Entity
public class O implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String bukrs;

	@Column(name = "DEFUNCT_IND", nullable = false, length = 1)
	private String defunctInd;

	private String kostl;

	private String parent;

	private String stext;

	private String zhrtxxlid;

	private String zhrtxxlms;

	private String zhrzzcjid;

	private String zhrzzdwid;

	public O() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBukrs() {
		return this.bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
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

	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
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

	public String getZhrzzcjid() {
		return this.zhrzzcjid;
	}

	public void setZhrzzcjid(String zhrzzcjid) {
		this.zhrzzcjid = zhrzzcjid;
	}

	public String getZhrzzdwid() {
		return this.zhrzzdwid;
	}

	public void setZhrzzdwid(String zhrzzdwid) {
		this.zhrzzdwid = zhrzzdwid;
	}

}