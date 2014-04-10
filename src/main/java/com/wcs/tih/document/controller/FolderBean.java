package com.wcs.tih.document.controller;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.document.controller.vo.FolderVO;
import com.wcs.tih.document.service.DocumentService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: 文件夹管理
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class FolderBean implements Serializable {
    private static final String MOVE_FOLDER = "移动文件夹：";
    private static final String PLEASE_SELECT_FOLDER = "请选中要操作的文件夹";
    private static final long serialVersionUID = 1L;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String SUCCESS = "success";
    @EJB
    protected   DocumentService documentService;

    // 根Folder->ObjectStore
    protected   TreeNode    rootFolder;  
    // 当前选中的Folder
    protected   TreeNode    selectedFolder; 
 // 当前Folder编辑模式
    protected   String      folderModel; 
 // 新增的Folder
    protected   FolderVO    folderVO;       
    
 // 选中此结点
    protected   TreeNode    folderSelected; 
  //新增文件夹变量
    protected   String  newFolderName, newFolderParentPath; 
    // 删除标识
    protected   String  deleteModel;   
    
    protected   String  username, password, classid;
 // 资源文件名称
    protected   String  configFileName = "filenet";    
 // 资源获取
    protected   ResourceBundle rb = ResourceBundle.getBundle(configFileName);   
 // 固有文件夹
    protected   Map<String, String> fixedFolder = new HashMap<String, String>();
    
    public FolderBean() {}
    
    @PostConstruct 
    public void initFolderBean() {
        // 此处增加判断，如果是管理员则做此操作；如果非管理员，跳过文件夹树构建
        initFolderTree();
        
        initFixedFolder();
        
        username = rb.getString("admin.id");
        password = rb.getString("admin.password");
        classid = rb.getString("ce.document.classid");
    }

    // 创建根文件夹树结构
    public void initFolderTree() {
        // 初始化文件夹选择树。
        
        if(documentService.isAdmin()){
        	FolderVO root = new FolderVO();
        	root.setFolderName("/");
        	root.setPathName("/");
        	rootFolder = new DefaultTreeNode(root, null);
        	try {
        		
        		documentService.findFolderTree(rootFolder);
        	} catch (Exception e) {
        		FacesContext.getCurrentInstance().addMessage(null,
        				new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
        	}
        }
    }
    
    public void refreshTree() {
        String pathName = "";
        if(selectedFolder != null) {
            pathName = ((FolderVO) selectedFolder.getData()).getPathName();
        }
        initFolderTree();
        if(!"".equals(pathName)) {
            TreeNode t = expandFolder(rootFolder, pathName);
            if(t != null) {
                while(t.getParent() != null) {
                    logger.info("TreeNode:\t" + t);
                    t = t.getParent();
                    t.setExpanded(true);
                }
            }
        }
        
    }
    private TreeNode expandFolder(TreeNode rf, String pn) {
        if(((FolderVO) rf.getData()).getPathName().equals(pn)) {
            rf.setSelected(true);
            return rf;
        }
        for( TreeNode t : rf.getChildren()) {
            expandFolder(t, pn);
        }
        return null;
    }
    
    // 确定系统预设的文件夹集合
    public void initFixedFolder() {
        Enumeration<String> keys = rb.getKeys();
        String key;
        while(keys.hasMoreElements()) {
            key = keys.nextElement();
            if(key.matches("ce.folder.*")){
            	fixedFolder.put(key, rb.getString(key));
            }
        }
    }
    
    public boolean isSelected() {
        return selectedFolder.equals(rootFolder) || selectedFolder==null ? false : true;
    }
    
    /**
     * <p>Description: 判断文件夹是否为已经确定的文件夹</p>
     * @param folderName
     * @return
     */
    public boolean folderIsFixed(String folderName) {
        return fixedFolder.containsValue(folderName);
    }
    /**
     * <p>Description: 初始化添加文件夹</p>
     */
    public void initAddFolder(){
        newFolderParentPath = selectedFolder == null ? "/" : ((FolderVO) selectedFolder.getData()).getPathName();
        newFolderName = "";
    }
    /**
     * <p>Description: 重新选择父文件夹</p>
     */
    public void selectParentFolder() {
        newFolderParentPath = ((FolderVO) folderSelected.getData()).getPathName();
    }
    /**
     * <p>Description: 新增文件夹, 只有管理员可以操作</p>
     */
    public void addFolder() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(!(ValidateUtil.validateRequired(context, newFolderParentPath, "文件夹位置：")
                & ValidateUtil.validateRequiredAndMax(context, newFolderName, "文件夹名称：", 50))){
        	return;
        }
        try {
            documentService.createFolder(newFolderName, newFolderParentPath);
            // 赋权限
        } catch (Exception e) {
            logger.error(e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "新建文件夹：", e.getMessage()));
            return;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "新建文件夹：", "操作成功"));
        RequestContext.getCurrentInstance().addCallbackParam("addFolder", SUCCESS);
        initFolderTree();
    }
    /**
     * <p>Description: 修改文件夹名称</p>
     */
    public void editFolder() {
        if(!isSelected()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PLEASE_SELECT_FOLDER, ""));
            return;
        }

        FolderVO fvo = ((FolderVO) selectedFolder.getData());
        if(folderIsFixed(fvo.getPathName())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "修改文件夹：", "操作失败。系统文件夹不可修改。"));
            return;
        }
        try {
            documentService.updateFolder(newFolderName, fvo.getPathName());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "修改文件夹：", e.getMessage()));
            return;
        }
        selectedFolder = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "修改文件夹：", "操作成功"));
        RequestContext.getCurrentInstance().addCallbackParam("editFolder", SUCCESS);
        initFolderTree();
    }
    /**
     * <p>Description: 删除文件夹, 只能文档管理员可以操作</p>
     * 1.  被删除的文件夹要求必须为空文件夹。
     * 还有就是当有文件检出时，文档不能被复制、移动、删除,包含此文档的文件夹也不能被复制、移动、删除
     */
    public void deleteFolder() {
        if(!isSelected()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PLEASE_SELECT_FOLDER, ""));
            return;
        }
        String folderPath = ((FolderVO) selectedFolder.getData()).getPathName();
        if(folderIsFixed(folderPath)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除文件夹：", "操作失败，不允许删除系统文件夹。请谨慎操作"));
            return;
        }
        try {
            documentService.deleteFolder(folderPath);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除文件夹：", e.getMessage()));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "删除文件夹：", "操作成功。"));
        initFolderTree();
    }
    /**
     * <p>Description: 复制文件夹</p>
     * 1.  复制文件夹时不允许需复制的文件夹和目标文件夹下的子文件夹同名。
     * 2.  复制文件夹包含了此文件夹下的文档和各级子文件夹及其中文档的复制。
     * 还有就是当有文件检出时，文档不能被复制、移动、删除,包含此文档的文件夹也不能被复制、移动、删除
     */
    public void copyFolder() {
        if(!isSelected()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PLEASE_SELECT_FOLDER, ""));
            return;
        }
        String sourcePath = ((FolderVO) selectedFolder.getData()).getPathName();
        String targetPath = ((FolderVO) folderSelected.getData()).getPathName();
        if(targetPath.equals(sourcePath)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "复制文件夹：", "操作失败，请选择一个目标文件夹"));
            return;
        }
        try {
            documentService.copyFolder(sourcePath, targetPath);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "复制文件夹：", e.getMessage()));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "复制文件夹：", "操作成功。"));
        RequestContext.getCurrentInstance().addCallbackParam("option", SUCCESS);
        initFolderTree();
    }
    /**
     * <p>Description: 移动文件夹</p>
     * 1.  移动文件夹时不允许需移动的文件夹和目标文件夹下的子文件夹同名。
     * 2.  移动文件夹包含了此文件夹下的文档和各级子文件夹及其中文档移动。
     * 4.  移动文档和移动文件夹后源文档和源文件夹（包括其中的文档和子文件夹）都被删除。
     * 还有就是当有文件检出时，文档不能被复制、移动、删除,包含此文档的文件夹也不能被复制、移动、删除
     */
    public void moveFolder() {
        if(!isSelected()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, PLEASE_SELECT_FOLDER, ""));
            return;
        }
        String sourcePath = ((FolderVO) selectedFolder.getData()).getPathName();
        String targetPath = ((FolderVO) folderSelected.getData()).getPathName();
        if(targetPath.equals(sourcePath)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, MOVE_FOLDER, "操作失败，请选择一个目标文件夹"));
            return;
        }
        if(folderIsFixed(sourcePath)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, MOVE_FOLDER, "操作失败，不允许移动系统文件夹。请谨慎操作"));
            return;
        }
        try {
            documentService.moveFolder(sourcePath, targetPath);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, MOVE_FOLDER, e.getMessage()));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, MOVE_FOLDER, "操作成功。"));
        RequestContext.getCurrentInstance().addCallbackParam("option", SUCCESS);
        initFolderTree();
    }
    
    // Getter && Setter
    public TreeNode getRootFolder() {
        return rootFolder;
    }
    
    public void setRootFolder(TreeNode rootFolder) {
        this.rootFolder = rootFolder;
    }
    
    public TreeNode getSelectedFolder() {
        return selectedFolder;
    }
    
    public void setSelectedFolder(TreeNode selectedFolder) {
        this.selectedFolder = selectedFolder;
    }
    
    public String getFolderModel() {
        return folderModel;
    }
    
    public void setFolderModel(String folderModel) {
        this.folderModel = folderModel;
    }
    
    public FolderVO getFolderVO() {
        return folderVO;
    }
    
    public void setFolderVO(FolderVO folderVO) {
        this.folderVO = folderVO;
    }
    
    public String getDeleteModel() {
        return deleteModel;
    }
    
    public void setDeleteModel(String deleteModel) {
        this.deleteModel = deleteModel;
    }
    
    public Map<String, String> getFixedFolder() {
        return fixedFolder;
    }
    
    public void setFixedFolder(Map<String, String> fixedFolder) {
        this.fixedFolder = fixedFolder;
    }

    public String getNewFolderName() {
        return newFolderName;
    }

    public void setNewFolderName(String newFolderName) {
        this.newFolderName = newFolderName;
    }

    public String getNewFolderParentPath() {
        return newFolderParentPath;
    }

    public void setNewFolderParentPath(String newFolderParentPath) {
        this.newFolderParentPath = newFolderParentPath;
    }

    public TreeNode getFolderSelected() {
        return folderSelected;
    }

    public void setFolderSelected(TreeNode folderSelected) {
        this.folderSelected = folderSelected;
    }
    
}
