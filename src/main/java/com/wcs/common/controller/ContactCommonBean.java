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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.util.StringUtils;
import com.wcs.common.controller.vo.ContactVo;
import com.wcs.common.service.ContactService;

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
	
	private String updateComponent;
    private String selectionMode;
    private String bean;
    private String method;
    
	private Map<String, Object> filter = new HashMap<String, Object>();

	private ContactVo selectedContactVo = null;
	private ContactVo[] selectedContactVos = null;
	private List<ContactVo> contactVos;

	public ContactCommonBean() {
		logger.info("ContactCommonBean.ContactCommonBean()");
	}

	@PostConstruct
	public void init() {
		logger.info("ContactCommonBean.init()");
		updateComponent = ":up_center:contactLink";
		selectionMode = "nomal";
		searchContact();
	}
	
	public void initMode(String component, String mode, String bean, String method) {
		logger.info("ContactCommonBean.initMode()");
		this.updateComponent = component;
		this.selectionMode = mode;
		this.bean = bean;
		this.method = method;
	}

	public void searchContact() {
		contactVos = contactService.findContactsBy(filter, 0, 0);
	}
	
	public void reset() {
		filter.clear();
	}
	
	public void excuteControllerBeanMethod() {
        if (!StringUtils.isBlankOrNull(bean)) {
            try {
                FacesContext fc = FacesContext.getCurrentInstance();
                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.bean);
                o.getClass().getDeclaredMethod(method).invoke(o);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
	
//	public void excuteControllerBeanMethodByParams() {
//        if (this.bean != null && !"".equals(this.bean)) {
//            try {
//                FacesContext fc = FacesContext.getCurrentInstance();
//                Object o = fc.getApplication().getELResolver().getValue(fc.getELContext(), null, this.bean);
//                // 设置参数类型
//                Class[] parameterTypes = new Class[2];
//                parameterTypes[0] = ContactVo.class;
//                parameterTypes[1] = ContactVo[].class;
//                // 给参数设值
//                Object[] args = new Object[2];
//                args[0] = selectedContactVo;
//                args[1] = selectedContactVos;
//                // 两种执行方式
//                Method method = o.getClass().getMethod(this.method, parameterTypes);
//                method.invoke(o, args);
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }
//        }
//    }


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
