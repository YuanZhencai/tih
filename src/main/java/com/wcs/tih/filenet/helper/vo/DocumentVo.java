package com.wcs.tih.filenet.helper.vo;

import java.util.Date;
import java.util.Iterator;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;

public abstract class DocumentVo extends EntityVo {
	private String creator;
	private Date dateLastModified;
	private Date dateCreated;
	private String majorVersionNumber;
	private ContentElementList contentElements = null;
	private String attachIds = null;

	public String getAttachIds() {
		if (this.attachIds == null) {
			return new String();
		}
		return this.attachIds;
	}

	public void setAttachIds(String attachIds) {
		this.attachIds = attachIds;
	}

	public String getDocumentPath(Document document) {
		FolderSet folderSet = document.get_FoldersFiledIn();
		
		for (Iterator<?> it = folderSet.iterator(); it.hasNext();) {
			Folder folder = (Folder) it.next();
			DocumentSet ds = folder.get_ContainedDocuments();
			for (Iterator<?> its = ds.iterator(); its.hasNext();) {
				Document doc = (Document) its.next();
				if (doc.get_Id().equals(document.get_Id())) {
					return folder.get_PathName();
				}
			}
		}
		return null;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getDateLastModified() {
		return this.dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDocCreateTime() {
		return this.dateCreated;
	}

	public void setDocCreateTime(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getMajorVersionNumber() {
		return this.majorVersionNumber;
	}

	public void setMajorVersionNumber(String majorVersionNumber) {
		this.majorVersionNumber = majorVersionNumber;
	}

	public ContentElementList getContentElements() {
		return this.contentElements;
	}

	public void setContentElements(ContentElementList contentElements) {
		this.contentElements = contentElements;
	}

	public DocumentVo fromDocument(Document doc) {
		setVersionSeriesId(doc.get_VersionSeries().get_Id().toString());
		setId(doc.get_Id().toString());
		setName(doc.get_Name());
		setClassName(doc.getClassName());
		setPath(getDocumentPath(doc));

		if ("DocSearch".equals(this.className))
			setMimeType("folder");
		else {
			setMimeType(doc.get_MimeType());
		}

		setCreator(doc.get_Creator());
		setCheckedOut(doc.get_IsReserved().booleanValue());
		setDateLastModified(doc.get_DateLastModified());
		setDateCreated(doc.get_DateCreated());
		setMajorVersionNumber(doc.get_MajorVersionNumber().toString());
		// setParentId(doc.getProperties().getStringValue("EkpParentId"));
		fromCustom(doc);
		return this;
	}

	protected abstract void fromCustom(Document paramDocument);
}