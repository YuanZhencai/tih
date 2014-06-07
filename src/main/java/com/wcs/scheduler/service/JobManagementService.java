package com.wcs.scheduler.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.vo.JobInfoVo;
import com.wcs.tih.model.JobInfo;
import com.wcs.tih.model.WfMailConfig;

@Stateless
public class JobManagementService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;
	@EJB
	private LoginService loginService;
	@EJB
	private CommonService commonService;

	private TreeNode root;

	public List<WfMailConfig> getWfMailConfig(String type) {
		String sql = "select wf from WfMailConfig wf where wf.mailInd <> 'Y' and wf.type = '" + type + "' and wf.jobId is null";
		return em.createQuery(sql).getResultList();
	}

	public void deleteWfMailConfig(String type, String jobId) {
		String sql = "delete from WfMailConfig wf where wf.jobId = '" + jobId + "' and wf.type = '" + type + "' and wf.jobInd <> 'Y' ";
		em.createQuery(sql).executeUpdate();
	}

	public void saveWfMailConfig(String type, String jobId) {
		String user = loginService.getCurrentUsermstr().getAdAccount();
		WfMailConfig wfMailConfig = new WfMailConfig();
		wfMailConfig.setType(type);
		wfMailConfig.setMailInd("Y");
		wfMailConfig.setSysNoticeInd("Y");
		wfMailConfig.setCreatedBy(user);
		wfMailConfig.setCreatedDatetime(new Date());
		wfMailConfig.setUpdatedBy(user);
		wfMailConfig.setUpdatedDatetime(new Date());
		wfMailConfig.setJobInd("N");
		wfMailConfig.setJobId(jobId);
		em.persist(wfMailConfig);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteWfMailConfigAll(String jobId) {
		String sql = "delete from WfMailConfig wf where wf.jobId = '" + jobId + "' ";
		em.createQuery(sql).executeUpdate();
	}

	/**
	 * 通过jobId查找WfMailConfig表中数据
	 * 
	 * @param jobId
	 * @return
	 */
	public List<String> findWFmailByJobId(String jobId) {
		String sql = "select wfMail.type from WfMailConfig wfMail where wfMail.jobId = '" + jobId + "' and wfMail.jobInd <> 'Y' ";
		return em.createQuery(sql).getResultList();
	}

	public List<JobInfo> findJobByClass(String className) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select job from JobInfo job");
		jpql.append(" where (job.endDate is null or job.endDate > '" + sdf.format(new Date()) + "')");
		jpql.append(" and job.defunctInd <> 'Y'");
		if(className != null){
			jpql.append(" and job.jobClassName = '" + className + "'");
		}
		jpql.append(" order by job.updatedDatetime desc");
		List<JobInfo> resulJobtList = em.createQuery(jpql.toString()).getResultList();
		List<JobInfo> list = new ArrayList<JobInfo>(resulJobtList);
		for (JobInfo job : resulJobtList) { // 遍历所有resultList集合，找出: 年-月-日 时：分：秒 拼接的字符串，转换为时间类型，是当前时间进行比较；如果小于当前时间，移出resultList这个集合
			if (!"*".equals(job.getYear()) && !"*".equals(job.getMonth()) && !"*".equals(job.getDayOfMonth()) && !"*".equals(job.getHour())) {
				String str = job.getYear() + "-" + job.getMonth() + "-" + job.getDayOfMonth() + " " + job.getHour() + ":" + job.getMinute() + ":"
						+ job.getSecond(); // 拼接字符串： 年-月-日 时：分：秒
				Date parse;
				try {
					parse = sdf1.parse(str.replace("*", "00"));
					Date date2 = new Date();
					if (date2.getTime() > parse.getTime()) {
						list.remove(job);
					}
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		resulJobtList = list;
		
		return resulJobtList;
	}

	/**
	 * 当定时器重新加载时，从数据库找到所有可执行的数据
	 * 
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JobInfo> getJobListFromDatabase() {
		return findJobByClass(null);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JobInfo> findTimedEmailJobInfos() {
		return findJobByClass("java:module/BatchJobEmail");
	}

	
	/**
	 * 从数据库删除所有不可执行的数据
	 * 
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteJobFromUselessDatabase() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "select job from JobInfo job where (job.endDate is null or job.endDate > '" + sdf.format(new Date())
				+ "' ) and job.defunctInd <> 'Y' order by job.updatedDatetime desc";
		List<JobInfo> resulJobtList = em.createQuery(sql).getResultList();
		for (JobInfo job : resulJobtList) { // 遍历所有resultList集合，找出: 年-月-日 时：分：秒 拼接的字符串，转换为时间类型，是当前时间进行比较；如果小于当前时间，移出resultList这个集合
			if (!"*".equals(job.getYear()) && !"*".equals(job.getMonth()) && !"*".equals(job.getDayOfMonth()) && !"*".equals(job.getHour())) {
				String str = job.getYear() + "-" + job.getMonth() + "-" + job.getDayOfMonth() + " " + job.getHour() + ":" + job.getMinute() + ":"
						+ job.getSecond(); // 拼接字符串： 年-月-日 时：分：秒
				Date parse;
				try {
					parse = sdf1.parse(str.replace("*", "00"));
					Date date2 = new Date();
					if (date2.getTime() > parse.getTime()) {
						deleteJobInfo(job);
						deleteWfMailConfigAll(job.getJobId());
					}
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public void deleteJobInfo(JobInfo jobInfo) {
		String sql = "delete from JobInfo job where job.jobId = '" + jobInfo.getJobId() + "' ";
		em.createQuery(sql).executeUpdate();
	}

	public void saveJobInfo(JobInfo jobInfo, Boolean flag) {
		String user = loginService.getCurrentUsermstr().getAdAccount();
		jobInfo.setDefunctInd("N");
		jobInfo.setUpdatedBy(user);
		jobInfo.setUpdatedDatetime(new Date());
		if (flag) {
			jobInfo.setCreatedBy(user);
			jobInfo.setCreatedDatetime(new Date());
			em.persist(jobInfo);
		} else {
			em.merge(jobInfo);
		}
	}

	/**
	 * 通过job的ID查找job数据
	 * 
	 * @param jobId
	 *            job的ID
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<JobInfo> findJobInfoByJobId(String jobId) {
		String sql = "select job from JobInfo job where job.jobId = '" + jobId + "' and job.defunctInd <> 'Y' ";
		return em.createQuery(sql).getResultList();
	}

	/**
	 * 通过job名称查找job数据
	 * 
	 * @param jobName
	 *            job名称
	 * @return
	 */
	public List<String> getValidate(String jobName) {
		String sql = "select job from JobInfo job where job.jobName = '" + jobName + "'  and job.defunctInd <> 'Y' ";
		return em.createQuery(sql).getResultList();
	}

	/**
	 * 得到treetable中的数据
	 * 
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public TreeNode getCreateTree() {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		root = new DefaultTreeNode("root", null);
		List<JobInfo> jobList = findTimedEmailJobInfos(); // 从数据库中得到数据
		for (JobInfo t : jobList) { // 创建treetable
			JobInfoVo vo = new JobInfoVo();
			vo.setJobId(t.getJobId());
			vo.setJobName(t.getJobName());
			vo.setDescription(t.getDescription());
			vo.setStartDate(t.getStartDate());
			vo.setEndDate(t.getEndDate());
			vo.setNextTimeout(t.getNextTimeout());
			TreeNode documents = new DefaultTreeNode("job", vo, root);// 创建treetable主干
			List<String> wfMailList = findWFmailByJobId(t.getJobId());
			for (String wf : wfMailList) {
				JobInfoVo vo1 = new JobInfoVo();
				vo1.setJobId(t.getJobId());
				vo1.setJobName(commonService.getValueByDictCatKey(wf, browserLang.toString()));
				new DefaultTreeNode("job", vo1, documents);// 创建treetable子节点
			}
		}
		return root;
	}

	public List<Dict> getSource() {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		return commonService.getDictByCat(DictConsts.TIH_TAX_REQUESTFORM, browserLang.toString());
	}

	public List<String> getSource(List<String> list) {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		List<String> sourceList = new ArrayList<String>();
		List<Dict> source = commonService.getDictByCat(DictConsts.TIH_TAX_REQUESTFORM, browserLang.toString());

		for (Dict s : source) {
			sourceList.add(s.getCodeVal());
		}
		sourceList.removeAll(list);
		return sourceList;
	}

	/**
	 * <p>
	 * pickList中 已选数据
	 * </p>
	 * 
	 * @param jobId
	 * @return
	 */
	public List<String> gettemptarget(String jobId) {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		List<String> list = new ArrayList<String>();
		List<String> resultList = findWFmailByJobId(jobId);
		for (String str : resultList) {
			list.add(commonService.getValueByDictCatKey(str, browserLang.toString()));
		}
		return list;
	}

	public List<Dict> getDictByCodeVal(String val) {
		String sql = "select dict from Dict dict where dict.codeVal = '" + val + "' and dict.defunctInd <> 'Y' ";
		return em.createQuery(sql).getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateNextTime(JobInfo jobInfo) {
		em.merge(jobInfo);
	}

}
