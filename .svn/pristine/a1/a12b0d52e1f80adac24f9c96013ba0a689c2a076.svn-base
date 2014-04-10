package com.wcs.tih.system.controller.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyEstate;

public class CompanyEstateVO extends IdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private CompanyEstate estate;
    
    public CompanyEstateVO(CompanyEstate estate) {
        setId(estate.getId());
        this.estate = estate;
        
        BigDecimal tenThousand = new BigDecimal(10000);
        if(estate.getEstateAccountCost() != null){
        	estate.setEstateAccountCost(estate.getEstateAccountCost().divide(tenThousand));
        }
        if(estate.getCalTaxLandCost() != null){
        	estate.setCalTaxLandCost(estate.getCalTaxLandCost().divide(tenThousand));
        }
        if(estate.getCalTaxEstateCost() != null){
        	estate.setCalTaxEstateCost(estate.getCalTaxEstateCost().divide(tenThousand));
        }
    }

    public CompanyEstate getEstate() {
        return estate;
    }

    public void setEstate(CompanyEstate estate) {
        this.estate = estate;
    }
}
