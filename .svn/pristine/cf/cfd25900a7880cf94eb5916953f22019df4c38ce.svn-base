package com.wcs.tih.filenet.pe.service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.P;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.document.controller.vo.UploadWorkflowVo;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;

import filenet.vw.api.VWException;
import filenet.vw.api.VWStepElement;

/**
 * Project: tih Description: 流程保存数据 
 * Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 */

@Stateless
public class DefaultWorkflowImpl extends WorkflowInterface {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String userPassword = ResourceBundle.getBundle("filenet").getString("user.password");
  
    @EJB 
    private TDSLocal tdslocal;
    
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginservice;
    @EJB
    private UserCommonService userservice;
    @EJB
    private TimeoutEmailService timeoutEmailService;
    
   
    /**
     * 
     * <p>Description:注册监听 </p>
     */
    @Inject 
    private Event<WfStepmstr> events;

    
    @PostConstruct
    public void init(){
        
    }
    
    
    /**
     * @see WorkflowInterface#saveData
     * @see WorkflowInterface#dispatchData
     */
    @Override
    public void saveWfStepmstrProperty(WfStepmstr wf, Map<String, String> paramMap) {
        for (String s : paramMap.keySet()) {
            WfStepmstrProperty p = new WfStepmstrProperty();
            p.setValue(paramMap.get(s));
            p.setName(s);
            p.setWfStepmstr(wf);
            this.em.persist(p);
        }
    }

    /**
     * @see WorkflowInterface#saveData
     * @see WorkflowInterface#dispatchData
     */
    @Override
    public void saveWfInstanceProperty(WfInstancemstr wf, Map<String, String> paramMap) {
        for (String s : paramMap.keySet()) {
            WfInstancemstrProperty p = new WfInstancemstrProperty();
            p.setValue(paramMap.get(s));
            p.setName(s);
            p.setWfInstancemstr(wf);
            this.em.persist(p);
        }
    }

    /**
     * @see WorkflowInterface#saveData
     * @see WorkflowInterface#dispatchData
     */
    @Override
    public void saveWfStr(WfInstancemstr wfstr) {
        this.em.persist(wfstr);
    }

    /**
     * @see WorkflowInterface#saveData
     * @see WorkflowInterface#dispatchData
     */
    @Override
    public void saveWfStep(WfStepmstr wfstep) {
        this.em.persist(wfstep);
    }

    public WfStepmstr getFirstStep(String workflowNumber) {
        
        return (WfStepmstr) this.em.createQuery(new StringBuilder().append("select s from WfStepmstr s where s.wfInstancemstr.no ='").append(workflowNumber).append("'").toString()).getResultList().get(0);
    }

    public WfStepmstr getLastStep(WfInstancemstr w) {
        List li = this.em.createQuery( new StringBuilder().append("select s from WfStepmstr s where s.wfInstancemstr.id=").append(w.getId()).append(" order by s.id ").toString()).getResultList();
        return (WfStepmstr) li.get(li.size() - 1);
    }
    public List<WfStepmstr> getStepmstr(Long id) {
    	String sql = "select steps from WfStepmstr steps where steps.wfInstancemstr.id = "+id+" order by steps.id ";
    	return em.createQuery(sql).getResultList();
    }


    /**
     * Description: 创建流程
     * @param intanceMap
     * @param requestForm
     * @param workflowName
     * @param stepMap
     * @param wfRemindVo 
     * @throws VWException
     */
    public WfInstancemstr createworkflow(Map<String, String> intanceMap, String requestForm, String workflowName, Map<String, String> stepMap,String nodeName,String userName,String remarks,String importance,String urgency, WfRemindVo wfRemindVo) throws VWException {
        String createBy=this.loginservice.getCurrentUsermstr().getAdAccount();
        String changedBy=this.getAuthorizer(userName, requestForm);
        String dnstr=new StringBuilder().append("uid=").append(userName).append(",").append(this.dnStr).toString();
        Map<String,String[]> paramMap= new HashMap<String, String[]>();
        paramMap.put(nodeName, new String[]{dnstr});
        WfInstancemstr w = new WfInstancemstr();
        w.setImportance(importance);
        w.setUrgency(urgency);
        w.setRemarks(remarks);
        w.setRequestBy(createBy);
        w.setType(requestForm);
        w.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_2);
        w.setCreatedBy(createBy);
        w.setDefunctInd("N");
        w.setUpdatedBy(createBy);
        w.setUpdatedDatetime(new Date());
        w.setCreatedDatetime(new Date());
        w.setSubmitDatetime(new Date());
        WfStepmstr step = new WfStepmstr();
        step.setUpdatedBy(createBy);
        step.setCreatedBy(createBy);
        step.setDefunctInd("N");
        step.setUpdatedDatetime(new Date());
        step.setCreatedDatetime(new Date());
        step.setChargedBy(changedBy);
        step.setDealMethod(DictConsts.TIH_TAX_APPROACH_1);
        step.setWfInstancemstr(w);
        step.setFromStepId(0);
        step.setCompletedDatetime(w.getCreatedDatetime());
        super.setIntanceMap(intanceMap);
        super.setSaveStep(step);
        super.setStepMap(stepMap);
        super.setSaveWfstr(w);
        super.setWorkflowName(workflowName);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for(String  p : paramMap.keySet()){
            dataMap.put(p, new String []{new StringBuilder().append("uid=").append(this.tdslocal.addPre(changedBy)).append(",").append(super.dnStr).toString()});
        }
        super.createWorkFlow(this.tdslocal.addPre(loginservice.getCurrentUsermstr().getAdAccount()), this.userPassword, dataMap);
        wfRemindVo.setWfId(w.getId());
        timeoutEmailService.saveWfTimeoutRemind(wfRemindVo);
        try {
            this.events.fire(step); //注册监听
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
		return w;
    }

    public void doDispath(Map<String, Object> paramMap, WfStepmstr saveStep, Map<String, String> stepMap, String workflownumber) throws Exception,VWException {
        String userName = "";
        if(saveStep.getChargedBy().indexOf(",")!=-1){
            userName=saveStep.getChargedBy();
        }else{
            userName=this.getAuthorizer(saveStep.getChargedBy(), saveStep.getWfInstancemstr().getType());
        }
        saveStep.setChargedBy(userName);
        super.setStepMap(stepMap);
        super.setSaveStep(saveStep);
        for(String aa : paramMap.keySet()){
            if(paramMap.get(aa) instanceof String[]){
                paramMap.put(aa, new String []{new StringBuilder().append("uid=").append(this.tdslocal.addPre(userName)).append(",").append(super.dnStr).toString()});
            }
        }
        super.doDispatch(this.tdslocal.addPre(loginservice.getCurrentUsermstr().getAdAccount()), userPassword, workflownumber, paramMap,saveStep.getWfInstancemstr().getType());
        try {
            this.events.fire(saveStep);//注册监听
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<WfStepmstrProperty> getWorkflowDetail(String workflowNumber) {
        Query q = this.em.createQuery(new StringBuilder().append("select s from WfStepmstrProperty s where s.wfStepmstr.wfInstancemstr.no ='").append(workflowNumber).append("' order by s.id").toString());
        q.setFirstResult(1);
        return q.getResultList();
    }

    /**
     * update WfInstanceMstr status
     * @param wfInstancemstr
     */
    public void updateWfInstanceStatus(WfInstancemstr wfInstancemstr) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select w from WfInstancemstr w where w.id =").append( wfInstancemstr.getId());
        WfInstancemstr wf = (WfInstancemstr) this.em.createQuery(sb.toString()).getResultList().get(0);
        wf.setStatus(wfInstancemstr.getStatus());
        this.em.merge(wf);
    }

    /**
     * Description:get workflow node name
     * @param workflowNumber
     * @return
     * @throws VWException
     */
    public String getWorkStepNameById(String workflowNumber) throws VWException {
        return super.getStepNameByWobNum(this.tdslocal.addPre(this.loginservice.getCurrentUsermstr().getAdAccount()), this.userPassword, workflowNumber);
    }

    /**
     * Description:完成之后调用该方法
     * @param id
     * @param status
     */
    public void editWfInstance(WfInstancemstr w) {
        WfInstancemstr wf = (WfInstancemstr) this.em.createQuery(new StringBuilder().append("select w from WfInstancemstr w where w.id =").append(w.getId()).toString()).getResultList().get(0);
        wf.setStatus(w.getStatus());
        this.em.merge(wf);
    }
    public void updateWfInstance(String no,String importance,String urgency) {
    	String sql1 = "update WF_INSTANCEMSTR set IMPORTANCE = '"+ importance +"' where NO = '"+  no +"'";
    	String sql2 = "update WF_INSTANCEMSTR set URGENCY = '"+ urgency +"' where NO = '"+  no +"'";
    	this.em.createNativeQuery(sql1).executeUpdate();
    	this.em.createNativeQuery(sql2).executeUpdate();
    }

    /**
     * Description: update WfInstanceProperty
     */
    public void editWfInstanceProperty(WfInstancemstrProperty p) {
        WfInstancemstrProperty w = (WfInstancemstrProperty) this.em.createQuery(new StringBuilder().append("select w from WfInstancemstrProperty w where w.wfInstancemstr.id = ").append(p.getWfInstancemstr().getId()).append(" and w.name = '").append( p.getName()).append("'").toString()).getResultList().get(0);
        w.setValue(p.getValue());
        this.em.merge(w);
    }

    /**
     * get WfInstancemstrProperty By Key And Value
     * @param id
     * @return
     */
    public String getWfInstancemstrPropertyByKeyAndValue(long id, String keyValue) {
        return (String) this.em.createQuery(new StringBuilder().append("select c.value from WfInstancemstrProperty c where c.name ='").append(keyValue).append("' and c.wfInstancemstr.id=").append(id).toString()).getResultList().get(0);
    }

    /**
     * <p>
     * Description:获得紧急程度和重要程度
     * </p>
     * @param wfinstanceId
     * @return
     */

    public UploadWorkflowVo getUploadWorkflowVo(long wfinstanceId) {
        UploadWorkflowVo up = new UploadWorkflowVo();
        List li = this.em.createQuery(new StringBuilder().append(" select s from WfInstancemstrProperty s  where s.wfInstancemstr.id = ").append(wfinstanceId).toString()).getResultList();
        for (int i = 0; i < li.size(); i++) {
            WfInstancemstrProperty wf = (WfInstancemstrProperty) li.get(i);
            if (wf.getName().equals(DictConsts.TIH_TAX_WORKFLOWURGENCY)){
            	up.setWorkflowUgency(wf.getValue());
            }
            if (wf.getName().equals(DictConsts.TIH_TAX_WORKFLOWIMPORTANCE)){
            	up.setWorkflowImportance(wf.getValue());
            }
        }
        return up;
    }

    public String getAuthorizer(String username, String requestform) {
        return userservice.getAuthorizer(username, requestform);
    }
    
    public String getUserName(String str) {
        if (str == null){
        	return "";
        }
        if (this.userservice.getUsermstrVoByAdAccount(str) == null){
        	return str;
        }
        P p = this.userservice.getUsermstrVoByAdAccount(str).getP();
        if (p == null){
        	return str;
        }
        if(p.getNachn()==null||p.getNachn().equals("")){
        	return str;
        }
        return p.getNachn();
    }
}
