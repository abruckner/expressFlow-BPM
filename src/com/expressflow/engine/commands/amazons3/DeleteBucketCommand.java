package com.expressflow.engine.commands.amazons3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.Activity;
import com.expressflow.model.aws.AmazonS3Activity;
import com.expressflow.security.KeyHolder;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class DeleteBucketCommand implements ICommand {
	
	private AmazonS3 s3 = null;

	@Override
	public void execute(Activity activity) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
			AmazonS3Activity awsActivity = (AmazonS3Activity)activity;
			s3 = new AmazonS3Client(new BasicAWSCredentials(KeyHolder.AMAZON_ACCES_KEY, KeyHolder.AMAZON_SECRET_KEY));
			s3.deleteBucket(awsActivity.getBucketName());
		}
	}

}
