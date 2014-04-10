package com.wcs.tih.report.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.ReportPayableTax;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.report.controller.vo.ReportPayableTaxVO;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class ReportPayableTaxService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext 
    private EntityManager em;
    
    @EJB 
    private FileNetUploadDownload fud;
    @EJB 
    private LoginService loginService;
    
    private ResourceBundle rb;
    private String classId;
    private String folder;
    
    @PostConstruct 
    public void init() {
        rb = ResourceBundle.getBundle("filenet");
        classId = rb.getString("ce.document.classid");
        folder = rb.getString("ce.folder.reportSummary.payTax");
    }
    
    /**
     * <p>Description: 取得所有符合条件的公司</p>
     * @param query
     * @return
     */
    public List<ReportPayableTaxVO> getCompanys(Map<String, Object> query,List<Long> companys) {
        String sql = "SELECT r,o,c,lower(c.code) as lowerCode " +
                "FROM Companymstr c, O o, ReportPayableTax r " +
                "WHERE c.oid=o.id AND c.defunctInd='N' " +
                "AND r.companymstrId = c.id AND r.defunctInd='N' AND r.statisticDatetime = :statisticDatetime " +
                "AND r.status = '"+DictConsts.TIH_TAX_WORKFLOWSTATUS_3+"'";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date statisticDate;
        try {
            statisticDate = sdf.parse(sdf.format(query.get("statisticDate")));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        if (companys != null && companys.size() > 0) {
            String companyIds = "";
            for (int i = 0; i < companys.size(); i++) {
                companyIds = companyIds + companys.get(i)+(i==companys.size() -1?"":",");
            }
            sql = sql + " AND r.companymstrId in ("+companyIds+")";
        }
        sql = sql + " ORDER BY lowerCode";
        List resultList = em.createQuery(sql).setParameter("statisticDatetime", statisticDate).getResultList();
        List<ReportPayableTaxVO> payTaxVos = new ArrayList<ReportPayableTaxVO>();
        if(resultList == null || resultList.size() == 0 ){
        	return payTaxVos;
        }
        ReportPayableTaxVO payTaxVo = null;
        for (int i = 0; i < resultList.size(); i++) {
            payTaxVo = new ReportPayableTaxVO();
            Object[] result = (Object[]) resultList.get(i);
            payTaxVo.setPayableTax(result[0] == null ? new ReportPayableTax():(ReportPayableTax)result[0]);
            payTaxVo.setO(result[1] == null ? new O():(O)result[1]);
            payTaxVo.setC(result[2] == null ? new Companymstr():(Companymstr)result[2]);
            payTaxVo.setLowerCode(result[3] == null ? "":(String)result[3]);
            payTaxVos.add(payTaxVo);
        }
        em.clear();
        return payTaxVos;
    }
    /**
     * <p>Description: 生成报表</p>
     * @param selections
     * @param date
     * @return Filename, Fileid
     * @throws Exception
     */
    public void generateExcelReport(ReportPayableTaxVO[] selections, Date date) throws Exception {
        Workbook wb = new ExportReportPayableTax().generateExcelReport(selections, date);
        String[] rs = uploadReport(wb);
        // 记录历史表
        ReportSummaryHistory summary = new ReportSummaryHistory();
        String member = loginService.getCurrentUsermstr().getAdAccount();
        Date d = new Date();
        summary.setCreatedBy(member);
        summary.setCreatedDatetime(d);
        summary.setDefunctInd("N");
        summary.setFileId(rs[1]);
        summary.setName(rs[0]);
        summary.setReportType(DictConsts.TIH_TAX_REPORT_3);
        summary.setSummaryDatetime(d);
        summary.setUpdatedBy(member);
        summary.setUpdatedDatetime(d);
        em.persist(summary);
    }
    /**
     * <p>Description: </p>
     * @param wb
     * @return
     * @throws Exception
     */
    private String[] uploadReport(Workbook wb) throws Exception {
        String fileName = "应交税费汇总报表-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int randomNum = new Random().nextInt(1000);
        for(; randomNum < 100; ) {
            randomNum = new Random().nextInt(1000);
        }
        fileName += "-" + randomNum + ".xls";
        FileOutputStream fos = new FileOutputStream("payable.xls");
        wb.write(fos);
        fos.close();
        
        FileInputStream is = new FileInputStream("payable.xls");
        Document doc = fud.upLoadDocumentCheckIn(is, new HashMap(), fileName, classId, folder);
        is.close();
        File file = new File(fileName);
        file.delete();
        return new String[]{fileName, doc.get_Id().toString()};
    }
    /**
     * <p>Description: 下载</p>
     * @param id
     * @return
     * @throws Exception
     */
    public StreamedContent downloadReport(String id) throws Exception {
        return fud.downloadDocumentEncoding(id, "utf-8", "iso8859-1");
    }
    
    public String getFileIdByTaxId(Long taxId) throws Exception {
        String sql = "SELECT p FROM WfInstancemstrProperty p WHERE p.name=:name AND p.value=:value " 
                + "AND p.wfInstancemstr.status=:status";
        List<WfInstancemstrProperty> rs = em.createQuery(sql).setParameter("name", WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID)
                .setParameter("value", String.valueOf(taxId))
                .setParameter("status", DictConsts.TIH_TAX_WORKFLOWSTATUS_3)
                .getResultList();
        logger.info("" + rs.size());
        if(rs.isEmpty()) {
            throw new Exception("没有找到报表所对应的流程");
        }
        sql = "SELECT p FROM WfInstancemstrProperty p WHERE p.name=:name AND p.wfInstancemstr.id=:id";
        List<WfInstancemstrProperty> ss = em.createQuery(sql)
                .setParameter("name", WorkflowConsts.SENDREPORT_SQLKEY_REPORT_ATTID)
                .setParameter("id", rs.get(0).getWfInstancemstr().getId()).getResultList();
        if(ss.isEmpty()){
        	throw new Exception("没有在流程中找到原始上传附件");
        }
        return ss.get(0).getValue();
    }
    
    public List<ReportSummaryHistory> getHistorys() {
        String sql = "SELECT s FROM ReportSummaryHistory s WHERE s.createdBy=:createBy AND s.defunctInd='N' AND s.reportType=:reportType ORDER BY s.id DESC";
        return em.createQuery(sql).setParameter("createBy", loginService.getCurrentUsermstr().getAdAccount())
        .setParameter("reportType", DictConsts.TIH_TAX_REPORT_3).getResultList();
    }
    
    // 
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
