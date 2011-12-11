package com.gdma.good2go;

import java.util.List;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.gdma.good2go.communication.RestClient;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import flexjson.JSONDeserializer;




public class MapTab extends MapActivity {
	
	
	private EventsDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        
        /**CLIENT-SERVER**/
        /**@TODO THIS IS NOT THE CORRECT PLACE FOR THIS*/

		String JSONResponse = null; // this will hold the response from server
		
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getEvents");
		client.AddParam("lon", "3124.872");
		client.AddParam("lat", "3346.115");
		
		try{
		client.Execute(1); //1 is GET
		}
		catch (Exception e){}
		
		JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		List<Event> eventList = new JSONDeserializer<List<Event>>().deserialize(JSONResponse);

        
        /**POPULATE DB**/
        mDbHelper = new EventsDbAdapter(this);
        mDbHelper.open();
        for(Event event : eventList)
        {
        	String lat=Integer.toString(
        			event.getEventAddress().getGood2GoPoint().getLat());
        	String lon=Integer.toString(
        			event.getEventAddress().getGood2GoPoint().getLon());
        	
        			mDbHelper.createEvent(event.getEventName(),
        			event.getDescription(),
        			event.getPrerequisites(),
        			lat, lon);
        }
                  
       /**OVERLAY*/ 
        //All overlay elements on a map are held by the MapView, so when you want to add some, you have to get a list from the getOverlays() method.
        List<Overlay> mapOverlays = mapView.getOverlays();
        //instantiate the Drawable used for the map marker
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        //The constructor for G2GItemizedOverlay (your custom ItemizedOverlay) takes the Drawable in order to set the default marker for all overlay items
        EventsItemizedOverlay itemizedoverlay = new EventsItemizedOverlay(drawable,mapView);
        
          
        
        /**ADDING DB POINTS TO THE MAP OVERLAY*/
        // Get all of the rows from the database and create the item list
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