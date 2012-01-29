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

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.gdma.good2go.Event;
import com.gdma.good2go.Event.SuitableFor;
import com.gdma.good2go.Event.VolunteeringWith;
import com.gdma.good2go.Event.WorkType;
import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.DateParser;
import com.gdma.good2go.communication.RestClient;
import com.gdma.good2go.utils.AppPreferencesEventsRetrievalDate;
import com.gdma.good2go.utils.AppPreferencesFilterDetails;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.google.android.maps.GeoPoint;

import flexjson.JSONDeserializer;

public class MainActivity extends ActionBarActivity {
	private static final String TAG = "Main";
	
	private static final int LOGIN_REQUEST = 7;
	
	private EventsDbAdapter mDbHelper;
	private GeoPoint mMyGeoPoint;
	private String mSender;
	private String mLocalUsername;
	private boolean serverOKNoEvents=false;
	private boolean isServerComm = true;
	
    private AppPreferencesEventsRetrievalDate mEventsRetrievalDate;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.AppTheme);    	
        super.onCreate(savedInstanceState); 
               
        
        Bundle extras = getIntent().getExtras();
	    if(extras!=null){
	    	mSender = extras.getString("sender");
		}
    	/********************************************Remove this to work with Android Accounts**********************/
    	//saveLocalUsername("Bypass Account");
        /***********************************************************************************************************/             
               
        /*check accounts*/
        mLocalUsername = getLocalUsername();
        if (mLocalUsername == null){
        	
        	Account[] accounts = getAccounts(this);
        	if (accounts.length == 0){
        		
        		Intent newIntent = new Intent(this, LoginNoAccounts.class);
        		startActivityForResult(newIntent, LOGIN_REQUEST );
        		
        	}
        	else{
        		Intent newIntent = new Intent(this, Login.class);
        		startActivityForResult(newIntent,LOGIN_REQUEST );
            }
        	
        }
        else{
        	continueActivityStart();
        }
    }

    
    @Override
    protected void onResume() {
        super.onResume();
        
//    	if (areEventsFromToday()==false)
//    	{
//            new GetEventsFromDatastoreTask().execute(
//            		mMyGeoPoint.getLatitudeE6(), 
//            		mMyGeoPoint.getLongitudeE6()); 
//    	}
    }
    
    
	private String getLocalUsername(){
		AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
		return prefs.getUserName();
	}

	
    private void continueActivityStart() {   	
    	if (mSender==null || mSender.compareTo("Me")==0 || mSender.compareTo("About")==0)
		{
    		if (areEventsFromLastHalfHour()==false)
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
    		AppPreferencesFilterDetails fd = new AppPreferencesFilterDetails(this);
    		fd.setIsUserFiltersExist(false);
//        	givePoints();
		}
    	else
    	{
    		setDashboardView();
    	}
	}
	
	
//	private void givePoints() {
//		RemoteFunctions rf = RemoteFunctions.INSTANCE;
//		/**TODO put in async*/
//		int res  = rf.addUserKarma(RemoteFunctions.ADD_USER_KARMA, mLocalUsername, 
//				Karma.ActionType.OPEN_APP.name());
//		//showToast("Thanks for cheking out the app, you get " + " +10 " +"point bonus.");		
//	}

	
	private boolean areEventsFromLastHalfHour() {
		mEventsRetrievalDate = new AppPreferencesEventsRetrievalDate(getApplicationContext());
		return (mEventsRetrievalDate.isFromLast(AppPreferencesEventsRetrievalDate.HALF_HOUR));
	}


	private GeoPoint getUserLocation() {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNETWORK = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        
        double lat = (locationGPS != null) ? locationGPS.getLatitude() : 
        	(locationNETWORK != null) ? locationNETWORK.getLatitude() : 32.067228;
        double lon = (locationGPS != null) ? locationGPS.getLongitude() : 
        	(locationNETWORK != null) ? locationNETWORK.getLongitude() : 34.824256;
        	
        return new GeoPoint((int)(lat * 1E6),(int)(lon * 1E6));		
	}

	
	private void setDashboardView() {
		setContentView(R.layout.main);  
		
        //NEARBY
        findViewById(R.id.nearbybtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (isServerComm==false)
            	{
            		showToast("No server communication. Please try again later.");
            	}
            	else if (serverOKNoEvents==true) 
            	{
            		showToast("It's late, no events for today. Please try again tomorrow.");
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
            	if (isServerComm==false)
            	{
            		showToast("No server communication. Please try again later.");
            	}
            	else if (serverOKNoEvents==true) 
            	{
            		showToast("It's late, no events for today. Please try again tomorrow.");
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
            	if (isServerComm==true)
            	{
		            Intent newIntent = new Intent(view.getContext(), MeTab.class);
		            startActivity(newIntent);
            	}
            	else
            	{
                	showToast("No server communication. Please try again later.");
            	}
            }
        });
        
        //ABOUT
        findViewById(R.id.aboutbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent newIntent = new Intent(view.getContext(), AboutTab.class);
	            startActivity(newIntent);
            }
        });
        

	}
	
	
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
        if (requestCode == LOGIN_REQUEST) {
        	if (resultCode==Activity.RESULT_OK)
        	{
        		mLocalUsername=getLocalUsername();
        		continueActivityStart();
        	}
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
    
    
	private List<Event> getEventsFromServer(int lat, int lon) {
		String JSONResponse = null; // this will hold the response from server
		
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getEvents");
		client.AddParam("lon", String.valueOf(lon));
		client.AddParam("lat", String.valueOf(lat));
		
		/**TODO send actual date**/
		Calendar c = Calendar.getInstance();
		//c.set(2012,Calendar.JANUARY,30,0,0,0);
		c.set(Calendar.HOUR_OF_DAY,8);
		Date myDate = new Date();
		myDate = c.getTime();
		String dateToSend = Long.toString(myDate.getTime());
		
		client.AddParam("userDate", dateToSend);
		
		try
		{
			client.Execute(1); //1 is HTTP GET
			
			JSONResponse = client.getResponse();
			if (JSONResponse=="")
			{
				/**TODO handle alerting the user that there are no events for today*/
				Log.i(TAG, "No events for today response is completely empty");
				mEventsRetrievalDate.removeDate();
				return null;
			}
			if (JSONResponse!=null)
			{
				JSONResponse = JSONResponse.trim();
				JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
				
				//Parse the response from server
				List<Event> events = new JSONDeserializer<List<Event>>()
							.use(Date.class, new DateParser()).deserialize(JSONResponse);
				
 				if (events!=null && events.size()>0)
				{
					mEventsRetrievalDate.saveDate(new Date());
					return events;
					}
				else
				{
					//no events from today - json is  []
					Log.i(TAG, "No events for today, response is empty");
					isServerComm = true;
					serverOKNoEvents=true;
				}
			}
		}
		catch (ClassCastException e)
		{
			String  eMsg= e.getMessage();
			Log.e(TAG, "Couldn't get events from server, due to HTTP 505 Error: " + eMsg);
			isServerComm = false;
			//mEventsRetrievalDate.removeDate();
		}
		catch (Exception e)
		{
			String  eMsg= e.getMessage();
			Log.e(TAG, "Couldn't get events from server: " + eMsg);
			isServerComm = false;
			//mEventsRetrievalDate.removeDate();
		}

		return null;
	}
    
	
    private void writeEventsToLocalDB(List<Event> eventList) {
        if (eventList!=null)
        {
	        /**POPULATE DB**/
        	
	        mDbHelper.open();
	        mDbHelper.deleteAllEvents();
	        	        
	        for(Event event : eventList)
	        {
	        	String eventLat=Integer.toString(
	        			event.getEventAddress().getGood2GoPoint().getLat());
	        	String eventLon=Integer.toString(
	        			event.getEventAddress().getGood2GoPoint().getLon());
	        	    	        	
	        	//calculate distance
	        	String distance = (eventLat.compareTo("0")==0) || (eventLon.compareTo("0")==0)
	        					? "-"
	        					: String.format("%.1f", 
	        			(float)(event.getDistance(mMyGeoPoint.getLongitudeE6(),mMyGeoPoint.getLatitudeE6())));
	        	distance = distance + " km";

	        	//calculate duration
	        	String duration = getDuration(event.getMinDuration());
	        	        		        	
	        	//get eventTime
	        	String startTime = getTime(event.getOccurrences().get(0).getStartTime());
	        	String endTime = getTime(event.getOccurrences().get(0).getEndTime());
	        	
	        	//get eventAdditionalDetails
	        	String preReq = event.getPrerequisites();
	        	String npoName = event.getNPOName();
	        	npoName = npoName.replace("(", "\'");
	        	npoName = npoName.replace(")", "\'");
	        	npoName = npoName.replace("&quot;", "\"");
	        	
	        	//get suitable for	        	
	        	Set<SuitableFor> sf = event.getSuitableFor();
	        	String groups = isSetContains(sf, SuitableFor.GROUPS);
	        	String individ = isSetContains(sf, SuitableFor.INDIVIDUALS);
	        	String kids = isSetContains(sf, SuitableFor.KIDS);
	        	
	        	String groupsHowMany = String.valueOf(event.getHowMany()); 
	        	
	        	//get work type
	        	Set<WorkType> wt = event.getWorkType();
	        	String menial = isSetContains(wt, WorkType.MENIAL);
	        	String mental = isSetContains(wt, WorkType.MENTAL);
	        		        	   	        	
	        	//assign event types
	        	Set<VolunteeringWith> vw = event.getVolunteeringWith();
	        	String animals = isSetContains(vw, VolunteeringWith.ANIMALS);
	        	String children = isSetContains(vw, VolunteeringWith.CHILDREN); 
	        	//String disabled = isSetContains(vw, VolunteeringWith.DISABLED);
	        	String elderly = isSetContains(vw, VolunteeringWith.ELDERLY);
	        	String env = isSetContains(vw, VolunteeringWith.ENVIRONMENT);
	        	String special = isSetContains(vw, VolunteeringWith.SPECIAL);
	        	String disadvant = isSetContains(vw, VolunteeringWith.DISADVANTAGED);
	        	
	        	if (animals.contains("0")
	        			 && children.contains("0")
	        			 && disadvant.contains("0")
	        			 && elderly.contains("0")
	        			 && env.contains("0") 
	        			 && special.contains("0"))
	        		special = "1";
	        	
	        	//get occurence key
	        	String occKey = event.getOccurrences().get(0).getOccurrenceKey();
	        	
	        	//assign an image to event 
	        	String eventImage = chooseImage (animals, children, disadvant, elderly, env, special);	
	        	
	        	//populate db
	        	mDbHelper.createEvent(event.getEventKey(),event.getInfo(),
	        			event.getDescription(),
	        			event.getContent(),
	        			eventLat, eventLon,distance,duration,
	        			event.getEventAddress().getCity(),
	        			event.getEventAddress().getStreet().replace("&quot;", "\""),
	        			String.valueOf(event.getEventAddress().getNumber()),
	        			animals, children,disadvant,
	        			elderly, env, special, eventImage, startTime, endTime,
	        			preReq, npoName, groups, individ, kids, 
	        			menial, mental, occKey, groupsHowMany, Integer.toString(event.getMinDuration()));
	        }
	        mDbHelper.close();
	        
	     }
        else
        	if (areEventsFromLastHalfHour() == false )
        	{
        		showToast ("Couldn't connect to the server or no events for today");
        		
        		//DEBUG
//        		mDbHelper.open();
//        		createManualEventForDebug();
//        		mEventsRetrievalDate.saveDate(new Date());
//        		mDbHelper.close();
        		//DEBUG
        	}
        }
     
    
    private  <T> String isSetContains(Set<T> set, T member) {
		return  set.contains(member) ? "1" : "0";
	}


	private void createManualEventForDebug() {
    	String eventTest="eventTest";
    	String lat="32067228";
    	String lon="32067228";
    	String type="1";
    	int image = R.drawable.event_children;
    	String image2 = Integer.toString(image);
    	
	    	mDbHelper.createEvent(eventTest,eventTest,
	    			eventTest,
	    			eventTest,
	    			lat, lon,"1 km","20 min",
	    			eventTest,
	    			eventTest,
	    			type,
	    			type, type,type,
	    			type,type,type,
	    			image2,
	    			"08:00", "12:00", eventTest, eventTest, type, type, type, 
	    			type, type, eventTest, type, "240");
	    	
	}


	private String getDuration(int totalDurationInMins) 
    {
    	int actualDurationHours = totalDurationInMins/60;
    	int actualDurationMins = totalDurationInMins%60;
    	if (actualDurationHours > 2)
    		actualDurationHours = 2;
    	String duration = actualDurationMins!=0 ? actualDurationMins + "min" : "";
    	if (actualDurationHours!=0)
    		duration = actualDurationHours + "h " + duration;
    	if (duration.length()==0)
    		duration = "2h";
    	
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


	private String chooseImage(String animals, String children, String disadvant, 
    		String elderly, String environment, String special) 
    {
    	  int imageId=R.drawable.event_paint;
    	  
    	  if (children=="1")
    		  imageId=R.drawable.event_children;
    	  
    	  if (disadvant=="1")
    		  imageId=randomizeInt(new int[] {R.drawable.event_disadvantaged1,
    				  R.drawable.event_disadvantaged2,
    				  R.drawable.event_disadvantaged3,
    				  R.drawable.event_disadvantaged4,
    				  R.drawable.event_disadvantaged5,
    				  R.drawable.event_disadvantaged1});
    	  
    	  if (elderly=="1")
    		  imageId=R.drawable.event_elderly;
    	  
    	  if (environment=="1")
    		  imageId=randomizeInt(new int[] {R.drawable.event_env,
    				  R.drawable.event_env1,
    				  R.drawable.event_env2,
    				  R.drawable.event_env3,
    				  R.drawable.event_env4,
    				  R.drawable.event_env5});
    	  
    	  if (special=="1")
    		  imageId=randomizeInt(new int[] {R.drawable.event_default1,
    				  R.drawable.event_default1,
    				  R.drawable.event_default2,
    				  R.drawable.event_default3,
    				  R.drawable.event_default4,
    				  R.drawable.event_default5});
    	  
    	  if (animals=="1")
    		  imageId=randomizeInt(new int[] {R.drawable.event_animals,
    				  R.drawable.event_animals1,
    				  R.drawable.event_animals2,
    				  R.drawable.event_animals3,
    				  R.drawable.event_animals4,
    				  R.drawable.event_animals5});
    	      	  
		return Integer.toString(imageId);
	}
	
	
	private int randomizeInt(int [] eventImages) {
		 Random randomGenerator = new Random();
		 return eventImages[randomGenerator.nextInt(eventImages.length)];
	}


	/** FOR ACTION BAR MENUS **/
	//TODO create settings menu for the main screen

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
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
    
    private Account[] getAccounts(Context cntxt){

		AccountManager am = AccountManager.get(cntxt);
   		Account[] accounts;
		accounts = am.getAccounts();
		Account[] tempAccs, tempAccs2;
		tempAccs = new Account[accounts.length];
		int j = 0;
		
		for (int i = 0; i<accounts.length; i++){
			if (accounts[i].name.contains("@")){
				tempAccs[j++] = accounts[i];
			}
		}
		
		
		boolean tst;
		int finCount=0;
		
		for (int i = 0; i<j; i++){
			tst = true;
			for (int k = 0; k<i; k++){
				if (tempAccs[i].name.equals(tempAccs[k].name)){
					tst = false;
					tempAccs[i] = null;
				}
			}
			if (tst == true){
				finCount++;
			}
		}
		
		tempAccs2 = new Account[finCount];
		j = 0;
		for (int i = 0; i<tempAccs.length; i++){
			if (tempAccs[i] != null){
				tempAccs2[j++] = tempAccs[i];
			}
		}
		
		return tempAccs2;
		
	}

}