package com.wcs.tih.report.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

/**
 * <p>Project: tih</p>
 * <p>Description: 税种税率Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class TaxRatioVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -6475061347039097748L;
    private String taxType;// 税种
    private String taxBasis;// 计税基础
    private String taxRate;// 税率
    private String reportFrequency;// 申报频率

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxBasis() {
        return taxBasis;
    }

    public void setTaxBasis(String taxBasis) {
        this.taxBasis = taxBasis;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getReportFrequency() {
        return reportFrequency;
    }

    public void setReportFrequency(String reportFrequency) {
        this.reportFrequency = reportFrequency;
    }

}
