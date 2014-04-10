package com.wcs.tih.filenet.helper.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HistoryVo extends Vo
{
  private Map<String, Object> properties = new HashMap<String, Object>();
  private String F_WorkFlowNumber;
  private String subject;
  private String workflowName;
  private String queueName;
  private String stepName;
  private Date F_TimeStamp;
  private Integer workClassID;
  private String userName;

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getF_WorkFlowNumber() {
    return this.F_WorkFlowNumber;
  }

  public void setF_WorkFlowNumber(String workFlowNumber) {
    this.F_WorkFlowNumber = workFlowNumber;
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

  public Date getF_TimeStamp() {
    return this.F_TimeStamp;
  }

  public void setF_TimeStamp(Date timeStamp) {
    this.F_TimeStamp = timeStamp;
  }

  public Integer getWorkClassID() {
    return this.workClassID;
  }

  public void setWorkClassID(Integer workClassID) {
    this.workClassID = workClassID;
  }

  public String getQueueName() {
    return this.queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public Object getFieldValue(String name) {
    return this.properties.get(name);
  }

  public void setFieldValue(String name, Object value) {
    this.properties.put(name, value);
  }
}