package com.wcs.tih.system.controller.vo;

import java.io.Serializable;

public class CommonLinkVO implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 3716757593945111810L;
	private Long id;
	private String name;
	private String url;
	private int priority;
	private String defunct;
	private String createdBy;
	private String updatedBy;

	public CommonLinkVO(Long id, String name, String url, int priority,
			String defunct) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.priority = priority;
		this.defunct = defunct;
	}

	public CommonLinkVO() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getDefunct() {
		return defunct;
	}

	public void setDefunct(String defunct) {
		this.defunct = defunct;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
