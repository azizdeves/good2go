package com.gdma.good2go.ui;


import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarListActivity;
import com.gdma.good2go.ui.maputils.EventOverlayItem;
import com.gdma.good2go.ui.maputils.EventsItemizedOverlay;
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.AppPreferencesFilterDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.FiltersUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;


//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;

//import android.support.v4.app.ListFragment;;

public class ListTab extends ActionBarListActivity {
	private static final String TAG = "List";
	private EventsDbAdapter mDbHelper;
	Cursor mEventsCursor;
	
	private int mLatitude;
	private int mLongitude;
	private String mSender;
	
	private String[] mColumns;
	private Button buttonFilterEvents;
	private AppPreferencesFilterDetails mFilterPrefs;	
	
	private class EventsViewBinder implements SimpleCursorAdapter.ViewBinder 
	{    
	    public boolean setViewValue(View view, Cursor cursor, int columnIndex) 
	    {
	    	if(view instanceof ImageView) 
			{
				ImageView iv = (ImageView) view;
				int imageResource = cursor.getInt(columnIndex);
				iv.setImageResource(imageResource);
				return true;
			}		
			return false;
		}
	}

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
	    // get location passed from map
		Bundle extras = getIntent().getExtras();
		mSender= (extras!= null) ?
				extras.getString("sender") : null;
		if (mSender.compareTo("map")==0)
		{
			mLatitude = extras.getInt("lat");
			mLongitude = extras.getInt("lon");
		}
    	
	      mFilterPrefs = new AppPreferencesFilterDetails(this);
	      showFilteredEventsOnList();


    }

    
    @Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data)
 	{
 		super.onActivityResult(requestCode, resultCode, data);
		 if (requestCode == ActivitysCodeUtil.GET_FILTERED_EVENTS) {
			if(resultCode==RESULT_OK){
				showFilteredEventsOnList();
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
    

	
	private void onCreateHelper(){
    	/**GET POINTS FROM LOCAL DB*/
    	mDbHelper = new EventsDbAdapter(this);
		mDbHelper.open(); //TODO CLOSE WHEN THE APP IS CLOSED
		
		mEventsCursor = mDbHelper.fetchAllEvents();
		startManagingCursor(mEventsCursor);
	
		showPointsInList();
        setContentView(R.layout.list);
        mDbHelper.close();
        getFilteredEventsButton();
		

		
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
        	newIntent.putExtra("sender", TAG);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
    private void showFilteredEventsOnList(){


		mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
	    mEventsCursor = mDbHelper.fetchEventByFilters(FiltersUtil.getArrayOfFilteredTypes(mFilterPrefs), mFilterPrefs.getRadius(), mFilterPrefs.getDuration());
//				Toast debugging=Toast.makeText(this, "#of results:"+Integer.toString(eventsCursor.getCount()), Toast.LENGTH_SHORT);
//				debugging.show();

		
		startManagingCursor(mEventsCursor);
		showPointsInList();
        setContentView(R.layout.list);			

        getUnFilteredEventsButton();
    	
    }
    
	public void getFilterScreen(View view){
    	Intent myIntent = new Intent(view.getContext(),FilterTab.class);
    	startActivityForResult(myIntent, ActivitysCodeUtil.GET_FILTERED_EVENTS);		
	}
	
	private void updateEventsDistance()
	{
		int cnt = 0;
		
        if (mEventsCursor.moveToFirst()) 
        {
        	do{
            	Long eventId = mEventsCursor.getLong
            			(mEventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENTID));
            	String oldDistance=(mEventsCursor.getString
            			(mEventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_DISTANCE)));
            	int lat=(mEventsCursor.getInt
            			(mEventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_GP_LAT)));
            	int lon=(mEventsCursor.getInt
            			(mEventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_GP_LONG)));
            	
            	if (lat!=0 && lon!=0)
            	{
            		
	            	float results[]=new float[3];
	    			
	            	Location.distanceBetween(mLatitude / 1E6, mLongitude/ 1E6, lat / 1E6, lon / 1E6, results);
	            	String newDistance=String.format("%.1f", (float)(results[0]/1E3))+" km";	
	            	
	            	if (oldDistance.compareTo(newDistance)==0) 
	            		break;
	            	
	            	mDbHelper.updateEvent(eventId, newDistance);
	        		
	            	cnt++;
	            	}
            	}
        	while(mEventsCursor.moveToNext() && cnt!=mEventsCursor.getCount());
        }
        
        mEventsCursor.requery();
	}
    
    private void showPointsInList(){
    	updateEventsDistance();
    	
        mColumns = new String[] {EventsDbAdapter.KEY_EVENTNAME, 
        		EventsDbAdapter.KEY_EVENT_SHORT_INFO,
        		EventsDbAdapter.KEY_EVENT_DISTANCE,
        		EventsDbAdapter.KEY_EVENT_IMAGE};
       
        int[] to = new int[] {R.id.eventname_entry, 
        		R.id.eventinfo_entry, 
        		R.id.eventdistance_entry,
        		R.id.eventPicList};
        
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, 
        		R.layout.list_item, 
        		mEventsCursor, mColumns, to);
        
        adapter.setViewBinder(new EventsViewBinder());
        setListAdapter(adapter);
        
    }

    private void getUnFilteredEventsButton(){
		
		buttonFilterEvents = (Button) findViewById(R.id.FilterEventsListViewButton);	
		buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_on); 
		buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mFilterPrefs.saveDefaultFilterPrefs();
            	onCreateHelper();
            }
        }); 
    }
    
    private void getFilteredEventsButton(){
        buttonFilterEvents = (Button) findViewById(R.id.FilterEventsListViewButton);
        buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_off); 
        buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFilterScreen(v);
            }
        });	
    }
}