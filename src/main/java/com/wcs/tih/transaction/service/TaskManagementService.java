package com.wcs.tih.transaction.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.Dict;
import com.wcs.common.model.Rolemstr;
import com.wcs.common.service.CommonService;
import com.wcs.common.service.NoticeService;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.util.DateUtils;
import com.wcs.tih.consts.TaskConsts;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.filenet.pe.service.WorkflowService;
import com.wcs.tih.homepage.contronller.vo.NoticeVo;
import com.wcs.tih.model.NotificationReceiver;
import com.wcs.tih.model.NotificationSender;
import com.wcs.tih.model.Notificationmstr;
import com.wcs.tih.model.WfAuthorizmstr;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfMailConfig;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.transaction.controller.vo.AuthorizmstrSearchVo;
import com.wcs.tih.transaction.controller.vo.AuthorizmstrVo;
import com.wcs.tih.transaction.controller.vo.EmailInfo;
import com.wcs.tih.transaction.controller.vo.MailConfigVo;
import com.wcs.tih.transaction.controller.vo.TaskSearchVo;
import com.wcs.tih.transaction.controller.vo.TaskTreeNodeVo;
import com.wcs.tih.transaction.controller.vo.WfAuthorizmstrVo;
import com.wcs.tih.transaction.controller.vo.WfInstancemstrVo;
import com.wcs.tih.util.DateUtil;

import filenet.vw.api.VWException;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 任务
 * </p>
 * <p>
 * Copyright (c) 2012 Wilmar Consultancy Services
 * </p>
 * <p>
 * All Rights Reserved.
 * </p>
 * 
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class TaskManagementService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;
	@EJB
	private LoginService loginService;
	@EJB
	private CommonService commonService;
	@EJB
	private UserCommonService userCommonService;
	@EJB
	private WorkflowService workflowService;
	@EJB
	private NoticeService noticeService;

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

	/**
	 * <p>
	 * Description: 创建任务树
	 * </p>
	 * 
	 * @param isTaskadmins
	 *            是否是任务管理员
	 * @param taskNumber
	 *            任务号
	 * @return
	 */
	public TreeNode createTree(String browserLang, boolean isTaskadmins) {
		TreeNode taskTreeRoot = new DefaultTreeNode("root", null);
		List<Dict> dicts = commonService.getDictByCat(DictConsts.TIH_TAX_REQUESTFORM, browserLang);
		TreeNode sqdTreeNode = new DefaultTreeNode(new TaskTreeNodeVo(TaskConsts.TASK_NODE_CREATE_TASK, "Folder", 0, ""), taskTreeRoot);
		String documentName = "document";
		String documentType = "Word Document";
		if (dicts != null && dicts.size() != 0) {
			for (Dict d : dicts) {
				if (!(d.getCodeCat() + "." + d.getCodeKey()).equals(DictConsts.TIH_TAX_REQUESTFORM_1)
						&& !(d.getCodeCat() + "." + d.getCodeKey()).equals(DictConsts.TIH_TAX_REQUESTFORM_2)) {
					new DefaultTreeNode(documentName, new TaskTreeNodeVo(d.getCodeVal(), documentType, 0, d.getCodeCat() + "." + d.getCodeKey()),
							sqdTreeNode);
				}
			}
		}
		new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_DRAFT, documentType, getMyDraftCount(), ""), taskTreeRoot);
		new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_APPLICATION, documentType, 0, ""), taskTreeRoot);
		new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_WAIT_DEALWITH_TASK, documentType, getMyWaitTaskCount(), ""),
				taskTreeRoot);
		new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_HADBEEN_DEALWITH_TASK, documentType, 0, ""), taskTreeRoot);
		new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MY_AUTHORIZED, documentType, 0, ""), taskTreeRoot);
		if (isTaskadmins) {
			new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_ALL_TASK, documentType, 0, ""), taskTreeRoot);

			DefaultTreeNode mailSetNode = new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MAIL_SET, documentType, 0, ""),
					taskTreeRoot);
			new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MAIL_TIMED, documentType, 0, ""), mailSetNode);
			new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MAIL_TIMEOUT, documentType, 0, ""), mailSetNode);
			new DefaultTreeNode(documentName, new TaskTreeNodeVo(TaskConsts.TASK_NODE_MAIL_INFO, documentType, 0, ""), mailSetNode);

		}
		return taskTreeRoot;
	}

	/**
	 * <p>
	 * Description: 我的草稿
	 * </p>
	 * 
	 * @param wfInstancemstrFormItemsVo
	 *            查询条件VO
	 * @return
	 */
	public List<WfInstancemstrVo> getMyDraft(String browserLang, TaskSearchVo wfInstancemstrFormItemsVo) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct wfi from WfInstancemstr wfi,WfInstancemstrProperty wfip join fetch wfi.wfInstancemstrProperties join fetch wfi.wfStepmstrs where wfi.defunctInd <> 'Y' and wfi.status='"
				+ DictConsts.TIH_TAX_WORKFLOWSTATUS_1
				+ "' and wfi.requestBy='"
				+ this.loginService.getCurrentUserName()
				+ "' and wfi.id = wfip.wfInstancemstr.id ");
		return getWfInstancemstrVo(browserLang, sb, wfInstancemstrFormItemsVo);
	}

	/**
	 * <p>
	 * Description: 我的申请
	 * </p>
	 * 
	 * @param wfInstancemstrFormItemsVo
	 *            查询条件VO
	 * @return
	 */
	public List<WfInstancemstrVo> getMyApplication(String browserLang, TaskSearchVo wfInstancemstrFormItemsVo) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct wfi from WfInstancemstr wfi,WfInstancemstrProperty wfip join fetch wfi.wfInstancemstrProperties join fetch wfi.wfStepmstrs where wfi.defunctInd <> 'Y' and wfi.status <> '"
				+ DictConsts.TIH_TAX_WORKFLOWSTATUS_1
				+ "' and wfi.requestBy='"
				+ this.loginService.getCurrentUserName()
				+ "' and wfi.id = wfip.wfInstancemstr.id ");
		return getWfInstancemstrVo(browserLang, sb, wfInstancemstrFormItemsVo);
	}

	/**
	 * <p>
	 * Description: 我的待办
	 * </p>
	 * 
	 * @param wfInstancemstrFormItemsVo
	 *            查询条件VO
	 * @return
	 */
	public List<WfInstancemstrVo> getMyWaitTask(String browserLang, TaskSearchVo wfInstancemstrFormItemsVo) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct step from WfStepmstr step,WfInstancemstr wfi ,WfInstancemstrProperty wfip join fetch wfi.wfInstancemstrProperties join fetch wfi.wfStepmstrs where step.wfInstancemstr.id = wfi.id and (step.chargedBy ='"
				+ this.loginService.getCurrentUserName()
				+ "' or step.chargedBy like '%,"
				+ this.loginService.getCurrentUserName()
				+ "%' or step.chargedBy like '%" + this.loginService.getCurrentUserName() + ",%') ");
		sb.append(" and step.id in(select max(s.id) from WfStepmstr s where s.defunctInd <> 'Y' group by s.wfInstancemstr.id)");
		sb.append(" and wfi.defunctInd <> 'Y' and step.defunctInd <> 'Y' and wfi.id = wfip.wfInstancemstr.id and wfi.id = step.wfInstancemstr.id ");
		if (wfInstancemstrFormItemsVo != null && (wfInstancemstrFormItemsVo.getStatus() == null || "".equals(wfInstancemstrFormItemsVo.getStatus()))) {
			wfInstancemstrFormItemsVo.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_2);
		} else if (wfInstancemstrFormItemsVo == null) {
			wfInstancemstrFormItemsVo = new TaskSearchVo();
			wfInstancemstrFormItemsVo.setStatus(DictConsts.TIH_TAX_WORKFLOWSTATUS_2);
		} else if (wfInstancemstrFormItemsVo != null && !DictConsts.TIH_TAX_WORKFLOWSTATUS_2.equals(wfInstancemstrFormItemsVo.getStatus())) {
			return null;
		}
		return getHandledData(browserLang, sb, wfInstancemstrFormItemsVo);
	}

	/**
	 * <p>
	 * Description: 我的已办
	 * </p>
	 * 
	 * @param wfInstancemstrFormItemsVo
	 *            查询条件VO
	 * @return
	 */
	public List<WfInstancemstrVo> getMyHandledTask(String browserLang, TaskSearchVo wfInstancemstrFormItemsVo) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct wfi from WfStepmstr step,WfInstancemstr wfi,WfInstancemstrProperty wfip join fetch wfi.wfInstancemstrProperties join fetch wfi.wfStepmstrs where step.wfInstancemstr.id = wfi.id and (step.chargedBy ='"
				+ this.loginService.getCurrentUserName()
				+ "' or step.chargedBy like '%,"
				+ this.loginService.getCurrentUserName()
				+ "%' or step.chargedBy like '%" + this.loginService.getCurrentUserName() + ",%') ");
		sb.append(" and step.id not in(select max(s.id) from WfStepmstr s where s.defunctInd <> 'Y' group by s.wfInstancemstr.id)");
		sb.append(" and wfi.defunctInd <> 'Y' and step.defunctInd <> 'Y' and wfi.id = wfip.wfInstancemstr.id and wfi.id = step.wfInstancemstr.id");
		if (wfInstancemstrFormItemsVo != null && DictConsts.TIH_TAX_WORKFLOWSTATUS_1.equals(wfInstancemstrFormItemsVo.getStatus())) {
			return null;
		}
		return getWfInstancemstrVo(browserLang, sb, wfInstancemstrFormItemsVo);
	}

	/**
	 * <p>
	 * Description: 所有任务
	 * </p>
	 * 
	 * @param wfInstancemstrFormItemsVo
	 *            查询条件VO
	 * @return
	 */
	public List<WfInstancemstrVo> getAllTask(String browserLang, TaskSearchVo wfInstancemstrFormItemsVo) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct wfi from WfInstancemstr wfi,WfInstancemstrProperty wfip join fetch wfi.wfInstancemstrProperties join fetch wfi.wfStepmstrs where wfi.defunctInd <> 'Y' and wfi.status <> '"
				+ DictConsts.TIH_TAX_WORKFLOWSTATUS_1 + "' and wfi.id = wfip.wfInstancemstr.id");
		return getWfInstancemstrVo(browserLang, sb, wfInstancemstrFormItemsVo);
	}

	private List<WfInstancemstrVo> getHandledData(String browserLang, StringBuffer sb, TaskSearchVo wfInstancemstrFormItemsVo) {
		logger.debug("browserLang:" + browserLang);
		if (wfInstancemstrFormItemsVo != null) {
			if (wfInstancemstrFormItemsVo.getTaskNumber() != null && !"".equals(wfInstancemstrFormItemsVo.getTaskNumber().trim())) {
				sb.append(" and wfi.no like '%" + wfInstancemstrFormItemsVo.getTaskNumber().trim() + "%'");
			}
			if (wfInstancemstrFormItemsVo.getTaskName() != null && !"".equals(wfInstancemstrFormItemsVo.getTaskName())) {
				sb.append(" and wfi.type =  '" + wfInstancemstrFormItemsVo.getTaskName() + "'");
			}
			if (wfInstancemstrFormItemsVo.getStatus() != null && !"".equals(wfInstancemstrFormItemsVo.getStatus())) {
				sb.append(" and wfi.status = '" + wfInstancemstrFormItemsVo.getStatus() + "'");
			}
			if (wfInstancemstrFormItemsVo.getStartSubmitDatetime() != null) {
				sb.append(" and wfi.submitDatetime >= '" + df.format(wfInstancemstrFormItemsVo.getStartSubmitDatetime()) + "'");
			}
			if (wfInstancemstrFormItemsVo.getEndSubmitDatetime() != null) {
				sb.append(" and wfi.submitDatetime <= '" + df.format(DateUtil.getNextDate(wfInstancemstrFormItemsVo.getEndSubmitDatetime())) + "'");
			}
			if (DictConsts.TIH_TAX_REQUESTFORM_1.equals(wfInstancemstrFormItemsVo.getTaskName())
					|| DictConsts.TIH_TAX_REQUESTFORM_2.equals(wfInstancemstrFormItemsVo.getTaskName())) {
				if (wfInstancemstrFormItemsVo.getDocumentName() != null && !"".equals(wfInstancemstrFormItemsVo.getDocumentName().trim())) {
					sb.append(" and wfi.remarks like '%" + wfInstancemstrFormItemsVo.getDocumentName().trim() + "%'");
				}
			} else if (DictConsts.TIH_TAX_REQUESTFORM_3.equals(wfInstancemstrFormItemsVo.getTaskName())
					&& wfInstancemstrFormItemsVo.getQuestionTitle() != null && !"".equals(wfInstancemstrFormItemsVo.getQuestionTitle().trim())) {
				sb.append(" and wfi.remarks like '%" + wfInstancemstrFormItemsVo.getQuestionTitle().trim() + "%'");
			} else if (DictConsts.TIH_TAX_REQUESTFORM_4.equals(wfInstancemstrFormItemsVo.getTaskName())
					&& wfInstancemstrFormItemsVo.getSelectedQuestionCompanys() != null
					&& wfInstancemstrFormItemsVo.getSelectedQuestionCompanys().size() > 0) {
				sb.append(" and wfip.value in ('"
						+ wfInstancemstrFormItemsVo.getSelectedQuestionCompanys().toString().replace(", ", "','").replace("[", "").replace("]", "")
						+ "')");
			} else if (DictConsts.TIH_TAX_REQUESTFORM_5.equals(wfInstancemstrFormItemsVo.getTaskName())
					&& wfInstancemstrFormItemsVo.getSelectedFeebBackCompanys() != null
					&& wfInstancemstrFormItemsVo.getSelectedFeebBackCompanys().size() > 0) {
				sb.append(" and wfip.value in ('"
						+ wfInstancemstrFormItemsVo.getSelectedFeebBackCompanys().toString().replace(", ", "','").replace("[", "").replace("]", "")
						+ "')");
			}
		}
		sb.append(" order by step.completedDatetime desc");
		List<WfStepmstr> steps = this.em.createQuery(sb.toString()).getResultList();
		List<WfInstancemstrVo> wfivList = new ArrayList<WfInstancemstrVo>();
		if (null != steps && steps.size() != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			WfInstancemstrVo wfiv;
			for (WfStepmstr wfs : steps) {
				wfiv = new WfInstancemstrVo();
				wfiv.setId(wfs.getId());
				if (null != wfs) {
					wfiv.setWfInstancemstr(wfs.getWfInstancemstr());
					wfiv.setFrontHandleDatetime(sdf.format(wfs.getCompletedDatetime()));
					wfivList.add(wfiv);
				}
			}
		}
		return wfivList;
	}

	/**
	 * <p>
	 * Description: 取得结果集
	 * </p>
	 * 
	 * @param sb
	 *            基础sql语句
	 * @param wfInstancemstrFormItemsVo
	 *            查询条件Vo
	 * @return
	 */
	private List<WfInstancemstrVo> getWfInstancemstrVo(String browserLang, StringBuffer sb, TaskSearchVo wfInstancemstrFormItemsVo) {
		logger.debug("browserLang:" + browserLang);
		if (wfInstancemstrFormItemsVo != null) {
			if (wfInstancemstrFormItemsVo.getTaskNumber() != null && !"".equals(wfInstancemstrFormItemsVo.getTaskNumber().trim())) {
				sb.append(" and wfi.no like '%" + wfInstancemstrFormItemsVo.getTaskNumber().trim() + "%'");
			}
			if (wfInstancemstrFormItemsVo.getTaskName() != null && !"".equals(wfInstancemstrFormItemsVo.getTaskName())) {
				sb.append(" and wfi.type =  '" + wfInstancemstrFormItemsVo.getTaskName() + "'");
			}
			if (wfInstancemstrFormItemsVo.getStatus() != null && !"".equals(wfInstancemstrFormItemsVo.getStatus())) {
				sb.append(" and wfi.status = '" + wfInstancemstrFormItemsVo.getStatus() + "'");
			}
			if (wfInstancemstrFormItemsVo.getStartSubmitDatetime() != null) {
				sb.append(" and wfi.submitDatetime >= '" + df.format(wfInstancemstrFormItemsVo.getStartSubmitDatetime()) + "'");
			}
			if (wfInstancemstrFormItemsVo.getEndSubmitDatetime() != null) {
				sb.append(" and wfi.submitDatetime <= '" + df.format(DateUtil.getNextDate(wfInstancemstrFormItemsVo.getEndSubmitDatetime())) + "'");
			}
			if (DictConsts.TIH_TAX_REQUESTFORM_1.equals(wfInstancemstrFormItemsVo.getTaskName())
					|| DictConsts.TIH_TAX_REQUESTFORM_2.equals(wfInstancemstrFormItemsVo.getTaskName())) {
				if (wfInstancemstrFormItemsVo.getDocumentName() != null && !"".equals(wfInstancemstrFormItemsVo.getDocumentName().trim())) {
					sb.append(" and wfi.remarks like '%" + wfInstancemstrFormItemsVo.getDocumentName().trim() + "%'");
				}
			} else if (DictConsts.TIH_TAX_REQUESTFORM_3.equals(wfInstancemstrFormItemsVo.getTaskName())) {
				if (wfInstancemstrFormItemsVo.getQuestionTitle() != null && !"".equals(wfInstancemstrFormItemsVo.getQuestionTitle().trim())) {
					sb.append(" and wfi.remarks like '%" + wfInstancemstrFormItemsVo.getQuestionTitle().trim() + "%'");
				}
			} else if (DictConsts.TIH_TAX_REQUESTFORM_4.equals(wfInstancemstrFormItemsVo.getTaskName())) {
				if (wfInstancemstrFormItemsVo.getSelectedQuestionCompanys() != null
						&& wfInstancemstrFormItemsVo.getSelectedQuestionCompanys().size() > 0) {
					sb.append(" and wfip.value in ('"
							+ wfInstancemstrFormItemsVo.getSelectedQuestionCompanys().toString().replace(", ", "','").replace("[", "")
									.replace("]", "") + "')");
				}
			} else if (DictConsts.TIH_TAX_REQUESTFORM_5.equals(wfInstancemstrFormItemsVo.getTaskName())
					&& wfInstancemstrFormItemsVo.getSelectedFeebBackCompanys() != null
					&& wfInstancemstrFormItemsVo.getSelectedFeebBackCompanys().size() > 0) {
				sb.append(" and wfip.value in ('"
						+ wfInstancemstrFormItemsVo.getSelectedFeebBackCompanys().toString().replace(", ", "','").replace("[", "").replace("]", "")
						+ "')");
			}
		}
		sb.append(" order by wfi.submitDatetime desc");
		List<WfInstancemstr> wfiList = this.em.createQuery(sb.toString()).getResultList();
		List<WfInstancemstrVo> wfivList = new ArrayList<WfInstancemstrVo>();
		if (wfiList != null && wfiList.size() != 0) {
			WfInstancemstrVo wfiv = null;
			long num = 0;
			for (WfInstancemstr wfi : wfiList) {
				num++;
				wfiv = new WfInstancemstrVo();
				wfiv.setId(num);
				wfiv.setWfInstancemstr(wfi);
				wfivList.add(wfiv);
			}
		}
		return wfivList;
	}

	public String findImportanceORUrgency(Long id, String imporurg) {
		String sql = "select wfs.value from WfInstancemstrProperty wfs where wfs.wfInstancemstr.id = " + id + " and wfs.name = '" + imporurg + "'";
		List<String> importance = em.createQuery(sql).getResultList();
		if (importance != null && importance.size() != 0) {
			return importance.get(0);
		} else {
			return null;
		}
	}

	public List<WfStepmstr> findWfstr(Long id) {
		String sql = "select wfs from WfStepmstr wfs where wfs.wfInstancemstr.id = " + id + "";
		return em.createQuery(sql).getResultList();
	}

	/**
	 * <p>
	 * Description: 终止流程
	 * </p>
	 * 
	 * @param workFlowNumber
	 *            流程号
	 * @throws VWException
	 */
	public void stopWorkFlow(WfInstancemstr wfInstancemstr) throws VWException {
		this.workflowService.doTerminate(wfInstancemstr);
		String sql = "update WfInstancemstr wfi set wfi.status = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_4 + "' where wfi.no = '"
				+ wfInstancemstr.getNo() + "'";
		this.em.createQuery(sql).executeUpdate();
		// 判断终止流程的类型是否是报表流程
		try {
			List<WfInstancemstrProperty> wfInstancemstrProperties = this.getWfInstancemstrPropertiesByWfInId(wfInstancemstr);
			if (wfInstancemstrProperties != null) {
				if (DictConsts.TIH_TAX_REQUESTFORM_4.equals(wfInstancemstr.getType())) {
					String type = "";
					String jpql = "";
					String value = "";
					for (WfInstancemstrProperty p : wfInstancemstrProperties) {
						if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE.equals(p.getName())) {
							type = p.getValue();
						}
						if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID.equals(p.getName())) {
							value = p.getValue();
						}
					}// 判断报表的类型&&上传过报表的流程才把报表状态更新为终止
					if (value != null && !"".equals(value) && type != null && !"".equals(type)) {
						if (DictConsts.TIH_TAX_REQUESTFORM_4_1.equals(type)) {
							jpql = "UPDATE ReportPayableTax pay set pay.status='" + DictConsts.TIH_TAX_WORKFLOWSTATUS_4 + "' where pay.id=" + value;
						} else if (DictConsts.TIH_TAX_REQUESTFORM_4_2.equals(type)) {
							jpql = "UPDATE ReportVatIptDeduction vat set vat.status='" + DictConsts.TIH_TAX_WORKFLOWSTATUS_4 + "' where vat.id="
									+ value;
						}
					}
					if (jpql != null && !"".equals(jpql)) {
						this.em.createQuery(jpql).executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * Description: 取得流程属性
	 * </p>
	 * 
	 * @param wfInstancemstr
	 *            流程实体
	 */
	public List<WfInstancemstrProperty> getWfInstancemstrPropertiesByWfInId(WfInstancemstr wfInstancemstr) {
		List<WfInstancemstrProperty> wfInstancemstrProperties = null;
		try {
			String sql = "select p from WfInstancemstrProperty p where p.wfInstancemstr.id=" + wfInstancemstr.getId();
			wfInstancemstrProperties = em.createQuery(sql).getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return wfInstancemstrProperties;
	}

	/**
	 * <p>
	 * Description: 取得所有的邮件配置
	 * </p>
	 * 
	 * @return
	 */
	public List<MailConfigVo> getAllWfMailConfig() {
		try {
			String sql = "select wmc from WfMailConfig wmc where wmc.jobId is null order by wmc.type";
			Query query = this.em.createQuery(sql);
			List<WfMailConfig> wfMailConfigs = query.getResultList();
			List<MailConfigVo> wfMailConfigVos = new ArrayList<MailConfigVo>();
			if (wfMailConfigs != null) {
				MailConfigVo wmcv = null;
				long num = 0;
				for (WfMailConfig wmc : wfMailConfigs) {
					wmcv = new MailConfigVo();
					num++;
					wmcv.setId(num);
					wmcv.setWfMailConfig(wmc);
					if (wmc.getMailInd().equals("N")) {
						wmcv.setEmailFlag(true);
					} else {
						wmcv.setEmailFlag(false);
					}
					if (wmc.getSysNoticeInd().equals("N")) {
						wmcv.setSysNoticeFlag(true);
					} else {
						wmcv.setSysNoticeFlag(false);
					}
					wfMailConfigVos.add(wmcv);
				}
			}
			return wfMailConfigVos;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * <p>
	 * Description: 保存授权
	 * </p>
	 * 
	 * @param authorizmstrSearchVo
	 *            授权Vo
	 * @return
	 */
	public boolean saveAuthorizmstr(String browserLang, AuthorizmstrVo authorizmstrSearchVo) {
		try {
			String userName = this.loginService.getCurrentUserName();
			WfAuthorizmstr wfAuthorizmstr = new WfAuthorizmstr();
			wfAuthorizmstr.setAuthorizedBy(authorizmstrSearchVo.getAuthorizedBy());
			wfAuthorizmstr.setAuthorizedTo(authorizmstrSearchVo.getAuthorizedTo());
			wfAuthorizmstr.setStartDatetime(authorizmstrSearchVo.getStartDatetime());
			wfAuthorizmstr.setEndDatetime(authorizmstrSearchVo.getEndDatetime());
			wfAuthorizmstr.setRemarks(null != authorizmstrSearchVo.getRemarks() ? authorizmstrSearchVo.getRemarks().trim() : "");
			wfAuthorizmstr.setType(authorizmstrSearchVo.getType());
			if (authorizmstrSearchVo.isEmailFlag()) {
				wfAuthorizmstr.setMailInd("N");
			} else {
				wfAuthorizmstr.setMailInd("Y");
			}
			if (authorizmstrSearchVo.isSysNoticeFlag()) {
				wfAuthorizmstr.setSysNoticeInd("N");
			} else {
				wfAuthorizmstr.setSysNoticeInd("Y");
			}
			wfAuthorizmstr.setCreatedBy(userName);
			wfAuthorizmstr.setCreatedDatetime(new Date());
			wfAuthorizmstr.setUpdatedBy(userName);
			wfAuthorizmstr.setUpdatedDatetime(new Date());
			this.em.persist(wfAuthorizmstr);
			// 发邮件或消息
			sendNotice(browserLang, authorizmstrSearchVo, wfAuthorizmstr.getId());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <p>
	 * Description: 验证授权信息
	 * </p>
	 * 
	 * @param authorizedBy
	 *            授权人
	 * @param type
	 *            申请单类型
	 * @param startDatetime
	 *            开始时间
	 * @param endDatetime
	 *            结束时间
	 * @param method
	 *            执行方法
	 * @return
	 */
	public int getAllAuthorizmstrVo(String authorizedBy, String type, Date startDatetime, Date endDatetime) {
		String sql = "select wa from WfAuthorizmstr wa where wa.startDatetime >= '" + df.format(startDatetime) + "' and wa.endDatetime <= '"
				+ df.format(startDatetime) + "' and wa.authorizedBy ='" + authorizedBy + "' and wa.type ='" + type + "'";
		List<WfAuthorizmstr> wfAuthorizmstrs = this.em.createQuery(sql).getResultList();
		if (null != wfAuthorizmstrs && wfAuthorizmstrs.size() != 0) {
			return wfAuthorizmstrs.size();
		}
		String sql2 = "select wa from WfAuthorizmstr wa where wa.startDatetime >= '" + df.format(endDatetime) + "' and wa.endDatetime <= '"
				+ df.format(endDatetime) + "' and wa.authorizedBy ='" + authorizedBy + "' and wa.type ='" + type + "'";
		List<WfAuthorizmstr> wfAuthorizmstrs2 = this.em.createQuery(sql2).getResultList();
		if (null != wfAuthorizmstrs2 && wfAuthorizmstrs2.size() != 0) {
			return wfAuthorizmstrs2.size();
		}
		return 0;
	}

	/**
	 * <p>
	 * Description: 删除授权
	 * </p>
	 * 
	 * @param id
	 *            授权ID
	 * @return
	 */
	public boolean deleteAuthorizmstr(long id) {
		try {
			String sql = "delete from WfAuthorizmstr wa where wa.id = " + id;
			this.em.createQuery(sql).executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <p>
	 * Description: 更新授权
	 * </p>
	 * 
	 * @param id
	 *            授权ID
	 * @param authorizmstrSearchVo
	 *            授权Vo
	 * @return
	 */
	public boolean updateAuthorizmstr(String browserLang, long id, AuthorizmstrVo authorizmstrSearchVo) {
		try {
			String userName = this.loginService.getCurrentUserName();
			WfAuthorizmstr wa = new WfAuthorizmstr();
			wa.setId(id);
			wa.setAuthorizedBy(authorizmstrSearchVo.getAuthorizedBy());
			wa.setAuthorizedTo(authorizmstrSearchVo.getAuthorizedTo());
			wa.setStartDatetime(authorizmstrSearchVo.getStartDatetime());
			wa.setEndDatetime(authorizmstrSearchVo.getEndDatetime());
			wa.setRemarks(null != authorizmstrSearchVo.getRemarks() ? authorizmstrSearchVo.getRemarks().trim() : "");
			wa.setType(authorizmstrSearchVo.getType());
			if (authorizmstrSearchVo.isEmailFlag()) {
				wa.setMailInd("N");
			} else {
				wa.setMailInd("Y");
			}
			if (authorizmstrSearchVo.isSysNoticeFlag()) {
				wa.setSysNoticeInd("N");
			} else {
				wa.setSysNoticeInd("Y");
			}
			wa.setUpdatedBy(userName);
			this.em.merge(wa);
			// 发邮件或消息
			sendNotice(browserLang, authorizmstrSearchVo, wa.getId());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <p>
	 * Description: 取得所有的授权
	 * </p>
	 * 
	 * @param isTaskadmins
	 *            是否是任务管理员
	 * @param wafiv
	 *            查询条件Vo
	 * @return
	 */
	public List<WfAuthorizmstrVo> getAllWfAuthorizmstrVo(boolean isTaskadmins, AuthorizmstrSearchVo wafiv) {
		StringBuffer sb = new StringBuffer();
		sb.append("select wa from WfAuthorizmstr wa where 1=1");
		if (isTaskadmins) {
			if (wafiv != null && wafiv.getAuthorizedBy() != null && !"".equals(wafiv.getAuthorizedBy().trim())) {
				sb.append(" and wa.authorizedBy like '%" + wafiv.getAuthorizedBy().trim() + "%'");
			}
		} else {
			sb.append(" and wa.authorizedBy like '%" + this.loginService.getCurrentUserName() + "%'");
		}
		if (wafiv != null) {
			if (wafiv.getAuthorizedTo() != null && !"".equals(wafiv.getAuthorizedTo().trim())) {
				sb.append(" and wa.authorizedTo like '%" + wafiv.getAuthorizedTo().trim() + "%'");
			}
			if (wafiv.getStartDatetime() != null) {
				sb.append(" and wa.startDatetime >= '" + df.format(wafiv.getStartDatetime()) + "'");
			}
			if (wafiv.getEndDatetime() != null) {
				sb.append(" and wa.endDatetime < '" + df.format(DateUtil.getNextDate(wafiv.getEndDatetime())) + "'");
			}
		}
		sb.append(" order by wa.type,wa.startDatetime");
		List<WfAuthorizmstr> wfAuthorizmstrs = this.em.createQuery(sb.toString()).getResultList();
		List<WfAuthorizmstrVo> wfAuthorizmstrVos = new ArrayList<WfAuthorizmstrVo>();
		if (wfAuthorizmstrs != null && wfAuthorizmstrs.size() != 0) {
			WfAuthorizmstrVo wav = null;
			long num = 0;
			for (WfAuthorizmstr wa : wfAuthorizmstrs) {
				wav = new WfAuthorizmstrVo();
				num++;
				wav.setId(num);
				wav.setWfAuthorizmstr(wa);
				if (wa.getMailInd() != null && !"".equals(wa.getMailInd()) && wa.getMailInd().equals("N")) {
					wav.setEmailFlag(true);
				} else {
					wav.setEmailFlag(false);
				}
				if (wa.getSysNoticeInd() != null && !"".equals(wa.getSysNoticeInd()) && wa.getSysNoticeInd().equals("N")) {
					wav.setSysNoticeFlag(true);
				} else {
					wav.setSysNoticeFlag(false);
				}
				wfAuthorizmstrVos.add(wav);
			}
		}
		return wfAuthorizmstrVos;
	}

	/**
	 * <p>
	 * Description: 取得某个授权人的被授权人 没有则返回null
	 * </p>
	 * 
	 * @param authorizedBy
	 *            授权人
	 * @param requestFormType
	 *            申请单类型
	 * @param datetime
	 *            时间
	 * @return 授权对象或null
	 */
	public WfAuthorizmstr getAuthorizmstr(String authorizedBy, String requestFormType) {
		String sql = "select wa from WfAuthorizmstr wa where wa.startDatetime >= '" + df.format(new Date()) + "' and wa.endDatetime < '"
				+ df.format(DateUtil.getNextDate(new Date())) + "' and wa.authorizedBy ='" + authorizedBy + "' and wa.type ='" + requestFormType
				+ "'";
		List<WfAuthorizmstr> wfAuthorizmstrs = this.em.createQuery(sql).getResultList();
		if (wfAuthorizmstrs != null && wfAuthorizmstrs.size() != 0) {
			return wfAuthorizmstrs.get(0);
		}
		return null;
	}

	/**
	 * <p>
	 * Description: 发送通知(邮件或消息)
	 * </p>
	 * 
	 * @param authorizmstrSearchVo
	 *            授权Vo
	 */
	private void sendNotice(String browserLang, AuthorizmstrVo authorizmstrSearchVo, long typeId) {
		String showAuthorizer = userCommonService.getUsermstrVo(authorizmstrSearchVo.getAuthorizedBy()).getP().getNachn();
		List<String> receiverAccounts = new ArrayList<String>();
		// 收件人
		receiverAccounts.add(authorizmstrSearchVo.getAuthorizedTo());
		// 标题
		String subject = "[益海嘉里]税务信息平台-任务临时授权通知";
		// 内容格式：**，您好！**把自己在**任务中的处理权临时转授给您，授权期限是从yyyy-mm-dd到yyyy-mm-dd。
		String content = showAuthorizer + "把自己在" + commonService.getValueByDictCatKey(authorizmstrSearchVo.getType(), browserLang)
				+ "任务中的处理权临时转授给您，授权期限是从" + df.format(authorizmstrSearchVo.getStartDatetime()) + "到"
				+ df.format(authorizmstrSearchVo.getEndDatetime()) + ",备注是：" + authorizmstrSearchVo.getRemarks(); // 内容
		NotificationVo noticeVo = new NotificationVo();
		noticeVo.setReceiverList(receiverAccounts);
		noticeVo.setTypeId(String.valueOf(typeId));
		noticeVo.setTitle(subject);
		noticeVo.setContent(content);
		noticeService.sendNoticeForWarrant(authorizmstrSearchVo.isEmailFlag(), authorizmstrSearchVo.isSysNoticeFlag(), noticeVo);
	}

	/**
	 * <p>
	 * Description: 初始化邮件配置信息
	 * </p>
	 */
	public void initMailConfig(String browserLang) {
		String sql = "select count(wmc) from WfMailConfig wmc where wmc.jobId is null";
		Query query = this.em.createQuery(sql);
		int count = Integer.parseInt(query.getSingleResult().toString());
		if (count == 0) {
			List<Dict> dicts = commonService.getDictByCat(DictConsts.TIH_TAX_REQUESTFORM, browserLang);
			if (dicts != null && dicts.size() != 0) {
				WfMailConfig wmc = null;
				Date date = new Date();
				for (Dict dict : dicts) {
					wmc = new WfMailConfig();
					wmc.setType(dict.getCodeCat() + "." + dict.getCodeKey());
					wmc.setMailInd("Y");
					wmc.setSysNoticeInd("Y");
					wmc.setCreatedBy(this.loginService.getCurrentUserName());
					wmc.setCreatedDatetime(date);
					wmc.setUpdatedBy(this.loginService.getCurrentUserName());
					wmc.setUpdatedDatetime(date);
					this.em.persist(wmc);
				}
			}
		}
	}

	/**
	 * <p>
	 * Description: 更新邮件配置信息
	 * </p>
	 * 
	 * @param mailConfigVoList
	 *            邮件配置集合
	 * @return
	 */
	public boolean updateWfMailConfig(List<MailConfigVo> mailConfigVoList) {
		try {
			if (mailConfigVoList != null) {
				for (MailConfigVo wfMailConfigVo : mailConfigVoList) {
					if (wfMailConfigVo.isEmailFlag()) {
						wfMailConfigVo.getWfMailConfig().setMailInd("N");
					} else {
						wfMailConfigVo.getWfMailConfig().setMailInd("Y");
					}
					if (wfMailConfigVo.isSysNoticeFlag()) {
						wfMailConfigVo.getWfMailConfig().setSysNoticeInd("N");
					} else {
						wfMailConfigVo.getWfMailConfig().setSysNoticeInd("Y");
					}
					wfMailConfigVo.getWfMailConfig().setCreatedBy(this.loginService.getCurrentUserName());
					wfMailConfigVo.getWfMailConfig().setUpdatedDatetime(new Date());
					this.em.merge(wfMailConfigVo.getWfMailConfig());
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <p>
	 * Description: 取得某任务的邮件配置信息
	 * </p>
	 * 
	 * @param type
	 *            任务类型
	 * @return MailConfig集合
	 */
	public List<WfMailConfig> getWfMailConfig(String requestFormType) {
		String sql = "select wmc from WfMailConfig wmc where wmc.type='" + requestFormType + "' order by wmc.jobInd desc";
		return this.em.createQuery(sql).getResultList();
	}

	/**
	 * <p>
	 * Description: 通过帐号取得用户Vo
	 * </p>
	 * 
	 * @param adAccount
	 *            帐号
	 * @return
	 */
	public UsermstrVo getUsermstrVo(String adAccount) {
		return userCommonService.getUsermstrVo(adAccount);
	}

	/**
	 * <p>
	 * Description: 取得当前的用户名
	 * </p>
	 * 
	 * @return
	 */
	public String getCurrentUserName() {
		return this.loginService.getCurrentUserName();
	}

	/**
	 * <p>
	 * Description: 取得当权用户的角色
	 * </p>
	 * 
	 * @return
	 */
	public Boolean getCurrentRoles(String taskadmins) {
		List<Rolemstr> roles = this.loginService.getCurrentRoles();
		if (roles != null && roles.size() != 0) {
			for (Rolemstr r : roles) {
				if (taskadmins.equals(r.getCode())) {
					return true;
				}
			}
		}
		return false;
	}

	public List<WfInstancemstrVo> getTasksToDo(String userName) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("select step from WfStepmstr step,WfInstancemstr wfi");
		jpql.append(" where step.wfInstancemstr.id = wfi.id");
		jpql.append(" and (step.chargedBy = '" + userName + "' or step.chargedBy like '%," + userName + "%' or step.chargedBy like '%" + userName
				+ ",%')");
		jpql.append(" and step.id in(select max(s.id) from WfStepmstr s where s.defunctInd <> 'Y' group by s.wfInstancemstr.id)");
		jpql.append(" and wfi.defunctInd <> 'Y' and step.defunctInd <> 'Y'");
		jpql.append(" and wfi.status = ?1");
		jpql.append(" order by step.completedDatetime desc");
		Query query = this.em.createQuery(jpql.toString());
		query.setParameter(1, DictConsts.TIH_TAX_WORKFLOWSTATUS_2);
		List<WfStepmstr> steps = query.getResultList();
		List<WfInstancemstrVo> wfivList = new ArrayList<WfInstancemstrVo>();
		if (null != steps && steps.size() != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			WfInstancemstrVo wfiv;
			for (WfStepmstr wfs : steps) {
				wfiv = new WfInstancemstrVo();
				wfiv.setId(wfs.getId());
				if (null != wfs) {
					WfInstancemstr wfi = wfs.getWfInstancemstr();
					wfiv.setWfInstancemstr(wfi);
					wfiv.setTypeDictVo(commonService.getDictVoByKey(wfi.getType()));
					wfiv.setFrontHandleDatetime(sdf.format(wfs.getCompletedDatetime()));
					wfivList.add(wfiv);
				}
			}
		}
		return wfivList;
	}

	public List<WfInstancemstrVo> getTasksDone(String userName) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("select distinct wfi from WfStepmstr step,WfInstancemstr wfi join fetch wfi.wfStepmstrs");
		jpql.append(" where step.wfInstancemstr.id = wfi.id");
		jpql.append(" and (step.chargedBy = '" + userName + "' or step.chargedBy like '%," + userName + "%' or step.chargedBy like '%" + userName
				+ "+,%')");
		jpql.append(" and step.id not in(select max(s.id) from WfStepmstr s where s.defunctInd <> 'Y' group by s.wfInstancemstr.id)");
		jpql.append(" and wfi.defunctInd <> 'Y' and step.defunctInd <> 'Y'");
		jpql.append(" order by step.completedDatetime desc");
		Query query = this.em.createQuery(jpql.toString());
		List<WfInstancemstr> wfiList = query.getResultList();
		return getWfInstancemstrVos(wfiList);
	}

	public List<WfInstancemstrVo> getTasksApplied(String userName) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("select wfi from WfInstancemstr wfi");
		jpql.append(" where wfi.defunctInd <> 'Y' and wfi.status <> ?1 and wfi.requestBy= ?2");
		jpql.append(" order by wfi.submitDatetime desc");
		Query query = this.em.createQuery(jpql.toString());
		query.setParameter(1, DictConsts.TIH_TAX_WORKFLOWSTATUS_1);
		query.setParameter(2, userName);
		List<WfInstancemstr> wfiList = query.getResultList();
		return getWfInstancemstrVos(wfiList);
	}

	public List<WfInstancemstrVo> getWfInstancemstrVos(List<WfInstancemstr> wfiList) {
		List<WfInstancemstrVo> wfivList = new ArrayList<WfInstancemstrVo>();
		if (wfiList != null && wfiList.size() != 0) {
			WfInstancemstrVo wfiv = null;
			long num = 0;
			for (WfInstancemstr wfi : wfiList) {
				num++;
				wfiv = new WfInstancemstrVo();
				wfiv.setId(num);
				wfiv.setWfInstancemstr(wfi);
				wfiv.setTypeDictVo(commonService.getDictVoByKey(wfi.getType()));
				wfivList.add(wfiv);
			}
		}
		return wfivList;
	}

	public int getMyDraftCount() {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select count(wf) from WfInstancemstr wf");
		jpql.append(" where wf.defunctInd = 'N'");
		jpql.append(" and wf.status='" + DictConsts.TIH_TAX_WORKFLOWSTATUS_1 + "'");
		jpql.append(" and wf.requestBy='" + loginService.getCurrentUserName() + "'");
		Long draftCount = 0L;
		try {
			draftCount = (Long) em.createQuery(jpql.toString()).getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return draftCount.intValue();

	}

	public int getMyWaitTaskCount() {
		String userName = loginService.getCurrentUserName();
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select count(wf) from WfStepmstr s,WfInstancemstr wf");
		jpql.append(" where wf.id = s.wfInstancemstr.id");
		jpql.append(" and s.defunctInd <> 'Y'");
		jpql.append(" and s.wfInstancemstr.defunctInd <> 'Y'");
		jpql.append(" and s.wfInstancemstr.status = '" + DictConsts.TIH_TAX_WORKFLOWSTATUS_2 + "'");
		jpql.append(" and (s.chargedBy = '" + userName + "' or s.chargedBy like '%," + userName + "%' or s.chargedBy like '%" + userName + ",%')");
		jpql.append(" and s.id in");
		jpql.append(" (select max(s.id) from WfStepmstr s");
		jpql.append(" group by s.wfInstancemstr.id)");
		Long taskCount = 0L;
		try {
			taskCount = (Long) em.createQuery(jpql.toString()).getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return taskCount.intValue();
	}

	/**
	 * 
	 * <p>
	 * Description: 判断一个字符串str1是否包含另一个字符串str2
	 * </p>
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean ifContainIndexOf(String str1, String str2) {
		if (str1.indexOf(str2) >= 0) {
			return true;
		}
		return false;
	}

	public WfInstancemstrVo findWfInstancemstrByNo(String no) {
		WfInstancemstrVo wfVo = null;
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select wf from WfInstancemstr wf");
		jpql.append(" left join fetch wf.wfInstancemstrProperties");
		jpql.append(" left join fetch wf.wfStepmstrs");
		jpql.append(" where wf.no = '" + no + "'");
		List<WfInstancemstr> list = em.createQuery(jpql.toString()).getResultList();
		if (list != null && list.size() > 0) {
			wfVo = new WfInstancemstrVo();
			WfInstancemstr wf = list.get(0);
			wfVo.setId(wf.getId());
			wfVo.setWfInstancemstr(wf);
		}
		return wfVo;
	}

	public Integer findCountFromEmailHistory(Map<String, Object> filter) {
		List resultList = em.createQuery(buildjpql(filter, true)).getResultList();
		if (resultList.size() > 0) {
			return Integer.valueOf(resultList.get(0).toString());
		}
		return null;
	}

	public List<EmailInfo> findEmailHistoryBy(Map<String, Object> filter, int first, int pageSize) {
		List<NotificationReceiver> receivers = em.createQuery(buildjpql(filter, false)).setFirstResult(first).setMaxResults(first + pageSize)
				.getResultList();
		return getEmailInfos(receivers);
	}

	private String buildjpql(Map<String, Object> filter, boolean isCount) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select");
		if (isCount) {
			jpql.append(" count(r)");
		} else {
			jpql.append(" r");
		}
		jpql.append(" from NotificationReceiver r");
		jpql.append(" where 1=1");
		jpql.append(" and r.notificationSender.notificationmstr.type = '" + DictConsts.TIH_TAX_MSG_REFTYPE_1 + "'");
		jpql.append(" and r.notificationSender.sendOption = '" + DictConsts.TIH_TAX_MSG_TYPE_1 + "'");
		if (filter != null) {
			Object typeId = filter.get("typeId");
			if (typeId != null && !"".equals((String) typeId)) {
				jpql.append(" and r.notificationSender.notificationmstr.typeId like '%" + typeId + "%'");
			}
			Object status = filter.get("status");
			if (status != null && !"".equals((String) status)) {
				jpql.append(" and r.notificationSender.status = '" + status + "'");
			}
			Object createStartDate = filter.get("createStartDate");
			if (createStartDate != null) {
				jpql.append(" and r.createdDatetime >= '" + DateUtils.format((Date) createStartDate, "yyyy-MM-dd 00:00:00") + "'");
			}
			Object createEndDate = filter.get("createEndDate");
			if (createEndDate != null) {
				jpql.append(" and r.createdDatetime < '" + DateUtils.format((Date) createEndDate, "yyyy-MM-dd 00:00:00") + "'");
			}
			Object sentBy = filter.get("sentBy");
			if (sentBy != null && !"".equals(sentBy)) {
				jpql.append(" and r.notificationSender.sentBy like '%" + sentBy + "%'");
			}
			Object receievedBy = filter.get("receivedBy");
			if (receievedBy != null && !"".equals(receievedBy)) {
				jpql.append(" and r.receivedBy like '%" + receievedBy + "%'");
			}
			Object sendStartDate = filter.get("sendStartDate");
			if (sendStartDate != null) {
				jpql.append(" and r.notificationSender.sendDatetime >= '" + DateUtils.format((Date) sendStartDate, "yyyy-MM-dd 00:00:00") + "'");
			}
			Object sendEndDate = filter.get("sendEndDate");
			if (sendEndDate != null) {
				jpql.append(" and r.notificationSender.sendDatetime < '" + DateUtils.format((Date) sendEndDate, "yyyy-MM-dd 00:00:00") + "'");
			}
		}
		if (!isCount) {
			jpql.append(" order by r.notificationSender.notificationmstr.typeId,r.createdDatetime desc");
		}
		return jpql.toString();
	}

	private List<EmailInfo> getEmailInfos(List<NotificationReceiver> receivers) {
		List<EmailInfo> emailInfos = new ArrayList<EmailInfo>();
		EmailInfo emailInfo = null;
		for (NotificationReceiver r : receivers) {
			NotificationSender s = r.getNotificationSender();
			Notificationmstr n = s.getNotificationmstr();
			emailInfo = new EmailInfo();
			emailInfo.setTypeId(n.getTypeId());
			emailInfo.setType(n.getType());
			emailInfo.setStatus(s.getStatus());
			emailInfo.setCreatedDatetime(r.getCreatedDatetime());
			emailInfo.setSentBy(s.getSentBy());
			emailInfo.setSendDatetime(s.getSendDatetime());
			emailInfo.setContent(n.getContent());
			emailInfo.setReceivedBy(r.getReceivedBy());
			emailInfos.add(emailInfo);
		}
		return emailInfos;
	}
}
