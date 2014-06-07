package com.wcs.tih.filenet.pe.util;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.service.NoticeService;
import com.wcs.tih.filenet.pe.vo.MailVo;
import com.wcs.tih.model.WfStepmstr;
/**
 * Project: tih
 * Description:  流程发送邮件观察者
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
*/
@Stateless
public class SendNoticeObserver {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @EJB
    private MailUtil mailUtil;
    @EJB
    private NoticeService noticeservice;

    public void listener(@Observes WfStepmstr wf) {
        logger.info("sendmail..." + wf.getCreatedDatetime());
        List<MailVo> list = mailUtil.getTitleTypeByDealMethod(wf);
        logger.info("length==="+list.size());
        List<String> receiverAccounts = null;
        List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
        for (int i = 0; i < list.size(); i++) {
            receiverAccounts = new ArrayList<String>();
            logger.info("发送人===" + list.get(i).getSendBy() + "body ==" + list.get(i).getBody());
            if(list.get(i).getSendBy().indexOf(",")!=-1){
            	String [] a=list.get(i).getSendBy().split(",");
            	for(int j=0;j<a.length;j++){
            		logger.info(a[j]);
            		logger.info("发送人===" + a[j] + "body ==" + mailUtil.getUserName(a[j])+list.get(i).getBody().substring(list.get(i).getBody().lastIndexOf(",")+1,list.get(i).getBody().length()));
            		receiverAccounts.add(a[j]);
            	}
            	NotificationVo noticeVo = new NotificationVo();
            	noticeVo.setReceiverList(receiverAccounts);
            	noticeVo.setTypeId(wf.getWfInstancemstr().getNo());
            	noticeVo.setTitle(list.get(i).getTitle());
            	noticeVo.setContent(list.get(i).getBody().substring(list.get(i).getBody().lastIndexOf(",")+1,list.get(i).getBody().length()));
            	noticeVos.add(noticeVo);
            	
            }else{
                receiverAccounts.add(list.get(i).getSendBy());
                NotificationVo noticeVo = new NotificationVo();
                noticeVo.setReceiverList(receiverAccounts);
                noticeVo.setTypeId(wf.getWfInstancemstr().getNo());
                noticeVo.setTitle(list.get(i).getTitle());
                noticeVo.setContent(list.get(i).getBody());
                noticeVos.add(noticeVo);
            }
        }
        
        noticeservice.sendNoticeForTask(wf.getWfInstancemstr().getType(), noticeVos);
        
    }
}
