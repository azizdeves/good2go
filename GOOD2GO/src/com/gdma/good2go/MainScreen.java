package com.gdma.good2go;

import java.util.List;

import com.gdma.good2go.R;
import com.gdma.good2go.communication.RestClient;

import flexjson.JSONDeserializer;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainScreen extends TabActivity {
	
//	Facebook facebook = new Facebook("327638170583802"); //new facebook app instance;
	
	private EventsDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /**GET EVENTS FROM SERVER**/
        // TODO on resume check if we have the events if not - repopulate
        List<Event> eventList=getEventsFromServer();
        
	
        /**POPULATE DB**/
        mDbHelper = new EventsDbAdapter(this);
        mDbHelper.open();
        for(Event event : eventList)
        {
        	String lat=Integer.toString(
        			event.getEventAddress().getGood2GoPoint().getLat());
        	String lon=Integer.toString(
        			event.getEventAddress().getGood2GoPoint().getLon()); 	
     	
        			mDbHelper.createEvent(event.getEventKey(),event.getEventName(),
        			event.getDescription(),
        			event.getPrerequisites(),
        			lat, lon);
        }

        
        /*
        facebook.authorize(this, new DialogListener() {
            //@Override
            public void onComplete(Bundle values) {}

            //@Override
            public void onFacebookError(FacebookError error) {}

            //@Override
            public void onError(DialogError e) {}

            //@Override
            public void onCancel() {}
            
    
        });
*/
//        
//        facebook.authorize(this, new String[] { "email", "offline_access", "publish_checkins", "publish_stream" },
//
//        	      new DialogListener() {
//  //      	           @Override
//        	           public void onComplete(Bundle values) {
//        	        	   updateStatus(values.getString(facebook.getAccessToken()));
//        	           }
//
//        	           private void updateStatus(String accessToken) {
//        	        	   // TODO Auto-generated method stub
//        	        	   try{
//        	        		   Bundle bundle = new Bundle();
//        	        		   bundle.putString("message", "GOOD2GO Hello World!!");
//            	        	   bundle.putString(Facebook.TOKEN, accessToken);
//            	        	   String response = facebook.request("me/feed",bundle,"POST");
//            	        	   Log.d("UPDATE RESPONSE", ""+response);
//        	        	   }
//        	        	   catch(MalformedURLException e){
//        	        		   Log.e("MALFORMED URL",""+e.getMessage());
//        	        	   }
//        	        	   catch (IOException e){
//        	        		   Log.e("IOEX",""+e.getMessage());
//        	        	   }
//        	        	   
//        	        	   
//        	           }
//
////      	           @Override
//        	           public void onFacebookError(FacebookError error) {}
//
//  //      	           @Override
//        	           public void onError(DialogError e) {}
//
//  //      	           @Override
//        	           public void onCancel() {}
//        	      }
//        	);

        
        
        

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

        //Set the map tab to be first 
        tabHost.setCurrentTab(0);
    }

	private List<Event> getEventsFromServer() {
		//TODO is this supposed to be async task?

		String JSONResponse = null; // this will hold the response from server
		
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getEvents");
		client.AddParam("lon", "3124.872");
		client.AddParam("lat", "3346.115");
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			return null;
		}
		
		JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		return new JSONDeserializer<List<Event>>().deserialize(JSONResponse);
	}
    

    
//    //added - FB
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	super.onActivityResult(requestCode, resultCode, data);	
//
//    	facebook.authorizeCallback(requestCode, resultCode, data);
//    }

}