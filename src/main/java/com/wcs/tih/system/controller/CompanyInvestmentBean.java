package com.wcs.tih.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyInvestment;
import com.wcs.tih.system.controller.vo.InvestVo;
import com.wcs.tih.system.service.CompanyInvestmentService;

@ManagedBean
@ViewScoped
public class CompanyInvestmentBean {
	
	@EJB
	private CompanyInvestmentService companyInvestmentService;
	private LazyDataModel<CompanyInvestment> investmentLazyModel;

	private Map<String, Object> searchMap = new HashMap<String, Object>();

	private Companymstr companymstr = new Companymstr();
	private boolean insert;
	private List<InvestVo> investVos;
	private InvestVo investVo = new InvestVo();

	public boolean isInsert() {
		return insert;
	}

	public void init(long id) {
		companymstr = companyInvestmentService.getCompanyById(id);
		searchMap = new HashMap<String, Object>();
		searchMap.put("defunctInd", "N");
		investVo = new InvestVo();
		this.search();
	}

	public void search() {
		investVos = companyInvestmentService.findInvestVosBy(searchMap, companymstr);
	}

	public void initAddInvest() {
		investVo = new InvestVo();
		investVo.setDefunctInd("N");
		insert = true;
	}

	public void insertOrUpdate() {
		if (!companyInvestmentService.validate(investVo)) {
			return;
		}
		investVo.setCompanymstr(companymstr);
		companyInvestmentService.saveInvest(investVo);
		search();
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。请查询确认"));
		RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
	}

	public void resetForm() {
		searchMap.clear();
		searchMap.put("defunctInd", "N");
	}

	public LazyDataModel<CompanyInvestment> getInvestmentLazyModel() {
		return investmentLazyModel;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public List<InvestVo> getInvestVos() {
		return investVos;
	}

	public void setInvestVos(List<InvestVo> investVos) {
		this.investVos = investVos;
	}

	public InvestVo getInvestVo() {
		return investVo;
	}

	public void setInvestVo(InvestVo investVo) {
		this.investVo = investVo;
	}

}
