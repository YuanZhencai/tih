/** * ContactCommonBean.java 
 * Created on 2014年4月10日 下午5:24:44 
 */

package com.wcs.common.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.StringUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.service.ContactCommonService;
import com.wcs.common.service.ContactService;

/** 
* <p>Project: tih</p> 
* <p>Title: ContactCommonBean.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@ManagedBean
@ViewScoped
public class ContactCommonBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ContactCommonBean.class);

	@EJB
	private ContactService contactService;
	@EJB
	private ContactCommonService contactCommonService;
	
	private String updateComponent;
    private String selectionMode;
    private String bean;
    private String method;
    
	private Map<String, Object> filter = new HashMap<String, Object>();

	private ContactVo selectedContactVo = null;
	private ContactVo[] selectedContactVos = null;
	private List<ContactVo> contactVos;

	public ContactCommonBean() {
	}

	@PostConstruct
	public void init() {
		updateComponent = ":contactCommonForm";
		selectionMode = "single";
	}
	
	public void initMode(String component, String mode, String bean, String method) {
		this.updateComponent = component;
		this.selectionMode = mode;
		this.bean = bean;
		this.method = method;
		selectedContactVo = null;
		selectedContactVos = null;
		contactVos = new ArrayList<ContactVo>();
	}

	public void searchContact() {
		filter.put("EQS_defunctInd", "N");
		filter.put("EQS_other", "其他");
		contactVos = contactCommonService.findContactsAndUsersBy(filter);
	}
	
	public void reset() {
		filter.clear();
	}
	
	private void invokeMethodBySingle() {
        if (!StringUtils.isBlankOrNull(bean)) {
        	try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.bean);
                o.getClass().getDeclaredMethod(method, ContactVo.class).invoke(o, new Object[] { selectedContactVo });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
	
	private void invokeMethodByMultiple() {
        if (!StringUtils.isBlankOrNull(bean)) {
        	try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, bean);
                o.getClass().getDeclaredMethod(method, ContactVo[].class).invoke(o, new Object[] { selectedContactVos });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

	public void invoke() {
        if (selectionMode.equals("single")) {
            this.invokeMethodBySingle();
        } else {
            this.invokeMethodByMultiple();
        }
    }

	// ==========================================================GET && SET ===========================================================//
	
	public String getUpdateComponent() {
		return updateComponent;
	}

	public void setUpdateComponent(String updateComponent) {
		this.updateComponent = updateComponent;
	}

	public String getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(String selectionMode) {
		this.selectionMode = selectionMode;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public ContactVo getSelectedContactVo() {
		return selectedContactVo;
	}

	public void setSelectedContactVo(ContactVo selectedContactVo) {
		this.selectedContactVo = selectedContactVo;
	}

	public ContactVo[] getSelectedContactVos() {
		return selectedContactVos;
	}

	public void setSelectedContactVos(ContactVo[] selectedContactVos) {
		this.selectedContactVos = selectedContactVos;
	}

	public List<ContactVo> getContactVos() {
		return contactVos;
	}

	public void setContactVos(List<ContactVo> contactVos) {
		this.contactVos = contactVos;
	}

	
}
