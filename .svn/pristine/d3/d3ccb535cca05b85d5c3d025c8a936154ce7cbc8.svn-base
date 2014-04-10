package com.wcs.tih.filenet.common.service;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.UserContext;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;

/**
 * @features FileNet CE连接接口实现
 * @ClassName: DefaultCEConnectionManager
 * @date Jul 24, 2011 4:54:44 AM
 * 
 */
public class DefaultConnectionManager implements ConnectionManager {

	public DefaultConnectionManager(){}
	
	/**
	 * @features: 获取FileNet CE连接接口
	 * @param @param user 用户名称
	 * @param @param password 用户密码
	 * @param @return
	 * @param @throws Exception
	 * @return 回归FileNet CE连接
	 * @throws Exception
	 * @see getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String user, String password) throws EngineRuntimeException {
		Connection connection = null;
		String uri = ConfigManager.getConfigValue("ce.uri");
		String stanza = ConfigManager.getConfigValue("ce.stanza");
		connection = Factory.Connection.getConnection(uri);
		Subject subject = UserContext.createSubject(connection, user, password, stanza);
		UserContext userContext = UserContext.get();
		userContext.pushSubject(subject);
		return connection;
	}

	/**
	 * 
	 */
	public VWSession getvwSession(String user, String password) throws VWException {
	    return new VWSession();
	}

	/**
	 * @features: 释放FileNet CE连接接口
	 * @param @param FileNet CE连接
	 * @throws Exception
	 */
	public void releaseConnection(Connection connection) throws Exception {
		UserContext.get().popSubject();
	}
}
