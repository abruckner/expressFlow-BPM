package com.expressflow.gwt.administration.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AdminTreeFolder extends BaseTreeModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4863909511250530448L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String efid;
	
	@Persistent
	private String name;
	
	@Persistent
	private List<AdminTreeNode> children = new ArrayList<AdminTreeNode>();
	
	public AdminTreeFolder(String name){
		this.name = name;
	}
	
	public AdminTreeFolder(String name, AdminTreeNode[] children){
		this.name = name;
		this.children = new ArrayList<AdminTreeNode>(Arrays.asList(children));
	}

	public String getEfid() {
		return efid;
	}

	public void setEfid(String efid) {
		this.efid = efid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
