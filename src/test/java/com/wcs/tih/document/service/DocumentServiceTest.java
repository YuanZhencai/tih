/**
 * DocumentServiceTest.java
 * Created: 2012-7-13 下午2:55:04
 */
package com.wcs.tih.document.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class DocumentServiceTest {

    /**
     * <p>Description: </p>
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * <p>Description: 根据用户帐号取名称</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getUsernameByAccount(java.lang.String)}.
     */
    @Test
    public void testGetUsernameByAccount() {
        
    }

    /**
     * <p>Description: Filenet ce 连接</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#connect()}.
     */
    @Test
    public void testConnect() {
        
    }

    /**
     * <p>Description: 用管理员做Filenet ce连接</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#adminConnect()}.
     */
    @Test
    public void testAdminConnect() {
        
    }

    /**
     * <p>Description: 查找文件夹，并构建成一棵树</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#findFolderTree(org.primefaces.model.TreeNode)}.
     */
    @Test
    public void testFindFolderTree() {
        
    }

    /**
     * <p>Description: 创建文件夹</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#createFolder(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testCreateFolder() {
        
    }

    /**
     * <p>Description: 修改文件夹</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#updateFolder(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testUpdateFolder() {
        
    }

    /**
     * <p>Description: 删除文件夹</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#deleteFolder(java.lang.String)}.
     */
    @Test
    public void testDeleteFolder() {
        
    }

    /**
     * <p>Description: 移动文件夹</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#moveFolder(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMoveFolder() {
        
    }

    /**
     * <p>Description: 复制文件夹</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#copyFolder(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testCopyFolder() {
        
    }

    /**
     * <p>Description: 复制文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#copyDocument(com.filenet.api.util.Id, java.lang.String)}.
     */
    @Test
    public void testCopyDocument() {
        
    }

    /**
     * <p>Description: 是否存在上级主管</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#isExistSupervisor(java.lang.String)}.
     */
    @Test
    public void testIsExistSupervisor() {
        
    }

    /**
     * <p>Description: 上传文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#uploadDocument(java.io.InputStream, java.util.Map, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testUploadDocument() {
        
    }

    /**
     * <p>Description: 下载文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#downloadDocument(java.lang.String)}.
     */
    @Test
    public void testDownloadDocument() {
        
    }

    /**
     * <p>Description: 文档权限复制查询</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getDocuments(java.util.Map)}.
     */
    @Test
    public void testGetDocumentsMapOfStringObject() {
        
    }

    /**
     * <p>Description: 文档查询</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getDocuments(com.wcs.tih.filenet.model.FnDocument, java.util.Map, java.lang.String)}.
     */
    @Test
    public void testGetDocumentsFnDocumentMapOfStringObjectString() {
        
    }

    /**
     * <p>Description: 将Filenet ce文档转为FnDocument模型</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#conversionDocument(com.filenet.api.core.Document)}.
     */
    @Test
    public void testConversionDocument() {
        
    }

    /**
     * <p>Description: 查询文件夹下的文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getDocumentsByFolder(java.lang.String)}.
     */
    @Test
    public void testGetDocumentsByFolder() {
        
    }

    /**
     * <p>Description: 查询文档类型的文件</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getDocumentsByCate(java.lang.String)}.
     */
    @Test
    public void testGetDocumentsByCate() {
        
    }

    /**
     * <p>Description: 检出文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#checkOutDoc(com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testCheckOutDoc() {
        
    }

    /**
     * <p>Description: 检出文档取消</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#cancelCheckOutDoc(com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testCancelCheckOutDoc() {
        
    }

    /**
     * <p>Description: 删除文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#deleteDoc(com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testDeleteDoc() {
        
    }

    /**
     * <p>Description: 移动文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#moveDoc(com.wcs.tih.filenet.model.FnDocument, java.lang.String)}.
     */
    @Test
    public void testMoveDoc() {
        
    }

    /**
     * <p>Description: 复制文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#copyDoc(com.wcs.tih.filenet.model.FnDocument, java.lang.String)}.
     */
    @Test
    public void testCopyDoc() {
        
    }

    /**
     * <p>Description: 判断文件夹内是否有重名文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#isNameExists(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testIsNameExists() {
        
    }

    /**
     * <p>Description: 修改文档属性</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#editDocProperty(com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testEditDocProperty() {
        
    }

    /**
     * <p>Description: 查询文档所有版本，排除检出版本</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getDocVersions(com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testGetDocVersions() {
        
    }

    /**
     * <p>Description: 查询文档权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getPermissions(com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testGetPermissions() {
        
    }

    /**
     * <p>Description: 设置文档权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#setPermissions(java.util.List, com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testSetPermissions() {
        
    }

    /**
     * <p>Description: 复制文档权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#copyPermissionsFromDocToDoc(com.wcs.tih.filenet.model.FnDocument, com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testCopyPermissions() {
        
    }

    /**
     * <p>Description: 查询用户和组</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getUserGroups(java.util.Map)}.
     */
    @Test
    public void testGetUserGroups() {
        
    }

    /**
     * <p>Description: 判断当前用户是否有该文档操作权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#isUserHasPermiss(java.lang.String, com.wcs.tih.filenet.model.FnDocument)}.
     */
    @Test
    public void testIsUserHasPermiss() {
        
    }

    /**
     * <p>Description: 查询公司</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getCompanys(java.util.Map)}.
     */
    @Test
    public void testGetCompanys() {
        
    }

    /**
     * <p>Description: 给文档赋权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#bindUserRoleToDoc(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testBindUserRoleToDoc() {
        
    }

    /**
     * <p>Description: 给文档赋管理组权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#bindAdimnToDoc(java.lang.String)}.
     */
    @Test
    public void testBindAdimnToDoc() {
        
    }

    /**
     * <p>Description: 收回用户对文档的操作权限</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#removeUserRoleFromDoc(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testRemoveUserRoleFromDoc() {
        
    }

    /**
     * <p>Description: 查询组下的所有用户</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#getAllUsersOfGroup(java.lang.String)}.
     */
    @Test
    public void testGetAllUsersOfGroup() {
        
    }

    /**
     * <p>Description: 检入文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#checkinDocument(java.io.InputStream, java.util.Map, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testCheckinDocument() {
        
    }

    /**
     * <p>Description: Filenet ce文档检入</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#checkinDoc(com.filenet.api.util.Id, com.filenet.api.util.Id)}.
     */
    @Test
    public void testCheckinDoc() {
        
    }

    /**
     * <p>Description: 替换文档</p>
     * Test method for {@link com.wcs.tih.document.service.DocumentService#replaceDocument(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReplaceDocument() {
        
    }

}
