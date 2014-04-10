/** * EmailInfo.java 
* Created on 2014年3月4日 下午4:51:32 
*/

package com.wcs.tih.transaction.controller.vo;

import java.util.Date;

/** 
 * <p>Project: tih</p> 
 * <p>Title: EmailInfo.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */

public class EmailInfo {

	private String typeId;
	private String status;
	private Date createdDatetime;
	private String sentBy;
	private String receivedBy;
	private Date sendDatetime;
	private String type;
	private String content;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDatetime() {
		return createdDatetime;
	}
	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public String getSentBy() {
		return sentBy;
	}
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}
	public String getReceivedBy() {
		return receivedBy;
	}
	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}
	public Date getSendDatetime() {
		return sendDatetime;
	}
	public void setSendDatetime(Date sendDatetime) {
		this.sendDatetime = sendDatetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
