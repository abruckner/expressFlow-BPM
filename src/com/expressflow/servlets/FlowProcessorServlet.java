package com.expressflow.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.expressflow.datastore.PMF;
import com.expressflow.jdo.Process;
import com.expressflow.services.processservice.ProcessService;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class FlowProcessorServlet extends HttpServlet {
	private static final long serialVersionUID = -4333457979548248927L;
	
	private static final Logger log = Logger.getLogger(FlowProcessorServlet.class
			.getName()); 
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
			String processId = request.getParameter("key");
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Key k = KeyFactory.createKey(Process.class.getSimpleName(),
						processId);
				Process process = pm.getObjectById(Process.class, k);
				
				ProcessService pService = new ProcessService();
				pService.executeProcess(process.getExecXml(),
						request.getParameterMap());
				process.setTimesExecuted(process.getTimesExecuted() + 1);
			} finally {
				pm.close();
			}
		
	}
}
