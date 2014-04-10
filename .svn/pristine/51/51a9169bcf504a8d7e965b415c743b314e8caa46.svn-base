package com.wcs.tih.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.consts.ReportConsts;
import com.wcs.tih.system.controller.vo.CommonTempVo;
import com.wcs.tih.system.controller.vo.TaxIncentiveVo;
import com.wcs.tih.system.service.TaxIncentiveService;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean(name = "taxIncentiveBean")
@ViewScoped
public class TaxIncentiveBean {
    private static final String ISSUCC = "issucc";
    @EJB
    private TaxIncentiveService taxIncentiveService;
    private TaxIncentiveVo taxIncentiveVo;
    private LazyDataModel<TaxIncentiveVo> lazyTaxIncentiveVoModel;
    private LazyDataModel<CommonTempVo> lazyPreferentialItemModel;
    private static List<CommonTempVo> preferentialItemList;
    private CommonTempVo ctv;
    private String existTaxType;
    private String existPreferentialItem;
    private String taxType;
    private String effective;
    private String excuteMethod;
    private long companymstrId;

    static {
        preferentialItemList = new ArrayList<CommonTempVo>();
        preferentialItemList.add(new CommonTempVo((long) 1, ReportConsts.PREFERENTIAL_ITEM_1));
        preferentialItemList.add(new CommonTempVo((long) 2, ReportConsts.PREFERENTIAL_ITEM_2));
        preferentialItemList.add(new CommonTempVo((long) 3, ReportConsts.PREFERENTIAL_ITEM_3));
        preferentialItemList.add(new CommonTempVo((long) 4, ReportConsts.PREFERENTIAL_ITEM_4));
        preferentialItemList.add(new CommonTempVo((long) 5, ReportConsts.PREFERENTIAL_ITEM_5));
        preferentialItemList.add(new CommonTempVo((long) 6, ReportConsts.PREFERENTIAL_ITEM_6));
        preferentialItemList.add(new CommonTempVo((long) 7, ReportConsts.PREFERENTIAL_ITEM_7));
        preferentialItemList.add(new CommonTempVo((long) 8, ReportConsts.PREFERENTIAL_ITEM_8));
        preferentialItemList.add(new CommonTempVo((long) 9, ReportConsts.PREFERENTIAL_ITEM_9));
        preferentialItemList.add(new CommonTempVo((long) 10, ReportConsts.PREFERENTIAL_ITEM_10));
        preferentialItemList.add(new CommonTempVo((long) 11, ReportConsts.PREFERENTIAL_ITEM_11));
    }

    /**
     * <p>
     * Description: 初始化
     * </p>
     */
    @PostConstruct
    public void init() {
        taxIncentiveVo = new TaxIncentiveVo();
        lazyPreferentialItemModel = new PageModel<CommonTempVo>(preferentialItemList, false);
    }

    /**
     * <p>
     * Description: 初始化
     * </p>
     */
    public void initTaxIncentive() {
        taxType = "";
        effective = "N";
        lazyTaxIncentiveVoModel = null;
        this.queryTaxIncentive();
    }

    public void select(SelectEvent se) {
        ctv = (CommonTempVo) se.getObject();
    }

    /**
     * <p>
     * Description: 查询税收优惠信息
     * </p>
     */
    public void queryTaxIncentive() {
        List<TaxIncentiveVo> tivs = taxIncentiveService.queryTaxIncentive(this.companymstrId, taxType, effective);
        if (null != tivs && tivs.size() != 0) {
            lazyTaxIncentiveVoModel = new PageModel<TaxIncentiveVo>(tivs, false);
        } else {
            lazyTaxIncentiveVoModel = null;
        }
    }

    /**
     * <p>
     * Description: 增加税收优惠信息
     * </p>
     */
    public void addTaxIncentive() {
        taxIncentiveVo = new TaxIncentiveVo();
        taxIncentiveVo.setEffective("N");
    }

    public void selectPreferentialItem() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
        if (null == ctv || null == ctv.getName() || "".equals(ctv.getName())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "请先选择一个项目"));
            return;
        }
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
        taxIncentiveVo.setPreferentialItem(ctv.getName());
    }

    /**
     * <p>
     * Description: 保存税收优惠信息
     * </p>
     */
    public void saveTaxIncentive() {
        Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
        if (!(ValidateUtil.validateRequired(context, taxIncentiveVo.getTaxType(), "税种：") & ValidateUtil.validateRequiredAndMax(context, taxIncentiveVo.getPreferentialItem(), "优惠项目：", 100) & ValidateUtil.validateRequired(context, taxIncentiveVo.getSituationStatus(), "审批或备案状态：") & ValidateUtil.validateRequired(context, taxIncentiveVo.getPreferentialStartDatetime(), "优惠开始时间：") & ValidateUtil.validateRequired(context, taxIncentiveVo.getPreferentialEndDatetime(), "优惠结束时间：") & ValidateUtil.validateRequiredAndMax(context, taxIncentiveVo.getPolicy(), "政策依据：", 1000) & ValidateUtil.validateRequiredAndMax(context, taxIncentiveVo.getApprovalOrgan(), "审批机构：", 200))){
        	return;
        }
        boolean b = true;
        if (taxIncentiveService.isExistTaxIncentive(companymstrId, taxIncentiveVo)) {
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税种、优惠项目、优惠时间有重复数据", ""));
        	b = false;
        }
        if (taxIncentiveVo.getPreferentialStartDatetime().getTime() > taxIncentiveVo.getPreferentialEndDatetime().getTime()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "优惠期间前一个日期不能大于后一个日期", ""));
            b = false;
        }
        if (!b){
        	return;
        }
        if ("add".equals(excuteMethod)) {
            try {
                taxIncentiveService.saveTaxIncentiveVo(browserLang.toString(), companymstrId, taxIncentiveVo);
                RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "保存税种：" + taxIncentiveService.getValueByDictCatKey(taxIncentiveVo.getTaxType(), browserLang.toString()) + "，优惠项目：" + taxIncentiveVo.getPreferentialItem() + "的税收优惠信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        } else {
            try {
                taxIncentiveService.updateTaxIncentiveVo(browserLang.toString(), taxIncentiveVo);
                RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "编辑税种：" + taxIncentiveService.getValueByDictCatKey(taxIncentiveVo.getTaxType(), browserLang.toString()) + "，优惠项目：" + taxIncentiveVo.getPreferentialItem() + "的税收优惠信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        }
        queryTaxIncentive();
    }

    public TaxIncentiveVo getTaxIncentiveVo() {
        return taxIncentiveVo;
    }

    public void setTaxIncentiveVo(TaxIncentiveVo taxIncentiveVo) {
        this.taxIncentiveVo = taxIncentiveVo;
    }

    public LazyDataModel<TaxIncentiveVo> getLazyTaxIncentiveVoModel() {
        return lazyTaxIncentiveVoModel;
    }

    public void setLazyTaxIncentiveVoModel(LazyDataModel<TaxIncentiveVo> lazyTaxIncentiveVoModel) {
        this.lazyTaxIncentiveVoModel = lazyTaxIncentiveVoModel;
    }

    public String getExistTaxType() {
        return existTaxType;
    }

    public void setExistTaxType(String existTaxType) {
        this.existTaxType = existTaxType;
    }

    public String getExistPreferentialItem() {
        return existPreferentialItem;
    }

    public void setExistPreferentialItem(String existPreferentialItem) {
        this.existPreferentialItem = existPreferentialItem;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getExcuteMethod() {
        return excuteMethod;
    }

    public void setExcuteMethod(String excuteMethod) {
        this.excuteMethod = excuteMethod;
    }

    public long getCompanymstrId() {
        return companymstrId;
    }

    public void setCompanymstrId(long companymstrId) {
        this.companymstrId = companymstrId;
    }

    public LazyDataModel<CommonTempVo> getLazyPreferentialItemModel() {
        return lazyPreferentialItemModel;
    }

}
