package com.wcs.tih.system.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.CompanyStockStructure;
import com.wcs.tih.system.service.CompanyStockService;

@ManagedBean
@ViewScoped
public class CompanyStockBean {
	private static Logger logger = LoggerFactory.getLogger(CompanyStockBean.class);

	private static final String NOT_NULL = "不允许为空。";
	private static final String DATA_INFO_SUMBIT = "dataInfoSumbit";

	public class CompanyStockBeanComparator implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			CompanyStockStructure c = (CompanyStockStructure) o1;
			CompanyStockStructure c2 = (CompanyStockStructure) o2;
			return c.getRegisteredCapital().compareTo(c2.getRegisteredCapital());
		}

	}

	@EJB
	private CompanyStockService companyStockService;
	@EJB
	private CommonService commonservice;
	@EJB
	private LoginService loginservice;

	private Map<String, Object> searchMap = new HashMap<String, Object>();
	private CompanyStockStructure savemodel = new CompanyStockStructure();

	private List<CompanyStockStructure> insertList;
	private List list = new ArrayList();
	private boolean step;
	private Date insertDate;
	private Companymstr companymstr = new Companymstr();

	private boolean insertDatetime = true;

	public boolean isInsertDatetime() {
		return insertDatetime;
	}

	private boolean insert;

	public void changeInsert() {
		this.savemodel = new CompanyStockStructure();
		this.savemodel.setDefunctInd("N");
		this.insert = true;
		this.insertList = new ArrayList<CompanyStockStructure>();
		this.insertDate = null;
		this.realUpdate = false;
		insertDatetime = false;

		if (this.list.size() > 0) {
			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
		} else {
			this.insertDatetime = false;

			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "no");
		}
	}

	public void changeAgainInsert() {
		this.savemodel = new CompanyStockStructure();
		this.savemodel.setDefunctInd("N");
		this.insert = true;
		if (this.insertDate != null) {
			savemodel.setStatisticsDatetime(this.insertDate);
		}
	}

	public void changeUpdate(long id) {
		this.insert = false;
		savemodel = this.companyStockService.getCompanyStockStructureById(id);
		this.realUpdate = true;
	}

	public void changeListUpdate() {
		this.insert = false;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public void init(long id) {
		this.list = new ArrayList();
		searchMap = new HashMap<String, Object>();
		searchMap.put("defunctInd", "N");
		this.companymstr = this.companyStockService.getComapnyById(id);
		realUpdate = false;
		this.search();
	}

	public void search() {
		this.list = this.companyStockService.searchStockNew(this.companymstr.getId(), searchMap, null);
	}

	private String searchStr;
	private Comparator<? super CompanyStockStructure> companyStockBeanComparator = new CompanyStockBeanComparator();

	public String getSearchStr() {
		return searchStr;
	}

	public String formatStockDatetime(Date d) {
		if (d == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	public void insertOrUpdate() {
		boolean flag = false;
		if (this.savemodel.getShareholder() == null || this.savemodel.getShareholder().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "股东名称：", NOT_NULL));
			flag = true;
		}
		if (this.savemodel.getType() == null || this.savemodel.getType().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "股东类型：", NOT_NULL));
			flag = true;
		}
		if (this.savemodel.getRegisteredCapital() == null || this.savemodel.getRegisteredCapital().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "注册资金：", NOT_NULL));
			flag = true;
		}
		if (this.savemodel.getCurrency() == null || this.savemodel.getCurrency().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "币种：", NOT_NULL));
			flag = true;
		}

		if (this.savemodel.getStatisticsDatetime() == null || this.savemodel.getStatisticsDatetime().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计时间：", NOT_NULL));
			flag = true;
		}

		double sumCount = 0;
		this.savemodel.setCreatedBy(this.loginservice.getCurrentUsermstr().getAdAccount());
		this.savemodel.setCreatedDatetime(new Date());
		this.savemodel.setUpdatedBy(this.loginservice.getCurrentUsermstr().getAdAccount());
		this.savemodel.setUpdatedDatetime(new Date());
		if (!flag) {
			if (!realUpdate) {
				if (this.insert) {
					for (int i = 0; i < insertList.size(); i++) {
						sumCount += insertList.get(i).getRegisteredCapital().doubleValue();
					}
					sumCount += this.savemodel.getRegisteredCapital().doubleValue();
					this.savemodel.setCompanymstr(this.companymstr);
					this.insertList.add(savemodel);
					this.searchStr = this.getInsertCount(insertList);
				} else {
					this.savemodel.setUpdatedBy(this.loginservice.getCurrentUsermstr().getAdAccount());
					this.savemodel.setUpdatedDatetime(new Date());
					for (int i = 0; i < insertList.size(); i++) {
						CompanyStockStructure c = (CompanyStockStructure) insertList.get(i);
						if (c.getShareholder().equals(this.savemodel.getShareholder())) {
							insertList.remove(i);
							insertList.add(this.savemodel);
							break;
						}
					}
				}
				this.searchStr = this.getInsertCount(insertList);
				Collections.sort(insertList, this.companyStockBeanComparator);
			}
			if (realUpdate) {
//				if(companyStockService.isExistedStock(savemodel)){
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "投资人重复：" + savemodel.getShareholder(), NOT_NULL));
//				} else {
					this.companyStockService.update(savemodel);
//				}
				this.search();
				realUpdate = false;
			}

			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
		}
	}

	public void addErroMessage() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计时间：", "不能为空。"));
	}

	private boolean realUpdate;

	public boolean isRealUpdate() {
		return realUpdate;
	}

	public void realInsertUpdate() {
		HashMap<String, CompanyStockStructure> h = new HashMap<String, CompanyStockStructure>();

		for (int i = 0; i < this.insertList.size(); i++) {
			CompanyStockStructure stockStructure = insertList.get(i);
			if ("N".equals(stockStructure.getDefunctInd())) {
				if (h.get(stockStructure.getShareholder()) == null) {
					h.put(stockStructure.getShareholder(), stockStructure);
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "投资人重复：" + h.get(stockStructure.getShareholder()).getShareholder(), ""));
					return;
				}
			}
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
		RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
		this.companyStockService.insertOrUpdate(this.companymstr.getId(), this.insertList);
		this.search();
	}

	public String getInsertCount(List<CompanyStockStructure> c) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		for (int i = 0; i < c.size(); i++) {

			if (m.get(c.get(i).getCurrency()) == null) {
				m.put(c.get(i).getCurrency(), c.get(i).getRegisteredCapital().intValue());
			} else {
				m.put(c.get(i).getCurrency(), m.get(c.get(i).getCurrency()) + c.get(i).getRegisteredCapital().intValue());
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("总注册资本：");
		for (String a : m.keySet()) {
			sb.append(m.get(a)).append("   ").append(this.commonservice.getValueByDictCatKey(a, "zh_CN")).append("+");
		}
		String str = sb.toString();
		return str.substring(0, str.length() - 1);
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insert = false;
		this.insertDatetime = true;
		this.step = false;
		this.insertDate = insertDate;
		this.insertList = this.companyStockService.searchStockNew(this.companymstr.getId(), new HashMap<String, Object>(), new SimpleDateFormat(
				"yyyy-MM-dd").format(insertDate));
		Collections.sort(insertList, this.companyStockBeanComparator);
		this.searchStr = this.getInsertCount(insertList);
	}

	public void resetForm() {
		this.searchMap.clear();
		searchMap.put("defunctInd", "N");
	}

	public String formatDouble(double ss) {
		DecimalFormat df = new DecimalFormat("0.00");
		df.setMinimumFractionDigits(0);
		return df.format(ss);
	}

	public String getCompanyStockStructureByDate(Date date) {
		if (date == null || date.equals("")) {
			return "";
		}
		return this.companyStockService.getCompanyStockStructureByDate(date, this.companymstr.getId());
	}

	public void insertTrue() {
		this.insertDate = this.companyStockService.getLastStock(this.companymstr.getId());
		this.insertList = this.companyStockService.searchStockNew(this.companymstr.getId(), new HashMap<String, Object>(), new SimpleDateFormat(
				"yyyy-MM-dd").format(insertDate));
		this.searchStr = this.getInsertCount(insertList);
		this.insertDatetime = true;
		Collections.sort(insertList, this.companyStockBeanComparator);
	}

	public List<CompanyStockStructure> getInsertList() {
		return insertList;
	}

	public void setInsertList(List<CompanyStockStructure> insertList) {
		this.insertList = insertList;
	}

	public CompanyStockStructure getSavemodel() {
		return savemodel;
	}

	public void setSavemodel(CompanyStockStructure savemodel) {
		this.savemodel = savemodel;
	}

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public boolean isInsert() {
		return insert;
	}

	public boolean isStep() {
		return step;
	}

	public void setStep(boolean step) {
		this.step = step;
	}

}
