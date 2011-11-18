package com.expressflow.engine.xml;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Element;

import sun.util.logging.resources.logging;

import com.expressflow.model.Activity;
import com.expressflow.model.Assign;
import com.expressflow.model.DirectInvoke;
import com.expressflow.model.ExclusiveGateway;
import com.expressflow.model.MobileHumanTask;
import com.expressflow.model.SendEmail;
import com.expressflow.model.SendSMS;
import com.expressflow.model.StartEvent;
import com.expressflow.model.aws.AmazonS3Activity;
import com.expressflow.model.facebook.FBStatusUpdateActivity;
import com.expressflow.model.googleapps.GoogleDocsActivity;
import com.expressflow.model.googleplus.GooglePlusActivity;

public class ElementMap {
	
	public static final int PROCESS = 10;
	public static final int SEQUENCEFLOW = 11;
	
	public static final int DIRECTINVOKE = 1;
	public static final int START_EVENT = 2;
	public static final int END_EVENT = 12;
	public static final int MOBILEFORM = 3;
	public static final int VARIABLE = 4;
	public static final int EXCLUSIVE_GATEWAY = 5;
	public static final int ASSIGN = 6;
	public static final int LOOP = 7;
	public static final int WEBFORM = 8;
	public static final int MOBILETASKLIST = 9;
	
	public static final int SEND_SMS = 101;
	public static final int SEND_EMAIL = 102;
	public static final int GOOGLE_DOCS = 103;
	public static final int FACEBOOK_STATUS_UPDATE = 104;
	public static final int GOOGLE_CALENDAR = 105;
	public static final int AMAZON_S3 = 106;
	public static final int GOOGLE_PLUS = 107;
	public static final int TIMER = 108;
	
	private static final Logger log = Logger.getLogger(ElementMap.class.getSimpleName());
	
	private static HashMap<String, Integer> map = 
		new HashMap<String, Integer>(){
			private static final long serialVersionUID = 1L;

			{
				put("PROCESS", 10);
				put("SEQUENCEFLOW", 11);
				put("DIRECTINVOKE", 1);
				put("STARTEVENT", 2);
				put("ENDEVENT", 12);
				put("MOBILEHUMANTASK", 3);
				put("VARIABLE", 4);
				put("EXCLUSIVEGATEWAY", 5);
				put("SENDSMS", 101);
				put("SENDEMAIL", 102);
				put("ASSIGN", 6);
				put("FOR", 7);
				put("HUMANTASK", 8);
				put("MOBILETASKLIST", 9);
				put("GDOC", 103);
				put("FB_STATUS", 104);
				put("GCALENDAR", 105);
				put("AWS_S3", 106);
				put("GPLUS", 107);
				put("TIMER", 108);
			}
		};
	
	public static HashMap<String, Integer> getMap(){
		return map;
	}
	
	public static Activity resolveActivity(Element element){
		Activity activity = new Activity();
		switch(map.get(element.getName().toUpperCase())){
		case AMAZON_S3:
			activity = AmazonS3Activity.fromXML(element);
			break;
		case ASSIGN:
			activity = Assign.fromXML(element);
			break;
		case DIRECTINVOKE:
			activity = DirectInvoke.fromXML(element);
			break;
		case EXCLUSIVE_GATEWAY:
			activity = ExclusiveGateway.fromXML(element);
			break;
		case GOOGLE_DOCS:
			activity = GoogleDocsActivity.fromXML(element);
			break;
		case GOOGLE_PLUS:
			activity = GooglePlusActivity.fromXML(element);
			break;
		case FACEBOOK_STATUS_UPDATE:
			activity = FBStatusUpdateActivity.fromXML(element);
			break;
		case WEBFORM:
			break;
		case MOBILEFORM:
			activity = MobileHumanTask.fromXML(element);
			break;
		case PROCESS:
			// Not implemented
			break;
		case SEND_EMAIL:
			activity = SendEmail.fromXML(element);
			break;
		case SEND_SMS:
			activity = SendSMS.fromXML(element);
			break;
		case SEQUENCEFLOW:
			// Not implemented
			break;
		case START_EVENT:
			activity = StartEvent.fromXML(element);
			break;
		case VARIABLE:
			// Not implemented
			break;
		default:
			log.log(Level.ALL, element.getName() + ": Unrecognized.");
			break;
		}
		return activity;
	}
}
