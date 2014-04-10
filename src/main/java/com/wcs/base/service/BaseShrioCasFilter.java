/**
 * MyShrioCasFilter.java
 * Created: 2012-1-11 下午03:23:55
 */
package com.wcs.base.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;


/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:chengchao@wcs-global.com">ChengChao</a>
 */
public class BaseShrioCasFilter implements Filter {

	private Logger logger = Logger.getLogger(BaseShrioCasFilter.class.getName());
	
	private static final String DEFAULT_PASSWORD = "";

	@EJB
	private LoginService loginService;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		logger.log(Level.INFO, BaseShrioCasFilter.class.getName()+" doFilter：" + ((HttpServletRequest) request).getUserPrincipal());
		Object userPrincipal = ((HttpServletRequest) request).getUserPrincipal();
		String userId = userPrincipal == null ? null : userPrincipal.toString();
		HttpSession session = ((HttpServletRequest) request).getSession(true);
		if (session.getAttribute(LoginService.SESSION_KEY_CURRENTUSR) == null && userId != null) {
			session.setAttribute(LoginService.SESSION_KEY_CURRENTUSR, doLogin(userId));
		}
		if(session.getAttribute(LoginService.SESSION_KEY_CURRENTUSR) == null){
			String project=((HttpServletRequest)request).getContextPath();
			((HttpServletResponse) response).sendRedirect(project+"/error.xhtml");
		}else{
			logger.log(Level.INFO, "session:" + session.getId() + ",currentUser is " + session.getAttribute(LoginService.SESSION_KEY_CURRENTUSR));
			filterChain.doFilter(request, response);
		}
	}
	
	private Subject doLogin(String userId) throws IOException {
		try {
			Subject currentUser = SecurityUtils.getSubject();
			if (userId == null) {
			    // not login
				logger.log(Level.INFO, BaseShrioCasFilter.class.getName() + " currentUser.logout()");
				currentUser.logout();
				return null;
			} else {
				logger.log(Level.INFO, "MyShrioCasFilter currentUser.login(" + userId + ")");
				currentUser.login(new UsernamePasswordToken(userId, DEFAULT_PASSWORD));
				// judge is tih user?
				if (loginService.isAppUser(userId)) {
					logger.log(Level.INFO, userId + " is validated user for the app!");
					return currentUser;
				} else {
					logger.log(Level.INFO, userId + " is invalidated user for the app!");
					return null;
				}
			}
		} catch (UnknownAccountException uae) {
		    logger.log(Level.SEVERE, "", uae);
		} catch (IncorrectCredentialsException ice) {
		    logger.log(Level.SEVERE, "", ice);
		} catch (LockedAccountException lae) {
		    logger.log(Level.SEVERE, "", lae);
		} catch (ExcessiveAttemptsException eae) {
		    logger.log(Level.SEVERE, "", eae);
		} catch (AuthenticationException ae) {
		    // unexpected error?
		    logger.log(Level.SEVERE, "", ae);
		}
		return null;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
