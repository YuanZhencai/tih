package com.wcs.tih.filenet.helper.ce.util;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;

public class ConnectionUtils {
	public static Connection getConnection() {
		String ceUrl = CeConfigOptions.getContentEngineUrl();
		return Factory.Connection.getConnection(ceUrl);
	}
}