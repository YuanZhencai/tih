package com.wcs.tih.filenet.helper.pe;


import filenet.vw.api.VWDataField;
import filenet.vw.api.VWException;
import filenet.vw.api.VWLogElement;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWRosterElement;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;
import com.wcs.tih.filenet.helper.vo.PropertyVo;
import com.wcs.tih.filenet.helper.vo.WorkItem;

public class WorkItemHelper {
	public static void append(Object obj, ToStringBuilder b) {
		if ((!(obj instanceof VWQueueElement)) && (!(obj instanceof VWStepElement)) && (!(obj instanceof VWWorkObject)))
			throw new IllegalArgumentException(obj + " is not one of queue element, step element or work object");
		b.append("=== work element common properties ===");
		b.append("WorkObjectNumber", invoke(obj, "getWorkObjectNumber"));
		b.append("WorkObjectName", invoke(obj, "getWorkObjectName"));
		b.append("WorkClassName", invoke(obj, "getWorkClassName"));
		b.append("Tag", invoke(obj, "getTag"));
		b.append("WorkflowName", invoke(obj, "getWorkflowName"));
		b.append("CurrentOperationName", invoke(obj, "getOperationName"));
		ToStringUtil.appendSectionEndLine(b);
	}

	public static void appendFields(Object o, String[] fieldNames, ToStringBuilder b) {
		if ((!(o instanceof VWLogElement)) && (!(o instanceof VWQueueElement)))
			throw new IllegalArgumentException(o + " is neither queue element nor log element");
		try {
			Class<? extends Object> cls = o.getClass();
			Method m = cls.getMethod("getFieldValue", new Class[] { String.class });
			for (int i = 0; i < fieldNames.length; ++i)
				if (fieldNames[i] != null) {
					Object value = m.invoke(o, new Object[] { fieldNames[i] });
					b.append(fieldNames[i], value);
				}
		} catch (Exception e) {
			throw new P8BpmException(e);
		}
	}

	private static String invoke(Object o, String methodName) {
		try {
			Class<? extends Object> cls = o.getClass();
			return (String) (cls.getMethod(methodName, null).invoke(o, null));
		} catch (Exception e) {
			throw new P8BpmException(e);
		}
	}

	public static List<WorkItem> getWorkItemsFromVWRosterElements(List<VWRosterElement> vwRosterElements, String[] fieldNames) throws VWException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		for (VWRosterElement vwRosterElement : vwRosterElements) {
			workItems.add(getWorkItemFromVWRosterElement(vwRosterElement, fieldNames));
		}
		return workItems;
	}

	public static List<WorkItem> getWorkItemsFromVWQueueElements(List<VWQueueElement> vwQueueElements, String[] fieldNames) throws VWException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		for (VWQueueElement vwQueueElement : vwQueueElements) {
			workItems.add(getWorkItemFromVWQueueElement(vwQueueElement, fieldNames));
		}
		return workItems;
	}

	public static List<WorkItem> getWorkItemsFromVWStepElements(List<VWStepElement> vwStepElements, String[] fieldNames) throws VWException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		for (VWStepElement vwStepElement : vwStepElements) {
			workItems.add(getWorkItemFromVWStepElement(vwStepElement, fieldNames));
		}
		return workItems;
	}

	public static List<WorkItem> getWorkItemsFromVWWorkObjects(List<VWWorkObject> vwWorkObjects, String[] fieldNames) throws VWException {
		List<WorkItem> workItems = new ArrayList<WorkItem>();
		for (VWWorkObject vwWorkObject : vwWorkObjects) {
			workItems.add(getWorkItemFromVWWorkObject(vwWorkObject, fieldNames));
		}
		return workItems;
	}

	public static WorkItem getWorkItemFromVWRosterElement(VWRosterElement vwRosterElement, String[] fieldNames) throws VWException {
		WorkItem workObj = new WorkItem();
		workObj.setWorkObjectNumber(vwRosterElement.getWorkObjectNumber());
		if (vwRosterElement.hasFieldName("F_EnqueueTime")) {
			workObj.setEnqueueTime((Date) vwRosterElement.getFieldValue("F_EnqueueTime"));
		}
		if (vwRosterElement.hasFieldName("F_Locked")) {
			workObj.setLocked(((Integer) vwRosterElement.getFieldValue("F_Locked")).intValue());
		}
		workObj.setSubject(vwRosterElement.getSubject());
		if (vwRosterElement.hasFieldName("F_Class")) {
			workObj.setWorkflowName((String) vwRosterElement.getFieldValue("F_Class"));
		}
		workObj.setWorkflowNumber(vwRosterElement.getWorkflowNumber());
		if (vwRosterElement.hasFieldName("F_StartTime")) {
			workObj.setLaunchDate((Date) vwRosterElement.getFieldValue("F_StartTime"));
		}
		workObj.setStepName(vwRosterElement.getStepName());
		workObj.setWorkflowNumber(vwRosterElement.getWorkflowNumber());

		if (fieldNames != null) {
			for (String name : fieldNames) {
				workObj.setFieldValue(name, vwRosterElement.getFieldValue(name));
			}
		}
		List<PropertyVo> propertyVos = new ArrayList<PropertyVo>();
		PropertyVo propertyVo = null;
		for (VWDataField field : vwRosterElement.getDataFields()) {
			propertyVo = new PropertyVo();
			propertyVo.setName(field.getName());
			propertyVo.setValue(field.getStringValue());
			propertyVos.add(propertyVo);
		}
		workObj.setPropertyVos(propertyVos);
		if (vwRosterElement.hasFieldName("F_Originator")) {
			workObj.setOriginator(vwRosterElement.getFieldValue("F_Originator").toString());
		}
		return workObj;
	}

	public static WorkItem getWorkItemFromVWQueueElement(VWQueueElement vwQueueElement, String[] fieldNames) throws VWException {
		WorkItem workObj = new WorkItem();
		workObj.setWorkObjectNumber(vwQueueElement.getWorkObjectNumber());
		workObj.setEnqueueTime(vwQueueElement.hasFieldName("F_EnqueueTime") ? (Date) vwQueueElement.getFieldValue("F_EnqueueTime") : new Date());
		workObj.setLocked(vwQueueElement.hasFieldName("F_Locked") ? ((Integer) vwQueueElement.getFieldValue("F_Locked")).intValue() : null);
		workObj.setLockUsername(vwQueueElement.getLockedUser());
		workObj.setQueueName(vwQueueElement.getQueueName());
		workObj.setSubject(vwQueueElement.getSubject());
		workObj.setWorkflowName(vwQueueElement.getWorkflowName());
		workObj.setWorkflowNumber(vwQueueElement.getWorkFlowNumber());
		workObj.setLaunchDate(vwQueueElement.hasFieldName("F_StartTime") ? (Date) vwQueueElement.getFieldValue("F_StartTime") : new Date());
		workObj.setStepName(vwQueueElement.getStepName());
		// workObj.setWorkflowNumber(vwQueueElement.hasFieldName("F_WorkFlowNumber") ? (String) vwQueueElement.getFieldValue("F_WorkFlowNumber"):null);

		if (fieldNames != null) {
			for (String name : fieldNames)
				workObj.setFieldValue(name, vwQueueElement.getFieldValue(name));
		}
		if (vwQueueElement.hasFieldName("F_Originator")) {
			workObj.setOriginator(vwQueueElement.getFieldValue("F_Originator").toString());
		}
		return workObj;
	}

	public static WorkItem getWorkItemFromVWStepElement(VWStepElement vwStepElement, String[] fieldNames) throws VWException {
		WorkItem workObj = new WorkItem();
		workObj.setWorkObjectNumber(vwStepElement.getWorkObjectNumber());

		workObj.setQueueName(vwStepElement.getCurrentQueueName());
		workObj.setSubject(vwStepElement.getSubject());
		workObj.setWorkflowName(vwStepElement.getWorkflowName());
		workObj.setWorkflowNumber(vwStepElement.getWorkflowNumber());
		workObj.setComment(vwStepElement.getComment());
		workObj.setDateReceived(vwStepElement.getDateReceived());
		workObj.setLaunchDate(vwStepElement.getLaunchDate());
		workObj.setOriginator(vwStepElement.getOriginator());
		workObj.setSelectedResponse(vwStepElement.getSelectedResponse());
		workObj.setStepResponses(vwStepElement.getStepResponses());
		workObj.setStepName(vwStepElement.getStepName());
		workObj.setParticipantName(vwStepElement.getParticipantName());

		if (fieldNames != null) {
			for (String name : fieldNames)
				workObj.setFieldValue(name, vwStepElement.getParameterValue(name));
		}

		List<PropertyVo> propertyVos = new ArrayList<PropertyVo>();
		PropertyVo propertyVo = null;
		for (String parameterName : vwStepElement.getParameterNames()) {
			propertyVo = new PropertyVo();
			propertyVo.setName(parameterName);
			propertyVo.setValue(vwStepElement.getParameterValue(parameterName).toString());
			propertyVos.add(propertyVo);
		}

		return workObj;
	}

	public static WorkItem getWorkItemFromVWWorkObject(VWWorkObject vwWorkObject, String[] fieldNames) throws VWException {
		WorkItem workObj = new WorkItem();
		workObj.setWorkObjectNumber(vwWorkObject.getWorkObjectNumber());
		workObj.setEnqueueTime((Date) vwWorkObject.getFieldValue("F_EnqueueTime"));
		workObj.setLocked(((Integer) vwWorkObject.getFieldValue("F_Locked")).intValue());
		workObj.setLockUsername(vwWorkObject.getLockedUser());
		workObj.setQueueName(vwWorkObject.getCurrentQueueName());
		workObj.setSubject(vwWorkObject.getSubject());
		workObj.setWorkflowName(vwWorkObject.getWorkflowName());
		workObj.setWorkflowNumber(vwWorkObject.getWorkflowNumber());
		workObj.setComment(vwWorkObject.getComment());
		workObj.setDateReceived(vwWorkObject.getDateReceived());
		workObj.setLaunchDate(vwWorkObject.getLaunchDate());
		workObj.setOriginator(vwWorkObject.getOriginator());
		workObj.setSelectedResponse(vwWorkObject.getSelectedResponse());
		workObj.setStepResponses(vwWorkObject.getStepResponses());
		workObj.setStepName(vwWorkObject.getStepName());

		if (fieldNames != null) {
			for (String name : fieldNames)
				workObj.setFieldValue(name, vwWorkObject.getFieldValue(name));
		}
		if (vwWorkObject.hasFieldName("F_Originator")) {
			workObj.setOriginator(vwWorkObject.getFieldValue("F_Originator").toString());
		}
		return workObj;
	}
}