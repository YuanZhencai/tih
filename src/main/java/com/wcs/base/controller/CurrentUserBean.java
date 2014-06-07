/**
 * CurrentUserBean.java Created: 2012-1-30 上午11:20:07
 */
package com.wcs.base.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.base.util.JSFUtils;
import com.wcs.common.model.Resourcemstr;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Usermstr;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:chengchao@wcs-global.com">ChengChao</a>
 */
@ManagedBean(name = "currentUser")
@SessionScoped
public class CurrentUserBean implements Serializable {

    private static final long serialVersionUID = 9157970199871052634L;
    private Logger logger = LoggerFactory.getLogger(CurrentUserBean.class);

    @EJB
    private LoginService loginService;

    private static final String LOGIN_SUCCESS = "/template/template.xhtml";

    // 1级
    private List<TreeNode> topNavs;
    // 选中的1级菜单id
    private Long selectedTopNavId;
    // 2级
    private List<TreeNode> midNavs;

    // 当前用户的菜单树（1~3级）
    private List<TreeNode> menuTree;

    @PostConstruct
    void init() {
        menuTree = getCurrentMenus();
        topNavs = menuTree;
    }

    
    
    /**
     * <p>Description: 注销</p>
     */
    public void doLogout() {
        try {
            loginService.logout();
            String casServerUrl = JSFUtils.getParamValue("casServerUrl");
            String appUrl = JSFUtils.getParamValue("appUrl");
            if(casServerUrl != null && appUrl != null){
                JSFUtils.redirect(casServerUrl + "/logout?service=" + appUrl);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    /**
     * <p>Description: 生成菜单的1级和2级树</p>
     * @param topNavId
     */
    public void generateTree(Long topNavId) {
        selectedTopNavId = null;
        midNavs = new ArrayList<TreeNode>();
        if (null != menuTree && menuTree.size() != 0) {
            Resourcemstr r1 = null;
            for (TreeNode menu : menuTree) {
                r1 = (Resourcemstr) menu.getData();
                if (topNavId.equals(r1.getId())) {
                    midNavs = menu.getChildren();
                    selectedTopNavId = topNavId;
                }
            }
        }
    }

    /**
     * <p>Description: 点击以及菜单跳转链接</p>
     * @param topNavId
     * @return
     */
    public String clickMenu(TreeNode menu) {
        try {
            if(menu != null && menu.getData() != null){
                Resourcemstr r = (Resourcemstr) menu.getData();
                if(r.getParentId() == 0){
                    midNavs = menu.getChildren();
                }
            }
            sendRedirect(getAvailableUri(menu));
            return "";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return LOGIN_SUCCESS;
        }
    }

    public String getAvailableUri(TreeNode menu){
        String url = LOGIN_SUCCESS;
        if(menu.getData() == null){
        	return url;
        }
        Resourcemstr r = (Resourcemstr) menu.getData();
        if(r.getUri()!=null && !"".equals(r.getUri())){
            url = r.getUri();
        }else{
            List<TreeNode> children = menu.getChildren();
            if(children != null){
                for (TreeNode childMeun : children) {
                    return getAvailableUri(childMeun);
                }
            }
        }
        return url;
    }
    
    public void sendRedirect(String url){
        HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
        HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        String project = ((HttpServletRequest) request).getContextPath();
        try {
            ((HttpServletResponse) response).sendRedirect(project + url);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<TreeNode> getMenuTree() {
        return menuTree;
    }

    public void setMenuTree(List<TreeNode> menuTree) {
        this.menuTree = menuTree;
    }

    public List<TreeNode> getTopNavs() {
        return topNavs;
    }

    public void setTopNavs(List<TreeNode> topNavs) {
        this.topNavs = topNavs;
    }

    public Long getSelectedTopNavId() {
        return selectedTopNavId;
    }

    public void setSelectedTopNavId(Long selectedTopNavId) {
        this.selectedTopNavId = selectedTopNavId;
    }

    public List<TreeNode> getMidNavs() {
        return midNavs;
    }

    public void setMidNavs(List<TreeNode> midNavs) {
        this.midNavs = midNavs;
    }

    public String getCurrentUserName() {
        return loginService.getCurrentUserName();
    }

    public Usermstr getCurrentUsermstr() {
        return loginService.getCurrentUsermstr();
    }

    public List<Rolemstr> getCurrentRoles() {
        return loginService.getCurrentRoles();
    }

    public List<Resourcemstr> getCurrentResources() {
        return loginService.getCurrentResources();
    }

    public List<TreeNode> getCurrentMenus() {
        return loginService.getCurrentMenus();
    }
}
