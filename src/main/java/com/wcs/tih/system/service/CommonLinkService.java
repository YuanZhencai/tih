package com.wcs.tih.system.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.wcs.tih.model.Linkmstr;
import com.wcs.tih.system.controller.vo.CommonLinkVO;

@Stateless
public class CommonLinkService implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	// 查询方法
	public List<CommonLinkVO> searchData(String linkName, String linkState) {
		StringBuilder strSql = new StringBuilder();
		strSql.append("SELECT lm FROM Linkmstr AS lm WHERE 1=1 ");
		
		if( null!= linkName && !linkName.trim().equals("")){
			strSql.append(" AND lm.name like '%" + linkName + "%' ");
		}
		
		if ( null!= linkState && !linkState.trim().equals("") ) {
			strSql.append(" AND lm.defunctInd='" + linkState + "' ");
		}
		
		strSql.append(" order by lm.priority");
		Query query = em.createQuery(strSql.toString());
		List<Linkmstr> result = query.getResultList();
		List<CommonLinkVO> list = new ArrayList<CommonLinkVO>();
		for (Linkmstr lm : result) {
			list.add(new CommonLinkVO(lm.getId(), lm.getName(),
					lm.getLinkUrl(), lm.getPriority(), lm.getDefunctInd()));
		}
		return list;
	}

	// Insert方法
	public void InsertData(String addLinkName, String addLinkUrl,
			String addLinkPriority,String addLinkdefunct,String createUser) {
		Linkmstr linkmstr = new Linkmstr();
		int addPriority = Integer.valueOf(addLinkPriority);
		linkmstr.setName(addLinkName);
		linkmstr.setLinkUrl(addLinkUrl);
		linkmstr.setPriority(addPriority);
		linkmstr.setDefunctInd(addLinkdefunct);// 第一次添加的时候默认为生效,也就是 N
		linkmstr.setCreatedBy(createUser);
		linkmstr.setCreatedDatetime(new Date());
		linkmstr.setUpdatedBy(createUser);
		linkmstr.setUpdatedDatetime(new Date());
		this.em.persist(linkmstr);
	}

	// 更新方法
	public void ModifyData(CommonLinkVO selectData, String updateUser) {
		StringBuilder strSql = new StringBuilder("update Linkmstr L ");
		strSql.append("SET L.name= '" + selectData.getName() + "',");
		strSql.append("L.linkUrl= '" + selectData.getUrl() + "',");
		strSql.append("L.priority= '" + selectData.getPriority() + "',");
		strSql.append("L.defunctInd= '" + selectData.getDefunct() + "',");
		strSql.append("L.updatedBy= '" + updateUser + "', ");
		strSql.append("L.updatedDatetime= '"
				+ new SimpleDateFormat("yyyy-MM-dd HH:ss:mm")
						.format(new Date()) + "' ");
		strSql.append(" WHERE L.id=" + selectData.getId() + " ");
		this.em.createQuery(strSql.toString()).executeUpdate();
	}

}
