package com.expressflow.engine.xml;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.jdom.Element;

import com.expressflow.engine.ObjectMap;
import com.expressflow.engine.commands.AssignCommand;
import com.expressflow.engine.commands.DirectInvokeCommand;
import com.expressflow.engine.commands.ExclusiveGatewayCommand;
import com.expressflow.engine.commands.LoopCommand;
import com.expressflow.engine.commands.MobileHumanTaskCommand;
import com.expressflow.engine.commands.MobileHumanTaskListCommand;
import com.expressflow.engine.commands.ProcessCommand;
import com.expressflow.engine.commands.SendEmailCommand;
import com.expressflow.engine.commands.SequenceFlowCommand;
import com.expressflow.engine.commands.extension.SendSMSCommand;
import com.expressflow.engine.commands.amazons3.DeleteBucketCommand;
import com.expressflow.engine.commands.facebook.FBStatusUpdateCommand;
import com.expressflow.engine.commands.googleapps.CreateGoogleCalendarEventCommand;
import com.expressflow.engine.commands.googleapps.DeleteGoogleDocCommand;
import com.expressflow.engine.commands.googleapps.PublishGoogleDocCommand;
import com.expressflow.engine.commands.googleplus.PublishActivityCommand;
import com.expressflow.model.Activity;
import com.expressflow.model.Assign;
import com.expressflow.model.DirectInvoke;
import com.expressflow.model.ExclusiveGateway;
import com.expressflow.model.HumanTask;
import com.expressflow.model.Loop;
import com.expressflow.model.MobileHumanTask;
import com.expressflow.model.MobileHumanTaskList;
import com.expressflow.model.Process;
import com.expressflow.model.SendEmail;
import com.expressflow.model.SendSMS;
import com.expressflow.model.SequenceFlow;
import com.expressflow.model.StartEvent;
import com.expressflow.model.Variable;
import com.expressflow.model.aws.AmazonS3Activity;
import com.expressflow.model.facebook.FBStatusUpdateActivity;
import com.expressflow.model.googleapps.GoogleDocsActivity;
import com.expressflow.model.googleapps.GoogleCalendarActivity;
import com.expressflow.model.googleplus.GooglePlusActivity;

public class Parser {

	private Logger log = Logger.getLogger(Parser.class.getSimpleName());
	private HashMap<String, Integer> elementsMap;
	private HashMap<Class, Integer> objectMap;
	private ModelSingleton modelSingleton;

	public Parser() {
		elementsMap = ElementMap.getMap();
		objectMap = ObjectMap.getMap();
		modelSingleton = ModelSingleton.getInstance();
	}

	public void executingObjectParse(Activity activity) {
		int index = objectMap.get(activity.getClass());
		switch (index) {
		case ObjectMap.DIRECTINVOKE:
			DirectInvokeCommand directInvokeCommand = new DirectInvokeCommand();
			directInvokeCommand.execute(activity);
			break;
		case ObjectMap.EXCLUSIVE_GATEWAY:
			break;
		case ObjectMap.MOBILEFORM:
			break;
		case ObjectMap.VARIABLE:
			modelSingleton.variables.put(activity.getName(),
					(Variable) activity);
			break;
		default:
			break;
		}
	}

	public void parseVariables(Element element) {
		List<?> rootInfo = element.getChildren();
		Iterator<?> variableIterator = rootInfo.iterator();
		while (variableIterator.hasNext()) {
			Element variable = (Element) variableIterator.next();
			parseVariable(variable);
			parseVariables(variable);
		}
	}

	private void parseVariable(Element element) {
		switch (elementsMap.get(element.getName().toUpperCase())) {
		case ElementMap.VARIABLE:
			Variable variable = Variable.fromXML(element);
			modelSingleton.variables.put(variable.getName(), variable);
			break;
		default:
			// Do nothing
			break;
		}
	}

	/**
	 * 
	 * @param element
	 * @return Activity
	 */
	public Activity executingParse(Element element) {
		// if (element.getChildren().size() > 0) {
		// List<?> children = element.getChildren();
		// Iterator<?> iterator = children.iterator();
		// while (iterator.hasNext())
		// executingParse((Element) iterator.next());
		// }
		Activity activity = new Activity();
		try {
			switch (elementsMap.get(element.getName().toUpperCase())) {
			/**
			 * DirectInvoke element
			 */
			case ElementMap.AMAZON_S3:
				activity = AmazonS3Activity.fromXML(element);
				modelSingleton.activities.add(activity);
				
				AmazonS3Activity awsS3 = (AmazonS3Activity)activity;
				
				switch (AmazonS3Activity.get(awsS3.getAction())) {
					case AmazonS3Activity.DELETE_BUCKET:
						DeleteBucketCommand deleteBucketCommand = new DeleteBucketCommand();
						deleteBucketCommand.execute(activity);
						break;
					default:
						break;
				}
				break;
			case ElementMap.ASSIGN:
				activity = Assign.fromXML(element);
				modelSingleton.activities.add(activity);

				AssignCommand assignCommand = new AssignCommand();
				assignCommand.execute(activity);
				break;
			case ElementMap.DIRECTINVOKE:
				activity = DirectInvoke.fromXML(element);
				modelSingleton.activities.add(activity);

				DirectInvokeCommand directInvokeCommand = new DirectInvokeCommand();
				directInvokeCommand.execute(activity);
				break;
			case ElementMap.EXCLUSIVE_GATEWAY:
				activity = ExclusiveGateway.fromXML(element);
				modelSingleton.activities.add(activity);

				ExclusiveGatewayCommand exclusiveGatewayCommand = new ExclusiveGatewayCommand();
				exclusiveGatewayCommand.execute(activity);
				break;
			case ElementMap.FACEBOOK_STATUS_UPDATE:
				activity = FBStatusUpdateActivity.fromXML(element);
				modelSingleton.activities.add(activity);

				FBStatusUpdateCommand fbStatusUpdateCommand = new FBStatusUpdateCommand();
				fbStatusUpdateCommand.execute(activity);
				break;
			case ElementMap.GOOGLE_DOCS:
				activity = GoogleDocsActivity.fromXML(element);
				modelSingleton.activities.add(activity);

				GoogleDocsActivity gA = (GoogleDocsActivity) activity;

				switch (GoogleDocsActivity.get(gA.getAction())) {
				case GoogleDocsActivity.PUBLISH:
					PublishGoogleDocCommand publishGDCommand = new PublishGoogleDocCommand();
					publishGDCommand.execute(activity);
					break;
				case GoogleDocsActivity.DELETE:
					DeleteGoogleDocCommand deleteGDCommand = new DeleteGoogleDocCommand();
					deleteGDCommand.execute(activity);
					break;
				default:
					break;
				}
				break;
			case ElementMap.GOOGLE_CALENDAR:
				activity = GoogleCalendarActivity.fromXML(element);
				modelSingleton.activities.add(activity);
				
				GoogleCalendarActivity gta = (GoogleCalendarActivity)activity;
				
				CreateGoogleCalendarEventCommand cgtc = new CreateGoogleCalendarEventCommand();
				cgtc.execute(activity);
				
				break;
			case ElementMap.GOOGLE_PLUS:
				activity = GooglePlusActivity.fromXML(element);
				modelSingleton.activities.add(activity);

				GooglePlusActivity gpA = (GooglePlusActivity) activity;
				
				PublishActivityCommand gpAC = new PublishActivityCommand();
				gpAC.execute(gpA);
				break;
			case ElementMap.LOOP:
				activity = Loop.fromXML(element);
				modelSingleton.activities.add(activity);

				LoopCommand loopCommand = new LoopCommand();
				loopCommand.execute(activity);
				break;
			/**
			 * Mobile form element
			 */
			case ElementMap.MOBILEFORM:
				activity = MobileHumanTask.fromXML(element);
				modelSingleton.activities.add(activity);

				MobileHumanTaskCommand mobileHTCommand = new MobileHumanTaskCommand();
				mobileHTCommand.execute(activity);
				break;
			/**
			 * Mobile Task List
			 */
			case ElementMap.MOBILETASKLIST:
				try {
					activity = MobileHumanTaskList.fromXML(element);
					modelSingleton.activities.add(activity);

					MobileHumanTaskListCommand mobileHTLCommand = new MobileHumanTaskListCommand();
					mobileHTLCommand.execute(activity);
				} catch (Exception e) {
					log.warning(e.getMessage());
				}
				break;
			/**
			 * Entire Process - root element
			 */
			case ElementMap.PROCESS:
				log.info("Element Process parsed!");
				// Parse Process
				Process process = Process.fromXML(element);
				process.setVariables(modelSingleton.variables);
				process.setActivities(modelSingleton.activities);

				// Execute Process command
				ProcessCommand processCommand = new ProcessCommand();
				processCommand.execute(process);
				break;
			/**
			 * Send Email - Google App Engine Email integration
			 */
			case ElementMap.SEND_EMAIL:
				log.info("Element Send Email parsed!");
				activity = SendEmail.fromXML(element);

				modelSingleton.activities.add(activity);
				SendEmailCommand sendEmailCommand = new SendEmailCommand();
				sendEmailCommand.execute(activity);
				break;
			/**
			 * Send SMS - Twilio.com integration
			 */
			case ElementMap.SEND_SMS:
				log.info("Element Send SMS parsed!");
				activity = SendSMS.fromXML(element);

				modelSingleton.activities.add(activity);
				SendSMSCommand sendSMSCommand = new SendSMSCommand();
				sendSMSCommand.execute(activity);
				break;
			/**
			 * Sequence Flow Element
			 */
			case ElementMap.SEQUENCEFLOW:
				SequenceFlow sequenceFlow = SequenceFlow.fromXML(element);
				modelSingleton.activities.add(sequenceFlow);

				SequenceFlowCommand sequenceFlowCmd = new SequenceFlowCommand();
				sequenceFlowCmd.execute(sequenceFlow);
				break;
			/**
			 * Start Event Element
			 */
			case ElementMap.START_EVENT:
				StartEvent startEvent = StartEvent.fromXML(element);
				modelSingleton.activities.add(startEvent);
				log.info("Element Start Event parsed!");
				break;
			// case ElementMap.VARIABLE:
			// log.info("Element Variable parsed!");
			// Variable variable = Variable.fromXML(element);
			// modelSingleton.variables.put(variable.getName(), variable);
			// break;
			/**
			 * Default - unknown element parsed
			 */
			default:
				break;
			}
		} catch (NullPointerException e) {
			// Do nothing
		}
		return activity;
	}

	public static Activity parse(Element element) {
		if (element.getChildren().size() > 0) {
			List<?> children = element.getChildren();
			Iterator<?> iterator = children.iterator();
			while (iterator.hasNext())
				parse((Element) iterator.next());
		}
		Activity activity = new Activity();
		HashMap<String, Integer> elementsMap = ElementMap.getMap();
		try {
			switch (elementsMap.get(element.getName().toUpperCase())) {
			/**
			 * DirectInvoke element
			 */
			case ElementMap.DIRECTINVOKE:
				activity = DirectInvoke.fromXML(element);
				break;
			case ElementMap.EXCLUSIVE_GATEWAY:
				activity = ExclusiveGateway.fromXML(element);
				break;
			case ElementMap.FACEBOOK_STATUS_UPDATE:
				activity = FBStatusUpdateActivity.fromXML(element);
				break;
			case ElementMap.GOOGLE_DOCS:
				activity = GoogleDocsActivity.fromXML(element);
				break;
			/**
			 * Mobile form element
			 */
			case ElementMap.MOBILEFORM:
				activity = MobileHumanTask.fromXML(element);
				break;
			/**
			 * Entire Process - root element
			 */
			case ElementMap.PROCESS:
				// Parse Process
				Process process = Process.fromXML(element);
				process.setXml(element.toString());
				break;
			/**
			 * Send Email - Google App Engine Email integration
			 */
			case ElementMap.SEND_EMAIL:
				activity = SendEmail.fromXML(element);
				break;
			/**
			 * Send SMS - Twilio.com integration
			 */
			case ElementMap.SEND_SMS:
				activity = SendSMS.fromXML(element);
				break;
			/**
			 * Sequence Flow Element
			 */
			case ElementMap.SEQUENCEFLOW:
				break;
			/**
			 * Start Event Element
			 */
			case ElementMap.START_EVENT:
				activity = StartEvent.fromXML(element);
				break;
			case ElementMap.WEBFORM:
				activity = HumanTask.fromXML(element);
				break;
			// case ElementMap.VARIABLE:
			// activity = Variable.fromXML(element);
			// break;
			/**
			 * Default - unknown element parsed
			 */
			default:
				break;
			}
		} catch (NullPointerException e) {
			activity = null;
		}
		return activity;
	}

	public static Boolean hasChild(Element element, String name) {
		Boolean result = false;
		List<?> children = element.getChildren();
		Iterator<?> iterator = children.iterator();
		while (iterator.hasNext()) {
			Element child = (Element) iterator.next();
			if (child.getName() == name)
				return true;
		}
		return result;
	}

	public static Element getChild(Element element, String name) {
		Element result = null;
		List<?> children = element.getChildren();
		Iterator<?> iterator = children.iterator();
		while (iterator.hasNext()) {
			Element child = (Element) iterator.next();
			if (child.getName() == name)
				return child;
		}
		return result;
	}
}
