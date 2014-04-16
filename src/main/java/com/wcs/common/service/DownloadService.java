package com.wcs.common.service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.tih.filenet.helper.ce.ObjectStoreProvider;
import com.wcs.tih.filenet.helper.ce.util.CeConfigOptions;
import com.wcs.tih.filenet.helper.ce.util.MimeTypeMap;

@Stateless
public class DownloadService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(DownloadService.class);
	private static final String USER_PASSWORD = CeConfigOptions.getUserPassword();

	@EJB
	private LoginService loginService;
	@EJB
	private TDSLocal tdsService;

	public StreamedContent download(String fileId) {
		ObjectStoreProvider provider = new ObjectStoreProvider(tdsService.addPre(this.loginService.getCurrentUserName()), USER_PASSWORD);
		Map<String, InputStream> fileMap = provider.getAttachment(fileId);
		try {
			for (String fileName : fileMap.keySet()) {
				return new DefaultStreamedContent((InputStream) fileMap.get(fileName), MimeTypeMap.getMimetypeByFileName(fileName), new String(fileName.getBytes("utf-8"), "iso8859-1"));
			}
		} catch (Exception e) {
			logger.error("下载失败", e);
		}
		return null;
	}
}