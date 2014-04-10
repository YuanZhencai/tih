package com.wcs.tih.filenet.helper.pe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;





import com.wcs.tih.filenet.helper.ce.util.Assert;
import com.wcs.tih.filenet.helper.vo.HistoryVo;
import com.wcs.tih.filenet.helper.vo.WorkItem;

import filenet.vw.api.VWException;
import filenet.vw.api.VWLogElement;
import filenet.vw.api.VWProcess;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;
import filenet.vw.api.VWWorkflowHistory;
import filenet.vw.api.VWXMLData;

public class BPMWorkflowServices {
	private VWSession vwSession;
	private static Logger logger = Logger.getLogger(BPMWorkflowServices.class);

	public BPMWorkflowServices(VWSession vwSession) {
		this.vwSession = vwSession;
	}

	public void destroyBPMWorkflowServices() {
		SessionHelper.logoff(this.vwSession);
	}

	public boolean startProcess(String workflowName, Map<String, Object> dataMap) throws BPMException {
		try {
			VWStepElement stepElement = this.vwSession.createWorkflow(workflowName);
			setDataMap(stepElement, dataMap);
			stepElement.doDispatch();
			logger.debug("The created Workflow number is " + stepElement.getWorkflowNumber());
		} catch (VWException e) {
			logger.error("启动流程失败", e);
			throw new BPMException("启动流程失败", e);
		}
		return true;
	}

	public List<WorkItem> queryWorkItemsInRoster(String condition, String[] fieldNames) throws BPMException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		try {
			workItems = WorkItemHelper.getWorkItemsFromVWRosterElements(RosterHelper.getRosterElements(this.vwSession, condition), fieldNames);
		} catch (VWException e) {
			throw new BPMException("在Roster中获取任务列表失败！", e);
		}
		return workItems;
	}

	public List<WorkItem> queryWorkItems(String queueName, String condition, String[] fieldNames) throws BPMException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		try {
			workItems = WorkItemHelper.getWorkItemsFromVWQueueElements(QueueHelper.getQueueElementsByfilter(this.vwSession, queueName, condition),
					fieldNames);
		} catch (VWException e) {
			throw new BPMException("在Queue" + queueName + "中，使用条件" + condition + "获取任务列表失败！", e);
		}
		return workItems;
	}

	public List<WorkItem> queryWorkItemsInMyInbox(String condition, String[] fieldNames) throws BPMException {
		addUserIdToCondition(condition);
		List<String> queueNames = new ArrayList<String>();
		queueNames.add("Inbox");
		return queryWorkObjects(queueNames, condition, fieldNames);
	}

	private void addUserIdToCondition(String condition) {
		try {
			int userId = new Long(this.vwSession.getCurrentUserSecId()).intValue();
			logger.debug("userId:" + userId);
			if ((condition != null) && (!(condition.equals("")))) {
				condition = condition + " AND F_BoundUser =" + userId;

				return;
			}

			condition = condition + "F_BoundUser =" + userId;
		} catch (VWException e) {
			logger.error("获取用户UserID时失败！", e);
		}
	}

	private List<WorkItem> queryWorkObjects(List<String> queueNames, String condition, String[] fieldNames) throws BPMException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		try {
			for (String queueName : queueNames) {
				if (logger.isDebugEnabled()) {
					logger.debug("Queuename = " + queueName);
				}
				List<VWQueueElement> onequelist = QueueHelper.getQueueElementsByfilter(this.vwSession, queueName, condition, null);
				workItems.addAll(WorkItemHelper.getWorkItemsFromVWQueueElements(onequelist, fieldNames));
			}
		} catch (VWException e) {
			throw new BPMException("获取任务列表失败！", e);
		}
		return workItems;
	}

	public WorkItem getWorkItem(String workObjectNumber, String[] fieldNames) throws BPMException {
		try {
			return WorkItemHelper.getWorkItemFromVWStepElement(RosterHelper.getStepElementByWorkObjectNumber(this.vwSession, workObjectNumber),
					fieldNames);
		} catch (VWException e) {
			logger.error("通过流程号获取流程对象失败！", e);
			throw new BPMException("通过流程号获取流程对象失败！", e);
		}
	}

	public VWWorkObject openWorkItem(String workObjectNumber) throws BPMException {
		try {
			return StepElementHelper.lockWorkObject(RosterHelper.getStepElementByWorkObjectNumber(this.vwSession, workObjectNumber));
		} catch (VWException e) {
			logger.error("通过流程号获取流程对象失败！", e);
			throw new BPMException("通过流程号获取流程对象失败！", e);
		}
	}

	public boolean saveWorkItem(String workObjectNumber, Map<String, Object> dataMap) throws BPMException {
		try {
			VWStepElement stepElement = RosterHelper.getStepElementByWorkObjectNumber(this.vwSession, workObjectNumber);
			stepElement.doLock(true);
			setDataMap(stepElement, dataMap);
			stepElement.doSave(true);
		} catch (VWException e) {
			logger.error("通过流程号获取流程对象失败！", e);
			throw new BPMException("通过流程号获取流程对象失败！", e);
		}
		return true;
	}

	public boolean assignWorkItem(String workObjectNumber, String assignUserID) throws BPMException {
		try {
			VWStepElement stepElement = RosterHelper.getStepElementByWorkObjectNumber(this.vwSession, workObjectNumber);
			stepElement.doLock(true);
			stepElement.doReassign(assignUserID, true, "Inbox");
		} catch (VWException e) {
			logger.error("将用户的工作项委托给其他用户失败！", e);
			throw new BPMException("将用户的工作项委托给其他用户失败！", e);
		}
		return true;
	}

	public boolean completeWorkItem(String workObjectNumber, Map<String, Object> dataMap) throws BPMException {
		try {
			VWStepElement stepElement = RosterHelper.getStepElementByWorkObjectNumber(this.vwSession, workObjectNumber);
			stepElement.doLock(true);
			setDataMap(stepElement, dataMap);
			stepElement.doSave(false);
			stepElement.doDispatch();
		} catch (VWException e) {
			logger.error("完成节点失败！", e);
			throw new BPMException("完成节点失败！", e);
		}
		return true;
	}

	public boolean updateWorkItems(String condition, Map<String, Object> dataMap) {
		List<VWStepElement> steps = RosterHelper.getStepElements(this.vwSession, condition);
		try {
			for (VWStepElement stepElement : steps) {
				stepElement.doLock(true);
				setDataMap(stepElement, dataMap);
				stepElement.doSave(true);
			}
		} catch (VWException e) {
			logger.error("修改流程信息时出错！", e);
			return false;
		}
		return true;
	}

	public boolean completeAllWorkItems(List<String> queueNames, String workflowNumber) throws BPMException {
		try {
			for (String queueName : queueNames) {
				VWWorkObject vwWorkObject = QueueHelper.getWorkObjectByWorkflowNumber(this.vwSession, queueName, workflowNumber);
				vwWorkObject.doLock(true);
				vwWorkObject.doDispatch();
			}
		} catch (VWException e) {
			logger.error("完成节点失败！", e);
			throw new BPMException("完成节点失败！", e);
		}
		return true;
	}

	public boolean terminalWorkItem(String workObjectNumber) throws BPMException {
		try {
			StepElementHelper.lockWorkObject(RosterHelper.getStepElementByWorkObjectNumber(this.vwSession, workObjectNumber)).doDelete(true, true);
		} catch (VWException e) {
			logger.error("终结流程工作项失败！", e);
			throw new BPMException("终结流程工作项失败！", e);
		}
		return true;
	}

	public List<HistoryVo> getWorkflowHistory(String workflowNumber, String[] fieldNames) throws VWException {
		List<HistoryVo> historyVos = new ArrayList<HistoryVo>();
		logger.debug("workflowNumber" + workflowNumber);
		List<VWLogElement> logElements = EventLogHelper.queryEventLogsByWobNum(this.vwSession, workflowNumber);
		for (VWLogElement logElement : logElements) {
			historyVos.add(LogElementHelper.getHistoryVo(logElement, fieldNames));
		}
		return historyVos;
	}

	public List<HistoryVo> getWorkflowHistoryByFilter(String filter, String[] fieldNames) throws VWException {
		List<HistoryVo> historyVos = new ArrayList<HistoryVo>();
		logger.debug("filter" + filter);
		List<VWLogElement> logElements = EventLogHelper.queryEventLogs(this.vwSession, filter);
		for (VWLogElement logElement : logElements) {
			historyVos.add(LogElementHelper.getHistoryVo(logElement, fieldNames));
		}
		return historyVos;
	}

	public VWWorkflowHistory getProcessInfo(String workObjectNumber) throws BPMException {
		VWWorkflowHistory history = null;
		VWProcess process = null;
		try {
			VWWorkObject vwWorkObject = StepElementHelper.fetchWorkObject(RosterHelper.getStepElementByWorkObjectNumber(this.vwSession,
					workObjectNumber));
			if (vwWorkObject != null)
				process = vwWorkObject.fetchProcess();
			logger.debug("\t\tWork Object Id: " + vwWorkObject.getWorkObjectNumber());

			if (process != null)
				history = process.fetchWorkflowHistory(-2);
		} catch (VWException e) {
			logger.error("终结流程工作项失败！", e);
			throw new BPMException("终结流程工作项失败！", e);
		}
		return history;
	}

	private void setDataMap(VWStepElement stepElement, Map<String, Object> dataMap) throws VWException {
		if (dataMap == null)
			return;
		Assert.notNull(stepElement, "流程实例为NULL.");
		for (String attrName : dataMap.keySet()) {
			Object attrValue = dataMap.get(attrName);
			if ((attrName != null) && (attrName.equals("selectedResponse"))) {
				if ((attrValue != null) && (!(attrValue.equals(""))))
					stepElement.setSelectedResponse(attrValue.toString());
				else {
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

	public void setDataMap(VWWorkObject vwWorkObject, Map<String, Object> dataMap) throws VWException {
		Assert.notNull(vwWorkObject, "流程实例为NULL.");
		for (String attrName : dataMap.keySet()) {
			Object attrValue = dataMap.get(attrName);
			if (attrValue != null)
				vwWorkObject.setFieldValue(attrName, attrValue, true);
		}
	}

	public int convertUserNameToId(String userName) throws BPMException {
		int userId = -1;
		try {
			userId = this.vwSession.convertUserNameToId(userName);
		} catch (VWException e) {
			throw new BPMException("获取用户ID失败！", e);
		}
		return userId;
	}

}