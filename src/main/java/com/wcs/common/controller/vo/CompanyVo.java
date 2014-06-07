/** * CompanyVo.java 
* Created on 2014年4月15日 下午3:32:36 
*/

package com.wcs.common.controller.vo;

import java.util.Date;

import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;

/** 
 * <p>Project: tih</p> 
 * <p>Title: CompanyVo.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */

public class CompanyVo {

	
	private Companymstr company = new Companymstr();
	private O o = new O();
	
	// Companymstr
	private String address;

    private Date startDatetime;
    private Date stepDatetime;

    private String createdBy;

    private Date createdDatetime;

    private String defunctInd;

    private String desc;

    private String oid;

    private String telphone;

    private String type;

    private String updatedBy;

    private Date updatedDatetime;

    private String zipcode;

    private String region;
    
    private String province;
    
    private String code;
    
    // O
	private String bukrs;

	private String kostl;

	private String parent;

	private String stext;

	private String zhrtxxlid;

	private String zhrtxxlms;

	private String zhrzzcjid;

	private String zhrzzdwid;
	
	public CompanyVo() {
		// TODO Auto-generated constructor stub
	}

	public CompanyVo(Companymstr company, O o) {
		this.company = company;
		this.o = o;
	}
	
	public CompanyVo(Companymstr company, O o, String bukrs) {
		this.company = company;
		this.o = o;
		this.bukrs = bukrs;
	}
	
	public Companymstr getCompany() {
		return company;
	}

	public void setCompany(Companymstr company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStartDatetime() {
		return startDatetime;
	}

	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}

	public Date getStepDatetime() {
		return stepDatetime;
	}

	public void setStepDatetime(Date stepDatetime) {
		this.stepDatetime = stepDatetime;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public O getO() {
		return o;
	}

	public void setO(O o) {
		this.o = o;
	}

	public String getBukrs() {
		return bukrs;
	}

	public void setBukrs(String bukrs) {
		this.bukrs = bukrs;
	}

	public String getKostl() {
		return kostl;
	}

	public void setKostl(String kostl) {
		this.kostl = kostl;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getStext() {
		return stext;
	}

	public void setStext(String stext) {
		this.stext = stext;
	}

	public String getZhrtxxlid() {
		return zhrtxxlid;
	}

	public void setZhrtxxlid(String zhrtxxlid) {
		this.zhrtxxlid = zhrtxxlid;
	}

	public String getZhrtxxlms() {
		return zhrtxxlms;
	}

	public void setZhrtxxlms(String zhrtxxlms) {
		this.zhrtxxlms = zhrtxxlms;
	}

	public String getZhrzzcjid() {
		return zhrzzcjid;
	}

	public void setZhrzzcjid(String zhrzzcjid) {
		this.zhrzzcjid = zhrzzcjid;
	}

	public String getZhrzzdwid() {
		return zhrzzdwid;
	}

	public void setZhrzzdwid(String zhrzzdwid) {
		this.zhrzzdwid = zhrzzdwid;
	}
    
    
}
