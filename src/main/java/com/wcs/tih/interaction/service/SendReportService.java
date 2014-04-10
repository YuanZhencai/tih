package com.wcs.tih.interaction.service;

import java.io.InputStream;
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
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.O;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.filenet.pe.service.DefaultWorkflowImpl;
import com.wcs.tih.model.ReportPayableTax;
import com.wcs.tih.model.ReportPayableTaxAddedMaterial;
import com.wcs.tih.model.ReportPayableTaxEstate;
import com.wcs.tih.model.ReportPayableTaxIncome;
import com.wcs.tih.model.ReportPayableTaxLand;
import com.wcs.tih.model.ReportPayableTaxOther;
import com.wcs.tih.model.ReportPayableTaxStamp;
import com.wcs.tih.model.ReportPayableTaxStayed;
import com.wcs.tih.model.ReportPayableTaxVat;
import com.wcs.tih.model.ReportVatIptDeduction;
import com.wcs.tih.model.ReportVatIptDeductionDetail;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.report.service.ImportReportPayableTaxService;
import com.wcs.tih.report.service.VATReportEntryService;
import com.wcs.tih.system.service.OrganizationLevelInterface;

import filenet.vw.api.VWException;

@Stateless
public class SendReportService {
    
    private static final String ERROR = "error";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager em;
    
    @EJB 
    private LoginService loginService;
    @EJB 
    private DefaultWorkflowImpl defaultWorkflowImpl;
    @EJB 
    private FileNetUploadDownload fileUpService;
    @EJB 
    private ImportReportPayableTaxService importReportPayableTaxService;
    @EJB 
    private VATReportEntryService vATReportEntryService;
    @EJB 
    private UserCommonService userCommonService;
    @EJB 
    private OrganizationLevelInterface organizationLevelInterface;
    
    private ResourceBundle positons = ResourceBundle.getBundle("positons");
    
    /**
     * <p>Description: 创建一个工作流程</p>
     * @param wfInsProMap 实例属性表的数据,都装在这个MAP里.
     * @param stepProMap 步骤属性表的数据,也都装在MAP里
     * @param wfRemindVo 
     * @throws VWException 连接FileNet出现异常或错误!
     */
    public WfInstancemstr createDialogSumbitAndSaveData(Map <String,String> wfInsProMap,Map <String,String> stepProMap,String userName,String remarks,String importance,String urgency, WfRemindVo wfRemindVo) throws VWException{
        String requestForm=DictConsts.TIH_TAX_REQUESTFORM_4;  //报送报表,这个是要存数据库的
        String workflowName=WorkflowConsts.SENDREPORT_WORKFLOW_NAME; //流程名称,启动fileNet使用.
        return this.defaultWorkflowImpl.createworkflow(wfInsProMap, requestForm, workflowName, stepProMap, WorkflowConsts.SENDREPORT_PARAM_PROCESSOR, userName,remarks,importance,urgency,wfRemindVo);
    }
    
    public String findCompanyName(String oid){
    	O o = em.find(O.class, oid);
    	return o.getStext();
    }
    
    public List<String> validateProcessor(List<String> companyList){
    	List<String> errorInfoList=new ArrayList<String>();
    	for (String oid : companyList) {
    		String userNameArr[]=this.queryProcessor(oid);
    		if(userNameArr[0].equals(ERROR)){
    			String companyName=this.queryCompanyNameById(oid);
    			errorInfoList.add(companyName+":"+userNameArr[1]);
    		}
    	}
    	return errorInfoList;
    }
    
	public String[] queryProcessor(String oid) {
		String code = positons.getString("RPDEAL");// 公司报表处理岗
		logger.info("code:" + code);
		List<String> list = this.userCommonService.getAdAccountByOidCode(oid, code);
		if (null==list || list.size() == 0) {
			String[] arr = { ERROR, "公司报表处理岗人员空缺，请联系管理员！" };
			return arr;
		} else if (list.size() > 1) {
			String repeatUser = "";
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					repeatUser = list.get(i);
				} else {
					repeatUser = repeatUser + "," + list.get(i);
				}
			}
			String[] arr = { ERROR, "公司报表处理岗人员重复，请联系管理员！重复帐号为:" + repeatUser + "." };
			return arr;
		}
		logger.info("公司报表处理岗:" + list.get(0));
		String[] arr = { "true", list.get(0) };
		return arr;
	}
    
	/**
     * <p>Description: 取得上级主管</p>
     * @return
     */
    public String getSuperadvisor(Long companyId) {
        UsermstrVo uv = organizationLevelInterface.getUsermstrVo(loginService.getCurrentUsermstr().getAdAccount(), DictConsts.TIH_TAX_REQUESTFORM_4, companyId);
        if(uv == null){
        	return null;
        }
        if(uv.getUsermstr() == null){
        	return null;
        }
        return uv.getUsermstr().getAdAccount();
    }
    
    public Long getCompanyId(String oid){
        String sql = "select c.id from Companymstr c where c.oid = '"+oid+"' and c.defunctInd = 'N' ";
        List list = this.em.createQuery(sql).getResultList();
        if(list.size() > 0){
            return (Long)list.get(0);
        }
        return null;
    }
	
    //查找集团报表审核岗
    public String[] queryAuditor(String excelUsedOid){
    	String code=positons.getString("RPAUDIT");//集团审核岗
    	List<String> list=this.userCommonService.getAdAccountByOidCode(excelUsedOid,code);
    	if(null==list ||  list.size()!=1){
    		if(null==list||list.size()==0){
    			String[] arr={ERROR,"集团报表审核岗人员空缺，请联系管理员！"};
    			return arr;
    		}else if(!list.isEmpty()&&list.size()>1){
    			String repeatUser = "";
    			for(int i = 0; i < list.size(); i++){
    				 if (i == 0) {
    	                    repeatUser = list.get(i);
    	                } else {
    	                    repeatUser = repeatUser + "," + list.get(i);
    	                }  
    			}
    			String[] arr={ERROR,"集团报表审核岗人员重复，请联系管理员！重复帐号为:" + repeatUser + "."};
    			return arr;
    		}
    	}
    	logger.info("集团报表审核岗人员:"+list.get(0));
    	String[] arr={"true",list.get(0)};
    	return arr;
    }
    
    
    /**
     * <p>Description:查找最后一个步骤表的步骤信息 </p>
     * @param w WfInstancemstr:实例表对象
     * @return 流程走到的最后一个步骤表对象.
     */
    public WfStepmstr queryLastStepInfo(WfInstancemstr w){
        return this.defaultWorkflowImpl.getLastStep(w);
    }
    
    public List<WfInstancemstrProperty> getwfips(Long id){
    	String sql = "select wfip from WfInstancemstrProperty wfip where wfip.wfInstancemstr.id = "+id+"";
    	return em.createQuery(sql).getResultList();
    }
    public List<WfStepmstr> getwfstepmatrs(Long id){
    	String sql = "select wfstep from WfStepmstr wfstep where wfstep.wfInstancemstr.id = "+id+" order by wfstep.id";
    	return em.createQuery(sql).getResultList();
    }
    public List<WfStepmstrProperty> getWfStepProperty(Long id){
    	String sql = "select wfstepPro from WfStepmstrProperty wfstepPro where wfstepPro.wfStepmstr.id = "+id+"";
    	return em.createQuery(sql).getResultList();
    }
    
    /**
     * <p>Description:查询现在这个工作流到达的工作节点,用于判断. </p>
     * @param workflowNumber 工作流唯一标识
     * @return 返回节点的名称.
     * @throws VWException 查询FileNet出现问题.
     */
    public String queryNowWorkflowPlace(String workflowNumber) throws VWException{
        String nowWorkflowPlace=defaultWorkflowImpl.getWorkStepNameById(workflowNumber);
        logger.info("nowWorkflowPlace167:"+nowWorkflowPlace);
        return nowWorkflowPlace;
    }
    
    /**
     * Description: 创建流程
     * @param intanceMap wfInsProMap,主表的key,value的集合
     * @param requestForm  如:public String TIH_TAX_REQUESTFORM_4="TIH.TAX.REQUESTFORM.4";//报送报表流程
     * @param workflowName 流程名称,fileNet端的流程名.
     * @param stepMap  步骤属性表的key,value集合
     * @param nodeName 下个节点名
     * @param userName 下个节点的处理人账户
     * @throws VWException
     */
    //Page with Excel Service,执行工作流数据存储 重载(Overloading)三个方法
    //执行工作流的方法体
    public void processSumbitData(Map<String, Object> paramMap,Map<String, String> stepMap,WfStepmstr saveStep,String workflownumber) throws Exception{
        this.processDialogExecuteWorkflow(paramMap, stepMap, saveStep, workflownumber);
    }
    
    //执行工作流并且要更新wfinsPro里附件id和附件Name
    @SuppressWarnings("unchecked")
	public void processSumbitData(Map<String, Object> paramMap,Map<String, String> stepMap,WfStepmstr saveStep,String workflownumber,WfInstancemstrProperty wfInsProFileId,WfInstancemstrProperty wfInsProFileName,boolean isUpdate,WfInstancemstrProperty wfInsProWithExcelTime,Object[] excelInfo) throws Exception{
        this.processDialogExecuteWorkflow(paramMap, stepMap, saveStep, workflownumber);
        if(isUpdate){
        	this.updateWfInsPro(wfInsProFileId);
            this.updateWfInsPro(wfInsProFileName);
        }
        this.updateWfInsPro(wfInsProWithExcelTime);
        if(null!=excelInfo){
        	String type=excelInfo[2].toString();
        	List<Long> idList=(List<Long>) excelInfo[3];
        	if(type.trim().equals("vat")){
        		for(Long vatId:idList){
        			logger.info("N->Y ID:"+vatId);
        			String jpql="UPDATE ReportVatIptDeduction vat set vat.defunctInd='Y' where vat.id="+vatId+"";
        			logger.info("JPQL:"+jpql);
        			this.em.createQuery(jpql).executeUpdate();
        		}
        	}else if(type.trim().equals("pay")){
        		for(Long payId:idList){
        			String jpql="UPDATE ReportPayableTax pay set pay.defunctInd='Y' where pay.id="+payId+"";
        			this.em.createQuery(jpql).executeUpdate();
        		}
        	}
        	for(Long id:idList){
        		logger.info("N->Y ID:"+id);
        		String info="报表信息被"+workflownumber+"单的数据覆盖.";
        		String jpql="UPDATE WfInstancemstrProperty wp set wp.value='"+info+"' where wp.name='"+WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID+"' and wp.value='"+id.toString()+"'";
        		logger.info("JPQL:"+jpql);
        		this.em.createQuery(jpql).executeUpdate();
        	}
        }
    }
    //执行最后一步,最后一个参数为要修改主表的信息,这个重载方法用于最后一步.
    public void processSumbitData(Map<String, Object> paramMap,Map<String, String> stepMap,WfStepmstr saveStep,String workflownumber,WfInstancemstr wfInsWithIndexSend) throws Exception{
        this.processDialogExecuteWorkflow(paramMap, stepMap, saveStep, workflownumber);
        this.lastStepEditWfIns(wfInsWithIndexSend);
    }
    
    /**
     * <p>Description:执行流程处理步骤,和fileNet打交道并且存数据到DB2. </p>
     * @param paramMap 里面有两个值,一个是下个节点的节点Name,一个是路由参数.如:paramMap.put(roleName, new String[] { "uid=" + userName + "," + dnStr }); paramMap.put("status", this.status);
     * @param stepMap 步骤属性表的数据,这里只会添加一条数据.
     * @param saveStep 保存的步骤表的数据.
     * @param workflownumber 工作流唯一标识.
     * @throws Exception 
     */
    private void processDialogExecuteWorkflow(Map<String, Object> paramMap,Map<String, String> stepMap,WfStepmstr saveStep,String workflownumber) throws Exception{
        this.defaultWorkflowImpl.doDispath(paramMap, saveStep, stepMap, workflownumber);
    }
    
    /**
     * <p>Description:执行流程的最后一步,将主表的属性改为完成 </p>
     * @param w 主表
     */
    private void lastStepEditWfIns(WfInstancemstr w){
        //设置wfInsWithIndexSend的状态为完成
        w.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_3);
        w.setUpdatedBy(loginService.getCurrentUserName());
        w.setUpdatedDatetime(new Date());
        this.defaultWorkflowImpl.editWfInstance(w);
    }
    
	//添加附件,抛出2个异常,在Bean里进行抓取
	public Document addFileCE(UploadedFile upFile) throws MimeException,Exception{
	    String fileName = upFile.getFileName();
        InputStream inputStream;
        com.filenet.api.core.Document document;
        inputStream = upFile.getInputstream();
        ResourceBundle rb=ResourceBundle.getBundle("filenet");
        String folder=rb.getString("ce.folder.mission");
        String tihDoc=rb.getString("ce.document.classid");
        logger.info("folder:"+folder);
        document=fileUpService.upLoadDocumentCheckIn(inputStream, new HashMap<String, Object>(), fileName, tihDoc, folder);
        return document;
	}
	//下载附件
    public StreamedContent downloadFile(String fileId) throws MimeException,Exception {
        return fileUpService.downloadDocumentEncoding(fileId,"utf-8","iso8859-1");
    }
    
    public void updateWfInsPro(WfInstancemstrProperty wfInsPro){
    	this.em.merge(wfInsPro);
    }
    
    @SuppressWarnings("unchecked")
	public String queryCompanyNameById(String id){
    	String jpql="SELECT o from O o where o.id='"+id+"' ";
    	logger.info("jpql:"+jpql);
    	List<O> list=this.em.createQuery(jpql).getResultList();
    	return list.get(0).getStext();
    }
    
    public Object[] validatorExcel(boolean isVAT,UploadedFile upFile,String oid,Date uploadDate) throws Exception{
    	Object[] objArray;
		if (isVAT) {
			//调我的方法进行效验
			objArray=vATReportEntryService.readExcel(upFile, oid, uploadDate);
		} else {
			objArray=importReportPayableTaxService.importTaxExcel(upFile, oid, uploadDate);
		}
		return objArray;
    }
    
    /**
     * @param excelDataArray 效验Excel后得到的数据.2个的数组数据不一样.
     * @param isVAT 布尔值,true为vat.
     * @param longId 需要转换为long类型进行数据库查询.这个id是vat或者payableExcel表信息的ID.
     */
    @SuppressWarnings("unchecked")
	public void saveOrUpdateExcelData(Object[] excelDataArray,boolean isVAT,String excelLongId,Date statisticDate,long wfInsId,String remarks){
    	String newExcelTableId="";
    	if(isVAT){
    		//保存主表,其他每条数据都是一个实体保存到VAT二表. 
        	ReportVatIptDeduction vatReport=(ReportVatIptDeduction) excelDataArray[0];
        	vatReport.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_2);//设置流程状态,如果是问题结束的时候需要单独将状态修改为完成.
        	vatReport.setStatisticDatetime(statisticDate);
        	List<ReportVatIptDeductionDetail> vatDetailList=(List<ReportVatIptDeductionDetail>) excelDataArray[1];
    		if(excelLongId == null){
    			//表示是VAT的添加操作
    			newExcelTableId=this.saveVatExcel(vatReport,vatDetailList);
    		}else{
    			logger.info("更新操作,时间是:"+vatReport.getStatisticDatetime());
    			//否则就是VAT的更新操作
    			newExcelTableId=this.updateVatExcel(vatReport,vatDetailList,Long.parseLong(excelLongId));
    		}
    	}else{
    		ReportPayableTax rpPay=(ReportPayableTax) excelDataArray[0];
    		rpPay.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_2);
    		rpPay.setStatisticDatetime(statisticDate);
    		if(excelLongId == null || "".equals(excelLongId)){
    			//表示是payable的添加操作
    			newExcelTableId=this.savePayableExcel(excelDataArray,rpPay);
    			//最后要更新ID到主表属性表的里面
    		}else{
    			//否则就是payable的更新操作
    			newExcelTableId=this.updatePayableExcel(excelDataArray,Long.parseLong(excelLongId),rpPay);
    		}
    	}
    	//更新主表属性表的数据.
    	updateWfInsProDataWithExcelTableId(wfInsId,newExcelTableId);
    }
    
    
	private String saveVatExcel(ReportVatIptDeduction vatReport,List<ReportVatIptDeductionDetail> vatDetailList){
    	this.em.persist(vatReport);
    	for(ReportVatIptDeductionDetail rd:vatDetailList){
    		rd.setReportVatIptDeduction(vatReport);
    		this.em.persist(rd);
    	}
    	return vatReport.getId().toString();
    }
    
    private String updateVatExcel(ReportVatIptDeduction vatReport,List<ReportVatIptDeductionDetail> vatDetailList,long vatlongId){
    	//更新,先设ID.
    	vatReport.setId(vatlongId);
    	this.em.merge(vatReport);
    	for(ReportVatIptDeductionDetail rd:vatDetailList ){
    		//根据主表id,发票类型编码,品种编码获取唯一性的ID
    		long vatDeductionId=this.queryVATDetailId(vatlongId, rd.getInvoiceTypeCode(), rd.getVarietyCode());
    		rd.setId(vatDeductionId);
    		rd.setUpdatedDatetime(new Date());
    		this.em.merge(rd);
    	}
    	return vatReport.getId().toString();
    }

    @SuppressWarnings("unchecked")
	private String savePayableExcel(Object[] excelDataArray,ReportPayableTax rpPay){
    	//保存
    	this.em.persist(rpPay);
    	 // 第一部分：增值税
    	List<ReportPayableTaxVat> taxVats=(List<ReportPayableTaxVat>) excelDataArray[1];
    	for(ReportPayableTaxVat taxVAT:taxVats){
    		taxVAT.setReportPayableTax(rpPay);
    		this.em.persist(taxVAT);
    	}
    	// 第二部分：其他税种
    	List<ReportPayableTaxOther> taxOthers=(List<ReportPayableTaxOther>) excelDataArray[2];
    	for(ReportPayableTaxOther taxOther:taxOthers){
    		taxOther.setReportPayableTax(rpPay);
    		this.em.persist(taxOther);
    	}
    	//所得税（分季度填写）
    	ReportPayableTaxIncome income =(ReportPayableTaxIncome) excelDataArray[3];
    	income.setReportPayableTax(rpPay);
    	this.em.persist(income);
    	// 增值税留抵明细表
    	ReportPayableTaxStayed stayed=(ReportPayableTaxStayed) excelDataArray[4];
    	stayed.setReportPayableTax(rpPay);
    	em.persist(stayed);
    	// 印花税
        List<ReportPayableTaxStamp> taxStamps = (List<ReportPayableTaxStamp>) excelDataArray[5];
        for(ReportPayableTaxStamp taxStamp:taxStamps){
        	taxStamp.setReportPayableTax(rpPay);
        	this.em.persist(taxStamp);
        }
        // 房产税
        List<ReportPayableTaxEstate> taxEstates = (List<ReportPayableTaxEstate>) excelDataArray[6];
        for(ReportPayableTaxEstate taxEstate:taxEstates){
        	taxEstate.setReportPayableTax(rpPay);
        	this.em.persist(taxEstate);
        }
        // 土地使用税
        List<ReportPayableTaxLand> taxLands = (List<ReportPayableTaxLand>) excelDataArray[7];
        for(ReportPayableTaxLand taxLand:taxLands){
        	taxLand.setReportPayableTax(rpPay);
        	this.em.persist(taxLand);
        }
        // 土地使用税
        List<ReportPayableTaxAddedMaterial> taxAddedMaterials = (List<ReportPayableTaxAddedMaterial>) excelDataArray[8];
        for(ReportPayableTaxAddedMaterial taxAddedMaterial:taxAddedMaterials){
        	taxAddedMaterial.setReportPayableTax(rpPay);
        	this.em.persist(taxAddedMaterial);
        }
        return rpPay.getId().toString();
    }
    
    @SuppressWarnings("unchecked")
	private String updatePayableExcel(Object[] excelDataArray,long excelLongId,ReportPayableTax rpPay){
    	
    	String newId=this.savePayableExcel(excelDataArray, rpPay);
    	
    	//通过id查找到主表信息,remove.
    	String jpql="SELECT rp from ReportPayableTax rp where rp.id="+excelLongId+" ";
    	List<ReportPayableTax> listRP=this.em.createQuery(jpql).getResultList();
    	ReportPayableTax rp=listRP.get(0);
    	// 第一部分：增值税
    	for(ReportPayableTaxVat rt:rp.getReportPayableTaxVats()){
    		this.em.remove(rt);
    	}
    	// 第二部分：其他税种
    	for(ReportPayableTaxOther r:rp.getReportPayableTaxOthers()){
    		this.em.remove(r);
    	}
    	//所得税（分季度填写）
    	for (ReportPayableTaxIncome r : rp.getReportPayableTaxIncomes()) {
    		this.em.remove(r);
		}
    	// 增值税留抵明细表
    	for(ReportPayableTaxStayed r:rp.getReportPayableTaxStayeds()){
    		this.em.remove(r);
    	}
    	// 印花税
    	for(ReportPayableTaxStamp r:rp.getReportPayableTaxStamps()){
    		this.em.remove(r);
    	}
    	// 房产税
    	for( ReportPayableTaxEstate r:rp.getReportPayableTaxEstates()){
    		this.em.remove(r);
    	}
    	// 土地使用税
    	for(ReportPayableTaxLand r:rp.getReportPayableTaxLands()){
    		this.em.remove(r);
    	}
    	 // 土地使用税
    	for(ReportPayableTaxAddedMaterial r :rp.getReportPayableTaxAddedMaterials()){
    		this.em.remove(r);
    	}
    	this.em.remove(rp);
    	
    	return newId;
    }
	
	//根据vat主表id,发票编码,品种编码来查询得到vat附表的id.
	@SuppressWarnings("unchecked")
	private long queryVATDetailId(long vatId,String invoiceTypeCode,String varietyCode){
		String jpql="select vd from ReportVatIptDeductionDetail vd where vd.reportVatIptDeduction.id="+vatId+" AND vd.invoiceTypeCode='"+invoiceTypeCode+"' AND vd.varietyCode='"+varietyCode+"'";
		List<ReportVatIptDeductionDetail> list=this.em.createQuery(jpql).getResultList();
		return list.get(0).getId();
	}
	
	private void updateWfInsProDataWithExcelTableId(long wfInsId,String newId){
		String jpql="UPDATE WfInstancemstrProperty wp SET wp.value='"+newId+"' WHERE wp.wfInstancemstr.id="+wfInsId+" AND wp.name='"+WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID+"' ";
		this.em.createQuery(jpql).executeUpdate();
	}
	
	public void updateExcelStatusToOver(String excelType,long excelId){
		logger.info("updateExcelStatusToOver:excelType-->"+excelType);
		logger.info("updateExcelStatusToOver:excelId-->"+excelId);
		String over=DictConsts.TIH_TAX_WORKFLOWSTATUS_3;
		String jpql="";
		//增值税进项税额抵扣情况表
		if(excelType.equals(DictConsts.TIH_TAX_REQUESTFORM_4_2)){
			jpql="UPDATE ReportVatIptDeduction vat SET vat.status='"+over+"' where vat.id="+excelId+"";
		}else{
			//应交税费综合表
			jpql="UPDATE ReportPayableTax tax SET tax.status='"+over+"' where tax.id="+excelId+"";
		}
		logger.info("updateExcelStatusToOver:"+jpql);
		if(!jpql.equals("")){
			this.em.createQuery(jpql).executeUpdate();
			logger.info("successful!");
		}
	}
	
	//判断是否必须提交excel文件,还是说可以不提交,或者说是通过时间判断是不能提交
	@SuppressWarnings("unchecked")
	public Object[] judgeExcelCondition(String wfInsProExcelId,String oid,Date statisticsTime,boolean isVAT,WfStepmstr lastStep){
		String haveError="no";
		String errorInfo="no";
		String type="";
		List<Long> idList=new ArrayList<Long>();
		
		Long companyId=vATReportEntryService.queryCompanyByOid(oid).getId();
		String companyName=this.vATReportEntryService.queryOById(oid).getStext();
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(statisticsTime);
		int yearTime=calendar.get(Calendar.YEAR);
		int monthTime=calendar.get(Calendar.MONTH)+1;
        String excelCollectTime = monthTime < 10 ? yearTime + "-0" + monthTime : yearTime + "-" + monthTime;
        String queryDate = excelCollectTime + "-01 00:00:00";
		if(isVAT){
		    StringBuffer jpql = new StringBuffer();
		    jpql.append(" select vat.ID from REPORT_VAT_IPT_DEDUCTION vat,WF_INSTANCEMSTR_PROPERTY wfp,WF_INSTANCEMSTR wf");
		    jpql.append(" where cast(vat.id as VARCHAR(2000)) = wfp.value and wf.ID = wfp.WF_INSTANCEMSTR_ID");
		    jpql.append(" and vat.DEFUNCT_IND <> 'Y'");
		    jpql.append(" and vat.COMPANYMSTR_ID=").append(companyId);
		    jpql.append(" and vat.STATISTIC_DATETIME= '" + queryDate +"'");
		    jpql.append(" and wf.status = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_3 + "'");
		    idList = this.em.createNativeQuery(jpql.toString()).getResultList();
			if(!idList.isEmpty()){
				 errorInfo="报送清单："+companyName+"已经有完成的"+excelCollectTime+"的增值税进项税额抵扣Excel统计!";
				 haveError="warn";
				 type="vat";
			}
		}else{
		    StringBuffer jpql = new StringBuffer();
            jpql.append(" select pay.ID from REPORT_PAYABLE_TAX pay,WF_INSTANCEMSTR_PROPERTY wfp,WF_INSTANCEMSTR wf");
            jpql.append(" where cast(pay.id as VARCHAR(2000)) = wfp.value and wf.ID = wfp.WF_INSTANCEMSTR_ID");
            jpql.append(" and pay.DEFUNCT_IND <> 'Y'");
            jpql.append(" and pay.COMPANYMSTR_ID=").append(companyId);
            jpql.append(" and pay.STATISTIC_DATETIME= '" + queryDate +"'");
            jpql.append(" and wf.status = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_3 + "'");
            idList=this.em.createNativeQuery(jpql.toString()).getResultList();
			if(!idList.isEmpty()){
				 errorInfo="报送清单："+companyName+"已经有完成的"+excelCollectTime+"的应交税费Excel统计!";
				 haveError="warn";
				 type="pay";
			}
		}
		if(isVAT){
		    StringBuffer jpql = new StringBuffer();
            jpql.append(" select vat.ID from REPORT_VAT_IPT_DEDUCTION vat,WF_INSTANCEMSTR_PROPERTY wfp,WF_INSTANCEMSTR wf");
            jpql.append(" where cast(vat.id as VARCHAR(2000)) = wfp.value and wf.ID = wfp.WF_INSTANCEMSTR_ID");
            jpql.append(" and vat.DEFUNCT_IND <> 'Y'");
            jpql.append(" and vat.COMPANYMSTR_ID=").append(companyId);
            jpql.append(" and vat.STATISTIC_DATETIME= '" + queryDate +"'");
            jpql.append(" and wf.status = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
            idList=this.em.createNativeQuery(jpql.toString()).getResultList();
			if(!idList.isEmpty()&&DictConsts.TIH_TAX_APPROACH_1.equals(lastStep.getDealMethod())){
				 errorInfo="报送清单："+companyName+"已经有在进行的"+excelCollectTime+"的增值税进项税额抵扣Excel统计!不允许上传新的统计信息!";
				 haveError=ERROR;
				 type="vat";
			}
		}else{
		    StringBuffer jpql = new StringBuffer();
            jpql.append(" select pay.ID from REPORT_PAYABLE_TAX pay,WF_INSTANCEMSTR_PROPERTY wfp,WF_INSTANCEMSTR wf");
            jpql.append(" where cast(pay.id as VARCHAR(2000)) = wfp.value and wf.ID = wfp.WF_INSTANCEMSTR_ID");
            jpql.append(" and pay.DEFUNCT_IND <> 'Y'");
            jpql.append(" and pay.COMPANYMSTR_ID=").append(companyId);
            jpql.append(" and pay.STATISTIC_DATETIME= '" + queryDate +"'");
            jpql.append(" and wf.status = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
            idList=this.em.createNativeQuery(jpql.toString()).getResultList();
			if(!idList.isEmpty()&&DictConsts.TIH_TAX_APPROACH_1.equals(lastStep.getDealMethod())){
				 errorInfo="报送清单："+companyName+"已经有在进行的"+excelCollectTime+"的应交税费Excel统计!不允许上传新的统计信息!";
				 haveError=ERROR;
				 type="pay";
			}
		}
		
		Object[] error={haveError,errorInfo,type,idList};
		return error;
	}
	
	@SuppressWarnings("rawtypes")
	public List workflowDetail(String no){
		String sql = "select w from WfStepmstrProperty w where w.name = '"+WorkflowConsts.SENDREPORT_SQLKEY_OPINION+"' and w.wfStepmstr.wfInstancemstr.no ='"+no+"' order by w.wfStepmstr.id ASC";
		return this.em.createQuery(sql).getResultList();
	}
	
	public WfInstancemstr saveDrafts(Map<String,String> map,String remarks,String importance,String urgency){
		WfInstancemstr wfIns=new WfInstancemstr();
		wfIns.setNo("未启动");
		wfIns.setType(DictConsts.TIH_TAX_REQUESTFORM_4);
		wfIns.setRequestBy(loginService.getCurrentUserName());
		wfIns.setSubmitDatetime(new Date());
		wfIns.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_1);
		wfIns.setDefunctInd("N");
		wfIns.setCreatedBy(loginService.getCurrentUserName());
		wfIns.setCreatedDatetime(new Date());
		wfIns.setUpdatedBy(loginService.getCurrentUserName());
		wfIns.setUpdatedDatetime(new Date());
		wfIns.setRemarks(remarks);
		wfIns.setImportance(importance);
		wfIns.setUrgency(urgency);
		this.em.persist(wfIns);
		for(Map.Entry<String,String> temp:map.entrySet()){
			logger.info("temp.getKey():"+temp.getKey());
			logger.info("temp.getValue():"+temp.getValue());
			WfInstancemstrProperty wp=new WfInstancemstrProperty();
			wp.setName(temp.getKey());
			wp.setValue(temp.getValue());
			wp.setWfInstancemstr(wfIns);
			this.em.persist(wp);
		}
		return wfIns;
	}
	
	public void deleteDrafts(WfInstancemstr wfIns){
		wfIns.setDefunctInd("Y");
		wfIns.setUpdatedDatetime(new Date());
		this.em.merge(wfIns);
	}
	
	public void updateDrafts(WfInstancemstr wfIns,Map<String,String> map,String remarks,String importance,String urgency){
		wfIns.setUpdatedBy(loginService.getCurrentUserName());
		wfIns.setUpdatedDatetime(new Date());
		wfIns.setRemarks(remarks);
		wfIns.setImportance(importance);
		wfIns.setUrgency(urgency);
		this.em.merge(wfIns);
		List<WfInstancemstrProperty> listPro=wfIns.getWfInstancemstrProperties();
		for(Map.Entry<String,String> temp:map.entrySet()){
			String name=temp.getKey();
			String value=temp.getValue();
			for(WfInstancemstrProperty wp:listPro){
				if(wp.getName().equals(name)){
					wp.setValue(value);
					this.em.merge(wp);
				}
			}
		}
	}
	
	public String getCompanyNameByOid(String oid){
		String sql = "select o from O o where o.defunctInd <> 'Y' and o.id = '"+oid+"' ";
		List<O> resultList = this.em.createQuery(sql).getResultList();
		String name = "";
		if(resultList.size() > 0){
			name = resultList.get(0).getStext();
		}
		return name;
	}
	
}
