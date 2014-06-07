package com.wcs.tih.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wcs.common.model.Companymstr;

/**
 * The persistent class for the COMPANY_ESTATE database table.
 * 
 */
@Entity
@Table(name = "COMPANY_ESTATE")
public class CompanyEstate extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double area;

    @Column(name = "CAL_TAX_ESTATE_COST")
    private BigDecimal calTaxEstateCost;

    @Column(name = "CAL_TAX_LAND_COST")
    private BigDecimal calTaxLandCost;

    @Column(name = "CAL_TAX_TYPE")
    private String calTaxType;

    @Column(name = "CAPITALIZATION_DATETIME")
    private Date capitalizationDatetime;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEDUCTION_RATE")
    private Double deductionRate;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "ESTATE_ACCOUNT_COST")
    private BigDecimal estateAccountCost;

    @Column(name = "ESTATE_COMPLETION_DATETIME")
    private Date estateCompletionDatetime;

    @Column(name = "ESTATE_NO")
    private String estateNo;

    @Column(name = "LAND_COST")
    private BigDecimal landCost;

    @Column(name = "LAND_NO")
    private String landNo;

    @Column(name = "LAND_UNIT_COST")
    private Double landUnitCost;

    @Column(name = "LAND_VOLUMN_RATE")
    private Double landVolumnRate;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAY_TAX_START_DATETIME")
    private Date payTaxStartDatetime;

    @Column(name = "TAX_ACCOUNT")
    private BigDecimal taxAccount;

    @Column(name = "TAX_RATE")
    private Double taxRate;

    @Column(name = "\"TYPE\"")
    private String type;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    @Column(name = "USAGE_START_DATETIME")
    private Date usageStartDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyEstate() {
    }

    public Double getArea() {
        return this.area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public BigDecimal getCalTaxEstateCost() {
        return this.calTaxEstateCost;
    }

    public void setCalTaxEstateCost(BigDecimal calTaxEstateCost) {
        this.calTaxEstateCost = calTaxEstateCost;
    }

    public BigDecimal getCalTaxLandCost() {
        return this.calTaxLandCost;
    }

    public void setCalTaxLandCost(BigDecimal calTaxLandCost) {
        this.calTaxLandCost = calTaxLandCost;
    }

    public String getCalTaxType() {
        return this.calTaxType;
    }

    public void setCalTaxType(String calTaxType) {
        this.calTaxType = calTaxType;
    }

    public Date getCapitalizationDatetime() {
        return this.capitalizationDatetime;
    }

    public void setCapitalizationDatetime(Date capitalizationDatetime) {
        this.capitalizationDatetime = capitalizationDatetime;
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

    public Double getDeductionRate() {
        return this.deductionRate;
    }

    public void setDeductionRate(Double deductionRate) {
        this.deductionRate = deductionRate;
    }

    public String getDefunctInd() {
        return this.defunctInd;
    }

    public void setDefunctInd(String defunctInd) {
        this.defunctInd = defunctInd;
    }

    public BigDecimal getEstateAccountCost() {
        return this.estateAccountCost;
    }

    public void setEstateAccountCost(BigDecimal estateAccountCost) {
        this.estateAccountCost = estateAccountCost;
    }

    public Date getEstateCompletionDatetime() {
        return this.estateCompletionDatetime;
    }

    public void setEstateCompletionDatetime(Date estateCompletionDatetime) {
        this.estateCompletionDatetime = estateCompletionDatetime;
    }

    public String getEstateNo() {
        return this.estateNo;
    }

    public void setEstateNo(String estateNo) {
        this.estateNo = estateNo;
    }

    public BigDecimal getLandCost() {
        return this.landCost;
    }

    public void setLandCost(BigDecimal landCost) {
        this.landCost = landCost;
    }

    public String getLandNo() {
        return this.landNo;
    }

    public void setLandNo(String landNo) {
        this.landNo = landNo;
    }

    public Double getLandUnitCost() {
        return this.landUnitCost;
    }

    public void setLandUnitCost(Double landUnitCost) {
        this.landUnitCost = landUnitCost;
    }

    public Double getLandVolumnRate() {
        return this.landVolumnRate;
    }

    public void setLandVolumnRate(Double landVolumnRate) {
        this.landVolumnRate = landVolumnRate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPayTaxStartDatetime() {
        return this.payTaxStartDatetime;
    }

    public void setPayTaxStartDatetime(Date payTaxStartDatetime) {
        this.payTaxStartDatetime = payTaxStartDatetime;
    }

    public BigDecimal getTaxAccount() {
        return this.taxAccount;
    }

    public void setTaxAccount(BigDecimal taxAccount) {
        this.taxAccount = taxAccount;
    }

    public Double getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
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

    public Date getUsageStartDatetime() {
        return this.usageStartDatetime;
    }

    public void setUsageStartDatetime(Date usageStartDatetime) {
        this.usageStartDatetime = usageStartDatetime;
    }

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

}