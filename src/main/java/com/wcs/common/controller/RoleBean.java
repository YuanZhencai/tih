package com.wcs.common.controller;

import java.io.Serializable;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Resourcemstr;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.model.Roleresource;
import com.wcs.common.service.RoleAndUserToTdsService;
import com.wcs.common.service.RoleServcie;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: Role Managed Bean 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class RoleBean implements Serializable {
    private static final long serialVersionUID = 1L;
    // role service
    @EJB 
    private RoleServcie roleServcie;
    @EJB 
    private LoginService loginService;
    @EJB 
    private RoleAndUserToTdsService roleAndUserToTdsService;
    
    // index.xhtml; search conditions.
    private Map<String, String> query;
    // DataLazyModel for show results of the search
    private LazyDataModel<Rolemstr> roles;
    // current object of the Rolemstr
    private Rolemstr role;
    
    // Resources Tree Node root
    private TreeNode root;
    // Resources list
    private List<Resourcemstr> resources;
    // Selected Resources
    private TreeNode[] selectedNodes;
    
    // total nodes
    private List<TreeNode> totalNodes = new ArrayList<TreeNode>();
    
    // has been communicate resources
    private List<Roleresource> resourced;
    
    public RoleBean() { }
    
    @PostConstruct public void initRoleBean() {
        query = new HashMap<String, String>(2);
        search();
    }

    // reset search conditions
    public void reset() {
        query.clear();
    }
    // empty method for no action button
    public void editRole() {}
    
    /**
     * <p>Description: search roles by query conditions</p>
     */
    public void search() {
        roles = new LazyDataModel<Rolemstr>() {
            public List<Rolemstr> load(int arg0, int arg1, String arg2, SortOrder arg3, Map<String, String> arg4) {
                List<Rolemstr> list = roleServcie.search(query);
                setRowCount(list.size());
                if(getRowCount() > arg1) {
                    try {
                        return list.subList(arg0, arg0 + arg1);
                    } catch (IndexOutOfBoundsException e) {
                        return list.subList(arg0, arg0 + (getRowCount() % arg1));
                    }
                } else {
                    return list;
                }
            }
        };
    }
    
    /**
     * <p>Description: add role</p>
     */
    public void addRole() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(!(ValidateUtil.validateRequiredAndMax(context, role.getName(), "角色名称：", 20)
                & ValidateUtil.validateRequiredAndMax(context, role.getCode(), "角色编码：", 50)
                & ValidateUtil.validateRegex(context, role.getCode(), "角色编码：", "^[a-zA-Z]+$", "只允许填写英文字符")
                & ValidateUtil.validateMaxlength(context, role.getDesc(), "角色描述：", 50))){
        	return ;
        }
        
        role.setId(null);
        if(roleServcie.isRoleNameExist(role)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增错误:", "角色名称已经存在"));
            return;
        }
        role.setCreatedBy(loginService.getCurrentUserName());
        role.setCreatedDatetime(new Date());
        role.setUpdatedBy(loginService.getCurrentUserName());
        role.setUpdatedDatetime(new Date());
        
        try {
            roleServcie.insert(role);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新增角色:", e.getMessage()));
            return;
        }
        search();   //re search	
		roleAndUserToTdsService.synchronousRoleToTds("insert", role);
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "新增角色:", "操作成功."));
    }
    
    /**
     * <p>Description: edit role</p>
     */
    public void updateRole() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(!(ValidateUtil.validateRequiredAndMax(context, role.getName(), "角色名称：", 20)
                & ValidateUtil.validateRequiredAndMax(context, role.getCode(), "角色编码：", 50)
                & ValidateUtil.validateRegex(context, role.getCode(), "角色编码：", "^[a-zA-Z]+$", "只允许填写英文字符")
                & ValidateUtil.validateMaxlength(context, role.getDesc(), "角色描述：", 50))){
        	return ;
        }
        
        if(roleServcie.isRoleNameExist(role)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改角色:", "角色名称已经存在"));
            return;
        }
        role.setUpdatedBy(loginService.getCurrentUserName());
        role.setUpdatedDatetime(new Date());
        try {
            roleServcie.update(role);
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改角色:", e.getMessage()));
            return;
        }
        search();   //re search
        roleAndUserToTdsService.synchronousRoleToTds("update", role);
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改角色:", "操作成功."));
    }
    
    /**
     * <p>Description: communicate the role and resources</p>
     */
    public void roleResourceCommunicate() {
        if(role.getDefunctInd().equals("Y")) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "角色权限分配：", "当前角色为失效状态，不可分配资源"));
            return;
        }
        
        List<Resourcemstr> newRes = new ArrayList<Resourcemstr>();
        List<Roleresource> repeat = new ArrayList<Roleresource>();
        List<Roleresource> source = new ArrayList<Roleresource>();
        for(Roleresource r : resourced) {
            source.add(r);
        }
        
        for(int i = 0; i < selectedNodes.length; i ++) {
            boolean f = false;
            for(Roleresource r : resourced) {
                if((long) r.getResourcemstr().getId() == ((Resourcemstr) selectedNodes[i].getData()).getId()) {
                    repeat.add(r);
                    f = true;
                    break;
                }
            }
            if(!f) {
                newRes.add((Resourcemstr) selectedNodes[i].getData());
            }
        }
        source.removeAll(repeat);
        try {
            roleServcie.bindRoleToResources(source, repeat, newRes, role, loginService.getCurrentUserName());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "角色权限分配：", "请刷新重试。如果还没有解决问题，请联系系统管理员"));
            return;
        }
        
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "角色权限分配：", "操作成功."));
    }
    
    /**
     * <p>Description: find all useful resources</p>
     */
    public void searchResources() {
        // initial
        root = new DefaultTreeNode("root", null);
        totalNodes.clear();
        
        // build tree nodes
        resources = roleServcie.searchResources();
        for(Resourcemstr r : resources) {
            if(r.getParentId() == 0) {
                TreeNode tn = new DefaultTreeNode(r, root);
                totalNodes.add(tn);
                findSonResource(r, tn, resources);
            }
        }

        // old communicates
        resourced = roleServcie.searchResourcesByRole(role);
        
        // find those last son nodes
        for(int i = 0; i < resourced.size(); i ++) {
            Resourcemstr r = resourced.get(i).getResourcemstr();
            boolean f = false;
            for(Resourcemstr ri : resources) {
                if((long) r.getId() == ri.getParentId()) {
                    f = true;
                    break;
                }
            }
            if(!f) {    // is the last son nodes, then selected
                for(TreeNode n : totalNodes) {
                    if((long) ((Resourcemstr)n.getData()).getId() == (long) r.getId()) {
                        n.setSelected(true);
                        break;
                    }
                }
            }
        }
    }
    // 递归
    public void findSonResource(Resourcemstr r, TreeNode tn, List<Resourcemstr> rs) {
        for(Resourcemstr rm : rs) {
            if(rm.getParentId() == r.getId()) {
                TreeNode n = new DefaultTreeNode(rm, tn);
                totalNodes.add(n);
                findSonResource(rm, n, rs);
            }
        }
    }
    
    // Getter & Setter
    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public LazyDataModel<Rolemstr> getRoles() {
        return roles;
    }

    public void setRoles(LazyDataModel<Rolemstr> roles) {
        this.roles = roles;
    }

    public Rolemstr getRole() {
        if(this.role == null) {
            this.role = new Rolemstr();
            this.role.setDefunctInd("N");
        }
        return role;
    }

    public void setRole(Rolemstr role) {
        this.role = role;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<Resourcemstr> getResources() {
        return resources;
    }

    public void setResources(List<Resourcemstr> resources) {
        this.resources = resources;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<Roleresource> getResourced() {
        return resourced;
    }

    public void setResourced(List<Roleresource> resourced) {
        this.resourced = resourced;
    }

    public List<TreeNode> getTotalNodes() {
        return totalNodes;
    }

    public void setTotalNodes(List<TreeNode> totalNodes) {
        this.totalNodes = totalNodes;
    }
}
