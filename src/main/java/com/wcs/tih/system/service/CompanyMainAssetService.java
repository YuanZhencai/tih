package com.wcs.tih.system.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyDepreciation;
import com.wcs.tih.model.CompanyMainAsset;
import com.wcs.tih.system.controller.vo.CompanyDepreciationVO;
import com.wcs.tih.system.controller.vo.CompanyMainAssetVO;

/**
 * Project: tih
 * Description: 公司主要资产Service
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class CompanyMainAssetService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB private LoginService loginService;
    
    @PersistenceContext private EntityManager em;
    /**
     * <p>Description:  查询资产信息</p>
     * @param query     查询条件
     * @return          返回List<CompanyMainAssetVO>集合
     */
    public List<CompanyMainAssetVO> search(Map<String, String> query, Long companyId) {
        StringBuilder sql = new StringBuilder("SELECT NEW com.wcs.tih.system.controller.vo.CompanyMainAssetVO(c) " +
        		"FROM CompanyMainAsset c WHERE c.companymstr.id=:id ");
        String item = query.get("item");
        String defunct = query.get("defunct");
        if(item != null && !"".equals(item)) {
            sql.append("AND c.item = '" + item + "' ");
        }
        if(defunct != null && !"".equals(defunct)) {
            sql.append("AND c.defunctInd = '" + defunct + "' ");
        }
        sql.append("ORDER BY c.id");
        
        Query q = em.createQuery(sql.toString()).setParameter("id", companyId);
        return q.getResultList();
    }
    /**
     * <p>Description: 保存公司资产信息</p>
     * @param companyId 
     * @param currentAsset
     * @param depreList
     * @throws Exception 
     */
    public void saveAssert(Long companyId, CompanyMainAssetVO currentAsset, List<CompanyDepreciationVO> depreList) throws Exception {
    	CompanyMainAsset cm = currentAsset.getAsset();
    	  String sql = "SELECT COUNT(1) FROM CompanyMainAsset c WHERE c.defunctInd = 'N' and c.companymstr.id=:companyId AND c.item=:item";
          if("N".equals(cm.getDefunctInd())){
        	  if(cm.getId() == null) {    //new
        		  List ls = em.createQuery(sql).setParameter("companyId", companyId).setParameter("item", cm.getItem()).getResultList();
        		  if(! ls.toArray()[0].toString().equals("0")) {
        			  throw new Exception("项目已经存在,一个项目只能添加一次");
        		  }
        	  } else {                    //modify
        		  sql += " AND c.id<>:id";
        		  List ls = em.createQuery(sql).setParameter("companyId", companyId).setParameter("item", cm.getItem())
        				  .setParameter("id", cm.getId()).getResultList();
        		  if(! ls.toArray()[0].toString().equals("0")) {
        			  throw new Exception("项目已经存在,一个项目只能添加一次");
        		  }
        	  }
          }
        Companymstr c = em.find(Companymstr.class, companyId);
        String updateUser = loginService.getCurrentUsermstr().getAdAccount();
        Date updateDate = new Date();
        cm.setUpdatedBy(updateUser);
        cm.setUpdatedDatetime(updateDate);
        cm.setCompanymstr(c);
        if(cm.getId() == null) {
            cm.setCreatedBy(updateUser);
            cm.setCreatedDatetime(updateDate);
            em.persist(cm);
        } else {
            em.merge(cm);
        }
        
        BigDecimal tenThousand = new BigDecimal(10000);
        
        for(CompanyDepreciationVO cdv : depreList) {
            CompanyDepreciation cd = cdv.getDepre();
            cd.setUpdatedBy(updateUser);
            cd.setUpdatedDatetime(updateDate);
            cd.setCompanyMainAsset(cm);
            // 将万元值转化为元
            cd.setCost(cd.getCost().multiply(tenThousand));
            cd.setNetWorth(cd.getNetWorth().multiply(tenThousand));
            
            if(cd.getId() == null) {
                cd.setCreatedBy(updateUser);
                cd.setCreatedDatetime(updateDate);
                em.persist(cd);
            } else {
                em.merge(cd);
            }
        }
    }
    
    public List<CompanyDepreciationVO> getDepres(Long assertId) {
        String sql = "SELECT NEW com.wcs.tih.system.controller.vo.CompanyDepreciationVO(c) " +
        		"FROM CompanyDepreciation c " +
        		"WHERE c.companyMainAsset.id=:id " +
        		"ORDER BY c.id";
        List<CompanyDepreciationVO> rs = em.createQuery(sql).setParameter("id", assertId).getResultList();
        em.clear();
        return rs;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
