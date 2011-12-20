package com.gdma.good2go;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListTab extends ListActivity {
	
	private static final int GET_FILTERED_EVENTS = 10;
	private EventsDbAdapter mDbHelper;
	Cursor mEventsCursor;
	
	private String[] mColumns;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	
    	/**GET POINTS FROM LOCAL DB*/
    	mDbHelper = new EventsDbAdapter(this);
		mDbHelper.open(); //TODO CLOSE WHEN THE APP IS CLOSED
		
		mEventsCursor = mDbHelper.fetchAllEvents();
		startManagingCursor(mEventsCursor);
	
		showPointsInList();
        setContentView(R.layout.list);
		final Button buttonFilterEvents = (Button) findViewById(R.id.FilterEventsListViewButton);
    }
    
    private void showPointsInList(){
        mColumns = new String[] {EventsDbAdapter.KEY_EVENTNAME, EventsDbAdapter.KEY_EVENT_SHORT_INFO,EventsDbAdapter.KEY_EVENT_DISTANCE};
        int[] to = new int[] {R.id.eventname_entry, R.id.eventinfo_entry, R.id.eventdistance_entry };
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,R.layout.list_item,mEventsCursor, mColumns, to);
        setListAdapter(mAdapter);
        
    }
    
    @Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data)
 	{
 		super.onActivityResult(requestCode, resultCode, data);
 		 if (requestCode == GET_FILTERED_EVENTS) {
 			if(resultCode==RESULT_OK){
 				Bundle bundleResult = data.getExtras();
 				int i=0;
 				int duration=0;
 				int radius=0;
 				String types = (bundleResult!=null) ? bundleResult.getString("1"):null;
// 				Toast debugging=Toast.makeText(this,types, Toast.LENGTH_LONG);
// 				debugging.show();	
 				duration= (bundleResult!=null) ? bundleResult.getInt("durationInMinutes"): -1;
 				radius = (bundleResult!=null) ? bundleResult.getInt("radius"):-1;

 				mEventsCursor = mDbHelper.fetchAllEvents();
 				//mEventsCursor = mDbHelper.fetchEventByFilters(types, radius, duration)
 				startManagingCursor(mEventsCursor);
 				showPointsInList();
 			}
 			if(resultCode==RESULT_CANCELED){
			
 			}		
 		 }
 	}
    
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Cursor c = mEventsCursor;
        c.moveToPosition(position);
        
        Intent i = new Intent(this, EventDetails.class);
        i.putExtra("sender", "list");
        i.putExtra(EventsDbAdapter.KEY_EVENTID, id);
        startActivity(i);
    }
    
	public void getFilterScreen(View view){
    	Intent myIntent = new Intent(view.getContext(),FilterTab.class);
    	startActivityForResult(myIntent, GET_FILTERED_EVENTS);		
	}
}