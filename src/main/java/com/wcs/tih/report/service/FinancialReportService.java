/** * FinancialReportService.java 
 * Created on 2014年4月15日 下午3:07:16 
 */

package com.wcs.tih.report.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.EntityService;
import com.wcs.base.service.LoginService;
import com.wcs.base.util.JSFUtils;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyVo;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.service.CompanyService;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.util.JasperUtil;
import com.wcs.scheduler.util.DateUtils;
import com.wcs.tih.filenet.helper.ce.ObjectStoreProvider;
import com.wcs.tih.filenet.helper.ce.util.CeConfigOptions;
import com.wcs.tih.model.CompanyFinancialReturn;
import com.wcs.tih.report.service.summary.SummaryService;

/** 
* <p>Project: tih</p> 
* <p>Title: FinancialReportService.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@Stateless
public class FinancialReportService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(FinancialReportService.class);

	@EJB
	private EntityService entityService;
	@EJB
	private LoginService loginService;
	@EJB
	private CompanyService companyService;
	@EJB
	private TDSLocal tdsService;
	@EJB
	private SummaryService summaryService;

	private static final String TEMPLATE_FOLDER = "/faces/report/excel/jasper/";

	private static final String DOC_CLASS_NAME = CeConfigOptions.getString("ce.document.classid");
	private static final String FINANCIAL_EXPORT_FOLDER = CeConfigOptions.getString("ce.folder.reportSummary.financial");
	private static final String USER_PASSWORD = CeConfigOptions.getUserPassword();

	public List<CompanyVo> findCompanysBy(Map<String, Object> filter) {
		return companyService.findCompanysBy(filter, 0, 0);
	}

	public List<CompanyFinancialReturn> findFinancialsByCompanys(List<CompanyVo> companys) {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select ft from CompanyFinancialReturn fr");
		jpql.append(" where 1= 1");
		jpql.append(" and fr.company.id in ?1");
		return entityService.find(jpql.toString(), companys);
	}

	public void exportFinancials(List<CompanyVo> companys) throws Exception {
		List<CompanyFinancialReturn> fts = findFinancialsByCompanys(companys);
		String templatePath = JSFUtils.getRealPath() + TEMPLATE_FOLDER + "Financial.jasper";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		String name = "财政返还信息汇总" + "-" + sdf.format(new Date());
		File excel = new File(name);
		try {
			// 1. create xls
			String subRrportDir = JSFUtils.getRealPath() + TEMPLATE_FOLDER;
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", subRrportDir);
			
			JasperUtil.createXlsReport(templatePath, fts, new FileOutputStream(excel), parameters);

			// 2. upload xls
			ObjectStoreProvider provider = new ObjectStoreProvider(tdsService.addPre(loginService.getCurrentUserName()), USER_PASSWORD);

			FileInputStream is = new FileInputStream(excel);
			String documentId = provider.createDocument(DOC_CLASS_NAME, name + ".xls", null, FINANCIAL_EXPORT_FOLDER, "xls", is);
			is.close();
			
			// 3. save documentId to db
			summaryService.saveReportSummaryHistory(DictConsts.TIH_TAX_REPORT_7, name, documentId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		} finally {
			if (excel != null && excel.isFile()) {
				excel.delete();
			}
		}
	}

}