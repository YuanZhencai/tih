package com.wcs.tih.system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.wcs.tih.model.Taxauthority;
import com.wcs.tih.system.controller.vo.TaxAuthorityVO;

@Stateless
public class TaxAuthorityService implements Serializable {

	private static final long serialVersionUID = -7707410191691768066L;
	@PersistenceContext
	private EntityManager em;

	// 查询方法
	@SuppressWarnings("unchecked")
	public List<TaxAuthorityVO> queryDataPlan(String taxName,
			String taxAddress, String taxState) {
		StringBuilder jpql = new StringBuilder("select t from Taxauthority t where 1=1 ");
		if (taxName != null && !taxName.trim().equals("")) {
		    jpql.append(" AND t.name LIKE '%" + taxName + "%' ");
		}
		if (taxAddress != null && !taxAddress.trim().equals("")) {
		    jpql.append(" AND t.address LIKE '%" + taxAddress + "%' ");
		}
		if (taxState != null && !taxState.trim().equals("")) {
		    jpql.append(" AND t.defunctInd = '" + taxState + "'");
		}
		List<Taxauthority> result = em.createQuery(jpql.toString()).getResultList();
		List<TaxAuthorityVO> list = new ArrayList<TaxAuthorityVO>();
		for (Taxauthority t : result) {
			list.add(new TaxAuthorityVO(t));
		}
		return list;
	}

	// 添加方法
	public void addTaxManage(Taxauthority tax) {
		this.em.persist(tax);
	}

	// 更新方法
	public void modifyData(Taxauthority tax) {
	    this.em.merge(tax);
	}
	
	//查询数据库是否有重复的name
    public String repeatNameBool(String name,String id){
        StringBuffer jpql=new StringBuffer("SELECT count(t) FROM Taxauthority t where t.name='"+name+"'");
        if(id!=null){
            jpql.append(" and t.id <> "+id+"");
        }
        Query query=em.createQuery(jpql.toString());
        Long result = (Long) query.getResultList().get(0);
        if(result!=0){
            return "repeat";
        }else{
            return "norepeat";
        }
    }
}
