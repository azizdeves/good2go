package com.gdma.good2go;

import com.example.android.actionbarcompat.ActionBarTabActivity;
import com.example.android.actionbarcompat.MainActivity;
import com.example.android.actionbarcompat.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class EventDetails extends ActionBarTabActivity {
	
    TabHost mTabHost;
    
    private Long mRowId;
    private String mEventName;
    private String mEventDesc;
    private String mEventDetails;
    private String mEventDistance;
    private String mEventCity;
    private String mEventSreet;
    private String mEventStreetNumber;
    
        
	private EventsDbAdapter mDbHelper;
    
    /**@TODO - add all the fields here in case this activity dies, so it can resume
  
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.event_details);

	    mTabHost = getTabHost();
	    
	    
	    /**GET EVENT ID PASSED FROM CALLING ACTIVITY*/
		Bundle extras = getIntent().getExtras();
		mRowId=extras!= null?
				mRowId = extras.getLong(EventsDbAdapter.KEY_EVENTID):0;
	       
	    
	    /**GET DATA FROM DB*/
	    mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
	    
	    Cursor event = mDbHelper.fetchEvent(mRowId);
	    startManagingCursor(event);
	    
	    /**POPULATE VIEWS FROM DB*/   
	    TextView eventTitle = (TextView) findViewById(R.id.eventTitle);
	    TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
	    TextView eventDetails = (TextView) findViewById(R.id.eventdetails);
	    TextView eventDuration = (TextView) findViewById(R.id.howlong);
	    TextView eventWhen = (TextView) findViewById(R.id.when);
	    TextView eventWhere = (TextView) findViewById(R.id.where);

	    
	    mEventName=event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENTNAME));
	    mEventDesc=event.getString(event.getColumnIndexOrThrow(
	    		EventsDbAdapter.KEY_EVENT_SHORT_INFO));
	    mEventDetails=event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DETAILS));
	    mEventDistance=event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DISTANCE));
	    mEventCity=event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_CITY));
	    
	    eventTitle.setText(mEventName);
	    eventDescription.setText(mEventDesc);
	    eventDetails.setText(mEventDetails);
	    eventWhere.setText(mEventCity);
	    
	    /**TODO: add distance*/

	    if(event!=null&&!event.isClosed()){
        	mDbHelper.close();
        }
	    
	    
	    /**SHOW TABS AND LAYOUT*/
	    mTabHost.addTab(mTabHost.newTabSpec("tab_about_event").setIndicator
	    		("About").setContent(R.id.eventdetails));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_map_event").setIndicator(
	    		"Map").setContent(R.id.textview2));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_npo").setIndicator
	    		("Who").setContent(R.id.textview3));
	    
	    mTabHost.setCurrentTab(0);
	    
	    
	    final Button buttonCountMeIn = (Button) findViewById(R.id.countmeinbtn);
	   
	    buttonCountMeIn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	    		Bundle extraInfo = new Bundle();
	            extraInfo.putString("name", mEventName);
	            extraInfo.putString("desc", mEventDesc);
	            
	            Intent newIntent = new Intent(view.getContext(), 
	                            CountMeIn.class);
	            newIntent.putExtras(extraInfo);
	            startActivityForResult(newIntent, 1);
	        }
	    });
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode==Activity.RESULT_OK)
		{
			final Button buttonCountMeIn = (Button) findViewById(R.id.countmeinbtn);
			buttonCountMeIn.setText("You're in!");
			buttonCountMeIn.setClickable(false);
		}
	}

    
    
	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.empty, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Intent newIntent = null;
        switch (item.getItemId()) {
        
        case android.R.id.home:	
        	newIntent = new Intent(this, MainActivity.class);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
}