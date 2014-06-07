package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the NOTIFICATION_EXT database table.
 * 
 */
@Entity
@Table(name="NOTIFICATION_EXT")
public class NotificationExt extends com.wcs.base.model.IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Column(name="CREATED_DATETIME")
	private Date createdDatetime;

	@Column(name="DEFUNCT_IND")
	private String defunctInd;

	@Column(name="NOTIFICATIONMSTR_ID")
	private long notificationmstrId;

	@Column(name="TABLE_COLUMN")
	private String tableColumn;

	@Column(name="TABLE_ID")
	private long tableId;

	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATETIME")
	private Date updatedDatetime;

	public NotificationExt() {
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

	public long getNotificationmstrId() {
		return this.notificationmstrId;
	}

	public void setNotificationmstrId(long notificationmstrId) {
		this.notificationmstrId = notificationmstrId;
	}

	public String getTableColumn() {
		return this.tableColumn;
	}

	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}

	public long getTableId() {
		return this.tableId;
	}

	public void setTableId(long tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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

}