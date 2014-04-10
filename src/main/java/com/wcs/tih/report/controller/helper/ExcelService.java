package com.wcs.tih.report.controller.helper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;


@Stateless
public class ExcelService {
    private Logger logger = LoggerFactory.getLogger(ExcelService.class);
	private static final String XLS = "xls";
	private static final String XLSX = "xlsx";
	
	private List<String> errMsgs = new ArrayList<String>();
	
	public Workbook createWorkbook(UploadedFile upFile) throws Exception {
	    try {
	        Workbook workbook = null;
	        String suffix = getSuffix(upFile.getFileName());
	        InputStream is = upFile.getInputstream();
	        if (XLS.equals(suffix)) {
	            workbook = new HSSFWorkbook(is);
	        } else if (XLSX.equals(suffix)) {
	            workbook = new XSSFWorkbook(is);
	        }
	        return workbook;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("文件格式错误！请确保你上传的文件格式是“Excel 97-2003 工作簿(*.xls)”, 或 “Excel工作簿(*.xlsx)”");
        }
	}
	
    //读取字符串
    public String readString(Row row, Integer cellIndex) throws Exception {
        String errMsg = "在第" + row.getRowNum()+1 + "行的第" + cellIndex+1 + "列，应为字符型值，请核对！。";
    	try {
    		return row.getCell(cellIndex).getStringCellValue();
    	}catch (Exception e) {
    		if(row.getRowNum()==0&&cellIndex==0){
                throw new Exception(errMsg);
    		}
    		errMsgs.add(errMsg);
    		return null;
        }
    }
    
    //读取金额大数字
    public BigDecimal readBigDecimal(Row row, Integer cellIndex) {
    	try {
    		return new BigDecimal(row.getCell(cellIndex).getNumericCellValue());
    	}catch (Exception e) {
    		errMsgs.add("在第" + (row.getRowNum()+1) + "行的第" + (cellIndex+1) + "列，应为数值型值，请核对！。");
    		return null;
        }
    }
    
    //读取double数字
    public double readDouble(Row row, Integer cellIndex) {
    	try {
    		return row.getCell(cellIndex).getNumericCellValue();
    	}catch (Exception e) {
    		errMsgs.add("在第" + (row.getRowNum()+1) + "行的第" + (cellIndex+1) + "列，应为数值型值，请核对！。");
    		return 0;
        }
    }
    
	private String getSuffix(String filename) {
		return filename.split("[.]")[filename.split("[.]").length - 1];
	}
	


	public List<String> getErrMsgs() {
		return errMsgs;
	}

	public void setErrMsgs(List<String> errMsgs) {
		this.errMsgs = errMsgs;
	}
    
}
