package com.wcs.common.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the PS database table.
 * 
 */
@Entity
public class PS implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "DEFUNCT_IND")
	private String defunctInd;

	private String pid;

	private String sid;

	public PS() {
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

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}