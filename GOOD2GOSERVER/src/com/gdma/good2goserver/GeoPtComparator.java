package com.gdma.good2goserver;

import java.util.Comparator;
import com.google.appengine.api.datastore.GeoPt;

/*
 * Comparator for comparing distances between GeoPt-s in Event-s.
 * Returns squared distance between points.
 * 0 means it is the same point.
 */

public class GeoPtComparator implements Comparator<Event>{
	
	private GeoPt center;
	
	public GeoPtComparator(GeoPt center){
		this.center = center;
	}
	
    @Override
    public int compare(Event o1, Event o2) {
    	GeoPt g1 = o1.getEventAddress().getGeoPoint();
    	GeoPt g2 = o2.getEventAddress().getGeoPoint();
    	
        float lon1 = g1.getLongitude()-center.getLongitude();
        float lat1 = g1.getLatitude()-center.getLatitude();
        float dis1 = lon1*lon1 + lat1*lat1;
        
        float lon2 = g2.getLongitude()-center.getLongitude();
        float lat2 = g2.getLatitude()-center.getLatitude();
        float dis2 = lon2*lon2 + lat2*lat2;
        
        if (dis1<dis2)
        	return -1;
        if (dis2<dis1)
        	return 1;
        return 0;
    }
}
