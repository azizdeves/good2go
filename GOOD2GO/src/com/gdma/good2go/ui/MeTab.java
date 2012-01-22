package com.gdma.good2go.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.drawable;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gdma.good2go.Event;
import com.gdma.good2go.Karma;
import com.gdma.good2go.R;
import com.gdma.good2go.User;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.actionbarcompat.ActionBarListActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.communication.RestClient;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.KeyManager;
import com.gdma.good2go.utils.UsersHistoryDbAdapter;
import com.gdma.good2go.utils.UsersUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;

public class MeTab extends ActionBarActivity {
	private static final String TAG = "Me";
	private drawable myPic;
	private long mPoints;
	private String mBadge;
	private String mUserId;
	private String mUserName="";
	private String mUserNiceName="";
	private String mUserFirstName="";
	private String mUserLastName="";
	private List<Event> usersEvents;
	private RestClient mClient = null;
    private UsersHistoryDbAdapter mDbHelper;
    private Cursor mEventsCursor;
	private String[] mColumns;
	private PopupWindow pw;
	private AppPreferencesPrivateDetails mUsersPref; 
    private String mEventName;
    private String mEventDesc;
    private String mEventKey;
    private String mEventDate;
    private String mOccurenceKey;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.me);
        //check for unrated events async              
        new checkForUsersUnRatedEventsTask().execute(	);
        
        final Button buttonGetFutureEvents = (Button) findViewById(R.id.FutureEventsMeViewButton);
        ListView l = (ListView)findViewById(R.id.historyListMeView);
        mUsersPref = new AppPreferencesPrivateDetails(this) ;
        mUserName = mUsersPref.getUserName();
        mPoints=UsersUtil.remote_getUsersKarma(mUserName);
 		mBadge=Karma.Badge.getMyBadge(mPoints).getName();

        
//       mUserFirstName = mUsersPref.doPrivateDetailsExist() ? mUsersPref.getUserFirstName() : mUsersPref.substring(0,mUsersPref.indexOf('@'));				
 		if(mUsersPref.doPrivateDetailsExist()){
 			mUserFirstName = mUsersPref.getUserFirstName();
 			mUserLastName = mUsersPref.getUserLastName();
 		}
 		else{
 	        User u = UsersUtil.remote_getUsersDetails(mUserName);
 	        if (u!=null){
 	        	mUserFirstName=u.getFirstName();
 	        	mUserLastName=u.getLastName();
 	        }
 	        else{
 	        	mUserFirstName=mUserName.substring(0, mUserName.indexOf("@"));
 	        }
 	       
 		}
 		
        mUserNiceName=mUserFirstName+" "+ mUserLastName;
        TextView tvName = (TextView) findViewById(R.id.userNameMeView);
        TextView tvPoints = (TextView) findViewById(R.id.pointSeekValMeView);
        SeekBar pointsProg = (SeekBar)findViewById(R.id.pointSeekMeView);
        setBadgesPictures(mBadge);
        
        
        tvName.setText(mUserNiceName);
        tvPoints.setText(Integer.toString((int)mPoints));
        pointsProg.setProgress((int)mPoints);
        pointsProg.setEnabled(false);        
 
        int status = remote_getUsersHistory(mUserName);
        if (status==-1){
        	//TODO write to log(?)
        }
        
        mDbHelper = new UsersHistoryDbAdapter(this);
        mDbHelper.open();
/**********************************/
/***********DEBUG AREA*************/
/**********************************/

        mDbHelper.createUsersHistory("mor1", "1 - Feed the hungry in Even Gvirol", "01/02/12", "40", "2h");
        mDbHelper.createUsersHistory("mor2", "Clean the beach", "12/02/12", "200", "2h") ; 
      
      
      
/**********************************/
/*******END OF DEBUG AREA**********/
/**********************************/   
    

      mEventsCursor=mDbHelper.fetchAllUsersHistory();
      mDbHelper.close();
      startManagingCursor(mEventsCursor);
      showHistoryInList();
      showFutureInList();
    }

   private int remote_getUsersHistory(String username){
		Date myDate = new Date();
		String dateToSend = Long.toString(myDate.getTime());
		mClient = new RestClient("http://good-2-go.appspot.com/good2goserver");
		mClient.AddParam("action", "getUserHistory");
		mClient.AddParam("userName", username);
		mClient.AddParam("userDate", dateToSend);
		
		/* - Gil - added test print to find out the correct time format sent to the server 
		Toast test=Toast.makeText(this,dateToSend, Toast.LENGTH_LONG);
		test.show();
		// this is the actual get request sent:  http://good-2-go.appspot.com/good2goserver?action=getUserHistory&userName=596351&userDate=1325455552537
		*/
		
		try{
			mClient.Execute(1); //1 is HTTP GET
			/* - Gil - added test print to make sure we entered this part 
			Toast test=Toast.makeText(this,"Connection to server -remote_getUsersHistory- succeded", Toast.LENGTH_LONG);
			test.show();
			*/
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersHistory- failed", Toast.LENGTH_LONG);
			debugging.show();
			return -1;
		}
		
		
		mDbHelper = new UsersHistoryDbAdapter(this);
	    mDbHelper.open();
	    mDbHelper.createUsersHistory("mor1", "Feed the hungry in Even Gvirol", "01/01/12", "200", "2h");
	      
	      
		List<Event> history = new ArrayList<Event>();
		String json = mClient.getResponse();
		json = json.trim();
		
		JsonArray jsonArray = new JsonArray();
		JsonParser parser = new JsonParser();
		
		KeyManager key = KeyManager.init();
		jsonArray = parser.parse(json).getAsJsonArray();
		for (int i=0;i<jsonArray.size();i++){
			JsonObject jsonKarma = (JsonObject) jsonArray.get(i);
			String eventName = jsonKarma.getAsJsonPrimitive("Event").getAsString();
			String date = jsonKarma.getAsJsonPrimitive("Date").getAsString();
			long points = jsonKarma.getAsJsonPrimitive("Points").getAsLong();
			mDbHelper.createUsersHistory(Long.toString(key.getKey()), eventName, date, Long.toString(points), "1h");
		}
		mDbHelper.close();
		return 1;
	

		
	}

    private List<Event> remote_getUsersFutureEvents(String username){
		Date myDate = new Date();
		String dateToSend = Long.toString(myDate.getTime());
    	mClient = new RestClient("http://good-2-go.appspot.com/good2goserver");
		mClient.AddParam("action", "getRegisteredFutureEvents");
		mClient.AddParam("userName", username);
		mClient.AddParam("userDate", dateToSend);
		
		try{
			mClient.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersFutureEvents- failed", Toast.LENGTH_LONG);
			debugging.show();
			return null;
		}

		String JSONResponse = mClient.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		JSONResponse = JSONResponse.trim();
		
		//Parse the response from server

		List<Event> eventList = new JSONDeserializer<List<Event>>().use(Date.class, new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS")).deserialize(JSONResponse);
		return eventList;
	}

    
    private void showHistoryInList(){
    	ListView l1 = (ListView)findViewById(R.id.historyListMeView);
    	mColumns = new String[] {UsersHistoryDbAdapter.KEY_EVENTDATE, UsersHistoryDbAdapter.KEY_EVENTNAME,UsersHistoryDbAdapter.KEY_EVENT_DURATION, UsersHistoryDbAdapter.KEY_EVENPOINTS};
    	// lv1.setAdapter(new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, s1));
    	int[] to = new int[] {R.id.eventDate_entry, R.id.eventInfo_entry, R.id.eventDuration_entry, R.id.eventPoints_entry};
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,R.layout.me_history_list_item,mEventsCursor, mColumns, to);
        l1.setAdapter(mAdapter);
    }
  
    
    private void showFutureInList(){
//    	ListView l1 = (ListView)findViewById(R.id.futureListMeView);
//    	mColumns = new String[] {UsersHistoryDbAdapter.KEY_EVENTDATE, UsersHistoryDbAdapter.KEY_EVENTNAME, UsersHistoryDbAdapter.KEY_EVENT_DURATION, UsersHistoryDbAdapter.KEY_EVENPOINTS};
//    	// lv1.setAdapter(new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, s1));
//    	int[] to = new int[] {R.id.eventDate_entry, R.id.eventInfo_entry, R.id.eventDuration_entry, R.id.eventPoints_entry};
//        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,R.layout.me_future_list_item,mEventsCursor, mColumns, to);
//        l1.setAdapter(mAdapter);
    }
    
//	public void getUsersFutureEvents(View view){
//    	Intent myIntent = new Intent(view.getContext(),FutureEventsTab.class);
//    	startActivityForResult(myIntent, GET_FILTERED_EVENTS);		
//	}

	
	public void closeFutureEvents(View view){

		pw.dismiss();

	}
	//("MR_NICE_GUY","ANGEL","MOTHER_TERESA","BUDDHIST_MONK","DALAI_LAMA","GOD");	
	
	private void setBadgesPictures(String s){
		if(s.compareTo("MR_NICE_GUY")==0){
			setPicture1();
		}
		if(s.compareTo("ANGEL")==0){
			setPicture1();
			setPicture2();
		}
		if(s.compareTo("MOTHER_TERESA")==0){
			setPicture1();
			setPicture2();
			setPicture3();
		}
		if(s.compareTo("BUDDHIST_MONK")==0){
			setPicture1();
			setPicture2();
			setPicture3();
			setPicture4();
		}
		if(s.compareTo("DALAI_LAMA")==0){
			setPicture1();
			setPicture2();
			setPicture3();
			setPicture4();
			setPicture5();
			
		}
		if(s.compareTo("GOD")==0){
			setPicture1();
			setPicture2();
			setPicture3();
			setPicture4();
			setPicture5();
			setPicture6();
			
		}

	}
	
	private void setPicture1(){
		ImageView im1=(ImageView)findViewById(R.id.mrNiceGuy_PicMeView);
		im1.setImageResource(R.drawable.badge_mrniceguy);
	}
	private void setPicture2(){
		ImageView im2=(ImageView)findViewById(R.id.angle_PicMeView);
		im2.setImageResource(R.drawable.badge_angel); 
	}
	private void setPicture3(){
		ImageView im3=(ImageView)findViewById(R.id.motherTeresa_PicMeView);
		im3.setImageResource(R.drawable.badge_mothertheresa); 
	}
	private void setPicture4(){
		ImageView im4=(ImageView)findViewById(R.id.buddhistMonk_PicMeView);
		im4.setImageResource(R.drawable.badge_buddhistmonk); 
	}
	private void setPicture5(){
		ImageView im5=(ImageView)findViewById(R.id.dalaiLama_PicMeView);
		im5.setImageResource(R.drawable.badge_dalailama); 
	}
	private void setPicture6(){
		ImageView im6=(ImageView)findViewById(R.id.god_PicMeView);
		im6.setImageResource(R.drawable.badge_god); 
	}
    

	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.me, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Intent newIntent = null;
    	
        switch (item.getItemId()) {

        case R.id.menu_map:
        	newIntent = new Intent(this, MapTab.class);
        	startActivity(newIntent);
            break;

        case R.id.menu_list:
        	newIntent = new Intent(this, ListTab.class);
        	startActivity(newIntent);
            break;
            
        case android.R.id.home:
        	newIntent = new Intent(this, MainActivity.class);
        	newIntent.putExtra("sender", TAG);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
            }
        
        return super.onOptionsItemSelected(item);
    }
    
    
    private class checkForUsersUnRatedEventsTask extends AsyncTask<String, Void, Integer> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(MeTab.this);
    		dialog.setMessage(getString(R.string.sending_user_details));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(Integer execResult) {
    		dialog.dismiss();
    	}
			
		@Override
		protected Integer doInBackground(String... userDetails) {
			return checkFeedback();
			
		}
	
    }
    
    
    
    //asyn
	private int checkFeedback() {
        List<Event> feedbackList = UsersUtil.remote_getEventsForFeedback(mUserName);
        if (feedbackList!=null){
        	for (Event event : feedbackList) {
        		mEventName=event.getEventName();
        		mOccurenceKey=event.getOccurrences().get(0).getOccurrenceKey();
        		
        		Date occDate = event.getOccurrences().get(0).getOccurrenceDate();
        		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM");
        		mEventDate = sdf.format(occDate);
        		
        		Bundle extraInfo = new Bundle();        		
                extraInfo.putString(EventsDbAdapter.KEY_EVENTNAME, mEventName);
                extraInfo.putString(EventsDbAdapter.KEY_EVENT_OCCURENCE_KEY, mOccurenceKey);
                extraInfo.putString("VOLUNTEER_DATE", mEventDate);
                //TODO add points
                extraInfo.putString("POINTS", "3200");
                
                Intent newIntent = new Intent(this, FeedbackTab.class);
                newIntent.putExtras(extraInfo);
                startActivity(newIntent);
			}
        	return 1;
        }
        return 0;
		
	}
}