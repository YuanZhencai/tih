package com.wcs.tih.report.controller.vo;

import java.io.Serializable;
import java.util.List;

import com.wcs.common.controller.helper.IdModel;

/**
 * <p>Project: tih</p>
 * <p>Description: 公司的税种税率Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class CompanyTaxRatioVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -3261381846154027655L;
    private String companyName;
    private List<TaxRatioVo> trvs;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<TaxRatioVo> getTrvs() {
        return trvs;
    }

    public void setTrvs(List<TaxRatioVo> trvs) {
        this.trvs = trvs;
    }

}
