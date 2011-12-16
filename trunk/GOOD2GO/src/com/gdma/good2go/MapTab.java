package com.gdma.good2go;

import java.util.List;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;




public class MapTab extends MapActivity {
	
	
	private EventsDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        
        /**OVERLAY*/ 
        
        //All overlay elements on a map are held by the MapView, so when you want to add some, you have to get a list from the getOverlays() method.
        List<Overlay> mapOverlays = mapView.getOverlays();
        //instantiate the Drawable used for the map marker
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        //The constructor for G2GItemizedOverlay (your custom ItemizedOverlay) takes the Drawable in order to set the default marker for all overlay items
        EventsItemizedOverlay itemizedoverlay = new EventsItemizedOverlay(drawable,mapView);
        
          
        /**ADDING DB POINTS TO THE MAP OVERLAY*/
        
	    mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
	    
    	Cursor eventsCursor = mDbHelper.fetchAllEvents();
        startManagingCursor(eventsCursor); //is this needed?
        
        int cnt=0;//BUG IN CURSOR
        if(eventsCursor.moveToFirst()){
        	do{
        		EventOverlayItem point=MakeEventPoint(eventsCursor);
        		itemizedoverlay.addOverlay(point);
        		cnt++;//BUG IN CURSOR
        		}
        	while(eventsCursor.moveToNext()&&cnt!=6/*BUG IN CURSOR*/);
        }
        if(eventsCursor!=null&&!eventsCursor.isClosed()){
        	mDbHelper.close();
        }
        
        //GeoPoint gp=new GeoPoint (32074938,34775591);

        //itemizedoverlay.addOverlay(new EventOverlayItem(gp, "adi", "anna", "230"));
             
        /**SHOW ON MAP*/ 
        //Add the G2GItemizedOverlay to the MapView
        mapOverlays.add(itemizedoverlay);
        
		final MapController mc = mapView.getController();
		mc.animateTo(new GeoPoint (32069156,34774003));
		mc.setZoom(16);
        
    }

    private EventOverlayItem MakeEventPoint(Cursor eventsCursor) {    
    	
    	String rowID=eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENTID));
    	int gplat=Integer.parseInt(eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_GP_LAT)));
    	int gplong=Integer.parseInt(eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_GP_LONG)));
    	String name=eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENTNAME));
    	String info=eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_SHORT_INFO));

    	
    	GeoPoint gp= new GeoPoint(gplat,gplong); 
    	EventOverlayItem overlayitem = new EventOverlayItem(gp, name, info, rowID);

    	return overlayitem;	
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}