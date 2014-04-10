package com.wcs.tih.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;

import com.wcs.common.model.Companymstr;
import com.wcs.common.util.PageModel;
import com.wcs.tih.model.Taxauthority;
import com.wcs.tih.model.TaxauthorityCompanymstr;
import com.wcs.tih.system.service.CompanyTaxauthorityService;

@ManagedBean
@ViewScoped
public class CompanyTaxauthorityBean {
    @EJB 
    private CompanyTaxauthorityService companyTaxauthorityService;
    private List companyList = new ArrayList();
    private String insertName;
    private String taxName;
    private Taxauthority  tax;
    
    
    
    private List showList =new ArrayList();
    
    public List getShowList() {
        return showList;
    }
  
    private Companymstr company;
    public void init(long id){
        this.taxName="";
        this.insertName="";
        tax=null;
        this.company=this.companyTaxauthorityService.getCompanyById(id);
        this.tax= null;
        search();
    }
    public void search(){
        this.companyList=this.companyTaxauthorityService.search(company);
        this.showList=new ArrayList();
        showList.addAll(companyList);
    }
    public void searchTax(){
        this.t=new PageModel<Taxauthority>(this.companyTaxauthorityService.searchTaxauthority(this.taxName), false);
    }
   
    public void changeInsert(){
      boolean fflag=true;
        if(this.tax==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税务机关：", "请先选择"));
            fflag=false;
        }
        if(this.insertName==null||this.insertName.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "纳税人识别号：", "不能为空"));
            fflag=false;
        }
        if(this.tax != null && this.tax.getType() == null){
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税务类型：", "不能为空"));
        	fflag=false;
        }
        
        for(int i=0;i<this.showList.size();i++){
            TaxauthorityCompanymstr t=(TaxauthorityCompanymstr)showList.get(i);
            if(t.getTaxauthority().getType().equals(this.tax.getType())){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税务机关：", "最多有一家国税和一家地税"));
                fflag=false;
            }
        }
        if(!fflag){
            return;
        }
        TaxauthorityCompanymstr tax= new TaxauthorityCompanymstr();
        tax.setCompanymstr(this.company);
        tax.setTaxauthority(this.tax);
        tax.setTaxpayerIdentifier(this.insertName);
        this.tax= null;
        this.insertName="";
        companyTaxauthorityService.saveTaxauthorityCompanymstr(tax);
        search();
    }

    public void resetTax(){
        this.taxName="";
    }

    public void clearBefore(){
        this.t=new PageModel<Taxauthority>(new ArrayList(), false);
        this.taxName="";
    }
    
    public void delete(long taxid){
    	companyTaxauthorityService.deleteById(taxid, this.company.getId());
    	search();
    }

    public String getInsertName() {
        return insertName;
    }
    public void setInsertName(String insertName) {
        this.insertName = insertName;
    }
    public Taxauthority getTax() {
        return tax;
    }
    public void setTax(Taxauthority tax) {
        this.tax = tax;
    }
    public void setShowList(List showList) {
        this.showList = showList;
    }
    private LazyDataModel<Taxauthority> t;
    
    public LazyDataModel<Taxauthority> getT() {
        return t;
    }
    public void setT(LazyDataModel<Taxauthority> t) {
        this.t = t;
    }
    public String getTaxName() {
        return taxName;
    }
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }
}
