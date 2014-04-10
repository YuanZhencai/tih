package com.wcs.tih.system.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyAnnualTaxPay;
import com.wcs.tih.model.CompanyTaxTypeRatio;
import com.wcs.tih.system.controller.vo.CompanyAnnualTaxPayVO;
import com.wcs.tih.system.controller.vo.CompanyTaxTypeRatioVO;

/**
 * Project: tih Description: 税种税率Service Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * 
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class CompanyTaxTypeRatioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private LoginService loginService;
	@PersistenceContext
	private EntityManager em;

	/**
	 * <p>
	 * Description: 查找税种税率
	 * </p>
	 * 
	 * @param companyId
	 * @param query
	 * @return
	 */
	public List<CompanyTaxTypeRatioVO> search(Long companyId, Map<String, String> query) {
		StringBuilder sql = new StringBuilder("SELECT NEW com.wcs.tih.system.controller.vo.CompanyTaxTypeRatioVO(c) "
				+ "FROM CompanyTaxTypeRatio c WHERE c.companymstr.id=:id ");
		String tax = query.get("tax");
		if (tax != null && !"".equals(tax)) {
			sql.append("AND c.taxType = '" + tax + "' ");
		}
		String defunct = query.get("defunct");
		if (defunct != null && !"".equals(defunct)) {
			sql.append("AND c.defunctInd = '" + defunct + "' ");
		}
		sql.append("ORDER BY c.id");
		return em.createQuery(sql.toString()).setParameter("id", companyId).getResultList();
	}

	/**
	 * <p>
	 * Description: 查找纳税金额
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public List<CompanyAnnualTaxPayVO> getTaxPays(Long id) {
		String sql = "SELECT c FROM CompanyAnnualTaxPay c " + "WHERE c.companyTaxTypeRatio.id=:id " + "ORDER BY c.id";
		List<CompanyAnnualTaxPay> cats = em.createQuery(sql).setParameter("id", id).getResultList();
		List<CompanyAnnualTaxPayVO> catvos = new ArrayList<CompanyAnnualTaxPayVO>();
		CompanyAnnualTaxPayVO catvo = null;
		for (CompanyAnnualTaxPay taxPay : cats) {
			catvo = new CompanyAnnualTaxPayVO();
			catvo.setId(taxPay.getId());
			catvo.setTaxPay(taxPay);
			catvo.setTaxRate(taxPay.getNewTaxRate());
			BigDecimal taxPayAccount = taxPay.getTaxPayAccount();
			if (taxPayAccount != null) {
				catvo.setTaxPayAccount(taxPayAccount.divide(new BigDecimal(10000))+"");
			}
			catvo.setDefunctInd(taxPay.getDefunctInd());
			catvo.setCreatedBy(taxPay.getCreatedBy());
			catvo.setCreatedDatetime(taxPay.getCreatedDatetime());
			catvo.setUpdatedBy(taxPay.getUpdatedBy());
			catvo.setUpdatedDatetime(taxPay.getUpdatedDatetime());
			catvo.setRemarks(taxPay.getRemarks());
			catvo.setTaxPayYear(taxPay.getTaxPayYear());
			
			catvos.add(catvo);
		}
		return catvos;
	}

	/**
	 * <p>
	 * Description: 保存税种税率信息
	 * </p>
	 * 
	 * @param companyId
	 * @param currentTaxTypeRatio
	 * @param taxPayList
	 * @throws Exception
	 */
	public void saveTaxTypeRatio(Long companyId, CompanyTaxTypeRatioVO currentTaxTypeRatio, List<CompanyAnnualTaxPayVO> taxPayList) throws Exception {
		Companymstr c = em.find(Companymstr.class, companyId);

		String updateUser = loginService.getCurrentUsermstr().getAdAccount();
		Date updateDate = new Date();

		CompanyTaxTypeRatio ratio = currentTaxTypeRatio.getTypeRatio();
		ratio.setCreatedBy(updateUser);
		ratio.setCreatedDatetime(updateDate);
		ratio.setUpdatedBy(updateUser);
		ratio.setUpdatedDatetime(updateDate);
		ratio.setCompanymstr(c);
		StringBuilder jpql = new StringBuilder();
		jpql.append( " SELECT t FROM CompanyTaxTypeRatio t");
		jpql.append( " WHERE t.companymstr.id = ").append(companyId);
		jpql.append( " AND t.taxType= '" + ratio.getTaxType() + "'");
		
		String sql = "SELECT t FROM CompanyTaxTypeRatio t WHERE t.companymstr.id = :companyId AND t.taxType=:taxType";
        
        if(ratio.getId() == null) {
        	if("N".equals(ratio.getDefunctInd())){
        		System.out.println("[saveTaxTypeRatio] persist " + ratio.getId());
        		List<CompanyTaxTypeRatio> rs = em.createQuery(jpql.toString()).getResultList();
        		if(! rs.isEmpty()) {
        			throw new Exception("不可重复添加此税种类型数据。");
        		}
        	}
            ratio.setCreatedBy(updateUser);
            ratio.setCreatedDatetime(updateDate);
            em.persist(ratio);
        } else {
        	if("N".equals(ratio.getDefunctInd())){
        		jpql.append( " AND t.id <> ").append(ratio.getId());
        		List<CompanyTaxTypeRatio> rs = em.createQuery(jpql.toString()).getResultList();
        		if(! rs.isEmpty()) {
        			throw new Exception("不可重复添加此税种类型数据。");
        		}
        	}
             
            em.merge(ratio);
        }
        
		for (CompanyAnnualTaxPayVO atpvo : taxPayList) {
			CompanyAnnualTaxPay atp = atpvo.getTaxPay();
			atp.setUpdatedBy(updateUser);
			atp.setUpdatedDatetime(updateDate);
			atp.setCompanyTaxTypeRatio(ratio);
			// 将万元转化为元
			if (atpvo.getTaxPayAccount() != null) {
				atp.setTaxPayAccount(new BigDecimal(atpvo.getTaxPayAccount()).multiply(new BigDecimal(10000)));
			}
			atp.setNewTaxRate(atpvo.getTaxRate());
			atp.setDefunctInd(atpvo.getDefunctInd());
			atp.setRemarks(atpvo.getRemarks());
			atp.setTaxPayYear(atpvo.getTaxPayYear());

			if (atp.getId() == null) {
				atp.setCreatedBy(updateUser);
				atp.setCreatedDatetime(updateDate);
				em.persist(atp);
			} else {
				em.merge(atp);
			}
		}
	}

	//
	public LoginService getLoginService() {
		return loginService;
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

}
