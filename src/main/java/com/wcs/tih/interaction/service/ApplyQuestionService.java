package com.wcs.tih.interaction.service;

import java.io.InputStream;
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

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.base.util.StringUtils;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.service.NoticeService;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.service.UserCommonService;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.filenet.pe.service.DefaultWorkflowImpl;
import com.wcs.tih.filenet.pe.service.WorkflowService;
import com.wcs.tih.interaction.controller.vo.QuestionVo;
import com.wcs.tih.model.NotificationExt;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.system.service.PositionProfessionInterface;

import filenet.vw.api.VWException;
import filenet.vw.api.VWStepElement;

@Stateless
public class ApplyQuestionService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private CEserviceLocal ceservice;
	@EJB
	private WorkflowService workflowService;
	@EJB
	private LoginService loginService;
	@EJB
	private PositionProfessionInterface positionProfessionInterface;
	@EJB
	private FileNetUploadDownload fileUpService;
	@EJB
	private DefaultWorkflowImpl defaultWorkflowImpl;
	@EJB
	private NoticeService noticeService;
	@EJB
	private UserCommonService userCommonService;
	@EJB
	private DefaultWorkflowImpl d;
	@EJB
	private TimeoutEmailService timeoutEmailService;

	@PersistenceContext
	private EntityManager em;

	private ResourceBundle rb = ResourceBundle.getBundle("filenet");
	private final String userPassword = "user.password";
	private final String requestForm = DictConsts.TIH_TAX_REQUESTFORM_3;

	// 在配置文件applyquestionEmail中，找到要设置的默认收件人的邮箱；添加默认收件人的邮箱是，要添加单引号，逗号分隔；比如：,'wangxuan@wcs-global.com'
	private final String emails = ResourceBundle.getBundle("applyquestionEmail").getString("email");

	// ======================================草稿箱====================================================

	public WfInstancemstr saveToDraft(QuestionVo questionVo) {
		Map<String, Object> createDlgParamMap = questionVo.getCreateDlgParamMap();
		String importance = questionVo.getImportance();
		String urgency = questionVo.getUrgency();
		String recipients = questionVo.getRecipients();
		StringBuffer documentIds = questionVo.getDocumentIds();
		StringBuffer documentNames = questionVo.getDocumentNames();

		Object ccs  = createDlgParamMap.get("cc");
		String cc =  ccs == null ? null : (String)ccs;
		
		// 存入主表
		WfInstancemstr wfInsM = this.addWfInsCommonCode("未启动", DictConsts.TIH_TAX_WORKFLOWSTATUS_1, createDlgParamMap.get("queHead").toString()
				.trim(), importance, urgency);
		// 存入附表
		this.addWfInsProExtraCommonCode(wfInsM, createDlgParamMap, recipients);
		WfStepmstr wfStepM = this.addWfStepCommonCode(wfInsM, 0, "", "", "", DictConsts.TIH_TAX_APPROACH_1);
		this.addWfStepProExtraCommonCode(wfStepM, createDlgParamMap.get("queMore").toString(), documentIds, documentNames);
		addWfStepProCommonCode(wfStepM, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_CC, cc);
		return wfInsM;
	}

	public void updateToDraft(QuestionVo questionVo) {
		Map<String, Object> createDlgParamMap = questionVo.getCreateDlgParamMap();
		String importance = questionVo.getImportance();
		String urgency = questionVo.getUrgency();
		String recipients = questionVo.getRecipients();
		StringBuffer documentIds = questionVo.getDocumentIds();
		StringBuffer documentNames = questionVo.getDocumentNames();
		WfInstancemstr wfIns = questionVo.getWfIns();
		Object ccs  = createDlgParamMap.get("cc");
		String cc =  ccs == null ? null : (String)ccs;

		this.updateWfInsCommonCode(wfIns, "N", DictConsts.TIH_TAX_WORKFLOWSTATUS_1, createDlgParamMap.get("queHead").toString().trim(), importance,
				urgency);
		// 更新八条数据
		this.updateWfInsProExtraCommonCode(wfIns, createDlgParamMap, null);
		this.updateWfStepProExtraCommonCode(wfIns.getWfStepmstrs().get(0).getWfStepmstrProperties(), createDlgParamMap.get("queMore").toString(),
				documentIds, documentNames);
		WfInstancemstrProperty wfInsPro = this.queryWfInsProByWfIns(wfIns, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT);
		wfInsPro.setValue(recipients);
		this.updateWfInsProAll(wfInsPro);
		addWfStepProCommonCode(wfIns.getWfStepmstrs().get(0), WorkflowConsts.TIH_WORKFLOW_APPLYQUE_CC, cc);
	}

	/**
	 * <p>
	 * Description:在草稿箱中点击删除按钮,删除这个未启动的流程,并且需要删除FileNet上已经删除的文件
	 * </p>
	 * 
	 * @param wfIns
	 *            主表数据
	 * @param documentIds
	 *            StringBuffer类型,数据库中存储的文件ID
	 */
	public void deleteWorkflowFromDraft(WfInstancemstr wfIns, StringBuffer documentIds) {
		// 根据wfIns来更新wfInsPro,更新为:终止,defunt为Y
		this.updateWfInsCommonCode(wfIns, "Y", DictConsts.TIH_TAX_WORKFLOWSTATUS_4, null, null, null);
		// 删除PE上传的所有文件
		if (documentIds.toString() != null && !documentIds.toString().equals("")) {
			String[] fileIdGroup = documentIds.toString().split(",");
			List<String> list = new ArrayList<String>();
			for (String fileId : fileIdGroup) {
				list.add(fileId);
			}
			this.deleteBatchFileCE(list);
		}
	}

	// =========================================FileNetCE=====================================================
	// 批量删除
	public void deleteBatchFileCE(List<String> rubbishFileIdList) {
		try {
			ceservice.connect(tds.addPre(loginService.getCurrentUserName()), rb.getString(userPassword));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		for (int i = 0; i < rubbishFileIdList.size(); i++) {
			String fileId = rubbishFileIdList.get(i);
			Document d = ceservice.getDocument(fileId);
			d.delete();
			d.save(RefreshMode.REFRESH);
		}
	}

	// 单个删除
	public void deleteSingleFileCE(String fileId) throws Exception {
		ceservice.connect(tds.addPre(loginService.getCurrentUserName()), rb.getString(userPassword));
		Document d = ceservice.getDocument(fileId);
		d.delete();
		d.save(RefreshMode.REFRESH);
	}

	// 添加附件,抛出2个异常,在Bean里进行抓取
	public Document addFileCE(UploadedFile upFile) throws MimeException, Exception {
		String fileName = upFile.getFileName();
		InputStream inputStream;
		com.filenet.api.core.Document document;
		inputStream = upFile.getInputstream();
		ResourceBundle rbx = ResourceBundle.getBundle("filenet");
		String folder = rbx.getString("ce.folder.mission");
		String tihDoc = rbx.getString("ce.document.classid");
		logger.info("folder:" + folder);
		document = fileUpService.upLoadDocumentCheckIn(inputStream, new HashMap<String, Object>(), fileName, tihDoc, folder);
		return document;
	}

	// 下载附件
	public StreamedContent downloadFile(String fileId) throws MimeException, Exception {
		return fileUpService.downloadDocumentEncoding(fileId, "utf-8", "iso8859-1");
	}

	// ========================================流程控制总枢纽================================================
	/**
	 * 发射一个工作流.下面的createWorkFlow使用
	 * 
	 * @param fileNetName
	 *            登录名=当前登录用户的帐号
	 * @param fileNetPwd
	 *            登录密码
	 * @param answer
	 *            回答人,下个节点,必须指定
	 * @return VWStepElement参数,用于获取一些特定的FileNet方面的参数.
	 */
	@EJB
	private TDSLocal tds;
	private static String dnStr = ResourceBundle.getBundle("filenet").getString("tds.users.dn");

	// 由下面的createWorkFlow方法调用.
	private VWStepElement launchAskWorkflow(String answer) {
		// 设定工作流中的回答人参数,answer_param是Filenet中定义的回答人参数名.
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		String answerParam = WorkflowConsts.ASK_FLOW_PARAM_ANSWER;
		paramMap.put(answerParam, new String[] { "uid=" + tds.addPre(answer) + "," + dnStr });
		// 获取工作流名字.这里全部为提问工作流.
		String workflowName = WorkflowConsts.ASK_WORKFLOW_NAME;
		// FileNet的登录名和密码
		VWStepElement stepElement = null;
		try {
			// 调用方法执行创建工作流
			stepElement = workflowService.createWorkFlow(workflowName, tds.addPre(loginService.getCurrentUserName()), rb.getString(userPassword),
					paramMap);
		} catch (VWException e) {
			logger.error(e.getMessage(), e);
		}
		return stepElement;
	}

	/**
	 * 可追加事务.
	 * <p>
	 * Description:创建流程
	 * </p>
	 * 
	 * @param noDraft
	 *            是否是草稿箱
	 * @param wfIns
	 *            实例表实体
	 * @param createDlgParamMap
	 *            dialog页面参数的MAP集合.
	 * @param documentIds
	 *            附件ID, id,id,id 形式
	 * @param documentNames
	 *            附件name,形式同ID.
	 * @return 错误信息,为null表示成功,有错误信息,前台抛出.
	 * @throws VWException
	 */
	public String createWorkFlow(QuestionVo questionVo) throws VWException {
		String answerOne = "";
		Map<String, Object> createDlgParamMap = questionVo.getCreateDlgParamMap();
		List<String> createDlgNameWithEmail = questionVo.getCreateDlgNameWithEmail();
		StringBuffer documentIds = questionVo.getDocumentIds();
		StringBuffer documentNames = questionVo.getDocumentNames();
		String importance = questionVo.getImportance();
		String urgency = questionVo.getUrgency();
		WfInstancemstr wfIns = questionVo.getWfIns();
		WfRemindVo wfRemindVo = questionVo.getWfRemindVo();
		boolean noDraft = questionVo.isNoDraft();
		
		Object ccs  = createDlgParamMap.get("cc");
		String cc =  ccs == null ? null : (String)ccs;
		
		List<String> ccEmails = new ArrayList<String>();
		if (!StringUtils.isBlankOrNull(cc)) {
			for (String email : cc.split(";")) {
				ccEmails.add(email);
			}
		}

		List<UsermstrVo> answerList = positionProfessionInterface.getUsermstrVo(createDlgParamMap.get("taxType").toString());// 具体参数得页面传过来,考虑为null的情况
		if (null == answerList) {
			return "系统回答岗上人员空缺，请联系管理员！";
		} else if (answerList.size() == 1) {
			answerOne = answerList.get(0).getUsermstr().getAdAccount().toString();
		} else if (answerList.size() > 1) {
			String repeatUser = "";
			for (int i = 0; i < answerList.size(); i++) {
				if (i == 0) {
					repeatUser = answerList.get(i).getUsermstr().getAdAccount().toString();
				} else {
					repeatUser = repeatUser + "," + answerList.get(i).getUsermstr().getAdAccount().toString();
				}
			}
			return "系统回答岗上人员重复，请联系管理员！重复帐号为:" + repeatUser + ".";
		}
		// 如果成功了,就运行到这里做授权的处理
		answerOne = this.processAuthorizer(answerOne);

		// 定义一个工作流唯一标识以及数据库需要的code字段.
		String workflowNumber = null;
		String code = null;
		String workPost = null;

		// 执行工作流发射方法,参数为上面获取到的answerOne,也就是税种回答岗对应的唯一的回答人.
		VWStepElement stepElement = this.launchAskWorkflow(answerOne);
		workflowNumber = stepElement.getWorkflowNumber();
		Integer tempCode = stepElement.getWorkOrderId();
		code = tempCode.toString();
		workPost = WorkflowConsts.ASKWORKFLOW_LAUNCH_STEP;

		// 收件人
		String recipientNames = createDlgNameWithEmail.toString().substring(1, createDlgNameWithEmail.toString().length() - 1).replace(" ", "");

		String remarks = createDlgParamMap.get("queHead").toString();
		WfStepmstrProperty opionionStepPro = null;
		if (noDraft) {
			// 直接提交
			WfInstancemstr wfInsM = this.addWfInsCommonCode(workflowNumber, DictConsts.TIH_TAX_WORKFLOWSTATUS_2, remarks, importance, urgency);
			wfRemindVo.setWfId(wfInsM.getId());
			this.addWfInsProExtraCommonCode(wfInsM, createDlgParamMap, recipientNames);
			WfStepmstr wfStepM = this.addWfStepCommonCode(wfInsM, 0, workPost, code, answerOne, DictConsts.TIH_TAX_APPROACH_1);
			opionionStepPro = this.addWfStepProExtraCommonCode(wfStepM, createDlgParamMap.get("queMore").toString(), documentIds, documentNames);
			addWfStepProCommonCode(wfStepM, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_CC, cc);
		} else {
			// false代表着是草稿箱的提交.
			wfIns.setNo(workflowNumber);
			wfIns.setRemarks(remarks);
			this.updateWfInsCommonCode(wfIns, "N", DictConsts.TIH_TAX_WORKFLOWSTATUS_2, remarks, importance, urgency);
			wfRemindVo.setWfId(wfIns.getId());
			this.updateWfInsProExtraCommonCode(wfIns, createDlgParamMap, recipientNames);
			WfStepmstr ws = wfIns.getWfStepmstrs().get(0);
			ws.setUpdatedDatetime(new Date());
			ws.setCode(code);
			ws.setName(workPost);
			ws.setChargedBy(answerOne);
			ws.setCompletedDatetime(new Date());
			this.updateWfStepCommonCode(ws);
			opionionStepPro = this.updateWfStepProExtraCommonCode(ws.getWfStepmstrProperties(), createDlgParamMap.get("queMore").toString(),
					documentIds, documentNames);
			addWfStepProCommonCode(ws, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_CC, cc);
		}
		// 流程超时规则
		timeoutEmailService.saveWfTimeoutRemind(wfRemindVo);

		// 发送Email给回答人
		String subject2 = ("[益海嘉里]税务信息平台-提问申请单流程回答通知");
		String currentNachn = userCommonService.getUsermstrVo(loginService.getCurrentUserName()).getP().getNachn();
		String body2 = (currentNachn + "提的问题：'" + createDlgParamMap.get("queHead") + "'，请尽快处理此流程。");
		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();
		List<String> receiverAccounts = new ArrayList<String>();
		receiverAccounts.add(answerOne);
		NotificationVo noticeVo = new NotificationVo();
		noticeVo.setReceiverList(receiverAccounts);
		noticeVo.setTypeId(workflowNumber);
		noticeVo.setTitle(subject2);
		noticeVo.setContent(body2);
		noticeVos.add(noticeVo);

		// 避免给回答人发送两次Email
		List<String> userName = new ArrayList<String>();
		for (String name : createDlgNameWithEmail) {
			if (!answerOne.equals(name)) {
				userName.add(name);
			}
		}

		// 抄送人邮件
		userName.addAll(ccEmails);

		// 发送收件人邮件
		String subject = ("[益海嘉里]税务信息平台-提问处理流程-'" + createDlgParamMap.get("queHead") + "'");
		String content = currentNachn + "提的问题，" + userCommonService.getUsermstrVo(answerOne).getP().getNachn() + "正在处理。";
		NotificationVo recipientNoticeVo = new NotificationVo();

		recipientNoticeVo.setReceiverList(userName);
		recipientNoticeVo.setTypeId(workflowNumber);
		recipientNoticeVo.setTitle(subject);
		recipientNoticeVo.setContent(content);
		if (opionionStepPro != null) {
			NotificationExt noticeExt = new NotificationExt();
			noticeExt.setTableName("WfStepmstrProperty");
			noticeExt.setTableId(opionionStepPro.getId());
			noticeExt.setTableColumn("value");
			recipientNoticeVo.setNoticeExt(noticeExt);
		}

		noticeVos.add(recipientNoticeVo);

		this.noticeService.sendNoticeForTask(requestForm, noticeVos);
		return null;
	}

	/**
	 * 可追加事务. 工作流处理,这里主要是指定路由方向,下个节点人名,
	 * 
	 * @param workflowNodalPoint
	 *            处理节点,四个参数"answer","helper","assigner","asker"
	 * @param authorizer
	 *            下个节点接收人的account
	 * @param sendToDirection
	 *            路由方向,指向各个方向,具体
	 * @param workflowNum
	 *            工作流标识
	 * @throws Exception
	 */
	public void workflowExecuteAction(QuestionVo questionVo) throws Exception {
		String sendToName = questionVo.getSendToName();
		String workflowNodalPoint = questionVo.getWorkflowNodalPoint();
		String sendToDirection = questionVo.getSendToDirection();
		List<String> recipientsSelectList = questionVo.getRecipientsSelectList();
		WfInstancemstr wfIns = questionVo.getWfIns();
		String taskStatus = questionVo.getTaskStatus();
		String txtOpionion = questionVo.getTxtOpionion();
		StringBuffer documentIds = questionVo.getDocumentIds();
		StringBuffer documentNames = questionVo.getDocumentNames();
		Map<String, Object> processDlgParamMap = questionVo.getProcessDlgParamMap();
		String workflowNum = questionVo.getWorkflowNum();
		
		System.out.println("[sendToName]" + sendToName);
		System.out.println("[workflowNodalPoint]" + workflowNodalPoint);
		System.out.println("[sendToDirection]" + sendToDirection);
		System.out.println("[taskStatus]" + taskStatus);
		System.out.println("[txtOpionion]" + txtOpionion);
		System.out.println("[workflowNum]" + workflowNum);
		
		Object ccs  = processDlgParamMap.get("cc");
		String cc =  ccs == null ? null : (String)ccs;
		
		List<String> ccEmails = new ArrayList<String>();
		if (!StringUtils.isBlankOrNull(cc)) {
			for (String email : cc.split(";")) {
				ccEmails.add(email);
			}
		}

		// 首先进行授权的判定,数据库和fileNet上存储的接收人名
		String authorizer = this.processAuthorizer(sendToName);
		// 获取发Email用的真实姓名
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		String status = null;
		String sendToParam = null;
		String processMode = ""; // 最后添加到步骤表的处理方式
		String workPost = "";
		String button = "";

		if (workflowNodalPoint.equals("answer")) { // 回答人岗位处理,workflowNodalPoint,sendToDirection必然有值
			workPost = WorkflowConsts.ASKWORKFLOW_ANSWER_STEP;
			if (sendToDirection.equals("asker")) {
				status = WorkflowConsts.ANSWER_SENDTO_ASKER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ASKER;
				processMode = DictConsts.TIH_TAX_APPROACH_2;// 回答
			} else if (sendToDirection.equals("helper")) {
				status = WorkflowConsts.ANSWER_SENDTO_HELPER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_HELPER;
				processMode = DictConsts.TIH_TAX_APPROACH_3;// 协助
			} else if (sendToDirection.equals("assigner")) {
				status = WorkflowConsts.ANSWER_SENDTO_ASSIGNER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ASSIGNER;
				processMode = DictConsts.TIH_TAX_APPROACH_4;// 转签
			}
			paramMap.put(WorkflowConsts.ANSWER_STATUS, status);
		} else if (workflowNodalPoint.equals("helper")) { // 协助人岗位处理
			processMode = DictConsts.TIH_TAX_APPROACH_5;// 协助这里全部是反馈
			workPost = WorkflowConsts.ASKWORKFLOW_HELPER_STEP;
			if (sendToDirection.equals("answer")) {
				status = WorkflowConsts.HELPER_SENDTO_ANSWER; // 锁定路线方向参数名的值
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ANSWER; // 取得接收人的参数名
			} else if (sendToDirection.equals("assigner")) {
				status = WorkflowConsts.HELPER_SENDTO_ASSIGNER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ASSIGNER;
			}
			paramMap.put(WorkflowConsts.HELPER_STATUS, status); // 括号中第一个是方向参数名,第二个是方向参数名的值
		} else if (workflowNodalPoint.equals("assigner")) { // 转签人处理岗位
			workPost = WorkflowConsts.ASKWORKFLOW_ASSIGNER_STEP;
			if (sendToDirection.equals("answer")) {
				processMode = DictConsts.TIH_TAX_APPROACH_6;// 拒绝
				status = WorkflowConsts.ASSIGNER_SENDTO_ANSWER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ANSWER;
			} else if (sendToDirection.equals("helper")) {
				processMode = DictConsts.TIH_TAX_APPROACH_3;// 协助
				status = WorkflowConsts.ASSIGNER_SENDTO_HELPER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_HELPER;
			} else if (sendToDirection.equals("asker")) {
				processMode = DictConsts.TIH_TAX_APPROACH_2;// 回答
				status = WorkflowConsts.ASSIGNER_SENDTO_ASKER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ASKER;
			}
			paramMap.put(WorkflowConsts.ASSIGNER_STATUS, status);
		} else if (workflowNodalPoint.equals("asker")) { // 提问人岗位,如果是结束,也就是over,不需要执行传下个节点人参数
			workPost = WorkflowConsts.ASKWORKFLOW_ASKER_STEP;
			processMode = DictConsts.TIH_TAX_APPROACH_7;// 驳回
			if (sendToDirection.equals("over")) {
				processMode = DictConsts.TIH_TAX_APPROACH_8;// 通过
				status = WorkflowConsts.ASKER_SENDTO_OVER;
			} else if (sendToDirection.equals("answer")) {
				status = WorkflowConsts.ASKER_SENDTO_ANSWER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ANSWER;
			} else if (sendToDirection.equals("assigner")) {
				status = WorkflowConsts.ASKER_SENDTO_ASSIGNER;
				sendToParam = WorkflowConsts.ASK_FLOW_PARAM_ASSIGNER;
			}
			paramMap.put(WorkflowConsts.ASKER_STATUS, status);
		}

		if (!sendToDirection.equals("over")) {
			paramMap.put(sendToParam, new String[] { "uid=" + tds.addPre(authorizer) + "," + dnStr });
		}

		// 将收件人的下个节点的人员移除
		List<String> recipientsName = new ArrayList<String>();
		if (recipientsSelectList.size() > 0) {
			for (String strName : recipientsSelectList) {
				if (!strName.equals(authorizer)) {
					recipientsName.add(strName);
				}
			}
		}

		//抄送人
		recipientsName.addAll(ccEmails);
		
		// 这里是如果调用东俊方法的地方.!下面是我的方法
		workflowService.execute(tds.addPre(loginService.getCurrentUserName()), rb.getString(userPassword), workflowNum, paramMap);
		String code = ""; // 获取code字段
		Integer tempCode = workflowService.getStepElement().getWorkOrderId();
		code = tempCode.toString();
		// FileNet方面的代码执行完毕.下面就是进行数据库存储==>1表更新,2表不动,3表添加一条,4表添加三条
		this.updateWfInsCommonCode(wfIns, "N", taskStatus, null, null, null);
		// 获取步骤表中的现在的ID,先要获取到wfs,JPQL查出最后一条步骤的ID.
		WfStepmstr wfs = this.queryLastWfsByWfIns(wfIns);
		// 下面是存入步骤主表一条数据,判断这里的处理方式是什么,通过sendToDirection来判断,处理方式:提交,回答,协助,转签,结束等,这里需要动态传参
		WfStepmstr wfsM = this.addWfStepCommonCode(wfIns, wfs.getId(), workPost, code, authorizer, processMode);
		WfStepmstrProperty opionionStepPro = this.addWfStepProExtraCommonCode(wfsM, txtOpionion, documentIds, documentNames);
		
		addWfStepProCommonCode(wfsM, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_CC, cc);
		
		NotificationExt noticeExt = null;
		if (opionionStepPro != null) {
			noticeExt = new NotificationExt();
			noticeExt.setTableName("WfStepmstrProperty");
			noticeExt.setTableId(opionionStepPro.getId());
			noticeExt.setTableColumn("value");
		}

		List<NotificationVo> noticeVos = new ArrayList<NotificationVo>();

		StringBuilder subject = new StringBuilder();
		StringBuilder body = new StringBuilder();
		String title = processDlgParamMap.get("createUser") + "提的问题:'" + processDlgParamMap.get("queHead") + "'，";
		if (processMode == DictConsts.TIH_TAX_APPROACH_2) {
			// 回答,查询wfInsPro中的标题,问题,查询stepPro中的回答,
			subject.append("[益海嘉里]税务信息平台-提问处理流程-'" + processDlgParamMap.get("queHead") + "'，");
			body.append(title + "已被" + userCommonService.getUsermstrVo(loginService.getCurrentUserName()).getP().getNachn() + "回答。");
			button = "回答";
		} else if (processMode == DictConsts.TIH_TAX_APPROACH_3) {
			// 协助
			subject.append("[益海嘉里]税务信息平台-提问申请单流程协助通知");
			body.append(title + "请尽快处理此流程。");
			button = "协助";
		} else if (processMode == DictConsts.TIH_TAX_APPROACH_4) {
			// 转签
			subject.append("[益海嘉里]税务信息平台-提问申请单流程转签通知");
			body.append(title + "请尽快处理此流程。");
			button = "转签";
		} else if (processMode == DictConsts.TIH_TAX_APPROACH_5) {
			// 反馈
			subject.append("[益海嘉里]税务信息平台-提问申请单流程协助反馈通知");
			body.append(title + "请尽快处理此流程。");
			button = "反馈";
		} else if (processMode == DictConsts.TIH_TAX_APPROACH_6) {
			// 拒绝
			subject.append("[益海嘉里]税务信息平台-提问申请单流程转签拒绝通知");
			body.append(title + "请尽快处理此流程。");
			button = "拒绝";
		} else if (processMode == DictConsts.TIH_TAX_APPROACH_7) {
			// 驳回
			subject.append("[益海嘉里]税务信息平台-提问申请单流程回答驳回通知");
			body.append(title + "请尽快处理此流程。");
			button = "驳回";
		} else if (processMode == DictConsts.TIH_TAX_APPROACH_8) {
			// 通过,需要给多个人发送,查询history people. 所以要使用单独的sendEmailForOver方法.
			subject.append("[益海嘉里]税务信息平台-提问申请单流程结束通知");
			body.append(title + "此流程已处理结束。");
			button = "通过";

			NotificationVo overEmailNoticeVo = getOverEmailNoticeVo(wfIns, subject.toString(), body.toString());
			noticeVos.add(overEmailNoticeVo);// 流程结束后给中间节点处理人的邮件

			NotificationVo recipientNoticeVo = new NotificationVo();
			recipientNoticeVo.setReceiverList(recipientsName);
			recipientNoticeVo.setTypeId(workflowNum);
			recipientNoticeVo.setTitle(subject.toString());
			recipientNoticeVo.setContent(body.toString());
			recipientNoticeVo.setNoticeExt(noticeExt);
			noticeVos.add(recipientNoticeVo);// 给默认收件人邮件

			this.noticeService.sendNoticeForTask(requestForm, noticeVos);
			return;
		}

		if (recipientsSelectList.size() > 0) {
			for (WfInstancemstrProperty wfp : wfIns.getWfInstancemstrProperties()) {
				if (WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT.equals(wfp.getName())) {
					WfInstancemstrProperty wfpRecipientNames = wfp;
					wfpRecipientNames.setValue(recipientsSelectList.toString().replace("[", "").replace("]", "").replace(" ", "").trim());
					this.em.merge(wfp);
					break;
				}
			}
		}

		// 给收件人发送email
		String recipientsRealName = userCommonService.getUsermstrVo(authorizer).getP().getNachn();
		String recipientsSubject = ("[益海嘉里]税务信息平台-提问处理流程-'" + processDlgParamMap.get("queHead") + "'");
		String recipientsBody = "";
		if ("回答".equals(button)) {
			recipientsBody = processDlgParamMap.get("createUser").toString() + "提的问题，已被"
					+ userCommonService.getUsermstrVo(loginService.getCurrentUserName()).getP().getNachn() + button + "。";
		} else {
			recipientsBody = processDlgParamMap.get("createUser").toString() + "提的问题，已被"
					+ userCommonService.getUsermstrVo(loginService.getCurrentUserName()).getP().getNachn() + button + "给" + recipientsRealName + "。";
		}
		NotificationVo recipientNoticeVo = new NotificationVo();
		recipientNoticeVo.setReceiverList(recipientsName);
		recipientNoticeVo.setTypeId(workflowNum);
		recipientNoticeVo.setTitle(recipientsSubject);
		recipientNoticeVo.setContent(recipientsBody);
		recipientNoticeVo.setNoticeExt(noticeExt);
		noticeVos.add(recipientNoticeVo);// 给默认收件人的邮件
		// 最后调用Email发送信息,发送给下个节点的处理人
		List<String> userName = new ArrayList<String>();
		userName.add(authorizer);
		NotificationVo noticeVo = new NotificationVo();
		noticeVo.setReceiverList(userName);
		noticeVo.setTypeId(workflowNum);
		noticeVo.setTitle(subject.toString());
		noticeVo.setContent(body.toString());
		noticeVos.add(noticeVo);// 给下个节点处理人的邮件
		this.noticeService.sendNoticeForTask(requestForm, noticeVos);
	}

	/**
	 * <p>
	 * Description: 根据workflowNumber到fileNet查询当前的运行节点
	 * </p>
	 */
	public String queryNowWorkflowPlace(String workflowNumber) throws VWException {
		String nowWorkflowPlace = d.getWorkStepNameById(workflowNumber);
		logger.info("service409:" + nowWorkflowPlace);
		return nowWorkflowPlace;
	}

	public List<WfInstancemstrProperty> getwfips(Long id) {
		String sql = "select wfip from WfInstancemstrProperty wfip where wfip.wfInstancemstr.id = " + id + "";
		return em.createQuery(sql).getResultList();
	}

	public List<WfStepmstr> getwfstepmatrs(Long id) {
		String sql = "select wfstep from WfStepmstr wfstep where wfstep.wfInstancemstr.id = " + id + "";
		return em.createQuery(sql).getResultList();
	}

	/**
	 * <p>
	 * Description: 判断转签人是否有拒绝操作,Bean方法使用此方法判断.判断依据是此用户是否有过协助和回答的操作,有就返回一个false,没有就返回true.
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	public boolean queryAssignerHaveRefuse(Long wfInsId) {
		boolean assignerHaveRefuse = false;
		String jpql = "Select wfs from WfStepmstr as wfs where wfs.wfInstancemstr.id=" + wfInsId + "  " + " AND wfs.createdBy='"
				+ loginService.getCurrentUserName() + "' " + " AND (wfs.dealMethod = '" + DictConsts.TIH_TAX_APPROACH_3 + "'  OR wfs.dealMethod = '"
				+ DictConsts.TIH_TAX_APPROACH_2 + "') " + " AND wfs.name = '" + WorkflowConsts.ASKWORKFLOW_ASSIGNER_STEP + "' ";
		List<WfStepmstr> list = this.em.createQuery(jpql).getResultList();
		if (list.isEmpty()) {
			assignerHaveRefuse = true;
		}
		logger.info("判断转签人是否有拒绝操作的布尔值:" + assignerHaveRefuse);
		return assignerHaveRefuse;
	}

	// ===============================================JPQL=================================================
	// 下面4个方法,分别是主表,主表属性表,步骤表,步骤属性表的保存方法
	public void saveToWfIns(WfInstancemstr wfIns) {
		em.persist(wfIns);
	}

	public void saveToWfInsPro(WfInstancemstrProperty wfInsPro) {
		em.persist(wfInsPro);
	}

	public void saveToStep(WfStepmstr wfStep) {
		em.persist(wfStep);
	}

	public void saveToStepPro(WfStepmstrProperty wfStepPro) {
		em.persist(wfStepPro);
	}

	/**
	 * @param wfInsPro
	 *            更新主表属性表
	 */
	public void updateWfInsProAll(WfInstancemstrProperty wfInsPro) {
		em.merge(wfInsPro);
	}

	/**
	 * @param wfIns
	 *            更新主表
	 */
	public void updateWfInsAll(WfInstancemstr wfIns) {
		em.merge(wfIns);
	}

	/**
	 * 根据主表wfins来查wfs步骤表
	 * 
	 * @param wfIns
	 *            主表对象
	 * @return 步骤表对象
	 */
	@SuppressWarnings("unchecked")
	public WfStepmstr queryLastWfsByWfIns(WfInstancemstr wfIns) {
		String jpql = "Select wfs from WfStepmstr as wfs where wfs.wfInstancemstr.id=" + wfIns.getId() + " order by wfs.id desc";
		List<WfStepmstr> list = this.em.createQuery(jpql).getResultList();
		return list.isEmpty() ? null : list.get(0);
	}

	/**
	 * @param wfIns
	 *            根据ID查询提问人
	 * @return 提问人字符串
	 */
	public String queryAsker(WfInstancemstr wfIns) {
		String jpql = "Select w.createdBy from WfInstancemstr w where w.id=" + wfIns.getId() + "";
		return this.em.createQuery(jpql).getResultList().get(0).toString();
	}

	/**
	 * @param wfIns
	 * @param judgeName
	 *            判断名字(数据库的key) 用于先查询出对应的流程实例属性表的对应数据,然后更新到数据库.
	 * @return
	 */
	public WfInstancemstrProperty queryWfInsProByWfIns(WfInstancemstr wfIns, String judgeName) {
		String jpql = "select wp from WfInstancemstrProperty wp where wp.wfInstancemstr.id=" + wfIns.getId() + " and wp.name='" + judgeName + "' ";
		return (WfInstancemstrProperty) em.createQuery(jpql).getResultList().get(0);
	}

	public WfStepmstr queryLastStep(WfInstancemstr w) {
		return defaultWorkflowImpl.getLastStep(w);
	}

	// 处理授权
	private String processAuthorizer(String userName) {
		return this.userCommonService.getAuthorizer(userName, requestForm);
	}

	@SuppressWarnings("unchecked")
	private NotificationVo getOverEmailNoticeVo(WfInstancemstr wfIns, String subject, String body) {
		List<String> userName = new ArrayList<String>();
		// 动作为反馈,驳回,通过的,不需要Email.
		String jpql = "SELECT DISTINCT s.createdBy from WfStepmstr s where s.wfInstancemstr.id=" + wfIns.getId() + " " + " AND s.dealMethod <> '"
				+ DictConsts.TIH_TAX_APPROACH_5 + "' " + " AND s.dealMethod <> '" + DictConsts.TIH_TAX_APPROACH_7 + "' " + " AND s.dealMethod <> '"
				+ DictConsts.TIH_TAX_APPROACH_8 + "' ";
		userName = this.em.createQuery(jpql).getResultList();
		NotificationVo noticeVo = new NotificationVo();
		noticeVo.setReceiverList(userName);
		noticeVo.setTypeId(wfIns.getNo());
		noticeVo.setTitle(subject);
		noticeVo.setContent(body);
		return noticeVo;

	}

	// ====================================公用代码===================================================
	// 1表
	private WfInstancemstr addWfInsCommonCode(String workflowNum, String status, String remarks, String importance, String urgency) {
		WfInstancemstr wfInsM = new WfInstancemstr();
		wfInsM.setNo(workflowNum); // 工作流数字标志
		wfInsM.setType(DictConsts.TIH_TAX_REQUESTFORM_3); // 流程类型申请单类型:提问申请单
		wfInsM.setRequestBy(loginService.getCurrentUserName());
		wfInsM.setSubmitDatetime(new Date());
		wfInsM.setStatus(status); // 当前处理类型:处理中
		wfInsM.setDefunctInd("N");
		wfInsM.setCreatedBy(loginService.getCurrentUserName());
		wfInsM.setCreatedDatetime(new Date()); // 日期
		wfInsM.setUpdatedBy(loginService.getCurrentUserName());
		wfInsM.setUpdatedDatetime(new Date());
		wfInsM.setRemarks(remarks);
		wfInsM.setImportance(importance);
		wfInsM.setUrgency(urgency);
		this.saveToWfIns(wfInsM);
		return wfInsM;
	}

	private void updateWfInsCommonCode(WfInstancemstr wfIns, String defunctInd, String status, String remarks, String importance, String urgency) {
		wfIns.setDefunctInd(defunctInd);
		wfIns.setStatus(status);
		wfIns.setSubmitDatetime(new Date());
		wfIns.setUpdatedBy(loginService.getCurrentUserName());
		wfIns.setUpdatedDatetime(new Date());
		if (remarks != null && importance != null && urgency != null) {
			wfIns.setImportance(importance);
			wfIns.setUrgency(urgency);
			wfIns.setRemarks(remarks);
		}
		this.updateWfInsAll(wfIns);
	}

	// 2表
	private void addWfInsProCommonCode(WfInstancemstr wfInsX, String sqlNameX, String sqlValueX) {
		WfInstancemstrProperty wfInsProX = new WfInstancemstrProperty();
		wfInsProX.setWfInstancemstr(wfInsX);
		wfInsProX.setName(sqlNameX);
		wfInsProX.setValue(sqlValueX); // 问题附件ID列表
		this.saveToWfInsPro(wfInsProX);
	}

	private void updateWfInsProCommonCode(WfInstancemstr wfInsN, String sqlName, String sqlValue) {
		WfInstancemstrProperty wfInsProA = this.queryWfInsProByWfIns(wfInsN, sqlName);
		wfInsProA.setValue(sqlValue);
		this.updateWfInsProAll(wfInsProA);
	}

	private void addWfInsProExtraCommonCode(WfInstancemstr wfInsM, Map<String, Object> createDlgParamMap, String recipientNames) {
		this.addWfInsProCommonCode(wfInsM, DictConsts.TIH_TAX_REGION, createDlgParamMap.get("region").toString());
		this.addWfInsProCommonCode(wfInsM, DictConsts.TIH_TAX_TYPE, createDlgParamMap.get("taxType").toString());
		this.addWfInsProCommonCode(wfInsM, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_QUESTIONHEAD, createDlgParamMap.get("queHead").toString());
		this.addWfInsProCommonCode(wfInsM, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT, recipientNames);
		this.addWfInsProCommonCode(wfInsM, WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, DictConsts.TIH_TAX_REQUESTFORM_3);
	}

	private void updateWfInsProExtraCommonCode(WfInstancemstr wfIns, Map<String, Object> createDlgParamMap, String recipientNames) {
		this.updateWfInsProCommonCode(wfIns, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_RECIPIENT, recipientNames);
		this.updateWfInsProCommonCode(wfIns, DictConsts.TIH_TAX_REGION, createDlgParamMap.get("region").toString());
		this.updateWfInsProCommonCode(wfIns, DictConsts.TIH_TAX_TYPE, createDlgParamMap.get("taxType").toString());
		this.updateWfInsProCommonCode(wfIns, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_QUESTIONHEAD, createDlgParamMap.get("queHead").toString());
		this.updateWfInsProCommonCode(wfIns, WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, DictConsts.TIH_TAX_REQUESTFORM_3);
	}

	// 3表
	private WfStepmstr addWfStepCommonCode(WfInstancemstr wfIns, long stepId, String name, String code, String chargedBy, String dealMethod) {
		WfStepmstr wfsM = new WfStepmstr();
		wfsM.setWfInstancemstr(wfIns);
		wfsM.setFromStepId(stepId); // 上一流程节点为 "提交",因为是新建,所以为0,下次表要存入这条数据的ID.
		wfsM.setName(name);
		wfsM.setCode(code);
		wfsM.setChargedBy(chargedBy); // 接收人!
		wfsM.setCompletedDatetime(new Date());
		wfsM.setDealMethod(dealMethod); // 当前处理方式为:"提交",字典表中codeCat+key
		wfsM.setDefunctInd("N");
		wfsM.setCreatedBy(loginService.getCurrentUserName());
		wfsM.setCreatedDatetime(new Date());
		wfsM.setUpdatedBy(loginService.getCurrentUserName());
		wfsM.setUpdatedDatetime(new Date());
		this.saveToStep(wfsM);
		return wfsM;
	}

	private WfStepmstr updateWfStepCommonCode(WfStepmstr ws) {
		em.merge(ws);
		return ws;
	}

	// 四表
	private WfStepmstrProperty addWfStepProCommonCode(WfStepmstr step, String name, String value) {

		StringBuilder jpql = new StringBuilder();
		jpql.append(" select wsp from WfStepmstrProperty wsp");
		jpql.append(" where wsp.wfStepmstr.id = ").append(step.getId());
		jpql.append(" and wsp.name = '" + name + "'");
		List<WfStepmstrProperty> wsps = em.createQuery(jpql.toString()).getResultList();
		WfStepmstrProperty wsp = null;
		if (wsps.size() > 0) {
			wsp = wsps.get(0);
			wsp.setValue(value);
			em.merge(wsp);
		} else {
			wsp = new WfStepmstrProperty();
			wsp.setWfStepmstr(step);
			wsp.setName(name);
			wsp.setValue(value);
			em.persist(wsp);
		}

		return wsp;
	}

	private WfStepmstrProperty addWfStepProExtraCommonCode(WfStepmstr wfs, String txtOpionion, StringBuffer documentIds, StringBuffer documentNames) {
		WfStepmstrProperty opionionStepPro = this.addWfStepProCommonCode(wfs, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION, txtOpionion);
		this.addWfStepProCommonCode(wfs, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID, documentIds.toString());
		this.addWfStepProCommonCode(wfs, WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME, documentNames.toString());
		return opionionStepPro;
	}

	private void updateWfStepProCommandCode(WfStepmstrProperty wsp) {
		em.merge(wsp);
	}

	private WfStepmstrProperty updateWfStepProExtraCommonCode(List<WfStepmstrProperty> wfStepmstrProList, String txtOpionion,
			StringBuffer documentIds, StringBuffer documentNames) {
		WfStepmstrProperty opionionStepPro = null;
		for (WfStepmstrProperty wsp : wfStepmstrProList) {
			if (wsp.getName().equals(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION)) {
				wsp.setValue(txtOpionion);
				opionionStepPro = wsp;
			} else if (wsp.getName().equals(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID)) {
				wsp.setValue(documentIds.toString());
			} else {
				wsp.setValue(documentNames.toString());
			}
			this.updateWfStepProCommandCode(wsp);
		}
		return opionionStepPro;
	}

	public List<UsermstrVo> getUserByEmail() {
		if ("".equals(emails)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct u.ad_account from Usermstr u left join cas_usr_p cup on u.AD_ACCOUNT = cup.id left join P p on cup.pernr=p.id where u.defunct_ind <> 'Y' and cup.defunct_ind <> 'Y' and p.defunct_ind <> 'Y'");
		sb.append(" and p.EMAIL IN (");
		sb.append(emails);
		sb.append(")");
		sb.append(" order by u.ad_account");
		List listObjectArray = this.em.createNativeQuery(sb.toString()).getResultList();
		List<UsermstrVo> usermstrVoList = new ArrayList<UsermstrVo>();
		String adAccount = "";
		UsermstrVo uv = null;
		long num = 0l;
		for (int i = 0; i < listObjectArray.size(); i++) {
			adAccount = listObjectArray.get(i).toString();
			if (null != adAccount && !"".equals(adAccount)) {
				num++;
				uv = this.userCommonService.getUsermstrVo(adAccount);
				uv.setId(num);
				usermstrVoList.add(uv);
			}
		}
		return usermstrVoList;
	}

	public void sendEmailToDefaultRecipient(NotificationVo noticeVo) {
		if (noticeVo.getReceiverList() == null || noticeVo.getReceiverList().size() == 0) {
			return;
		}
		noticeService.sendOnlyEmail(noticeVo);
	}

	public static void main(String[] args) {
		List<String> ss = new ArrayList<String>();
		ss.add("s1");
		ss.add("s2");
		ss.add("s3");
		ss.add("s4");
		ss.add("s5");

		System.out.println(ss.toString());
	}

}
