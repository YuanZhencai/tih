package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the REPORT_PAYABLE_TAX database table.
 * 
 */
@Entity
@Table(name = "REPORT_PAYABLE_TAX")
public class ReportPayableTax extends com.wcs.base.model.IdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bukrs;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    @Column(name = "COMPANYMSTR_ID")
    private long companymstrId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATETIME")
    private Date createdDatetime;

    @Column(name = "DEFUNCT_IND")
    private String defunctInd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATISTIC_DATETIME")
    private Date statisticDatetime;

    private String status;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATETIME")
    private Date updatedDatetime;

    // bi-directional many-to-one association to ReportPayableTaxAddedMaterial
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxAddedMaterial> reportPayableTaxAddedMaterials;

    // bi-directional many-to-one association to ReportPayableTaxEstate
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxEstate> reportPayableTaxEstates;

    // bi-directional many-to-one association to ReportPayableTaxIncome
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxIncome> reportPayableTaxIncomes;

    // bi-directional many-to-one association to ReportPayableTaxLand
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxLand> reportPayableTaxLands;

    // bi-directional many-to-one association to ReportPayableTaxOther
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxOther> reportPayableTaxOthers;

    // bi-directional many-to-one association to ReportPayableTaxStamp
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxStamp> reportPayableTaxStamps;

    // bi-directional many-to-one association to ReportPayableTaxStayed
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxStayed> reportPayableTaxStayeds;

    // bi-directional many-to-one association to ReportPayableTaxVat
    @OneToMany(mappedBy = "reportPayableTax", fetch = FetchType.EAGER)
    private List<ReportPayableTaxVat> reportPayableTaxVats;

    public ReportPayableTax() {
    }

    public String getBukrs() {
        return this.bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public long getCompanymstrId() {
        return this.companymstrId;
    }

    public void setCompanymstrId(long companymstrId) {
        this.companymstrId = companymstrId;
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

    public Date getStatisticDatetime() {
        return this.statisticDatetime;
    }

    public void setStatisticDatetime(Date statisticDatetime) {
        this.statisticDatetime = statisticDatetime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<ReportPayableTaxAddedMaterial> getReportPayableTaxAddedMaterials() {
        return this.reportPayableTaxAddedMaterials;
    }

    public void setReportPayableTaxAddedMaterials(List<ReportPayableTaxAddedMaterial> reportPayableTaxAddedMaterials) {
        this.reportPayableTaxAddedMaterials = reportPayableTaxAddedMaterials;
    }

    public List<ReportPayableTaxEstate> getReportPayableTaxEstates() {
        return this.reportPayableTaxEstates;
    }

    public void setReportPayableTaxEstates(List<ReportPayableTaxEstate> reportPayableTaxEstates) {
        this.reportPayableTaxEstates = reportPayableTaxEstates;
    }

    public List<ReportPayableTaxIncome> getReportPayableTaxIncomes() {
        return this.reportPayableTaxIncomes;
    }

    public void setReportPayableTaxIncomes(List<ReportPayableTaxIncome> reportPayableTaxIncomes) {
        this.reportPayableTaxIncomes = reportPayableTaxIncomes;
    }

    public List<ReportPayableTaxLand> getReportPayableTaxLands() {
        return this.reportPayableTaxLands;
    }

    public void setReportPayableTaxLands(List<ReportPayableTaxLand> reportPayableTaxLands) {
        this.reportPayableTaxLands = reportPayableTaxLands;
    }

    public List<ReportPayableTaxOther> getReportPayableTaxOthers() {
        return this.reportPayableTaxOthers;
    }

    public void setReportPayableTaxOthers(List<ReportPayableTaxOther> reportPayableTaxOthers) {
        this.reportPayableTaxOthers = reportPayableTaxOthers;
    }

    public List<ReportPayableTaxStamp> getReportPayableTaxStamps() {
        return this.reportPayableTaxStamps;
    }

    public void setReportPayableTaxStamps(List<ReportPayableTaxStamp> reportPayableTaxStamps) {
        this.reportPayableTaxStamps = reportPayableTaxStamps;
    }

    public List<ReportPayableTaxStayed> getReportPayableTaxStayeds() {
        return this.reportPayableTaxStayeds;
    }

    public void setReportPayableTaxStayeds(List<ReportPayableTaxStayed> reportPayableTaxStayeds) {
        this.reportPayableTaxStayeds = reportPayableTaxStayeds;
    }

    public List<ReportPayableTaxVat> getReportPayableTaxVats() {
        return this.reportPayableTaxVats;
    }

    public void setReportPayableTaxVats(List<ReportPayableTaxVat> reportPayableTaxVats) {
        this.reportPayableTaxVats = reportPayableTaxVats;
    }

}