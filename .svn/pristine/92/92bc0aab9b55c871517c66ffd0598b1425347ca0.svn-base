package com.wcs.tih.filenet.ce.service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.collection.VersionableSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.wcs.tih.filenet.ce.util.MimeUtil;
import com.wcs.tih.filenet.common.service.CommonBase;

@Stateless
public class CEserviceImpl implements CEserviceLocal{
    private final Logger logger = LoggerFactory.getLogger(getClass());
	private ObjectStore store;

	public CEserviceImpl(){};
	
	@PostConstruct
	public void init() {
	    logger.info("initialize");
	}
	
	public boolean connect(String username, String password) throws Exception {
		CommonBase dd = new CommonBase();
		this.store = dd.getOS(username, password);
		return true;
	}

	public Folder createRootFolder(String folderName) {
		Folder myFolder = Factory.Folder.createInstance(store, null);
		Folder rootFolder = store.get_RootFolder();
		myFolder.set_Parent(rootFolder);
		myFolder.set_FolderName(folderName);
		myFolder.save(RefreshMode.REFRESH);
		logger.info("rootfolder " + myFolder.get_Name() + " created");
		return myFolder;
	}

	public Folder createSubFolder(Folder parentFolder, String folderName) {
		Folder subFolder = parentFolder.createSubFolder(folderName);
		subFolder.save(RefreshMode.REFRESH);
		logger.info("Sub Folder  " + subFolder.get_Name() + " is created");
		return subFolder;
	}

	public Folder getFolder(String folderName) throws Exception{
		Folder folder = null;
		try {
			folder = Factory.Folder.fetchInstance(store, folderName, null);
		} catch (Exception e) {
			throw new Exception(e.toString());
		}
		logger.info(folder.get_FolderName() + " folder has been retrieved");
		return folder;
	}

	public void deleteFolder(Id id) {
		Folder folder = Factory.Folder.fetchInstance(store, id, null);
		String folderName = folder.get_FolderName();
		folder.delete();
		folder.save(RefreshMode.REFRESH);
		logger.info(folderName + " folder has been Deleted");
	}

	public void deleteFolder(String foldername) {
		Folder folder = Factory.Folder.fetchInstance(store, foldername, null);
		folder.delete();
		folder.save(RefreshMode.REFRESH);
		logger.info(folder.get_FolderName() + " folder has been Deleted");
	}

	public Folder getRootFolder() {
	    return store.get_RootFolder();
	}
	
	public List<Folder> getSubFolders(String parentFolderName) {
		Folder pfolder = Factory.Folder.fetchInstance(store, parentFolderName, null);
		FolderSet subFolders = pfolder.get_SubFolders();
		logger.info("List of Folders Under the Test " + parentFolderName + ":");
		Iterator it = subFolders.iterator();
		List<Folder> foldersList1 = new ArrayList<Folder>();
		while (it.hasNext()) {
			Folder retrieveFolder = (Folder) it.next();
			foldersList1.add(retrieveFolder);
			logger.info(retrieveFolder.get_Name() + ",");
		}
		return foldersList1;
	}

	public List<Document> getDocsInThisFolder(String parentFolder) {
		Folder folder = Factory.Folder.fetchInstance(store, parentFolder, null);
		DocumentSet documents = folder.get_ContainedDocuments();
		List<Document> docList = new ArrayList<Document>();
		Iterator it = documents.iterator();
		logger.info("List of Documents Under the Test Folder:");
		while (it.hasNext()) {
			Document retrieveDoc = (Document) it.next();
			String name = retrieveDoc.get_Name();
			logger.info(name + ",");
			docList.add(retrieveDoc);
		}
		return docList;
	}

	public List<IndependentObject> getFolderContainees(String parentFolder) {
		Folder folder = Factory.Folder.fetchInstance(store, parentFolder, null);
		ReferentialContainmentRelationshipSet refConRelSet = folder.get_Containees();
		Iterator it = refConRelSet.iterator();
		logger.info("List of Objects Under the Test Folder:");
		List<IndependentObject> containees = new ArrayList<IndependentObject>();
		while (it.hasNext()) {
			logger.info("-----------------");
			ReferentialContainmentRelationship retrieveObj = (ReferentialContainmentRelationship) it.next();
			IndependentObject containee = retrieveObj.get_Head();
			String className = containee.getClassName();
			logger.info("class= " + className);
			String displayName = retrieveObj.get_Name();
			logger.info("display Name =" + displayName);
			containees.add(containee);
		}
		logger.info("-----------------");
		return containees;
	}

	public Document createDocument(InputStream filestream, Map propertiesMap, String mimeType, String classId, String foldername, String documentTitle) {
		Document doc = Factory.Document.createInstance(store, classId);
		// set content
		ContentElementList contentList = Factory.ContentElement.createList();
		ContentTransfer content = Factory.ContentTransfer.createInstance();
		content.setCaptureSource(filestream);
		contentList.add(content);
		doc.set_ContentElements(contentList);

		// check in the document
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

		// Set the custom properties
		com.filenet.api.property.Properties properties = doc.getProperties();
		Set keySet = propertiesMap.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String key = it.next();
			properties.putObjectValue(key, propertiesMap.get(key));
		}
		// Set the document title
		properties.putValue("DocumentTitle", MimeUtil.removeStrValue(documentTitle));
		// Set the MIME type
		doc.set_MimeType(mimeType);
		ContentElementList contentElementList = doc.get_ContentElements();
	    ContentElement contentElement;
	    Iterator contentElementListItr = contentElementList.iterator();
		while(contentElementListItr.hasNext()) {
			contentElement = (ContentElement) contentElementListItr.next();
			ContentTransfer contentTransfer = (ContentTransfer) contentElement;
			contentTransfer.set_RetrievalName(documentTitle);
		}
		// Save the Document
		doc.save(RefreshMode.REFRESH);
		// File the document into a folder
		Folder folder = Factory.Folder.fetchInstance(store, foldername, null);
		ReferentialContainmentRelationship rel = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, documentTitle, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		
		rel.save(RefreshMode.NO_REFRESH);
		logger.info("Document" + doc.get_Id() + " is created");
		return (Document) (rel.get_Head());
	}

	public Document getDocument(Id id) throws Exception{
		Document doc = null;
		try {
			doc = Factory.Document.fetchInstance(store, id, null);
			String documentName = doc.get_Name();
			logger.info(documentName + " Document has been retrieved");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return doc;
	}

	public Document getDocument(String path) {
		Document doc = Factory.Document.fetchInstance(store, path, null);
		String documentName = doc.get_Name();
		logger.info(documentName + " Document has been retrieved");
		return doc;
	}

	public void getDocumentContent(Document document) throws Exception {
		ContentElementList contents = document.get_ContentElements();
		ContentElement content;
		Iterator itContent = contents.iterator();
		while (itContent.hasNext()) {
			content = (ContentElement) itContent.next();
			logger.info("content type = " + content.get_ContentType());
			getContentStream((ContentTransfer) content);
		}
	}

	public OutputStream getContentStream(ContentTransfer content) throws Exception {
		String fileName = content.get_RetrievalName();
		InputStream inputStream = content.accessContentStream();
		OutputStream outputStream = new FileOutputStream(fileName);
		byte[] nextBytes = new byte[64000];
		int nBytesRead;
		while ((nBytesRead = inputStream.read(nextBytes)) != -1) {
			outputStream.write(nextBytes, 0, nBytesRead);
			outputStream.flush();
		}
		return outputStream;
	}

	public DocumentSet searchDocuments(String sqlStr, Integer pageSize) {
		SearchScope search = new SearchScope(store);
		SearchSQL sql = new SearchSQL(sqlStr);
		return (DocumentSet) search.fetchObjects(sql, pageSize, null, Boolean.TRUE);
	}

	
	public VersionableSet getDocumentVersions(String documentPath) {
		Document doc = Factory.Document.fetchInstance(store, documentPath, null);
		return doc.get_Versions();
	}

	public Document demoteVersions(String documentPath) {
		Document doc = Factory.Document.fetchInstance(store, documentPath, null);
		if (!doc.get_IsCurrentVersion().booleanValue()){
			doc = (Document) doc.get_CurrentVersion();
		}
		if ((doc.get_VersionStatus().getValue() == VersionStatus.RELEASED_AS_INT) && !doc.get_IsReserved().booleanValue()) {
			doc.demoteVersion();
			doc.save(RefreshMode.REFRESH);
		}
		return doc;
	}

	public Document promoteVersions(String documentPath) {
		Document doc = Factory.Document.fetchInstance(store, documentPath, null);
		if (!doc.get_IsCurrentVersion().booleanValue()){
			doc = (Document) doc.get_CurrentVersion();
		}
		if ((doc.get_VersionStatus().getValue() == VersionStatus.IN_PROCESS_AS_INT) && !doc.get_IsReserved().booleanValue()) {
			doc.promoteVersion();

			doc.save(RefreshMode.REFRESH);
		}
		return doc;
	}

	public Document deleteVersions(String documentPath) {
		Document doc = Factory.Document.fetchInstance(store, documentPath, null);
		if (!doc.get_IsCurrentVersion().booleanValue()){
			doc = (Document) doc.get_CurrentVersion();
		}
		doc.delete();
		doc.save(RefreshMode.REFRESH);
		return doc;
	}

	public ObjectStore getStore() {
		return store;
	}

}
