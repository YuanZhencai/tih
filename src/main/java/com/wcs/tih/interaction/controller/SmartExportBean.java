/** * SmartExportBean.java 
 * Created on 2014年1月17日 下午5:22:42 
 */

package com.wcs.tih.interaction.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.base.util.JSFUtils;
import com.wcs.tih.filenet.model.DocVo;
import com.wcs.tih.interaction.service.SmartImportService;

/** 
* <p>Project: tih</p> 
* <p>Title: SmartExportBean.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@ManagedBean(name = "smartExportBean")
@SessionScoped
public class SmartExportBean {
	private Logger logger = LoggerFactory.getLogger(SmartExportBean.class.getName());

	@EJB
	private SmartImportService smartImportService;

	@EJB
	private LoginService loginService;
	private Future<String> future;
	private List<DocVo> compressFiles;
	private Map<String, Object> filters;
	
	public void searchCompressFiles() {
		compressFiles = this.smartImportService.findCompressFiles(filters);
	}

	public void export() {
		if (future == null) {
			future = smartImportService.compressSmarts(JSFUtils.getRealPath(), loginService.getCurrentUserName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "开始导出：", "请耐心等待"));
		} else {
			if (future.isDone()) {
				future = smartImportService.compressSmarts(JSFUtils.getRealPath(), loginService.getCurrentUserName());
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "正在导出：", "请不要重复操作"));
			}
		}
	}

	public void getExportStatus() {
		String status = "";
		try {
			if (future != null && future.isDone()) {
				status = "1";
				future = null;
				searchCompressFiles();
			}
		} catch (Exception e) {
			status = "0";
			future = null;
			logger.error("精灵导出失败", e);
		}
		logger.info("[status]" + status);
		RequestContext.getCurrentInstance().addCallbackParam("status", status);
		status = "";
	}


	public List<DocVo> getCompressFiles() {
		return compressFiles;
	}


	public void setCompressFiles(List<DocVo> compressFiles) {
		this.compressFiles = compressFiles;
	}


	public Map<String, Object> getFilters() {
		return filters;
	}


	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}
	
	
}
