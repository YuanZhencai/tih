package com.wcs.common.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.tds.TdsUtilImpl;
import com.wcs.common.controller.vo.UserVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.P;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Synclog;
import com.wcs.common.model.Usermstr;
import com.wcs.common.model.Userrole;

/**
 * <p>Project: tih</p>
 * <p>Description: TDS</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class TDSServiceImpl implements TDSLocal {
    private static final String DELETE = "delete";

    private static final String UPDATE = "update";

    private static final String INSERT = "insert";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
	@PersistenceContext
	private EntityManager em;
	private static ResourceBundle rb = ResourceBundle.getBundle("default");
	
    /**
     * <p>Description: 给帐号增加前缀</p>
     * @param item  帐号
     * @return
     */
    public String addPre(String item) {
        TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
        try {
            tdsUtilImpl.connectTDS();
            String proItem = tdsUtilImpl.addPre(item);
            if (null != proItem && !"".equals(proItem)) { 
            	return proItem; 
            }
        } catch (Exception e) {
            return item;
        }
        return item;
    }

    /**
     * <p>Description: 批量给帐号增加前缀</p>
     * @param items
     * @return
     */
    public List<String> addPres(List<String> items) {
        TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
        try {
            tdsUtilImpl.connectTDS();
            List<String> proItems = tdsUtilImpl.addPreLis(items);
            if (null != proItems && proItems.size() != 0) { 
            	return proItems; 
            }
        } catch (Exception e) {
            return items;
        }
        return items;
    }

    /**
     * <p>Description: 移除帐号前缀</p>
     * @param item
     * @return
     */
    public String removePre(String item) {
        TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
        try {
            String proItem = tdsUtilImpl.removePre(item);
            if (null != proItem && !"".equals(proItem)) { 
            	return proItem; 
            }
        } catch (Exception e) {
            return item;
        }
        return item;
    }

    /**
     * <p>Description: 批量移除帐号前缀</p>
     * @param items
     * @return
     */
    public List<String> removePres(List<String> items) {
        TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
        try {
            tdsUtilImpl.connectTDS();
            List<String> proItems = tdsUtilImpl.removePreLis(items);
            if (null != proItems && proItems.size() != 0) { 
            	return proItems; 
            }
        } catch (Exception e) {
            return items;
        }
        return items;
    }

	/**
	 * 同步用户到TDS上
	 * 
	 * @param uv
	 *            用户 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示同步成功 ，2表示同步失败
	 */
	@Override
	public int synchronousUserToTds(UserVo uv, String action) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		try {
			boolean bb = false;
			String adAccount = this.getUserFromTDS(addPre(uv.getUserName()));
			if (adAccount != null && !"".equals(adAccount)) {
				bb = true;
			}
			if (action.equals(INSERT) || action.equals(UPDATE)) {
				Map<String, Object> tdsUser = new HashMap<String, Object>();
				tdsUser.put(rb.getString("attr_tds_person_cn").trim(), uv.getUserName());
				tdsUser.put(rb.getString("attr_tds_person_sn").trim(), uv.getUserName());
				tdsUser.put(rb.getString("attr_tds_person_uid").trim(), uv.getUserName());
				tdsUser.put(rb.getString("attr_tds_person_displayName").trim(),uv.getRealName());
				tdsUser.put(rb.getString("attr_tds_person_description").trim(),uv.getRemark());
				tdsUser.put(rb.getString("attr_tds_person_mail").trim(), uv.getEmail());
				tdsUser.put(rb.getString("attr_tds_person_telephoneNumber").trim(), uv.getTelephone());
				tdsUser.put(rb.getString("attr_tds_person_mobile").trim(), uv.getPhone());
				tdsUser.put(rb.getString("attr_tds_person_password").trim(), rb.getString("attr_tds_person_password_value").trim());
				if (bb) {
					// 存在就更新
					tdsUtilImpl.updateUser(tdsUser);
				} else {
					// 不存在就增加
					tdsUtilImpl.addUser(tdsUser);
				}
			}
			// 存在才删除
			if (action.equals(DELETE) && bb) {
				tdsUtilImpl.removeUser(uv.getUserName());
			}
			tdsUtilImpl.closeTDS();
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 2;
		}
	}

	/**
	 * 批量同步用户到TDS上
	 * 
	 * @param UsermstrVo
	 *            ： CasUsrP 、P 、Usermstr 信息 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示同步成功 ，2表示同步失败
	 */
	@Override
	public int batchSynchronousUserToTds(List<UsermstrVo> uvs, String action) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		try {
			if (uvs != null && uvs.size() != 0) {
				List<Map<String, Object>> insertTdsUsers = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> updateTdsUsers = new ArrayList<Map<String, Object>>();
				String adAccount = null;
				Map<String, Object> tdsUser = null;
				// 生成TDS需要的数据
				for (UsermstrVo uv : uvs) {
					tdsUser = new HashMap<String, Object>();
					tdsUser.put(rb.getString("attr_tds_person_cn").trim(), uv.getUsermstr().getAdAccount());
					tdsUser.put(rb.getString("attr_tds_person_sn").trim(), uv.getUsermstr().getAdAccount());
					tdsUser.put(rb.getString("attr_tds_person_uid").trim(), uv.getUsermstr().getAdAccount());
					tdsUser.put(rb.getString("attr_tds_person_displayName").trim(), uv.getP().getNachn());
					tdsUser.put(rb.getString("attr_tds_person_description").trim(), uv.getUsermstr().getBackgroundInfo());
					tdsUser.put(rb.getString("attr_tds_person_mail").trim(), uv.getP().getEmail());
					tdsUser.put(rb.getString("attr_tds_person_telephoneNumber").trim(), uv.getP().getTelno());
					tdsUser.put(rb.getString("attr_tds_person_mobile").trim(),uv.getP().getCelno());
					tdsUser.put(rb.getString("attr_tds_person_password").trim(),rb.getString("attr_tds_person_password_value").trim());
					// 取得用户是否存在
					adAccount = this.getUserFromTDS(uv.getUsermstr().getAdAccount());
					if (adAccount != null && !"".equals(adAccount)) {
						// 存在就更新
						updateTdsUsers.add(tdsUser);
					} else {
						// 不存在的就增加
						insertTdsUsers.add(tdsUser);
					}
				}
				// 增加或更新时验证是否存在 存在就更新 不存在就增加
				if (action.equals(INSERT) || action.equals(UPDATE)) {
					if (insertTdsUsers.size() != 0) {
						tdsUtilImpl.addUsers(insertTdsUsers);
					}
					if (updateTdsUsers.size() != 0) {
						tdsUtilImpl.updateUsers(updateTdsUsers);
					}
				}
				// 批量删除已经存在
				if (action.equals(DELETE)) {
					if (updateTdsUsers.size() != 0) {
						List<String> adAccounts = new ArrayList<String>();
						for (int i = 0; i < updateTdsUsers.size(); i++) {
							Map<String, Object> user = updateTdsUsers.get(i);
							adAccounts.add((String) user.get("uid"));
						}
						tdsUtilImpl.removeUsers(adAccounts);
					}
				}
			} else {
				tdsUtilImpl.closeTDS();
				return 3;
			}
			tdsUtilImpl.closeTDS();
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 2;
		}
	}

	/**
	 * 从TDS上取得用户
	 * 
	 * @param String
	 *            param 用户adAccount
	 * @return 用户adAccount
	 */
	@Override
	public String getUserFromTDS(String param) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "";
		}
		try {
			Map<String, Object> user = tdsUtilImpl.getUserByUid(param);
			tdsUtilImpl.closeTDS();
			if (user != null) {
				return (String) user.get("uid");
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "";
		}
	}

	

	/**
	 * 同步组到TDS上
	 * 
	 * @param Rolemstr
	 *            role 组信息 , String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加到TDS上成功 ，2表示增加到TDS上失败
	 */
	@Override
	public int synchronousGroupToTds(Rolemstr role, String action) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		try {
			boolean bb = false;
			String code = this.getGroupFromTDS(addPre(role.getCode()));
			if (code != null && !"".equals(code)) {
				bb = true;
			}
			if (action.equals(INSERT) || action.equals(UPDATE)) {
				Map<String, Object> tdsGroup = new HashMap<String, Object>();
				tdsGroup.put(rb.getString("attr_tds_group_cn").trim(),role.getCode());
				tdsGroup.put(rb.getString("attr_tds_group_description").trim(),role.getName());
				tdsGroup.put(rb.getString("attr_tds_group_uniqueMember").trim(), "");
				if (bb) {
					// 存在就更新
					tdsUtilImpl.updateGroup(tdsGroup);
				} else {
					// 不存在就增加
					tdsUtilImpl.addGroup(tdsGroup);
				}
			}
			// 存在才删除
			if (action.equals(DELETE) && bb) {
				tdsUtilImpl.removeGroup(role.getCode());
			}
			tdsUtilImpl.closeTDS();
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 2;
		}
	}

	/**
	 * 批量同步组到TDS上
	 * 
	 * @param Rolemstr
	 *            roles 组集合信息, String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加到TDS上成功 ，2表示增加到TDS上失败
	 */
	@Override
	public int batchSynchronousGroupToTds(List<Rolemstr> roles, String action) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		try {
			if (roles != null && roles.size() != 0) {
				List<Map<String, Object>> insertTdsGroups = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> updateTdsGroups = new ArrayList<Map<String, Object>>();
				String code = "";
				Map<String, Object> tdsGroup = null;
				for (Rolemstr role : roles) {
					tdsGroup = new HashMap<String, Object>();
					tdsGroup.put(rb.getString("attr_tds_group_cn").trim(),role.getCode());
					tdsGroup.put(rb.getString("attr_tds_group_description").trim(), role.getName());
					tdsGroup.put(rb.getString("attr_tds_group_uniqueMember").trim(), "");
					code = this.getGroupFromTDS(role.getCode());
					if (code != null && !"".equals(code)) {
						updateTdsGroups.add(tdsGroup);
					} else {
						insertTdsGroups.add(tdsGroup);
					}
				}
				// 存在就更新 不存在就增加
				if (action.equals(INSERT) || action.equals(UPDATE)) {
					if (insertTdsGroups.size() != 0) {
						tdsUtilImpl.addGroups(insertTdsGroups);
					}
					if (updateTdsGroups.size() != 0) {
						tdsUtilImpl.updateGroups(updateTdsGroups);
					}
				}
				// 存在才删除
				if (action.equals(DELETE)) {
					if (updateTdsGroups.size() != 0) {
						List<String> codes = new ArrayList<String>();
						for (int i = 0; i < updateTdsGroups.size(); i++) {
							Map<String, Object> group = updateTdsGroups.get(i);
							codes.add((String) group.get("cn"));
						}
						tdsUtilImpl.removeGroups(codes);
					}
				}
			} else {
				tdsUtilImpl.closeTDS();
				return 3;
			}
			tdsUtilImpl.closeTDS();
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 2;
		}
	}

	/**
	 * 从TDS上取得组
	 * 
	 * @param String
	 *            param 角色code
	 * @return 角色code
	 */
	@Override
	public String getGroupFromTDS(String param) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "";
		}
		try {
			Map<String, Object> group = tdsUtilImpl.getGroupByCN(param);
			tdsUtilImpl.closeTDS();
			if(group==null){
				return "";
			}
			return (String) group.get("cn");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "";
		}
	}

	/**
	 * 同步用户到组
	 * 
	 * @param String
	 *            adAccount, String code 用户的adAccount ，角色的code, String
	 *            action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加成功 ，2表示增加失败
	 */
	@Override
	public int synchronousUserToGroupToTDS(String adAccount, String code,
			String action) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(rb.getString("attr_tds_person_uid").trim(), adAccount);
			map.put(rb.getString("attr_tds_group_cn").trim(), code);
			if (action.equals(INSERT)) {
				tdsUtilImpl.removeUserToGroup(map);
				tdsUtilImpl.addUserToGroup(map);
			}
			if (action.equals(DELETE)) {
				tdsUtilImpl.removeUserToGroup(map);
			}
			tdsUtilImpl.closeTDS();
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 2;
		}
	}

	/**
	 * 批量同步用户到组
	 * 
	 * @param List
	 *            <Map<String, Object>> adAccountsCodes
	 *            表示用adAccount和角色code和Map对象 的List集合, String action表示执行什么方法
	 * @return int 0表示连接TDS失败 ，1表示增加成功 ，2表示增加失败
	 */
	@Override
	public int batchSynchronousUserToGroupToTDS(
			List<Map<String, Object>> adAccountsCodes, String action) {
		TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
		try {
			tdsUtilImpl.connectTDS();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 0;
		}
		try {
			if (adAccountsCodes != null && adAccountsCodes.size() != 0) {
				List<Map<String, Object>> tdsUserGroups = new ArrayList<Map<String, Object>>();
				Map<String, Object> map = null;
				String value = "";
				for (int i = 0; i < adAccountsCodes.size(); i++) {
					Map<String, Object> adAccountCode = adAccountsCodes.get(i);
					for (String key : adAccountCode.keySet()) {
						value = (String) adAccountCode.get(key);
						map = new HashMap<String, Object>();
						map.put(rb.getString("attr_tds_person_uid").trim(), key);
						map.put(rb.getString("attr_tds_group_cn").trim(), value);
						tdsUserGroups.add(map);
					}
				}
                tdsUtilImpl.addUsersToGroups(tdsUserGroups);
                if (action.equals(INSERT)) {
                    tdsUtilImpl.removeUsersToGroups(tdsUserGroups);
                    tdsUtilImpl.addUsersToGroups(tdsUserGroups);
                }
                if (action.equals(DELETE)) {
                    tdsUtilImpl.removeUsersToGroups(tdsUserGroups);
                }
			} else {
				tdsUtilImpl.closeTDS();
				return 3;
			}
			tdsUtilImpl.closeTDS();
			return 1;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return 2;
		}
	}

	/**
	 * 一键同步到TDS
	 * 
	 * @return int 0表示连接TDS失败 ，1表示增加成功 ，2表示增加失败
	 */
	@Override
    public String allSynchronous() {
        String msgs = "";
        int num1N = 0;
        int num2N = 0;
        int num3N = 0;
        int num3Y = 0;
        Synclog synclog = null;
        // 批量同步用户
        List<UsermstrVo> usermstrVosN = getAllUsermstrVo("N");
        if (usermstrVosN != null && usermstrVosN.size() != 0) {
            num1N = batchSynchronousUserToTds(usermstrVosN, INSERT);
            synclog = new Synclog();
            synclog.setSyncType("TIH->TDS");
            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
            synclog.setVersion(new Timestamp(new Date().getTime()));
            if (num1N == 0) {
                synclog.setRemarks("TDS连接失败，批量同步" + usermstrVosN.size() + "行用户数据到TDS上失败");
                msgs += "TDS连接失败，批量同步" + usermstrVosN.size() + "行用户数据到TDS上失败;";
            } else if (num1N == 1) {
                synclog.setRemarks("批量同步" + usermstrVosN.size() + "行用户数据到TDS上成功");
                msgs += "批量同步" + usermstrVosN.size() + "行用户数据到TDS上成功;";
            } else if (num1N == 2) {
                synclog.setRemarks("系统异常，批量同步" + usermstrVosN.size() + "行用户数据到TDS上失败");
                msgs += "系统异常，批量同步" + usermstrVosN.size() + "行用户数据到TDS上失败;";
            }
            saveSynclog(synclog);
        }
        // 不做删除操作,但是要删除用户组的关系
        List<UsermstrVo> usermstrVosY = getAllUsermstrVo("Y");
        if (usermstrVosY != null && usermstrVosY.size() != 0) {
            Usermstr usermstr = null;
            List<Userrole> urs = null;
            for (UsermstrVo uv : usermstrVosY) {
                usermstr = uv.getUsermstr();
                if (null != usermstr) {
                    urs = usermstr.getUserroles();
                    if (null != urs && urs.size() != 0) {
                        List<Map<String, Object>> adAccountsCodes = getUserRoleCodeMap(urs);
                        if (null!=adAccountsCodes&&adAccountsCodes.size() != 0) {
                            this.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                            int num = this.batchSynchronousUserToGroupToTDS(adAccountsCodes, DELETE);
                            synclog = new Synclog();
                            synclog.setSyncType("TIH->TDS");
                            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                            synclog.setVersion(new Timestamp(new Date().getTime()));
                            if (num == 0) {
                                synclog.setRemarks("TDS连接失败，批量从TDS上删除用户" + usermstr.getAdAccount() + "的" + urs.size() + "个组关系失败");
                                msgs += "TDS连接失败，批量从TDS上删除用户" + usermstr.getAdAccount() + "的" + urs.size() + "个组关系失败;";
                            } else if (num == 1) {
                                synclog.setRemarks("批量从TDS上删除用户" + usermstr.getAdAccount() + "的" + urs.size() + "个组关系成功");
                                msgs += "批量从TDS上删除用户" + usermstr.getAdAccount() + "的" + urs.size() + "个组关系成功;";
                            } else if (num == 2) {
                                synclog.setRemarks("系统异常，批量从TDS上删除用户" + usermstr.getAdAccount() + "的" + urs.size() + "个组关系失败");
                                msgs += "系统异常，批量从TDS上删除用户" + usermstr.getAdAccount() + "的" + urs.size() + "个组关系失败;";
                            }
                            saveSynclog(synclog);
                        }
                    }
                }
            }
        }
        // 批量同步角色
        List<Rolemstr> rolemstrsN = getAllRolemstr("N");
        if (rolemstrsN != null && rolemstrsN.size() != 0) {
            num2N = this.batchSynchronousGroupToTds(rolemstrsN, INSERT);
            synclog = new Synclog();
            synclog.setSyncType("TIH->TDS");
            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
            synclog.setVersion(new Timestamp(new Date().getTime()));
            if (num2N == 0) {
                synclog.setRemarks("TDS连接失败，批量同步" + rolemstrsN.size() + "行角色数据到TDS上失败");
                msgs += "TDS连接失败，批量同步" + rolemstrsN.size() + "行角色数据到TDS上失败;";
            } else if (num2N == 1) {
                synclog.setRemarks("批量同步" + rolemstrsN.size() + "行角色数据到TDS成功");
                msgs += "批量同步" + rolemstrsN.size() + "行角色数据到TDS上成功;";
            } else if (num2N == 2) {
                synclog.setRemarks("系统异常，批量同步" + rolemstrsN.size() + "行角色数据到TDS上失败");
                msgs += "系统异常，批量同步" + rolemstrsN.size() + "行角色数据到TDS上失败;";
            }
            saveSynclog(synclog);
        }
        // 不做删除操作,但需要删除角色和用户的关系
        List<Rolemstr> rolemstrsY = getAllRolemstr("Y");
        if (rolemstrsY != null && rolemstrsY.size() != 0) {
            List<Userrole> urs = null;
            for (Rolemstr r : rolemstrsY) {
                if (null != r) {
                    urs = r.getUserroles();
                    if (null != urs && urs.size() != 0) {
                        List<Map<String, Object>> adAccountsCodes = getUserRoleCodeMap(urs);
                        if (null!=adAccountsCodes&&adAccountsCodes.size() != 0) {
                            this.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                            int num = this.batchSynchronousUserToGroupToTDS(adAccountsCodes, DELETE);
                            synclog = new Synclog();
                            synclog.setSyncType("TIH->TDS");
                            synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                            synclog.setVersion(new Timestamp(new Date().getTime()));
                            if (num == 0) {
                                synclog.setRemarks("TDS连接失败，批量从TDS上删除组" + r.getCode() + "的" + urs.size() + "个用户关系失败");
                                msgs += "TDS连接失败，批量从TDS上删除组" + r.getCode() + "的" + urs.size() + "个用户关系失败;";
                            } else if (num == 1) {
                                synclog.setRemarks("批量从TDS上删除组" + r.getCode() + "的" + urs.size() + "个用户关系成功");
                                msgs += "批量从TDS上删除用户" + r.getCode() + "的" + urs.size() + "个用户关系成功;";
                            } else if (num == 2) {
                                synclog.setRemarks("系统异常，批量从TDS上删除组" + r.getCode() + "的" + urs.size() + "个用户关系失败");
                                msgs += "系统异常，批量从TDS上删除组" + r.getCode() + "的" + urs.size() + "个用户关系失败;";
                            }
                            saveSynclog(synclog);
                        }
                    }
                }
            }
        }
        // 批量同步用户角色关系
        List<Userrole> ursN = this.getAllUserrole("N");
        if (ursN != null && ursN.size() != 0) {
            List<Map<String, Object>> adAccountsCodes = getUserRoleCodeMap(ursN);
            if (adAccountsCodes.size() != 0) {
                num3N = this.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                synclog = new Synclog();
                synclog.setSyncType("TIH->TDS");
                synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                synclog.setVersion(new Timestamp(new Date().getTime()));
                if (num3N == 0) {
                    synclog.setRemarks("TDS连接失败，批量同步" + adAccountsCodes.size() + "行角色用户关系数据到TDS上失败");
                    msgs += "TDS连接失败，批量同步" + adAccountsCodes.size() + "行角色用户关系数据到TDS上失败;";
                } else if (num3N == 1) {
                    synclog.setRemarks("批量同步" + adAccountsCodes.size() + "行角色用户关系数据到TDS成功");
                    msgs += "批量同步" + adAccountsCodes.size() + "行角色用户关系数据到TDS上成功;";
                } else if (num3N == 2) {
                    synclog.setRemarks("系统异常，批量同步" + adAccountsCodes.size() + "行角色用户关系数据到TDS上失败");
                    msgs += "系统异常，批量同步" + adAccountsCodes.size() + "行角色用户关系数据到TDS上失败;";
                }
                saveSynclog(synclog);
            }
        }
        List<Userrole> ursY = this.getAllUserrole("Y");
        if (ursY != null && ursY.size() != 0) {
            List<Map<String, Object>> adAccountsCodes = getUserRoleCodeMap(ursY);
            if (null!=adAccountsCodes&&adAccountsCodes.size() != 0) {
                this.batchSynchronousUserToGroupToTDS(adAccountsCodes, INSERT);
                num3Y = this.batchSynchronousUserToGroupToTDS(adAccountsCodes, DELETE);
                synclog = new Synclog();
                synclog.setSyncType("TIH->TDS");
                synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                synclog.setVersion(new Timestamp(new Date().getTime()));
                if (num3Y == 0) {
                    synclog.setRemarks("TDS连接失败，批量从TDS上删除" + adAccountsCodes.size() + "行角色用户关系数据失败");
                    msgs += "TDS连接失败，批量从TDS上删除" + adAccountsCodes.size() + "行角色用户关系数据失败;";
                } else if (num3Y == 1) {
                    synclog.setRemarks("批量从TDS上删除" + adAccountsCodes.size() + "行角色用户关系数据成功");
                    msgs += "批量从TDS上删除" + ursY.size() + "行角色用户关系数据成功;";
                } else if (num3Y == 2) {
                    synclog.setRemarks("系统异常，批量从TDS上删除" + adAccountsCodes.size() + "行角色用户关系数据失败");
                    msgs += "系统异常，批量从TDS上删除" + adAccountsCodes.size() + "行角色用户关系数据失败;";
                }
                saveSynclog(synclog);
            }
        }
        return msgs;
    }
	
    public List<Map<String, Object>> getUserRoleCodeMap(List<Userrole> urs) {
        TdsUtilImpl tdsUtilImpl = new TdsUtilImpl();
        List<Map<String, Object>> adAccountsCodes = new ArrayList<Map<String, Object>>();
        try {
            tdsUtilImpl.connectTDS();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return adAccountsCodes;
        }
        Map<String, Object> adAccountCodeMap = null;
        if (null != urs && urs.size() != 0) {
            logger.info("已存在用户角色关系：" + urs + "  " + urs.size());
            for (Userrole ur : urs) {
                if (null != ur && null != ur.getUsermstr() && null != ur.getRolemstr()) {
                    // 判断用户和组是否存在TDS上
                    String adAccount = this.getUserFromTDS(this.addPre(ur.getUsermstr().getAdAccount()));
                    String code = this.getGroupFromTDS(this.addPre(ur.getRolemstr().getCode()));
                    logger.info("判断用户和角色是否存在TDS上  adAccount = " + adAccount + "  code = " + code);
                    if (null != adAccount && !"".equals(adAccount) && null != code && !"".equals(code)) {
                        adAccountCodeMap = new HashMap<String, Object>();
                        adAccountCodeMap.put(ur.getUsermstr().getAdAccount(), ur.getRolemstr().getCode());
                        adAccountsCodes.add(adAccountCodeMap);
                    }
                }
            }
        }
        try {
            tdsUtilImpl.closeTDS();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        logger.info("adAccountsCodes.size() = " + adAccountsCodes.size());
        return adAccountsCodes;
    }

    private List<UsermstrVo> getAllUsermstrVo(String defunctInd) {
        List<Usermstr> usermstrs = this.em.createQuery("select u from Usermstr u where u.defunctInd = '" + defunctInd + "'").getResultList();
        List<UsermstrVo> usermstrVos = new ArrayList<UsermstrVo>();
        UsermstrVo uv = null;
        if (usermstrs != null && usermstrs.size() != 0) {
            for (Usermstr u : usermstrs) {
                uv = new UsermstrVo();
                uv.setUsermstr(u);
                String sql = "select cup from CasUsrP cup where cup.id = '" + u.getAdAccount() + "'";
                List<CasUsrP> casUsrPs = this.em.createQuery(sql).getResultList();
                if (null != casUsrPs && casUsrPs.size() == 1) {
                    CasUsrP cup = casUsrPs.get(0);
                    uv.setCup(cup);
                    if (null != cup.getPernr() && !"".equals(cup.getPernr())) {
                        try {
                            uv.setP(this.em.find(P.class, cup.getPernr()));
                        } catch (Exception e) {
                            uv.setP(new P());
                            uv.getP().setNachn(u.getAdAccount());
                        }
                    } else {
                        uv.setP(new P());
                        uv.getP().setNachn(u.getAdAccount());
                    }
                } else {
                    uv.setCup(new CasUsrP());
                    uv.setP(new P());
                    uv.getP().setNachn(u.getAdAccount());
                }
                usermstrVos.add(uv);
            }
        }
        return usermstrVos;
    }

	private List<Rolemstr> getAllRolemstr(String defunctInd) {
		return this.em.createQuery("select r from Rolemstr r where r.defunctInd = '" + defunctInd+ "'").getResultList();
	}

	private List<Userrole> getAllUserrole(String defunctInd) {
		return this.em.createQuery("select ur from Userrole ur where ur.defunctInd = '"+ defunctInd + "'").getResultList();
	}
	
	/**
	 * 保存日志信息
	 * 
	 * @param sl
	 *            日志对象
	 * 
	 * @return 返回boolean
	 */
	public boolean saveSynclog(Synclog sl) {
		boolean b = false;
		try {
			this.em.persist(sl);
			b = true;
		} catch (Exception e) {
			b = false;
		}
		return b;
	}
}
