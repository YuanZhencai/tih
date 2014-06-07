package com.wcs.tih.filenet.common.service;

import com.filenet.api.core.Connection;
import com.filenet.api.exception.EngineRuntimeException;

import filenet.vw.api.VWSession;

/** 
 * @features CE连接管理接口 
 * @ClassName: CEConnectionManager 
 * @date Jul 23, 2011 9:16:33 PM 
 *  
 */
public interface ConnectionManager {

	/**
	 * @param password 
	 * 
	* @features: 获取FileNet CE连接接口
	* @param @param user 用户名称
	* @param @param password 用户密码
	* @param @return
	* @param @throws Exception
	* @return 回归FileNet CE连接  
	* @throws
	 */
	public Connection getConnection(String user, String password) throws EngineRuntimeException;

	/**
	 * 
	* @features: 数开FileNet CE连接接口
	* @param @param FileNet CE连接
	* @param @throws Exception
	* @return void  
	* @throws
	 */
	public void releaseConnection(Connection connection) throws Exception;
	/**
	 * 
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	
	public VWSession getvwSession(String user, String password)throws Exception;


}
