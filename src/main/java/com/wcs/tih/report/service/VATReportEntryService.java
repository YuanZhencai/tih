package com.wcs.tih.report.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.UploadedFile;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.tih.model.ReportVatIptDeduction;
import com.wcs.tih.model.ReportVatIptDeductionDetail;
import com.wcs.tih.report.controller.helper.ExcelService;


@Stateless
public class VATReportEntryService {
	
	@EJB 
	private LoginService loginService;
	@EJB 
	private ExcelService excelService;
    private String defunctInd;
    private Date currentDate;
    private String currentUser;
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct 
    public void init() {
        defunctInd = "N";
        currentDate = new Date();
        currentUser = loginService.getCurrentUsermstr().getAdAccount();
    }
    
    /**
     * @param fileAddress
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    public Object[] readExcel(UploadedFile upFile, String oId, Date uploadDate) throws FileNotFoundException, IOException ,Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("excel");
    	Workbook wb = excelService.createWorkbook(upFile);
    	String vatReportName = bundle.getString("vatReportName");
    	String vatReportVersion = bundle.getString("vatReportVersion");
    	String error = "请上传增值税进项税抵扣报表，版本号："+vatReportVersion+"；请到首页下载最新模版";
    	//校验是否
        Sheet versionSheet = wb.getSheet("TIH-TEMPLATE-INFO");
        Row lastRow;
        if(versionSheet != null){
            lastRow = versionSheet.getRow(versionSheet.getLastRowNum());
        }else{
            throw new Exception(error);
        }
        if(lastRow != null){
            Cell nameCell = lastRow.getCell(0);
            if(!vatReportName.equals(nameCell.getStringCellValue())){
                throw new Exception(error);
            }
            Cell versionCell = lastRow.getCell(1);
            if(!vatReportVersion.equals(versionCell.getStringCellValue())){
                throw new Exception(error);
            }
        }else{
            throw new Exception(error);
        }
    	
        //读取第一个sheet
        Sheet sheet = (Sheet) wb.getSheetAt(0);
        
        //VAT主表
        ReportVatIptDeduction vatReport=new ReportVatIptDeduction();
        vatReport.setCompanymstrId(queryCompanyByOid(oId).getId()); //查询得到
        vatReport.setCompanyName(queryOById(oId).getStext()); //要先和页面对比然后用传来的值
        vatReport.setBukrs(queryOById(oId).getBukrs()); //公司代码,需要查询
        //vatReport.setTaxRate(111); //税率,前五个的 在下面86行设
        //vatReport.setStatisticDatetime(statisticTime); //这个时间不确定!在最后添加
        //vatReport.setStatus("111"); //和流程的状态一样.在最后添加
        vatReport.setDefunctInd(defunctInd);
        vatReport.setUpdatedBy(currentUser);
        vatReport.setUpdatedDatetime(currentDate);
        vatReport.setCreatedBy(currentUser);
        vatReport.setCreatedDatetime(currentDate);
        
        //通过月份获取到当前的起始列数
        Calendar c=Calendar.getInstance();
        c.setTime(uploadDate);
        int month=(c.get(Calendar.MONTH) + 1);
        int beginColumn=queryColumnNumber(month);
        
        //设置vat主表的税率
        Row taxRow=sheet.getRow(5);
        vatReport.setTaxRate(excelService.readDouble(taxRow, beginColumn+2));
        
        List<ReportVatIptDeductionDetail> vatDetailList=new ArrayList<ReportVatIptDeductionDetail>();
        for(int i=0;i<5;i++){
        	//获取发票类型名,循环五次
        	Row invoiceRow=sheet.getRow(i*12+5);
        	String invoiceTypeName=excelService.readString(invoiceRow, 0);
        	for(int j=0;j<10;j++){
        		//获取每次循环的行数
        		Row overRow=sheet.getRow(i*12+5+j);
        		
        		ReportVatIptDeductionDetail vatDetail=new ReportVatIptDeductionDetail();
            	//vatDetail.setReportVatIptDeduction(vatReport);  持久化时添加!!!!!!!!!
            	vatDetail.setInvoiceTypeCode(""); //发票类型编码
            	vatDetail.setInvoiceTypeName(invoiceTypeName); //发票类型名
            	vatDetail.setVarietyCode("");			//品种编码
            	vatDetail.setVarietyName(excelService.readString(overRow, 1));			//品种名,永远在"1"列
            	//excel中排列为:数量,金额,税率,税额
            	vatDetail.setTaxRate(excelService.readDouble(overRow, beginColumn+2));					//税率
            	vatDetail.setAmmount(excelService.readDouble(overRow, beginColumn));					//数量
            	vatDetail.setMoneySum(excelService.readBigDecimal(overRow, beginColumn+1));				//金额
            	vatDetail.setTaxAmmount(excelService.readBigDecimal(overRow, beginColumn+3));			//税额
            	vatDetail.setCreatedBy(currentUser);
            	vatDetail.setCreatedDatetime(currentDate);
            	vatDetail.setUpdatedBy(currentUser);
            	vatDetail.setUpdatedDatetime(currentDate);
            	vatDetailList.add(vatDetail);
        	}
        }
        
        //增值税专用发票
        Row invoiceRowLast3=sheet.getRow(66);
    	String last3invoiceTypeName=(excelService.readString(invoiceRowLast3, 0));
        for(int i=0;i<5;i++){
        	Row overRow=sheet.getRow(66+i);
        	
        	ReportVatIptDeductionDetail vatDetail=new ReportVatIptDeductionDetail();
        	//vatDetail.setReportVatIptDeduction(vatReport);  持久化时添加!!!!!!!!!
        	vatDetail.setInvoiceTypeCode(""); //发票类型编码
        	vatDetail.setInvoiceTypeName(last3invoiceTypeName); //发票类型名
        	vatDetail.setVarietyCode("");			//品种编码
        	vatDetail.setVarietyName(excelService.readString(overRow, 1));			//品种名,永远在"1"列
        	//excel中排列为:数量,金额,税率,税额
        	vatDetail.setTaxRate(excelService.readDouble(overRow, beginColumn+2));					//税率
        	vatDetail.setAmmount(excelService.readDouble(overRow, beginColumn));					//数量
        	vatDetail.setMoneySum(excelService.readBigDecimal(overRow, beginColumn+1));				//金额
        	vatDetail.setTaxAmmount(excelService.readBigDecimal(overRow, beginColumn+3));			//税额
        	vatDetail.setCreatedBy(currentUser);
        	vatDetail.setCreatedDatetime(currentDate);
        	vatDetail.setUpdatedBy(currentUser);
        	vatDetail.setUpdatedDatetime(currentDate);
        	vatDetailList.add(vatDetail);
        }
        
        //运输费用结算单据
        Row invoiceRowLast2=sheet.getRow(72);
    	String last2invoiceTypeName=(excelService.readString(invoiceRowLast2, 0));
        for(int i=0;i<3;i++){
        	Row overRow=sheet.getRow(72+i);
        	
        	ReportVatIptDeductionDetail vatDetail=new ReportVatIptDeductionDetail();
        	//vatDetail.setReportVatIptDeduction(vatReport);  持久化时添加!!!!!!!!!
        	vatDetail.setInvoiceTypeCode(""); //发票类型编码
        	vatDetail.setInvoiceTypeName(last2invoiceTypeName); //发票类型名
        	vatDetail.setVarietyCode("");			//品种编码
        	vatDetail.setVarietyName(excelService.readString(overRow, 1));			//品种名,永远在"1"列
        	//excel中排列为:数量,金额,税率,税额
        	vatDetail.setTaxRate(excelService.readDouble(overRow, beginColumn+2));					//税率
        	vatDetail.setAmmount(excelService.readDouble(overRow, beginColumn));					//数量
        	vatDetail.setMoneySum(excelService.readBigDecimal(overRow, beginColumn+1));				//金额
        	vatDetail.setTaxAmmount(excelService.readBigDecimal(overRow, beginColumn+3));			//税额
        	vatDetail.setCreatedBy(currentUser);
        	vatDetail.setCreatedDatetime(currentDate);
        	vatDetail.setUpdatedBy(currentUser);
        	vatDetail.setUpdatedDatetime(currentDate);
        	vatDetailList.add(vatDetail);
        }
        
        //海关进口增值税专用缴款书 76,77
        Row invoiceRowLast=sheet.getRow(76);
    	String lastinvoiceTypeName=(excelService.readString(invoiceRowLast, 0));
        for(int i=0;i<2;i++){
        	Row overRow=sheet.getRow(76+i);
        	
        	ReportVatIptDeductionDetail vatDetail=new ReportVatIptDeductionDetail();
        	//vatDetail.setReportVatIptDeduction(vatReport);  持久化时添加!!!!!!!!!
        	vatDetail.setInvoiceTypeCode(""); //发票类型编码
        	vatDetail.setInvoiceTypeName(lastinvoiceTypeName); //发票类型名
        	vatDetail.setVarietyCode("");			//品种编码
        	vatDetail.setVarietyName(excelService.readString(overRow, 1));			//品种名,永远在"1"列
        	//excel中排列为:数量,金额,税率,税额
        	vatDetail.setTaxRate(excelService.readDouble(overRow, beginColumn+2));					//税率
        	vatDetail.setAmmount(excelService.readDouble(overRow, beginColumn));					//数量
        	vatDetail.setMoneySum(excelService.readBigDecimal(overRow, beginColumn+1));				//金额
        	vatDetail.setTaxAmmount(excelService.readBigDecimal(overRow, beginColumn+3));			//税额
        	vatDetail.setCreatedBy(currentUser);
        	vatDetail.setCreatedDatetime(currentDate);
        	vatDetail.setUpdatedBy(currentUser);
        	vatDetail.setUpdatedDatetime(currentDate);
        	vatDetailList.add(vatDetail);
        }
        
        //over 79
        Row over=sheet.getRow(79);
        String overinvoiceTypeName=(excelService.readString(over, 0));
        ReportVatIptDeductionDetail vatDetail=new ReportVatIptDeductionDetail();
       //vatDetail.setReportVatIptDeduction(vatReport);  持久化时添加!!!!!!!!!
        vatDetail.setInvoiceTypeCode(""); //发票类型编码
    	vatDetail.setInvoiceTypeName(overinvoiceTypeName); //发票类型名
    	vatDetail.setVarietyCode("");			//品种编码
    	vatDetail.setVarietyName(overinvoiceTypeName);			//品种名,永远在"1"列
    	//excel中排列为:数量,金额,税率,税额
    	vatDetail.setTaxRate(excelService.readDouble(over, beginColumn+2));					//税率
    	vatDetail.setAmmount(excelService.readDouble(over, beginColumn));					//数量
    	vatDetail.setMoneySum(excelService.readBigDecimal(over, beginColumn+1));				//金额
    	vatDetail.setTaxAmmount(excelService.readBigDecimal(over, beginColumn+3));			//税额
    	vatDetail.setCreatedBy(currentUser);
    	vatDetail.setCreatedDatetime(currentDate);
    	vatDetail.setUpdatedBy(currentUser);
    	vatDetail.setUpdatedDatetime(currentDate);
    	vatDetailList.add(vatDetail);
    	List<String> errMsgs = excelService.getErrMsgs();
        Object[] objArray={vatReport,vatDetailList,errMsgs};
        excelService.setErrMsgs(new ArrayList<String>());
        return objArray;
    }
    
    // 判断月份,获取对应的列行
    private int queryColumnNumber(int month) {
        // 一月份对应的要读的列数为2,3,5,第一行为int row=5;.依序依次加1
        return 2 + 4 * (month - 1);
    }
    
    
    @SuppressWarnings("unchecked")
	public O queryOById(String oid){
    	String jpql="select o from O o where o.id='"+oid+"' ";
    	List<O> list=this.em.createQuery(jpql).getResultList();
    	O o=new O();
    	if(!list.isEmpty()){
    		o=list.get(0);
    	}
    	return o;
    }
    
    @SuppressWarnings("unchecked")
	public Companymstr queryCompanyByOid(String oid){
    	String jpql="SELECT c from Companymstr c where c.oid='"+oid+"'";
    	List<Companymstr> list=this.em.createQuery(jpql).getResultList();
    	return list.get(0);
    } 
}
