/** * ContactBean.java 
 * Created on 2014年4月10日 下午5:24:44 
 */

package com.wcs.common.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.service.ContactService;
import com.wcs.tih.util.ValidateUtil;

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
		contactVos = findEmailHistory(filter);
	}

	public void searchAllContact() {
		contactVos = findEmailHistory(new HashMap<String, Object>());
	}

	public LazyDataModel<ContactVo> findEmailHistory(final Map<String, Object> filter) {
		LazyDataModel<ContactVo> contactVos = new LazyDataModel<ContactVo>() {
			@Override
			public List<ContactVo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				Integer rowCount = contactService.findContactsCountBy(filter);
				setRowCount(rowCount);
				return contactService.findContactsBy(filter, first, pageSize);
			}
		};

		return contactVos;
	}

	public void initAddContact() {
		contactVo = new ContactVo();
	}

	public void saveContact() {
		try {
			if (validate(contactVo)) {
				contactService.saveContact(contactVo);
				JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "操作成功", ""));
				JSFUtils.handleDialogByWidgetVar("contactSaveDialogVar", "close");
				searchAllContact();
			}
		} catch (Exception e) {
			JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "操作失败", ""));
			logger.error("操作失败", e);
		}
	}

	public void editContact() {
		try {
			contactVo = new ContactVo();
			ConvertUtils.register(new DateConverter(null), java.util.Date.class);
			BeanUtils.copyProperties(contactVo, selectedContactVo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public boolean validate(ContactVo contactVo) {
		boolean validate = true;
		FacesContext context = FacesContext.getCurrentInstance();
		if (!ValidateUtil.validateRequired(context, contactVo.getUsername(), "人员姓名：")) {
			validate = false;
		}
		return validate;
	}

	public void reset() {
		filter.clear();
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
