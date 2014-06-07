/** * QuestionVo.java 
 * Created on 2014年4月8日 上午10:57:09 
 */

package com.wcs.tih.interaction.controller.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.model.WfInstancemstr;


public class QuestionVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean noDraft;
	private WfInstancemstr wfIns;
	private Map<String, Object> createDlgParamMap;
	private StringBuffer documentIds;
	private StringBuffer documentNames;
	private List<String> createDlgNameWithEmail;
	private String importance;
	private String urgency;
	private WfRemindVo wfRemindVo;
	private String recipients;
	private String workflowNodalPoint; 
	private String sendToName; 
	private String sendToDirection;
	private String workflowNum;
	private String txtOpionion;
	private String taskStatus;
	private Map<String,Object> processDlgParamMap;
	private List<String> recipientsSelectList;
	private List<ApplyQuestionVO> fileDetails;
	private Map<String, String> recipientsMap;
	private String supervisor;
	private String showSupervisor;
	
	public boolean isNoDraft() {
		return noDraft;
	}
	public void setNoDraft(boolean noDraft) {
		this.noDraft = noDraft;
	}
	public WfInstancemstr getWfIns() {
		return wfIns;
	}
	public void setWfIns(WfInstancemstr wfIns) {
		this.wfIns = wfIns;
	}
	public Map<String, Object> getCreateDlgParamMap() {
		return createDlgParamMap;
	}
	public void setCreateDlgParamMap(Map<String, Object> createDlgParamMap) {
		this.createDlgParamMap = createDlgParamMap;
	}
	public StringBuffer getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(StringBuffer documentIds) {
		this.documentIds = documentIds;
	}
	public StringBuffer getDocumentNames() {
		return documentNames;
	}
	public void setDocumentNames(StringBuffer documentNames) {
		this.documentNames = documentNames;
	}
	public List<String> getCreateDlgNameWithEmail() {
		return createDlgNameWithEmail;
	}
	public void setCreateDlgNameWithEmail(List<String> createDlgNameWithEmail) {
		this.createDlgNameWithEmail = createDlgNameWithEmail;
	}
	public String getImportance() {
		return importance;
	}
	public void setImportance(String importance) {
		this.importance = importance;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}
	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}
	public String getRecipients() {
		return recipients;
	}
	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}
	public String getWorkflowNodalPoint() {
		return workflowNodalPoint;
	}
	public void setWorkflowNodalPoint(String workflowNodalPoint) {
		this.workflowNodalPoint = workflowNodalPoint;
	}
	public String getSendToName() {
		return sendToName;
	}
	public void setSendToName(String sendToName) {
		this.sendToName = sendToName;
	}
	public String getSendToDirection() {
		return sendToDirection;
	}
	public void setSendToDirection(String sendToDirection) {
		this.sendToDirection = sendToDirection;
	}
	public String getWorkflowNum() {
		return workflowNum;
	}
	public void setWorkflowNum(String workflowNum) {
		this.workflowNum = workflowNum;
	}
	public String getTxtOpionion() {
		return txtOpionion;
	}
	public void setTxtOpionion(String txtOpionion) {
		this.txtOpionion = txtOpionion;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Map<String, Object> getProcessDlgParamMap() {
		return processDlgParamMap;
	}
	public void setProcessDlgParamMap(Map<String, Object> processDlgParamMap) {
		this.processDlgParamMap = processDlgParamMap;
	}
	public List<String> getRecipientsSelectList() {
		return recipientsSelectList;
	}
	public void setRecipientsSelectList(List<String> recipientsSelectList) {
		this.recipientsSelectList = recipientsSelectList;
	}
	public List<ApplyQuestionVO> getFileDetails() {
		return fileDetails;
	}
	public void setFileDetails(List<ApplyQuestionVO> fileDetails) {
		this.fileDetails = fileDetails;
	}
	public Map<String, String> getRecipientsMap() {
		return recipientsMap;
	}
	public void setRecipientsMap(Map<String, String> recipientsMap) {
		this.recipientsMap = recipientsMap;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getShowSupervisor() {
		return showSupervisor;
	}
	public void setShowSupervisor(String showSupervisor) {
		this.showSupervisor = showSupervisor;
	}
	
}
