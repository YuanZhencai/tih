package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the NOTIFICATION_RECEIVER database table.
 * 
 */
@Entity
@Table(name="NOTIFICATION_RECEIVER")
public class NotificationReceiver extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="READ_DATE")
	private Date readDate;

	@Column(name="READ_IND")
	private String readInd;

	@Column(name="RECEIVED_BY")
	private String receivedBy;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to NotificationSender
    @ManyToOne
	@JoinColumn(name="NOTIFICATION_SENDER")
	private NotificationSender notificationSender;

    public NotificationReceiver() {
    }

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDatetime() {
		return this.createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getDefunctInd() {
		return this.defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public Date getReadDate() {
		return this.readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	public String getReadInd() {
		return this.readInd;
	}

	public void setReadInd(String readInd) {
		this.readInd = readInd;
	}

	public String getReceivedBy() {
		return this.receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDatetime() {
		return this.updatedDatetime;
	}

	public void setUpdatedDatetime(Date updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}

	public NotificationSender getNotificationSender() {
		return this.notificationSender;
	}

	public void setNotificationSender(NotificationSender notificationSender) {
		this.notificationSender = notificationSender;
	}
	
}