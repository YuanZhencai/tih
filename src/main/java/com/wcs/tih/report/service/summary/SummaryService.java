package com.wcs.tih.report.service.summary;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.StreamedContent;

import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.base.util.Validate;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.service.CommonService;
import com.wcs.common.service.UserCommonService;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.model.InvsAntiAvoidance;
import com.wcs.tih.model.InvsAntiAvoidanceHistory;
import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.model.InvsInspectationHistory;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.report.controller.vo.AntiAvoidanceVo;
import com.wcs.tih.report.controller.vo.InspectVo;
import com.wcs.tih.transaction.controller.vo.TransferPriceVo;
import com.wcs.tih.transaction.service.TransferPriceService;
import com.wcs.tih.util.ArithUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class SummaryService {
    
    private static ResourceBundle filenetProperties = ResourceBundle.getBundle("filenet");
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private TransferPriceReportService transferPriceReportService;
    @EJB
    private InspectReportSrevice inspectReportSrevice;
    @EJB
    private AntiAvoidanceReportService antiAvoidanceReportService;
    @EJB
    private TransferPriceService transferPriceService;
    @EJB
    private LoginService loginService;
    @EJB
    private FileNetUploadDownload fileUpService;
    @EJB
    private UserCommonService userservice;
    @EJB
    private CommonService commonService;
    
    public String getAppPath(FacesContext context){
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String path = request.getSession().getServletContext().getRealPath("/faces/");
        return new File(path).getParent();
    }
    
    public void summaryByTemplate(String oldPath, String filename, List datas) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        AReportSummary reportSummary = null;
        String classId = filenetProperties.getString("ce.document.classid");
        String folderId = "";
        String excelName = "";
        String reportType = "";
        if(filename.contains("transPrice")){
            reportSummary = transferPriceReportService;
            excelName = "转让定价信息汇总" + "-" + sdf.format(new Date());
            folderId = filenetProperties.getString("ce.folder.reportSummary.transPrice");
            reportType = DictConsts.TIH_TAX_REPORT_4;
        }
        if(filename.contains("antiAvoidance")){
            reportSummary = antiAvoidanceReportService;
            excelName = "反避税信息汇总" + "-" + sdf.format(new Date());
            folderId = filenetProperties.getString("ce.folder.reportSummary.antiAvoidance");
            reportType = DictConsts.TIH_TAX_REPORT_5;
        }
        if(filename.contains("inspect")){
            reportSummary = inspectReportSrevice;
            excelName = "稽查信息汇总" + "-" + sdf.format(new Date());
            folderId = filenetProperties.getString("ce.folder.reportSummary.inspect");
            reportType = DictConsts.TIH_TAX_REPORT_6;
        }
        String temporaryFilename = summary(oldPath, filename, reportSummary, datas);
        Document uploadExcel = uploadExcel(temporaryFilename, excelName, classId, folderId);
        saveReportSummaryHistory(reportType, excelName, uploadExcel.get_Id().toString());
    }
    
    public String summary(String oldPath, String filename, AReportSummary reportSummary, List datas) throws Exception{
        return reportSummary.summary(oldPath, filename, datas);
    }
    
    public List<TransferPriceVo> findTransPricesBy(TransferPriceVo tpVo) throws Exception{
        tpVo.setDefunctInd("N");
        return transferPriceService.findTransPricesBy(tpVo,false);
    }
    
    public List<AntiAvoidanceVo> findAntiAvoidancesBy(AntiAvoidanceVo aa, List<Long> companys) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String select = "select aa.*,wf.status,p.value,temp.*,c.code,lower(c.code) as lowerCode";
        StringBuffer jpql = new StringBuffer();
        jpql.append(" (");
        jpql.append("select aa.id as sumid,sum(re.VAT) as sumvat,sum(re.CIT) as sumcit,sum(re.ADD_INTEREST) as sumit,sum(re.ADD_FINE) as sumfine,sum(re.REDUCED_LOSS) as sumloss");
        jpql.append(" from INVS_ANTI_AVOIDANCE aa, INVS_ANTI_RESULT re");
        jpql.append(" where aa.id = re.INVS_ANTI_AVOIDANCE_ID");
        jpql.append(" group by aa.id");
        jpql.append(") as temp ");
        StringBuffer query = new StringBuffer();
        if(aa!=null){
            if(companys!=null && companys.size()>0){
                String companyIds = "";
                for (int i = 0; i < companys.size(); i++) {
                    companyIds = companyIds + companys.get(i)+(i==companys.size() -1?"":",");
                }
                query.append(" and aa.COMPANYMSTR_ID in ("+companyIds+")");
            }
            Date missionStartDatetime = aa.getMissionStartDatetime();
            if (missionStartDatetime!=null) {
                query.append(" and aa.MISSION_START_DATETIME >= '" + df.format(missionStartDatetime) + "'");
            }
            Date missionEndDatetime = aa.getMissionEndDatetime();
            if (missionEndDatetime!=null) {
                query.append(" and aa.MISSION_END_DATETIME <= '" + df.format(missionEndDatetime) + "'");
            }
            Date investStartDatetime = aa.getInvestStartDatetime();
            if (investStartDatetime!=null) {
                query.append(" and aa.INVEST_START_DATETIME >= '" + df.format(investStartDatetime) + "'");
            }
            Date investEndDatetime = aa.getInvestEndDatetime();
            if (investEndDatetime!=null) {
                query.append(" and aa.INVEST_END_DATETIME <= '" + df.format(investEndDatetime) + "'");
            }
            String status = aa.getWfStatus();
            if(status!=null&&!"".equals(status)){
                query.append(" and wf.status = '"+status+"'");
            }
            String stage = aa.getStage();
            if(stage!=null&&!"".equals(stage)){
                query.append(" and p.value ='"+stage+"'");
            }
        }
        query.append(" and aa.DEFUNCT_IND = 'N'");
        query.append(" order by lower(c.code)");
        String from = " from WF_INSTANCEMSTR wf,WF_INSTANCEMSTR_PROPERTY p,COMPANYMSTR c,INVS_ANTI_AVOIDANCE aa";
        String join = " left join " + jpql.toString() + " on aa.id = temp.sumid";
        String where = " where aa.WF_ID = wf.id and wf.id = p.WF_INSTANCEMSTR_ID and aa.COMPANYMSTR_ID = c.id and (wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_2 +"' or wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_3 +"') and p.name = '"+DictConsts.TIH_TAX_REQUESTFORM_5_4+"'";
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
            aaVo.setInvestType(aa[6]== null ? "" : getDictValueByKey((String) aa[6]));
            aaVo.setInvestStartDatetime(aa[7]== null ? null : (Date) aa[7]);
            aaVo.setInvestEndDatetime(aa[8]== null ? null : (Date) aa[8]);
            aaVo.setMethod(aa[9]== null ? "" : (String) aa[9]);
            aaVo.setDoubt(aa[10]== null ? "" : (String) aa[10]);
            aaVo.setRiskAccount(aa[11]== null ? null : (BigDecimal) aa[11]);
            aaVo.setDealWith(aa[12]== null ? "" : (String) aa[12]);
            aaVo.setPhaseRemarks(aa[13]== null ? "" : (String) aa[13]);
            aaVo.setConclusion(aa[14]== null ? "" : (String) aa[14]);
            aaVo.setTraceStartDatetime(aa[15]== null ? null : (Date) aa[15]);
            aaVo.setTraceEndDatetime(aa[16]== null ? null : (Date) aa[16]);
            aaVo.setContact(aa[17]== null ? "" : (String) aa[17]);
            aaVo.setContactNum(aa[18]== null ? "" : (String) aa[18]);
            //19~24
            aaVo.setMissionStartDatetime(aa[25]== null ? null : (Date) aa[25]);
            aaVo.setMissionEndDatetime(aa[26]== null ? null : (Date) aa[26]);
            aaVo.setTaxTypes(aa[27]== null ? "" : getTaxValue((String) aa[27]));
            aaVo.setWfStatus(aa[28]== null ? "" : getDictValueByKey((String) aa[28]));
            aaVo.setStage(aa[29]==null ? "" : getDictValueByKey((String) aa[29]));
            //30
            aaVo.setVat(aa[31]==null ? 0 : ((BigDecimal) aa[31]).doubleValue());
            aaVo.setCit(aa[32]==null ? 0 : ((BigDecimal) aa[32]).doubleValue());
            aaVo.setCitAndVat(ArithUtil.add(aaVo.getCit(), aaVo.getVat()));
            aaVo.setAddInterest(aa[33]==null ? 0 : ((BigDecimal) aa[33]).doubleValue());
            aaVo.setAddFine(aa[34]==null ? 0 : ((BigDecimal) aa[34]).doubleValue());
            aaVo.setReducedLoss(aa[35]==null ? 0 : ((BigDecimal) aa[35]).doubleValue());
            aaVo.setCompanyCode(aa[36]==null ? "" : (String) aa[36]);
            aaVo.setLowerCode(aa[37]==null ? "" : getDictValueByKey((String) aa[37]));
            aaVos.add(aaVo);
        }
        return aaVos;
    }
    
    public WfInstancemstrProperty findWfPropertyBy(long wfId,String type){
        StringBuffer jpql = new StringBuffer();
        jpql.append("select p from WfInstancemstrProperty p");
        jpql.append(" where p.wfInstancemstr.id = "+wfId);
        jpql.append(" and p.name = '"+type+"'");
        List list = em.createQuery(jpql.toString()).getResultList();
        if(list!=null&&list.size()>0){
            return (WfInstancemstrProperty) list.get(0);
        }else{
            return null;
        }
    }
    
    public List<InspectVo> findInspectsBy(InspectVo it, List<Long> companys) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String select = " select it.*,wf.status,p.value,temp.*,c.code,lower(c.code) as lowerCode";
        StringBuffer jpql = new StringBuffer();
        jpql.append(" (");
        jpql.append("select it.id as sumid,sum(re.OVERDUE_TAX) as sumtax,sum(re.PENALTY) as sumpenalty,sum(re.INPUT_TAX_TURNS_OUT) as sumout,sum(re.REDUCTION_PREV_LOSS) as sumloss,sum(re.FINE) as sumfine");
        jpql.append(" from INVS_INSPECTATION it,INVS_INSPECTATION_RESULT re");
        jpql.append(" where it.id = re.INVS_INSPECTATION_ID");
        jpql.append(" group by it.id");
        jpql.append(") as temp ");
        StringBuffer query = new StringBuffer();
        if(it!=null){
            if(companys!=null && companys.size()>0){
                String companyIds = "";
                for (int i = 0; i < companys.size(); i++) {
                    companyIds = companyIds + companys.get(i)+(i==companys.size() -1?"":",");
                }
                query.append(" and it.COMPANYMSTR_ID in ("+companyIds+")");
            }
            Date missionStartDatetime = it.getMissionStartDatetime();
            if (missionStartDatetime!=null) {
                query.append(" and it.MISSION_START_DATETIME >= '" + df.format(missionStartDatetime) + "'");
            }
            Date missionEndDatetime = it.getMissionEndDatetime();
            if (missionEndDatetime!=null) {
                query.append(" and it.MISSION_END_DATETIME <= '" + df.format(missionEndDatetime) + "'");
            }
            Date inspectStartDatetime = it.getInspectStartDatetime();
            if (inspectStartDatetime!=null) {
                query.append(" and it.INSPECT_START_DATETIME >= '" + df.format(inspectStartDatetime) + "'");
            }
            Date inspectEndDatetime = it.getInspectEndDatetime();
            if (inspectEndDatetime!=null) {
                query.append(" and it.INSPECT_END_DATETIME <= '" + df.format(inspectEndDatetime) + "'");
            }
            String status = it.getWfStatus();
            if(status!=null&&!"".equals(status)){
                query.append(" and wf.status = '"+status+"'");
            }
            String stage = it.getStage();
            if(stage!=null&&!"".equals(stage)){
                query.append(" and p.value ='"+stage+"'");
            }
        }
        query.append(" and it.DEFUNCT_IND = 'N'");
        query.append(" order by lower(c.code)");
        String from = " from WF_INSTANCEMSTR wf,WF_INSTANCEMSTR_PROPERTY p,COMPANYMSTR c,INVS_INSPECTATION it ";
        String join = " left join " + jpql.toString() + " on it.id = temp.sumid";
        String where = " where it.WF_ID = wf.id and wf.id = p.WF_INSTANCEMSTR_ID and it.COMPANYMSTR_ID = c.id and (wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_2 +"' or wf.status = '"+ DictConsts.TIH_TAX_WORKFLOWSTATUS_3 +"') and p.name = '"+DictConsts.TIH_TAX_REQUESTFORM_5_2+"'";
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
            itVo.setContactNum(it[11]== null ? "" : (String) it[11]);
            itVo.setMainProblemDesc(it[12]== null ? "" : (String) it[12]);
            itVo.setRectificationPlan(it[13]== null ? "" : (String) it[13]);
            itVo.setRectificationResult(it[14]== null ? "" : (String) it[14]);
            //15~22
            itVo.setWfStatus(it[23]== null ? "" : getDictValueByKey((String) it[23]));
            itVo.setStage(it[24]== null ? "" : getDictValueByKey((String) it[24]));
            //25
            itVo.setOverdueTax(it[26]==null ? 0 : ((BigDecimal) it[26]).doubleValue());
            itVo.setPenalty(it[27]==null ? 0 : ((BigDecimal) it[27]).doubleValue());
            itVo.setInputTaxTurnsOut(it[28]==null ? 0 : ((BigDecimal) it[28]).doubleValue());
            itVo.setReductionPrevLoss(it[29]==null ? 0 : ((BigDecimal) it[29]).doubleValue());
            itVo.setFine(it[30]==null ? 0 : ((BigDecimal) it[30]).doubleValue());
            double[] values = {itVo.getOverdueTax(),itVo.getPenalty(),itVo.getInputTaxTurnsOut(),itVo.getReductionPrevLoss(),itVo.getFine()};
            itVo.setSum(ArithUtil.sum(values));
            itVo.setCompanyCode(it[31]==null ? "" : (String) it[31]);
            itVo.setLowerCode(it[32]==null ? "" : (String) it[32]);
            itVos.add(itVo);
        }
        return itVos;
    }
    
    public Document uploadExcel(String temporaryFilename, String fileName,String classId, String folderId) throws Exception{
        FileInputStream inputStream = new FileInputStream(temporaryFilename);
        Document document = fileUpService.upLoadDocumentCheckIn(inputStream, new HashMap<String, Object>(), fileName + ".xls", classId, folderId);
        inputStream.close();
        File file = new File(temporaryFilename);
        file.delete();
        return document;
    }
    
    public void saveReportSummaryHistory(String reportType,String filename,String fileId){
        ReportSummaryHistory rsh = new ReportSummaryHistory();
        rsh.setReportType(reportType);
        rsh.setSummaryDatetime(new Date());
        rsh.setName(filename);
        rsh.setFileId(fileId);
        rsh.setDefunctInd("N");
        String currentUserName = this.loginService.getCurrentUserName();
        rsh.setCreatedBy(currentUserName);
        rsh.setCreatedDatetime(new Date());
        rsh.setUpdatedBy(currentUserName);
        rsh.setUpdatedDatetime(new Date());
        this.em.persist(rsh);
    }
    
    public List<ReportSummaryHistory> findSummaryHistoryByType(String reportType) {
        String sql = "select rsh from ReportSummaryHistory rsh where rsh.defunctInd <> 'Y' and rsh.reportType ='" + reportType + "' and rsh.createdBy = '" + this.loginService.getCurrentUserName() + "' order by rsh.summaryDatetime desc";
        return this.em.createQuery(sql).getResultList();
    }

    public StreamedContent download(String documentId) throws Exception {
       return fileUpService.downloadDocumentEncoding(documentId.trim(), "utf-8", "iso8859-1");
    }
    
    /**
     * 按id获取对象.
     */
    public <T> T findUnique(Class<T> entityClass, final Serializable id) {
        Validate.notNull(id, "id不能为空");
        return (T) em.find(entityClass, id);
    }
    
    public InvsAntiAvoidance findAntiAvoidanceById(Long id) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append("select aa from InvsAntiAvoidance aa left join fetch aa.invsAntiResults");
        jpql.append(" where aa.id =?1");
        return (InvsAntiAvoidance) em.createQuery(jpql.toString()).setParameter(1, id).getSingleResult();
    }
    
    public List<InvsAntiAvoidanceHistory> findAntiHistoriesByAntiId(Long antiId)throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append("select distinct(h) from InvsAntiAvoidanceHistory h left join fetch h.invsAntiResultHistories");
        jpql.append(" where h.invsAntiAvoidance.id ="+antiId);
        jpql.append(" order by h.updatedDatetime desc");
        return em.createQuery(jpql.toString()).getResultList();
    }

    public InvsInspectation findInspectById(Long id) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append("select it from InvsInspectation it left join fetch it.invsInspectationResults");
        jpql.append(" where it.id =?1");
        return (InvsInspectation) em.createQuery(jpql.toString()).setParameter(1, id).getSingleResult();
    }
    
    public List<InvsInspectationHistory> findInspectHistoriesByInspectId(Long inspectId)throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append("select distinct(h) from InvsInspectationHistory h left join fetch h.invsInspectationResultHistories");
        jpql.append(" where h.invsInspectation.id ="+inspectId);
        jpql.append(" order by h.updatedDatetime desc");
        return em.createQuery(jpql.toString()).getResultList();
    }

    public List<InvsAntiAvoidanceHistory> searchAntiAvoidanceHistory(Long id) throws Exception{
        InvsAntiAvoidance aa = findAntiAvoidanceById(id);
        List<InvsAntiAvoidanceHistory> histories = new ArrayList<InvsAntiAvoidanceHistory>();
        if("N".equals(aa.getDefunctInd())){
            InvsAntiAvoidanceHistory history = new InvsAntiAvoidanceHistory();
            history.setId(aa.getId());
            history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4);
            history.setUpdatedDatetime(aa.getUpdatedDatetime());
            history.setUpdatedBy(aa.getUpdatedBy());
            histories.add(history);
        }else if ("Y".equals(aa.getDefunctInd())) {
            InvsAntiAvoidanceHistory history = new InvsAntiAvoidanceHistory();
            history.setId(aa.getId());
            history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_3);
            history.setUpdatedDatetime(aa.getUpdatedDatetime());
            history.setUpdatedBy(aa.getUpdatedBy());
            histories.add(history);
        }
        List<InvsAntiAvoidanceHistory> oldHistories = findAntiHistoriesByAntiId(id);
        if(oldHistories != null){
            histories.addAll(oldHistories);
        }
        return histories;
    }
    
    public List<InvsInspectationHistory> searchInspectHistory(Long id) throws Exception{
        InvsInspectation it = findInspectById(id);
        List<InvsInspectationHistory> histories = new ArrayList<InvsInspectationHistory>();
        if("N".equals(it.getDefunctInd())){
            InvsInspectationHistory history = new InvsInspectationHistory();
            history.setId(it.getId());
            history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4);
            history.setUpdatedDatetime(it.getUpdatedDatetime());
            history.setUpdatedBy(it.getUpdatedBy());
            histories.add(history);
        }else if ("Y".equals(it.getDefunctInd())) {
            InvsInspectationHistory history = new InvsInspectationHistory();
            history.setId(it.getId());
            history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_3);
            history.setUpdatedDatetime(it.getUpdatedDatetime());
            history.setUpdatedBy(it.getUpdatedBy());
            histories.add(history);
        }
        List<InvsInspectationHistory> oldHistories = findInspectHistoriesByInspectId(id);
        if(oldHistories != null){
            histories.addAll(oldHistories);
        }
        return histories;
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
    
    public String getUserName(String str) {
        if (str == null){
            return "";
        }
        UsermstrVo usermstrVo = userservice.getUsermstrVo(str);
        if (usermstrVo == null){
            return str;
        }
        P p = this.userservice.getUsermstrVo(str).getP();
        if (p == null){
            return str;
        }
        return p.getNachn();
    }
    
}
