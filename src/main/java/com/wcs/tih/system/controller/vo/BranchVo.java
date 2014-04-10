package com.wcs.tih.system.controller.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;

/**
 * <p>Project: tih</p>
 * <p>Description: 分支机构Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class BranchVo extends IdModel implements Serializable {

    private static final long serialVersionUID = 4563759847309494025L;
    private String organizationName;
    private Date setUpDatetime;
    private String location;
    private String businessScope;
    private String remakrs;
    private String effective;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Date getSetUpDatetime() {
        return setUpDatetime;
    }

    public void setSetUpDatetime(Date setUpDatetime) {
        this.setUpDatetime = setUpDatetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getRemakrs() {
        return remakrs;
    }

    public void setRemakrs(String remakrs) {
        this.remakrs = remakrs;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

}
