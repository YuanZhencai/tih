package com.wcs.tih.report.service.summary;

import java.io.FileOutputStream;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.wcs.tih.report.controller.vo.AntiAvoidanceVo;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class AntiAvoidanceReportService extends AReportSummary {

    @Override
    public String summary(String oldPath, String filename, List aaVos) throws Exception {
        String newFileName = copyTemplate(oldPath, oldPath + TEMPORARY_PATH, filename);
        Workbook workbook = getWorkbook(newFileName);
        getSheet("反避税统计汇总表");
        setDisplayGridlines(false);

        int rownum = 4;
        ;

        for (int i = 0; i < aaVos.size(); i++) {
            AntiAvoidanceVo aaVo = (AntiAvoidanceVo) aaVos.get(i);
            Row newRow = createRow(rownum);
            for (int k = 0; k < 22; k++) {
                Cell newCell = newRow.createCell(k);
                setBorder(newCell, null);
            }
            float height = 0;
            float height1 = getExcelCellAutoHeight(aaVo.getCause(), 9f);
            float height2 = getExcelCellAutoHeight(aaVo.getSponsorOrg(), 5f);
            float height3 = getExcelCellAutoHeight(aaVo.getImplementOrg(), 4f);
            float height4 = getExcelCellAutoHeight(aaVo.getMethod(), 11f);
            float height5 = getExcelCellAutoHeight(aaVo.getDoubt(), 31f);
            float height6 = getExcelCellAutoHeight(aaVo.getDealWith(), 11f);
            float height7 = getExcelCellAutoHeight(aaVo.getPhaseRemarks(), 7f);
            float height8 = getExcelCellAutoHeight(aaVo.getConclusion(), 9f);
            float height9 = getExcelCellAutoHeight(aaVo.getTaxTypes(), 3.5f);
            float height10 = getExcelCellAutoHeight(aaVo.getInvestType(), 3f);
            float height11 = getExcelCellAutoHeight(aaVo.getCompanyName(), 4f);
            height = maxNumber(height1, height2);
            height = maxNumber(height, height3);
            height = maxNumber(height, height4);
            height = maxNumber(height, height5);
            height = maxNumber(height, height6);
            height = maxNumber(height, height7);
            height = maxNumber(height, height8);
            height = maxNumber(height, height9);
            height = maxNumber(height, height10);
            height = maxNumber(height, height11);
            newRow.setHeight((short) height);

            newRow.getCell(0).setCellValue(rownum - 3);
            newRow.getCell(1).getCellStyle().setWrapText(true);
            newRow.getCell(1).setCellValue(aaVo.getCompanyName());

            newRow.getCell(2).getCellStyle().setWrapText(true);
            newRow.getCell(2).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(2).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(2).setCellValue(aaVo.getCause());

            newRow.getCell(3).getCellStyle().setWrapText(true);
            newRow.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(3).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(3).setCellValue(aaVo.getSponsorOrg());

            newRow.getCell(4).getCellStyle().setWrapText(true);
            newRow.getCell(4).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(4).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(4).setCellValue(aaVo.getImplementOrg());

            newRow.getCell(5).getCellStyle().setWrapText(true);
            newRow.getCell(5).setCellValue(aaVo.getInvestType());
            newRow.getCell(6).setCellValue(
                    formatDate(aaVo.getInvestStartDatetime()) + "/" + formatDate(aaVo.getInvestEndDatetime()));
            newRow.getCell(7).setCellValue(
                    formatDate(aaVo.getMissionStartDatetime()) + "/" + formatDate(aaVo.getMissionEndDatetime()));

            newRow.getCell(8).getCellStyle().setWrapText(true);
            newRow.getCell(8).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(8).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(8).setCellValue(aaVo.getTaxTypes());

            newRow.getCell(9).getCellStyle().setWrapText(true);
            newRow.getCell(9).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(9).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(9).setCellValue(aaVo.getMethod());

            newRow.getCell(10).getCellStyle().setWrapText(true);
            newRow.getCell(10).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(10).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(10).setCellValue(aaVo.getDoubt());

            setDoubleValue(newRow.getCell(11), aaVo.getRiskAccount() == null ? 0 : aaVo.getRiskAccount().doubleValue());

            newRow.getCell(12).getCellStyle().setWrapText(true);
            newRow.getCell(12).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(12).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(12).setCellValue(aaVo.getDealWith());

            newRow.getCell(13).getCellStyle().setWrapText(true);
            newRow.getCell(13).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(13).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(13).setCellValue(aaVo.getPhaseRemarks());

            newRow.getCell(14).getCellStyle().setWrapText(true);
            newRow.getCell(14).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            newRow.getCell(14).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            newRow.getCell(14).setCellValue(aaVo.getConclusion());

            newRow.getCell(20).setCellValue(
                    formatDate(aaVo.getTraceStartDatetime()) + "/" + formatDate(aaVo.getTraceEndDatetime()));
            newRow.getCell(21).setCellValue(aaVo.getContact());

            setDoubleValue(newRow.getCell(15), aaVo.getVat());
            setDoubleValue(newRow.getCell(16), aaVo.getCit());
            setDoubleValue(newRow.getCell(17), aaVo.getVat() + aaVo.getAddInterest());
            setDoubleValue(newRow.getCell(18), aaVo.getAddFine() + aaVo.getAddInterest());
            setDoubleValue(newRow.getCell(19), aaVo.getReducedLoss());
            rownum++;
        }

        // 表尾的实现，即表格的最后一行，根据实际报表要求
        Row lastRow = createRow(rownum);// 创建行
        for (int j = 0; j < 22; j++) {
            Cell lastRowCell = lastRow.createCell(j);// 创建单元格
            setBorder(lastRowCell, null);// 加边框
        }
        mergedCell(rownum, rownum, 0, 1);// 合并单元格
        lastRow.getCell(0).setCellValue("合计");
        // 给单元格设置公式
        setDoubleFormat(lastRow.getCell(15)).setCellFormula("SUM(P5:P" + rownum + ")");
        setDoubleFormat(lastRow.getCell(16)).setCellFormula("SUM(Q5:Q" + rownum + ")");
        setDoubleFormat(lastRow.getCell(17)).setCellFormula("SUM(R5:R" + rownum + ")");
        setDoubleFormat(lastRow.getCell(18)).setCellFormula("SUM(S5:S" + rownum + ")");
        setDoubleFormat(lastRow.getCell(19)).setCellFormula("SUM(T5:T" + rownum + ")");

        // 保存文件
        FileOutputStream os = new FileOutputStream(newFileName);
        workbook.write(os);
        os.close();
        return newFileName;
    }

}
