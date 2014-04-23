package com.wcs.tih.report.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.model.ReportPayableTax;
import com.wcs.tih.model.ReportPayableTaxAddedMaterial;
import com.wcs.tih.model.ReportPayableTaxOther;
import com.wcs.tih.model.ReportPayableTaxVat;
import com.wcs.tih.report.controller.vo.ReportPayableTaxVO;

public class ExportReportPayableTax {

    private static final String GET_CURR_MONTH_DECLARE = "getCurrMonthDeclare";

    private static final String GET_YEAR_ACCUM_HAVE_PAIED = "getYearAccumHavePaied";

    private static final String GET_THIS_YEAR_ACCUM_PAY = "getThisYearAccumPay";

    private static final String GET_YEAR_ACCUM_SHOULD_PAY = "getYearAccumShouldPay";

    private static final String GET_CUR_DECLARE_NUM = "getCurDeclareNum";

    private static final String GET_YEAR_ACCUM_PAIED = "getYearAccumPaied";

    private static final String GET_YEAR_ACCUM_NOT_PAY = "getYearAccumNotPay";

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private final String getItemNameMethod = "getItemName";
    private final String getTaxNameMethod = "getTaxName";
    private CellStyle styleAlignCenter = null;
    private CellStyle styleAlignLeft = null;
    private CellStyle styleAlignRight = null;
    private CellStyle cellTitleStyle = null;
    private CellStyle tailStyleAlignRight = null;
    
    private Font font = null;
    private final short rowHeight = 500;
    
        private Workbook wb = new HSSFWorkbook();
        public Workbook generateExcelReport(ReportPayableTaxVO[] selections, Date date) throws Exception {
        Sheet sheet = wb.createSheet("汇总");
        // 创建第一行
        Row row1 = sheet.createRow(0);
        Cell cell = row1.createCell(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        cell.setCellValue(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月税费汇总表");
        cell.getCellStyle().setFont(getFont(wb));
        row1.setHeight(rowHeight);
        // 表头
        generateExcelReportHead(wb, sheet);
        
        // 表体
        int rowIndex = 3;   //开始行
        int sequence = 1;   //开始序号
        ReportPayableTax tax;
        Double totals[] = new Double[42];   //存储合计信息
        for (ReportPayableTaxVO payableTaxVO : selections) {
            tax = payableTaxVO.getPayableTax();
            Row row = createRowWithStyle(wb, sheet, rowIndex ++);
            
            createCellWithAlignCenter(wb, row, 0, sequence ++);
            createCellWithAlignLeft(wb, row, 1, tax.getCompanyName());
            
            // 增值税
            List<ReportPayableTaxVat> vats = tax.getReportPayableTaxVats();
            
            Double n2 = getDoubleByMethod(getObjectByItemName(vats, "期初留抵（应纳）余额"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 2, n2, totals, 0);
            Double n3 = getDoubleByMethod(getObjectByItemName(vats, "增值税－已交税金"), GET_CUR_DECLARE_NUM)
                    + getDoubleByMethod(getObjectByItemName(vats, "按简易征收办法计算的应纳增值税额"), GET_CUR_DECLARE_NUM);
            createNumberCell(wb, row, 3, n3, totals, 1);
            Double n4 = getDoubleByMethod(getObjectByItemName(vats, "增值税－出口退税"), GET_YEAR_ACCUM_SHOULD_PAY);
            createNumberCell(wb, row, 4, n4, totals, 2);
            Double n5 = getDoubleByMethod(getObjectByItemName(vats, "期末余额"), GET_YEAR_ACCUM_HAVE_PAIED)
                    + getDoubleByMethod(getObjectByItemName(vats, "按简易征收办法计算的应纳增值税额"), GET_YEAR_ACCUM_HAVE_PAIED);
            createNumberCell(wb, row, 5, n5, totals, 3);
            
            Double n6 = getDoubleByMethod(getObjectByItemName(vats, "期末余额"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 6, n6, totals, 4);
            
            // 企业所得税
            List<ReportPayableTaxOther> others = tax.getReportPayableTaxOthers();
            
            Double n7 = getDoubleByMethod(getObjectByTaxName(others, "企业所得税"), GET_CURR_MONTH_DECLARE);
            createNumberCell(wb, row, 7, n7, totals, 5);
            Double n8 = getDoubleByMethod(getObjectByTaxName(others, "企业所得税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 8, n8, totals, 6);
            
            // 本年实交数
            Double n9 = getDoubleByMethod(getObjectByTaxName(others, "营业税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 9, n9, totals, 7);
            Double n10 = getDoubleByMethod(getObjectByTaxName(others, "代扣代缴营业税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 10, n10, totals, 8);
            Double n11 = getDoubleByMethod(getObjectByTaxName(others, "城市建设维护税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 11, n11, totals, 9);
            Double n12 = getDoubleByMethod(getObjectByTaxName(others, "教育费附加"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 12, n12, totals, 10);
            Double n13 = getDoubleByMethod(getObjectByTaxName(others, "个人所得税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 13, n13, totals, 11);
            Double n14 = getDoubleByMethod(getObjectByTaxName(others, "印花税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 14, n14, totals, 12);
            Double n15 = getDoubleByMethod(getObjectByTaxName(others, "预提所得税\n(代扣代缴非居民企业所得税)"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 15, n15, totals, 13);
            Double n16 = getDoubleByMethod(getObjectByTaxName(others, "房产税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 16, n16, totals, 14);
            Double n17 = getDoubleByMethod(getObjectByTaxName(others, "契税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 17, n17, totals, 15);
            Double n18 = getDoubleByMethod(getObjectByTaxName(others, "城镇土地使用税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 18, n18, totals, 16);
            Double n19 = getDoubleByMethod(getObjectByTaxName(others, "车船使用税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 19, n19, totals, 17);
            Double n20 = getDoubleByMethod(getObjectByTaxName(others, "代扣代缴增值税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 20, n20, totals, 18);
            Double n21 = getDoubleByMethod(getObjectByTaxName(others, "土地增值税"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 21, n21, totals, 19);
            Double n22 = n9 + n10 + n11 + n12 + n13 + n14 + n15 + n16 + n17 + n18 + n19 + n20 + n21;
            createNumberCell(wb, row, 22, n22, totals, 20);
            
            // 第三行.期末末交数
            Double n23 = getDoubleByMethod(getObjectByTaxName(others, "营业税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 23, n23, totals, 21);
            Double n24 = getDoubleByMethod(getObjectByTaxName(others, "城市建设维护税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 24, n24, totals, 22);
            Double n25 = getDoubleByMethod(getObjectByTaxName(others, "教育费附加"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 25, n25, totals, 23);
            Double n26 = getDoubleByMethod(getObjectByTaxName(others, "个人所得税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 26, n26, totals, 24);
            Double n27 = getDoubleByMethod(getObjectByTaxName(others, "印花税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 27, n27, totals, 25);
            Double n28 = getDoubleByMethod(getObjectByTaxName(others, "预提所得税\n(代扣代缴非居民企业所得税)"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 28, n28, totals, 26);
            Double n29 = getDoubleByMethod(getObjectByTaxName(others, "房产税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 29, n29, totals, 27);
            Double n30 = getDoubleByMethod(getObjectByTaxName(others, "契税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 30, n30, totals, 28);
            Double n31 = getDoubleByMethod(getObjectByTaxName(others, "城镇土地使用税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 31, n31, totals, 29);
            Double n32 = getDoubleByMethod(getObjectByTaxName(others, "车船使用税"), GET_YEAR_ACCUM_NOT_PAY);
            createNumberCell(wb, row, 32, n32, totals, 30);
            
            Double n33 = n23 + n24 + n25 + n26 + n27 + n28 + n29 + n30 + n31 + n32;
            createNumberCell(wb, row, 33, n33, totals, 31);
            
            // 本年累计实交数
            List<ReportPayableTaxAddedMaterial> taxAddedMaterials = tax.getReportPayableTaxAddedMaterials();
            
            Double n34 = getDoubleByMethod(getObjectByItemName(taxAddedMaterials, "海关代征增值税"), GET_THIS_YEAR_ACCUM_PAY);
            createNumberCell(wb, row, 34, n34, totals, 32);
            Double n35 = getDoubleByMethod(getObjectByItemName(taxAddedMaterials, "关税"), GET_THIS_YEAR_ACCUM_PAY);
            createNumberCell(wb, row, 35, n35, totals, 33);
            Double n36 = getDoubleByMethod(getObjectByTaxName(others, "地方教育附加"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 36, n36, totals, 34);
            Double n37 = getDoubleByMethod(getObjectByItemName(taxAddedMaterials, "税务机关代征工会费"), GET_THIS_YEAR_ACCUM_PAY);
            createNumberCell(wb, row, 37, n37, totals, 35);
            Double n38 = getDoubleByMethod(getObjectByTaxName(others, "堤围防护费"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 38, n38, totals, 36);
            Double n39 = getDoubleByMethod(getObjectByTaxName(others, "防洪基金"), GET_YEAR_ACCUM_PAIED);
            createNumberCell(wb, row, 39, n39, totals, 37);
            Double n40 = getDoubleByMethod(getObjectByItemName(taxAddedMaterials, "税收滞纳金"), GET_THIS_YEAR_ACCUM_PAY);
            createNumberCell(wb, row, 40, n40, totals, 38);
            Double n41 = getDoubleByMethod(getObjectByItemName(taxAddedMaterials, "税收罚款"), GET_THIS_YEAR_ACCUM_PAY);
            createNumberCell(wb, row, 41, n41, totals, 39);
            Double n42 = n34 + n35 + n36 + n37 + n38 + n39 + n40 + n41;
            createNumberCell(wb, row, 42, n42, totals, 40);

            Double n43 = n42 + n22 + n8 + n5;
            createNumberCell(wb, row, 43, n43, totals, 41);
        }
        
        generateExcelReportTail(wb, sheet, rowIndex, totals);
        return wb;
    }
    
    /**
     * <p>Description: 创建表头</p>
     * @param wb
     * @param sheet
     */
    private void generateExcelReportHead(Workbook wb, Sheet sheet) {
        // 合并单元格
        addMergedRegion(sheet, 1, 2, 0, 0);
        addMergedRegion(sheet, 1, 2, 1, 1);
        
        addMergedRegion(sheet, 1, 1, 2, 6);
        addMergedRegion(sheet, 1, 1, 7, 8);
        addMergedRegion(sheet, 1, 1, 9, 22);
        addMergedRegion(sheet, 1, 1, 23, 33);
        addMergedRegion(sheet, 1, 1, 34, 42);
        
        addMergedRegion(sheet, 1, 2, 43, 43);
        // 创建第二、三行
        Row row2 = createRowWithStyle(wb, sheet, 1);
        Row row3 = createRowWithStyle(wb, sheet, 2);
        // 第二行内容
        createTitleCell(wb, row2, 0, "序号");
        createTitleCell(wb, row2, 1, "公司");
        createTitleCell(wb, row2, 2, "增值税");
        createTitleCell(wb, row2, 7, "企业所得税");
        createTitleCell(wb, row2, 9, "本期实交数");
        createTitleCell(wb, row2, 23, "期末未交数");
        createTitleCell(wb, row2, 34, "本年累计实交数");
        createTitleCell(wb, row2, 43, "合计");
        
        // 第三行.增值税
        createTitleCell(wb, row3, 2, "期初余额");
        createTitleCell(wb, row3, 3, "本月实交数");
        createTitleCell(wb, row3, 4, "本年累计出口退税");
        createTitleCell(wb, row3, 5, "本年累计实交数");
        createTitleCell(wb, row3, 6, "期末留抵");
        // 第三行.企业所得税
        createTitleCell(wb, row3, 7, "本月实交数");
        createTitleCell(wb, row3, 8, "本年实交数");
        // 第三行.本年实交数
        createTitleCell(wb, row3, 9, "营业税");
        createTitleCell(wb, row3, 10, "代扣代缴营业税");
        createTitleCell(wb, row3, 11, "城建税");
        createTitleCell(wb, row3, 12, "教育费附加");
        createTitleCell(wb, row3, 13, "个人所得税");
        createTitleCell(wb, row3, 14, "印花税");
        createTitleCell(wb, row3, 15, "预提所得税");
        createTitleCell(wb, row3, 16, "房产税");
        createTitleCell(wb, row3, 17, "契税");
        createTitleCell(wb, row3, 18, "土地使用税");
        createTitleCell(wb, row3, 19, "车船使用税");
        createTitleCell(wb, row3, 20, "代扣代缴增值税");
        createTitleCell(wb, row3, 21, "土地增值税");
        createTitleCell(wb, row3, 22, "小计");
        // 第三行.期末末交数
        createTitleCell(wb, row3, 23, "营业税");
        createTitleCell(wb, row3, 24, "城建税");
        createTitleCell(wb, row3, 25, "教育费附加");
        createTitleCell(wb, row3, 26, "个人所得税");
        createTitleCell(wb, row3, 27, "印花税");
        createTitleCell(wb, row3, 28, "预提所得税");
        createTitleCell(wb, row3, 29, "房产税");
        createTitleCell(wb, row3, 30, "契税");
        createTitleCell(wb, row3, 31, "土地使用税");
        createTitleCell(wb, row3, 32, "车船使用税");
        createTitleCell(wb, row3, 33, "小计");
        // 第三行.本年累计实交数
        createTitleCell(wb, row3, 34, "海关代征增值税");
        createTitleCell(wb, row3, 35, "关税");
        createTitleCell(wb, row3, 36, "地方教育附加");
        createTitleCell(wb, row3, 37, "税务机关代征工会费");
        createTitleCell(wb, row3, 38, "河道费");
        createTitleCell(wb, row3, 39, "防洪基金");
        createTitleCell(wb, row3, 40, "税收滞纳金");
        createTitleCell(wb, row3, 41, "税收罚款");
        createTitleCell(wb, row3, 42, "小计");
    }
    
    /**
     * <p>Description: 创建表尾</p>
     * @param wb
     * @param sheet
     * @param rowIndex
     * @param totals
     */
    private void generateExcelReportTail(Workbook wb, Sheet sheet, int rowIndex, Double[] totals) {
        addMergedRegion(sheet, rowIndex, rowIndex, 0, 1);
        Row row = createRowWithStyle(wb, sheet, rowIndex);
        Cell cell = createCell(row, 0, "合计");
        cell.setCellStyle(getStyleAlignCenter(wb));
        int cellIndex = 2;
        for(Double tol : totals) {
            createTailNumberCell(wb, row, cellIndex ++, tol);
        }
    }
    
    /**
     * <p>Description: 合并单元格</p>
     * @param sheet
     * @param firstRow
     * @param lastRow
     * @param firstCol
     * @param lastCol
     */
    private void addMergedRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * <p>Description: 创建带样式的行</p>
     * @param wb
     * @param sheet
     * @param rowIndex
     * @return
     */
    private Row createRowWithStyle(Workbook wb, Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        row.setHeight(rowHeight);
        row.setRowStyle(getStyleAlignCenter(wb));
        return row;
    }
    /**
     * <p>Description: 创建标题样式的单元格</p>
     * @param wb
     * @param row
     * @param cellIndex
     * @param cellValue
     */
    private void createTitleCell(Workbook wb, Row row, int cellIndex, String cellValue) {
        Cell cell = createCell(row, cellIndex, cellValue);
        cell.setCellStyle(getCellTitleStyle(wb));
    }
    
    /**
     * <p>Description: 创建数值型显示单元格，并封装了 合计</p>
     * @param wb
     * @param row
     * @param cellIndex
     * @param num
     * @param totals
     * @param index
     */
    private void createNumberCell(Workbook wb, Row row, int cellIndex, double num, Double[] totals, int index) {
        if(totals[index] == null){
        	totals[index] = 0d;
        }
        totals[index] += num;
        createCellWithAlignRight(wb, row, cellIndex, num);
    }
    /**
     * <p>Description: 创建表尾单元格样式</p>
     * @param wb
     * @param row
     * @param cellIndex
     * @param cellValue
     */
    private void createTailNumberCell(Workbook wb, Row row, int cellIndex, Double cellValue) {
        Cell cell = createCell(row, cellIndex, cellValue);
        CellStyle style = getTailStyleAlignRight(wb);
        cell.setCellStyle(style);
        style.setFont(getFont(wb));
    }
    /**
     * <p>Description: 创建居中显示的单元格</p>
     * @param wb
     * @param row
     * @param cellIndex
     * @param cellValue
     */
    private void createCellWithAlignCenter(Workbook wb, Row row, int cellIndex, Integer cellValue) {
        Cell cell = createCell(row, cellIndex, cellValue);
        cell.setCellStyle(getStyleAlignCenter(wb));
    }
    /**
     * <p>Description: 创建居左显示的单元格</p>
     * @param wb
     * @param row
     * @param cellIndex
     * @param cellValue
     */
    private void createCellWithAlignLeft(Workbook wb, Row row, int cellIndex, String cellValue) {
        Cell cell = createCell(row, cellIndex, cellValue);
        cell.setCellStyle(getStyleAlignLeft(wb));
        
    }
    /**
     * <p>Description: 创建居右显示的单元格</p>
     * @param wb
     * @param row
     * @param cellIndex
     * @param cellValue
     */
    private void createCellWithAlignRight(Workbook wb, Row row, int cellIndex, Double cellValue) {
        Cell cell = createCell(row, cellIndex, cellValue);
        cell.setCellStyle(getStyleAlignRight(wb));
    }
    /**
     * <p>Description: 创建单元格</p>
     * @param row
     * @param cellIndex
     * @param cellValue
     * @return
     */
    private Cell createCell(Row row, int cellIndex, Object cellValue) {
        Cell cell = row.createCell(cellIndex);
        if(cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if(cellValue instanceof Double) {
            String format = new DecimalFormat("#0.00").format((Double) cellValue);
            cell.setCellValue(format);
        } else if(cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
        }
        return cell;
    }
    
    /**
     * <p>Description: 根据 itemName 属性，查找对应的实体记录</p>
     * @param objs
     * @param name
     * @return
     * @throws Exception
     */
    private Object getObjectByItemName(List<?> objs, String name) throws Exception {
        return getObjectByName(objs, getItemNameMethod, name);
    }
    /**
     * <p>Description: 根据 taxName 属性，查找对应的实体记录</p>
     * @param objs
     * @param name
     * @return
     * @throws Exception
     */
    private Object getObjectByTaxName(List<?> objs, String name) throws Exception {
        return getObjectByName(objs, getTaxNameMethod, name);
    }
    /**
     * <p>Description: 根据方法名，和相应传入的值判断，并返回对象</p>
     * @param objs
     * @param method
     * @param name
     * @return
     * @throws Exception
     */
    private Object getObjectByName(List<?> objs, String method, String name) throws Exception {
        for(Object obj : objs) {
            Class<?> c = obj.getClass();
            try {
                Object result = c.getMethod(method).invoke(obj);
                if(name.equals(result)) {
                    return obj;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
                throw new Exception("方法调用失败");
            }
        }
        return null;
    }
    /**
     * <p>Description: 根据对象的方法返回Double型数据</p>
     * @param obj
     * @param method
     * @return
     * @throws Exception
     */
    private double getDoubleByMethod(Object obj, String method) throws Exception {
        if(obj == null){
            return 0;
        }
        Class<?> c = obj.getClass();
        Object rs;
        try {
            rs = c.getMethod(method).invoke(obj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("方法取值失败");
        }
        return rs instanceof BigDecimal ? ((BigDecimal) rs).doubleValue() : (Double) rs;
    }
    
    /**
     * <p>Description: 创建标题样式</p>
     * @param wb
     * @return
     */
    private CellStyle getCellTitleStyle(Workbook wb) {
        if(cellTitleStyle != null){
        	return cellTitleStyle;
        }
        cellTitleStyle = wb.createCellStyle();
        cellTitleStyle.setFillForegroundColor(HSSFColor.LIME.index);
        cellTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        packageStyleBorder(cellTitleStyle);
        packageStyleCenter(cellTitleStyle);
        cellTitleStyle.setFont(getFont(wb));
        return cellTitleStyle;
    }
    /**
     * <p>Description: 创建四面实线，并且垂直居中,左右居中的样式</p>
     * @param wb
     * @return
     */
    private CellStyle getStyleAlignCenter(Workbook wb) {
        if(styleAlignCenter != null){
        	return styleAlignCenter;
        }
        styleAlignCenter = wb.createCellStyle();
        packageStyleBorder(styleAlignCenter);
        packageStyleCenter(styleAlignCenter);
        return styleAlignCenter;
    }
    /**
     * <p>Description: 创建四面实线，并且垂直居中，左右居左</p>
     * @param wb
     * @return
     */
    private CellStyle getStyleAlignLeft(Workbook wb) {
        if(styleAlignLeft != null){
        	return styleAlignLeft;
        }
        styleAlignLeft = wb.createCellStyle();
        packageStyleBorder(styleAlignLeft);
        packageStyleLeft(styleAlignLeft);
        return styleAlignLeft;
    }
    /**
     * <p>Description: 创建四面实线，并且垂直居中，左右居右</p>
     * @param wb
     * @return
     */
    private CellStyle getStyleAlignRight(Workbook wb) {
        if(styleAlignRight != null){
        	return styleAlignRight;
        }
        styleAlignRight = wb.createCellStyle();
        packageStyleBorder(styleAlignRight);
        packageStyleRight(styleAlignRight);
        return styleAlignRight;
    }
    /**
     * <p>Description: 报表底部数值样式</p>
     * @param wb
     * @return
     */
    private CellStyle getTailStyleAlignRight(Workbook wb) {
        if(tailStyleAlignRight != null){
        	return tailStyleAlignRight;
        }
        tailStyleAlignRight = wb.createCellStyle();
        packageStyleBorder(tailStyleAlignRight);
        packageStyleRight(tailStyleAlignRight);
        tailStyleAlignRight.setFillForegroundColor(HSSFColor.WHITE.index);
        tailStyleAlignRight.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return tailStyleAlignRight;
    }
    /**
     * <p>Description: 封装属性值</p>
     * @param style
     */
    private void packageStyleBorder(CellStyle style) {
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    }
    private void packageStyleLeft(CellStyle style) {
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
    }
    private void packageStyleCenter(CellStyle style) {
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    }
    private void packageStyleRight(CellStyle style) {
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
    }
    /**
     * <p>Description: 字体</p>
     * @param wb
     * @return
     */
    private Font getFont(Workbook wb) {
        if(font != null){
        	return font;
        }
        font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight((short) 10);
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        return font;
    }
}
