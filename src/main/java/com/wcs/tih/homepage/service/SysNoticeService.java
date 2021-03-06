package com.wcs.tih.homepage.service;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.service.NoticeService;

@Stateless
public class SysNoticeService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private LoginService LoginService;
	@EJB
	private NoticeService noticeService;
	
	public void sendSysNotice(NotificationVo noticeVo) {
		noticeVo.setTitle(" [益海嘉里]税务信息平台-系统通知");
		noticeService.sendSysNotice(noticeVo);
	}

}
