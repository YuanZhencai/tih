package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;

public class TaskTreeNodeVo implements Serializable {

    private static final long serialVersionUID = -252155270879792479L;

    private String taskNodeName;

    private String nodeType;

    private int taskCount;// 各个任务个数

    private String requestFormType;// 申请单类型

    public TaskTreeNodeVo(String taskNodeName, String nodeType, int taskCount, String requestFormType) {
        this.taskNodeName = taskNodeName;
        this.nodeType = nodeType;
        this.taskCount = taskCount;
        this.requestFormType = requestFormType;
    }

    public String getTaskNodeName() {
        return taskNodeName;
    }

    public void setTaskNodeName(String taskNodeName) {
        this.taskNodeName = taskNodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public String getRequestFormType() {
        return requestFormType;
    }

    public void setRequestFormType(String requestFormType) {
        this.requestFormType = requestFormType;
    }

}