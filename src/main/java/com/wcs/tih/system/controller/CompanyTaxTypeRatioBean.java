package com.wcs.tih.system.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.model.CompanyTaxTypeRatio;
import com.wcs.tih.system.controller.vo.CompanyAnnualTaxPayVO;
import com.wcs.tih.system.controller.vo.CompanyTaxTypeRatioVO;
import com.wcs.tih.system.service.CompanyTaxTypeRatioService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih Description: 税种税率Backing Bean Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * 
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ViewScoped
@ManagedBean
public class CompanyTaxTypeRatioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private CompanyTaxTypeRatioService ratioService;
	// 查询条件
	private Map<String, String> query;
	// 当前操作的公司id
	private Long companyId;
	// 税种税率
	private LazyDataModel<CompanyTaxTypeRatioVO> lazyTypeRatios;
	private CompanyTaxTypeRatioVO currentTaxTypeRatio;

	// 纳税金额
	private LazyDataModel<CompanyAnnualTaxPayVO> lazyTaxPays;
	private CompanyAnnualTaxPayVO currentTaxPay;
	private List<CompanyAnnualTaxPayVO> taxPayList;

	private CompanyAnnualTaxPayVO selectedTaxPay;

	private String model;

	@PostConstruct
	public void initBean() {
		query = new HashMap<String, String>(2);

		taxPayList = new ArrayList<CompanyAnnualTaxPayVO>();
		initTaxTypeRatio();
		initTaxPay();
	}

	/**
	 * <p>
	 * Description: 按钮点击，清除信息
	 * </p>
	 */
	public void clearTaxInfo() {
		query.clear();
		query.put("defunct", "N");
		lazyTypeRatios = null;
		search();
	}

	/**
	 * <p>
	 * Description: 查询税种税率信息
	 * </p>
	 */
	public void search() {
		// 测试用
		lazyTypeRatios = new PageModel<CompanyTaxTypeRatioVO>(ratioService.search(companyId, query), false);
	}

	/**
	 * <p>
	 * Description: 初始化税种税率信息
	 * </p>
	 */
	public void initTaxTypeRatio() {
		CompanyTaxTypeRatio ttr = new CompanyTaxTypeRatio();
		ttr.setDefunctInd("N");
		currentTaxTypeRatio = new CompanyTaxTypeRatioVO(ttr);
		taxPayList.clear();
		lazyTaxPays = new PageModel<CompanyAnnualTaxPayVO>(taxPayList, false);
	}

	/**
	 * <p>
	 * Description: 查询纳税金额信息
	 * </p>
	 */
	public void searchTaxPays() {
		if (currentTaxTypeRatio == null) {
			return;
		}
		taxPayList.clear();
		List<CompanyAnnualTaxPayVO> tmp = ratioService.getTaxPays(currentTaxTypeRatio.getTypeRatio().getId());
		for (CompanyAnnualTaxPayVO t : tmp) {
			taxPayList.add(t);
		}
		lazyTaxPays = new PageModel<CompanyAnnualTaxPayVO>(taxPayList, false);
	}

	/**
	 * <p>
	 * Description: 新增、修改税种税率信息
	 * </p>
	 */
	public void editTaxTypeRatio() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateRequired(context, currentTaxTypeRatio.getTypeRatio().getTaxType(), "税种：")
				& ValidateUtil.validateRequiredAndMax(context, currentTaxTypeRatio.getTypeRatio().getTaxBasis(), "计税基础：", 50)
				& ValidateUtil.validateRequired(context, currentTaxTypeRatio.getTypeRatio().getReportFrequency(), "申报频率：") & ValidateUtil
					.validateMaxlength(context, currentTaxTypeRatio.getTypeRatio().getRemarks(), "备注：", 100))) {
			return;
		}

		if (currentTaxTypeRatio == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "税种税率维护：", "操作异常……"));
			return;
		}
		try {
			ratioService.saveTaxTypeRatio(companyId, currentTaxTypeRatio, taxPayList);
			search();
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "税种税率维护：", e.getMessage()));
			return;
		}
		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "税种税率维护：", "操作成功。"));
	}

	/**
	 * <p>
	 * Description: 初始化纳税金额信息
	 * </p>
	 */
	public void initTaxPay() {
		currentTaxPay = new CompanyAnnualTaxPayVO();
		currentTaxPay.setDefunctInd("N");
		selectedTaxPay = new CompanyAnnualTaxPayVO();
	}

	public void getTaxPayDetails() {
		try {
			BeanUtils.copyProperties(currentTaxPay, selectedTaxPay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Description: 新增、修改纳税金额
	 * </p>
	 */
	public void saveAnnualTaxPay() {
		FacesContext context = FacesContext.getCurrentInstance();
		boolean validate = true;
		if (currentTaxPay == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "纳税金额维护：", "操作异常……"));
			validate = false;
		}
		if (!(ValidateUtil.validateRegex(context, currentTaxPay.getTaxPayAccount(), "纳税金额：", "^-?[0-9]+([.][0-9]{1,2})?$", "填写格式只能为整数或保留2位小数")
				& ValidateUtil.validateRequired(context, currentTaxPay.getTaxPayYear(), "纳税年度：") & ValidateUtil.validateMaxlength(context,
						currentTaxPay.getRemarks(), "税率变化说明：", 100))) {
			validate = false;
		}

		if ("N".equals(currentTaxPay.getDefunctInd())) {
			for (CompanyAnnualTaxPayVO tax : taxPayList) {
				if (!tax.equals(selectedTaxPay) && "N".equals(tax.getDefunctInd())) {
					if (currentTaxPay.getTaxPayYear().getYear() == tax.getTaxPayYear().getYear()) {
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "纳税年度：", "纳税年度不能重复"));
						validate = false;
					}
				}
			}
		}

		if (!validate) {
			return;
		}

		try {
			ConvertUtils.register(new DateConverter(null), java.util.Date.class);
			BeanUtils.copyProperties(selectedTaxPay, currentTaxPay);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("add".equals(model)) {
			taxPayList.add(selectedTaxPay);
			lazyTaxPays = new PageModel<CompanyAnnualTaxPayVO>(taxPayList, false);
		}
		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "纳税金额维护：", "操作成功。"));
	}

	// G & S
	public CompanyTaxTypeRatioService getRatioService() {
		return ratioService;
	}

	public void setRatioService(CompanyTaxTypeRatioService ratioService) {
		this.ratioService = ratioService;
	}

	public Map<String, String> getQuery() {
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public LazyDataModel<CompanyTaxTypeRatioVO> getLazyTypeRatios() {
		return lazyTypeRatios;
	}

	public void setLazyTypeRatios(LazyDataModel<CompanyTaxTypeRatioVO> lazyTypeRatios) {
		this.lazyTypeRatios = lazyTypeRatios;
	}

	public CompanyTaxTypeRatioVO getCurrentTaxTypeRatio() {
		return currentTaxTypeRatio;
	}

	public void setCurrentTaxTypeRatio(CompanyTaxTypeRatioVO currentTaxTypeRatio) {
		this.currentTaxTypeRatio = currentTaxTypeRatio;
	}

	public LazyDataModel<CompanyAnnualTaxPayVO> getLazyTaxPays() {
		return lazyTaxPays;
	}

	public void setLazyTaxPays(LazyDataModel<CompanyAnnualTaxPayVO> lazyTaxPays) {
		this.lazyTaxPays = lazyTaxPays;
	}

	public CompanyAnnualTaxPayVO getCurrentTaxPay() {
		return currentTaxPay;
	}

	public void setCurrentTaxPay(CompanyAnnualTaxPayVO currentTaxPay) {
		this.currentTaxPay = currentTaxPay;
	}

	public List<CompanyAnnualTaxPayVO> getTaxPayList() {
		return taxPayList;
	}

	public void setTaxPayList(List<CompanyAnnualTaxPayVO> taxPayList) {
		this.taxPayList = taxPayList;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public CompanyAnnualTaxPayVO getSelectedTaxPay() {
		return selectedTaxPay;
	}

	public void setSelectedTaxPay(CompanyAnnualTaxPayVO selectedTaxPay) {
		this.selectedTaxPay = selectedTaxPay;
	}
}
