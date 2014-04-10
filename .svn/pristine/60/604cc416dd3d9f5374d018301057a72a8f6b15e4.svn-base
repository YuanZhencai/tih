package com.wcs.scheduler.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;

import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.service.JobManagementService;
import com.wcs.scheduler.service.JobService;
import com.wcs.scheduler.vo.JobInfoVo;
import com.wcs.tih.model.JobInfo;
import com.wcs.tih.model.WfMailConfig;
import com.wcs.tih.util.ValidateUtil;

/**
 *
 * @author Christopher Lam
 */
@ManagedBean(name = "JobMBean")
@ViewScoped
public class JobBean implements java.io.Serializable {
    private static final String OPTION = "option";
    @EJB
    private JobService jobService;
    @EJB 
    private JobManagementService jobManagementService;
    @EJB 
    private CommonService commonService;
    
    private String excuteMethod = "add";
    
    private JobInfoVo jobVo;
    private JobInfo newJob;
    private List<JobInfo> jobList = null;
    
    private TreeNode root;
    
    private Date startDate;
    private Date endDate;
    
    //picklist
  	private DualListModel<String> pickList;  
  	private List<String> source = null;
  	private List<String> target = null;
  	
  	private List<WfMailConfig> tamp;
    
  	private static final String JOB_CLASS_NAME = ResourceBundle.getBundle("uns").getString("JobClassName");
    
    public JobBean() {
		super();
	}

	@PostConstruct
    public void init(){
		root= null;
		createTree();
		pickList = new DualListModel<String>(new ArrayList<String>(0), new ArrayList<String>(0));
    }
	
	public void createTree(){
		root = jobManagementService.getCreateTree();
	}
	
	public void pickList(){
		source = new ArrayList<String>();
		target = new ArrayList<String>();
		List<Dict> tempSource = new ArrayList<Dict>();
		tempSource = jobManagementService.getSource();
		for(Dict v : tempSource){
			source.add(v.getCodeVal());
		}
        pickList = new DualListModel<String>(source, target);
    }
	
	public void resertPickList(String jobId){
		initDialog(jobId);
		source = new ArrayList<String>();
		target = new ArrayList<String>();
		target = jobManagementService.gettemptarget(jobId);
		source = jobManagementService.getSource(target);
        pickList = new DualListModel<String>(source, target);
	}
    
    public void createTimer(){
    	newJob = new JobInfo();
    	this.newJob.setJobId("<Job ID>");
    	this.newJob.setJobName("");
    	this.newJob.setJobClassName(JOB_CLASS_NAME);
    	this.newJob.setStartDate(new Date());
    	this.newJob.setSecond("0");
    	this.newJob.setMinute("0");
    	this.newJob.setHour("0");
    	this.newJob.setDayOfMonth("*");
    	this.newJob.setMonth("*");
    	this.newJob.setYear("*");
    	pickList();
    }
    
    public List<String> getYear(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    	endDate = newJob.getEndDate();
    	startDate = newJob.getStartDate();
    	int j = 2030;
    	if(endDate != null){
    		String end = sdf.format(endDate);
    		j = Integer.valueOf(end);
    	}
    	List<String> year = new ArrayList<String>();
    	String format = sdf.format(new Date());
    	int i = Integer.valueOf(format);
    	if(startDate != null){
    		String end = sdf.format(startDate);
    		i = Integer.valueOf(end);
    	}
    	for( ; i<=j;i++){
    		year.add(String.valueOf(i));
    	}
    	return year;
    }
    public List<String> getHour(){
    	List<String> hour = new ArrayList<String>();
    	for(int i= 0;i<24;i++){
    		hour.add(String.valueOf(i));
    	}
    	return hour;
    }
    public List<String> getMinsecond(){
    	List<String> minsecond = new ArrayList<String>();
    	for(int i= 0;i<60;i++){
    		minsecond.add(String.valueOf(i));
    	}
    	return minsecond;
    }
    public List<String> getDay(){
    	List<String> day = new ArrayList<String>();
    	for(int i= 1;i<=31;i++){
    		day.add(String.valueOf(i));
    	}
    	return day;
    }

    /*
     * 删除定时器
     */
    public void deleteJob()  {
    	if(newJob != null){
    		jobService.deleteJob(newJob);
            jobManagementService.deleteJobInfo(newJob);
            jobManagementService.deleteWfMailConfigAll(newJob.getJobId());//删除WfMailConfig表中的数据
    		createTree();
    	}
    }
    
    /**
     *<p>删除定时发送邮件中的流程</p>
     */
    public void deleteWfMailConfig(){
    	String type = getDictByCodeVal(jobVo.getJobName());
    	jobManagementService.deleteWfMailConfig(type, jobVo.getJobId());
    	createTree();
    }
    
    public void initDialog(String jobId){
    	newJob = jobManagementService.findJobInfoByJobId(jobId).get(0);
    }
    public void deletewf(String jobId){
    	initDialog(jobId);
    	List<String> selectedwf = jobManagementService.gettemptarget(jobId);;  //已选流程
    	if(selectedwf.size() == 1){
    		RequestContext.getCurrentInstance().addCallbackParam(OPTION, "2");
    	}else{
    		RequestContext.getCurrentInstance().addCallbackParam(OPTION, "1");
    	}
    }

    /*
     * 创建定时器
     */
    public void createJob() {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale browserLang=FacesContext.getCurrentInstance().getViewRoot().getLocale();
        List<WfMailConfig> wfMailConfig = getWfMailConfigByTarget(); 
        String type;
        String value = "";
        boolean a = false;
        boolean b = true;
        for(WfMailConfig mail : wfMailConfig){
        	value = value + commonService.getValueByDictCatKey(mail.getType(), browserLang.toString()) + ";";
        	a = true;
        }
        if(a){
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", value.substring(0, value.length()-1) + "同时也配置了实时发送邮件,在此情况下只发送实时邮件"));
        }
        List<String> selectedwf = pickList.getTarget();  //已选流程
        try {
        	if(! (ValidateUtil.validateRequired(context, newJob.getJobName(), "名称：") //验证名称是否为空，验证名称不能重复
        			& ValidateUtil.validateRepeat(context, jobManagementService.getValidate(newJob.getJobName()).size(), "名称", 1)
        			& ValidateUtil.validateStartTimeAndEndTime(context, newJob.getStartDate(), newJob.getEndDate())
        			& ValidateUtil.validateNewTimeAndEndTime(context, new Date(), newJob.getEndDate())
        			& ValidateUtil.validateRepeatWf(context, selectedwf.size(), "已选流程：", 0))){
        		b = false;
        	}
        	if(!b){
        		return;
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
        	String jobId = sdf.format(new Date()).replace(" ", "");
        	newJob.setJobId(jobId); //设置job的Id（由年月日时分秒组成）
        	jobService.createJob(newJob);
        	if(newJob.getNextTimeout() == null){
        		newJob.setNextTimeout(jobService.getNextTimeout());
        	}
            jobManagementService.saveJobInfo(newJob,true);//保存信息到数据库
            for(String s : selectedwf){
            	String id = newJob.getJobId();
            	type = getDictByCodeVal(s);
            	jobManagementService.saveWfMailConfig(type, id);
            }
            createTree();
            RequestContext.getCurrentInstance().addCallbackParam(OPTION, "success");
        }catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "年、月、日、时、分、秒：", "填写有问题！"));
            Logger.getLogger(JobBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * 修改定时器
     */
    public void updateJob() {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale browserLang=FacesContext.getCurrentInstance().getViewRoot().getLocale();
        List<WfMailConfig> wfMailConfig = getWfMailConfigByTarget();
        String type;
        String value = "";
        boolean a = false;
        boolean b = true;
        for(WfMailConfig mail : wfMailConfig){
        	value = value + commonService.getValueByDictCatKey(mail.getType(), browserLang.toString()) + ";";
        	a = true;
        }
        if(a){
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"", value.substring(0, value.length()-1) + "同时也配置了实时发送邮件,在此情况下只发送实时邮件"));
        }
        try {
        	List<String> selectedwf = pickList.getTarget();
        	if(! (ValidateUtil.validateRequired(context, newJob.getJobName(), "名称：") //验证名称是否为空，验证名称不能重复
        			& ValidateUtil.validateRepeat(context, jobManagementService.getValidate(newJob.getJobName()).size(), "名称", 2)
        			& ValidateUtil.validateStartTimeAndEndTime(context, newJob.getStartDate(), newJob.getEndDate())
        			& ValidateUtil.validateNewTimeAndEndTime(context, new Date(), newJob.getEndDate())
        			& ValidateUtil.validateRepeatWf(context, selectedwf.size(), "已选流程：", 0))){
        		b = false;
        	}
        	if(!b){
        		return;
        	}
        	jobService.updateJob(newJob);
        	newJob.setNextTimeout(jobService.getNextTimeout());
        	jobManagementService.deleteWfMailConfigAll(newJob.getJobId());//删除WfMailConfig表中的数据
            jobManagementService.saveJobInfo(newJob,false);//保存job信息到数据库
            for(String s : selectedwf){
            	String id = newJob.getJobId();
            	type = getDictByCodeVal(s);
            	jobManagementService.saveWfMailConfig(type, id);
            }
        	
        	createTree();
            RequestContext.getCurrentInstance().addCallbackParam(OPTION, "success");
        }
        catch (Exception ex)
        {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "年、月、日、时、分、秒：", "填写有问题！"));
            Logger.getLogger(JobBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 根据已选流程， 查找已选流程是否配置实时发送邮件
     * @return
     */
    public List<WfMailConfig> getWfMailConfigByTarget(){
    	tamp = new ArrayList<WfMailConfig>();
    	String type;
    	for(String str : pickList.getTarget()){
    		type = getDictByCodeVal(str);
    		List<WfMailConfig> wfMailConfig = jobManagementService.getWfMailConfig(type);
    		if(wfMailConfig.size() > 0){
    			tamp.add(wfMailConfig.get(0));
    		}
    	}
    	return tamp;
    }
    
    
    public void getStartDateVal(ValueChangeEvent event){
    	UIComponent component = event.getComponent();
    	Calendar cal=(Calendar)component;
    	Object value = cal.getValue();
    	startDate=(Date)value;
    }
    public void getEndDateVal(ValueChangeEvent event){
    	UIComponent component = event.getComponent();
    	Calendar cal=(Calendar)component;
    	Object value = cal.getValue();
    	endDate=(Date)value;
    }
    
    public String getDictByCodeVal(String type){
    	String val = "";
		List<Dict> dictList = jobManagementService.getDictByCodeVal(type);
		if(dictList.size() > 0){
			Dict dict = dictList.get(0);
			val = dict.getCodeCat() + "." + dict.getCodeKey();
			return val;
		}else{
			return null;
			
		}
    }

	public JobInfo getNewJob() {
		if(newJob == null){
			newJob = new JobInfo();
		}
		return newJob;
	}

	public void setNewJob(JobInfo newJob) {
		this.newJob = newJob;
	}

	public List<JobInfo> getJobList() {
		return jobList;
	}

	public void setJobList(List<JobInfo> jobList) {
		this.jobList = jobList;
	}

	public DualListModel<String> getPickList() {
		return pickList;
	}

	public void setPickList(DualListModel<String> pickList) {
		this.pickList = pickList;
	}

	public List<String> getSource() {
		return source;
	}

	public void setSource(List<String> source) {
		this.source = source;
	}

	public List<String> getTarget() {
		return target;
	}

	public void setTarget(List<String> target) {
		this.target = target;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public JobInfoVo getJobVo() {
		return jobVo;
	}

	public void setJobVo(JobInfoVo jobVo) {
		this.jobVo = jobVo;
	}

	public List<WfMailConfig> getTamp() {
		return tamp;
	}

	public void setTamp(List<WfMailConfig> tamp) {
		this.tamp = tamp;
	}

	public String getExcuteMethod() {
		return excuteMethod;
	}

	public void setExcuteMethod(String excuteMethod) {
		this.excuteMethod = excuteMethod;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
