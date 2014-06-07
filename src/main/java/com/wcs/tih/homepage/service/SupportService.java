/** * SupportService.java 
 * Created on 2014年4月23日 下午2:24:24 
 */

package com.wcs.tih.homepage.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.wcs.base.service.EntityService;
import com.wcs.base.service.LoginService;
import com.wcs.common.model.Usermstr;


/** 
* <p>Project: tih-1.3.4-SNAPSHOT</p> 
* <p>Title: SupportService.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@Stateless
public class SupportService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private LoginService LoginService;
	@EJB
	private EntityService entityService;

	public List<Usermstr> findSupportUsers() {
		StringBuffer jpql = new StringBuffer();
        jpql.append("select ur.usermstr from Userrole ur");
        jpql.append(" where 1=1");
        jpql.append(" and ur.usermstr.defunctInd <> 'Y'");
        jpql.append(" and ur.defunctInd <> 'Y'");
        jpql.append(" and ur.rolemstr.code = ?1");
        List<Usermstr> supportUsers = entityService.createQuery(jpql.toString(), "support").getResultList();
		return supportUsers;
	}

}
