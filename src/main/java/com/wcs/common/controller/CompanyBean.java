package com.wcs.common.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.helper.AreaHelper;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.CompanyVo;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.common.service.CompanyService;
import com.wcs.common.util.PageModel;
import com.wcs.tih.model.Taxauthority;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean
@ViewScoped
public class CompanyBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @EJB
    private CompanyService companyService;

    private CompanyManagerModel companyManagerModel = new CompanyManagerModel();
    private CompanyManagerModel companyInsertModel = new CompanyManagerModel();
    private CompanyManagerModel companySaveModel = new CompanyManagerModel();
    private LazyDataModel<CompanyManagerModel> lazyModel;
    private LazyDataModel<O> insertLazyModel;
    private Taxauthority selectTaxauthority;
    private boolean flag;
    private boolean secondBtn;
    private O selecto;
    private Taxauthority selectt;
    private List<SelectItem> regionItems;
    private List<SelectItem> provinceItems;
	private Map<String, Object> filter = new HashMap<String, Object>();
	private LazyDataModel<CompanyVo> companyVos = null;
	private CompanyVo companyVo = new CompanyVo();
	private CompanyVo selectedCompanyVo = null;

    
    @PostConstruct
    public void init() {
        regionItems = AreaHelper.getItemsByCatKey("0");
        provinceItems = new ArrayList<SelectItem>();
        companyVos = searchCompanysBy(new HashMap<String, Object>());
    }
    
    
    // ==========================================================Yuan=============================================//
    public void resetForm() {
    	filter.clear();
        provinceItems = new ArrayList<SelectItem>();
    }

    public void handleRegionChange(String catKey){
    	provinceItems = AreaHelper.getItemsByCatKey(catKey);
    }
    
    public void searchCompanys() {
    	companyVos = searchCompanysBy(filter);
    }
    
    public LazyDataModel<CompanyVo> searchCompanysBy(final Map<String, Object> filter) {
    	LazyDataModel<CompanyVo> companyVos = new LazyDataModel<CompanyVo>() {
			@Override
			public List<CompanyVo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				Integer rowCount = companyService.findCompanyCountBy(filter);
				setRowCount(rowCount);
				return companyService.findCompanysBy(filter, first, pageSize);
			}
		};
		return companyVos;
    }
    
    public void createCompany() {
    	companyVo = new CompanyVo();
    	companyVo.getCompany().setDefunctInd("N");
    }
    
    public void editCompany() {
    	companyVo = new CompanyVo();
    	try {
    		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
			BeanUtils.copyProperties(companyVo, selectedCompanyVo);
			provinceItems = AreaHelper.getItemsByCatKey(companyVo.getCompany().getRegion());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    }
    
    public void saveCompany() {
    	if(validateCompany(companyVo)) {
    		try {
    			selectedCompanyVo = new CompanyVo();
    			ConvertUtils.register(new DateConverter(null), java.util.Date.class);
				BeanUtils.copyProperties(selectedCompanyVo, companyVo);
				companyService.saveCompany(selectedCompanyVo);
				companyVos = searchCompanysBy(new HashMap<String, Object>());
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "操作成功", ""));
				JSFUtils.handleDialogByWidgetVar("saveCompanyDialogVar", "close");
			} catch (Exception e) {
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败", ""));
				logger.error(e.getMessage(), e);
			}
    	}
    	
    }
    
    public boolean validateCompany(CompanyVo companyVo) {
    	FacesContext context = FacesContext.getCurrentInstance();
    	Companymstr c = companyVo.getCompany();
    	O o = companyVo.getO();
    	boolean validate = true;
		if(!ValidateUtil.validateRequired(context , o.getStext(), "公司名称：")) {
			validate = false;
    	}
		if(!ValidateUtil.validateRequired(context , o.getBukrs(), "机构代码：")) {
			validate = false;
		}
		if(!ValidateUtil.validateRequired(context , c.getCode(), "公司代码：")) {
			validate = false;
		}
		if(!ValidateUtil.validateRequired(context , c.getAddress(), "公司地址：")) {
			validate = false;
		}
		if(!ValidateUtil.validateRequired(context , c.getRegion(), "地域：")) {
			validate = false;
		}
		if(!ValidateUtil.validateRequired(context , c.getProvince(), "省：")) {
			validate = false;
		}
		return validate;
    }

    // ==========================================================Yuan=============================================//
    
    private CompanyManagerModel selectModel;
    public CompanyManagerModel getSelectModel() {
        return selectModel;
    }

    public void setSelectModel(CompanyManagerModel selectModel) {
        this.selectModel = selectModel;
    }

    private CompanyManagerModel insertSelect;
    
    public void searchInsert() {
        List<O> bbv = companyService.getInsertCompanyModel(companyInsertModel);
        insertLazyModel = new PageModel(bbv, false);
    }
    
    
    public void invokeMethod(String controllerName,String methodName){
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null,controllerName);
            o.getClass().getDeclaredMethod(methodName, long.class).invoke(o, new Object[] { this.selectModel.getId() });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void saveOrUpdateCompany() {
        Companymstr companymstr = new Companymstr();

        boolean flag = true;
        if (this.companySaveModel.getAddress() == null || this.companySaveModel.getAddress().trim().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "公司地址：", "不能为空。"));
            flag = false;
        }

        if (this.companySaveModel.getAddress().getBytes().length > 500) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "公司地址：", "不能超过500个字符!"));
            flag = false;
        }

        if (this.companySaveModel.getTelphone() != null && !this.companySaveModel.getTelphone().trim().equals("")) {
            Pattern pattern = Pattern.compile("^\\d{2,4}\\-\\d{3,8}$|^\\d{3,8}");
            if (!pattern.matcher(this.companySaveModel.getTelphone()).matches()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "电话号码：", "不合法。"));
                flag = false;
            }
        }

        String region = this.companySaveModel.getRegion();
        if (region == null || "".equals(region.trim())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "地域：", "不能为空。"));
            flag = false;
        }
        String province = this.companySaveModel.getProvince();
        if (province == null || "".equals(province.trim())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "省：", "不能为空。"));
            flag = false;
        }

        String code = this.companySaveModel.getCode();
        if (code != null && !"".equals(code.trim())) {
            if (code.length() > 50) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "公司代码：", "不能超过50个字符"));
                flag = false;
            } else {
                Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
                if ((p.matcher(code)).find()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "公司代码：", "不允许有中文"));
                    flag = false;
                } else {
                    if (companyService.isExistedCompanyCode(companySaveModel)) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_ERROR, "对不起该公司代码已经添加过，不能再添加！", ""));
                        flag = false;
                    }
                }
            }
        }
        // fix bug 12797
        if (!this.flag) {
            // add
            if (this.companyService.exists(this.selecto)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "对不起该公司已经添加过，不能再添加！", ""));
                flag = false;
            }
            companymstr.setOid(this.selecto.getId());
        } else {
            // update
            companymstr.setId(this.companySaveModel.getId());
        }
        if (!flag) {
            search();
            return;
        }

        companymstr.setStartDatetime(this.companySaveModel.getStartDatetime());
        companymstr.setStepDatetime(this.companySaveModel.getStepDatetime());
        companymstr.setAddress(this.companySaveModel.getAddress());
        companymstr.setDefunctInd(this.companySaveModel.getDefuctInt());
        companymstr.setType(this.companySaveModel.getType());
        companymstr.setDesc(this.companySaveModel.getDesc());
        companymstr.setZipcode(this.companySaveModel.getZipcode());
        companymstr.setTelphone(this.companySaveModel.getTelphone());
        companymstr.setRegion(this.companySaveModel.getRegion());
        companymstr.setProvince(this.companySaveModel.getProvince());
        companymstr.setCode(this.companySaveModel.getCode());
        companymstr.setRepresentative(this.companySaveModel.getRepresentative());
        companymstr.setUpdatedDatetime(new Date());
        this.companyService.saveOrUpdate(companymstr, this.flag);
        search();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "操作成功", ""));
        RequestContext.getCurrentInstance().addCallbackParam("dataInfoSumbit", "yes");
    }


    public void updateLast(SelectEvent event) {
        this.lastBtn = false;
        this.flag2 = true;
    }

    public void update() {
        this.flag = true;
        this.flag2 = true;
        this.secondBtn = false;
        provinceItems = AreaHelper.getItemsByCatKey(companySaveModel.getRegion());
        
    }
    public void search() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        List<CompanyManagerModel> bbv = companyService.getCompanyManagerModel(companyManagerModel, request.getRequestURL().toString());
        lazyModel = new PageModel<CompanyManagerModel>(bbv, false);
    }

    public void setSelectt(Taxauthority selectt) {

        this.selectt = selectt;
    }

    public void oRowSelect(SelectEvent event) {

        firstBtnFlag = false;
        this.selecto = (O) event.getObject();
    }

    public void insert() {
        this.flag = false;
        this.flag2 = false;
        this.selecto = null;
        this.selectt = null;
        insertLazyModel = new PageModel(new ArrayList<O>(), false);
        this.firstBtnFlag = true;
        this.secondBtn = true;
        companySaveModel.setDefuctInt("N");
    }
    public CompanyManagerModel getCompanySaveModel() {
        return companySaveModel;
    }

    public void setCompanySaveModel(CompanyManagerModel companySaveModel) {
        this.companySaveModel = companySaveModel;
    }


    public CompanyManagerModel getInsertSelect() {
        return insertSelect;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private boolean lastBtn = true;

    public boolean isLastBtn() {
        return lastBtn;
    }

    public void setLastBtn(boolean lastBtn) {
        this.lastBtn = lastBtn;
    }

    private boolean firstBtnFlag;

    public boolean isFirstBtnFlag() {
        return firstBtnFlag;
    }

    public void setFirstBtnFlag(boolean firstBtnFlag) {
        this.firstBtnFlag = firstBtnFlag;
    }

    public void setInsertSelect(CompanyManagerModel insertSelect) {
        this.insertSelect = insertSelect;
    }

    public CompanyManagerModel getCompanyInsertModel() {
        return companyInsertModel;
    }

    public void setCompanyInsertModel(CompanyManagerModel companyInsertModel) {
        this.companyInsertModel = companyInsertModel;
    }

    public CompanyManagerModel getCompanyManagerModel() {
        return companyManagerModel;
    }

    public void setCompanyManagerModel(CompanyManagerModel companyManagerModel) {
        this.companyManagerModel = companyManagerModel;
    }

    public Taxauthority getSelectTaxauthority() {
        return selectTaxauthority;
    }

    public void setSelectTaxauthority(Taxauthority selectTaxauthority) {
        this.selectTaxauthority = selectTaxauthority;
    }

    public LazyDataModel<O> getInsertLazyModel() {
        return insertLazyModel;
    }

    public void setInsertLazyModel(LazyDataModel<O> insertLazyModel) {
        this.insertLazyModel = insertLazyModel;
    }

    public void setLazyModel(LazyDataModel<CompanyManagerModel> lazyModel) {
        this.lazyModel = lazyModel;
    }



    public boolean isSecondBtn() {
        return secondBtn;
    }

    public void setSecondBtn(boolean secondBtn) {
        this.secondBtn = secondBtn;
    }

    private boolean flag2;

    public boolean isFlag2() {
        return flag2;
    }

    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }

  
    private Companymstr insertOrUpdateCompany;

    public Companymstr getInsertOrUpdateCompany() {
        return insertOrUpdateCompany;
    }

    public void setInsertOrUpdateCompany(Companymstr insertOrUpdateCompany) {
        this.insertOrUpdateCompany = insertOrUpdateCompany;
    }

    public void setNameAndCode() {
        this.companySaveModel = new CompanyManagerModel();
        this.companySaveModel.setDefuctInt("N");
        this.companySaveModel.setStext(this.selecto.getStext());
        this.companySaveModel.setJgCode(this.selecto.getBukrs());
        provinceItems = new ArrayList<SelectItem>();
    }

    public LazyDataModel<CompanyManagerModel> getLazyModel() {
        return lazyModel;
    }

    
    public void resetInsertForm() {
        this.companyInsertModel = new CompanyManagerModel();
    }


    public void findJgName() {
        this.secondBtn = false;
        this.companySaveModel.setJgName(this.selectt.getName());
    }

    public void searchTax() {
        this.selectt = this.companyService.selectTax(companySaveModel.getJgName());
    }

    public Taxauthority getSelectt() {
        return selectt;
    }

	public List<SelectItem> getRegionItems() {
		return regionItems;
	}

	public void setRegionItems(List<SelectItem> regionItems) {
		this.regionItems = regionItems;
	}

	public List<SelectItem> getProvinceItems() {
		return provinceItems;
	}

	public void setProvinceItems(List<SelectItem> provinceItems) {
		this.provinceItems = provinceItems;
	}


	public Map<String, Object> getFilter() {
		return filter;
	}


	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}


	public LazyDataModel<CompanyVo> getCompanyVos() {
		return companyVos;
	}


	public void setCompanyVos(LazyDataModel<CompanyVo> companyVos) {
		this.companyVos = companyVos;
	}


	public CompanyVo getSelectedCompanyVo() {
		return selectedCompanyVo;
	}


	public void setSelectedCompanyVo(CompanyVo selectedCompanyVo) {
		this.selectedCompanyVo = selectedCompanyVo;
	}


	public CompanyVo getCompanyVo() {
		return companyVo;
	}


	public void setCompanyVo(CompanyVo companyVo) {
		this.companyVo = companyVo;
	}
  
}
