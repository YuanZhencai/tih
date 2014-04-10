package com.wcs.tih.filenet.pe.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;

import filenet.vw.api.VWException;
import filenet.vw.api.VWStepElement;
@Stateless
public class DraftWorkflow extends WorkflowInterface{
    private static String userPassword = ResourceBundle.getBundle("filenet").getString("user.password");
    
    @EJB 
    private TDSLocal tdslocal;
    /**
     * 
     * <p>Description:注册监听 </p>
     */
    @Inject 
    private Event<WfStepmstr> events;
    @PersistenceContext
    private EntityManager em;
    
    @EJB 
    private WorkflowService workflowService;
    
    @EJB 
    private LoginService loginservice;
    
    @EJB 
    private UserCommonService userCommonService;
    
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
    
    /**
     * 创建草稿箱
     * <p>Description: </p>
     * @param wf
     * @param wfMap
     */
    public void saveDraft(WfInstancemstr wf,Map<String,String> wfMap){
        this.saveWfStr(wf);
        this.saveWfInstanceProperty(wf, wfMap);
    }
    
    
    /**
     * 创建流程
     * <p>Description: </p>
     * @param wf
     * @param step
     * @param stepMap
     * @param workflowMap
     * @param workflowName
     * @throws VWException
     */
    public void createWorkflow(WfInstancemstr wf,WfStepmstr step,Map<String,String> stepMap,Map<String,String[]> workflowMap,String workflowName,String requestForm) throws VWException{
        String changedBy=this.getAuthorizer(step.getChargedBy(), requestForm);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for(String aa : workflowMap.keySet()){
            if(workflowMap.get(aa) instanceof String[]){
                dataMap.put(aa, new String []{new StringBuilder().append("uid=").append(changedBy).append(",").append(super.dnStr).toString()});
            }
        }
        VWStepElement vw= this.workflowService.createWorkFlow(workflowName, loginservice.getCurrentUsermstr().getAdAccount(), this.userPassword, dataMap);
        step.setCode(String.valueOf(vw.getWorkOrderId()));
        step.setName(vw.getStepName());
        step.setChargedBy(tdslocal.addPre(changedBy));
        this.updateWfMstr(wf, vw.getWorkflowNumber());
        this.saveWfStep(step);
        this.saveWfStepmstrProperty(step, stepMap);
        this.events.fire(step);
    }
    public void updateWfMstr(WfInstancemstr wf,String workflowNumber){
        String sql="select w from  WfInstancemstr w where w.id="+wf.getId();
        WfInstancemstr w = (WfInstancemstr)this.em.createQuery(sql).getResultList().get(0);
        w.setNo(workflowNumber);
        this.em.merge(w);
    }
    public String getAuthorizer(String username, String requestform) {
        return userCommonService.getAuthorizer(username, requestform);
    }
}
