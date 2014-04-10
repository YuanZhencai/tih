package com.wcs.tih.system.service;

import java.util.List;

import javax.ejb.Local;

import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.tih.system.controller.vo.WfSupervisorVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 组织层级Service接口类</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Local
public interface OrganizationLevelInterface {
    
	/**
	 * <p>Description: 通过用户帐号取得该用户的所以上级主管</p>
	 * @param adAccount 用户账号
	 * @return
	 */
	public List<WfSupervisorVo> getAllWfSupervisorVo(String adAccount);

	/**
	 * <p>Description: 保存组织层级 及保存用户的上级主管</p>
	 * @param wfSupervisorVoList 用户申请单上级主管集合
	 * @return
	 */
	public boolean saveOrganizationLevel(String selectedAdAccount,List<WfSupervisorVo> wfSupervisorVoList);

	/**
	 * <p>Description: 取得用户的上级主管</p>
	 * @param adAccount 用户帐号
	 * @param requestFormType 申请单类型
	 * @param companyId 公司ID
	 * @return 上级主管的用户VO对象 没有返回null
	 */
	public UsermstrVo getUsermstrVo(String adAccount, String requestFormType, Long companyId);
}
