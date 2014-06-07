/** * ContactBean.java 
 * Created on 2014年4月10日 下午5:24:44 
 */

package com.wcs.common.controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.JSFUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.service.ContactCommonService;
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
	@EJB
	private ContactCommonService contactCommonService;
	private Map<String, Object> filter = new HashMap<String, Object>();
	private LazyDataModel<ContactVo> contactVos = null;

	private ContactVo contactVo = new ContactVo();
	private ContactVo selectedContactVo = null;
	private static final ResourceBundle REGEX_BUNDLE = ResourceBundle.getBundle("regex");
	private List<ContactVo> contacts;

	public ContactBean() {
	}

	@PostConstruct
	public void init() {
		searchAllContact();
	}

	public void searchContact() {
		contactVos = findContactsBy(filter);
	}

	public void searchAllContact() {
		contactVos = findContactsBy(new HashMap<String, Object>());
	}

	public LazyDataModel<ContactVo> findContactsBy(final Map<String, Object> filter) {
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

	public void searchContactsAndUsers() {
		filter.put("EQS_defunctInd", "N");
		filter.put("EQS_other", "其他");
		contacts = contactCommonService.findContactsAndUsersBy(filter);
	}

	public LazyDataModel<ContactVo> findContactsAndUsersBy(final Map<String, Object> filter) {
		LazyDataModel<ContactVo> contactVos = new LazyDataModel<ContactVo>() {
			@Override
			public List<ContactVo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
				Integer rowCount = 0;
				List<ContactVo> contactVos = new ArrayList<ContactVo>();
				Integer contactCount = contactService.findContactsCountBy(filter);
				Integer usersCount = contactCommonService.findUsersCountBy(filter);
				rowCount = contactCount + usersCount;
				List<ContactVo> contacts = contactService.findContactsBy(filter, first, pageSize);
				contactVos.addAll(contacts);
				if (contactCount > first) {
					int last = first + pageSize;
					if(last > contactCount){
						List<ContactVo> users = contactCommonService.findUsersBy(filter, 0, last - contactCount);
						contactVos.addAll(users);
					}
				} else {
					List<ContactVo> users = contactCommonService.findUsersBy(filter, first - contactCount, pageSize);
					contactVos.addAll(users);
				}
				setRowCount(rowCount);
				return contactVos;
			}
		};

		return contactVos;
	}

	public void initAddContact() {
		contactVo = new ContactVo();
		contactVo.setDefunctInd("N");
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
		if (!ValidateUtil.validateRegex(context, contactVo.getMobile(), "手机号码：",  REGEX_BUNDLE.getString("PHONE"), "格式错误，请重新填写。")) {
			validate = false;
		}
		if (!ValidateUtil.validateRegex(context, contactVo.getEmail(), "电子邮箱：",  REGEX_BUNDLE.getString("EMAIL"), "格式错误，请重新填写。")) {
			validate = false;
		}
		return validate;
	}

	public void reset() {
		filter.clear();
	}

	public void exportContacts() {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		ServletOutputStream servletOutputStream = null;
        try {
            String fileName = java.net.URLEncoder.encode("通讯录.xls", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			servletOutputStream = response.getOutputStream();
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("EQS_defunctInd", "N");
			contactCommonService.exportContactsReport(map, servletOutputStream);
            servletOutputStream.close();
            JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "导出成功", ""));
        } catch (UnsupportedEncodingException e) {
        	logger.error(e.getMessage(), e);
            JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "导出失败：", "文件名编码错误，请重新操作。"));
            return;
        } catch (Exception e) {
        	JSFUtils.addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "导出失败：", "生成excel错误，请重新操作。"));
        	logger.error(e.getMessage(), e);
            return;
        } finally {
        	try {
				servletOutputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
            FacesContext.getCurrentInstance().responseComplete();
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

	public List<ContactVo> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactVo> contacts) {
		this.contacts = contacts;
	}
	

}
