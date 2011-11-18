package com.expressflow.model.googleplus;

import org.jdom.Element;

import com.expressflow.model.Activity;

public class GooglePlusActivity extends Activity {
	
	public static GooglePlusActivity fromXML(Element element){
		GooglePlusActivity activity = new GooglePlusActivity();
		return activity;
	}
}
