package com.wcs.tih.feedback.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.service.CommonService;
import com.wcs.tih.feedback.controller.vo.InspectionVo;
import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.model.InvsInspectationHistory;
import com.wcs.tih.model.InvsInspectationResult;
import com.wcs.tih.model.InvsInspectationResultHistory;
import com.wcs.tih.report.controller.vo.InspectVo;
import com.wcs.tih.util.ArithUtil;
import com.wcs.tih.util.ValidateUtil;


/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:wangxuan@wcs-global.com">XUAN</a>
 */
@Stateless
public class FeedBackInspectationService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
	@PersistenceContext
	private EntityManager em;
	@EJB 
	private LoginService loginService;
	@EJB
    private CommonService commonService;
	
	/**
	 * <p>Description:通过“稽查信息表”的ID查找表中的数据</p>
	 * @param id 稽查信息表”的ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsInspectation> getInspection(Long id){
		List<InvsInspectation> insList;
		String sql = "select ins from InvsInspectation ins left join fetch ins.invsInspectationResults where ins.id = " + id + " ";
		insList = this.em.createQuery(sql).getResultList();
		return insList;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsInspectation> getInspectionToHistory(Long id, String defunctInd){
		List<InvsInspectation> insList;
		StringBuffer sb = new StringBuffer();
		sb.append("select ins from InvsInspectation ins left join fetch ins.invsInspectationResults where ins.id = " + id + "  ");
		if(defunctInd != null && !"".equals(defunctInd)){
			sb.append(" and ins.defunctInd = '"+defunctInd+"' ");
		}
		insList = this.em.createQuery(sb.toString()).getResultList();
		return insList;
	}
	
	/**
	 * <p>Description:通过流程的ID查找稽查表中的数据</p>
	 * @param wfId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsInspectation> getInspectionByWfId(Long wfId){
		List<InvsInspectation> insList;
		String wid = wfId.toString();
		String sql = "select ins from InvsInspectation ins left  join fetch ins.invsInspectationResults where ins.wfId = " + wid + "  ";
		insList = this.em.createQuery(sql).getResultList();
		return insList;
	}
	
	/**
	 * <p>Description:通过“稽查信息表”的ID查找“稽查结果表”的数据</p>
	 * @param inspectationId “稽查信息表”的ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsInspectationResult> getInvsInspectationResultLists(Long inspectationId){
		List<InvsInspectationResult> resultList;
		String sql = "select insResult from InvsInspectationResult insResult  where insResult.invsInspectation.id = " + inspectationId + " and insResult.defunctInd <> 'Y' ";
		resultList = this.em.createQuery(sql).getResultList();
		return resultList;
	}
	
	/**
	 * <p>Description:通过“稽查信息表”的ID查找“稽查信息表_历史”的数据</p>
	 * @param inspectationId “稽查信息表”的ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsInspectationHistory> getInspectationHistoryLists(Long inspectationId) throws Exception{
		String sql = "select insHistory from InvsInspectationHistory insHistory left join fetch insHistory.invsInspectationResultHistories where insHistory.invsInspectation.id = " + inspectationId + " and insHistory.defunctInd <> 'Y' order by insHistory.updatedDatetime DESC";
		return this.em.createQuery(sql).getResultList();
	}
	
	/**
	 * 
	 * @param id 历史表ID
	 * @return
	 */
	public InvsInspectationHistory getHistoryById(Long id){
	    return this.em.find(InvsInspectationHistory.class, id);
	}
	
	public void removeResult(InvsInspectationResult result){
	    String sql = "delete from INVS_INSPECTATION_RESULT result where result.id = "+result.getId()+" ";
	    this.em.createNativeQuery(sql).executeUpdate();
	}
	
	/**
	 * <p>Description:通过“稽查信息表_历史”的ID查找“稽查结果表_历史”的数据</p>
	 * @param insHistoryId “稽查信息表_历史”的ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsInspectationResultHistory> getInvsInspectationResultHistoryLists(Long insHistoryId){
		List<InvsInspectationResultHistory> insResultHistorys;
		String sql = "select resultHistory from InvsInspectationResultHistory resultHistory  where resultHistory.invsInspectationHistory.id = " + insHistoryId + " and resultHistory.defunctInd <> 'Y' order by resultHistory.updatedDatetime DESC";
		insResultHistorys = this.em.createQuery(sql).getResultList();
		return insResultHistorys;
	}
	
	public List<InspectVo> findAllInspectionBy(InspectionVo inspectionVo,String inspectionPositionLook,String inspectionPositionManager) throws Exception{
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	    String select = "select it.id"+
	    		",it.COMPANYMSTR_ID"+
	    		",it.COMPANY_NAME"+
	    		",it.INSPECT_ORG"+
	    		",it.MISSION_START_DATETIME"+
	    		",it.MISSION_END_DATETIME"+
	    		",it.INSPECT_START_DATETIME"+
	    		",it.INSPECT_END_DATETIME"+
	    		",it.INSPECT_TYPE"+
	    		",it.TAX_TYPES"+
	    		",it.CONTACT"+
	    		",it.MAIN_PROBLEM_DESC"+
	    		",it.RECTIFICATION_PLAN"+
	    		",it.RECTIFICATION_RESULT" +
	    		",it.WF_ID";
	    select = select +",wf.status,p.value,temp.sumtax,temp.sumpenalty,temp.sumout,temp.sumloss,temp.sumfine";
	    StringBuffer jpql = new StringBuffer();
	    jpql.append(" (");
	    jpql.append("select it.id as sumid,sum(re.OVERDUE_TAX) as sumtax,sum(re.PENALTY) as sumpenalty,sum(re.INPUT_TAX_TURNS_OUT) as sumout,sum(re.REDUCTION_PREV_LOSS) as sumloss,sum(re.FINE) as sumfine");
	    jpql.append(" from INVS_INSPECTATION it,INVS_INSPECTATION_RESULT re");
	    jpql.append(" where it.id = re.INVS_INSPECTATION_ID");
	    jpql.append(" group by it.id");
	    jpql.append(") as temp ");
	    StringBuffer query = new StringBuffer();
	    if(inspectionVo!=null){
	    	String companyName = inspectionVo.getCompanyName();
	    	if(companyName!=null && !"".equals(companyName.trim())){
	    		query.append(" and it.COMPANY_NAME like '%"+companyName.trim()+"%'");
	    	}
	    	Date missionStartDatetime = inspectionVo.getMissionStartDatetime();
	    	if (missionStartDatetime!=null) {
	    		query.append(" and it.MISSION_START_DATETIME >= '" + df.format(missionStartDatetime) + "'");
	    	}
	    	Date missionEndDatetime = inspectionVo.getMissionEndDatetime();
	    	if (missionEndDatetime!=null) {
	    		query.append(" and it.MISSION_END_DATETIME <= '" + df.format(missionEndDatetime) + "'");
	    	}
	    	Date inspectStartDatetime = inspectionVo.getInspectStartDatetime();
	    	if (inspectStartDatetime!=null) {
	    		query.append(" and it.INSPECT_START_DATETIME >= '" + df.format(inspectStartDatetime) + "'");
	    	}
	    	Date inspectEndDatetime = inspectionVo.getInspectEndDatetime();
	    	if (inspectEndDatetime!=null) {
	    		query.append(" and it.INSPECT_END_DATETIME <= '" + df.format(inspectEndDatetime) + "'");
	    	}
	    	String inspectType = inspectionVo.getInspectType();
	    	if(inspectType != null && !"".equals(inspectType)){
	    		query.append(" and it.INSPECT_TYPE = '"+inspectType+"'");
	    	}
	    	String defunctInd = inspectionVo.getDefunctInd();
	    	if(defunctInd != null){
	    		query.append(" and it.DEFUNCT_IND = '"+defunctInd+"'");
	    	}else{
	    		query.append(" and it.DEFUNCT_IND = 'N'");
	    	}
	    }
	    query.append(" and it.COMPANYMSTR_ID in (");
	    query.append("select distinct c.id from COMPANYMSTR c,POSITIONORG po,POSITION p,USERPOSITIONORG up,USERMSTR u " +
	    		"where c.OID = po.OID and p.id = po.POSITION_ID and (p.code = '"+inspectionPositionLook+"' or p.code = '"+inspectionPositionManager+"') " +
	    		"and po.id = up.POSITIONORG_ID and u.id = up.USERMSTR_ID and u.AD_ACCOUNT = '"+loginService.getCurrentUserName()+"' and c.DEFUNCT_IND <> 'Y' and po.DEFUNCT_IND <> 'Y' and p.DEFUNCT_IND <> 'Y' and up.DEFUNCT_IND <> 'Y' and u.DEFUNCT_IND <> 'Y'");
	    query.append(")");
	    query.append(" order by it.UPDATED_DATETIME DESC");
	    String from = " from WF_INSTANCEMSTR wf,WF_INSTANCEMSTR_PROPERTY p,INVS_INSPECTATION it ";
	    String join = " left join " + jpql.toString() + " on it.id = temp.sumid";
	    String where = " where it.WF_ID = wf.id and wf.id = p.WF_INSTANCEMSTR_ID and (wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_2 +"' or wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_3 +"') and p.name = '"+DictConsts.TIH_TAX_REQUESTFORM_5_2+"'";
	    List resultList = em.createNativeQuery(select + from + join + where + query.toString()).getResultList();
	    return convertInspectVo(resultList);
	}
	
	public List<InspectVo> convertInspectVo(List its){
        List<InspectVo> itVos = new ArrayList<InspectVo>();
        InspectVo itVo = null;
        for (int i = 0; i < its.size(); i++) {
            itVo = new InspectVo();
            Object[] it = (Object[]) its.get(i);
            itVo.setId(it[0]== null ? null : (Long) it[0]);
            itVo.setCompanymstrId(it[1]== null ? null : (Long) it[1]);
            itVo.setCompanyName(it[2]== null ? "" : (String) it[2]);
            itVo.setInspectOrg(it[3]== null ? "" : (String) it[3]);
            itVo.setMissionStartDatetime(it[4]== null ? null : (Date) it[4]);
            itVo.setMissionEndDatetime(it[5]== null ? null : (Date) it[5]);
            itVo.setInspectStartDatetime(it[6]== null ? null : (Date) it[6]);
            itVo.setInspectEndDatetime(it[7]== null ? null : (Date) it[7]);
            itVo.setInspectType(it[8]== null ? "" : getDictValueByKey((String) it[8]));
            itVo.setTaxTypes(it[9]== null ? "" : getTaxValue((String) it[9]));
            itVo.setContact(it[10]== null ? "" : (String) it[10]);
            itVo.setMainProblemDesc(it[11]== null ? "" : (String) it[11]);
            itVo.setRectificationPlan(it[12]== null ? "" : (String) it[12]);
            itVo.setRectificationResult(it[13]== null ? "" : (String) it[13]);
            itVo.setWfId((it[14]== null ? null : (Long)it[14]));
            
            itVo.setWfStatus(it[15]== null ? "" : getDictValueByKey((String) it[15]));
            itVo.setStage(it[16]== null ? "" : getDictValueByKey((String) it[16]));
            
            itVo.setOverdueTax(it[17]==null ? 0 : ((BigDecimal) it[17]).doubleValue());
            itVo.setPenalty(it[18]==null ? 0 : ((BigDecimal) it[18]).doubleValue());
            itVo.setInputTaxTurnsOut(it[19]==null ? 0 : ((BigDecimal) it[19]).doubleValue());
            itVo.setReductionPrevLoss(it[20]==null ? 0 : ((BigDecimal) it[20]).doubleValue());
            itVo.setFine(it[21]==null ? 0 : ((BigDecimal) it[21]).doubleValue());
            double[] values = {itVo.getOverdueTax(),itVo.getPenalty(),itVo.getInputTaxTurnsOut(),itVo.getReductionPrevLoss(),itVo.getFine()};
            itVo.setSum(ArithUtil.sum(values));
            itVos.add(itVo);
        }
        return itVos;
    }
	
	public String getDictValueByKey(String key){
        DictVo dictVo = commonService.getDictVoByKey(key);
        return dictVo.getCodeVal()==null?"":dictVo.getCodeVal();
    }
	
	public String getTaxValue(String keys){
        String value = "";
        String[] key = keys.split(",");
        for (int i = 0; i < key.length; i++) {
            value = value +getDictValueByKey(key[i].trim()) +((i==key.length-1)?"":",");
        }
        return value;
    }
	
	/**
	 * <p>Description:我的草稿中的删除按钮</p>
	 * @param id 
	 */
	public void deleteInspection(Long id){
		InvsInspectation inspectation = null;
		List<InvsInspectation> inspectationList = getInspection(id);
		if(inspectationList.size() > 0){
			inspectation = inspectationList.get(0);
			inspectation.setDefunctInd("Y");
			for(InvsInspectationResult result : inspectation.getInvsInspectationResults()){
				result.setDefunctInd("Y");
				this.em.merge(result);
			}
			this.em.merge(inspectation);
		}
	}
	
	/**
	 * <p>Description:物理删除</p>
	 * @param id 流程id
	 */
	public void deleteInspectionDraft(Long id){
		List<InvsInspectation> inspectationList = getInspectionByWfId(id);
		if(inspectationList.size() > 0){
			InvsInspectation inspectation = inspectationList.get(0);
			List<InvsInspectationResult> results = inspectation.getInvsInspectationResults();
			if(results.size() > 0){
				for(InvsInspectationResult result : results){
					this.em.remove(result);
				}
			}
			this.em.remove(inspectation);
		}
	}
	
	/**
	 * <p>Description:保存历史表和历史字表信息</p>
	 * @param inspectationId
	 * @param user
	 * @throws Exception 
	 */
	public void saveInspectationHistory(Long inspectationId) throws Exception{
		List<InvsInspectation> invsInspectationLists = getInspection(inspectationId);
		BigDecimal big = BigDecimal.ZERO;
		if(invsInspectationLists.size() > 0){
		    InvsInspectation invsInspectation = invsInspectationLists.get(0);
		    List<InvsInspectationResult> invsInspectationResults = invsInspectation.getInvsInspectationResults();
		    if(invsInspectationResults != null){
		        invsInspectationResults = getInvsInspectationResultLists(inspectationId);
		    }
		    InvsInspectationHistory history = new InvsInspectationHistory();
		    if(getInspectationHistoryLists(inspectationId).size() > 0){
		        history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_2);  //更新
		    }else{
		        history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_1);  //新建
		    }
		    history.setInvsInspectation(invsInspectation);
		    history.setWfId(invsInspectation.getWfId());
		    history.setCompanymstrId(invsInspectation.getCompanymstrId());
		    history.setCompanyName(invsInspectation.getCompanyName());
		    history.setInspectOrg(invsInspectation.getInspectOrg());
		    history.setMissionStartDatetime(invsInspectation.getMissionStartDatetime());
		    history.setMissionEndDatetime(invsInspectation.getMissionEndDatetime());
		    history.setInspectStartDatetime(invsInspectation.getInspectStartDatetime());
		    history.setInspectEndDatetime(invsInspectation.getInspectEndDatetime());
		    history.setInspectType(invsInspectation.getInspectType());
		    history.setTaxTypes(invsInspectation.getTaxTypes());
		    history.setContact(invsInspectation.getContact());
		    history.setContactNum(invsInspectation.getContactNum());
		    history.setMainProblemDesc(invsInspectation.getMainProblemDesc());
		    history.setRectificationPlan(invsInspectation.getRectificationPlan());
		    history.setRectificationResult(invsInspectation.getRectificationResult());
		    history.setTotalPenalty(invsInspectation.getTotalPenalty());
		    history.setTotalFine(invsInspectation.getTotalFine());
		    history.setCreatedBy(invsInspectation.getUpdatedBy());
		    history.setCreatedDatetime(invsInspectation.getUpdatedDatetime());
		    history.setUpdatedBy(invsInspectation.getUpdatedBy());
		    history.setUpdatedDatetime(invsInspectation.getUpdatedDatetime());
		    history.setDefunctInd("N");
		    this.em.persist(history);
		    for(InvsInspectationResult result : invsInspectationResults){
		        InvsInspectationResultHistory resultHistory = new InvsInspectationResultHistory();
		        resultHistory.setInvsInspectationHistory(history);
		        resultHistory.setTaxType(result.getTaxType());
		        if(result.getOverdueTax() != null){
		            resultHistory.setOverdueTax(result.getOverdueTax());
		        }else{
		            resultHistory.setOverdueTax(big);
		        }
		        if(result.getInputTaxTurnsOut() != null){
		            resultHistory.setInputTaxTurnsOut(result.getInputTaxTurnsOut());
		        }else{
		            resultHistory.setInputTaxTurnsOut(big);
		        }
		        if(result.getPenalty() != null){
		            resultHistory.setPenalty(result.getPenalty());
		        }else{
		            resultHistory.setPenalty(big);
		        }
		        if(result.getReductionPrevLoss() != null){
		            resultHistory.setReductionPrevLoss(result.getReductionPrevLoss());
		        }else{
		            resultHistory.setReductionPrevLoss(big);
		        }
		        if(result.getFine() != null){
		            resultHistory.setFine(result.getFine());
		        }else{
		            resultHistory.setFine(big);
		        }
		        if(result.getSituationRemarks() != null){
		            resultHistory.setSituationRemarks(result.getSituationRemarks());
		        }else{
		            resultHistory.setSituationRemarks("");
		        }
		        resultHistory.setOperateInd("N");
		        resultHistory.setUpdatedBy(result.getUpdatedBy());
		        resultHistory.setUpdatedDatetime(result.getUpdatedDatetime());
		        resultHistory.setCreatedBy(result.getUpdatedBy());
		        resultHistory.setCreatedDatetime(result.getUpdatedDatetime());
		        resultHistory.setDefunctInd("N");
		        this.em.persist(resultHistory);
		    }
		}
		
	}
	
	/**
	 * <p>Description:更新主表和子表信息</p>
	 * @param invsInspectation
	 * @param inspectationResult
	 * @param user
	 */
	public void updateInspectation(InvsInspectation invsInspectation, List<InvsInspectationResult> inspectationResult)throws Exception{
		String user = loginService.getCurrentUsermstr().getAdAccount();
		BigDecimal totalPenalty = BigDecimal.ZERO;
		BigDecimal totalFine = BigDecimal.ZERO;
		
		for(InvsInspectationResult result : inspectationResult){
			if(result.getId() != null){
			    if(result.getPenalty() != null){
                    totalPenalty = totalPenalty.add(result.getPenalty());
                }
				if(result.getFine() != null){
                    totalFine = totalFine.add(result.getFine());
                }
				result.setUpdatedBy(user);
				result.setUpdatedDatetime(new Date());
				this.em.merge(result);
			}else{
				result.setInvsInspectation(invsInspectation);
				if(result.getPenalty() != null){
				    totalPenalty = totalPenalty.add(result.getPenalty());
				}
				if(result.getFine() != null){
				    totalFine = totalFine.add(result.getFine());
				}
				result.setCreatedBy(user);
				result.setCreatedDatetime(new Date());
				result.setUpdatedBy(user);
				result.setUpdatedDatetime(new Date());
				result.setDefunctInd("N");
				this.em.persist(result);
			}
		}
		
		invsInspectation.setTotalPenalty(totalPenalty);
		invsInspectation.setTotalFine(totalFine);
		invsInspectation.setUpdatedBy(user);
		invsInspectation.setUpdatedDatetime(new Date());
		this.em.merge(invsInspectation);
	}
	
	
	/**
	 * <p>Descrioption:保存稽查信息及其字表信息</p>
	 * @param invsInspectation
	 * @param companyId
	 * @param wfNo
	 * @param inspectationResult
	 */
	public void saveInvsInspectation(InvsInspectation invsInspectation, List<InvsInspectationResult> inspectationResult)throws Exception{
		String user = loginService.getCurrentUsermstr().getAdAccount();
		String companyName = getComapnyName(invsInspectation.getCompanymstrId());
		BigDecimal totalPenalty = BigDecimal.ZERO;
		BigDecimal totalFine = BigDecimal.ZERO;
		
		invsInspectation.setCompanyName(companyName);
		invsInspectation.setTotalPenalty(totalPenalty);
		invsInspectation.setTotalFine(totalFine);
		invsInspectation.setCreatedBy(user);
		invsInspectation.setCreatedDatetime(new Date());
		invsInspectation.setUpdatedBy(user);
		invsInspectation.setUpdatedDatetime(new Date());
		invsInspectation.setDefunctInd("N");
		em.persist(invsInspectation);
		
		for(InvsInspectationResult result : inspectationResult){
			if(result.getTaxType() != null){
				totalPenalty = totalPenalty.add(result.getPenalty());
				totalFine = totalFine.add(result.getFine());
				result.setInvsInspectation(invsInspectation);
				result.setCreatedBy(user);
				result.setCreatedDatetime(new Date());
				result.setUpdatedBy(user);
				result.setUpdatedDatetime(new Date());
				result.setDefunctInd("N");
				this.em.persist(result);
			}
		}
		invsInspectation.setTotalPenalty(totalPenalty);
		invsInspectation.setTotalFine(totalFine);
		this.em.merge(invsInspectation);
	}
	
	public void updateInspectionDraft(InvsInspectation invsInspectation,List<InvsInspectationResult> inspectationResult) throws Exception{
		saveInspectationHistory(invsInspectation.getId());
		updateInspectation(invsInspectation,inspectationResult);
	}
	
	/**
	 * 在流程过程中保存或更新
	 * @param invsInspectation
	 * @param inspectationResult
	 * @throws Exception 
	 */
	public void saveOrUpdateInspectation(InvsInspectation invsInspectation, List<InvsInspectationResult> inspectationResult) throws Exception{
		if(invsInspectation.getId() != null){ //判断是跟新操作还是保存操作
			if(validateUpdate(invsInspectation, inspectationResult)){//验证稽查信息是否修改
				saveInspectationHistory(invsInspectation.getId());
				updateInspectation(invsInspectation, inspectationResult);
			}
		}else{
			saveInvsInspectation(invsInspectation, inspectationResult);
		}
	}
	
	public void saveOrUpdatedInDraft(InvsInspectation invsInspectation, List<InvsInspectationResult> inspectationResult) throws Exception{
		if(invsInspectation.getId() != null){ //判断是跟新操作还是保存操作
			String companyName = getComapnyName(invsInspectation.getCompanymstrId());
			invsInspectation.setCompanyName(companyName);
			updateInspectation(invsInspectation, inspectationResult);
		}else{
			saveInvsInspectation(invsInspectation, inspectationResult);
		}
	}
	
	/**
	 * <p>Descrioption:通过公司ID查找公司名称</p>
	 * @param id
	 */
	public String getComapnyName(Long id){
		String sql = "select o.stext from Companymstr c,O o where c.oid = o.id and c.id = "+ id +" and c.defunctInd <> 'Y' and o.defunctInd <> 'Y'";
		List list = this.em.createQuery(sql).getResultList();
		if(list.size() > 0){
			logger.info("公司名称："+list.get(0));
		    return (String)list.get(0);
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> findTransCompanysByPositon(String positon) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append("select c.id from Usermstr u,Userpositionorg up,Companymstr c,Positionorg po,Position p");
        sb.append(" where u.id = up.usermstr.id and up.positionorg.id = po.id and po.oid = c.oid and po.position.id = p.id");
        sb.append(" and p.code = '"+positon+"'");
        sb.append(" and po.defunctInd <> 'Y'");
        sb.append(" and up.defunctInd <> 'Y'");
        sb.append(" and u.adAccount = '"+loginService.getCurrentUserName()+"'");
        return em.createQuery(sb.toString()).getResultList();
    }
	
	public List<Long> findTransCompanysByPositons(String lookPositon, String managerPositon) throws Exception{
	    StringBuffer sb = new StringBuffer();
	    sb.append("select distinct c.id from Usermstr u,Userpositionorg up,Companymstr c,Positionorg po,Position p");
	    sb.append(" where u.id = up.usermstr.id and up.positionorg.id = po.id and po.oid = c.oid and po.position.id = p.id");
	    sb.append(" and (p.code = '"+lookPositon.trim()+"' or p.code = '" + managerPositon + "' )");
	    sb.append(" and po.defunctInd <> 'Y'");
	    sb.append(" and up.defunctInd <> 'Y'");
	    sb.append(" and u.adAccount = '"+loginService.getCurrentUserName()+"'");
	    return em.createQuery(sb.toString()).getResultList();
	}
	
	public List<Long> findCompanysByPositonAndCompanyId(String positon, Long id) throws Exception{
	    StringBuffer sb = new StringBuffer();
	    sb.append("select distinct c.id from Usermstr u,Userpositionorg up,Companymstr c,Positionorg po,Position p");
	    sb.append(" where u.id = up.usermstr.id and up.positionorg.id = po.id and po.oid = c.oid and po.position.id = p.id");
	    sb.append(" and p.code = '"+positon+"'");
	    sb.append(" and c.id = "+id+"");
	    sb.append(" and po.defunctInd <> 'Y'");
	    sb.append(" and up.defunctInd <> 'Y'");
	    sb.append(" and u.adAccount = '"+loginService.getCurrentUserName()+"'");
	    return em.createQuery(sb.toString()).getResultList();
	}
	
	/**
	 * 
	 * <p>Description: 流程中的验证</p>
	 * @throws Exception
	 */
	public Boolean isValidate(FacesContext context,InvsInspectation invsInspectation) throws Exception{
		if(!validateForm(context, invsInspectation)) {
			throw new Exception("请仔细核对校验");
		}else{
			return true;
		}
	}
	
	
	public boolean validateForm(FacesContext context, InvsInspectation invsInspectation) {
		return ValidateUtil.validateRequired(context, invsInspectation.getInspectOrg(), "稽（检）查机关：") 
                &ValidateUtil.validateStartTimeGTEndTime(context, invsInspectation.getInspectStartDatetime(), invsInspectation.getInspectEndDatetime(), "稽（检）查业务期间：","结束时间必须晚于开始时间")
                &ValidateUtil.validateStartTimeGTEndTime(context, invsInspectation.getMissionStartDatetime(), invsInspectation.getMissionEndDatetime(), "稽（检）查时间：","结束时间必须晚于开始时间")
                &ValidateUtil.validateStartTimeAndEndTime(context, invsInspectation.getInspectStartDatetime(), invsInspectation.getMissionStartDatetime(), "稽（检）查业务期间：","稽（检）查时间开始时间不能早于稽（检）查业务期间开始时间")
                &ValidateUtil.validateStartTimeGTEndTime(context, invsInspectation.getInspectEndDatetime(), invsInspectation.getMissionEndDatetime(), "稽（检）查业务期间：","稽（检）查时间结束时间必须晚于稽（检）查业务期间结束时间");
    }
	
	/**
	 * <p>Description:验证稽查信息是否修改；如果修改，返回true；否则，返回false</p>
	 * @param invsInspectation 稽查信息
	 * @param inspectationResult 稽查明细
	 * @return
	 */
	public boolean validateUpdate(InvsInspectation invsInspectation,List<InvsInspectationResult> inspectationResult){
	    List<InvsInspectation> list = getInspection(invsInspectation.getId());
	    InvsInspectation ins = null;
	    List<InvsInspectationResult> listResults = null;
	    if(list.size() > 0){
	        ins = list.get(0);
	        listResults = ins.getInvsInspectationResults();
	    }
	    if(!invsInspectation.getInspectOrg().trim().equals(ins.getInspectOrg().trim())){
	        return true;
	    }else if (!invsInspectation.getContact().equals(ins.getContact())) {
            return true;
        }else if (!invsInspectation.getContactNum().equals(ins.getContactNum())) {
            return true;
        }else if (!invsInspectation.getMainProblemDesc().equals(ins.getMainProblemDesc())) {
            return true;
        }else if (!invsInspectation.getRectificationPlan().equals(ins.getRectificationPlan())) {
            return true;
        }else if (!invsInspectation.getRectificationResult().equals(ins.getRectificationResult())) {
            return true;
        }
	    
	    if(validateString(invsInspectation.getTaxTypes(),ins.getTaxTypes())){
	    	return true;
	    }
	    
	    if(validateString(invsInspectation.getInspectType(), ins.getInspectType())){
	        return true;
	    }
	    
	    if(validateTwoTime(invsInspectation.getInspectStartDatetime(), ins.getInspectStartDatetime())){
	        return true;
	    }
	    if(validateTwoTime(invsInspectation.getInspectEndDatetime(), ins.getInspectEndDatetime())){
	        return true;
	    }
	    if(validateTwoTime(invsInspectation.getMissionStartDatetime(), ins.getMissionStartDatetime())){
	        return true;
	    }
	    if(validateTwoTime(invsInspectation.getMissionEndDatetime(), ins.getMissionEndDatetime())){
	        return true;
	    }
	    
	    if(inspectationResult.size() != listResults.size()){
	    	return true;
	    }
	    
	    if(listResults.size() > 0){
	        for(InvsInspectationResult result : inspectationResult){
	        	if(result.getId() == null){
	        		return true;
	        	}
	            if(!listResults.contains(result)){
	                return true;
	            }else{
	                InvsInspectationResult insResult = getInsResult(listResults, result.getId());
	                if(!result.getTaxType().equals(insResult.getTaxType())){
	                    return true;
	                }else if (!result.getOverdueTax().equals(insResult.getOverdueTax())) {
	                    return true;
                    }else if (!result.getPenalty().equals(insResult.getPenalty())) {
                        return true;
                    }else if (!result.getInputTaxTurnsOut().equals(insResult.getInputTaxTurnsOut())) {
                        return true;
                    }else if (!result.getReductionPrevLoss().equals(insResult.getReductionPrevLoss())) {
                        return true;
                    }else if (!result.getFine().equals(insResult.getFine())) {
                        return true;
                    }else if (!result.getSituationRemarks().equals(insResult.getSituationRemarks())) {
                        return true;
                    }
	            }
	        }
	    }
        return false;
	}
	
	public boolean validateString(String str1, String str2){
	    if(str1 != null && str2 != null){
	        if(!str1.equals(str2)){
	            return true;
	        }
	    }
	    if(str1 != null && str2 == null){
	        return true;
	    }
	    if(str1 == null && str2 != null){
	        return true;
	    }
	    return false;
	}
	
	private Boolean validateTwoTime(Date antiAvoidanceTime, Date antiTime){
        if(antiAvoidanceTime == null && antiTime != null){
            return true;
        }
        if(antiAvoidanceTime != null && antiTime == null){
            return true;
        }
        if(antiAvoidanceTime != null && antiTime != null){
            if(!antiAvoidanceTime.equals(antiTime)){
                return true;
            }
        }
        return false;
    }
	
	public InvsInspectationResult getInsResult(List<InvsInspectationResult> result, Long id){
	    InvsInspectationResult insResult = new InvsInspectationResult();
	    for(InvsInspectationResult re : result){
	        if(re.getId().equals(id)){
	            insResult = re;
	        }
	    }
        return insResult;
	}
	
	/**
	 * <p>Description: 将税种List集合转换为String类型</p>
	 */
    public String taxTypeToString(List<String> taxTypes) {
        if(taxTypes.size() > 0){
			String taxType = taxTypes.toString();
			return taxType.substring(1, taxType.length()-1).replace(" ", "");
		}
        return null;
    }
    
    /**
	 * <p>Description: 将税种的String类型转换为List集合</p>
	 */
    public List<String> taxTypeToList(String taxType) {
        String[] arrayStr = taxType.split(",");
        List<String> taxTypes = new ArrayList<String>();
        for(int i = 0 ; i < arrayStr.length ; i++){
            taxTypes.add(arrayStr[i].trim());
        }
        return taxTypes;
    }
	
}
