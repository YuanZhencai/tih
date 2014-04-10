package com.wcs.tih.document.controller.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.filenet.api.core.Folder;
import com.wcs.tih.filenet.model.FnDocument;

/**
 * Project: tih
 * Description: 封装Filenet上取得的Folder对象的一些基本属性，方便文件夹树操作
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class FolderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String folderName;
    private String pathName;
    private String createdBy;
    private Date createdDate;
    private List<FnDocument> subDocuments;
    private List<FolderVO> subFolders;

    public FolderVO() {
        this.subFolders = new ArrayList<FolderVO>();
    }
    public FolderVO(Folder f) {
        this();
        this.id = f.get_Id().toString();
        this.folderName = f.get_Name();
        this.pathName = f.get_PathName();
        this.createdBy = f.get_Creator();
        this.createdDate = f.get_DateCreated();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<FnDocument> getSubDocuments() {
        return subDocuments;
    }

    public void setSubDocuments(List<FnDocument> subDocuments) {
        this.subDocuments = subDocuments;
    }

    public List<FolderVO> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<FolderVO> subFolders) {
        this.subFolders = subFolders;
    }

}
