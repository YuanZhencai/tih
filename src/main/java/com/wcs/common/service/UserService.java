package com.wcs.common.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.PSearchVo;
import com.wcs.common.controller.vo.PVo;
import com.wcs.common.controller.vo.UserPositionVo;
import com.wcs.common.controller.vo.UserSearchVo;
import com.wcs.common.controller.vo.UserVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.O;
import com.wcs.common.model.P;
import com.wcs.common.model.Position;
import com.wcs.common.model.Positionorg;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Usermstr;
import com.wcs.common.model.Userpositionorg;
import com.wcs.common.model.Userrole;

/**
 * <p>Project: tih</p>
 * <p>Description: 用户Service</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;
    @EJB
    private RoleAndUserToTdsService roleAndUserToTdsService;
    @EJB
    private UserCommonService userCommonService;
    
    /**
     * <p>Description:保存用户 </p>
     * @param userVo 用户Vo
     * @throws Exception 
     */
    public void saveUser(UserVo userVo) throws Exception {
        String sql = "select u from Usermstr u where u.adAccount = '" + userVo.getUserName() + "'";
        if (this.em.createQuery(sql).getResultList().size() != 0){
        	throw new Exception("帐号：" + userVo.getUserName() + "，姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "，用户已经存在税务系统中，不能重复添加！");
        }
        Usermstr um = new Usermstr();
        um.setAdAccount(userVo.getUserName());
        um.setPernr(userVo.getJobNumber());
        um.setIdentityType(userVo.getCertificatesType());
        um.setIdtentityId(userVo.getCertificatesNumber());
        if (userVo.getWorkDateTime() != null) {
            um.setOnboardDate(new Timestamp(userVo.getWorkDateTime().getTime()));
        }
        um.setBirthday(userVo.getBirthday());
        um.setBackgroundInfo(userVo.getRemark());
        um.setPositionRemark(userVo.getPositionRemark());
        um.setDefunctInd(userVo.getEffective());
        um.setCreatedDatetime(new Date());
        um.setCreatedBy(this.loginService.getCurrentUserName());
        um.setUpdatedDatetime(new Date());
        um.setUpdatedBy(this.loginService.getCurrentUserName());
        try {
            this.em.persist(um);
            //同步到TDS上
            roleAndUserToTdsService.synchronousUserToTds("insert", userVo);
        } catch (Exception e) {
            throw new Exception("保存用户数据出错了，请重新保存！");
        }
    }

    /**
     * <p>Description: 更新用户</p>
     * @param id 用户Id
     * @param userVo 用户Vo
     * @throws Exception
     */
    public void updateUser(long id, UserVo userVo) throws Exception {
        Usermstr um = new Usermstr();
        um.setId(id);
        um.setIdentityType(userVo.getCertificatesType());
        um.setIdtentityId(userVo.getCertificatesNumber());
        if (userVo.getWorkDateTime() != null) {
            um.setOnboardDate(new Timestamp(userVo.getWorkDateTime().getTime()));
        }
        um.setBirthday(userVo.getBirthday());
        um.setBackgroundInfo(userVo.getRemark());
        um.setPositionRemark(userVo.getPositionRemark());
        um.setDefunctInd(userVo.getEffective());
        um.setUpdatedDatetime(new Date());
        um.setUpdatedBy(this.loginService.getCurrentUserName());
        try {
            this.em.merge(um);
            //同步到TDS上
            roleAndUserToTdsService.synchronousUserToTds("update", userVo);
        } catch (Exception e) {
            throw new Exception("更新用户数据出错了，请重新保存！");
        }
    }

    /**
     * <p>Description: 保存用户岗位</p>
     * @param selectedUserPositionVos 选中的岗位
     * @param selectedUsermstr 选中的用户
     * @throws Exception
     */
    public void saveUserPosition(UserPositionVo[] selectedUserPositionVos, Usermstr selectedUsermstr) throws Exception {
        List<Userpositionorg> existUserPositionorgs = this.getUserpositionorgByUsermstrId(selectedUsermstr.getId(), "");
        String assignPositionMsg = "用户分配岗位失败，请重新分配！";
        if (selectedUserPositionVos != null && selectedUserPositionVos.length != 0) {
            Userpositionorg userpositionorg;
            Positionorg psitionorg;
            if (existUserPositionorgs != null && existUserPositionorgs.size() != 0) {
                boolean b = false;
                for (Userpositionorg upo : existUserPositionorgs) {
                    b = false;
                    for (UserPositionVo upv : selectedUserPositionVos) {
                        if ((long) upo.getPositionorg().getId() == (long) upv.getPositionorgId()) {
                            b = true;
                            upo.setDefunctInd("N");
                            upo.setUpdatedBy(this.loginService.getCurrentUserName());
                            upo.setUpdatedDatetime(new Date());
                            try {
                                this.em.merge(upo);
                            } catch (Exception e) {
                                throw new Exception(assignPositionMsg);
                            }
                            break;
                        }
                    }
                    if (!b) {
                        upo.setDefunctInd("Y");
                        upo.setUpdatedBy(this.loginService.getCurrentUserName());
                        upo.setUpdatedDatetime(new Date());
                        try {
                            this.em.merge(upo);
                        } catch (Exception e) {
                            throw new Exception(assignPositionMsg);
                        }
                    }
                }
                for (UserPositionVo upv : selectedUserPositionVos) {
                    b = false;
                    for (Userpositionorg upo : existUserPositionorgs) {
                        if ((long) upv.getPositionorgId() == (long) upo.getPositionorg().getId()) {
                            b = true;
                            upo.setDefunctInd("N");
                            upo.setUpdatedBy(this.loginService.getCurrentUserName());
                            upo.setUpdatedDatetime(new Date());
                            try {
                                this.em.merge(upo);
                            } catch (Exception e) {
                                throw new Exception(assignPositionMsg);
                            }
                            break;
                        }
                    }
                    if (!b) {
                        psitionorg = this.em.find(Positionorg.class, upv.getPositionorgId());
                        userpositionorg = new Userpositionorg();
                        userpositionorg.setUsermstr(selectedUsermstr);
                        userpositionorg.setPositionorg(psitionorg);
                        userpositionorg.setDefunctInd("N");
                        userpositionorg.setCreatedBy(this.loginService.getCurrentUserName());
                        userpositionorg.setCreatedDatetime(new Date());
                        userpositionorg.setUpdatedBy(this.loginService.getCurrentUserName());
                        userpositionorg.setUpdatedDatetime(new Date());
                        try {
                            this.em.persist(userpositionorg);
                        } catch (Exception e) {
                            throw new Exception(assignPositionMsg);
                        }
                    }
                }
            } else {
                UserPositionVo upv;
                for (int i = 0; i < selectedUserPositionVos.length; i++) {
                    upv = selectedUserPositionVos[i];
                    psitionorg = this.em.find(Positionorg.class, upv.getPositionorgId());
                    userpositionorg = new Userpositionorg();
                    userpositionorg.setUsermstr(selectedUsermstr);
                    userpositionorg.setPositionorg(psitionorg);
                    userpositionorg.setDefunctInd("N");
                    userpositionorg.setCreatedBy(this.loginService.getCurrentUserName());
                    userpositionorg.setCreatedDatetime(new Date());
                    userpositionorg.setUpdatedBy(this.loginService.getCurrentUserName());
                    userpositionorg.setUpdatedDatetime(new Date());
                    try {
                        this.em.persist(userpositionorg);
                    } catch (Exception e) {
                        throw new Exception(assignPositionMsg);
                    }
                }
            }
        } else {
            if (existUserPositionorgs != null && existUserPositionorgs.size() != 0) {
                for (Userpositionorg upo : existUserPositionorgs) {
                    upo.setDefunctInd("Y");
                    upo.setUpdatedBy(this.loginService.getCurrentUserName());
                    upo.setUpdatedDatetime(new Date());
                    try {
                        this.em.merge(upo);
                    } catch (Exception e) {
                        throw new Exception(assignPositionMsg);
                    }
                }
            }
        }
    }

    /**
     * <p>Description: 保存用户角色</p>
     * @param selectedRoleVos 选中的角色
     * @param selectedUsermstr 选中的用户
     * @throws Exception
     */
    public void saveUserRole(List<Long> selectedRoleVos, Usermstr selectedUsermstr) throws Exception {
        List<Userrole> existUserroles = getUserroleByUsermstrId(selectedUsermstr.getId(), "");
        Userrole userrole;
        Rolemstr rolemstr;
        String assignRoleMsg = "用户分配角色失败了，请重新分配！";
        if (existUserroles != null && existUserroles.size() != 0) {
            if (selectedRoleVos != null && selectedRoleVos.size() != 0) {
                boolean b = false;
                for (Userrole ur : existUserroles) {
                    b = false;
                    for (int i = 0; i < selectedRoleVos.size(); i++) {
                        rolemstr = this.em.find(Rolemstr.class, selectedRoleVos.get(i));
                        if ((long) ur.getRolemstr().getId() == (long) rolemstr.getId()) {
                            b = true;
                            ur.setDefunctInd("N");
                            ur.setUpdatedBy(this.loginService.getCurrentUserName());
                            ur.setUpdatedDatetime(new Date());
                            try {
                                this.em.merge(ur);
                            } catch (Exception e) {
                                throw new Exception(assignRoleMsg);
                            }
                            break;
                        }
                    }
                    if (!b) {
                        ur.setDefunctInd("Y");
                        ur.setUpdatedBy(this.loginService.getCurrentUserName());
                        ur.setUpdatedDatetime(new Date());
                        try {
                            this.em.merge(ur);
                        } catch (Exception e) {
                            throw new Exception(assignRoleMsg);
                        }
                    }
                }
                for (int i = 0; i < selectedRoleVos.size(); i++) {
                    b = false;
                    rolemstr = this.em.find(Rolemstr.class, selectedRoleVos.get(i));
                    for (Userrole ur : existUserroles) {
                        if ((long) rolemstr.getId() == (long) ur.getRolemstr().getId()) {
                            b = true;
                            ur.setDefunctInd("N");
                            ur.setUpdatedBy(this.loginService.getCurrentUserName());
                            ur.setUpdatedDatetime(new Date());
                            try {
                                this.em.merge(ur);
                            } catch (Exception e) {
                                throw new Exception(assignRoleMsg);
                            }
                            break;
                        }
                    }
                    if (!b) {
                        userrole = new Userrole();
                        userrole.setUsermstr(selectedUsermstr);
                        userrole.setRolemstr(this.em.find(Rolemstr.class, selectedRoleVos.get(i)));
                        userrole.setDefunctInd("N");
                        userrole.setCreatedBy(this.loginService.getCurrentUserName());
                        userrole.setCreatedDatetime(new Date());
                        userrole.setUpdatedBy(this.loginService.getCurrentUserName());
                        userrole.setUpdatedDatetime(new Date());
                        try {
                            this.em.persist(userrole);
                        } catch (Exception e) {
                            throw new Exception(assignRoleMsg);
                        }
                    }
                }
            } else {
                for (Userrole ur : existUserroles) {
                    ur.setDefunctInd("Y");
                    ur.setUpdatedBy(this.loginService.getCurrentUserName());
                    ur.setUpdatedDatetime(new Date());
                    try {
                        this.em.merge(ur);
                    } catch (Exception e) {
                        throw new Exception(assignRoleMsg);
                    }
                }
            }
        } else {
            if (selectedRoleVos != null && selectedRoleVos.size() != 0) {
                for (int i = 0; i < selectedRoleVos.size(); i++) {
                    userrole = new Userrole();
                    userrole.setUsermstr(selectedUsermstr);
                    userrole.setRolemstr(this.em.find(Rolemstr.class, selectedRoleVos.get(i)));
                    userrole.setDefunctInd("N");
                    userrole.setCreatedBy(this.loginService.getCurrentUserName());
                    userrole.setCreatedDatetime(new Date());
                    userrole.setUpdatedBy(this.loginService.getCurrentUserName());
                    userrole.setUpdatedDatetime(new Date());
                    try {
                        this.em.persist(userrole);
                    } catch (Exception e) {
                        throw new Exception(assignRoleMsg);
                    }
                }
            }
        }
        List<Rolemstr> rs = new ArrayList<Rolemstr>();
        if (selectedRoleVos != null && selectedRoleVos.size() != 0) {
            for (int i = 0; i < selectedRoleVos.size(); i++) {
                rolemstr = this.em.find(Rolemstr.class, selectedRoleVos.get(i));
                rs.add(rolemstr);
            }
        }
        //同步用户角色关系数据到TDS上
        UserVo uv = new UserVo();
        uv.setUserName(selectedUsermstr.getAdAccount());
        uv.setJobNumber(selectedUsermstr.getPernr());
        uv.setRealName(selectedUsermstr.getAdAccount());
        roleAndUserToTdsService.synchronousUserRoleToTds(uv, existUserroles, rs);
    }
    
    /**
     * <p>Description: 查询P</p>
     * @param pfiv 查询条件Vo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<PVo> getAllP(PSearchVo pfiv) {
        StringBuffer sb = new StringBuffer();
        sb.append("select p from P p where 1=1");
        if (pfiv != null) {
            if (pfiv.getNachn() != null && !"".equals(pfiv.getNachn())) {
                sb.append(" and p.nachn like '%" + pfiv.getNachn().trim() + "%'");
            }
            if (pfiv.getEmail() != null && !"".equals(pfiv.getEmail())) {
                sb.append(" and p.email like '%" + pfiv.getEmail().trim() + "%'");
            }
            if (pfiv.getPernr() != null && !"".equals(pfiv.getPernr())) {
                sb.append(" and p.id like '%" + pfiv.getPernr() + "%'");
            }
        }
        sb.append(" and p.defunctInd='N' order by p.id");
        List<P> list = this.em.createQuery(sb.toString()).getResultList();
        List<PVo> pVoList = new ArrayList<PVo>();
        PVo pVo = null;
        long num = 0;
        if (null != list && list.size() != 0) {
            for (P p : list) {
                num++;
                pVo = new PVo();
                pVo.setId(num);
                pVo.setP(p);
                pVo.setCup(this.em.find(CasUsrP.class, p.getId()));
                if (null != p.getBukrs() && !"".equals(p.getBukrs())) {
                    pVo.setO(getOrganization("bukrs", p.getBukrs()));
                }else{
                    pVo.setO(new O());
                }
                pVoList.add(pVo);
            }
        }
        return pVoList;
    }

    /**
     * <p>Description: 查询用户</p>
     * @param usv 查询条件Vo
     * @return
     */
    public List<UsermstrVo> getAllUser(UserSearchVo usv) {
        return userCommonService.findUsers(usv);
    }

    /**
     * <p>Description: 查询岗位(岗位必须和组织挂钩)</p>
     * @param positionName  岗位名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserPositionVo> getAllPositionByName(UserPositionVo upo) {
        StringBuffer sb = new StringBuffer();
        sb.append("select po,o from Positionorg po,Position p,Companymstr c,O o where po.position.id=p.id and po.oid=c.oid and c.oid=o.id and 1=1");
        if(upo != null){
            String positionName = upo.getPositionName();
            if (positionName != null && !"".equals(positionName.trim())) {
                sb.append(" and p.name like '%" + positionName.trim() + "%'");
            }
            String organizationName = upo.getOrganizationName();
            if (organizationName != null && !"".equals(organizationName.trim())) {
                sb.append(" and o.stext like '%" + organizationName.trim() + "%'");
            }
        }
        sb.append(" and po.defunctInd<>'Y' and p.defunctInd<>'Y' and c.defunctInd<>'Y' and o.defunctInd <> 'Y'");
        List resultList = this.em.createQuery(sb.toString()).getResultList();
        List<UserPositionVo> userPositionVos = null;
        if(resultList !=null && resultList.size() >0){
            userPositionVos = new ArrayList<UserPositionVo>();
            UserPositionVo userPositionVo = null;
            for (int i = 0; i < resultList.size(); i++) {
                Object[] result = (Object[]) resultList.get(i);
                Positionorg po = (result[0] == null ? null:(Positionorg)result[0]);
                O o = (result[1] == null ? null:(O)result[1]);
                if(po != null){
                    userPositionVo = new UserPositionVo();
                    userPositionVo.setId(po.getId());
                    userPositionVo.setPositionorgId(po.getId());
                    if (po.getPosition() != null){
                    	userPositionVo.setPositionName(po.getPosition().getName());
                    }
                    if (o != null){
                    	userPositionVo.setOrganizationName(o.getStext());
                    }
                }
                userPositionVos.add(userPositionVo);
            }
        }
        return userPositionVos ;
    }
    
    /**
     * <p>Description: 查询所有的岗位</p>
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Position> getAllPosition() {
        StringBuffer sb = new StringBuffer();
        sb.append("select p from Position p where p.defunctInd='N' order by p.name");
        String sql = sb.toString();
        return this.em.createQuery(sql).getResultList();
    }

    /**
     * <p>Description: 查询所有的角色</p>
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Rolemstr> getAllRole() {
        StringBuffer sb = new StringBuffer();
        sb.append("select r from Rolemstr r where r.defunctInd='N' order by r.name");
        String sql = sb.toString();
        return this.em.createQuery(sql).getResultList();
    }

    /**
     * <p>Description: 查询用户的角色</p>
     * @param usermstrId 用户Id
     * @return
     */
    public List<Userrole> getUserroleByUsermstrId(long usermstrId, String effective) {
        return userCommonService.getUserroleByUsermstrId(usermstrId, effective);
    }

    /**
     * <p>Description: 查询用户岗位</p>
     * @param usermstrId 用户Id
     * @return
     */
    public List<Userpositionorg> getUserpositionorgByUsermstrId(long usermstrId, String effective) {
        return userCommonService.getUserpositionorgByUsermstrId(usermstrId, effective);
    }

    /**
     * <p>Description: 取得组织</p>
     * @param method 表示是通过Id还是Bukrs取得组织
     * @param idOrBukrs Id或Bukes
     * @return
     */
    public O getOrganization(String method, String idOrBukrs) {
        if ("id".equals(method)) {
            return this.em.find(O.class, idOrBukrs);
        } else {
            return userCommonService.getOrganization(idOrBukrs);
        }
    }
    
}
