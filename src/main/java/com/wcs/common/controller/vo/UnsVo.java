package com.wcs.common.controller.vo;

import java.io.Serializable;


public class UnsVo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * JMS用到的VO
	 */
	private String account;
	private String email;
	private String telNo;
	
	public UnsVo(){
		
	}
	
	public UnsVo(String account,String email,String telNo){
		this.account=account;
		this.email=email;
		this.telNo=telNo;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	
	
}
