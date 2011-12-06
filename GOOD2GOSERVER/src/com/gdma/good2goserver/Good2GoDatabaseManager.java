package com.gdma.good2goserver;

import javax.jdo.JDOHelper;
import javax.jdo.Transaction;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.GeoPt;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.io.IOException;

public class Good2GoDatabaseManager {
	private PersistenceManager pm;
	
	public Good2GoDatabaseManager(){
		this.pm=PMF.get().getPersistenceManager();
	}
	
	public void close(){
		pm.close();
	}
	
	public boolean checkUserNameExists(String userName){
		try{
			pm.getObjectById(User.class, userName);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public void addUser(User newUser) throws IOException{
		Transaction txn = pm.currentTransaction();
		
		try {
			txn.begin();
			
			if (checkUserNameExists(newUser.getUserName()))
				throw new IOException("Username " + newUser.getUserName() + " already exists in database.");
			
			pm.makePersistent(newUser);
			
			txn.commit();
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}
	/*
	public void addEvent(Event newEvent){
		Transaction txn = pm.currentTransaction();
		
		try {
			txn.begin();
			
			if (checkUserNameExists(newUser.getUserName()))
				throw new IOException("Username " + newUser.getUserName() + " already exists in database.");
			
			pm.makePersistent(newUser);
			
			txn.commit();
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}*/
}
