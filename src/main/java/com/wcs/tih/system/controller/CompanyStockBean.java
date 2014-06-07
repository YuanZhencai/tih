package com.wcs.tih.system.controller;

import java.math.BigDecimal;
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
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.CompanyStockStructure;
import com.wcs.tih.system.controller.vo.StockVo;
import com.wcs.tih.system.service.CompanyStockService;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean
@ViewScoped
public class CompanyStockBean {
	public class CompanyStockBeanComparator implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			CompanyStockStructure c = (CompanyStockStructure) o1;
			CompanyStockStructure c2 = (CompanyStockStructure) o2;
			return c.getRegisteredCapital().compareTo(c2.getRegisteredCapital());
		}

	}

	public class StockVoComparator implements Comparator {

		@Override
		public int compare(Object o1, Object o2) {
			StockVo c = (StockVo) o1;
			StockVo c2 = (StockVo) o2;
			String capital1 = c.getRegisteredCapital();
			String capital2 = c2.getRegisteredCapital();
			capital1 = capital1 == null ? "0" : capital1;
			capital2 = capital2 == null ? "0" : capital2;
			return new BigDecimal(capital1).compareTo(new BigDecimal(capital2));
		}

	}

	private static final String DATA_INFO_SUMBIT = "dataInfoSumbit";

	private static Logger logger = LoggerFactory.getLogger(CompanyStockBean.class);

	private static final String NOT_NULL = "不允许为空。";

	@EJB
	private CommonService commonservice;
	private Companymstr companymstr = new Companymstr();
	private Comparator<? super CompanyStockStructure> companyStockBeanComparator = new CompanyStockBeanComparator();

	@EJB
	private CompanyStockService companyStockService;
	private boolean insert;

	private Date insertDate;
	private boolean insertDatetime = true;
	private List<CompanyStockStructure> insertList;
	private List list = new ArrayList();
	@EJB
	private LoginService loginservice;

	private boolean realUpdate;
	private CompanyStockStructure savemodel = new CompanyStockStructure();

	private Map<String, Object> searchMap = new HashMap<String, Object>();

	private String searchStr;

	private boolean step;

	private List<StockVo> stockVos;
	private List<StockVo> selectedStockVos;

	private StockVo stockVo = new StockVo();
	private StockVo selectedStockVo = new StockVo();

	private String operate = null;

	private Date statisticsDate = null;

	// ===================================================Yuan====================================//

	public void searchStockVos() {
		stockVos = companyStockService.findStockVosBy(companymstr.getId(), searchMap);
	}

	public void initAddStocks() {
		if (stockVos.size() > 0) {
			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
		} else {
			selectedStockVos = new ArrayList<StockVo>();
			insertDatetime = false;
			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "no");
		}

	}

	public void addStocks() {
		selectedStockVos = new ArrayList<StockVo>();
		statisticsDate = null;
		insertDatetime = false;
	}

	public void updateStocks() {
		selectedStockVos = companyStockService.findStockVosByLatestDate(companymstr.getId());
		statisticsDate = selectedStockVos.get(0).getStatisticsDatetime();
		insertDatetime = true;
		getSummeryBy(selectedStockVos);
	}

	public void updateStocksByDate(Date date) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("statisticsDatetime", date);
		selectedStockVos = companyStockService.findStockVosBy(companymstr.getId(), filter );
		statisticsDate = selectedStockVos.get(0).getStatisticsDatetime();
		insertDatetime = true;
		getSummeryBy(selectedStockVos);
	}

	public void initAddStock() {
		stockVo = new StockVo();
		selectedStockVo = new StockVo();
		stockVo.setDefunctInd("N");
		operate = "addStockByDate";
		if (statisticsDate == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计时间：", "不能为空。"));
		} else {
			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
		}
	}

	public void editStockByDate() {
		try {
			stockVo = new StockVo();
			BeanUtils.copyProperties(stockVo, selectedStockVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		operate = "editStockByDate";
	}

	public void saveStockByDate() {
		stockVo.setStatisticsDatetime(statisticsDate);
		stockVo.setCompanymstr(companymstr);
		if (validate(stockVo)) {
			try {
				ConvertUtils.register(new DateConverter(null), java.util.Date.class);
				BeanUtils.copyProperties(selectedStockVo, stockVo);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if ("addStockByDate".equals(operate)) {
				selectedStockVos.add(stockVo);
			}
			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
			getSummeryBy(selectedStockVos);
		} else {
			return;
		}
	}

	public void saveStocks() {
		try {
			companyStockService.saveStocks(selectedStockVos);
			searchStockVos();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作失败。"));
			logger.error(e.getMessage(), e);
		}

	}

	public void editStock() {
		try {
			stockVo = new StockVo();
			BeanUtils.copyProperties(stockVo, selectedStockVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void saveStock() {
		stockVo.setCompanymstr(companymstr);
		if (validate(stockVo)) {
			try {
				BeanUtils.copyProperties(selectedStockVo, stockVo);
				companyStockService.saveStock(selectedStockVo);
				searchStockVos();
				RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作失败。"));
				logger.error(e.getMessage(), e);
			}
		} else {
			return;
		}
	}

	public boolean validate(StockVo s) {
		boolean validate = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if (!ValidateUtil.validateRequired(context, s.getShareholder(), "股东名称：")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequired(context, s.getType(), "股东类型：")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequiredAndRegex(context, s.getRegisteredCapital(), "注册资金：", "^-?[0-9]+([.][0-9]{1,2})?$", "只能为数值类型或小数点后两位")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequiredAndRegex(context, s.getRatio(), "比例：", "^-?[0-9]+([.][0-9]{1,2})?$", "只能为数值类型或小数点后两位")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequired(context, s.getCurrency(), "币种：")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequired(context, s.getStatisticsDatetime(), "统计时间：")) {
			validate = false;
		}

		if ("N".equals(s.getDefunctInd())) {
			if ("editStock".equals(operate)) {
				if (companyStockService.isExistedStock(s)) {
					validate = false;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "股东名称：", "已存在的股东名称。"));
				}
			} else {
				for (StockVo stock : selectedStockVos) {
					if (!stock.equals(selectedStockVo) && "N".equals(stock.getDefunctInd()) && stock.getShareholder().equals(s.getShareholder())) {
						validate = false;
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "股东名称：", "已存在的股东名称。"));
						break;
					}
				}
			}
		}
		return validate;

	}

	public void getSummeryBy(List<StockVo> stocks) {
		Map<String, Integer> m = new HashMap<String, Integer>();
		for (int i = 0; i < stocks.size(); i++) {

			StockVo s = stocks.get(i);
			if ("N".equals(s.getDefunctInd())) {
				if (m.get(s.getCurrency()) == null) {
					m.put(s.getCurrency(), Integer.valueOf((new BigDecimal(s.getRegisteredCapital()).intValue())));
				} else {
					m.put(s.getCurrency(), m.get(s.getCurrency()) + Integer.valueOf((new BigDecimal(s.getRegisteredCapital()).intValue())));
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		sb.append("总注册资本：");
		for (String a : m.keySet()) {
			sb.append(m.get(a)).append("   万").append(this.commonservice.getValueByDictCatKey(a, "zh_CN")).append("+");
		}
		String str = sb.toString();
		searchStr = str.substring(0, str.length() - 1);
	}

	// ===================================================Yuan====================================//
	public void addErroMessage() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计时间：", "不能为空。"));
	}

	public void changeAgainInsert() {
		this.savemodel = new CompanyStockStructure();
		this.savemodel.setDefunctInd("N");
		this.insert = true;
		if (this.insertDate != null) {
			savemodel.setStatisticsDatetime(this.insertDate);
		}
	}

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

	public void changeListUpdate() {
		this.insert = false;
	}

	public void changeUpdate(long id) {
		this.insert = false;
		savemodel = this.companyStockService.getCompanyStockStructureById(id);
		this.realUpdate = true;
	}

	public String formatDouble(String ratio) {
		if(ratio == null || "".equals(ratio)) {
			return null;
		}
		DecimalFormat df = new DecimalFormat("0.00");
		df.setMinimumFractionDigits(2);
		return df.format(new BigDecimal(ratio));
	}

	public String formatStockDatetime(Date d) {
		if (d == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}

	public String getCompanyStockStructureByDate(Date date) {
		if (date == null || date.equals("")) {
			return "";
		}
		return this.companyStockService.getCompanyStockStructureByDate(date, this.companymstr.getId());
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
			sb.append(m.get(a)).append("   万").append(this.commonservice.getValueByDictCatKey(a, "zh_CN")).append("+");
		}
		String str = sb.toString();
		return str.substring(0, str.length() - 1);
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public List<CompanyStockStructure> getInsertList() {
		return insertList;
	}

	public List getList() {
		return list;
	}

	public CompanyStockStructure getSavemodel() {
		return savemodel;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public String getSearchStr() {
		return searchStr;
	}

	public void init(long companymstrId) {
		searchMap = new HashMap<String, Object>();
		searchMap.put("defunctInd", "N");
		companymstr = this.companyStockService.findCompanymstrByCompanymstrId(companymstrId);
		searchStockVos();
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
				// if(companyStockService.isExistedStock(savemodel)){
				// FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "投资人重复：" + savemodel.getShareholder(), NOT_NULL));
				// } else {
				this.companyStockService.update(savemodel);
				// }
				this.search();
				realUpdate = false;
			}

			RequestContext.getCurrentInstance().addCallbackParam(DATA_INFO_SUMBIT, "yes");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "操作成功。"));
		}
	}

	public void insertTrue() {
		this.insertDate = this.companyStockService.getLastStock(this.companymstr.getId());
		this.insertList = this.companyStockService.searchStockNew(this.companymstr.getId(), new HashMap<String, Object>(), new SimpleDateFormat(
				"yyyy-MM-dd").format(insertDate));
		this.searchStr = this.getInsertCount(insertList);
		this.insertDatetime = true;
		Collections.sort(insertList, this.companyStockBeanComparator);
	}

	public boolean isInsert() {
		return insert;
	}

	public boolean isInsertDatetime() {
		return insertDatetime;
	}

	public boolean isRealUpdate() {
		return realUpdate;
	}

	public boolean isStep() {
		return step;
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

	public void resetForm() {
		this.searchMap.clear();
		searchMap.put("defunctInd", "N");
	}

	public void search() {
		this.list = this.companyStockService.searchStockNew(this.companymstr.getId(), searchMap, null);
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

	public void setInsertList(List<CompanyStockStructure> insertList) {
		this.insertList = insertList;
	}

	public void setList(List list) {
		this.list = list;
	}

	public void setSavemodel(CompanyStockStructure savemodel) {
		this.savemodel = savemodel;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public void setStep(boolean step) {
		this.step = step;
	}

	public List<StockVo> getStockVos() {
		return stockVos;
	}

	public void setStockVos(List<StockVo> stockVos) {
		this.stockVos = stockVos;
	}

	public List<StockVo> getSelectedStockVos() {
		return selectedStockVos;
	}

	public void setSelectedStockVos(List<StockVo> selectedStockVos) {
		this.selectedStockVos = selectedStockVos;
	}

	public StockVo getStockVo() {
		return stockVo;
	}

	public void setStockVo(StockVo stockVo) {
		this.stockVo = stockVo;
	}

	public StockVo getSelectedStockVo() {
		return selectedStockVo;
	}

	public void setSelectedStockVo(StockVo selectedStockVo) {
		this.selectedStockVo = selectedStockVo;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Date getStatisticsDate() {
		return statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

}
