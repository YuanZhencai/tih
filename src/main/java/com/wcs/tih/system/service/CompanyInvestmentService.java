package com.wcs.tih.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyInvestment;
import com.wcs.tih.system.controller.vo.InvestVo;
import com.wcs.tih.util.ValidateUtil;

@Stateless
public class CompanyInvestmentService {

	private static final ResourceBundle REGEX_BUNDLE = ResourceBundle.getBundle("regex");

	@PersistenceContext
	private EntityManager em;
	@EJB
	private LoginService loginservice;

	public List<InvestVo> findInvestVosBy(Map<String, Object> m, Companymstr company) {
		StringBuilder sb = new StringBuilder();
		sb.append("select s from CompanyInvestment s where s.companymstr.id=").append(company.getId());
		if (m.get("defunctInd") != null && !m.get("defunctInd").equals("")) {
			sb.append(" and s.defunctInd= '").append(m.get("defunctInd")).append("'");
		}
		if (m.get("phase") != null && !m.get("phase").equals("")) {
			sb.append(" and s.phase = '").append(m.get("phase")).append("'");
		}
		if (m.get("currency") != null && !m.get("currency").equals("")) {
			sb.append(" and s.currency ='").append(m.get("currency")).append("'");
		}
		List<CompanyInvestment> invests = em.createQuery(sb.toString()).getResultList();
		List<InvestVo> investVos = new ArrayList<InvestVo>();
		for (CompanyInvestment invest : invests) {
			investVos.add(getInvestVo(invest));
		}
		return investVos;
	}

	public InvestVo getInvestVo(CompanyInvestment invest) {
		InvestVo investVo = new InvestVo();
		investVo.setCompanymstr(invest.getCompanymstr());
		investVo.setCreatedBy(invest.getCreatedBy());
		investVo.setCreatedDatetime(invest.getCreatedDatetime());
		investVo.setCurrency(invest.getCurrency());
		investVo.setDefunctInd(invest.getDefunctInd());
		investVo.setEndDatetime(invest.getEndDatetime());
		investVo.setInvest(invest);
		investVo.setInvestAccount(invest.getInvestAccount() + "");
		investVo.setInvestmentRatio(invest.getInvestmentRatio());
		investVo.setPhase(invest.getPhase());
		investVo.setStartDatetime(invest.getStartDatetime());
		investVo.setUpdatedBy(invest.getUpdatedBy());
		investVo.setUpdatedDatetime(invest.getUpdatedDatetime());
		investVo.setInvestAddress(invest.getInvestAddress());
		return investVo;
	}

	public void saveInvest(InvestVo investVo) {
		CompanyInvestment invest = getInvest(investVo);
		em.merge(invest);
	}

	public CompanyInvestment getInvest(InvestVo investVo) {
		String userName = loginservice.getCurrentUserName();
		Date currentDate = new Date();
		CompanyInvestment invest = investVo.getInvest();
		if (invest == null) {
			invest = new CompanyInvestment();
			invest.setCreatedBy(userName);
			invest.setCreatedDatetime(currentDate);
		}
		invest.setUpdatedBy(userName);
		invest.setUpdatedDatetime(currentDate);
		invest.setDefunctInd(investVo.getDefunctInd());
		invest.setCompanymstr(investVo.getCompanymstr());
		invest.setCurrency(investVo.getCurrency());
		invest.setEndDatetime(investVo.getEndDatetime());
		invest.setInvestAccount(new BigDecimal(investVo.getInvestAccount()));
		invest.setInvestAddress(investVo.getInvestAddress());
		invest.setInvestee(investVo.getInvestee());
		invest.setInvestmentRatio(investVo.getInvestmentRatio());
		invest.setPhase(investVo.getPhase());
		invest.setStartDatetime(investVo.getStartDatetime());
		return invest;
	}

	public boolean validate(InvestVo investVo) {
		boolean validated = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateRequired(context, investVo.getPhase(), "投资对象：")
				& ValidateUtil.validateRequired(context, investVo.getInvestAddress(), "投资对象地址：")
				& ValidateUtil.validateRequired(context, investVo.getStartDatetime(), "投资日期：")
				& ValidateUtil.validateRequired(context, investVo.getInvestmentRatio(), "投资所占比例：")
				& ValidateUtil.validateRequiredAndRegex(context, investVo.getInvestAccount(), "投资金额：", REGEX_BUNDLE.getString("TWODECIMAL"), "只能为数值类型且小数点后两位。") & ValidateUtil
					.validateRequired(context, investVo.getCurrency(), "币种："))) {
			validated = false;
		}
		return validated;
	}

	public void save(CompanyInvestment companyInvestment) {
		companyInvestment.setCreatedBy(loginservice.getCurrentUsermstr().getAdAccount());
		companyInvestment.setUpdatedBy(loginservice.getCurrentUsermstr().getAdAccount());
		companyInvestment.setUpdatedBy(this.loginservice.getCurrentUsermstr().getAdAccount());
		companyInvestment.setCreatedBy(this.loginservice.getCurrentUsermstr().getAdAccount());
		companyInvestment.setCreatedDatetime(new Date());
		companyInvestment.setUpdatedDatetime(new Date());
		this.em.persist(companyInvestment);
	}

	public void update(CompanyInvestment companyInvestment) {
		CompanyInvestment c = this.getCompanyInvestmentById(companyInvestment.getId());
		c.setUpdatedDatetime(new Date());
		c.setUpdatedBy(loginservice.getCurrentUsermstr().getAdAccount());
		c.setPhase(companyInvestment.getPhase());
		c.setStartDatetime(companyInvestment.getStartDatetime());
		c.setInvestAccount(companyInvestment.getInvestAccount());
		c.setDefunctInd(companyInvestment.getDefunctInd());
		c.setCurrency(companyInvestment.getCurrency());
		c.setInvestAddress(companyInvestment.getInvestAddress());
		c.setInvestmentRatio(companyInvestment.getInvestmentRatio());
		this.em.merge(c);

	}

	public CompanyInvestment getCompanyInvestmentById(long id) {
		return (CompanyInvestment) this.em.createQuery("select c from CompanyInvestment c where c.id = " + id).getSingleResult();
	}

	public Companymstr getCompanyById(long id) {
		return this.em.createQuery("select c from Companymstr c where c.id = " + id).getResultList().size() > 0 ? (Companymstr) this.em
				.createQuery("select c from Companymstr c where c.id = " + id).getResultList().get(0) : null;
	}

	public String getLastPhase(long id) {
		List<CompanyInvestment> li = this.em.createQuery(
				new StringBuilder().append("select s from CompanyInvestment s where s.companymstr.id=").append(id).toString()).getResultList();
		return li.size() > 0 ? li.get(li.size() - 1).getPhase() : null;
	}

}
