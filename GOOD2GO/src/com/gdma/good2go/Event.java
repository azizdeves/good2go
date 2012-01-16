package com.gdma.good2go;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.EmbeddedOnly;

//import com.google.appengine.api.datastore.GeoPt;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

@PersistenceCapable
public class Event{

	public enum VolunteeringWith{
		ANIMALS, CHILDREN, ELDERLY, DISABLED, ENVIRONMENT, DISADVANTAGED, SPECIAL, OTHER;
		
		public static boolean isMember(String s){
			for (VolunteeringWith vw : VolunteeringWith.values()){
				if (vw.name().equalsIgnoreCase(s))
					return true;
			}
			
			return false;
		}
	}

	public enum SuitableFor{
		KIDS, INDIVIDUALS, GROUPS;
		
		public static boolean isMember(String s){
			for (SuitableFor sf : SuitableFor.values()){
				if (sf.name().equalsIgnoreCase(s))
					return true;
			}
			
			return false;
		}
	}

	public enum WorkType{
		MENIAL, MENTAL;
		
		public static boolean isMember(String s){
			for (WorkType wt : WorkType.values()){
				if (wt.name().equals(s))
					return true;
			}
			
			return false;
		}
	}

	@PrimaryKey
	@Persistent
	private String eventKey;

	@Persistent
	private String eventName;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String info;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String description;
	
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String content;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String prerequisites;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private int minDuration;

	@Persistent
	private boolean isArriveAnyTime;
	
	@Persistent
	private short howMany;

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
	private List<String> occurrenceKeys;
	
	//Occurrences for extraction by applet
	@NotPersistent
	private List<Occurrence> occurrences;
	
	public Event(){
		this.eventName = "";
		this.info = "";
		this.description = "";
		this.content = "";
		this.prerequisites = "";
		this.minDuration = 0;
		this.isArriveAnyTime = false;
		this.howMany = 0;
		this.NPOName = "";
		this.numRaters = 0;
		this.sumRatings = 0;
		this.occurrenceKeys = new LinkedList<String>();
		this.occurrences = new LinkedList<Occurrence>();
		this.eventAddress = null;
		this.suitableFor = new HashSet<Event.SuitableFor>();
		this.volunteeringWith = new HashSet<Event.VolunteeringWith>();
		this.workType = new HashSet<Event.WorkType>();
		this.trainingRequired = false;
	}
	
	public Event(String eventName, String info, String description, String content, String prerequisites, int minDuration, boolean isArriveAnyTime,
				 Address eventAddress,short howMany, String NPOName, Set<VolunteeringWith> volunteeringWith, Set<SuitableFor> suitableFor,
				 Set<WorkType> workType, boolean trainingRequired){
		
		this.eventName = eventName;
		this.info = info;
		this.description = description;
		this.content = content;
		this.prerequisites = prerequisites;
		this.minDuration = minDuration;
		this.isArriveAnyTime = isArriveAnyTime;
		this.howMany = howMany;
		this.eventAddress = eventAddress;
		this.NPOName = NPOName;
		this.numRaters = 0;
		this.sumRatings = 0;
		this.occurrenceKeys = new LinkedList<String>();
		this.occurrences = new LinkedList<Occurrence>();
		this.volunteeringWith = volunteeringWith;
		this.suitableFor = suitableFor;
		this.workType = workType;
		this.trainingRequired = trainingRequired;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Event)
			return ((Event) o).getEventKey().equals(this.getEventKey());
		
		return false;
	}
	
	public Event(String eventName, String info, String description, String content, String prerequisites, int minDuration, boolean isArriveAnyTime,
			 Address eventAddress,short howMany, String NPOName, boolean trainingRequired){
		
		this(eventName, info, description, content, prerequisites, minDuration, isArriveAnyTime, eventAddress, howMany, NPOName,
			 null, null, null, trainingRequired);
	}
	
	public String getEventKey() {
		return this.eventKey;
	}

	public String getEventName() {
		return eventName;
	}

	public String getInfo() {
		return info;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getContent() {
		return content;
	}

	public String getPrerequisites() {
		return prerequisites;
	}

	public int getMinDuration() {
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
	
	public double getDistance(int lon, int lat){
		Good2GoPoint userGp =new Good2GoPoint();
		userGp.setLon(lon);
		userGp.setLat(lat);
		Good2GoPoint eventgp = this.getEventAddress().getGood2GoPoint();
		return eventgp.getDistance(userGp);
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

	public List<String> getOccurrenceKeys() {
		return occurrenceKeys;
	}
	
	public List<Occurrence> getOccurrences() {
		return occurrences;
	}

	protected void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}

	public void setMinDuration(int minDuration) {
		this.minDuration = minDuration;
	}

	public void setArriveAnyTime(boolean isArriveAnyTime) {
		this.isArriveAnyTime = isArriveAnyTime;
	}

	public void setEventAddress(Address eventAddress) {
		this.eventAddress = eventAddress;
	}
	
	public void setEventAddress(String city, String street, short number, Good2GoPoint good2GoPoint){
		this.eventAddress = new Address(city,street,number,good2GoPoint);
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

	public void setOccurrenceKeys(List<String> occurrenceKeys) {
		this.occurrenceKeys = occurrenceKeys;
	}
	
	public void addOccurrenceKey(String occurrenceKey) {
		this.occurrenceKeys.add(occurrenceKey);
	}
	
	public void removeOccurrenceKey(String occurrenceKey) {
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
	
	public void incNumRaters(){
		numRaters++;
	}
	
	public void addRating(int rating){
		sumRatings+=(long) rating;
	}

	public short getHowMany() {
		return howMany;
	}

	public void setHowMany(short howMany) {
		this.howMany = howMany;
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
		@Embedded
		private Good2GoPoint good2GoPoint;

		public Address(String city, String street, short number, Good2GoPoint good2GoPoint) {
			this.setCity(city);
			this.setStreet(street);
			this.setNumber(number);
			this.setGood2GoPoint(good2GoPoint);
		}
		
		public Address(){
			this.city = "";
			this.street = "";
			this.number = 0;
			this.good2GoPoint = null;
		}
		
		public Address(Address a) {
			this(a.getCity(),a.getStreet(),a.getNumber(),a.getGood2GoPoint());
		}

		public Address(Good2GoPoint good2GoPoint) {
			this(null,null,(short) 0,good2GoPoint);
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

		public Good2GoPoint getGood2GoPoint() {
			return good2GoPoint;
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

		public void setGood2GoPoint(Good2GoPoint good2GoPoint) {
			this.good2GoPoint = good2GoPoint;
		}
	}

}