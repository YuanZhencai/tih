package com.wcs.common.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

public class AttachmentVo extends IdModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tableName;
	private Long entityId;
	private String operationType;//控制tab页：上传/下载的显示；001：上传tab页面不显示，010：下载tab页面不显示，100：上传和下载tab都要显示

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

}
