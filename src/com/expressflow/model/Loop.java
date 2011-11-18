package com.expressflow.model;

/*
Copyright (c) 2011 Martin Vasko, Ph.D.

licensing@expressflow.com
http://expressflow.com/license
*/

import org.jdom.Element;

import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.utils.NameUtil;

public class Loop extends Activity {
	private static final String VARIABLE = "variable";
	private static final String START_COUNTER_VALUE = "startcountervalue";
	private static final String FINAL_COUNTER_VALUE = "finalcountervalue";
	
	private Variable variable;
	private String startCounterValue;
	private String finalCounterValue;
	
	public Variable getVariable() {
		return variable;
	}



	public void setVariable(Variable variable) {
		this.variable = variable;
	}



	public String getStartCounterValue() {
		return startCounterValue;
	}



	public void setStartCounterValue(String startCounterValue) {
		this.startCounterValue = startCounterValue;
	}



	public String getFinalCounterValue() {
		return finalCounterValue;
	}



	public void setFinalCounterValue(String finalCounterValue) {
		this.finalCounterValue = finalCounterValue;
	}



	public static Loop fromXML(Element element){
		ModelSingleton modelSingleton = ModelSingleton.getInstance();
		Loop loop = new Loop();
		loop.setVariable(modelSingleton.variables.get(NameUtil.normalizeVariableName(element.getAttributeValue(VARIABLE))));
		loop.setStartCounterValue(element.getAttributeValue(START_COUNTER_VALUE));
		loop.setFinalCounterValue(element.getAttributeValue(FINAL_COUNTER_VALUE));
		return loop;
	}
}
