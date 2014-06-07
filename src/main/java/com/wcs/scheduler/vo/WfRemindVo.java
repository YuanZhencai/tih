/** * PositionRemindVo.java 
 * Created on 2014年2月14日 上午9:49:13 
 */

package com.wcs.scheduler.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.tih.model.WfTimeoutRemind;

public class WfRemindVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WfTimeoutRemind wfTimeoutRemind = null;

	private Date wfCompleteDate;

	private long wfId;

	private String wfIntervalDays;

	private Date wfUrgeDate;
	
	private String positionTimeoutInd;
	
	private String wfTimeoutInd;

	public WfTimeoutRemind getWfTimeoutRemind() {
		return wfTimeoutRemind;
	}

	public void setWfTimeoutRemind(WfTimeoutRemind wfTimeoutRemind) {
		this.wfTimeoutRemind = wfTimeoutRemind;
	}

	public Date getWfCompleteDate() {
		return wfCompleteDate;
	}

	public void setWfCompleteDate(Date wfCompleteDate) {
		this.wfCompleteDate = wfCompleteDate;
	}

	public long getWfId() {
		return wfId;
	}

	public void setWfId(long wfId) {
		this.wfId = wfId;
	}

	public String getWfIntervalDays() {
		return wfIntervalDays;
	}

	public void setWfIntervalDays(String wfIntervalDays) {
		this.wfIntervalDays = wfIntervalDays;
	}

	public Date getWfUrgeDate() {
		return wfUrgeDate;
	}

	public void setWfUrgeDate(Date wfUrgeDate) {
		this.wfUrgeDate = wfUrgeDate;
	}

	public String getPositionTimeoutInd() {
		return positionTimeoutInd;
	}

	public void setPositionTimeoutInd(String positionTimeoutInd) {
		this.positionTimeoutInd = positionTimeoutInd;
	}

	public String getWfTimeoutInd() {
		return wfTimeoutInd;
	}

	public void setWfTimeoutInd(String wfTimeoutInd) {
		this.wfTimeoutInd = wfTimeoutInd;
	}

}
