package com.expressflow.services.humantaskservice;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.expressflow.datastore.PMF;
import com.expressflow.datastore.process.MobileHumanTaskDAO;
import com.expressflow.engine.commands.SendReminderCommand;
import com.expressflow.engine.xml.Parser;
import com.expressflow.model.MobileHumanTask;
import com.expressflow.model.SendEmail;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MobileHumanTaskService {
	private static Logger log = Logger.getLogger(MobileHumanTaskService.class.getSimpleName());
	
	public String saveMobileHT(String source){
		Document doc = null;
		String result = "";
		try{
			doc = new SAXBuilder().build(new InputSource(new StringReader(source)));
			Element mobileHTXml = doc.getRootElement();
			
			// Save MobileHuman Task
			MobileHumanTask mobileHT = (MobileHumanTask)Parser.parse(mobileHTXml);
			mobileHT.persist();
			
			Key efid = mobileHT.getDAO().getEfid();
			result = "mobilehumantask/" + efid.getId();
			
		}catch(Exception je){
			result = je.getMessage();
		}
		return result;
	}
	
	public String updateMobileHT(String mobilehtid, String processendpoint){
		String result = "";
		PersistenceManager pm = PMF.getManager();
		try{
			Key k = KeyFactory.createKey(MobileHumanTaskDAO.class.getSimpleName(), Long.parseLong(mobilehtid));
			MobileHumanTaskDAO mht = pm.getObjectById(MobileHumanTaskDAO.class, k);
			mht.setMethod(processendpoint);
			pm.makePersistent(mht);
			result = "OK";
		}
		catch(Exception e){
			result = "NOK";
		}
		finally{
			pm.close();
		}
		return result;
	}
	
	public List getMobileHTs(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		List<Long> vector = new ArrayList<Long>();
		if (user != null) {
			PersistenceManager pm = PMF.getManager();
			Query query = pm.newQuery(MobileHumanTaskDAO.class);
			List<MobileHumanTaskDAO> mHTs = (List<MobileHumanTaskDAO>)query.execute();
			for(MobileHumanTaskDAO m : mHTs){
				vector.add(m.getEfid().getId());
			}
		}
		return vector;
	}
	
	public String sendEmailReminder(String source){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String result = "Ok";
		if (user != null) {
			SendEmail send = new SendEmail();
			send.setTo(user.getEmail());
			send.setSubject("expressFlow Engine - Mobile Human Task Endpoint");
			String body = "Dear " + user.getNickname() + "!\n\n";
			body += "Your Mobile Human Task endpoint was generated at:\n";
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
