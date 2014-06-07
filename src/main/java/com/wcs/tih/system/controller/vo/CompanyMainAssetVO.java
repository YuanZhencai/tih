package com.wcs.tih.system.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyMainAsset;

/**
 * Project: tih
 * Description: 资产信息vo
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class CompanyMainAssetVO extends IdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private CompanyMainAsset asset;

    public CompanyMainAssetVO() {}
    
    public CompanyMainAssetVO(CompanyMainAsset asset) {
        setId(asset.getId());
        this.asset = asset;
    }
    
    public CompanyMainAsset getAsset() {
        return asset;
    }

    public void setAsset(CompanyMainAsset asset) {
        this.asset = asset;
    }
    
}
