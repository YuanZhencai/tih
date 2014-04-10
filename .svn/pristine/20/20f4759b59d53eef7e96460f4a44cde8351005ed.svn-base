package com.wcs.tih.report.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.StreamedContent;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.model.ReportVatIptDeduction;
import com.wcs.tih.model.ReportVatIptDeductionDetail;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.report.controller.helper.VATSummaryExpExcel;
import com.wcs.tih.report.controller.vo.VATSummaryVO;


@Stateless
public class VATSummaryService {
	
	private static final String MONTH = "month";
    @PersistenceContext 
	private EntityManager em;
	@EJB 
	private VATSummaryExpExcel vATSummaryExpExcel;
	@EJB 
	private FileNetUploadDownload fileUpService;
	@EJB 
	private LoginService loginService;
	private static ResourceBundle filenetProperties = ResourceBundle.getBundle("filenet");
	
	/**
	 * 查询出VAT主表的所有的公司名,公司ID,去掉重复的.
	 * @param statisticalTime
	 * @param statisticalWay
	 * @param companyName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VATSummaryVO> queryData(Date statisticalTime,String statisticalWay,List<Long> queryCompanys){
		//查询出VAT主表里所有不同的公司,按时间,按
		StringBuilder jpql=new StringBuilder();
		jpql.append("SELECT vat,c,lower(c.code) as lowerCode FROM ReportVatIptDeduction vat,Companymstr c WHERE vat.defunctInd<>'Y' AND vat.status='"+DictConsts.TIH_TAX_WORKFLOWSTATUS_3+"'  ");
		jpql.append(" and vat.companymstrId = c.id");
		if(statisticalTime==null){
			statisticalTime=new Date();
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(statisticalTime);
		int yearTime=cal.get(Calendar.YEAR);
		if(statisticalWay.equals(MONTH)){//以当前月计算
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		    String monthDate = df.format(statisticalTime);
			jpql.append(" AND vat.statisticDatetime = '"+monthDate+"'");
		}else{//以一年计算
			jpql.append(" AND vat.statisticDatetime BETWEEN '"+yearTime+"-01-01 00:00:00' AND '"+(yearTime+1)+"-01-01 00:00:00' ");
		}
		if(null!=queryCompanys && queryCompanys.size()>0){
		    String companyIds = "";
            for (int i = 0; i < queryCompanys.size(); i++) {
                companyIds = companyIds + queryCompanys.get(i)+(i==queryCompanys.size() -1?"":",");
            }
            jpql.append(" AND vat.companymstrId in ("+companyIds+")");
        }
		jpql.append(" order by lowerCode");
		List resultList = em.createQuery(jpql.toString()).getResultList();
		List<VATSummaryVO> vatVos = new ArrayList<VATSummaryVO>();
		if(resultList == null || resultList.size() == 0){
			return vatVos;
		}
		VATSummaryVO vatVo = null;
		for (int i = 0; i < resultList.size(); i++) {
		    vatVo = new VATSummaryVO();
		    Object[] result = (Object[]) resultList.get(i);
		    vatVo.setReportVat(result[0] == null ? new ReportVatIptDeduction():(ReportVatIptDeduction)result[0]);
		    vatVo.setCompany(result[1] == null ? new Companymstr():(Companymstr)result[1]);
		    vatVo.setLowerCode(result[2] == null ? "":(String)result[2]);
		    vatVos.add(vatVo);
        }
		return vatVos;
		
	}
	
	/**
	 * 准备生成excel所需要的数据
	 * @return 
	 */
	public Map<String,List<ReportVatIptDeductionDetail>> prepareExcelData(VATSummaryVO[] vatSelectData,String statisticalWay,Date statisticalTime){
		Map<String,List<ReportVatIptDeductionDetail>> resultMap=new HashMap<String, List<ReportVatIptDeductionDetail>>();
		//处理统计时间,在此基础上月的处理是月份加1,年的处理是年加1,并且年小于减1
			//当月
		for(VATSummaryVO vo:vatSelectData){
			String companyName=vo.getCompanyName();
			String sql="";
			Calendar cal=Calendar.getInstance();
			cal.setTime(statisticalTime);
			int yearTime=cal.get(Calendar.YEAR);
			int monthTime=cal.get(Calendar.MONTH)+1;
			if(statisticalWay.equals(MONTH)){
				sql="SELECT INVOICE_TYPE_NAME,VARIETY_NAME,AVG(TAX_RATE),SUM(AMMOUNT),SUM(MONEY_SUM),SUM(TAX_AMMOUNT) " +
						"FROM REPORT_VAT_IPT_DEDUCTION_DETAIL  " +
						"WHERE REPORT_VAT_IPT_DEDUCTION_ID in (SELECT id FROM REPORT_VAT_IPT_DEDUCTION WHERE COMPANYMSTR_ID="+vo.getId()+" AND STATISTIC_DATETIME ='"+yearTime+"-"+(monthTime)+"-01 00:00:00'  AND DEFUNCT_IND <> 'Y' )" +
						"GROUP BY INVOICE_TYPE_NAME,VARIETY_NAME " +
						"ORDER BY INVOICE_TYPE_NAME,VARIETY_NAME ";
			}else{
				sql="SELECT INVOICE_TYPE_NAME,VARIETY_NAME,AVG(TAX_RATE),SUM(AMMOUNT),SUM(MONEY_SUM),SUM(TAX_AMMOUNT) " +
						"FROM REPORT_VAT_IPT_DEDUCTION_DETAIL  " +
						"WHERE REPORT_VAT_IPT_DEDUCTION_ID in (SELECT id FROM REPORT_VAT_IPT_DEDUCTION WHERE COMPANYMSTR_ID="+vo.getId()+" AND STATISTIC_DATETIME BETWEEN '"+yearTime+"-01-01 00:00:00' AND '"+yearTime+"-"+(monthTime)+"-02 00:00:00'  AND DEFUNCT_IND <> 'Y' )" +
						"GROUP BY INVOICE_TYPE_NAME,VARIETY_NAME " +
						"ORDER BY INVOICE_TYPE_NAME,VARIETY_NAME ";
			}
			List<Object[]> list=this.em.createNativeQuery(sql).getResultList();
			List<ReportVatIptDeductionDetail> resultList=new ArrayList<ReportVatIptDeductionDetail>();
			for(int i=0;i<list.size();i++){
				ReportVatIptDeductionDetail r=new ReportVatIptDeductionDetail();
				Object[] objArr=list.get(i);
				r.setInvoiceTypeName(objArr[0].toString());
				r.setVarietyName(objArr[1].toString());
				r.setTaxRate((Double)objArr[2]);
				r.setAmmount((Double)objArr[3]);
				r.setMoneySum((BigDecimal)objArr[4]);
				r.setTaxAmmount((BigDecimal)objArr[5]);
				resultList.add(r);
			}
			resultMap.put(companyName, resultList);
		}
		
		return resultMap;
	}
	
	
	//先存到fileNet CE上,然后再保存数据到历史记录里面.之后查询出这个人所有的VAT汇总历史记录
	@SuppressWarnings("deprecation")
	public void downVATExcel(Map<String,List<ReportVatIptDeductionDetail>> mapWithExcelSummary,Date statisticalTime,String statisticalWay) throws Exception{
		//"--先存到fileNet CE上,然后再保存数据到历史记录里面.之后查询出这个人所有的VAT汇总历史记录");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fileName ="增值税进项税抵扣汇总-"+ (statisticalWay.equals(MONTH) ? "当月" : "累计") + "-" + sdf.format(statisticalTime==null?new Date():statisticalTime)+  "-" + (int) ((Math.random() * 900) + 100) +".xls";
		InputStream inputStream = this.vATSummaryExpExcel.createVATSummaryExcel(mapWithExcelSummary);
		
		com.filenet.api.core.Document document = fileUpService.upLoadDocumentCheckIn(inputStream, new HashMap<String, Object>(), fileName + ".xls", filenetProperties.getString("ce.document.classid"), filenetProperties.getString("ce.folder.reportSummary.vatInputTax"));
        inputStream.close();
        ReportSummaryHistory rsh = new ReportSummaryHistory();
        rsh.setReportType(DictConsts.TIH_TAX_REPORT_2);
        rsh.setSummaryDatetime(new Date());
        rsh.setName(fileName);
        rsh.setFileId(document.get_Id().toString());
        rsh.setDefunctInd("N");
        rsh.setCreatedBy(this.loginService.getCurrentUserName());
        rsh.setCreatedDatetime(new Date());
        rsh.setUpdatedBy(this.loginService.getCurrentUserName());
        rsh.setUpdatedDatetime(new Date());
        this.em.persist(rsh);
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportSummaryHistory> queryVATStatisticalInfo(){
		String jpql="SELECT rsh FROM ReportSummaryHistory rsh where rsh.createdBy='"+loginService.getCurrentUserName()+"' " +
				" AND rsh.reportType='"+DictConsts.TIH_TAX_REPORT_2+"' " +
				" AND rsh.defunctInd='N' order by rsh.id desc";
		return this.em.createQuery(jpql).getResultList();
	}
	
	//下载附件
    public StreamedContent downloadFile(String fileId) throws MimeException,Exception {
        return fileUpService.downloadDocumentEncoding(fileId,"utf-8","iso8859-1");
    }
    
    //根据vatId查找出fileId
    @SuppressWarnings("unchecked")
	public String queryFileIdByVatId(long vatId) throws Exception{
    	String sql = "SELECT p FROM WfInstancemstrProperty p WHERE p.name=:name AND p.value=:value AND p.wfInstancemstr.defunctInd='N' AND p.wfInstancemstr.status=:status";
        List<WfInstancemstrProperty> rs = em.createQuery(sql).setParameter("name", WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID)
                .setParameter("value", String.valueOf(vatId))
                .setParameter("status", DictConsts.TIH_TAX_WORKFLOWSTATUS_3).getResultList();
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
    
  //根据vatId查找出fileId
    @SuppressWarnings("unchecked")
	public String queryFileNameByVatId(long vatId) throws Exception{
    	String sql = "SELECT p FROM WfInstancemstrProperty p WHERE p.name=:name AND p.value=:value AND p.wfInstancemstr.defunctInd='N' AND p.wfInstancemstr.status=:status";
        List<WfInstancemstrProperty> rs = em.createQuery(sql).setParameter("name", WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID)
                .setParameter("value", String.valueOf(vatId))
                .setParameter("status", DictConsts.TIH_TAX_WORKFLOWSTATUS_3).getResultList();
        if(rs.isEmpty()) {
            throw new Exception("没有找到报表所对应的流程");
        }
        sql = "SELECT p FROM WfInstancemstrProperty p WHERE p.name=:name AND p.wfInstancemstr.id=:id";
        List<WfInstancemstrProperty> ss = em.createQuery(sql)
                .setParameter("name", WorkflowConsts.SENDREPORT_SQLKEY_REPORT_ATTNAME)
                .setParameter("id", rs.get(0).getWfInstancemstr().getId()).getResultList();
        if(ss.isEmpty()){
        	throw new Exception("没有在流程中找到原始上传附件");
        }
        return ss.get(0).getValue();
    }
    
    //通过datatable给的公司ID查出所有的vatId
    public Long[] queryVatIdByCompanyId(long companyId,String statisticalWay,Date statisticalTime){
    	Calendar cal=Calendar.getInstance();
		cal.setTime(statisticalTime);
		int yearTime=cal.get(Calendar.YEAR);
		int monthTime=cal.get(Calendar.MONTH)+1;
		
    	StringBuilder sb=new StringBuilder();
    	sb.append("SELECT vat.id FROM ReportVatIptDeduction vat WHERE vat.companymstrId="+companyId+" AND vat.defunctInd<>'Y' AND vat.status='"+DictConsts.TIH_TAX_WORKFLOWSTATUS_3+"' ");
    	if(statisticalWay.equals(MONTH)){
    		sb.append(" AND vat.statisticDatetime='"+yearTime+"-"+(monthTime)+"-01 00:00:00' ");
    	}else{
    		sb.append(" AND vat.statisticDatetime BETWEEN '"+yearTime+"-01-01 00:00:00' AND '"+yearTime+"-"+(monthTime)+"-02 00:00:00' ");
    	}
    	
    	List<Long> list=this.em.createQuery(sb.toString()).getResultList();
    	Long[] vatIdArray=new Long[list.size()];
    	for(int i=0;i<list.size();i++){
    		vatIdArray[i]=list.get(i);
    	}
    	
    	return vatIdArray;
    }
    
}
