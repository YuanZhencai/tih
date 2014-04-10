package com.wcs.common.download;

import java.io.InputStream;
import java.util.Iterator;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
import com.wcs.common.upload.util.MimeUtil;
import com.wcs.tih.filenet.ce.service.CEserviceImpl;
import com.wcs.tih.filenet.common.service.CEBase;

public class FileNetDownload {

	
	private String fileName;
	
	private String id;
	
	private InputStream contentStream;
	
	private String mimeType;
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InputStream getContentStream() {
		return contentStream;
	}

	public void setContentStream(InputStream contentStream) {
		this.contentStream = contentStream;
	}


	
	public StreamedContent downloadDocument(String documentId) throws Exception {
		// TODO Auto-generated method stub
		
		CEserviceImpl c=new CEserviceImpl();
		c.connect("fnadmin", "Bttt,001");
		ObjectStore objectStore = c.getStore();
		Document document = Factory.Document.fetchInstance(objectStore, new Id(documentId), null);
		Id id = document.get_Id();
		if(id != null) {
			this.setId(id.toString());
		}

		String mimeType = document.get_MimeType();
		
		this.setMimeType(mimeType);
		Properties properties = document.getProperties();
		
		this.setFileName(properties.getStringValue("documentTitle")+"."+MimeUtil.getMimeTypeWidhValue(this.getMimeType()));

		
		ContentElementList contentElementList = document.get_ContentElements();
		ContentElement contentElement;
		Iterator contentElementListItr = contentElementList.iterator();
		while(contentElementListItr.hasNext()) {
			contentElement = (ContentElement) contentElementListItr.next();
			ContentTransfer contentTransfer = (ContentTransfer) contentElement;
			InputStream inputStream = contentTransfer.accessContentStream();
			this.setContentStream(inputStream);
		}
		return new DefaultStreamedContent(this.getContentStream(),this.getMimeType(),this.getFileName());
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}
