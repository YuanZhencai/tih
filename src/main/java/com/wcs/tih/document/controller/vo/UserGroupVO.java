package com.wcs.tih.document.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class UserGroupVO extends IdModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String type;
    private String value;

    public UserGroupVO() {}
    public UserGroupVO(Long id, String name, String type) {
        this.setId(id);
        this.name = name;
        this.type = type;
    }

    public UserGroupVO(Long id, String name, String type, String value) {
    	this.setId(id);
		this.name = name;
		this.type = type;
		this.value = value;
	}
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
