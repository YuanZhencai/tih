/**
 * TihmTaskResource.java
 * Created: 2013-1-15 上午09:49:41
 */
package com.wcs.tihm.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wcs.tih.transaction.controller.vo.WfInstancemstrVo;
import com.wcs.tihm.util.RestfulUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:chengchao@wcs-global.com">ChengChao</a>
 */
@Path("/task/{type}")
@Stateless
public class TihmTaskResource {
	
	public static final int TYPE_TASK_TODO = 1;
	public static final int TYPE_TASK_DONE = 2;
	public static final int TYPE_TASK_APPLIED = 3;
	
	private static final String EMPTY="[]";
	@EJB
	private TihmTaskService tihmTaskService;


	/**
	 * <p>Description: 根据type查询任务列表</p>
	 * @param type： TYPE_TASK_TODO，TYPE_TASK_DONE ，TYPE_TASK_APPLIED
	 * @param userName：用户名
	 * @return List<WfInstancemstrVo> JSON格式
	 */
	@GET
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getTasks(@PathParam("type") int type, @QueryParam("username") String userName) {
		if (userName == null || "".equals(userName)) {
			return EMPTY;
		}
		switch (type) {
		case TYPE_TASK_TODO:
			return getTasksToDo(userName);
		case TYPE_TASK_DONE:
			return getTasksDone(userName);
		case TYPE_TASK_APPLIED:
			return getTasksApplied(userName);
		default:
			return EMPTY;
		}
	}
	
	/**
	 * <p>Description: 查询的代办任务列表</p>
	 * @param userName
	 * @return
	 */
	public String getTasksToDo(String userName) {
		// TODO 调用注入的service,根据用户名获得待办任务对象集合
	    List<WfInstancemstrVo> tasksToDo = tihmTaskService.getTasksToDo(userName);
		return RestfulUtil.objectToJson(tasksToDo);
	}
	
	
	/**
	 * <p>Description: 查询我的已办列表</p>
	 * @param userName
	 * @return
	 */
	public String getTasksDone(String userName) {
	    List<WfInstancemstrVo> tasksDone = tihmTaskService.getTasksDone(userName);
	    return RestfulUtil.objectToJson(tasksDone);
	}
	
	/**
	 * <p>Description: 查询我的申请列表</p>
	 * @param userName
	 * @return
	 */
	public String getTasksApplied(String userName) {
	    List<WfInstancemstrVo> tasksApplied = tihmTaskService.getTasksApplied(userName);
	    return RestfulUtil.objectToJson(tasksApplied);
	}
	
}
