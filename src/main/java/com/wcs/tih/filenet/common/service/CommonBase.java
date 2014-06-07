package com.wcs.tih.filenet.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;


public class CommonBase {
	
	private Log log = LogFactory.getLog(CommonBase.class.getName());
	
	public ObjectStore getOS(String username, String password) throws Exception {
		Connection conn = getconnection(username, password);
		Domain domain;
		String objStoreName = ConfigManager.getConfigValue("ce.os");
		ObjectStore os;
		try {
    		domain = Factory.Domain.fetchInstance(conn, null, null);
    		os = Factory.ObjectStore.fetchInstance(domain, objStoreName, null);
		} catch (Exception e) {
            throw new Exception("数据库不存在");
        }
		return os;
	}

	private Connection getconnection(String username, String password)
	        throws Exception {
		Connection conn = null;
		try {
            conn = ServiceFactory.getConnectionManager().getConnection(username, password);
        } catch (EngineRuntimeException e) {
            if (e.getExceptionCode() == ExceptionCode.E_NOT_AUTHENTICATED) {
              log.error(e.getMessage());
              throw new Exception("用户名/密码不正确，请重新尝试");
          } else if (e.getExceptionCode() == ExceptionCode.API_UNABLE_TO_USE_CONNECTION) {
              log.error(e.getMessage());
              throw new Exception("连接不到服务器，请联系管理员");
          } else {
              log.error(e.getMessage());
              throw new Exception("未知错误");
          }
        } catch (ClassNotFoundException e) {
            throw new Exception("配置不正确.");
        } catch (InstantiationException e) {
            throw new Exception("配置不正确.");
        } catch (IllegalAccessException e) {
            throw new Exception("配置不正确.");
        }
		return conn;
	}
}
