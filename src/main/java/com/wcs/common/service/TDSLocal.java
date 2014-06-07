package com.wcs.common.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.wcs.common.controller.vo.UserVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Userrole;

/**
 * <p>Project: tih</p>
 * <p>Description: TDS接口</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Local
public interface TDSLocal {
    /**
     * <p>Description: 给帐号增加前缀</p>
     * @param item  帐号
     * @return
     */
    public String addPre(String item);

    /**
     * <p>Description: 批量给帐号增加前缀</p>
     * @param items
     * @return
     */
    public List<String> addPres(List<String> items);

    /**
     * <p>Description: 移除帐号前缀</p>
     * @param item
     * @return
     */
    public String removePre(String item);

    /**
     * <p>Description: 批量移除帐号前缀</p>
     * @param items
     * @return
     */
    public List<String> removePres(List<String> items);
    
	/**
	 * 同步用户到TDS上
	 * 
	 * @param UsermstrVo
	 *            ： CasUsrP 、P 、Usermstr 信息 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示同步成功 ，2表示同步失败
	 */
	public int synchronousUserToTds(UserVo uv, String action);

	/**
	 * 批量同步用户到TDS上
	 * 
	 * @param UsermstrVo
	 *            ： CasUsrP 、P 、Usermstr 信息 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示同步成功 ，2表示同步失败
	 */
	public int batchSynchronousUserToTds(List<UsermstrVo> uvs, String action);

	/**
	 * 从TDS上取得用户
	 * 
	 * @param String
	 *            param 用户adAccount
	 * @return 用户adAccount
	 */
	public String getUserFromTDS(String param);

	/**
	 * 同步组到TDS上
	 * 
	 * @param Rolemstr
	 *            role 组信息 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加到TDS上成功 ，2表示增加到TDS上失败
	 */
	public int synchronousGroupToTds(Rolemstr role, String action);

	/**
	 * 批量同步组到TDS上
	 * 
	 * @param Rolemstr
	 *            roles 组集合信息 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加到TDS上成功 ，2表示增加到TDS上失败
	 */
	public int batchSynchronousGroupToTds(List<Rolemstr> roles, String action);

	/**
	 * 从TDS上取得组
	 * 
	 * @param String
	 *            param 角色code
	 * @return 角色code
	 */
	public String getGroupFromTDS(String param);

	/**
	 * 同步用户到组
	 * 
	 * @param String
	 *            adAccount, String code 用户的adAccount ，角色的code , String
	 *            action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加成功 ，2表示增加失败
	 */
	public int synchronousUserToGroupToTDS(String adAccount, String code,
			String action);

	/**
	 * 批量同步用户到组
	 * 
	 * @param List
	 *            <Map<String, Object>> adAccountsCodes
	 *            表示用adAccount和角色code和Map对象 的List集合 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加成功 ，2表示增加失败
	 */
	public int batchSynchronousUserToGroupToTDS(List<Map<String, Object>> adAccountsCodes, String action);

	/**
	 * 一键同步到TDS
	 * 
	 * @return int 0表示连接TDS失败 ，1表示增加成功 ，2表示增加失败
	 */
	public String allSynchronous();
	
	public List<Map<String, Object>> getUserRoleCodeMap(List<Userrole> urs);

}
