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
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.io.IOException;
import javax.jdo.Query;

public class Good2GoDatabaseManager {
	
	public boolean checkUserNameExists(String userName){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			pm.getObjectById(User.class, userName);
			return true;
		}
		catch(Exception e){
			return false;
		}
		finally{
			pm.close();
		}
	}
	
	public boolean checkEventKeyExists(Key eventKey){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			pm.getObjectById(Event.class, eventKey);
			return true;
		}
		catch(Exception e){
			return false;
		}
		finally{
			pm.close();
		}
	}
	
	public void addUser(User newUser) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
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
			pm.close();
		}
	}
	
	public void addEvent(Event newEvent) throws IOException{
		if (newEvent.getEventKey()!=null)
			throw new IOException("Event key must be null prior to database insertion.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			pm.makePersistent(newEvent);
		}
		finally{
			pm.close();
		}
		
		for (Occurrence o : newEvent.getOccurrences()){
			o.setContainingEventKey(newEvent.getEventKey());
			addOccurrence(o);
		}
	}
	
	public void addOccurrence(Occurrence newOccurrence) throws IOException{
		if (newOccurrence.getOccurrenceKey()!=null)
			throw new IOException("Occurrence key must be null prior to database insertion.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String eventKey = newOccurrence.getContainingEventKey();
		
		try {
			
			Event e = pm.getObjectById(Event.class, eventKey);
			
			pm.makePersistent(newOccurrence);
			
			e.addOccurrenceKey(newOccurrence.getOccurrenceKey());
		}
		finally {
			pm.close();
		}
	}
	
	public List<Event> getNextEventsByGeoPt(GeoPt gp, Date userDate){
		List<Event> res = new LinkedList<Event>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// WHERE eventDate == today && endTime > now PARAMETERS java.util.Date today, java.util.Date now
		
		Query query = pm.newQuery(Occurrence.class);
		query.setOrdering("endTime asc, containingEventKey asc");
		
		Calendar occCal = Calendar.getInstance();
		Calendar evCal = Calendar.getInstance();
		Calendar userCal = Calendar.getInstance();
		userCal.setTime(userDate);
		
		int year = userCal.get(Calendar.YEAR);
		int month = userCal.get(Calendar.MONTH);
		int day = userCal.get(Calendar.DATE);
		int hour = userCal.get(Calendar.HOUR_OF_DAY);
		int minute = userCal.get(Calendar.MINUTE);
		
		userCal.set(year, month,day,0,0,0);
		Date today = userCal.getTime();
		
		userCal.set(0, 0,0,hour,minute,0);
		Date now = userCal.getTime();
		
		userCal.set(year, month, day, hour, minute, 0);
		
		try {	
			@SuppressWarnings("unchecked")
			List<Occurrence> results = (List<Occurrence>) query.execute();  //today,now);
		
			if (!results.isEmpty()) {
				Collections.sort(results);
				
				String eventKey = null;
				String lastEventKey = null;
				boolean isInserted = false;
				Event event = null;
				
				for (Occurrence occurrence : results){
					
					int minHours = 0;
					int minMinutes = 0;
					
					lastEventKey = eventKey;
					eventKey = occurrence.getContainingEventKey();
					if (lastEventKey!=null && eventKey.equals(lastEventKey)){
						if (isInserted == true)
							continue;
					}
					else {
						isInserted = false;
						event = (Event) pm.getObjectById(Event.class, eventKey);
						/*evCal.setTime(event.getMinDuration());
						minHours = evCal.get(Calendar.HOUR_OF_DAY);
						minMinutes = evCal.get(Calendar.MINUTE);*/
					}
					
					
					occCal.setTime(occurrence.getEndTime());
					/*occCal.add(Calendar.HOUR_OF_DAY, -minHours);
					occCal.add(Calendar.MINUTE, -minMinutes);*/
					
					if (occCal.compareTo(userCal) > 0){
						event.addOccurrence(occurrence);
						res.add(event);
						isInserted = true;
					}
				}
			
				Collections.sort(res, new GeoPtComparator());
			}
			else {
				res = null;
			}
		}
		finally {
			query.closeAll();
			pm.close();
		}
		return res;
	}
}
