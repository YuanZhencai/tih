package com.wcs.tih.document.controller.vo;

import java.io.Serializable;

import com.filenet.api.security.AccessPermission;
import com.wcs.common.controller.helper.IdModel;

/**
 * Project: tih
 * Description: 权限VO
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class PermissionVO extends IdModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean fullControl = false;
    private boolean promote = false;
    private boolean modifyContent = false;
    private boolean modifyProperty = false;
    private boolean viewContent = false;
    private boolean viewProperty = false;
    private boolean publish = false;
    
    private String type;
    private String name;
    private int mask;
    private String value;
    
    public PermissionVO() { }
    
    // Getter && Setter
    public boolean isFullControl() {
        return fullControl;
    }

    public void setFullControl(boolean fullControl) {
        this.fullControl = fullControl;
    }

    public boolean isPromote() {
        return promote;
    }

    public void setPromote(boolean promote) {
        this.promote = promote;
    }

    public boolean isModifyContent() {
        return modifyContent;
    }

    public void setModifyContent(boolean modifyContent) {
        this.modifyContent = modifyContent;
    }

    public boolean isModifyProperty() {
        return modifyProperty;
    }

    public void setModifyProperty(boolean modifyProperty) {
        this.modifyProperty = modifyProperty;
    }

    public boolean isViewContent() {
        return viewContent;
    }

    public void setViewContent(boolean viewContent) {
        this.viewContent = viewContent;
    }

    public boolean isViewProperty() {
        return viewProperty;
    }

    public void setViewProperty(boolean viewProperty) {
        this.viewProperty = viewProperty;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
