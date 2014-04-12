/** * ContactCommonService.java 
* Created on 2014年4月10日 下午3:27:15 
*/

package com.wcs.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.wcs.base.service.EntityService;
import com.wcs.common.controller.vo.ContactVo;

/** 
 * <p>Project: tih</p> 
 * <p>Title: ContactCommonService.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class ContactCommonService {

	@EJB
	private EntityService entityService;
	@EJB
	private ContactService contactService;
	
	
	public List<ContactVo> findContactsAndUsersBy(Map<String, Object> filter) {
		
		return null;
	}
	
	public List<ContactVo> findUsersBy(Map<String, Object> filter, int first, int pageSize) {
		Query query = entityService.createNativeQuery(buildSql(false), filter);
		if(first > 0){
			query = query.setFirstResult(first);
		}
		if(pageSize > 0){
			query = query.setMaxResults(pageSize);
		}
		
		List resultList = query.getResultList();
		return getContactVosBy(resultList);
	}
	
	public List<ContactVo> getContactVosBy(List<Object[]> rows){
		List<ContactVo> contactVos = new ArrayList<ContactVo>();
		
		ContactVo contactVo = null;
		for (Object[] coulums : rows) {
			contactVo = new ContactVo();
			//.....
			//.....
			//.....
			contactVos.add(contactVo);
		}
		
		return contactVos;
		
	}
	
	public Integer findUsersCountBy(Map<String, Object> filter) {
		Long count = (Long) entityService.createNativeQuery(buildSql(true), filter).getSingleResult();
		return Integer.valueOf(String.valueOf(count));
	}
	
	public String buildSql(boolean isCount){
		StringBuffer xsql = new StringBuffer();
		xsql.append(" select");
		if(isCount){
			xsql.append(" count(u.id)");
		} else {
			xsql.append(" distinct u.*,p.*,lower(u.ad_account),c.*");
		}
		xsql.append(" from Usermstr u");
	    xsql.append(" left join cas_usr_p cup on u.ad_account = cup.id");
	    xsql.append(" left join P p on cup.pernr=p.id");
	    xsql.append(" left join vw_org_and_com c on p.orgeh = c.oid");
	    xsql.append(" left join Userpositionorg upo on u.id=upo.usermstr_id and upo.defunct_ind='N'");
	    xsql.append(" left join Positionorg po on po.id=upo.positionorg_id and upo.defunct_ind='N'");
	    xsql.append(" left join Position ps on ps.id=po.position_id and ps.defunct_ind='N'");
	    xsql.append(" left join Userrole ur on u.id=ur.usermstr_id and ur.defunct_ind='N'");
	    xsql.append(" where 1 = 1 ");
	    
	    xsql.append(" /~ and p.nachn like '[username]' ~/ ");
	    xsql.append(" /~ and ps.position like '[position]' ~/ ");
	    xsql.append(" /~ and c.stext like '[company]' ~/ ");
		
		return xsql.toString();
		
	}
}
