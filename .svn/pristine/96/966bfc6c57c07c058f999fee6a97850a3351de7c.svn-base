package com.wcs.common.service;

import com.wcs.base.service.LoginService;
import com.wcs.tih.filenet.helper.ce.ObjectStoreProvider;
import com.wcs.tih.filenet.helper.ce.util.CeConfigOptions;

import java.io.InputStream;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class DownloadService {
	private static final String USER_PASSWORD = CeConfigOptions.getUserPassword();

	@EJB
	private LoginService loginService;
	@EJB 
	private TDSLocal tdsService;

	public Map<String, InputStream> download(String fileId) {
		ObjectStoreProvider provider = new ObjectStoreProvider(tdsService.addPre(this.loginService.getCurrentUserName()), USER_PASSWORD);
		return provider.getAttachment(fileId);
	}
}