package com.expressflow.engine.commands.facebook;

import java.util.logging.Logger;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.facebook.FacebookService;
import com.expressflow.model.Activity;
import com.expressflow.model.facebook.FBStatusUpdateActivity;

public class FBStatusUpdateCommand implements ICommand {
	
	private static final Logger log = Logger.getLogger(FBStatusUpdateCommand.class
			.getSimpleName());

	private ModelSingleton modelSingleton;

	@Override
	public void execute(Activity activity) {
		modelSingleton = ModelSingleton.getInstance();
		FBStatusUpdateActivity fbActivity = (FBStatusUpdateActivity) activity;
		
		FacebookService fb = new FacebookService();
		
		fb.updateUsersStatusMessage(fbActivity.getStatus());
	}

}
