package com.wcs.tih.filenet.helper.ce.util;

import java.io.File;
import java.net.URL;
import java.util.MissingResourceException;
import org.apache.log4j.Logger;

public class CeConfigUtils {
	private static Logger logger = Logger.getLogger(CeConfigUtils.class);

	public static void config() {
		logger.debug("Configuration file path:");
		try {
			String waspLocation = CeConfigOptions.getWaspLocation();
			if ((waspLocation != null) && (!(waspLocation.equals("")))) {
				URL waspLocationURL = UserContextUtils.class.getResource(waspLocation);
				String waspLocationPath = waspLocationURL.getPath();

				logger.debug("Use WebService transport.");
				logger.debug("wasp config file = " + waspLocationPath);
				System.setProperty("wasp.location", waspLocationPath);
			}
		} catch (MissingResourceException localMissingResourceException) {
		}

		String jaasConfigFile = CeConfigOptions.getJaasConfigFile();
		URL jaasConfigURL = UserContextUtils.class.getResource(jaasConfigFile);
		String jaasConfigFilePath = jaasConfigURL.getPath();
		if (logger.isDebugEnabled())
			logger.debug("jaas config file = " + jaasConfigFilePath);
		System.setProperty("java.security.auth.login.config", jaasConfigFilePath);

		String ceUrl = CeConfigOptions.getContentEngineUrl();
		System.setProperty("filenet.pe.bootstrap.ceuri", ceUrl);

		logger.debug("java.security.auth.login.config = " + System.getProperty("java.security.auth.login.config"));
		logger.debug("wasp.location = " + System.getProperty("wasp.location"));
		logger.debug("filenet.pe.bootstrap.ceuri = " + System.getProperty("filenet.pe.bootstrap.ceuri"));
	}

	public static String toFilePath(URL url) {
		if ((url == null) || (!(url.getProtocol().equals("file")))) {
			return null;
		}
		String filename = url.getFile().replace('/', File.separatorChar);
		int position = 0;
		while ((position = filename.indexOf(37, position)) >= 0) {
			if (position + 2 < filename.length()) {
				String hexStr = filename.substring(position + 1, position + 3);
				char ch = (char) Integer.parseInt(hexStr, 16);
				filename = filename.substring(0, position) + ch + filename.substring(position + 3);
			}
		}
		return new File(filename).getPath();
	}
}