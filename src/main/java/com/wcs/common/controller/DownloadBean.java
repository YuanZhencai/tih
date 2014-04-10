package com.wcs.common.controller;

import java.io.InputStream;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.wcs.common.service.DownloadService;
import com.wcs.tih.filenet.helper.ce.util.MimeTypeMap;

@ManagedBean(name = "downloadBean")
@ApplicationScoped
public class DownloadBean {

	@EJB
	private DownloadService downloadService;

	public StreamedContent download(String fileId) {
		Map<String, InputStream> fileMap = this.downloadService.download(fileId);
		for (String fileName : fileMap.keySet()) {
			return new DefaultStreamedContent((InputStream) fileMap.get(fileName), MimeTypeMap.getMimetypeByFileName(fileName), fileName);
		}
		return null;
	}
}