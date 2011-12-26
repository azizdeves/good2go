package com.gdma.good2go;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

import com.gdma.good2go.Event.VolunteeringWith;
import com.gdma.good2go.communication.RestClient;
import com.google.android.maps.GeoPoint;

import flexjson.JSONDeserializer;

public class MainScreen extends TabActivity {
	
//	Facebook facebook = new Facebook("327638170583802"); //new facebook app instance;
	
	private EventsDbAdapter mDbHelper;
	private GeoPoint mMyGeoPoint;
	private Location mMyLocation;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**GET MY LOCATION**/
        
        /**TODO: add actual calculation**/
        mMyGeoPoint=new GeoPoint((int)(32.055699*1E6),(int)(34.769540*1E6));       
        mMyLocation = new Location("myLocation"); 	
        mMyLocation.setLatitude(mMyGeoPoint.getLatitudeE6()/1E6);
        mMyLocation.setLongitude(mMyGeoPoint.getLongitudeE6()/1E6);
        
        /**GET EVENTS FROM SERVER**/
        // TODO on resume check if we have the events if not - repopulate
        List<Event> eventList=remote_getEventsFromServer(mMyGeoPoint.getLatitudeE6(),mMyGeoPoint.getLongitudeE6());

        /**POPULATE DB**/
        /**TODO: drop what we have if it's a new day
         * or if there's been a change since last read
         */
        mDbHelper = new EventsDbAdapter(this);
        mDbHelper.open();
        for(Event event : eventList)
        {
        	String eventLat=Integer.toString(
        			event.getEventAddress().getGood2GoPoint().getLat());
        	String eventLon=Integer.toString(
        			event.getEventAddress().getGood2GoPoint().getLon());
        	    	        	
        	//calculate distance
    	   	float results[]=new float[3];
        	Location.distanceBetween(mMyLocation.getLatitude(), mMyLocation.getLongitude(), 
        			Integer.valueOf(eventLat)/1E6, Integer.valueOf(eventLon)/1E6, results);
        	String distance=String.format("%.1f", (float)(results[0]/1E3))+" km";
        	
        	//calculate duration        	
        	String duration=String.valueOf(event.getMinDuration().getMinutes())+ " min";
        	       	       	
        	//populate db.

        	Set<VolunteeringWith> listType = event.getVolunteeringWith();
        	String animals="0", children="0", disabled="0", elderly="0", environment="0", special="0" ;
        	
        	for (Iterator<VolunteeringWith> iterator = listType.iterator(); iterator.hasNext();) {
				VolunteeringWith volunteeringWith = (VolunteeringWith) iterator.next();
				if(volunteeringWith==VolunteeringWith.ANIMALS)
					animals="1";
				else if(volunteeringWith==VolunteeringWith.CHILDREN)
					children="1";
//				else if(volunteeringWith==VolunteeringWith.DISABLED)
//					disabled="1";
//				else if(volunteeringWith==VolunteeringWith.ELDERLY)
//					elderly="1";
//				else if(volunteeringWith==VolunteeringWith.ENVIRONMENT)
//					environment="1";
//				else if(volunteeringWith==VolunteeringWith.SPECIAL)
//					special="1";
			
			}
        	mDbHelper.createEvent(event.getEventKey(),event.getEventName(),
        			event.getDescription(),
        			event.getPrerequisites(),
        			eventLat, eventLon,distance,duration,
        			event.getEventAddress().getCity(),
        			event.getEventAddress().getStreet(),
        			String.valueOf(event.getEventAddress().getNumber()),
        			animals, children,disabled,
        			elderly,environment,special);
        }
        
        /** SHOW TABS**/
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MapTab.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("map").setIndicator("",
                          res.getDrawable(R.drawable.ic_tab_map))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, ListTab.class);
        spec = tabHost.newTabSpec("list").setIndicator("",
                          res.getDrawable(R.drawable.ic_tab_list))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, FilterTab.class);
        spec = tabHost.newTabSpec("search").setIndicator("",
                          res.getDrawable(R.drawable.ic_tab_search))
                      .setContent(intent);
        tabHost.addTab(spec);

      
        intent = new Intent().setClass(this, MeTab.class);
        spec = tabHost.newTabSpec("me").setIndicator("",
                          res.getDrawable(R.drawable.ic_tab_map))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        //Set the map tab to be first 
        tabHost.setCurrentTab(0);
    }

	private List<Event> remote_getEventsFromServer(int lat, int lon) {
		//TODO is this supposed to be async task?

		
		String JSONResponse = null; // this will hold the response from server
		
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getEvents");
		client.AddParam("lon", String.valueOf(lon/1000));
		client.AddParam("lat", String.valueOf(lat/1000));
		client.AddParam("userDate", (new Date()).toString());
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this, "Error: could not connect to the server", Toast.LENGTH_LONG);
			debugging.show();
		}
		
		JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		return new JSONDeserializer<List<Event>>().deserialize(JSONResponse);
	}

}