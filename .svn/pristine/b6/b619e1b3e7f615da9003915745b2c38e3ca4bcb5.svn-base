package com.wcs.tih.system.controller.vo;

import java.math.BigDecimal;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyDepreciation;

public class CompanyDepreciationVO extends IdModel {

    private static final long serialVersionUID = 1L;

    private CompanyDepreciation depre;
    
    public CompanyDepreciationVO(CompanyDepreciation depre) {
        setId(depre.getId());
        this.depre = depre;
        
        BigDecimal tenThousand = new BigDecimal(10000);
        if(depre.getCost() != null){
        	depre.setCost(depre.getCost().divide(tenThousand));
        }
        if(depre.getNetWorth() != null){
        	depre.setNetWorth(depre.getNetWorth().divide(tenThousand));
        }
    }

    public CompanyDepreciation getDepre() {
        return depre;
    }

    public void setDepre(CompanyDepreciation depre) {
        this.depre = depre;
    }
}
