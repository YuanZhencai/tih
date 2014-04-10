package com.wcs.tih.feedback.service;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.util.Id;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.common.model.Position;
import com.wcs.common.model.Positionorg;
import com.wcs.common.model.Userpositionorg;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.feedback.controller.vo.AttachmentVO;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.filenet.pe.service.DefaultWorkflowImpl;
import com.wcs.tih.interaction.service.SendReportService;
import com.wcs.tih.model.Filemstr;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.system.service.OrganizationLevelInterface;

import filenet.vw.api.VWException;

/**
 * Project: tih Description: 情况反馈Service Copyright (c) 2012 Wilmar Consultancy Services All Rights Reserved.
 * 
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@Stateless
public class FeedBackService implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private FileNetUploadDownload fud;
	@EJB
	private CEserviceLocal ceservice;
	@EJB
	private LoginService loginService;
	@EJB
	private DefaultWorkflowImpl workflow;
	@EJB
	private OrganizationLevelInterface organizationLevelInterface;
	@EJB
	private UserCommonService userCommonService;
	@EJB
	private TDSLocal tdsLocal;
	@EJB
	private SendReportService sendReportService;

	@PersistenceContext
	private EntityManager em;

	private static final String REQUESTFORM = DictConsts.TIH_TAX_REQUESTFORM_5;
	private static final String TIH_WORKFLOW_FEEDBACK_COMPANY = "TIH.WORKFLOW.FEEDBACK.COMPANY";
	// 发起人所属公司

	private ResourceBundle rb = ResourceBundle.getBundle("filenet");
	private final String password = "user.password";
	private final String classId = "ce.document.classid";
	private final String mission = "ce.folder.mission";

	private final String inspectation = "INVS_INSPECTATION";
	private final String antiAvoidance = "INVS_ANTI_AVOIDANCE";

	public void connect() throws Exception {
		ceservice.connect(tdsLocal.addPre(loginService.getCurrentUsermstr().getAdAccount()), rb.getString(password));
	}

	/**
	 * <p>
	 * Description: 获取文件流
	 * </p>
	 * 
	 * @param fileId
	 * @return 文件流
	 */
	public StreamedContent getFileByFileid(String fileId) throws Exception {
		try {
			return fud.downloadDocumentEncoding(fileId, "utf-8", "iso-8859-1");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * <p>
	 * Description: 上传附件
	 * </p>
	 * 
	 * @param is
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String uploadDocument(InputStream is, String fileName) throws Exception {
		Document doc = fud.upLoadDocumentCheckIn(is, new HashMap<String, Object>(), fileName, rb.getString(classId), rb.getString(mission));
		return doc.get_Id().toString();
	}

	/**
	 * <p>
	 * Description: 删除文档
	 * </p>
	 * 
	 * @param fileId
	 * @throws Exception
	 */
	public void deleteDocument(String fileId) throws Exception {
		try {
			connect();
			Document doc = Factory.Document.fetchInstance(ceservice.getStore(), new Id(fileId), null);
			doc.delete();
			doc.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("文件删除操作失败。请联系管理员");
		}

	}

	/**
	 * <p>
	 * Description: 取得上级主管
	 * </p>
	 * 
	 * @return
	 */
	public String getSuperadvisor(Long companyId) {
		UsermstrVo uv = organizationLevelInterface.getUsermstrVo(loginService.getCurrentUsermstr().getAdAccount(), REQUESTFORM, companyId);
		if (uv == null) {
			return null;
		}
		if (uv.getUsermstr() == null) {
			return null;
		}
		return uv.getUsermstr().getAdAccount();
	}

	/**
	 * <p>
	 * Description: 启动流程
	 * </p>
	 * 
	 * @param instanceProperty
	 *            流程信息
	 * @param stepProperty
	 *            附件信息
	 * @param workflowName
	 *            filenet上流程名称
	 * @param wfRemidVo
	 * @throws Exception
	 */
	public WfInstancemstr submitFeedBack(Map<String, String> instanceProperty, Map<String, String> stepProperty, String workflowName, String sendBy,
			String remarks, String importance, String urgency, List<String> selectedMovies, WfInstancemstr wfInstancemstr, WfRemindVo wfRemidVo)
			throws Exception {
		WfInstancemstr savedWfInstancemstr = null;
		try {
			if ("TihfeedbackWorkflow".equals(workflowName)) { // 工厂发起流程（通过流程名称判断）
				instanceProperty.put(TIH_WORKFLOW_FEEDBACK_COMPANY, selectedMovies.get(0));// 公司ID
				savedWfInstancemstr = workflow.createworkflow(instanceProperty, REQUESTFORM, workflowName, stepProperty,
						WorkflowConsts.WORK_FLOW_PARAM_SUPERVISOR, getSuperadvisor(Long.valueOf(selectedMovies.get(0))), remarks, importance,
						urgency, wfRemidVo);
			} else { // 集团发起流程
				instanceProperty.put(TIH_WORKFLOW_FEEDBACK_COMPANY, "");
				for (String companyId : selectedMovies) {
					instanceProperty.put(TIH_WORKFLOW_FEEDBACK_COMPANY, companyId);// 公司ID
					String situcp = getCompanyer("SITUCP", companyId);// 公司处理岗
					savedWfInstancemstr = workflow.createworkflow(instanceProperty, REQUESTFORM, workflowName, stepProperty,
							WorkflowConsts.PASSWORD_PARAM_COMPANY, situcp, remarks, importance, urgency, wfRemidVo);
				}
			}
			// 删除草稿流程
			if (wfInstancemstr != null) {
				if ("未启动".equals(wfInstancemstr.getNo())) {
					Long id = wfInstancemstr.getId();
					deleteFeedBack(id);
				}
			}
		} catch (VWException e) {
			throw new Exception("创建流程失败，请联系系统管理员。");
		}
		return savedWfInstancemstr;
	}

	public Long getWfId() {
		return workflow.getWfId();
	}

	/**
	 * <p>
	 * Description:流程下一步（根据流程单号启动下一步流程）
	 * </p>
	 * 
	 * @param paramMap
	 *            路由
	 * @param step
	 *            数据信息
	 * @param stepMap
	 *            上传到filenet上的文件信息
	 * @param workflownumber
	 *            流程单号
	 * @throws Exception
	 */
	public void doWorkflowNext(Map<String, Object> paramMap, WfStepmstr step, Map<String, String> stepMap, String workflownumber, String remarks)
			throws Exception {
		try {
			workflow.doDispath(paramMap, step, stepMap, workflownumber);
			this.updateRemarks(workflownumber, remarks);
		} catch (VWException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			throw new Exception("流程执行失败，请联系系统管理员。");
		}
	}

	public void updateRemarks(String no, String remarks) {
		String sql = "update WF_INSTANCEMSTR set REMARKS = '" + remarks + "' where NO = '" + no + "'";
		this.em.createNativeQuery(sql).executeUpdate();
	}

	/**
	 * <p>
	 * Description: 获取当前步骤名称
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCurrentNodeName(String workflowNo) throws Exception {
		try {
			return workflow.getWorkStepNameById(workflowNo);
		} catch (VWException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("获取当前节点名称错误");
		}
	}

	/**
	 * <p>
	 * Description: 通过Adaccount查找用户名称
	 * </p>
	 * 
	 * @param account
	 * @return
	 */
	public String getUsernameByAdaccount(String account) {
		UsermstrVo uv = userCommonService.getUsermstrVo(account);
		if (uv == null) {
			return account;
		}
		if (uv.getP() == null) {
			return account;
		}
		return uv.getP().getNachn();
	}

	public void doFinish(WfInstancemstr wfInstancemstr) {
		workflow.editWfInstance(wfInstancemstr);
	}

	/**
	 * <p>
	 * Description: 根据id查找公司名称
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	public String getCompanyNameById(String id) {
		String sql = "SELECT o FROM O o, Companymstr c " + "WHERE o.id=c.oid AND c.id=:id";
		List<O> os = em.createQuery(sql).setParameter("id", Long.valueOf(id)).getResultList();
		if (os.isEmpty()) {
			return "";
		}
		return os.get(0).getStext();
	}

	/**
	 * <p>
	 * Description: 取集团处理岗人员帐号
	 * </p>
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getGrouper(String code) throws Exception {
		String sql = "SELECT p FROM Position p WHERE p.code=:code AND p.defunctInd='N'";
		List<Position> ps = em.createQuery(sql).setParameter("code", code).getResultList();
		if (ps.isEmpty()) {
			throw new Exception("没有设置集团处理岗位信息。");
		}
		if (ps.size() > 1) {
			throw new Exception("存在多条集团处理岗位信息。");
		}
		Position p = ps.get(0);
		List<Positionorg> pos = p.getPositionorgs();
		Positionorg po = null;
		int times = 0;
		for (Positionorg p1 : pos) {
			if (p1.getDefunctInd().trim().equals("N")) {
				times++;
				po = p1;
			}
		}
		if (times == 0) {
			throw new Exception("集团处理岗位没有对应的公司。");
		}
		if (times > 1) {
			throw new Exception("集团处理岗对应多个公司。");
		}
		List<Userpositionorg> uos = po.getUserpositionorgs();
		times = 0;
		Userpositionorg uo = null;
		for (Userpositionorg u : uos) {
			if (u.getDefunctInd().trim().equals("N")) {
				times++;
				uo = u;
			}
		}
		if (times == 0) {
			throw new Exception("集团处理岗位没有对应的责任人。");
		}
		if (times > 1) {
			throw new Exception("集团处理岗位有多个责任人。");
		}
		return uo.getUsermstr().getAdAccount();
	}

	public String getCompanyer(String code, String companyId) throws Exception {
		String sql = "SELECT p FROM Position p WHERE p.code=:code AND p.defunctInd='N'";
		List<Position> ps = em.createQuery(sql).setParameter("code", code).getResultList();
		if (ps.isEmpty()) {
			throw new Exception("没有设置公司处理岗位信息。");
		}
		if (ps.size() > 1) {
			throw new Exception("存在多条公司处理岗位信息。");
		}
		Position p = ps.get(0);
		List<Positionorg> pos = p.getPositionorgs();
		int times = 0;
		for (Positionorg po : pos) {
			if (po.getDefunctInd().trim().equals("N")) {
				times++;
			}
		}
		if (times == 0) {
			throw new Exception("公司处理岗位没有对应的公司。");
		}
		sql = "SELECT o FROM O o, Companymstr c WHERE c.id=:id AND c.oid=o.id AND c.defunctInd='N'";
		List<O> os = em.createQuery(sql).setParameter("id", Long.valueOf(companyId)).getResultList();
		if (os.isEmpty()) {
			throw new Exception("流程发起公司不存在。");
		}
		O o = os.get(0);
		for (Positionorg po : pos) {
			if (po.getDefunctInd().trim().equals("Y")) {
				continue;
			}
			if (po.getOid().trim().equals(o.getId().trim())) {
				List<Userpositionorg> us = po.getUserpositionorgs();
				Userpositionorg userpositionorg = null;
				times = 0;
				for (Userpositionorg u : us) {
					if (u.getDefunctInd().trim().equals("N")) {
						times++;
						userpositionorg = u;
					}
				}
				if (times == 0) {
					throw new Exception("当前公司没有人负责公司处理岗。");
				}
				if (times > 1) {
					throw new Exception("当前公司有多人负责公司处理岗。");
				}
				return userpositionorg.getUsermstr().getAdAccount();
			}
		}
		throw new Exception("没有设置当前公司到公司处理岗。");
	}

	public Long saveOrUpdateDraft(Boolean draft, Map<String, String> instanceMap, Map<String, String> stepMap, WfInstancemstr wfInstancemstr,
			String remarks, String importance, String urgency) {
		Long wfId = null; // 流程ID
		if (draft) {
			wfId = updateFeedBack(instanceMap, stepMap, wfInstancemstr, remarks, importance, urgency);
		} else {
			wfId = saveFeedBack(instanceMap, stepMap, remarks, importance, urgency);
		}
		return wfId;
	}

	/**
	 * <p>
	 * Description: 保存情况反馈流程
	 * </p>
	 * 
	 * @param instanceMap
	 * @param stepMap
	 */
	public Long saveFeedBack(Map<String, String> instanceMap, Map<String, String> stepMap, String remarks, String importance, String urgency) {
		String user = loginService.getCurrentUsermstr().getAdAccount();
		Date d = new Date();
		WfInstancemstr wf = new WfInstancemstr();
		wf.setNo("未启动");
		wf.setType(REQUESTFORM);
		wf.setRequestBy(user);
		wf.setSubmitDatetime(d);
		wf.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_1);
		wf.setCreatedBy(user);
		wf.setCreatedDatetime(d);
		wf.setDefunctInd("N");
		wf.setUpdatedBy(user);
		wf.setUpdatedDatetime(d);
		wf.setRemarks(remarks);
		wf.setImportance(importance);
		wf.setUrgency(urgency);
		em.persist(wf);

		for (Entry<String, String> pro : instanceMap.entrySet()) {
			WfInstancemstrProperty p = new WfInstancemstrProperty();
			p.setName(pro.getKey());
			p.setValue(pro.getValue());
			p.setWfInstancemstr(wf);
			em.persist(p);
		}

		WfStepmstr step = new WfStepmstr();
		step.setFromStepId(0);
		step.setName("");
		step.setCode("");
		step.setChargedBy("");
		step.setCompletedDatetime(wf.getCreatedDatetime());
		step.setDealMethod(DictConsts.TIH_TAX_APPROACH_1);
		step.setCreatedBy(user);
		step.setCreatedDatetime(d);
		step.setDefunctInd("N");
		step.setUpdatedBy(user);
		step.setUpdatedDatetime(d);
		step.setWfInstancemstr(wf);
		em.persist(step);

		for (Entry<String, String> sp : stepMap.entrySet()) {
			WfStepmstrProperty p = new WfStepmstrProperty();
			p.setName(sp.getKey());
			p.setValue(sp.getValue());
			p.setWfStepmstr(step);
			em.persist(p);
		}

		return wf.getId();
	}

	public CompanyManagerModel getCMM(Long id) {
		String sql = "SELECT o,c FROM O o,Companymstr c WHERE o.id=c.oid AND c.id=:id";
		List<Object[]> rs = em.createQuery(sql).setParameter("id", id).getResultList();
		if (rs.isEmpty()) {
			return null;
		}
		O o = (O) rs.get(0)[0];
		Companymstr c = (Companymstr) rs.get(0)[1];
		CompanyManagerModel cmm = new CompanyManagerModel();
		cmm.setId(c.getId());
		cmm.setStext(o.getStext());
		cmm.setAddress(c.getAddress());
		cmm.setZipcode(c.getZipcode());
		cmm.setTelphone(c.getTelphone());
		cmm.setJgCode(o.getBukrs());
		cmm.setType(c.getType());
		cmm.setDesc(c.getDesc());
		cmm.setOid(c.getOid());

		return cmm;
	}

	public List<CompanyManagerModel> getCMMList(String companyId) {
		String[] str = companyId.split(",");
		List<CompanyManagerModel> cmmls = new ArrayList<CompanyManagerModel>();
		for (int i = 0; i < str.length; i++) {
			String sql = "SELECT o,c FROM O o,Companymstr c WHERE o.id=c.oid AND c.id=:id";
			List<Object[]> rs = em.createQuery(sql).setParameter("id", Long.valueOf(str[i])).getResultList();
			if (rs.isEmpty()) {
				return null;
			}
			O o = (O) rs.get(0)[0];
			Companymstr c = (Companymstr) rs.get(0)[1];
			CompanyManagerModel cmm = new CompanyManagerModel();
			cmm.setId(c.getId());
			cmm.setStext(o.getStext());
			cmm.setAddress(c.getAddress());
			cmm.setZipcode(c.getZipcode());
			cmm.setTelphone(c.getTelphone());
			cmm.setJgCode(o.getBukrs());
			cmm.setType(c.getType());
			cmm.setDesc(c.getDesc());
			cmm.setOid(c.getOid());
			cmmls.add(cmm);
		}
		return cmmls;
	}

	/**
	 * <p>
	 * 通过id查找数据，修改数据
	 * </p>
	 * 
	 * @param id
	 *            数据库中的id
	 * @param stage
	 *            更新的数据
	 */
	public void updateStage(Long id, String stage) {
		WfInstancemstrProperty property = em.find(WfInstancemstrProperty.class, id);
		property.setValue(stage);
		em.persist(property);
	}

	public Long updateFeedBack(Map<String, String> instanceMap, Map<String, String> stepMap, WfInstancemstr wfInstancemstr, String remarks,
			String importance, String urgency) {
		WfInstancemstr wfi = em.find(WfInstancemstr.class, wfInstancemstr.getId());
		wfi.setRemarks(remarks);
		wfi.setImportance(importance);
		wfi.setUrgency(urgency);
		em.persist(wfi);

		List<WfInstancemstrProperty> properties = wfi.getWfInstancemstrProperties();
		int i = 0;
		for (Entry<String, String> pro : instanceMap.entrySet()) {
			WfInstancemstrProperty p = properties.get(i);
			p.setName(pro.getKey());
			p.setValue(pro.getValue());
			em.merge(p);
			i++;
		}
		for (WfStepmstrProperty p : wfi.getWfStepmstrs().get(0).getWfStepmstrProperties()) {
			for (Entry<String, String> sp : stepMap.entrySet()) {
				if (p.getName().equals(sp.getKey())) {
					p.setValue(sp.getValue());
					em.merge(p);
				}
			}
		}
		return wfi.getId();
	}

	public void deleteFeedBack(Long id) {
		WfInstancemstr wfi = em.find(WfInstancemstr.class, id);

		List<WfInstancemstrProperty> properties = null;
		if (wfi.getWfInstancemstrProperties() != null) {
			properties = wfi.getWfInstancemstrProperties();
		} else {
			properties = sendReportService.getwfips(id);
		}

		for (WfInstancemstrProperty p : properties) {
			em.remove(p);
		}

		List<WfStepmstr> wfsteps = null;
		if (wfi.getWfStepmstrs() != null) {
			wfsteps = wfi.getWfStepmstrs();
		} else {
			wfsteps = this.sendReportService.getwfstepmatrs(id);
		}

		for (WfStepmstr s : wfsteps) {

			List<WfStepmstrProperty> wfstepsProperty = null;
			if (s.getWfStepmstrProperties() != null) {
				wfstepsProperty = s.getWfStepmstrProperties();
			} else {
				wfstepsProperty = sendReportService.getWfStepProperty(s.getId());
			}

			for (WfStepmstrProperty p : wfstepsProperty) {
				em.remove(p);
			}
			em.remove(s);
		}
		em.remove(wfi);
	}

	public void saveFilemstr(AttachmentVO attachmentVO, String type, String defunctInd, Long entityId) throws Exception {
		Filemstr filemstr = new Filemstr();
		if (DictConsts.TIH_TAX_REQUESTFORM_5_2.equals(type)) {
			filemstr.setTableName(inspectation);
		} else {
			filemstr.setTableName(antiAvoidance);
		}
		filemstr.setFnId(attachmentVO.getFileId());
		filemstr.setName(attachmentVO.getFileName());
		filemstr.setEntityId(entityId);
		filemstr.setDefunctInd(defunctInd);
		filemstr.setCreatedBy(loginService.getCurrentUserName());
		filemstr.setCreatedDatetime(new Date());
		filemstr.setUpdatedBy(loginService.getCurrentUserName());
		filemstr.setUpdatedDatetime(new Date());
		em.persist(filemstr);
	}

	public void updateFilemstr(String fnId, String defunctInd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String sql = "update Filemstr f set f.defunctInd = '" + defunctInd + "',f.updatedDatetime = '" + sdf.format(new Date())
				+ "' where f.fnId = '" + fnId + "' ";
		em.createQuery(sql).executeUpdate();
	}

	public List<String> selectFilemstrByFnId(String fnIds) {
		String sql = "select f.fnId from Filemstr f where f.fnId in (" + fnIds + ") and f.defunctInd <> 'Y'";
		return em.createQuery(sql).getResultList();
	}

	public void deleteFilemstr(Long wfId) {
		String sql = "delete from Filemstr f where f.entityId = " + wfId + "";
		em.createQuery(sql).executeUpdate();
	}
}
