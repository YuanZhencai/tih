package com.wcs.tih.system.controller.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyTaxIncentive;

/**
 * <p>Project: tih</p>
 * <p>Description: 税收优惠Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class TaxIncentiveVo extends IdModel implements Serializable {

    private static final long serialVersionUID = 1638471267630996852L;
    private CompanyTaxIncentive taxIncentive;
    private String taxType;// 税种
    private String preferentialItem;// 优惠项目
    private String situationStatus;// 审批或备案状态
    private Date preferentialStartDatetime;// 优惠时间
    private Date preferentialEndDatetime;//
    private String policy;// 政策依据
    private String approvalOrgan;// 审批机构
    private String effective;// 有效

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getPreferentialItem() {
        return preferentialItem;
    }

    public void setPreferentialItem(String preferentialItem) {
        this.preferentialItem = preferentialItem;
    }

    public String getSituationStatus() {
        return situationStatus;
    }

    public void setSituationStatus(String situationStatus) {
        this.situationStatus = situationStatus;
    }

    public Date getPreferentialStartDatetime() {
        return preferentialStartDatetime;
    }

    public void setPreferentialStartDatetime(Date preferentialStartDatetime) {
        this.preferentialStartDatetime = preferentialStartDatetime;
    }

    public Date getPreferentialEndDatetime() {
        return preferentialEndDatetime;
    }

    public void setPreferentialEndDatetime(Date preferentialEndDatetime) {
        this.preferentialEndDatetime = preferentialEndDatetime;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getApprovalOrgan() {
        return approvalOrgan;
    }

    public void setApprovalOrgan(String approvalOrgan) {
        this.approvalOrgan = approvalOrgan;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

	public CompanyTaxIncentive getTaxIncentive() {
		return taxIncentive;
	}

	public void setTaxIncentive(CompanyTaxIncentive taxIncentive) {
		this.taxIncentive = taxIncentive;
	}

}
