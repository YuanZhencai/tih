package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the WF_SUPERVISOR database table.
 * 
 */
@Entity
@Table(name="WF_SUPERVISOR")
public class WfSupervisor extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CHARGED_BY")
	private String chargedBy;

	@Column(name="COMPANYMSTR_ID")
	private long companymstrId;

	private String supervisor;

	private String type;

	private String value;

	public WfSupervisor() {
	}

	public String getChargedBy() {
		return this.chargedBy;
	}

	public void setChargedBy(String chargedBy) {
		this.chargedBy = chargedBy;
	}

	public long getCompanymstrId() {
		return this.companymstrId;
	}

	public void setCompanymstrId(long companymstrId) {
		this.companymstrId = companymstrId;
	}

	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}