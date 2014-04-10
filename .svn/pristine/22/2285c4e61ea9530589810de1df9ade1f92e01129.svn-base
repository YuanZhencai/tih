package com.wcs.tih.filenet.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @ClassName: ServiceFactory
 * @Description: TODO
 * @author huzhanggen
 * @date Jul 23, 2011 9:15:21 PM
 * 
 */
public final class ServiceFactory {

	@SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(ServiceFactory.class);

	private static final String CONNECTION_MANAGER_CLASSNAME = "factory.className.ConnectionManager";
	private static ConnectionManager connectionManager;
	@SuppressWarnings("rawtypes")
    private static Class classConnectionManager;
	
	private ServiceFactory() {
    }

	/**
	 * @return 回归CE连接
	 */
	public static ConnectionManager getConnectionManager() throws ClassNotFoundException,InstantiationException,IllegalAccessException {
	    classConnectionManager = Class.forName(ConfigManager.getConfigValue(CONNECTION_MANAGER_CLASSNAME));
        connectionManager = (ConnectionManager) classConnectionManager.newInstance();
		return connectionManager;
	}

}
