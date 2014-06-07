package com.wcs.common.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.wcs.common.controller.vo.CompanyManagerModel;

@Stateless
public class CompanyCommonService {
    
    @EJB 
    private CompanyService companyservice;
    public List<CompanyManagerModel> search(CompanyManagerModel queryCondition){
     return this.companyservice.getCompanyManagerModel(queryCondition, "");
    }
    
    public List<CompanyManagerModel> searchMyCompany(CompanyManagerModel queryCondition){
        return this.companyservice.findMySpecialManageCompanys(queryCondition,false);
    }
}
