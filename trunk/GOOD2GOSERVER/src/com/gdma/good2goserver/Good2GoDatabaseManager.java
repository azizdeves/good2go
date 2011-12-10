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
import javax.jdo.Query;

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
	public void addEvent(Event newEvent) throws IOException{
		
		if (newEvent.getEventKey()!=null)
			throw new IOException("Event key must be null prior to database insertion.");
		
		for (Event.Occurrence o : newEvent.getOccurrences()){
			if (o.getOccurrenceKey()!=null)
				throw new IOException("All occurrence keys must be null prior to database insertion.");
		}
		
		pm.makePersistent(newEvent);
	}
	
	public List<Event> getNextEventsByGeoPt(GeoPt gp, Date userDate){
		List<Event> res = new LinkedList<Event>();
		
		final Query query = pm.newQuery("SELECT FROM Event WHERE occurences.eventDate = today && occurrences.endTime > now PARAMETERS java.util.Date today, java.util.Date now");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(userDate);
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		calendar.set(year, month,day,0,0,0);
		Date today = calendar.getTime();
		
		calendar.set(0, 0,0,hour,minute,0);
		Date now = calendar.getTime();
		/*
		try {
			List<Event> results = (List<Event>) query.execute(today,now);
			if (!results.isEmpty()) {
				for (Employee e : results) {
					
				}
			}
			else {
				
			}
		}
		finally {
			query.closeAll();
		}*/
		
		return res;
	}
}
