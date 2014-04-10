package com.wcs.common.controller.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wcs.tih.model.NotificationExt;
import com.wcs.tih.model.Notificationmstr;

public class NotificationVo {

	private String title;// 消息标题

	private String content;// 消息内容

	private String refType;// 消息关联类型

	private String typeId;// 关联ID(任务申请单号，项目编号等)

	private Date sendDatetime;// 发送时间

	private String sendOption;// 发送类型

	private String sentBy;// 发送人

	private String status;// 发送状态 (已发送，未发送)

	private List<String> receiverList;// 接收人

	private String currentUserName;// 当前操作人

	private Notificationmstr notice = new Notificationmstr();// 消息实体

	private boolean detailsDisplay = false;// 是否显示消息详细

	private List<NoticeDetailVo> noticeDetails = new ArrayList<NoticeDetailVo>();// 消息的详细，主要在邮件中使用

	private NotificationExt noticeExt = null;// 邮件扩展表

	private SubjectVo subjectVo = new SubjectVo();// 消息title

	private String mailInd; // 发送邮件标识

	private String sysNoticeInd; // 发送消息表示

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public Date getSendDatetime() {
		return sendDatetime;
	}

	public void setSendDatetime(Date sendDatetime) {
		this.sendDatetime = sendDatetime;
	}

	public String getSendOption() {
		return sendOption;
	}

	public void setSendOption(String sendOption) {
		this.sendOption = sendOption;
	}

	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(List<String> receiverList) {
		this.receiverList = receiverList;
	}

	public String getCurrentUserName() {
		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public Notificationmstr getNotice() {
		return notice;
	}

	public void setNotice(Notificationmstr notice) {
		this.notice = notice;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public NotificationExt getNoticeExt() {
		return noticeExt;
	}

	public void setNoticeExt(NotificationExt noticeExt) {
		this.noticeExt = noticeExt;
	}

	public SubjectVo getSubjectVo() {
		return subjectVo;
	}

	public void setSubjectVo(SubjectVo subjectVo) {
		this.subjectVo = subjectVo;
	}

	public List<NoticeDetailVo> getNoticeDetails() {
		return noticeDetails;
	}

	public void setNoticeDetails(List<NoticeDetailVo> noticeDetails) {
		this.noticeDetails = noticeDetails;
	}

	public boolean isDetailsDisplay() {
		return detailsDisplay;
	}

	public void setDetailsDisplay(boolean detailsDisplay) {
		this.detailsDisplay = detailsDisplay;
	}

	public String getMailInd() {
		return mailInd;
	}

	public void setMailInd(String mailInd) {
		this.mailInd = mailInd;
	}

	public String getSysNoticeInd() {
		return sysNoticeInd;
	}

	public void setSysNoticeInd(String sysNoticeInd) {
		this.sysNoticeInd = sysNoticeInd;
	}
	
}
