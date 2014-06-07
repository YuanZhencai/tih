package com.wcs.tih.filenet.helper.ce;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.collection.VersionableSet;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.ReservationType;
import com.filenet.api.constants.VersionStatus;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.core.UpdatingBatch;
import com.filenet.api.core.VersionSeries;
import com.filenet.api.core.Versionable;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.publishing.PublishRequest;
import com.filenet.api.publishing.PublishTemplate;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.property.PropertyImpl;
import com.filenet.apiimpl.util.SessionLocator;
import com.wcs.tih.filenet.helper.ce.util.AuthenticatedObjectStore;
import com.wcs.tih.filenet.helper.ce.util.EngineCollectionUtils;
import com.wcs.tih.filenet.helper.ce.util.MimeTypeMap;
import com.wcs.tih.filenet.helper.vo.HistoryDocumentVo;
import com.wcs.tih.filenet.helper.vo.PaginationVo;
import com.wcs.tih.filenet.helper.vo.PermissionsVo;
import com.wcs.tih.filenet.helper.vo.RankVo;
import com.wcs.tih.filenet.helper.vo.RetrieveResultVo;

public class ObjectStoreProvider {
	private Logger logger;
	private ObjectStore os;
	private AuthenticatedObjectStore aos;

	public AuthenticatedObjectStore getAuthenticatedObjectStore() {
		return this.aos;
	}

	private ObjectStoreProvider(AuthenticatedObjectStore aos) {
		this.logger = Logger.getLogger(super.getClass());

		this.aos = aos;
		this.os = (ObjectStore) aos.getObjectStore();
	}

	public ObjectStoreProvider() {
		this(AuthenticatedObjectStore.createDefault());
	}

	public ObjectStore getObjectStore() {
		return this.os;
	}

	public ObjectStoreProvider(String username, String password) {
		this(new AuthenticatedObjectStore(username, password, true));
	}

	public String createFolder(String folderName, Folder parentFolder) {
		return createFolder(folderName, parentFolder, null);
	}

	public String createFolder(String folderName, Folder parentFolder, String symbolicClassName) {
		Folder folder = (Folder) Factory.Folder.createInstance(this.os, symbolicClassName);

		folder.set_Parent(parentFolder);
		folder.set_FolderName(folderName);
		folder.save(RefreshMode.REFRESH);

		String folderId = folder.get_Id().toString();
		return folderId;
	}

	public String createFolder(String folderName, Folder parentFolder, Map<String, Object> propMap, String symbolicClassName) {
		Folder folder = Factory.Folder.createInstance(this.os, symbolicClassName);

		folder.set_Parent(parentFolder);
		folder.set_FolderName(folderName);

		Iterator<String> iterator = propMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = propMap.get(key);
			folder.getProperties().putObjectValue(key, value);
		}
		folder.save(RefreshMode.REFRESH);

		return folder.get_Id().toString();
	}

	public String createFolderwithPropInherit(String folderName, Folder parentFolder, String symbolicClassName) {
		if (symbolicClassName.equals("Folder")) {
			Folder folder = Factory.Folder.createInstance(this.os, symbolicClassName);
			folder.set_Parent(parentFolder);
			Properties properties = parentFolder.getProperties();
			Properties selfProperties = folder.getProperties();
			for (Iterator<?> it = properties.iterator(); it.hasNext();) {
				Property parentProperty = (Property) it.next();
				for (Iterator<?> its = selfProperties.iterator(); its.hasNext();) {
					Property childProperty = (Property) its.next();
					if ((!(childProperty.isSettable())) || (!(childProperty.getPropertyName().equals(parentProperty.getPropertyName()))))
						continue;
					childProperty = parentProperty;
				}
			}

			folder.set_FolderName(folderName);
			folder.set_InheritParentPermissions(Boolean.valueOf(true));
			folder.save(RefreshMode.REFRESH);
			String folderId = folder.get_Id().toString();
			return folderId;
		}
		return null;
	}

	public Folder fetchFolder(String id) {
		return fetchFolder(id, null);
	}

	private Folder fetchFolder(String id, PropertyFilter propertyFilter) {
		try {
			Folder folder = Factory.Folder.fetchInstance(this.os, new Id(id), propertyFilter);
			return folder;
		} catch (EngineRuntimeException e) {
			if (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
				return null;
			}
			throw e;
		}
	}

	public Folder fetchFolderFromPath(String path) {
		return fetchFolderFromPath(path, null);
	}

	private Folder fetchFolderFromPath(String path, PropertyFilter propertyFilter) {
		return Factory.Folder.fetchInstance(this.os, path, propertyFilter);
	}

	public void updateFolderAttrs(String id, Map<String, Object> updatedAttrs) {
		updateAttrs(fetchFolder(id), updatedAttrs);
	}

	public String deleteFolderByID(String folderId) throws Exception {
		String isDelFolder = "failure";
		try {
			Folder curFolder = fetchFolder(folderId);
			Document document = null;
			FolderSet subfoldes = curFolder.get_SubFolders();
			DocumentSet ds = curFolder.get_ContainedDocuments();
			if ((subfoldes != null) && (!(subfoldes.isEmpty()))) {
				Iterator<?> it = subfoldes.iterator();
				while (it.hasNext()) {
					Folder fd = (Folder) it.next();
					deleteFolderByID(fd.get_Id().toString());
				}
			} else if ((ds != null) && (!(ds.isEmpty()))) {
				Iterator<?> dit = ds.iterator();
				while (dit.hasNext()) {
					document = (Document) dit.next();
					document.delete();
					document.save(RefreshMode.NO_REFRESH);
				}
			}
			curFolder.delete();
			curFolder.save(RefreshMode.NO_REFRESH);
			isDelFolder = "success";
		} catch (Exception e) {
			throw e;
		}

		return isDelFolder;
	}

	public void deteleFolderAndAllContainedObjectsWithBatch(String folderId) {
		UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(this.os.get_Domain(), RefreshMode.REFRESH);

		deteleAllContainedObjectsWithBatch(ub, folderId);

		ub.updateBatch();
	}

	public List<Folder> searchFolders(String className, String whereClause, boolean includeSubclasses) {
		Search search = new Search();
		return search.setObjectSql(className, includeSubclasses, whereClause).setScope(this.os).setPropertyFilter().fetchFolders();
	}

	public List<Folder> searchFolders(String className, String whereClause, boolean includeSubclasses, String orderBy) {
		Search search = new Search();
		return search.setObjectSql(className, includeSubclasses, whereClause, orderBy, -1).setScope(this.os).setPropertyFilter().fetchFolders();
	}

	public RetrieveResultVo searchFolders(String className, String whereClause, boolean includeSubclasses, String orderBy, PaginationVo paginationVo) {
		RetrieveResultVo rrVo = new RetrieveResultVo();
		int currentPage = paginationVo.getCurrentPage();
		int docsPerPage = paginationVo.getDocsPerPage();

		Search search = new Search();
		List<Folder> folderList = search.setObjectSql(className, includeSubclasses, whereClause, orderBy, -1).setScope(this.os).setPropertyFilter()
				.fetchFolders();

		Object[] o = folderList.toArray();
		Arrays.sort(o);
		for (int i = 0; i < o.length; ++i) {
			folderList.set(i, (Folder) o[i]);
		}

		int totDocs = folderList.size();
		paginationVo.setTotalDocs(totDocs);
		paginationVo.setTotalPages((totDocs + docsPerPage - 1) / docsPerPage);

		int fromIndex = (currentPage - 1) * docsPerPage;
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		int toIndex = fromIndex + docsPerPage;
		if (toIndex > totDocs - 1) {
			toIndex = totDocs;
		}
		if (fromIndex < toIndex)
			folderList = folderList.subList(fromIndex, toIndex);
		else {
			paginationVo.setCurrentPage((paginationVo.getTotalPages() != 0) ? paginationVo.getTotalPages() : 1);
		}

		rrVo.setDocsList(folderList);
		rrVo.setPaginationVo(paginationVo);

		return rrVo;
	}

	public RetrieveResultVo getDocumentListFormFolder(Folder folder, PaginationVo paginationVo) {
		return getDocumentListFromFolder(folder, true, null, paginationVo);
	}

	public RetrieveResultVo getDocumentListFromFolder(Folder folder, boolean onlyCurrentVersion, String docSymbolicClassName,
			PaginationVo paginationVo) {
		RetrieveResultVo rrVo = new RetrieveResultVo();
		int currentPage = paginationVo.getCurrentPage();
		int docsPerPage = paginationVo.getDocsPerPage();

		List<Document> documentList = new ArrayList<Document>();
		DocumentSet documentSet = folder.get_ContainedDocuments();
		Iterator<?> iterator = documentSet.iterator();
		while (iterator.hasNext()) {
			Document document = (Document) iterator.next();
			String className = document.getClassName();
			if (docSymbolicClassName == null) {
				if (onlyCurrentVersion) {
					if (document.get_IsCurrentVersion().booleanValue())
						documentList.add(document);
				} else
					documentList.add(document);
			} else if (docSymbolicClassName.equals(className)) {
				if (onlyCurrentVersion) {
					if (document.get_IsCurrentVersion().booleanValue())
						documentList.add(document);
				} else {
					documentList.add(document);
				}
			}
		}

		Object[] o = documentList.toArray();
		Arrays.sort(o);
		for (int i = 0; i < o.length; ++i) {
			documentList.set(i, (Document) o[i]);
		}

		int totDocs = documentList.size();
		paginationVo.setTotalDocs(totDocs);
		paginationVo.setTotalPages((totDocs + docsPerPage - 1) / docsPerPage);

		int fromIndex = (currentPage - 1) * docsPerPage;
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		int toIndex = fromIndex + docsPerPage;
		if (toIndex > totDocs - 1) {
			toIndex = totDocs;
		}
		if (fromIndex < toIndex)
			documentList = documentList.subList(fromIndex, toIndex);
		else {
			paginationVo.setCurrentPage((paginationVo.getTotalPages() != 0) ? paginationVo.getTotalPages() : 1);
		}

		rrVo.setDocsList(documentList);
		rrVo.setPaginationVo(paginationVo);

		return rrVo;
	}

	public Folder getRootFolder() {
		return this.os.get_RootFolder();
	}

	public boolean moveFolder(String sourceFolderId, String destParentFolderId) throws Exception {
		boolean flag = false;
		try {
			Folder sourceFolder = fetchFolder(sourceFolderId);
			Folder destParentFolder = fetchFolder(destParentFolderId);
			sourceFolder.set_Parent(destParentFolder);
			sourceFolder.save(RefreshMode.REFRESH);
			destParentFolder.save(RefreshMode.REFRESH);
			flag = true;
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	public boolean copyFolder(String sourceFolderId, String destParentFolderId) throws Exception {
		boolean flag = false;
		try {
			Folder sourceFolder = fetchFolder(sourceFolderId);
			Folder destParentFolder = fetchFolder(destParentFolderId);

			String newFolderId = createFolder(sourceFolder.get_FolderName(), destParentFolder);
			Folder newFolder = fetchFolder(newFolderId);

			FolderSet subSourceFolders = sourceFolder.get_SubFolders();
			DocumentSet subDocuments = sourceFolder.get_ContainedDocuments();

			if ((subDocuments != null) && (!(subDocuments.isEmpty()))) {
				Iterator<?> dit = subDocuments.iterator();
				while (dit.hasNext()) {
					Document document = (Document) dit.next();
					String symbolicClassName = document.getClassName();
					String documentTitle = document.get_Name();
					ContentElementList contentList = document.get_ContentElements();
					String includedInFolderId = newFolderId;
					HashMap<String, Object> propMap = new HashMap<String, Object>();
					Properties property = document.getProperties();

					for (Iterator<?> it = property.iterator(); it.hasNext();) {
						Property prop = (Property) it.next();
						if (!(isSettable(prop)))
							continue;
						System.out.print(prop.getPropertyName() + "\n");
						propMap.put(prop.getPropertyName(), prop.getObjectValue());
					}

					createDoucmentByFolderId(symbolicClassName, documentTitle, propMap, contentList, includedInFolderId);
				}
			} else if ((subSourceFolders != null) && (!(subSourceFolders.isEmpty()))) {
				Iterator<?> fit = subSourceFolders.iterator();
				while (fit.hasNext()) {
					Folder subSourceFolder = (Folder) fit.next();
					Folder subDestParentFolder = newFolder;
					flag = copyFolder(subSourceFolder.get_Id().toString(), subDestParentFolder.get_Id().toString());
				}
			}
			flag = true;
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	static boolean isSettable(Property property) {
		if (SessionLocator.isExecutingInServer()) {
			return true;
		}

		return ((property == null) || ((property.isSettable()) && (!(((PropertyImpl) property).isReadOnly()))));
	}

	public List<PermissionsVo> getFolderPermissions(Folder folder) throws Exception {
		ArrayList<PermissionsVo> permissionsList = new ArrayList<PermissionsVo>();
		AccessPermissionList apl = folder.get_Permissions();
		for (int i = 0; i < apl.size(); ++i) {
			PermissionsVo pvo = new PermissionsVo();
			try {
				AccessPermission ap = (AccessPermission) apl.get(i);
				String granteeName = ap.get_GranteeName();
				String title = granteeName.split("@")[0];
				pvo.setTitle(title);

				int mask = ap.get_AccessMask().intValue();
				AccessLevel accessLevel = AccessLevel.getInstanceFromInt(mask);
				if (accessLevel.equals(AccessLevel.FULL_CONTROL)) {
					pvo.setAllControl(true);
					pvo.setUpGradeVersion(true);
					pvo.setUpdatePath(true);
					pvo.setUpdateProperties(true);
					pvo.setViewPath(true);
					pvo.setViewProperties(true);
					pvo.setPublish(true);
				} else if (accessLevel.equals(AccessLevel.READ)) {
					pvo.setAllControl(false);
					pvo.setUpGradeVersion(false);
					pvo.setUpdatePath(false);
					pvo.setUpdateProperties(false);
					pvo.setViewPath(true);
					pvo.setViewProperties(true);
					pvo.setPublish(false);
				} else {
					pvo.setAllControl(false);
					pvo.setUpGradeVersion(false);
					pvo.setUpdatePath(false);
					pvo.setUpdateProperties(false);
					pvo.setViewPath(false);
					pvo.setViewProperties(false);
					pvo.setPublish(false);
				}
				permissionsList.add(pvo);
			} catch (Exception e) {
				throw e;
			}
		}
		return permissionsList;
	}

	public boolean updateFolderPermissions(String id, List<PermissionsVo> pervoList) throws Exception {
		boolean flag = false;
		Folder folder = fetchFolder(id);
		AccessPermissionList apl = folder.get_Permissions();
		int length = apl.size();

		for (int i = 0; i < length; ++i) {
			try {
				AccessPermission ap = (AccessPermission) apl.get(i);
				String granteeName = ap.get_GranteeName();
				String title = granteeName.split("@")[0];
				boolean breakflag = false;
				for (PermissionsVo perVo : pervoList) {
					String perVoTitle = perVo.getTitle();
					if (perVoTitle.equals(title)) {
						breakflag = true;
						if ((perVo.isAllControl()) && (perVo.isUpGradeVersion()) && (perVo.isUpdatePath()) && (perVo.isUpdateProperties())
								&& (perVo.isViewPath()) && (perVo.isViewProperties()) && (perVo.isPublish())) {
							ap.set_AccessMask(Integer.valueOf(999415));
						} else if ((!(perVo.isAllControl())) && (!(perVo.isUpGradeVersion())) && (!(perVo.isUpdatePath()))
								&& (!(perVo.isUpdateProperties())) && (perVo.isViewPath()) && (perVo.isViewProperties()) && (!(perVo.isPublish()))) {
							ap.set_AccessMask(Integer.valueOf(131073));
						} else if ((!(perVo.isAllControl())) && (!(perVo.isUpGradeVersion())) && (!(perVo.isUpdatePath()))
								&& (!(perVo.isUpdateProperties())) && (!(perVo.isViewPath())) && (!(perVo.isViewProperties()))
								&& (!(perVo.isPublish()))) {
							ap.set_AccessMask(Integer.valueOf(999415));
							ap.set_AccessType(AccessType.DENY);
						}
					}
					if (breakflag)
						;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		folder.set_Permissions(apl);
		folder.save(RefreshMode.REFRESH);
		return flag;
	}

	public String createDocument(String symbolicClassName, String documentTitle, Map<String, Object> propMap, String includedInFolderPath,
			String mimeType, InputStream inputStream) {
		Folder folder = fetchFolderFromPath(includedInFolderPath);
		String id = creatingDocument(symbolicClassName, documentTitle, mimeType, propMap, inputStream, folder);
		return id;
	}

	public static ContentTransfer createContentTransfer(String filePath, String fileName, String mimeType) throws Exception {
		ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
		InputStream is;
		try {
			is = new FileInputStream(filePath);
		} catch (Exception e) {
			throw e;
		}
		contentTransfer.setCaptureSource(is);
		contentTransfer.set_RetrievalName(fileName);
		contentTransfer.set_ContentType(mimeType);
		return contentTransfer;
	}

	public String creatingDocument(String symbolicClassName, String documentTitle, String mimeType, Map<String, Object> propertyMap,
			InputStream inputStream, Folder folder) {
		Document doc = Factory.Document.createInstance(this.os, symbolicClassName);
		
		if (inputStream != null) {
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer ctObject = Factory.ContentTransfer.createInstance();

			ctObject.setCaptureSource(inputStream);
			ctObject.set_RetrievalName(documentTitle + "." + mimeType);

			contentList.add(ctObject);

			doc.set_ContentElements(contentList);

			doc.save(RefreshMode.NO_REFRESH);
			
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		}

		doc.getProperties().putValue("DocumentTitle", documentTitle);

		if (propertyMap != null) {
			Iterator<String> iterator = propertyMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				Object value = propertyMap.get(key);
				System.out.printf("-------------Property key=[%s] value=[%s]\n", new Object[] { key, value });

				doc.getProperties().putObjectValue(key, value);
			}

		}

		doc.set_MimeType(MimeTypeMap.getMimetype(mimeType));
		
		doc.save(RefreshMode.REFRESH);
		
		ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, documentTitle,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);

		rcr.save(RefreshMode.NO_REFRESH);
		return doc.get_Id().toString();
	}

	public String createDoucmentByFolderId(String symbolicClassName, String documentTitle, Map<String, Object> propMap,
			ContentElementList contentList, String includedInFolderId) {
		Folder folder = fetchFolder(includedInFolderId);

		Document myDoc = Factory.Document.createInstance(this.os, symbolicClassName);

		myDoc.getProperties().putValue("DocumentTitle", documentTitle);
		Iterator<String> iterator = propMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object value = propMap.get(key);

			myDoc.getProperties().putObjectValue(key, value);
		}

		myDoc.set_ContentElements(contentList);

		myDoc.save(RefreshMode.REFRESH);

		myDoc.checkin(AutoClassify.AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

		ReferentialContainmentRelationship rel = folder.file(myDoc, AutoUniqueName.AUTO_UNIQUE, null,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);

		rel.set_ContainmentName(documentTitle);
		rel.save(RefreshMode.NO_REFRESH);

		String id = myDoc.get_Id().toString();
		return id;
	}

	public List<Map<String, InputStream>> getAttachments(String id) {
		Document doc = fetchDocument(id);
		ContentElementList contentList = doc.get_ContentElements();
		Iterator<?> iterator = contentList.iterator();
		List<Map<String, InputStream>> list = new ArrayList<Map<String, InputStream>>();
		Map<String, InputStream> attachment = new HashMap<String, InputStream>();
		while (iterator.hasNext()) {
			ContentTransfer ctObject = (ContentTransfer) iterator.next();
			attachment.put(ctObject.get_RetrievalName(), ctObject.accessContentStream());
			list.add(attachment);
		}
		return list;
	}

	public Map<String, InputStream> getAttachment(String id) {
		Document doc = fetchDocument(id);
		ContentElementList contentList = doc.get_ContentElements();
		Iterator<?> iterator = contentList.iterator();
		Map<String, InputStream> attachment = new HashMap<String, InputStream>();
		while (iterator.hasNext()) {
			ContentTransfer ctObject = (ContentTransfer) iterator.next();
			attachment.put(ctObject.get_RetrievalName(), ctObject.accessContentStream());
		}
		return attachment;
	}

	public String checkinDocument(String docID, ContentElementList contentLists, String path, String mimetype) throws Exception {
		String ischeckin = "failure";
		try {
			Document doc = fetchDocument(docID);
			Document releasedDoc = (Document) doc.get_Reservation();

			releasedDoc.set_ContentElements(contentLists);

			releasedDoc.getProperties().putValue("DocumentTitle", path);

			releasedDoc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

			releasedDoc.save(RefreshMode.NO_REFRESH);
			ischeckin = "success";
		} catch (Exception e) {
			throw e;
		}
		return ischeckin;
	}

	public boolean isCheckOut(String docId) {
		return (getReservedDocument(docId) != null);
	}

	public void checkoutDocument(Document document) {
		document.checkout(ReservationType.OBJECT_STORE_DEFAULT, null, null, null);
		document.save(RefreshMode.NO_REFRESH);
	}

	public void cancelCheckoutDocument(String docID) {
		Document document = fetchDocument(docID);
		Document reservation = (Document) document.get_Reservation();

		document.cancelCheckout();
		reservation.save(RefreshMode.REFRESH);
	}

	private Document fetchDocument(String id, PropertyFilter propertyFilter) {
		try {
			Document doc = Factory.Document.fetchInstance(this.os, new Id(id), propertyFilter);
			return doc;
		} catch (EngineRuntimeException e) {
			if (e.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
				return null;
			}
			throw e;
		}
	}

	public Document fetchDocumentByPath(String path) {
		return fetchDocument(path, null);
	}

	public Document fetchDocument(String id) {
		return fetchDocument(id, null);
	}

	public Document getReleasedDocument(String id) {
		Versionable versionable = fetchDocument(id).get_ReleasedVersion();
		if (versionable == null)
			return null;
		return ((Document) versionable);
	}

	public Document getReservedDocument(String id) {
		IndependentObject ido = fetchDocument(id).get_Reservation();
		if (ido == null)
			return null;
		return ((Document) ido);
	}

	public void updateDocumentAttrs(String id, Map<String, Object> updatedAttrs) {
		updateAttrs(fetchDocument(id), updatedAttrs);
	}

	public void updateDocumentAttrsAndAttachment(String id, Map<String, Object> updatedAttrs, ContentElementList contentList) {
		Document doc = fetchDocument(id);
		for (String attrName : updatedAttrs.keySet()) {
			doc.getProperties().putObjectValue(attrName, updatedAttrs.get(attrName));
		}
		doc.set_ContentElements(contentList);
		doc.save(RefreshMode.REFRESH);
	}

	public String deleteDocument(String documentId) {
		String isDelFolder = "failure";
		try {
			Document doc = fetchDocument(documentId);
			doc.delete();
			doc.save(RefreshMode.NO_REFRESH);
			isDelFolder = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDelFolder;
	}

	public String deleteDocumentAllVersions(String documentId) {
		String isDelFolder = "failure";
		try {
			Document doc = fetchDocument(documentId);
			VersionSeries versionSeries = doc.get_VersionSeries();
			versionSeries.delete();
			versionSeries.save(RefreshMode.NO_REFRESH);
			isDelFolder = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isDelFolder;
	}

	public void deleteMultiDocument(String[] ids) {
		if ((ids == null) || (ids.length < 1)) {
			return;
		}

		int length = ids.length;
		for (int i = 0; i < length; ++i) {
			Document doc = Factory.Document.getInstance(this.os, "Document", new Id(ids[i]));
			doc.delete();
			doc.save(RefreshMode.NO_REFRESH);
		}
	}

	public void deleteMultiDocumentAllVersions(String[] ids) {
		if ((ids == null) || (ids.length < 1)) {
			return;
		}

		int length = ids.length;
		for (int i = 0; i < length; ++i)
			deleteDocumentAllVersions(ids[i]);
	}

	public void deleteDocumentToRecycle(String id) {
		Document document = fetchDocument(id);
		deleteToRecycle(document);
	}

	public List<Document> searchObjects(String className, String whereClause, boolean includeSubclasses) {
		Search search = new Search();
		return search.setObjectSql(className, includeSubclasses, whereClause).setScope(this.os).setPropertyFilter().fetchObjects();
	}

	public List<Document> searchObjects(String className, String whereClause, boolean includeSubclasses, int maxRecords) {
		Search search = new Search();
		return search.setObjectSql(className, includeSubclasses, whereClause, null, maxRecords).setScope(this.os).setPropertyFilter().fetchObjects();
	}

	public List<Document> searchObjects(String className, String whereClause, boolean includeSubclasses, String orderBy) {
		Search search = new Search();
		return search.setObjectSql(className, includeSubclasses, whereClause, orderBy, -1).setScope(this.os).setPropertyFilter().fetchObjects();
	}

	public List<Document> searchObjects(String selectClause, String className, String whereClause, boolean includeSubclasses, String orderBy) {
		Search search = new Search();
		return search.setObjectSql(selectClause, className, includeSubclasses, whereClause, orderBy, -1).setScope(this.os).setPropertyFilter()
				.fetchObjects();
	}

	public List<Document> searchObjectsByFullText(String className, String whereClause, boolean includeSubclasses, String orderBy) {
		Search search = new Search();
		return search.setObjectSql(className, includeSubclasses, whereClause, orderBy, -1, true).setScope(this.os).setPropertyFilter().fetchObjects();
	}

	public List<RankVo> searchObjectsByFullTextWithRank(String className, String whereClause, boolean includeSubclasses, String orderBy) {
		Search search = new Search();
		return search.setRowSqlWithFullText(className, includeSubclasses, whereClause, orderBy, -1, true).setScope(this.os).setPropertyFilter()
				.fetchRowsWithRank();
	}

	public RetrieveResultVo searchObjectsByFullTextWithRank(String className, String whereClause, boolean includeSubclasses,
			boolean onlyCurrentVersion, String orderBy, PaginationVo paginationVo) {
		RetrieveResultVo rrVo = new RetrieveResultVo();
		int currentPage = paginationVo.getCurrentPage();
		int docsPerPage = paginationVo.getDocsPerPage();

		Search search = new Search();
		List<RankVo> rankVoList = search.setRowSqlWithFullText(className, includeSubclasses, whereClause, orderBy, -1, true).setScope(this.os)
				.setPropertyFilter().fetchRowsWithRank();

		Object[] o = rankVoList.toArray();
		Arrays.sort(o);
		rankVoList.clear();

		if (onlyCurrentVersion) {
			int j = 0;
			for (int i = 0; i < o.length; ++i)
				if (((RankVo) o[i]).getDocument().get_IsCurrentVersion().booleanValue()) {
					rankVoList.set(j, (RankVo) o[i]);
					++j;
				}
		} else {
			for (int i = 0; i < o.length; ++i) {
				rankVoList.set(i, (RankVo) o[i]);
			}
		}

		int totDocs = rankVoList.size();
		paginationVo.setTotalDocs(totDocs);
		paginationVo.setTotalPages((totDocs + docsPerPage - 1) / docsPerPage);

		int fromIndex = (currentPage - 1) * docsPerPage;
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		int toIndex = fromIndex + docsPerPage;
		if (toIndex > totDocs - 1) {
			toIndex = totDocs;
		}
		if (fromIndex < toIndex)
			rankVoList = rankVoList.subList(fromIndex, toIndex);
		else {
			paginationVo.setCurrentPage((paginationVo.getTotalPages() != 0) ? paginationVo.getTotalPages() : 1);
		}

		rrVo.setDocsList(rankVoList);
		rrVo.setPaginationVo(paginationVo);

		return rrVo;
	}

	public RetrieveResultVo searchObjectsByFullText(String className, String whereClause, boolean includeSubclasses, boolean onlyCurrentVersion,
			String orderBy, PaginationVo paginationVo) {
		RetrieveResultVo rrVo = new RetrieveResultVo();
		int currentPage = paginationVo.getCurrentPage();
		int docsPerPage = paginationVo.getDocsPerPage();

		Search search = new Search();
		List<Document> documentList = search.setObjectSql(className, includeSubclasses, whereClause, orderBy, -1, true).setScope(this.os)
				.setPropertyFilter().fetchObjects();

		Object[] o = documentList.toArray();
		Arrays.sort(o);
		documentList.clear();

		if (onlyCurrentVersion) {
			int j = 0;
			for (int i = 0; i < o.length; ++i)
				if (((Document) o[i]).get_IsCurrentVersion().booleanValue()) {
					documentList.set(j, (Document) o[i]);
					++j;
				}
		} else {
			for (int i = 0; i < o.length; ++i) {
				documentList.set(i, (Document) o[i]);
			}
		}

		int totDocs = documentList.size();
		paginationVo.setTotalDocs(totDocs);
		paginationVo.setTotalPages((totDocs + docsPerPage - 1) / docsPerPage);

		int fromIndex = (currentPage - 1) * docsPerPage;
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		int toIndex = fromIndex + docsPerPage;
		if (toIndex > totDocs - 1) {
			toIndex = totDocs;
		}
		if (fromIndex < toIndex)
			documentList = documentList.subList(fromIndex, toIndex);
		else {
			paginationVo.setCurrentPage((paginationVo.getTotalPages() != 0) ? paginationVo.getTotalPages() : 1);
		}

		rrVo.setDocsList(documentList);
		rrVo.setPaginationVo(paginationVo);

		return rrVo;
	}

	public RetrieveResultVo searchObjects(String className, String whereClause, boolean includeSubclasses, boolean onlyCurrentVersion,
			String orderBy, PaginationVo paginationVo) {
		RetrieveResultVo rrVo = new RetrieveResultVo();
		int currentPage = paginationVo.getCurrentPage();
		int docsPerPage = paginationVo.getDocsPerPage();

		Search search = new Search();
		List<Document> documentList = search.setObjectSql(className, includeSubclasses, whereClause, orderBy, -1).setScope(this.os)
				.setPropertyFilter().fetchObjects();

		Object[] o = documentList.toArray();
		Arrays.sort(o);
		documentList.clear();

		if (onlyCurrentVersion) {
			int j = 0;
			for (int i = 0; i < o.length; ++i)
				if (((Document) o[i]).get_IsCurrentVersion().booleanValue()) {
					documentList.set(j, (Document) o[i]);
					++j;
				}
		} else {
			for (int i = 0; i < o.length; ++i) {
				documentList.set(i, (Document) o[i]);
			}
		}

		int totDocs = documentList.size();
		paginationVo.setTotalDocs(totDocs);
		paginationVo.setTotalPages((totDocs + docsPerPage - 1) / docsPerPage);

		int fromIndex = (currentPage - 1) * docsPerPage;
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		int toIndex = fromIndex + docsPerPage;
		if (toIndex > totDocs - 1) {
			toIndex = totDocs;
		}
		if (fromIndex < toIndex)
			documentList = documentList.subList(fromIndex, toIndex);
		else {
			paginationVo.setCurrentPage((paginationVo.getTotalPages() != 0) ? paginationVo.getTotalPages() : 1);
		}

		rrVo.setDocsList(documentList);
		rrVo.setPaginationVo(paginationVo);

		return rrVo;
	}

	public void deleteToRecycle(Document document) {
		Folder recycle = fetchFolderFromPath("/04 ����վ");
		FolderSet fs = document.get_FoldersFiledIn();
		Folder localFolder1;
		for (Iterator<Folder> localIterator = EngineCollectionUtils.c(fs, Folder.class).iterator(); localIterator.hasNext(); localFolder1 = localIterator
				.next())
			;
	}

	public boolean copyDocument(String sourceDocumentId, String destParentFolderId) {
		return false;
	}

	public void moveDocument(String sourceDocumentId, String sourceParentFolderId, String destParentFolderId) throws Exception {
		try {
			Document document = fetchDocument(sourceDocumentId);
			ReferentialContainmentRelationship rel = fetchFolder(sourceParentFolderId).unfile(document);
			rel.save(RefreshMode.NO_REFRESH);
			ReferentialContainmentRelationship destFolder = fetchFolder(destParentFolderId).file(document, AutoUniqueName.AUTO_UNIQUE, null,
					DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
			destFolder.save(RefreshMode.NO_REFRESH);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<HistoryDocumentVo> getDocVerson(String docId) {
		List<HistoryDocumentVo> versionList = new ArrayList<HistoryDocumentVo>();
		Document document = fetchDocument(docId);

		VersionableSet versionSet = document.get_Versions();

		Iterator<?> it = versionSet.iterator();
		while (it.hasNext()) {
			HistoryDocumentVo historyDocument = new HistoryDocumentVo();
			Document versionaDoc = (Document) it.next();

			VersionStatus versionS = versionaDoc.get_VersionStatus();
			int versionStatus = versionS.getValue();
			if (versionStatus == 1) {
				historyDocument.setVersionStatus("released");
			} else if (versionStatus == 4) {
				historyDocument.setVersionStatus("superseded");
			}

			String documentTitle = versionaDoc.getProperties().getStringValue("DocumentTitle");
			historyDocument.setDocumentTitle(documentTitle);

			String mimeType = versionaDoc.getProperties().getStringValue("MimeType");
			historyDocument.setMimeType(mimeType);

			String majorVersionNumber = versionaDoc.get_MajorVersionNumber().toString();
			String minorVersionNumber = versionaDoc.get_MinorVersionNumber().toString();
			historyDocument.setMajorVersionNumber(majorVersionNumber);
			historyDocument.setMinorVersionNumber(minorVersionNumber);

			String lastModifier = versionaDoc.get_LastModifier();
			historyDocument.setLastModifier(lastModifier);

			SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateLastModified = versionaDoc.get_DateLastModified();
			String lastmo = sd2.format(dateLastModified);
			historyDocument.setDateLastModified(lastmo);

			String id = versionaDoc.get_Id().toString();
			historyDocument.setId(id);

			versionList.add(historyDocument);
		}

		return versionList;
	}

	public List<PermissionsVo> getDocumentPermissions(Document document) throws Exception {
		ArrayList<PermissionsVo> permissionsList = new ArrayList<PermissionsVo>();
		AccessPermissionList apl = document.get_Permissions();
		for (int i = 0; i < apl.size(); ++i) {
			PermissionsVo pvo = new PermissionsVo();
			try {
				AccessPermission ap = (AccessPermission) apl.get(i);
				String granteeName = ap.get_GranteeName();
				String title = granteeName.split("@")[0];
				pvo.setTitle(title);

				int mask = ap.get_AccessMask().intValue();
				AccessLevel accessLevel = AccessLevel.getInstanceFromInt(mask);

				if (accessLevel.equals(AccessLevel.FULL_CONTROL_DOCUMENT)) {
					pvo.setAllControl(true);
					pvo.setUpGradeVersion(true);
					pvo.setUpdatePath(true);
					pvo.setUpdateProperties(true);
					pvo.setViewPath(true);
					pvo.setViewProperties(true);
					pvo.setPublish(true);
				} else if (accessLevel.equals(AccessLevel.VIEW)) {
					pvo.setAllControl(false);
					pvo.setUpGradeVersion(false);
					pvo.setUpdatePath(false);
					pvo.setUpdateProperties(false);
					pvo.setViewPath(true);
					pvo.setViewProperties(true);
					pvo.setPublish(false);
				} else {
					pvo.setAllControl(false);
					pvo.setUpGradeVersion(false);
					pvo.setUpdatePath(false);
					pvo.setUpdateProperties(false);
					pvo.setViewPath(false);
					pvo.setViewProperties(false);
					pvo.setPublish(false);
				}
				permissionsList.add(pvo);
			} catch (Exception e) {
				throw e;
			}
		}
		return permissionsList;
	}

	public boolean updateDocumentPermissions(String id, List<PermissionsVo> pervoList) throws Exception {
		boolean flag = false;
		Document doc = fetchDocument(id);
		AccessPermissionList apl = doc.get_Permissions();
		int length = apl.size();

		for (int i = 0; i < length; ++i) {
			try {
				AccessPermission ap = (AccessPermission) apl.get(i);
				String granteeName = ap.get_GranteeName();
				String title = granteeName.split("@")[0];
				boolean breakflag = false;
				for (PermissionsVo perVo : pervoList) {
					String perVoTitle = perVo.getTitle();
					if (perVoTitle.equals(title)) {
						breakflag = true;
						if ((perVo.isAllControl()) && (perVo.isUpGradeVersion()) && (perVo.isUpdatePath()) && (perVo.isUpdateProperties())
								&& (perVo.isViewPath()) && (perVo.isViewProperties()) && (perVo.isPublish())) {
							ap.set_AccessMask(Integer.valueOf(999415));
						} else if ((!(perVo.isAllControl())) && (!(perVo.isUpGradeVersion())) && (!(perVo.isUpdatePath()))
								&& (!(perVo.isUpdateProperties())) && (perVo.isViewPath()) && (perVo.isViewProperties()) && (!(perVo.isPublish()))) {
							ap.set_AccessMask(Integer.valueOf(131073));
						} else if ((!(perVo.isAllControl())) && (!(perVo.isUpGradeVersion())) && (!(perVo.isUpdatePath()))
								&& (!(perVo.isUpdateProperties())) && (!(perVo.isViewPath())) && (!(perVo.isViewProperties()))
								&& (!(perVo.isPublish()))) {
							ap.set_AccessMask(Integer.valueOf(999415));
							ap.set_AccessType(AccessType.DENY);
						}
					}
					if (breakflag)
						;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
		return flag;
	}

	public List<Folder> getFolderListFromRootFolder() {
		List<Folder> folderList = new ArrayList<Folder>();
		FolderSet fs = this.os.get_RootFolder().get_SubFolders();

		for (Folder folder : EngineCollectionUtils.c(fs, Folder.class)) {
			String folderName = folder.get_Name();
			if ((folderName.equalsIgnoreCase("Access Roles")) || (folderName.equalsIgnoreCase("Preferences"))
					|| (folderName.equalsIgnoreCase("CodeModules")))
				continue;
			folderList.add(folder);
		}

		return folderList;
	}

	public List<Folder> getFolderListFromFolder(Folder folder) {
		List<Folder> folderList = new ArrayList<Folder>();
		FolderSet folderSet = folder.get_SubFolders();
		for (Folder subFolder : EngineCollectionUtils.c(folderSet, Folder.class)) {
			folderList.add(subFolder);
		}
		return folderList;
	}

	public List<Document> getDocumentListFromFolder(Folder folder, String symbolicClassName) {
		List<Document> documentList = new ArrayList<Document>();
		DocumentSet documentSet = folder.get_ContainedDocuments();
		Iterator<?> iterator = documentSet.iterator();
		while (iterator.hasNext()) {
			Document document = (Document) iterator.next();
			String className = document.getClassName();
			if (symbolicClassName == null)
				documentList.add(document);
			else if (symbolicClassName.equals(className)) {
				documentList.add(document);
			}
		}
		return documentList;
	}

	public List<Document> searchRows(String className, String searchExpr, boolean includeSubclasses) {
		Search search = new Search();
		return search.setRowSql(className, includeSubclasses, searchExpr).setScope(this.os).setPropertyFilter().fetchRows();
	}

	public void getDocumentClass() throws Exception {
		PropertyFilter filter = null;

		ClassDescription cd = Factory.ClassDescription.fetchInstance(this.os, "MyCard", filter);
		PropertyDescriptionList pds = cd.get_PropertyDescriptions();

		Iterator<?> it = pds.iterator();
		while (it.hasNext()) {
			PropertyDescription propDesc = (PropertyDescription) it.next();

			if ((propDesc.get_IsSystemGenerated().booleanValue()) || (propDesc.get_IsSystemOwned().booleanValue())) {
				continue;
			}

			String displayname = propDesc.get_DisplayName();
			System.out.print("displayname========" + displayname);
			System.out.print("    displayname IsHidden() " + propDesc.get_IsHidden());
			System.out.print("    displayname IsOrderable() " + propDesc.get_IsOrderable());
			System.out.print("    displayname get_IsSearchable() " + propDesc.get_IsSearchable());
			System.out.print("    displayname get_IsSearchable() " + propDesc.get_IsSearchable());
			System.out.print("    displayname get_IsReadOnly() " + propDesc.get_IsReadOnly());
			System.out.print("    displayname get_IsSelectable() " + propDesc.get_IsSelectable());
			System.out.println("    displayname get_IsValueRequired() " + propDesc.get_IsValueRequired());
		}
	}

	public void showProperties(String className) {
		PropertyFilter filter = new PropertyFilter();
		filter.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);
		ClassDescription cd = Factory.ClassDescription.fetchInstance(this.os, className, filter);
		PropertyDescriptionList pds = cd.get_PropertyDescriptions();
		for (PropertyDescription propDesc : EngineCollectionUtils.c(pds, PropertyDescription.class)) {
			if ((propDesc.get_IsSystemGenerated().booleanValue()) || (propDesc.get_IsSystemOwned().booleanValue()))
				continue;
			System.out.println("name: " + propDesc.get_Name() + ", type: " + propDesc.get_DataType());
		}
	}

	public void updateAttrs(IndependentlyPersistableObject ipo, Map<String, Object> updatedAttrs) {
		for (String attrName : updatedAttrs.keySet()) {
			ipo.getProperties().putObjectValue(attrName, updatedAttrs.get(attrName));
		}
		ipo.save(RefreshMode.REFRESH);
	}

	public void deleteInstancesByQueryStringWithBatch(String documentClassName, String queryString) {
		Search search = new Search();
		IndependentObjectSet independentObjectSet = search.setObjectSql(documentClassName, true, queryString).setScope(this.os).setPropertyFilter()
				.fetchIndependentObjectSet();
		deleteInstancesWithBatch(independentObjectSet);
	}

	private void deteleAllContainedObjectsWithBatch(UpdatingBatch ub, String folderId) {
		Folder folder = fetchFolder(folderId);
		FolderSet folderSet = folder.get_SubFolders();
		DocumentSet documentSet = folder.get_ContainedDocuments();
		if (folderSet.isEmpty()) {
			deleteInstancesWithBatch(ub, documentSet);
		} else {
			Iterator<?> iter = folderSet.iterator();
			while (iter.hasNext()) {
				Folder f = (Folder) iter.next();
				deteleAllContainedObjectsWithBatch(ub, f.get_Id().toString());
			}
		}
		folder.delete();

		ub.add(folder, null);
	}

	private void deleteInstancesWithBatch(UpdatingBatch ub, IndependentObjectSet independentObjectSet) {
		Iterator<?> myObjectsIter = independentObjectSet.iterator();
		if (!(myObjectsIter.hasNext()))
			return;
		while (myObjectsIter.hasNext()) {
			IndependentlyPersistableObject ipo = (IndependentlyPersistableObject) myObjectsIter.next();
			ipo.delete();

			ub.add(ipo, null);
		}
	}

	private void deleteInstancesWithBatch(IndependentObjectSet independentObjectSet) {
		Iterator<?> myObjectsIter = independentObjectSet.iterator();
		int i = 0;
		if (myObjectsIter.hasNext()) {
			UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(this.os.get_Domain(), RefreshMode.REFRESH);
			while (myObjectsIter.hasNext()) {
				IndependentlyPersistableObject ipo = (IndependentlyPersistableObject) myObjectsIter.next();
				ipo.delete();

				ub.add(ipo, null);
				++i;
			}

			ub.updateBatch();
		}
		this.logger.info("Batch deleted result count: " + i);
	}

	private void deleteInstances(IndependentObjectSet independentObjectSet) {
		Iterator<?> myObjectsIter = independentObjectSet.iterator();
		int i = 0;
		while (myObjectsIter.hasNext()) {
			IndependentlyPersistableObject ipo = (IndependentlyPersistableObject) myObjectsIter.next();
			ipo.delete();
			ipo.save(RefreshMode.NO_REFRESH);
			++i;
		}
		this.logger.info("Deleted result count: " + i);
	}

	public Map<String, Object> getProperty(String id, HashMap<String, Object> parameterMap, String symbolicClassName) {
		Properties properties;
		Iterator<?> it;
		Property prop;
		int i;
		if (symbolicClassName.equals("Document")) {
			Document document = fetchDocument(id);
			properties = document.getProperties();
			for (it = properties.iterator(); it.hasNext();) {
				prop = (Property) it.next();
				for (i = 0; i < parameterMap.size(); ++i) {
					if (parameterMap.containsKey(prop.getPropertyName()))
						parameterMap.put(prop.getPropertyName(), prop.getObjectValue());
				}
			}
		} else if (symbolicClassName.equals("Folder")) {
			Folder folder = fetchFolder(id);
			properties = folder.getProperties();
			for (it = properties.iterator(); it.hasNext();) {
				prop = (Property) it.next();
				for (i = 0; i < parameterMap.size(); ++i) {
					if (parameterMap.containsKey(prop.getPropertyName())) {
						parameterMap.put(prop.getPropertyName(), prop.getObjectValue());
					}
				}
			}
		}
		return parameterMap;
	}

	public List<Object> getProperty(String id, List<String> parameterList, String symbolicClassName) {
		List<Object> valueList = new ArrayList<Object>();
		Properties properties;
		Iterator<?> it;
		Property prop;
		int i;
		if (symbolicClassName.equals("Document")) {
			Document document = fetchDocument(id);
			properties = document.getProperties();
			for (it = properties.iterator(); it.hasNext();) {
				prop = (Property) it.next();
				for (i = 0; i < parameterList.size(); ++i)
					if (parameterList.get(i) == prop.getPropertyName())
						valueList.set(i, prop.getObjectValue());
			}
		} else if (symbolicClassName.equals("Folder")) {
			Folder folder = fetchFolder(id);
			properties = folder.getProperties();
			for (it = properties.iterator(); it.hasNext();) {
				prop = (Property) it.next();
				for (i = 0; i < parameterList.size(); ++i) {
					if (parameterList.get(i) == prop.getPropertyName())
						valueList.set(i, prop.getObjectValue());
				}
			}
		}
		return valueList;
	}

	public Object getProperty(String id, String parameter, String symbolicClassName) {
		Object value = new Object();
		Properties properties;
		Iterator<?> it;
		Property prop;
		if (symbolicClassName.equals("Document")) {
			Document document = fetchDocument(id);
			properties = document.getProperties();
			for (it = properties.iterator(); it.hasNext();) {
				prop = (Property) it.next();
				if (parameter == prop.getPropertyName())
					value = prop.getObjectValue();
			}
		} else if (symbolicClassName.equals("Folder")) {
			Folder folder = fetchFolder(id);
			properties = folder.getProperties();
			for (it = properties.iterator(); it.hasNext();) {
				prop = (Property) it.next();
				if (parameter == prop.getPropertyName())
					value = prop.getObjectValue();
			}
		}
		return value;
	}

	public void docLinkToFolder(String docId, String linkTofolderId) throws Exception {
		try {
			Folder folder = fetchFolder(linkTofolderId);
			List<Document> documentList = getDocumentListFromFolder(folder, null);
			String compareString = "";
			for (int i = 0; i < documentList.size(); ++i) {
				compareString = documentList.get(i).get_Id().toString();

				if (compareString.substring(1, compareString.length() - 1).equalsIgnoreCase(docId)) {
					return;
				}
			}
			Document document = fetchDocument(docId);
			ReferentialContainmentRelationship rcr = folder.file(document, AutoUniqueName.AUTO_UNIQUE, document.get_Name(),
					DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);

			rcr.save(RefreshMode.NO_REFRESH);
		} catch (Exception e) {
			throw e;
		}
	}

	public void docUnlinkToFolder(String docId, String linkTofolderId) throws Exception {
		try {
			Folder folder = fetchFolder(linkTofolderId);
			Document document = fetchDocument(docId);
			ReferentialContainmentRelationship rcr = folder.unfile(document);
			rcr.save(RefreshMode.NO_REFRESH);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<Folder> getDocumentContainer(Document doc) {
		ReferentialContainmentRelationshipSet linkSet = doc.get_Containers();
		List<Folder> folderList = new ArrayList<Folder>();
		for (Iterator<?> it = linkSet.iterator(); it.hasNext();) {
			ReferentialContainmentRelationship rcr = (ReferentialContainmentRelationship) it.next();
			Folder folder = (Folder) rcr.get_Tail();
			folderList.add(folder);
		}
		return folderList;
	}

	public void publishDocument(Document document, String desFolderId, String publishName, String TemplateId) throws Exception {
		try {
			PublishTemplate myTemplate = Factory.PublishTemplate.fetchInstance(this.os, new Id(TemplateId), null);
			String publishOptions = "<?xml version='1.0'?><publishoptions><publicationname>" + publishName + "</publicationname>"
					+ "<outputfolderid>" + desFolderId + "</outputfolderid>" + "</publishoptions>";
			PublishRequest myRequest = document.publish(myTemplate, publishOptions);
			myRequest.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			throw e;
		}
	}
}