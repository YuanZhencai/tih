package com.wcs.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wcs.tih.model.CompanyEstate;
import com.wcs.tih.model.CompanyFinancialReturn;
import com.wcs.tih.model.CompanyInvestment;
import com.wcs.tih.model.CompanyLandDetail;
import com.wcs.tih.model.CompanyMainAsset;
import com.wcs.tih.model.CompanyMaterial;
import com.wcs.tih.model.CompanyStockStructure;
import com.wcs.tih.model.CompanyTaxIncentive;
import com.wcs.tih.model.CompanyTaxTypeRatio;
import com.wcs.tih.model.TaxauthorityCompanymstr;

/**
 * The persistent class for the COMPANYMSTR database table.
 * 
 */
@Entity
public class Companymstr extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String address;

    @Column(name = "START_DATETIME")
    private Date startDatetime;
    @Column(name = "SETUP_DATETIME")
    private Date stepDatetime;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private String desc;

    private String oid;

    private String telphone;

    private String type;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    private String zipcode;

    private String region;
    
    private String province;
    
    private String code;
    
    private String  representative;
    
    // bi-directional many-to-one association to Usercompany
    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<Usercompany> usercompanies;

    // bi-directional many-to-one association to CompanyBranch
//    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
//    private List<CompanyBranch> companyBranches; // 分支机构

    // bi-directional many-to-one association to CompanyLandDetail
    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyLandDetail> companyLandDetails; // 土地明细

    // bi-directional many-to-one association to CompanyMaterial
    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyMaterial> companyMaterials;// 原料以及工艺明细

    // bi-directional many-to-one association to CompanyTaxIncentive
    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyTaxIncentive> companyTaxIncentives;// 收税优惠

    // bi-directional many-to-one association to CompanyFinancialReturn
    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyFinancialReturn> companyFinancialReturns;// 财政返还

    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyMainAsset> companyMainAssets;// 主要资产

    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyEstate> companyEstates;// 房产明细

    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyTaxTypeRatio> companyTaxTypeRatios;// 税种税率

    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<TaxauthorityCompanymstr> taxauthorityCompanymstrs;// 税务机关

    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyStockStructure> companyStockStructures;// 股权机构

    @OneToMany(mappedBy = "companymstr", fetch = FetchType.EAGER)
    private List<CompanyInvestment> companyInvestments;// 投资信息

    public Companymstr() {
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDatetime() {
        return this.createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getDefunctInd() {
        return this.defunctInd;
    }

    public void setDefunctInd(String defunctInd) {
        this.defunctInd = defunctInd;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOid() {
        return this.oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTelphone() {
        return this.telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDatetime() {
        return this.updatedDatetime;
    }

    public void setUpdatedDatetime(Date updatedDatetime) {
        this.updatedDatetime = updatedDatetime;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Usercompany> getUsercompanies() {
        return this.usercompanies;
    }

    public void setUsercompanies(List<Usercompany> usercompanies) {
        this.usercompanies = usercompanies;
    }

    public List<CompanyLandDetail> getCompanyLandDetails() {
        return companyLandDetails;
    }

    public void setCompanyLandDetails(List<CompanyLandDetail> companyLandDetails) {
        this.companyLandDetails = companyLandDetails;
    }

    public List<CompanyMaterial> getCompanyMaterials() {
        return companyMaterials;
    }

    public void setCompanyMaterials(List<CompanyMaterial> companyMaterials) {
        this.companyMaterials = companyMaterials;
    }

    public List<CompanyTaxIncentive> getCompanyTaxIncentives() {
        return companyTaxIncentives;
    }

    public void setCompanyTaxIncentives(List<CompanyTaxIncentive> companyTaxIncentives) {
        this.companyTaxIncentives = companyTaxIncentives;
    }

    public List<CompanyFinancialReturn> getCompanyFinancialReturns() {
        return companyFinancialReturns;
    }

    public void setCompanyFinancialReturns(List<CompanyFinancialReturn> companyFinancialReturns) {
        this.companyFinancialReturns = companyFinancialReturns;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getStepDatetime() {
        return stepDatetime;
    }

    public void setStepDatetime(Date stepDatetime) {
        this.stepDatetime = stepDatetime;
    }

    public List<CompanyMainAsset> getCompanyMainAssets() {
        return companyMainAssets;
    }

    public void setCompanyMainAssets(List<CompanyMainAsset> companyMainAssets) {
        this.companyMainAssets = companyMainAssets;
    }

    public List<CompanyEstate> getCompanyEstates() {
        return companyEstates;
    }

    public void setCompanyEstates(List<CompanyEstate> companyEstates) {
        this.companyEstates = companyEstates;
    }

    public List<CompanyTaxTypeRatio> getCompanyTaxTypeRatios() {
        return companyTaxTypeRatios;
    }

    public void setCompanyTaxTypeRatios(List<CompanyTaxTypeRatio> companyTaxTypeRatios) {
        this.companyTaxTypeRatios = companyTaxTypeRatios;
    }

    public List<TaxauthorityCompanymstr> getTaxauthorityCompanymstrs() {
        return taxauthorityCompanymstrs;
    }

    public void setTaxauthorityCompanymstrs(List<TaxauthorityCompanymstr> taxauthorityCompanymstrs) {
        this.taxauthorityCompanymstrs = taxauthorityCompanymstrs;
    }

    public List<CompanyStockStructure> getCompanyStockStructures() {
        return companyStockStructures;
    }

    public void setCompanyStockStructures(List<CompanyStockStructure> companyStockStructures) {
        this.companyStockStructures = companyStockStructures;
    }

    public List<CompanyInvestment> getCompanyInvestments() {
        return companyInvestments;
    }

    public void setCompanyInvestments(List<CompanyInvestment> companyInvestments) {
        this.companyInvestments = companyInvestments;
    }

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	
}