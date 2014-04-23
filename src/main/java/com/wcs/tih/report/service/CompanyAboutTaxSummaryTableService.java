package com.wcs.tih.report.service;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.EntityService;
import com.wcs.base.service.LoginService;
import com.wcs.base.util.JSFUtils;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.model.CompanyAnnualTaxPay;
import com.wcs.tih.model.CompanyDepreciation;
import com.wcs.tih.model.CompanyMainAsset;
import com.wcs.tih.model.CompanyMaterial;
import com.wcs.tih.model.CompanyStockStructure;
import com.wcs.tih.model.CompanyTaxIncentive;
import com.wcs.tih.model.CompanyTaxTypeRatio;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.model.TaxauthorityCompanymstr;
import com.wcs.tih.report.controller.helper.CompanyAbouttaxExpExcel;
import com.wcs.tih.report.controller.vo.CompanyBasicInfoVo;
import com.wcs.tih.report.controller.vo.CompanyTaxIncentiveVo;
import com.wcs.tih.report.controller.vo.CompanyTaxRatioVo;
import com.wcs.tih.report.controller.vo.StockStructureVo;
import com.wcs.tih.report.controller.vo.TaxRatioVo;
import com.wcs.tih.system.controller.vo.TaxIncentiveVo;
import com.wcs.tih.util.DataUtil;

@Stateless
public class CompanyAboutTaxSummaryTableService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;
    @EJB
    private CommonService commonService;
    @EJB
    private FileNetUploadDownload fileUpService;
    @EJB
    private EntityService entityService;
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static ResourceBundle filenetProperties = ResourceBundle.getBundle("filenet");
    private static int decimal = 2;

    /**
     * <p>Description: 取得公司</p>
     * @param name 公司名
     * @return
     */
    public List<CompanyManagerModel> getCompanyByName(List<Long> queryCompanys) {
        String join = "";
        List<CompanyManagerModel> list = new ArrayList<CompanyManagerModel>();
        StringBuilder sb = new StringBuilder();
        sb.append("select o.STEXT,COMPANYMSTR.ADDRESS,COMPANYMSTR.ZIPCODE,COMPANYMSTR.TELPHONE,COMPANYMSTR.DEFUNCT_IND,COMPANYMSTR.id,o.BUKRS,COMPANYMSTR.type,COMPANYMSTR.desc,o.id as oid ,COMPANYMSTR.START_DATETIME,COMPANYMSTR.SETUP_DATETIME,COMPANYMSTR.id,COMPANYMSTR.REGION,COMPANYMSTR.PROVINCE,COMPANYMSTR.CODE,lower(COMPANYMSTR.CODE),COMPANYMSTR.REPRESENTATIVE from COMPANYMSTR INNER JOIN O on O.id=COMPANYMSTR.oid " + join + " where 1=1");
        if(queryCompanys !=null && queryCompanys.size() > 0){
            String companyIds = "";
            for (int i = 0; i < queryCompanys.size(); i++) {
                companyIds = companyIds + queryCompanys.get(i)+(i==queryCompanys.size() -1?"":",");
            }
            sb.append(" and COMPANYMSTR.id in ("+ companyIds +")");
        }
        sb.append(" and COMPANYMSTR.DEFUNCT_IND = 'N'");
        sb.append(" order by lower(COMPANYMSTR.CODE)");
        List li = this.em.createNativeQuery(sb.toString()).getResultList();
        for (int i = 0; i < li.size(); i++) {
            Object[] result = (Object[]) li.get(i);
            CompanyManagerModel model = new CompanyManagerModel();
            model.setStext(result[0] == null ? "" : result[0].toString());
            model.setAddress(result[1] == null ? "" : result[1].toString().trim());
            model.setZipcode(result[2] == null ? "" : result[2].toString());
            model.setTelphone(result[3] == null ? "" : result[3].toString());
            model.setDefuctInt(result[4] == null ? "" : result[4].toString());
            model.setId(Long.valueOf(result[5].toString()));
            model.setJgCode(result[6] == null ? "" : result[6].toString());
            model.setType(result[7] == null ? "" : result[7].toString());
            model.setDesc(result[8] == null ? "" : result[8].toString());
            model.setOid(result[9].toString());
            model.setStartDatetime(result[10] == null ? null : (Date) result[10]);
            model.setStepDatetime(result[11] == null ? null : (Date) result[11]);
            model.setId(result[12] == null ? null : (Long) result[12]);
            model.setRegion(result[13] == null ? "" : (String) result[13]);
            model.setProvince(result[14] == null ? "" : (String) result[14]);
            model.setCode(result[15] == null ? "" : (String) result[15]);
            model.setLowererCode(result[16] == null ? "" : (String) result[16]);
            model.setRepresentative(result[17] == null ? "" : (String) result[17]);
            list.add(model);
        }
        return list;
        
    }

    /**
     * <p>Description: 公司涉税信息汇总</p>
     * @param selectedCompanyManagerModels 选中的公司
     * @param lang 浏览器语言
     * @throws Exception
     */
    public void companyAboutTaxSummary(CompanyManagerModel[] selectedCompanyManagerModels, Date annual) throws Exception {
        if (null != selectedCompanyManagerModels && selectedCompanyManagerModels.length != 0) {
            List<CompanyBasicInfoVo> cbvs = getCompanyBasicInfo(selectedCompanyManagerModels);
            List<CompanyTaxRatioVo> ctrvs = getCompanyTaxRatioInfo(selectedCompanyManagerModels, annual);
            List<CompanyTaxIncentiveVo> cttvs = getCompanyTaxIncentiveInfo(selectedCompanyManagerModels);
            try {
                String reportName = this.commonService.getValueByDictCatKey(DictConsts.TIH_TAX_REPORT_1, JSFUtils.getLanguage());
                String fileName = (reportName != null ? reportName : "公司涉税信息汇总") + "-" + sdf.format(new Date());
                InputStream inputStream = CompanyAbouttaxExpExcel.taxSummary(cbvs, ctrvs, cttvs);
                com.filenet.api.core.Document document = fileUpService.upLoadDocumentCheckIn(inputStream, new HashMap<String, Object>(), fileName + ".xls", filenetProperties.getString("ce.document.classid"), filenetProperties.getString("ce.folder.reportSummary.aboutTax"));
                inputStream.close();
                ReportSummaryHistory rsh = new ReportSummaryHistory();
                rsh.setReportType(DictConsts.TIH_TAX_REPORT_1);
                rsh.setSummaryDatetime(new Date());
                rsh.setName(fileName);
                rsh.setFileId(document.get_Id().toString());
                rsh.setDefunctInd("N");
                rsh.setCreatedBy(this.loginService.getCurrentUserName());
                rsh.setCreatedDatetime(new Date());
                rsh.setUpdatedBy(this.loginService.getCurrentUserName());
                rsh.setUpdatedDatetime(new Date());
                this.em.persist(rsh);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new Exception("生成Excel失败,请重新生成!");
            }
        }
    }

    /**
     * <p>Description: 取得公司的基本信息</p>
     * @param selectedCompanyManagerModels
     * @param lang
     * @return
     */
    private List<CompanyBasicInfoVo> getCompanyBasicInfo(CompanyManagerModel[] selectedCompanyManagerModels) {
    	String lang = JSFUtils.getLanguage();
        if (null != selectedCompanyManagerModels && selectedCompanyManagerModels.length != 0) {
            HashMap<String, Integer> processDictMap = new HashMap<String, Integer>();
            List<Dict> processDicts = commonService.getDictByCat(DictConsts.TIH_TAX_PROCESSING, lang);
            for (int i = 0; i < 8; i++) {
                Dict dict = processDicts.get(i);
                processDictMap.put(dict.getCodeVal(), i);
            }
            
            List<CompanyBasicInfoVo> cbvs = new ArrayList<CompanyBasicInfoVo>();
            CompanyBasicInfoVo cbv;
            List<TaxauthorityCompanymstr> tcs;// 税务机关
            List<CompanyStockStructure> csss;// 股权结构
            List<CompanyMainAsset> cmas;// 主要资产
            List<CompanyMaterial> cms;// 加工能力
            for (CompanyManagerModel cmm : selectedCompanyManagerModels) {
                // 设置公司基本信息
                cbv = new CompanyBasicInfoVo();
                cbv.setCompanyName(cmm.getStext());
                cbv.setCompanyAddress(cmm.getAddress());
                cbv.setRepresentative(cmm.getRepresentative());
                cbv.setSetUpDatetime(null != cmm.getStepDatetime() ? df.format(cmm.getStepDatetime()) : "");
                cbv.setStartDatetime(null != cmm.getStartDatetime() ? df.format(cmm.getStartDatetime()) : "");
                cbv.setManageArea(cmm.getDesc());
                // 取得投资总额
                cbv.setInvestTotal(getCompanyInvestment(cmm.getId()));
                // 设置公司税务机关
                tcs = getTaxauthorityCompanymstr(cmm.getId());
                if (null != tcs && tcs.size() != 0) {
                    for (TaxauthorityCompanymstr tc : tcs) {
                        if (null != tc.getTaxauthority() && DictConsts.TIH_TAX_AUTHORITY_TYPE_1.equals(tc.getTaxauthority().getType())) {
                            cbv.setNationalTax(commonService.getValueByDictCatKey(tc.getTaxauthority().getType(), lang));
                            cbv.setNationalTaxpayerIdentifier(tc.getTaxpayerIdentifier());
                        } else if (null != tc.getTaxauthority() && DictConsts.TIH_TAX_AUTHORITY_TYPE_2.equals(tc.getTaxauthority().getType())) {
                            cbv.setLandTax(commonService.getValueByDictCatKey(tc.getTaxauthority().getType(), lang));
                            cbv.setLandTaxTaxpayerIdentifier(tc.getTaxpayerIdentifier());
                        }
                    }
                }
                // 设置公司股权结构
                csss = getCompanyStockStructure(cmm.getId());
                List<StockStructureVo> ssvs = new ArrayList<StockStructureVo>();
                if (null != csss && csss.size() != 0) {
                    StockStructureVo ssv;
                    for (CompanyStockStructure css : csss) {
                        ssv = new StockStructureVo();
                        ssv.setId(css.getId());
                        ssv.setShareholder(css.getShareholder());
                        ssv.setRegisteredCapital(null != css.getRegisteredCapital() && css.getRegisteredCapital().doubleValue() != 0 ? (DataUtil.dataFormat(css.getRegisteredCapital().doubleValue(), decimal) + commonService.getValueByDictCatKey(css.getCurrency(),lang)) : "");
                        ssv.setRatio(css.getRatio() != 0 ? (DataUtil.dataFormat(css.getRatio(), decimal) + "%") : "");
                        ssvs.add(ssv);
                    }
                }
                cbv.setSsvs(ssvs);
                // 设置主要资产
                cmas = getCompanyMainAsset(cmm.getId());
                if (null != cmas && cmas.size() != 0) {
                    List<CompanyDepreciation> cds;
                    double cost = 0d;
                    String costStr;
                    for (CompanyMainAsset cma : cmas) {
                        cds = cma.getCompanyDepreciations();
                        cost = 0d;
                        if (null != cds && cds.size() != 0) {
                            for (CompanyDepreciation cd : cds) {
                                if (!"Y".equals(cd.getDefunctInd())) {
                                    cost += cd.getCost().doubleValue();
                                }
                            }
                        }
                        costStr = cost != 0 ? DataUtil.dataFormat(cost / 10000, decimal) : "";
                        if (DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM_1.equals(cma.getItem())) {
                            cbv.setMainAssetsCast1(costStr);
                        } else if (DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM_2.equals(cma.getItem())) {
                            cbv.setMainAssetsCast2(costStr);
                        } else if (DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM_3.equals(cma.getItem())) {
                            cbv.setMainAssetsCast3(costStr);
                        } else if (DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM_4.equals(cma.getItem())) {
                            cbv.setMainAssetsCast4(costStr);
                        } else if (DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM_5.equals(cma.getItem())) {
                            cbv.setMainAssetsCast5(costStr);
                        } else if (DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM_6.equals(cma.getItem())) {
                            cbv.setMainAssetsCast6(costStr);
                        }
                    }
                }
                // 设置加工能力
                cms = getCompanyMaterial(cmm.getId());
                
                HashMap<Integer, List<CompanyMaterial>> tmpProcessMap = new HashMap<Integer, List<CompanyMaterial>>();
                for (CompanyMaterial cm : cms) {
                    Integer processIndex = processDictMap.get(cm.getProcessing());
                    processIndex = processIndex == null ? 8 : processIndex;
                    List<CompanyMaterial> list = tmpProcessMap.get(processIndex);
                    list = list == null ? new ArrayList<CompanyMaterial>(): list;
                    list.add(cm);
                    tmpProcessMap.put(processIndex, list);
                }
                cbv.setProcessMap(tmpProcessMap);
                cbvs.add(cbv);
            }
            return cbvs;
        }
        return null;
    }
    
    /**
     * <p>Description: 取得公司的税种税率信息</p>
     * @param selectedCompanyManagerModels
     * @param lang
     * @return
     */
    private List<CompanyTaxRatioVo> getCompanyTaxRatioInfo(CompanyManagerModel[] selectedCompanyManagerModels, Date annual) {
    	
    	String lang =JSFUtils.getLanguage();
        if (null != selectedCompanyManagerModels && selectedCompanyManagerModels.length != 0) {
            List<CompanyTaxRatioVo> ctrvs = new ArrayList<CompanyTaxRatioVo>();
            CompanyTaxRatioVo ctrv;
            List<CompanyTaxTypeRatio> cttrs;// 税种税率
            for (CompanyManagerModel cmm : selectedCompanyManagerModels) {
                // 设置税种税率信息
                ctrv = new CompanyTaxRatioVo();
                ctrv.setId(cmm.getId());
                ctrv.setCompanyName(cmm.getStext());
                ctrv.setRepresentative(cmm.getRepresentative());
                cttrs = this.getCompanyTaxTypeRatio(cmm.getId());
                if (null != cttrs && cttrs.size() != 0) {
                    List<TaxRatioVo> trvs = new ArrayList<TaxRatioVo>();
                    TaxRatioVo trv;
                    for (CompanyTaxTypeRatio cttr : cttrs) {
                        trv = new TaxRatioVo();
                        trv.setId(cttr.getId());
                        trv.setTaxType(commonService.getValueByDictCatKey(cttr.getTaxType(), lang));
                        trv.setTaxBasis(cttr.getTaxBasis());
                        // 设置税率 税率取当前年度上年的税率
                        List<CompanyAnnualTaxPay> catps = cttr.getCompanyAnnualTaxPays();
                        if (null != catps && catps.size() != 0) {
                            for (CompanyAnnualTaxPay catp : catps) {
                                if (!"Y".equals(catp.getDefunctInd())) {
                                	// 取得当权年度
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(annual);
                                    int currentYear = cal.get(Calendar.YEAR);
                                    if(catp.getTaxPayYear() != null) {
                                    	cal.setTime(catp.getTaxPayYear());
                                    	int taxPayYear = cal.get(Calendar.YEAR);
                                    	if (taxPayYear == (currentYear - 1)) {
                                    		trv.setTaxRate(catp.getNewTaxRate());
                                    		trv.setTaxPayAccount(catp.getTaxPayAccount() + "");
                                    		break;
                                    	}
                                    }
                                }
                            }
                        }
                        trv.setReportFrequency(commonService.getValueByDictCatKey(cttr.getReportFrequency(), lang));
                        trvs.add(trv);
                    }
                    ctrv.setTrvs(trvs);
                }
                ctrvs.add(ctrv);
            }
            return ctrvs;
        }
        return null;
    }

    /**
     * <p>Description: 取得公司的税收优惠信息</p>
     * @param selectedCompanyManagerModels
     * @param lang
     * @return
     */
    private List<CompanyTaxIncentiveVo> getCompanyTaxIncentiveInfo(CompanyManagerModel[] selectedCompanyManagerModels) {
        if (null != selectedCompanyManagerModels && selectedCompanyManagerModels.length != 0) {
            List<CompanyTaxIncentiveVo> cttvs = new ArrayList<CompanyTaxIncentiveVo>();
            CompanyTaxIncentiveVo cttv;
            List<CompanyTaxIncentive> ctis;// 税收优惠
            for (CompanyManagerModel cmm : selectedCompanyManagerModels) {
                // 设置税收优惠信息
                cttv = new CompanyTaxIncentiveVo();
                cttv.setId(cmm.getId());
                cttv.setCompanyName(cmm.getStext());
                cttv.setRepresentative(cmm.getRepresentative());
                ctis = getCompanyTaxIncentive(cmm.getId());
                if (null != ctis && ctis.size() != 0) {
                    List<TaxIncentiveVo> tivs = new ArrayList<TaxIncentiveVo>();
                    TaxIncentiveVo tiv;
                    for (CompanyTaxIncentive cti : ctis) {
                        tiv = new TaxIncentiveVo();
                        tiv.setId(cti.getId());
                        tiv.setPreferentialItem(cti.getPreferentialItem());
                        tiv.setPreferentialStartDatetime(cti.getPreferentialStartDatetime());
                        tiv.setPreferentialEndDatetime(cti.getPreferentialEndDatetime());
                        tiv.setApprovalOrgan(cti.getApprovalOrgan());
                        tiv.setPolicy(cti.getPolicy());
                        tivs.add(tiv);
                    }
                    cttv.setTtvs(tivs);
                }
                cttvs.add(cttv);
            }
            return cttvs;
        }
        return null;
    }

    private String getCompanyInvestment(Long companyId) {
        String sql = "select sum(ci.investAccount),ci.currency from CompanyInvestment ci where ci.defunctInd <> 'Y' and ci.companymstr.defunctInd <> 'Y' and ci.companymstr.id = " + companyId +" group by ci.currency";
        List resultList = this.em.createQuery(sql).getResultList();
        String investmentTotal = "";
        for (int i = 0; i < resultList.size(); i++) {
            Object[] row = (Object[])resultList.get(i);
            investmentTotal = investmentTotal + row[0].toString()+"万"+commonService.getValueByDictCatKey(row[1].toString(),"zh_CN") + (i==resultList.size()-1?"":",");
        }
        return investmentTotal;
    }

    private List<TaxauthorityCompanymstr> getTaxauthorityCompanymstr(Long companyId) {
        String sql = "select tc from TaxauthorityCompanymstr tc where tc.defunctInd <> 'Y' and tc.companymstr.defunctInd <> 'Y' and tc.taxauthority.defunctInd <> 'Y' and tc.companymstr.id = " + companyId;
        return this.em.createQuery(sql).getResultList();
    }

    private List<CompanyStockStructure> getCompanyStockStructure(Long companyId) {
        String sql = "select css from CompanyStockStructure css where css.defunctInd <> 'Y' and css.companymstr.defunctInd <> 'Y' and css.companymstr.id = " + companyId;
        sql += " and css.statisticsDatetime = (select max(ss.statisticsDatetime) from CompanyStockStructure ss where ss.defunctInd <> 'Y' and ss.companymstr.defunctInd <> 'Y' and ss.companymstr.id = " + companyId+")"; 
        return this.em.createQuery(sql).getResultList();
    }

    private List<CompanyMainAsset> getCompanyMainAsset(Long companyId) {
        String sql = "select cma from CompanyMainAsset cma where cma.defunctInd <> 'Y' and cma.companymstr.defunctInd <> 'Y' and cma.companymstr.id = " + companyId;
        return this.em.createQuery(sql).getResultList();
    }

    private List<CompanyMaterial> getCompanyMaterial(Long companyId) {
        String sql = "select cm from CompanyMaterial cm where cm.defunctInd <> 'Y' and cm.companymstr.defunctInd <> 'Y' and cm.companymstr.id = " + companyId;
        return this.em.createQuery(sql).getResultList();
    }

    private List<CompanyTaxTypeRatio> getCompanyTaxTypeRatio(Long companyId) {
        String sql = "select cttr from CompanyTaxTypeRatio cttr where cttr.defunctInd <> 'Y' and cttr.companymstr.defunctInd <> 'Y' and cttr.companymstr.id = " + companyId;
        return this.em.createQuery(sql).getResultList();
    }

    private List<CompanyTaxIncentive> getCompanyTaxIncentive(Long companyId) {
        String sql = "select cti from CompanyTaxIncentive cti where cti.defunctInd <> 'Y' and cti.companymstr.defunctInd <> 'Y' and cti.companymstr.id = " + companyId;
        return this.em.createQuery(sql).getResultList();
    }

    /**
     * <p>Description: 取得公司涉税信息汇总历史</p>
     * @param reportType
     * @return
     */
    public List<ReportSummaryHistory> getReportSummaryHistory(String reportType) {
        String sql = "select rsh from ReportSummaryHistory rsh where rsh.defunctInd <> 'Y' and rsh.reportType ='" + reportType + "' and rsh.createdBy = '" + this.loginService.getCurrentUserName() + "' order by rsh.summaryDatetime desc";
        return this.em.createQuery(sql).getResultList();
    }

    /**
     * <p>Description: 下载</p>
     * @param documentId
     * @return
     */
    public StreamedContent download(String documentId) {
        try {
            return fileUpService.downloadDocumentEncoding(documentId.trim(), "utf-8", "iso8859-1");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}
