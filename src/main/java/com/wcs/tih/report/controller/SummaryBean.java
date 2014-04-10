package com.wcs.tih.report.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.wcs.tih.model.InvsTransferPrice;
import com.wcs.tih.model.InvsTransferPriceHistory;
import com.wcs.tih.model.InvsVerifyTransType;
import com.wcs.tih.model.InvsVerifyTransTypeHistory;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.report.service.summary.SummaryService;
import com.wcs.tih.transaction.controller.vo.TransTypeVo;
import com.wcs.tih.transaction.controller.vo.TransferPriceVo;
import com.wcs.tih.transaction.service.TransferPriceService;
import com.wcs.tih.util.ArithUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@ManagedBean(name = "summaryBean")
@ViewScoped
public class SummaryBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @EJB
    private SummaryService summaryService;
    @EJB
    private TransferPriceService transferPriceService;
    
    
    private static final String TEMPLETE_PATH = "/faces/report/excel/";
    private static final String TRANSPRICE_EXCEL = "transPrice.xls";
    
    private List<TransferPriceVo> transPriceVos;
    private TransferPriceVo searchTransPrice;
    private InvsTransferPrice selectedTransPrice;
    private InvsTransferPriceHistory selectedHistory;
    private List<ReportSummaryHistory> transPriceHistories;
    private boolean selectedAll = true;
    private List<InvsTransferPriceHistory> priceHistories;
    private String operateInd;
    private TransferPriceVo transferPriceVo;
    private List<TransTypeVo> transTypeVos;
    private int activeIndex;
    private boolean checkAll;
    private TransferPriceVo[] selectedTransPriceVos;
    private List<Long> companys;
    private List<CompanyManagerModel> companyItems;
    
    
    
    public SummaryBean() {
        searchTransPrice = new TransferPriceVo();
        selectedTransPrice = new InvsTransferPrice();
        transPriceHistories = new ArrayList<ReportSummaryHistory>();
        transPriceVos = new ArrayList<TransferPriceVo>();
        priceHistories = new ArrayList<InvsTransferPriceHistory>();
        transferPriceVo = new TransferPriceVo();
        transTypeVos = new ArrayList<TransTypeVo>();
        selectedHistory = new InvsTransferPriceHistory();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }
    
    @PostConstruct
    public void init(){
        searchSummaryTransPriceHistory();
    }

    
    public void checkAll(){
        for (TransferPriceVo tpVo : transPriceVos) {
            if(tpVo.isParent()){
                tpVo.setSelected(checkAll);
            }
        }
    }
    
    public void searchTransPricesBy() {
        try {
            searchTransPrice.setCompanys(companys);
            transPriceVos = summaryService.findTransPricesBy(searchTransPrice);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void transPriceSummary(){
        String appPath = getAppPath();
        try {
            List<InvsTransferPrice> datas = new ArrayList<InvsTransferPrice>();
            for (TransferPriceVo tpVo : transPriceVos) {
                if(tpVo.isParent()&&tpVo.isSelected()){
                    datas.add(tpVo.getTransferPrice());
                }
            }
            if(datas.size() ==0){
                showMessage(FacesMessage.SEVERITY_WARN, "请选择汇总数据");
                return;
            }
            summaryService.summaryByTemplate(appPath + TEMPLETE_PATH, TRANSPRICE_EXCEL, datas);
            searchSummaryTransPriceHistory();
            activeIndex = 1;
            showMessage(FacesMessage.SEVERITY_INFO, "汇总成功");
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "汇总失败");
            logger.error(e.getMessage(), e);
        }
    }
    
    public void searchSummaryTransPriceHistory(){
        try {
            transPriceHistories = summaryService.findSummaryHistoryByType(DictConsts.TIH_TAX_REPORT_4);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
    
    
    public void  searchPriceHistories(){
        priceHistories = transferPriceService.searchPriceHistories(selectedTransPrice);
    }
    
    public void getTransPriceDetail(){
        transferPriceVo = new TransferPriceVo();
        transferPriceVo.setId(selectedTransPrice.getId());
        transferPriceVo.setTransferPrice(selectedTransPrice);
        transferPriceVo.setCompanymstrId(selectedTransPrice.getCompanymstrId());
        transferPriceVo.setCompanyName(selectedTransPrice.getCompanyName());
        transferPriceVo.setDecade(selectedTransPrice.getDecade());
        transferPriceVo.setAssoDebtEquityRatio(selectedTransPrice.getAssoDebtEquityRatio());
        transferPriceVo.setPrepareDocInd(selectedTransPrice.getPrepareDocInd());
        transferPriceVo.setSubmitDocInd(selectedTransPrice.getSubmitDocInd());
        transferPriceVo.setDocSubmitDatetime(selectedTransPrice.getDocSubmitDatetime());
        transferPriceVo.setRemarks(selectedTransPrice.getRemarks());
        transTypeVos = new ArrayList<TransTypeVo>();
        TransTypeVo typeVo = null;
        for (InvsVerifyTransType type : selectedTransPrice.getInvsVerifyTransTypes()) {
            typeVo = new TransTypeVo();
            typeVo.setTransType(type.getTransType());
            typeVo.setValidationMethod(type.getValidationMethod());
            typeVo.setCompareCompanyMedian(ArithUtil.mul(type.getCompareCompanyMedian(), 100));
            typeVo.setBeforeAdjustRatio(ArithUtil.mul(type.getBeforeAdjustRatio(), 100));
            typeVo.setAfterAdjustRatio(ArithUtil.mul(type.getAfterAdjustRatio(), 100));
            typeVo.setAdjustSpecialReason(type.getAdjustSpecialReason());
            transTypeVos.add(typeVo);
        }
    }
    
    public void getHistoryDetail(){
        String operate = selectedHistory.getOperateInd();
        if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(operate)||DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(operate)){
            getTransPriceDetail();
        }else{
            transferPriceVo = new TransferPriceVo();
            transferPriceVo.setId(selectedHistory.getId());
            transferPriceVo.setTransferPrice(selectedTransPrice);
            transferPriceVo.setCompanymstrId(selectedHistory.getCompanymstrId());
            transferPriceVo.setCompanyName(selectedHistory.getCompanyName());
            transferPriceVo.setDecade(selectedHistory.getDecade());
            transferPriceVo.setAssoDebtEquityRatio(selectedHistory.getAssoDebtEquityRatio());
            transferPriceVo.setPrepareDocInd(selectedHistory.getPrepareDocInd());
            transferPriceVo.setSubmitDocInd(selectedHistory.getSubmitDocInd());
            transferPriceVo.setDocSubmitDatetime(selectedHistory.getDocSubmitDatetime());
            transferPriceVo.setRemarks(selectedHistory.getRemarks());
            transTypeVos = new ArrayList<TransTypeVo>();
            TransTypeVo typeVo = null;
            for (InvsVerifyTransTypeHistory type : selectedHistory.getInvsVerifyTransTypeHistories()) {
                typeVo = new TransTypeVo();
                typeVo.setTransType(type.getTransType());
                typeVo.setValidationMethod(type.getValidationMethod());
                typeVo.setCompareCompanyMedian(ArithUtil.mul(type.getCompareCompanyMedian(), 100));
                typeVo.setBeforeAdjustRatio(ArithUtil.mul(type.getBeforeAdjustRatio(), 100));
                typeVo.setAfterAdjustRatio(ArithUtil.mul(type.getAfterAdjustRatio(), 100));
                typeVo.setAdjustSpecialReason(type.getAdjustSpecialReason());
                transTypeVos.add(typeVo);
            }
        }
    }
    
    public void handleHistoryChange(){
        if(selectedTransPrice!=null&&selectedTransPrice.getId()!=null){
            priceHistories = new ArrayList<InvsTransferPriceHistory>();
            List<InvsTransferPriceHistory> histories = transferPriceService.searchPriceHistories(selectedTransPrice);
            if(operateInd!=null&&!"".equals(operateInd)){
                for (InvsTransferPriceHistory history : histories) {
                    if(operateInd.equals(history.getOperateInd())){
                        priceHistories.add(history);
                    }
                }
            }else{
                priceHistories = histories;
            }
        }
        if(priceHistories!=null&&priceHistories.size()>0){
            selectedHistory = priceHistories.get(0);
            getHistoryDetail();
        }else{
            transferPriceVo = new TransferPriceVo();
            transTypeVos = new ArrayList<TransTypeVo>();
        }
    }
    
    public String getAppPath(){
        return summaryService.getAppPath(FacesContext.getCurrentInstance());
    }

    public void showMessage(Severity severityType,String message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityType, "" , message));
    }
    
    public String getUserName(String str) {
        return summaryService.getUserName(str);
    }
    
    public void resetSearchForm(){
        searchTransPrice = new TransferPriceVo();
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
    }
    
    public void selectCompanys(CompanyManagerModel[] com) {
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
        for (CompanyManagerModel vo : com) {
            companys.add(vo.getId());
            companyItems.add(vo);
        }
    }
    
    //---------------------------------------- GET && SET --------------------------------------//
    

    public InvsTransferPrice getSelectedTransPrice() {
        return selectedTransPrice;
    }

    public TransferPriceVo getSearchTransPrice() {
        return searchTransPrice;
    }

    public void setSearchTransPrice(TransferPriceVo searchTransPrice) {
        this.searchTransPrice = searchTransPrice;
    }

    public void setSelectedTransPrice(InvsTransferPrice selectedTransPrice) {
        this.selectedTransPrice = selectedTransPrice;
    }


    public List<ReportSummaryHistory> getTransPriceHistories() {
        return transPriceHistories;
    }

    public void setTransPriceHistories(List<ReportSummaryHistory> transPriceHistories) {
        this.transPriceHistories = transPriceHistories;
    }

    public List<TransferPriceVo> getTransPriceVos() {
        return transPriceVos;
    }

    public void setTransPriceVos(List<TransferPriceVo> transPriceVos) {
        this.transPriceVos = transPriceVos;
    }

    public boolean isSelectedAll() {
        return selectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        this.selectedAll = selectedAll;
    }

    public List<InvsTransferPriceHistory> getPriceHistories() {
        return priceHistories;
    }

    public void setPriceHistories(List<InvsTransferPriceHistory> priceHistories) {
        this.priceHistories = priceHistories;
    }

    public String getOperateInd() {
        return operateInd;
    }

    public void setOperateInd(String operateInd) {
        this.operateInd = operateInd;
    }

    public TransferPriceVo getTransferPriceVo() {
        return transferPriceVo;
    }

    public void setTransferPriceVo(TransferPriceVo transferPriceVo) {
        this.transferPriceVo = transferPriceVo;
    }

    public List<TransTypeVo> getTransTypeVos() {
        return transTypeVos;
    }

    public void setTransTypeVos(List<TransTypeVo> transTypeVos) {
        this.transTypeVos = transTypeVos;
    }

    public InvsTransferPriceHistory getSelectedHistory() {
        return selectedHistory;
    }

    public void setSelectedHistory(InvsTransferPriceHistory selectedHistory) {
        this.selectedHistory = selectedHistory;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public boolean isCheckAll() {
        return checkAll;
    }

    public void setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
    }

    public TransferPriceVo[] getSelectedTransPriceVos() {
        return selectedTransPriceVos;
    }

    public void setSelectedTransPriceVos(TransferPriceVo[] selectedTransPriceVos) {
        this.selectedTransPriceVos = selectedTransPriceVos;
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
