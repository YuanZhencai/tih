package com.wcs.common.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.model.Dict;
import com.wcs.common.service.DictService;

@ManagedBean(name="dictBean")
@ViewScoped
public class DictBean implements Serializable{

	private static final String NOT_NULL = "不允许为空。";
    private static final long serialVersionUID = 7687928301071245573L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB 
	private DictService dictService;
	@ManagedProperty(value = "#{currentUser}")
	private CurrentUserBean currentUser;
	@ManagedProperty(value = "#{commonBean}")
	private CommonBean commonBean;
	
	//index页面参数
	private Map<String, String> query = new HashMap<String, String>(5);
	private LazyDataModel<DictVo> indexLazyModel;
	
	//insertdialog页面参数
	private Dict addDict;
	private String dictSeqNo;  //因为model的SeqNO为long类型,无法在首次进入为NULL.
	private String sysInd="N";
	private String defunctInd="N";
	
	public void initAddDict() {
        addDict=new Dict();
    }
 
	//updatedialog页面参数
	private DictVo selectData=new DictVo();
	
	public DictBean(){
	}
	
	public void reset(){
		query.clear();
	}
	
	//查询 String codeCat,String codeKey,String codeVal,String sysInd, String lang
	@PostConstruct
	public void serachData(){
		//query.get("codeCat"),query.get("codeKey"),query.get("codeVal"),query.get("sysInd"),query.get("lang")
		indexLazyModel = new LazyDataModel<DictVo>() {
			private static final long serialVersionUID = 8692533692951707240L;
			private List<DictVo> bbv = null;
			@Override
			public List<DictVo> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, String> filters) {
				bbv=dictService.searchData(query.get("codeCat"),query.get("codeKey"),query.get("codeVal"),query.get("sysInd"),query.get("lang"));
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
			public Object getRowKey(DictVo object) {
				return object.getId();
			}
			@Override
			public DictVo getRowData(String rowKey) {
				for (DictVo b : bbv) {
					if (b.getId() == Integer.parseInt(rowKey)) {
						return b;
					}
				}
				return null;
			}
			
		}; //end lazyModel
	}

	
	//添加
	public void insertData(){
		boolean haveError=false;
		haveError=validator(addDict.getCodeCat(),addDict.getCodeKey(),addDict.getCodeVal(),addDict.getLang(),dictSeqNo);
		if(haveError){
			return;
		}
		//注意SeqNO必须为NUM.在前台进行验证.
		if(dictSeqNo.trim().equals("") || dictSeqNo == null){
			addDict.setSeqNo(0);
		}else{
			addDict.setSeqNo(Long.parseLong(dictSeqNo));
		}
		addDict.setCreatedBy(currentUser.getCurrentUserName());
		addDict.setCreatedDatetime(new Date());
		addDict.setUpdatedBy(currentUser.getCurrentUserName());
		addDict.setUpdatedDatetime(new Date());
		//执行添加方法
		addDict.setDefunctInd(defunctInd);
		addDict.setSysInd(sysInd);
		String result=dictService.checkRepeatDataWithAdd(addDict.getCodeCat(),addDict.getCodeKey(),null);
		if(result.equals("repeat")){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"数据重复,提示:字典类别和键的组合数据不能重复.",""));
		}
		else{
		dictService.insertData(addDict);
		RequestContext.getCurrentInstance().addCallbackParam("addInfo","yes");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("成功","添加字典表信息:'"+addDict.getCodeVal()+"'成功，请查询并确认！"));
		}
		dictSeqNo="";
	}
	
	//编辑
	public void updateData(){
		boolean haveError=false;
		haveError=validator(selectData.getCodeCat(),selectData.getCodeKey(),selectData.getCodeVal(),selectData.getLang(),selectData.getSeqNo());
		if(haveError){
			return;
		}
		String strId=((Long)selectData.getId()).toString();
		String result=dictService.checkRepeatDataWithAdd(selectData.getCodeCat(),selectData.getCodeKey(),strId);
		if(result.equals("repeat")){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"数据重复:","字典类别和键的组合数据不能重复。"));
		}
		else{
		dictService.updateData(selectData, currentUser.getCurrentUserName());
		RequestContext.getCurrentInstance().addCallbackParam("updateInfo", "yes");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("成功","更新字典表信息:'"+selectData.getCodeVal()+"'成功，请查询并确认！"));
		}
	}
	
	//刷新字典表数据
	public void refreshData(){
		try{
			commonBean.refreshDictData();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("刷新字典表信息成功!"));
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"刷新字典表信息失败!",""));
			logger.error(e.getMessage(), e);
		}
	}
	
	public boolean validator(String codeCat,String codeKey,String codeVal,String lang,String seqNo){
		ResourceBundle regexrb = ResourceBundle.getBundle("regex");
		boolean haveError=false;
		if(null==codeCat || ("").equals(codeCat.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"字典类别：",NOT_NULL));
			haveError=true;
		}
		if(null==codeKey || ("").equals(codeKey.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"键：",NOT_NULL));
			haveError=true;
		}
		if(null==codeVal || ("").equals(codeVal.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"值：",NOT_NULL));
			haveError=true;
		}
		if(null==lang || ("").equals(lang.trim())){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"字典语言：",NOT_NULL));
			haveError=true;
		}
		if(null!=seqNo && !("").equals(seqNo.trim()) && !seqNo.matches(regexrb.getString("ONLYNUM")) ){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"字典顺序：","只允许输入数字。"));
			haveError=true;
		}
		
		return haveError;
	}
	
	//get and set
	public Dict getAddDict() {
		return addDict;
	}

	public void setAddDict(Dict addDict) {
		this.addDict = addDict;
	}

	public LazyDataModel<DictVo> getIndexLazyModel() {
		return indexLazyModel;
	}

	public void setIndexLazyModel(LazyDataModel<DictVo> indexLazyModel) {
		this.indexLazyModel = indexLazyModel;
	}

	public Map<String, String> getQuery() {
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}

	public String getDictSeqNo() {
		return dictSeqNo;
	}

	public void setDictSeqNo(String dictSeqNo) {
		this.dictSeqNo = dictSeqNo;
	}

	public DictVo getSelectData() {
		return selectData;
	}

	public void setSelectData(DictVo selectData) {
		this.selectData = selectData;
	}

	public String getSysInd() {
		return sysInd;
	}

	public void setSysInd(String sysInd) {
		this.sysInd = sysInd;
	}
	public String getDefunctInd() {
		return defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}
	public CurrentUserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CurrentUserBean currentUser) {
		this.currentUser = currentUser;
	}

	public CommonBean getCommonBean() {
		return commonBean;
	}

	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}
}
