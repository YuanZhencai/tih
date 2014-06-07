package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonBackReference;


/**
 * The persistent class for the WF_STEPMSTR_PROPERTY database table.
 * 
 */
@Entity
@Table(name="WF_STEPMSTR_PROPERTY")
public class WfStepmstrProperty extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;

	private String value;

	//bi-directional many-to-one association to WfStepmstr
    @ManyToOne
	@JoinColumn(name="WF_STEPMSTR_ID")
	private WfStepmstr wfStepmstr;

    public WfStepmstrProperty() {
    }

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonBackReference
	public WfStepmstr getWfStepmstr() {
		return this.wfStepmstr;
	}

	public void setWfStepmstr(WfStepmstr wfStepmstr) {
		this.wfStepmstr = wfStepmstr;
	}
	
}