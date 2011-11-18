package com.expressflow.services.humantaskservice;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.expressflow.datastore.PMF;
import com.expressflow.datastore.process.HumanTaskDAO;
import com.expressflow.datastore.process.MobileHumanTaskDAO;
import com.expressflow.engine.commands.SendEmailCommand;
import com.expressflow.engine.commands.SendReminderCommand;
import com.expressflow.engine.xml.Parser;
import com.expressflow.model.HumanTask;
import com.expressflow.model.SendEmail;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class HumanTaskService {
private static Logger log = Logger.getLogger(HumanTaskService.class.getSimpleName());
	
	public String saveHT(String source){
		Document doc = null;
		String result = "";
		try{
			doc = new SAXBuilder().build(new InputSource(new StringReader(source)));
			Element HTXml = doc.getRootElement();
			
			// Save Human Task
			HumanTask HT = (HumanTask)Parser.parse(HTXml);
			HT.persist();
			
			Key efid = HT.getDAO().getEfid();
			result = "humantask/" + efid.getId();
			
		}catch(Exception je){
			result = je.getMessage();
		}
		return result;
	}
	
	public List getHTs(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List<Long> list = new ArrayList<Long>();
		if (user != null) {
			PersistenceManager pm = PMF.getManager();
			Query query = pm.newQuery(HumanTaskDAO.class);
			List<HumanTaskDAO> HTs = (List<HumanTaskDAO>)query.execute();
			for(HumanTaskDAO m : HTs){
				list.add(m.getEfid().getId());
			}
		}
		return list;
	}
	
	public String sendEmailReminder(String source){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String result = "Ok";
		if (user != null) {
			SendEmail send = new SendEmail();
			send.setTo(user.getEmail());
			send.setSubject("expressFlow Engine - Human Task Endpoint");
			String body = "Dear " + user.getNickname() + "!\n\n";
			body += "Your Human Task endpoint was generated at:\n";
			body += source + "\n\n";
			body += "Forward this message to all Participants using your workflow!\n\n";
			Text bodyTxt = new Text(body);
			send.setBody(bodyTxt);
			
			SendReminderCommand command = new SendReminderCommand();
			command.execute(send);
		}
		return result;
	}
}
