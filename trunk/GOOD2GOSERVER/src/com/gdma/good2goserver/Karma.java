package com.gdma.good2goserver;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@PersistenceCapable
public class Karma implements Comparable<Karma>{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String karmaKey;
	
	@Persistent
	private String userName;
	
	@Persistent
	private ActionType actionType;
	
	@Persistent
	private Date actionTime;
	
	@Persistent
	private String occurrenceKey;
	
	public Karma(String userName, ActionType actionType, Date actionTime, String occurrenceKey){
		this.userName = userName;
		this.actionType = actionType;
		this.actionTime = actionTime;
		this.occurrenceKey = occurrenceKey;
	}
	
	public Karma(String userID, ActionType actionType, Date actionTime){
		this(userID,actionType,actionTime,null);
	}
	
	public String getKey(){
		return karmaKey;
	}

	public String getUserName() {
		return userName;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public String getOccurrenceKey() {
		return occurrenceKey;
	}
	
	enum ActionType{
		OPEN_ACCOUNT(10),
		OPEN_APP(10),
		SEARCH_EVENT(10), //once a day
		REGISTER_TO_EVENT(100),
		INVITE_FRIEND(50),
		FRIEND_REGISTERED(100),
		RATE_AN_EVENT(10),
		NO_RATE(0),
		POST_STATUS(50);
		
		private final long points;
		
		ActionType(long points){
			this.points=points;
		}
		
		public long getPoints(){
			return points;
		}
		
		public static boolean isMember(String s){
			for (ActionType at : ActionType.values()){
				if (at.name().equals(s))
					return true;
			}
			
			return false;
		}
	}
	
	public enum Badge{
		MR_NICE_GUY(0,"Mr. Nice Guy"),
		ANGEL(3000,"Angel"),
		SAINT(5000,"Saint"),
		MOTHER_TERESA(7000,"Mother Teresa"),
		BUDDHIST_MONK(10000,"Buddhist Monk"),
		DALAI_LAMA(50000,"Dalai Lama"),
		GOD(100000,"GOD!");
		
		private final long points;
		
		private final String badgeName;
		
		Badge(long points, String badgeName){
			this.points = points;
			this.badgeName = badgeName;
		}
		
		public static Badge getMyBadge(long points){
			Badge highest = Badge.MR_NICE_GUY;
			long highPoints = Badge.MR_NICE_GUY.getPoints();
			
			for (Badge curBadge : Badge.values()){
				long curPoints = curBadge.getPoints();
				if (curPoints > highPoints && curPoints<points){
					highPoints = curPoints;
					highest = curBadge;
				}
			}			
			
			return highest;
		}
		
		public static List<Badge> getMyBadges(long points){
			List<Badge> badges = new LinkedList<Badge>();

			for (Badge curBadge : Badge.values()){
				long curPoints = curBadge.getPoints();
				if (curPoints<points){
					badges.add(curBadge);
				}
			}			
			
			return badges;
		}
		
		public long getPoints(){
			return points;
		}
		
		public String getName(){
			return badgeName;
		}
		
		public String toString(){
			return badgeName;
		}
	}

	@Override
	public int compareTo(Karma other) {
		return this.getActionTime().compareTo(other.getActionTime());
	}

}