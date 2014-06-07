package com.wcs.tih.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the NOTIFICATION_SENDER database table.
 * 
 */
@Entity
@Table(name="NOTIFICATION_SENDER")
public class NotificationSender extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="SEND_DATETIME")
	private Date sendDatetime;

	@Column(name="SEND_OPTION")
	private String sendOption;

	@Column(name="SENT_BY")
	private String sentBy;

	private String status;

	@Column(name="UPDATED_BY")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	//bi-directional many-to-one association to NotificationReceiver
	@OneToMany(mappedBy="notificationSender", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private List<NotificationReceiver> notificationReceivers;

	//bi-directional many-to-one association to Notificationmstr
    @ManyToOne
	private Notificationmstr notificationmstr;

    public NotificationSender() {
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

	public Date getSendDatetime() {
		return this.sendDatetime;
	}

	public void setSendDatetime(Date sendDatetime) {
		this.sendDatetime = sendDatetime;
	}

	public String getSendOption() {
		return this.sendOption;
	}

	public void setSendOption(String sendOption) {
		this.sendOption = sendOption;
	}

	public String getSentBy() {
		return this.sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<NotificationReceiver> getNotificationReceivers() {
		return this.notificationReceivers;
	}

	public void setNotificationReceivers(List<NotificationReceiver> notificationReceivers) {
		this.notificationReceivers = notificationReceivers;
	}
	
	public Notificationmstr getNotificationmstr() {
		return this.notificationmstr;
	}

	public void setNotificationmstr(Notificationmstr notificationmstr) {
		this.notificationmstr = notificationmstr;
	}
	
}