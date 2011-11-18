package com.expressflow.servlets.tasks;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class TaskListServlet extends HttpServlet{

	private static final long serialVersionUID = -7005732800638289237L;
	
	private static final Logger log = Logger.getLogger(TaskListServlet.class.getName());
	
	public void doGet(HttpServletRequest requ, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			resp.setContentType("text/html");
			resp.getOutputStream()
					.println("<html>"
									+ "<head>"
									+ "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>"
									+ "<title>expressFlow Task list: " + user.getNickname() + "</title>"
									+ "<link rel='stylesheet' href='/tasks/sencha-touch.css' type='text/css'>"
									+ "<script type='text/javascript' src='/tasks/sencha-touch-all.js'></script>"
									+ "<script type='text/javascript' src='/tasks/com/expressflow/MyTaskList.js'></script>"
									+ "</head>"
									+ "<body>"
									+ "</body>" + "</html>");
		}
		else {
			resp.sendRedirect(resp.encodeRedirectURL(userService
					.createLoginURL(requ.getRequestURI())));
		}
	}
}
