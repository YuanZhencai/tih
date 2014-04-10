package com.wcs.tih.homepage.contronller.vo;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.NotificationReceiver;

public class NoticeVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -2393289910220942299L;
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private long receiverId;// 接收人I
    private String title;
    private String content;
    private Date sendDatetime;
    private NotificationReceiver receiver = new NotificationReceiver();
    
    public long getReceiverId() {
        if(receiver != null){
            receiverId = receiver.getId();
        }
    	return receiverId;
    }
    
    public void setReceiverId(long receiverId) {
    	this.receiverId = receiverId;
    }

    public String getTitle() {
    	try {
    	    title = receiver.getNotificationSender().getNotificationmstr().getTitle();
		} catch (Exception e) {
		    logger.debug(e.getMessage());
		}
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
    	try {
    	    content = receiver.getNotificationSender().getNotificationmstr().getContent();
		} catch (Exception e) {
		    logger.debug(e.getMessage());
		}
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendDatetime() {
    	try {
    	    sendDatetime = (Date)receiver.getNotificationSender().getSendDatetime();
		} catch (Exception e) {
		    logger.debug(e.getMessage());
		}
        return sendDatetime;
    }

    public void setSendDatetime(Date sendDatetime) {
        this.sendDatetime = sendDatetime;
    }

	public NotificationReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(NotificationReceiver receiver) {
		this.receiver = receiver;
	}

	
}
