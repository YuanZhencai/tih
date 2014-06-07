package com.wcs.tih.report.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.ReportVatIptDeduction;

public class VATSummaryVO extends IdModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private ReportVatIptDeduction reportVat = new ReportVatIptDeduction();
	private Companymstr company = new Companymstr();
	private String lowerCode;
	
	public VATSummaryVO(){
		
	}
	
	public VATSummaryVO(Long id,ReportVatIptDeduction reportVat){
		setId(id);
		this.reportVat=reportVat;
	}
	
	private long companyId;
	private String companyName;
	
	private String fileId;
	private String fileName;

	public VATSummaryVO(Long companyId,String companyName){
		setId(companyId);
		this.companyId=companyId;
		this.companyName=companyName;
	}
	
	public VATSummaryVO(String fileId,String fileName){
		this.fileId=fileId;
		this.fileName=fileName;
	}
	
	public VATSummaryVO(Companymstr c, String lowerCode, ReportVatIptDeduction vat){
	    this.company = c;
	    this.lowerCode = lowerCode;
	    this.reportVat = vat;
	}
	
	public ReportVatIptDeduction getReportVat() {
		return reportVat;
	}
	public void setReportVat(ReportVatIptDeduction reportVat) {
		this.reportVat = reportVat;
	}
	
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

    public Companymstr getCompany() {
        return company;
    }

    public void setCompany(Companymstr company) {
        this.company = company;
    }

    public String getLowerCode() {
        return lowerCode;
    }

    public void setLowerCode(String lowerCode) {
        this.lowerCode = lowerCode;
    }

}
