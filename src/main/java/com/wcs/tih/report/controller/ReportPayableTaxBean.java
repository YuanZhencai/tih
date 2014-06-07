package com.wcs.tih.report.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.report.controller.vo.ReportPayableTaxVO;
import com.wcs.tih.report.service.ReportPayableTaxService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: 应交税费
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class ReportPayableTaxBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @EJB
    private ReportPayableTaxService taxService;

    private Map<String, Object> query;
    private LazyDataModel<ReportPayableTaxVO> lazyData;
    private ReportPayableTaxVO[] selections;
    private List<ReportSummaryHistory> reports;
    private List<Long> companys = new ArrayList<Long>();
    private List<CompanyManagerModel> companyItems = new ArrayList<CompanyManagerModel>();

    private List<ReportPayableTaxVO> payTaxCompanys = new ArrayList<ReportPayableTaxVO>();
    private int activeIndex;

    @PostConstruct
    public void init() {
        query = new HashMap<String, Object>(2);
        setLazyData(new PageModel<ReportPayableTaxVO>(new ArrayList<ReportPayableTaxVO>(), false));
        reports = taxService.getHistorys();
    }

    /**
     * <p>Description: 查询出要汇总的公司 </p>
     */
    public void searchCompanys() {
        if(!ValidateUtil.validateRequired(FacesContext.getCurrentInstance(), query.get("statisticDate"), "统计日期：")){
        	return;
        }
        payTaxCompanys  = taxService.getCompanys(query,companys);
        reports = taxService.getHistorys();
    }

    /**
     * <p>Description: 下载公司以前上传的某月份报表 </p>
     * @return
     */
    public StreamedContent getTaxExcel(Long currentTaxId) {
        String fileId;
        try {
            fileId = taxService.getFileIdByTaxId(currentTaxId);
        } catch (Exception e1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, e1.getMessage(), ""));
            return null;
        }
        try {
            return taxService.downloadReport(fileId);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载错误", ""));
            return null;
        }
    }

    /**
     * <p>Description: 下载公司汇总报表 </p>
     * @return
     */
    public StreamedContent getTotalTaxExcel(String fileId) {
        try {
            return taxService.downloadReport(fileId);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载错误", ""));
            return null;
        }
    }

    public void collectCompanysTax() {
        if (selections == null || selections.length == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "请先选择要汇总的公司", ""));
            return;
        }
        try {
            taxService.generateExcelReport(selections, (Date) query.get("statisticDate"));
            reports = taxService.getHistorys();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "汇总成功，请下载", ""));
            activeIndex = 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        }
    }

    public void selectCompanys(CompanyManagerModel[] com){
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
        for (CompanyManagerModel vo : com) {
            companys.add(vo.getId());
            companyItems.add(vo);
        }
    }
    
    public void reset(){
        query = new HashMap<String, Object>();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }
    
    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public LazyDataModel<ReportPayableTaxVO> getLazyData() {
        return lazyData;
    }

    public void setLazyData(LazyDataModel<ReportPayableTaxVO> lazyData) {
        this.lazyData = lazyData;
    }

    public ReportPayableTaxVO[] getSelections() {
        return selections;
    }

    public void setSelections(ReportPayableTaxVO[] selections) {
        this.selections = selections;
    }

    public List<ReportSummaryHistory> getReports() {
        return reports;
    }

    public void setReports(List<ReportSummaryHistory> reports) {
        this.reports = reports;
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

    public List<ReportPayableTaxVO> getPayTaxCompanys() {
        return payTaxCompanys;
    }

    public void setPayTaxCompanys(List<ReportPayableTaxVO> payTaxCompanys) {
        this.payTaxCompanys = payTaxCompanys;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

}
