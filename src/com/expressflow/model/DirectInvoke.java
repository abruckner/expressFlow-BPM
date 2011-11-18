package com.expressflow.model;

/*
Copyright (c) 2011 Martin Vasko, Ph.D.

licensing@expressflow.com
http://expressflow.com/license
*/

import java.util.logging.Logger;

import org.jdom.Element;

import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.utils.NameUtil;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public class DirectInvoke extends Activity {
	
	private static final String HEADER = "header";
	private static final String METHOD = "method";
	private static final String NAME = "name";
	private static final String OUTPUTVARIABLE = "outputvariable";
	private static final String REQUESTBODY = "requestBody";
	private static final String ENDPOINT = "endpoint";
	
	private static Logger logger = Logger.getLogger(DirectInvoke.class.getSimpleName());
	
	private String method;
	
	private String requestBody;
	
	private String header;
	
	private Long outputVariableKey;
	
	private Variable outputVariable;
	
	public Variable getOutputVariable() {
		return outputVariable;
	}

	public void setOutputVariable(Variable outputVariable) {
		this.outputVariable = outputVariable;
	}

	private String responseHeader;
	
	private String responseCode;
	
	private int timeout;
	
	private Variable endpoint;
	
	public Variable getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Variable endpointVariable) {
		this.endpoint = endpointVariable;
	}

	@Override
	public void persist(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("DirectInvoke");
		entity.setProperty(NAME, name);
		entity.setProperty("predecessor", predecessor);
		entity.setProperty("ancestor", ancestor);
		entity.setProperty(METHOD, method);
		entity.setProperty(REQUESTBODY, new Text(requestBody));
		entity.setProperty(HEADER, header);
		entity.setProperty(OUTPUTVARIABLE, outputVariableKey);
		// outputVariable.persist();
		entity.setProperty("responseHeader", responseHeader);
		entity.setProperty("responseCode", responseCode);
		entity.setProperty("timeout", timeout);
		entity.setProperty("state", state);
		datastore.put(entity);
	}
	
	public static DirectInvoke fromXML(Element element){
		ModelSingleton model = ModelSingleton.getInstance();
		DirectInvoke directInvoke = new DirectInvoke();
		if(element.getAttributeValue(HEADER) != null)
			directInvoke.set_header(element.getAttributeValue(HEADER));
		else
			directInvoke.set_header("HTTP 1.1");
		if(element.getAttributeValue(METHOD) != null)
			directInvoke.set_method(element.getAttributeValue(METHOD));
		else
			directInvoke.set_method("GET");
		directInvoke.setOutputVariable(model.variables.get(NameUtil.normalizeVariableName(element.getAttributeValue(OUTPUTVARIABLE))));
		directInvoke.set_requestBody(element.getAttributeValue(REQUESTBODY));
		directInvoke.setEndpoint(model.variables.get(NameUtil.normalizeVariableName(element.getAttributeValue(ENDPOINT))));
		return directInvoke;
	}
	
	public static DirectInvoke load(Key efid){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		DirectInvoke activity = new DirectInvoke();
		try{
			Entity entity = datastore.get(efid);
			activity.setName((String)entity.getProperty(NAME));
			activity.setAncestor((Long)entity.getProperty("ancestor"));
			activity.setPredecessor((Long)entity.getProperty("predecessor"));
			activity.set_method((String)entity.getProperty(METHOD));
			activity.set_requestBody((String)entity.getProperty("requestBody"));
			activity.set_header((String)entity.getProperty(HEADER));
			activity.set_outputvariable((Long)entity.getProperty("outputVariable"));
			activity.set_responseHeader((String)entity.getProperty("responseHeader"));
			activity.set_responseCode((String)entity.getProperty("responseCode"));
			activity.set_timeout((Integer)entity.getProperty("timeout"));
			activity.setState((String)entity.getProperty("state"));
		}
		catch(EntityNotFoundException enfe){
			logger.warning(enfe.getMessage());
			activity = null;
		}
		return activity;
	}

	public String get_method() {
		return method;
	}

	public void set_method(String _method) {
		this.method = _method;
	}

	public String get_requestBody() {
		return requestBody;
	}

	public void set_requestBody(String _requestBody) {
		this.requestBody = _requestBody;
	}

	public String get_header() {
		return header;
	}

	public void set_header(String _header) {
		this.header = _header;
	}

	public Long get_outputvariable() {
		return outputVariableKey;
	}

	public void set_outputvariable(Long _outputvariable) {
		this.outputVariableKey = _outputvariable;
	}

	public String get_responseHeader() {
		return responseHeader;
	}

	public void set_responseHeader(String _responseHeader) {
		this.responseHeader = _responseHeader;
	}

	public String get_responseCode() {
		return responseCode;
	}

	public void set_responseCode(String _responseCode) {
		this.responseCode = _responseCode;
	}

	public int get_timeout() {
		return timeout;
	}

	public void set_timeout(int _timeout) {
		this.timeout = _timeout;
	}
}
