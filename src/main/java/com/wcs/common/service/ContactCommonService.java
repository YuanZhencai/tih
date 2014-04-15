/** * ContactCommonService.java 
 * Created on 2014年4月10日 下午3:27:15 
 */

package com.wcs.common.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import com.wcs.base.service.EntityService;
import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.util.JasperUtil;

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

	private static final String TEMPLATE_FOLDER = "/faces/report/excel/jasper/";

	public void exportContactsReport(Map<String, Object> filter, OutputStream os) {
		List<ContactVo> contacts = findContactsAndUsersBy(filter);
		String templatePath = JSFUtils.getRealPath() + TEMPLATE_FOLDER + "Contact.jasper";
		JasperUtil.createXlsReport(templatePath, contacts, os, new HashMap<String, Object>());
	}

	public List<ContactVo> findContactsAndUsersBy(Map<String, Object> filter) {
		List<ContactVo> contacts = contactService.findContactsBy(filter, 0, 0);
		List<ContactVo> users = findUsersBy(filter, 0, 0);
		contacts.addAll(users);
		return contacts;
	}

	public List<ContactVo> findUsersBy(Map<String, Object> filter, int first, int pageSize) {
		Query query = entityService.createNativeQueryByMap(buildSql(false), filter);
		if (first > 0) {
			query = query.setFirstResult(first);
		}
		if (pageSize > 0) {
			query = query.setMaxResults(pageSize);
		}

		List resultList = query.getResultList();
		return getContactVosBy(resultList);
	}

	private List<ContactVo> getContactVosBy(List<Object[]> rows) {
		List<ContactVo> contactVos = new ArrayList<ContactVo>();

		ContactVo contactVo = null;
		for (Object[] coulums : rows) {
			contactVo = new ContactVo();
			contactVo.setId(coulums[0] == null ? 0 : (Long) coulums[0]);
			contactVo.setCompany(coulums[31] == null ? "" : (coulums[31].toString()));
			contactVo.setDefunctInd(coulums[8] == null ? "" : (coulums[8].toString()));
			contactVo.setEmail(coulums[17] == null ? "" : (coulums[17].toString()));
			contactVo.setTelephone(coulums[19] == null ? "" : (coulums[19].toString()));
			contactVo.setPosition(coulums[28] == null ? "" : (coulums[28].toString()));
			contactVo.setMobile(coulums[20] == null ? "" : (coulums[20].toString()));
			contactVo.setAccount(coulums[1] == null ? "" : (coulums[1].toString()));
			contactVo.setUsername(coulums[14] == null ? "" : (coulums[14].toString()));
			contactVos.add(contactVo);
		}

		return contactVos;

	}

	public Integer findUsersCountBy(Map<String, Object> filter) {
		Integer count = (Integer) entityService.createNativeQueryByMap(buildSql(true), filter).getSingleResult();
		return count;
	}

	private String buildSql(boolean isCount) {
		StringBuffer xsql = new StringBuffer();
		xsql.append(" select");
		if (isCount) {
			xsql.append(" count(distinct(u.id))");
		} else {
			xsql.append(" distinct");
			xsql.append(" u.ID,");
			xsql.append(" u.AD_ACCOUNT,");
			xsql.append(" u.PERNR,");
			xsql.append(" u.ONBOARD_DATE,");
			xsql.append(" u.BIRTHDAY,");
			xsql.append(" u.IDENTITY_TYPE,");
			xsql.append(" u.IDTENTITY_ID,");
			xsql.append(" u.BACKGROUND_INFO,");
			xsql.append(" u.DEFUNCT_IND,");
			xsql.append(" u.CREATED_BY,");
			xsql.append(" u.CREATED_DATETIME,");
			xsql.append(" u.UPDATED_BY,");
			xsql.append(" u.UPDATED_DATETIME,");
			xsql.append(" p.*,lower(u.ad_account) as account,u.POSITION_REMARK,o.*");
		}
		xsql.append(" from Usermstr u");
		xsql.append(" left join cas_usr_p cup on u.ad_account = cup.id");
		xsql.append(" left join P p on cup.pernr=p.id");
		xsql.append(" left join vw_org_and_com c on p.orgeh = c.oid");
		xsql.append(" left join O o on o.id = c.id");
		xsql.append(" left join Userpositionorg upo on u.id=upo.usermstr_id and upo.defunct_ind='N'");
		xsql.append(" left join Positionorg po on po.id=upo.positionorg_id and upo.defunct_ind='N'");
		xsql.append(" left join Position ps on ps.id=po.position_id and ps.defunct_ind='N'");
		xsql.append(" left join Userrole ur on u.id=ur.usermstr_id and ur.defunct_ind='N'");
		xsql.append(" where 1 = 1 ");

		xsql.append(" /~ and u.ad_account like {username} ~/ ");
		xsql.append(" /~ and ps.position like {position} ~/ ");
		xsql.append(" /~ and c.stext like {company} ~/ ");
		xsql.append(" /~ and u.defunct_ind = {defunctInd} ~/ ");
		if (!isCount) {
			xsql.append(" order by account");
		}

		return xsql.toString();

	}
}
