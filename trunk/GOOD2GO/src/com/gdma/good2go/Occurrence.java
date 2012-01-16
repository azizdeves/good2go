package com.gdma.good2go;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Occurrence implements Comparable<Occurrence>{
	@Persistent
	@PrimaryKey
    private String occurrenceKey;
	
	@Persistent
	private String containingEventKey;

	@Persistent
	private Date occurrenceDate;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Date startTime;

	@Persistent
	private Date endTime;

	@Persistent
	private List<String> registeredUserNames;

	public Occurrence() {
		this.occurrenceKey = null;
		this.containingEventKey = "";
		this.occurrenceDate = null;
		this.startTime = null;
		this.endTime = null;
		this.registeredUserNames = new LinkedList<String>();
	}
	
	public Occurrence(String containingEventKey, Date occurrenceDate, Date startTime, Date endTime){
		this();
		
		this.setContainingEventKey(containingEventKey);
		this.setOccurrenceDate(occurrenceDate);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}
	
	public String getOccurrenceKey() {
		return this.occurrenceKey;
	}
	
	public String getContainingEventKey() {
		return this.containingEventKey;
	}

	public Date getOccurrenceDate() {
		return occurrenceDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<String> getRegisteredUserNames() {
		return registeredUserNames;
	}

	protected void setOccurrenceKey(String occurrenceKey) {
		this.occurrenceKey = occurrenceKey;
	}
	
	public void setContainingEventKey(String containingEventKey){
		this.containingEventKey = containingEventKey;
	}
	
	public void setOccurrenceDate(Date occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}

	public void setOccurrenceDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.occurrenceDate = calendar.getTime();
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setStartTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(1970, Calendar.JANUARY, 1, hour, minute, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.startTime = calendar.getTime();
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setEndTime(int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(1970, Calendar.JANUARY, 1, hour, minute, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.endTime = calendar.getTime();
	}

	protected void setRegisteredUserNames(List<String> registeredUserNames) {
		this.registeredUserNames = registeredUserNames;
	}
	
	public void addRegisteredUser(String newName) {
		this.registeredUserNames.add(newName);
	}

	public void removeRegisteredUser(String registeredName) {
		this.registeredUserNames.remove(registeredName);
	}

	@Override
	public int compareTo(Occurrence o) {
		int keyDiff = this.getContainingEventKey().compareTo(o.getContainingEventKey());
		
		if (keyDiff!=0)
			return keyDiff;
		
		if (this.getEndTime().getTime() > o.getEndTime().getTime())
			return 1;
		
		if (this.getEndTime().getTime() < o.getEndTime().getTime())
			return -1;
		
		return 0;
	}
}
