package com.expressflow.servlets;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.expressflow.datastore.PMF;
import com.expressflow.datastore.process.MobileHumanTaskDAO;
import com.expressflow.datastore.process.MobileHumanTaskListDAO;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MobileHumanTaskListServlet extends HttpServlet {

	private static final long serialVersionUID = 8053490407535428406L;

	private static final Logger log = Logger
			.getLogger(MobileHumanTaskServlet.class.getName());

	private Map<String, String> formElements = new HashMap<String, String>();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/javascript");

		String taskName = request.getParameter("mobiletasklistname");
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (taskName != null && user != null) {
			Long id = Long.parseLong(taskName);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Key k = KeyFactory.createKey(
					MobileHumanTaskListDAO.class.getSimpleName(), id);
			MobileHumanTaskListDAO mobileHT = pm.getObjectById(
					MobileHumanTaskListDAO.class, k);

			Document doc = null;
			try {
				doc = new SAXBuilder().build(new InputSource(new StringReader(
						mobileHT.getXmlSrc().getValue())));
				Element root = doc.getRootElement();
				
				PrintWriter out = new PrintWriter(response.getWriter());

				out.println(JavaScriptFormatter.ExtSetup("Tasklist"));
				
				if (mobileHT.getName() != null)
					out.println(JavaScriptFormatter.ExtFormBase(
							mobileHT.getMethod(), mobileHT.getName()));
				else
					out.println(JavaScriptFormatter.ExtFormBase(
							mobileHT.getMethod(), "Mobile Human Task List "
									+ taskName));

			}
		catch (JDOMException jde) {
			PrintWriter out = new PrintWriter(response.getWriter());
			out.println("<html>");
			out.println("<head>");
			out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
			out.println("<title>expressFlow - Mobile Human Task - Exception</title>");
			out.println("</head>");
			out.println("<body>");
			out.println(jde.getMessage());
			out.println("</body>");
			out.println("</html>");
		}
		}
	}
}
