package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.Activity;
import com.expressflow.model.Loop;
import com.expressflow.model.Variable;
import com.expressflow.singleton.ProcessSingleton;

public class LoopCommand implements ICommand {
	
	private Logger log = Logger.getLogger(LoopCommand.class.getSimpleName());
	private ProcessSingleton pSingleton;

	@Override
	public void execute(Activity activity) {
		pSingleton = ProcessSingleton.getInstance();
		
		Loop loop = (Loop)activity;
		Variable variable = loop.getVariable();
		String startCounterVal = loop.getStartCounterValue();
		Integer startCounter = Integer.parseInt(startCounterVal);
		String finalCounterVal = loop.getFinalCounterValue();
		try{
			List<?> list = XPath.selectNodes(pSingleton.getDocument(), finalCounterVal);
			
		}
		catch(JDOMException je){
			log.log(Level.WARNING, je.getMessage());
		}
	}

}
