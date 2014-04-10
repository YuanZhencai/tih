package com.wcs.tih.system.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.model.CompanyEstate;
import com.wcs.tih.system.controller.vo.CompanyEstateVO;
import com.wcs.tih.system.service.CompanyEstateService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: 房产明细 Backing Bean.
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class CompanyEstateBean implements Serializable {

    private static final String TOW_DECIMAL_MSG = "填写格式只能为整数或保留2位小数";

    private static final String TOW_DECIMAL = "^[0-9]+([.][0-9]{1,2})?$";

    private static final long serialVersionUID = 1L;

    @EJB private CompanyEstateService estateService;
    private Long companyId;
    private Map<String, Object> query;
    
    private LazyDataModel<CompanyEstateVO> lazyEstates;
    private CompanyEstateVO currentEstate;
    
    @PostConstruct public void initBean() {
        setQuery(new HashMap<String, Object>(4));
        initAddEstate();
    }

    public void search() {
        lazyEstates = new PageModel<CompanyEstateVO>(estateService.search(companyId, query), false);
    }
    public void clearEstateInfo() {
        query.clear();
        query.put("defunct", "N");
        lazyEstates = null;
        search();
    }
    /**
     * <p>Description: 新增房产信息初始化</p>
     */
    public void initAddEstate() {
        CompanyEstate est = new CompanyEstate();
        est.setDefunctInd("N");
        currentEstate = new CompanyEstateVO(est);
    }
    /**
     * <p>Description: 添加、编辑房产信息</p>
     */
    public void saveEstate() {
        FacesContext context = FacesContext.getCurrentInstance();
        CompanyEstate est = currentEstate.getEstate();
        
        if(!(ValidateUtil.validateRequiredAndMax(context, est.getEstateNo(), "房产证编号：", 20)
                & ValidateUtil.validateRequiredAndMax(context, est.getName(), "房产名称：", 30)
                & ValidateUtil.validateRegex(context, est.getArea(), "建筑面积：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRequired(context, est.getType(), "房产类型：")
                & ValidateUtil.validateMaxlength(context, est.getLandNo(), "所属土地证编号：", 20)
                & ValidateUtil.validateRegex(context, est.getLandVolumnRate(), "宗地容积率：", "^[0-9]+([.][0-9]{1,4})?$", "填写格式只能为整数或保留4位小数")
                & ValidateUtil.validateRegex(context, est.getLandUnitCost(), "宗地单位成本：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRegex(context, est.getLandCost(), "土地原值：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRequiredAndRegex(context, est.getEstateAccountCost(), "房产账面原值：", "^-?[0-9]+([.][0-9]{1,2})?$", TOW_DECIMAL_MSG)
                & ValidateUtil.validateRegex(context, est.getCalTaxLandCost(), "计入房产税纳税的土地价值：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRegex(context, est.getCalTaxEstateCost(), "房产税计税原值：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRegex(context, est.getDeductionRate(), "扣除比例：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRegex(context, est.getTaxRate(), "税率：", TOW_DECIMAL, TOW_DECIMAL_MSG)
                & ValidateUtil.validateRegex(context, est.getTaxAccount(), "税额：", TOW_DECIMAL, TOW_DECIMAL_MSG))){
        	return ;
        }
        
        try {
            estateService.saveEstate(companyId, est);
            RequestContext.getCurrentInstance().addCallbackParam("option", "success");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "维护房产信息：", "操作成功。"));
        } catch (Exception e) {
            context.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "维护房产信息：", e.getMessage()));
        }
        this.search();
    }
    
    // Getter & Setter
    public LazyDataModel<CompanyEstateVO> getLazyEstates() {
        return lazyEstates;
    }

    public void setLazyEstates(LazyDataModel<CompanyEstateVO> lazyEstates) {
        this.lazyEstates = lazyEstates;
    }

    public CompanyEstateVO getCurrentEstate() {
        return currentEstate;
    }

    public void setCurrentEstate(CompanyEstateVO currentEstate) {
        this.currentEstate = currentEstate;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
