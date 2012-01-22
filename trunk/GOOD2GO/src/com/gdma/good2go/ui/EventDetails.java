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
import android.widget.TextView;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;

public class EventDetails extends ActionBarActivity {
	private static final String TAG = "EventDetails";
    //TabHost mTabHost;
    
    private Long mRowId;
    private String mEventName;
    private String mEventDesc;
    private String mEventDetails;
    private String mEventDistance;
    private String mEventCity;
    private String mEventSreet;
    private String mEventSreetNum;
    private String mEventWhen;
    private String mEventDuration;
    private String mEventNPO;
    private int mEventImage;
    private String mEventPrereq;
    private boolean mPhysical;
    private boolean mMental;
    private boolean mForKids;
    private boolean mForGroups;
    private boolean mForIndivid;
    private int mForGroupsHowMany;
    
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
	    TextView eventDetails = (TextView) findViewById(R.id.detailsData);
	    TextView eventDuration = (TextView) findViewById(R.id.howlong);
	    TextView eventWhen = (TextView) findViewById(R.id.when);
	    TextView eventWhere = (TextView) findViewById(R.id.where);
	    ImageView eventImage = (ImageView) findViewById(R.id.eventPic);
	    TextView eventNPO = (TextView) findViewById(R.id.eventNPO);
	    TextView eventPrereq = (TextView) findViewById(R.id.notesData);
	    ImageView typeMenial = (ImageView) findViewById(R.id.typeMenial);
	    ImageView typeMental = (ImageView) findViewById(R.id.typeMental);
	    ImageView forKids = (ImageView) findViewById(R.id.suitKids);
	    ImageView forIndivid = (ImageView) findViewById(R.id.suitIndivid);
	    ImageView forGroups = (ImageView) findViewById(R.id.suitGroup);
	    TextView textGroup = (TextView) findViewById(R.id.textGroup);
	    

	    
	    mEventName = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENTNAME));
	    mEventDesc = event.getString(event.getColumnIndexOrThrow(
	    		EventsDbAdapter.KEY_EVENT_SHORT_INFO));
	    mEventDetails = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DETAILS));
	    
	    
	    mEventDetails = mEventDetails.replace(". ", ".\n");
	    mEventDetails = mEventDetails.replace("? ", "?\n");
	    mEventDetails = mEventDetails.replace("! ", "!\n");
	    
	    mEventDistance = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_DISTANCE));
	    mEventCity = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_CITY));
	    
	    mEventSreet = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_STREET));
	    mEventSreetNum = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_STREET_NUMBER));
	    
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
	    
	    mEventPrereq = event.getString(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_PRE_REQ));
	    
	    mEventPrereq = mEventDetails.replace("(", " ");
	    mEventPrereq = mEventDetails.replace(")", " ");
	    mEventPrereq = mEventPrereq + " " + mEventSreet + " " + mEventSreetNum;
	    
	    if (mEventSreet.length()!=0)
	    {
	    	mEventSreet = "\n" + mEventSreet;
		 
		    if (mEventSreetNum.length()!=0){
		    	mEventSreetNum = " " + mEventSreetNum;}
		    
		    mEventPrereq = mEventPrereq + mEventSreet + mEventSreetNum;
	    }
	    
	    mPhysical = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_WORK_MENIAL)) 
	    		 == 1 ? true : false;
	    
	    mMental = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_WORK_MENTAL)) 
	    		 == 1 ? true : false;
	    
	    mForKids = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_IS_FOR_KIDS)) 
	    		 == 1 ? true : false;
	    
	    mForGroups = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_IS_FOR_GROUPS)) 
	    		 == 1 ? true : false;
	    
	    mForIndivid = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_IS_FOR_INDIVID)) 
	    		 == 1 ? true : false;
	    
	    mForGroupsHowMany = event.getInt(event.getColumnIndexOrThrow
	    		(EventsDbAdapter.KEY_EVENT_WORK_MENIAL));
	    	    
	    eventTitle.setText(mEventName);
	    eventDescription.setText(mEventDesc);
	    eventDetails.setText(mEventDetails);	    
	    eventWhere.setText(mEventCity);
	    eventDuration.setText(mEventDuration);
	    eventWhen.setText(mEventWhen);	    
	    eventImage.setImageResource(mEventImage);
	    eventNPO.setText(mEventNPO);
	    eventPrereq.setText(mEventPrereq);
	    
	    
	    if (mPhysical){
	    	typeMenial.setImageResource(R.drawable.worktype_menial_on);
	    }
	    
	    if (mMental){
	    	typeMental.setImageResource(R.drawable.worktype_mental_on);
	    }
	    
	    if (mForKids){
	    	forKids.setImageResource(R.drawable.suitfor_kid_on);
	    }
	    
	    if (mForGroups){
	    	forGroups.setImageResource(R.drawable.suitfor_group_on);
	    	textGroup.setText(textGroup.getText() + " (" + mForGroupsHowMany + ")");
	    }
	    
	    if (mForIndivid){
	    	forIndivid.setImageResource(R.drawable.suitfor_individ_on);
	    }

	    if(event!=null&&!event.isClosed()){
        	mDbHelper.close();
        }
	    

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