package com.expressflow.engine.commands;

/*
 * Copyright (c) 2011 Martin Vasko, Ph.D.
 * 
 * licensing@expressflow.com http://expressflow.com/license
 */

import com.expressflow.engine.interfaces.ICommand;
import com.expressflow.engine.xml.ModelSingleton;
import com.expressflow.model.Activity;
import com.expressflow.model.Assign;
import com.expressflow.model.Variable;

public class AssignCommand implements ICommand {
	
	private ModelSingleton modelSingleton;

	@Override
	public void execute(Activity activity) {
		modelSingleton = ModelSingleton.getInstance();
		
		Assign assign = (Assign)activity;
		
		String fromExpression = "";
		if(assign.getFromExpression() != null)
			fromExpression = assign.getFromExpression();
		
		Variable fromVariable = new Variable();
		if(assign.getFromVariable() != null)
			fromVariable = modelSingleton.variables.get(assign.getFromVariable().getName());
		
		String literal = "";
		if(assign.getLiteral() != null)
			literal = assign.getLiteral();
		
		Variable outputVariable = new Variable();
		if(assign.getOutputVariable() != null)
			outputVariable = modelSingleton.variables.get(assign.getOutputVariable().getName());
		
		String toExpression = "";
		if(assign.getToExpression() != null)
			toExpression = assign.getToExpression();
		
		// Currently only Variable assignments are supported
		if(fromVariable != null && outputVariable != null){
			// Assign from fromVariable to outputVariable and change internal datastructure
			outputVariable.setValue(fromVariable.getValue());
			modelSingleton.variables.remove(outputVariable.getName());
			modelSingleton.variables.put(outputVariable.getName(), outputVariable);
			assign.setOutputVariable(outputVariable);
		}
		
	}

}
