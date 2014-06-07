/** * MailConfigVo.java 
* Created on 2014年2月11日 下午2:05:27 
*/

package com.wcs.scheduler.vo;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import com.wcs.tih.model.WfTimeoutConfig;

/** 
 * <p>Project: tih</p> 
 * <p>Title: MailConfigVo.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */

public class TimeoutConfigVo {
	private WfTimeoutConfig config;
	
	private String effectiveDays;

	private String enableInd;

	private String mailInd;

	private String positionTimeoutInd;

	private String remarks;

	private String sysNoticeInd;

	private String wfRequestformType;

	private String wfTimeoutInd;

	private String wfType;
	
	private String timeoutType;
	
	private String wpIntervalDays;

	private String wpTimeoutDays;

	private String wpUrgeDays;
	
	private boolean mail;
	
	private boolean notice;

	private List<String> requestforms = new ArrayList<String>();
	
	private String defunctInd;
	
	public WfTimeoutConfig getConfig() {
		return config;
	}

	public void setConfig(WfTimeoutConfig config) {
		this.config = config;
	}

	public String getEnableInd() {
		return enableInd;
	}

	public void setEnableInd(String enableInd) {
		this.enableInd = enableInd;
	}

	public String getMailInd() {
		return mailInd;
	}

	public void setMailInd(String mailInd) {
		this.mailInd = mailInd;
	}

	public String getPositionTimeoutInd() {
		return positionTimeoutInd;
	}

	public void setPositionTimeoutInd(String positionTimeoutInd) {
		this.positionTimeoutInd = positionTimeoutInd;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSysNoticeInd() {
		return sysNoticeInd;
	}

	public void setSysNoticeInd(String sysNoticeInd) {
		this.sysNoticeInd = sysNoticeInd;
	}

	public String getWfRequestformType() {
		return wfRequestformType;
	}

	public void setWfRequestformType(String wfRequestformType) {
		this.wfRequestformType = wfRequestformType;
	}

	public String getWfTimeoutInd() {
		return wfTimeoutInd;
	}

	public void setWfTimeoutInd(String wfTimeoutInd) {
		this.wfTimeoutInd = wfTimeoutInd;
	}

	public String getWfType() {
		return wfType;
	}

	public void setWfType(String wfType) {
		this.wfType = wfType;
	}

	public String getTimeoutType() {
		return timeoutType;
	}

	public void setTimeoutType(String timeoutType) {
		this.timeoutType = timeoutType;
	}

	public List<String> getRequestforms() {
		return requestforms;
	}

	public void setRequestforms(List<String> requestforms) {
		this.requestforms = requestforms;
	}

	public boolean isMail() {
		return mail;
	}

	public void setMail(boolean mail) {
		this.mail = mail;
	}

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public String getDefunctInd() {
		return defunctInd;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public String getEffectiveDays() {
		return effectiveDays;
	}

	public void setEffectiveDays(String effectiveDays) {
		this.effectiveDays = effectiveDays;
	}

	public String getWpIntervalDays() {
		return wpIntervalDays;
	}

	public void setWpIntervalDays(String wpIntervalDays) {
		this.wpIntervalDays = wpIntervalDays;
	}

	public String getWpTimeoutDays() {
		return wpTimeoutDays;
	}

	public void setWpTimeoutDays(String wpTimeoutDays) {
		this.wpTimeoutDays = wpTimeoutDays;
	}

	public String getWpUrgeDays() {
		return wpUrgeDays;
	}

	public void setWpUrgeDays(String wpUrgeDays) {
		this.wpUrgeDays = wpUrgeDays;
	}
	
}
