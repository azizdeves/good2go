package com.gdma.good2goserver;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.GeoPt;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

@PersistenceCapable
public class Event {

	public enum VolunteeringWith {
		ANIMALS, CHILDREN, ELDERLY, PHYSICALLY_CHALLENGED, MENTALLY_CHALLENGED, OTHER
	}

	public enum SuitableFor {
		CHILDREN, INDIVIDUALS, GROUPS
	}

	public enum WorkType {
		MENIAL, METNAL
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key eventKey;

	@Persistent
	private String eventName;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String description;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String prerequisites;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Date minDuration;

	@Persistent
	private boolean isArriveAnyTime;

	@Persistent
	@Embedded
	private Address eventAddress;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private long numRaters;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private long sumRatings;

	@Persistent
	private String NPOName;

	@Persistent
	private Set<VolunteeringWith> volunteeringWith;

	@Persistent
	private Set<SuitableFor> suitableFor;

	@Persistent
	private Set<WorkType> workType;
	
	@Persistent
	private boolean trainingRequired;

	//Occurrence Keys for persistence in database
	@Persistent
	private List<Key> occurrenceKeys;
	
	//Occurrences for extraction by applet
	private List<Occurrence> occurrences;
	
	public Event(){
		this.numRaters = 0;
		this.sumRatings = 0;
		this.occurrenceKeys = new LinkedList<Key>();
		this.occurrences = new LinkedList<Occurrence>();
	}
	
	public Event(String eventName, String description, String prerequisites, Date minDuration, boolean isArriveAnyTime,
				 Address eventAddress, String NPOName, Set<VolunteeringWith> volunteeringWith, Set<SuitableFor> suitableFor,
				 Set<WorkType> workType, boolean trainingRequired){
		this();
		
		this.eventName = eventName;
		this.description = description;
		this.prerequisites = prerequisites;
		this.minDuration = minDuration;
		this.isArriveAnyTime = isArriveAnyTime;
		this.eventAddress = eventAddress;
		this.NPOName = NPOName;
		this.volunteeringWith = volunteeringWith;
		this.suitableFor = suitableFor;
		this.workType = workType;
		this.trainingRequired = trainingRequired;
	}
	
	public Event(String eventName, String description, String prerequisites, Date minDuration, boolean isArriveAnyTime,
			 Address eventAddress, String NPOName, boolean trainingRequired){
		
		this(eventName, description, prerequisites, minDuration, isArriveAnyTime, eventAddress, NPOName,
			 null, null, null, trainingRequired);
	}
	
	public Key getEventKey() {
		return this.eventKey;
	}

	public String getEventName() {
		return eventName;
	}

	public String getDescription() {
		return description;
	}

	public String getPrerequisites() {
		return prerequisites;
	}

	public Date getMinDuration() {
		return minDuration;
	}

	public boolean isArriveAnyTime() {
		return isArriveAnyTime;
	}

	public Address getEventAddress() {
		return eventAddress;
	}

	public long getNumRaters() {
		return numRaters;
	}

	public long getSumRatings() {
		return sumRatings;
	}

	public String getNPOName() {
		return NPOName;
	}

	public Set<VolunteeringWith> getVolunteeringWith() {
		return volunteeringWith;
	}

	public Set<SuitableFor> getSuitableFor() {
		return suitableFor;
	}

	public Set<WorkType> getWorkType() {
		return workType;
	}

	public boolean isTrainingRequired() {
		return trainingRequired;
	}

	public List<Key> getOccurrenceKeys() {
		return occurrenceKeys;
	}
	
	public List<Occurrence> getOccurrences() {
		return occurrences;
	}

	protected void setEventKey(Key eventKey) {
		this.eventKey = eventKey;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}

	public void setMinDuration(Date minDuration) {
		this.minDuration = minDuration;
	}

	public void setArriveAnyTime(boolean isArriveAnyTime) {
		this.isArriveAnyTime = isArriveAnyTime;
	}

	public void setEventAddress(Address eventAddress) {
		this.eventAddress = eventAddress;
	}
	
	public void setEventAddress(String city, String street, short number, GeoPt geoPoint){
		this.eventAddress = new Address(city,street,number,geoPoint);
	}

	public void setNumRaters(long numRaters) {
		this.numRaters = numRaters;
	}

	public void setSumRatings(long sumRatings) {
		this.sumRatings = sumRatings;
	}

	public void setNPOName(String nPOName) {
		NPOName = new String(nPOName);
	}

	public void setVolunteeringWith(Set<VolunteeringWith> volunteeringWith) {
		this.volunteeringWith = volunteeringWith;
	}

	public void addVolunteeringWith(VolunteeringWith vw) {
		this.volunteeringWith.add(vw);
	}
	
	public void removeVolunteeringWith(VolunteeringWith vw){
		this.volunteeringWith.remove(vw);
	}

	public void setSuitableFor(Set<SuitableFor> suitableFor) {
		this.suitableFor = suitableFor;
	}
	
	public void addSuitableFor(SuitableFor sf){
		this.suitableFor.add(sf);
	}
	
	public void removeSuitableFor(SuitableFor sf){
		this.suitableFor.remove(sf);
	}

	public void setWorkType(Set<WorkType> workType) {
		this.workType = workType;
	}
	
	public void addWorkType(WorkType wt){
		this.workType.add(wt);
	}
	
	public void removeWorkType(WorkType wt){
		this.workType.remove(wt);
	}

	public void setTrainingRequired(boolean trainingRequired) {
		this.trainingRequired = trainingRequired;
	}

	public void setOccurrenceKeys(List<Key> occurrenceKeys) {
		this.occurrenceKeys = occurrenceKeys;
	}
	
	public void addOccurrenceKey(Key occurrenceKey) {
		this.occurrenceKeys.add(occurrenceKey);
	}
	
	public void removeOccurrenceKey(Key occurrenceKey) {
		this.occurrenceKeys.remove(occurrenceKey);
	}
	
	public void setOccurrences(List<Occurrence> occurrences) {
		this.occurrences = occurrences;
	}
	
	public void addOccurrence(Occurrence occurrence) {
		this.occurrences.add(occurrence);
	}
	
	public void removeOccurrence(Occurrence occurrence) {
		this.occurrences.remove(occurrence);
	}

	@PersistenceCapable
	@EmbeddedOnly
	public static class Address {
		@Persistent
		private String city;

		@Persistent
		private String street;

		@Persistent
		private short number;

		@Persistent
		private GeoPt geoPoint;

		public Address(String city, String street, short number, GeoPt geoPoint) {
			this.setCity(city);
			this.setStreet(street);
			this.setNumber(number);
			this.setGeoPoint(geoPoint);
		}
		
		public Address(){
		}
		
		public Address(Address a) {
			this(a.getCity(),a.getStreet(),a.getNumber(),a.getGeoPoint());
		}

		public Address(GeoPt geoPoint) {
			this(null,null,(short) 0,geoPoint);
		}

		public Address(String city, String street, short number) {
			this(city,street,number,null);
		}

		public String getCity() {
			return city;
		}

		public String getStreet() {
			return street;
		}

		public short getNumber() {
			return number;
		}

		public GeoPt getGeoPoint() {
			return geoPoint;
		}
		
		public void setCity(String city) {
			this.city = city;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public void setNumber(short number) {
			this.number = number;
		}

		public void setGeoPoint(GeoPt geoPoint) {
			this.geoPoint = geoPoint;
		}
	}

}