package com.gdma.good2goserver;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class BadgesMap {
	
	@Persistent
	@PrimaryKey
	private String badge;
	
	@Persistent
	private long points;
	
	public BadgesMap(String badge, long points){
		this.badge = badge;
		this.points = points;
	}
	
	public String getBadge(){
		return badge;
	}
	
	public long getPoints(){
		return points;
	}
	
	public void setPoints(long points){
		this.points = points;
	}
}
