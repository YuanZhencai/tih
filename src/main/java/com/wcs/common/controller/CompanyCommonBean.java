package com.wcs.common.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.AreaHelper;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.service.CompanyCommonService;
import com.wcs.common.util.PageModel;
import com.wcs.tih.document.service.DocumentCheckinService;
/**
 * Project: tih Description: 公司公用dialog
 * Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 * @see DocumentCheckinService
 */
@ManagedBean
@ViewScoped
public class CompanyCommonBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @EJB
    private CompanyCommonService companyCommonService;

    private String type;

    private CompanyManagerModel selectCompany;
    private CompanyManagerModel[] selectCompanys;
    private CompanyManagerModel companyManagerModel = new CompanyManagerModel();
    private LazyDataModel<CompanyManagerModel> lazyCompanymstr;
    private String update = ":commonCompany";
    private String controllerName;
    private String methodName;
    private List<CompanyManagerModel> companys;
    private List<SelectItem> regionItems;
    private List<SelectItem> provinceItems;
    private String isMyCompany = "";

    @PostConstruct
    public void init(){
    	this.selectCompany = null;
        this.selectCompanys = null;
        this.lazyCompanymstr = new PageModel<CompanyManagerModel>(new ArrayList(), false);
        this.type = "single";
        regionItems = AreaHelper.getItemsByCatKey("0");
        provinceItems = new ArrayList<SelectItem>();
        companys= new ArrayList<CompanyManagerModel>();
        isMyCompany = "";
    }
    
    public void init(String update, String controllerName, String type,String methodName) {
        this.selectCompany = null;
        this.selectCompanys = null;
        this.lazyCompanymstr = new PageModel<CompanyManagerModel>(new ArrayList(), false);
        this.update = update;
        this.controllerName = controllerName;
        this.type = type;
        this.methodName=methodName;
        this.companyManagerModel= new CompanyManagerModel();
        companys= new ArrayList<CompanyManagerModel>();
    }

    public void init(String update, String controllerName, String type,String methodName,String isMyCompany) {
        this.selectCompany = null;
        this.selectCompanys = null;
        this.lazyCompanymstr = new PageModel<CompanyManagerModel>(new ArrayList(), false);
        this.update = update;
        this.controllerName = controllerName;
        this.type = type;
        this.methodName=methodName;
        this.companyManagerModel= new CompanyManagerModel();
        companys= new ArrayList<CompanyManagerModel>();
        this.isMyCompany = isMyCompany;
    }
    
    private void invokeMethodByMultiple() {
        if (this.controllerName != null && !"".equals(this.controllerName)) {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.controllerName);
                o.getClass().getDeclaredMethod(methodName, CompanyManagerModel[].class).invoke(o, new Object[] { selectCompanys });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void invokeMethodBySingle() {
        if (this.controllerName != null && !"".equals(this.controllerName)) {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.controllerName);
                o.getClass().getDeclaredMethod(methodName, CompanyManagerModel.class).invoke(o, new Object[] { selectCompany });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void invoke() {
        if (this.type.equals("single")) {
            this.invokeMethodBySingle();
        } else {
            this.invokeMethodByMultiple();
        }
    }

    public void search() {
    	this.companyManagerModel.setDefuctInt("N");
    	if("true".equals(isMyCompany)){
    	    companys = this.companyCommonService.searchMyCompany(this.companyManagerModel);
    	}else{
    	    companys = this.companyCommonService.search(this.companyManagerModel);
    	}
        this.lazyCompanymstr = new PageModel<CompanyManagerModel>(companys, false);
    }

    public void reset() {
        companyManagerModel = new CompanyManagerModel();
        provinceItems = new ArrayList<SelectItem>();
    }
    
    public void handleRegionChange(String catKey){
    	provinceItems = AreaHelper.getItemsByCatKey(catKey);
    }
    
    public LazyDataModel<CompanyManagerModel> getLazyCompanymstr() {
        return lazyCompanymstr;
    }

    public void setLazyCompanymstr(LazyDataModel<CompanyManagerModel> lazyCompanymstr) {
        this.lazyCompanymstr = lazyCompanymstr;
    }

    public String getUpdate() {
        return update;
    }

    public String getType() {
        return type;
    }

    public CompanyManagerModel getSelectCompany() {
        return selectCompany;
    }

    public void setSelectCompany(CompanyManagerModel selectCompany) {
        this.selectCompany = selectCompany;
    }

    public CompanyManagerModel[] getSelectCompanys() {
        return selectCompanys;
    }

    public void setSelectCompanys(CompanyManagerModel[] selectCompanys) {
        this.selectCompanys = selectCompanys;
    }

    public CompanyManagerModel getCompanyManagerModel() {
        return companyManagerModel;
    }

    public void setCompanyManagerModel(CompanyManagerModel companyManagerModel) {
        this.companyManagerModel = companyManagerModel;
    }

	public List<CompanyManagerModel> getCompanys() {
		return companys;
	}

	public void setCompanys(List<CompanyManagerModel> companys) {
		this.companys = companys;
	}

	public List<SelectItem> getRegionItems() {
		return regionItems;
	}

	public void setRegionItems(List<SelectItem> regionItems) {
		this.regionItems = regionItems;
	}

	public List<SelectItem> getProvinceItems() {
		return provinceItems;
	}

	public void setProvinceItems(List<SelectItem> provinceItems) {
		this.provinceItems = provinceItems;
	}
}
