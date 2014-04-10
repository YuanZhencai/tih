package com.wcs.common.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.common.controller.vo.UserCommonFormItemsVo;
import com.wcs.common.controller.vo.UserSearchVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.O;
import com.wcs.common.model.P;
import com.wcs.common.model.Usermstr;
import com.wcs.common.model.Userpositionorg;
import com.wcs.common.model.Userrole;
import com.wcs.tih.model.WfAuthorizmstr;
import com.wcs.tih.util.DateUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: 用户公共信息</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class UserCommonService {
	@PersistenceContext
	private EntityManager em;

	/**
	 * <p>Description: 查询公共用户信息</p>
	 * @param ucfiv 查询条件Vo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
    public List<UsermstrVo> queryCommomUser(UserCommonFormItemsVo ucfiv) {
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select u.*,p.*,lower(u.ad_account) from Usermstr u");
	    sql.append(" left join cas_usr_p cup on u.ad_account = cup.id");
	    sql.append(" left join P p on cup.pernr=p.id");
	    sql.append(" where u.defunct_ind <> 'Y'");
	    sql.append(" and cup.defunct_ind <> 'Y'");
	    sql.append(" and p.defunct_ind <> 'Y'");
        if (ucfiv.getAdAccount() != null && !"".equals(ucfiv.getAdAccount())) {
            sql.append(" and u.ad_account like '%" + ucfiv.getAdAccount().trim() + "%'");
        }
        if (ucfiv.getEmail() != null && !"".equals(ucfiv.getEmail())) {
            sql.append(" and p.email like '%" + ucfiv.getEmail().trim() + "%'");
        }
        if (ucfiv.getPernr() != null && !"".equals(ucfiv.getPernr())) {
            sql.append(" and cup.pernr like '%" + ucfiv.getPernr().trim() + "%'");
        }
        if (ucfiv.getNachn() != null && !"".equals(ucfiv.getNachn())) {
            sql.append(" and p.nachn like '%" + ucfiv.getNachn().trim() + "%'");
        }
	    sql.append(" order by lower(u.ad_account)");
	    List resultList = em.createNativeQuery(sql.toString()).getResultList();
	    return convertUserVos(resultList);
	}

    private List<UsermstrVo> convertUserVos(List resultList) {
        List<UsermstrVo> usermstrVoList = new ArrayList<UsermstrVo>();
        if(resultList == null || resultList.size() == 0){
        	return usermstrVoList;
        }
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = (Object[]) resultList.get(i);
	        UsermstrVo uv = convertUserVo(result);
            usermstrVoList.add(uv);
        }
        return usermstrVoList;
    }
    
    private Usermstr getUsermstrBy(Object[] result){
    	Usermstr usermstr = new Usermstr();
        usermstr.setId(result[0] == null? 0:(Long.parseLong(result[0].toString())));
        usermstr.setAdAccount(result[1] == null? "":(result[1].toString()));
        usermstr.setPernr(result[2] == null? "":(result[2].toString()));
        usermstr.setOnboardDate(result[3] == null? null:(new Timestamp(((Date)result[3]).getTime())));
        usermstr.setBirthday(result[4] == null? null:(new Timestamp(((Date)result[4]).getTime())));
        usermstr.setIdentityType(result[5] == null? "":(result[5].toString()));
        usermstr.setIdtentityId(result[6] == null? "":(result[6].toString()));
        usermstr.setBackgroundInfo(result[7] == null? "":(result[7].toString()));
        usermstr.setDefunctInd(result[8] == null? "":(result[8].toString()));
        usermstr.setCreatedBy(result[9] == null? "":(result[9].toString()));
        usermstr.setCreatedDatetime(result[10] == null? null:(new Timestamp(((Date)result[10]).getTime())));
        usermstr.setUpdatedBy(result[11] == null? "":(result[11].toString()));
        usermstr.setUpdatedDatetime(result[12] == null? null:(new Timestamp(((Date)result[12]).getTime())));
		return usermstr;
    }
    
    private P getPersonBy(Object[] result) {
    	P p = new P();
        String pid = result[13] == null? "":(result[13].toString());
        if(pid != null && !"".equals(pid)){
            p.setId(pid);
            p.setNachn(result[14] == null? "":(result[14].toString()));
            p.setName2(result[15] == null? "":(result[15].toString()));
            p.setIcnum(result[16] == null? "":(result[16].toString()));
            p.setEmail(result[17] == null? "":(result[17].toString()));
            p.setGesch(result[18] == null? "":(result[18].toString()));
            p.setTelno(result[19] == null? "":(result[19].toString()));
            p.setCelno(result[20] == null? "":(result[20].toString()));
            p.setBukrs(result[21] == null? "":(result[21].toString()));
            p.setKostl(result[22] == null? "":(result[22].toString()));
            p.setDefunctInd(result[23] == null? "":(result[23].toString()));
            p.setOrgeh(result[24] == null? "":(result[24].toString()));
            p.setZhrzjid(result[25] == null? "":(result[25].toString()));
            p.setZhrzjms(result[26] == null? "":(result[26].toString()));
        }else{
            p.setId(result[2] == null? "":(result[2].toString()));
            p.setNachn(result[1] == null? "":(result[1].toString()));
        }
		return p;
    }

    private O getOBy(Object[] result) {
    	O o = new O();
    	o.setId(result[28] == null? "":(result[28].toString()));
    	o.setStext(result[30] == null? "":(result[30].toString()));
		return o;
    }
    
    private UsermstrVo convertUserVo(Object[] result) {
        UsermstrVo uv = new UsermstrVo();
        if(result == null){
        	return uv;
        }
        Usermstr usermstr = getUsermstrBy(result);
		P p = getPersonBy(result);
        uv.setUsermstr(usermstr);
        uv.setP(p);
        uv.setLowerAdAccount(result[27] == null? "":(result[27].toString()));
        return uv;
    }

	/**
	 * <p>Description: 通过用户帐号取得用户</p>
	 * @param adAccount
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Usermstr getUsermstr(String adAccount) {
        String sql = "select u from Usermstr u where u.adAccount = '" + adAccount + "'";
        List<Usermstr> us = em.createQuery(sql).getResultList();
        if (null != us && us.size() == 1) {
            return us.get(0);
        } else {
            return null;
        }
    }
	
	/**
     * <p>Description: 通过帐号取得用户Vo</p>
     * @param adAccount 帐号
     * @return
     */
    public UsermstrVo getUsermstrVo(String adAccount) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select u.*,p.*,lower(u.ad_account) from Usermstr u");
        sql.append(" left join cas_usr_p cup on u.ad_account = cup.id");
        sql.append(" left join P p on cup.pernr=p.id");
        sql.append(" where u.ad_account = '"+ adAccount +"'");
        List resultList = em.createNativeQuery(sql.toString()).getResultList();
        UsermstrVo uv = new UsermstrVo();
        if(resultList !=null && resultList.size()>0){
            uv = convertUserVo((Object[])resultList.get(0));
            if(resultList.size() > 1){
                P p = new P();
                p.setId(uv.getUsermstr().getPernr());
                p.setNachn(adAccount);
                uv.setP(p);
            }
        }else{
            Usermstr u = new Usermstr();
            u.setAdAccount(adAccount);
            uv.setUsermstr(u);
            P p = new P();
            p.setNachn(adAccount);
            uv.setP(p);
        }
        return uv;
    }
    
    /**
     * <p>Description: 取得用户Vo</p>
     * @param adAccount 帐号
     * @return
     */
    public UsermstrVo getUsermstrVoByAdAccount(String adAccount) {
        return getUsermstrVo(adAccount);
    }
    
    /**
     * <p>Description: 通过用户帐号取得Cas对象</p>
     * @param adAccount 帐号
     * @return CasUsrP对象
     */
    @SuppressWarnings("unchecked")
    public CasUsrP getCasUsrP(String adAccount) {
        String sql = "select cup from CasUsrP cup where cup.id = '" + adAccount + "'";
        List<CasUsrP> casUsrPs = this.em.createQuery(sql).getResultList();
        if (null != casUsrPs && casUsrPs.size() == 1) {
            return casUsrPs.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * <p>Description: 取得组织</p>
     * @param bukrs 
     * @return O对象
     */
    @SuppressWarnings("unchecked")
    public O getOrganization(String bukrs) {
        String sql = "select o from O o where o.bukrs = '" + bukrs + "' and o.parent = ''";
        List<O> os = em.createQuery(sql).getResultList();
        if (null != os && os.size() == 1) {
            return os.get(0);
        } else {
            return null;
        }
    }

	/**
	 * <p>Description: 通过用户Id取得用户的角色</p>
	 * @param usermstrId
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<Userrole> getUserroleByUsermstrId(long usermstrId, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select ur from Userrole ur where 1 = 1");
        if (effective != null && !"".equals(effective)) {
            sb.append(" and ur.defunctInd='" + effective + "'");
        }
        sb.append(" and ur.usermstr.id=" + usermstrId);
        return this.em.createQuery(sb.toString()).getResultList();
    }
    
    /**
     * <p>Description: 通过角色Id取得用户的角色</p>
     * @param usermstrId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Userrole> getUserroleByRolemstrId(long rolemstrId, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select ur from Userrole ur where 1 = 1");
        if (effective != null && !"".equals(effective)) {
            sb.append(" and ur.defunctInd='" + effective + "'");
        }
        sb.append(" and ur.rolemstr.id=" + rolemstrId);
        return this.em.createQuery(sb.toString()).getResultList();
    }

	/**
	 * <p>Description:取得用户的岗位 </p>
	 * @param usermstrId
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public List<Userpositionorg> getUserpositionorgByUsermstrId(long usermstrId, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select upo from Userpositionorg upo where 1 = 1");
        if (effective != null && !"".equals(effective)) {
            sb.append(" and upo.defunctInd='" + effective + "'");
        }
        sb.append(" and upo.usermstr.id=" + usermstrId);
        return this.em.createQuery(sb.toString()).getResultList();
    }
	
    /**
     * <p>Description: 通过税种或文档类别取得对应的人</p>
     * @param dictCatKey 税种或文档类别 字典表数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getAdAccountByDictCatKey(String dictCatKey) {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct u from Usermstr u,Userpositionorg upo,Positionorg po,Position position,WfPosition wp where u.id=upo.usermstr.id and upo.positionorg.id=po.id and po.position.id=position.id and position.id=wp.positionId");
        sb.append(" and wp.value ='" + dictCatKey + "'");
        sb.append(" and u.defunctInd='N' and upo.defunctInd='N' and po.defunctInd='N' and position.defunctInd='N'");
        List<Usermstr> usermstrs = this.em.createQuery(sb.toString()).getResultList();
        if (null != usermstrs && usermstrs.size() != 0) {
            List<String> adAccounts = new ArrayList<String>();
            for (Usermstr u : usermstrs) {
                adAccounts.add(u.getAdAccount());
            }
            return adAccounts;
        } else {
            return null;
        }
    }
    
    /**
     * <p>Description: 取得某个授权人的被授权人</p>
     * @param authorizedBy 授权人
     * @param requestFormType 申请单类型
     * @param datetime 时间
     * @return 
     */
    @SuppressWarnings("unchecked")
    public String getAuthorizer(String authorizedBy, String requestFormType) {
        String str = authorizedBy;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "select wa from WfAuthorizmstr wa where wa.startDatetime <= '" + sdf.format(new Date())
                + "' and wa.endDatetime > '" + sdf.format(DateUtil.getNextDate(new Date(), -1))
                + "' and wa.authorizedBy ='" + authorizedBy + "' and wa.type ='" + requestFormType + "'";
        List<WfAuthorizmstr> wfAuthorizmstrs = this.em.createQuery(sql).getResultList();
        if (wfAuthorizmstrs != null && wfAuthorizmstrs.size() != 0) {
            str = wfAuthorizmstrs.get(0).getAuthorizedTo();
        }
        return str;
    }
    
    /**
     * <p>Description: 通过组织的Id取得人</p>
     * @param oid 组织id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getAdAccountByOid(String oid) {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct u from Usermstr u,Userpositionorg upo,Positionorg po where u.id=upo.usermstr.id and upo.positionorg.id=po.id");
        sb.append(" and po.oid = '" + oid + "'");
        sb.append(" and u.defunctInd <> 'Y' and upo.defunctInd <> 'Y' and po.defunctInd <> 'Y' and po.position.defunctInd <> 'Y'");
        List<Usermstr> usermstrs = this.em.createQuery(sb.toString()).getResultList();
        if (null != usermstrs && usermstrs.size() != 0) {
            List<String> adAccounts = new ArrayList<String>();
            for (Usermstr u : usermstrs) {
                adAccounts.add(u.getAdAccount());
            }
            return adAccounts;
        } else {
            return null;
        }
    }
    
    /**
     * <p>Description: 通过组织的Id和岗位代码取得人</p>
     * @param oid 组织id
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getAdAccountByOidCode(String oid, String code) {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct u from Usermstr u,Userpositionorg upo,Positionorg po where u.id=upo.usermstr.id and upo.positionorg.id=po.id");
        sb.append(" and po.oid = '" + oid + "' and po.position.code = '" + code + "'");
        sb.append(" and u.defunctInd <> 'Y' and upo.defunctInd <> 'Y' and po.defunctInd <> 'Y' and po.position.defunctInd <> 'Y'");
        List<Usermstr> usermstrs = this.em.createQuery(sb.toString()).getResultList();
        if (null != usermstrs && usermstrs.size() != 0) {
            List<String> adAccounts = new ArrayList<String>();
            for (Usermstr u : usermstrs) {
                adAccounts.add(u.getAdAccount());
            }
            return adAccounts;
        } else {
            return null;
        }
    }
    
    /**
     * <p>Description: 通过岗位的code取得人</p>
     * @param code 岗位的code
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getAdAccountByPositionCode(String code) {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct u from Usermstr u,Userpositionorg upo,Positionorg po where u.id=upo.usermstr.id and upo.positionorg.id=po.id");
        sb.append(" and po.position.code = '" + code + "'");
        sb.append(" and u.defunctInd <> 'Y' and upo.defunctInd <> 'Y' and po.defunctInd <> 'Y' and po.position.defunctInd <> 'Y'");
        List<Usermstr> usermstrs = this.em.createQuery(sb.toString()).getResultList();
        if (null != usermstrs && usermstrs.size() != 0) {
            List<String> adAccounts = new ArrayList<String>();
            for (Usermstr u : usermstrs) {
                adAccounts.add(u.getAdAccount());
            }
            return adAccounts;
        } else {
            return null;
        }
    }
    
    /**
     * 
     * <p>Description: 判断一个字符串str1是否包含另一个字符串str2</p>
     * @param str1
     * @param str2
     * @return
     */
    public boolean ifContainIndexOf(String str1, String str2) {  
        if (str1.indexOf(str2) >= 0) {  
            return true;  
        }  
        return false;  
    }
    
    public String getUserName(String str) {
        if (str == null){
            return "";
        }
        UsermstrVo usermstrVo = getUsermstrVo(str);
        if(usermstrVo != null && usermstrVo.getP() != null){
        	return usermstrVo.getP().getNachn();
        }
        return str;
    }
    
    public List<UsermstrVo> findUsers(UserSearchVo usv){
    	StringBuffer sql = new StringBuffer();
	    sql.append(" select distinct u.*,p.*,lower(u.ad_account),c.* from Usermstr u");
	    sql.append(" left join cas_usr_p cup on u.ad_account = cup.id");
	    sql.append(" left join P p on cup.pernr=p.id");
	    sql.append(" left join vw_org_and_com c on p.orgeh = c.oid");
	    sql.append(" left join Userpositionorg upo on u.id=upo.usermstr_id and upo.defunct_ind='N'");
	    sql.append(" left join Positionorg po on po.id=upo.positionorg_id and upo.defunct_ind='N'");
	    sql.append(" left join Userrole ur on u.id=ur.usermstr_id and ur.defunct_ind='N'");
	    sql.append(" where 1 = 1 ");
	    
	    if (usv.getAdAccount() != null && !"".equals(usv.getAdAccount())) {
	    	sql.append(" and u.ad_account like '%" + usv.getAdAccount().trim() + "%'");
        }
        if (usv.getUserName() != null && !"".equals(usv.getUserName())) {
        	sql.append(" and p.nachn like '%" + usv.getUserName().trim() + "%'");
        }
        if (usv.getPositionId() != null && !"".equals(usv.getPositionId())) {
        	sql.append(" and po.position_id=" + usv.getPositionId());
        }
        if (usv.getRolemstrId() != null && !"".equals(usv.getRolemstrId())) {
        	sql.append(" and ur.rolemstr_id=" + usv.getRolemstrId() + "");
        }
        if (usv.getStatus() != null && !"".equals(usv.getStatus())) {
        	sql.append(" and u.defunct_ind='" + usv.getStatus() + "'");
        }
	    
	    sql.append(" order by lower(u.ad_account)");
	    List<Object[]> resultList = em.createNativeQuery(sql.toString()).getResultList();
	    List<UsermstrVo> users = new ArrayList<UsermstrVo>();
	    UsermstrVo user = null;
	    for (Object[] objects : resultList) {
	    	user = new UsermstrVo();
	    	Usermstr usermstr = getUsermstrBy(objects);
	    	P p = getPersonBy(objects);
	    	O o = getOBy(objects);
	    	user.setUsermstr(usermstr);
	    	user.setP(p);
	    	user.setO(o);
	    	users.add(user);
		}
		return users;
    }

}
