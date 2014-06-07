package com.wcs.tih.transaction.controller.vo;

public class TaskScheduleVo {
    private String userName;
    private String taskNumber;
    private String requestFormtype;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getRequestFormtype() {
        return requestFormtype;
    }

    public void setRequestFormtype(String requestFormtype) {
        this.requestFormtype = requestFormtype;
    }
}
