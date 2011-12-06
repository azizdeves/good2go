package com.gdma.good2goserver;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import java.util.Date;

@PersistenceCapable
public class Karma {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key karmaKey;
	
	@Persistent
	private String userID;
	
	@Persistent
	private ActionType actionType;
	
	@Persistent
	private Date actionTime;
	
	@Persistent
	private Key occurrenceKey;
	
	public Karma(String userID, ActionType actionType, Date actionTime, Key occurrenceKey){
		this.userID = userID;
		this.actionType = actionType;
		this.actionTime = actionTime;
		this.occurrenceKey = occurrenceKey;
	}
	
	public Karma(String userID, ActionType actionType, Date actionTime){
		this(userID,actionType,actionTime,null);
	}
	
	public Key getKey(){
		return karmaKey;
	}

	public String getUserID() {
		return userID;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public Key getOccurrenceKey() {
		return occurrenceKey;
	}
	
	enum ActionType{
		OPEN_ACCOUNT(5),
		REGISTER_TO_EVENT(20),
		RATE_AN_EVENT(2);
		
		private final long points;
		
		ActionType(long points){
			this.points=points;
		}
		
		public long getPoints(){
			return points;
		}
	}

}