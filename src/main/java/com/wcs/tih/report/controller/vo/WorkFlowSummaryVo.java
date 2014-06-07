package com.wcs.tih.report.controller.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkFlowSummaryVo  extends com.wcs.base.model.IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String workFlowType;
    private String name;
    private String auditor;
    private Long process;
    private Long complete;
    private Long termination;
    private String auditorName;
    private List<WorkFlowSummaryVo> summaryVos = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> updateDocument = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> checkinDocument = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> askQuestion = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> taxPayable = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> addTax = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> groupFeed = new ArrayList<WorkFlowSummaryVo>();
    private List<WorkFlowSummaryVo> factoryDeed = new ArrayList<WorkFlowSummaryVo>();
    
    public String getWorkFlowType() {
        return workFlowType;
    }
    public void setWorkFlowType(String workFlowType) {
        this.workFlowType = workFlowType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuditor() {
        return auditor;
    }
    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }
    public Long getProcess() {
        return process;
    }
    public void setProcess(Long process) {
        this.process = process;
    }
    public Long getComplete() {
        return complete;
    }
    public void setComplete(Long complete) {
        this.complete = complete;
    }
    public Long getTermination() {
        return termination;
    }
    public void setTermination(Long termination) {
        this.termination = termination;
    }
    public String getAuditorName() {
        return auditorName;
    }
    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }
    public List<WorkFlowSummaryVo> getSummaryVos() {
        return summaryVos;
    }
    public void setSummaryVos(List<WorkFlowSummaryVo> summaryVos) {
        this.summaryVos = summaryVos;
    }
    public List<WorkFlowSummaryVo> getUpdateDocument() {
        return updateDocument;
    }
    public void setUpdateDocument(List<WorkFlowSummaryVo> updateDocument) {
        this.updateDocument = updateDocument;
    }
    public List<WorkFlowSummaryVo> getCheckinDocument() {
        return checkinDocument;
    }
    public void setCheckinDocument(List<WorkFlowSummaryVo> checkinDocument) {
        this.checkinDocument = checkinDocument;
    }
    public List<WorkFlowSummaryVo> getAskQuestion() {
        return askQuestion;
    }
    public void setAskQuestion(List<WorkFlowSummaryVo> askQuestion) {
        this.askQuestion = askQuestion;
    }
    public List<WorkFlowSummaryVo> getTaxPayable() {
        return taxPayable;
    }
    public void setTaxPayable(List<WorkFlowSummaryVo> taxPayable) {
        this.taxPayable = taxPayable;
    }
    public List<WorkFlowSummaryVo> getAddTax() {
        return addTax;
    }
    public void setAddTax(List<WorkFlowSummaryVo> addTax) {
        this.addTax = addTax;
    }
    public List<WorkFlowSummaryVo> getGroupFeed() {
        return groupFeed;
    }
    public void setGroupFeed(List<WorkFlowSummaryVo> groupFeed) {
        this.groupFeed = groupFeed;
    }
    public List<WorkFlowSummaryVo> getFactoryDeed() {
        return factoryDeed;
    }
    public void setFactoryDeed(List<WorkFlowSummaryVo> factoryDeed) {
        this.factoryDeed = factoryDeed;
    }
    
}
