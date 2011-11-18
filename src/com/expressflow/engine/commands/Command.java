package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import com.expressflow.model.Activity;

public abstract class Command {

	public Command() {
	}
	
	// Overridden
	abstract void execute(Activity activity);

}
