package com.wcs.tih.system.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

/**
 * <p>Project: tih</p>
 * <p>Description: 原料及工艺Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class MaterialVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -8109260199777625698L;
    private String mainMaterial;// 主要原料
    private String mainProduct;// 主要产品
    private String processing;// 加大项目
    private Integer ability;// 处理能力
    private String unit;// 单位
    private String effective;// 有效

    public String getMainMaterial() {
        return mainMaterial;
    }

    public void setMainMaterial(String mainMaterial) {
        this.mainMaterial = mainMaterial;
    }

    public String getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public Integer getAbility() {
        return ability;
    }

    public void setAbility(Integer ability) {
        this.ability = ability;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }
}
