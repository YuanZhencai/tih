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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.util.Id;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.filenet.pe.service.DefaultWorkflowImpl;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.system.service.PositionProfessionInterface;

import filenet.vw.api.VWException;

/**
 * Project: tih Description: checkin service Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * 
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 */
@Stateless
public class DocumentCheckinService extends DefaultWorkflowImpl {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private PositionProfessionInterface positionProfessionInterface;
	@EJB
	private FileNetUploadDownload file;
	@EJB
	private UserCommonService userservice;
	@EJB
	private CEserviceLocal ceservice;
	@EJB
	private DocumentService documentservice;
	@PersistenceContext
	private EntityManager em;
	private static String notPassFloder = ResourceBundle.getBundle("filenet").getString("ce.folder.checkinNotPassed");

	public List<UsermstrVo> getuserList(String type) {
		return positionProfessionInterface.getUsermstrVo(type);
	}

	public DefaultStreamedContent getFile(String id) throws Exception {
		return (DefaultStreamedContent) this.file.downloadDocumentEncoding(id, "utf-8", "iso-8859-1");
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

	public FnDocument getDocuments(String id) {
		try {
			this.documentservice.connect();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return this.documentservice.conversionDocument(ceservice.getDocument(id));
	}

	private Map<String, String> getOldIdAndNewId(WfInstancemstr wf) {
		Map<String, String> m = new HashMap<String, String>();
		List<WfInstancemstrProperty> wfinps = getwfips(wf.getId());
		for (WfInstancemstrProperty ss : wfinps) {
			if (ss.getName().equals(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID)) {
				m.put(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID, ss.getValue());
			}
			if (ss.getName().equals(WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID)) {
				m.put(WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID, ss.getValue());
			}
		}
		return m;
	}

	public List<WfInstancemstrProperty> getwfips(Long id) {
		String sql = "select wfip from WfInstancemstrProperty wfip where wfip.wfInstancemstr.id = " + id + "";
		return em.createQuery(sql).getResultList();
	}

	public void bindUserRoleToDoc(String fileId, String userName) throws Exception {
		this.documentservice.bindUserRoleToDoc(fileId, userName, false);
	}

	public void removeUserRoleFromDoc(String fileId, String userName) throws Exception {
		this.documentservice.removeUserRoleFromDoc(fileId, userName);
	}

	public void checkInPass(WfInstancemstr wf, String creadeBy) throws Exception {
		Map<String, String> m = this.getOldIdAndNewId(wf);
		String newId = this.documentservice.checkinDoc(new Id(m.get(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID)),
				new Id(m.get(WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID)), wf.getCreatedBy());
		this.updateCheckIn(wf, newId);
		String wfCreadeBy = wf.getCreatedBy();
		if (!this.getLastStep(wf).getChargedBy().equals(creadeBy)) {
			logger.info("DocumentCheckinService.checkInPass()~~~~~~remover" + this.getLastStep(wf).getChargedBy());
			logger.info("~~~~~~~~~~~connct~~~~~~~~~~~" + wfCreadeBy);
			this.documentservice.bindUserRoleToDoc(newId, this.getLastStep(wf).getChargedBy(), true);
			this.documentservice.bindAndRemoveRole(newId, this.getLastStep(wf).getChargedBy(), wf.getCreatedBy());
		} else {
			this.documentservice.bindUserRoleToDoc(newId, wf.getCreatedBy(), false);
		}
	}

	public void checkInNotPass(WfInstancemstr wf, FnDocument fnz) {
		try {
			FnDocument fn = new FnDocument();
			fn.setId(this.getCheckInId(wf));
			fn.setAuditStatus(DictConsts.TIH_DOC_STATUS_7);
			this.documentservice.editDocProperty(fn);
			this.documentservice.simpleMoveDoc(fn, this.notPassFloder);
			Document d = ceservice.getDocument(new Id(fnz.getId()));
			d.getProperties().putValue("auditStatus", "TIH.DOC.STATUS.4");
			// 改为检出状态
			d.save(RefreshMode.REFRESH);
			this.documentservice.bindAndRemoveRole(fnz.getId().toString(), this.getLastStep(wf).getChargedBy(), wf.getCreatedBy());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void bindAndRemoveRole(String fileId, String removeUser, String bindUser) throws Exception {
		this.documentservice.bindAndRemoveRole(fileId, removeUser, this.getAuthorizer(bindUser, DictConsts.TIH_TAX_REQUESTFORM_2));
	}

	public WfInstancemstr createworkflow(String sourceId, Document dd, String perUserName, String supervisor, WfRemindVo wfRemindVo) throws VWException, Exception {
		documentservice.bindAdimnToDoc(dd.get_Id().toString());
		HashMap<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID, sourceId);
		propertyMap.put(WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID, dd.get_Id().toString());

		propertyMap.put(WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, DictConsts.TIH_TAX_REQUESTFORM_2);

		HashMap<String, String> stepMap = new HashMap<String, String>();
		stepMap.put(WorkflowConsts.TIH_AUDITE_OPIONION, " ");
		this.documentservice.bindUserRoleToDoc(dd.get_Id().toString(), this.getAuthorizer(supervisor, DictConsts.TIH_TAX_REQUESTFORM_2), false);
		this.documentservice.bindUserRoleToDoc(sourceId, this.getAuthorizer(supervisor, DictConsts.TIH_TAX_REQUESTFORM_2), false);
		String userName = perUserName + "," + this.getAuthorizer(supervisor, DictConsts.TIH_TAX_REQUESTFORM_2);
		propertyMap.put(WorkflowConsts.TIH_DCOUMENT_CHECKIN_PERMISSION, userName);
		return super.createworkflow(propertyMap, DictConsts.TIH_TAX_REQUESTFORM_2, WorkflowConsts.TIH_CHECKIN_DOCUMENT_AUDIT_WORKFLOW, stepMap,
				WorkflowConsts.WORK_FLOW_PARAM_SUPERVISOR, supervisor, dd.get_Name(), DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1,
				DictConsts.TIH_TAX_WORKFLOWURGENCY_1, wfRemindVo);
	}

	public String getCheckInId(WfInstancemstr wf) {
		String sql = " select w.value from WfInstancemstrProperty w  where w.wfInstancemstr.no='" + wf.getNo() + "' and w.name='"
				+ WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID + "'";
		return (String) this.em.createQuery(sql).getResultList().get(0);
	}

	public void updateCheckIn(WfInstancemstr wf, String newId) {
		String sql = " select w from WfInstancemstrProperty w  where w.wfInstancemstr.no='" + wf.getNo() + "' and w.name ='"
				+ WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID + "'";
		WfInstancemstrProperty wfz = (WfInstancemstrProperty) this.em.createQuery(sql).getResultList().get(0);
		wfz.setValue(newId);
	}

}
