package com.gdma.good2go;

import java.util.Date;
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
        
  
        points=70;
//      points=remote_getUsersKarma(userName);
// 		badge=Karma.Badge.getMyBadge(points)       
//      User u = getUsersDetails(userName);
//      userFirstName=u.getFirstName();
//      userLastName=u.getLastName();
        userNiceName=userFirstName+" "+userLastName;
        TextView tvName = (TextView) findViewById(R.id.userNameMeView);
        TextView tvPoints = (TextView) findViewById(R.id.pointSeekValMeView);
        SeekBar pointsProg = (SeekBar)findViewById(R.id.pointSeekMeView);
        
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
      mDbHelper.createUsersHistory("Feed the hungry in Even Gvirol", "12/12/12", "100", "2h");
      mDbHelper.createUsersHistory("Clean the beach", "13/13/13", "30", "2h") ; 
      mEventsCursor=mDbHelper.fetchAllUsersHistory();
      int debugger = mEventsCursor.getCount(); 
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
		return 0;
//		
//		JsonParser parser = new JsonParser();
//		
//		jsonArray = parser.parse(json).getAsJsonArray();
//		for (int i=0;i<jsonArray.size();i++){
//			JsonObject jsonKarma = (JsonObject) jsonArray.get(i);
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
}