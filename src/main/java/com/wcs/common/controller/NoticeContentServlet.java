package com.wcs.common.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.EmailVo;
import com.wcs.common.service.NotificationService;


/**
 * Servlet implementation class NoticeContentServlet
 * 邮件模版转换Servlet
 */
@WebServlet("/NoticeContentServlet")
public class NoticeContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    private static final String LANG = "zh_CN";
	
	@EJB 
	private NotificationService notificationService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeContentServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String locale1 = request.getLocale().toString();
		logger.info("locale:" + locale1);
		String locale=LANG;
		
		EmailVo emailVo = new EmailVo();
		if(request.getParameter("receiverIds")!=null&&!"".equals(request.getParameter("receiverIds").toString())){
		    String receiverIds = (String)request.getParameter("receiverIds");
		    if(receiverIds.indexOf(',')==-1){
		        emailVo = notificationService.getRealTimeEmailVo(Integer.parseInt(receiverIds), locale);
		    }else{
		        String[] ids = receiverIds.split(",");
		        emailVo = notificationService.getTimedEmailVo(ids, locale);
		    }
		}
		request.setAttribute("emailVo", emailVo);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/email_template.jsp");//消息模版路径
		requestDispatcher.forward(request,response);
	}

}
