package com.gdma.good2go;

import java.util.List;

import android.R.drawable;
import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gdma.good2go.communication.RestClient;

import flexjson.JSONDeserializer;

public class MeTab extends ListActivity {
    
    drawable myPic;
    int points;
    String badget;
    String userId;
    String userName="";
    String userFirstName="Mor";
    String userLastName="Yogev";
    List<Event> usersEvents;
    RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    private UsersHistoryDbAdapter mDbHelper;
	Cursor mEventsCursor;
	private String[] mColumns;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.me);
        //GET USERS INFO FROM THE DB
        points=70;
        badget="mother tereza";
//      User u = getUsersDetails(userName);
//      userFirstName=u.getFirstName();
//      userLastName=u.getLastName();
//      usersEvents=getUsersEvent(userName);
//      points=getUsersPoints(userName);
//      badget=getUsersBadget(userName);        
        userName=userFirstName+" "+userLastName;
        TextView tv = (TextView) findViewById(R.id.userNameMeView);
        tv.setText(userName);
        SeekBar pointsProg = (SeekBar)findViewById(R.id.pointSeekMeView);
        pointsProg.setProgress(points);
        pointsProg.setEnabled(false);        
 
//        List<Event> historyList=remote_getUsersHistory(userId);
//        mDbHelper = new UsersHistoryDbAdapter(this);
//        mDbHelper.open();
//        for(Event event : historyList)
//        {
//        	mDbHelper.createUsersHistory(event.getEventKey(), event.getEventName(), event.getMinDuration(), event.getEventDate, event.getEventPoints);
//        }
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
      mDbHelper.createUsersHistory("mor", "mor", "12/12/12", "100", "2h");
      mDbHelper.createUsersHistory("mor2", "mor2", "13/13/13", "30", "2h") ;
      mEventsCursor=mDbHelper.fetchAllUsersHistory();
      startManagingCursor(mEventsCursor);
      showHistoryInList();
      
    }
    
    private User remote_getUsersDetails(String username){
		client.AddParam("action", "getUsersDetails");
		client.AddParam("username", username);

		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server - faild", Toast.LENGTH_LONG);
			debugging.show();
			return null;
		}
		String JSONResponse = client.getResponse();
		User u = new JSONDeserializer<User>().deserialize( JSONResponse );
		return u;
	}

    private List<Event> remote_getUsersHistory(String username){
		client.AddParam("action", "getUsersHistory");
		client.AddParam("username", username);
		
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
	
        mColumns = new String[] {UsersHistoryDbAdapter.KEY_EVENTDATE, UsersHistoryDbAdapter.KEY_EVENTNAME,
        		UsersHistoryDbAdapter.KEY_EVENT_DURATION, UsersHistoryDbAdapter.KEY_EVENPOINTS};
        int[] to = new int[] {R.id.eventDate_entry, R.id.eventInfo_entry, R.id.eventDuration_entry, R.id.eventPoints_entry};
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,R.layout.me_history_list_item,mEventsCursor, mColumns, to);
        setListAdapter(mAdapter);
        
    }

    

//    private K getUsersKarma(String username){
//		client.AddParam("action", "getUsersKarma");
//		client.AddParam("username", username);
//		
//		String JSONResponse = client.getResponse();
//
//	}
}