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

package com.gdma.good2go.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.gdma.good2go.Event;
import com.gdma.good2go.Event.VolunteeringWith;
import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.DateParser;
import com.gdma.good2go.communication.RestClient;
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.google.android.maps.GeoPoint;

import flexjson.JSONDeserializer;

public class MainActivity extends ActionBarActivity {
	
	private EventsDbAdapter mDbHelper;
	private GeoPoint mMyGeoPoint;
	private Calendar mLastServerUpdate;
	private boolean mNoEventsFromToday = false;
	private String mSender;
	private String mLocalUsername;
	private RestClient mClient;
    private String mEventName;
    private String mEventDesc;
    private String mEventKey;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.AppTheme);    	
        super.onCreate(savedInstanceState); 
                              
    	/********************************************Remove this to work with Android Accounts**********************/
    	//saveLocalUsername("Bypass Account");
        /***********************************************************************************************************/             
               
        /*check accounts*/
        mLocalUsername = getLocalUsername();
        if (mLocalUsername == null){
        	showToast("no local username");
        	Intent newIntent = new Intent(this, Login.class);
        	startActivityForResult(newIntent,7);            	
        }
        else{
    	    /**GET EVENT ID PASSED FROM CALLING ACTIVITY*/
    		Bundle extras = getIntent().getExtras();
    		mSender= extras!= null?
    				mSender = extras.getString("sender"):null;
    				
        	continueActivityStart();
        }
    }

    
    @Override
    protected void onResume() {
        super.onResume();
        
    	if (mNoEventsFromToday==true)
    	{
            /*new GetEventsFromDatastoreTask().execute(
            		mMyGeoPoint.getLatitudeE6(), 
            		mMyGeoPoint.getLongitudeE6());*/  
    	}
    }
    
    
	private String getLocalUsername(){
		SharedPreferences settings = getSharedPreferences("savedUsername", MODE_PRIVATE);
		return settings.getString("userNameVal", null);
	}

	
    private void continueActivityStart() {   	
    	if (mSender==null)
		{
	    	/**GET MY LOCATION**/       
	        mMyGeoPoint = getUserLocation();
	     	        
	        mDbHelper = new EventsDbAdapter(this);
	        
	        /**GET EVENTS FROM SERVER ASYNC**/	               
	        new GetEventsFromDatastoreTask().execute(
	        		mMyGeoPoint.getLatitudeE6(), 
	        		mMyGeoPoint.getLongitudeE6());
		}
		else
		{
			setDashboardView();
		}
	}
	
	
	private GeoPoint getUserLocation() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNETWORK = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        
        double lat = (locationGPS != null) ? locationGPS.getLatitude() : 
        	(locationNETWORK != null) ? locationNETWORK.getLatitude() : 32.067228;
        double lon = (locationGPS != null) ? locationGPS.getLongitude() : 
        	(locationNETWORK != null) ? locationNETWORK.getLongitude() : 32.067228;
        	
        return new GeoPoint((int)(lat * 1E6),(int)(lon * 1E6));		
	}

	
	private void setDashboardView() {
		
        //setTheme(R.style.AppTheme);
		setContentView(R.layout.main);  
		
        //NEARBY
        findViewById(R.id.nearbybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (false && mNoEventsFromToday==true)
            	{
            		showToast("No server communication. Please try again later.");
            	}
            	else
            	{
		            Intent newIntent = new Intent(view.getContext(), MapTab.class);
		            startActivity(newIntent);
            	}
            }
        });
                
        //SEARCH
        findViewById(R.id.searchbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (false && mNoEventsFromToday==true)
            	{
            		showToast("No server communication. Please try again later.");
            	}
            	else
            	{
		            Intent newIntent = new Intent(view.getContext(), FilterTab.class);
		            Bundle caller = new Bundle();
		            caller.putString("caller", "MainTab");
		            newIntent.putExtras(caller);
		            startActivity(newIntent);
            	}
            }
        });
               
        //ME 
        findViewById(R.id.mebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (false && mNoEventsFromToday==true)
            	{
            		showToast("No server communication. Please try again later.");
            	}
            	else
            	{
		            Intent newIntent = new Intent(view.getContext(), MeTab.class);
		            startActivity(newIntent);
            	}
            }
        });
        
        //ABOUT
        findViewById(R.id.aboutbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	//setTitle("About");
            	Intent newIntent = new Intent(view.getContext(), FeedbackTab.class);
	            startActivity(newIntent);
            }
        });
        
        List<Event> feedbackList = remote_getEventsForFeedback();  
        if (feedbackList!=null){
        	for (Event event : feedbackList) {
        		mEventDesc = event.getDescription();
        		mEventName=event.getEventName();
        		mEventKey=event.getEventKey();
        		Bundle extraInfo = new Bundle();        		
                extraInfo.putString("mEventDesc", mEventDesc);
                extraInfo.putString("mEventName", mEventName);
                extraInfo.putString("mEventKey", mEventKey);
                Intent newIntent = new Intent(this, FeedbackTab.class);
                newIntent.putExtras(extraInfo);
                startActivity(newIntent);
			}
        }
	}
	
	
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    
    
	private void saveLocalUsername(String userName){
		SharedPreferences settings = getSharedPreferences("savedUsername", MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userNameVal", userName);
		editor.commit();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode==Activity.RESULT_OK)
		{
			continueActivityStart();
		}
	}	
	
	
	/**THREADS*/		
    private class GetEventsFromDatastoreTask extends AsyncTask<Integer, Void, List<Event>> {
    	Dialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new Dialog(MainActivity.this, R.style.SplashScreen);   		
    		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);    		
    		dialog.setContentView(R.layout.splash);
	    	dialog.setCancelable(false);
	    	dialog.show();
	    }

    	protected void onPostExecute(List<Event> eventList) {
    		dialog.dismiss();
    		writeEventsToLocalDB(eventList);
    		setDashboardView();
    	}
			
		@Override
		protected List<Event> doInBackground(Integer... coordinates) {
			return getEventsFromServer(coordinates[0],coordinates[1]);
		}
	}
	
    
    
    public List<Event> remote_getEventsForFeedback(){
    	String JSONResponse = null;
		mClient = new RestClient("http://good-2-go.appspot.com/good2goserver");
		mClient.AddParam("action", "addUser");
		mClient.AddParam("userName", mLocalUsername);
		Date myDate = new Date();
		String dateToSend = Long.toString(myDate.getTime());
		mClient.AddParam("userDate", dateToSend);
		try{
			mClient.Execute(1); //1 is HTTP GET
			
			JSONResponse = mClient.getResponse();
			if (JSONResponse!=null)
			{
				JSONResponse = JSONResponse.trim();
				JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
				
				//Parse the response from server
				//return new JSONDeserializer<List<Event>>().deserialize(JSONResponse);
				List<Event> events = new JSONDeserializer<List<Event>>().use(Date.class, new DateParser()).deserialize(JSONResponse);
				return events;
			}
		}
		catch (Exception e){
			//Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersHistory- failed", Toast.LENGTH_LONG);
			//debugging.show();
			return null;
		}
		return null;
    }
    
	private List<Event> getEventsFromServer(int lat, int lon) {
		String JSONResponse = null; // this will hold the response from server
		
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getEvents");
		client.AddParam("lon", String.valueOf(lon));
		client.AddParam("lat", String.valueOf(lat));
		
		/**TODO send actual date**/
		Calendar c = Calendar.getInstance();
		c.set(2011,Calendar.DECEMBER,31,0,0,0);
		c.set(Calendar.HOUR_OF_DAY,8);
		Date myDate = new Date();
		myDate = c.getTime();
		String dateToSend = Long.toString(myDate.getTime());
		
		client.AddParam("userDate", dateToSend);
		
		try
		{
			client.Execute(1); //1 is HTTP GET
			
			JSONResponse = client.getResponse();
			if (JSONResponse!=null)
			{
				JSONResponse = JSONResponse.trim();
				JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
				
				//Parse the response from server
				//return new JSONDeserializer<List<Event>>().deserialize(JSONResponse);
				List<Event> events = new JSONDeserializer<List<Event>>()
							.use(Date.class, new DateParser()).deserialize(JSONResponse);
				
				mLastServerUpdate = Calendar.getInstance();
				mNoEventsFromToday = false;
				return events;
			}
		}
		catch (Exception e)
		{
		}
		return null;
	}
    
	
    private void writeEventsToLocalDB(List<Event> eventList) {
        if (eventList!=null)
        {
	        /**POPULATE DB**/
	        
	        /**TODO: drop what we have if it's a new day
	         * or if there's been a change since last read
	         */
	        mDbHelper.open();
	        for(Event event : eventList)
	        {
	        	String eventLat=Integer.toString(
	        			event.getEventAddress().getGood2GoPoint().getLat());
	        	String eventLon=Integer.toString(
	        			event.getEventAddress().getGood2GoPoint().getLon());
	        	    	        	
	        	//calculate distance	
	        	String distance=String.format("%.1f", 
	        			(float)(event.getDistance(mMyGeoPoint.getLongitudeE6(),mMyGeoPoint.getLatitudeE6())))
	        			+" km";
	        	
	        	//calculate duration
	        	String duration = getDuration(event.getMinDuration());
	        	        		        	
	        	//get eventTime
	        	String startTime = getTime(event.getOccurrences().get(0).getStartTime());
	        	String endTime = getTime(event.getOccurrences().get(0).getEndTime());
	        	
	        	//get eventAdditionalDetails
	        	event.getNPOName();
	        	event.getSuitableFor();
	        	event.getWorkType();
	        	   	        	
	        	//assign event types
	        	Set<VolunteeringWith> listType = event.getVolunteeringWith();
	        	String animals="0", children="0", disabled="0", elderly="0", environment="0", special="0" ;
	        	
	        	for (Iterator<VolunteeringWith> iterator = listType.iterator(); iterator.hasNext();) 
	        	{
					VolunteeringWith volunteeringWith = (VolunteeringWith) iterator.next();
					if(volunteeringWith==VolunteeringWith.ANIMALS)
						animals="1";
					else if(volunteeringWith==VolunteeringWith.CHILDREN)
						children="1";
					else if(volunteeringWith==VolunteeringWith.DISABLED)
						disabled="1";
					else if(volunteeringWith==VolunteeringWith.ELDERLY)
						elderly="1";
					else if(volunteeringWith==VolunteeringWith.ENVIRONMENT)
						environment="1";
					else if(volunteeringWith==VolunteeringWith.SPECIAL)
						special="1";
				}
	        	
	        	//assign an image to event 
	        	String eventImage = chooseImage (animals, children, disabled, elderly, environment, special);	
	        	
	        	//populate db
	        	mDbHelper.createEvent(event.getEventKey(),event.getEventName(),
	        			event.getDescription(),
	        			event.getPrerequisites(),
	        			eventLat, eventLon,distance,duration,
	        			event.getEventAddress().getCity(),
	        			event.getEventAddress().getStreet(),
	        			String.valueOf(event.getEventAddress().getNumber()),
	        			animals, children,disabled,
	        			elderly,environment,special,eventImage, startTime, endTime);
	        }
	     }
        else 
        {
        	if (mLastServerUpdate == null ||
        			daysBetween(mLastServerUpdate, Calendar.getInstance()) > 0)
        	{
        		mNoEventsFromToday = true;
        		showToast ("Couldn't connect to the server");
        	}
        }
     }

    private static long daysBetween(Calendar startDate, Calendar endDate) {
    	  Calendar date = (Calendar) startDate.clone();
    	  long daysBetween = 0;
    	  while (date.before(endDate)) {
    	      date.add(Calendar.DAY_OF_MONTH, 1);
    	      daysBetween++;
    	  }
    	  return daysBetween;
    	}
    
    private String getDuration(int totalDurationInMins) 
    {
    	int actualDurationHours = totalDurationInMins/60;
    	int actualDurationMins = totalDurationInMins%60;
    	String duration = actualDurationMins!=0 ? actualDurationMins + "min" : "";
    	if (actualDurationHours!=0)
    		duration = actualDurationHours + "h " + duration;
    	
    	return duration;
	}


	private String getTime(Date dateFromEvent) {
 		Calendar c = Calendar.getInstance();
 		
		c.setTime(dateFromEvent);
		int eventHour = c.get(Calendar.HOUR_OF_DAY);
		int eventMin = c.get(Calendar.MINUTE);
		String eventMinStr = (eventMin==0) ? "00" :  Integer.toString(eventMin);
		return eventHour + ":" + eventMinStr;
	}


	private String chooseImage(String animals, String children, String disabled, 
    		String elderly, String environment, String special) 
    {
    	  int imageId=R.drawable.event_paint;;
    	  
    	  if (animals=="1")
    		  imageId=R.drawable.event_animals;
    	  
    	  if (children=="1")
    		  imageId=R.drawable.event_children;
    	  
    	  if (disabled=="1")
    		  imageId=R.drawable.event_handicapped;
    	  
    	  if (elderly=="1")
    		  imageId=R.drawable.event_elderly;
    	  
    	  if (environment=="1")
    		  imageId=R.drawable.event_env;
    	  
    	  if (special=="1")
    		  imageId=R.drawable.event_special;
    	      	  
		return Integer.toString(imageId);
	}
	
	
	/** FOR ACTION BAR MENUS **/
	//TODO create settings menu for the main screen

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
        }
        return super.onOptionsItemSelected(item);
    }

}