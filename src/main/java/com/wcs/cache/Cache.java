/** * Cache.java 
* Created on 2014年5月5日 下午3:11:17 
*/

package com.wcs.cache;

import java.io.Serializable;
import java.util.Date;


/** 
* <p>Project: tih</p> 
* <p>Title: Cache.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
public class Cache implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;//缓存ID 
	private Object value;//缓存数据 
	private long timeOut;//更新时间 
	private boolean expired; //是否终止 
	
	private String createdBy;
	private Date createdDatetime;
	private String defunctInd;
	private String updatedBy;
	private Date updatedDatetime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public long getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDatetime() {
		return createdDatetime;
	}
	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public String getDefunctInd() {
		return defunctInd;
	}
	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDatetime() {
		return updatedDatetime;
	}
	public void setUpdatedDatetime(Date updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}


}
