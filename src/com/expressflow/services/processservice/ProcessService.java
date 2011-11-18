package com.expressflow.services.processservice;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.expressflow.datastore.PMF;
import com.expressflow.datastore.process.ProcessDAO;
import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.engine.xml.Parser;
import com.expressflow.model.Process;
import com.expressflow.model.Variable;
import com.expressflow.singleton.ProcessSingleton;
import com.expressflow.utils.ProcessStates;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ProcessService {
	private static Logger log = Logger.getLogger(ProcessService.class
			.getSimpleName());

	public String getProcesses() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String result = "NOK";
		PersistenceManager pm = PMF.getManager();
		if (user != null) {
			Element xProcesses = new Element("processes");
			Query query = pm.newQuery(ProcessDAO.class);
			query.setFilter("creator == creatorParam && access == accessParam");
			query.declareParameters("String creatorParam, String accessParam");
			try {
				List<ProcessDAO> processes = (List<ProcessDAO>) query
						.execute(user.getNickname(), "private");
				if (!processes.isEmpty()) {
					for (ProcessDAO p : processes) {
						Element process = new Element("processElement");
						process.setAttribute("name", p.getName());
						process.setAttribute("accessDate", p.getAccessDate());
						process.setAttribute("creationDate",
								p.getCreationDate());
						process.setAttribute("creator", p.getCreator());
						process.setAttribute("state", p.getState());
						process.setAttribute("access", p.getAccess());
						process.setAttribute("timesExecuted", p.getTimesExecuted());
						process.setAttribute("id", p.getId());
						process.addContent(p.getXml());
						xProcesses.addContent(process);
					}
				}
			} catch (Exception e) {
				log.warning(e.getMessage());
			}
			XMLOutputter serializer = new XMLOutputter();
			serializer.setFormat(Format.getPrettyFormat());
			result = serializer.outputString(xProcesses);
			result = StringEscapeUtils.unescapeXml(result);
		}
		return result;
	}
	
	public String getPublicProcesses() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String result = "NOK";
		PersistenceManager pm = PMF.getManager();
		if (user != null) {
			Element xProcesses = new Element("processes");
			Query query = pm.newQuery(ProcessDAO.class);
			query.setFilter("access == accessParam");
			query.declareParameters("String accessParam");
			try {
				List<ProcessDAO> processes = (List<ProcessDAO>) query
						.execute("public");
				if (!processes.isEmpty()) {
					for (ProcessDAO p : processes) {
						Element process = new Element("processElement");
						process.setAttribute("name", p.getName());
						process.setAttribute("accessDate", p.getAccessDate());
						process.setAttribute("creationDate",
								p.getCreationDate());
						process.setAttribute("creator", p.getCreator());
						process.setAttribute("state", p.getState());
						process.setAttribute("access", p.getAccess());
						process.setAttribute("timesExecuted", p
								.getTimesExecuted().toString());
						process.setAttribute("id", p.getId());
						process.addContent(p.getXml());
						xProcesses.addContent(process);
					}
				}
			} catch (Exception e) {
				log.warning(e.getMessage());
			}
			XMLOutputter serializer = new XMLOutputter();
			serializer.setFormat(Format.getPrettyFormat());
			result = serializer.outputString(xProcesses);
			result = StringEscapeUtils.unescapeXml(result);
		}
		return result;
	}

	public String executeProcess(String source, Map parameters) {
		ProcessSingleton pSingleton = ProcessSingleton.getInstance();
		ModelSingleton mSingleton = ModelSingleton.getInstance();
		Document doc = null;
		try {
			Parser parser = new Parser();
			doc = new SAXBuilder().build(new InputSource(new StringReader(
					source)));
			pSingleton.setDocument(doc);
			Element root = doc.getRootElement();
			List<?> rootInfo = root.getChildren();

			// 1st pass parse - build variables structure
			parser.parseVariables(root);

			// Inject parameters passed from endpoint
			Iterator<?> parIterator = parameters.entrySet().iterator();
			while (parIterator.hasNext()) {
				Map.Entry pEntry = (Map.Entry) parIterator.next();
				Variable v = mSingleton.variables.get(pEntry.getKey());
				if (v != null && pEntry.getValue() != null) {
					v.setValue(((String[]) pEntry.getValue())[0]);
					mSingleton.variables.put(pEntry.getKey().toString(), v);
				}
			}

			// 2nd pass parse - execute the process
			Iterator<?> elementIterator = rootInfo.iterator();
			while (elementIterator.hasNext())
				parser.executingParse((Element) elementIterator.next());
			
		} catch (Exception e) {
			log.warning(e.getMessage());
		}
		String message = "";
		if (doc != null)
			message = "Executed successfully!";
		else
			message = "Execution failed!";
		return message;
	}

	public String deployProcess(String efid) {
		// Document doc = null;
		String message = "";
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			PersistenceManager pm = PMF.getManager();
			try {
				ProcessDAO p = (ProcessDAO) pm.getObjectById(ProcessDAO.class,
						efid);
				p.setState(ProcessStates.DEPLOYED);
				message = "exec/" + p.getId().toString();
			} catch (Exception e) {
				message = "Deploying process failed!";
				e.printStackTrace();
			} finally {
				pm.close();
			}
		}
		return message;
	}

	public String saveProcess(String source) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Document doc = null;
		String message = "";
		if (user != null) {
			try {
				doc = new SAXBuilder().build(new InputSource(new StringReader(
						source)));
				Element processXml = doc.getRootElement();
				Process process = Process.fromXML(processXml);
				process.setState(ProcessStates.SAVED);
				process.setCreator(user.getNickname());
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
				process.setCreationDate(dateFormat.format(now).toString());
				process.setAccessDate(dateFormat.format(now).toString());
				process.persist();
				message = process.getId().toString();
			} catch (Exception e) {
				message = "Saving process failed!";
				e.printStackTrace();
			}
		}
		return message;
	}

	public void deleteProcess(String source) {

	}

	public String updateProcess(String source) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		Document doc = null;
		String message = "";
		if (user != null) {
			PersistenceManager pm = PMF.getManager();
			try {
				doc = new SAXBuilder().build(new InputSource(new StringReader(
						source)));
				Element processXml = doc.getRootElement();
				String efid = processXml.getAttributeValue("efid");
				Process process = Process.fromXML(processXml);
				ProcessDAO p = (ProcessDAO) pm.getObjectById(ProcessDAO.class,
						efid);
				// p.setState(ProcessStates.DEPLOYED);
				p.setAccess(process.getAccess());
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
				p.setAccessDate(dateFormat.format(now).toString());
				XMLOutputter serializer = new XMLOutputter();
				serializer.setFormat(Format.getPrettyFormat());
				p.setXml(serializer.outputString(processXml));
				message = "exec/" + p.getId();
			} catch (Exception e) {
				message = "Updating process failed!";
				e.printStackTrace();
			} finally {
				pm.close();
			}
		}
		return message;
	}

}
