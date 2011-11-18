package com.expressflow.services.dropbox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;

import org.json.simple.parser.ParseException;

import com.dropbox.client.Authenticator;
import com.dropbox.client.DropboxClient;

public class DropboxService {
	
	private Map config = null;
	private Authenticator auth = null;
	
	public DropboxService(){
		try{
		initAuthentication();
		DropboxClient client = new DropboxClient(config, auth);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initAuthentication() throws FileNotFoundException, IOException, ParseException, OAuthCommunicationException, OAuthException{
		config = Authenticator.loadConfig("config/testing.json");
		auth = new Authenticator(config);
		String url = auth.retrieveRequestToken("http://mysite.com/theyaredone?blah=blah");
		// bounce them to the URL
		auth.retrieveAccessToken("");
		String access_key = auth.getTokenKey();
		String access_secret = auth.getTokenSecret();
	}
}
