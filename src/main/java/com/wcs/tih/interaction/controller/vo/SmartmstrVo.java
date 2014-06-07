package com.wcs.tih.interaction.controller.vo;


import java.io.Serializable;

import com.wcs.common.controller.vo.DictVo;
import com.wcs.tih.model.Smartmstr;

public class SmartmstrVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Smartmstr smartmstr;
	private DictVo taxType;
	private DictVo region;

	public Smartmstr getSmartmstr() {
		return smartmstr;
	}

	public void setSmartmstr(Smartmstr smartmstr) {
		this.smartmstr = smartmstr;
	}

    public DictVo getTaxType() {
        return taxType;
    }

    public void setTaxType(DictVo taxType) {
        this.taxType = taxType;
    }

    public DictVo getRegion() {
        return region;
    }

    public void setRegion(DictVo region) {
        this.region = region;
    }
    
}
