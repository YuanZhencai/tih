package com.wcs.tih.report.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.service.UserCommonService;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.report.controller.vo.WorkFlowSummaryVo;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class SummaryChartService {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private UserCommonService userservice;
    
    public WorkFlowSummaryVo findWorkFlowSummary(Map<String, Object> query) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String taskType = "";
        String missionStartDatetime = "";
        String missionEndDatetime = "";
        String reportDatetime = "";
        if(query != null){
            if(query.get("taskType")!=null){
                taskType = ((String) query.get("taskType")).trim();
            }
            if(query.get("missionStartDatetime")!=null){
                missionStartDatetime = df.format((Date)query.get("missionStartDatetime"));
            }
            if(query.get("missionEndDatetime")!=null){
                missionEndDatetime = df.format((Date)query.get("missionEndDatetime"));
            }
            if(query.get("reportDatetime")!=null){
                reportDatetime = df.format((Date)query.get("reportDatetime"));
            }
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TEMP.SQ_NO,NAME,TYPE,CHARGED_BY");
        sql.append(",SUM(case when TEMP.STATUS = '"+DictConsts.TIH_TAX_WORKFLOWSTATUS_2+"' then TEMP.NUM else 0 end) AS process");
        sql.append(",SUM(case when TEMP.STATUS = '"+DictConsts.TIH_TAX_WORKFLOWSTATUS_3+"' then TEMP.NUM else 0 end) AS complete");
        sql.append(",SUM(case when TEMP.STATUS = '"+DictConsts.TIH_TAX_WORKFLOWSTATUS_4+"' then TEMP.NUM else 0 end) AS termination");
        sql.append(" FROM (");
        //      --文档上传审核
        StringBuffer uploadDocument = new StringBuffer();
        uploadDocument.append(" SELECT 1 AS SQ_NO,'文档上传审核' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        uploadDocument.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI");
        uploadDocument.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = '上传人主管岗'");
        uploadDocument.append(" AND WI.STATUS NOT IN ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        uploadDocument.append(" AND WI.DEFUNCT_IND <>'Y'");
        uploadDocument.append(" AND WS.CHARGED_BY !=''");
        uploadDocument.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_1+"'");
        uploadDocument.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_8+"'");
        if(!"".equals(missionStartDatetime)){
            uploadDocument.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            uploadDocument.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        uploadDocument.append(" GROUP BY WI.TYPE, WS.CHARGED_BY,WI.STATUS");
        
        //      --检入文档审核
        StringBuffer checkIn = new StringBuffer();
        checkIn.append(" SELECT 2 AS SQ_NO,'检入文档审核' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        checkIn.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI");
        checkIn.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = '检入人主管'");
        checkIn.append(" AND WI.STATUS NOT IN ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        checkIn.append(" AND WI.DEFUNCT_IND <>'Y'");
        checkIn.append(" AND WS.CHARGED_BY !=''");
        checkIn.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_2+"'");
        checkIn.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_8+"'");
        if(!"".equals(missionStartDatetime)){
            checkIn.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            checkIn.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        checkIn.append(" GROUP BY WI.TYPE, WS.CHARGED_BY,WI.STATUS");
        
        //      --提问流程回答
        StringBuffer askQuestion = new StringBuffer();
        askQuestion.append(" SELECT 3 AS SQ_NO,'提问流程回答' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        askQuestion.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI");
        askQuestion.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = 'LaunchStep'");
        askQuestion.append(" AND WI.STATUS NOT IN ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        askQuestion.append(" AND WI.DEFUNCT_IND <>'Y'");
        askQuestion.append(" AND WS.CHARGED_BY !=''");
        askQuestion.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_3+"'");
        askQuestion.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_1+"'");
        if(!"".equals(missionStartDatetime)){
            askQuestion.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            askQuestion.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        askQuestion.append(" GROUP BY WI.TYPE, WS.CHARGED_BY,WI.STATUS");
        
        //      --应交税务综合表审核
        StringBuffer taxReport = new StringBuffer();
        taxReport.append(" SELECT 4 AS SQ_NO,'应交税务综合表审核' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        taxReport.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI,WF_INSTANCEMSTR_PROPERTY WIP");
        taxReport.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = '报表处理岗主管'");
        taxReport.append(" AND WI.ID = WIP.WF_INSTANCEMSTR_ID");
        taxReport.append(" AND WIP.NAME = '"+WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE+"' AND WIP.VALUE = '"+DictConsts.TIH_TAX_REQUESTFORM_4_1+"'");
        taxReport.append(" AND WI.STATUS NOT IN ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        taxReport.append(" AND WI.DEFUNCT_IND <>'Y'");
        taxReport.append(" AND WS.CHARGED_BY !=''");
        taxReport.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_4+"'");
        taxReport.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_8+"'");
        if(!"".equals(missionStartDatetime)){
            taxReport.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            taxReport.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        if(!"".equals(reportDatetime)){
            taxReport.append(" AND WIP.NAME = '"+WorkflowConsts.SENDREPORT_SQLKEY_REPORT_STATISTIC_TIME+"'");
            taxReport.append(" AND WIP.VALUE = '"+reportDatetime+"'");
        }
        taxReport.append(" GROUP BY WI.TYPE, WS.CHARGED_BY,WI.STATUS");
        
        //      --增值税进项税额抵扣情况表审核
        StringBuffer vatReport = new StringBuffer();
        vatReport.append(" SELECT 5 AS SQ_NO,'增值税进项税额抵扣情况表审核' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        vatReport.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI,WF_INSTANCEMSTR_PROPERTY WIP");
        vatReport.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = '报表处理岗主管'");
        vatReport.append(" AND WI.ID = WIP.WF_INSTANCEMSTR_ID");
        vatReport.append(" AND WIP.NAME = '"+WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE+"' AND WIP.VALUE = '"+DictConsts.TIH_TAX_REQUESTFORM_4_2+"'");
        vatReport.append(" AND WI.STATUS NOT IN ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        vatReport.append(" AND WI.DEFUNCT_IND <>'Y'");
        vatReport.append(" AND WS.CHARGED_BY !=''");
        vatReport.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_4+"'");
        vatReport.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_8+"'");
        if(!"".equals(missionStartDatetime)){
            vatReport.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            vatReport.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        if(!"".equals(reportDatetime)){
            vatReport.append(" AND WIP.NAME = '"+WorkflowConsts.SENDREPORT_SQLKEY_REPORT_STATISTIC_TIME+"'");
            vatReport.append(" AND WIP.VALUE = '"+reportDatetime+"'");
        }
        vatReport.append(" GROUP BY WI.TYPE, WS.CHARGED_BY,WI.STATUS");
       
        //      --集团发起的情况反馈流程处理
        StringBuffer companyFeedback = new StringBuffer();
        companyFeedback.append(" SELECT 6 AS SQ_NO,'集团发起的情况反馈流程处理' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        companyFeedback.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI,WF_INSTANCEMSTR_PROPERTY WIP");
        companyFeedback.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = '公司处理岗主管'");
        companyFeedback.append(" AND WI.ID = WIP.WF_INSTANCEMSTR_ID");
        companyFeedback.append(" AND WIP.NAME = '"+DictConsts.TIH_TAX_REQUESTBY+"' AND WIP.VALUE = '"+DictConsts.TIH_TAX_REQUESTBY_1+"'");
        companyFeedback.append(" AND WI.STATUS NOT IN ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        companyFeedback.append(" AND WI.DEFUNCT_IND <>'Y'");
        companyFeedback.append(" AND WS.CHARGED_BY !=''");
        companyFeedback.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_5+"'");
        companyFeedback.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_8+"'");
        if(!"".equals(missionStartDatetime)){
            companyFeedback.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            companyFeedback.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        companyFeedback.append(" GROUP BY WI.TYPE, WS.CHARGED_BY,WI.STATUS");
        
        //      --工厂发起的情况反馈流程处理
        StringBuffer factoryFeedback = new StringBuffer();
        factoryFeedback.append(" SELECT 7 AS SQ_NO,'工厂发起的情况反馈流程处理' AS NAME,WI.TYPE,WS.CHARGED_BY,WI.STATUS,COUNT(DISTINCT(WI.ID)) AS NUM");
        factoryFeedback.append(" FROM WF_STEPMSTR WS,WF_INSTANCEMSTR WI,WF_INSTANCEMSTR_PROPERTY WIP");
        factoryFeedback.append(" WHERE WS.WF_INSTANCEMSTR_ID = WI.ID AND WS.NAME = '发起人主管'");
        factoryFeedback.append(" AND WI.ID = WIP.WF_INSTANCEMSTR_ID");
        factoryFeedback.append(" AND WIP.NAME = '"+DictConsts.TIH_TAX_REQUESTBY+"' AND WIP.VALUE = '"+DictConsts.TIH_TAX_REQUESTBY_2+"'");
        factoryFeedback.append(" AND WI.STATUS NOT IN ('TIH.TAX.WORKFLOWSTATUS.1')");
        factoryFeedback.append(" AND WI.DEFUNCT_IND <>'Y'");
        factoryFeedback.append(" AND WS.CHARGED_BY !=''");
        factoryFeedback.append(" AND WI.TYPE = '"+DictConsts.TIH_TAX_REQUESTFORM_5+"'");
        factoryFeedback.append(" AND WS.DEAL_METHOD = '"+DictConsts.TIH_TAX_APPROACH_8+"'");
        if(!"".equals(missionStartDatetime)){
            factoryFeedback.append(" AND WI.SUBMIT_DATETIME >= '"+missionStartDatetime+"'");
        }
        if(!"".equals(missionEndDatetime)){
            factoryFeedback.append(" AND WI.SUBMIT_DATETIME <= '"+missionEndDatetime+"'");
        }
        factoryFeedback.append(" GROUP BY WI.TYPE,WS.CHARGED_BY,WI.STATUS");
        
        if(!"".equals(taskType)){
            taskType = (String) query.get("taskType");
            if(DictConsts.TIH_TAX_REQUESTFORM_1.equals(taskType)){
                sql.append(uploadDocument);
            }
            if(DictConsts.TIH_TAX_REQUESTFORM_2.equals(taskType)){
                sql.append(checkIn);
            }
            if(DictConsts.TIH_TAX_REQUESTFORM_3.equals(taskType)){
                sql.append(askQuestion);
            }
            if(DictConsts.TIH_TAX_REQUESTFORM_4_1.equals(taskType)){
                sql.append(taxReport);
            }
            if(DictConsts.TIH_TAX_REQUESTFORM_4_2.equals(taskType)){
                sql.append(vatReport);
            }
            if(DictConsts.TIH_TAX_REQUESTBY_1.equals(taskType)){
                sql.append(companyFeedback);
            }
            if(DictConsts.TIH_TAX_REQUESTBY_2.equals(taskType)){
                sql.append(factoryFeedback);
            }
        }else{
            sql.append(uploadDocument);
            sql.append(" UNION");
            sql.append(checkIn);
            sql.append(" UNION");
            sql.append(askQuestion);
            sql.append(" UNION");
            sql.append(taxReport);
            sql.append(" UNION");
            sql.append(vatReport);
            sql.append(" UNION");
            sql.append(companyFeedback);
            sql.append(" UNION");
            sql.append(factoryFeedback);
        }
        sql.append(" ) AS TEMP");
        sql.append(" GROUP BY SQ_NO,NAME,TYPE,CHARGED_BY");
        sql.append(" ORDER BY SQ_NO,CHARGED_BY");
        List resultList = em.createNativeQuery(sql.toString()).getResultList();
        return convertWorkFlowSummaryVo(resultList);
    }
    
    public WorkFlowSummaryVo convertWorkFlowSummaryVo(List resultList){
        List<WorkFlowSummaryVo> summaryVos = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> updateDocument = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> checkinDocument = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> askQuestion = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> taxPayable = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> addTax = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> groupFeed = new ArrayList<WorkFlowSummaryVo>();
        ArrayList<WorkFlowSummaryVo> factoryDeed = new ArrayList<WorkFlowSummaryVo>();
        
        WorkFlowSummaryVo summaryVo = null;
        List<String> distinctNames = new ArrayList<String>();
        if(resultList!=null && resultList.size()>0){
            for (int i = 0; i < resultList.size(); i++) {
                Object[] rs = (Object[]) resultList.get(i);
                summaryVo = new WorkFlowSummaryVo();
                summaryVo.setId(rs[0]== null ? null : Long.valueOf(rs[0].toString()));
                if(summaryVo.getId()!= null){
                    Integer summaryId = Integer.valueOf(summaryVo.getId().toString());
                    switch (summaryId) {
                        case 1:
                            updateDocument.add(summaryVo);
                            break;
                        case 2:
                            checkinDocument.add(summaryVo);
                            break;
                        case 3:
                            askQuestion.add(summaryVo);
                            break;
                        case 4:
                            taxPayable.add(summaryVo);
                            break;
                        case 5:
                            addTax.add(summaryVo);
                            break;
                        case 6:
                            groupFeed.add(summaryVo);
                            break;
                        case 7:
                            factoryDeed.add(summaryVo);
                            break;

                        default:
                            break;
                    } 
                }
                String name = rs[1]== null ? "" : (String) rs[1];
                if(name!=null){
                    if(!distinctNames.contains(name)){
                        summaryVo.setName(name);
                        distinctNames.add(name);
                    }
                }
                summaryVo.setWorkFlowType(rs[2]== null ? "" : (String) rs[2]);
                summaryVo.setAuditor(rs[3]== null ? "" : (String) rs[3]);
                summaryVo.setAuditorName(getUserName(summaryVo.getAuditor()));
                summaryVo.setProcess(rs[4]== null ? null : Long.valueOf(rs[4].toString()));
                summaryVo.setComplete(rs[5]== null ? null : Long.valueOf(rs[5].toString()));
                summaryVo.setTermination(rs[6]== null ? null : Long.valueOf(rs[6].toString()));
                summaryVos.add(summaryVo);
            }
        }
        WorkFlowSummaryVo tmpVo = new WorkFlowSummaryVo();
        tmpVo.setSummaryVos(summaryVos);
        tmpVo.setUpdateDocument(updateDocument);
        tmpVo.setCheckinDocument(checkinDocument);
        tmpVo.setAskQuestion(askQuestion);
        tmpVo.setTaxPayable(taxPayable);
        tmpVo.setAddTax(addTax);
        tmpVo.setGroupFeed(groupFeed);
        tmpVo.setFactoryDeed(factoryDeed);
        return tmpVo;
    }

    
    public List<WfInstancemstr> findUploadDocumentBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws");
        jpql.append(" where wi.id = ws.wfInstancemstr.id");
        jpql.append(" and ws.name = '上传人主管岗'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_1+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_8 +"'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findCheckInBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws");
        jpql.append(" where wi.id = ws.wfInstancemstr.id");
        jpql.append(" and ws.name = '检入人主管'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_2+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_8 +"'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findAskQuestionBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws");
        jpql.append(" where wi.id = ws.wfInstancemstr.id");
        jpql.append(" and ws.name = 'LaunchStep'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_3+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_1 +"'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findTaxReportBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws,WfInstancemstrProperty wip");
        jpql.append(" where wi.id = ws.wfInstancemstr.id and wi.id = wip.wfInstancemstr.id");
        jpql.append(" and ws.name = '报表处理岗主管'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_4+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_8 +"'");
        jpql.append(" and wip.name = '" + WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE + "'");
        jpql.append(" and wip.value = '" + DictConsts.TIH_TAX_REQUESTFORM_4_1 + "'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findVatReportBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws,WfInstancemstrProperty wip");
        jpql.append(" where wi.id = ws.wfInstancemstr.id and wi.id = wip.wfInstancemstr.id");
        jpql.append(" and ws.name = '报表处理岗主管'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_4+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_8 +"'");
        jpql.append(" and wip.name = '" + WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE + "'");
        jpql.append(" and wip.value = '" + DictConsts.TIH_TAX_REQUESTFORM_4_2 + "'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findCompanyFeedbackBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws,WfInstancemstrProperty wip");
        jpql.append(" where wi.id = ws.wfInstancemstr.id and wi.id = wip.wfInstancemstr.id");
        jpql.append(" and ws.name = '公司处理岗主管'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_5+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_8 +"'");
        jpql.append(" and wip.name = '" + DictConsts.TIH_TAX_REQUESTBY + "'");
        jpql.append(" and wip.value = '" + DictConsts.TIH_TAX_REQUESTBY_1 + "'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findFactoryFeedbackBy(String userName) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select distinct(wi) from WfInstancemstr wi,WfStepmstr ws,WfInstancemstrProperty wip");
        jpql.append(" where wi.id = ws.wfInstancemstr.id and wi.id = wip.wfInstancemstr.id");
        jpql.append(" and ws.name = '发起人主管'");
        jpql.append(" and wi.status not in ('"+DictConsts.TIH_TAX_WORKFLOWSTATUS_1+"')");
        jpql.append(" and wi.type = '"+DictConsts.TIH_TAX_REQUESTFORM_5+"'");
        jpql.append(" and wi.defunctInd <> 'Y'");
        jpql.append(" and ws.dealMethod = '"+ DictConsts.TIH_TAX_APPROACH_8 +"'");
        jpql.append(" and wip.name = '" + DictConsts.TIH_TAX_REQUESTBY + "'");
        jpql.append(" and wip.value = '" + DictConsts.TIH_TAX_REQUESTBY_2 + "'");
        jpql.append(" and ws.chargedBy = '"+ userName +"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public List<WfInstancemstr> findWorkFlowSummaryDetailsBy(List<Integer> types,String userName) throws Exception{
        List<WfInstancemstr> wis = new ArrayList<WfInstancemstr>();
        List<WfInstancemstr> workFlows = null;
        for (int i = 0; i < types.size(); i++) {
            workFlows = new ArrayList<WfInstancemstr>();
            switch (types.get(i)) {
                case 1:
                    workFlows = findUploadDocumentBy(userName);
                    break;
                case 2:
                    workFlows = findCheckInBy(userName);
                    break;
                case 3:
                    workFlows = findAskQuestionBy(userName);
                    break;
                case 4:
                    workFlows = findTaxReportBy(userName);
                    break;
                case 5:
                    workFlows = findVatReportBy(userName);
                    break;
                case 6:
                    workFlows = findCompanyFeedbackBy(userName);
                    break;
                case 7:
                    workFlows = findFactoryFeedbackBy(userName);
                    break;

                default:
                    break;
            }
            wis.addAll(workFlows);
        }
        return wis;
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
