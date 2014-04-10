package com.wcs.tih.report.service.summary;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
public abstract class AReportSummary {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";
    private Workbook wb;
    private Sheet sheet;
    public static final String TEMPORARY_PATH = "/download/";
    
    private String getSuffix(String filename) {
        return filename.split("[.]")[filename.split("[.]").length - 1];
    }
    
    public Workbook createWorkbook(String filename) throws Exception {
        String suffix = getSuffix(filename);
        if (XLS.equals(suffix)) {
            wb = new HSSFWorkbook();
        } else if (XLSX.equals(suffix)) {
            wb = new XSSFWorkbook();
        }
        return wb;
    }
    
    public Sheet createSheet(String sheetname) throws Exception {
        String safeName = WorkbookUtil.createSafeSheetName(sheetname); 
        sheet = wb.createSheet(safeName);
        return sheet;
    }
    
    public Workbook getWorkbook(String filename) throws Exception {
        String suffix = getSuffix(filename);
        if (XLS.equals(suffix)) {
            wb = new HSSFWorkbook(new FileInputStream(filename));
        } else if (XLSX.equals(suffix)) {
            wb = new XSSFWorkbook(new FileInputStream(filename));
        }
        return wb;
    }
    
    public Sheet getSheet(String sheetname) throws Exception {
        sheet = wb.getSheet(sheetname);
        return sheet;
    }
    
    public String copyTemplate(String oldPath, String newPath, String filename) throws Exception {
        String newFilePath = null;
        String uuid=UUID.randomUUID().toString();
        String[] file = filename.split("[.]");
        FileInputStream inStream = new FileInputStream(oldPath + filename);

        FileOutputStream outStream = new FileOutputStream(newPath + file[0] + uuid + "." + file[1]);

        //通过available方法取得流的最大字符数

        byte[] inOutb = new byte[inStream.available()];
        inStream.read(inOutb);  //读入流,保存在byte数组
        outStream.write(inOutb);  //写出流,保存在文件
        inStream.close();
        outStream.close();
        
        newFilePath = newPath + file[0] + uuid + "." + file[1];
        return newFilePath;
    }
    
    /**
     * <p>Description: 创建格式</p>
     * @return 格式
     */
    public CellStyle createCellStyle(){
        return wb.createCellStyle();
    }
    
    /**
     * <p>Description:单元格加四边框 </p>
     * @param cell 单元格
     * @param cellStyle 单元格格式
     */
    public void setBorder(Cell cell,CellStyle cellStyle){
        if(cellStyle == null){
            cellStyle = createCellStyle();
        }
        short borderThin = CellStyle.BORDER_THIN;
        cellStyle.setBorderTop(borderThin );
        cellStyle.setBorderBottom(borderThin);
        cellStyle.setBorderLeft(borderThin);
        cellStyle.setBorderRight(borderThin);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);//左右居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//上下居中
        cell.setCellStyle(cellStyle);
    }
    
    /**
     * <p>Description:是否显示网格线 </p>
     * @param show 是否显示
     */
    public void setDisplayGridlines(boolean show){
        sheet.setDisplayGridlines(show);
    }
    
    /**
     * <p>Description:创建行 </p>
     * @param rownum 行号
     * @return 行
     */
    public Row createRow(int rownum){
        return sheet.createRow(rownum);
    }
    
    /**
     * <p>Description: 查找行</p>
     * @param rownum 行号
     * @return 行
     */
    public Row getRow(int rownum){
        return sheet.getRow(rownum);
    }
    
    /**
     * <p>Description: 设置列宽</p>
     * @param columnIndex 列号
     * @param width 宽度
     */
    public void setColumnWidth(int columnIndex, int width){
        sheet.setColumnWidth(columnIndex, width);
    }
    
    /**
     * <p>Description: 查找列宽</p>
     * @param columnIndex 列号
     * @return 宽度
     */
    public int getColumnWidth(int columnIndex){
        return sheet.getColumnWidth(columnIndex);
    }
    
    /**
     * <p>Description: 合并单元格</p>
     * @param firstRow 合并的第一行
     * @param lastRow 合并的最后行
     * @param firstCol 合并的第一列
     * @param lastCol 合并的最后列
     */
    public void mergedCell(int firstRow,int lastRow, int firstCol, int lastCol){
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
    
    public String formatKeepTwoScale(double d) {
        return new DecimalFormat("#0.00").format(d);
    }
    
    public String formatDate(Date date) {
        if(date == null){
            return "";
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
    
    public void setDoubleValue(Cell cell,double value){
        cell = setDoubleFormat(cell);
        cell.setCellValue(value);
    }
    
    public Cell setDoubleFormat(Cell cell){
        DataFormat format = wb.createDataFormat();
        cell.getCellStyle().setDataFormat(format.getFormat("0.00"));
        return cell;
    }
    
    /**
     * 
     * <p>Description: 计算行高</p>  
     * @param str
     * @param fontCountInline
     * @return
     */
    public static float getExcelCellAutoHeight(String str, float fontCountInline) {
        float defaultRowHeight = 270.00f;//每一行的高度指定
        float defaultCount = 0.00f;
        for (int i = 0; i < str.length(); i++) {
            float ff = getregex(str.substring(i, i + 1));
            defaultCount = defaultCount + ff;
            if(str.substring(i, i + 1).equals("\n")){
            	defaultCount = defaultCount + fontCountInline;
            }
        }
        return ((int) (defaultCount / fontCountInline) + 1) * defaultRowHeight;//计算
    }
    
    public static float getregex(String charStr) {
		if (charStr.equals(" ")) {
			return 0.5f;
		}
		// 判断是否为字母或字符
		if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
			return 0.5f;
		}
		// 判断是否为全角
		if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		// 全角符号 及中文
		if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
			return 1.00f;
		}
		return 0.5f;
	}
    
    public Float maxNumber(Float f1, Float f2){
    	if(f1 >= f2){
    		return f1;
    	}
		return f2;
    }
    
    public abstract String summary(String oldPath, String filename,List datas) throws Exception;
}
