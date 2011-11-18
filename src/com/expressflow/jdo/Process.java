package com.expressflow.jdo;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Process {
	@PrimaryKey 
	@Persistent
	private String id;
	
	@Persistent
	private Date accessDate;
	
	@Persistent
	private Date creationDate;
	
	@Persistent
	private String creator;
	
	@Persistent
	private String state;
	
	@Persistent
	private String access;
	
	@Persistent
	private String name;
	
	@Persistent
	private int timesExecuted;
	
	public int getTimesExecuted() {
		return timesExecuted;
	}

	public void setTimesExecuted(int timesExecuted) {
		this.timesExecuted = timesExecuted;
	}

	@SuppressWarnings("unused")
	private String xml;

	public String getXml() {
		return _xml.getValue();
	}

	public void setXml(String xml) {
		this._xml = new Text(xml);
	}
	
	// XML Model for the designer
	@Persistent
	private Text _xml;
	
	// XML Model for the engine ;-) We work on harmony here...
	@Persistent
	private Text _execXml;

	public String getExecXml() {
		return _execXml.getValue();
	}

	public void setExecXml(String _xmlSrc) {
		this._execXml = new Text(_xmlSrc);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Persistent
	private List<Task> tasks;

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}
