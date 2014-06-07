package com.wcs.tih.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.wcs.common.model.Companymstr;

/**
 * The persistent class for the COMPANY_TAX_TYPE_RATIO database table.
 * 
 */
@Entity
@Table(name = "COMPANY_TAX_TYPE_RATIO")
public class CompanyTaxTypeRatio extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    private String remarks;

    @Column(name = "REPORT_FREQUENCY")
    private String reportFrequency;

    @Column(name = "TAX_BASIS")
    private String taxBasis;

    @Column(name = "TAX_TYPE")
    private String taxType;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to CompanyAnnualTaxPay
    @OneToMany(mappedBy = "companyTaxTypeRatio", fetch = FetchType.EAGER)
    private List<CompanyAnnualTaxPay> companyAnnualTaxPays;

    // bi-directional many-to-one association to Companymstr
    @ManyToOne
    private Companymstr companymstr;

    public CompanyTaxTypeRatio() {
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

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getReportFrequency() {
        return this.reportFrequency;
    }

    public void setReportFrequency(String reportFrequency) {
        this.reportFrequency = reportFrequency;
    }

    public String getTaxBasis() {
        return this.taxBasis;
    }

    public void setTaxBasis(String taxBasis) {
        this.taxBasis = taxBasis;
    }

    public String getTaxType() {
        return this.taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
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

    public List<CompanyAnnualTaxPay> getCompanyAnnualTaxPays() {
        return this.companyAnnualTaxPays;
    }

    public void setCompanyAnnualTaxPays(List<CompanyAnnualTaxPay> companyAnnualTaxPays) {
        this.companyAnnualTaxPays = companyAnnualTaxPays;
    }

    public Companymstr getCompanymstr() {
        return this.companymstr;
    }

    public void setCompanymstr(Companymstr companymstr) {
        this.companymstr = companymstr;
    }

}