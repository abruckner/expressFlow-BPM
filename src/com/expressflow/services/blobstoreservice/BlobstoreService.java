package com.expressflow.services.blobstoreservice;

import javax.jdo.PersistenceManager;

import com.expressflow.datastore.PMF;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

public class BlobstoreService {

	public String createUploadURL(){
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			String url = "NOK";
			com.google.appengine.api.blobstore.BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
			url = blobstore.createUploadUrl("/upload");
			return url;
		}
		return null;
	}
}
