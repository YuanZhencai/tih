package com.wcs.tih.system.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.Taxauthority;
import com.wcs.tih.model.TaxauthorityCompanymstr;

@Stateless
public class CompanyTaxauthorityService {
    @EJB
    private LoginService loginservice;
    @PersistenceContext
    private EntityManager em;
    @EJB 
    private CompanyInvestmentService companyInvestmentService;
    public List<TaxauthorityCompanymstr> search(Companymstr company){
        StringBuilder sb = new StringBuilder();
        sb.append("select s from TaxauthorityCompanymstr s where s.companymstr.id=").append(company.getId()).append(" and s.defunctInd='N'");
        return this.em.createQuery(sb.toString()).getResultList();
    }
    public Companymstr getCompanyById(long id){
        return this.companyInvestmentService.getCompanyById(id);
    }
    public List searchTaxauthority(String name){
        String sql ="select t from Taxauthority t  where t.defunctInd='N'";
        if(name!=null && !name.trim().equals("")){
        	sql+= " and t.name like '%"+name+"%'";
        }
        return this.em.createQuery(sql).getResultList();
    }
    public void deleteById(Long taxid, long companyId){
        this.em.createNativeQuery("delete from TAXAUTHORITY_COMPANYMSTR where TAXAUTHORITY_ID ="+taxid +" and COMPANYMSTR_ID = "+companyId).executeUpdate();
    }
    
    public void saveTaxauthorityCompanymstr(TaxauthorityCompanymstr tax){
    	tax.setCreatedBy(loginservice.getCurrentUsermstr().getAdAccount());
        tax.setUpdatedBy(loginservice.getCurrentUsermstr().getAdAccount());
        tax.setCreatedDatetime(new Date());
        tax.setUpdatedDatetime(new Date());
        tax.setDefunctInd("N");
        this.em.persist(tax);
    }
    
    public void delete(List<TaxauthorityCompanymstr> deleteTaxauthorityLists){
    	for(TaxauthorityCompanymstr tac : deleteTaxauthorityLists){
    		deleteById(tac.getTaxauthority().getId(), tac.getCompanymstr().getId());
    	}
    }

}
