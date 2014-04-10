package com.wcs.tih.report.controller.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.model.ReportVatIptDeductionDetail;
import com.wcs.tih.util.DataUtil;

/**
 * 生成增值税进项税额抵扣Excel汇总信息
 * @author zhaoqian
 */
@Stateless
public class VATSummaryExpExcel {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
	public InputStream createVATSummaryExcel(Map<String,List<ReportVatIptDeductionDetail>> mapWithData) throws Exception {
		logger.info("进入生成增值税进项税额抵扣Excel汇总信息!");
		try {
			// 创建一个新的excel
			HSSFWorkbook wb = new HSSFWorkbook();
			
			// 创建样式对象,Create a new Cell style and add it to the workbook's style,table. You can define up to 4000 unique styles in a .xls
			HSSFCellStyle titleStyle = wb.createCellStyle();
			titleStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			//样式一,
			HSSFCellStyle lightBuleTitleStyle = wb.createCellStyle();
			lightBuleTitleStyle.setFillForegroundColor(HSSFColor.TAN.index);
			lightBuleTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			lightBuleTitleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			lightBuleTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			lightBuleTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			lightBuleTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			lightBuleTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			lightBuleTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//样式二
			HSSFCellStyle royalBuleTitleStyle = wb.createCellStyle();
			royalBuleTitleStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			royalBuleTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			royalBuleTitleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			royalBuleTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			royalBuleTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			royalBuleTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			royalBuleTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			royalBuleTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//样式三 
			HSSFCellStyle darkYellowTitle = wb.createCellStyle();
			darkYellowTitle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			darkYellowTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			darkYellowTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			darkYellowTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			darkYellowTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			darkYellowTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			darkYellowTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			darkYellowTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//样式四
			HSSFCellStyle coralTitleStyle = wb.createCellStyle();
			coralTitleStyle.setFillForegroundColor(HSSFColor.CORAL.index);
			coralTitleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			coralTitleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			coralTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			coralTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			coralTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			coralTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			coralTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 生成一个字体,excel使用的字体设置
			HSSFFont titleFont = wb.createFont();
			titleFont.setColor(HSSFColor.BLACK.index);
			titleFont.setBoldweight((short) 10);
			titleFont.setFontHeightInPoints((short) 10);
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			titleStyle.setFont(titleFont);

			// 设置行样式
			HSSFCellStyle rowStyle = wb.createCellStyle();
			rowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			rowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			rowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			rowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			rowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			rowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

			// 奇数行样式
			HSSFCellStyle oddNumberRowStyle = wb.createCellStyle();
			oddNumberRowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			oddNumberRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			oddNumberRowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			oddNumberRowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			oddNumberRowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			oddNumberRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			oddNumberRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			oddNumberRowStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
			
			// 偶数行样式
			HSSFCellStyle evenRowStyle = wb.createCellStyle();
			evenRowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			evenRowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			evenRowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			evenRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			evenRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			evenRowStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
			
			//TODO
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
			HSSFCellStyle orderOddNumberRowStyle = wb.createCellStyle();
			orderOddNumberRowStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			orderOddNumberRowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			orderOddNumberRowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			orderOddNumberRowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			orderOddNumberRowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			orderOddNumberRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			orderOddNumberRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			orderOddNumberRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

			// 序号偶数行样式
			HSSFCellStyle orderEvenRowStyle = wb.createCellStyle();
			orderEvenRowStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			orderEvenRowStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			orderEvenRowStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			orderEvenRowStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			orderEvenRowStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
			orderEvenRowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

			// 创建一个或多个sheet
			HSSFSheet sheetVAT = wb.createSheet("增值税进项税额抵扣情况表汇总");

			// 合并单元格(startRow，endRow，startColumn，endColumn)  总的,就是excel共占多少列
			sheetVAT.addMergedRegion(new CellRangeAddress(0, 0, 0, 76));

			// 设置每个 列宽的长度,由0开始,总共到76.共77列.
			for (int i = 0; i <= 76; i++) {
				sheetVAT.setColumnWidth((short) i, 3500);
			}

			// 设置行数
			HSSFRow sheetVATRow1 = sheetVAT.createRow((short) 1);
			sheetVATRow1.setHeight((short) 399);
			sheetVATRow1.setRowStyle(rowStyle);
			HSSFRow sheetVATRow2 = sheetVAT.createRow((short) 2);
			sheetVATRow2.setHeight((short) 399);
			sheetVATRow2.setRowStyle(rowStyle);

			// 0 公司名称,第一列
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
			HSSFCell cell0 = sheetVATRow1.createCell(0);
			cell0.setCellValue("公司名称");
			cell0.setCellStyle(titleStyle);

			// 1 票据类型,第二列
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 1, 1, 1));
			HSSFCell cell1 = sheetVATRow1.createCell(1);
			cell1.setCellValue("票据类型");
			cell1.setCellStyle(lightBuleTitleStyle);
			HSSFCell cell2 = sheetVATRow2.createCell(1);
			cell2.setCellValue("品种");
			cell2.setCellStyle(lightBuleTitleStyle);

			// 2~61
			for (int i = 2; i < 62; i = i + 12) {
				String cellName = "";
				HSSFCellStyle titleStyleX=null;
				switch (i) {
				case 2:
					cellName = "农产品收购发票";
					titleStyleX=royalBuleTitleStyle;
					break;
				case 14:
					cellName = "农产品销售发票";
					titleStyleX=darkYellowTitle;
					break;
				case 26:
					cellName = "增值税普通发票";
					titleStyleX=royalBuleTitleStyle;
					break;
				case 38:
					cellName = "增值税专用发票";
					titleStyleX=darkYellowTitle;
					break;
				case 50:
					cellName = "海关进口增值税专用缴款书";
					titleStyleX=royalBuleTitleStyle;
					break;
				}
				sheetVAT.addMergedRegion(new CellRangeAddress(1, 1, i, i + 11));
				HSSFCell cellX1 = sheetVATRow1.createCell(i);
				cellX1.setCellValue(cellName);
				cellX1.setCellStyle(titleStyleX);
				String[] strArray={"大豆","菜籽","水稻","小麦","花生","芝麻","棉籽","葵籽","玉米","其他杂粮","小计","比例"};
				for(int j=0;j<strArray.length;j++){
					HSSFCell cellX2 = sheetVATRow2.createCell(i+j);
					cellX2.setCellValue(strArray[j]);
					cellX2.setCellStyle(titleStyleX);
				}
			}

			// 62 合计
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 2, 62, 62));
			HSSFCell cell62 = sheetVATRow1.createCell(62);
			cell62.setCellValue("合计");
			cell62.setCellStyle(coralTitleStyle);
			// 63~~68
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 1, 63, 68));
			HSSFCell cell63 = sheetVATRow1.createCell(63);
			cell63.setCellValue("增值税专用发票");
			cell63.setCellStyle(darkYellowTitle);
			
			String[] strArrayWithVATSPE={"3%货物及劳务","6%货物","11%劳务","13%货物","17%货物及劳务","小计"};
			for(int i=0;i<strArrayWithVATSPE.length;i++){
				HSSFCell cell63t = sheetVATRow2.createCell(63+i);
				cell63t.setCellValue(strArrayWithVATSPE[i]);
				cell63t.setCellStyle(darkYellowTitle);
			}
			
			// 69~~72
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 1, 69, 72));
			HSSFCell cell68 = sheetVATRow1.createCell(69);
			cell68.setCellValue("运输费用结算单据");
			cell68.setCellStyle(royalBuleTitleStyle);
			
			String[] arrTransportation={"铁路大票","管道运输发票","公路内河运输发票","小计"};
			for(int i=0;i<arrTransportation.length;i++){
				HSSFCell cell68t = sheetVATRow2.createCell(69+i);
				cell68t.setCellValue(arrTransportation[i]);
				cell68t.setCellStyle(royalBuleTitleStyle);
			}
			
			// 73~~75
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 1, 73, 75));
			HSSFCell cell72 = sheetVATRow1.createCell(73);
			cell72.setCellValue("海关进口增值税专用缴款书");
			cell72.setCellStyle(darkYellowTitle);
			
			String[] arrCustoms={"13%税率货物","17%税率货物","小计"};
			for(int i=0;i<arrCustoms.length;i++){
				HSSFCell cell72t = sheetVATRow2.createCell(73+i);
				cell72t.setCellValue(arrCustoms[i]);
				cell72t.setCellStyle(darkYellowTitle);
			}
			// 76
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 2, 76, 76));
			HSSFCell cell75 = sheetVATRow1.createCell(76);
			cell75.setCellValue("其他");
			cell75.setCellStyle(royalBuleTitleStyle);
			// 77
			sheetVAT.addMergedRegion(new CellRangeAddress(1, 2, 77, 77));
			HSSFCell cell76 = sheetVATRow1.createCell(77);
			cell76.setCellValue("总计");
			cell76.setCellStyle(coralTitleStyle);
			
			//在这里添加数据到excel中去.
			int mapRowBegin=3;
			int forTime=1;
			int i=2;
			for( Map.Entry<String, List<ReportVatIptDeductionDetail>> map:mapWithData.entrySet()){
				logger.info("map循环开始!"+map);
				HSSFCellStyle cellStyleNow;
				HSSFCellStyle companyStyle;
				if(i%2==0)
				{
					logger.info("这个数是偶数");
					cellStyleNow=numberEvenRowStyle;
					companyStyle=evenRowStyle;
				}else{
					//TODO qishu
					logger.info("这个数是奇数");
					cellStyleNow=numberOddNumberRowStyle;
					companyStyle=oddNumberRowStyle;
				}
				//创建数据填充的四行
				HSSFRow sheetVATRowBegin1 = sheetVAT.createRow((short) mapRowBegin);
				HSSFRow sheetVATRowBegin2 = sheetVAT.createRow((short) mapRowBegin+1);
				HSSFRow sheetVATRowBegin3 = sheetVAT.createRow((short) mapRowBegin+2);
				HSSFRow sheetVATRowBegin4 = sheetVAT.createRow((short) mapRowBegin+3);
				sheetVATRowBegin1.setHeight((short) 399);
				sheetVATRowBegin2.setHeight((short) 399);
				sheetVATRowBegin3.setHeight((short) 399);
				sheetVATRowBegin4.setHeight((short) 399);
				HSSFCellStyle mapRowStyle;
				if(forTime%2!=0 ){
					mapRowStyle=oddNumberRowStyle;
				}else{
					mapRowStyle=evenRowStyle;
				}
				sheetVATRowBegin1.setRowStyle(mapRowStyle);
				sheetVATRowBegin2.setRowStyle(mapRowStyle);
				sheetVATRowBegin3.setRowStyle(mapRowStyle);
				sheetVATRowBegin4.setRowStyle(mapRowStyle);
				
				//合并公司占用的单元格,这里赋值是公司名,如公司AAA,公司BBB
				sheetVAT.addMergedRegion(new CellRangeAddress(mapRowBegin, mapRowBegin+3, 0, 0));
				HSSFCell cellMapCommanyName = sheetVATRowBegin1.createCell(0);
				cellMapCommanyName.setCellValue(map.getKey());
				cellMapCommanyName.setCellStyle(companyStyle);
				//设置数量(吨),金额(元),税率,税额(元)
				
				HSSFCell cell1Map = sheetVATRowBegin1.createCell(1);
				cell1Map.setCellValue("数量(吨)");
				cell1Map.setCellStyle(companyStyle);
				HSSFCell cell2Map = sheetVATRowBegin2.createCell(1);
				cell2Map.setCellValue("金额(元)");
				cell2Map.setCellStyle(companyStyle);
				HSSFCell cell3Map = sheetVATRowBegin3.createCell(1);
				cell3Map.setCellValue("税率");
				cell3Map.setCellStyle(companyStyle);
				HSSFCell cell4Map = sheetVATRowBegin4.createCell(1);
				cell4Map.setCellValue("税额(元)");
				cell4Map.setCellStyle(companyStyle);
				
				//前五个的小计,比例for循环完毕后进行获取,小计无税率
				double summaryAmount1=0;
				double summaryTaxMoney1=0;
				double summaryMoney1=0;
				
				double summaryAmount2=0;
				double summaryTaxMoney2=0;
				double summaryMoney2=0;
				
				double summaryAmount3=0;
				double summaryTaxMoney3=0;
				double summaryMoney3=0;
				
				double summaryAmount4=0;
				double summaryTaxMoney4=0;
				double summaryMoney4=0;
				
				double summaryAmount5=0;
				double summaryTaxMoney5=0;
				double summaryMoney5=0;
				//中间三个的小计
				double summaryAmountM1=0;
				double summaryMoneyM1=0;
				double summaryTaxMoneyM1=0;
				
				double summaryAmountM2=0;
				double summaryMoneyM2=0;
				double summaryTaxMoneyM2=0;
				
				double summaryAmountM3=0;
				double summaryMoneyM3=0;
				double summaryTaxMoneyM3=0;
				
				//最后一个的小计,比例都是没有的.
				
				//合计summary的变量:执行前五次的时候,每次要加一次 数量(吨),金额(元),税率无,税额(元)
				double summaryAmount=0;
				double summaryMoney=0;
				double summaryTaxMoney=0;
				
				//总计allSummary的变量
				double allSummaryAmount=0;	//数量
				double allSummaryMoney=0;	//金额
				double allSummaryTaxMoney=0; //税额
				
				//开始for循环,获取其中的值,要计算的值在数据库数据处理完之后进行赋值.
				//map.getValue()是一个list集合,里面每个对象都是ReportVatIptDeductionDetail,所以在这个计算完后,要计算
				for(ReportVatIptDeductionDetail r:map.getValue()){
					//农产品收购发票,第一组数据
					if(r.getInvoiceTypeName().equals("农产品收购发票")){
						if(r.getVarietyName().equals("大豆")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 2, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("菜籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 3, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("水稻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 4, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("小麦")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 5, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("花生")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 6, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("芝麻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 7, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("棉籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 8, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("葵籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 9, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("玉米")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 10, r,true,cellStyleNow);
						}
						else if(r.getVarietyName().equals("其他杂粮")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 11, r,true,cellStyleNow);
						}
						summaryAmount1=summaryAmount1+r.getAmmount();
						summaryMoney1=summaryMoney1+r.getMoneySum().doubleValue();
						summaryTaxMoney1=summaryTaxMoney1+r.getTaxAmmount().doubleValue();
						summaryAmount=summaryAmount+r.getAmmount();
						summaryMoney=summaryMoney+r.getMoneySum().doubleValue();
						summaryTaxMoney=summaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//农产品销售发票
					else if(r.getInvoiceTypeName().equals("农产品销售发票")){
						//大豆
						if(r.getVarietyName().equals("大豆")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 14, r,true,cellStyleNow);
						}
						//菜籽
						else if(r.getVarietyName().equals("菜籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 15, r,true,cellStyleNow);
						}
						//水稻
						else if(r.getVarietyName().equals("水稻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 16, r,true,cellStyleNow);
						}
						//小麦
						else if(r.getVarietyName().equals("小麦")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 17, r,true,cellStyleNow);
						}
						//花生
						else if(r.getVarietyName().equals("花生")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 18, r,true,cellStyleNow);
						}
						//芝麻
						else if(r.getVarietyName().equals("芝麻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 19, r,true,cellStyleNow);
						}
						//棉籽
						else if(r.getVarietyName().equals("棉籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 20, r,true,cellStyleNow);
						}
						//葵籽
						else if(r.getVarietyName().equals("葵籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 21, r,true,cellStyleNow);
						}
						//玉米
						else if(r.getVarietyName().equals("玉米")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 22, r,true,cellStyleNow);
						}
						//其他杂粮
						else if(r.getVarietyName().equals("其他杂粮")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 23, r,true,cellStyleNow);
						}
						summaryAmount2=summaryAmount2+r.getAmmount();
						summaryMoney2=summaryMoney2+r.getMoneySum().doubleValue();
						summaryTaxMoney2=summaryTaxMoney2+r.getTaxAmmount().doubleValue();
						summaryAmount=summaryAmount+r.getAmmount();
						summaryMoney=summaryMoney+r.getMoneySum().doubleValue();
						summaryTaxMoney=summaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//增值税普通发票
					else if(r.getInvoiceTypeName().equals("增值税普通发票")){
						//大豆
						if(r.getVarietyName().equals("大豆")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 26, r,true,cellStyleNow);
						}
						//菜籽
						else if(r.getVarietyName().equals("菜籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 27, r,true,cellStyleNow);
						}
						//水稻
						else if(r.getVarietyName().equals("水稻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 28, r,true,cellStyleNow);
						}
						//小麦
						else if(r.getVarietyName().equals("小麦")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 29, r,true,cellStyleNow);
						}
						//花生
						else if(r.getVarietyName().equals("花生")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 30, r,true,cellStyleNow);
						}
						//芝麻
						else if(r.getVarietyName().equals("芝麻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 31, r,true,cellStyleNow);
						}
						//棉籽
						else if(r.getVarietyName().equals("棉籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 32, r,true,cellStyleNow);
						}
						//葵籽
						else if(r.getVarietyName().equals("葵籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 33, r,true,cellStyleNow);
						}
						//玉米
						else if(r.getVarietyName().equals("玉米")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 34, r,true,cellStyleNow);
						}
						//其他杂粮
						else if(r.getVarietyName().equals("其他杂粮")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 35, r,true,cellStyleNow);
						}
						summaryAmount3=summaryAmount3+r.getAmmount();
						summaryMoney3=summaryMoney3+r.getMoneySum().doubleValue();
						summaryTaxMoney3=summaryTaxMoney3+r.getTaxAmmount().doubleValue();
						summaryAmount=summaryAmount+r.getAmmount();
						summaryMoney=summaryMoney+r.getMoneySum().doubleValue();
						summaryTaxMoney=summaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//增值税专用发票
					else if(r.getInvoiceTypeName().equals("增值税专用发票")&&!(r.getVarietyName().equals("3% 货物及劳务")||r.getVarietyName().equals("6% 货物及劳务")||r.getVarietyName().equals("11%劳务")||r.getVarietyName().equals("13%货物")||r.getVarietyName().equals("17%货物及劳务"))){
						//大豆
						if(r.getVarietyName().equals("大豆")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 38, r,true,cellStyleNow);
						}
						//菜籽
						else if(r.getVarietyName().equals("菜籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 39, r,true,cellStyleNow);
						}
						//水稻
						else if(r.getVarietyName().equals("水稻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 40, r,true,cellStyleNow);
						}
						//小麦
						else if(r.getVarietyName().equals("小麦")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 41, r,true,cellStyleNow);
						}
						//花生
						else if(r.getVarietyName().equals("花生")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 42, r,true,cellStyleNow);
						}
						//芝麻
						else if(r.getVarietyName().equals("芝麻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 43, r,true,cellStyleNow);
						}
						//棉籽
						else if(r.getVarietyName().equals("棉籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 44, r,true,cellStyleNow);
						}
						//葵籽
						else if(r.getVarietyName().equals("葵籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 45, r,true,cellStyleNow);
						}
						//玉米
						else if(r.getVarietyName().equals("玉米")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 46, r,true,cellStyleNow);
						}
						//其他杂粮
						else if(r.getVarietyName().equals("其他杂粮")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 47, r,true,cellStyleNow);
						}
						summaryAmount4=summaryAmount4+r.getAmmount();
						summaryMoney4=summaryMoney4+r.getMoneySum().doubleValue();
						summaryTaxMoney4=summaryTaxMoney4+r.getTaxAmmount().doubleValue();
						summaryAmount=summaryAmount+r.getAmmount();
						summaryMoney=summaryMoney+r.getMoneySum().doubleValue();
						summaryTaxMoney=summaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//海关进口增值税专用缴款书
					else if(r.getInvoiceTypeName().equals("海关进口增值税专用缴款书")&&!(r.getVarietyName().equals("13%税率货物")||r.getVarietyName().equals("17%税率货物"))){
						//大豆
						if(r.getVarietyName().equals("大豆")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 50, r,true,cellStyleNow);
						}
						//菜籽
						else if(r.getVarietyName().equals("菜籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 51, r,true,cellStyleNow);
						}
						//水稻
						else if(r.getVarietyName().equals("水稻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 52, r,true,cellStyleNow);
						}
						//小麦
						else if(r.getVarietyName().equals("小麦")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 53, r,true,cellStyleNow);
						}
						//花生
						else if(r.getVarietyName().equals("花生")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 54, r,true,cellStyleNow);
						}
						//芝麻
						else if(r.getVarietyName().equals("芝麻")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 55, r,true,cellStyleNow);
						}
						//棉籽
						else if(r.getVarietyName().equals("棉籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 56, r,true,cellStyleNow);
						}
						//葵籽
						else if(r.getVarietyName().equals("葵籽")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 57, r,true,cellStyleNow);
						}
						//玉米
						else if(r.getVarietyName().equals("玉米")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 58, r,true,cellStyleNow);
						}
						//其他杂粮
						else if(r.getVarietyName().equals("其他杂粮")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 59, r,true,cellStyleNow);
						}
						summaryAmount5=summaryAmount5+r.getAmmount();
						summaryMoney5=summaryMoney5+r.getMoneySum().doubleValue();
						summaryTaxMoney5=summaryTaxMoney5+r.getTaxAmmount().doubleValue();
						summaryAmount=summaryAmount+r.getAmmount();
						summaryMoney=summaryMoney+r.getMoneySum().doubleValue();
						summaryTaxMoney=summaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//增值税专用发票（特）
					if(r.getInvoiceTypeName().equals("增值税专用发票")&&(r.getVarietyName().equals("3% 货物及劳务")||r.getVarietyName().equals("6% 货物及劳务")||r.getVarietyName().equals("11%劳务")||r.getVarietyName().equals("13%货物")||r.getVarietyName().equals("17%货物及劳务"))){
						//3% 货物及劳务
						if(r.getVarietyName().equals("3% 货物及劳务")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 63, r,true,cellStyleNow);
						}
						//6% 货物及劳务
						else if(r.getVarietyName().equals("6% 货物及劳务")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 64, r,true,cellStyleNow);
						}
						//11% 劳务
						else if(r.getVarietyName().equals("11%劳务")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 65, r,true,cellStyleNow);
						}
						//13%货物
						else if(r.getVarietyName().equals("13%货物")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 66, r,true,cellStyleNow);
						}
						//17%货物及劳务
						else if(r.getVarietyName().equals("17%货物及劳务")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 67, r,true,cellStyleNow);
						}
						summaryAmountM1=summaryAmountM1+r.getAmmount();
						summaryMoneyM1=summaryMoneyM1+r.getMoneySum().doubleValue();
						summaryTaxMoneyM1=summaryTaxMoneyM1+r.getTaxAmmount().doubleValue();
						allSummaryAmount=allSummaryAmount+r.getAmmount();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//运输费用结算单据（特）
					else if(r.getInvoiceTypeName().equals("运输费用结算单据")){
						//铁路大票
						if(r.getVarietyName().equals("铁路大票")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 69, r,true,cellStyleNow);
						}
						//管道运输发票
						else if(r.getVarietyName().equals("管道运输发票")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 70, r,true,cellStyleNow);
						}
						//公路内河运输发票
						else if(r.getVarietyName().equals("公路内河运输发票")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 71, r,true,cellStyleNow);
						}
						summaryAmountM2=summaryAmountM2+r.getAmmount();
						summaryMoneyM2=summaryMoneyM2+r.getMoneySum().doubleValue();
						summaryTaxMoneyM2=summaryTaxMoneyM2+r.getTaxAmmount().doubleValue();
						allSummaryAmount=allSummaryAmount+r.getAmmount();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//海关进口增值税专用缴款书（特）
					else if(r.getInvoiceTypeName().equals("海关进口增值税专用缴款书")&&(r.getVarietyName().equals("13%税率货物")||r.getVarietyName().equals("17%税率货物"))){
						//13%税率货物
						if(r.getVarietyName().equals("13%税率货物")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 73, r,true,cellStyleNow);
						}
						//17%税率货物
						else if(r.getVarietyName().equals("17%税率货物")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 74, r,true,cellStyleNow);
						}
						summaryAmountM3=summaryAmountM3+r.getAmmount();
						summaryMoneyM3=summaryMoneyM3+r.getMoneySum().doubleValue();
						summaryTaxMoneyM3=summaryTaxMoneyM3+r.getTaxAmmount().doubleValue();
						allSummaryAmount=allSummaryAmount+r.getAmmount();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					//其他
					else if(r.getInvoiceTypeName().equals("其他")||r.getInvoiceTypeName().equals("其它")){
						//其他
						if(r.getVarietyName().equals("其他")||r.getVarietyName().equals("其它")){
							excelCellValueCommonCode(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3, sheetVATRowBegin4, 76, r,true,cellStyleNow);
						}
						allSummaryAmount=allSummaryAmount+r.getAmmount();
						allSummaryTaxMoney=allSummaryTaxMoney+r.getTaxAmmount().doubleValue();
						allSummaryMoney=allSummaryMoney+r.getMoneySum().doubleValue();
					}
					
				}
				allSummaryAmount=allSummaryAmount+summaryAmount;
				//循环完毕就是计算合计和总计,以及每个里面的小计,前5个的比例
				//计算前五个的小计,比例
				this.fiveSubtotalAndProportion(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 12, summaryAmount1, summaryMoney1, summaryTaxMoney1, summaryAmount, allSummaryMoney, allSummaryTaxMoney,cellStyleNow);
				this.fiveSubtotalAndProportion(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 24, summaryAmount2, summaryMoney2, summaryTaxMoney2, summaryAmount, allSummaryMoney, allSummaryTaxMoney,cellStyleNow);
				this.fiveSubtotalAndProportion(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 36, summaryAmount3, summaryMoney3, summaryTaxMoney3, summaryAmount, allSummaryMoney, allSummaryTaxMoney,cellStyleNow);
				this.fiveSubtotalAndProportion(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 48, summaryAmount4, summaryMoney4, summaryTaxMoney4, summaryAmount, allSummaryMoney, allSummaryTaxMoney,cellStyleNow);
				this.fiveSubtotalAndProportion(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 60, summaryAmount5, summaryMoney5, summaryTaxMoney5, summaryAmount, allSummaryMoney, allSummaryTaxMoney,cellStyleNow);
				//中间三个的小计,无比例
				this.middleSubtotal(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 68,summaryAmountM1, summaryMoneyM1, summaryTaxMoneyM1,cellStyleNow);
				this.middleSubtotal(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 72,summaryAmountM2, summaryMoneyM2, summaryTaxMoneyM2,cellStyleNow);
				this.middleSubtotal(sheetVATRowBegin1, sheetVATRowBegin2, sheetVATRowBegin3,sheetVATRowBegin4, 75,summaryAmountM3, summaryMoneyM3, summaryTaxMoneyM3,cellStyleNow);
				//计算合计位置:62 
				HSSFCell cellr1 = sheetVATRowBegin1.createCell(62);
				cellr1.setCellValue(summaryAmount);
				cellr1.setCellStyle(cellStyleNow);
				HSSFCell cellr2 = sheetVATRowBegin2.createCell(62);
				cellr2.setCellValue(summaryMoney);
				cellr2.setCellStyle(cellStyleNow);
				HSSFCell cellr3 = sheetVATRowBegin3.createCell(62);
				cellr3.setCellValue("");
				cellr3.setCellStyle(cellStyleNow);
				HSSFCell cellr4 = sheetVATRowBegin4.createCell(62);
				cellr4.setCellValue(summaryTaxMoney);
				cellr4.setCellStyle(cellStyleNow);
				//计算总计位置:77
				HSSFCell cellr2All = sheetVATRowBegin2.createCell(77);
				cellr2All.setCellValue(allSummaryMoney);
				cellr2All.setCellStyle(cellStyleNow);
				HSSFCell cellr4All = sheetVATRowBegin4.createCell(77);
				cellr4All.setCellValue(allSummaryTaxMoney);
				cellr4All.setCellStyle(cellStyleNow);
				
				HSSFCell cellr1All = sheetVATRowBegin1.createCell(77);
				cellr1All.setCellValue(allSummaryAmount);
				cellr1All.setCellStyle(cellStyleNow);
				HSSFCell cellr3All = sheetVATRowBegin3.createCell(77);
				cellr3All.setCellValue("");
				cellr3All.setCellStyle(cellStyleNow);
				mapRowBegin=mapRowBegin+4;
				i=i+1;
				logger.info("i:"+i);
			}
			
			   // 输出到指定的文件夹
            FileOutputStream fileOut = new FileOutputStream("VATStatistics.xls");
            wb.write(fileOut);
            logger.info("VSVSVS:"+fileOut);
            fileOut.close();
            logger.info("生成Excel成功,请LOOK!");
            return new FileInputStream("VATStatistics.xls");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("生成Excel失败,请重新生成!");
        }
	}
	
	private void excelCellValueCommonCode(HSSFRow sheetVATRowBegin1,HSSFRow sheetVATRowBegin2,HSSFRow sheetVATRowBegin3,HSSFRow sheetVATRowBegin4,int column,ReportVatIptDeductionDetail r,boolean needAmount,HSSFCellStyle cellStyleNow){
		//中间三个是没有数量的,如果needAmount=true的就是需要,中间三个设这个布尔为false.
		HSSFCell cellr1 = sheetVATRowBegin1.createCell(column);
		cellr1.setCellValue(needAmount?String.valueOf(r.getAmmount()):"");
		cellr1.setCellStyle(cellStyleNow);
		
		HSSFCell cellr2 = sheetVATRowBegin2.createCell(column);
		cellr2.setCellValue(r.getMoneySum().doubleValue());
		cellr2.setCellStyle(cellStyleNow);
		
		HSSFCell cellr3 = sheetVATRowBegin3.createCell(column);
		cellr3.setCellValue((DataUtil.dataFormat((r.getTaxRate())*100,2)+"%"));
		cellr3.setCellStyle(cellStyleNow);
		
		HSSFCell cellr4 = sheetVATRowBegin4.createCell(column);
		cellr4.setCellValue(r.getTaxAmmount().doubleValue());
		cellr4.setCellStyle(cellStyleNow);
	}
	
	private void fiveSubtotalAndProportion(HSSFRow sheetVATRowBegin1,HSSFRow sheetVATRowBegin2,HSSFRow sheetVATRowBegin3,HSSFRow sheetVATRowBegin4,int column,double amount1,double money1,double taxMoney1,double summaryAmount,double summaryMoney,double summaryTaxMoney,HSSFCellStyle cellStyleNow){
		//小计
		HSSFCell cellr1 = sheetVATRowBegin1.createCell(column);
		cellr1.setCellValue(amount1);
		cellr1.setCellStyle(cellStyleNow);
		
		HSSFCell cellr2 = sheetVATRowBegin2.createCell(column);
		cellr2.setCellValue(money1);
		cellr2.setCellStyle(cellStyleNow);
		
		//new
		HSSFCell cellr3 = sheetVATRowBegin3.createCell(column);
		cellr3.setCellValue("");
		cellr3.setCellStyle(cellStyleNow);
		
		HSSFCell cellr4 = sheetVATRowBegin4.createCell(column);
		cellr4.setCellValue(taxMoney1);
		cellr4.setCellStyle(cellStyleNow);
		
		//比例,why don't string?
		//TODO
		HSSFCell cellr11 = sheetVATRowBegin1.createCell(column+1);
		cellr11.setCellValue(summaryAmount==0?"":(DataUtil.dataFormat((amount1/summaryAmount)*100,2)+"%"));
		cellr11.setCellStyle(cellStyleNow);
		HSSFCell cellr21 = sheetVATRowBegin2.createCell(column+1);
		cellr21.setCellValue(summaryMoney==0?"":(DataUtil.dataFormat((money1/summaryMoney)*100,2)+"%"));
		cellr21.setCellStyle(cellStyleNow);
		//new
		HSSFCell cellr31 = sheetVATRowBegin3.createCell(column+1);
		cellr31.setCellValue("");
		cellr31.setCellStyle(cellStyleNow);
		HSSFCell cellr41 = sheetVATRowBegin4.createCell(column+1);
		cellr41.setCellValue(summaryTaxMoney==0?"":(DataUtil.dataFormat((taxMoney1/summaryTaxMoney)*100,2)+"%"));
		cellr41.setCellStyle(cellStyleNow);
	}
	
	private void middleSubtotal(HSSFRow sheetVATRowBegin1,HSSFRow sheetVATRowBegin2,HSSFRow sheetVATRowBegin3,HSSFRow sheetVATRowBegin4,int column,double summaryAmountM1,double summaryMoneyM1,double summaryTaxMoneyM1,HSSFCellStyle cellStyleNow){
		//小计
		HSSFCell cellr2 = sheetVATRowBegin2.createCell(column);
		cellr2.setCellValue(summaryMoneyM1);
		cellr2.setCellStyle(cellStyleNow);
		HSSFCell cellr4 = sheetVATRowBegin4.createCell(column);
		cellr4.setCellValue(summaryTaxMoneyM1);
		cellr4.setCellStyle(cellStyleNow);
		
		HSSFCell cellr1 = sheetVATRowBegin1.createCell(column);
		cellr1.setCellValue(summaryAmountM1);
		cellr1.setCellStyle(cellStyleNow);
		HSSFCell cellr3 = sheetVATRowBegin3.createCell(column);
		cellr3.setCellValue("");
		cellr3.setCellStyle(cellStyleNow);
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
