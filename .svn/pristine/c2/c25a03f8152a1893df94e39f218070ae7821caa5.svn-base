package com.wcs.tih.interaction.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.wcs.base.controller.CurrentUserBean;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.CommonBean;
import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.scheduler.service.TimeoutEmailService;
import com.wcs.scheduler.vo.WfRemindVo;
import com.wcs.tih.feedback.controller.vo.DictPictureVO;
import com.wcs.tih.feedback.service.FeedBackService;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.interaction.service.SendReportService;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;

import filenet.vw.api.VWException;

@ManagedBean(name = "sendReportBean")
@ViewScoped
public class SendReportBean implements Serializable {

	private static final String NOT_NULL = "不允许为空。";
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private SendReportService sendReportService;
	@ManagedProperty(value = "#{currentUser}")
	private CurrentUserBean currentUser;
	@ManagedProperty(value = "#{commonBean}")
	private CommonBean commonBean;
	@ManagedProperty(value = "#{userCommonBean}")
	private UserCommonBean userCommonBean;

	@EJB
	private FeedBackService feedBackService;
	@EJB
	private TimeoutEmailService timeoutEmailService;

	@SuppressWarnings("rawtypes")
	private List workflowdetail;

	@SuppressWarnings("rawtypes")
	public List getWorkflowdetail() {
		return workflowdetail;
	}

	// create_dialog.xhtml保存页面需要保存的是(都在createDialogMap中):重要程度importance,紧急程度urgency,报表类型reportType,公司company,意见opinion
	private Date createDate; // 时间(Map无,只做页面显示)
	private String createUser; // ,创建人(Map无,只做页面显示)
	private Map<String, String> createDialogMap = new HashMap<String, String>();
	private String companyTitle = "点击右侧按钮可选多个公司启动多个流程."; // p:selectCheckboxMenu使用的动态lable
	private Map<String, String> companyMap = new HashMap<String, String>(); // p:selectCheckboxMenu使用的下拉框列表,使用key值.就是f:selectItems的value值.
	private List<String> companyList = new ArrayList<String>(); // p:selectCheckboxMenu使用的Value.
	private final String reportType = DictConsts.TIH_TAX_REQUESTFORM_4;
	private String excelName;
	private String excelId;

	private WfInstancemstr wfInsDrafts;// 草稿箱数据
	private boolean deleteDrafts = false;// 控制草稿箱处的删除按钮是否显示

	// selectOneMenu中文字和图片一起显示
	private List<DictPictureVO> photos;
	private DictPictureVO selectedhoto;

	private List<DictPictureVO> photosUrgent;
	private DictPictureVO selectedUrgent;

	// OID
	private String oid;
	private WfRemindVo wfRemindVo;

	@PostConstruct
	public void init() {
		selectedhoto = new DictPictureVO();
		photos = new ArrayList<DictPictureVO>();
		photos.add(new DictPictureVO("一般", "TIH.TAX.WORKFLOWIMPORTANCE.1", "important3.png"));
		photos.add(new DictPictureVO("重要", "TIH.TAX.WORKFLOWIMPORTANCE.2", "important2.png"));
		photos.add(new DictPictureVO("非常重要", "TIH.TAX.WORKFLOWIMPORTANCE.3", "important1.png"));

		selectedUrgent = new DictPictureVO();
		photosUrgent = new ArrayList<DictPictureVO>();
		photosUrgent.add(new DictPictureVO("一般", "TIH.TAX.WORKFLOWURGENCY.1", "urgent3.png"));
		photosUrgent.add(new DictPictureVO("紧急", "TIH.TAX.WORKFLOWURGENCY.2", "urgent2.png"));
		photosUrgent.add(new DictPictureVO("非常紧急", "TIH.TAX.WORKFLOWURGENCY.3", "urgent1.png"));
	}

	public void initAddReport() {
		logger.info("[initAddReport]");
		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_4, DictConsts.TIH_TAX_REQUESTFORM_4, null);
	}

	/**
	 * Description:直接获取多个公司
	 * 
	 * @param com
	 */
	public void setCompanys(CompanyManagerModel[] com) {
		for (CompanyManagerModel vo : com) {
			String company = vo.getStext();
			String companyId = vo.getOid();
			companyList.add(companyId);
			companyMap.put(company, companyId);
		}
		if (companyMap.size() > 0) {
			this.companyTitle = "公司列表,可下拉进行编辑.";
		}
	}

	/**
	 * Description:create_dialog.xhtml页面提交流程的方法体,并保存数据
	 */
	public void createDialogSumbitData() {
		boolean haveError = this.validateCreateDlgValue();
		if (haveError) {
			return;
		}

		Map<String, String> wfInsProMap = new HashMap<String, String>();

		// 超时邮件申请单类型
		wfInsProMap.put(WorkflowConsts.TIMEOUT_EMAIL_REQUESTFORM_TYPE, DictConsts.TIH_TAX_REQUESTFORM_4);

		wfInsProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE, createDialogMap.get("reportType")); // 报表类型,两种
		wfInsProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID, ""); // 存对应的增值税和另一个税主表ID
		wfInsProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_REPORT_ATTID, ""); // 上传到CE的excel的ID
		wfInsProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_REPORT_ATTNAME, ""); // 上传到CE的excel的Name
		wfInsProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_REPORT_STATISTIC_TIME, ""); // 上传到CE的excel的Name
		Map<String, String> stepProMap = new HashMap<String, String>();
		stepProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_OPINION, createDialogMap.get("opinion")); // 报送说明
		// 循环公司的list,每个公司创建一个流程,在此之前需要传入公司OID集合并判断这些公司的报表处理人是否正确.如果不正确,需要返回一个总的错误信息.
		List<String> errorInfoList = this.sendReportService.validateProcessor(companyList);
		// 如果错误不为空,或者长度不为0.表明有错误,最后要return.
		if (!errorInfoList.isEmpty() || errorInfoList.size() != 0) {
			for (String errorInfo : errorInfoList) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorInfo, ""));
			}
			return;
		}

		boolean allSuccess = true;
		for (String oid : companyList) {
			wfInsProMap.put(WorkflowConsts.SENDREPORT_SQLKEY_COMPANY, oid); // 公司
			String companyName = this.sendReportService.findCompanyName(oid);
			String remarks = companyName + "," + commonBean.getValueByDictCatKey(createDialogMap.get("reportType").toString().trim());
			// 这里要根据公司,报表类型来获取唯一性的公司报表处理人
			String userNameArr[] = this.sendReportService.queryProcessor(oid);
			if (userNameArr[0].equals("error")) {
				allSuccess = false;
				String companyNameX = this.sendReportService.queryCompanyNameById(oid);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, companyNameX + "的报送报表流程未成功!其" + userNameArr[1], ""));
				continue;
			}
			String userName = userNameArr[1];
			try {
				sendReportService.createDialogSumbitAndSaveData(wfInsProMap, stepProMap, userName, remarks, selectedhoto.getCode(),
						selectedUrgent.getCode(), wfRemindVo);
			} catch (VWException e) {
				logger.error(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "提交流程：", "创建流程失败，请联系系统管理员!"));
			}
		}
		if (allSuccess) {
			RequestContext.getCurrentInstance().addCallbackParam("sendReportInfo", "yes");
			this.createDialogClearPage();
			// 如果是草稿箱处的提交就需要删除掉那些数据
			if (null != wfInsDrafts || deleteDrafts) {
				this.sendReportService.deleteDrafts(wfInsDrafts);
			}
			wfInsDrafts = null;// 提交数据后设置wfInsDrafts为null.
			// 刷新主页面
			try {
				com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
				logger.error(e.getMessage(), e);
			}
		}
	}

	// 进入草稿箱
	public void showInfoToDrafts() {
		this.createDialogClearPage();
		List<WfInstancemstrProperty> listPro = wfInsDrafts.getWfInstancemstrProperties();
		for (WfInstancemstrProperty wp : listPro) {
			if (wp.getName().equals("companyNameAndOid")) {
				if (!wp.getValue().trim().equals("")) {
					String[] companyInfo = wp.getValue().split(",");
					if (companyInfo.length > 0) {
						for (int i = 0; i < companyInfo.length; i++) {
							companyMap.put(sendReportService.getCompanyNameByOid(companyInfo[i]), companyInfo[i]);
							companyList.add(companyInfo[i]);
						}
						this.companyTitle = "公司列表,可下拉进行编辑.";
					}
				}
			} else {
				createDialogMap.put(wp.getName(), wp.getValue());
			}
		}
		selectedhoto = new DictPictureVO();
		String importance = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1;
		if (wfInsDrafts.getImportance() != null) {
			importance = wfInsDrafts.getImportance();
		}
		selectedhoto.setCode(importance);
		selectedUrgent = new DictPictureVO();
		String urgency = DictConsts.TIH_TAX_WORKFLOWURGENCY_1;
		if (wfInsDrafts.getUrgency() != null) {
			urgency = wfInsDrafts.getUrgency();
		}
		selectedUrgent.setCode(urgency);
		this.deleteDrafts = true;

		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_4, DictConsts.TIH_TAX_REQUESTFORM_4, wfInsDrafts.getId());
	}

	// 删除草稿箱
	public void deleteDrafts() {
		this.sendReportService.deleteDrafts(wfInsDrafts);
		RequestContext.getCurrentInstance().addCallbackParam("sendReportInfo", "yes");
		this.createDialogClearPage();
		// 刷新主页面
		try {
			com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
	}

	// 保存到草稿箱
	public void saveDrafts() {
		boolean haveError = this.validateCreateDlgValue();
		if (haveError) {
			return;
		}
		// 重要程度,紧急程度,报表类型,公司集合,报送说明,五条数据全部存到wfInsPro中.以MAP形式存储,
		Map<String, String> map = new HashMap<String, String>();
		map.put("reportType", createDialogMap.get("reportType"));
		map.put("opinion", createDialogMap.get("opinion"));
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String, String> tempMap : companyMap.entrySet()) {
			for (String oid : companyList) {
				if (tempMap.getValue().equals(oid)) {
					str.append(tempMap.getKey() + ",");
				}
			}
		}
		String remarks = str.toString().trim() + commonBean.getValueByDictCatKey(createDialogMap.get("reportType"));
		if (remarks.length() > 600) { // 控制remarks的长度，数据库中只有2000个字符
			remarks = remarks.substring(0, 600);
		}
		map.put("companyNameAndOid", companyList.toString().replace("[", "").replace("]", "").replace(" ", ""));
		// 大分支判断,deleteDrafts是true就表明页面上有删除键,反之没有就是保存操作,所以是!deleteDrafts.
		if (!deleteDrafts) {
			WfInstancemstr saveDrafts = this.sendReportService.saveDrafts(map, remarks, selectedhoto.getCode(), selectedUrgent.getCode());// 保存到数据库
			wfRemindVo.setWfId(saveDrafts.getId());
		} else {
			this.sendReportService.updateDrafts(wfInsDrafts, map, remarks, selectedhoto.getCode(), selectedUrgent.getCode());
			wfRemindVo.setWfId(wfInsDrafts.getId());
		}
		timeoutEmailService.saveWfTimeoutRemind(wfRemindVo);

		RequestContext.getCurrentInstance().addCallbackParam("sendReportInfo", "yes");
		this.createDialogClearPage();
		// 刷新主页面
		try {
			com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Description:清空create_dialog.xhtml页面的数据.
	 */
	public void createDialogClearPage() {
		this.deleteDrafts = false;
		this.createDialogMap.clear();
		this.txtUserOpinion = "";
		this.processUser = "";
		this.attUploadBtnShow = false;
		companyTitle = "点击右侧按钮可选多个公司启动多个流程.";
		companyMap.clear();
		companyList.clear();
		this.excelStatus = "";
		isUploadExcel = false;
		this.excelCollectTime = "";
		this.excelPageCollectTime = null;
		this.selectedhoto = null;
		this.selectedUrgent = null;
	}

	// process_dialog.xhtml,进入页面的时候会获取到一个wfIns.
	private WfInstancemstr wfInsWithIndexSend;
	// 有日期,所以用Object.
	private Map<String, Object> processDialogMap = new HashMap<String, Object>();
	private boolean processPanelShow; // 处理页面块是否显示,如果是指定人,显示true,负责为false
	private boolean passOrNotBtnShow; // 通过和不通过按钮,
	private boolean sumbitButtonShow; // 提交按钮是否显示.这个是给报表处理岗用的.至于下方的dataTable可用不可用,也用这个参数来判断.
	private boolean whoProcessShow;
	private String cancelBtnTxt = "取消"; // 取消按钮显示是取消还是退出.
	private String workPlaceNow = "";
	private static String dnStr = ResourceBundle.getBundle("filenet").getString("tds.users.dn");
	private String txtUserOpinion; // 处理意见
	private String processUser; // 处理人
	private String excelCollectTime; // Excel使用的效验时间,yyyy-MM 格式.提供给效验的时候需要格式化为日期.
	private Date excelPageCollectTime;

	private WfStepmstr lastStep;

	private boolean attUploadBtnShow = false; // 附件是否显示,默认为false.当岗位为SENDREPORT_NAME_PROCESSOR=公司报表处理岗就显示.
	private String fileName;
	private String fileId;

	private boolean isUploadExcel = false;
	private WfInstancemstrProperty wfInsProWithFileId = new WfInstancemstrProperty();
	private WfInstancemstrProperty wfInsProWithFileName = new WfInstancemstrProperty();
	private WfInstancemstrProperty wfInsProWithExcelTime = new WfInstancemstrProperty();

	// 关于Excel的数据
	private String excelUsedOid = "";
	private Object[] excelDataArray = null;
	private String excelStatus = "";

	public void resetTimeData() {
		this.excelPageCollectTime = null;
	}

	/**
	 * Description:根据WfIns查询出所有的流程数据.
	 */
	public void queryProcessDialogByWfIns() {
		this.workflowdetail = this.sendReportService.workflowDetail(this.wfInsWithIndexSend.getNo());
		// 第一个accordionPanel使用的数据
		List<WfInstancemstrProperty> list = null;
		if (this.wfInsWithIndexSend.getWfInstancemstrProperties() != null) {
			list = wfInsWithIndexSend.getWfInstancemstrProperties();
		} else {
			list = this.sendReportService.getwfips(this.wfInsWithIndexSend.getId());
		}
		processDialogMap.put("submitTime", wfInsWithIndexSend.getSubmitDatetime());
		processDialogMap.put("submitUser", userCommonBean.getUserRealName(wfInsWithIndexSend.getRequestBy()));
		processDialogMap.put("importance", wfInsWithIndexSend.getImportance());
		processDialogMap.put("urgency", wfInsWithIndexSend.getUrgency());
		for (WfInstancemstrProperty wp : list) {
			if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_TYPE.equals(wp.getName())) {
				processDialogMap.put("reportType", wp.getValue());
			} else if (WorkflowConsts.SENDREPORT_SQLKEY_COMPANY.equals(wp.getName())) {
				this.excelUsedOid = wp.getValue();
				oid = wp.getValue();
				processDialogMap.put("company", sendReportService.queryCompanyNameById(wp.getValue()));
			} else if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_ATTNAME.equals(wp.getName())) { // 读取excel名称和ID用于页面datatable显示,下载使用.
				wfInsProWithFileName = wp;
			} else if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_ATTID.equals(wp.getName())) {
				wfInsProWithFileId = wp;
			} else if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_STATISTIC_TIME.equals(wp.getName())) {
				this.excelCollectTime = wp.getValue();
				logger.info("excelCollectTime:" + excelCollectTime);
				if (null != excelCollectTime && !excelCollectTime.trim().equals("")) {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
					try {
						this.excelPageCollectTime = sdf1.parse(excelCollectTime);
					} catch (ParseException e) {
						logger.error(e.getMessage(), e);
					}
				}
				wfInsProWithExcelTime = wp;
			} else if (WorkflowConsts.SENDREPORT_SQLKEY_REPORT_SQLTABLEID.equals(wp.getName())) {
				processDialogMap.put("excelTabId", wp.getValue());
				if (null != wp.getValue() && !"".equals(wp.getValue().trim()) && !wp.getValue().trim().matches("^\\d+$")) {
					excelStatus = wp.getValue();
				}
			}
		}
		// 添加数据到reportExcelAtt,最下方的附件信息
		this.fileId = "";
		this.fileName = "";
		if (!wfInsProWithFileId.getValue().trim().equals("")) {
			this.fileId = wfInsProWithFileId.getValue();
			this.fileName = wfInsProWithFileName.getValue();
		}

		this.lastStep = this.sendReportService.queryLastStepInfo(wfInsWithIndexSend);
		this.processUser = lastStep.getChargedBy();
		// 查找出当前节点,并进行判断流程分支
		try {
			logger.info("347wfInsWithIndexSend.getNo():" + wfInsWithIndexSend.getNo());
			String status = this.wfInsWithIndexSend.getStatus();
			if (!DictConsts.TIH_TAX_WORKFLOWSTATUS_3.equals(status) && !DictConsts.TIH_TAX_WORKFLOWSTATUS_4.equals(status)) {
				workPlaceNow = this.sendReportService.queryNowWorkflowPlace(wfInsWithIndexSend.getNo());
			} else {
				workPlaceNow = "";
			}
			logger.info("workPlaceNow:" + workPlaceNow);
		} catch (VWException e1) {
			logger.error("查找当前工作流节点失败!", e1);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "查找当前工作流FileNet节点失败!请联系管理员!", ""));
			return;
		}
		if (WorkflowConsts.SENDREPORT_NAME_AUDITOR.equals(workPlaceNow)) {// 审核岗
			passOrNotBtnShow = true;
			sumbitButtonShow = false;
		} else if (WorkflowConsts.SENDREPORT_NAME_SUPERVISOR.equals(workPlaceNow)) {// 主管岗
			passOrNotBtnShow = true;
			sumbitButtonShow = false;
		} else if (WorkflowConsts.SENDREPORT_NAME_PROCESSOR.equals(workPlaceNow)) { // 报表处理岗
			passOrNotBtnShow = false;
			sumbitButtonShow = true;
			attUploadBtnShow = true;// 附件信息添加按钮.
		} else {
			logger.info("任务完成或者FileNet工作节点名是否被修改!");
		}
		// 进行页面显示和不显示的判断,也根据税种类别显示要加载的Excel的信息,并且判断用户是否应该上传
		// 上述两个,如果在数据库存的是""(空),就进行初始动作,也就是给与报表处理岗一个上传单附件的按钮.
		if (this.currentUser.getCurrentUsermstr().getAdAccount().equals(processUser)) {
			// 如果是处理人
			processPanelShow = true;
			whoProcessShow = false;
		} else {
			// 其他来宾观看
			logger.info("其他来宾观看");
			processPanelShow = false;// 不现实处理页面模块
			passOrNotBtnShow = false;
			sumbitButtonShow = false; // 不显示通过,未通过,提交三个按钮,并把取消修改为退出.
			whoProcessShow = true;
			cancelBtnTxt = "退出";
		}
		if (("").equals(processUser)) {
			whoProcessShow = false;
		}
		processUser = workPlaceNow + " " + userCommonBean.getUserRealName(processUser);
		logger.info("whoProcessShow::" + whoProcessShow);
		wfRemindVo = timeoutEmailService.findWfRemindVo(DictConsts.TIH_TAX_REQUESTFORM_4, DictConsts.TIH_TAX_REQUESTFORM_4,
				wfInsWithIndexSend.getId());

	}

	/**
	 * 显示用. Description:循环使用的步骤表list,页面直接调用Steps循环显示流程的每个步骤信息
	 * 
	 * @return List集合,页面直接诶调用
	 */
	public List<WfStepmstr> getSteps() {
		if (wfInsWithIndexSend == null || wfInsWithIndexSend.getWfStepmstrs().isEmpty()) {
			return null;
		}
		List<WfStepmstr> steps = wfInsWithIndexSend.getWfStepmstrs();
		Collections.sort(steps, new Comparator<WfStepmstr>() {
			public int compare(WfStepmstr o1, WfStepmstr o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		return steps;
	}

	/**
	 * Description:运行流程的方法.
	 * 
	 * @param direction
	 *            有三个值:提交对应send,通过对应pass,驳回对应nopass
	 */
	public void processDialogSumbitData(String direction) {
		boolean haveError = this.validateProcessDlgValue();
		if (haveError) {
			return;
		}
		String workflowNumber = this.wfInsWithIndexSend.getNo();
		// 这里有三个参数,需要了解清楚.roleName===>下个节点的fielnet上的参数名,如 auditer_param!userName===>下个节点指定的 账户名,paramMap.put(roleName, new String[] { "uid=" + userName + "," + dnStr
		// });status=====>路由值,paramMap.put("status", this.status);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String roleName = null;
		String userName = null;
		String status = null;
		String dealMethod = null;
		if ("send".equals(direction)) {
			status = WorkflowConsts.SENDREPORT_STATUS_SEND;
		} else if ("pass".equals(direction)) {
			status = WorkflowConsts.SENDREPORT_STATUS_PASS;
		} else if ("nopass".equals(direction)) {
			status = WorkflowConsts.SENDREPORT_STATUS_NOPASS;
		}
		// 判断从fileNet获取到的岗位.然后结合上面的status.然后指定路由和参数
		if (WorkflowConsts.SENDREPORT_NAME_AUDITOR.equals(workPlaceNow)) {// 审核岗(发起人,结束人)WorkflowConsts.SENDREPORT_PARAM_AUDITOR;
			if (WorkflowConsts.SENDREPORT_STATUS_PASS.equals(status)) {// 结束
				roleName = "over";
				userName = ""; // 结束的话,没人名
				dealMethod = DictConsts.TIH_TAX_APPROACH_10; // 通过
			} else {
				roleName = WorkflowConsts.SENDREPORT_PARAM_PROCESSOR;
				// 这里要查找公司报表处理岗
				String userNameArr[] = this.sendReportService.queryProcessor(excelUsedOid);
				if (userNameArr[0].equals("error")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userNameArr[1], ""));
					return;
				}
				userName = userNameArr[1];
				dealMethod = DictConsts.TIH_TAX_APPROACH_7; // 不通过
			}
		} else if (WorkflowConsts.SENDREPORT_NAME_SUPERVISOR.equals(workPlaceNow)) {
			// 主管岗(中间人)
			if (WorkflowConsts.SENDREPORT_STATUS_PASS.equals(status)) {// 通过,userName为集团报表审核岗,
				roleName = WorkflowConsts.SENDREPORT_PARAM_AUDITOR;
				// 这里是要查找集团报表审核岗
				String userNameArr[] = this.sendReportService.queryAuditor(excelUsedOid);
				if (userNameArr[0].equals("error")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userNameArr[1], ""));
					return;
				}
				userName = userNameArr[1];
				dealMethod = DictConsts.TIH_TAX_APPROACH_8; // 通过
			} else {
				roleName = WorkflowConsts.SENDREPORT_PARAM_PROCESSOR;
				String userNameArr[] = this.sendReportService.queryProcessor(excelUsedOid);
				if (userNameArr[0].equals("error")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, userNameArr[1], ""));
					return;
				}
				userName = userNameArr[1]; // 这里要返回给公司报表处理岗
				dealMethod = DictConsts.TIH_TAX_APPROACH_7; // 不通过
			}
		} else if (WorkflowConsts.SENDREPORT_NAME_PROCESSOR.equals(workPlaceNow)) { // 报表处理岗(填写报表人)roleName=WorkflowConsts.SENDREPORT_PARAM_PROCESSOR;
			roleName = WorkflowConsts.SENDREPORT_PARAM_SUPERVISOR;
			// 查找报表处理岗主管
			userName = this.sendReportService.getSuperadvisor(sendReportService.getCompanyId(oid));
			if (null == userName || ("").equals(userName)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "当前用户未设置主管,请联系管理员!", ""));
				return;
			}
			dealMethod = DictConsts.TIH_TAX_APPROACH_8; // 回答
		} else {
			logger.info("当前用户只能看,或者流程已经结束,或者是一个ERROR:FileNet工作节点名被修改!");
		}
		if (roleName == null || userName == null || status == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "流程运行出现问题,请联系管理员!", ""));
			logger.info("流程运行出现问题,请联系管理员!");
			return;
		}
		if (!("over").equals(roleName)) {
			paramMap.put(roleName, new String[] { "uid=" + userName + "," + dnStr });
		}
		paramMap.put("status", status);

		WfStepmstr step = new WfStepmstr();
		step.setWfInstancemstr(wfInsWithIndexSend);
		step.setUpdatedBy(this.currentUser.getCurrentUsermstr().getAdAccount());
		step.setCreatedBy(this.currentUser.getCurrentUsermstr().getAdAccount());
		step.setDefunctInd("N");
		step.setUpdatedDatetime(new Date());
		step.setCreatedDatetime(new Date());
		step.setChargedBy(userName);
		step.setDealMethod(dealMethod); // 获取提交还是通过还是其他
		step.setCreatedDatetime(new Date());
		step.setCompletedDatetime(new Date());
		step.setFromStepId(lastStep.getId()); // 未开始流程前最后一个
		Map<String, String> stepMap = new HashMap<String, String>();
		stepMap.put(WorkflowConsts.SENDREPORT_SQLKEY_OPINION, this.txtUserOpinion);
		try {
			// 执行工作流.需要进行事务处理!执行的时候还要判断下,如果是公司报表处理岗的话,也就是提交按钮传来的是"send",就执行报表提交动作! 最后同时要更新下报送报表的流程状态数据.
			if (("over").equals(roleName)) {
				this.sendReportService.processSumbitData(paramMap, stepMap, step, workflowNumber, wfInsWithIndexSend);
				// 在这里要更新主表属性表里的excel表id关联的表数据里的状态,修改为完成.
				this.sendReportService.updateExcelStatusToOver(processDialogMap.get("reportType").toString(),
						Long.parseLong(processDialogMap.get("excelTabId").toString()));
			} else if ("send".equals(direction)) {
				boolean isVATa = false;
				if (this.processDialogMap.get("reportType").toString().trim().equals(DictConsts.TIH_TAX_REQUESTFORM_4_2)) {
					isVATa = true;
				}
				Object[] errorInfo = sendReportService.judgeExcelCondition(processDialogMap.get("excelTabId").toString(), this.excelUsedOid,
						excelPageCollectTime, isVATa, lastStep);// 408
				if (errorInfo[0].toString().equals("warn")) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, errorInfo[1].toString() + "新上传的文件信息将覆盖这些数据!", ""));
				} else if (errorInfo[0].toString().equals("error")) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorInfo[1].toString(), ""));
					return;
				}
				// 同时也需要更新或保存数据库Excel数据.
				// 参数,统计时间,将页面上的时间只取年月转换为String,然后再转换为Date.
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(excelPageCollectTime);
				int yearTime = calendar.get(Calendar.YEAR);
				int monthTime = calendar.get(Calendar.MONTH) + 1;
				if (monthTime < 10) {
					excelCollectTime = (yearTime + "-0" + monthTime);
				} else {
					excelCollectTime = (yearTime + "-" + monthTime);
				}
				Date statisticsDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				try {
					statisticsDate = sdf.parse(this.excelCollectTime);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
				// 参数:excel类型
				boolean isVAT = false;
				String longId = null;
				if (this.processDialogMap.get("reportType").toString().trim().equals(DictConsts.TIH_TAX_REQUESTFORM_4_2)) {
					isVAT = true;
				}
				if (this.processDialogMap.get("excelTabId") != null && !this.processDialogMap.get("excelTabId").toString().trim().equals("")) {
					longId = this.processDialogMap.get("excelTabId").toString();
				}
				if (null == excelDataArray || excelDataArray.length == 0) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "您必须上传新的Excel数据文件!", ""));
					return;
				}
				String remarks = excelCollectTime + "," + this.wfInsWithIndexSend.getRemarks().trim();
				if (WorkflowConsts.SENDREPORT_NAME_PROCESSOR.equals(workPlaceNow)) {
					feedBackService.updateRemarks(this.wfInsWithIndexSend.getNo(), remarks);
				}
				this.sendReportService.saveOrUpdateExcelData(excelDataArray, isVAT, longId, statisticsDate, this.wfInsWithIndexSend.getId(), remarks);
				excelDataArray = null;

				// 需要提交或者更新fileId,Name,
				boolean isUpdate = false;
				if (null != excelId && !excelId.trim().equals("")) {
					wfInsProWithFileId.setValue(excelId);
					wfInsProWithFileName.setValue(excelName);
					isUpdate = true;
				}
				wfInsProWithExcelTime.setValue(this.excelCollectTime);
				this.sendReportService.processSumbitData(paramMap, stepMap, step, workflowNumber, wfInsProWithFileId, wfInsProWithFileName, isUpdate,
						wfInsProWithExcelTime, errorInfo);
			} else {
				this.sendReportService.processSumbitData(paramMap, stepMap, step, workflowNumber);
			}
		} catch (VWException e) {
			logger.error(e.getMessage(), e);
			logger.info("执行工作流失败!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "执行工作流失败!请联系管理员!", ""));
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.info("发生异常,请联系系统管理员!");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "流程运行出现问题,请联系管理员!", ""));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("流程运行成功!请重新查询!", ""));
		RequestContext.getCurrentInstance().addCallbackParam("applyInfo", "yes");
		// 刷新主页面
		try {
			com.wcs.tih.transaction.controller.helper.TaskRefreshHelper.refreshTask(FacesContext.getCurrentInstance());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
	}

	private UploadedFile upFile;// 上传文件
	private int fileTempNum;
	private int countInt = 1;

	public void uploadCount() {
		if (null == excelPageCollectTime) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计时间:", NOT_NULL));
			return;
		}
		this.fileId = null;
		this.fileName = null;
		this.excelDataArray = null;
		// 8.6 new code
		boolean isVAT = false;
		if (this.processDialogMap.get("reportType").toString().trim().equals(DictConsts.TIH_TAX_REQUESTFORM_4_2)) {
			isVAT = true;
		}
		logger.info("excelPageCollectTime:" + excelPageCollectTime);
		Object[] errorInfo = sendReportService.judgeExcelCondition(processDialogMap.get("excelTabId").toString(), this.excelUsedOid,
				excelPageCollectTime, isVAT, lastStep);
		if (errorInfo[0].equals("warn")) {
			if (countInt == 1) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "警告:请慎重!" + errorInfo[1].toString() + "重新上传文件将覆盖这些数据!如果确定,请再次点击上传/替换按钮.", ""));
				countInt = 2;
				return;
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "警告:请慎重!" + errorInfo[1].toString() + "重新上传文件将覆盖这些数据!", ""));
		} else if (errorInfo[0].equals("error")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorInfo[1].toString(), ""));
			return;
		}
		// 8.6 new code
		this.fileTempNum = 0;
		RequestContext.getCurrentInstance().addCallbackParam("dateInfo", "yes");
	}

	public void uploadSingleExcel(FileUploadEvent event) {
		if (fileTempNum != 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "上传多个附件只使用并上传第一个有效附件!", ""));
			return;
		} else {
			fileTempNum++;
		}

		upFile = event.getFile();
		if (upFile == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件不能为空", ""));
			return;
		}
		if (upFile.getFileName().length() > 300) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件名称不能超过300个字符!", ""));
			return;
		}
		// 在这里效验Excel...
		try {
			String oid = this.excelUsedOid;
			Date statisticsDate = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(excelPageCollectTime);
			int yearTime = calendar.get(Calendar.YEAR);
			int monthTime = calendar.get(Calendar.MONTH) + 1;
			if (monthTime < 10) {
				excelCollectTime = (yearTime + "-0" + monthTime);
			} else {
				excelCollectTime = (yearTime + "-" + monthTime);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			try {
				statisticsDate = sdf.parse(this.excelCollectTime);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
			// 如果是VAT
			if (this.processDialogMap.get("reportType").toString().trim().equals(DictConsts.TIH_TAX_REQUESTFORM_4_2)) {
				this.excelDataArray = sendReportService.validatorExcel(true, upFile, oid, statisticsDate);
			} else {
				this.excelDataArray = sendReportService.validatorExcel(false, upFile, oid, statisticsDate);
			}
			List<String> errMsgs = (ArrayList<String>) excelDataArray[excelDataArray.length - 1];
			if (errMsgs.size() > 0) {
				for (int i = 0; i < errMsgs.size(); i++) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errMsgs.get(i), ""));
				}
				return;
			}
		} catch (IOException e1) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e1.getMessage() == null ? "请确认上传文件是否是当前所选报表类型文件!" : e1.getMessage(), ""));
			logger.error(e1.getMessage(), e1);
			return;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage() == null ? "请确认上传文件是否是当前所选报表类型文件!" : e.getMessage(), ""));
			logger.error(e.getMessage(), e);
			return;
		}

		Document document;
		try {
			document = sendReportService.addFileCE(upFile);
		} catch (MimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档类型不正确。", ""));
			e.getStackTrace();
			return;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传失败，请重新执行.", ""));
			e.getStackTrace();
			return;
		}
		this.excelId = document.get_Id().toString();
		this.excelName = document.get_Name().toString();
		this.isUploadExcel = true;
		this.fileId = this.excelId;
		this.fileName = this.excelName;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("文档上传成功,请查询!", ""));
	}

	public StreamedContent downloadFile(String fileId) {
		if (fileId == null || fileId.trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "没有可供下载的文件。", ""));
			return null;
		}
		try {
			return sendReportService.downloadFile(fileId);
		} catch (MimeException e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档类型不正确。", ""));
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载出现异常，请联系系统管理员。", ""));
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	private boolean validateProcessDlgValue() {
		boolean haveError = false;
		if (null != txtUserOpinion && txtUserOpinion.length() > 200) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "说明信息：", "长度不允许大于200个字符。"));
			haveError = true;
		}
		if (this.attUploadBtnShow) {// 显示了添加按钮,需要判断是否有附件上传信息,否则报错,提示必须上传附件.见406行.
			if (!this.isUploadExcel && processDialogMap.get("excelTabId").toString().trim().equals("")) { // 如果没有上传文件isUploadExcel=false,并且主表属性表ID为空,就报错.
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "报送清单：", "请上传报送报表Excel文件。"));
				haveError = true;
			}
			if (this.excelPageCollectTime == null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计时间：", NOT_NULL));
				haveError = true;
			}

		}
		return haveError;
	}

	private boolean validateCreateDlgValue() {
		boolean haveError = false;
		if (createDialogMap.get("reportType") == null || createDialogMap.get("reportType").trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "报表类型：", NOT_NULL));
			haveError = true;
		}
		if (createDialogMap.get("opinion") == null || createDialogMap.get("opinion").trim().equals("")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "报送说明：", NOT_NULL));
			haveError = true;
		} else {
			if (createDialogMap.get("opinion").toString().length() > 500) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "报送说明：", "长度不允许大于500个字符。"));
				haveError = true;
			}
		}
		if (companyList.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "所选公司：", NOT_NULL));
			haveError = true;
		}
		if (!timeoutEmailService.validateWfRemindVo(wfRemindVo)) {
			haveError = true;
		}
		return haveError;
	}

	public SendReportBean() {
		// 构造方法
	}

	public Map<String, String> getCreateDialogMap() {
		return createDialogMap;
	}

	public void setCreateDialogMap(Map<String, String> createDialogMap) {
		this.createDialogMap = createDialogMap;
	}

	public Date getCreateDate() {
		createDate = new Date();
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		createUser = userCommonBean.getUserRealName(currentUser.getCurrentUserName());
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public CurrentUserBean getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(CurrentUserBean currentUser) {
		this.currentUser = currentUser;
	}

	public Map<String, Object> getProcessDialogMap() {
		return processDialogMap;
	}

	public void setProcessDialogMap(Map<String, Object> processDialogMap) {
		this.processDialogMap = processDialogMap;
	}

	public String getTxtUserOpinion() {
		return txtUserOpinion;
	}

	public void setTxtUserOpinion(String txtUserOpinion) {
		this.txtUserOpinion = txtUserOpinion;
	}

	public boolean isProcessPanelShow() {
		return processPanelShow;
	}

	public void setProcessPanelShow(boolean processPanelShow) {
		this.processPanelShow = processPanelShow;
	}

	public boolean isPassOrNotBtnShow() {
		return passOrNotBtnShow;
	}

	public void setPassOrNotBtnShow(boolean passOrNotBtnShow) {
		this.passOrNotBtnShow = passOrNotBtnShow;
	}

	public boolean isSumbitButtonShow() {
		return sumbitButtonShow;
	}

	public void setSumbitButtonShow(boolean sumbitButtonShow) {
		this.sumbitButtonShow = sumbitButtonShow;
	}

	public String getCancelBtnTxt() {
		return cancelBtnTxt;
	}

	public void setCancelBtnTxt(String cancelBtnTxt) {
		this.cancelBtnTxt = cancelBtnTxt;
	}

	public String getProcessUser() {
		return processUser;
	}

	public void setProcessUser(String processUser) {
		this.processUser = processUser;
	}

	public WfInstancemstr getWfInsWithIndexSend() {
		return wfInsWithIndexSend;
	}

	public void setWfInsWithIndexSend(WfInstancemstr wfInsWithIndexSend) {
		this.wfInsWithIndexSend = wfInsWithIndexSend;
	}

	public boolean isWhoProcessShow() {
		return whoProcessShow;
	}

	public void setWhoProcessShow(boolean whoProcessShow) {
		this.whoProcessShow = whoProcessShow;
	}

	public CommonBean getCommonBean() {
		return commonBean;
	}

	public void setCommonBean(CommonBean commonBean) {
		this.commonBean = commonBean;
	}

	public String getCompanyTitle() {
		return companyTitle;
	}

	public void setCompanyTitle(String companyTitle) {
		this.companyTitle = companyTitle;
	}

	public Map<String, String> getCompanyMap() {
		return companyMap;
	}

	public void setCompanyMap(Map<String, String> companyMap) {
		this.companyMap = companyMap;
	}

	public List<String> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<String> companyList) {
		this.companyList = companyList;
	}

	public String getReportType() {
		return reportType;
	}

	public UserCommonBean getUserCommonBean() {
		return userCommonBean;
	}

	public void setUserCommonBean(UserCommonBean userCommonBean) {
		this.userCommonBean = userCommonBean;
	}

	public UploadedFile getUpFile() {
		return upFile;
	}

	public void setUpFile(UploadedFile upFile) {
		this.upFile = upFile;
	}

	public boolean getAttUploadBtnShow() {
		return attUploadBtnShow;
	}

	public void setAttUploadBtnShow(boolean attUploadBtnShow) {
		this.attUploadBtnShow = attUploadBtnShow;
	}

	public String getExcelCollectTime() {
		return excelCollectTime;
	}

	public void setExcelCollectTime(String excelCollectTime) {
		this.excelCollectTime = excelCollectTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public WfInstancemstr getWfInsDrafts() {
		return wfInsDrafts;
	}

	public void setWfInsDrafts(WfInstancemstr wfInsDrafts) {
		this.wfInsDrafts = wfInsDrafts;
	}

	public boolean isDeleteDrafts() {
		return deleteDrafts;
	}

	public void setDeleteDrafts(boolean deleteDrafts) {
		this.deleteDrafts = deleteDrafts;
	}

	public Date getExcelPageCollectTime() {
		return excelPageCollectTime;
	}

	public void setExcelPageCollectTime(Date excelPageCollectTime) {
		this.excelPageCollectTime = excelPageCollectTime;
	}

	public String getExcelStatus() {
		return excelStatus;
	}

	public void setExcelStatus(String excelStatus) {
		this.excelStatus = excelStatus;
	}

	public List<DictPictureVO> getPhotos() {
		return photos;
	}

	public void setPhotos(List<DictPictureVO> photos) {
		this.photos = photos;
	}

	public DictPictureVO getSelectedhoto() {
		return selectedhoto;
	}

	public void setSelectedhoto(DictPictureVO selectedhoto) {
		this.selectedhoto = selectedhoto;
	}

	public List<DictPictureVO> getPhotosUrgent() {
		return photosUrgent;
	}

	public void setPhotosUrgent(List<DictPictureVO> photosUrgent) {
		this.photosUrgent = photosUrgent;
	}

	public DictPictureVO getSelectedUrgent() {
		return selectedUrgent;
	}

	public void setSelectedUrgent(DictPictureVO selectedUrgent) {
		this.selectedUrgent = selectedUrgent;
	}

	public WfRemindVo getWfRemindVo() {
		return wfRemindVo;
	}

	public void setWfRemindVo(WfRemindVo wfRemindVo) {
		this.wfRemindVo = wfRemindVo;
	}

}
