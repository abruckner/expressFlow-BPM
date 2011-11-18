package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.model.Activity;
import com.expressflow.model.DirectInvoke;
import com.expressflow.model.Variable;

public class DirectInvokeCommand implements ICommand{

	private static Logger log = Logger.getLogger(DirectInvokeCommand.class
			.getSimpleName());
	private ModelSingleton modelSingleton;

	public DirectInvokeCommand() {
	}

	public void execute(Activity activity) {
		modelSingleton = ModelSingleton.getInstance();
		try {
			DirectInvoke di = (DirectInvoke)activity;
			Variable endpoint = modelSingleton.variables.get(di.getEndpoint().getName());
			URL url = new URL(endpoint.getValue().toString());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestProperty("use_intranet", "true");
			connection.setConnectTimeout(di.get_timeout());
			connection.setDoOutput(true);
			String method = di.get_method();
			if(method == null)
				method = "GET";
			connection.setRequestMethod(method);
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                connection.getInputStream()));
	        String inputLine;
	        String response = "";
	        while ((inputLine = in.readLine()) != null) 
	            response +="\n" + inputLine;
	        in.close();
	        
	        // Variable outputVariable = Variable.load(KeyFactory.createKey(DirectInvoke.class.getSimpleName(), di.get_outputvariable()));
	        Variable outputVariable = modelSingleton.variables.get(di.getOutputVariable().getName());
//	        if(outputVariable.getMessage() == null){
//	        	outputVariable.setMessage("");
//	        }
	        outputVariable.setValue(response);
	        modelSingleton.variables.put(outputVariable.getName(), outputVariable);
	        di.persist();
	        
			
		} catch (IOException ioe) {
			log.warning(ioe.toString());
		}
	}
}
