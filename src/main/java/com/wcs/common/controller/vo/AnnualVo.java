/** * AnnualVo.java 
* Created on 2014年3月19日 上午9:25:23 
*/

package com.wcs.common.controller.vo;

import java.util.Date;

/** 
 * <p>Project: tih</p> 
 * <p>Title: AnnualVo.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */

public class AnnualVo {

	private String itemLabel;
	private Date itemValue;
	private String date;
	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}
	public Date getItemValue() {
		return itemValue;
	}
	public void setItemValue(Date itemValue) {
		this.itemValue = itemValue;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
