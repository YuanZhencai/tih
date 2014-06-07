package com.wcs.tih.system.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.system.controller.vo.AnnualReturnVo;
import com.wcs.tih.system.controller.vo.FinancialReturnVo;
import com.wcs.tih.system.service.FinancialReturnService;
import com.wcs.tih.util.DateUtil;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean(name = "financialReturnBean")
@ViewScoped
public class FinancialReturnBean {
	private static final String ISSUCC = "issucc";
	@EJB
	private FinancialReturnService financialReturnService;
	private AnnualReturnVo annualReturnVo;
	private FinancialReturnVo financialReturnVo;
	private LazyDataModel<FinancialReturnVo> lazyFinancialReturnVoModel;
	private String existItem;
	private String item;
	private String effective;
	private String financialExcuteMethod;
	private String annualExcuteMethod;
	private long companymstrId;

	/**
	 * <p>
	 * Description: 初始化
	 * </p>
	 */
	@PostConstruct
	public void init() {
		annualReturnVo = new AnnualReturnVo();
		financialReturnVo = new FinancialReturnVo();
		financialReturnVo.setAnnualReturnVos(new ArrayList<AnnualReturnVo>());
	}

	/**
	 * <p>
	 * Description: 初始化
	 * </p>
	 */
	public void initFinancialReturn() {
		lazyFinancialReturnVoModel = null;
		item = "";
		effective = "N";
		this.queryFinancialReturn();
	}

	/**
	 * <p>
	 * Description: 查询财政返回信息
	 * </p>
	 */
	public void queryFinancialReturn() {
		List<FinancialReturnVo> frvs = financialReturnService.queryFinancialReturn(this.companymstrId, item, effective);
		if (null != frvs && frvs.size() != 0) {
			lazyFinancialReturnVoModel = new PageModel<FinancialReturnVo>(frvs, false);
		} else {
			lazyFinancialReturnVoModel = null;
		}
	}

	/**
	 * <p>
	 * Description: 增加财政返回信息
	 * </p>
	 */
	public void addFinancialReturn() {
		financialReturnVo = new FinancialReturnVo();
		financialReturnVo.setEffective("N");
		financialReturnVo.setAnnualReturnVos(new ArrayList<AnnualReturnVo>());
	}

	/**
	 * <p>
	 * Description: 保存财政返回信息
	 * </p>
	 */
	public void saveFinancialReturn() {
		FacesContext context = FacesContext.getCurrentInstance();
		RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
		if (!(ValidateUtil.validateRequired(context, financialReturnVo.getRegistration(), "公司注册地：")
				& ValidateUtil.validateRequired(context, financialReturnVo.getTaxType(), "税种：")
				& ValidateUtil.validateTwoDate(context, financialReturnVo.getReturnStartDatetime(), financialReturnVo.getReturnEndDatetime(),
						"返还期间前一个日期不能大于后一个日期") & ValidateUtil.validateRequiredAndMax(context, financialReturnVo.getReturnBase(), "返还计算基数：", 100)
				& ValidateUtil.validateRequiredAndMax(context, financialReturnVo.getReturnRatio(), "返还比例：", 100) & ValidateUtil.validateMaxlength(
				context, financialReturnVo.getReturnAccording(), "备注：", 500))) {
			return;
		}
		boolean b = true;
		if (financialReturnService.isExistFinancialReturn(companymstrId, financialReturnVo)) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败：", "不能添加相同财政返还信息"));
			b = false;
		}
		if (!b) {
			return;
		}
		
		if ("add".equals(financialExcuteMethod)) {
			try {
				financialReturnService.saveFinancialReturn(companymstrId, financialReturnVo);
				RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "保存财政返还信息成功,请查看并确认"));
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
			}
		} else {
			try {
				financialReturnService.updateFinancialReturn(financialReturnVo);
				RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "编辑财政返还信息成功,请查看并确认"));
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
			}
		}
		queryFinancialReturn();
	}

	/**
	 * <p>
	 * Description: 增加年返回金额
	 * </p>
	 */
	public void addAnnualReturn() {
		annualReturnVo = new AnnualReturnVo();
		annualReturnVo.setEffective("N");
	}

	/**
	 * <p>
	 * Description: 保存年返回金额
	 * </p>
	 */
	public void saveAnnualReturn() {
		FacesContext context = FacesContext.getCurrentInstance();
		RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
		boolean b = true;
		if (!(( ValidateUtil.validateRequired(context, annualReturnVo.getPaymentDatetime(), "收款时间：") 
				& ValidateUtil.validateRequiredAndRegex(context, annualReturnVo.getBaseReturnAccount(), "返还计算基数金额：", "^-?[0-9]+([.][0-9]{1,2})?$",
				"填写格式只能为整数或保留2位小数")
				& ValidateUtil.validateRequiredAndRegex(context, annualReturnVo.getShouldReturnAccount(), "应返还金额：", "^-?[0-9]+([.][0-9]{1,2})?$",
						"填写格式只能为整数或保留2位小数")
				& ValidateUtil.validateRequiredAndRegex(context, annualReturnVo.getReturnAccount(), "返还金额：", "^-?[0-9]+([.][0-9]{1,2})?$",
						"填写格式只能为整数或保留2位小数") & ValidateUtil.validateRequiredAndRegex(context, annualReturnVo.getActualReturnAccount(), "实际纳税金额：",
				"^-?[0-9]+([.][0-9]{1,2})?$", "填写格式只能为整数或保留2位小数")))) {
			b = false;
		}
		if (!b) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		if (null != financialReturnVo && null != financialReturnVo.getAnnualReturnVos() && financialReturnVo.getAnnualReturnVos().size() != 0) {
			if (annualReturnVo.getReturnYear() != null && "N".equals(annualReturnVo.getEffective())) {
				for (AnnualReturnVo arv : financialReturnVo.getAnnualReturnVos()) {
					if (!arv.equals(annualReturnVo) && "N".equals(arv.getEffective()) && arv.getReturnYear() != null) {
						if (sdf.format(arv.getReturnYear()).equals(sdf.format(annualReturnVo.getReturnYear()))) {
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "返还年度重复", ""));
							return;
						}
					}
				}
			}
		}
		if ("add".equals(annualExcuteMethod)) {
			if (null != financialReturnVo && null == financialReturnVo.getAnnualReturnVos()) {
				financialReturnVo.setAnnualReturnVos(new ArrayList<AnnualReturnVo>());
			}
			if (null != financialReturnVo && null != financialReturnVo.getAnnualReturnVos()) {
				financialReturnVo.getAnnualReturnVos().add(annualReturnVo);
			}
		}
		RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
	}

	public AnnualReturnVo getAnnualReturnVo() {
		return annualReturnVo;
	}

	public void setAnnualReturnVo(AnnualReturnVo annualReturnVo) {
		this.annualReturnVo = annualReturnVo;
	}

	public FinancialReturnVo getFinancialReturnVo() {
		return financialReturnVo;
	}

	public void setFinancialReturnVo(FinancialReturnVo financialReturnVo) {
		this.financialReturnVo = financialReturnVo;
	}

	public LazyDataModel<FinancialReturnVo> getLazyFinancialReturnVoModel() {
		return lazyFinancialReturnVoModel;
	}

	public void setLazyFinancialReturnVoModel(LazyDataModel<FinancialReturnVo> lazyFinancialReturnVoModel) {
		this.lazyFinancialReturnVoModel = lazyFinancialReturnVoModel;
	}

	public String getExistItem() {
		return existItem;
	}

	public void setExistItem(String existItem) {
		this.existItem = existItem;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getEffective() {
		return effective;
	}

	public void setEffective(String effective) {
		this.effective = effective;
	}

	public String getFinancialExcuteMethod() {
		return financialExcuteMethod;
	}

	public void setFinancialExcuteMethod(String financialExcuteMethod) {
		this.financialExcuteMethod = financialExcuteMethod;
	}

	public String getAnnualExcuteMethod() {
		return annualExcuteMethod;
	}

	public void setAnnualExcuteMethod(String annualExcuteMethod) {
		this.annualExcuteMethod = annualExcuteMethod;
	}

	public long getCompanymstrId() {
		return companymstrId;
	}

	public void setCompanymstrId(long companymstrId) {
		this.companymstrId = companymstrId;
	}

}
