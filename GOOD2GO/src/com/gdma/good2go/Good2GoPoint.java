package com.gdma.good2go;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.EmbeddedOnly;

@PersistenceCapable
@EmbeddedOnly
public class Good2GoPoint {
	@Persistent
	private int lat;
	
	@Persistent
	private int lon;
	
	public Good2GoPoint(int lat, int lon){
		this.lat=lat;
		this.lon=lon;
	}
	
	public Good2GoPoint(){
	}
	
	protected void setLat(int lat){
		this.lat=lat;
	}
	
	protected void setLon(int lon){
		this.lon=lon;
	}
	
	public int getLat(){
		return lat;
	}
	
	public int getLon(){
		return lon;
	}
	
	public double getDistance(Good2GoPoint otherPoint){
		double myLon = (double) this.getLon();
		double otherLon = (double) otherPoint.getLon();
		double lon = (myLon - otherLon)/10000;
		
		double myLat = (double) this.getLat();
		double otherLat = (double) otherPoint.getLat();
		double lat = (myLat - otherLat)/10000;
		
		return (Math.sqrt((lon*lon + lat*lat)))*1.852;
	}
}
