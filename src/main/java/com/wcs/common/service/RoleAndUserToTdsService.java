package com.wcs.common.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.common.controller.vo.UserVo;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Synclog;
import com.wcs.common.model.Usermstr;
import com.wcs.common.model.Userrole;

@Stateless
public class RoleAndUserToTdsService {
    private static final String DELETE = "delete";
    private static final String UPDATE = "update";
    private static final String INSERT = "insert";
    @PersistenceContext
    private EntityManager em;
    @EJB
    private TDSLocal tDSLocal;
    @EJB
    private UserCommonService userCommonService;

    @Asynchronous
    public void synchronousUserToTds(String action, UserVo userVo) {
        if (action.equals(INSERT)) {
            int flag = tDSLocal.synchronousUserToTds(userVo, INSERT);
            Synclog synclog = new Synclog();
            synclog.setSyncType("同步TIH用户数据到TDS上");
            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
            synclog.setVersion(new Timestamp(new Date().getTime()));
            if (flag == 0) {
                synclog.setRemarks("TDS连接失败，同步帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的用户数据失败");
            } else if (flag == 1) {
                synclog.setRemarks("同步帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的用户数据成功");
            } else if (flag == 2) {
                synclog.setRemarks("系统异常，同步帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的用户数据到TDS上失败");
            }
            em.persist(synclog);
        }
        if (action.equals(UPDATE)) {
            Synclog synclog = null;
            Usermstr u = userCommonService.getUsermstr(userVo.getUserName());
            if (userVo.getEffective() != null && "N".equals(userVo.getEffective())) {
                int flag = tDSLocal.synchronousUserToTds(userVo, UPDATE);
                synclog = new Synclog();
                synclog.setSyncType("从TDS上更新用户数据");
                synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                synclog.setVersion(new Timestamp(new Date().getTime()));
                if (flag == 0) {
                    synclog.setRemarks("TDS连接失败，更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的用户数据失败");
                } else if (flag == 1) {
                    synclog.setRemarks("更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的用户数据成功");
                } else if (flag == 2) {
                    synclog.setRemarks("系统异常，从TDS上更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的用户数据失败");
                }
                em.persist(synclog);
                if (u != null) {
                    List<Userrole> ursN = userCommonService.getUserroleByUsermstrId(u.getId(), "N");
                    List<Map<String, Object>> adAccountsCodesN = tDSLocal.getUserRoleCodeMap(ursN);
                    if (null != adAccountsCodesN && adAccountsCodesN.size() != 0) {
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesN, INSERT);
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesN, DELETE);
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesN, INSERT);
                        synclog = new Synclog();
                        synclog.setSyncType("更新用户时，同步用户组关系");
                        synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                        synclog.setVersion(new Timestamp(new Date().getTime()));
                        if (flag == 0) {
                            synclog.setRemarks("TDS连接失败，更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "用户时，同步" + adAccountsCodesN.size() + "用户组关系数据失败");
                        } else if (flag == 1) {
                            synclog.setRemarks("更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "，同步" + adAccountsCodesN.size() + "用户组关系数据成功");
                        } else if (flag == 2) {
                            synclog.setRemarks("系统异常，从TDS上更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "，同步" + adAccountsCodesN.size() + "用户组关系数据失败");
                        }
                        em.persist(synclog);
                    }
                    List<Userrole> ursY = userCommonService.getUserroleByUsermstrId(u.getId(), "Y");
                    List<Map<String, Object>> adAccountsCodesY = tDSLocal.getUserRoleCodeMap(ursY);
                    if (null != adAccountsCodesY && adAccountsCodesY.size() != 0) {
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesY, INSERT);
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesY, DELETE);
                        synclog = new Synclog();
                        synclog.setSyncType("更新用户时，同步用户组关系");
                        synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                        synclog.setVersion(new Timestamp(new Date().getTime()));
                        if (flag == 0) {
                            synclog.setRemarks("TDS连接失败，更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "用户时，同步删除" + adAccountsCodesY.size() + "用户组关系数据失败");
                        } else if (flag == 1) {
                            synclog.setRemarks("更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "，同步删除" + adAccountsCodesY.size() + "用户组关系数据成功");
                        } else if (flag == 2) {
                            synclog.setRemarks("系统异常，从TDS上更新帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "，同步删除" + adAccountsCodesY.size() + "用户组关系数据失败");
                        }
                        em.persist(synclog);
                    }
                }
            } else if (userVo.getEffective() != null && "Y".equals(userVo.getEffective())) {
                // 删除用户组关系
                if (null != u) {
                    List<Userrole> urs = userCommonService.getUserroleByUsermstrId(u.getId(), "");
                    if (null != urs && urs.size() != 0) {
                        List<Map<String, Object>> adAccountsCodes = tDSLocal.getUserRoleCodeMap(urs);
                        if (null != adAccountsCodes && adAccountsCodes.size() != 0) {
                            tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                            int num = tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, DELETE);
                            synclog = new Synclog();
                            synclog.setSyncType("用户置为无效，删除用户组关系");
                            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                            synclog.setVersion(new Timestamp(new Date().getTime()));
                            if (num == 0) {
                                synclog.setRemarks("TDS连接失败，批量从TDS上删除用户" + userVo.getUserName() + "的" + adAccountsCodes.size() + "个用户关系失败");
                            } else if (num == 1) {
                                synclog.setRemarks("批量从TDS上删除用户" + userVo.getUserName() + "的" + adAccountsCodes.size() + "个用户关系成功");
                            } else if (num == 2) {
                                synclog.setRemarks("系统异常，批量从TDS上删除用户" + userVo.getUserName() + "的" + adAccountsCodes.size() + "个用户关系失败");
                            }
                            em.persist(synclog);
                        }
                    }
                }
            }
        }
    }

    @Asynchronous
    public void synchronousRoleToTds(String action, Rolemstr role) {
        if (action.equals(INSERT)) {
            // 同步角色到TDS上
            int flag = tDSLocal.synchronousGroupToTds(role, INSERT);
            Synclog synclog = new Synclog();
            synclog.setSyncType("同步TIH角色数据到TDS上");
            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
            synclog.setVersion(new Timestamp(new Date().getTime()));
            if (flag == 0) {
                synclog.setRemarks("TDS连接失败，同步代码：" + role.getCode() + "，名称：" + role.getName() + "的角色数据失败");
            } else if (flag == 1) {
                synclog.setRemarks("同步代码：" + role.getCode() + "，名称：" + role.getName() + "的角色数据成功");
            } else if (flag == 2) {
                synclog.setRemarks("系统异常，同步代码：" + role.getCode() + "，名称：" + role.getName() + "的角色数据到TDS上失败");
            }
            em.persist(synclog);
        }
        if (action.equals(UPDATE)) {
            if (null != role.getDefunctInd() && "N".equals(role.getDefunctInd())) {
                int flag = tDSLocal.synchronousGroupToTds(role, UPDATE);
                Synclog synclog = new Synclog();
                synclog.setSyncType("从TDS上更新角色");
                synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                synclog.setVersion(new Timestamp(new Date().getTime()));
                if (flag == 0) {
                    synclog.setRemarks("TDS连接失败，更新代码：" + role.getCode() + "，名称：" + role.getName() + "的角色数据失败");
                } else if (flag == 1) {
                    synclog.setRemarks("更新代码：" + role.getCode() + "，名称：" + role.getName() + "的角色数据成功");
                } else if (flag == 2) {
                    synclog.setRemarks("系统异常，更新代码：" + role.getCode() + "，名称：" + role.getName() + "的角色数据到TDS上失败");
                }
                em.persist(synclog);
                if (null != role) {
                    List<Userrole> ursN = userCommonService.getUserroleByRolemstrId(role.getId(), "N");
                    List<Map<String, Object>> adAccountsCodesN = tDSLocal.getUserRoleCodeMap(ursN);
                    if (null != adAccountsCodesN && adAccountsCodesN.size() != 0) {
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesN, INSERT);
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesN, DELETE);
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesN, INSERT);
                        synclog = new Synclog();
                        synclog.setSyncType("更组户时，同步用户组关系");
                        synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                        synclog.setVersion(new Timestamp(new Date().getTime()));
                        if (flag == 0) {
                            synclog.setRemarks("TDS连接失败，更新代码：" + role.getCode() + "，名称：" + role.getName() + "时，同步" + adAccountsCodesN.size() + "行角色用户关系数据失败");
                        } else if (flag == 1) {
                            synclog.setRemarks("更新代码：" + role.getCode() + "，名称：" + role.getName() + "时，同步" + adAccountsCodesN.size() + "行角色用户关系数据成功");
                        } else if (flag == 2) {
                            synclog.setRemarks("系统异常，更新代码：" + role.getCode() + "，名称：" + role.getName() + "时，同步" + adAccountsCodesN.size() + "行角色用户关系数据到TDS上失败");
                        }
                        em.persist(synclog);
                    }
                    List<Userrole> ursY = userCommonService.getUserroleByRolemstrId(role.getId(), "Y");
                    List<Map<String, Object>> adAccountsCodesY = tDSLocal.getUserRoleCodeMap(ursY);
                    if (null != adAccountsCodesY && adAccountsCodesY.size() != 0) {
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesY, INSERT);
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodesY, DELETE);
                        synclog = new Synclog();
                        synclog.setSyncType("更组户时，同步用户组关系");
                        synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                        synclog.setVersion(new Timestamp(new Date().getTime()));
                        if (flag == 0) {
                            synclog.setRemarks("TDS连接失败，更新代码：" + role.getCode() + "，名称：" + role.getName() + "时，同步删除" + adAccountsCodesN.size() + "行角色用户关系数据失败");
                        } else if (flag == 1) {
                            synclog.setRemarks("更新代码：" + role.getCode() + "，名称：" + role.getName() + "时，同步删除" + adAccountsCodesN.size() + "行角色用户关系数据成功");
                        } else if (flag == 2) {
                            synclog.setRemarks("系统异常，更新代码：" + role.getCode() + "，名称：" + role.getName() + "时，同步删除" + adAccountsCodesN.size() + "行角色用户关系数据到TDS上失败");
                        }
                        em.persist(synclog);
                    }
                }
            } else if (null != role.getDefunctInd() && "Y".equals(role.getDefunctInd())) {
                // 删除用户组关系
                List<Userrole> urs = userCommonService.getUserroleByRolemstrId(role.getId(), "");
                if (null != urs && urs.size() != 0) {
                    List<Map<String, Object>> adAccountsCodes = tDSLocal.getUserRoleCodeMap(urs);
                    if (null != adAccountsCodes && adAccountsCodes.size() != 0) {
                        tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                        int num = tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, DELETE);
                        Synclog synclog = new Synclog();
                        synclog.setSyncType("组置为无效，删除用户组关系");
                        synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                        synclog.setVersion(new Timestamp(new Date().getTime()));
                        if (num == 0) {
                            synclog.setRemarks("TDS连接失败，批量从TDS上删除组" + role.getCode() + "的" + adAccountsCodes.size() + "个用户关系失败");
                        } else if (num == 1) {
                            synclog.setRemarks("批量从TDS上删除组" + role.getCode() + "的" + adAccountsCodes.size() + "个用户关系成功");
                        } else if (num == 2) {
                            synclog.setRemarks("系统异常，批量从TDS上删除组" + role.getCode() + "的" + adAccountsCodes.size() + "个用户关系失败");
                        }
                        em.persist(synclog);
                    }
                }
            }
        }
    }

    @Asynchronous
    public void synchronousUserRoleToTds(UserVo userVo, List<Userrole> existUserroles, List<Rolemstr> selectedRoleVos) {
        if (existUserroles != null && existUserroles.size() != 0) {
            List<Map<String, Object>> adAccountsCodes = tDSLocal.getUserRoleCodeMap(existUserroles);
            if (adAccountsCodes.size() != 0) {
                this.tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                int flag = this.tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, DELETE);
                Synclog synclog = new Synclog();
                synclog.setSyncType("同步TIH用户角色数据到TDS上");
                synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                synclog.setVersion(new Timestamp(new Date().getTime()));
                if (flag == 0) {
                    synclog.setRemarks("TDS连接失败，移除帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的" + existUserroles.size() + "个用户角色数据失败");
                } else if (flag == 1) {
                    synclog.setRemarks("移除帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的" + existUserroles.size() + "个用户角色数据成功");
                } else if (flag == 2) {
                    synclog.setRemarks("系统异常，移除帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的" + existUserroles.size() + "个用户角色数据到TDS上失败");
                }
                em.persist(synclog);
            }
        }
        if (selectedRoleVos != null && selectedRoleVos.size() != 0) {
            List<Map<String, Object>> adAccountsCodes = new ArrayList<Map<String, Object>>();
            Map<String, Object> adAccountCodeMap = null;
            Rolemstr r = null;
            for (int i = 0; i < selectedRoleVos.size(); i++) {
                adAccountCodeMap = new HashMap<String, Object>();
                r = selectedRoleVos.get(i);
                if (null != userVo && null != r) {
                    String adAccount = tDSLocal.getUserFromTDS(tDSLocal.addPre(userVo.getUserName()));
                    String code = tDSLocal.getGroupFromTDS(tDSLocal.addPre(r.getCode()));
                    if (null != adAccount && !"".equals(adAccount) && null != code && !"".equals(code)) {
                        adAccountCodeMap = new HashMap<String, Object>();
                        adAccountCodeMap.put(userVo.getUserName(), r.getCode());
                        adAccountsCodes.add(adAccountCodeMap);
                    }
                }
            }
            if (null != adAccountsCodes && adAccountsCodes.size() != 0) {
                int flag = this.tDSLocal.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                Synclog synclog = new Synclog();
                synclog.setSyncType("同步TIH用户角色数据到TDS上");
                synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                synclog.setVersion(new Timestamp(new Date().getTime()));
                if (flag == 0) {
                    synclog.setRemarks("TDS连接失败，同步帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的" + selectedRoleVos.size() + "个用户角色数据失败");
                } else if (flag == 1) {
                    synclog.setRemarks("同步帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的" + selectedRoleVos.size() + "个用户角色数据成功");
                } else if (flag == 2) {
                    synclog.setRemarks("系统出现异常，同步帐号：" + userVo.getUserName() + "，用户姓名：" + userVo.getRealName() + "，员工号：" + userVo.getJobNumber() + "的" + selectedRoleVos.size() + "个用户角色数据到TDS上失败");
                }
                em.persist(synclog);
            }
        }
    }

}
