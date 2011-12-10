package com.gdma.good2goserver;

import java.util.Comparator;
import com.google.appengine.api.datastore.GeoPt;

/*
 * Comparator for comparing distances between GeoPt-s in Event-s.
 * Returns squared distance between points.
 * 0 means it is the same point.
 */

public class GeoPtComparator implements Comparator<Event>{
	
    @Override
    public int compare(Event o1, Event o2) {
    	GeoPt g1 = o1.getEventAddress().getGeoPoint();
    	GeoPt g2 = o2.getEventAddress().getGeoPoint();
    	
        int lon = (int) ((g1.getLongitude()-g2.getLongitude())*1000);
        int lat = (int) ((g1.getLatitude()-g2.getLatitude())*1000);
        return lon*lon + lat*lat;
    }
}
