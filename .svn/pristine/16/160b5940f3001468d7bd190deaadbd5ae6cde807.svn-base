package com.wcs.tih.filenet.ce.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Remote;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.VersionableSet;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;

@Local
public interface CEserviceLocal {

	/**
	 * 创建根目录
	 * 
	 * @param folderName
	 * @return Folder
	 */
	public abstract Folder createRootFolder(String folderName);

	/**
	 * 创建子目录
	 * 
	 * @param parentFolder
	 * @param folderName
	 * @return
	 */
	public abstract Folder createSubFolder(Folder parentFolder, String folderName);

	/**
	 * 获得文件夹
	 * 
	 * @param folderName
	 * @return
	 */
	public abstract Folder getFolder(String folderName) throws Exception;

	/**
	 * 删除文件夹
	 * 
	 * @param id
	 * @param folderToDelete
	 */
	public abstract void deleteFolder(Id id);

	/**
	 * 删除文件夹
	 * 
	 * @param foldername
	 */
	public abstract void deleteFolder(String foldername);

	/**
	 * 获得子文件夹
	 * 
	 * @param parentFolderName
	 * @return
	 */
	public abstract List<Folder> getSubFolders(String parentFolderName);

	/**
	 * 获得文件夹中的文件
	 * 
	 * @param parentFolder
	 * @return
	 */
	public abstract List<Document> getDocsInThisFolder(String parentFolder);

	/**
	 * 获得文件夹中的对象
	 * 
	 * @param parentFolder
	 */
	public abstract List<IndependentObject> getFolderContainees(String parentFolder);

	/**
	 * @param filestream
	 *            : 文件流
	 * @param propertiesMap
	 *            : 文件属性集合
	 * @param mimeType
	 *            : 文件类型(如:"image/jpeg")
	 * @param classId
	 *            : fileNet document class
	 * @param foldername
	 *            : 文件夹路径(如:"/Travel Document Upload")
	 * @return
	 */
	public abstract Document createDocument(InputStream filestream, Map propertiesMap, String mimeType, String classId, String foldername, String documentTitle);

	/**
	 * @param id
	 * @return
	 */
	public abstract Document getDocument(Id id) throws Exception;

	/**
	 * @param path
	 * @return
	 */
	public abstract Document getDocument(String path);

	public abstract void getDocumentContent(Document document) throws Exception;

	public abstract OutputStream getContentStream(ContentTransfer content) throws Exception;

	/**
	 * @param sqlStr: 查询语句(如："select * from Document where Creator='administrator'")
	 */
	public abstract DocumentSet searchDocuments(String sqlStr, Integer pageSize);

	/** 返回文件版本
	 * @param documentPath
	 * @return
	 */
	public abstract VersionableSet getDocumentVersions(String documentPath);

	//
	/**
	 * @param documentPath
	 * @return
	 */
	public abstract Document demoteVersions(String documentPath);

	/**
	 * @param documentPath
	 * @return
	 */
	public abstract Document promoteVersions(String documentPath);

	/**
	 * @param documentPath
	 * @return
	 */
	public abstract Document deleteVersions(String documentPath);

	public abstract boolean connect(String username, String password) throws Exception;

	public abstract ObjectStore getStore();

	public abstract Folder getRootFolder();

}