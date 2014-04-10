package com.wcs.tih.document.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class CompanyVO extends IdModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Companymstr c;
    private O o;
    
    public CompanyVO() {}
    
    public CompanyVO(Companymstr c, O o) {
        this.setId(c.getId());
        this.c = c;
        this.o = o;
    }

    public Companymstr getC() {
        return c;
    }

    public void setC(Companymstr c) {
        this.c = c;
    }

    public O getO() {
        return o;
    }

    public void setO(O o) {
        this.o = o;
    }
}
