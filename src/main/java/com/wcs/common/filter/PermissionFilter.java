package com.wcs.common.filter;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wcs.base.controller.CurrentUserBean;
import com.wcs.base.service.LoginService;
import com.wcs.common.model.Resourcemstr;

/**
 * <p>Project: tih</p>
 * <p>Description: 权限过滤器</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class PermissionFilter implements Filter {
    @EJB
    private LoginService loginService;
    private final static String ERROR_PAGE = "/pageauthority.xhtml";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String projectName = ((HttpServletRequest) request).getContextPath();
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        String userName = loginService.getCurrentUserName();
        if (userName != null && !"".equals(userName) && requestURI != null && !(projectName + "/faces/index.xhtml").equals(requestURI)) {// 不验证首页,因为在页面上已经验证
            // 验证该用户是否具有该页面的权限
            List<Resourcemstr> resourcemstrs = loginService.getCurrentResources();
            if (resourcemstrs != null && resourcemstrs.size() != 0) {
                boolean bb = false;
                for (Resourcemstr r : resourcemstrs) {
                    bb = false;
                    if ((projectName + r.getUri()).equals(requestURI)) {
                        bb = true;
                        break;
                    }
                }
                if (!bb) {
                    ((HttpServletResponse) response).sendRedirect(projectName + ERROR_PAGE);
                    return;
                }
            }
            // 生成树菜单
            Resourcemstr resourcemstr = null;
            if (resourcemstrs != null && resourcemstrs.size() != 0) {
                for (Resourcemstr r : resourcemstrs) {
                    if ((projectName + r.getUri()).equals(requestURI)) {
                        resourcemstr = r;
                        break;
                    }
                }
                if (resourcemstr != null) {
                    CurrentUserBean currentUser = (CurrentUserBean) ((HttpServletRequest) request).getSession().getAttribute("currentUser");
                    if (currentUser != null) {
                        createTree(resourcemstrs, resourcemstr, currentUser);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * <p>Description: 递归生成菜单</p>
     * @param resourcemstrs
     * @param parentResourcemstr
     * @param currentUser
     */
    private void createTree(List<Resourcemstr> resourcemstrs, Resourcemstr parentResourcemstr, CurrentUserBean currentUser) {
        for (Resourcemstr resourcemstr : resourcemstrs) {
            if (parentResourcemstr.getParentId() == 0) {
                currentUser.generateTree(parentResourcemstr.getId());
            }
            if ((long) resourcemstr.getId() == (long) parentResourcemstr.getParentId()) {
                createTree(resourcemstrs, resourcemstr, currentUser);
            }
        }
    }

}
