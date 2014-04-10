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
import com.wcs.tih.feedback.controller.vo.InspectationHistoryVo;
import com.wcs.tih.feedback.controller.vo.InspectionVo;
import com.wcs.tih.feedback.service.FeedBackInspectationService;
import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.model.InvsInspectationHistory;
import com.wcs.tih.model.InvsInspectationResult;
import com.wcs.tih.model.InvsInspectationResultHistory;
import com.wcs.tih.report.controller.vo.InspectVo;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:wangxuan@wcs-global.com">XUAN</a>
 */
@ManagedBean(name="feedBackInspectationBean")
@ViewScoped
public class FeedBackInspectationBean implements Serializable{

	private static final String FALSE = "false";
    private static final String TRUE = "true";
    private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@EJB
	private FeedBackInspectationService inspectationService;
	@EJB
	private DocumentService documentService;
	
	private InvsInspectation invsInspectation = new InvsInspectation();
	private  List<InvsInspectationResult> inspectationResult = new ArrayList<InvsInspectationResult>();
	private List<String> taxTypes = new ArrayList<String>();
	private InvsInspectationResult result;
	
	private InspectionVo inspectionVo = new InspectionVo(); //差查询字段
	
	private List<InspectVo> invsInspectationLists;
	
	private List<InspectationHistoryVo> inspectationHistoryVo;
	private String operationType = "";
	private InspectationHistoryVo selectedInspectation;
	private String lookOrEdit = TRUE;
	private String authority = FALSE;
	private Long id;
	private Long companyNameId;
	private static final String INSPECTION_POSITION_MANAGER = "SPE_INSPC_ADM";
	private static final String INSPECTION_POSITION_LOOK = "SPE_INSPC_OBSV";
	private boolean flag = false;
	
	@PostConstruct
	public void init(){
        searchAllInspection();
	}
	
	/**
	 * <p>Description:给稽查结果表添加新的一行一行</p>
	 */
	public void addInspectationResult(){
		String taxType = taxTypes.toString();
		invsInspectation.setTaxTypes(taxType.substring(1, taxType.length()-1).replace(" ", ""));
		List<InvsInspectationResult> list = new ArrayList<InvsInspectationResult>();
		if(inspectationResult.size() == 0){
		    inspectationResult = new ArrayList<InvsInspectationResult>();
		}else{
		    list = inspectationResult;
		    inspectationResult = new ArrayList<InvsInspectationResult>();
		}
		InvsInspectationResult result = new InvsInspectationResult();
		result.setCreatedDatetime(new Date());
		inspectationResult.add(result);
		inspectationResult.addAll(list);
		
	}
	
	/**
	 * <p>Description:删除稽查信息表（逻辑删除）</p>
	 * @param id
	 */
	public void deleteInspection(){
	    FacesContext context = FacesContext.getCurrentInstance();
	    if(isDeleteAuthority()){
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "无权限删除信息，请联系管理员！", ""));
	        return;
	    }
		inspectationService.deleteInspection(id);
		searchAllInspection();
		findHistoryByID();
	}
	
	/**
	 * 从数据库中删除
	 * @param id 流程id
	 */
	public void deleteInspection(Long id){
		inspectationService.deleteInspectionDraft(id);
		clear();
	}
	
	/**
	 * <p>Description:删除一行</p>
	 */
	public void deleteInspectionResult(){
		int j = 0;
		for(int i = 0; i < inspectationResult.size(); i++){
			if(inspectationResult.get(i).getCreatedDatetime() == result.getCreatedDatetime()){
				j = i;
			}
		}
		if(result.getId() != null){
		    inspectationService.removeResult(result);
		}
		inspectationResult.remove(j);
	}
	
	/**
	 * 稽查信息页面中修改按钮
	 */
	public void specialEdit(){
		lookOrEdit = FALSE;
	}
	
	public void onRowSelect(SelectEvent event) {
	    taxTypes = new ArrayList<String>();
		if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(selectedInspectation.getOperationType()) || DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(selectedInspectation.getOperationType())){
			invsInspectation = new InvsInspectation();
			invsInspectation = inspectationService.getInspection(selectedInspectation.getId()).get(0);
			if(invsInspectation.getTaxTypes() != null && !"".equals(invsInspectation.getTaxTypes().trim())){
				taxTypes = inspectationService.taxTypeToList(invsInspectation.getTaxTypes());
			}
			inspectationResult = invsInspectation.getInvsInspectationResults();
		}else{
			inspectationResult = new ArrayList<InvsInspectationResult>();
			InvsInspectationHistory history = inspectationService.getHistoryById(selectedInspectation.getId());
			List<InvsInspectationResultHistory> historyResults = inspectationService.getInvsInspectationResultHistoryLists(selectedInspectation.getId());
			inspecationByHistory(history);
			resultByHistoryResult(historyResults);
		}
		lookOrEdit = TRUE;
	}


    private void resultByHistoryResult(List<InvsInspectationResultHistory> historyResults) {
        for(InvsInspectationResultHistory resultHistoryList : historyResults){
        	InvsInspectationResult result = new InvsInspectationResult();
        	result.setTaxType(resultHistoryList.getTaxType());
        	result.setOverdueTax(resultHistoryList.getOverdueTax());
        	result.setPenalty(resultHistoryList.getPenalty());
        	result.setInputTaxTurnsOut(resultHistoryList.getInputTaxTurnsOut());
        	result.setFine(resultHistoryList.getFine());
        	result.setReductionPrevLoss(resultHistoryList.getReductionPrevLoss());
        	result.setSituationRemarks(resultHistoryList.getSituationRemarks());
        	inspectationResult.add(result);
        }
    }

    private void inspecationByHistory(InvsInspectationHistory history) {
        invsInspectation.setCompanyName(history.getCompanyName());
        invsInspectation.setInspectOrg(history.getInspectOrg());
        invsInspectation.setInspectStartDatetime(history.getInspectStartDatetime());
        invsInspectation.setInspectEndDatetime(history.getInspectEndDatetime());
        invsInspectation.setMissionStartDatetime(history.getMissionStartDatetime());
        invsInspectation.setMissionEndDatetime(history.getMissionEndDatetime());
        invsInspectation.setInspectType(history.getInspectType());
        invsInspectation.setTaxTypes(history.getTaxTypes());
        //税种
        if(history.getTaxTypes() != null && !"".equals(history.getTaxTypes().trim())){
        	taxTypes = inspectationService.taxTypeToList(history.getTaxTypes());
        }
        
        invsInspectation.setMainProblemDesc(history.getMainProblemDesc());
        invsInspectation.setRectificationPlan(history.getRectificationPlan());
        invsInspectation.setRectificationResult(history.getRectificationResult());
    }
	
	
	/**
	 * <p>Description:修改稽查信息表（编辑稽查信息，流程已提交）</p>
	 * @param id 主表ID
	 */
	public void updateInspection(){
		taxTypeToString(taxTypes);
		try {
            inspectationService.saveInspectationHistory(invsInspectation.getId());
            inspectationService.updateInspectation(invsInspectation,inspectationResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.info("更新失败！！！");
            logger.error(e.getMessage(), e);
        }
	}

	/**
	 * <p>Description: 将税种List集合转换为String类型</p>
	 */
    private void taxTypeToString(List<String> taxTypes) {
        invsInspectation.setTaxTypes("");
        if(taxTypes.size() > 0){
			String taxType = taxTypes.toString();
			invsInspectation.setTaxTypes(taxType.substring(1, taxType.length()-1).replace(" ", ""));
		}
    }
	
	/**
	 * <p>Description:新增稽查信息</p>
	 * @param companyId 公司ID
	 * @param wfId 流程ID
	 * @throws Exception 
	 */
	public void saveInspection(Long companyId, Long wfId) {
	    taxTypeToString(taxTypes);
		//表单不为空，当验证完成后，保存稽查信息
	    invsInspectation.setCompanymstrId(companyId);
	    invsInspectation.setWfId(wfId);
		try {
			if(invsInspectation.getId() == null){
			    inspectationService.saveInvsInspectation(invsInspectation,inspectationResult); 
			}else{
			    inspectationService.updateInspectation(invsInspectation, inspectationResult);
			}
			flag = false;
		} catch (Exception e) {
			logger.info("保存失败！！！");
			logger.error(e.getMessage(), e);
		}
	}
	
	
	/**
	 * 
	 * <p>Description: 流程中的验证（完成之前）</p>
	 * @throws Exception
	 */
	public void validate() throws Exception{
	    FacesContext context = FacesContext.getCurrentInstance();
	    taxTypeToString(taxTypes);
	    inspectationService.isValidate(context, invsInspectation);
	}

    
	
	
	/**
	 * <p>Description:还原稽查信息</p>
	 * @param id 流程ID
	 */
	public void revertInspection(Long id){
		clear();
		List<InvsInspectation> list = inspectationService.getInspectionByWfId(id);
		if(list.size() > 0){
			invsInspectation = list.get(0);
			if(invsInspectation.getTaxTypes() != null && !"".equals(invsInspectation.getTaxTypes().trim())){
				taxTypes = inspectationService.taxTypeToList(invsInspectation.getTaxTypes());
			}
			if(invsInspectation.getInvsInspectationResults().size() > 0){
				inspectationResult = invsInspectation.getInvsInspectationResults();
			}else{
				inspectationResult = inspectationService.getInvsInspectationResultLists(id);
			}
		}
	}
	
	/**
	 * <p>Description:新增或修改</p>
	 * @param companyId
	 * @param wfNo
	 * @throws Exception 
	 */
	public void saveOrUpdate(Long companyId, Long wfId) throws Exception{
	    taxTypeToString(taxTypes);
	    if(invsInspectation.getId() != null){
	        if(inspectationService.validateUpdate(invsInspectation,inspectationResult)){
	        	inspectationService.updateInspectionDraft(invsInspectation, inspectationResult);
	        }
	    }else{
	        saveInspection(companyNameId, wfId);
	    }
	    clear();
	}
	

	public void specialUpdate(){
	    FacesContext context = FacesContext.getCurrentInstance();
	    taxTypeToString(taxTypes);
	    if(!inspectationService.validateForm(context, invsInspectation)){
	    	return;
	    }
	    
	    if(inspectationService.validateUpdate(invsInspectation,inspectationResult)){
	        try {
	        	inspectationService.updateInspectionDraft(invsInspectation, inspectationResult);
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改稽查信息：", "操作失败。"));
				return;
			}
	        operationType = "";
	        findHistoryByID();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改稽查信息：", "操作成功。"));
	    }
	    lookOrEdit = TRUE;
	    searchAllInspection();
	}
	
    public void onEdit(RowEditEvent event) {  
    }
    
    public void searchAllInspection(){
    	try {
            setInvsInspectationLists(inspectationService.findAllInspectionBy(inspectionVo,INSPECTION_POSITION_LOOK,INSPECTION_POSITION_MANAGER));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    /**
     * <p>Descripton:通过主表ID得到历史信息</p>
     * @param id 主表ID
     */
    public void findHistoryByID(){
    	List<InvsInspectationHistory> historyLists = null; //降序排列
        try {
            historyLists = inspectationService.getInspectationHistoryLists(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    	inspectationHistoryVo = new ArrayList<InspectationHistoryVo>();
    	
    	if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4.equals(operationType)){ //当前
    		inspectationHistoryVo.add(getSepcialInspectationHistoryVo(operationType, ""));
    	}else if (DictConsts.TIH_TAX_OPERATETYPE_TYPE_1.equals(operationType)) { //新建
    	    inspectationResult = new ArrayList<InvsInspectationResult>();
    		if(historyLists.size() > 0){  //如果有历史记录，标示符为C表示新建
    		    
    		    inspecationByHistory(historyLists.get(historyLists.size() - 1));
    		    resultByHistoryResult(inspectationService.getInvsInspectationResultHistoryLists(historyLists.get(historyLists.size() - 1).getId()));
    		    
    			for(InvsInspectationHistory history : historyLists){
    				if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_1.equals(history.getOperateInd())){
    					InspectationHistoryVo voHistory = new InspectationHistoryVo();
    					voHistory.setId(history.getId());
    					voHistory.setOperationType(history.getOperateInd());
    					voHistory.setUpdateBy(findUsernameByAccount(history.getCreatedBy()));
    					voHistory.setUpdateTime(history.getUpdatedDatetime());
    					inspectationHistoryVo.add(voHistory);
    				}
    			}
    		}else{  //如果没有历史记录，则主表数据位新建数据
    		    invsInspectation = new InvsInspectation();
                inspectationResult = new ArrayList<InvsInspectationResult>();
                taxTypes = new ArrayList<String>();
    		}
		}else if (DictConsts.TIH_TAX_OPERATETYPE_TYPE_2.equals(operationType)) {  //更新
		    inspectationResult = new ArrayList<InvsInspectationResult>();
		    if(historyLists.size() > 1){ //如果历史表中只有一条记录，只能是新建，不可能是更新
		        inspecationByHistory(historyLists.get(0));
		        resultByHistoryResult(inspectationService.getInvsInspectationResultHistoryLists(historyLists.get(0).getId()));
		        for(InvsInspectationHistory history : historyLists){
		            if(DictConsts.TIH_TAX_OPERATETYPE_TYPE_2.equals(history.getOperateInd())){
		                InspectationHistoryVo voHistory = new InspectationHistoryVo();
		                voHistory.setId(history.getId());
		                voHistory.setOperationType(history.getOperateInd());
		                voHistory.setUpdateBy(findUsernameByAccount(history.getCreatedBy()));
		                voHistory.setUpdateTime(history.getUpdatedDatetime());
		                inspectationHistoryVo.add(voHistory);
		            }
		        }
		    }else{
		        invsInspectation = new InvsInspectation();
		        inspectationResult = new ArrayList<InvsInspectationResult>();
		        taxTypes = new ArrayList<String>();
		    }
		    
		}else if (DictConsts.TIH_TAX_OPERATETYPE_TYPE_3.equals(operationType)) {  //删除
		    inspectationResult = new ArrayList<InvsInspectationResult>();
			if(getSepcialInspectationHistoryVo(operationType, "Y") != null){
				inspectationHistoryVo.add(getSepcialInspectationHistoryVo(operationType, "Y"));
			}else{
			    invsInspectation = new InvsInspectation();
			    inspectationResult = new ArrayList<InvsInspectationResult>();
			    taxTypes = new ArrayList<String>();
			}
			
    		
		}else{  //所有数据
			inspectationHistoryVo.add(getSepcialInspectationHistoryVo(operationType, ""));
    		
    		for(InvsInspectationHistory history : historyLists){
    			InspectationHistoryVo voHistory = new InspectationHistoryVo();
    			voHistory.setId(history.getId());
    			voHistory.setOperationType(history.getOperateInd());
    			voHistory.setUpdateBy(findUsernameByAccount(history.getCreatedBy()));
    			voHistory.setUpdateTime(history.getUpdatedDatetime());
    			inspectationHistoryVo.add(voHistory);
    		}
		}
    	
    	if(inspectationHistoryVo != null && inspectationHistoryVo.size() > 0){
    	    selectedInspectation = inspectationHistoryVo.get(0);
    	}
    	
    }
    
    /**
     * <p>Description:通过账号得到名字</p>
     * @param account
     * @return
     */
    public String findUsernameByAccount(String account) {
    	P p = documentService.getNameByAdAccount(account);
        return p==null?account:p.getNachn();
    }
    
    public InspectationHistoryVo getSepcialInspectationHistoryVo(String operationType, String defunctInd){
    	InspectationHistoryVo vo = new InspectationHistoryVo();
    	List<InvsInspectation> list = inspectationService.getInspectionToHistory(id, defunctInd);
    	
    	List<Long> positonManager = null;
    	try {
			positonManager = inspectationService.findTransCompanysByPositon(INSPECTION_POSITION_MANAGER);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if(list.size() > 0){
		    List<InvsInspectation> inspection = inspectationService.getInspection(id);
		    revertInspection(inspection.get(0).getWfId());
		    
			//通过岗位和当前操作人判断是否具有修改和删除权限
			if(positonManager.contains(inspection.get(0).getCompanymstrId())){  //拥有这个权限
				authority = FALSE;
			}else{  //不拥有这个权限
				authority = TRUE;
			}
			vo.setId(inspection.get(0).getId());
			if("N".equals(inspection.get(0).getDefunctInd())){
				vo.setOperationType(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4);
			}else{
				vo.setOperationType(DictConsts.TIH_TAX_OPERATETYPE_TYPE_3);
			}
			vo.setUpdateBy(findUsernameByAccount(inspection.get(0).getUpdatedBy()));
			vo.setUpdateTime(inspection.get(0).getUpdatedDatetime());
			return vo;
		}else{
			return null;
		}
    }
    
    public void clearSearch(){
    	inspectionVo = new InspectionVo();
    }
    
    public void clear(){
    	invsInspectation = new InvsInspectation();
    	inspectationResult = new ArrayList<InvsInspectationResult>();
    	lookOrEdit = TRUE;
    	authority = FALSE;
    	operationType = "";
    	flag = false;
    	taxTypes = null;
    }
    
    public void onCancel(){
        List<InvsInspectation> inspection = inspectationService.getInspection(id);
        if(inspection.size() > 0){
            revertInspection(inspection.get(0).getWfId());
        }
        lookOrEdit = TRUE;
    }
    
    public boolean isDeleteAuthority(){
        try {
            List<Long> list = inspectationService.findCompanysByPositonAndCompanyId(INSPECTION_POSITION_MANAGER, companyNameId);
            if(list.size() <= 0){
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
	
	// Getter & Setter
	public List<InvsInspectationResult> getInspectationResult() {
		return inspectationResult;
	}

	public void setInspectationResult(List<InvsInspectationResult> inspectationResult) {
		this.inspectationResult = inspectationResult;
	}

	public List<String> getTaxTypes() {
		return taxTypes;
	}

	public void setTaxTypes(List<String> taxTypes) {
		this.taxTypes = taxTypes;
	}

	public InvsInspectation getInvsInspectation() {
		return invsInspectation;
	}

	public void setInvsInspectation(InvsInspectation invsInspectation) {
		this.invsInspectation = invsInspectation;
	}

	public InspectionVo getInspectionVo() {
		return inspectionVo;
	}

	public void setInspectionVo(InspectionVo inspectionVo) {
		this.inspectionVo = inspectionVo;
	}

	public List<InspectationHistoryVo> getInspectationHistoryVo() {
		return inspectationHistoryVo;
	}

	public void setInspectationHistoryVo(List<InspectationHistoryVo> inspectationHistoryVo) {
		this.inspectationHistoryVo = inspectationHistoryVo;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public InspectationHistoryVo getSelectedInspectation() {
		return selectedInspectation;
	}

	public void setSelectedInspectation(InspectationHistoryVo selectedInspectation) {
		this.selectedInspectation = selectedInspectation;
	}

	public String getLookOrEdit() {
		return lookOrEdit;
	}

	public void setLookOrEdit(String lookOrEdit) {
		this.lookOrEdit = lookOrEdit;
	}

	public InvsInspectationResult getResult() {
		return result;
	}

	public void setResult(InvsInspectationResult result) {
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

    public Long getCompanyNameId() {
        return companyNameId;
    }

    public void setCompanyNameId(Long companyNameId) {
        this.companyNameId = companyNameId;
    }

    public List<InspectVo> getInvsInspectationLists() {
        return invsInspectationLists;
    }

    public void setInvsInspectationLists(List<InspectVo> invsInspectationLists) {
        this.invsInspectationLists = invsInspectationLists;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
