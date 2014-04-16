package com.wcs.common.controller;

import java.io.InputStream;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.service.DownloadService;
import com.wcs.tih.filenet.helper.ce.util.MimeTypeMap;
import com.wcs.tih.report.controller.FinancialReportBean;

@ManagedBean(name = "downloadBean")
@ApplicationScoped
public class DownloadBean {
	private static Logger logger = LoggerFactory.getLogger(FinancialReportBean.class);

	@EJB
	private DownloadService downloadService;

	public StreamedContent download(String fileId) {
		Map<String, InputStream> fileMap = this.downloadService.download(fileId);
		try {
			for (String fileName : fileMap.keySet()) {
				return new DefaultStreamedContent((InputStream) fileMap.get(fileName), MimeTypeMap.getMimetypeByFileName(fileName), new String(fileName.getBytes("utf-8"),"iso8859-1"));
			}
		} catch (Exception e) {
			logger.error("下载失败", e);
		}
		return null;
	}
}