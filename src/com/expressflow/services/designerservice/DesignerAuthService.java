package com.expressflow.services.designerservice;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import com.expressflow.singleton.ProcessSingleton;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class DesignerAuthService {
	
	private static final long serialVersionUID = -1903120037375357159L;

	public com.expressflow.model.User authenticate() {
		UserService userService = UserServiceFactory.getUserService(); 
		User user = userService.getCurrentUser();
		com.expressflow.model.User userModel = new com.expressflow.model.User();
		userModel.email = user.getEmail();
		userModel.nickname = user.getNickname();
		userModel.userid = user.getUserId();
		if(ProcessSingleton.getInstance().getOAuthParameters(user.getEmail()) != null)
			userModel.accessGoogleAPIs = true;
		else
			userModel.accessGoogleAPIs = false;
		if (user != null) {
			return userModel;
		}
		return null;
	}
}
