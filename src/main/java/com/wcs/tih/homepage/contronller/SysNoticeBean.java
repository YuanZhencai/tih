package com.wcs.tih.homepage.contronller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.controller.vo.NotificationVo;
import com.wcs.tih.homepage.service.SysNoticeService;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean
@ViewScoped
public class SysNoticeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(SysNoticeBean.class);

	@EJB
	private SysNoticeService sysNoticeService;

	private NotificationVo sysNoticeVo = new NotificationVo();
	
	private List<ContactVo> selectedNoticeReceivers = null;


	public SysNoticeBean() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void init() {
	}
	
	public void addSysNotice() {
		sysNoticeVo  = new NotificationVo();
	}

	public void sendSysNotice() {

		if (validateSysNotice(sysNoticeVo)) {
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
		if (!ValidateUtil.validateRequired(context, sysNoticeVo.getTypeId(), "通知主题：")) {
			validate = false;
		}
		if (!ValidateUtil.validateRequired(context, sysNoticeVo.getContent(), "消息内容：")) {
			validate = false;
		}
		List<String> receivers = sysNoticeVo.getReceiverList();
		if (receivers == null || receivers.size() == 0) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "接收人：", "不能为空。"));
		}
		return validate;
	}

	public void selectContacts(ContactVo[] contactVos) {
		List<String> receivers = sysNoticeVo.getReceiverList();
		selectedNoticeReceivers = new ArrayList<ContactVo>();
		for (ContactVo contactVo : contactVos) {
			if (contactVo.getContact() != null) {
				contactVo.setAccount(contactVo.getEmail());
			}
			receivers.add(contactVo.getAccount());
			selectedNoticeReceivers.add(contactVo);
		}
	}

	public NotificationVo getSysNoticeVo() {
		return sysNoticeVo;
	}

	public void setSysNoticeVo(NotificationVo sysNoticeVo) {
		this.sysNoticeVo = sysNoticeVo;
	}

	public List<ContactVo> getSelectedNoticeReceivers() {
		return selectedNoticeReceivers;
	}

	public void setSelectedNoticeReceivers(List<ContactVo> selectedNoticeReceivers) {
		this.selectedNoticeReceivers = selectedNoticeReceivers;
	}
	
	

}
