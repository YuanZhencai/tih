package com.wcs.tih.filenet.common.service;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassName: ConfigManager
 * @Description: TODO
 * @author huzhanggen
 * @date Jul 19, 2011 18:15:56 PM
 */
public final class ConfigManager {

	private static Log logger = LogFactory.getLog(ConfigManager.class.getName());
	private static final String CONFIG_FILE = "/filenet.properties";
	private static final String LOGIN_CONFIG = "java.security.auth.login.config";
	private static final String WSAP_LOCATION = "wasp.location";
	private static Properties configProps = new Properties();
	private ConfigManager() {
    }

	static {
		try {
			configProps.load(ConfigManager.class.getResourceAsStream(CONFIG_FILE));
			String loginConfigFile = configProps.getProperty(ConfigManager.LOGIN_CONFIG);
			String waspLocation = configProps.getProperty(ConfigManager.WSAP_LOCATION);
			System.setProperty(ConfigManager.LOGIN_CONFIG, loginConfigFile);
			System.setProperty(ConfigManager.WSAP_LOCATION, waspLocation);
			logger.info("filenet loginConfigFile location:" + loginConfigFile);
			logger.info("filenet waspLocation location:" + waspLocation);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.info("FileNet ConfigManager: Intitializing ConfiguraitonManager error: ", e);
		}
	}

	/**
	 * @Title: getConfigValue
	 * @Description: TODO
	 * @param @param configKey
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getConfigValue(String configKey) {
	    return (String) configProps.getProperty(configKey);
	}
	
	public static void main(String[] args) {
		Properties pros = new Properties();
		try {
			pros.load(ConfigManager.class.getResourceAsStream(CONFIG_FILE));
			logger.info(pros);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
