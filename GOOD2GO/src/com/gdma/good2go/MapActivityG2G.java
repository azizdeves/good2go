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
import com.google.android.maps.OverlayItem;


public class MapActivityG2G extends MapActivity {
	
	
	private EventsDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        /**POPULATE DB**/
        mDbHelper = new EventsDbAdapter(this);
        mDbHelper.open();
        
        mDbHelper.createEvent("Fun Horseback Riding", 
        		"Help teenagers and enjoy a horseack ride!",
        		"Assist handicapped teenagers in therapeutic horse-back riding," 
        		+ "lead their horse and help them follow instructors commands.",
        		"32069156", "34774003");
        
        mDbHelper.createEvent("Dogs are our best friends", 
        		"Have a walk with a city chelter dog!",
        		"Make a furry cute friend for life!",
        		"32069211", "34763403");
        
        mDbHelper.createEvent("Surf the internet", 
        		"Share what you know by teaching internet to kids!",
        		"Show the wonders of Google and Wikipedia to our youngest.",
        		"32086865", "34789581");
        
        mDbHelper.createEvent("Read your favourite book", 
        		"Read anything you like to the elderly",
        		"Make someone happy and provide company to the elderly",
        		"32074938", "34775591");
        
        mDbHelper.createEvent("Konichiwa", 
        		"Adi's in the house!",
        		"Ran and Adi forever!!!!",
        		"32055555", "34769572");
        
        mDbHelper.createEvent("Give a hot meal to the needy", 
        		"Help pack and distribute hot meals to those in need",
        		"They are very hungry. Help them.",
        		"32063374", "34773080");
        
                  
       /**OVERLAY*/ 
        //All overlay elements on a map are held by the MapView, so when you want to add some, you have to get a list from the getOverlays() method.
        List<Overlay> mapOverlays = mapView.getOverlays();
        //instantiate the Drawable used for the map marker
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        //The constructor for G2GItemizedOverlay (your custom ItemizedOverlay) takes the Drawable in order to set the default marker for all overlay items
        ItemizedOverlayG2G itemizedoverlay = new ItemizedOverlayG2G(drawable,mapView);
        
          
        
        /**ADDING DB POINTS TO THE MAP OVERLAY*/
        // Get all of the rows from the database and create the item list
    	Cursor eventsCursor = mDbHelper.fetchAllEvents();

        startManagingCursor(eventsCursor); //is this needed?
        
        if(eventsCursor.moveToFirst()){
        	do{
        		OverlayItem point=MakeEventPoint(eventsCursor);
        		itemizedoverlay.addOverlay(point);
        		}
        	while(eventsCursor.moveToNext());
        }
        if(eventsCursor!=null&&!eventsCursor.isClosed()){
        	mDbHelper.close();
        }
             
        /**SHOW ON MAP*/ 
        //Add the G2GItemizedOverlay to the MapView
        mapOverlays.add(itemizedoverlay);
        
		final MapController mc = mapView.getController();
		mc.animateTo(new GeoPoint (32069156,34774003));
		mc.setZoom(16);
        
    }

    private OverlayItem MakeEventPoint(Cursor eventsCursor) {
    	
    	int gplat=Integer.parseInt(eventsCursor.getString(eventsCursor.getColumnIndex("gplat")));
    	int gplong=Integer.parseInt(eventsCursor.getString(eventsCursor.getColumnIndex("gplong")));
    	String name=eventsCursor.getString(eventsCursor.getColumnIndex("name"));
    	String details=eventsCursor.getString(eventsCursor.getColumnIndex("details"));
    	
    	GeoPoint gp= new GeoPoint(gplat,gplong); 
    	OverlayItem overlayitem = new OverlayItem(gp, name, details);
		
    	return overlayitem;	
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}