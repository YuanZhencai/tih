/** * FinancialReportBean.java 
* Created on 2014年4月15日 下午3:09:42 
*/

package com.wcs.tih.report.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.report.service.FinancialReportService;

/** 
 * <p>Project: tih</p> 
 * <p>Title: FinancialReportBean.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@ManagedBean
@ViewScoped
public class FinancialReportBean {

	private static Logger logger = LoggerFactory.getLogger(FinancialReportBean.class);
	
	@EJB
	private FinancialReportService financialReportService;
	
	public FinancialReportBean() {
		logger.info("FinancialReportBean.FinancialReportBean()");
	}
	
	public void init() {
		logger.info("FinancialReportBean.init()");
	}
	
}
