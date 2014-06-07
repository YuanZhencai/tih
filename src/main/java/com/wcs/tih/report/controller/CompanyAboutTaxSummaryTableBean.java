package com.wcs.tih.report.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.util.PageModel;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.report.service.CompanyAboutTaxSummaryTableService;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 公司涉税信息汇总Bean
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "companyAboutTaxSummaryTableBean")
@ViewScoped
public class CompanyAboutTaxSummaryTableBean {
	@EJB
	private CompanyAboutTaxSummaryTableService companyAboutTaxSummaryTableService;
	private LazyDataModel<CompanyManagerModel> lazyCompanyManagerModelModel;
	private CompanyManagerModel[] selectedCompanyManagerModels;
	private LazyDataModel<ReportSummaryHistory> lazyReportSummaryHistoryModel;
	private String companyName;
	private List<Long> companys = new ArrayList<Long>();
	private List<CompanyManagerModel> companyItems = new ArrayList<CompanyManagerModel>();
	private List<CompanyManagerModel> taxCompanys = new ArrayList<CompanyManagerModel>();
	private int activeIndex;

	private Date annual = new Date();

	/**
	 * <p>
	 * Description: 初始化
	 * </p>
	 */

	@PostConstruct
	public void init() {
		companyName = "";
		queryCompany();
		queryTaxSummary();
	}

	/**
	 * <p>
	 * Description: 查询公司
	 * </p>
	 */
	public void queryCompany() {
		taxCompanys = companyAboutTaxSummaryTableService.getCompanyByName(companys);
		selectedCompanyManagerModels = null;
	}

	/**
	 * <p>
	 * Description: 查询公司涉税信息汇总历史
	 * </p>
	 */
	public void queryTaxSummary() {
		List<ReportSummaryHistory> rshs = companyAboutTaxSummaryTableService.getReportSummaryHistory(DictConsts.TIH_TAX_REPORT_1);
		if (null != rshs && rshs.size() != 0) {
			lazyReportSummaryHistoryModel = new PageModel<ReportSummaryHistory>(rshs, false);
		} else {
			lazyReportSummaryHistoryModel = null;
		}
	}

	/**
	 * <p>
	 * Description: 涉税信息汇总
	 * </p>
	 */
	public void aboutTaxSummary() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean validated = true;
		if (selectedCompanyManagerModels == null || selectedCompanyManagerModels.length == 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "请选择公司"));
			validated = false;
		}
		if (annual == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "请选择年度"));
			validated = false;
		}
		try {
			if (validated) {
				companyAboutTaxSummaryTableService.companyAboutTaxSummary(selectedCompanyManagerModels, annual);
				queryTaxSummary();
				activeIndex = 1;
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "汇总成功，请下载查看！"));
			}
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
		}
	}

	/**
	 * <p>
	 * Description: 下载
	 * </p>
	 * 
	 * @param documentId
	 * @return
	 */
	public StreamedContent download(String documentId) {
		return companyAboutTaxSummaryTableService.download(documentId);
	}

	public void selectCompanys(CompanyManagerModel[] com) {
		companys = new ArrayList<Long>();
		companyItems = new ArrayList<CompanyManagerModel>();
		for (CompanyManagerModel vo : com) {
			companys.add(vo.getId());
			companyItems.add(vo);
		}
	}

	public void reset() {
		companys = new ArrayList<Long>();
		companyItems = new ArrayList<CompanyManagerModel>();
	}

	public LazyDataModel<CompanyManagerModel> getLazyCompanyManagerModelModel() {
		return lazyCompanyManagerModelModel;
	}

	public void setLazyCompanyManagerModelModel(LazyDataModel<CompanyManagerModel> lazyCompanyManagerModelModel) {
		this.lazyCompanyManagerModelModel = lazyCompanyManagerModelModel;
	}

	public CompanyManagerModel[] getSelectedCompanyManagerModels() {
		return selectedCompanyManagerModels;
	}

	public void setSelectedCompanyManagerModels(CompanyManagerModel[] selectedCompanyManagerModels) {
		this.selectedCompanyManagerModels = selectedCompanyManagerModels;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public LazyDataModel<ReportSummaryHistory> getLazyReportSummaryHistoryModel() {
		return lazyReportSummaryHistoryModel;
	}

	public void setLazyReportSummaryHistoryModel(LazyDataModel<ReportSummaryHistory> lazyReportSummaryHistoryModel) {
		this.lazyReportSummaryHistoryModel = lazyReportSummaryHistoryModel;
	}

	public List<Long> getCompanys() {
		return companys;
	}

	public void setCompanys(List<Long> companys) {
		this.companys = companys;
	}

	public List<CompanyManagerModel> getCompanyItems() {
		return companyItems;
	}

	public void setCompanyItems(List<CompanyManagerModel> companyItems) {
		this.companyItems = companyItems;
	}

	public List<CompanyManagerModel> getTaxCompanys() {
		return taxCompanys;
	}

	public void setTaxCompanys(List<CompanyManagerModel> taxCompanys) {
		this.taxCompanys = taxCompanys;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public Date getAnnual() {
		return annual;
	}

	public void setAnnual(Date annual) {
		this.annual = annual;
	}

}
