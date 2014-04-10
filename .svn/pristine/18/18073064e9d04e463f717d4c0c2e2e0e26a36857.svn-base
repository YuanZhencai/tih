package com.wcs.tih.system.controller;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.wcs.base.service.LoginService;
import com.wcs.tih.system.controller.vo.CommonLinkVO;
import com.wcs.tih.system.service.CommonLinkService;


@ManagedBean(name="commonLinkBean")
@ViewScoped
public class CommonLinkBean implements Serializable{

	private static final long serialVersionUID = 1328673767356250347L;
	
	@EJB 
	private CommonLinkService commonLinkService;
	@EJB 
	private LoginService loginService;
	
	//index.xhtml
	private String linkName;
	private String linkState;
	private LazyDataModel<CommonLinkVO> indexLazyModel;
	
	//update_dialog.xhtml
	//WebSphere 8 下,在用这个VO的时候,必须new一个示例出来,
	//虽然浪费,但不new会报错,update_dialog中的h:selectOneMenu组件的获取值会出错.
	private CommonLinkVO selectData=new CommonLinkVO();
	
	//insert_dialog.xhtml
	private String addLinkName;
	private String addLinkUrl;
	private String addLinkPriority;
	private String addLinkdefunct="N";
	
	//重置
	public void reset(){
		linkName=null;
		linkState=null;
	}
	
	//查询
	@PostConstruct
	public void queryData(){
		indexLazyModel = new LazyDataModel<CommonLinkVO>() {
			private static final long serialVersionUID = 1L;
			private List<CommonLinkVO> bbv = null;
			@Override
			public List<CommonLinkVO> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				bbv=commonLinkService.searchData(linkName, linkState);
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
			public Object getRowKey(CommonLinkVO object) {
				return object.getId();
			}
			
			@Override
			public CommonLinkVO getRowData(String rowKey) {
				for (CommonLinkVO b : bbv) {
					if (b.getId() == Integer.parseInt(rowKey)) {
						return b;
					}
				}
				return null;
			}
			
		};
	}
	
	//添加
	public void insertData(){
		boolean haveError=this.validateLink(addLinkName, addLinkUrl, addLinkPriority);
		if(haveError){
			return;
		}
		commonLinkService.InsertData(addLinkName, addLinkUrl, addLinkPriority,addLinkdefunct,loginService.getCurrentUserName());
		RequestContext.getCurrentInstance().addCallbackParam("addInfo", "yes");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("添加链接'"+addLinkName+"'成功，请查询并确认！",""));
	}
	
	//更新
	public void modifyData(){
		String priority=((Integer)selectData.getPriority()).toString();
		boolean haveError=this.validateLink(selectData.getName(), selectData.getUrl(), priority);
		if(haveError){
			return;
		}
		 commonLinkService.ModifyData(selectData,loginService.getCurrentUserName());
		 RequestContext.getCurrentInstance().addCallbackParam("modifyInfo", "yes");
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("更新链接'"+selectData.getName()+"'成功，请查询并确认！",""));
	}
	
	private boolean validateLink(String vLinkName,String vLinkUrl,String vLinkPriority) {
		boolean haveError=false;
		ResourceBundle regexrb = ResourceBundle.getBundle("regex");
		if(null==vLinkName||("").equals(vLinkName.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"链接名称：","不允许为空。"));
			haveError=true;
		}else if(vLinkName.length()>15){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"链接名称：","长度不允许大于15个字符。"));
			haveError=true;
		}
		if(null==vLinkUrl||("").equals(vLinkUrl.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"链接地址：","不允许为空。"));
			haveError=true;
		}else if(vLinkUrl.length()>70){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"链接地址：","长度不允许大于70个字符。"));
			haveError=true;
		}else if(!vLinkUrl.matches(regexrb.getString("ONLYHTTP"))){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"链接地址：","必须是合法的URL地址。"));
			haveError=true;
		}
		if(null==vLinkPriority||("").equals(vLinkPriority.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"优先级数：","不允许为空。"));
			haveError=true;
		}
		return haveError;
	}
	
	public CommonLinkBean(){
	}
	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkState() {
		return linkState;
	}

	public void setLinkState(String linkState) {
		this.linkState = linkState;
	}

	public LazyDataModel<CommonLinkVO> getIndexLazyModel() {
		return indexLazyModel;
	}

	public void setIndexLazyModel(LazyDataModel<CommonLinkVO> indexLazyModel) {
		this.indexLazyModel = indexLazyModel;
	}
	
	public String getAddLinkName() {
		return addLinkName;
	}

	public void setAddLinkName(String addLinkName) {
		this.addLinkName = addLinkName;
	}

	public String getAddLinkUrl() {
		return addLinkUrl;
	}

	public void setAddLinkUrl(String addLinkUrl) {
		this.addLinkUrl = addLinkUrl;
	}

	public String getAddLinkPriority() {
		return addLinkPriority;
	}
	
	public String getAddLinkdefunct() {
		return addLinkdefunct;
	}

	public void setAddLinkdefunct(String addLinkdefunct) {
		this.addLinkdefunct = addLinkdefunct;
	}

	public void setAddLinkPriority(String addLinkPriority) {
		this.addLinkPriority = addLinkPriority;
	}

	public CommonLinkVO getSelectData() {
		return selectData;
	}

	public void setSelectData(CommonLinkVO selectData) {
		this.selectData = selectData;
	}

}

	
	
	