package com.wcs.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.model.Dict;

/**
 * @author ZhaoQian
 *
 */
@Stateless
public class DictService {
    private Logger logger = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext 
	private EntityManager em;
	
	/**
	 * 查询datatable的数据,模糊条件有"类别","键","值","系统标识","语言"五个.
	 * @param codeCat 
	 * @param codeKey
	 * @param codeVal 
	 * @param sysInd 系统标识
	 * @param lang 语言
	 * @return list<Dict>
	 * @author ZhaoQian
	 */
	public List<DictVo> searchData(String codeCat,String codeKey,String codeVal,String sysInd, String lang){
		StringBuilder strSql=new StringBuilder();
		strSql.append("SELECT d FROM Dict d WHERE 1=1 ");
		if(null!=codeCat && !codeCat.trim().equals("")){
			strSql.append(" AND d.codeCat LIKE '%" + codeCat + "%' ");
		}
		if(null!= codeKey && !codeKey.trim().equals("")){
			strSql.append(" AND d.codeKey LIKE '%" + codeKey + "%' ");
		}
		if(null!= codeVal && !codeVal.trim().equals("")){
			strSql.append(" AND d.codeVal LIKE '%" + codeVal + "%' ");
		}
		if(null!= sysInd && !sysInd.trim().equals("")){
			strSql.append(" AND d.sysInd LIKE '%" + sysInd + "%' ");
		}
		if(null!= lang && !lang.trim().equals("")){
			strSql.append(" AND d.lang LIKE '%" + lang + "%' ");
		}
		Query query=em.createQuery(strSql.toString());
		@SuppressWarnings("unchecked")
		List<Dict> result=query.getResultList();
		List<DictVo> list=new ArrayList<DictVo>();
		for(Dict d: result){
			list.add(new DictVo(d.getId(), d.getCodeCat(), d.getCodeKey(), d.getCodeVal(), d.getRemarks(), ((Long)d.getSeqNo()).toString(), d.getSysInd(), d.getLang(), d.getDefunctInd()));
		}
		return list;
	}//end
	
	/**
	 * 添加,
	 * @param dict,顺序为"类别","键","值","顺序号","语言","系统标识","是否生效","备注".共8个参数.
	 * @author ZhaoQian
	 */
	public void insertData(Dict dict){
		this.em.persist(dict);
	}
	
	/**
	 * 编辑页面
	 * @param selectData 被编辑的datatable数据行
	 * @param updateUser 更新人的account
	 * @author ZhaoQian
	 */
	public void updateData(DictVo selectData,String updateUser){
		StringBuilder strSql=new StringBuilder("UPDATE Dict d ");
		strSql.append("SET d.codeCat = '"+ selectData.getCodeCat() +"',");
		strSql.append("d.codeKey = '"+ selectData.getCodeKey() +"',");
		strSql.append("d.codeVal = '"+ selectData.getCodeVal() +"',");
		strSql.append("d.seqNo = '"+ selectData.getSeqNo() +"',");
		strSql.append("d.lang = '"+ selectData.getLang() +"',");
		strSql.append("d.sysInd = '"+ selectData.getSysInd() +"',");
		strSql.append("d.defunctInd = '"+ selectData.getDefunctInd() +"',");
		strSql.append("d.remarks = '"+ selectData.getRemarks() +"',");
		strSql.append("d.updatedBy = '"+ updateUser +"',");
		strSql.append("d.updatedDatetime = '"+ new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()) +"'");
		strSql.append(" WHERE d.id=" + (int)selectData.getId() + " ");
		this.em.createQuery(strSql.toString()).executeUpdate();
	}
	
	
	/**
	 * 验证添加更新时是否存在重复的数据
	 * 如果是更新的时候使用这个方法时,先把long id转为Long id,再转为String id;
	 * 如:String strId=((Long)selectData.getId()).toString();
	 * 如果是添加直接赋null.
	 * @return "repaat"=有重复数据,不能添加或更新. "norepeat"=不存在重复数据.可以进行下一步.
	 */
	public String checkRepeatDataWithAdd(String codeCat,String codeKey,String id){
		StringBuffer jpql=new StringBuffer("SELECT count(d) FROM Dict d WHERE d.codeCat = '"+codeCat+"' AND d.codeKey = '"+codeKey+"'");
		if(id !=null ){
			jpql.append(" AND d.id <> "+id+" ");
		}
		Query query=em.createQuery(jpql.toString());
		String result = query.getResultList().get(0).toString();
		logger.info("result.size:"+result);
		if(!result.equals("0")){
			return "repeat";
		}else{
			return "norepeat";
		}
		
	}
	
}
