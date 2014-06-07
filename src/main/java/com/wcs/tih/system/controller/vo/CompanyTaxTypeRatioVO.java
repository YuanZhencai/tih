package com.wcs.tih.system.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyTaxTypeRatio;

public class CompanyTaxTypeRatioVO extends IdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private CompanyTaxTypeRatio typeRatio;
    
    public CompanyTaxTypeRatioVO() {}
    
    public CompanyTaxTypeRatioVO(CompanyTaxTypeRatio typeRatio) {
        setId(typeRatio.getId());
        this.typeRatio = typeRatio;
    }

    public CompanyTaxTypeRatio getTypeRatio() {
        return typeRatio;
    }

    public void setTypeRatio(CompanyTaxTypeRatio typeRatio) {
        this.typeRatio = typeRatio;
    }
}
