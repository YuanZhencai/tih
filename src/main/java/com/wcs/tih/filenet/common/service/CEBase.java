package com.wcs.tih.filenet.common.service;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;

//@Stateless
public final class CEBase {

	private static Log logger = LogFactory.getLog(CEBase.class.getName());
	private static CEBase cebase = null;

	private CEBase() {
	}

	public static CEBase getInstance() {
		if (cebase == null) {
			cebase = new CEBase();
		}
		return cebase;
	}

	public static ObjectStore getOS(String uri, String username, String password, String objStoreName) {
		ObjectStore os = null;
		Connection conn = getconnection(username, password);
		Domain domain = Factory.Domain.fetchInstance(conn, null, null);
		os = Factory.ObjectStore.fetchInstance(domain, objStoreName, null);
		return os;
	}

	private static Connection getconnection(String username, String password) {
		Connection conn = null;
		try {
			conn = ServiceFactory.getConnectionManager().getConnection(username, password);
		} catch (Exception e) {
			logger.error("connection ce fail!", e);
		}
		return conn;
	}

}
