package com.wcs.tih.feedback.service;

import java.io.Serializable;
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
import com.wcs.tih.feedback.controller.vo.SearchAntiVo;
import com.wcs.tih.model.InvsAntiAvoidance;
import com.wcs.tih.model.InvsAntiAvoidanceHistory;
import com.wcs.tih.model.InvsAntiResult;
import com.wcs.tih.model.InvsAntiResultHistory;
import com.wcs.tih.report.controller.vo.AntiAvoidanceVo;
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
public class FeedBackAntiService implements Serializable{

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	private EntityManager em;
	@EJB 
	private LoginService loginService;
	@EJB
	private FeedBackInspectationService inspectationService;
	
	/**
	 * <p>Description:保存反避税信息及其字表<p>
	 * @param invsAntiAvoidance
	 * @param invsAntiResult
	 * @param wfId
	 * @param companyId
	 */
	public void saveAntiAndResult(InvsAntiAvoidance invsAntiAvoidance, List<InvsAntiResult> invsAntiResult) throws Exception{
		String user = loginService.getCurrentUsermstr().getAdAccount();
		String companyName = inspectationService.getComapnyName(invsAntiAvoidance.getCompanymstrId());
		invsAntiAvoidance.setCompanyName(companyName);
		if(invsAntiAvoidance.getRiskAccount() == null){
			invsAntiAvoidance.setRiskAccount(BigDecimal.ZERO);
		}
		invsAntiAvoidance.setCreatedBy(user);
		invsAntiAvoidance.setCreatedDatetime(new Date());
		invsAntiAvoidance.setUpdatedBy(user);
		invsAntiAvoidance.setUpdatedDatetime(new Date());
		invsAntiAvoidance.setDefunctInd("N");
		this.em.persist(invsAntiAvoidance);
		
		for(InvsAntiResult result : invsAntiResult){
			result.setInvsAntiAvoidance(invsAntiAvoidance);
			result.setCreatedBy(user);
			result.setCreatedDatetime(new Date());
			result.setUpdatedBy(user);
			result.setUpdatedDatetime(new Date());
			result.setDefunctInd("N");
			this.em.persist(result);
		}
	}
	
	/**
	 * <p>Description:修改反避税中的信息<p>
	 * @param invsAntiAvoidance
	 * @param invsAntiResults
	 */
	public void updateAntiAndResult(InvsAntiAvoidance invsAntiAvoidance, List<InvsAntiResult> invsAntiResults) throws Exception{
		String user = loginService.getCurrentUsermstr().getAdAccount();
		
		for(InvsAntiResult result : invsAntiResults){
			if(result.getId() != null){
				result.setUpdatedBy(user);
				result.setUpdatedDatetime(new Date());
				this.em.merge(result);
			}else{
				result.setInvsAntiAvoidance(invsAntiAvoidance);
				result.setCreatedBy(user);
				result.setCreatedDatetime(new Date());
				result.setUpdatedBy(user);
				result.setUpdatedDatetime(new Date());
				result.setDefunctInd("N");
				this.em.persist(result);
			}
		}
		
		invsAntiAvoidance.setUpdatedBy(user);
		invsAntiAvoidance.setUpdatedDatetime(new Date());
		this.em.merge(invsAntiAvoidance);
	}
	
	public void removeResult(InvsAntiResult result){
	    InvsAntiResult antiResult = this.em.find(InvsAntiResult.class, result.getId());
	    this.em.remove(antiResult);
	}
	
	/**
	 * 物理删除反避税信息
	 * @param id
	 */
	public void deleteAntiAndResult(Long id){
		List<InvsAntiAvoidance> antiLists = getAntiByWfId(id);
		if(antiLists.size() > 0){
			InvsAntiAvoidance avoidance = antiLists.get(0);
			List<InvsAntiResult> results = avoidance.getInvsAntiResults();
			if(results.size() > 0){
				for(InvsAntiResult result : results){
					this.em.remove(result);
				}
			}
			this.em.remove(avoidance);
		}
	}
	
	/**
	 * <p>Description:新增历史信息<p>
	 * @param id 主表ID
	 */
	public void saveHistoryAndResult(Long id) throws Exception{
		InvsAntiAvoidance antiAvoidance = getInvsAntiAvoidance(id, "N").get(0);
		
		InvsAntiAvoidanceHistory history = new InvsAntiAvoidanceHistory();
		if(getHistory(id).size() > 0){
			history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_2);
		}else{
			history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_1);
		}
		history.setInvsAntiAvoidance(antiAvoidance);
		history.setWfId(antiAvoidance.getWfId());
		history.setCompanymstrId(antiAvoidance.getCompanymstrId());
		history.setCompanyName(antiAvoidance.getCompanyName());
		history.setSponsorOrg(antiAvoidance.getSponsorOrg());
		history.setImplementOrg(antiAvoidance.getImplementOrg());
		history.setCause(antiAvoidance.getCause());
		history.setInvestType(antiAvoidance.getInvestType());
		history.setTaxTypes(antiAvoidance.getTaxTypes());
		history.setMissionStartDatetime(antiAvoidance.getMissionStartDatetime());
		history.setMissionEndDatetime(antiAvoidance.getMissionEndDatetime());
		history.setInvestStartDatetime(antiAvoidance.getInvestStartDatetime());
		history.setInvestEndDatetime(antiAvoidance.getInvestEndDatetime());
		history.setMethod(antiAvoidance.getMethod());
		history.setDoubt(antiAvoidance.getDoubt());
		history.setDealWith(antiAvoidance.getDealWith());
		history.setRiskAccount(antiAvoidance.getRiskAccount());
		history.setPhaseRemarks(antiAvoidance.getPhaseRemarks());
		history.setConclusion(antiAvoidance.getConclusion());
		history.setTraceStartDatetime(antiAvoidance.getTraceStartDatetime());
		history.setTraceEndDatetime(antiAvoidance.getTraceEndDatetime());
		history.setContact(antiAvoidance.getContact());
		history.setContactNum(antiAvoidance.getContactNum());
		history.setCreatedBy(antiAvoidance.getUpdatedBy());
		history.setCreatedDatetime(antiAvoidance.getUpdatedDatetime());
		history.setUpdatedBy(antiAvoidance.getUpdatedBy());
		history.setUpdatedDatetime(antiAvoidance.getUpdatedDatetime());
		history.setDefunctInd("N");
		this.em.persist(history);
		
		List<InvsAntiResult> antiResults = antiAvoidance.getInvsAntiResults();
		for(InvsAntiResult result : antiResults){
			InvsAntiResultHistory resultHistory = new InvsAntiResultHistory();
			resultHistory.setInvsAntiAvoidanceHistory(history);
			resultHistory.setVat(result.getVat());
			resultHistory.setCit(result.getVat());
			resultHistory.setAddInterest(result.getAddInterest());
			resultHistory.setAddFine(result.getAddFine());
			resultHistory.setReducedLoss(result.getReducedLoss());
			resultHistory.setOperateInd("N");
			resultHistory.setDefunctInd("N");
			resultHistory.setCreatedBy(result.getUpdatedBy());
			resultHistory.setCreatedDatetime(result.getUpdatedDatetime());
			resultHistory.setUpdatedBy(result.getUpdatedBy());
			resultHistory.setUpdatedDatetime(result.getUpdatedDatetime());
			this.em.persist(resultHistory);
		}
	}
	
	/**
	 * <p>Description:通过主表ID查找反避税信息<p>
	 * @param id 主表ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsAntiAvoidance> getInvsAntiAvoidance(Long id, String defunctInd){
		StringBuffer sb = new StringBuffer();
		sb.append("select anti from InvsAntiAvoidance anti ,InvsAntiResult result left  join fetch anti.invsAntiResults where anti.id = " + id + " ");
		if(defunctInd != null && !"".equals(defunctInd)){
			sb.append(" and anti.defunctInd = '"+defunctInd+"' ");
		}
		return this.em.createQuery(sb.toString()).getResultList();
	}
	
	/**
	 * 
	 * @param id 主表id
	 */
	@SuppressWarnings("unchecked")
	public List<InvsAntiResult> getAntiResult(Long id){
		String sql = "select result from InvsAntiResult result where result.invsAntiAvoidance.id = " + id + " ";
		return this.em.createQuery(sql).getResultList();
	}
	
	/**
	 * <p>Description:通过流程ID查找反避税信息<p>
	 * @param wfId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvsAntiAvoidance> getAntiByWfId(Long wfId){
		String wf = wfId.toString();
		String sql = "select anti from InvsAntiAvoidance anti left  join fetch anti.invsAntiResults where anti.wfId = " + wf + "  and anti.defunctInd <> 'Y' ";
		return this.em.createQuery(sql).getResultList();
	}
	
	/**
	 * 
	 * @param id 主表ID
	 */
	@SuppressWarnings("unchecked")
	public List<InvsAntiAvoidanceHistory> getHistory(Long id){
		String sql = "select history from InvsAntiAvoidanceHistory history left join fetch history.invsAntiResultHistories  where history.invsAntiAvoidance.id = " + id + " order by history.updatedDatetime DESC";
		return this.em.createQuery(sql).getResultList();
	}
	
	/**
	 * <p>Description: </p>
	 * @param id 历史表ID
	 * @return
	 */
	public InvsAntiAvoidanceHistory getSpecialHistory(Long id){
		String sql = "select history from InvsAntiAvoidanceHistory history left join fetch history.invsAntiResultHistories  where history.id = " + id + " ";
		return (InvsAntiAvoidanceHistory) this.em.createQuery(sql).getResultList().get(0);
	}
	
	public List<AntiAvoidanceVo> getSearchForm(SearchAntiVo searchAntiVo, String antiPositionLook, String antiPsoitionManager) throws Exception{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String select = "select aa.id"+
				",aa.COMPANYMSTR_ID"+
				",aa.COMPANY_NAME"+
				",aa.SPONSOR_ORG"+
				",aa.IMPLEMENT_ORG"+
				",aa.CAUSE"+
				",aa.INVEST_TYPE"+
				",aa.TAX_TYPES"+
				",aa.MISSION_START_DATETIME"+
				",aa.MISSION_END_DATETIME"+
				",aa.INVEST_START_DATETIME"+
				",aa.INVEST_END_DATETIME"+
				",aa.METHOD"+
				",aa.DOUBT"+
				",aa.RISK_ACCOUNT"+
				",aa.DEAL_WITH"+
				",aa.PHASE_REMARKS"+
				",aa.CONCLUSION"+
				",aa.TRACE_START_DATETIME"+
				",aa.TRACE_END_DATETIME"+
				",aa.CONTACT"+
				",aa.DEFUNCT_IND" +
				",aa.WF_ID";
		select = select +",wf.status,p.value,temp.sumvat,temp.sumcit,temp.sumit,temp.sumfine,temp.sumloss";
		StringBuffer jpql = new StringBuffer();
		jpql.append(" (");
		jpql.append("select aa.id as sumid,sum(re.VAT) as sumvat,sum(re.CIT) as sumcit,sum(re.ADD_INTEREST) as sumit,sum(re.ADD_FINE) as sumfine,sum(re.REDUCED_LOSS) as sumloss");
		jpql.append(" from INVS_ANTI_AVOIDANCE aa, INVS_ANTI_RESULT re");
		jpql.append(" where aa.id = re.INVS_ANTI_AVOIDANCE_ID");
		jpql.append(" group by aa.id");
		jpql.append(") as temp ");
		StringBuffer query = new StringBuffer();
		if(searchAntiVo != null){
			String companyName = searchAntiVo.getCompanyName();
			if(companyName!=null && !"".equals(companyName.trim())){
				query.append(" and aa.COMPANY_NAME like '%"+companyName.trim()+"%'");
			}
			Date missionStartDatetime = searchAntiVo.getMissionStartDatetime();
			if (missionStartDatetime!=null) {
				query.append(" and aa.MISSION_START_DATETIME >= '" + df.format(missionStartDatetime) + "'");
			}
			Date missionEndDatetime = searchAntiVo.getMissionEndDatetime();
			if (missionEndDatetime!=null) {
				query.append(" and aa.MISSION_END_DATETIME <= '" + df.format(missionEndDatetime) + "'");
			}
			Date investStartDatetime = searchAntiVo.getInvestStartDatetime();
			if (investStartDatetime!=null) {
				query.append(" and aa.INVEST_START_DATETIME >= '" + df.format(investStartDatetime) + "'");
			}
			Date investEndDatetime = searchAntiVo.getInvestEndDatetime();
			if (investEndDatetime!=null) {
				query.append(" and aa.INVEST_END_DATETIME <= '" + df.format(investEndDatetime) + "'");
			}
			String investType = searchAntiVo.getInvestType();
			if(investType != null && !"".equals(investType.trim())){
				query.append(" and aa.INVEST_TYPE = '"+investType+"'");
			}
			String defunctInd = searchAntiVo.getDefunctInd();
			if(defunctInd!=null){
				query.append(" and aa.DEFUNCT_IND = '"+defunctInd+"'");
			}else{
				query.append(" and aa.DEFUNCT_IND = 'N'");
			}
		}
		query.append(" and aa.COMPANYMSTR_ID in (");
		query.append("select distinct c.id from COMPANYMSTR c,POSITIONORG po,POSITION p,USERPOSITIONORG up,USERMSTR u " +
				"where c.OID = po.OID and p.id = po.POSITION_ID and (p.code = '"+antiPositionLook+"' or p.code = '"+antiPsoitionManager+"') " +
				"and po.id = up.POSITIONORG_ID and u.id = up.USERMSTR_ID and u.AD_ACCOUNT = '"+loginService.getCurrentUserName()+"' and c.DEFUNCT_IND <> 'Y' and po.DEFUNCT_IND <> 'Y' and p.DEFUNCT_IND <> 'Y' and up.DEFUNCT_IND <> 'Y' and u.DEFUNCT_IND <> 'Y'");
		query.append(")");
		query.append(" order by aa.UPDATED_DATETIME DESC");
		String from = " from WF_INSTANCEMSTR wf,WF_INSTANCEMSTR_PROPERTY p,INVS_ANTI_AVOIDANCE aa";
		String join = " left join " + jpql.toString() + " on aa.id = temp.sumid";
		String where = " where aa.WF_ID = wf.id and wf.id = p.WF_INSTANCEMSTR_ID and (wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_2 +"' or wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_3 +"') and p.name = '"+DictConsts.TIH_TAX_REQUESTFORM_5_4+"'";
		List resultList = em.createNativeQuery(select + from + join + where + query.toString()).getResultList();
		return convertAntiAvoidanceVo(resultList);
	}
	
	public List<AntiAvoidanceVo> convertAntiAvoidanceVo(List aas){
        List<AntiAvoidanceVo> aaVos = new ArrayList<AntiAvoidanceVo>();
        AntiAvoidanceVo aaVo = null;
        for (int i = 0; i < aas.size(); i++) {
            aaVo = new AntiAvoidanceVo();
            Object[] aa = (Object[]) aas.get(i);
            aaVo.setId(aa[0] == null ? null : (Long) aa[0]);
            aaVo.setCompanymstrId(aa[1]== null ? null : (Long) aa[1]);
            aaVo.setCompanyName(aa[2]== null ? "" : (String) aa[2]);
            aaVo.setSponsorOrg(aa[3]== null ? "" : (String) aa[3]);
            aaVo.setImplementOrg(aa[4]== null ? "" : (String) aa[4]);
            aaVo.setCause(aa[5]== null ? "" : (String) aa[5]);
            aaVo.setInvestType(aa[6]== null ? "" : inspectationService.getDictValueByKey((String) aa[6]));
            aaVo.setTaxTypes(aa[7]== null ? "" : inspectationService.getTaxValue((String) aa[7]));
            aaVo.setMissionStartDatetime(aa[8]== null ? null : (Date) aa[8]);
            aaVo.setMissionEndDatetime(aa[9]== null ? null : (Date) aa[9]);
            aaVo.setInvestStartDatetime(aa[10]== null ? null : (Date) aa[10]);
            aaVo.setInvestEndDatetime(aa[11]== null ? null : (Date) aa[11]);
            aaVo.setMethod(aa[12]== null ? "" : (String) aa[12]);
            aaVo.setDoubt(aa[13]== null ? "" : (String) aa[13]);
            aaVo.setRiskAccount(aa[14]== null ? null : (BigDecimal) aa[14]);
            aaVo.setDealWith(aa[15]== null ? "" : (String) aa[15]);
            aaVo.setPhaseRemarks(aa[16]== null ? "" : (String) aa[16]);
            aaVo.setConclusion(aa[17]== null ? "" : (String) aa[17]);
            aaVo.setTraceStartDatetime(aa[18]== null ? null : (Date) aa[18]);
            aaVo.setTraceEndDatetime(aa[19]== null ? null : (Date) aa[19]);
            aaVo.setContact(aa[20]== null ? "" : (String) aa[20]);
            aaVo.setDefunctInd(aa[21]== null ? "" : (String) aa[21]);
            aaVo.setWfId(aa[22] == null ? null : (Long) aa[22]);
            
            aaVo.setWfStatus(aa[23]== null ? "" : inspectationService.getDictValueByKey((String) aa[23]));
            aaVo.setStage(aa[24]==null ? "" : inspectationService.getDictValueByKey((String) aa[24]));
            
            aaVo.setVat(aa[25]==null ? 0 : ((BigDecimal) aa[25]).doubleValue());
            aaVo.setCit(aa[26]==null ? 0 : ((BigDecimal) aa[26]).doubleValue());
            aaVo.setCitAndVat(ArithUtil.add(aaVo.getCit(), aaVo.getVat()));
            aaVo.setAddInterest(aa[27]==null ? 0 : ((BigDecimal) aa[27]).doubleValue());
            aaVo.setAddFine(aa[28]==null ? 0 : ((BigDecimal) aa[28]).doubleValue());
            aaVo.setReducedLoss(aa[29]==null ? 0 : ((BigDecimal) aa[29]).doubleValue());
            aaVos.add(aaVo);
        }
        return aaVos;
    }
	
	
    public void deleteAnti(Long id){
		List<InvsAntiAvoidance> anti = getInvsAntiAvoidance(id, "N");
		if(anti.size() > 0){
			InvsAntiAvoidance avoidance = anti.get(0);
			avoidance.setDefunctInd("Y");
			List<InvsAntiResult> antiResult = getAntiResult(id);
			for(InvsAntiResult result : antiResult){
				result.setDefunctInd("Y");
				this.em.merge(result);
			}
			this.em.merge(avoidance);
		}
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
	
	public List<Long> deleteAuthorityService(String positon, Long id) throws Exception{
	    List<Long> companyId = new ArrayList<Long>();
	    companyId = inspectationService.findCompanysByPositonAndCompanyId(positon, id);
	    return companyId;
	}
	
	
	public Boolean vaildate(FacesContext context,InvsAntiAvoidance antiAvoidance,List<InvsAntiResult> antiResults) throws Exception{
		if(!validateForm(context,antiAvoidance)) {
			throw new Exception("请仔细核对校验");
		}else{
			return true;
		}
    }
	
	
	/**
	 * 必填字段和日期校验
	 * @param context
	 * @param antiAvoidance
	 * @return
	 */
	public boolean validateForm(FacesContext context, InvsAntiAvoidance antiAvoidance) {
		return ValidateUtil.validateRequired(context, antiAvoidance.getSponsorOrg(), "发起机关：")
				&ValidateUtil.validateStartTimeGTEndTime(context, antiAvoidance.getInvestStartDatetime(), antiAvoidance.getInvestEndDatetime(), "调查/评估开始时间：","结束时间必须晚于开始时间")
				&ValidateUtil.validateStartTimeGTEndTime(context, antiAvoidance.getMissionStartDatetime(), antiAvoidance.getMissionEndDatetime(), "任务处理时间：","结束时间必须晚于开始时间")
				&ValidateUtil.validateStartTimeGTEndTime(context, antiAvoidance.getTraceStartDatetime(), antiAvoidance.getTraceEndDatetime(), "税务跟踪期：","结束时间必须晚于开始时间")
				&ValidateUtil.validateStartTimeAndEndTime(context, antiAvoidance.getInvestStartDatetime(), antiAvoidance.getMissionStartDatetime(), "任务处理时间：","任务处理开始时间不能早于调查/评估开始时间")
				&ValidateUtil.validateStartTimeGTEndTime(context, antiAvoidance.getInvestEndDatetime(), antiAvoidance.getMissionEndDatetime(), "任务处理时间：","任务处理结束时间必须晚于调查/评估结束时间");
    }
	
	/**
	 * 将税种的List集合转换为String类型
	 * @return
	 */
	public String taxTypeToString(List<String> taxTypes) {
		return inspectationService.taxTypeToString(taxTypes);
    }
	
	/**
	 * 在草稿中保存或更新反避税信息
	 * @param invsAntiAvoidance
	 * @param invsAntiResult
	 */
	public void saveOrUpdatedInDraft(InvsAntiAvoidance invsAntiAvoidance, List<InvsAntiResult> invsAntiResults) throws Exception{
		if(invsAntiAvoidance.getId() != null){//判断是跟新操作还是保存操作
			String companyName = inspectationService.getComapnyName(invsAntiAvoidance.getCompanymstrId());
			invsAntiAvoidance.setCompanyName(companyName);
			updateAntiAndResult(invsAntiAvoidance,invsAntiResults);
		}else{
			saveAntiAndResult(invsAntiAvoidance,invsAntiResults);
		}
	}
	
	public void saveOrUpdateWF(InvsAntiAvoidance invsAntiAvoidance, List<InvsAntiResult> invsAntiResults) throws Exception{
		if(invsAntiAvoidance.getId() != null){//判断是跟新操作还是保存操作
			if(validateUpdate(invsAntiAvoidance, invsAntiResults)){//如果反避税信息被修改过，则更新反避税信息
				saveHistoryAndResult(invsAntiAvoidance.getId());
				updateAntiAndResult(invsAntiAvoidance,invsAntiResults);
			}
		}else{
			saveAntiAndResult(invsAntiAvoidance,invsAntiResults);
		}
	}
	
	public void saveAndUpdate(InvsAntiAvoidance invsAntiAvoidance, List<InvsAntiResult> invsAntiResults) throws Exception{
		saveHistoryAndResult(invsAntiAvoidance.getId());
		updateAntiAndResult(invsAntiAvoidance, invsAntiResults);
	}
	
    public void updateAnti(Long id) throws Exception{
    	saveHistoryAndResult(id);
    	saveHistoryAndResult(id);
    }
	
	/**
	 * 验证反避税信息是否被修改；如果修改，返回true；否则，返回false
	 * @param antiAvoidance
	 * @param antiResults
	 * @return
	 */
	public boolean validateUpdate(InvsAntiAvoidance antiAvoidance, List<InvsAntiResult> antiResults){
        List<InvsAntiAvoidance> list = getInvsAntiAvoidance(antiAvoidance.getId(), "");
        InvsAntiAvoidance anti = null;
        List<InvsAntiResult> listResult = null;
        if(list.size() > 0){
            anti = list.get(0);
            listResult = anti.getInvsAntiResults();
        }
        if(!(antiAvoidance.getSponsorOrg().trim()).equals(anti.getSponsorOrg().trim())){
            return true;
        }else if(!antiAvoidance.getImplementOrg().equals(anti.getImplementOrg())){
            return true;
        }else if (!antiAvoidance.getCause().equals(anti.getCause())) {
            return true;
        }else if (!antiAvoidance.getMethod().equals(anti.getMethod())) {
            return true;
        }else if (!antiAvoidance.getDoubt().equals(anti.getDoubt())) {
            return true;
        }else if (!antiAvoidance.getRiskAccount().equals(anti.getRiskAccount())) {
            return true;
        }else if (!antiAvoidance.getPhaseRemarks().equals(anti.getPhaseRemarks())) {
            return true;
        }else if (!antiAvoidance.getConclusion().equals(anti.getConclusion())) {
            return true;
        }else if (!antiAvoidance.getContact().equals(anti.getContact())) {
            return true;
        }else if (!antiAvoidance.getContactNum().equals(anti.getContactNum())) {
            return true;
        }
        
        if(validateString(antiAvoidance.getDealWith(), anti.getDealWith())){
        	return true;
        }
        
        if(validateString(antiAvoidance.getTaxTypes(), anti.getTaxTypes())){
            return true;
        }
        
        if(validateString(antiAvoidance.getInvestType(), anti.getInvestType())){
            return true;
        }
        
        if(validateTwoTime(antiAvoidance.getInvestStartDatetime(), anti.getInvestStartDatetime())){
            return true;
        }
        
        if(validateTwoTime(antiAvoidance.getInvestEndDatetime(), anti.getInvestEndDatetime())){
            return true;
        }
        
        if(validateTwoTime(antiAvoidance.getMissionStartDatetime(), anti.getMissionStartDatetime())){
            return true;
        }
        
        if(validateTwoTime(antiAvoidance.getMissionEndDatetime(), anti.getMissionEndDatetime())){
            return true;
        }
        
        if(validateTwoTime(antiAvoidance.getTraceStartDatetime(), anti.getTraceStartDatetime())){
            return true;
        }
        if(validateTwoTime(antiAvoidance.getTraceEndDatetime(), anti.getTraceEndDatetime())){
            return true;
        }
        
        for(InvsAntiResult result : antiResults){
            if(!listResult.contains(result)){
                logger.info("~~~~不包含~~~~~");
                return true;
            }else {
                logger.info("~~~~包含~~~~~");
                InvsAntiResult antiResult = antiResult(listResult, result.getId());
                if(!result.getVat().equals(antiResult.getVat())){
                    return true;
                }else if (!result.getCit().equals(antiResult.getCit())) {
                    return true;
                }else if (!result.getAddInterest().equals(antiResult.getAddInterest())) {
                    return true;
                }else if (!result.getAddFine().equals(antiResult.getAddFine())) {
                    return true;
                }else if (!result.getReducedLoss().equals(antiResult.getReducedLoss())) {
                    return true;
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
    
    public InvsAntiResult antiResult(List<InvsAntiResult> listResult, Long id){
        InvsAntiResult antiResult = new InvsAntiResult();
        for(InvsAntiResult re : listResult){
            if(re.getId().equals(id)){
                antiResult = re;
            }
        }
        return antiResult;
    }
    
}
