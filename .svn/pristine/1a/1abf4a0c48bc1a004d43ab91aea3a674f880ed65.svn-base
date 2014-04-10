package com.wcs.tih.report.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.report.controller.vo.WorkFlowSummaryVo;
import com.wcs.tih.report.service.SummaryChartService;

@ManagedBean(name = "summaryChartBean")
@ViewScoped
public class SummaryChartBean {
    @EJB
    private SummaryChartService summaryChartService;
    
    private PieChartModel updateDocumentModel;//上传文档审核饼图
    private PieChartModel checkinDocumentModel;//检入文档审核饼图
    private PieChartModel askQuestionModel;//提问处理流程饼图
    private PieChartModel taxPayableModel;//应交税务综合表审核饼图
    private PieChartModel addTaxModel;//增值税进项税额抵扣情况表审核饼图
    private PieChartModel groupFeedModel;//集团发起情况反馈流程饼图
    private PieChartModel factoryModel;//工厂发起情况反馈流程饼图
    private PieChartModel typeModel;//状态的饼图
    private PieChartModel importanceModel;//重要程度
    private PieChartModel urgencyModel;//紧急程度
    private Map<String, Object> searchFormMap = new HashMap<String, Object>();//查询条件
    private static final String TIH_TAX_REQUESTFORM = DictConsts.TIH_TAX_REQUESTFORM;//申请单类型
    private static final String TASK_PAGE = "/faces/transaction/task/index.xhtml";
    private List<WorkFlowSummaryVo> summary = new ArrayList<WorkFlowSummaryVo>();
    private WorkFlowSummaryVo selectedSummary;
    private List<WfInstancemstr> summaryDetail;
    private WfInstancemstr selectedWf;
    
    private List<SelectItem> item;
    
    private String flag = "true";
    private String detail = "fasle";
    
    private String title;
    private String searchWorkFlowName = "";
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PostConstruct
    public void init(){
        selectedSummary = new WorkFlowSummaryVo();
        summaryDetail = new ArrayList<WfInstancemstr>();
        
        //任务类型（搜索下拉列表）
        item = new ArrayList<SelectItem>();
        item.add(getSelectItem("文档上传审核",DictConsts.TIH_TAX_REQUESTFORM_1));
        item.add(getSelectItem("检入文档审核",DictConsts.TIH_TAX_REQUESTFORM_2));
        item.add(getSelectItem("提问流程回答",DictConsts.TIH_TAX_REQUESTFORM_3));
        item.add(getSelectItem("应交税务综合表审核",DictConsts.TIH_TAX_REQUESTFORM_4_1));
        item.add(getSelectItem("增值税进项税额抵扣情况表审核",DictConsts.TIH_TAX_REQUESTFORM_4_2));
        item.add(getSelectItem("集团发起的情况反馈流程处理",DictConsts.TIH_TAX_REQUESTBY_1));
        item.add(getSelectItem("工厂发起的情况反馈流程处理",DictConsts.TIH_TAX_REQUESTBY_2));
        
        getSearch();
        
        createTypeModel(selectedSummary);
        createImportanceModel(summaryDetail);
        createUrgencyModel(summaryDetail);
        
    }

    private SelectItem getSelectItem(String label, String value) {
        SelectItem selectItem = new SelectItem();
        selectItem.setLabel(label);
        selectItem.setValue(value);
        return selectItem;
    }

    /**
     * 
     * <p>Description:按搜索条件查询 </p>
     */
    public void getSearch(){
        searchWorkFlowName = "";
        Object taskType = searchFormMap.get("taskType");
        if(taskType!=null){
            searchWorkFlowName = (String) taskType;
        }
        try {
            WorkFlowSummaryVo summaryVo = summaryChartService.findWorkFlowSummary(searchFormMap);
            summary = summaryVo.getSummaryVos();
            createUpdateDocumentModel(summaryVo.getUpdateDocument());
            createCheckinDocumentModel(summaryVo.getCheckinDocument());
            createAskQuestionModel(summaryVo.getAskQuestion());
            createTaxPayableModel(summaryVo.getTaxPayable());
            createAddTaxModel(summaryVo.getAddTax());
            createGroupFeedModel(summaryVo.getGroupFeed());
            createFactoryModel(summaryVo.getFactoryDeed());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void selectedSummaryDetail(){
        flag = "false";
        detail = "true";
        summaryDetail = new ArrayList<WfInstancemstr>();
        ArrayList<Integer> intList = new ArrayList<Integer>();
        intList.add(Integer.valueOf(selectedSummary.getId().toString()));
        try {
            summaryDetail = summaryChartService.findWorkFlowSummaryDetailsBy(intList, selectedSummary.getAuditor());
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
        }
        createTypeModel(selectedSummary);
        createImportanceModel(summaryDetail);
        createUrgencyModel(summaryDetail);
    }
    
    public void onRowSelect(SelectEvent event){
        RequestContext.getCurrentInstance().addCallbackParam("pieId", selectedSummary.getId());
    }
    
    public void selectPage(){
        flag = "true";
        detail = "fasle";
    }
    
    /**
     * 
     * <p>Description: 重置按钮，清空数据</p>
     */
    public void reset(){
        searchFormMap = new HashMap<String, Object>();
    }
    
    public boolean getReportBoolean(){
        if(DictConsts.TIH_TAX_REQUESTFORM_4_1.equals(searchFormMap.get("taskType")) || DictConsts.TIH_TAX_REQUESTFORM_4_2.equals(searchFormMap.get("taskType"))){
            return true;
        }
        if(searchFormMap.get("reportDatetime") != null){
            searchFormMap.put("reportDatetime", null);
        }
        return false;
    }
    
    /**
     * 
     * <p>Description: 创建上传文档的饼图</p>
     */
    private void createUpdateDocumentModel(List<WorkFlowSummaryVo> voList) {
        updateDocumentModel = new PieChartModel();  
        creatData(voList,updateDocumentModel);
    }

    public void pageJump(){
        try {
            HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
            HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
            String project = ((HttpServletRequest) request).getContextPath();
            ((HttpServletResponse) response).sendRedirect(project + TASK_PAGE+"?taskNumber="+selectedWf.getNo());
        } catch (IOException e) {
        	logger.error(e.getMessage());
        }
    }
    
    /**
     * 
     * <p>Description: 创建检入文档的饼图</p>
     */
    private void createCheckinDocumentModel(List<WorkFlowSummaryVo> voList) {
        checkinDocumentModel = new PieChartModel();  
        creatData(voList,checkinDocumentModel);
    }
    
    /**
     * 
     * <p>Description: 创建提问流程回答的饼图</p>
     */
    private void createAskQuestionModel(List<WorkFlowSummaryVo> voList) {
        askQuestionModel = new PieChartModel();  
        creatData(voList,askQuestionModel);
    }
    
    /**
     * 
     * <p>Description: 创建应交税务综合表审核的饼图</p>
     */
    private void createTaxPayableModel(List<WorkFlowSummaryVo> voList) {
        taxPayableModel = new PieChartModel();  
        creatData(voList,taxPayableModel);
    }
    
    /**
     * 
     * <p>Description: 创建增值税进项税额抵扣情况表审核的饼图</p>
     */
    private void createAddTaxModel(List<WorkFlowSummaryVo> voList) {
        addTaxModel = new PieChartModel();  
        creatData(voList,addTaxModel);
    }
    
    /**
     * 
     * <p>Description: 创建集团发起的情况反馈流程的饼图</p>
     */
    private void createGroupFeedModel(List<WorkFlowSummaryVo> voList) {
        groupFeedModel = new PieChartModel();  
        creatData(voList,groupFeedModel);
    }
    
    /**
     * 
     * <p>Description: 创建工厂发起的情况反馈流程的饼图</p>
     */
    private void createFactoryModel(List<WorkFlowSummaryVo> voList) {
        factoryModel = new PieChartModel();  
        creatData(voList,factoryModel);
    }
    
    /**
     * 
     * <p>Description: 创建处理状态的饼图</p>
     */
    private void createTypeModel(WorkFlowSummaryVo selected) {
        typeModel = new PieChartModel();
        typeModel.set("处理中", selected.getProcess());
        typeModel.set("完成", selected.getComplete());
        typeModel.set("终止", selected.getTermination());
    }
    
    /**
     * 
     * <p>Description: 创建重要程度的饼图</p>
     */
    private void createImportanceModel(List<WfInstancemstr> wfs) {
        importanceModel = new PieChartModel();
        long x = 0;//一般
        long y = 0;//重要
        long z = 0;//非常重要
        for(WfInstancemstr wf : wfs){
            if(DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1.equals(wf.getImportance())){
                x++;
            }else if (DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_2.equals(wf.getImportance())) {
                y++;
            }else if (DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_3.equals(wf.getImportance())) {
                z++;
            }
        }
        importanceModel.set("一般", x);
        importanceModel.set("重要", y);
        importanceModel.set("非常重要", z);
    }
    
    /**
     * 
     * <p>Description: 创建紧急程度的饼图</p>
     */
    private void createUrgencyModel(List<WfInstancemstr> wfs) {
        urgencyModel = new PieChartModel();
        long x = 0;//一般
        long y = 0;//重要
        long z = 0;//非常重要
        for(WfInstancemstr wf : wfs){
            if(DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1.equals(wf.getImportance())){
                x++;
            }else if (DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_2.equals(wf.getImportance())) {
                y++;
            }else if (DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_3.equals(wf.getImportance())) {
                z++;
            }
        }
        urgencyModel.set("一般", x);
        urgencyModel.set("紧急", y);
        urgencyModel.set("非常紧急", z);
    }
    
    private void creatData(List<WorkFlowSummaryVo> voList, PieChartModel model) {
        for(WorkFlowSummaryVo vo : voList){
            Long total = vo.getProcess() + vo.getComplete() + vo.getTermination();
            model.set(vo.getAuditorName(), total);
        }
    }
    
    
    // Getter & Setter

    public Map<String, Object> getSearchFormMap() {
        return searchFormMap;
    }

    public void setSearchFormMap(Map<String, Object> searchFormMap) {
        this.searchFormMap = searchFormMap;
    }

    public List<WorkFlowSummaryVo> getSummary() {
        return summary;
    }

    public void setSummary(List<WorkFlowSummaryVo> summary) {
        this.summary = summary;
    }

    public PieChartModel getUpdateDocumentModel() {
        return updateDocumentModel;
    }

    public void setUpdateDocumentModel(PieChartModel updateDocumentModel) {
        this.updateDocumentModel = updateDocumentModel;
    }

    public PieChartModel getCheckinDocumentModel() {
        return checkinDocumentModel;
    }

    public void setCheckinDocumentModel(PieChartModel checkinDocumentModel) {
        this.checkinDocumentModel = checkinDocumentModel;
    }

    public PieChartModel getAskQuestionModel() {
        return askQuestionModel;
    }

    public void setAskQuestionModel(PieChartModel askQuestionModel) {
        this.askQuestionModel = askQuestionModel;
    }

    public PieChartModel getTaxPayableModel() {
        return taxPayableModel;
    }

    public void setTaxPayableModel(PieChartModel taxPayableModel) {
        this.taxPayableModel = taxPayableModel;
    }

    public PieChartModel getAddTaxModel() {
        return addTaxModel;
    }

    public void setAddTaxModel(PieChartModel addTaxModel) {
        this.addTaxModel = addTaxModel;
    }

    public PieChartModel getGroupFeedModel() {
        return groupFeedModel;
    }

    public void setGroupFeedModel(PieChartModel groupFeedModel) {
        this.groupFeedModel = groupFeedModel;
    }

    public PieChartModel getFactoryModel() {
        return factoryModel;
    }

    public void setFactoryModel(PieChartModel factoryModel) {
        this.factoryModel = factoryModel;
    }

    public PieChartModel getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(PieChartModel typeModel) {
        this.typeModel = typeModel;
    }

    public PieChartModel getImportanceModel() {
        return importanceModel;
    }

    public void setImportanceModel(PieChartModel importanceModel) {
        this.importanceModel = importanceModel;
    }

    public PieChartModel getUrgencyModel() {
        return urgencyModel;
    }

    public void setUrgencyModel(PieChartModel urgencyModel) {
        this.urgencyModel = urgencyModel;
    }

    public WorkFlowSummaryVo getSelectedSummary() {
        return selectedSummary;
    }

    public void setSelectedSummary(WorkFlowSummaryVo selectedSummary) {
        this.selectedSummary = selectedSummary;
    }

    public List<WfInstancemstr> getSummaryDetail() {
        return summaryDetail;
    }

    public void setSummaryDetail(List<WfInstancemstr> summaryDetail) {
        this.summaryDetail = summaryDetail;
    }

    public WfInstancemstr getSelectedWf() {
        return selectedWf;
    }

    public void setSelectedWf(WfInstancemstr selectedWf) {
        this.selectedWf = selectedWf;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<SelectItem> getItem() {
        return item;
    }

    public void setItem(List<SelectItem> item) {
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearchWorkFlowName() {
        return searchWorkFlowName;
    }

    public void setSearchWorkFlowName(String searchWorkFlowName) {
        this.searchWorkFlowName = searchWorkFlowName;
    }

}
