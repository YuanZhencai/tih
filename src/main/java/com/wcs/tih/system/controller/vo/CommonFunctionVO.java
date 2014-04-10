package com.wcs.tih.system.controller.vo;

import java.io.Serializable;

public class CommonFunctionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private int priority;
	private Long rid;
	private String defunctInd;
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public CommonFunctionVO() {

	}

	public CommonFunctionVO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public CommonFunctionVO(Long id, String name, Long rid) {
		this.id = id;
		this.name = name;
		this.rid = rid;
	}

	public CommonFunctionVO(Long id, String name, Long rid, String defunctInd) {
		this.id = id;
		this.name = name;
		this.rid = rid;
		this.defunctInd = defunctInd;
	}

	public CommonFunctionVO(Long id, String name, int priority) {
		this.id = id;
		this.name = name;
		this.priority = priority;
	}

	public CommonFunctionVO(Long id, String name, int priority,
			String defunctInd, Long rid ,String uri) {
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.defunctInd = defunctInd;
		this.rid = rid;
		this.uri=uri;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getDefunctInd() {
		return defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

}
