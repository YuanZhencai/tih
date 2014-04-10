package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the WF_POSITION database table.
 * 
 */
@Entity
@Table(name="WF_POSITION")
public class WfPosition extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="POSITION_ID")
	private long positionId;

	private String type;

	private String value;

    public WfPosition() {
    }

	public long getPositionId() {
		return this.positionId;
	}

	public void setPositionId(long positionId) {
		this.positionId = positionId;
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