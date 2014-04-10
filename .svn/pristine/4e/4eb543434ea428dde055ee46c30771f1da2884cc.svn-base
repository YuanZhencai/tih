package com.wcs.tih.filenet.pe.service;

import javax.ejb.Local;

/**
 * Project: tih
 * Description: workflow回调接口
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 * @see WorkflowInterface#setWorkflowInterface(WorkflowCallBack)
 */
@Local
public interface WorkflowCallBack {
    public String getAuthorizer(String  userName,String requetForm);
	public void sendNotice(String [] userName,String requestForm,String subject,String body);
}
