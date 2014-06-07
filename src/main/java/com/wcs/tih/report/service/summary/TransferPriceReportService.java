package com.wcs.tih.report.service.summary;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.InvsTransferPrice;
import com.wcs.tih.model.InvsVerifyTransType;
import com.wcs.tih.util.ArithUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class TransferPriceReportService extends AReportSummary {

    @EJB
    private CommonService commonService;

    @Override
    public String summary(String oldPath, String filename, List tps) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy");
        String newFileName = copyTemplate(oldPath, oldPath + TEMPORARY_PATH, filename);
        Workbook workbook = getWorkbook(newFileName);
        CellStyle cellStyle = createCellStyle();
        getSheet("同期资料管理汇总");
        setDisplayGridlines(false);

        int rownum = 1;
        ;
        int firstNum = 0;
        int lastNum = 0;
        for (int i = 0; i < tps.size(); i++) {
            InvsTransferPrice tp = (InvsTransferPrice) tps.get(i);
            List<InvsVerifyTransType> tts = tp.getInvsVerifyTransTypes();
            firstNum = rownum;
            for (int j = 0; j < tts.size(); j++) {
                InvsVerifyTransType tt = tts.get(j);
                // 根据子类的数量来循环创建多少行
                Row newRow = createRow(rownum);
                for (int k = 0; k < 12; k++) {
                    // 每行有12列
                    Cell newCell = newRow.createCell(k);
                    setBorder(newCell, cellStyle);
                }
                rownum++;
                float height = 0;
                float height1 = getExcelCellAutoHeight(tt.getAdjustSpecialReason(), 12f);
                float height2 = getExcelCellAutoHeight(tt.getValidationMethod(), 4f);
                height = maxNumber(height1, height2);
                newRow.setHeight((short) height);

                // 填子类数据（6~11）
                newRow.getCell(5).setCellValue(getDictValueByKey(tt.getTransType()));
                newRow.getCell(6).getCellStyle().setWrapText(true);
                newRow.getCell(6).setCellValue(getDictValueByKey(tt.getValidationMethod()));
                newRow.getCell(7).setCellValue(formatKeepTwoScale(ArithUtil.mul(tt.getCompareCompanyMedian(), 100)));
                newRow.getCell(8).setCellValue(formatKeepTwoScale(ArithUtil.mul(tt.getBeforeAdjustRatio(), 100)));

                newRow.getCell(9).getCellStyle().setWrapText(true);
                newRow.getCell(9).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                newRow.getCell(9).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
                newRow.getCell(9).setCellValue(tt.getAdjustSpecialReason());

                newRow.getCell(10).setCellValue(formatKeepTwoScale(ArithUtil.mul(tt.getAfterAdjustRatio(), 100)));
            }
            lastNum = rownum - 1;
            // 填父类类数据（1~5，12）
            Row firstRow = getRow(firstNum);
            firstRow.getCell(0).setCellValue(tp.getDecade() == null ? "" : df.format(tp.getDecade()));
            firstRow.getCell(1).getCellStyle().setWrapText(true);
            firstRow.getCell(1).setCellValue(tp.getCompanyName());
            firstRow.getCell(2).setCellValue(formatKeepTwoScale(tp.getAssoDebtEquityRatio()));
            firstRow.getCell(3).setCellValue(getBooleanByKey(tp.getPrepareDocInd()));
            firstRow.getCell(4).setCellValue(getBooleanByKey(tp.getSubmitDocInd()));

            firstRow.getCell(11).getCellStyle().setWrapText(true);
            firstRow.getCell(11).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            firstRow.getCell(11).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            firstRow.getCell(11).setCellValue(tp.getRemarks());
            // 合并单元格（1~5，12）
            for (int j = 0; j < 12; j++) {
                mergedCell(firstNum, lastNum, j, j);
                // 跳过6~11列
                if (j == 4) {
                    j = 10;
                }
            }
        }
        // 保存文件
        FileOutputStream os = new FileOutputStream(newFileName);
        workbook.write(os);
        os.close();
        return newFileName;
    }

    private String getDictValueByKey(String key) {
        DictVo dictVo = commonService.getDictVoByKey(key);
        return dictVo.getCodeVal() == null ? "" : dictVo.getCodeVal();
    }

    private String getBooleanByKey(String key) {
        return "Y".equals(key) ? "是" : "否";
    }
}
