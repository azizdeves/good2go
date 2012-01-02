package com.gdma.good2go;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.android.actionbarcompat.ActionBarListActivity;
import com.example.android.actionbarcompat.MainActivity;
import com.example.android.actionbarcompat.R;


//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;

//import android.support.v4.app.ListFragment;;

public class ListTab extends ActionBarListActivity {
	
	private static final int GET_FILTERED_EVENTS = 10;
	private EventsDbAdapter mDbHelper;
	Cursor mEventsCursor;
	
	private String[] mColumns;
	private Button buttonFilterEvents;

    /** Called when the activity is first created. */
    @Override
    
    
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.map);
//        onCreateHelper();
//       
//    }
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
       	onCreateHelper();

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
				int arrSize=0;
				int duration=-1;
				int radius=-1;
				String[] types= new String[bundleResult.size()-2];
				for (int j = 0; j < types.length; j++) {
					types[i]="";
				}
				
				if(bundleResult!=null){
					if(bundleResult.getString("animals").compareTo("1")==0){
						types[i]="animals";
						i++;
						arrSize++;
					}	
					if(bundleResult.getString("children")=="1"){
						types[i]="children";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("disabled")=="1"){
						types[i]="disabled";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("elderly")=="1"){
						types[i]="elderly";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("environment")=="1"){
						types[i]="environment";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("special")=="1"){
						types[i]="special";
						i++;
						arrSize++;
					}
					if( bundleResult.getInt("durationInMinutes")>-1)
						duration= bundleResult.getInt("durationInMinutes");
					if( bundleResult.getInt("radius")>-1)
						radius =  bundleResult.getInt("radius");
				
				}
				/******DEBUGGING AREA******/
//				Toast debugging=Toast.makeText(this, Integer.toString(duration), Toast.LENGTH_SHORT);
//				debugging.show();	
//				debugging=Toast.makeText(this, Integer.toString(radius), Toast.LENGTH_SHORT);
//				debugging.show();	
//				for (int j = 0; j < arrSize; j++) {
//					debugging=Toast.makeText(this, types[j], Toast.LENGTH_SHORT);
//					debugging.show();					
//				}
	
				/***END OF DEBUGGING AREA***/	
				
				//mEventsCursor = mDbHelper.fetchAllEvents();
				mEventsCursor = mDbHelper.fetchEventByFilters(types, radius, duration);

				startManagingCursor(mEventsCursor);
 				showPointsInList();
 				
				buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_on); 
				buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	onCreateHelper();
		            }
		        });
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
	
	private void onCreateHelper(){
    	/**GET POINTS FROM LOCAL DB*/
    	mDbHelper = new EventsDbAdapter(this);
		mDbHelper.open(); //TODO CLOSE WHEN THE APP IS CLOSED
		
		mEventsCursor = mDbHelper.fetchAllEvents();
		startManagingCursor(mEventsCursor);
	
		showPointsInList();
        setContentView(R.layout.list);
		buttonFilterEvents = (Button) findViewById(R.id.FilterEventsListViewButton);
        buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_off); 
        buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFilterScreen(v);
            }
        });
		
	}
	
	
	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list, menu);

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
        
        case android.R.id.home:
        	
        	newIntent = new Intent(this, MainActivity.class);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
}