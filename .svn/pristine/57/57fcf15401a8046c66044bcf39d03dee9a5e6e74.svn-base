package com.wcs.tih.transaction.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.service.CommonService;
import com.wcs.common.service.NoticeService;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.model.ProjectAttachment;
import com.wcs.tih.model.ProjectMembermstr;
import com.wcs.tih.model.ProjectMissionmstr;
import com.wcs.tih.model.ProjectProblemmstr;
import com.wcs.tih.model.Projectmstr;
import com.wcs.tih.transaction.controller.vo.AttachmentVO;
import com.wcs.tih.transaction.controller.vo.ProblemVO;
import com.wcs.tih.transaction.controller.vo.ProjectMemberVO;
import com.wcs.tih.transaction.controller.vo.ProjectVO;
import com.wcs.tih.util.DateUtil;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class ProjectService {
    private static final String TIH_SUBJECT = "[益海嘉里]税务信息平台-";
    private Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceContext 
    private EntityManager em;
    @EJB 
    private LoginService loginService;
    @EJB 
    private UserCommonService commonService;
    @EJB 
    private CEserviceLocal ceservice;
    @EJB 
    private FileNetUploadDownload fud;
    @EJB
    private NoticeService noticeService;
    @EJB 
    private CommonService commService;
    @EJB 
    private TDSLocal tdsLocal;
    
    private ResourceBundle role = ResourceBundle.getBundle("roles");
    private String projectAdmin = "projectadmins";

    // 资源
    private ResourceBundle rb = ResourceBundle.getBundle("filenet");
    private final String username = "admin.id";
    private final String password = "admin.password";
    private final String classid  = "ce.document.classid";
    private final String projectFolder = "ce.folder.project";

    public ProjectService() { }
    
    @SuppressWarnings("unchecked")
    public List<ProjectVO> searchProjects(Map<String, Object> conditions) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT NEW com.wcs.tih.transaction.controller.vo.ProjectVO(po) " +
        		"FROM Projectmstr po " +
        		"WHERE po.defunctInd='N' AND po.code LIKE :code AND po.name LIKE :name " +
        		"AND po.status LIKE :status ");
        sql.append(loginService.hasRole(role.getString(projectAdmin)) ? "" : "AND EXISTS(SELECT 1 FROM ProjectMembermstr m WHERE m.projectmstr=po AND m.member LIKE :member) ");
        
        Date sd = (Date) conditions.get("qstartDate");
        sql.append(sd!=null && !"".equals(sd) ? "AND po.startDate >= :startDate " : "");
        Date cd = (Date) conditions.get("qcloseDate");
        sql.append(cd!=null && !"".equals(cd) ? "AND po.closeDate <= :closeDate " : "");
        sql.append("ORDER BY po.id DESC");
        
        Query query = em.createQuery(sql.toString());
        String code = (String) conditions.get("qcode");
        query.setParameter("code", (code != null && !"".equals(code.trim()) ? "%" + code.trim() + "%" : "%"));
        
        String name = (String) conditions.get("qname");
        query.setParameter("name", (name != null && !"".equals(name.trim()) ? "%" + name.trim() + "%" : "%"));
        
        String status = (String) conditions.get("qstatus");
        query.setParameter("status", (status != null && !"".equals(status) ? status : "%"));
        
        if(!loginService.hasRole(role.getString(projectAdmin))) {    // 限定只有自己参与的项目才会被查询出来
            query.setParameter("member", loginService.getCurrentUsermstr().getAdAccount());
        }
        
        if(sd!=null && !"".equals(sd)) {
            query.setParameter("startDate", DateUtil.getCurrentDate(sd));
        }
        if(cd!=null && !"".equals(cd)) {
            query.setParameter("closeDate", DateUtil.getCurrentDate(cd));
        }

        List<ProjectVO> ls = query.getResultList();
        em.clear();
        
        return ls;
    }
    
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long getProjectsNumByDay() {
        if(loginService.getCurrentUsermstr() == null
                || loginService.getCurrentUsermstr().getAdAccount().equals("")) {
            return 0l;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = null;
        try {
            today = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        String sql = "SELECT COUNT(p) FROM Projectmstr p " +
        		"WHERE p.createdDatetime >= :date AND p.pmId = :pmId";
        return (Long) em.createQuery(sql).setParameter("date", today)
                    .setParameter("pmId", loginService.getCurrentUsermstr().getAdAccount())
                    .getResultList().get(0);
    }
    
    public String getUsernameByAccount(String account) {
        return commonService.getUsermstrVo(account).getP().getNachn();
    }
    
    public void addProject(Projectmstr project) {
        em.persist(project);
    }
    
    public void editProject(Projectmstr project, String lang) {
        // 若状态改变，通知所有人
        Projectmstr p = em.find(Projectmstr.class, project.getId());
        String oldStatus = p == null ? "" : p.getStatus();
        String newStatus = project.getStatus();
        if(!"".equals(oldStatus) && !oldStatus.equals(newStatus)) {
            String sql = "SELECT p.member FROM ProjectMembermstr p WHERE p.projectmstr.id = :id AND p.defunctInd = 'N'";
            List<String> members = em.createQuery(sql).setParameter("id", project.getId()).getResultList();
            String subject = TIH_SUBJECT + project.getName() + "项目情况变化通知";
            String body = project.getName() + "项目的项目阶段已调整为" +
                    commService.getValueByDictCatKey(project.getStatus(), lang);
            sendNotices(members,project.getCode(), subject, body);
        }
        em.merge(project);
    }
    /**
     * <p>Description: 发送邮件或消息</p>
     * @param members
     * @param subject
     * @param body
     */
    public void sendNotices(List<String> members,String typeId, String subject, String body) {
        NotificationVo noticeVo = new NotificationVo();
        noticeVo.setReceiverList(members);
        noticeVo.setTypeId(typeId);
        noticeVo.setTitle(subject);
        noticeVo.setContent(body);
        noticeService.sendNoticeForProject(noticeVo);
    }
    
    // search members
    @SuppressWarnings("unchecked")
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ProjectMemberVO> getMembers(Projectmstr project) {
        StringBuilder sql = new StringBuilder(
                "SELECT NEW com.wcs.tih.transaction.controller.vo.ProjectMemberVO(m) " +
        		"FROM ProjectMembermstr m,Projectmstr j " +
        		"WHERE j.id=:id AND j=m.projectmstr AND m.defunctInd='N' " +
        		"ORDER BY m.id");
        return em.createQuery(sql.toString()).setParameter("id", project.getId()).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public void validateRepeat(String account,Long id) throws Exception{
    	String sql = "SELECT m FROM ProjectMembermstr m WHERE m.projectmstr.id=:pid AND m.member=:member AND m.defunctInd='N'";
        List<ProjectMembermstr> ls = em.createQuery(sql)
                .setParameter("pid", id)
                .setParameter("member", account)
                .getResultList();
        if(ls.size() > 0) {
            throw new Exception("该干系人已经存在，不可重复添加。");
        }
    }
    
    // 增加干系人
    public void addMember(ProjectMembermstr m){
        em.persist(m);
        String subject = TIH_SUBJECT + m.getProjectmstr().getName() + "项目创建通知";
        String body = "项目经理"+getUsernameByAccount(m.getProjectmstr().getCreatedBy())+"创建了“"+m.getProjectmstr().getName() +"”项目，您被项目经理设置为此项目的干系人。";
        List<String> members = new ArrayList<String>();
        members.add(m.getMember());
        sendNotices(members,m.getProjectmstr().getCode(), subject, body);
    }
    
    public void editMember(ProjectMembermstr m) {
        em.merge(m);
    }
    
    public void deleteMembers(ProjectMemberVO[] vs) throws Exception {
        String user = loginService.getCurrentUserName();
        Date date = new Date();
        for(ProjectMemberVO vo : vs) {
            ProjectMembermstr m = vo.getM();
            m.setUpdatedBy(user);
            m.setUpdatedDatetime(date);
            m.setDefunctInd("Y");
            editMember(m);
        }
    }

    // Task
    public void addTask(ProjectMissionmstr task, String lang) throws Exception {
        em.persist(task);

        sendTaskNotices(task, commService.getValueByDictCatKey(task.getStatus(), lang));
    }
    
    public void editTask(ProjectMissionmstr task, String lang) throws Exception {
        ProjectMissionmstr old = em.find(ProjectMissionmstr.class, task.getId());
        task.setChargedBy(old.getChargedBy());
        // 状态改变，邮件、消息通知
        if(! old.getStatus().equals(task.getStatus())) {
            sendTaskNotices(old, commService.getValueByDictCatKey(task.getStatus(), lang));
        }
        em.merge(task);
    }
    
    private void sendTaskNotices(ProjectMissionmstr task, String status) {
        List<String> members = new ArrayList<String>();
        members.add(task.getChargedBy());
        members.add(task.getProjectmstr().getCreatedBy());
        String subject = TIH_SUBJECT + task.getProjectmstr().getName() + "项目情况变化通知";
        String body = task.getProjectmstr().getName() + "项目的" + task.getName() + "任务的状态变化为" + status + "。";
        sendNotices(members,task.getProjectmstr().getCode(), subject, body);
    }
    
    public void deleteTask(ProjectMissionmstr task) throws Exception {
        task.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
        task.setUpdatedDatetime(new Date());
        task.setDefunctInd("Y");
        em.merge(task);
    }
    
    //Problem
    @SuppressWarnings("unchecked")
    public List<ProblemVO> searchProblems(ProjectMissionmstr task) {
        String sql = "SELECT NEW com.wcs.tih.transaction.controller.vo.ProblemVO(po) " +
        		"FROM ProjectProblemmstr po, ProjectMissionmstr m " +
        		"WHERE po.projectMissionmstr=m AND po.defunctInd='N' AND m.id=:id  " +
        		"ORDER BY po.id";
        return em.createQuery(sql).setParameter("id", task.getId()).getResultList();
    }
    public void addProblem(ProjectProblemmstr pb, String lang) {
        em.persist(pb);
        sendProblemNotices(pb, commService.getValueByDictCatKey(pb.getStatus(), lang));
    }
    public void editProblem(ProjectProblemmstr pb, String lang) {
        ProjectProblemmstr old = em.find(ProjectProblemmstr.class, pb.getId());
        if(!old.getStatus().equals(pb.getStatus())) {
            // 状态改变，邮件通知
            sendProblemNotices(old, commService.getValueByDictCatKey(pb.getStatus(), lang));
        }
        em.merge(pb);
    }
    private void sendProblemNotices(ProjectProblemmstr pb, String status) {
        List<String> members = new ArrayList<String>();
        members.add(pb.getSolvedBy());
        members.add(pb.getProjectMissionmstr().getChargedBy());
        members.add(pb.getProjectMissionmstr().getProjectmstr().getCreatedBy());
        String subject = TIH_SUBJECT + pb.getProjectMissionmstr().getProjectmstr().getName() + "项目情况变化通知";
        String body = pb.getProjectMissionmstr().getProjectmstr().getName() + 
                "项目的" + pb.getProjectMissionmstr().getName() + "任务中" + pb.getDesc() + "问题的状态变化为" + status + "。";
        sendNotices(members,pb.getProjectMissionmstr().getProjectmstr().getCode(), subject, body);
    }
    
    public void deleteProblems(ProblemVO[] pms) throws Exception {
        String name = loginService.getCurrentUserName();
        Date date = new Date();
        for(ProblemVO pm : pms) {
            ProjectProblemmstr p = pm.getProblem();
            p.setUpdatedBy(name);
            p.setUpdatedDatetime(date);
            p.setDefunctInd("Y");
            em.merge(p);
        }
    }

    // Attachment
    public List<AttachmentVO> searchProjectAttachments(Long typeId, String type) {
        String sql = "SELECT NEW com.wcs.tih.transaction.controller.vo.AttachmentVO(p) " +
        		"FROM ProjectAttachment p " +
        		"WHERE p.defunctInd = 'N' AND p.typeId=:typeId AND p.type=:type " +
        		"ORDER BY p.id";
        return em.createQuery(sql).setParameter("typeId", typeId)
                .setParameter("type", type).getResultList();
    }
    public void addAttach(ProjectAttachment pa) {
        em.persist(pa);
    }
    public void editAttach(ProjectAttachment pa) {
        em.merge(pa);
    }
    public boolean isAttachNameExist(Long typeId, String type, String name) {
        String sql = "SELECT p FROM ProjectAttachment p " +
        		"WHERE p.typeId=:typeId AND p.type=:type AND p.name=:name AND p.defunctInd<>'Y'";
        List ls = em.createQuery(sql).setParameter("typeId", typeId)
                .setParameter("type", type).setParameter("name", name).getResultList();
        return ls == null || ls.size() == 0 ? false : true;
    }
    
    /**
     * <p>Description: 上传项目附件</p>
     * @param is
     * @param fileName
     * @param classid
     * @param projectFolder
     * @return
     * @throws Exception
     */
    public String uploadAttachments(InputStream is, String fileName) throws Exception {
        Document doc = fud.upLoadDocumentCheckIn(is, new HashMap<String, Object>(), 
                fileName, rb.getString(classid), rb.getString(projectFolder));
        return doc.get_Id().toString();
    }
    /**
     * <p>Description: 获取文件流</p>
     * @param fileid
     * @return
     * @throws Exception
     */
    public StreamedContent downloadAttachment(String fileid) throws Exception {
        return fud.downloadDocumentEncoding(fileid, "utf-8", "iso8859-1");
    }
    /**
     * <p>Description: 删除附件</p>
     * @param selectedAttachments
     * @throws Exception
     */
    public void deleteAtts(AttachmentVO[] selectedAttachments) throws Exception {
        ceservice.connect(tdsLocal.addPre(rb.getString(username)), rb.getString(password));
        for(AttachmentVO avo : selectedAttachments) {
            Document d = ceservice.getDocument(avo.getAttachment().getFilemstrId());
            d.delete();
            d.save(RefreshMode.REFRESH);
            deleteAttachment(avo.getAttachment());
        }
    }
    public void deleteAttachment(ProjectAttachment p) {
        p.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
        p.setUpdatedDatetime(new Date());
        p.setDefunctInd("Y");
        editAttach(p);
    }
    /**
     * <p>Description: 删除单个附件</p>
     * @param att
     * @throws Exception 
     */
    public void deleteAtt(AttachmentVO att) throws Exception {
        ProjectAttachment pa = att.getAttachment();
        ceservice.connect(tdsLocal.addPre(rb.getString(username)), rb.getString(password));
        Document d = ceservice.getDocument(pa.getFilemstrId());
        d.delete();
        d.save(RefreshMode.REFRESH);

        deleteAttachment(pa);
    }
    
    public ProjectMembermstr findMemberById(Long id) {
        return em.find(ProjectMembermstr.class, id);
    }
    
    public void findProjectMissions(ProjectVO po){
        Projectmstr p = po.getP();
        StringBuffer jpql = new StringBuffer();
        jpql.append(" select pm from ProjectMissionmstr pm");
        jpql.append(" where pm.projectmstr.id ="+p.getId());
        jpql.append(" and pm.defunctInd <> 'Y'");
        try {
            List<ProjectMissionmstr> pms = em.createQuery(jpql.toString()).getResultList();
            if(pms != null&& pms.size()>0){
                po.setPms(pms);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}