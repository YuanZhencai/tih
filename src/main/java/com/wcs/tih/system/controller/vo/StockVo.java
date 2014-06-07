/** * StockVo.java 
 * Created on 2014年3月25日 下午3:33:21 
 */

package com.wcs.tih.system.controller.vo;

import java.util.Date;

import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyStockStructure;

/** 
* <p>Project: tih</p> 
* <p>Title: StockVo.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
public class StockVo {

	private Companymstr companymstr;

	private String createdBy;

	private Date createdDatetime;

	private String currency;

	private String defunctInd;

	private String ratio;

	private String registeredCapital;

	private String shareholder;

	private Date statisticsDatetime;

	private CompanyStockStructure stock;

	private String type;

	private String updatedBy;

	private Date updatedDatetime;

	public Companymstr getCompanymstr() {
		return companymstr;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public String getCurrency() {
		return currency;
	}

	public String getDefunctInd() {
		return defunctInd;
	}

	public String getRatio() {
		return ratio;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public String getShareholder() {
		return shareholder;
	}

	public Date getStatisticsDatetime() {
		return statisticsDatetime;
	}

	public CompanyStockStructure getStock() {
		return stock;
	}

	public String getType() {
		return type;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedDatetime() {
		return updatedDatetime;
	}

	public void setCompanymstr(Companymstr companymstr) {
		this.companymstr = companymstr;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDefunctInd(String defunctInd) {
		this.defunctInd = defunctInd;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public void setShareholder(String shareholder) {
		this.shareholder = shareholder;
	}

	public void setStatisticsDatetime(Date statisticsDatetime) {
		this.statisticsDatetime = statisticsDatetime;
	}

	public void setStock(CompanyStockStructure stock) {
		this.stock = stock;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDatetime(Date updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
}
