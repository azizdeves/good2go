package com.gdma.good2goserver;

import javax.jdo.Transaction;
import javax.jdo.PersistenceManager;

import com.gdma.good2goserver.Karma.ActionType;
import com.google.appengine.api.datastore.Key;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.Query;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

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
	
	public boolean checkEventKeyExists(String eventKey){
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
	
	public Event getEvent(String eventKey){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Event result = null, detached = null;
		
		try{
			result = pm.getObjectById(Event.class, eventKey);
			result.getEventAddress();
			
			detached = pm.detachCopy(result);
		}
		catch(Exception e){
			return null;
		}
		finally{
			pm.close();
		}
		
		return detached;
	}
	
	public Occurrence getOccurrence(String occurrenceKey){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Occurrence result = null, detached = null;
		
		try{
			result = pm.getObjectById(Occurrence.class, occurrenceKey);
			detached = pm.detachCopy(result);
		}
		catch(Exception e){
			return null;
		}
		finally{
			pm.close();
		}
		
		return detached;
	}
	
	public void addUser(User newUser) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Karma karma = new Karma(newUser.getUserName(), ActionType.OPEN_ACCOUNT, new Date());
		Transaction txn = pm.currentTransaction();
		
		try {
			txn.begin();
			
			if (checkUserNameExists(newUser.getUserName()))
				throw new IOException("Username " + newUser.getUserName() + " already exists in database.");
			
			pm.makePersistent(newUser);
			
			txn.commit();
			pm.makePersistent(karma);
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
			pm.close();
		}
	}
	
	public void editUser(User newUser) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction txn = pm.currentTransaction();
		User user=null;
		
		try {
			txn.begin();
			
			if (!checkUserNameExists(newUser.getUserName()))
				throw new IOException("Username " + newUser.getUserName() + " doesn't exist in database.");
			
			user = pm.getObjectById(User.class, newUser.getUserName());
			if (!newUser.getFirstName().equals(new String("")))
				user.setFirstName(newUser.getFirstName());
			if (!newUser.getLastName().equals(new String("")))
				user.setLastName(newUser.getLastName());
			if (newUser.getBirthYear()!=0)
				user.setBirthYear(newUser.getBirthYear());
			if (!newUser.getEmail().equals(new String("")))
				user.setEmail(newUser.getEmail());
			if (!newUser.getPhone().equals(new String("")))
				user.setPhone(newUser.getPhone());
			if (!newUser.getCity().equals(new String("")))
				user.setCity(newUser.getCity());
			if (newUser.getSex()!=null)
				user.setSex(newUser.getSex());
			if (newUser.getRegistrationDate()!=null)
				user.setRegistrationDate(newUser.getRegistrationDate());
			
			txn.commit();
		}
		finally {
			if (txn.isActive()) {
				txn.rollback();
			}
			pm.close();
		}
	}
	
	public void addKarma(Karma karma){
		PersistenceManager pm = PMF.get().getPersistenceManager();
			
		try {
			pm.makePersistent(karma);
		}
		finally {
			pm.close();
		}
	}
	
	public User getUserDetails(String userName){
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user=null;
		
		try {
			user = pm.getObjectById(User.class, userName);
		}
		finally {
			pm.close();
		}
		
		return user;
	}
	
	public boolean registerToOccurrence(String userName, String occurrenceKey){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Karma karma = new Karma(userName, ActionType.REGISTER_TO_EVENT, new Date(), occurrenceKey);
		
		try {
			
			//Register Occurrence.
			
			User user = pm.getObjectById(User.class, userName);			
			Occurrence occurrence = pm.getObjectById(Occurrence.class, occurrenceKey);
			
			if (!user.getRegisteredOccurrenceKeys().contains(occurrenceKey)){
				user.addRegisteredOccurrenceKey(occurrenceKey);
				occurrence.addRegisteredUser(userName);
			}
		}
		catch(Exception e){
			pm.close();
			return false;
		}
		
			//Add Karma, if doesn't exist.
		try{	
			Query query = pm.newQuery(Karma.class);
			query.setFilter("userName == '" + userName + "' && occurrenceKey == '" + occurrenceKey + "'");
			@SuppressWarnings("unchecked")
			List<Karma> karmas = (List<Karma>) query.execute();
			
			boolean isFound = false;
			
			for (Karma k : karmas){
				if (k.getActionType().equals(Karma.ActionType.REGISTER_TO_EVENT)){
					isFound = true;
					break;
				}
			}
			
			if (!isFound)
				pm.makePersistent(karma);
		}
		finally {
			pm.close();
		}
		
		return true;
	}
	
	public void CancelRegistration(String userName, String occurrenceKey){
		PersistenceManager pm = PMF.get().getPersistenceManager();
			
		try {
			
			//Cancel the registration.
			
			User user = pm.getObjectById(User.class, userName);
			Occurrence occurrence = pm.getObjectById(Occurrence.class, occurrenceKey);
			
			user.removeRegisteredOccurrenceKey(occurrenceKey);
			occurrence.removeRegisteredUser(userName);

			Query query = pm.newQuery(Karma.class);
			query.setFilter("userName == '" + userName + "' && occurrenceKey == '" + occurrenceKey + "'");
			@SuppressWarnings("unchecked")
			List<Karma> karmas = (List<Karma>) query.execute();
			
			
			for (Karma k : karmas){
				if (k.getActionType().equals(Karma.ActionType.REGISTER_TO_EVENT)){
					Query deleteQuery = pm.newQuery(Karma.class);
					deleteQuery.setFilter("karmaKey == '" + k.getKey() + "'");
					deleteQuery.deletePersistentAll();
					break;
				}
			}

		}
		finally {
			pm.close();
		}
	}
	
	public void addFeedback (String userName, String occurrenceKey, int rating){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Karma.class);
		query.setFilter("userName == '" + userName + "' && occurrenceKey == '" + occurrenceKey +"'");
		
		try{
		
			@SuppressWarnings("unchecked")
			List<Karma> karmas = (List<Karma>) query.execute();
			
			boolean isFound = false;
			
			for (Karma k : karmas){
				Karma.ActionType type = k.getActionType();
				if (type.equals(Karma.ActionType.NO_RATE) || type.equals(Karma.ActionType.RATE_AN_EVENT)){
					isFound = true;
					break;
				}
			}
			
			if (!isFound){
				Occurrence occurrence = pm.getObjectById(Occurrence.class, occurrenceKey);
				
				//calculate duration of event (number of hours)
				Calendar start = Calendar.getInstance();
				Calendar end = Calendar.getInstance();
				
				start.setTime(occurrence.getStartTime());
				end.setTime(occurrence.getEndTime());
				
				int numHours=end.get(Calendar.HOUR_OF_DAY)-start.get(Calendar.HOUR_OF_DAY);
				
				int points=1000*numHours; // points for participating in the event
				
				Karma karmap = new Karma(userName, ActionType.PARTICIPATE, new Date(),occurrenceKey, points);
				pm.makePersistent(karmap);
				
				if (rating!=-1){
					Karma karma = new Karma(userName, ActionType.RATE_AN_EVENT, new Date(),occurrenceKey);
						
						String eventKey = occurrence.getContainingEventKey();
						
						Event event = pm.getObjectById(Event.class, eventKey);
						
						event.incNumRaters();
						event.addRating(rating);
						
						pm.makePersistent(karma);
				}
				
				else{
					Karma karma = new Karma(userName, ActionType.NO_RATE, new Date(),occurrenceKey);
					pm.makePersistent(karma);
				}
			}
		}
		finally {
			pm.close();
		}
	}
	
	public List<Event> getEventsToFeedback(String userName, Date userDate){
		
		List<Event> results=new LinkedList<Event>();
		Event event = null;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Date today = userDate;
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(today);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		today = cal.getTime();
		
		
		//retrieve occurrences where userName is registered and date<today
		Query ocQuery =pm.newQuery(Occurrence.class);
		ocQuery.declareParameters("java.util.Date today");
		ocQuery.setFilter("registeredUserNames == '" + userName + "' && occurrenceDate < today");
		
		//retrieve Karma records where userName performed action RATE_AN_EVENT or NO_RATE
		Query karQuery = pm.newQuery(Karma.class);
		karQuery.setFilter("userName == '" + userName + "'");
		karQuery.setOrdering("occurrenceKey asc");
		

		try {
			
			//get occurrences, sort by occurrence key
			@SuppressWarnings("unchecked")
			List<Occurrence> occurrences= (List<Occurrence>) ocQuery.execute(today);
			Collections.sort(occurrences, new OccurrenceByKeyComparator());
			
			@SuppressWarnings("unchecked")
			List<Karma> tempKarmas = (List<Karma>) karQuery.execute();
			List<Karma> karmas = new LinkedList<Karma>();
			
			for (Karma k : tempKarmas){
				Karma.ActionType type = k.getActionType();
				if (type.equals(Karma.ActionType.NO_RATE) || type.equals(Karma.ActionType.RATE_AN_EVENT))
					karmas.add(k);
			}
			
			Iterator<Occurrence> occIt = occurrences.iterator();
			Iterator<Karma> karIt =karmas.iterator();
			
			Occurrence curOcc = null;
			Karma curKar = null;
			
			if (karIt.hasNext())
				curKar = karIt.next();
			
			while (occIt.hasNext()){
				curOcc = occIt.next();
				if (curKar != null)
					while ((curKar.getOccurrenceKey().compareTo(curOcc.getOccurrenceKey()) < 0) && (karIt.hasNext()))
						curKar = karIt.next();
					
				if (curKar==null || curKar.getOccurrenceKey().compareTo(curOcc.getOccurrenceKey()) != 0){
					event = pm.getObjectById(Event.class, curOcc.getContainingEventKey());
					event.addOccurrence(curOcc);
					results.add(event);
				}
			}
		}
		finally {
			pm.close();
		}
		
		return results;
	}
	
	public long getKarma(String userName){
		
		long points=0;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Karma.class);
		query.setFilter("userName==\"" + userName + "\"");
		
		try {	
			
			@SuppressWarnings("unchecked")
			List<Karma> results = (List<Karma>) query.execute();
			for (Karma k : results){
				points+=k.getPoints();
			}
		}
		finally {
			pm.close();
		}
		
		return points;
	}
	
	//Dumb batch insert for events.
	public void addEvents(Collection<Event> events) throws Exception{
		PersistenceManager pm = null;
		
		Collection<Event> put = new LinkedList<Event>();
		
		Iterator<Event> it = events.iterator();
		
		int i = 0;
		while (it.hasNext()){
			put.add(it.next());
			i++;
			
			if (i%50 == 0 || !it.hasNext()){
				i=0;
				try {
					pm = PMF.get().getPersistenceManager();
					pm.makePersistentAll(put);		
				}
				catch (Exception e){
					throw e;
				}
				finally {
					pm.close();
				}
				put.clear();
			}
		}
	}
	
	public void addEvent(Event newEvent) throws IOException{	
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
	
	public void editEvent(Event newEvent) throws Exception{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			Event e = pm.getObjectById(Event.class, newEvent.getEventKey());
			
			if (e==null)
				throw new IOException("This event is not registered in the database.");
			
			if (newEvent.getContent()!=null && !newEvent.getContent().equals(new String("")))
				e.setContent(newEvent.getContent());
			
			if (newEvent.getDescription()!=null && !newEvent.getDescription().equals(new String("")))
				e.setDescription(newEvent.getDescription());
			
			if (newEvent.getEventAddress() != null)
				e.setEventAddress(newEvent.getEventAddress());
			
			if (newEvent.getEventName()!=null && !newEvent.getEventName().equals(new String("")))
				e.setEventName(newEvent.getEventName());
			
			if (newEvent.getHowMany() != 0)
				e.setHowMany(newEvent.getHowMany());
			
			if (newEvent.getInfo()!=null && !newEvent.getInfo().equals(new String("")))
				e.setInfo(newEvent.getInfo());
			
			if (newEvent.getMinDuration() != 0)
				e.setMinDuration(newEvent.getMinDuration());
			
			if (newEvent.getNPOName()!=null && !newEvent.getNPOName().equals(new String("")))
				e.setNPOName(newEvent.getNPOName());
			
			if (newEvent.getNumRaters() != 0)
				e.setNumRaters(newEvent.getNumRaters());
			
			if (newEvent.getOccurrenceKeys()!=null && !newEvent.getOccurrenceKeys().isEmpty())
				e.setOccurrenceKeys(newEvent.getOccurrenceKeys());
			
			if (newEvent.getPrerequisites()!=null && !newEvent.getPrerequisites().equals(new String("")))
				e.setPrerequisites(newEvent.getPrerequisites());
			
			if (newEvent.getSuitableFor()!=null && !newEvent.getSuitableFor().isEmpty())
				e.setSuitableFor(newEvent.getSuitableFor());
			
			if (newEvent.getSumRatings() != 0)
				e.setSumRatings(newEvent.getSumRatings());

			if (newEvent.getVolunteeringWith()!=null && !newEvent.getVolunteeringWith().isEmpty())
				e.setVolunteeringWith(newEvent.getVolunteeringWith());
			
			if (newEvent.getWorkType()!=null && !newEvent.getWorkType().isEmpty())
				e.setWorkType(newEvent.getWorkType());
		}
		catch (Exception e){
			throw e;
		}
		finally{
			pm.close();
		}
	}
	
	//Dumb batch insert for occurrences.
	public void addOccurrences(Collection<Occurrence> occurrences) throws Exception{
		PersistenceManager pm = null;
		
		Collection<Occurrence> put = new LinkedList<Occurrence>();
		
		Iterator<Occurrence> it = occurrences.iterator();
		
		int i = 0;
		while (it.hasNext()){
			put.add(it.next());
			i++;
			
			if (i%50 == 0 || !it.hasNext()){
				i=0;
				try {
					pm = PMF.get().getPersistenceManager();
					pm.makePersistentAll(put);		
				}
				catch (Exception e){
					throw e;
				}
				finally {
					pm.close();
				}
				put.clear();
			}
		}
		
	}
	
	public void addOccurrence(Occurrence newOccurrence){	
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
	
	public void editOccurrence(Occurrence newOccurrence) throws Exception{	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try {
			Occurrence o = pm.getObjectById(Occurrence.class, newOccurrence.getOccurrenceKey());
			
			if (newOccurrence.getContainingEventKey()!=null && !newOccurrence.getContainingEventKey().equals(new String("")))
				o.setContainingEventKey(newOccurrence.getContainingEventKey());
			
			if (newOccurrence.getEmail()!=null && !newOccurrence.getEmail().equals(new String("")))
				o.setEmail(newOccurrence.getEmail());
			
			if (newOccurrence.getEndTime() != null)
				o.setEndTime(newOccurrence.getEndTime());
			
			if (newOccurrence.getOccurrenceDate() != null)
				o.setOccurrenceDate(newOccurrence.getOccurrenceDate());
			
			if (newOccurrence.getRegisteredUserNames()!=null && !newOccurrence.getRegisteredUserNames().isEmpty())
				o.setRegisteredUserNames(newOccurrence.getRegisteredUserNames());
			
			if (newOccurrence.getStartTime() != null)
				o.setStartTime(newOccurrence.getStartTime());
		}
		catch (Exception e){
			throw e;
		}
		finally {
			pm.close();
		}
	}
	
	//Returns JSON String representation of the user's history.
	public String getUserHistory(String userName, Date userDate){
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		JsonObject jsonKarma = null;
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);

		String dateString;

		Calendar cal = Calendar.getInstance();
		cal.setTime(userDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		userDate = cal.getTime();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Karma.class);
		query.setFilter("userName == '" + userName + "'");
		
		try{
			@SuppressWarnings("unchecked")
			List<Karma> karmas = (List<Karma>) query.execute();
			Collections.sort(karmas);
			
			for (Karma karma : karmas){
				if (!karma.getActionType().equals(Karma.ActionType.PARTICIPATE))
					continue;
				
				Occurrence occurrence = pm.getObjectById(Occurrence.class, karma.getOccurrenceKey());
				
				if (occurrence.getOccurrenceDate().compareTo(userDate)>=0)
					continue;
				
				Event event = pm.getObjectById(Event.class, occurrence.getContainingEventKey());
				
				dateString = dateFormatter.format(occurrence.getOccurrenceDate());
				
				jsonKarma = new JsonObject();
				jsonKarma.addProperty("Date", dateString);
				jsonKarma.addProperty("Event", event.getEventName());
				jsonKarma.addProperty("Points", karma.getPoints());
				
				jsonArray.add(jsonKarma);
			}
		}
		finally{
			pm.close();
		}
		
		return gson.toJson(jsonArray);
	}
	
	public String getRegisteredFutureEvents(String userName, Date userDate){
		Gson gson = new Gson();
		JsonArray jsonArray = new JsonArray();
		JsonObject jsonEvent = null;
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		String dateString = "";
		int duration = 0;
		
		List<Occurrence> allOccurrences = new LinkedList<Occurrence>();
		Event event = null;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query todayQuery = pm.newQuery(Occurrence.class);
		todayQuery.declareParameters("java.util.Date today, java.util.Date now");
		todayQuery.setFilter("occurrenceDate == today && endTime > now && registeredUserNames == '" + userName + "'");
		todayQuery.setOrdering("endTime asc, occurrenceKey asc");
		
		Query futureQuery = pm.newQuery(Occurrence.class);
		futureQuery.declareParameters("java.util.Date today");
		futureQuery.setFilter("occurrenceDate > today && registeredUserNames == '" + userName + "'");
		futureQuery.setOrdering("occurrenceDate asc, endTime asc, occurrenceKey asc");
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(userDate);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = cal.getTime();
		
		cal.setTime(userDate);
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date now = cal.getTime();
		
		try {	
			@SuppressWarnings("unchecked")
			List<Occurrence> occurrences = (List<Occurrence>) todayQuery.execute(today,now);
			@SuppressWarnings("unchecked")
			List<Occurrence> futureOccurrences = (List<Occurrence>) futureQuery.execute(today);
			
			allOccurrences.addAll(occurrences);
			allOccurrences.addAll(futureOccurrences);
			
			for (Occurrence o : allOccurrences){
				event = (Event) pm.getObjectById(Event.class, o.getContainingEventKey());
				dateString = dateFormatter.format(o.getOccurrenceDate());
				
				Calendar c = Calendar.getInstance();
				c.setTime(o.getEndTime());
				
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(o.getStartTime());
				
				c.add(Calendar.HOUR_OF_DAY, -startCal.get(Calendar.HOUR_OF_DAY));
				c.add(Calendar.MINUTE, -startCal.get(Calendar.MINUTE));
				
				duration = c.get(Calendar.HOUR_OF_DAY);
				
				jsonEvent = new JsonObject();
				jsonEvent.addProperty("Date", dateString);
				jsonEvent.addProperty("Event", event.getEventName());
				jsonEvent.addProperty("Points", duration*1000);
				
				jsonArray.add(jsonEvent);
			}
		}
		finally{
			pm.close();
		}
		
		return gson.toJson(jsonArray);
	}
	
	public List<Event> getNextEventsByGood2GoPoint(Good2GoPoint gp, Date userDate, int duration, double distance, List<Event.VolunteeringWith> vw, List<Event.SuitableFor> sf, List<Event.WorkType> wt){
		List<Event> res = new LinkedList<Event>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Occurrence.class);
		String parameters = new String("java.util.Date today, java.util.Date now");
		query.setFilter("occurrenceDate == today && endTime > now");	
		query.setOrdering("endTime asc, containingEventKey asc");
		
		Calendar occCal = Calendar.getInstance();
		Calendar userCal = Calendar.getInstance();
		userCal.setTime(userDate);
		
		int year = userCal.get(Calendar.YEAR);
		int month = userCal.get(Calendar.MONTH);
		int day = userCal.get(Calendar.DATE);
		int hour = userCal.get(Calendar.HOUR_OF_DAY);
		int minute = userCal.get(Calendar.MINUTE);
		
		userCal.set(year, month,day,0,0,0);
		userCal.set(Calendar.MILLISECOND, 0);
		Date today = userCal.getTime();
		
		userCal.set(1970, Calendar.JANUARY,1,hour,minute,0);
		Date now = userCal.getTime();
		
		query.declareParameters(parameters);
		
		try {	
			@SuppressWarnings("unchecked")
			List<Occurrence> results = (List<Occurrence>) query.execute(today,now);
		
			if (results!=null && !results.isEmpty()) {
				Collections.sort(results);
				
				String eventKey = null;
				String lastEventKey = null;
				boolean isInserted = false;
				Event event = null;
				int minDuration = 0;
				
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
						event.getEventAddress();
						minDuration = Math.min(event.getMinDuration(),120);
						//Check lists.
						
						boolean containsVW = true;
						
						if (!vw.isEmpty()){
							containsVW = false;
							for (Event.VolunteeringWith curVW : event.getVolunteeringWith()){
								if (vw.contains(curVW)){
									containsVW = true;
									break;
								}
							}
						}
						
						boolean containsSF = true;
						
						if (!sf.isEmpty()){
							containsSF = false;
							for (Event.SuitableFor curSF : event.getSuitableFor()){
								if (sf.contains(curSF)){
									containsSF = true;
									break;
								}
							}
						}
						
						boolean containsWT = true;
						
						if (!wt.isEmpty()){
							containsWT = false;
							for (Event.WorkType curWT : event.getWorkType()){
								if (wt.contains(curWT)){
									containsWT = true;
									break;
								}
							}
						}
						
						//Check duration and distance.
						
						if ((duration!=-1 && minDuration>duration) || (distance>0 && event.getEventAddress().getGood2GoPoint().getDistance(gp)>distance) || !containsVW || !containsSF || !containsWT){
							isInserted = true;
							continue;
						}
						
						minHours = minDuration / 60;
						minMinutes = minDuration % 60;
					}
					
					
					occCal.setTime(occurrence.getEndTime());
					occCal.add(Calendar.HOUR_OF_DAY, -minHours);
					occCal.add(Calendar.MINUTE, -minMinutes);
					
					if (occCal.compareTo(userCal) > 0){
						event.addOccurrence(occurrence);
						res.add(event);
						isInserted = true;
					}
				}
			
				Collections.sort(res, new Good2GoPointComparator(gp));
			}
		}
		finally {
			query.closeAll();
			pm.close();
		}
		return res;
	}
}
