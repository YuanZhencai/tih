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
 * The persistent class for the COMPANY_LAND_DETAILS database table.土地明细
 * 
 */
@Entity
@Table(name = "COMPANY_LAND_DETAILS")
public class CompanyLandDetail extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "ANNUAL_PAY")
    private BigDecimal annualPay;

    @Column(name = "CERTIFICATE_RIGHT_BY")
    private String certificateRightBy;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Column(name = "DEV_DEGREE")
    private String devDegree;

    @Column(name = "LAND_ADDRESS")
    private String landAddress;

    @Column(name = "LAND_AREA")
    private double landArea;

    @Column(name = "LAND_CERTIFICATE_NO")
    private String landCertificateNo;

    @Column(name = "LAND_COST")
    private BigDecimal landCost;

    @Column(name = "LAND_GET_DATETIME")
    private Date landGetDatetime;

    @Column(name = "LAND_KIND")
    private String landKind;

    @Column(name = "LAND_NAME")
    private String landName;

    @Column(name = "LAND_OVER_DATETIME")
    private Date landOverDatetime;

    @Column(name = "LAND_USAGE")
    private String landUsage;

    @Column(name = "LAND_VOLUME_RATE")
    private double landVolumeRate;

    @Column(name = "TAX_ACCRODING")
    private BigDecimal taxAccroding;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyLandDetail() {
    }

    public BigDecimal getAnnualPay() {
        return this.annualPay;
    }

    public void setAnnualPay(BigDecimal annualPay) {
        this.annualPay = annualPay;
    }

    public String getCertificateRightBy() {
        return this.certificateRightBy;
    }

    public void setCertificateRightBy(String certificateRightBy) {
        this.certificateRightBy = certificateRightBy;
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

    public String getDevDegree() {
        return this.devDegree;
    }

    public void setDevDegree(String devDegree) {
        this.devDegree = devDegree;
    }

    public String getLandAddress() {
        return this.landAddress;
    }

    public void setLandAddress(String landAddress) {
        this.landAddress = landAddress;
    }

    public double getLandArea() {
        return this.landArea;
    }

    public void setLandArea(double landArea) {
        this.landArea = landArea;
    }

    public String getLandCertificateNo() {
        return this.landCertificateNo;
    }

    public void setLandCertificateNo(String landCertificateNo) {
        this.landCertificateNo = landCertificateNo;
    }

    public BigDecimal getLandCost() {
        return this.landCost;
    }

    public void setLandCost(BigDecimal landCost) {
        this.landCost = landCost;
    }

    public Date getLandGetDatetime() {
        return this.landGetDatetime;
    }

    public void setLandGetDatetime(Date landGetDatetime) {
        this.landGetDatetime = landGetDatetime;
    }

    public String getLandKind() {
        return this.landKind;
    }

    public void setLandKind(String landKind) {
        this.landKind = landKind;
    }

    public String getLandName() {
        return this.landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public Date getLandOverDatetime() {
        return this.landOverDatetime;
    }

    public void setLandOverDatetime(Date landOverDatetime) {
        this.landOverDatetime = landOverDatetime;
    }

    public String getLandUsage() {
        return this.landUsage;
    }

    public void setLandUsage(String landUsage) {
        this.landUsage = landUsage;
    }

    public double getLandVolumeRate() {
        return this.landVolumeRate;
    }

    public void setLandVolumeRate(double landVolumeRate) {
        this.landVolumeRate = landVolumeRate;
    }

    public BigDecimal getTaxAccroding() {
        return this.taxAccroding;
    }

    public void setTaxAccroding(BigDecimal taxAccroding) {
        this.taxAccroding = taxAccroding;
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

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

}