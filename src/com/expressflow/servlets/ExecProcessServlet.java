package com.expressflow.servlets;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import com.expressflow.datastore.PMF;
import com.expressflow.services.processservice.ProcessService;
import com.expressflow.utils.ProcessStates;
import com.expressflow.jdo.Process;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ExecProcessServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8640966752142329271L;
	private static final Logger log = Logger.getLogger(ExecProcessServlet.class
			.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			response.getOutputStream().println(
					"Sorry, just serving POST requests ;-)");
		} else {
			response.sendRedirect(response.encodeRedirectURL(userService
					.createLoginURL(request.getRequestURI())));
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		performRequest(request, response);
		try {
			response.getOutputStream().println(new JSONObject().put("result", "success").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void performRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			StringBuilder sb = new StringBuilder(request.getRequestURI());
			String processID = sb.substring(6, sb.length());
			if (processID != null) {
				PersistenceManager pm = PMF.get().getPersistenceManager();
				Key k = KeyFactory.createKey(Process.class.getSimpleName(), processID);
				Process process = pm.getObjectById(Process.class, k);
				if (process.getState().equalsIgnoreCase(ProcessStates.DEPLOYED)) {
					try {
						// TaskQueues do not support UserService functions...
//						// Add the process to the flow-processing queue
//						Queue queue = QueueFactory.getQueue("flowprocessor");
//				        queue.add(TaskOptions.Builder.withUrl("/_ah/queue/flowprocessor").param("key", process.getId()));
					
						ProcessService pService = new ProcessService();
						pService.executeProcess(process.getExecXml(),
								request.getParameterMap());
						process.setTimesExecuted(process.getTimesExecuted() + 1);
					} catch (Exception e) {
						log.info(e.getMessage());
					} finally {
						pm.close();
					}
				}
			}
		} else {
			response.sendRedirect(response.encodeRedirectURL(userService
					.createLoginURL(request.getRequestURI())));
		}
	}
}
