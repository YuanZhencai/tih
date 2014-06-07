package com.wcs.tih.system.controller;

import java.util.Locale;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.TreeNode;

import com.wcs.tih.system.service.PositionProfessionInterface;

/**
 * <p>Project: tih</p>
 * <p>Description: 岗位业务ControllerBean类</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "positionProfessionBean")
@ViewScoped
public class PositionProfessionBean {
    @EJB
    private PositionProfessionInterface positionProfessionService;
    private TreeNode taxationRoot;
    private TreeNode[] selectedTaxationNodes;

    private TreeNode documentTypeRoot;
    private TreeNode[] selectedDocumentTypeNodes;

    private long selectedPositionId;
    private String selectedPositionName;

    /**
     * <p>Description: 初始化岗位的业务</p>
     * @param selectedPositionId 选中的岗位ID
     * @param selectedPositionName 选中的岗位名称
     */
    public void init(long selectedPositionId, String selectedPositionName) {
        this.selectedPositionId = selectedPositionId;
        this.selectedPositionName = selectedPositionName;
        Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        this.taxationRoot = positionProfessionService.createTaxationTree(browserLang,selectedPositionId);
        this.documentTypeRoot = positionProfessionService.createDocumentTypeTree(browserLang,selectedPositionId);
    }

    /**
     * <p>Description: 保存岗位业务信息</p>
     */
    public void savePositionProfession() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            positionProfessionService.savePositionProfession(selectedPositionId, selectedTaxationNodes, selectedDocumentTypeNodes);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "岗位：" + selectedPositionName + ",分配业务成功,请查询并确认！"));
            RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "岗位：" + selectedPositionName + "，" + e.getMessage()));
        }
    }

    public TreeNode getTaxationRoot() {
        return taxationRoot;
    }

    public void setTaxationRoot(TreeNode taxationRoot) {
        this.taxationRoot = taxationRoot;
    }

    public TreeNode[] getSelectedTaxationNodes() {
        return selectedTaxationNodes;
    }

    public void setSelectedTaxationNodes(TreeNode[] selectedTaxationNodes) {
        this.selectedTaxationNodes = selectedTaxationNodes;
    }

    public TreeNode getDocumentTypeRoot() {
        return documentTypeRoot;
    }

    public void setDocumentTypeRoot(TreeNode documentTypeRoot) {
        this.documentTypeRoot = documentTypeRoot;
    }

    public TreeNode[] getSelectedDocumentTypeNodes() {
        return selectedDocumentTypeNodes;
    }

    public void setSelectedDocumentTypeNodes(TreeNode[] selectedDocumentTypeNodes) {
        this.selectedDocumentTypeNodes = selectedDocumentTypeNodes;
    }

    public long getSelectedPositionId() {
        return selectedPositionId;
    }

    public void setSelectedPositionId(long selectedPositionId) {
        this.selectedPositionId = selectedPositionId;
    }

    public String getSelectedPositionName() {
        return selectedPositionName;
    }

    public void setSelectedPositionName(String selectedPositionName) {
        this.selectedPositionName = selectedPositionName;
    }

}
