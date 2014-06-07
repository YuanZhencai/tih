package com.wcs.tihm.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.wcs.tih.transaction.controller.vo.WfInstancemstrVo;
import com.wcs.tih.transaction.service.TaskManagementService;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class TihmTaskService {
    @EJB
    private TaskManagementService taskManagementService;

    public List<WfInstancemstrVo> getTasksToDo(String userName){
        return taskManagementService.getTasksToDo(userName);
    }
    
    public List<WfInstancemstrVo> getTasksDone(String userName){
        return taskManagementService.getTasksDone(userName);
    }

    public List<WfInstancemstrVo> getTasksApplied(String userName){
        return taskManagementService.getTasksApplied(userName);
    }
}
