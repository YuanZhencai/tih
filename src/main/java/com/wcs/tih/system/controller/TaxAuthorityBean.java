package com.wcs.tih.system.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.tih.model.Taxauthority;
import com.wcs.tih.system.controller.vo.TaxAuthorityVO;
import com.wcs.tih.system.service.TaxAuthorityService;

@ManagedBean
@ViewScoped
public class TaxAuthorityBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @EJB 
    private TaxAuthorityService taxAuthorityService;
	@ManagedProperty(value = "#{currentUser}")
	private CurrentUserBean currentUser;

    private String queryName;
	private String queryAddress;
	private String queryState;
	private LazyDataModel<TaxAuthorityVO> lazyModelIndex;
	private List<TaxAuthorityVO> pageList=new ArrayList<TaxAuthorityVO>();
	
	private final String finalDropListStr=DictConsts.TIH_TAX_AUTHORITY_TYPE;
    private Taxauthority tax=new Taxauthority();

	public void reset(){
		queryName=null;
		queryAddress=null;
		queryState=null;
	}
	// 查询方法
	@PostConstruct
	public void queryPlan() {
	    pageList=taxAuthorityService.queryDataPlan(queryName,queryAddress, queryState);
	    
		lazyModelIndex = new LazyDataModel<TaxAuthorityVO>() {
			private static final long serialVersionUID = 5472450185636472922L;
			private List<TaxAuthorityVO> bbv = null;

			@Override
			public List<TaxAuthorityVO> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				bbv = taxAuthorityService.queryDataPlan(queryName,queryAddress, queryState);
				int size = bbv.size();
				this.setRowCount(size);
				if (size > pageSize) {
					try {
						return bbv.subList(first, first + pageSize);
					} catch (IndexOutOfBoundsException e) {
						return bbv.subList(first, first + (size % pageSize));
					}
				} else {
					return bbv;
				}
			}

			@Override
			public Object getRowKey(TaxAuthorityVO object) {
				return object.getTaxauthority().getId();
			}

			@Override
			public TaxAuthorityVO getRowData(String rowKey) {
				for (TaxAuthorityVO b : bbv) {
					if (b.getTaxauthority().getId() == Integer.parseInt(rowKey)) {
						return b;
					}
				}
				return null;
			}
		};
	}
	
	public void newAdd(){
	    this.tax=new Taxauthority();
	    tax.setDefunctInd("N");
	}
	
	// 添加方法
	public void addInfo() {
		boolean haveError=false;
		haveError=validatorTax();
		if(haveError){
			return;
		}
		String repeatNameBool=taxAuthorityService.repeatNameBool(this.tax.getName(),null);
		if(repeatNameBool.equals("norepeat")){
		    tax.setCreatedBy(currentUser.getCurrentUserName());
	        tax.setCreatedDatetime(new Date());
	        tax.setUpdatedBy(currentUser.getCurrentUserName());
	        tax.setUpdatedDatetime(new Date());
			taxAuthorityService.addTaxManage(tax);
			RequestContext.getCurrentInstance().addCallbackParam("addInfo", "yes");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("成功","添加'"+tax.getName()+"'成功，请查询并确认！"));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"机关名称已存在,请重新输入!",null));
		}
	}
	
	//update
	public void modifyData(){
		boolean haveError=false;
		haveError=validatorTax();
		if(haveError){
			return;
		}
		String repeatNameBool=taxAuthorityService.repeatNameBool(tax.getName(),((Long)tax.getId()).toString());
		if(repeatNameBool.equals("norepeat")){
		    this.tax.setUpdatedBy(currentUser.getCurrentUserName());
		    this.tax.setUpdatedDatetime(new Date());
			taxAuthorityService.modifyData(this.tax);
			RequestContext.getCurrentInstance().addCallbackParam("modifyInfo", "yes");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("成功","更新'"+tax.getName()+"'成功，请查询并确认！"));
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"机关名称已存在,请重新输入!",""));
		}
	}
	
	private boolean validatorTax() {
		ResourceBundle regexrb = ResourceBundle.getBundle("regex");
		boolean haveError=false;
		if(null==tax.getType()||("").equals(tax.getType())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"机关类型：","不允许为空。"));
			haveError=true;
		}
		if(null==tax.getName()||("").equals(tax.getName())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"机关名称：","不允许为空。"));
			haveError=true;
		}else if(tax.getName().length()>30){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"机关名称：","长度不允许大于30个字符。"));
			haveError=true;
		}
		if(null!=tax.getTelphone()&&!("").equals(tax.getTelphone())&&!tax.getTelphone().matches(regexrb.getString("PHONE"))){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"机关联系电话：","应为11位手机或7-8位电话.注:含区号分机号用'-'分开。"));
			haveError=true;
		}
		if(null!=tax.getZipcode()&&!("").equals(tax.getZipcode())&&!tax.getZipcode().matches(regexrb.getString("ZIPCODE"))){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"邮编号码：","格式不正确,请输入六位有效邮政编码。"));
			haveError=true;
		}
		if(null==tax.getAddress()||("").equals(tax.getAddress())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"详细地址：","不允许为空。"));
			haveError=true;
		}else if(tax.getAddress().length()>30){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"详细地址：","长度不允许大于30个字符。"));
			haveError=true;
		}
		if(null!=tax.getLeaderName()&&!("").equals(tax.getLeaderName())&&tax.getLeaderName().length()>6){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"负责人名：","长度不允许大于6个字符。"));
			haveError=true;
		}
		if(null!=tax.getLeaderPosition()&&!("").equals(tax.getLeaderPosition())&&tax.getLeaderPosition().length()>6){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"负责人职位名称：","长度不允许大于6个字符。"));
			haveError=true;
		}
		if(null!=tax.getLeaderTelphone()&&!("").equals(tax.getLeaderTelphone())&&!tax.getLeaderTelphone().matches(regexrb.getString("PHONE"))){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"负责人联系电话：","应为11位手机或7-8位电话.注:含区号分机号用'-'分开。"));
			haveError=true;
		}
		if(null!=tax.getContacterName()&&!("").equals(tax.getContacterName())&&tax.getContacterName().length()>6){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"联系人名：","长度不允许大于6个字符。"));
			haveError=true;
		}
		if(null!=tax.getContacterPosition()&&!("").equals(tax.getContacterPosition())&&tax.getContacterPosition().length()>6){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"联系人职位名称：","格长度不允许大于6个字符。"));
			haveError=true;
		}
		if(null!=tax.getContacterTelphone()&&!("").equals(tax.getContacterTelphone())&&!tax.getContacterTelphone().matches(regexrb.getString("PHONE"))){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"联系人联系电话：","应为11位手机或7-8位电话.注:含区号分机号用'-'分开。"));
			haveError=true;
		}
		return haveError;
	}
	
	// getting and setting
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public String getQueryAddress() {
		return queryAddress;
	}
	public void setQueryAddress(String queryAddress) {
		this.queryAddress = queryAddress;
	}
	public String getQueryState() {
		return queryState;
	}
	public void setQueryState(String queryState) {
		this.queryState = queryState;
	}
	public LazyDataModel<TaxAuthorityVO> getLazyModelIndex() {
		return lazyModelIndex;
	}
	public void setLazyModelIndex(
			LazyDataModel<TaxAuthorityVO> lazyModelIndex) {
		this.lazyModelIndex = lazyModelIndex;
	}
    public CurrentUserBean getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(CurrentUserBean currentUser) {
        this.currentUser = currentUser;
    }
    public Taxauthority getTax() {
        return tax;
    }
    public void setTax(Taxauthority tax) {
        this.tax = tax;
    }
    public String getFinalDropListStr() {
        return finalDropListStr;
    }
    public List<TaxAuthorityVO> getPageList() {
        return pageList;
    }
    public void setPageList(List<TaxAuthorityVO> pageList) {
        this.pageList = pageList;
    }
}
