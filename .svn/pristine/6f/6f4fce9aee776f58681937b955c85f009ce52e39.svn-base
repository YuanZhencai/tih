package com.wcs.tih.filenet.ce.service;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.Properties;
import com.filenet.api.util.Id;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.service.TDSLocal;
import com.wcs.tih.filenet.ce.util.MimeUtil;

@Stateless
public class FileNetUploadDownload {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static String configFileName = "filenet";

    private static String loadAdminPasswordKey = "admin.password";

    private static String loadAdminNameKey = "admin.id";

    private static String loadUserNameKey = "user.password";

    @EJB
    private CEserviceLocal ceserviceImpl;
    @EJB
    private LoginService loginservice;

    @EJB 
    private TDSLocal tds;
    private void connect() throws Exception {
        String userName = loginservice.getCurrentUserName();
        logger.info(userName);
        ceserviceImpl.connect(tds.addPre(userName), this.getPassword(userName));
    }

    private void connect(String userName, String password) throws Exception {
        ceserviceImpl.connect(tds.addPre(userName), password);
    }

    private String getPassword(String userName) {
        ResourceBundle filenetconfig = ResourceBundle.getBundle(configFileName);
        String adminName = filenetconfig.getString(loadAdminNameKey);
        String adminPwd = filenetconfig.getString(loadAdminPasswordKey);
        String userPwd = filenetconfig.getString(loadUserNameKey);
        if (userName.equals(adminName)) { 
        	return adminPwd; 
        }
        return userPwd;

    }

    public Document upLoadDocument(InputStream inputStream, Map map, String fileName, String classId, String foldername)
            throws Exception {
        connect();
        try {
            return ceserviceImpl.createDocument(inputStream, map, this.getMimeType(fileName), classId, foldername,
                    fileName);
        } catch (Exception e) {
            logger.error("file upload error" + fileName);
            throw new Exception(e.getMessage());
        }
    }
    public Document upLoadDocumentCheckIn(InputStream inputStream, Map map, String fileName, String classId, String foldername)
            throws Exception {
        connect();
        try {
        	map.put("auditStatus", DictConsts.TIH_DOC_STATUS_3);
            return ceserviceImpl.createDocument(inputStream, map, this.getMimeType(fileName), classId, foldername,
                    fileName);
        } catch (Exception e) {
            logger.error("file upload error" + fileName);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @deprecated
     */
    public Document upLoadDocument(String username, String password, InputStream inputStream, Map map, String fileName,
            String classId, String foldername) throws EngineRuntimeException, Exception {
        connect(username, password);
        try {
            return this.ceserviceImpl.createDocument(inputStream, map, this.getMimeType(fileName), classId, foldername,
                    fileName);
        } catch (Exception e1) {
            logger.error("file upload error" + fileName);
            throw e1;
        }
    }

    private String getMimeType(String name) {
        if (name.lastIndexOf('.') == -1) { 
        	return "application/octet-stream"; 
        }
        return MimeUtil.getMimeType(name.substring(name.lastIndexOf('.') + 1, name.length()));
    }

  

    public StreamedContent downloadDocument(String documentId) throws Exception {
        // TODO Auto-generated method stub
        connect();
        ObjectStore objectStore = ceserviceImpl.getStore();
        Document document = Factory.Document.fetchInstance(objectStore, new Id(documentId), null);
        String mimeType = document.get_MimeType();
        Properties properties = document.getProperties();
        ContentElementList contentElementList = document.get_ContentElements();
        ContentElement contentElement;
        Iterator contentElementListItr = contentElementList.iterator();
        InputStream inputStream = null;
        String fileName = "";
        while (contentElementListItr.hasNext()) {
            contentElement = (ContentElement) contentElementListItr.next();
            ContentTransfer contentTransfer = (ContentTransfer) contentElement;
            fileName = contentTransfer.get_RetrievalName();
            inputStream = contentTransfer.accessContentStream();
        }
        String lastName = "";
        if (fileName.lastIndexOf('.') != -1) {
            lastName = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        }
        return new DefaultStreamedContent(inputStream, mimeType, properties.getStringValue("documentTitle") + lastName);
    }
    
    
    /**
     * 下载编码处理
     * <p>Description: </p>
     * @param args
     * @throws Exception 
     */
    public StreamedContent downloadDocumentEncoding(String documentId,String encoding,String toEncoding) throws Exception{
        connect();
        ObjectStore objectStore = ceserviceImpl.getStore();
        Document document = Factory.Document.fetchInstance(objectStore, new Id(documentId), null);
        String mimeType = document.get_MimeType();
        Properties properties = document.getProperties();
        ContentElementList contentElementList = document.get_ContentElements();
        ContentElement contentElement;
        Iterator contentElementListItr = contentElementList.iterator();
        InputStream inputStream = null;
        String fileName = "";
        while (contentElementListItr.hasNext()) {
            contentElement = (ContentElement) contentElementListItr.next();
            ContentTransfer contentTransfer = (ContentTransfer) contentElement;
            fileName = contentTransfer.get_RetrievalName();
            inputStream = contentTransfer.accessContentStream();
        }
        String lastName = "";
        if (fileName.lastIndexOf('.') != -1) {
            lastName = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        }
        return new DefaultStreamedContent(inputStream, mimeType, new String(properties.getStringValue("documentTitle").getBytes(encoding),toEncoding) + lastName);
    }


    /**
     * @deprecated
     */
    public List<Document> getDocsInThisFolder(String userName, String password, String parentFolder) {
        try {
            connect(userName, password);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ceserviceImpl.getDocsInThisFolder(parentFolder);
    }

    public List<Document> getDocsInThisFolder(String parentFolder) throws Exception {
        connect();
        return ceserviceImpl.getDocsInThisFolder(parentFolder);
    }
    
    public void deleteDocument(String documentId) throws Exception{
    	connect();
    	try {
            Document d = ceserviceImpl.getDocument(new Id(documentId));
            if(d.get_IsReserved()) {
                throw new Exception("文档已经被检出，不可删除。");
            }
            if(d.getProperties() != null){
                String auditStatus = d.getProperties().getStringValue("auditStatus");
                if(DictConsts.TIH_DOC_STATUS_5.equals(auditStatus)) {
                    throw new Exception("文档已经检入，尚未审核，不可删除。");
                }
                if(DictConsts.TIH_DOC_STATUS_1.equals(auditStatus)) {
                    throw new Exception("文档已经上传，尚未审核，不可删除。");
                }
            }
            d.delete();
            d.save(RefreshMode.REFRESH);
        } catch (EngineRuntimeException e) {
            throw new Exception("删除错误，请刷新重试");
        } 
    }

}
