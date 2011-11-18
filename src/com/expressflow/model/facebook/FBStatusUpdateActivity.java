package com.expressflow.model.facebook;

import org.jdom.Element;

import com.expressflow.model.Activity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class FBStatusUpdateActivity extends Activity {
	private static final String STATUS = "status";
	
	public String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public void persist(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity entity = new Entity("FBStatusUpdate");
		entity.setProperty(STATUS, status);
		datastore.put(entity);
	}
	
	public static FBStatusUpdateActivity fromXML(Element element){
		FBStatusUpdateActivity activity = new FBStatusUpdateActivity();
		activity.setStatus(element.getAttributeValue(STATUS));
		return activity;
	}
}
