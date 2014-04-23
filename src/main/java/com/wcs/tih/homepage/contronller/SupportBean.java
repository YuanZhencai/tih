package com.wcs.tih.homepage.contronller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.model.Usermstr;
import com.wcs.tih.homepage.service.SupportService;

@ManagedBean
@ViewScoped
public class SupportBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory.getLogger(SupportBean.class);

	@EJB
	private SupportService supportService;

	private List<Usermstr> supportUsers = null;

	public SupportBean() {
		// TODO Auto-generated constructor stub
	}
	
	@PostConstruct
	public void init() {
		logger.info("SupportBean.init()");
		findSupportUsers();
	}
	
	public void findSupportUsers() {
		supportUsers  = supportService.findSupportUsers();
	}

	public List<Usermstr> getSupportUsers() {
		return supportUsers;
	}

	public void setSupportUsers(List<Usermstr> supportUsers) {
		this.supportUsers = supportUsers;
	}

	
}
