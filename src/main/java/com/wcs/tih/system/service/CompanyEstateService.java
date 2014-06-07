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
import com.wcs.tih.model.CompanyEstate;
import com.wcs.tih.system.controller.vo.CompanyEstateVO;

/**
 * Project: tih
 * Description: 房产信息Service
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class CompanyEstateService implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB private LoginService loginService;
    
    @PersistenceContext private EntityManager em;
    
    /**
     * <p>Description: 取公司相关联的房产信息</p>
     * @param companyId
     * @param query
     * @return
     */
    public List<CompanyEstateVO> search(Long companyId, Map<String, Object> query) {
        String sql = "SELECT NEW com.wcs.tih.system.controller.vo.CompanyEstateVO(c) " +
        		"FROM CompanyEstate c " +
        		"WHERE c.companymstr.id=:id AND c.estateNo LIKE :no " +
        		"AND c.name LIKE :name AND c.defunctInd LIKE :defunct " +
        		"ORDER BY c.id";
        Query q = em.createQuery(sql).setParameter("id", companyId);
        
        Object no = query.get("no");
        q.setParameter("no", no != null && !"".equals(no.toString()) ? "%" + no.toString() + "%" : "%");
        Object name = query.get("name");
        q.setParameter("name", name != null && !"".equals(name.toString()) ? "%" + name.toString() + "%" : "%");
        Object df = query.get("defunct");
        q.setParameter("defunct", df != null && !"".equals(df.toString()) ? df.toString() : "%");
        List<CompanyEstateVO> rs = q.getResultList();
        em.clear();
        return rs;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void saveEstate(Long companyId, CompanyEstate est) throws Exception {
        est.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
        est.setUpdatedDatetime(new Date());
        // 将万元转化为元
        BigDecimal tenThousand = new BigDecimal(10000);
        if(est.getEstateAccountCost() != null){
        	est.setEstateAccountCost(est.getEstateAccountCost().multiply(tenThousand));
        }
        if(est.getCalTaxLandCost() != null){
        	est.setCalTaxLandCost(est.getCalTaxLandCost().multiply(tenThousand));
        }
        if(est.getCalTaxEstateCost() != null){
        	est.setCalTaxEstateCost(est.getCalTaxEstateCost().multiply(tenThousand));
        }
        String sql = "SELECT c FROM CompanyEstate c WHERE c.defunctInd = 'N' and c.name = :name and c.companymstr.id = " + companyId;
        List<CompanyEstate> ces = em.createQuery(sql).setParameter("name", est.getName()).getResultList();
        if(est.getId() == null) {
            if(ces != null && ces.size() > 0 && "N".equals(est.getDefunctInd())) {
                if(est.getEstateAccountCost() != null){
                	est.setEstateAccountCost(est.getEstateAccountCost().divide(tenThousand));
                }
                if(est.getCalTaxLandCost() != null){
                	est.setCalTaxLandCost(est.getCalTaxLandCost().divide(tenThousand));
                }
                if(est.getCalTaxEstateCost() != null){
                	est.setCalTaxEstateCost(est.getCalTaxEstateCost().divide(tenThousand));
                }
                throw new Exception("房产名称重复");
            }
            est.setCreatedBy(loginService.getCurrentUsermstr().getAdAccount());
            est.setCreatedDatetime(new Date());
            Companymstr c = em.find(Companymstr.class, companyId);
            est.setCompanymstr(c);
            em.persist(est);
        } else {
        	if("N".equals(est.getDefunctInd())){
        		for (CompanyEstate ce : ces) {
        			if(!ce.getId().equals(est.getId())){
        				if(est.getEstateAccountCost() != null){
        					est.setEstateAccountCost(est.getEstateAccountCost().divide(tenThousand));
        				}
        				if(est.getCalTaxLandCost() != null){
        					est.setCalTaxLandCost(est.getCalTaxLandCost().divide(tenThousand));
        				}
        				if(est.getCalTaxEstateCost() != null){
        					est.setCalTaxEstateCost(est.getCalTaxEstateCost().divide(tenThousand));
        				}
        				throw new Exception("房产名称重复");
        			}
        		}
        	}
            em.merge(est);
            if(est.getEstateAccountCost() != null){
            	est.setEstateAccountCost(est.getEstateAccountCost().divide(tenThousand));
            }
            if(est.getCalTaxLandCost() != null){
            	est.setCalTaxLandCost(est.getCalTaxLandCost().divide(tenThousand));
            }
            if(est.getCalTaxEstateCost() != null){
            	est.setCalTaxEstateCost(est.getCalTaxEstateCost().divide(tenThousand));
            }
        }
    }
}
