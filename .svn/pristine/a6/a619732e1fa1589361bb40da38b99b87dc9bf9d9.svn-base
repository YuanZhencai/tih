package com.wcs.tih.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyAnnualReturn;
import com.wcs.tih.model.CompanyFinancialReturn;
import com.wcs.tih.system.controller.vo.AnnualReturnVo;
import com.wcs.tih.system.controller.vo.FinancialReturnVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 财政返回Service</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class FinancialReturnService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;

    /**
     * <p>Description: 查询财政返回信息</p>
     * @param item 优惠项目
     * @param effective 有效
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FinancialReturnVo> queryFinancialReturn(long companymstrId,String item, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select cfr from CompanyFinancialReturn cfr where cfr.companymstr.id = "+companymstrId);
        if (null != item && !"".equals(item)) {
            sb.append(" and cfr.taxType like '%" + item + "%'");
        }
        if (null != effective && !"".equals(effective)) {
            sb.append(" and cfr.defunctInd ='" + effective + "'");
        }
        List<CompanyFinancialReturn> cfrs = this.em.createQuery(sb.toString()).getResultList();
        if (null != cfrs && cfrs.size() != 0) {
            List<FinancialReturnVo> frvs = new ArrayList<FinancialReturnVo>();
            List<CompanyAnnualReturn> cars;
            List<AnnualReturnVo> arvs;
            AnnualReturnVo arv;
            FinancialReturnVo frv;
            for (CompanyFinancialReturn cfr : cfrs) {
                frv = new FinancialReturnVo();
                frv.setFinancialReturn(cfr);
                frv.setId(cfr.getId());
                frv.setRegistration(cfr.getRegistration());
                frv.setItem(cfr.getItme());
                frv.setReturnStartDatetime(cfr.getReturnStartDatetime());
                frv.setReturnEndDatetime(cfr.getReturnEndDatetime());
                frv.setReturnBase(cfr.getReturnBase());
                frv.setReturnRatio(cfr.getReturnRatio());
                frv.setReturnAccording(cfr.getReturnAccording());
                cars = cfr.getCompanyAnnualReturns();
                if (null != cars && cars.size() != 0) {
                    arvs = new ArrayList<AnnualReturnVo>();
                    for (CompanyAnnualReturn car : cars) {
                        arv = new AnnualReturnVo();
                        arv.setAnnualReturn(car);
                        arv.setId(car.getId());
                        arv.setReturnYear(car.getReturnYear());
                        arv.setReturnAccount(car.getReturnAccount() + "");
                        arv.setEffective(car.getDefunctInd());
                        arv.setActualReturnAccount(car.getActualReturnAccount() + "");
                        arv.setBaseReturnAccount(car.getBaseReturnAccount() + "");
                        arv.setPaymentDatetime(car.getPaymentDatetime());
                        arv.setReturnPurpose(car.getReturnPurpose());
                        arv.setRemark(car.getRemark());
                        arv.setShouldReturnAccount(car.getShouldReturnAccount() + "");
                        arvs.add(arv);
                    }
                    frv.setAnnualReturnVos(arvs);
                }
                frv.setEffective(cfr.getDefunctInd());
                frv.setTaxType(cfr.getTaxType());
                frvs.add(frv);
            }
            return frvs;
        }
        return null;
    }

    /**
     * <p>Description: 判断是否存在财政返回信息</p>
     * @param item 优惠项目
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isExistFinancialReturn(long companymstrId,FinancialReturnVo frv) {
    	if("Y".equals(frv.getEffective())){
    		return false;
    	}
    	StringBuffer jpql = new StringBuffer();
    	jpql.append(" select cfr from CompanyFinancialReturn cfr");
    	jpql.append(" where cfr.defunctInd = 'N'");
    	jpql.append(" and cfr.companymstr.id = ").append(companymstrId);
    	
    	jpql.append(" and cfr.registration = '" + frv.getRegistration() + "'");
    	jpql.append(" and cfr.taxType = '" + frv.getTaxType() + "'");
    	jpql.append(" and cfr.returnBase = '" + frv.getReturnBase() + "'");
    	jpql.append(" and cfr.returnRatio = '" + frv.getReturnRatio() + "'");
    	
    	jpql.append(" and cfr.returnStartDatetime = ?1");
    	jpql.append(" and cfr.returnEndDatetime = ?2");
    	
    	CompanyFinancialReturn financialReturn = frv.getFinancialReturn();
    	if(financialReturn != null){
    		jpql.append(" and cfr.id <> ").append(financialReturn.getId());
    	}
    	
        List<CompanyFinancialReturn> cfrs = this.em.createQuery(jpql.toString()).setParameter(1, frv.getReturnStartDatetime()).setParameter(2, frv.getReturnEndDatetime()).getResultList();
        return cfrs.size() > 0 ;
    }

    /**
     * <p>Description: 保存财政返回信息</p>
     * @param companymstrId 公司Id
     * @param frv 财政返回信息Vo
     * @throws Exception
     */
    public void saveFinancialReturn(long companymstrId, FinancialReturnVo frv) throws Exception {
        String userName = this.loginService.getCurrentUserName();
        Date currentDatetime = new Date();
        CompanyFinancialReturn cfr = new CompanyFinancialReturn();
        cfr.setCompanymstr(this.em.find(Companymstr.class, companymstrId));
        cfr.setRegistration(frv.getRegistration());
        cfr.setReturnStartDatetime(frv.getReturnStartDatetime());
        cfr.setReturnEndDatetime(frv.getReturnEndDatetime());
        cfr.setReturnBase(frv.getReturnBase().trim());
        cfr.setReturnRatio(frv.getReturnRatio().trim());
        cfr.setReturnAccording(frv.getReturnAccording());
        cfr.setDefunctInd(frv.getEffective());
        cfr.setCreatedBy(userName);
        cfr.setCreatedDatetime(currentDatetime);
        cfr.setUpdatedBy(userName);
        cfr.setUpdatedDatetime(currentDatetime);
        cfr.setTaxType(frv.getTaxType());
        try {
            this.em.persist(cfr);
            if (null != frv.getAnnualReturnVos() && frv.getAnnualReturnVos().size() != 0) {
                for (AnnualReturnVo arv : frv.getAnnualReturnVos()) {
                    CompanyAnnualReturn car = new CompanyAnnualReturn();
                    car.setCompanyFinancialReturn(cfr);
                    car.setReturnYear(arv.getReturnYear());
                    car.setReturnAccount(new BigDecimal(arv.getReturnAccount()));
                    car.setDefunctInd(arv.getEffective());
                    car.setActualReturnAccount(new BigDecimal(arv.getActualReturnAccount()));
                    car.setBaseReturnAccount(new BigDecimal(arv.getBaseReturnAccount()));
                    car.setShouldReturnAccount(new BigDecimal(arv.getShouldReturnAccount()));
                    car.setPaymentDatetime(arv.getPaymentDatetime());
                    car.setReturnPurpose(arv.getReturnPurpose());
                    car.setRemark(arv.getRemark());
                    car.setPaymentDatetime(arv.getPaymentDatetime());
                    
                    car.setCreatedBy(userName);
                    car.setCreatedDatetime(currentDatetime);
                    car.setUpdatedBy(userName);
                    car.setUpdatedDatetime(currentDatetime);
                    this.em.persist(car);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("系统出现异常，保存财政返还信息失败");
        }
    }

    /**
     * <p>Description: 更新财政返回信息</p>
     * @param frv 财政返回信息Vo
     * @throws Exception
     */
    public void updateFinancialReturn(FinancialReturnVo frv) throws Exception {
        String userName = this.loginService.getCurrentUserName();
        Date currentDatetime = new Date();
        CompanyFinancialReturn cfr = new CompanyFinancialReturn();
        cfr.setId(frv.getId());
        cfr.setRegistration(frv.getRegistration());
        cfr.setReturnStartDatetime(frv.getReturnStartDatetime());
        cfr.setReturnEndDatetime(frv.getReturnEndDatetime());
        cfr.setReturnBase(frv.getReturnBase().trim());
        cfr.setReturnRatio(frv.getReturnRatio().trim());
        cfr.setReturnAccording(frv.getReturnAccording());
        cfr.setDefunctInd(frv.getEffective());
        cfr.setUpdatedBy(userName);
        cfr.setUpdatedDatetime(currentDatetime);
        cfr.setTaxType(frv.getTaxType());
        try {
            this.em.merge(cfr);
            if (null != frv.getAnnualReturnVos() && frv.getAnnualReturnVos().size() != 0) {
                for (AnnualReturnVo arv : frv.getAnnualReturnVos()) {
                    CompanyAnnualReturn car = arv.getAnnualReturn();
                    if(car == null){
                    	car = new CompanyAnnualReturn();
                    }
                    car.setCompanyFinancialReturn(cfr);
                    car.setReturnYear(arv.getReturnYear());
                    car.setReturnAccount(new BigDecimal(arv.getReturnAccount()));
                    car.setDefunctInd(arv.getEffective());
                    car.setActualReturnAccount(new BigDecimal(arv.getActualReturnAccount()));
                    car.setBaseReturnAccount(new BigDecimal(arv.getBaseReturnAccount()));
                    car.setShouldReturnAccount(new BigDecimal(arv.getShouldReturnAccount()));
                    car.setPaymentDatetime(arv.getPaymentDatetime());
                    car.setReturnPurpose(arv.getReturnPurpose());
                    car.setRemark(arv.getRemark());
                    car.setPaymentDatetime(arv.getPaymentDatetime());
                    
                    car.setCreatedBy(userName);
                    car.setCreatedDatetime(currentDatetime);
                    car.setUpdatedBy(userName);
                    car.setUpdatedDatetime(currentDatetime);
                    this.em.merge(car);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("系统出现异常，编辑财政返还信息失败");
        }
    }
}
