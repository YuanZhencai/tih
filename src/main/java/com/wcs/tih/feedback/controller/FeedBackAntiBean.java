package com.wcs.tih.feedback.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.P;
import com.wcs.tih.document.service.DocumentService;
import com.wcs.tih.feedback.controller.vo.AntiHistoryVo;
import com.wcs.tih.feedback.controller.vo.SearchAntiVo;
import com.wcs.tih.feedback.service.FeedBackAntiService;
import com.wcs.tih.model.InvsAntiAvoidance;
import com.wcs.tih.model.InvsAntiAvoidanceHistory;
import com.wcs.tih.model.InvsAntiResult;
import com.wcs.tih.model.InvsAntiResultHistory;
import com.wcs.tih.report.controller.vo.AntiAvoidanceVo;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:wangxuan@wcs-global.com">XUAN</a>
 */
@ManagedBean(name="feedBackAntiBean")
@ViewScoped
public class FeedBackAntiBean implements Serializable{

	private static final String FALSE = "false";
    private static final String TRUE = "true";
    private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@EJB
	private FeedBackAntiService feedBackAntiService;
	@EJB
	private DocumentService documentService;
	
	private InvsAntiAvoidance antiAvoidance = new InvsAntiAvoidance();
	private List<InvsAntiResult> antiResults = new ArrayList<InvsAntiResult>();
	private List<String> taxTypes = new ArrayList<String>();
	private InvsAntiResult result;
	private SearchAntiVo antiAvoidancVo = new SearchAntiVo();
	private List<AntiAvoidanceVo> searchAntiTableVos;
	private List<AntiHistoryVo> historyVos;
	private AntiHistoryVo selectedAnti;
	private String operationType;
	private String lookOrEdit = TRUE;
	private String authority = FALSE;
	private Long id;
	private Long companyNameId;
	
	private static final String ANTI_POSITION_MANAGER = "SPE_ANT_TAX_ADM"; //反避税信息专项管理岗
	private static final String ANTI_POSITION_LOOK = "SPE_ANT_TAX_OBSV"; //反避税信息专项观察岗
	private boolean flag = false;
	
	@PostConstruct
	public void init(){
        searchAllAntiBy();
	}
	
	
	/**
	 * <p>Description:给反避税结果表添加一行</p>
	 */
	public void addAntiResult(){
		InvsAntiResult antiResult = new InvsAntiResult();
		antiResult.setCreatedDatetime(new Date());
		List<InvsAntiResult> list = antiResults;
		antiResults = new ArrayList<InvsAntiResult>();
		antiResults.add(antiResult);
		antiResults.addAll(list);
	}
	
	/**
	 * <p>Description:删除一行</p>
	 */
	public void deleteInspectionResult(){
		int j = 0;
		for(int i = 0; i < antiResults.size(); i++){
			if(antiResults.get(i).getCreatedDatetime() == result.getCreatedDatetime()){
				j = i;
			}
		}
		if(result != null){
		    feedBackAntiService.removeResult(result);
		}
		antiResults.remove(j);
	}
	
	public void onCancel(){
	    List<InvsAntiAvoidance> avoidance = feedBackAntiService.getInvsAntiAvoidance(id, "N");
	    if(avoidance.size() > 0){
	        revertAnti(avoidance.get(0).getWfId());
	    }
	    lookOrEdit = TRUE;
	}
	
	public void clearSearch(){
		antiAvoidancVo = new SearchAntiVo();
	}

    public void onEdit(RowEditEvent event) {
    }
    
    public void searchAllAntiBy(){
    	try {
            searchAntiTableVos = feedBackAntiService.getSearchForm(antiAvoidancVo,ANTI_POSITION_LOOK,ANTI_POSITION_MANAGER);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void specialEdit(){
    	lookOrEdit = FALSE;
    }
    
    public void onRowSelect(SelectEvent event) {
        taxTypes = new ArrayList<String>();
    	antiAvoidance = new InvsAntiAvoidance();
    	antiResults = new ArrayList<InvsAntiResult>();
    	if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(selectedAnti.getOperationType()) || DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(selectedAnti.getOperationType())){
    		antiAvoidance = feedBackAntiService.getInvsAntiAvoidance(selectedAnti.getId(), "").get(0);
    		if(antiAvoidance.getTaxTypes() != null && !"".equals(antiAvoidance.getTaxTypes().trim())){
    		    taxTypeToList(antiAvoidance.getTaxTypes());
    		}
    		antiResults = antiAvoidance.getInvsAntiResults();
    	}else{
    		InvsAntiAvoidanceHistory history = feedBackAntiService.getSpecialHistory(selectedAnti.getId());
    		List<InvsAntiResultHistory> resultHistoryList = history.getInvsAntiResultHistories();
    		antiByHistory(history);
			resultByHistoryResult(resultHistoryList);
			
    	}
    	lookOrEdit = TRUE;
    }

    private void resultByHistoryResult(List<InvsAntiResultHistory> resultHistoryList) {
        for(InvsAntiResultHistory resultHistory : resultHistoryList){
            if(!"Y".equals(resultHistory.getDefunctInd())){
                InvsAntiResult result = new InvsAntiResult();
                result.setVat(resultHistory.getVat());
                result.setCit(resultHistory.getCit());
                result.setAddInterest(resultHistory.getAddInterest());
                result.setAddFine(resultHistory.getAddFine());
                result.setReducedLoss(resultHistory.getReducedLoss());
                antiResults.add(result);
            }
        }
    }

    private void antiByHistory(InvsAntiAvoidanceHistory history) {
        antiAvoidance.setCompanyName(history.getCompanyName());
        antiAvoidance.setSponsorOrg(history.getSponsorOrg());
        antiAvoidance.setImplementOrg(history.getImplementOrg());
        antiAvoidance.setCause(history.getCause());
        antiAvoidance.setInvestType(history.getInvestType());
        antiAvoidance.setTaxTypes(history.getTaxTypes());
        //税种
        if(history.getTaxTypes() != null && !"".equals(history.getTaxTypes().trim())){
            taxTypeToList(history.getTaxTypes());
        }
        
        antiAvoidance.setInvestStartDatetime(history.getInvestStartDatetime());
        antiAvoidance.setInvestEndDatetime(history.getInvestEndDatetime());
        antiAvoidance.setMissionStartDatetime(history.getMissionStartDatetime());
        antiAvoidance.setMissionEndDatetime(history.getMissionEndDatetime());
        antiAvoidance.setMethod(history.getMethod());
        antiAvoidance.setDoubt(history.getDoubt());
        antiAvoidance.setRiskAccount(history.getRiskAccount());
        antiAvoidance.setDealWith(history.getDealWith());
        antiAvoidance.setPhaseRemarks(history.getPhaseRemarks());
        antiAvoidance.setConclusion(history.getConclusion());
        antiAvoidance.setContact(history.getContact());
        antiAvoidance.setContactNum(history.getContactNum());
    }
    
    /**
     * <p>Description:新增或编辑避税信息</p>
     * @param companyId 公司ID
     * @param wfId 流程ID
     * @throws Exception 
     */
    public void saveOrUpdateAnti(Long companyId, Long wfId) {
    	//将list集合转换为逗分隔的字符串
    	antiAvoidance.setTaxTypes(feedBackAntiService.taxTypeToString(taxTypes));
    	if(antiAvoidance.getId() != null){ //编辑反避税中的信息
    	    if(feedBackAntiService.validateUpdate(antiAvoidance,antiResults)){
    	    	try {
					feedBackAntiService.saveAndUpdate(antiAvoidance, antiResults);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改反避税信息：", "操作失败。"));
					return;
				}
    	    }
    	}else{ //新增反避税信息
    	    saveAnti(companyNameId, wfId);
    	}
    	flag = false;
    }

    public void specialUpdateAnti(){
        FacesContext context = FacesContext.getCurrentInstance();
        antiAvoidance.setTaxTypes(feedBackAntiService.taxTypeToString(taxTypes));
        
        if(!feedBackAntiService.validateForm(context, antiAvoidance)){
        	return;
        }
        
        if(feedBackAntiService.validateUpdate(antiAvoidance,antiResults)){
        	try {
				feedBackAntiService.saveAndUpdate(antiAvoidance, antiResults);
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改反避税信息：", "操作失败。"));
				return;
			}
            operationType = "";
            history();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改反避税信息：", "操作成功。"));
        }
        lookOrEdit = TRUE;
    }
    
    public void clear(){
    	lookOrEdit = TRUE;
    	authority = FALSE;
    	operationType = "";
    	antiAvoidance = new InvsAntiAvoidance();
    	antiResults = new ArrayList<InvsAntiResult>();
    	taxTypes = new ArrayList<String>();
    }
    
    /**
     * <p>Description:还原反避税信息</p>
     * @param wfId 流程ID
     */
    public void revertAnti(Long wfId){
    	List<InvsAntiAvoidance> lists = feedBackAntiService.getAntiByWfId(wfId);
    	if(lists.size() > 0){
    		antiAvoidance = lists.get(0);
    		if(antiAvoidance.getTaxTypes() != null && !"".equals(antiAvoidance.getTaxTypes().trim())){
    		    taxTypeToList(antiAvoidance.getTaxTypes());
    		}
			antiResults = antiAvoidance.getInvsAntiResults();
    	}
    }
    
    /**
     * 逻辑删除
     */
    public void deleteAnti(){
        FacesContext context = FacesContext.getCurrentInstance();
        if(isDeleteAuthority()){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "无权限删除信息，请联系管理员！", ""));
            return;
        }
    	feedBackAntiService.deleteAnti(id);
    	revertAnti(antiAvoidance.getWfId());
    	history();
    	searchAllAntiBy();
    }
    
    /**
     * 
     * @param id 主表ID
     */
    public void history(){
    	List<InvsAntiAvoidanceHistory> invsAntiAvoidanceHistory = feedBackAntiService.getHistory(id);
    	historyVos = new ArrayList<AntiHistoryVo>();
    	
    	if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(operationType)){  //当前
    		
    		historyVos.add(getSpecialAntiHistoryVo(operationType, ""));
    		
    	}else if (DictConsts.TIH_TAX_OPERATETYPE_TYPE_1.equals(operationType)) {  //新建
    	    antiResults = new ArrayList<InvsAntiResult>();
    		if(invsAntiAvoidanceHistory.size() > 0){  //如果有历史记录，标示符为C表示新建
    		    
    		    antiByHistory(invsAntiAvoidanceHistory.get(invsAntiAvoidanceHistory.size() - 1));
    		    resultByHistoryResult(invsAntiAvoidanceHistory.get(invsAntiAvoidanceHistory.size() - 1).getInvsAntiResultHistories());
    		    
    			for(InvsAntiAvoidanceHistory history : invsAntiAvoidanceHistory){
    				if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_1.equals(history.getOperateInd())){
    					AntiHistoryVo historyVo = new AntiHistoryVo();
    					historyVo.setId(history.getId());
    					historyVo.setOperationType(history.getOperateInd());
    					historyVo.setUpdateBy(findUsernameByAccount(history.getCreatedBy()));
    					historyVo.setUpdateTime(history.getUpdatedDatetime());
    					historyVos.add(historyVo);
    				}
    	    	}
    		}else{  //如果没有历史记录，则主表数据位新建数据
    		    antiAvoidance = new InvsAntiAvoidance();
                antiResults = new ArrayList<InvsAntiResult>();
                taxTypes = new ArrayList<String>();
    		}
    		
		}else if (DictConsts.TIH_TAX_OPERATETYPE_TYPE_2.equals(operationType)) {  //更新
		    antiResults = new ArrayList<InvsAntiResult>();
			if(invsAntiAvoidanceHistory.size() > 1){ //如果历史表中只有一条记录，只能是新建，不可能是更新
			    
			    antiByHistory(invsAntiAvoidanceHistory.get(0));
			    resultByHistoryResult(invsAntiAvoidanceHistory.get(0).getInvsAntiResultHistories());
			    
    			for(InvsAntiAvoidanceHistory history : invsAntiAvoidanceHistory){
    				if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_2.equals(history.getOperateInd())){
    					AntiHistoryVo historyVo = new AntiHistoryVo();
    					historyVo.setId(history.getId());
    					historyVo.setOperationType(history.getOperateInd());
    					historyVo.setUpdateBy(findUsernameByAccount(history.getCreatedBy()));
    					historyVo.setUpdateTime(history.getUpdatedDatetime());
    					historyVos.add(historyVo);
    				}
    	    	}
    		}else{
    		    antiAvoidance = new InvsAntiAvoidance();
    		    antiResults = new ArrayList<InvsAntiResult>();
    		    taxTypes = new ArrayList<String>();
    		}
			
		}else if (DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(operationType)) {  //删除
		    antiResults = new ArrayList<InvsAntiResult>();
			if(getSpecialAntiHistoryVo(operationType, "Y") != null){
				historyVos.add(getSpecialAntiHistoryVo(operationType, "Y"));
			}else{
			    antiAvoidance = new InvsAntiAvoidance();
                antiResults = new ArrayList<InvsAntiResult>();
                taxTypes = new ArrayList<String>();
			}
			
		}else{
			historyVos.add(getSpecialAntiHistoryVo(operationType, ""));
			
			for(InvsAntiAvoidanceHistory history : invsAntiAvoidanceHistory){
	    		AntiHistoryVo historyVo = new AntiHistoryVo();
	    		historyVo.setId(history.getId());
	    		historyVo.setOperationType(history.getOperateInd());
	    		historyVo.setUpdateBy(findUsernameByAccount(history.getCreatedBy()));
	    		historyVo.setUpdateTime(history.getUpdatedDatetime());
	    		historyVos.add(historyVo);
	    	}
		}
    	
    	if(historyVos != null && historyVos.size() > 0){
    	    selectedAnti = historyVos.get(0);
    	}
    	
    }
    
    public String findUsernameByAccount(String account) {
    	P p = documentService.getNameByAdAccount(account);
        return p==null?account:p.getNachn();
    }
    
    public AntiHistoryVo getSpecialAntiHistoryVo(String operationType, String defunctInd){
    	List<InvsAntiAvoidance> avoidanceList = feedBackAntiService.getInvsAntiAvoidance(id, defunctInd);
    	AntiHistoryVo antiVo = new AntiHistoryVo();
    	if(avoidanceList.size() > 0){
    		antiAvoidance = avoidanceList.get(0);
    		
    		if(antiAvoidance.getTaxTypes() != null && !"".equals(antiAvoidance.getTaxTypes().trim())){
    		    taxTypeToList(antiAvoidance.getTaxTypes());
    		}
            
    		antiResults = antiAvoidance.getInvsAntiResults();
    		
        	List<Long> positonManager = null;
        	try {
    			positonManager = feedBackAntiService.findTransCompanysByPositon(ANTI_POSITION_MANAGER);
    		} catch (Exception e) {
    			logger.error(e.getMessage(), e);
    		}
        	//通过岗位和当前操作人判断是否具有修改和删除权限
    		if(positonManager.contains(antiAvoidance.getCompanymstrId())){  //拥有这个权限
    			authority = FALSE;
        	}else{  //不拥有这个权限
        		authority = TRUE;
        	}
    		
    		antiVo.setId(antiAvoidance.getId());
    		if("N".equals(antiAvoidance.getDefunctInd())){
    			antiVo.setOperationType(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4);
    		}else{
    			antiVo.setOperationType(DictConsts.TIH_TAX_OPERATETYPE_TYPE_3);
    		}
    		antiVo.setUpdateBy(findUsernameByAccount(antiAvoidance.getUpdatedBy()));
    		antiVo.setUpdateTime(antiAvoidance.getUpdatedDatetime());
    		return antiVo;
    	}else{
    		return null;
    	}
    }

    private void taxTypeToList(String taxType) {
        String[] arrayStr = taxType.split(",");
        taxTypes = new ArrayList<String>();
        for(int i = 0 ; i < arrayStr.length ; i++){
            taxTypes.add(arrayStr[i].trim());
        }
    }
    
    /**
     * <p>Description:从数据库中删除反避税信息（物理删除）</p>
     * @param id
     */
    public void daleteAnti(Long id){
    	feedBackAntiService.deleteAntiAndResult(id);
    }
    
    /**
     * <p>Description:保存反避税信息</p>
     * @param companyId
     * @param wfId 流程ID
     * @throws Exception 
     */
    public void saveAnti(Long companyId, Long wfId) {
    	antiAvoidance.setTaxTypes(feedBackAntiService.taxTypeToString(taxTypes));
    	antiAvoidance.setWfId(wfId);
    	antiAvoidance.setCompanymstrId(companyId);
    	//保存反避税信息
    	try {
			if(antiAvoidance.getId() == null){
			    feedBackAntiService.saveAntiAndResult(antiAvoidance, antiResults);
			}else{
			    feedBackAntiService.updateAntiAndResult(antiAvoidance, antiResults);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "编辑反避税信息：", "操作失败。"));
		}
    	flag = false;
    }
    
    public void vaildate() throws Exception{
        FacesContext context = FacesContext.getCurrentInstance();
        antiAvoidance.setTaxTypes(feedBackAntiService.taxTypeToString(taxTypes));
        feedBackAntiService.vaildate(context, antiAvoidance, antiResults);
    }

    public boolean isDeleteAuthority(){
        try {
            List<Long> list = feedBackAntiService.deleteAuthorityService(ANTI_POSITION_MANAGER, companyNameId);
            if(list.size() <= 0){
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(), e);
        }
        return false;
    }
	
	// Getter & Setter
	public InvsAntiAvoidance getAntiAvoidance() {
		return antiAvoidance;
	}

	public void setAntiAvoidance(InvsAntiAvoidance antiAvoidance) {
		this.antiAvoidance = antiAvoidance;
	}

	public List<String> getTaxTypes() {
		return taxTypes;
	}

	public void setTaxTypes(List<String> taxTypes) {
		this.taxTypes = taxTypes;
	}

	public List<InvsAntiResult> getAntiResults() {
		return antiResults;
	}

	public void setAntiResults(List<InvsAntiResult> antiResults) {
		this.antiResults = antiResults;
	}

	public SearchAntiVo getAntiAvoidancVo() {
		return antiAvoidancVo;
	}

	public void setAntiAvoidancVo(SearchAntiVo antiAvoidancVo) {
		this.antiAvoidancVo = antiAvoidancVo;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getLookOrEdit() {
		return lookOrEdit;
	}

	public void setLookOrEdit(String lookOrEdit) {
		this.lookOrEdit = lookOrEdit;
	}

	public AntiHistoryVo getSelectedAnti() {
		return selectedAnti;
	}

	public void setSelectedAnti(AntiHistoryVo selectedAnti) {
		this.selectedAnti = selectedAnti;
	}


	public InvsAntiResult getResult() {
		return result;
	}


	public void setResult(InvsAntiResult result) {
		this.result = result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

    public List<AntiHistoryVo> getHistoryVos() {
        return historyVos;
    }

    public void setHistoryVos(List<AntiHistoryVo> historyVos) {
        this.historyVos = historyVos;
    }

    public Long getCompanyNameId() {
        return companyNameId;
    }

    public void setCompanyNameId(Long companyNameId) {
        this.companyNameId = companyNameId;
    }

    public List<AntiAvoidanceVo> getSearchAntiTableVos() {
        return searchAntiTableVos;
    }

    public void setSearchAntiTableVos(List<AntiAvoidanceVo> searchAntiTableVos) {
        this.searchAntiTableVos = searchAntiTableVos;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
