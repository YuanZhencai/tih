/** * FinancialReportBean.java 
 * Created on 2014年4月15日 下午3:09:42 
 */

package com.wcs.tih.report.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.CompanyVo;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.report.service.FinancialReportService;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Title: FinancialReportBean.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright (c) 2014 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@ManagedBean
@ViewScoped
public class FinancialReportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(FinancialReportBean.class);

	@EJB
	private FinancialReportService financialReportService;

	private Map<String, Object> filter = new HashMap<String, Object>();

	private List<CompanyVo> companys;
	private CompanyVo[] selectedCompanys;

	private List<ReportSummaryHistory> histories;

	private List<CompanyManagerModel> companyItems;
	private List<Long> companyIds = new ArrayList<Long>();
	private int activeIndex;

	public FinancialReportBean() {
		logger.info("FinancialReportBean.FinancialReportBean()");
	}

	@PostConstruct
	public void init() {
		logger.info("FinancialReportBean.init()");
		searchFinancialSummaryHistory();
	}

	public void searchCompanys() {
		if (companyIds.size() == 0) {
			filter.put("companyIds", null);
		} else {
			filter.put("companyIds", companyIds);
		}
		companys = financialReportService.findCompanysBy(filter);
	}

	public void exportFinancials() {
		try {
			if (selectedCompanys.length > 0) {
				financialReportService.exportFinancials(Arrays.asList(selectedCompanys));
				searchFinancialSummaryHistory();
				activeIndex = 1;
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "操作成功：", "请刷新查看。"));
			} else {
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败：", "请选择汇总公司。"));
			}
		} catch (Exception e) {
			JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败：", "请重新操作。"));
			logger.error(e.getMessage(), e);
		}
	}

	public void searchFinancialSummaryHistory() {
		histories = financialReportService.findFinancialSummaryHistory();
	}

	public void resetSearchForm() {
		companyItems = new ArrayList<CompanyManagerModel>();
	}

	public void selectCompanys(CompanyManagerModel[] com) {
		companyItems = new ArrayList<CompanyManagerModel>();
		companyIds = new ArrayList<Long>();
		for (CompanyManagerModel vo : com) {
			companyIds.add(vo.getId());
			companyItems.add(vo);
		}
	}

	// =======================================================GET && SET ====================================== //

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public List<CompanyVo> getCompanys() {
		return companys;
	}

	public void setCompanys(List<CompanyVo> companys) {
		this.companys = companys;
	}

	public CompanyVo[] getSelectedCompanys() {
		return selectedCompanys;
	}

	public void setSelectedCompanys(CompanyVo[] selectedCompanys) {
		this.selectedCompanys = selectedCompanys;
	}

	public List<ReportSummaryHistory> getHistories() {
		return histories;
	}

	public void setHistories(List<ReportSummaryHistory> histories) {
		this.histories = histories;
	}

	public List<CompanyManagerModel> getCompanyItems() {
		return companyItems;
	}

	public void setCompanyItems(List<CompanyManagerModel> companyItems) {
		this.companyItems = companyItems;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<Long> getCompanyIds() {
		return companyIds;
	}

	public void setCompanyIds(List<Long> companyIds) {
		this.companyIds = companyIds;
	}


}
