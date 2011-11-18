package com.expressflow.test.controller;

/*
Copyright (c) 2011 Martin Vasko, Ph.D.

licensing@expressflow.com
http://expressflow.com/license
*/

import java.net.URL;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.expressflow.datastore.PMF;
import com.expressflow.engine.commands.DirectInvokeCommand;
import com.expressflow.model.DirectInvoke;
import com.expressflow.model.Variable;

@Controller
@RequestMapping("/test/directinvokecommand")
public class DirectInvokeCommandTestController {
	private static Logger log = Logger
			.getLogger(DirectInvokeTestController.class.getSimpleName());

	@Autowired
	private PMF pmf;

	@SuppressWarnings("unchecked")
	@ModelAttribute("directinvoke")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public DirectInvoke directInvokeTestCase() throws Exception {
		log.info("GET /test/directinvoke/ Test start");

		// Define model
		DirectInvoke activity = new DirectInvoke();
		activity.set_header("HTTP 1.1");
		activity.set_method("GET");
		activity.set_outputvariable(new Long("123"));
		activity.set_requestBody("Test Body");
		activity.set_responseCode("404");
		activity.set_responseHeader("HTTP 1.1");
		activity.set_timeout(0);
		Variable endpoint = new Variable();
		endpoint.setName("endpoint");
		endpoint.setValue("http://expressflow.com");
		activity.setEndpoint(endpoint);

		// Define Command
		DirectInvokeCommand directInvokeCmd = new DirectInvokeCommand();
		directInvokeCmd.execute(activity);
		
		return activity;
	}
}
