package com.wcs.tih.report.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.InvsAntiAvoidance;
import com.wcs.tih.model.InvsAntiAvoidanceHistory;
import com.wcs.tih.model.InvsAntiResult;
import com.wcs.tih.model.InvsAntiResultHistory;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.report.controller.vo.AntiAvoidanceVo;
import com.wcs.tih.report.service.summary.SummaryService;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@ManagedBean(name = "antiSummaryBean")
@ViewScoped
public class AntiSummaryBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String TEMPLETE_PATH = "/faces/report/excel/";
    private static final String ANTIAVOIDANCE_EXCEL = "antiAvoidance.xls";
    
    @EJB
    private SummaryService summaryService;
    @EJB 
    private CommonService commonService;
    
    
    private List<AntiAvoidanceVo> antiAvoidanceVos;
    private AntiAvoidanceVo searchAntiAvoidanceVo;
    private AntiAvoidanceVo selectedAntiAvoidanceVo;
    private AntiAvoidanceVo[] selectedAntiAvoidanceVos;
    private List<ReportSummaryHistory> antiAvoidanceHistories;
    private List<InvsAntiAvoidanceHistory> antiHistories;
    private String operateInd;
    private InvsAntiAvoidance antiAvoidance;
    private List<Dict> workflowStatus;
    private List<Dict> stages;
    private InvsAntiAvoidanceHistory selectedHistory;
    private int activeIndex;
    //多种税收
    private List<String>taxTypeList;
    
    private List<Long> companys;
    private List<CompanyManagerModel> companyItems;
    
    public AntiSummaryBean() {
        antiAvoidanceVos = new ArrayList<AntiAvoidanceVo>();
        searchAntiAvoidanceVo = new AntiAvoidanceVo();
        selectedAntiAvoidanceVo = new AntiAvoidanceVo();
        antiAvoidanceHistories = new ArrayList<ReportSummaryHistory>();
        antiHistories = new ArrayList<InvsAntiAvoidanceHistory>();
        antiAvoidance = new InvsAntiAvoidance();
        workflowStatus = new ArrayList<Dict>();
        stages = new ArrayList<Dict>();
        selectedHistory = new InvsAntiAvoidanceHistory();
        taxTypeList = new ArrayList<String>();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }
    
    @PostConstruct
    public void init(){
        initWorkflowStatus();
        initStage();
        searchAntiSummaryHistory();
    }
    
    public void initWorkflowStatus(){
        Locale browserLang=FacesContext.getCurrentInstance().getViewRoot().getLocale();
        List<Dict> list = commonService.getDictByCat(DictConsts.TIH_TAX_WORKFLOWSTATUS,browserLang.toString());
        for (Dict dict : list) {
            String key = dict.getCodeCat()+"."+dict.getCodeKey();
            if(DictConsts.TIH_TAX_WORKFLOWSTATUS_2.equals(key)||DictConsts.TIH_TAX_WORKFLOWSTATUS_3.equals(key)){
                workflowStatus.add(dict);
            }
        }
    }
    
    public void initStage(){
        Locale browserLang=FacesContext.getCurrentInstance().getViewRoot().getLocale();
        stages =commonService.getDictByCat(DictConsts.TIH_TAX_REQUESTFORM_5_4,browserLang.toString());
       
    }
    
    public void searchAntiAvoidancesBy() {
        try {
            antiAvoidanceVos = summaryService.findAntiAvoidancesBy(searchAntiAvoidanceVo,companys);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void antiAvoidanceSummary(){
        try {
            if(selectedAntiAvoidanceVos.length ==0){
                showMessage(FacesMessage.SEVERITY_WARN, "请选择汇总数据");
                return;
            }
            String appPath = summaryService.getAppPath(FacesContext.getCurrentInstance());
            List<AntiAvoidanceVo> datas = new ArrayList<AntiAvoidanceVo>();
            for (int i = 0; i < selectedAntiAvoidanceVos.length; i++) {
                datas.add(selectedAntiAvoidanceVos[i]);
            }
            summaryService.summaryByTemplate(appPath+TEMPLETE_PATH, ANTIAVOIDANCE_EXCEL, datas);
            searchAntiSummaryHistory();
            activeIndex = 1;
            showMessage(FacesMessage.SEVERITY_INFO, "汇总成功");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "汇总失败");
            logger.error(e.getMessage(), e);
        }
    }

    public void searchAntiSummaryHistory(){
        try {
            antiAvoidanceHistories = summaryService.findSummaryHistoryByType(DictConsts.TIH_TAX_REPORT_5);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void getAntiAvoidanceDetails(){
        try {
            antiAvoidance = summaryService.findAntiAvoidanceById(selectedAntiAvoidanceVo.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void getAntiHistoryDetails(){
        try {
            String operate = selectedHistory.getOperateInd();
            if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(operate)||DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(operate)){
                getAntiAvoidanceDetails();
            }else{
                antiAvoidance = new InvsAntiAvoidance();
                antiAvoidance.setCompanymstrId(selectedHistory.getCompanymstrId());
                antiAvoidance.setSponsorOrg(selectedHistory.getSponsorOrg());
                antiAvoidance.setImplementOrg(selectedHistory.getImplementOrg());
                antiAvoidance.setCause(selectedHistory.getCause());
                antiAvoidance.setInvestType(selectedHistory.getInvestType());
                antiAvoidance.setTaxTypes(selectedHistory.getTaxTypes());
                antiAvoidance.setInvestStartDatetime(selectedHistory.getInvestStartDatetime());
                antiAvoidance.setInvestEndDatetime(selectedHistory.getInvestEndDatetime());
                antiAvoidance.setMissionStartDatetime(selectedHistory.getMissionStartDatetime());
                antiAvoidance.setMissionEndDatetime(selectedHistory.getMissionEndDatetime());
                antiAvoidance.setMethod(selectedHistory.getMethod());
                antiAvoidance.setDoubt(selectedHistory.getDoubt());
                antiAvoidance.setRiskAccount(selectedHistory.getRiskAccount());
                antiAvoidance.setDealWith(selectedHistory.getDealWith());
                antiAvoidance.setPhaseRemarks(selectedHistory.getPhaseRemarks());
                antiAvoidance.setConclusion(selectedHistory.getConclusion());
                antiAvoidance.setTraceStartDatetime(selectedHistory.getTraceStartDatetime());
                antiAvoidance.setTraceEndDatetime(selectedHistory.getTraceEndDatetime());
                antiAvoidance.setContact(selectedHistory.getContact());
                List<InvsAntiResult> results = new ArrayList<InvsAntiResult>();
                InvsAntiResult result = null;
                if(selectedHistory.getInvsAntiResultHistories()!=null){
                    for (InvsAntiResultHistory historyResult : selectedHistory.getInvsAntiResultHistories()) {
                        result = new InvsAntiResult();
                        result.setVat(historyResult.getVat());
                        result.setCit(historyResult.getCit());
                        result.setAddInterest(historyResult.getAddInterest());
                        result.setAddFine(historyResult.getAddFine());
                        result.setReducedLoss(historyResult.getReducedLoss());
                        results.add(result);
                    }
                }
                antiAvoidance.setInvsAntiResults(results);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void searchAntiAvoidanceHistory(){
        try {
            antiHistories = summaryService.searchAntiAvoidanceHistory(selectedAntiAvoidanceVo.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }

    public void handleHistoryChange(){
        if(selectedAntiAvoidanceVo!=null&&selectedAntiAvoidanceVo.getId()!=null){
            try {
                antiHistories = new ArrayList<InvsAntiAvoidanceHistory>();
                List<InvsAntiAvoidanceHistory> histories = summaryService.searchAntiAvoidanceHistory(selectedAntiAvoidanceVo.getId());
                if(operateInd!=null&&!"".equals(operateInd)){
                    for (InvsAntiAvoidanceHistory history : histories) {
                        if(operateInd.equals(history.getOperateInd())){
                            antiHistories.add(history);
                        }
                    }
                }else{
                    antiHistories = histories;
                }
                if(antiHistories!=null&&antiHistories.size()>0){
                    selectedHistory = antiHistories.get(0);
                    getAntiHistoryDetails();
                }else{
                    antiAvoidance = new InvsAntiAvoidance();
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void selectCompanys(CompanyManagerModel[] com) {
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
        for (CompanyManagerModel vo : com) {
            companys.add(vo.getId());
            companyItems.add(vo);
        }
    }
    
    /**
     * <p>Description: 下载</p>
     * @param documentId
     * @return
     */
    public StreamedContent download(String documentId) {
        StreamedContent download = null;
        try {
            download = summaryService.download(documentId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return download;
    }
    
    public void showMessage(Severity severityType,String message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityType, "" , message));
    }
    
    public String getUserName(String str) {
        return summaryService.getUserName(str);
    }
    
    public void resetSearchForm(){
        searchAntiAvoidanceVo = new AntiAvoidanceVo();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }
    //---------------------------------------- GET && SET --------------------------------------//
    
    
    public AntiAvoidanceVo getSelectedAntiAvoidanceVo() {
        return selectedAntiAvoidanceVo;
    }

    public void setSelectedAntiAvoidanceVo(AntiAvoidanceVo selectedAntiAvoidanceVo) {
        this.selectedAntiAvoidanceVo = selectedAntiAvoidanceVo;
    }

    public List<ReportSummaryHistory> getAntiAvoidanceHistories() {
        return antiAvoidanceHistories;
    }

    public void setAntiAvoidanceHistories(List<ReportSummaryHistory> antiAvoidanceHistories) {
        this.antiAvoidanceHistories = antiAvoidanceHistories;
    }

    public List<AntiAvoidanceVo> getAntiAvoidanceVos() {
        return antiAvoidanceVos;
    }

    public void setAntiAvoidanceVos(List<AntiAvoidanceVo> antiAvoidanceVos) {
        this.antiAvoidanceVos = antiAvoidanceVos;
    }

    public AntiAvoidanceVo getSearchAntiAvoidanceVo() {
        return searchAntiAvoidanceVo;
    }

    public void setSearchAntiAvoidanceVo(AntiAvoidanceVo searchAntiAvoidanceVo) {
        this.searchAntiAvoidanceVo = searchAntiAvoidanceVo;
    }

    public AntiAvoidanceVo[] getSelectedAntiAvoidanceVos() {
        return selectedAntiAvoidanceVos;
    }

    public void setSelectedAntiAvoidanceVos(AntiAvoidanceVo[] selectedAntiAvoidanceVos) {
        this.selectedAntiAvoidanceVos = selectedAntiAvoidanceVos;
    }

    public String getOperateInd() {
        return operateInd;
    }

    public void setOperateInd(String operateInd) {
        this.operateInd = operateInd;
    }

    public List<InvsAntiAvoidanceHistory> getAntiHistories() {
        return antiHistories;
    }

    public void setAntiHistories(List<InvsAntiAvoidanceHistory> antiHistories) {
        this.antiHistories = antiHistories;
    }

    public InvsAntiAvoidance getAntiAvoidance() {
        return antiAvoidance;
    }

    public void setAntiAvoidance(InvsAntiAvoidance antiAvoidance) {
        this.antiAvoidance = antiAvoidance;
    }

    public List<Dict> getWorkflowStatus() {
        return workflowStatus;
    }

    public void setWorkflowStatus(List<Dict> workflowStatus) {
        this.workflowStatus = workflowStatus;
    }

    public List<Dict> getStages() {
        return stages;
    }

    public void setStages(List<Dict> stages) {
        this.stages = stages;
    }

    public InvsAntiAvoidanceHistory getSelectedHistory() {
        return selectedHistory;
    }

    public void setSelectedHistory(InvsAntiAvoidanceHistory selectedHistory) {
        this.selectedHistory = selectedHistory;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public List<String> getTaxTypeList() {
        taxTypeList = new ArrayList<String>();
        if(antiAvoidance!=null&&antiAvoidance.getTaxTypes()!=null&&!"".equals(antiAvoidance.getTaxTypes())){
            String[] tmpTaxType = this.antiAvoidance.getTaxTypes().split(",");
            for (int i = 0; i < tmpTaxType.length; i++) {
                taxTypeList.add(tmpTaxType[i].trim());
            }
        }
        return taxTypeList;
    }

    public void setTaxTypeList(List<String> taxTypeList) {
        this.taxTypeList = taxTypeList;
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
    
}
