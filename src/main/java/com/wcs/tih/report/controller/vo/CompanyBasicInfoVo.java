package com.wcs.tih.report.controller.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.CompanyMaterial;

/**
 * <p>Project: tih</p>
 * <p>Description: 公司基本信息Vo</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class CompanyBasicInfoVo extends IdModel implements Serializable {

    private static final long serialVersionUID = 5350211735327925330L;

    private String companyName;
    private String companyAddress;
    //法人代表
    private String representative;
    private String setUpDatetime;
    private String startDatetime;
    private String nationalTax;
    private String nationalTaxpayerIdentifier;
    private String landTax;
    private String landTaxTaxpayerIdentifier;
    private String manageArea;// 经营范围
    private String investTotal;// 投资总额
    private List<StockStructureVo> ssvs;
    // 主要资产原值
    private String mainAssetsCast1;
    private String mainAssetsCast2;
    private String mainAssetsCast3;
    private String mainAssetsCast4;
    private String mainAssetsCast5;
    private String mainAssetsCast6;
    // 加工能力
    private String processAbility1;
    private String processAbility2;
    private String processAbility3;
    private String processAbility4;
    private String processAbility5;
    private String processAbility6;
    private String processAbility7;
    private String processAbility8;
    private String processAbility9;
    private List<String> processAbilitys;
    
    Map<Integer, List<CompanyMaterial>> processMap;

    public Map<Integer, List<CompanyMaterial>> getProcessMap() {
        return processMap;
    }

    public void setProcessMap(Map<Integer, List<CompanyMaterial>> processMap) {
        this.processMap = processMap;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getSetUpDatetime() {
        return setUpDatetime;
    }

    public void setSetUpDatetime(String setUpDatetime) {
        this.setUpDatetime = setUpDatetime;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getNationalTax() {
        return nationalTax;
    }

    public void setNationalTax(String nationalTax) {
        this.nationalTax = nationalTax;
    }

    public String getNationalTaxpayerIdentifier() {
        return nationalTaxpayerIdentifier;
    }

    public void setNationalTaxpayerIdentifier(String nationalTaxpayerIdentifier) {
        this.nationalTaxpayerIdentifier = nationalTaxpayerIdentifier;
    }

    public String getLandTax() {
        return landTax;
    }

    public void setLandTax(String landTax) {
        this.landTax = landTax;
    }

    public String getLandTaxTaxpayerIdentifier() {
        return landTaxTaxpayerIdentifier;
    }

    public void setLandTaxTaxpayerIdentifier(String landTaxTaxpayerIdentifier) {
        this.landTaxTaxpayerIdentifier = landTaxTaxpayerIdentifier;
    }

    public String getManageArea() {
        return manageArea;
    }

    public void setManageArea(String manageArea) {
        this.manageArea = manageArea;
    }

    public String getInvestTotal() {
        return investTotal;
    }

    public void setInvestTotal(String investTotal) {
        this.investTotal = investTotal;
    }

    public List<StockStructureVo> getSsvs() {
        return ssvs;
    }

    public void setSsvs(List<StockStructureVo> ssvs) {
        this.ssvs = ssvs;
    }

    public String getMainAssetsCast1() {
        return mainAssetsCast1;
    }

    public void setMainAssetsCast1(String mainAssetsCast1) {
        this.mainAssetsCast1 = mainAssetsCast1;
    }

    public String getMainAssetsCast2() {
        return mainAssetsCast2;
    }

    public void setMainAssetsCast2(String mainAssetsCast2) {
        this.mainAssetsCast2 = mainAssetsCast2;
    }

    public String getMainAssetsCast3() {
        return mainAssetsCast3;
    }

    public void setMainAssetsCast3(String mainAssetsCast3) {
        this.mainAssetsCast3 = mainAssetsCast3;
    }

    public String getMainAssetsCast4() {
        return mainAssetsCast4;
    }

    public void setMainAssetsCast4(String mainAssetsCast4) {
        this.mainAssetsCast4 = mainAssetsCast4;
    }

    public String getMainAssetsCast5() {
        return mainAssetsCast5;
    }

    public void setMainAssetsCast5(String mainAssetsCast5) {
        this.mainAssetsCast5 = mainAssetsCast5;
    }

    public String getMainAssetsCast6() {
        return mainAssetsCast6;
    }

    public void setMainAssetsCast6(String mainAssetsCast6) {
        this.mainAssetsCast6 = mainAssetsCast6;
    }

    public String getProcessAbility1() {
        return processAbility1;
    }

    public void setProcessAbility1(String processAbility1) {
        this.processAbility1 = processAbility1;
    }

    public String getProcessAbility2() {
        return processAbility2;
    }

    public void setProcessAbility2(String processAbility2) {
        this.processAbility2 = processAbility2;
    }

    public String getProcessAbility3() {
        return processAbility3;
    }

    public void setProcessAbility3(String processAbility3) {
        this.processAbility3 = processAbility3;
    }

    public String getProcessAbility4() {
        return processAbility4;
    }

    public void setProcessAbility4(String processAbility4) {
        this.processAbility4 = processAbility4;
    }

    public String getProcessAbility5() {
        return processAbility5;
    }

    public void setProcessAbility5(String processAbility5) {
        this.processAbility5 = processAbility5;
    }

    public String getProcessAbility6() {
        return processAbility6;
    }

    public void setProcessAbility6(String processAbility6) {
        this.processAbility6 = processAbility6;
    }

    public String getProcessAbility7() {
        return processAbility7;
    }

    public void setProcessAbility7(String processAbility7) {
        this.processAbility7 = processAbility7;
    }

    public String getProcessAbility8() {
        return processAbility8;
    }

    public void setProcessAbility8(String processAbility8) {
        this.processAbility8 = processAbility8;
    }

    public String getProcessAbility9() {
        return processAbility9;
    }

    public void setProcessAbility9(String processAbility9) {
        this.processAbility9 = processAbility9;
    }

    public List<String> getProcessAbilitys() {
        return processAbilitys;
    }

    public void setProcessAbilitys(List<String> processAbilitys) {
        this.processAbilitys = processAbilitys;
    }

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

}
