package com.wcs.tih.transaction.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.tih.model.InvsTransferPrice;
import com.wcs.tih.model.InvsTransferPriceHistory;
import com.wcs.tih.model.InvsVerifyTransType;
import com.wcs.tih.model.InvsVerifyTransTypeHistory;
import com.wcs.tih.transaction.controller.vo.TransTypeVo;
import com.wcs.tih.transaction.controller.vo.TransferPriceVo;
import com.wcs.tih.transaction.service.TransferPriceService;
import com.wcs.tih.util.ArithUtil;
import com.wcs.tih.util.ValidateUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@ManagedBean(name = "transferPriceBean")
@ViewScoped
public class TransferPriceBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String TWODECIMAL_REGX = "(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$";

    @EJB
    private TransferPriceService transferPriceService;
    
    private TransferPriceVo transferPriceVo;
    private List<InvsTransferPrice> transferPrices;
    private InvsTransferPrice selectedTransPrice;
    private TransferPriceVo searchTransPrice;
    private List<TransTypeVo> transTypeVos;
    private TransTypeVo selectedTransTypeVo;
    private List<InvsVerifyTransType> transTypes;
    private List<InvsTransferPriceHistory> priceHistories;
    private InvsTransferPriceHistory selectedHistory;
    private String operateInd = "";
    private CompanyManagerModel company;
    private boolean disabled = true;
    private boolean required = false;
    private int transPriceId = 0;
    private String operate = "";
    private List<SelectItem> submitDocItems;
    private boolean transAdmin = false;
    private boolean addButton = false;
    private List<TransferPriceVo> transPriceVos;
    
    public TransferPriceBean() {
        transferPriceVo = new TransferPriceVo();
        transferPrices = new ArrayList<InvsTransferPrice>();
        transTypes = new ArrayList<InvsVerifyTransType>();
        transTypeVos = new ArrayList<TransTypeVo>();
        priceHistories = new ArrayList<InvsTransferPriceHistory>();
        company = new CompanyManagerModel();
        searchTransPrice = new TransferPriceVo();
        submitDocItems = new ArrayList<SelectItem>();
        selectedTransTypeVo = new TransTypeVo();
        transPriceVos = new ArrayList<TransferPriceVo>();
    }
    
    @PostConstruct
    public void init(){
        searchAllTransferPrices();
        isShowAddButton();
    }
    public void isShowAddButton(){
        try {
            List<Long> adminCompanys = transferPriceService.findTransAdminCompanys();
            if(adminCompanys!=null && adminCompanys.size()>0){
                addButton = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public List<InvsTransferPrice> searchAllTransferPrices(){
        List<InvsTransferPrice> transferPrices = new ArrayList<InvsTransferPrice>();
        try {
            searchTransPrice = new TransferPriceVo();
            searchTransPrice.setDefunctInd("N");
            transPriceVos = transferPriceService.findTransPricesBy(searchTransPrice, true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return transferPrices;
    }
    
    public void searchTransPricesBy(){
        try {
            transPriceVos = transferPriceService.findTransPricesBy(searchTransPrice,true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void clear(){
        operate = "";
        operateInd = "";
        disabled = true;
        transAdmin = false;
        selectedTransPrice = new InvsTransferPrice();
        transferPriceVo = new TransferPriceVo();
        transTypeVos = new ArrayList<TransTypeVo>();
        company = new CompanyManagerModel();
        priceHistories = new ArrayList<InvsTransferPriceHistory>();
    }
    
    public void initAddTransPrice(){
        disabled = false;
    }

    public void addTransferPrice(){
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            //..验证transferPriceVo, transTypeVos
            transferPriceVo.setCompanymstrId(company.getId()==null?0:company.getId());
            transferPriceVo.setCompanyName(company.getStext());
            if (ValidateUtil.validateRequired(context, transferPriceVo.getCompanyName(), "公司名称：不能为空")
                    & ValidateUtil.validateRequired(context, transferPriceVo.getDecade(), "年度：不能为空")
                    & ValidateUtil.validateRequiredAndRegex(context, transferPriceVo.getAssoDebtEquityRatio(), "关联债资比：不能为空，且小数点后两位",
                            TWODECIMAL_REGX, "")
                    & ValidateUtil.validateRequired(context, transferPriceVo.getPrepareDocInd(), "是否准备同期资料：不能为空")
                    & ValidateUtil.validateRequiredAndMax(context, transferPriceVo.getRemarks(), "备注：不能为空，且长度为50", 50)) {
                    if("Y".equals(transferPriceVo.getPrepareDocInd())){
                        if(ValidateUtil.validateRequired(context, transferPriceVo.getSubmitDocInd(), "同期资料是否提交税务局：不能为空")){
                            if("Y".equals(transferPriceVo.getSubmitDocInd())){
                                if(!ValidateUtil.validateRequired(context, transferPriceVo.getDocSubmitDatetime(), "同期资料提交税务局日期：不能为空")){
                                    return;
                                }
                            }
                        }else{
                            return;
                        }
                    }
            }else{
                return;
            }
            if(transTypeVos==null||transTypeVos.size() == 0){
                showMessage(FacesMessage.SEVERITY_WARN, "请选择明细信息");
                return;
            }
            TransferPriceVo savedPriceVo = transferPriceService.saveTransferPrice(transferPriceVo, transTypeVos);
            disabled = true;
            if(!savedPriceVo.isSame()){
                selectedTransPrice = savedPriceVo.getTransferPrice();
                searchAllTransferPrices();
                handleHistoryChange();
                transAdmin = judgeTransAdmin(selectedTransPrice.getCompanymstrId());
                String msg = "操作成功";
                if("add".equals(operate)){
                    msg = "创建成功";
                }else if("update".equals(operate)){
                    msg = "更新成功";
                }
                showMessage(FacesMessage.SEVERITY_INFO, msg);
            }
        } catch (Exception e) {
            showMessage(FacesMessage.SEVERITY_ERROR, "操作失败");
            logger.error(e.getMessage(), e);
        }
    }
    
    public void getTransPriceDetail(){
        company = new CompanyManagerModel();
        transferPriceVo = new TransferPriceVo();
        transferPriceVo.setId(selectedTransPrice.getId());
        transferPriceVo.setTransferPrice(selectedTransPrice);
        company.setId(selectedTransPrice.getCompanymstrId());
        company.setStext(selectedTransPrice.getCompanyName());
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
            typeVo.setId(type.getId());
            typeVo.setVerifyTransType(type);
            typeVo.setTransType(type.getTransType());
            typeVo.setValidationMethod(type.getValidationMethod());
            typeVo.setCompareCompanyMedian(ArithUtil.mul(type.getCompareCompanyMedian(), 100));
            typeVo.setBeforeAdjustRatio(ArithUtil.mul(type.getBeforeAdjustRatio(), 100));
            typeVo.setAfterAdjustRatio(ArithUtil.mul(type.getAfterAdjustRatio(), 100));
            typeVo.setAdjustSpecialReason(type.getAdjustSpecialReason());
            transTypeVos.add(typeVo);
        }
        transAdmin = judgeTransAdmin(selectedTransPrice.getCompanymstrId());
    }
    
    public void getHistoryDetail(){
        disabled = true;
        String operate = selectedHistory.getOperateInd();
        if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(operate)||DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(operate)){
            getTransPriceDetail();
        }else{
            company = new CompanyManagerModel();
            transferPriceVo = new TransferPriceVo();
            transferPriceVo.setId(selectedHistory.getId());
            transferPriceVo.setTransferPrice(selectedTransPrice);
            company.setId(selectedHistory.getCompanymstrId());
            company.setStext(selectedHistory.getCompanyName());
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
                typeVo.setId(type.getId());
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
    
    public void editTransPrice(){
        disabled = false;
    }
    
    public void deleteTransPrice(){
        try {
            InvsTransferPrice tp = transferPriceService.findTransferPrice(transPriceId);
            if("Y".equals(tp.getDefunctInd())){
                showMessage(FacesMessage.SEVERITY_ERROR, "删除失败：此信息已无效");
                return;
            }
            boolean isTransAdmin = judgeTransAdmin(tp.getCompanymstrId());
            if(!isTransAdmin){
                showMessage(FacesMessage.SEVERITY_ERROR, "删除失败：您不是该家公司的专项管理员");
                return;
            }
            selectedTransPrice = transferPriceService.deleteTransPrice(tp);
            priceHistories = transferPriceService.searchPriceHistories(selectedTransPrice);
            searchAllTransferPrices();
            disabled = true;
            showMessage(FacesMessage.SEVERITY_INFO, "删除成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            showMessage(FacesMessage.SEVERITY_ERROR, "删除失败");
            logger.error(e.getMessage(), e);
        }
    }
    
    public void addTransType(){
        List<TransTypeVo> typeVos = new ArrayList<TransTypeVo>();
        TransTypeVo type = new TransTypeVo();
        typeVos.add(type);
        typeVos.addAll(transTypeVos);
        transTypeVos = typeVos;
    }
    
    public void saveTransType(RowEditEvent event) throws Exception{
    }
    
    public void deleteTransType(){
        transTypeVos.remove(selectedTransTypeVo);
    }
    
    public void handleHistoryChange(){
        transferPriceVo = new TransferPriceVo();
        transTypeVos = new ArrayList<TransTypeVo>();
        company = new CompanyManagerModel();
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
        }
    }
    public void handleDocChange(String docInd){
        submitDocItems = new ArrayList<SelectItem>();
        if("Y".equals(docInd)){
            SelectItem item1 = new SelectItem();
            item1.setLabel("是");
            item1.setValue("Y");
            SelectItem item2 = new SelectItem();
            item2.setLabel("否");
            item2.setValue("N");
            submitDocItems.add(item1);
            submitDocItems.add(item2);
        }
    }
    
    public boolean judgeTransAdmin(Long companyId){
        return transferPriceService.isTransAdmin(companyId);
    }
    
    public boolean judgeTransAdminAttachment(long companyId){
    	return transferPriceService.isTransAdmin(companyId);
    }
    
    
    public void handleDialogByWidgetVar(String widgetVar,String option){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("widgetVar", widgetVar);
        context.addCallbackParam("option", option);
    }
    
    public void showMessage(Severity severityType,String message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityType, message , ""));
    }
    
    public long random() {
        Random r = new Random(System.nanoTime());
        return r.nextLong();
    }
    
    public String getUserName(String str) {
        return transferPriceService.getUserName(str);
    }
    
    public void resetSearchForm(){
        searchTransPrice = new TransferPriceVo();
        searchTransPrice.setDefunctInd("N");
    }
    //---------------------------------------- GET && SET --------------------------------------//
    

    public List<InvsTransferPrice> getTransferPrices() {
        return transferPrices;
    }

    public void setTransferPrices(List<InvsTransferPrice> transferPrices) {
        this.transferPrices = transferPrices;
    }

    public List<InvsVerifyTransType> getTransTypes() {
        return transTypes;
    }

    public void setTransTypes(List<InvsVerifyTransType> transTypes) {
        this.transTypes = transTypes;
    }

    public List<TransTypeVo> getTransTypeVos() {
        return transTypeVos;
    }

    public void setTransTypeVos(List<TransTypeVo> transTypeVos) {
        this.transTypeVos = transTypeVos;
    }

    public TransferPriceVo getTransferPriceVo() {
        return transferPriceVo;
    }

    public void setTransferPriceVo(TransferPriceVo transferPriceVo) {
        this.transferPriceVo = transferPriceVo;
    }

    public CompanyManagerModel getCompany() {
        return company;
    }

    public void setCompany(CompanyManagerModel company) {
        this.company = company;
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

    public InvsTransferPrice getSelectedTransPrice() {
        return selectedTransPrice;
    }

    public void setSelectedTransPrice(InvsTransferPrice selectedTransPrice) {
        this.selectedTransPrice = selectedTransPrice;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public TransTypeVo getSelectedTransTypeVo() {
        return selectedTransTypeVo;
    }

    public void setSelectedTransTypeVo(TransTypeVo selectedTransTypeVo) {
        this.selectedTransTypeVo = selectedTransTypeVo;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getTransPriceId() {
        return transPriceId;
    }

    public void setTransPriceId(int transPriceId) {
        this.transPriceId = transPriceId;
    }

    public TransferPriceVo getSearchTransPrice() {
        return searchTransPrice;
    }

    public void setSearchTransPrice(TransferPriceVo searchTransPrice) {
        this.searchTransPrice = searchTransPrice;
    }

    public InvsTransferPriceHistory getSelectedHistory() {
        return selectedHistory;
    }

    public void setSelectedHistory(InvsTransferPriceHistory selectedHistory) {
        this.selectedHistory = selectedHistory;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public List<SelectItem> getSubmitDocItems() {
        return submitDocItems;
    }

    public void setSubmitDocItems(List<SelectItem> submitDocItems) {
        this.submitDocItems = submitDocItems;
    }

    public boolean isTransAdmin() {
        return transAdmin;
    }

    public void setTransAdmin(boolean transAdmin) {
        this.transAdmin = transAdmin;
    }

    public boolean isAddButton() {
        return addButton;
    }

    public void setAddButton(boolean addButton) {
        this.addButton = addButton;
    }

    public List<TransferPriceVo> getTransPriceVos() {
        return transPriceVos;
    }

    public void setTransPriceVos(List<TransferPriceVo> transPriceVos) {
        this.transPriceVos = transPriceVos;
    }
    
}
