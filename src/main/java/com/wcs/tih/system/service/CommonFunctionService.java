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

import com.wcs.common.model.Resourcemstr;
import com.wcs.tih.model.Commonfunction;
import com.wcs.tih.system.controller.vo.CommonFunctionVO;

@Stateless
public class CommonFunctionService implements Serializable {

	private static final long serialVersionUID = 1L;

	@PersistenceContext
	private EntityManager em;

	// 查询
	public List<CommonFunctionVO> queryData(String queryName) {
		StringBuilder strSql = new StringBuilder();
		// 说明:页面Table显示的是c表中优先级,还有就是关联到外表中的Name,还需要获取到页面并不显示的ID
		// 这里我觉得必须要有一个修改的方法,修改优先级.
		// 获取的数据是以C为主的,R为从表.C为主,必须是defunctInd为N,Y的不显示.而这里的N是要对应picklist的target.
		// select R.Name,C.priority FROM COMMONFUNCTION as C,RESOURCEMSTR as R
		// WHERE R.ID=C.RESOURCEMSTR_ID
		strSql.append("SELECT c FROM Commonfunction c WHERE c.defunctInd ='N' ");
		strSql.append(" AND c.resourcemstr.type ='MENU' ");
		strSql.append(" AND c.resourcemstr.uri  is not null ");
		strSql.append(" AND c.resourcemstr.uri <> ' ' ");  //modify1
		strSql.append(" AND c.resourcemstr.defunctInd ='N' ");
		if (queryName != null && !queryName.trim().equals("")) {
			strSql.append(" AND c.resourcemstr.name like '%" + queryName + "%' ");
		}
		strSql.append(" order by c.priority ");
		Query query = em.createQuery(strSql.toString());
		@SuppressWarnings("unchecked")
		List<Commonfunction> result = query.getResultList();
		List<CommonFunctionVO> list = new ArrayList<CommonFunctionVO>();
		for (Commonfunction c : result) {
			list.add(new CommonFunctionVO(c.getId(), c.getResourcemstr().getName(), c.getPriority(),c.getDefunctInd(),c.getResourcemstr().getId(),c.getResourcemstr().getUri()));
		}
		return list;
	}

	// pickList的查询方法
	@SuppressWarnings({ "unchecked" })
	public List<CommonFunctionVO> queryPickList(String operation) {
		if (!"source".equals(operation)) {
			//右边数据:1.两张表都必须有效 2.URI不能为null.空格 3如果commonfuntion表无,就要添加新的数据.
			List<CommonFunctionVO> target = new ArrayList<CommonFunctionVO>();
			StringBuilder tragetSql = new StringBuilder(
					"SELECT c FROM Commonfunction c WHERE c.defunctInd='N'  "
							+ "AND c.resourcemstr.type ='MENU'  "
							+ "AND c.resourcemstr.uri  is not null  "
							+ "AND c.resourcemstr.uri  <> ' '  "
							+ "AND c.resourcemstr.defunctInd ='N' "
							+ "order by c.priority");
			List<Commonfunction> rightResult = em.createQuery(
					tragetSql.toString()).getResultList();
			for (Commonfunction c : rightResult) {
				target.add(new CommonFunctionVO(c.getId(), c.getResourcemstr().getName(), c.getResourcemstr().getId(),c.getDefunctInd()));
			}
			return target;
		} else {
			List<CommonFunctionVO> source = new ArrayList<CommonFunctionVO>();
			String jpql="SELECT r FROM Resourcemstr r WHERE r.defunctInd='N' AND r.type='MENU' AND r.uri is not null AND r.uri <> ' ' " +
					"AND r.id not in" +
					"(SELECT c.resourcemstr.id FROM Commonfunction c WHERE  c.defunctInd='N' AND c.resourcemstr.type ='MENU' AND c.resourcemstr.uri  is not null AND c.resourcemstr.uri <> ' ' AND c.resourcemstr.defunctInd ='N')" +
					" ";
			List<Resourcemstr> leftResult = em.createQuery(jpql).getResultList();	
			
			for (Resourcemstr r : leftResult) {
				source.add(new CommonFunctionVO(r.getId(), r.getName(),r.getId(),"Y"));
			}
			return source;
			
		}
	}

	// 右表更新优先级
	public void updateRight(String name, int priorityNum, String loginName) {
		//判断cf表中是否关联r表
		String jpqlOne="SELECT c FROM Commonfunction c WHERE c.resourcemstr.name = '"+name+"'";
		List<Commonfunction> oneResult=em.createQuery(jpqlOne).getResultList();
		int num=oneResult.size();
		//等于1就修改
		if(num == 1){
			StringBuilder strSql = new StringBuilder();
			strSql.append("UPDATE Commonfunction c SET c.priority = '"+ priorityNum + "',c.updatedBy='" + loginName + "',");
			strSql.append("c.updatedDatetime= '"+ new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()) + "',");
			strSql.append(" c.defunctInd='N' ");
			strSql.append(" WHERE c.resourcemstr.name='" + name + "'");
			this.em.createQuery(strSql.toString()).executeUpdate();
		}//是0表示没有管理就添加新的数据
		else if(num == 0){
			//首先查出Rbi
			String jpqlf="SELECT r FROM Resourcemstr r WHERE r.name = '"+name+"'";
			List<Resourcemstr> fResult=em.createQuery(jpqlf).getResultList();
			
			Commonfunction cf=new Commonfunction();
			cf.setCreatedBy(loginName);
			cf.setCreatedDatetime(new Date());
			cf.setUpdatedBy(loginName);
			cf.setUpdatedDatetime(new Date());
			cf.setDefunctInd("N");
			cf.setPriority(priorityNum);
			cf.setResourcemstr(fResult.get(0));
			this.em.persist(cf);
		}else{
			//这个时候出问题了.......resourcemstr表里的NAME,不能重复 
		}
		
	}
	

		// 左表更新优先级
		public void updateLeft(String name, int priorityNum, String loginName) {
			String jpqlOne="SELECT c FROM Commonfunction c WHERE c.resourcemstr.name = '"+name+"'";
			List<Commonfunction> oneResult=em.createQuery(jpqlOne).getResultList();
			int num=oneResult.size();
			//等于1就修改
			if(num == 1){
				StringBuilder strSql = new StringBuilder();
				strSql.append("UPDATE Commonfunction c SET c.priority = 0,c.updatedBy='" + loginName + "',");
				strSql.append(" c.updatedDatetime= '"+ new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()) + "',");
				strSql.append(" c.defunctInd='Y' ");
				strSql.append(" WHERE c.resourcemstr.name='" + name + "'");
				this.em.createQuery(strSql.toString()).executeUpdate();
			}//是0表示没有管理就添加新的数据
			else if(num == 0){
				//首先查出Rbi
				String jpqlf="SELECT r FROM Resourcemstr r WHERE r.name = '"+name+"'";
				List<Resourcemstr> fResult=em.createQuery(jpqlf).getResultList();
				
				Commonfunction cf=new Commonfunction();
				cf.setCreatedBy(loginName);
				cf.setCreatedDatetime(new Date());
				cf.setUpdatedBy(loginName);
				cf.setUpdatedDatetime(new Date());
				cf.setDefunctInd("Y");
				cf.setPriority(priorityNum);
				cf.setResourcemstr(fResult.get(0));
				this.em.persist(cf);
			}else{
				//这个时候出问题了.......resourcemstr表里的NAME,不能重复 
			}
		}
	
}
