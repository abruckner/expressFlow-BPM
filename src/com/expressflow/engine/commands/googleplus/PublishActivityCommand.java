package com.expressflow.engine.commands.googleplus;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.Activity;
import com.expressflow.model.googleplus.GooglePlusActivity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PublishActivityCommand implements ICommand {

	@Override
	public void execute(Activity activity) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			GooglePlusActivity gDocA = (GooglePlusActivity) activity;
		}
	}

}
