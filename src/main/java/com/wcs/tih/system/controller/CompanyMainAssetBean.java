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

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.model.CompanyDepreciation;
import com.wcs.tih.model.CompanyMainAsset;
import com.wcs.tih.system.controller.vo.CompanyDepreciationVO;
import com.wcs.tih.system.controller.vo.CompanyMainAssetVO;
import com.wcs.tih.system.service.CompanyMainAssetService;
import com.wcs.tih.util.ValidateUtil;
/** 
* <p>Project: tih</p> 
* <p>Title: CompanyMainAssetBean.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@ManagedBean
@ViewScoped
public class CompanyMainAssetBean implements Serializable {

	private static final String TWO_DECIMAL = "^-?[0-9]+([.][0-9]{1,2})?$";
	private static final String TWO_DECIMAL_MSG = "填写格式只能为整数或保留2位小数";
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private CompanyMainAssetService mainAssetService;
	// 查询Map
	private Map<String, String> query;
	// 当前操作的公司id
	private Long companyId;
	// 要资产
	private LazyDataModel<CompanyMainAssetVO> lazyAssets;
	private CompanyMainAssetVO currentAsset;

	// 折旧或摊销
	private LazyDataModel<CompanyDepreciationVO> lazyDepres;
	private CompanyDepreciationVO depre;
	private List<CompanyDepreciationVO> depreList;

	// 操作模式
	private String model = "";

	@PostConstruct
	public void initBean() {
		setQuery(new HashMap<String, String>(2));

		depreList = new ArrayList<CompanyDepreciationVO>();
		initAddAsset();
		initAddDepre();
	}

	public void clearAssertInfo() {
		query.clear();
		query.put("defunct", "N");
		lazyAssets = null;
		search();
	}

	public void search() {
		// 测试用
		lazyAssets = new LazyDataModel<CompanyMainAssetVO>() {
			public List<CompanyMainAssetVO> load(int arg0, int arg1, String arg2, SortOrder arg3, Map<String, String> arg4) {
				List<CompanyMainAssetVO> list = mainAssetService.search(query, companyId);
				setRowCount(list.size());
				if (getRowCount() > arg1) {
					try {
						return list.subList(arg0, arg0 + arg1);
					} catch (IndexOutOfBoundsException e) {
						return list.subList(arg0, arg0 + (getRowCount() % arg1));
					}
				} else {
					return list;
				}
			}
		};
	}

	/**
	 * <p>
	 * Description: 新增资产信息初始化
	 * </p>
	 */
	public void initAddAsset() {
		CompanyMainAsset ma = new CompanyMainAsset();
		ma.setDefunctInd("N");
		currentAsset = new CompanyMainAssetVO(ma);

		depreList.clear();
		lazyDepres = null;
	}

	/**
	 * <p>
	 * Description: 查找出折旧信息
	 * </p>
	 */
	public void searchDepres() {
		if (currentAsset == null) {
			return;
		}
		depreList.clear();
		List<CompanyDepreciationVO> tmp = mainAssetService.getDepres(currentAsset.getAsset().getId());
		for (CompanyDepreciationVO t : tmp) {
			depreList.add(t);
		}
		lazyDepres = new LazyDataModel<CompanyDepreciationVO>() {
			public List<CompanyDepreciationVO> load(int arg0, int arg1, String arg2, SortOrder arg3, Map<String, String> arg4) {
				List<CompanyDepreciationVO> list = depreList;
				setRowCount(list.size());
				if (getRowCount() > arg1) {
					try {
						return list.subList(arg0, arg0 + arg1);
					} catch (IndexOutOfBoundsException e) {
						return list.subList(arg0, arg0 + (getRowCount() % arg1));
					}
				} else {
					return list;
				}
			}
		};
	}

	/**
	 * <p>
	 * Description: 保存资产信息
	 * </p>
	 */
	public void editAssert() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateRequired(context, currentAsset.getAsset().getItem(), "项目：")
				& (ValidateUtil.validateRequiredAndRegex(context, currentAsset.getAsset().getDepreciationTimes(), "折旧或摊销年限：",
						"^[0-9]+([.][0-9]{1,2})?$", TWO_DECIMAL_MSG) && ValidateUtil.validateNumericMorethan(context, currentAsset.getAsset()
						.getDepreciationTimes(), "折旧或摊销年限：", 0d))
				& ValidateUtil.validateRegex(context, currentAsset.getAsset().getAccount(), "项目数量：", "^[0-9]+([.][0-9]{1,2})?$", TWO_DECIMAL_MSG) & (ValidateUtil
				.validateMaxlength(context, currentAsset.getAsset().getUnit(), "数量单位：", 30) && ValidateUtil.validateRequired(context, currentAsset
				.getAsset().getUnit(), "数量单位")))) {
			return;
		}
		if (currentAsset == null) {
			return;
		}

		try {
			mainAssetService.saveAssert(companyId, currentAsset, depreList);
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
			return;
		}
		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "添加资产信息：", "操作成功。"));
	}

	/**
	 * <p>
	 * Description: 新增折旧信息初始化
	 * </p>
	 */
	public void initAddDepre() {
		CompanyDepreciation cd = new CompanyDepreciation();
		cd.setDefunctInd("N");
		depre = new CompanyDepreciationVO(cd);
	}

	/**
	 * <p>
	 * Description: 添加折旧信息
	 * </p>
	 */
	public void addDepre() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateRequiredAndRegex(context, depre.getDepre().getCost(), "原值：", TWO_DECIMAL, TWO_DECIMAL_MSG)
				& ValidateUtil.validateRequiredAndRegex(context, depre.getDepre().getNetWorth(), "净值：", TWO_DECIMAL, TWO_DECIMAL_MSG) & ValidateUtil
					.validateRequired(context, depre.getDepre().getYear(), "年度："))) {
			return;
		}

		depreList.add(depre);
		lazyDepres = new PageModel<CompanyDepreciationVO>(depreList, false);
		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "添加折旧信息：", "操作成功。"));
	}

	public void initEditDepre() {

	}

	public void editDepre() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (!(ValidateUtil.validateRequiredAndRegex(context, depre.getDepre().getCost(), "原值：", TWO_DECIMAL, TWO_DECIMAL_MSG)
				& ValidateUtil.validateRequiredAndRegex(context, depre.getDepre().getNetWorth(), "净值：", TWO_DECIMAL, TWO_DECIMAL_MSG) & ValidateUtil
					.validateRequired(context, depre.getDepre().getYear(), "年度："))) {
			return;
		}

		RequestContext.getCurrentInstance().addCallbackParam("option", "success");
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改折旧信息：", "操作成功。"));
	}

	// Getter & Setter
	public Map<String, String> getQuery() {
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	public LazyDataModel<CompanyMainAssetVO> getLazyAssets() {
		return lazyAssets;
	}

	public void setLazyAssets(LazyDataModel<CompanyMainAssetVO> lazyAssets) {
		this.lazyAssets = lazyAssets;
	}

	public CompanyMainAssetVO getCurrentAsset() {
		return currentAsset;
	}

	public void setCurrentAsset(CompanyMainAssetVO currentAsset) {
		this.currentAsset = currentAsset;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public LazyDataModel<CompanyDepreciationVO> getLazyDepres() {
		return lazyDepres;
	}

	public void setLazyDepres(LazyDataModel<CompanyDepreciationVO> lazyDepres) {
		this.lazyDepres = lazyDepres;
	}

	public CompanyDepreciationVO getDepre() {
		return depre;
	}

	public void setDepre(CompanyDepreciationVO depre) {
		this.depre = depre;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

}
