package com.wcs.common.controller.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wcs.common.model.P;

public class EmailVo {
    private String receiverName;
    private String receiverAdAccount;
    private String sendDate;
    private String emailSubject;
    private String htmlContent;
    private String appUrl;
    private List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
    private P sentTo = new P();

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAdAccount() {
        return receiverAdAccount;
    }

    public void setReceiverAdAccount(String receiverAdAccount) {
        this.receiverAdAccount = receiverAdAccount;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public P getSentTo() {
        return sentTo;
    }

    public void setSentTo(P sentTo) {
        this.sentTo = sentTo;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public List<NotificationVo> getNoticeVos() {
        return noticeVos;
    }

    public void setNoticeVos(List<NotificationVo> noticeVos) {
        this.noticeVos = noticeVos;
    }

}
