/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gdma.good2go.Event;
import com.gdma.good2go.EventsDbAdapter;
import com.gdma.good2go.FilterTab;
import com.gdma.good2go.ListTab;
import com.gdma.good2go.MapTab;
import com.gdma.good2go.MeTab;
import com.gdma.good2go.Event.VolunteeringWith;
import com.gdma.good2go.communication.RestClient;
import com.google.android.maps.GeoPoint;

import flexjson.JSONDeserializer;

public class MainActivity extends ActionBarActivity {
	
	/*******************************************
	              *******GOOD2GO***
	 ******************************************/
	private EventsDbAdapter mDbHelper;
	private GeoPoint mMyGeoPoint;
	private Location mMyLocation;
	/*******************************************
     *******GOOD2GO***
     *******************************************/

    private boolean mAlternateTitle = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        getActionBarHelper().setRefreshActionItemState(true);
    	/*******************************************
         *******GOOD2GO***
         *******************************************/
        
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
        	
        	for (Iterator<VolunteeringWith> iterator = listType.iterator(); iterator.hasNext();) 
        	{
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
    
    	/*******************************************
         *******GOOD2GO***
         *******************************************/
        getActionBarHelper().setRefreshActionItemState(true);
        

        findViewById(R.id.toggle_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	

                if (mAlternateTitle) {
                    setTitle(R.string.app_name);
                } else {
                    setTitle(R.string.alternate_title);
                }
                mAlternateTitle = !mAlternateTitle;
            }
        });
        
        
       
        //NEARBY
        findViewById(R.id.nearbybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            Intent newIntent = new Intent(view.getContext(), ListTab.class);
	            startActivity(newIntent);
            }
        });
        
        //SEARCH
        findViewById(R.id.searchbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            Intent newIntent = new Intent(view.getContext(), FilterTab.class);
	            startActivity(newIntent);
            }
        });
        
       //ME 
        findViewById(R.id.mebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            Intent newIntent = new Intent(view.getContext(), MeTab.class);
	            startActivity(newIntent);
            }
        });
        
        //ABOUT
        findViewById(R.id.aboutbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	setTitle("About");
            	/**TODO call About Activity */
            	Toast.makeText(view.getContext(), "Tapped About", Toast.LENGTH_SHORT).show();
            }
        });
    
    }
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Yes. You're awesome.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() 
                        {
                            @Override
                            public void run() 
                            {
                                getActionBarHelper().setRefreshActionItemState(false);
                            }
                         },1000);
                break;

            case R.id.menu_search:
                Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
               
                /**TESTTESTSTES*/
         
	            Intent newIntent = new Intent(this, ListTab.class);
	            startActivity(newIntent);
	            
                /**TESTTESTSTES*/
                break;

            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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