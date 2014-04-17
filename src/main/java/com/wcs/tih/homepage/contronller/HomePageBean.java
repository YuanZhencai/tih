package com.wcs.tih.homepage.contronller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.tih.homepage.contronller.vo.LearningGardenAndCommonDataVo;
import com.wcs.tih.homepage.contronller.vo.NewschannelmstrVo;
import com.wcs.tih.homepage.contronller.vo.NoticeVo;
import com.wcs.tih.homepage.service.HomePageService;
import com.wcs.tih.homepage.service.SysNoticeService;
import com.wcs.tih.model.Notificationmstr;
import com.wcs.tih.system.controller.vo.CommonFunctionVO;
import com.wcs.tih.system.controller.vo.CommonLinkVO;
import com.wcs.tih.transaction.controller.vo.WfInstancemstrVo;
import com.wcs.tih.util.ValidateUtil;

/**
 * <p>
 * Project: tih
 * </p>
 * <p>
 * Description: 首页
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
@ManagedBean(name = "homePageBean")
@ViewScoped
public class HomePageBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@EJB
	private HomePageService homePageService;
	@EJB
	private SysNoticeService sysNoticeService;
	
	private List<NewschannelmstrVo> newsChannelList;
	private List<CommonLinkVO> commonLinkList = null;
	private List<CommonFunctionVO> commonFunctionList = null;
	private List<LearningGardenAndCommonDataVo> learningGardenList = null;
	private List<LearningGardenAndCommonDataVo> commonDataList = null;
	private NoticeVo noticeVo;
	private List<WfInstancemstrVo> myWaitDealWithTaskList = null;
	private final static String ERROR_PAGE = "/pageauthority.xhtml";
	private final static String TASK_PAGE = "/faces/transaction/task/index.xhtml";
	private final static String PROJECT_PAGE = "/faces/transaction/project/index.xhtml";
	private List<Notificationmstr> notReadnotices;
	private List<Notificationmstr> readedNotices;
	private LazyDataModel<NoticeVo> notReadLazyModel;
	private LazyDataModel<NoticeVo> readedLazyModel;
	
	private NotificationVo sysNoticeVo = null;

	/**
	 * <p>
	 * Description: 初始化
	 * </p>
	 */
	@PostConstruct
	public void init() {
		Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		newsChannelList = new ArrayList<NewschannelmstrVo>();
		// 每一个可以try
		try {
			queryNotice();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			myWaitDealWithTaskList = homePageService.loadMyWaitDealWithTask(browserLang.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			newsChannelList = homePageService.loadNewsChannel();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			commonLinkList = homePageService.loadCommonLink();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			commonFunctionList = homePageService.loadCommonFunction();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			learningGardenList = homePageService.loadLearningGarden();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		try {
			commonDataList = homePageService.loadCommonData();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	// ======================================Yuan======================================//

	public void setAllReaded() {
		try {
			homePageService.setReadAll();
			queryNotice();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "全部置成已读失败：", "请刷新重试"));
		}
	}
	
	public void addSysNotice() {
		sysNoticeVo = new NotificationVo();
	}

	
	public void sendSysNotice() {
		if(validateSysNotice(sysNoticeVo)) {
			try {
				sysNoticeService.sendSysNotice(sysNoticeVo);
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "操作成功", ""));
				JSFUtils.handleDialogByWidgetVar("sysNoticeDialogVar", "close");
			} catch (Exception e) {
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败：", "请重新操作。"));
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	private boolean validateSysNotice(NotificationVo sysNoticeVo) {
		boolean validate = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if(!ValidateUtil.validateRequired(context , sysNoticeVo.getTitle(), "消息标题：")) {
			validate = false;
		}
		if(!ValidateUtil.validateRequired(context , sysNoticeVo.getContent(), "消息内容：")) {
			validate = false;
		}
		List<String> receivers = sysNoticeVo.getReceiverList();
		if (receivers == null || receivers.size() == 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "接收人：", "不能为空。"));
		}
		return validate;
	}
	// ======================================Yuan======================================//

	/**
	 * <p>
	 * Description: 查看消息并把消息改成已读
	 * </p>
	 * 
	 * @param id
	 *            消息ID
	 */
	public void lookMyNews(long id) {
		homePageService.updateReadInd(id);
		queryNotice();
	}

	/**
	 * <p>
	 * Description: 取得我的已读和为读消息
	 * </p>
	 */
	private void queryNotice() {
		notReadLazyModel = new NoticeVoLazyModel<NoticeVo>("Y");
		readedLazyModel = new NoticeVoLazyModel<NoticeVo>("N");
	}

	/**
	 * <p>
	 * Description: 跳转到常用功能对应的链接
	 * </p>
	 * 
	 * @param id
	 *            资源ID
	 * @return
	 */
	public String gotoCommonFunction(long id) {// 名字改下gotoCommonFunction
		try {
			HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
			HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
			String project = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(project + homePageService.getUri(id));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR_PAGE;
		}
		return "";
	}

	/**
	 * <p>
	 * Description: 从首页进入到任务管理的我的待处理页面
	 * </p>
	 * 
	 * @param taskNumber
	 *            任务号
	 * @return
	 */
	public String queryMyWaitDealWithTask(String taskNumber) {
		try {
			HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
			HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
			String project = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(project + TASK_PAGE + "?taskNumber=" + taskNumber);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR_PAGE;
		}
		return "";
	}

	public String sendRedirect() {
		try {
			String url = "";
			if (noticeVo != null) {
				Notificationmstr notice = noticeVo.getReceiver().getNotificationSender().getNotificationmstr();
				String type = notice.getType();
				if (DictConsts.TIH_TAX_MSG_REFTYPE_1.equals(type)) {
					url = TASK_PAGE + "?taskNumber=" + notice.getTypeId();
				} else if (DictConsts.TIH_TAX_MSG_REFTYPE_2.equals(type)) {
					url = PROJECT_PAGE + "?proNumber=" + notice.getTypeId();
				} else if (DictConsts.TIH_TAX_MSG_REFTYPE_3.equals(type)) {
					url = TASK_PAGE;
				}
			}
			HttpServletResponse response = (HttpServletResponse) (FacesContext.getCurrentInstance().getExternalContext().getResponse());
			HttpServletRequest request = (HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest());
			String project = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(project + url);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR_PAGE;
		}
		return "";
	}

	/**
	 * <p>
	 * Description: 下载
	 * </p>
	 * 
	 * @param documentId
	 *            文档ID
	 * @return
	 */
	public StreamedContent download(String documentId) {
		return homePageService.download(documentId);
	}

	public List<NewschannelmstrVo> getNewsChannelList() {
		return newsChannelList;
	}

	public void setNewsChannelList(List<NewschannelmstrVo> newsChannelList) {
		this.newsChannelList = newsChannelList;
	}

	public List<CommonLinkVO> getCommonLinkList() {
		return commonLinkList;
	}

	public void setCommonLinkList(List<CommonLinkVO> commonLinkList) {
		this.commonLinkList = commonLinkList;
	}

	public List<CommonFunctionVO> getCommonFunctionList() {
		return commonFunctionList;
	}

	public void setCommonFunctionList(List<CommonFunctionVO> commonFunctionList) {
		this.commonFunctionList = commonFunctionList;
	}

	public List<LearningGardenAndCommonDataVo> getLearningGardenList() {
		return learningGardenList;
	}

	public void setLearningGardenList(List<LearningGardenAndCommonDataVo> learningGardenList) {
		this.learningGardenList = learningGardenList;
	}

	public List<LearningGardenAndCommonDataVo> getCommonDataList() {
		return commonDataList;
	}

	public void setCommonDataList(List<LearningGardenAndCommonDataVo> commonDataList) {
		this.commonDataList = commonDataList;
	}

	public List<WfInstancemstrVo> getMyWaitDealWithTaskList() {
		return myWaitDealWithTaskList;
	}

	public void setMyWaitDealWithTaskList(List<WfInstancemstrVo> myWaitDealWithTaskList) {
		this.myWaitDealWithTaskList = myWaitDealWithTaskList;
	}

	public NoticeVo getNoticeVo() {
		return noticeVo;
	}

	public void setNoticeVo(NoticeVo noticeVo) {
		this.noticeVo = noticeVo;
	}

	public List<Notificationmstr> getNotReadnotices() {
		return notReadnotices;
	}

	public void setNotReadnotices(List<Notificationmstr> notReadnotices) {
		this.notReadnotices = notReadnotices;
	}

	public List<Notificationmstr> getReadedNotices() {
		return readedNotices;
	}

	public void setReadedNotices(List<Notificationmstr> readedNotices) {
		this.readedNotices = readedNotices;
	}

	public LazyDataModel<NoticeVo> getNotReadLazyModel() {
		return notReadLazyModel;
	}

	public void setNotReadLazyModel(LazyDataModel<NoticeVo> notReadLazyModel) {
		this.notReadLazyModel = notReadLazyModel;
	}

	public LazyDataModel<NoticeVo> getReadedLazyModel() {
		return readedLazyModel;
	}

	public void setReadedLazyModel(LazyDataModel<NoticeVo> readedLazyModel) {
		this.readedLazyModel = readedLazyModel;
	}

	public NoticeVo getSysNoticeVo() {
		return sysNoticeVo;
	}

	public void setSysNoticeVo(NoticeVo sysNoticeVo) {
		this.sysNoticeVo = sysNoticeVo;
	}

	class NoticeVoLazyModel<T extends NoticeVo> extends LazyDataModel<T> {
		private static final long serialVersionUID = 1L;
		private List<T> list;
		private String readFlag;
		private int queryCount = 0;
		private int last = -1;

		public NoticeVoLazyModel(String readFlag) {
			this.list = new ArrayList<T>();
			this.readFlag = readFlag;
		}

		public List<T> load(int first, int pageSize, String sortField, SortOrder arg3, Map<String, String> arg4) {
			if (first > this.last) {
				if (readFlag != null && !"".equals(readFlag)) {
					Map<String, Object> hashMap = homePageService.loadMyNotices(first, pageSize, readFlag);
					queryCount = Integer.parseInt(hashMap.get("queryCount").toString());
					list.addAll((List<T>) hashMap.get("queryDate"));
					setRowCount(queryCount);
					this.last = first;
				}
			}
			if (queryCount > pageSize) {
				try {
					return new ArrayList<T>(list.subList(first, first + pageSize));
				} catch (IndexOutOfBoundsException e) {
					return new ArrayList<T>(list.subList(first, first + (queryCount % pageSize)));
				}
			}
			return list;
		}

		public T getRowData(String rowKey) {
			for (T nv : list) {
				if (nv.getId().equals(rowKey)) {
					return nv;
				}
			}
			return null;
		}

		public Object getRowKey(T nv) {
			return nv.getId();
		}
	}
}
