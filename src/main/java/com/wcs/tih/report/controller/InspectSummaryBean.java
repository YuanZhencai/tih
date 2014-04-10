package com.wcs.tih.report.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.model.InvsInspectationHistory;
import com.wcs.tih.model.InvsInspectationResult;
import com.wcs.tih.model.InvsInspectationResultHistory;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.report.controller.vo.InspectVo;
import com.wcs.tih.report.service.summary.SummaryService;

@ManagedBean(name = "inspectSummaryBean")
@ViewScoped
public class InspectSummaryBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String TEMPLETE_PATH = "/faces/report/excel/";
    private static final String INSPECT_EXCEL = "inspect.xls";
    
    @EJB
    private SummaryService summaryService;
    @EJB 
    private CommonService commonService;
    
    private List<InspectVo> inspectVos;
    private InspectVo searchInspectVo;
    private InspectVo selectedInspectVo;
    private InspectVo[] selectedInspectVos;
    private List<InvsInspectationHistory> inspectHistories;
    private List<ReportSummaryHistory> summaryHistories;
    private String operateInd;
    private InvsInspectation inspect;
    private InvsInspectationHistory selectedHistory;
    private int activeIndex;
    private List<Dict> workflowStatus;
    private List<Dict> stages;
    //多种税收
    private List<String>taxTypeList;
    
    private List<Long> companys;
    private List<CompanyManagerModel> companyItems;

    
    public InspectSummaryBean() {
        inspectVos = new ArrayList<InspectVo>();
        searchInspectVo = new InspectVo();
        selectedInspectVo = new InspectVo();
        inspectHistories = new ArrayList<InvsInspectationHistory>();
        summaryHistories = new ArrayList<ReportSummaryHistory>();
        inspect = new InvsInspectation();
        selectedHistory =  new InvsInspectationHistory();
        workflowStatus = new ArrayList<Dict>();
        stages = new ArrayList<Dict>();
        taxTypeList = new ArrayList<String>();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }

    @PostConstruct
    public void init(){
        searchInspectSummaryHistory();
        initWorkflowStatus();
        initStage();
    }
    
    public void searchInspectsBy() {
        try {
            inspectVos = summaryService.findInspectsBy(searchInspectVo,companys);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
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
        stages =commonService.getDictByCat(DictConsts.TIH_TAX_REQUESTFORM_5_2,browserLang.toString());
       
    }
    
    public void inspectSummary(){
        try {
            if(selectedInspectVos.length ==0){
                showMessage(FacesMessage.SEVERITY_WARN, "请选择汇总数据");
                return;
            }
            String appPath = summaryService.getAppPath(FacesContext.getCurrentInstance());
            List<InspectVo> datas = new ArrayList<InspectVo>();
            for (int i = 0; i < selectedInspectVos.length; i++) {
                datas.add(selectedInspectVos[i]);
            }
            summaryService.summaryByTemplate(appPath+TEMPLETE_PATH, INSPECT_EXCEL, datas);
            searchInspectSummaryHistory();
            activeIndex = 1;
            showMessage(FacesMessage.SEVERITY_INFO, "汇总成功");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "汇总失败");
            logger.error(e.getMessage(), e);
        }
    }
    
    
    
    public void searchInspectSummaryHistory(){
        try {
            summaryHistories = summaryService.findSummaryHistoryByType(DictConsts.TIH_TAX_REPORT_6);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void getInspectDetail(){
        try {
            inspect = summaryService.findInspectById(selectedInspectVo.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void getInspectHistoryDetails(){
        String operate = selectedHistory.getOperateInd();
        if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(operate)||DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(operate)){
            getInspectDetail();
        }else{
            inspect = new InvsInspectation();
            inspect.setCompanyName(selectedHistory.getCompanyName());
            inspect.setInspectOrg(selectedHistory.getInspectOrg());
            inspect.setInspectStartDatetime(selectedHistory.getInspectStartDatetime());
            inspect.setInspectEndDatetime(selectedHistory.getInspectEndDatetime());
            inspect.setMissionStartDatetime(selectedHistory.getMissionStartDatetime());
            inspect.setMissionEndDatetime(selectedHistory.getMissionEndDatetime());
            inspect.setInspectType(selectedHistory.getInspectType());
            inspect.setTaxTypes(selectedHistory.getTaxTypes());
            inspect.setMainProblemDesc(selectedHistory.getMainProblemDesc());
            inspect.setRectificationPlan(selectedHistory.getRectificationPlan());
            inspect.setRectificationResult(selectedHistory.getRectificationResult());
            List<InvsInspectationResult> results = new ArrayList<InvsInspectationResult>();
            InvsInspectationResult result = null;
            if(selectedHistory.getInvsInspectationResultHistories()!=null){
                for (InvsInspectationResultHistory historyResult : selectedHistory.getInvsInspectationResultHistories()) {
                    result = new InvsInspectationResult();
                    result.setTaxType(historyResult.getTaxType());
                    result.setOverdueTax(historyResult.getOverdueTax());
                    result.setPenalty(historyResult.getPenalty());
                    result.setInputTaxTurnsOut(historyResult.getInputTaxTurnsOut());
                    result.setReductionPrevLoss(historyResult.getReductionPrevLoss());
                    result.setFine(historyResult.getFine());
                    result.setSituationRemarks(historyResult.getSituationRemarks());
                    results.add(result);
                }
            }
            inspect.setInvsInspectationResults(results);
        }
    }
    
    public void searchInspectHistory(){
        try {
            inspectHistories = summaryService.searchInspectHistory(selectedInspectVo.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
    }
    
    public void handleHistoryChange(){
        if(selectedInspectVo!=null&&selectedInspectVo.getId()!=null){
            try {
                inspectHistories = new ArrayList<InvsInspectationHistory>();
                List<InvsInspectationHistory> histories = summaryService.searchInspectHistory(selectedInspectVo.getId());
                if(operateInd!=null&&!"".equals(operateInd)){
                    for (InvsInspectationHistory history : histories) {
                        if(operateInd.equals(history.getOperateInd())){
                            inspectHistories.add(history);
                        }
                    }
                }else{
                    inspectHistories = histories;
                }
                if(inspectHistories!=null&&inspectHistories.size()>0){
                    selectedHistory = inspectHistories.get(0);
                    getInspectHistoryDetails();
                }else{
                    inspect = new InvsInspectation();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
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
        searchInspectVo = new InspectVo();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }
    //---------------------------------------- GET && SET --------------------------------------//
    
    public List<InspectVo> getInspectVos() {
        return inspectVos;
    }

    public void setInspectVos(List<InspectVo> inspectVos) {
        this.inspectVos = inspectVos;
    }

    public InspectVo getSearchInspectVo() {
        return searchInspectVo;
    }

    public void setSearchInspectVo(InspectVo searchInspectVo) {
        this.searchInspectVo = searchInspectVo;
    }

    public InspectVo getSelectedInspectVo() {
        return selectedInspectVo;
    }

    public void setSelectedInspectVo(InspectVo selectedInspectVo) {
        this.selectedInspectVo = selectedInspectVo;
    }

    public InspectVo[] getSelectedInspectVos() {
        return selectedInspectVos;
    }

    public void setSelectedInspectVos(InspectVo[] selectedInspectVos) {
        this.selectedInspectVos = selectedInspectVos;
    }

    public List<InvsInspectationHistory> getInspectHistories() {
        return inspectHistories;
    }

    public void setInspectHistories(List<InvsInspectationHistory> inspectHistories) {
        this.inspectHistories = inspectHistories;
    }

    public List<ReportSummaryHistory> getSummaryHistories() {
        return summaryHistories;
    }

    public void setSummaryHistories(List<ReportSummaryHistory> summaryHistories) {
        this.summaryHistories = summaryHistories;
    }

    public String getOperateInd() {
        return operateInd;
    }

    public void setOperateInd(String operateInd) {
        this.operateInd = operateInd;
    }

    public InvsInspectation getInspect() {
        return inspect;
    }

    public void setInspect(InvsInspectation inspect) {
        this.inspect = inspect;
    }

    public InvsInspectationHistory getSelectedHistory() {
        return selectedHistory;
    }

    public void setSelectedHistory(InvsInspectationHistory selectedHistory) {
        this.selectedHistory = selectedHistory;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
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
    
    public List<String> getTaxTypeList() {
        taxTypeList = new ArrayList<String>();
        if(inspect!=null&&inspect.getTaxTypes()!=null&&!"".equals(inspect.getTaxTypes())){
            String[] tmpTaxType = this.inspect.getTaxTypes().split(",");
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
