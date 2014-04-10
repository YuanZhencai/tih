package com.wcs.tih.filenet.helper.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkItem extends EntityVo {
    private Map<String, Object> properties = new HashMap<String, Object>();
    private String workObjectNumber;
    private String subject;
    private String workflowName;
    private String stepName;
    private String lockUsername;
    private int locked;
    private Date enqueueTime;
    private String queueName;
    private Object target;
    private String comment;
    private Date dateReceived;
    private Date launchDate;
    private String originator;
    private String selectedResponse;
    private String[] stepResponses;
    private String workflowNumber;
    private List<PropertyVo> propertyVos = new ArrayList<PropertyVo>();
    private String participantName;
    

    public Object getTarget() {
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getWorkObjectNumber() {
        return this.workObjectNumber;
    }

    public void setWorkObjectNumber(String workObjectNumber) {
        this.workObjectNumber = workObjectNumber;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWorkflowName() {
        return this.workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getStepName() {
        return this.stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public int getLocked() {
        return this.locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public Date getEnqueueTime() {
        return ((Date) this.enqueueTime.clone());
    }

    public void setEnqueueTime(Date enqueueTime) {
        this.enqueueTime = new Date(enqueueTime.getTime());
    }

    public String getQueueName() {
        return this.queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateReceived() {
        return this.dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Date getLaunchDate() {
        return this.launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getOriginator() {
        return this.originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getSelectedResponse() {
        return this.selectedResponse;
    }

    public void setSelectedResponse(String selectedResponse) {
        this.selectedResponse = selectedResponse;
    }

    public String[] getStepResponses() {
        return this.stepResponses;
    }

    public void setStepResponses(String[] stepResponses) {
        this.stepResponses = stepResponses;
    }

    public String getLockUsername() {
        return this.lockUsername;
    }

    public void setLockUsername(String lockUsername) {
        this.lockUsername = lockUsername;
    }

    public String getWorkflowNumber() {
        return this.workflowNumber;
    }

    public void setWorkflowNumber(String workflowNumber) {
        this.workflowNumber = workflowNumber;
    }

    public Object getFieldValue(String name) {
        return this.properties.get(name);
    }

    public void setFieldValue(String name, Object value) {
        this.properties.put(name, value);
    }

    public List<PropertyVo> getPropertyVos() {
        return propertyVos;
    }

    public void setPropertyVos(List<PropertyVo> propertyVos) {
        this.propertyVos = propertyVos;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }
}