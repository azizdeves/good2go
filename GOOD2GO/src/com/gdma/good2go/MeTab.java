package com.gdma.good2go;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.R.drawable;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gdma.good2go.communication.RestClient;

import flexjson.JSONDeserializer;

public class MeTab extends ListActivity {
    
    drawable myPic;
    long points;
    String badge;
    String userId;
    String userName="";
    String userNiceName="";
    String userFirstName="Mor";
    String userLastName="Yogev";
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
        badge="BUDDHIST_MONK";
//      points=remote_getUsersKarma(userName);
// 		badge=Karma.Badge.getMyBadge(points);
//      User u = getUsersDetails(userName);
//      userFirstName=u.getFirstName();
//      userLastName=u.getLastName();
        userNiceName=userFirstName+" "+userLastName;
        TextView tvName = (TextView) findViewById(R.id.userNameMeView);
        TextView tvPoints = (TextView) findViewById(R.id.pointSeekValMeView);
        SeekBar pointsProg = (SeekBar)findViewById(R.id.pointSeekMeView);
        setBadgesPictures(badge);
        
//        for (Iterator iterator = badges.iterator(); iterator.hasNext();) {
//			String b= (String) iterator.next();
//			if(b.compareTo("MR_NICE_GUY")==0){
//				
//			}
//			
//		}

        tvName.setText(userNiceName);
        tvPoints.setText(Integer.toString((int)points));
        pointsProg.setProgress((int)points);
        pointsProg.setEnabled(false);        
 
//        List<Event> historyList=remote_getUsersHistory(userId);
//        mDbHelper = new UsersHistoryDbAdapter(this);
//        mDbHelper.open();
//        for(Event event : historyList)
//        {
//        	mDbHelper.createUsersHistory(event.getEventKey(), event.getEventName(), event.getMinDuration(), event.getEventDate, event.getEventPoints);
//        }
//      List<Event> futureList=remote_getUsersFutureEvents(userId);
//      mDbHelper = new UsersHistoryDbAdapter(this);
//      mDbHelper.open();
//      for(Event event : historyList)
//      {
//      	mDbHelper.createUsersHistory(event.getEventKey(), event.getEventName(), event.getMinDuration(), event.getEventDate, event.getEventPoints);
//      }        
//		
//		mEventsCursor = mDbHelper.fetchAllUsersHistory();
//		startManagingCursor(mEventsCursor);
//	
//		showHistoryInList();
//        setContentView(R.layout.me);   
        
/**********************************/
/***********DEBUG AREA*************/
/**********************************/
        
      mDbHelper = new UsersHistoryDbAdapter(this);
      mDbHelper.open();
      mDbHelper.createUsersHistory("mor1","Feed the hungry in Even Gvirol", "12/12/12", "100", "2h");
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
		client.AddParam("username", username);

		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server - faild", Toast.LENGTH_LONG);
			debugging.show();
			return -1;
		}
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		long p = Long.parseLong(JSONResponse);
		return p;
	}


    private List<Event> remote_getUsersHistory(String username){
		client.AddParam("action", "getUserHistory");
		client.AddParam("username", username);
		client.AddParam("userDate", (new Date()).toString());
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server - faild", Toast.LENGTH_LONG);
			debugging.show();
			return null;
		}
				
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		return new JSONDeserializer<List<Event>>().deserialize(JSONResponse);		

		
	}

    private List<Event> remote_getUsersFutureEvents(String username){
		client.AddParam("action", "getRegisteredFutureEvents");
		client.AddParam("username", username);
		client.AddParam("userDate", (new Date()).toString());
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server - faild", Toast.LENGTH_LONG);
			debugging.show();
			return null;
		}
				
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		
		//Parse the response from server
		return new JSONDeserializer<List<Event>>().deserialize(JSONResponse);		

		
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
	
}