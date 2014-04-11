/** * ContactBean.java 
 * Created on 2014年4月10日 下午5:24:44 
 */

package com.wcs.common.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.beanutils.BeanUtils;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.service.ContactService;

@ManagedBean
@ViewScoped
public class ContactBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ContactBean.class);

	@EJB
	private ContactService contactService;
	private Map<String, Object> filter = new HashMap<String, Object>();
	private LazyDataModel<ContactVo> contactVos = null;

	private ContactVo contactVo = new ContactVo();
	private ContactVo selectedContactVo = null;

	public ContactBean() {
		logger.info("ContactBean.ContactBean()");
	}

	@PostConstruct
	public void init() {
		logger.info("ContactBean.init()");
	}

	public void searchContact() {
		contactVos = contactService.findEmailHistory(filter);
	}

	public void searchAllContact() {
		contactVos = contactService.findEmailHistory(new HashMap<String, Object>());
	}

	public void initAddContact() {
		contactVo = new ContactVo();
	}

	public void saveContact() {
		try {
			contactService.saveContact(contactVo);
			JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "操作成功", ""));
			// JSFUtils.handleDialogByWidgetVar(widgetVar, option);
		} catch (Exception e) {
			JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败", ""));
			logger.error("操作失败", e);
		}
	}

	public void editContact() {
		try {
			contactVo = new ContactVo();
			BeanUtils.copyProperties(selectedContactVo, contactVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	// ============================================GET && SET ===============================================//
	
	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public LazyDataModel<ContactVo> getContactVos() {
		return contactVos;
	}

	public void setContactVos(LazyDataModel<ContactVo> contactVos) {
		this.contactVos = contactVos;
	}

	public ContactVo getContactVo() {
		return contactVo;
	}

	public void setContactVo(ContactVo contactVo) {
		this.contactVo = contactVo;
	}

	public ContactVo getSelectedContactVo() {
		return selectedContactVo;
	}

	public void setSelectedContactVo(ContactVo selectedContactVo) {
		this.selectedContactVo = selectedContactVo;
	}

}
