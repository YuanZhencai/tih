package com.wcs.tih.filenet.pe.service;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.tih.document.service.DocumentService;
import com.wcs.tih.filenet.model.FnDocument;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;

import filenet.vw.api.VWException;
import filenet.vw.api.VWFetchType;
import filenet.vw.api.VWQueue;
import filenet.vw.api.VWQueueQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;
import filenet.vw.api.VWXMLData;

/**
 * Project: tih
 * Description: 流程保存数据
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 */
@Stateless
public class WorkflowService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB 
    private DocumentService documentservice;
    @EJB 
    private RosterService rosterService;
    
    private static final ResourceBundle FILENET_BUNDLE = ResourceBundle.getBundle("filenet");
    private static String adminName = FILENET_BUNDLE.getString("admin.id");
    private static String adminPassword = FILENET_BUNDLE.getString("admin.password");
    private static String notPassFloder = FILENET_BUNDLE.getString("ce.folder.newAuditedNotPassed");
    private static String chekcInnotPassFloder = FILENET_BUNDLE.getString("ce.folder.checkinNotPassed");
    public VWStepElement createWorkFlow(String workFlowName, String name, String password, Map<String, Object> dataMap) throws VWException {
        VWSession vwSession = PEConnection.getInstance().getSession(name, password);
        VWStepElement stepElement = vwSession.createWorkflow(workFlowName);
        setDataMap(stepElement, dataMap);
        stepElement.doDispatch();
        vwSession.logoff();
        return stepElement;
    }

    public String doTerminate(WfInstancemstr wf) throws VWException {
        
        if(wf==null){
        	throw new NullPointerException();
        }
        if(wf.getType().equals(DictConsts.TIH_TAX_REQUESTFORM_1)){
            List<WfInstancemstrProperty> list = wf.getWfInstancemstrProperties();
            String fileId="";
            for(int i=list.size()-1;i>=0;i--){
                if(list.get(i).getName().equals(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID)){
                    fileId=list.get(i).getValue();
                    break;
                }
            }
            FnDocument fn = new FnDocument();
            fn.setId(fileId);
            fn.setAuditStatus(DictConsts.TIH_DOC_STATUS_2);
            try {
				this.documentservice.editDocProperty(fn);
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
			}
            try {
                this.documentservice.moveDoc(fn, notPassFloder);
            } catch (Exception e) {
                logger.error("移动文件夹失败", e);
            }
            
        }else if(wf.getType().equals(DictConsts.TIH_TAX_REQUESTFORM_2)){
            String fileId ="";
            String checkInId="";
            List<WfInstancemstrProperty> list = null;
    		if(wf.getWfInstancemstrProperties() != null){
    			list = wf.getWfInstancemstrProperties();
    		}else{
    			list = this.getWorkflowProperty(wf.getId());
    		}
            for(int i=list.size()-1;i>=0;i--){
                if(list.get(i).getName().equals(WorkflowConsts.TIH_WORKFLOW_DOCUMENT_ID)){
                    fileId=list.get(i).getValue();
                }
                if(list.get(i).getName().equals(WorkflowConsts.TIH_WORKFLOW_DCOUMENT_CHECKIN_ID)){
                    checkInId=list.get(i).getValue();
                }
            }
            FnDocument fn =new FnDocument();
            fn.setId(checkInId);
            fn.setAuditStatus(DictConsts.TIH_DOC_STATUS_7);
            try {
                this.documentservice.editDocProperty(fn);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            try {
                this.documentservice.moveDoc(fn, chekcInnotPassFloder);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            FnDocument fnz =new FnDocument();
            fnz.setId(fileId);
            fnz.setAuditStatus(DictConsts.TIH_DOC_STATUS_4);
            try {
                this.documentservice.editDocProperty(fnz);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            boolean flag = false;
            //检入文档终止流程，移除权限
            List<WfStepmstr> steps = null;
        	if(wf.getWfStepmstrs() != null){
        		steps = wf.getWfStepmstrs();
        	}else{
        		steps = this.getWorkflowSteps(wf.getId());
        	}
            String userName = steps.get(steps.size()-1).getChargedBy();  
            //得到流程当前节点的人
            List<String> permissionUserName = null;
            //源文档文档的所有用户权限
            try {
				permissionUserName = this.documentservice.getPermissionByAllUserName(fileId);
			} catch (Exception e1) {
			    logger.error(e1.getMessage(), e1);
			}
            if(userName != null){
            	for(String user : permissionUserName){
            		if(userName.equals(user)){
            			flag = true;
            			break;
            		}
            	}
            }
            if(flag){
            	try {
            		this.documentservice.bindAndRemoveRole(fileId, userName, wf.getCreatedBy());
            		flag = false;
            	} catch (Exception e) {
            		logger.error(e.getMessage(), e);
            	}
            }
        }
        vwSession = PEConnection.getInstance().getSession(adminName, adminPassword);
        String[] queueNames = vwSession.fetchQueueNames(VWSession.QUEUE_PROCESS | VWSession.QUEUE_USER_CENTRIC);
        VWWorkObject stepElement = null;
        for (int i = 0; i < queueNames.length; i++) {
            String queueName = queueNames[i];
            VWQueue vwQueue = vwSession.getQueue(queueName);
            int queryFlags = VWQueue.QUERY_READ_LOCKED;
            VWQueueQuery qQuery = vwQueue.createQuery(null, null, null, queryFlags, null, null, VWFetchType.FETCH_TYPE_WORKOBJECT);
            while (qQuery.hasNext()) {
                stepElement = (VWWorkObject) qQuery.next();
                if (wf.getNo().equals(stepElement.getWorkflowNumber())) {
                    stepElement.doLock(false);
                    stepElement.doTerminate();
                }
            }
        }
        vwSession.logoff();
        return null;
    }

    private VWSession vwSession;

    private VWStepElement stepElement;

    public VWStepElement getStepElement() {
        return stepElement;
    }

    public void setStepElement(VWStepElement stepElement) {
        this.stepElement = stepElement;
    }

    public void execute(String name, String password, String workflowNumber, Map<String, Object> paramMap) throws Exception {
        vwSession =  PEConnection.getInstance().getSession(name, password);
        doDispatch(workflowNumber, paramMap);
        vwSession.logoff();
    }

    public void doDispatch(String workflowNumber, Map<String, Object> dataMap) throws VWException {
        VWStepElement stepElement= rosterService.getStepElementByWorkflowNumber(vwSession, workflowNumber);
        stepElement.doLock(true);
        setDataMap(stepElement, dataMap);
        stepElement.doSave(false);
        stepElement.doDispatch();
        this.setStepElement(stepElement);
    }

    private void setDataMap(VWStepElement stepElement, Map<String, Object> dataMap) throws VWException {
        if (dataMap == null) return;
        for (String attrName : dataMap.keySet()) {
            Object attrValue = dataMap.get(attrName);
            if ((attrName != null) && (attrName.equals("selectedResponse"))) {
                if ((attrValue != null) && (!(attrValue.equals("")))) {
                    stepElement.setSelectedResponse(attrValue.toString());
                } else {
                    logger.error("设置的selectedResponse为空！");
                }
            } else if ((attrName != null) && (attrName.equals("vo"))) {
                if ((attrValue != null) && (!(attrValue.equals("")))) {
                    String xmlContent = attrValue.toString();
                    VWXMLData xmlData = new VWXMLData();
                    xmlData.setXML(xmlContent);
                    stepElement.setParameterValue("vo", xmlData, true);
                } else {
                    logger.error("设置的xmlContent为空！");
                }
            } else if (attrValue != null) {
                stepElement.setParameterValue(attrName, attrValue, true);
            }
        }
    }

    public List<WfInstancemstrProperty> getWorkflowProperty(Long id){
        String sql = "select wfip from WfInstancemstrProperty wfip where wfip.wfInstancemstr.id = "+id+"";
        return em.createQuery(sql).getResultList();
    }

    public List<WfStepmstr> getWorkflowSteps(Long id){
        String sql = "select wfstep from WfStepmstr wfstep where wfstep.wfInstancemstr.id = "+id+" order by wfstep.id";
        return em.createQuery(sql).getResultList();
    }
}
