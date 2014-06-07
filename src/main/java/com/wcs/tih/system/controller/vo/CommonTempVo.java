package com.wcs.tih.system.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

public class CommonTempVo extends IdModel implements Serializable {
    private static final long serialVersionUID = 3916014536337792686L;
    private String name;

    public CommonTempVo(Long id, String name) {
        setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
