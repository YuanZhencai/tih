package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.P;
import com.wcs.common.model.Usermstr;
import com.wcs.tih.model.ProjectMembermstr;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class ProjectMemberVO extends IdModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private ProjectMembermstr m;

    public ProjectMemberVO() {}
    
    public ProjectMemberVO(ProjectMembermstr m) {
        this.m = m;
        this.setId(m.getId());
    }

    public ProjectMembermstr getM() {
        return m;
    }

    public void setM(ProjectMembermstr m) {
        this.m = m;
    }

}
