package com.wcs.tih.system.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.wcs.base.service.LoginService;
import com.wcs.tih.system.controller.vo.CommonFunctionVO;
import com.wcs.tih.system.service.CommonFunctionService;

@ManagedBean
@ViewScoped
public class CommonFunctionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB 
	private CommonFunctionService commonFunctionService;
	@EJB 
	private LoginService loginService;
	
	//index.xhtml
	private String queryName;
	private LazyDataModel<CommonFunctionVO> indexLazyModel;
	
	//picklist
	private DualListModel<String> pickListUsedVOs;  
	private List<CommonFunctionVO> target = null;
	private List<CommonFunctionVO> source = null;
	
	//重置
	public void reset(){
		queryName=null;
	}
	
	//查询
	@PostConstruct
	public void queryData(){
		
		indexLazyModel = new LazyDataModel<CommonFunctionVO>() {

			private static final long serialVersionUID = -906751666598951627L;
			private List<CommonFunctionVO> bbv=null;
			
			@Override
			public List<CommonFunctionVO> load(int first, int pageSize,
					String sortField, SortOrder sortOrder, Map<String, String> filters) {
				bbv=commonFunctionService.queryData(queryName);
				int size=bbv.size();
				this.setRowCount(size);
				if(size>pageSize){
					try{
						return bbv.subList(first, first+pageSize);
					}catch(IndexOutOfBoundsException e){
						return bbv.subList(first, first + (size % pageSize));
					}
				}else{
					return bbv;
				}
			}
			
			@Override
			public Object getRowKey(CommonFunctionVO object) {
				return object.getId();
			}
			
			@Override
			public CommonFunctionVO getRowData(String rowKey) {
				for(CommonFunctionVO cv:bbv){
					if (cv.getId() == Integer.parseInt(rowKey)) {
						return cv;
					}
				}
				return null;
			}
			
		};
	}
	

	
	//picklist查询
	public void queryPickList(){
        target = commonFunctionService.queryPickList("target");
        source = commonFunctionService.queryPickList("source");
        List<String> tempTarget = new ArrayList<String>();
        List<String> tempSource = new ArrayList<String>();
        //先查出的是target集合和source集合.然后循环遍历到temoTarget和tempSource中.
        //使用DualListModel<String>方式进行对picklist的赋值. 
        //值的唯一性,因为C表中的数据是根据R表定死的,所以也是唯一性的.
        for(CommonFunctionVO c : target) {
        	
        	tempTarget.add(c.getName());
        }
        for(CommonFunctionVO c : source) {
        	tempSource.add(c.getName());
        }
        
        pickListUsedVOs = new DualListModel<String>(tempSource, tempTarget); 
	}
	
	//pickList的同步数据库
	
	public void updatePickListData(){
		for(String str : pickListUsedVOs.getSource()) {
			boolean f = false;
			CommonFunctionVO cfvo = null;
			for(CommonFunctionVO c : source) {
				if(str.equals(c.getName())) {
					f = true;
					break;
				}
			}
			if(!f) {
				for(CommonFunctionVO c : target) {
					if(str.equals(c.getName())) {
						cfvo = c;
						break;
					}
				}
				source.add(cfvo);
				target.remove(cfvo);
			}
			commonFunctionService.updateLeft(str, 0,loginService.getCurrentUserName());
		}
		
		int i=0;
		for(String str1 : pickListUsedVOs.getTarget()) {
			++i;
			boolean f = false;
			CommonFunctionVO cfvo = null;
			for(CommonFunctionVO c : target) {
				if(str1.equals(c.getName())) {
					f = true;
					break;
				}
			}
			if(!f) {
				for(CommonFunctionVO c : source) {
					if(str1.equals(c.getName())) {
						cfvo = c;
						break;
					}
				}
				target.add(cfvo);
				source.remove(cfvo);
			}
			//在这里进行对优先级的赋值
			commonFunctionService.updateRight(str1, i,loginService.getCurrentUserName());
		}
		RequestContext.getCurrentInstance().addCallbackParam("modifyInfo", "yes");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("成功","设置'常用功能'成功，请查询并确认！"));
	}
	
	
	
//================构造方法和getting setting========
	public CommonFunctionBean(){ 
		pickListUsedVOs = new DualListModel<String>(new ArrayList<String>(0), new ArrayList<String>(0));
	}

	public LazyDataModel<CommonFunctionVO> getIndexLazyModel() {
		return indexLazyModel;
	}

	public void setIndexLazyModel(LazyDataModel<CommonFunctionVO> indexLazyModel) {
		this.indexLazyModel = indexLazyModel;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public DualListModel<String> getPickListUsedVOs() {
		return pickListUsedVOs;
	}

	public void setPickListUsedVOs(DualListModel<String> pickListUsedVOs) {
		this.pickListUsedVOs = pickListUsedVOs;
	}

	public List<CommonFunctionVO> getTarget() {
		return target;
	}

	public void setTarget(List<CommonFunctionVO> target) {
		this.target = target;
	}

	public List<CommonFunctionVO> getSource() {
		return source;
	}

	public void setSource(List<CommonFunctionVO> source) {
		this.source = source;
	}
}
