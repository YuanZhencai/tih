package com.wcs.tih.report.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.tih.model.ReportPayableTax;
import com.wcs.tih.model.ReportPayableTaxAddedMaterial;
import com.wcs.tih.model.ReportPayableTaxEstate;
import com.wcs.tih.model.ReportPayableTaxIncome;
import com.wcs.tih.model.ReportPayableTaxLand;
import com.wcs.tih.model.ReportPayableTaxOther;
import com.wcs.tih.model.ReportPayableTaxStamp;
import com.wcs.tih.model.ReportPayableTaxStayed;
import com.wcs.tih.model.ReportPayableTaxVat;
import com.wcs.tih.report.controller.helper.ExcelService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: 导入应缴税费Excel
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class ImportReportPayableTaxService {
    private Logger logger = LoggerFactory.getLogger(ImportReportPayableTaxService.class);
    
    @PersistenceContext 
    private EntityManager em;
    @EJB 
    private LoginService loginService;
    @EJB 
    private ExcelService excelService;
    private String defunctInd;
    private Date currentDate;
    private String currentUser;
    
    
    @PostConstruct public void init() {
        defunctInd = "N";
        currentDate = new Date();
        currentUser = loginService.getCurrentUsermstr().getAdAccount();
    }
    
    /**
     * <p>Description: 导入报表</p>
     * @param is
     * @param oid
     * @param uploadDate
     * @return
     * @throws Exception
     */
    public Object[] importTaxExcel(UploadedFile upFile, String oid, Date uploadDate) throws Exception {
        // read the fourth sheet
        ResourceBundle bundle = ResourceBundle.getBundle("excel");
    	Workbook wb = excelService.createWorkbook(upFile);
    	Sheet versionSheet = wb.getSheet("TIH-TEMPLATE-INFO");
    	String taxReportName = bundle.getString("taxReportName");
    	String taxReportVersion = bundle.getString("taxReportVersion");
    	String error = "请上传应交税费报表，版本号："+taxReportVersion+"；请到首页下载最新模版";
    	Row lastRow;
    	if(versionSheet != null){
    	    lastRow = versionSheet.getRow(versionSheet.getLastRowNum());
    	}else{
    	    throw new Exception(error);
    	}
    	if(lastRow != null){
    	    Cell nameCell = lastRow.getCell(0);
    	    if(!taxReportName.equals(nameCell.getStringCellValue())){
    	        throw new Exception(error);
    	    }
    	    Cell versionCell = lastRow.getCell(1);
    	    if(!taxReportVersion.equals(versionCell.getStringCellValue())){
    	        throw new Exception(error);
    	    }
    	}else{
    	    throw new Exception(error);
    	}
        Sheet sheet = wb.getSheet("2");//通过sheet名称取得sheet
        // 应交税费主表
        ReportPayableTax tax = packageTax(sheet, oid, uploadDate);
        // 第一部分：增值税
        List<ReportPayableTaxVat> taxVats = packageTaxVats(sheet);
        // 第二部分：其他税种
        List<ReportPayableTaxOther> taxOthers = packageTaxOthers(sheet);
//        所得税（分季度填写）
        ReportPayableTaxIncome income = packageTaxIncome(sheet);
        // 增值税留抵明细表
        Calendar statisticDate = Calendar.getInstance();
        statisticDate.setTime(tax.getStatisticDatetime());
        ReportPayableTaxStayed stayed = packageTaxStayed(sheet, statisticDate.get(Calendar.MONTH) + 1);
        // 印花税
        List<ReportPayableTaxStamp> taxStamps = packageTaxStamps(sheet);
        // 房产税, row(73 - 80 {73-1, 80-1 except(78-1)})
        List<ReportPayableTaxEstate> taxEstates = packageTaxEstates(sheet);
        // 土地使用税(85 - 91{85-1, 91-1})
        List<ReportPayableTaxLand> taxLands = packageTaxLands(sheet);
        // 补充资料(95 - 100{95-1, 100-1 except(97-1)})
        List<ReportPayableTaxAddedMaterial> taxAddedMaterials = packageTaxAddedMaterials(sheet);
        List<String> errMsgs = excelService.getErrMsgs();
        Object[] objectArray={tax, taxVats, taxOthers, income, stayed, taxStamps, taxEstates, taxLands, taxAddedMaterials, errMsgs};
        excelService.setErrMsgs(new ArrayList<String>());
        return objectArray;
    }
    
    /**
     * <p>Description: 封装应缴主表信息</p>
     * @param sheet
     * @param uploadDate 
     * @param companyId 
     * @return
     * @throws Exception 
     */
    private ReportPayableTax packageTax(Sheet sheet, String oid, Date uploadDate) throws Exception {
        ReportPayableTax tax = new ReportPayableTax();
        Row row = sheet.getRow(1);
        tax.setCompanyName(excelService.readString(row, 2));
        Long companyId = getCompanyId(oid);
        if(companyId == null){
        	throw new Exception("不存在此公司，请谨慎审核");
        }
        tax.setCompanymstrId(companyId);
        tax.setBukrs(getCompanyBukrs(oid));
        tax.setStatisticDatetime(uploadDate);
        tax.setStatus("");
        packageFixedProperty(tax);
        return tax;
    }
    /**
     * <p>Description: 封装增值税    Excel.rows(10 - 17 {10-1, 17-1})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private List<ReportPayableTaxVat> packageTaxVats(Sheet sheet) throws Exception {
        List<ReportPayableTaxVat> taxVats = new ArrayList<ReportPayableTaxVat>();
        Row row = null;
        ReportPayableTaxVat vat;
        for(int i = 8; i <= 18; i ++) {
            row = sheet.getRow(i);
            vat = new ReportPayableTaxVat();
            vat.setItemName(excelService.readString(row, 1));
            vat.setItemCode("");
            vat.setBeginYearOverage(formatKeepTwoScale(excelService.readDouble(row, 2)));
            vat.setEndYearOverage(formatKeepTwoScale(excelService.readDouble(row, 3)));
            vat.setCurDeclareNum(excelService.readDouble(row, 4));
            vat.setYearAccumShouldPay(formatKeepTwoScale(excelService.readDouble(row, 5)));
            vat.setYearAccumHavePaied(formatKeepTwoScale(excelService.readDouble(row, 6)));
            vat.setYearAccumNotPay(formatKeepTwoScale(excelService.readDouble(row, 7)));
            vat.setDifference(formatKeepTwoScale(excelService.readDouble(row, 8)));
            vat.setRemarks(excelService.readString(row, 9));
            packageFixedProperty(vat);
            taxVats.add(vat);
        }
        return taxVats;
    }
    /**
     * <p>Description: 封装其他税种   Excel.rows(24 - 42 { 24-1, 42-1})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private List<ReportPayableTaxOther> packageTaxOthers(Sheet sheet) throws Exception {
        List<ReportPayableTaxOther> taxOthers = new ArrayList<ReportPayableTaxOther>();
        Row row = null;
        ReportPayableTaxOther other;
        for(int i = 23; i <= 41; i ++) {
            row = sheet.getRow(i);
            if("".equals(excelService.readString(row, 1))){
            	continue;
            }
            other = new ReportPayableTaxOther();
            other.setTaxName(excelService.readString(row, 1));
            other.setTaxCode("");   
            other.setBeginYearOverage(formatKeepTwoScale(excelService.readDouble(row, 2)));
            other.setEndYearOverage(formatKeepTwoScale(excelService.readDouble(row, 3)));
            other.setCurrMonthDeclare(formatKeepTwoScale(excelService.readDouble(row, 4)));
            other.setBeginYearNotPay(formatKeepTwoScale(excelService.readDouble(row, 5)));
            other.setYearAccumShouldPay(formatKeepTwoScale(excelService.readDouble(row, 6)));
            other.setYearAccumPaied(formatKeepTwoScale(excelService.readDouble(row, 7)));
            other.setYearAccumNotPay(formatAndNoScale(excelService.readDouble(row, 8)));
            other.setDifference(formatKeepTwoScale(excelService.readDouble(row, 9)));
            other.setRemarks(excelService.readString(row, 10));
            packageFixedProperty(other);
            taxOthers.add(other);
        }
        return taxOthers;
    }
    /**
     * <p>Description: 所得税（分季度填写）   Excel.row(47 {47 - 1})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private ReportPayableTaxIncome packageTaxIncome(Sheet sheet) throws Exception {
      ReportPayableTaxIncome income = new ReportPayableTaxIncome();
      Row row = sheet.getRow(46);
      
      income.setFirstSeason(formatAndNoScale(excelService.readDouble(row, 2)));
      income.setSecondSeason(formatAndNoScale(excelService.readDouble(row, 3)));
      income.setThirdSeason(formatAndNoScale(excelService.readDouble(row, 4)));
      income.setFourthSeason(formatAndNoScale(excelService.readDouble(row, 5)));
      income.setSum(formatAndNoScale(excelService.readDouble(row, 6)));
      income.setFinalSetDeclare(formatKeepTwoScale(excelService.readDouble(row, 7)));
      income.setShouldAddNorRetrun(formatKeepTwoScale(excelService.readDouble(row, 8)));
      income.setEvaluateAmmount(formatKeepTwoScale(excelService.readDouble(row, 9)));
      income.setReturnAmmount(formatKeepTwoScale(excelService.readDouble(row, 10)));
      packageFixedProperty(income);
      return income;
    }
    /**
     * <p>Description: 增值税留抵明细表 Excel.row(52 {52 - 1})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private ReportPayableTaxStayed packageTaxStayed(Sheet sheet, Integer month) throws Exception {
        ReportPayableTaxStayed stayed = new ReportPayableTaxStayed();
        Row row = sheet.getRow(51);
        stayed.setMonthlyAmmount(formatKeepTwoScale(excelService.readDouble(row, month)));
        stayed.setMonth(month);
        packageFixedProperty(stayed);
        return stayed;
    }
    /**
     * <p>Description: 印花税      Excel.row(56 - 68{ 56-1, 68-1})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private List<ReportPayableTaxStamp> packageTaxStamps(Sheet sheet) throws Exception {
        List<ReportPayableTaxStamp> taxStamps = new ArrayList<ReportPayableTaxStamp>();
        Row row = null;
        ReportPayableTaxStamp stamp;
        for(int i = 55; i <= 67; i ++) {
            row = sheet.getRow(i);
            stamp = new ReportPayableTaxStamp();
            stamp.setTaxRating(excelService.readString(row, 1));
            stamp.setTaxRatingCode("");
            stamp.setContractAmount(formatKeepTwoScale(excelService.readDouble(row, 2)));
            stamp.setTaxRate(excelService.readDouble(row, 3));
            stamp.setYearAccumDeclare(formatKeepTwoScale(excelService.readDouble(row, 5)));
            packageFixedProperty(stamp);
            taxStamps.add(stamp);
        }
        return taxStamps;
    }
    /**
     * <p>Description: 房产税, Excel.row(73 - 80 {73-1, 80-1 except(78-1)})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private List<ReportPayableTaxEstate> packageTaxEstates(Sheet sheet) throws Exception {
        List<ReportPayableTaxEstate> taxEstates = new ArrayList<ReportPayableTaxEstate>();
        ReportPayableTaxEstate estate;
        Row row = null;
        for(int i = 72; i <= 79; i ++) {
            if(i == 77){
            	continue;
            }
            row = sheet.getRow(i);
            estate = new ReportPayableTaxEstate();
            estate.setItemName(excelService.readString(row, 1));
            estate.setItemCode("");         //Dict.
            estate.setYearShouldDeclare(formatKeepTwoScale(excelService.readDouble(row, 2)));
            estate.setCurrDeclare(formatKeepTwoScale(excelService.readDouble(row, 3)));
            estate.setYearAccumDeclare(formatKeepTwoScale(excelService.readDouble(row, 4)));
            estate.setRemarks(excelService.readString(row, 5));
            packageFixedProperty(estate);
            taxEstates.add(estate);
        }
        return taxEstates;
    }
    /**
     * <p>Description: 土地使用税    Excel.row(85 - 91{85-1, 91-1})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private List<ReportPayableTaxLand> packageTaxLands(Sheet sheet) throws Exception {
        List<ReportPayableTaxLand> taxLands = new ArrayList<ReportPayableTaxLand>();
        ReportPayableTaxLand land;
        Row row = null;
        String remarks = "";
        for(int i = 84; i <= 90; i ++) {
        	row = sheet.getRow(i);
            if(i == 84) {
                remarks = excelService.readString(row, 5);
            }
            if(i == 86 || i == 88 || i == 90){
            	continue;
            }
            row = sheet.getRow(i);
            land = new ReportPayableTaxLand();
            land.setItemName(excelService.readString(row, 1));
            land.setItemCode("");         //Dict.
            land.setYearShouldDeclare(formatKeepTwoScale(excelService.readDouble(row, 2)));
            land.setCurrDeclare(excelService.readDouble(row, 3));
            land.setYearAccumDeclare(excelService.readDouble(row, 4));
            land.setRemarks(remarks);
            packageFixedProperty(land);
            taxLands.add(land);
        }
        return taxLands;
    }
    /**
     * <p>Description: 补充资料    Excel.row(95 - 101{95-1, 101-1 except(97-1)})</p>
     * @param sheet
     * @return
     * @throws Exception 
     */
    private List<ReportPayableTaxAddedMaterial> packageTaxAddedMaterials(Sheet sheet) throws Exception {
        List<ReportPayableTaxAddedMaterial> taxAddedMaterials = new ArrayList<ReportPayableTaxAddedMaterial>();
        ReportPayableTaxAddedMaterial material;
        Row row = null;
        for(int i = 94; i < 100; i ++) {
            if(i == 96){
            	continue;
            }
            row = sheet.getRow(i);
            material = new ReportPayableTaxAddedMaterial();
            material.setItemName(excelService.readString(row, 1));
            material.setItemCode("");         //Dict.
            material.setLastYearMonthPay(formatKeepTwoScale(excelService.readDouble(row, 2)));
            material.setThisMonthPay(formatKeepTwoScale(excelService.readDouble(row, 3)));
            material.setThisYearAccumPay(formatKeepTwoScale(excelService.readDouble(row, 4)));
            material.setRemarks(excelService.readString(row, 5));
            packageFixedProperty(material);
            taxAddedMaterials.add(material);
        }
        return taxAddedMaterials;
    }
    /**
     * <p>Description: 封装创建时间、创建人、修改时间、修改人、是否有效等信息</p>
     * @param taxObj
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    private void packageFixedProperty(Object taxObj) {
        Class<?> c = taxObj.getClass();
        try {
            c.getDeclaredMethod("setDefunctInd", String.class).invoke(taxObj, defunctInd);
            c.getDeclaredMethod("setCreatedBy", String.class).invoke(taxObj, currentUser);
            c.getDeclaredMethod("setCreatedDatetime", Date.class).invoke(taxObj, currentDate);
            c.getDeclaredMethod("setUpdatedBy", String.class).invoke(taxObj, currentUser);
            c.getDeclaredMethod("setUpdatedDatetime", Date.class).invoke(taxObj, currentDate);
        } catch (IllegalArgumentException e1) {
            logger.error(e1.getMessage(), e1);
        } catch (SecurityException e1) {
            logger.error(e1.getMessage(), e1);
        } catch (IllegalAccessException e1) {
            logger.error(e1.getMessage(), e1);
        } catch (InvocationTargetException e1) {
            logger.error(e1.getMessage(), e1);
        } catch (NoSuchMethodException e1) {
            logger.error(e1.getMessage(), e1);
        }
    }
    
    private BigDecimal formatKeepTwoScale(double d) {
        return new BigDecimal(new DecimalFormat("#0.00").format(d));
    }
    private BigDecimal formatAndNoScale(double d) {
        return new BigDecimal(new DecimalFormat("#0").format(d));
    }

    private String getCompanyBukrs(String oid) {
        String sql = "SELECT o FROM O o WHERE o.id=:id";
        List<O> ors = em.createQuery(sql).setParameter("id", oid).getResultList();
        return ors.isEmpty() ? "" : ors.get(0).getBukrs();
    }
    private Long getCompanyId(String oid) {
        String sql = "SELECT c FROM Companymstr c WHERE c.oid=:oid";
        List<Companymstr> crs = em.createQuery(sql).setParameter("oid", oid).getResultList();
        return crs.isEmpty() ? null : crs.get(0).getId();
    }
    
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
