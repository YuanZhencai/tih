package com.wcs.tih.document.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.DefaultStreamedContent;

import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.service.NoticeService;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.document.controller.vo.CompanyVO;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.filenet.pe.service.DefaultWorkflowImpl;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.system.service.OrganizationLevelInterface;
import com.wcs.tih.system.service.PositionProfessionInterface;

import filenet.vw.api.VWException;

@Stateless
public class DocumentAuditService extends DefaultWorkflowImpl {
	private static String passFloder = ResourceBundle.getBundle("filenet").getString("ce.folder.auditPassed");
	private static String notPassFloder = ResourceBundle.getBundle("filenet").getString("ce.folder.newAuditedNotPassed");
	@EJB
	private CEserviceLocal ceservice;
	@PersistenceContext
	private EntityManager em;
	@EJB
	private DocumentService documentService;
	@EJB
	private PositionProfessionInterface positionProfessionInterface;
	@EJB
	private UserCommonService userservice;
	@EJB
	private LoginService loginservice;
	@EJB
	private FileNetUploadDownload file;
	@EJB
	private TDSLocal tds;
	@EJB
	private OrganizationLevelInterface organizationLevelInterface;
	@EJB
	private UserCommonService userCommonService;

	public FnDocument conversionDocument(Document doc) {
		return this.documentService.conversionDocument(doc);
	}

	public FnDocument getDocuments(String id) throws Exception {
		this.documentService.connect();
		return this.conversionDocument(ceservice.getDocument(id));
	}

	public void bindUserRoleToDoc(String fileId, String userName) throws Exception {
		this.documentService.bindUserRoleToDoc(fileId, this.getAuthorizer(userName, DictConsts.TIH_TAX_REQUESTFORM_1), false);
	}

	public void removeUserRoleFromDoc(String fileId, String userName) throws Exception {
		this.documentService.removeUserRoleFromDoc(fileId, userName);
	}

	public List<UsermstrVo> getuserList(String type) {
		return positionProfessionInterface.getUsermstrVo(type);
	}

	private void movDoc(FnDocument fd, String targetPath) throws Exception {
		this.documentService.simpleMoveDoc(fd, targetPath);
	}

	public String remove(String str) {
		return tds.removePre(str);
	}

	public void movNotpass(FnDocument fd) throws Exception {
		this.movDoc(fd, notPassFloder);
	}

	public void movPass(FnDocument fd) throws Exception {
		this.movDoc(fd, passFloder);
	}

	public List<String> getAllUsersOfGroup(String groupName) throws Exception {
		return this.documentService.getAllUsersOfGroup(groupName);
	}

	public void editDocProperty(FnDocument fd) throws Exception {
		this.documentService.editDocProperty(fd);
	}

	public String getUserName(String str) {
		if (str == null) {
			return "";
		}
		if (this.userservice.getUsermstrVo(str) == null) {
			return str;
		}
		P p = this.userservice.getUsermstrVo(str).getP();
		if (p == null) {
			return str;
		}
		return p.getNachn();
	}

	public void replaseDoc(FnDocument fn, String workflowNumber, Document d, String changedBy) throws Exception {
		this.documentService.bindUserRoleToDoc(d.get_Id().toString(), changedBy, false);
		this.documentService.replaceDocument(fn.getId(), d.get_Id().toString());
		this.updateDocument(workflowNumber, d.get_Id().toString());
	}

	public void updateDocument(String workflowNumber, String value) {
		String sql = " select s from WfInstancemstrProperty  s where s.wfInstancemstr.no='" + workflowNumber + "' and s.name ='"
				+ WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID + "' ";
		WfInstancemstrProperty w = (WfInstancemstrProperty) this.em.createQuery(sql).getResultList().get(0);
		w.setValue(value);
		this.em.merge(w);
	}

	public String replaceChangeBy() throws Exception {
		String curName = this.loginservice.getCurrentUsermstr().getAdAccount();
		String authorName = this.userCommonService.getAuthorizer(curName, DictConsts.TIH_TAX_REQUESTFORM_1);
		List<String> list = this.getAllUsersOfGroup("fnadmins");
		if (list.size() <= 0) {
			throw new Exception("fnadmins没有数据");
		}
		if (list.contains(null)) {
			throw new Exception("fnadmins有空值");
		}
		@SuppressWarnings("unused")
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(authorName)) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			list.remove(curName);
			list.add(authorName);
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(",");
		}
		String returnStr = sb.toString();
		return returnStr.substring(0, returnStr.length() - 1);
	}

	public void addRole(String curUser, String nextUser) {

	}

	public DefaultStreamedContent getFile(String id) throws Exception {
		return (DefaultStreamedContent) this.file.downloadDocument(id);
	}

	public List<CompanyVO> getCompanys(Map<String, String> m) {
		return null;
	}

	public void bindAndRemoveRole(String fileId, String removeUser, String bindUser) throws Exception {
		this.documentService.bindAndRemoveRole(fileId, removeUser, this.getAuthorizer(bindUser, DictConsts.TIH_TAX_REQUESTFORM_1));
	}

	public WfInstancemstr createWorkflow(Document dd, Long companyId, WfRemindVo wfRemindVo) throws Exception {
		HashMap<String, String> stepMap = new HashMap<String, String>();
		stepMap.put(WorkflowConsts.TIH_AUDITE_OPIONION, " ");
		HashMap<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID, dd.get_Id().toString());
		propertyMap.put(WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, DictConsts.TIH_TAX_REQUESTFORM_1);
		// 上级主管
		String superviosrAccount = organizationLevelInterface
				.getUsermstrVo(loginservice.getCurrentUsermstr().getAdAccount(), DictConsts.TIH_TAX_REQUESTFORM_1, companyId).getUsermstr()
				.getAdAccount();
		this.documentService
				.bindUserRoleToDoc(dd.get_Id().toString(), this.getAuthorizer(superviosrAccount, DictConsts.TIH_TAX_REQUESTFORM_1), false);
		return super.createworkflow(propertyMap, DictConsts.TIH_TAX_REQUESTFORM_1, WorkflowConsts.UPLOAD_DOCUMENT_AUDIT_WORKFLOW, stepMap,
				WorkflowConsts.WORK_FLOW_PARAM_SUPERVISOR, superviosrAccount, dd.get_Name(), DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1,
				DictConsts.TIH_TAX_WORKFLOWURGENCY_1, wfRemindVo);
	}

	public void editPermissions(String chargedBy, WfInstancemstr wf, FnDocument doc) throws Exception {
		String currentUserName = loginservice.getCurrentUserName();
		if (!chargedBy.equals(" ") && chargedBy.indexOf(',') == -1) {
			// 非文档管理岗

			// 得到下一节点授权人
			String authorizer = getAuthorizer(chargedBy, DictConsts.TIH_TAX_REQUESTFORM_1);
			if (!currentUserName.equals(authorizer)) {
				if (!authorizer.equals(wf.getCreatedBy())) {
					// 绑定权限
					this.documentService.bindUserRoleToDoc(doc.getId(), authorizer, false);
				}
				if (!currentUserName.equals(wf.getCreatedBy())) {
					// 移除权限
					this.documentService.removeUserRoleFromDoc(doc.getId(), currentUserName);
				}
			}
		} else {
			// 文档管理岗
			if (!currentUserName.equals(wf.getCreatedBy())) {
				// 移除权限
				this.documentService.removeUserRoleFromDoc(doc.getId(), currentUserName);
			}
		}

	}
}