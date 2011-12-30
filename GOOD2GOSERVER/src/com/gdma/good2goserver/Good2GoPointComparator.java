package com.gdma.good2goserver;

import java.util.Comparator;

/*
 * Comparator for comparing distances between GeoPt-s in Event-s.
 * Returns squared distance between points.
 * 0 means it is the same point.
 */

public class Good2GoPointComparator implements Comparator<Event>{
	
	private Good2GoPoint center;
	
	public Good2GoPointComparator(Good2GoPoint center){
		this.center = center;
	}
	
    @Override
    public int compare(Event o1, Event o2) {
    	Good2GoPoint g1 = o1.getEventAddress().getGood2GoPoint();
    	Good2GoPoint g2 = o2.getEventAddress().getGood2GoPoint();
    	
        long lon1 = g1.getLon()-center.getLon();
        long lat1 = g1.getLat()-center.getLat();
        long dis1 = lon1*lon1 + lat1*lat1;
        
        long lon2 = g2.getLon()-center.getLon();
        long lat2 = g2.getLat()-center.getLat();
        long dis2 = lon2*lon2 + lat2*lat2;
        
        if (dis1<dis2)
        	return -1;
        if (dis2<dis1)
        	return 1;
        return 0;
    }
}
