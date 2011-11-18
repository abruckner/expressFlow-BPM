package com.expressflow.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.expressflow.datastore.PMF;
import com.expressflow.jdo.MobileHumanTask;
import com.expressflow.jdo.Process;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
public class MobileHumanTaskController {
	
	private static final Logger logger = Logger.getLogger(MobileHumanTaskController.class.getSimpleName());
	
	@RequestMapping(value = "/mobilehumantask/{id}", method = RequestMethod.POST)
	public MobileHumanTask saveMobileHumanTask(@RequestBody MobileHumanTask mht) throws IOException{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (userService.isUserLoggedIn()) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Date now = new Date();
				if(mht.getId().equalsIgnoreCase("123")){
					mht.setId(KeyFactory.keyToString(KeyFactory.createKey(user.getEmail(), now.getTime())));
					mht.setCreator(user.getEmail());
					MobileHumanTask mt = pm.makePersistent(mht);
					return mt;
				} else{
					Key k = KeyFactory.createKey(MobileHumanTask.class.getSimpleName(), mht.getId());
					MobileHumanTask m = pm.getObjectById(MobileHumanTask.class, k);
					m.setMethod(mht.getMethod());
					return pm.makePersistent(m);
				}
	        } finally {
	            pm.close();
	        }
		}
		return null;
	}
	
	@RequestMapping(value = "/mobilehumantask/{id}", method = RequestMethod.GET)
	public Entity getMobileHumanTaskById(@PathVariable String id) throws EntityNotFoundException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("MobileHumanTask", id);
			Entity result = datastore.get(key);
			return result;
		} else
			return null;
	}
	
	@RequestMapping(value = "/mobilehumantasks/",	method = RequestMethod.GET)
	public List<MobileHumanTask> getMobileHumanTasks() throws IOException{
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (userService.isUserLoggedIn()) {
			PersistenceManager pm = PMF.getManager();
			Query query = pm.newQuery(MobileHumanTask.class);
			query.setFilter("creator == creatorParam");
			query.declareParameters("String creatorParam");
			try {
				List<MobileHumanTask> tasks = (List<MobileHumanTask>) query
						.execute(user.getEmail());
				return tasks;
			} catch (Exception e) {
				logger.fatal(e.getMessage());
				return null;
			}
		}
		return null;
	}
}
