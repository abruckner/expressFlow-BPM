package com.expressflow.gwt.administration.client;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EngineAdministration implements EntryPoint {
	@Override
	public void onModuleLoad() {
		PortalContainer pc = new PortalContainer();
		RootPanel.get().add(pc);
		pc.layout();
	}
}
