package com.wcs.tih.report.service.summary;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.Dict;
import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.report.controller.vo.InspectVo;

@Stateless
public class InspectReportSrevice extends AReportSummary {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext 
    private EntityManager em;
    
    @Override
    public String summary(String oldPath, String filename, List inspects) throws Exception {
        List<Dict> taxDicts = findTaxDicts(inspects);
        int dataSize = taxDicts.size();
        String newFileName = copyTemplate(oldPath ,filename, taxDicts);
        Workbook workbook = getWorkbook(newFileName);
        getSheet("汇总");
        setDisplayGridlines(false);
        int rownum = 4;;
        for (int i = 0; i < inspects.size(); i++) {
            InspectVo inspect = (InspectVo) inspects.get(i);
            Row createRow = createRow(rownum);
            for (int k = 0; k < 15 + dataSize; k++) {
                Cell createCell = createRow.createCell(k);
                setBorder(createCell, null);
            }
            float height = 0;
            //设置行高
            float height1 = getExcelCellAutoHeight(inspect.getInspectOrg(), 8f);
            float height2 = getExcelCellAutoHeight(inspect.getMainProblemDesc(), 10f);
            float height3 = getExcelCellAutoHeight(inspect.getRectificationPlan(), 4f);
            float height4 = getExcelCellAutoHeight(inspect.getTaxTypes(), 4.5f);
            float height5 = getExcelCellAutoHeight(inspect.getCompanyName(), 4f);
            height = maxNumber(height1, height2);
            height = maxNumber(height, height3);
            height = maxNumber(height, height4);
            height = maxNumber(height, height5);
            createRow.setHeight((short)height);
            
            createRow.getCell(0).setCellValue(rownum - 3);
            createRow.getCell(1).getCellStyle().setWrapText(true);
            createRow.getCell(1).setCellValue(inspect.getCompanyName());
            createRow.getCell(2).setCellValue(inspect.getInspectType());
            
            createRow.getCell(3).getCellStyle().setWrapText(true);
            createRow.getCell(3).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            createRow.getCell(3).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            createRow.getCell(3).setCellValue(inspect.getInspectOrg());
            
            createRow.getCell(4).setCellValue(formatDate(inspect.getInspectStartDatetime())+"/"+formatDate(inspect.getInspectEndDatetime()));
            createRow.getCell(5).setCellValue(formatDate(inspect.getMissionStartDatetime())+"/"+formatDate(inspect.getMissionEndDatetime()));
            
            createRow.getCell(6).getCellStyle().setWrapText(true);
            createRow.getCell(6).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            createRow.getCell(6).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            createRow.getCell(6).setCellValue(inspect.getTaxTypes());
            
            createRow.getCell(7).getCellStyle().setWrapText(true);
            createRow.getCell(7).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            createRow.getCell(7).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            createRow.getCell(7).setCellValue(inspect.getMainProblemDesc());
            
            createRow.getCell(dataSize+13).setCellValue(inspect.getContact());
            
            createRow.getCell(dataSize+14).getCellStyle().setWrapText(true);
            createRow.getCell(dataSize+14).getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
            createRow.getCell(dataSize+14).getCellStyle().setVerticalAlignment(CellStyle.VERTICAL_TOP);
            createRow.getCell(dataSize+14).setCellValue(inspect.getRectificationPlan());
            
            setDoubleValue(createRow.getCell(dataSize+8),inspect.getOverdueTax());
            setDoubleValue(createRow.getCell(dataSize+9),inspect.getPenalty());
            setDoubleValue(createRow.getCell(dataSize+10),inspect.getFine());
            setDoubleValue(createRow.getCell(dataSize+11),inspect.getReductionPrevLoss());
            setDoubleValue(createRow.getCell(dataSize+12),inspect.getInputTaxTurnsOut());
            for (int k = 0; k < dataSize; k++) {
                Dict taxDict = taxDicts.get(k);
                String codeKey = taxDict.getCodeCat()+"."+taxDict.getCodeKey();
                Cell cell = createRow.getCell(k+8);
                Double sumByTaxType = sumByTaxType(inspect.getId(), codeKey);
                if(sumByTaxType==null){
                    cell.setCellValue("-");
                }else{
                    setDoubleValue(cell, sumByTaxType);
                }
            }
            rownum++;
        }
        //保存文件
        FileOutputStream os = new FileOutputStream(newFileName);
        workbook.write(os);
        os.close();
        return newFileName;
    }
    
    public String copyTemplate(String oldPath, String filename, List<Dict> taxDicts) throws Exception {
        int taxSize = taxDicts.size();
        String template = copyTemplate(oldPath,oldPath + TEMPORARY_PATH, filename);
        Workbook workbook = getWorkbook(template);
        //得到报表模版
        Sheet templateSheet = getSheet("汇总模版");
        //创建新的汇总sheet
        createSheet("汇总");
        setDisplayGridlines(false);
        //创建新的表头
        for (Row row : templateSheet) {
            int columnIndex = 0;
            Row createRow = createRow(row.getRowNum());
            createRow.setHeight(row.getHeight());
            for (Cell cell : row) {
                Cell createCell = createRow.createCell(columnIndex);
                createCell.setCellValue(cell.getStringCellValue());
                CellStyle cellStyle = cell.getCellStyle();
                setBorder(createCell, cellStyle);
                int columnWidth = templateSheet.getColumnWidth(columnIndex);
                setColumnWidth(columnIndex, columnWidth);
                columnIndex++;
                if(columnIndex == 9){
                    for (int i = 9; i < 9 + taxSize; i++) {
                        Cell newCell = createRow.createCell(columnIndex);
                        setBorder(newCell, cellStyle);
                        setColumnWidth(columnIndex, columnWidth);
                        columnIndex++;
                    }
                }
            }
        }
        Row row4 = getRow(3);
        for (int i = 0; i < taxSize; i++) {
            row4.getCell(i+8).setCellValue(taxDicts.get(i).getCodeVal());
        }
        row4.getCell(taxSize+8).setCellValue("小计");
        //合并单元格
        for (int i = 0; i < 16 + taxSize; i++) {
            mergedCell(1, 3, i, i);
            if (i == 7) {
            	i = 12 + taxSize;
            }
        }
        mergedCell(1, 1, 8, taxSize + 12);
        mergedCell(2, 2, 8, taxSize + 8);
        for (int i = taxSize + 9; i < taxSize + 13; i++) {
            mergedCell(2, 3, i, i);
        }
        
        workbook.removeSheetAt(0);
        //保存到临时文件文件
        FileOutputStream os = new FileOutputStream(template);
        workbook.write(os);
        os.close();
        return template;
    }

    public List<InvsInspectation> findAllInspects(){
        List<InvsInspectation> inspects = new ArrayList<InvsInspectation>();
        
        return inspects;
    }
    
    public List<Dict> findTaxDicts(List<InspectVo> inspects){
        List<Long> inspectIds = new ArrayList<Long>();
        for (int i = 0; i < inspects.size(); i++) {
            inspectIds.add(inspects.get(i).getId());
        }
        
        StringBuffer jpql =  new StringBuffer();
        jpql.append("select distinct re.taxType from InvsInspectationResult re");
        jpql.append(" where re.invsInspectation.id in ?1");
        List<String> distinctTaxTypes = em.createQuery(jpql.toString()).setParameter(1, inspectIds).getResultList();
        List<Dict> allTaxDicts = findDictsByTax();
        List<Dict> taxDicts = new ArrayList<Dict>();
        boolean contains;
        for (int i = 0; i < allTaxDicts.size(); i++) {
            Dict dict = allTaxDicts.get(i);
            contains = distinctTaxTypes.contains(dict.getCodeCat()+"."+dict.getCodeKey());
            if(contains){
                taxDicts.add(dict);
            }
        }
        return taxDicts;
    }
    
    public List<Dict> findDictsByTax(){
        String jpql = "select d from Dict d where d.codeCat = ?1 order by d.id";
        return em.createQuery(jpql).setParameter(1, DictConsts.TIH_TAX_TYPE).getResultList();
    }
    
    public Double sumByTaxType(Long inspectId,String taxType) {
        try {
            StringBuffer jpql =  new StringBuffer();
            jpql.append("select sum(re.overdueTax) from InvsInspectationResult re");
            jpql.append(" where re.invsInspectation.id = "+inspectId);
            jpql.append(" group by re.taxType having re.taxType = '"+taxType+"'");
            List resultList = em.createQuery(jpql.toString()).getResultList();
            if(resultList!=null && resultList.size()>0){
                return ((BigDecimal)resultList.get(0)).doubleValue();
            }else{
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static void main(String[] args) {
        
    }
}
