package com.gdma.good2goserver;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Occurrence{
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Key occurrenceKey;
	
	@Persistent
	private Key eventKey;

	@Persistent
	private Date eventDate;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Date startTime;

	@Persistent
	private Date endTime;

	@Persistent
	private List<String> registeredUserNames;

	public Occurrence() {
		this.registeredUserNames = new LinkedList<String>();
	}
	
	public Occurrence(Key eventKey, Date occurrenceDate, Date startTime, Date endTime){
		this();
		
		this.setEventKey(eventKey);
		this.setEventDate(eventDate);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}
	
	public Key getOccurrenceKey() {
		return this.occurrenceKey;
	}
	
	public Key getEventKey() {
		return this.eventKey;
	}

	public Date getEventDate() {
		return (Date) eventDate.clone();
	}

	public Date getStartTime() {
		return (Date) startTime.clone();
	}

	public Date getEndTime() {
		return (Date) endTime.clone();
	}

	public List<String> getRegisteredUserNames() {
		return registeredUserNames;
	}

	protected void setOccurrenceKey(Key occurrenceKey) {
		this.occurrenceKey = occurrenceKey;
	}
	
	public void setEventKey(Key eventKey){
		this.eventKey = eventKey;
	}
	
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(year, month, day, 0, 0, 0);
		this.eventDate = calendar.getTime();
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setStartTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(0, 0, 0, hour, minute, 0);
		this.startTime = calendar.getTime();
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setEndTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(0, 0, 0, hour, minute, 0);
		this.endTime = calendar.getTime();
	}

	public void addRegisteredUser(String newName) {
		this.registeredUserNames.add(newName);
	}

	public void removeRegisteredUser(String registeredName) {
		this.registeredUserNames.remove(registeredName);
	}

	/*@Override
	public int compareTo(Occurrence o) {
		int keyDiff = this.getEventKey().compareTo(o.getEventKey());
		
		if (keyDiff!=0)
			return keyDiff;
		
		if (this.getEndTime().getTime() > o.getEndTime().getTime())
			return 1;
		
		if (this.getEndTime().getTime() < o.getEndTime().getTime())
			return -1;
		
		return 0;
	}*/
}
