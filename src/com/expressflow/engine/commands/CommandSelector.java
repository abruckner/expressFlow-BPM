package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.util.HashMap;
import java.util.Map;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.DirectInvoke;

public class CommandSelector {
	private Map<String, ICommand> commands = new HashMap<String, ICommand>(){
		private static final long serialVersionUID = 1L;

		{
			put(DirectInvoke.class.getSimpleName(), new DirectInvokeCommand());
		}
	};
	
	public ICommand getCommand(String activityName){
		return commands.get(activityName);
	}
}
