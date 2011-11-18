package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.model.Activity;
import com.expressflow.model.SendEmail;
import com.expressflow.utils.NameUtil;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailCommand implements ICommand {

	private static final Logger log = Logger.getLogger(SendEmailCommand.class
			.getSimpleName());

	private ModelSingleton modelSingleton;
	
	@Override
	public void execute(Activity activity) {
		log.info("Executing SendEmail Command.");
		modelSingleton = ModelSingleton.getInstance();
		SendEmail send = (SendEmail) activity;

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(send.getFrom(),send.getNickname()));
				if(send.getResponse().length() > 0)
					msg.setReplyTo(new Address[] { new InternetAddress(send.getResponse()) });
				
				// Recipient entered manually
				if(!send.getTo().startsWith("$"))
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						send.getTo(), send.getTo()));
				// Linked to a Variable
				else{
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress((String)modelSingleton.variables.get(NameUtil.normalizeVariableName(send.getTo())).getValue()));
				}
				
				msg.setSubject(send.getSubject());
				
				// Message Text entered manually
				if(!send.getBody().getValue().startsWith("$"))
					msg.setText(send.getBody().getValue());
				// Linked to a Variable
				else{
					msg.setText((String)modelSingleton.variables.get(NameUtil.normalizeVariableName(send.getBody().getValue())).getValue());
				}
				Transport.send(msg);

			} catch (AddressException e) {
				log.info(e.getMessage());
			} catch (MessagingException e) {
				log.info(e.getMessage());
			} catch (UnsupportedEncodingException e) {
				log.info(e.getMessage());
			}
		
	}

}
