package com.wcs.common.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.StreamedContent;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.security.AccessPermission;
import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.FilemstrVo;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.model.Filemstr;

@Stateless
public class AttachmentService {
	
	@PersistenceContext 
    private EntityManager em;
	
	@EJB
	private FileNetUploadDownload fud;
	@EJB
	private LoginService loginService;
	@EJB 
	private CEserviceLocal ceservice;
	@EJB 
	private TDSLocal tdsLocal;
	
	// 资源
    private ResourceBundle rb = ResourceBundle.getBundle("filenet");
    private final String classid  = "ce.document.classid";
    private ResourceBundle role = ResourceBundle.getBundle("roles");
    private String documentAdmin = "fnadmins";
    private final String uid = rb.getString("tds.users.uid");
    private final String cn = "cn=";
    private final String defaultGroup = "#AUTHENTICATED-USERS";
    
	
    /**
     * <p>Description: 上传项目附件</p>
     * @param is
     * @param fileName 文档名称
     * @param classid
     * @param projectFolder
     * @return
     * @throws Exception
     */
    public String uploadAttachments(InputStream is, String fileName, String fullFoldername) throws Exception {
    	floderName(fullFoldername);
        Document doc = fud.upLoadDocument(is, new HashMap<String, Object>(), 
                fileName, rb.getString(classid), fullFoldername);
        return doc.get_Id().toString();
    }
    
    /**
     * 
     * <p>Description: 判断上传的文件夹是否存在；不存在，则新建文件夹</p>  
     * @param fullFoldername
     * @throws Exception
     */
    private void floderName(String fullFoldername) throws Exception{
    	ceservice.connect(tdsLocal.addPre(rb.getString("admin.id")), rb.getString("admin.password"));
    	String[] split = fullFoldername.split("/");
    	String parentFoldername = "/";
    	for(int i=1;i < split.length; i++){
    		try {
    			ceservice.getFolder(parentFoldername + split[i]);
    			parentFoldername = parentFoldername + split[i] + "/";
    		}catch (Exception e) {
    			Folder parent = ceservice.getFolder(parentFoldername);
    			Folder sub = ceservice.createSubFolder(parent, split[i]);
    			parentFoldername = parentFoldername + split[i] + "/";
    			
    			String an = tdsLocal.addPre(role.getString(documentAdmin));
                boolean ia = false;
                
                AccessPermissionList apl = sub.get_Permissions();
                for(Iterator<AccessPermission> it = apl.iterator(); it.hasNext();) {
                    AccessPermission ap = it.next();
                    String n = getGrantName(ap.get_GranteeName().toString(), ap.get_GranteeType().toString());
                    if(n.equals(defaultGroup)) {
                        ap.set_AccessType(AccessType.ALLOW);
                        ap.set_AccessMask(131121);
                    }
                    if(n.equals(an)) {
                        ia = true;
                    }
                }
                // 添加文档管理员组完全控制
                if(!ia) {
                    AccessPermission ap = createPermissionWithPre(an, 999415);
                    apl.add(ap);
                }
                
                sub.set_Permissions(apl);
                sub.save(RefreshMode.REFRESH);
    		}
    	}
    }
    
    /**
     * <p>Description: 截取用户帐号</p>
     * @param grantName
     * @param type
     * @return
     */
    private String getGrantName(String grantName, String type) {
        String name;
        if(type.equals("USER")) {
            name = grantName.indexOf(uid) == -1 ? grantName : grantName.substring(grantName.indexOf(uid), grantName.indexOf(",")).substring(4);
        } else if(type.equals("GROUP")) {
            name = grantName.indexOf(cn) == -1 ? grantName : grantName.substring(grantName.indexOf(cn), grantName.indexOf(",")).substring(3);
        } else {
            name = grantName;
        }
        return name;
    }
    
    /**
     * <p>Description: 创建权限</p>
     * @param granteeName
     * @param mask
     * @return
     */
    private AccessPermission createPermissionWithPre(String granteeName, Integer mask) {
        AccessPermission ap = Factory.AccessPermission.createInstance();
        ap.set_AccessType(AccessType.ALLOW);
        ap.set_GranteeName(granteeName);
        ap.set_AccessMask(mask);
        return ap;
    }
    
    /**
     * <p>Description: 获取文件流</p>
     * @param fileid
     * @return
     * @throws Exception
     */
    public StreamedContent downloadAttachment(String fileid) throws Exception {
        return fud.downloadDocumentEncoding(fileid, "utf-8", "iso8859-1");
    }
    
    /**
     * <p>Description: 删除文档</p>
     * @param fileId
     * @throws Exception
     */
    private void deleteDocument(String fileId) throws Exception {
    	fud.deleteDocument(fileId);
    }
    
    public void saveFilemstr(Filemstr filemstr){
    	filemstr.setCreatedBy(loginService.getCurrentUsermstr().getAdAccount());
    	filemstr.setCreatedDatetime(new Date());
    	filemstr.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
    	filemstr.setUpdatedDatetime(new Date());
    	filemstr.setDefunctInd("N");
    	em.persist(filemstr);
    }
    
    public List<FilemstrVo> searchFilemstr(String tableName, Long entityId){
    	List<FilemstrVo> filemstrVos = new ArrayList<FilemstrVo>();
    	String sql = "select f from Filemstr f where f.tableName = '"+tableName+"' and f.entityId = "+entityId+" and f.defunctInd <> 'Y'";
    	List<Filemstr> resultList = em.createQuery(sql).getResultList();
		for(Filemstr f : resultList){
			FilemstrVo vo = new FilemstrVo();
			vo.setCreatedBy(f.getCreatedBy());
			vo.setCreatedDatetime(f.getCreatedDatetime());
			vo.setEntityId(f.getEntityId());
			vo.setFnId(f.getFnId());
			vo.setName(f.getName());
			vo.setId(f.getId());
			filemstrVos.add(vo);
		}
    	return filemstrVos;
    }
    
    /**
     * 
     * <p>Description: 删除数据库中的记录</p>  
     * @param fileId
     */
    private void deleteFilemstr(String fileId){
    	String sql = "delete from Filemstr f where f.fnId = '"+fileId+"' ";
    	em.createQuery(sql).executeUpdate();
    }
    
    public void deleteDocumentAndFilemstr(String fileId) throws Exception{
    	deleteDocument(fileId);
    	deleteFilemstr(fileId);
    }

}
