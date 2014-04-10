package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wcs.tih.model.ProjectMissionmstr;
import com.wcs.tih.model.Projectmstr;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class ProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Projectmstr p;
    private String progress;
    private List<ProjectMissionmstr> pms = new ArrayList<ProjectMissionmstr>();
    
    public ProjectVO(Projectmstr p) {
        this.p = p;
        this.progress = (int) (p.getProgress() * 100) + "";
    }

    public Projectmstr getP() {
        return p;
    }

    public void setP(Projectmstr p) {
        this.p = p;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public List<ProjectMissionmstr> getPms() {
        return pms;
    }

    public void setPms(List<ProjectMissionmstr> pms) {
        this.pms = pms;
    }
    
}
