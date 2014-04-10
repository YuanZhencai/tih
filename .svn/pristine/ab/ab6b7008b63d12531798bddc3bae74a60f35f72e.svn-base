package com.wcs.tih.report.controller.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.wcs.tih.model.CompanyMaterial;
import com.wcs.tih.report.controller.vo.CompanyBasicInfoVo;
import com.wcs.tih.report.controller.vo.CompanyTaxIncentiveVo;
import com.wcs.tih.report.controller.vo.CompanyTaxRatioVo;
import com.wcs.tih.report.controller.vo.StockStructureVo;
import com.wcs.tih.report.controller.vo.TaxRatioVo;
import com.wcs.tih.report.service.summary.AReportSummary;
import com.wcs.tih.system.controller.vo.TaxIncentiveVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 生成公司涉税信息汇总Excel</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public final class CompanyAbouttaxExpExcel {
    private static String sheet1Name = "汇总表1";
    private static String sheet2Name = "汇总表2";
    private static String sheet3Name = "汇总表3";
    
    @EJB
    private static AReportSummary aReportSummary;
    
    private CompanyAbouttaxExpExcel() {
    }

    /**
     * <p>Description: 生成Excel</p>
     * @param cbvs 公司基本信息
     * @param ctrvs 税种税率信息
     * @param cttvs 税收优惠信息
     * @return
     * @throws Exception
     */
    public static InputStream taxSummary(List<CompanyBasicInfoVo> cbvs, List<CompanyTaxRatioVo> ctrvs, List<CompanyTaxIncentiveVo> cttvs) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle titleStyle = setBorder(wb.createCellStyle());
        titleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        HSSFCellStyle greenTitleStyle = setBorder(wb.createCellStyle());
        greenTitleStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        greenTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        greenTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        greenTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 生成一个字体
        HSSFFont titleFont = wb.createFont();
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight((short) 10);
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(titleFont);
        greenTitleStyle.setFont(titleFont);
        // 设置行样式
        HSSFCellStyle rowStyle = setBorder(wb.createCellStyle());
        rowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 奇数行样式
        HSSFCellStyle oddNumberRowStyle = setBorder(wb.createCellStyle());
        oddNumberRowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        oddNumberRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        oddNumberRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        oddNumberRowStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
        // 偶数行样式
        HSSFCellStyle evenRowStyle = setBorder(wb.createCellStyle());
        evenRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        evenRowStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
        // 数字奇数行样式
        HSSFCellStyle numberOddNumberRowStyle = setBorder(wb.createCellStyle());
        numberOddNumberRowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        numberOddNumberRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        numberOddNumberRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        numberOddNumberRowStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平
        // 数字偶数行样式
        HSSFCellStyle numberEvenRowStyle = setBorder(wb.createCellStyle());
        numberEvenRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        numberEvenRowStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平
        // 序号奇数行样式
        HSSFCellStyle orderOddNumberRowStyle = setBorder(wb.createCellStyle());
        orderOddNumberRowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        orderOddNumberRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        orderOddNumberRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        orderOddNumberRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 序号偶数行样式
        HSSFCellStyle orderEvenRowStyle = setBorder(wb.createCellStyle());
        orderEvenRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        orderEvenRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        // 创建多个sheet
        HSSFSheet sheet1 = wb.createSheet(sheet1Name);
        HSSFSheet sheet2 = wb.createSheet(sheet2Name);
        HSSFSheet sheet3 = wb.createSheet(sheet3Name);
        // 合并单元格(startRow，endRow，startColumn，endColumn)
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 37));
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
        sheet1.setColumnWidth((short) 0, 3000);
        sheet1.setColumnWidth((short) 1, 10000);
        sheet1.setColumnWidth((short) 2, 8000);
        sheet1.setColumnWidth((short) 3, 5000);
        sheet1.setColumnWidth((short) 4, 5000);
        for (int i = 5; i < 38; i++) {
            sheet1.setColumnWidth((short) i, 4000);
        }
        //创建表头
        HSSFRow sheet1Row1 = sheet1.createRow((short) 1);// 第一行
        HSSFRow sheet1Row2 = sheet1.createRow((short) 2);// 第二行
        sheet1Row1.setHeight((short) 500);
        sheet1Row2.setHeight((short) 500);
        HSSFCell cell;
        for (int i = 0; i < 38; i++) {
            cell = sheet1Row1.createCell(i);
            cell.setCellStyle(titleStyle);
            cell = sheet1Row2.createCell(i);
            cell.setCellStyle(titleStyle);
        }
        String[] headersCompany = { "序号", "公司名称", "公司地址", "成立时间", "开始经营时间", "经营范围", "投资总额(万元)" };
        int num;
        for (int i = 0; i < headersCompany.length; i++) {
            if (i == 5) {
                num = 9;
            } else if (i == 6) {
                num = 10;
            } else {
                num = i;
            }
            cell = sheet1Row1.createCell(num);
            cell.setCellValue(headersCompany[i]);
            if (i == 0 || i == 1) {
                cell.setCellStyle(greenTitleStyle);
            } else {
                cell.setCellStyle(titleStyle);
            }
        }
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 9, 9));
        sheet1.addMergedRegion(new CellRangeAddress(1, 2, 10, 10));
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 5, 8));
        HSSFCell cellHead5 = sheet1Row1.createCell(5);
        cellHead5.setCellValue("税务机关");
        cellHead5.setCellStyle(titleStyle);
        String[] headersTax = { "国税", "纳税人识别号", "地税", "纳税人识别号" };
        for (int i = 0; i < headersTax.length; i++) {
            cell = sheet1Row2.createCell(i + 5);
            cell.setCellValue(headersTax[i]);
            cell.setCellStyle(titleStyle);
        }
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 11, 13));
        HSSFCell cell12 = sheet1Row1.createCell(11);
        cell12.setCellValue("股权结构");
        cell12.setCellStyle(titleStyle);
        String[] headersStock = { "股东名称", "注册资本（万元）", "股权比例" };
        for (int i = 0; i < headersStock.length; i++) {
            cell = sheet1Row2.createCell(i + 11);
            cell.setCellValue(headersStock[i]);
            cell.setCellStyle(titleStyle);
        }
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 14, 19));
        HSSFCell cellHead16 = sheet1Row1.createCell(14);
        cellHead16.setCellValue("主要资产原值(万元)");
        cellHead16.setCellStyle(titleStyle);
        String[] headersMainAssets = { "土地", "房屋建筑物", "机械设备", "运输设备", "办公设备", "其他设备" };
        for (int i = 0; i < headersMainAssets.length; i++) {
            cell = sheet1Row2.createCell(i + 14);
            cell.setCellValue(headersMainAssets[i]);
            cell.setCellStyle(titleStyle);
        }
        sheet1.addMergedRegion(new CellRangeAddress(1, 1, 20, 37));
        HSSFCell cell23 = sheet1Row1.createCell(20);
        cell23.setCellValue("加工能力(吨/天)");
        cell23.setCellStyle(titleStyle);
        
        String[] headersProcess = { "油脂压榨", "油脂精炼", "棕榈油分提", "面粉生产", "大米生产", "油化生产", "特油生产", "小包装油生产","其它" };
        for (int i = 0; i < headersProcess.length; i++) {
            cell = sheet1Row2.createCell(i*2 + 20);
            cell.setCellValue(headersProcess[i]);
            cell.setCellStyle(titleStyle);
            sheet1.addMergedRegion(new CellRangeAddress(2, 2, i*2 + 20, i*2 + 21));
        }
        
        int rowIndex = 3;
        int frist = 3;
        
        for (int i = 0; i < cbvs.size(); i++) {
            frist = rowIndex;
            HSSFCellStyle numberCellStyle = wb.createCellStyle();
            HSSFCellStyle stringCellStyle = wb.createCellStyle();
            numberCellStyle = i % 2 == 0 ? numberEvenRowStyle:numberOddNumberRowStyle;
            stringCellStyle = i % 2 == 0 ? evenRowStyle:oddNumberRowStyle;
            CompanyBasicInfoVo cbv = cbvs.get(i);
            List<StockStructureVo> ssvs = cbv.getSsvs();
            
            HSSFRow fristRow = sheet1.createRow(frist);
            fristRow = sheet1.createRow(frist);
            for (int j = 0; j < headersProcess.length; j++) {
                for (int l = 0; l < 38; l++) {
                    fristRow.createCell(l);
                }
            }
            rowIndex++;
            // 序号
            HSSFCell cell1 = fristRow.getCell(0);
            cell1.setCellValue(i + 1);
            // 公司名称
            HSSFCell cell2 = fristRow.getCell(1);
            cell2.setCellValue(cbv.getCompanyName());
            // 公司地址
            HSSFCell cell3 = fristRow.getCell(2);
            cell3.setCellValue(cbv.getCompanyAddress());
            // 成立时间
            HSSFCell cell4 = fristRow.getCell(3);
            cell4.setCellValue(cbv.getSetUpDatetime());
            // 开始经营时间
            HSSFCell cell5 = fristRow.getCell(4);
            cell5.setCellValue(cbv.getStartDatetime());
            // 税务机关
            HSSFCell cell6 = fristRow.getCell(5);
            cell6.setCellValue(cbv.getNationalTax());
            
            HSSFCell cell7 = fristRow.getCell(6);
            cell7.setCellValue(cbv.getNationalTaxpayerIdentifier());
            
            HSSFCell cell8 = fristRow.getCell(7);
            cell8.setCellValue(cbv.getLandTax());
            
            HSSFCell cell9 = fristRow.getCell(8);
            cell9.setCellValue(cbv.getLandTaxTaxpayerIdentifier());
            
            // 经营范围
            HSSFCell cell10 = fristRow.getCell(9);
            cell10.setCellValue(cbv.getManageArea());
           
            // 投资总额
            HSSFCell cell11 = fristRow.getCell(10);
            cell11.setCellValue(cbv.getInvestTotal());
            cell11.setCellStyle(numberCellStyle);
            cell11.getCellStyle().setWrapText(true);
            // 主要资产
            HSSFCell cell15 = fristRow.getCell(14);
            cell15.setCellValue(cbv.getMainAssetsCast1());
            
            HSSFCell cell16 = fristRow.getCell(15);
            cell16.setCellValue(cbv.getMainAssetsCast2());
            
            HSSFCell cell17 = fristRow.getCell(16);
            cell17.setCellValue(cbv.getMainAssetsCast3());
            
            HSSFCell cell18 = fristRow.getCell(17);
            cell18.setCellValue(cbv.getMainAssetsCast4());
            
            HSSFCell cell19 = fristRow.getCell(18);
            cell19.setCellValue(cbv.getMainAssetsCast5());
            
            HSSFCell cell20 = fristRow.getCell(19);
            cell20.setCellValue(cbv.getMainAssetsCast6());
            
            // 加工能力
            Map<Integer, List<CompanyMaterial>> processMap = cbv.getProcessMap();
            for (int j = 0; j < 9; j++) {
                List<CompanyMaterial> list = processMap.get(j);
                if(list != null){
                    for (int k = 0; k < list.size(); k++) {
                        CompanyMaterial cm = list.get(k);
                        HSSFRow row = sheet1.getRow(frist + k);
                        if(row == null){
                            row = sheet1.createRow(frist + k);
                            for (int l = 0; l < 38; l++) {
                                row.createCell(l);
                            }
                            row.setHeight((short) 400);
                            rowIndex++;
                        }
                        
                        HSSFCell materialCell = row.getCell(2 * j + 20);
                        materialCell.setCellValue(cm.getMainMaterial());
                        
                        HSSFCell abilityCell = row.getCell(2 * j + 21);
                        abilityCell.setCellValue(cm.getAbility() == 0 ? "": String.valueOf(cm.getAbility()));
                    }
                }
            }
            
            //股权结构
            for (int j = 0; j < ssvs.size(); j++) {
                HSSFRow row = sheet1.getRow(frist + j);
                if(row == null){
                    row = sheet1.createRow(frist + j);
                    for (int k = 0; k < 38; k++) {
                        row.createCell(k);
                    }
                    rowIndex++;
                }
                StockStructureVo ssv = ssvs.get(j);
                if(ssv.getShareholder() != null && !"".equals(ssv.getShareholder())){
                    float height = aReportSummary.getExcelCellAutoHeight(ssv.getShareholder(), 8f);
                    row.setHeight((short)height);
                }
                HSSFCell cellShareholder = row.getCell(11);
                cellShareholder.setCellValue(ssv.getShareholder());
                
                HSSFCell cellRegisteredCapital = row.getCell(12);
                cellRegisteredCapital.setCellValue(ssv.getRegisteredCapital());
                
                HSSFCell cellRatio = row.getCell(13);
                cellRatio.setCellValue(ssv.getRatio());
            }

            //设置样式
            for (int j = frist; j < rowIndex; j++) {
                HSSFRow row = sheet1.getRow(j);
                row.getCell(0).setCellStyle(stringCellStyle);
                row.getCell(1).setCellStyle(stringCellStyle);
                row.getCell(2).setCellStyle(stringCellStyle);
                row.getCell(3).setCellStyle(stringCellStyle);
                row.getCell(4).setCellStyle(stringCellStyle);
                row.getCell(5).setCellStyle(stringCellStyle);
                row.getCell(6).setCellStyle(stringCellStyle);
                row.getCell(7).setCellStyle(stringCellStyle);
                row.getCell(8).setCellStyle(stringCellStyle);
                cell10 = row.getCell(9);
                cell10.setCellStyle(stringCellStyle);
                cell10.getCellStyle().setWrapText(true);
                cell11 = row.getCell(10);
                cell11.setCellStyle(stringCellStyle);
                cell11.getCellStyle().setWrapText(true);
                row.getCell(11).setCellStyle(stringCellStyle);
                row.getCell(12).setCellStyle(numberCellStyle);
                row.getCell(13).setCellStyle(numberCellStyle);
                row.getCell(14).setCellStyle(numberCellStyle);
                row.getCell(15).setCellStyle(numberCellStyle);
                row.getCell(16).setCellStyle(numberCellStyle);
                row.getCell(17).setCellStyle(numberCellStyle);
                row.getCell(18).setCellStyle(numberCellStyle);
                row.getCell(19).setCellStyle(numberCellStyle);
                for (int k = 0; k < 9; k++) {
                    row.getCell(2 * k + 20).setCellStyle(stringCellStyle);
                    row.getCell(2 * k + 21).setCellStyle(numberCellStyle);
                }
            }
            //合并单元格
            for (int j = 0; j < 20; j++) {
                sheet1.addMergedRegion(new CellRangeAddress(frist, rowIndex - 1, j, j));
                if (j == 10) {
                    j = 13;
                }
            }
            
        }
      
        int startRow = 0;
        int endRow = 0;
        
        sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        sheet2.setColumnWidth((short) 0, 3000);
        sheet2.setColumnWidth((short) 1, 10000);
        sheet2.setColumnWidth((short) 2, 5000);
        sheet2.setColumnWidth((short) 3, 8000);
        sheet2.setColumnWidth((short) 4, 4000);
        sheet2.setColumnWidth((short) 5, 4000);
        HSSFRow sheet2Row = sheet2.createRow((short) 1);
        sheet2Row.setHeight((short) 800);
        String[] headersTaxRate = { "序号", "公司", "税种", "计税基础", "税率(%)", "申报频率" };
        for (int i = 0; i < headersTaxRate.length; i++) {
            cell = sheet2Row.createCell(i);
            cell.setCellValue(headersTaxRate[i]);
            if (i == 0 || i == 1) {
                cell.setCellStyle(greenTitleStyle);
            } else {
                cell.setCellStyle(titleStyle);
            }
        }
        
        // 循环添加税种税率数据
        CompanyTaxRatioVo ctrv;
        List<TaxRatioVo> trvs;
        TaxRatioVo trv;
        HSSFRow sheet2Rowi;
        HSSFCell sheet2Celli;
        for (int i = 0; i < ctrvs.size(); i++) {
            ctrv = ctrvs.get(i);
            trvs = ctrv.getTrvs();
            if (null != trvs && trvs.size() != 0) {
                if (i == 0) {
                    startRow = 2;
                    endRow = trvs.size() + 1;
                } else {
                    startRow = endRow + 1;
                    endRow = startRow + trvs.size() - 1;
                }
            } else {
                if (i == 0) {
                    startRow = 2;
                    endRow = 2;
                } else {
                    startRow = endRow + 1;
                    endRow = startRow;
                }
            }
            // 合并序号和公司
            sheet2.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
            sheet2.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
            sheet2Rowi = sheet2.createRow(startRow);
            sheet2Rowi.setHeight((short) 400);
            // 序号
            sheet2Celli = sheet2Rowi.createCell(0);
            sheet2Celli.setCellValue(i + 1);
            if (i % 2 == 0) {
                sheet2Celli.setCellStyle(orderEvenRowStyle);
            } else {
                sheet2Celli.setCellStyle(orderOddNumberRowStyle);
            }
            // 公司名称
            sheet2Celli = sheet2Rowi.createCell(1);
            sheet2Celli.setCellValue(ctrv.getCompanyName());
            if (i % 2 == 0) {
                sheet2Celli.setCellStyle(evenRowStyle);
            } else {
                sheet2Celli.setCellStyle(oddNumberRowStyle);
            }
            if (null != trvs && trvs.size() != 0) {
                for (int j = 0; j < trvs.size(); j++) {
                    trv = trvs.get(j);
                    if (j != 0) {
                        sheet2Rowi = sheet2.createRow(j + startRow);
                        sheet2Rowi.setHeight((short) 400);
                        sheet2Celli = sheet2Rowi.createCell(0);
                        if (i % 2 == 0) {
                            sheet2Celli.setCellStyle(orderEvenRowStyle);
                        } else {
                            sheet2Celli.setCellStyle(orderOddNumberRowStyle);
                        }
                        sheet2Celli = sheet2Rowi.createCell(1);
                        if (i % 2 == 0) {
                            sheet2Celli.setCellStyle(evenRowStyle);
                        } else {
                            sheet2Celli.setCellStyle(oddNumberRowStyle);
                        }
                    }
                    sheet2Celli = sheet2Rowi.createCell(2);
                    sheet2Celli.setCellValue(trv.getTaxType());
                    if (i % 2 == 0) {
                        sheet2Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet2Celli.setCellStyle(oddNumberRowStyle);
                    }
                    sheet2Celli = sheet2Rowi.createCell(3);
                    sheet2Celli.setCellValue(trv.getTaxBasis());
                    if (i % 2 == 0) {
                        sheet2Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet2Celli.setCellStyle(oddNumberRowStyle);
                    }
                    sheet2Celli = sheet2Rowi.createCell(4);
                    sheet2Celli.setCellValue(trv.getTaxRate());
                    if (i % 2 == 0) {
                        sheet2Celli.setCellStyle(numberEvenRowStyle);
                    } else {
                        sheet2Celli.setCellStyle(numberOddNumberRowStyle);
                    }
                    sheet2Celli = sheet2Rowi.createCell(5);
                    sheet2Celli.setCellValue(trv.getReportFrequency());
                    if (i % 2 == 0) {
                        sheet2Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet2Celli.setCellStyle(oddNumberRowStyle);
                    }
                }
            } else {
                sheet2Celli = sheet2Rowi.createCell(2);
                if (i % 2 == 0) {
                    sheet2Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet2Celli.setCellStyle(oddNumberRowStyle);
                }
                sheet2Celli = sheet2Rowi.createCell(3);
                if (i % 2 == 0) {
                    sheet2Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet2Celli.setCellStyle(oddNumberRowStyle);
                }
                sheet2Celli = sheet2Rowi.createCell(4);
                if (i % 2 == 0) {
                    sheet2Celli.setCellStyle(numberEvenRowStyle);
                } else {
                    sheet2Celli.setCellStyle(numberOddNumberRowStyle);
                }
                sheet2Celli = sheet2Rowi.createCell(5);
                if (i % 2 == 0) {
                    sheet2Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet2Celli.setCellStyle(oddNumberRowStyle);
                }
            }
        }

        sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        sheet3.setColumnWidth((short) 0, 3000);
        sheet3.setColumnWidth((short) 1, 10000);
        sheet3.setColumnWidth((short) 2, 10000);
        sheet3.setColumnWidth((short) 3, 8000);
        sheet3.setColumnWidth((short) 4, 8000);
        sheet3.setColumnWidth((short) 5, 8000);
        HSSFRow sheet3Row = sheet3.createRow((short) 1);
        sheet3Row.setHeight((short) 800);
        String[] headersTaxIncentive = { "序号", "公司", "税收优惠项目", "优惠期间", "审批机关", "政策依据" };
        for (int i = 0; i < headersTaxIncentive.length; i++) {
            cell = sheet3Row.createCell(i);
            cell.setCellValue(headersTaxIncentive[i]);
            if (i == 0 || i == 1) {
                cell.setCellStyle(greenTitleStyle);
            } else {
                cell.setCellStyle(titleStyle);
            }
        }
        // 循环添加税收数据
        CompanyTaxIncentiveVo cttv;
        TaxIncentiveVo ttv;
        List<TaxIncentiveVo> ttvs;
        HSSFRow sheet3Rowi;
        HSSFCell sheet3Celli;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < cttvs.size(); i++) {
            cttv = cttvs.get(i);
            ttvs = cttv.getTtvs();
            if (null != ttvs && ttvs.size() != 0) {
                if (i == 0) {
                    startRow = 2;
                    endRow = ttvs.size() + 1;
                } else {
                    startRow = endRow + 1;
                    endRow = startRow + ttvs.size() - 1;
                }
            } else {
                if (i == 0) {
                    startRow = 2;
                    endRow = 2;
                } else {
                    startRow = endRow + 1;
                    endRow = startRow;
                }
            }
            // 合并序号和公司
            sheet3.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
            sheet3.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
            sheet3Rowi = sheet3.createRow(startRow);
            sheet3Rowi.setHeight((short) 400);
            // 序号
            sheet3Celli = sheet3Rowi.createCell(0);
            sheet3Celli.setCellValue(i + 1);
            if (i % 2 == 0) {
                sheet3Celli.setCellStyle(orderEvenRowStyle);
            } else {
                sheet3Celli.setCellStyle(orderOddNumberRowStyle);
            }
            // 公司名称
            sheet3Celli = sheet3Rowi.createCell(1);
            sheet3Celli.setCellValue(cttv.getCompanyName());
            if (i % 2 == 0) {
                sheet3Celli.setCellStyle(evenRowStyle);
            } else {
                sheet3Celli.setCellStyle(oddNumberRowStyle);
            }
            if (null != ttvs && ttvs.size() != 0) {
                for (int j = 0; j < ttvs.size(); j++) {
                    ttv = ttvs.get(j);
                    if (j != 0) {
                        sheet3Rowi = sheet3.createRow(j + startRow);
                        sheet3Rowi.setHeight((short) 400);
                        sheet3Celli = sheet3Rowi.createCell(0);
                        if (i % 2 == 0) {
                            sheet3Celli.setCellStyle(orderEvenRowStyle);
                        } else {
                            sheet3Celli.setCellStyle(orderOddNumberRowStyle);
                        }
                        sheet3Celli = sheet3Rowi.createCell(1);
                        if (i % 2 == 0) {
                            sheet3Celli.setCellStyle(evenRowStyle);
                        } else {
                            sheet3Celli.setCellStyle(oddNumberRowStyle);
                        }
                    }
                    sheet3Celli = sheet3Rowi.createCell(2);
                    sheet3Celli.setCellValue(ttv.getPreferentialItem());
                    if (i % 2 == 0) {
                        sheet3Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet3Celli.setCellStyle(oddNumberRowStyle);
                    }
                    sheet3Celli = sheet3Rowi.createCell(3);
                    sheet3Celli.setCellValue(sdf.format(ttv.getPreferentialStartDatetime()) + "到" + sdf.format(ttv.getPreferentialEndDatetime()));
                    if (i % 2 == 0) {
                        sheet3Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet3Celli.setCellStyle(oddNumberRowStyle);
                    }
                    sheet3Celli = sheet3Rowi.createCell(4);
                    sheet3Celli.setCellValue(ttv.getApprovalOrgan());
                    if (i % 2 == 0) {
                        sheet3Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet3Celli.setCellStyle(oddNumberRowStyle);
                    }
                    sheet3Celli = sheet3Rowi.createCell(5);
                    sheet3Celli.setCellValue(ttv.getPolicy());
                    if (i % 2 == 0) {
                        sheet3Celli.setCellStyle(evenRowStyle);
                    } else {
                        sheet3Celli.setCellStyle(oddNumberRowStyle);
                    }
                }
            } else {
                sheet3Celli = sheet3Rowi.createCell(2);
                if (i % 2 == 0) {
                    sheet3Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet3Celli.setCellStyle(oddNumberRowStyle);
                }
                sheet3Celli = sheet3Rowi.createCell(3);
                if (i % 2 == 0) {
                    sheet3Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet3Celli.setCellStyle(oddNumberRowStyle);
                }
                sheet3Celli = sheet3Rowi.createCell(4);
                if (i % 2 == 0) {
                    sheet3Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet3Celli.setCellStyle(oddNumberRowStyle);
                }
                sheet3Celli = sheet3Rowi.createCell(5);
                if (i % 2 == 0) {
                    sheet3Celli.setCellStyle(evenRowStyle);
                } else {
                    sheet3Celli.setCellStyle(oddNumberRowStyle);
                }
            }
        }
        
        // 输出到指定的文件夹
        FileOutputStream fileOut = new FileOutputStream("companyAboutTax.xls");
        wb.write(fileOut);
        fileOut.close();
        return new FileInputStream("companyAboutTax.xls");
        
    }
    
    /**
     * <p>Description: 设置表格边框</p>
     * @param style
     * @return
     */
    private static HSSFCellStyle setBorder(HSSFCellStyle style) {
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        return style;
    }
    
}