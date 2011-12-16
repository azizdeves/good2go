package com.gdma.good2go;

import com.gdma.good2go.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

public class EventDetails extends TabActivity {
	
    TabHost mTabHost;
    
    private Long mRowId;
    private String mEventName;
    private String mEventDesc;
    private String mEventDetails;
    
        
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

	    
	    mEventName=event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENTNAME));
	    mEventDesc=event.getString(event.getColumnIndexOrThrow(
	    		EventsDbAdapter.KEY_EVENT_SHORT_INFO));
	    mEventDetails=event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DETAILS));
	    
	    eventTitle.setText(mEventName);
	    eventDescription.setText(mEventDesc);
	    eventDetails.setText(mEventDetails);

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

}