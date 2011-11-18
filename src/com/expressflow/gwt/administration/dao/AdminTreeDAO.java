package com.expressflow.gwt.administration.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.expressflow.datastore.PMF;

public class AdminTreeDAO {
	
	public AdminTreeFolder getFolder(){
//		PersistenceManager pm = PMF.getManager();
//		Query query = pm.newQuery(AdminTreeFolder.class);
//		List<AdminTreeFolder> folders = (List<AdminTreeFolder>) query.execute();
		AdminTreeFolder root = new AdminTreeFolder("root");
//		for (AdminTreeFolder adminTreeFolder : folders) {
//			root.add(adminTreeFolder);
//		}
		return root;
	}
}
