package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.Activity;
import com.expressflow.model.MobileHumanTask;

public class MobileHumanTaskCommand implements ICommand {

	@Override
	public void execute(Activity activity) {
		MobileHumanTask mobileHT = (MobileHumanTask)activity;

	}

}
