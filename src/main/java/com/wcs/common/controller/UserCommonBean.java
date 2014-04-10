package com.wcs.common.controller;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.UserCommonFormItemsVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.service.UserCommonService;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "userCommonBean")
@ViewScoped
public class UserCommonBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @EJB
    private UserCommonService userCommonService;
    private UserCommonFormItemsVo userCommonFormItemsVo;
    private LazyDataModel<UsermstrVo> lazyUsermstrVoModel;
    private UsermstrVo selectedUsermstrVo;
    private UsermstrVo[] selectedUsermstrVos;
    private String updateComponent;
    private String selectionMode;
    private String bean;
    private String method;
	private List<UsermstrVo> usermstrVoList;

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init() {
        userCommonFormItemsVo = new UserCommonFormItemsVo();
        this.lazyUsermstrVoModel = null;
        this.selectedUsermstrVo = null;
        this.selectedUsermstrVos = null;
        this.usermstrVoList = null;
        this.updateComponent = "commonUserFormItems";
        this.selectionMode = "single";
    }
    
    /**
     * <p>Description: 初始化那些页面组件需要调用公用用户信息</p>
     * @param component 需要更新的组件
     * @param mode datatable显示的方式 checkbox或radio
     * @param bean ControllerBean的名称
     * @param method 方法名
     */
    public void init(String component, String mode, String bean, String method) {
        this.lazyUsermstrVoModel = null;
        this.selectedUsermstrVo = null;
        this.selectedUsermstrVos = null;
        this.userCommonFormItemsVo = new UserCommonFormItemsVo();
        this.updateComponent = component;
        this.selectionMode = mode;
        this.bean = bean;
        this.method = method;
    }
    
    /**
     * <p>Description: 取得用户真实名称,用户top页面展示</p>
     * @param adAccount 用户帐号
     */
    public String getUserRealName(String adAccount){
        P p = userCommonService.getUsermstrVo(adAccount).getP();
        return p == null ? adAccount:p.getNachn();
    }

    /**
     * <p>Description: 反射执行不同Controller指定的方法</p>
     */
    public void excuteControllerBeanMethod() {
        if (this.bean != null && !"".equals(this.bean)) {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.bean);
                o.getClass().getDeclaredMethod(this.method).invoke(o);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    /**
     * <p>Description: 反射执行不同Controller指定的带参数的方法。两个参数UsermstrVo selectedUsermstrVo,UsermstrVo[] selectedUsermstrVos</p>
     */
    @SuppressWarnings("rawtypes")
    public void excuteControllerBeanParamsMethod() {
        if (this.bean != null && !"".equals(this.bean)) {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.bean);
                // 设置参数类型
                Class[] parameterTypes = new Class[2];
                parameterTypes[0] = UsermstrVo.class;
                parameterTypes[1] = UsermstrVo[].class;
                // 给参数设值
                Object[] args = new Object[2];
                args[0] = selectedUsermstrVo;
                args[1] = selectedUsermstrVos;
                // 两种执行方式
                Method method = o.getClass().getMethod(this.method, parameterTypes);
                method.invoke(o, args);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * <p>Description: 查询公用的用户信息</p>
     */
    public void queryCommonUser() {
        this.selectedUsermstrVo = null;
        this.selectedUsermstrVos = null;
        this.lazyUsermstrVoModel = null;
        usermstrVoList = this.userCommonService.queryCommomUser(this.userCommonFormItemsVo);
        this.lazyUsermstrVoModel = new PageModel<UsermstrVo>(usermstrVoList, false);
    }

    public List<UsermstrVo> getUsermstrVoList() {
		return usermstrVoList;
	}

	public void setUsermstrVoList(List<UsermstrVo> usermstrVoList) {
		this.usermstrVoList = usermstrVoList;
	}

	public UserCommonFormItemsVo getUserCommonFormItemsVo() {
        return userCommonFormItemsVo;
    }

    public void setUserCommonFormItemsVo(UserCommonFormItemsVo userCommonFormItemsVo) {
        this.userCommonFormItemsVo = userCommonFormItemsVo;
    }

    public LazyDataModel<UsermstrVo> getLazyUsermstrVoModel() {
        return lazyUsermstrVoModel;
    }

    public void setLazyUsermstrVoModel(LazyDataModel<UsermstrVo> lazyUsermstrVoModel) {
        this.lazyUsermstrVoModel = lazyUsermstrVoModel;
    }

    public UsermstrVo getSelectedUsermstrVo() {
        return selectedUsermstrVo;
    }

    public void setSelectedUsermstrVo(UsermstrVo selectedUsermstrVo) {
        this.selectedUsermstrVo = selectedUsermstrVo;
    }

    public UsermstrVo[] getSelectedUsermstrVos() {
        return selectedUsermstrVos;
    }

    public void setSelectedUsermstrVos(UsermstrVo[] selectedUsermstrVos) {
        this.selectedUsermstrVos = selectedUsermstrVos;
    }

    public String getUpdateComponent() {
        return updateComponent;
    }

    public void setUpdateComponent(String updateComponent) {
        this.updateComponent = updateComponent;
    }

    public String getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(String selectionMode) {
        this.selectionMode = selectionMode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

}
