package com.wcs.tih.report.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

/**
 * <p>Project: tih</p>
 * <p>Description: 股权机构Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class StockStructureVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -484305090492287862L;
    private String shareholder;// 股东
    private String registeredCapital;// 注册资本
    private String ratio;// 股权比率

    public StockStructureVo() {
    }

    public StockStructureVo(String shareholder, String registeredCapital, String ratio) {
        this.shareholder = shareholder;
        this.registeredCapital = registeredCapital;
        this.ratio = ratio;
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

}
