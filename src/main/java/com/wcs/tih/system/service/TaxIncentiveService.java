package com.wcs.tih.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.CompanyTaxIncentive;
import com.wcs.tih.system.controller.vo.TaxIncentiveVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 税收优惠信息Service</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class TaxIncentiveService {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;
    @EJB
    private CommonService commonService;

    /**
     * <p>Description: 查询税收优惠信息</p>
     * @param taxType 税种
     * @param effective 有效
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TaxIncentiveVo> queryTaxIncentive(long companymstrId,String taxType, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select cti from CompanyTaxIncentive cti where cti.companymstr.id = "+companymstrId);
        if (null != taxType && !"".equals(taxType)) {
            sb.append(" and cti.taxType = '" + taxType + "'");
        }
        if (null != effective && !"".equals(effective)) {
            sb.append(" and cti.defunctInd = '" + effective + "'");
        }
        sb.append(" order by cti.taxType,cti.preferentialItem");
        List<CompanyTaxIncentive> ctis = this.em.createQuery(sb.toString()).getResultList();
        List<TaxIncentiveVo> tivs = new ArrayList<TaxIncentiveVo>();
        if (null != ctis && ctis.size() != 0) {
            TaxIncentiveVo tiv;
            for (CompanyTaxIncentive cti : ctis) {
                tiv = new TaxIncentiveVo();
                tiv.setTaxIncentive(cti);
                tiv.setId(cti.getId());
                tiv.setTaxType(cti.getTaxType());
                tiv.setPreferentialItem(cti.getPreferentialItem());
                tiv.setSituationStatus(cti.getSituationRemarks());
                tiv.setPreferentialStartDatetime(cti.getPreferentialStartDatetime());
                tiv.setPreferentialEndDatetime(cti.getPreferentialEndDatetime());
                tiv.setPolicy(cti.getPolicy());
                tiv.setApprovalOrgan(cti.getApprovalOrgan());
                tiv.setEffective(cti.getDefunctInd());
                tivs.add(tiv);
            }
        }
        return tivs;
    }

    /**
     * <p>Description: 判断是否存在税收优惠信息</p>
     * @param existTaxType 税种
     * @param existPreferentialItem 优惠项目
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isExistTaxIncentive(long companymstrId,TaxIncentiveVo tiv) {
    	if("Y".equals(tiv.getEffective())){
    		return false;
    	}
        StringBuffer jpql = new StringBuffer();
        jpql.append( " select cti from CompanyTaxIncentive cti");
        jpql.append( " where cti.defunctInd = 'N'");
        jpql.append( " and cti.companymstr.id =").append(companymstrId);
        jpql.append( " and cti.taxType = '" + tiv.getTaxType() + "'");
        jpql.append( " and cti.preferentialItem = '" + tiv.getPreferentialItem() + "'");
        CompanyTaxIncentive taxIncentive = tiv.getTaxIncentive();
        if(taxIncentive != null){
        	jpql.append( " and cti.id <> ").append(taxIncentive.getId());
        }
        jpql.append( " and cti.preferentialStartDatetime = ?1");
        jpql.append( " and cti.preferentialEndDatetime = ?2");
        
        List<CompanyTaxIncentive> ctis = this.em.createQuery(jpql.toString()).setParameter(1, tiv.getPreferentialStartDatetime()).setParameter(2, tiv.getPreferentialEndDatetime()).getResultList();
        return ctis.size() > 0;
    }

    /**
     * <p>Description: 保存税收优惠信息</p>
     * @param browserLang 浏览器语言
     * @param companymstrId 公司Id
     * @param tiv 税收优惠Vo
     * @throws Exception
     */
    public void saveTaxIncentiveVo(String browserLang, long companymstrId, TaxIncentiveVo tiv) throws Exception {
        CompanyTaxIncentive cti = new CompanyTaxIncentive();
        cti.setCompanymstr(this.em.find(Companymstr.class, companymstrId));
        cti.setTaxType(tiv.getTaxType());
        cti.setPreferentialItem(tiv.getPreferentialItem().trim());
        cti.setSituationRemarks(tiv.getSituationStatus());
        cti.setPreferentialStartDatetime(tiv.getPreferentialStartDatetime());
        cti.setPreferentialEndDatetime(tiv.getPreferentialEndDatetime());
        cti.setPolicy(tiv.getPolicy().trim());
        cti.setApprovalOrgan(tiv.getApprovalOrgan().trim());
        cti.setDefunctInd(tiv.getEffective());
        cti.setCreatedBy(this.loginService.getCurrentUserName());
        cti.setCreatedDatetime(new Date());
        cti.setUpdatedBy(this.loginService.getCurrentUserName());
        cti.setUpdatedDatetime(new Date());
        cti.setStatisticDatetime(new Date());
        try {
            this.em.persist(cti);
        } catch (Exception e) {
            throw new Exception("系统出现异常，保存税种：" + getValueByDictCatKey(tiv.getTaxType(), browserLang) + "，优惠项目：" + tiv.getPreferentialItem() + "的税收优惠信息失败");
        }
    }

    /**
     * <p>Description: 更新税收优惠信息</p>
     * @param browserLang 游览器语言
     * @param tiv 税收优惠Vo
     * @throws Exception
     */
    public void updateTaxIncentiveVo(String browserLang, TaxIncentiveVo tiv) throws Exception {
        CompanyTaxIncentive cti = new CompanyTaxIncentive();
        cti.setId(tiv.getId());
        cti.setTaxType(tiv.getTaxType());
        cti.setPreferentialItem(tiv.getPreferentialItem().trim());
        cti.setSituationRemarks(tiv.getSituationStatus());
        cti.setPreferentialStartDatetime(tiv.getPreferentialStartDatetime());
        cti.setPreferentialEndDatetime(tiv.getPreferentialEndDatetime());
        cti.setPolicy(tiv.getPolicy().trim());
        cti.setApprovalOrgan(tiv.getApprovalOrgan().trim());
        cti.setDefunctInd(tiv.getEffective());
        cti.setUpdatedBy(this.loginService.getCurrentUserName());
        cti.setUpdatedDatetime(new Date());
        try {
            this.em.merge(cti);
        } catch (Exception e) {
            throw new Exception("系统出现异常，编辑税种：" + getValueByDictCatKey(tiv.getTaxType(), browserLang) + "，优惠项目：" + tiv.getPreferentialItem() + "的税收优惠信息失败");
        }
    }

    /**
     * <p>Description: 取得字典表值</p>
     * @param catPointKey cat和key值
     * @param lang 浏览器语言
     * @return
     */
    public String getValueByDictCatKey(String catPointKey, String lang) {
        return commonService.getValueByDictCatKey(catPointKey, lang);
    }

}
