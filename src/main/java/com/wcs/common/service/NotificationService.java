package com.wcs.common.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.EmailVo;
import com.wcs.common.controller.vo.NoticeDetailVo;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.controller.vo.SubjectVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.model.JobInfo;
import com.wcs.tih.model.NotificationExt;
import com.wcs.tih.model.NotificationReceiver;
import com.wcs.tih.model.NotificationSender;
import com.wcs.tih.model.Notificationmstr;
import com.wcs.tih.model.Projectmstr;
import com.wcs.tih.model.WfAuthorizmstr;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 系统消息
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yuanzhencai@wcs-global.com">袁振才</a>
 */
@Stateless
public class NotificationService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
	@PersistenceContext
	private EntityManager em;
	@EJB
	private LoginService loginService;
	@EJB 
	private CommonService commonService;
	@EJB 
	private UserCommonService userCommonService;
	
	private static final String APP_URL = ResourceBundle.getBundle("uns").getString("app_url");
	private static final String TASK_LINK = "/faces/transaction/task/index.xhtml";
	private static final String PRO_LINK = "/faces/transaction/project/index.xhtml";
	
	private static Map<String, String> maps = null;
	
	static{
		maps = new HashMap<String, String>();
		maps.put(DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1, APP_URL+"/images/important3.png");
		maps.put(DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_2, APP_URL+"/images/important2.png");
		maps.put(DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_3, APP_URL+"/images/important1.png");
		
		maps.put(DictConsts.TIH_TAX_WORKFLOWURGENCY_1, APP_URL+"/images/urgent3.png");
		maps.put(DictConsts.TIH_TAX_WORKFLOWURGENCY_2, APP_URL+"/images/urgent2.png");
		maps.put(DictConsts.TIH_TAX_WORKFLOWURGENCY_3, APP_URL+"/images/urgent1.png");
	}
	
	/**
	 * <p>Description: 保存系统消息</p>
	 * @param msgType 消息类型(邮件或系统消息)
	 * @param refType 关联类型(任务或项目)
	 * @param sendStatus 消息发送状态
	 * @param typeId 关联ID
	 * @param receiverList 接收人集合
	 * @param subject 主题
	 * @param content 内容(html格式)
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<NotificationReceiver> saveNotification(NotificationVo newsVo) throws Exception {
	    if (newsVo == null){
	    	return null;
	    }
		List<NotificationReceiver> receivers = new ArrayList<NotificationReceiver>();
		newsVo.setSendDatetime(new Date());
		String currentUserName = loginService.getCurrentUserName();
		if(currentUserName==null){
			currentUserName=" ";
		}
		newsVo.setSentBy(currentUserName);
		newsVo.setCurrentUserName(currentUserName);
			Date updatedDatetime = new Date();
			Timestamp createdDatetime = new Timestamp(
					updatedDatetime.getTime());
			// 保存消息内容
			Notificationmstr notificationmstr = new Notificationmstr();

			if (newsVo.getTitle() == null
					|| "".equals(newsVo.getTitle().trim())) {
				throw new Exception("消息的Title不能为空");
			}
			notificationmstr.setTitle(newsVo.getTitle());
			notificationmstr.setContent(newsVo.getContent());
			notificationmstr.setType(newsVo.getRefType());
			notificationmstr.setTypeId(newsVo.getTypeId());
			notificationmstr.setDefunctInd("N");
			if (newsVo.getCurrentUserName() == null || "".equals(newsVo.getCurrentUserName())) {
				throw new Exception("消息CreatedBy和UpdatedBy不能为空");
			}
			notificationmstr.setCreatedBy(newsVo.getCurrentUserName());
			if (createdDatetime == null) {
				throw new Exception("消息的createdDatetime不能为空");
			}
			notificationmstr.setCreatedDatetime(createdDatetime);
			notificationmstr.setUpdatedBy(newsVo.getCurrentUserName());
			if (updatedDatetime == null) {
				throw new Exception("消息的updatedDatetime不能为空");
			}
			notificationmstr.setUpdatedDatetime(updatedDatetime);
			this.em.persist(notificationmstr);
			// 保存发送人
			NotificationSender notificationSender = new NotificationSender();
			notificationSender.setNotificationmstr(notificationmstr);
			if (newsVo.getSentBy() == null || "".equals(newsVo.getSentBy())) {
				throw new Exception("消息发送者的SentBy不能为空");
			}
			notificationSender.setSentBy(newsVo.getSentBy());

			if (newsVo.getStatus() == null || "".equals(newsVo.getStatus().trim())) {
				throw new Exception("消息发送者的Status不能为空");
			}
			notificationSender.setStatus(newsVo.getStatus());

			notificationSender.setSendDatetime(updatedDatetime);

			if (newsVo.getSendOption() == null || "".equals(newsVo.getSendOption().trim())) {
				throw new Exception("消息发送者的SendOption不能为空");
			}
			notificationSender.setSendOption(newsVo.getSendOption());

			notificationSender.setDefunctInd("N");

			if (newsVo.getCurrentUserName() == null || "".equals(newsVo.getCurrentUserName())) {
				throw new Exception("消息发送者的CreatedBy和UpdatedBy不能为空");
			}
			notificationSender.setCreatedBy(newsVo.getCurrentUserName());

			if (createdDatetime == null) {
				throw new Exception("消息发送者的createdDatetime不能为空");
			}
			notificationSender.setCreatedDatetime(createdDatetime);

			notificationSender.setUpdatedBy(newsVo.getCurrentUserName());
			if (updatedDatetime == null) {
				throw new Exception("消息发送者的updatedDatetime不能为空");
			}
			notificationSender.setUpdatedDatetime(updatedDatetime);
			this.em.persist(notificationSender);
			// 保存接收人
			if (null != newsVo.getReceiverList() && newsVo.getReceiverList().size() != 0) {
				NotificationReceiver notificationReceiver;
				for (int i = 0; i < newsVo.getReceiverList().size(); i++) {
					notificationReceiver = new NotificationReceiver();
					notificationReceiver.setNotificationSender(notificationSender);
					if (newsVo.getReceiverList().get(i) == null || "".equals(newsVo.getReceiverList().get(i).trim())) {
						throw new Exception("消息接收者的 ReceivedBy不能为空");
					}
					notificationReceiver.setReceivedBy(newsVo.getReceiverList().get(i));
					notificationReceiver.setReadInd("Y");// Y 表示未阅读
					notificationReceiver.setDefunctInd("N");
					if (newsVo.getCurrentUserName() == null || "".equals(newsVo.getCurrentUserName())) {
						throw new Exception("消息接收者的 CreatedBy和UpdatedBy不能为空");
					}
					notificationReceiver.setCreatedBy(newsVo.getCurrentUserName());
					if (createdDatetime == null) {
						throw new Exception("消息接收者的createdDatetime不能为空");
					}
					notificationReceiver.setCreatedDatetime(createdDatetime);

					notificationReceiver.setUpdatedBy(newsVo.getCurrentUserName());
					if (updatedDatetime == null) {
						throw new Exception("消息接收者的updatedDatetime不能为空");
					}
					notificationReceiver.setUpdatedDatetime(updatedDatetime);
					this.em.persist(notificationReceiver);
					receivers.add(notificationReceiver);
				}
			}
			NotificationExt noticeExt = newsVo.getNoticeExt();
			if(noticeExt != null){
			    Date extDate = new Date();
			    noticeExt.setNotificationmstrId(notificationmstr.getId());
			    noticeExt.setDefunctInd("N");
			    noticeExt.setCreatedDatetime(extDate);
			    noticeExt.setCreatedBy(currentUserName);
			    noticeExt.setUpdatedDatetime(extDate);
			    noticeExt.setUpdatedBy(currentUserName);
			    this.em.persist(noticeExt);
			}
		return receivers;
	}
	
	/**
	 * <p>Description: 把消息状态更新为已发送</p>
	 * @param receivers 消息集合
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void sendEmailSuccess(List<NotificationReceiver> receivers){
	    sendEmailsSuccess(receivers);
	}

	public void sendEmailsSuccess(List<NotificationReceiver> receivers){
	    if(receivers != null && receivers.size() > 0 ){
	        String currentUserName = loginService.getCurrentUserName();
	        currentUserName = currentUserName == null ? "":currentUserName;
	        Date updateDate = new Date();
	        for (NotificationReceiver r : receivers) {
	            NotificationSender s = r.getNotificationSender();
	            s.setStatus(DictConsts.TIH_TAX_MSG_STATUS_1);
	            s.setUpdatedBy(currentUserName);
	            s.setUpdatedDatetime(updateDate);
	            this.em.merge(s);
	        }
	    }
	}
	
	/**
	 * <p>Description: 查找项目</p>
	 * @param typeId 项目编号
	 * @return
	 */
	private Projectmstr getProByTypeId(String typeId) {
		String jpql="select pro from Projectmstr pro where pro.code = '"+typeId+"'";
    	try {
    	    return (Projectmstr) this.em.createQuery(jpql).getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		
	}
	
	/**
	 * <p>Description: 得到所有定时邮件的接受人</p>
	 * @param jobInfo 定时器信息
	 * @return 接受人账号集合
	 */
	
	public List<NotificationReceiver> getTimedReceivers(JobInfo jobInfo){
		List<NotificationReceiver> distinctReceivers= new ArrayList<NotificationReceiver>();
		List<String> distinctNames = new ArrayList<String>();
		List<NotificationReceiver> receivers = this.getAllTimedReceivers(jobInfo);//得到所有未发送消息
		for (int i = 0; i < receivers.size(); i++) {//取到所未发送消息的没有重复的接受者
			NotificationReceiver r = receivers.get(i);
			String receiverAdAccount = r.getReceivedBy();
			if (distinctNames.contains(receiverAdAccount)) {
				continue;
			} else {
				distinctNames.add(receiverAdAccount);
				distinctReceivers.add(r);
			}
		}
		return distinctReceivers;
	}
	/**
	 * <p>Description: 得到定时任务的消息实体</p>
	 * @param receiverAdAccount 接受人登录帐号
	 * @param jobInfo 定时器信息
	 * @return 消息实体集合
	 */
	public List<NotificationReceiver> getTimedNotices(String receiverAdAccount,JobInfo jobInfo){
		Date startDate = jobInfo.getStartDate();
    	Date endDate = new Date();
    	
		StringBuilder jpql = new StringBuilder();
    	jpql.append("select r from NotificationReceiver r,NotificationSender s,Notificationmstr n,WfInstancemstr w");
    	jpql.append(" where r.notificationSender.id =s.id and s.notificationmstr.id=n.id and n.typeId = w.no");
    	jpql.append(" and s.sendOption = ?1 and s.status = ?2 and r.receivedBy = ?3 and n.createdDatetime between ?4 and ?5");
    	jpql.append(" and w.type in (select mc.type from WfMailConfig mc where mc.jobId = ?6)");
    	jpql.append(" order by r.receivedBy,w.type,n.typeId,s.sendDatetime");
    	logger.info("begin NotificationService.getTimedNotices()");
		Query query = this.em.createQuery(jpql.toString());
		query.setParameter(1, DictConsts.TIH_TAX_MSG_TYPE_1);
    	query.setParameter(2, DictConsts.TIH_TAX_MSG_STATUS_2);
    	query.setParameter(3, receiverAdAccount);
    	query.setParameter(4, startDate);
    	query.setParameter(5, endDate);
    	query.setParameter(6, jobInfo.getJobId());
    	logger.info("end NotificationService.getTimedNotices()");
    	return query.getResultList();
	}
	
	/**
	 * <p>Description: 根据实体Id查找实体</p>
	 * @param noticeId 消息实体Id
	 * @return 消息实体
	 */
	private NotificationReceiver getNoticeById(long receiverId){
	    return (NotificationReceiver) this.em.find(NotificationReceiver.class, receiverId);
	}
	
	/**
	 * <p>Description: 根据jobId查找JobInfo</p>
	 * @param id jobId
	 * @return 定时器实体
	 */
	public JobInfo getJobInfoByJobId(String jobId){
		String jpql="select job from JobInfo job where job.jobId = '"+jobId+"'";
    	try {
    	    return (JobInfo) this.em.createQuery(jpql).getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
    /**
     * <p>Description: 查找流程实体</p>
     * @param no 流程单号
     * @return 流程实体
     */
	private WfInstancemstr getWfByNo(String no) {
    	String jpql="select wf from WfInstancemstr wf where wf.no = '"+no+"'";
    	try {
    	    return (WfInstancemstr) this.em.createQuery(jpql).getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
    }
    
    
    /**
     * <p>Description: 查找所有定时任务的消息集合</p>
     * @param jobInfo 定时器
     * @return 消息集合
     */
	public List<NotificationReceiver> getAllTimedReceivers(JobInfo jobInfo){
    	Date startDate = jobInfo.getStartDate();
    	Date endDate = new Date();
    	logger.info("begin NotificationService.getTimedNotices()");
    	StringBuilder jpql = new StringBuilder();
    	jpql.append("select r from NotificationReceiver r,WfInstancemstr w");
    	jpql.append(" where r.notificationSender.notificationmstr.typeId = w.no");
    	jpql.append(" and r.notificationSender.sendOption = ?1 and r.notificationSender.status = ?2 and r.notificationSender.notificationmstr.createdDatetime between ?3 and ?4");
    	jpql.append(" and w.type in (select mc.type from WfMailConfig mc where mc.jobId = ?5)");
    	jpql.append(" order by r.receivedBy,w.submitDatetime desc,r.id,r.notificationSender.notificationmstr.id desc");
    	
    	Query query = this.em.createQuery(jpql.toString());
    	query.setParameter(1, DictConsts.TIH_TAX_MSG_TYPE_1);
    	query.setParameter(2, DictConsts.TIH_TAX_MSG_STATUS_2);
    	query.setParameter(3, startDate);
    	query.setParameter(4, endDate);
    	query.setParameter(5, jobInfo.getJobId());
    	List<NotificationReceiver> receivers = query.getResultList();
    	logger.info("end NotificationService.getTimedNotices():receivers:" + receivers.size());
		return receivers;
    }
    
    /**
     * <p>Description: 查找授权信息</p>
     * @param id 实体Id
     * @return 授权信息实体
     */
	private WfAuthorizmstr getAutById(long id){
	    return (WfAuthorizmstr) this.em.find(WfAuthorizmstr.class, id);
    }
    
    /**
     * <p>Description: 转化时间格式</p>
     * @param time
     * @return
     */
	private String formatDateFime(Date time) {
        if (time != null) { 
        	return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(time); 
        }
        return "";
    }
    
    
	public EmailVo getRealTimeEmailVo(long notificationReceiverId,String locale){
		EmailVo emailVo = new EmailVo();
		NotificationReceiver r = this.getNoticeById(notificationReceiverId);
		Notificationmstr n = r.getNotificationSender().getNotificationmstr();
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		NotificationVo noticeVo = new NotificationVo();
		if(n != null){
		    if (DictConsts.TIH_TAX_MSG_REFTYPE_1.equals(n.getType())) {
		        noticeVo = getNoticeVoByWfInstancemstr(n, locale);
		        logger.info("流程");
		    } else if (DictConsts.TIH_TAX_MSG_REFTYPE_2.equals(n.getType())) {// 项目
		        noticeVo = getNoticeVoByProject(n);
		        logger.info("项目");
		    } else if (DictConsts.TIH_TAX_MSG_REFTYPE_3.equals(n.getType())) {
		        noticeVo = getNoticeVoByAuthoriz(n, locale);
		        logger.info("授权");
		    } else if (DictConsts.TIH_TAX_MSG_REFTYPE_4.equals(n.getType())) {
		        noticeVo = getNoticeVoBySysNotice(n);
		        logger.info("通知");
		    }
		}
		noticeVos.add(noticeVo);
		emailVo.setSendDate((formatDateFime(new Date())));
		UsermstrVo usermstrVo = userCommonService.getUsermstrVoByAdAccount(r.getReceivedBy());
		emailVo.setReceiverName(usermstrVo==null?"adAccount":usermstrVo.getP().getNachn());
		emailVo.setNoticeVos(noticeVos);
		emailVo.setAppUrl(APP_URL);

		return emailVo;
	}
	
	public EmailVo getTimedEmailVo(String[] ids,String locale){
		
		EmailVo emailVo = new EmailVo();
		List<String> distinctTask = new ArrayList<String>();//distinct的定时待处理任务typeId
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		Date minDate = new Date();
		boolean hasQuestion = false;
		for (int i = 0; i < ids.length; i++) {
            Notificationmstr n = this.getReceiverById(Integer.parseInt(ids[i])).getNotificationSender().getNotificationmstr();
            NotificationVo noticeVo = getNoticeVoByWfInstancemstr(n,locale);
            //最小时间
            if(n.getCreatedDatetime().before(minDate)){
            	minDate=n.getCreatedDatetime();
            }
            
            if (distinctTask.contains(n.getTypeId())) {// 已经有的流程，那么就不显示subjectVo
                noticeVo.getSubjectVo().setDisplay(false);
            } else {
                hasQuestion = false;
                distinctTask.add(n.getTypeId());// 没有的流程加入到distinctTask
            }
            
            if(hasQuestion){
                List<NoticeDetailVo> noticeDetails = noticeVo.getNoticeDetails();
                if(noticeDetails != null && noticeDetails.size() > 0){
                    noticeVo.getNoticeDetails().get(0).setDisplay(false);
                }
            }
            
            hasQuestion = true;
            noticeVos.add(noticeVo);
		}
		
		emailVo.setSendDate(formatDateFime(minDate)+"~"+formatDateFime(new Date()));//最早时间~当前时间
		String receivedBy = this.getReceiverById(Integer.parseInt(ids[0])).getReceivedBy();
        UsermstrVo usermstrVo = userCommonService.getUsermstrVoByAdAccount(receivedBy);
        emailVo.setReceiverName(usermstrVo==null?receivedBy:usermstrVo.getP().getNachn());
        emailVo.setNoticeVos(noticeVos);
        emailVo.setAppUrl(APP_URL);
		return emailVo;
	}
	
	public NotificationReceiver getReceiverById(long id) {
	    return this.em.find(NotificationReceiver.class, id);
    }

    public SubjectVo getSubjectVoByWfInstancemstr(WfInstancemstr wf, String locale) {
        SubjectVo subjectVo = new SubjectVo();
        subjectVo.setLink(APP_URL + TASK_LINK + "?taskNumber=" + wf.getNo());
        subjectVo.setSubject(formatDateFime(wf.getSubmitDatetime()) + " " + commonService.getValueByDictCatKey(wf.getType(), locale));
        subjectVo.setImportant(maps.get(wf.getImportance()));
        subjectVo.setUrgent(maps.get(wf.getUrgency()));
        return subjectVo;
    }

    public NotificationVo getNoticeVoByWfInstancemstr(Notificationmstr n, String locale) {
        if(n.getTypeId() == null || "".endsWith(n.getTypeId())){
        	return null;
        }
        NotificationVo noticeVo = new NotificationVo();
        WfInstancemstr wf = this.getWfByNo(n.getTypeId());
        SubjectVo subjectVo = getSubjectVoByWfInstancemstr(wf, locale);
        noticeVo.setNotice(n);
        noticeVo.setSubjectVo(subjectVo);
        if (DictConsts.TIH_TAX_REQUESTFORM_3.equals(wf.getType())) {
            List<NoticeDetailVo> details = findNoticeExtByNotificationmstr(n,locale);
            if(details != null && details.size() > 0){
                noticeVo.setDetailsDisplay(true);
                noticeVo.setNoticeDetails(details);
            }
        }
        return noticeVo;
    }
    
    public NotificationVo getNoticeVoByProject(Notificationmstr n){
        if(n.getTypeId() == null || "".equals(n.getTypeId())){
        	return null;
        }
        NotificationVo noticeVo = new NotificationVo();
        noticeVo.setNotice(n);
        Projectmstr project = this.getProByTypeId(n.getTypeId());
        SubjectVo subjectVo = new SubjectVo();
        subjectVo.setLink(APP_URL+PRO_LINK + "?proNumber="+project.getCode());
        subjectVo.setSubject(formatDateFime(project.getCreatedDatetime())+" "+project.getName());
        noticeVo.setSubjectVo(subjectVo);
        return noticeVo;
    }
    
    public NotificationVo getNoticeVoByAuthoriz(Notificationmstr n, String locale){
        if(n.getTypeId() == null || "".equals(n.getTypeId())){
        	return null;
        }
        NotificationVo noticeVo = new NotificationVo();
        noticeVo.setNotice(n);
        WfAuthorizmstr aut = this.getAutById(Integer.parseInt(n.getTypeId()));
        SubjectVo subjectVo = new SubjectVo();
        subjectVo.setLink(APP_URL+TASK_LINK);
        subjectVo.setSubject(formatDateFime(aut.getCreatedDatetime())+" "+commonService.getValueByDictCatKey(aut.getType(),locale));
        noticeVo.setSubjectVo(subjectVo);
        return noticeVo;
    }
    
    public NotificationVo getNoticeVoBySysNotice(Notificationmstr n){
    	NotificationVo noticeVo = new NotificationVo();
    	noticeVo.setNotice(n);
    	SubjectVo subjectVo = new SubjectVo();
    	subjectVo.setLink("#");
    	subjectVo.setSubject(formatDateFime(n.getCreatedDatetime())+" "+ n.getTypeId());
    	noticeVo.setSubjectVo(subjectVo);
    	
    	return noticeVo;
    }
    
    
    /**
     * <p>Description: 根据消息查找邮件扩展表</p>
     * @param 消息实体
     * @return 扩展表集合
     */
    public List<NoticeDetailVo> findNoticeExtByNotificationmstr(Notificationmstr n, String locale){
        List<NoticeDetailVo> details = null;
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select ne from NotificationExt ne" );
        jpql.append(" where ne.notificationmstrId = " + n.getId());
        try {
            List<NotificationExt> nes = em.createQuery(jpql.toString()).getResultList();
            if(nes != null && nes.size() > 0){
                for (int i = 0; i < nes.size(); i++) {
                    details = findValueByNoticeExt(nes.get(i),locale);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return details;
    }
    
    public List<NoticeDetailVo> findValueByNoticeExt(NotificationExt noticeExt, String locale){
        List<NoticeDetailVo> details = new ArrayList<NoticeDetailVo>();
        String tableName = noticeExt.getTableName();
        long tableId = noticeExt.getTableId();
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select obj from " + tableName + " obj");
        jpql.append(" where obj.id = " + tableId);
        WfStepmstrProperty wfp = null;
        try {
            wfp =(WfStepmstrProperty) em.createQuery(jpql.toString()).getSingleResult();
            if(wfp != null ){
                WfStepmstr step = wfp.getWfStepmstr();
                List<WfStepmstrProperty> wsps = step.getWfInstancemstr().getWfStepmstrs().get(0).getWfStepmstrProperties();
                for (WfStepmstrProperty p : wsps) {
                    if(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION.equals(p.getName())){
                        NoticeDetailVo detail = new NoticeDetailVo();
                        detail.setDetailType("问题：");
                        detail.setValue(p.getValue());
                        details.add(detail);
                        break;
                    }
                }
                if(!DictConsts.TIH_TAX_APPROACH_1.equals(step.getDealMethod())){
                    NoticeDetailVo detail = new NoticeDetailVo();
                    detail.setDetailType(commonService.getValueByDictCatKey(step.getDealMethod(),locale)+"：");
                    detail.setValue(wfp.getValue());
                    details.add(detail);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return details;
    }
    
}
