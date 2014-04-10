package com.wcs.common.upload.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.wcs.tih.filenet.ce.service.CEserviceImpl;

public class PrimefacesUploadUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private UploadedFile uploadFile;
	
	private Map  map;
	
	private String className;
	
	
	private String folderName;
	
	public void setUploadFile(UploadedFile uploadFile) {
		this.uploadFile = uploadFile;
	}



	public void setMap(Map map) {
		this.map = map;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}



	public void setCeservice(CEserviceImpl ceservice) {
		this.ceservice = ceservice;
	}

	private CEserviceImpl ceservice;
	
	public Document uploadFileNet() throws IOException {
		try {
			return ceservice.createDocument(uploadFile.getInputstream(), map, this.getMimeType(uploadFile.getFileName()), className, folderName,this.removeStrValue(uploadFile.getFileName()));
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	public List<Document> getDocsInThisFolder(String parentFolder){
		return ceservice.getDocsInThisFolder(parentFolder);
	}

	public PrimefacesUploadUtil(UploadedFile uploadFile, Map map,
			String className, String folderName) {
		ceservice=new CEserviceImpl();
		try {
            ceservice.connect("fnadmin", "Bttt,001");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage(),e);
        }
		this.uploadFile = uploadFile;
		this.map = map;
		this.className = className;
		this.folderName = folderName;
	}
	
	public PrimefacesUploadUtil(){
		ceservice=new CEserviceImpl();
		try {
            ceservice.connect("fnadmin", "Bttt,001");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
	}


	private String removeStrValue(String str){
		if(str.lastIndexOf('.')==-1){
			return str;
		}
		return str.substring(0, str.lastIndexOf('.'));
	}


	private String getMimeType(String name) {
		if (name.lastIndexOf('.') == -1) {
			throw new RuntimeException("name error" + name);
		}
		return MimeUtil.getMimeType(name.substring(name.lastIndexOf('.') + 1, name.length()));
	}

}
