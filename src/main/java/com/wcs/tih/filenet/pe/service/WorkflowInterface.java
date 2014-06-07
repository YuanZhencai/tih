package com.wcs.tih.filenet.pe.service;

import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfStepmstr;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWXMLData;

/**
 * <p>Project: tih</p>
 * <p>Description: WorkflowInterface</p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
public abstract class WorkflowInterface {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static String dnStr = ResourceBundle.getBundle("filenet").getString("tds.users.dn");

    public static String workqueueGroup = ResourceBundle.getBundle("roles").getString("fnadmins");

    abstract public void saveWfStepmstrProperty(WfStepmstr wfstepmstr, Map<String, String> paramMap);

    abstract public void saveWfInstanceProperty(WfInstancemstr wf, Map<String, String> paramMap);

    abstract public void saveWfStr(WfInstancemstr wfstr);

    abstract public void saveWfStep(WfStepmstr wfstep);

    private String workflowName;
    private WfInstancemstr saveWfstr;
    private WfStepmstr saveStep;
    private Map<String, String> stepMap;
    private Map<String, String> intanceMap;
    private String workflowNumber;
    private Long wfId;

    @EJB
    private RosterService rosterService;

    /**
     * 
     * <p>
     * Description:保存数据
     * </p>
     */
    private void saveData() {
        this.saveWfStr(saveWfstr);
        this.wfId = saveWfstr.getId();
        this.saveWfStep(saveStep);
        this.saveWfInstanceProperty(saveWfstr, intanceMap);
        this.saveWfStepmstrProperty(saveStep, stepMap);
    }

    /**
     * 
     * <p>
     * Description:保存数据
     * </p>
     */
    private void dispatchData() {
        this.saveWfStep(saveStep);
        this.saveWfStepmstrProperty(saveStep, stepMap);
    }

    /**
     * <p>
     * Description:创建工作流
     * </p>
     * 
     * @param name
     * @param password
     * @param paramMap
     * @return
     * @throws VWException
     */
    public VWStepElement createWorkFlow(String name, String password, Map<String, Object> dataMap) throws VWException {
        VWSession vwSession = PEConnection.getInstance().getSession(name, password);
        VWStepElement stepElement = vwSession.createWorkflow(workflowName);
        setDataMap(stepElement, dataMap);
        stepElement.doDispatch();
        this.workflowNumber = stepElement.getWorkflowNumber();
        this.saveWfstr.setNo(stepElement.getWorkflowNumber());
        this.saveStep.setCode(String.valueOf(stepElement.getWorkOrderId()));
        this.saveStep.setName(stepElement.getStepName());
        saveData();
        vwSession.logoff();
        return stepElement;
    }

    /**
     * <p>
     * Description:执行工作流
     * </p>
     * 
     * @param name
     * @param password
     * @param paramMap
     * @return
     * @throws VWException
     */
    public void doDispatch(String name, String password, String workflowNumber, Map<String, Object> dataMap,
            String requestform) throws Exception, VWException {
        VWSession vwSession = PEConnection.getInstance().getSession(name, password);
        VWStepElement stepElement = rosterService.getStepElementByWorkflowNumber(vwSession, workflowNumber);
        stepElement.doLock(true);
        setDataMap(stepElement, dataMap);
        stepElement.doSave(false);
        stepElement.doDispatch();
        this.workflowNumber=stepElement.getWorkflowNumber();
        this.saveStep.setCode(String.valueOf(stepElement.getWorkOrderId()));
        this.saveStep.setName(stepElement.getStepName());
        dispatchData();
        vwSession.logoff();
    }

    /**
     * 
     * <p>
     * Description:根据流程号获取当前节点名称
     * </p>
     * 
     * @param name
     * @param password
     * @param worflowNumber
     * @return
     * @throws VWException
     */
    public String getStepNameByWobNum(String name, String password, String workflowNumber) throws VWException {
        // fix Bug #14694
    	VWSession vwSession = PEConnection.getInstance().getAdminSession();
        VWStepElement stepElement  = rosterService.getStepElementByWorkObjectNumber(vwSession, workflowNumber);
        String stepName = stepElement.getStepName();
        vwSession.logoff();
        return stepName;
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

    public String getWorkflowNumber() {
        return workflowNumber;
    }

    public void setSaveWfstr(WfInstancemstr saveWfstr) {
        this.saveWfstr = saveWfstr;
    }

    public void setSaveStep(WfStepmstr saveStep) {
        this.saveStep = saveStep;
    }

    public void setStepMap(Map<String, String> stepMap) {
        this.stepMap = stepMap;
    }

    public void setIntanceMap(Map<String, String> intanceMap) {
        this.intanceMap = intanceMap;

    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public Long getWfId() {
        return wfId;
    }

    public void setWfId(Long wfId) {
        this.wfId = wfId;
    }

    // public final
}
