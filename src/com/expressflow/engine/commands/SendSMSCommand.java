package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.util.HashMap;
import java.util.Map;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.model.Activity;
import com.expressflow.model.SendSMS;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;

public class SendSMSCommand implements ICommand {

	@Override
	public void execute(Activity activity) {
		SendSMS sendSMS = (SendSMS)activity;
		
		/* Instantiate a new Twilio Rest Client */
        TwilioRestClient client = new TwilioRestClient(sendSMS.getACCOUNTSID(), sendSMS.getAUTHTOKEN(), null);
         
            // Send a new outgoinging SMS by POST'ing to the SMS resource */
            // YYY-YYY-YYYY must be a Twilio validated phone number
 
            //build map of post parameters 
            Map<String,String> params = new HashMap<String,String>();
            params.put("From", sendSMS.getFrom());
            params.put("To", sendSMS.getTo());
            params.put("Body", sendSMS.getBody().toString());
 
            TwilioRestResponse response;
            try {
                response = client.request("/"+sendSMS.getAPIVERSION()+"/Accounts/"+sendSMS.getACCOUNTSID()+"/SMS/Messages", "POST", params);
             
                if(response.isError())
                    System.out.println("Error send SMS: "+response.getHttpStatus()+"\n"+response.getResponseText());
                else {
                    System.out.println("Success sending SMS: " + response.getResponseText());
                }
            } catch (TwilioRestException e) {
                e.printStackTrace();
            }
             

	}

}
