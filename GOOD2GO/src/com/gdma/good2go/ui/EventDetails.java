package com.gdma.good2go.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarTabActivity;
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventDetailsUtil;
import com.gdma.good2go.utils.EventsDbAdapter;

public class EventDetails extends ActionBarTabActivity {
	private static final String TAG = "EventDetails";
    TabHost mTabHost;
    
    private Long mRowId;
    private String mEventName;
    private String mEventDesc;
    private String mEventDetails;
    private String mEventDistance;
    private String mEventCity;
    private String mEventSreet;
    private String mEventWhen;
    private String mEventDuration;
    private String mEventNPO;
    private int mEventImage;
    private String mOccurenceKey;
    private String mSender;    
    private EventsDbAdapter mDbHelper;
    private AppPreferencesPrivateDetails mUsersPrefs;
    private String mUserName;
    private Context mContext;

    
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
		mSender= extras!= null?
				mSender = extras.getString("sender"):null;
	    
	    /**GET DATA FROM DB*/
	    mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
	    
	    Cursor event = mDbHelper.fetchEvent(mRowId);
	    startManagingCursor(event);
	    
	    /**POPULATE VIEWS FROM DB*/   
	    TextView eventTitle = (TextView) findViewById(R.id.eventTitle);
	    TextView eventDescription = (TextView) findViewById(R.id.eventDescription);
	    TextView eventDetails = (TextView) findViewById(R.id.event_details);
	    TextView eventDuration = (TextView) findViewById(R.id.howlong);
	    TextView eventWhen = (TextView) findViewById(R.id.when);
	    TextView eventWhere = (TextView) findViewById(R.id.where);
	    ImageView eventImage = (ImageView) findViewById(R.id.eventPic);
	    TextView eventNPO = (TextView) findViewById(R.id.npo_view);

	    
	    mEventName = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENTNAME));
	    mEventDesc = event.getString(event.getColumnIndexOrThrow(
	    		EventsDbAdapter.KEY_EVENT_SHORT_INFO));
	    mEventDetails = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DETAILS));
	    mEventDistance = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DISTANCE));
	    mEventCity = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_CITY));	   
	    mEventDuration = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DURATION));	    
	    mEventImage = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_IMAGE));
	    mEventWhen = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_START_TIME)) 
	    		+ "-" 
	    		+ event.getString(event.getColumnIndexOrThrow
	    				(EventsDbAdapter.KEY_EVENT_END_TIME));
	    mEventNPO = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_NPO_NAME));
	    
	    mOccurenceKey = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_OCCURENCE_KEY));
	    	    
	    eventTitle.setText(mEventName);
	    eventDescription.setText(mEventDesc);
	    eventDetails.setText(mEventDetails);	    
	    eventWhere.setText(mEventCity);
	    eventDuration.setText(mEventDuration);
	    eventWhen.setText(mEventWhen);	    
	    eventImage.setImageResource(mEventImage);
	    eventNPO.setText(mEventNPO);
	    
	    /**TODO: add distance*/

	    if(event!=null&&!event.isClosed()){
        	mDbHelper.close();
        }
	    
	    
	    /**SHOW TABS AND LAYOUT*/
	    mTabHost.addTab(mTabHost.newTabSpec("tab_about_event").setIndicator
	    		("About").setContent(R.id.event_details));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_map_event").setIndicator(
	    		"Map").setContent(R.id.map_view));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_npo").setIndicator
	    		("Who").setContent(R.id.npo_view));
	    
	    mTabHost.setCurrentTab(0);

	    EventDetailsUtil.initTabsAppearance(mTabHost);
	    

	    final Button buttonCountMeIn = (Button) findViewById(R.id.countmeinbtn);
	    if (mSender.compareTo("confirmation")!=0){	   
	    	buttonCountMeIn.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View view) {
		        	mContext = view.getContext();
		        	mUsersPrefs = new AppPreferencesPrivateDetails(view.getContext());
		        	mUserName = mUsersPrefs.getUserName();
		        	if (!mUsersPrefs.doPrivateDetailsExist()){
		        	  Intent newIntent = new Intent(view.getContext(), PersonalDetailsTab.class);
		        	  startActivityForResult(newIntent, ActivitysCodeUtil.GET_USERS_PRIVATE_DETAILS);
		        	}
		        	else{
		        		getCountMeInTab(view.getContext());
		        	}
		        }
	    	});
	    }

	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode==Activity.RESULT_OK)
		{
			if(requestCode==ActivitysCodeUtil.GET_USERS_PRIVATE_DETAILS){
            	getCountMeInTab(mContext);
			}
			if(requestCode==ActivitysCodeUtil.GET_COUNT_ME_IN_TAB){
				final Button buttonCountMeIn = (Button) findViewById(R.id.countmeinbtn);
				buttonCountMeIn.setText("You're in!");
				buttonCountMeIn.setClickable(false);			
			}
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
        	newIntent.putExtra("sender", TAG);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    

	private void getCountMeInTab( Context c){
		Bundle extraInfo = new Bundle();
		extraInfo.putString("userName", mUserName);
        extraInfo.putString("eventname", mEventName);
        extraInfo.putString("desc", mEventDesc);
        extraInfo.putString("occurence_key", mOccurenceKey);       
        extraInfo.putString("event_id", Long.toString(mRowId));
        Intent newIntent = new Intent(c, CountMeIn.class);
        newIntent.putExtras(extraInfo);
        startActivityForResult(newIntent, ActivitysCodeUtil.GET_COUNT_ME_IN_TAB);
	}
    
}