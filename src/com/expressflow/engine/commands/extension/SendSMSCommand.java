package com.expressflow.engine.commands.extension;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */ 

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.Activity;
import com.expressflow.model.SendSMS;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;

public class SendSMSCommand implements ICommand {

	private static Logger log = Logger.getLogger(SendSMSCommand.class
			.getSimpleName());

	public SendSMSCommand() {

	}

	@Override
	public void execute(Activity activity) {
		SendSMS send = (SendSMS) activity;
		
		TwilioRestClient client = new TwilioRestClient(send.getACCOUNTSID(), send.getAUTHTOKEN(), null);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("From", send.getFrom());
		params.put("To", send.getTo());
		params.put("Body", send.getBody().getValue() + "\npowered by http://expressflow.com");

		TwilioRestResponse response;
		try {
			response = client.request(
					"/" + send.getAPIVERSION() + "/Accounts/" + client.getAccountSid()
							+ "/SMS/Messages", "POST", params);

			if (response.isError())
				log.info("Error send SMS: "
						+ response.getHttpStatus() + "\n"
						+ response.getResponseText());
			else {
				log.info("Success sending SMS: "
						+ response.getResponseText());
			}
		} catch (TwilioRestException e) {
			log.warning(e.getMessage());
		}

	}

}
