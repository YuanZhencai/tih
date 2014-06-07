package com.wcs.common.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.PageModel;
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
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Usermstr;
import com.wcs.common.model.Userpositionorg;
import com.wcs.common.model.Userrole;
import com.wcs.common.service.UserService;
import com.wcs.tih.util.DateUtil;
import com.wcs.tih.util.ValidateUtil;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 用户Bean
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String ISSUCC = "issucc";
	@EJB
	private UserService userService;
	private UserSearchVo userSearchVo;
	private UsermstrVo selectedUsermstrVo;
	private LazyDataModel<UsermstrVo> lazyUsermstrVoModel;
	private UserVo userVo;
	private PSearchVo pSearchVo;
	private PVo selectedPVo;
	private LazyDataModel<PVo> lazyPVoModel;
	private LazyDataModel<UserPositionVo> lazyUserPositionVoModel;
	private UserPositionVo[] selectedUserPositionVos;
	private LazyDataModel<UserPositionVo> lazyPositionVoModel;
	private UserPositionVo[] selectedPositionVos;
	private Map<String, Long> roleVos;
	private List<Long> selectedRoleVos;
	private List<Position> positionList;
	private List<Rolemstr> roleList;
	private String positionName;
	private String userName;
	private String realName;
	private String excuteMethod;
	private boolean showBtn;
	private UserPositionVo searchUserPositionVo;

	/**
	 * <p>
	 * Description: 初始化
	 * </p>
	 */
	@PostConstruct
	public void init() {
		userVo = new UserVo();
		userSearchVo = new UserSearchVo();
		pSearchVo = new PSearchVo();
		searchUserPositionVo = new UserPositionVo();
		positionList = this.userService.getAllPosition();
		roleList = this.userService.getAllRole();
		queryUser();
	}

	/**
	 * <p>
	 * Description: 查询用户
	 * </p>
	 */
	public void queryUser() {
		List<UsermstrVo> uvs = userService.getAllUser(userSearchVo);
		lazyUsermstrVoModel = new PageModel<UsermstrVo>(uvs, false);
	}

	/**
	 * <p>
	 * Description: 选中后显示按钮
	 * </p>
	 * 
	 * @param event
	 */
	public void onRowSelectShowBtn(SelectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		PVo pVo = (PVo) event.getObject();
		if (null == pVo.getCup() || null == pVo.getCup().getId() || "".equals(pVo.getCup().getId())) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "错误", "该用户没有帐号，不能添加税务系统中，请重新选择！"));
			showBtn = true;
			return;
		}
		showBtn = false;
	}

	/**
	 * <p>
	 * Description: 增加用户
	 * </p>
	 */
	public void addUser() {
		showBtn = true;
		lazyPVoModel = null;
		selectedPVo = null;
		pSearchVo = new PSearchVo();
	}

	/**
	 * <p>
	 * Description:查询P
	 * </p>
	 */
	public void queryP() {
		showBtn = true;
		selectedPVo = null;
		List<PVo> pVs = userService.getAllP(pSearchVo);
		lazyPVoModel = new PageModel<PVo>(pVs, false);
	}

	/**
	 * <p>
	 * Description: 重置查询条件
	 * </p>
	 */
	public void resetUser() {
		userSearchVo = new UserSearchVo();
	}

	/**
	 * <p>
	 * Description:保存用户
	 * </p>
	 */
	public void saveUser() {
		userVo = new UserVo();
		userVo.setCertificatesType("identityCard");
		P p = selectedPVo.getP();
		if (p != null) {
			userVo.setSex(p.getGesch());
			userVo.setRealName(p.getNachn());
			userVo.setEmail(p.getEmail());
			userVo.setPhone(p.getCelno());
			userVo.setTelephone(p.getTelno());
			userVo.setCertificatesNumber(p.getIcnum());
		}
		CasUsrP cup = selectedPVo.getCup();
		if (cup != null) {
			userVo.setUserName(cup.getId());
			userVo.setJobNumber(cup.getPernr());
		}
		if (selectedPVo.getO() != null) {
			userVo.setOrganizationName(selectedPVo.getO().getStext());
		}
		userVo.setWorkDateTime(null);
		userVo.setBirthday(null);
		userVo.setEffective("N");
		userVo.setRemark("");
	}

	/**
	 * <p>
	 * Description:更新用户
	 * </p>
	 */
	public void updateUser() {
		Usermstr usermstr = selectedUsermstrVo.getUsermstr();
		P p = selectedUsermstrVo.getP();
		userVo = new UserVo();
		if (usermstr != null) {
			userVo.setUserName(usermstr.getAdAccount());
			userVo.setJobNumber(usermstr.getPernr());
			userVo.setCertificatesType(usermstr.getIdentityType());
			userVo.setCertificatesNumber(usermstr.getIdtentityId());
			if (usermstr.getOnboardDate() != null && !"".equals(usermstr.getOnboardDate())) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
				String dateStr = df.format(usermstr.getOnboardDate());
				try {
					userVo.setWorkDateTime(df.parse(dateStr));
				} catch (ParseException e) {
					userVo.setWorkDateTime(null);
					logger.error(e.getMessage(), e);
				}
			}
			userVo.setBirthday(usermstr.getBirthday());
			userVo.setEffective(usermstr.getDefunctInd());
			userVo.setRemark(usermstr.getBackgroundInfo());
			userVo.setPositionRemark(usermstr.getPositionRemark());
		}
		if (p != null) {
			userVo.setRealName(p.getNachn());
			userVo.setSex(p.getGesch());
			userVo.setEmail(p.getEmail());
			userVo.setPhone(p.getCelno());
			userVo.setTelephone(p.getTelno());
		}
		if (selectedUsermstrVo.getO() != null) {
			userVo.setOrganizationName(selectedUsermstrVo.getO().getStext());
		}
	}

	/**
	 * <p>
	 * Description:保存更新的用户
	 * </p>
	 */
	public void saveUpdateUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
		if (!ValidateUtil.validateStartTimeGTEndTime(context, userVo.getBirthday(), new Date(), "出生日期：", "不能大于当前日期")) {
			return;
		}
		if (!ValidateUtil.validateStartTimeGTEndTime(context, userVo.getBirthday(), userVo.getWorkDateTime(), "入职日期：", "不能小于出生日期")) {
			return;
		}
		if (null != userVo.getCertificatesNumber() && !"".equals(userVo.getCertificatesNumber().trim())) {
			if (userVo.getCertificatesNumber().length() > 50) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "证件号长度不能超过50个字符，请重新输入"));
				return;
			}
			userVo.setCertificatesNumber(userVo.getCertificatesNumber().trim());
		}
		if (null != userVo.getRemark() && !"".equals(userVo.getRemark().trim())) {
			if (userVo.getRemark().length() > 300) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "背景说明长度不能超过300个字符，请重新输入"));
				return;
			}
			userVo.setRemark(userVo.getRemark().trim());
		}
		if ("insert".equals(excuteMethod)) {
			try {
				userService.create(userVo);
				RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "添加帐号：" + userVo.getUserName() + "，姓名：" + userVo.getRealName() + "，员工号："
								+ userVo.getJobNumber() + "，用户信息成功，请查询并确认！"));
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
				return;
			}
		} else if ("update".equals(excuteMethod)) {
			try {
				userService.updateUser(selectedUsermstrVo.getUsermstr().getId(), userVo);
				RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "更新帐号：" + userVo.getUserName() + "，姓名：" + userVo.getRealName() + "，员工号："
								+ userVo.getJobNumber() + "，用户信息成功，请查询并确认！"));
				queryUser();
			} catch (Exception e) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
				return;
			}
		}
	}

	/**
	 * <p>
	 * Description: 用户岗位
	 * </p>
	 */
	public void userPosition() {
		userName = "";
		realName = "";
		selectedUserPositionVos = null;
		lazyUserPositionVoModel = null;
		Usermstr usermstr = selectedUsermstrVo.getUsermstr();
		if (usermstr != null) {
			userName = usermstr.getAdAccount();
		}
		if (selectedUsermstrVo.getP() != null) {
			realName = selectedUsermstrVo.getP().getNachn();
		}
		List<Userpositionorg> userpositionorgList = this.userService.getUserpositionorgByUsermstrId(usermstr.getId(), "N");
		Userpositionorg up = null;
		int size = userpositionorgList.size();
		if (size != 0) {
			List<UserPositionVo> userPositionVoList = new ArrayList<UserPositionVo>();
			if (selectedUserPositionVos == null || selectedUserPositionVos.length == 0) {
				selectedUserPositionVos = new UserPositionVo[size];
			}
			UserPositionVo userPositionVo = null;
			for (int i = 0; i < size; i++) {
				up = userpositionorgList.get(i);
				userPositionVo = new UserPositionVo();
				userPositionVo.setId(up.getId());
				if (up.getPositionorg() != null) {
					userPositionVo.setPositionorgId(up.getPositionorg().getId());
				}
				if (up.getPositionorg() != null && up.getPositionorg().getPosition() != null) {
					userPositionVo.setPositionName(up.getPositionorg().getPosition().getName());
				}
				if (up.getPositionorg().getOid() != null && !"".equals(up.getPositionorg().getOid())) {
					O o = userService.getOrganization("id", up.getPositionorg().getOid());
					if (o != null) {
						userPositionVo.setOrganizationName(o.getStext());
					}
				}
				userPositionVoList.add(userPositionVo);
				selectedUserPositionVos[i] = userPositionVo;
			}
			lazyUserPositionVoModel = new PageModel<UserPositionVo>(userPositionVoList, false);
		}
	}

	/**
	 * <p>
	 * Description: 保存用户岗位
	 * </p>
	 */
	public void saveUserPosition() {
		Usermstr usermstr = selectedUsermstrVo.getUsermstr();
		P p = selectedUsermstrVo.getP();
		FacesContext context = FacesContext.getCurrentInstance();
		RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
		try {
			this.userService.saveUserPosition(selectedUserPositionVos, this.selectedUsermstrVo.getUsermstr());
			RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "用户帐号：" + usermstr.getAdAccount() + "，姓名：" + p.getNachn()
					+ "，员工号：" + usermstr.getPernr() + "，分配岗位成功，请查询并确认！"));
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
		}
	}

	/**
	 * <p>
	 * Description:增加岗位
	 * </p>
	 */
	public void addPosition() {
		selectedPositionVos = null;
		lazyPositionVoModel = null;
		searchUserPositionVo = new UserPositionVo();
	}

	/**
	 * <p>
	 * Description:查询岗位
	 * </p>
	 */
	public void queryPosition() {
		selectedPositionVos = null;
		List<UserPositionVo> userPositionVos = this.userService.getAllPositionByName(searchUserPositionVo);
		if (userPositionVos != null && userPositionVos.size() != 0) {
			lazyPositionVoModel = new PageModel<UserPositionVo>(userPositionVos, false);
		} else {
			lazyPositionVoModel = null;
		}
	}

	/**
	 * <p>
	 * Description:保存增加的岗位
	 * </p>
	 */
	public void saveAddPosition() {
		List<UserPositionVo> list = new ArrayList<UserPositionVo>();
		if (selectedUserPositionVos != null && selectedUserPositionVos.length != 0) {
			for (UserPositionVo upv : selectedUserPositionVos) {
				list.add(upv);
			}
		}
		if (selectedPositionVos != null && selectedPositionVos.length != 0) {
			boolean b = false;
			for (UserPositionVo upv1 : selectedPositionVos) {
				b = false;
				if (selectedUserPositionVos != null && selectedUserPositionVos.length != 0) {
					for (UserPositionVo upv2 : selectedUserPositionVos) {
						if ((long) upv1.getPositionorgId() == (long) upv2.getPositionorgId()) {
							b = true;
							break;
						}
					}
				}
				if (!b) {
					list.add(upv1);
				}
			}
		}
		if (list.size() != 0) {
			selectedUserPositionVos = new UserPositionVo[list.size()];
			for (int i = 0; i < list.size(); i++) {
				selectedUserPositionVos[i] = list.get(i);
			}
			lazyUserPositionVoModel = new PageModel<UserPositionVo>(list, false);
		} else {
			selectedUserPositionVos = null;
			lazyUserPositionVoModel = null;
		}
	}

	/**
	 * <p>
	 * Description:用户角色
	 * </p>
	 */
	public void userRole() {
		userName = "";
		realName = "";
		roleVos = new HashMap<String, Long>();
		selectedRoleVos = new ArrayList<Long>();
		Usermstr usermstr = selectedUsermstrVo.getUsermstr();
		if (usermstr != null) {
			userName = usermstr.getAdAccount();
		}
		if (selectedUsermstrVo.getP() != null) {
			realName = selectedUsermstrVo.getP().getNachn();
		}
		List<Rolemstr> rolemstrList = this.userService.getAllRole();
		if (rolemstrList.size() != 0) {
			for (Rolemstr r : rolemstrList) {
				roleVos.put(r.getName(), r.getId());
			}
		}
		List<Userrole> userroleList = this.userService.getUserroleByUsermstrId(usermstr.getId(), "N");
		Userrole ur = null;
		if (userroleList != null && userroleList.size() != 0) {
			for (int i = 0; i < userroleList.size(); i++) {
				ur = userroleList.get(i);
				selectedRoleVos.add(ur.getRolemstr().getId());
			}
		}
	}

	/**
	 * <p>
	 * Description:保存用户角色
	 * </p>
	 */
	public void saveUserRole() {
		FacesContext context = FacesContext.getCurrentInstance();
		RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
		Usermstr usermstr = selectedUsermstrVo.getUsermstr();
		P p = selectedUsermstrVo.getP();
		try {
			this.userService.saveUserRole(selectedRoleVos, usermstr);
			RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "用户帐号：" + usermstr.getAdAccount() + "，姓名：" + p.getNachn()
					+ "，员工号：" + usermstr.getPernr() + "，分配角色成功，请查询并确认！"));
		} catch (Exception e) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
		}
	}

	public UserSearchVo getUserSearchVo() {
		return userSearchVo;
	}

	public void setUserSearchVo(UserSearchVo userSearchVo) {
		this.userSearchVo = userSearchVo;
	}

	public UsermstrVo getSelectedUsermstrVo() {
		return selectedUsermstrVo;
	}

	public void setSelectedUsermstrVo(UsermstrVo selectedUsermstrVo) {
		this.selectedUsermstrVo = selectedUsermstrVo;
	}

	public LazyDataModel<UsermstrVo> getLazyUsermstrVoModel() {
		return lazyUsermstrVoModel;
	}

	public void setLazyUsermstrVoModel(LazyDataModel<UsermstrVo> lazyUsermstrVoModel) {
		this.lazyUsermstrVoModel = lazyUsermstrVoModel;
	}

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	public PSearchVo getpSearchVo() {
		return pSearchVo;
	}

	public void setpSearchVo(PSearchVo pSearchVo) {
		this.pSearchVo = pSearchVo;
	}

	public PVo getSelectedPVo() {
		return selectedPVo;
	}

	public void setSelectedPVo(PVo selectedPVo) {
		this.selectedPVo = selectedPVo;
	}

	public LazyDataModel<PVo> getLazyPVoModel() {
		return lazyPVoModel;
	}

	public void setLazyPVoModel(LazyDataModel<PVo> lazyPVoModel) {
		this.lazyPVoModel = lazyPVoModel;
	}

	public LazyDataModel<UserPositionVo> getLazyUserPositionVoModel() {
		return lazyUserPositionVoModel;
	}

	public void setLazyUserPositionVoModel(LazyDataModel<UserPositionVo> lazyUserPositionVoModel) {
		this.lazyUserPositionVoModel = lazyUserPositionVoModel;
	}

	public UserPositionVo[] getSelectedUserPositionVos() {
		return selectedUserPositionVos;
	}

	public void setSelectedUserPositionVos(UserPositionVo[] selectedUserPositionVos) {
		this.selectedUserPositionVos = selectedUserPositionVos;
	}

	public LazyDataModel<UserPositionVo> getLazyPositionVoModel() {
		return lazyPositionVoModel;
	}

	public void setLazyPositionVoModel(LazyDataModel<UserPositionVo> lazyPositionVoModel) {
		this.lazyPositionVoModel = lazyPositionVoModel;
	}

	public UserPositionVo[] getSelectedPositionVos() {
		return selectedPositionVos;
	}

	public void setSelectedPositionVos(UserPositionVo[] selectedPositionVos) {
		this.selectedPositionVos = selectedPositionVos;
	}

	public Map<String, Long> getRoleVos() {
		return roleVos;
	}

	public void setRoleVos(Map<String, Long> roleVos) {
		this.roleVos = roleVos;
	}

	public List<Long> getSelectedRoleVos() {
		return selectedRoleVos;
	}

	public void setSelectedRoleVos(List<Long> selectedRoleVos) {
		this.selectedRoleVos = selectedRoleVos;
	}

	public List<Position> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

	public List<Rolemstr> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Rolemstr> roleList) {
		this.roleList = roleList;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getExcuteMethod() {
		return excuteMethod;
	}

	public void setExcuteMethod(String excuteMethod) {
		this.excuteMethod = excuteMethod;
	}

	public boolean isShowBtn() {
		return showBtn;
	}

	public void setShowBtn(boolean showBtn) {
		this.showBtn = showBtn;
	}

	public UserPositionVo getSearchUserPositionVo() {
		return searchUserPositionVo;
	}

	public void setSearchUserPositionVo(UserPositionVo searchUserPositionVo) {
		this.searchUserPositionVo = searchUserPositionVo;
	}

}
