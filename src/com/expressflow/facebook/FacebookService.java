/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

package com.expressflow.facebook;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.code.facebookapi.Permission;

import org.apache.log4j.Logger;
import org.json.*;

public class FacebookService {
	
	private FacebookXmlRestClient client;
	
	private static final Logger log = Logger.getLogger(FacebookService.class.getSimpleName());
	
	/* TODO: Set these values accordingly. 
	 * Details at: http://www.facebook.com/developers
	*/
	private String API_KEY = "--- YOUR APP ID HERE ---";
	private String SECRET = "--- YOUR SECRET HERE ---";
	
	public FacebookService(){
		client = new FacebookXmlRestClient(API_KEY, SECRET);
		
	}
	
	public String updateUsersStatusMessage(String status){
		String result = "NOK";
		try{
			if(client.users_hasAppPermission(Permission.STATUS_UPDATE)){
				client.users_setStatus(status);
				result = "OK";
			}
		}
		catch(FacebookException fe){
			log.error(fe.getMessage());
		}
		return result;
	}
	
	public String sendSMSToUser(String message){
		String result = "NOK";
		try{
			if (client.sms_canSend()) {
		        client.sms_send("I can send you text messages now!", null, false);
		        result = "OK";
		    }
		}
		catch(FacebookException fe){
			
		}
		return result;
	}
}
