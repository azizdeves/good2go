package com.gdma.good2go;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.gdma.good2go.mapviewbaloons.BalloonItemizedOverlay;
import com.google.android.maps.MapView;


/*This class can manage a whole set of Overlay (which are the individual items placed on the map)*/
public class EventsItemizedOverlay extends BalloonItemizedOverlay<EventOverlayItem> {
	
	/**ranisawesome*/
	
	/*The array in which we'll put each of the OverlayItem objects we want on the map*/
	private ArrayList<EventOverlayItem> mOverlays = new ArrayList<EventOverlayItem>();
	
	/*a reference to the application Context - to handle touch events on the overlay items*/
	private Context mContext;
	
/*	The constructor must define the default marker for each of the EventOverlayItems. 
	 * In order for the Drawable to actually get drawn, it must have its bounds defined. 
	 * Most commonly, you want the center-point at the bottom of the image to be the point at which it's attached to the map coordinates. 
	 * This is handled for you with the boundCenterBottom() method.
	 * Plus initialize context*/

	public EventsItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		mContext = mapView.getContext();
	}
	/*In order to add new EventOverlayItems to the ArrayList*/
	public void addOverlay(EventOverlayItem overlay) {
	    mOverlays.add(overlay);
	    /*Each time you add a new EventOverlayItem to the ArrayList, you must call populate() for the ItemizedOverlay, 
	     * which will read each of the EventOverlayItems and prepare them to be drawn*/
	    populate();
	}
	
	/*When the populate() method executes, it will call createItem(int) in the ItemizedOverlay to retrieve each EventOverlayItem. 
	 * You must override this method to properly read from the ArrayList and return the EventOverlayItem
	 *  from the position specified by the given integer*/
	@Override
	protected EventOverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	protected boolean onBalloonTap(int index, EventOverlayItem item) {
        
		Intent i = new Intent(mContext, EventDetails.class);
        i.putExtra("sender", "map");
        i.putExtra(EventsDbAdapter.KEY_EVENTID, item.getRowID());
    
		mContext.startActivity(i);
		return true;
}
}
