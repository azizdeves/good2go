package com.gdma.good2go;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.R.drawable;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.actionbarcompat.ActionBarActivity;
import com.example.android.actionbarcompat.ActionBarListActivity;
import com.example.android.actionbarcompat.MainActivity;
import com.example.android.actionbarcompat.R;
import com.gdma.good2go.communication.RestClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;

public class MeTab extends ActionBarListActivity {
    
    drawable myPic;
    long points;
    String badge;
    String userId;
    String userName="596351";
    String userNiceName="";
    String userFirstName="Mor";
    String userLastName="Cohen";
    List<Event> usersEvents;
    RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    private UsersHistoryDbAdapter mDbHelper;
	Cursor mEventsCursor;
	private String[] mColumns;
	PopupWindow pw;
	 
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.me);
        final Button buttonGetFutureEvents = (Button) findViewById(R.id.FutureEventsMeViewButton);
        
//("MR_NICE_GUY","ANGEL","MOTHER_TERESA","BUDDHIST_MONK","DALAI_LAMA","GOD");
        points=70;
       // badge="BUDDHIST_MONK";
        points=remote_getUsersKarma(userName);
 		badge=Karma.Badge.getMyBadge(points).getName();
        User u = remote_getUsersDetails(userName);
        if (u!=null){
        	userFirstName=u.getFirstName();
        	userLastName=u.getLastName();
        }
        userNiceName=userFirstName+" "+ userLastName;
        TextView tvName = (TextView) findViewById(R.id.userNameMeView);
        TextView tvPoints = (TextView) findViewById(R.id.pointSeekValMeView);
        SeekBar pointsProg = (SeekBar)findViewById(R.id.pointSeekMeView);
        setBadgesPictures(badge);
        
        tvName.setText(userNiceName);
        tvPoints.setText(Integer.toString((int)points));
        pointsProg.setProgress((int)points);
        pointsProg.setEnabled(false);        
 
      int status = remote_getUsersHistory(userId);
      if (status==-1){} //TODO ADD HANDLER
        mDbHelper = new UsersHistoryDbAdapter(this);
        mDbHelper.open();
		mEventsCursor = mDbHelper.fetchAllUsersHistory();
		startManagingCursor(mEventsCursor);
		showHistoryInList();

        
/**********************************/
/***********DEBUG AREA*************/
/**********************************/
        
      mDbHelper = new UsersHistoryDbAdapter(this);
      mDbHelper.open();
      mDbHelper.createUsersHistory("mor1", "Feed the hungry in Even Gvirol", "12/12/12", "100", "2h");
      mDbHelper.createUsersHistory("mor2", "Clean the beach", "13/13/13", "30", "2h") ; 
      mEventsCursor=mDbHelper.fetchAllUsersHistory();
      startManagingCursor(mEventsCursor);
      showHistoryInList();
      
/**********************************/
/*******END OF DEBUG AREA**********/
/**********************************/      
      
    }
    
    private long remote_getUsersKarma(String username){
		client.AddParam("action", "getKarma");
		client.AddParam("userName", username);

		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersKarma- failed", Toast.LENGTH_LONG);
			debugging.show();
			return -1;
		}
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		long p=0;
		if(JSONResponse!=null){
			try{
				p = Long.parseLong(JSONResponse);
			}
			catch(NumberFormatException nfe){
				p=0;
			}
		}
		return p;
	}

    private User remote_getUsersDetails(String username){
    	client.AddParam("action", "geUserDetails");
		client.AddParam("userName", username);
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server - remote_getUsersDetails - failed", Toast.LENGTH_LONG);
			debugging.show();
			return null;
		}
				
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		User u=null;
		try{
			 u= new JSONDeserializer<User>(). use(Date.class, new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS")).deserialize(JSONResponse);
		}
		catch(ClassCastException e){
			u=null;
		}
		return u;
    	
    }
   private int remote_getUsersHistory(String username){
		Date myDate = new Date();
		String dateToSend = Long.toString(myDate.getTime());
		client.AddParam("action", "getUserHistory");
		client.AddParam("userName", username);
		client.AddParam("userDate", dateToSend);
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersHistory- failed", Toast.LENGTH_LONG);
			debugging.show();
			return -1;
		}
		
		
		mDbHelper = new UsersHistoryDbAdapter(this);
	    mDbHelper.open();
	    mDbHelper.createUsersHistory("mor1", "Feed the hungry in Even Gvirol", "12/12/12", "100", "2h");
	      
	      
		List<Event> history = new ArrayList<Event>();
		String json = client.getResponse();
		json = json.replaceAll("good2goserver", "good2go");
		
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
		
		return 1;
	

		
	}

    private List<Event> remote_getUsersFutureEvents(String username){
		client.AddParam("action", "getRegisteredFutureEvents");
		client.AddParam("userName", username);
		client.AddParam("userDate", (new Date()).toString());
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersFutureEvents- failed", Toast.LENGTH_LONG);
			debugging.show();
			return null;
		}

		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server

		List<Event> eventList = new JSONDeserializer<List<Event>>().use(Date.class, new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS")).deserialize(JSONResponse);
		return eventList;
	}
    
    private void showHistoryInList(){
	
        mColumns = new String[] {UsersHistoryDbAdapter.KEY_EVENTDATE, UsersHistoryDbAdapter.KEY_EVENTNAME,UsersHistoryDbAdapter.KEY_EVENT_DURATION, UsersHistoryDbAdapter.KEY_EVENPOINTS};
        int[] to = new int[] {R.id.eventDate_entry, R.id.eventInfo_entry, R.id.eventDuration_entry, R.id.eventPoints_entry};
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,R.layout.me_history_list_item,mEventsCursor, mColumns, to);
        setListAdapter(mAdapter);
        
    }
    
//	public void getUsersFutureEvents(View view){
//    	Intent myIntent = new Intent(view.getContext(),FutureEventsTab.class);
//    	startActivityForResult(myIntent, GET_FILTERED_EVENTS);		
//	}
	public void getUsersFutureEvents(View view){
		LinearLayout layout = new LinearLayout(this);
		LayoutInflater inflater = (LayoutInflater)
		this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pw = new PopupWindow(
				 inflater.inflate(R.layout.me_future_list, null, false), 
			       100, 
			       100, 
			       true);
			    // The code below assumes that the root container has an id called 'main'
			    pw.showAtLocation(layout, Gravity.CENTER,10,10); 

	}
	
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
		im1.setImageResource(R.drawable.ic_launcher); //PUT THE RIGHT PICTURE HERE
	}
	private void setPicture2(){
		ImageView im2=(ImageView)findViewById(R.id.angle_PicMeView);
		im2.setImageResource(R.drawable.ic_launcher); //PUT THE RIGHT PICTURE HERE
	}
	private void setPicture3(){
		ImageView im3=(ImageView)findViewById(R.id.motherTeresa_PicMeView);
		im3.setImageResource(R.drawable.ic_launcher); //PUT THE RIGHT PICTURE HERE
	}
	private void setPicture4(){
		ImageView im4=(ImageView)findViewById(R.id.buddhistMonk_PicMeView);
		im4.setImageResource(R.drawable.ic_launcher); //PUT THE RIGHT PICTURE HERE
	}
	private void setPicture5(){
		ImageView im5=(ImageView)findViewById(R.id.dalaiLama_PicMeView);
		im5.setImageResource(R.drawable.ic_launcher); //PUT THE RIGHT PICTURE HERE
	}
	private void setPicture6(){
		ImageView im6=(ImageView)findViewById(R.id.god_PicMeView);
		im6.setImageResource(R.drawable.ic_launcher); //PUT THE RIGHT PICTURE HERE
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
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
            }
        
        return super.onOptionsItemSelected(item);
    }
}