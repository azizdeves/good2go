package com.gdma.good2go.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.gdma.good2go.Event;
import com.gdma.good2go.Karma;
import com.gdma.good2go.Karma.Badge;
import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.KeyManager;
import com.gdma.good2go.utils.UsersFutureEventsDbAdapter;
import com.gdma.good2go.utils.UsersHistoryDbAdapter;

public class MeTab extends ActionBarActivity {
	private static final String TAG = "Me";
	private static final int POINTS_PER_HOUR = 1000;
	
    TextView mTvUserName;
    TextView mUserPoints;
    SeekBar mPointsProg;
	
	private ListView mHistoryList;
	private ListView mFutureList;
	
    private UsersHistoryDbAdapter mDbHelperHistoryEvents;
    private UsersFutureEventsDbAdapter mDbHelperFutureEvents;
    
    private Cursor mHistoryEventsCursor;
    private Cursor mFutureEventsCursor;
    
	//private drawable myPic;
	private long mPoints;
	private String mBadge;

	private String mUserName="";

	private AppPreferencesPrivateDetails mUsersPref; 
	
    private String mEventName;
    private String mEventDate;
    private String mOccurenceKey;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);
        
        mUsersPref = new AppPreferencesPrivateDetails(this);
        
        mHistoryList = (ListView) findViewById(R.id.historyListMeView);
        mFutureList = (ListView) findViewById(R.id.futureListMeView);
        mTvUserName = (TextView) findViewById(R.id.userNameMeView);
        mUserPoints = (TextView) findViewById(R.id.pointSeekValMeView);
        mPointsProg = (SeekBar) findViewById(R.id.pointSeekMeView);
        
        mHistoryList.setEmptyView(findViewById(R.id.empty2));
        mFutureList.setEmptyView(findViewById(R.id.empty));
        
        mUserName = mUsersPref.getUserName();
        
        String userNiceName = mUsersPref.doPrivateDetailsExist() ?
        		mUsersPref.getUserFirstName() + " " + mUsersPref.getUserLastName()
        		: mUserName.substring(0, mUserName.indexOf("@"));
        		
        mTvUserName.setText(userNiceName);
       
        //check karma, unrated, history and future events async              
        new checkForUsersUnRatedEventsTask().execute(mUserName);
        new getUserKarmaTask().execute(mUserName); 
        new getUserHistoryTask().execute(mUserName);
        new getUserFutureTask().execute(mUserName);    
    }
    

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		new getUserKarmaTask().execute(mUserName);
    }
   	
	
    /** THREADS **/
    private class checkForUsersUnRatedEventsTask extends AsyncTask<String, Void, List<Event>> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(MeTab.this);
    		dialog.setMessage(getString(R.string.loading));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(List<Event> feedbackList) {
    		dialog.dismiss();
    		if (feedbackList!=null)
    			getUserFeedback(feedbackList);
    	}
			
		@Override
		protected List<Event> doInBackground(String... userDetails) {
			RemoteFunctions rf = RemoteFunctions.INSTANCE;    		
    		return rf.getUserUnfeedbackedEvents(userDetails[0], 
    				Long.toString(new Date().getTime()));			
		}
	
    }
    
    private class getUserHistoryTask extends AsyncTask<String, Void, List<String[]>> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(MeTab.this);
    		dialog.setMessage(getString(R.string.loading));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(List<String[]> historyEvents) {
    		dialog.dismiss();
    		if (historyEvents!=null)
    			writeEventsToLocalHistoryDB(historyEvents);    		
    	}
			
		@Override
		protected List<String[]> doInBackground(String... userDetails) {
			RemoteFunctions rf = RemoteFunctions.INSTANCE;
    		return rf.getUserHistoryEvents(userDetails[0], 
    				Long.toString(new Date().getTime()));			
		}
	
    }
         
    private class getUserFutureTask extends AsyncTask<String, Void, List<String[]>> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(MeTab.this);
    		dialog.setMessage(getString(R.string.loading));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(List<String[]> futureEvents) {
    		dialog.dismiss();
    		if (futureEvents!=null)
    			writeEventsToLocalFutureDB(futureEvents);    		
    	}
			

		@Override
		protected List<String[]> doInBackground(String... userDetails) {
			RemoteFunctions rf = RemoteFunctions.INSTANCE;
    		return rf.getUserFutureEvents(userDetails[0], 
    				Long.toString(new Date().getTime()));			
		}
	
    }
    
    private class getUserKarmaTask extends AsyncTask<String, Void, Long> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(MeTab.this);
    		dialog.setMessage(getString(R.string.loading));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(Long points) {
    		dialog.dismiss();
    		setKarma(points);   		
    	}
			

		@Override
		protected Long doInBackground(String... userName) {
			return RemoteFunctions.INSTANCE.getUserKarma(userName[0]);			
		}
	
    }
    
    /** FUNCS TO BE CALLED FROM THREADS */
	
    private void setKarma(long points) {
        mPoints=points;
 		mBadge=Karma.Badge.getMyBadge(mPoints).getName();
 		setBadgesPictures(mBadge);        
        mUserPoints.setText(Integer.toString((int)mPoints));
        mPointsProg.setProgress((int)mPoints);
        mPointsProg.setEnabled(false);  		
	}
	
	
    private void showHistoryInList(){
    	String[] columns = new String[] {UsersHistoryDbAdapter.KEY_EVENTDATE,
    			UsersHistoryDbAdapter.KEY_EVENTNAME,
    			UsersHistoryDbAdapter.KEY_EVENT_DURATION,
    			UsersHistoryDbAdapter.KEY_EVENPOINTS};

    	int[] to = new int[] {R.id.eventDate_entry, R.id.eventInfo_entry, 
    			R.id.eventDuration_entry, R.id.eventPoints_entry};
       
    	SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
    			R.layout.me_history_list_item,mHistoryEventsCursor, columns, to);
       
    	mHistoryList.setAdapter(mAdapter);
    }
  
    
    private void showFutureInList(){
    	String[] columns = new String[] {UsersFutureEventsDbAdapter.KEY_EVENTDATE, 
    			UsersFutureEventsDbAdapter.KEY_EVENTNAME, 
    			UsersFutureEventsDbAdapter.KEY_EVENT_DURATION, 
    			UsersFutureEventsDbAdapter.KEY_EVENPOINTS};

    	int[] to = new int[] {R.id.futureEventDate_entry, R.id.futureEventInfo_entry, 
    			R.id.futureEventDuration_entry, R.id.futureEventPoints_entry};
    	
        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
        		R.layout.me_future_list_item, mFutureEventsCursor, columns, to);
        
        mFutureList.setAdapter(mAdapter);
    }

	
	private void setBadgesPictures(String badge){
		
		setBadgePic(R.id.mrNiceGuy_PicMeView, R.drawable.badge_mrniceguy);		
		
		if(badge.compareTo(Badge.MR_NICE_GUY.getName())==0)
			return;
		
		setBadgePic(R.id.angle_PicMeView,R.drawable.badge_angel);
		
		if(badge.compareTo(Badge.ANGEL.getName())==0)
			return;
		
		setBadgePic(R.id.motherTeresa_PicMeView, R.drawable.badge_mothertheresa);
		
		if(badge.compareTo(Badge.MOTHER_TERESA.getName())==0)
			return;
		
		setBadgePic(R.id.buddhistMonk_PicMeView, R.drawable.badge_buddhistmonk);
		
		if(badge.compareTo(Badge.BUDDHIST_MONK.getName())==0
				|| badge.compareTo(Karma.Badge.SAINT.getName())==0)
			return;
		
		setBadgePic(R.id.dalaiLama_PicMeView, R.drawable.badge_dalailama);
		
		if(badge.compareTo(Badge.DALAI_LAMA.getName())==0)
			return;
		
		setBadgePic(R.id.god_PicMeView, R.drawable.badge_god);
	}
	
	private void setBadgePic(int id, int resId)
	{
		ImageView badgePic = (ImageView) findViewById (id);
		badgePic.setImageResource(resId);
	}    
    
	
	private void getUserFeedback(List <Event> feedbackList) {
		for (Event event : feedbackList) {
			mEventName=event.getEventName();
    		mOccurenceKey=event.getOccurrences().get(0).getOccurrenceKey();
    		
    		Date occDate = event.getOccurrences().get(0).getOccurrenceDate();
    		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM");
    		mEventDate = sdf.format(occDate);
    		        		
    		int unfeedbackedPoints = getDeservedPoints(event.getMinDuration());
    		String ufPoints = Integer.toString(unfeedbackedPoints);
    		
    		Bundle extraInfo = new Bundle();        		
            extraInfo.putString(EventsDbAdapter.KEY_EVENTNAME, mEventName);
            extraInfo.putString(EventsDbAdapter.KEY_EVENT_OCCURENCE_KEY, mOccurenceKey);
            extraInfo.putString("VOLUNTEER_DATE", mEventDate);
            extraInfo.putString("POINTS", ufPoints);
            
            Intent newIntent = new Intent(this, FeedbackTab.class);
            newIntent.putExtras(extraInfo);
            startActivity(newIntent);
            }
        }
	

	private int getDeservedPoints(int totalDurationInMins) {
		int hour = totalDurationInMins / 60;
		return hour * POINTS_PER_HOUR;
	}
	
	private void writeEventsToLocalHistoryDB(List<String[]> historyEvents) {
		KeyManager key = KeyManager.init();
		
		mDbHelperHistoryEvents = new UsersHistoryDbAdapter(this);
		mDbHelperHistoryEvents.open();
		
		if (!mDbHelperHistoryEvents.isUserHistoryEmpty())
			mDbHelperHistoryEvents.deleteAllEvents();
		
		for(String[] event : historyEvents){
			
			String date = convertAmericanDateToLocalDate(event[1]);
			mDbHelperHistoryEvents.createUsersHistory(Long.toString(key.getKey()), 
					event[0], date, event[2], "2h");
			 }
		mHistoryEventsCursor=mDbHelperHistoryEvents.fetchAllUsersHistory();
		startManagingCursor(mHistoryEventsCursor);
		showHistoryInList();
        
		mDbHelperHistoryEvents.close(); 		
	}
	
	
	private String convertAmericanDateToLocalDate(String americanDate) {
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yy");
			Date date = sdfSource.parse(americanDate);
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yy");
			return sdfDestination.format(date);
		} catch (ParseException e) {
			Log.i(TAG, "Couldn't parse american date to local");
		}
		return americanDate;
	}

	private void writeEventsToLocalFutureDB(List<String[]> futureEvents) {
		KeyManager key = KeyManager.init();
		
		mDbHelperFutureEvents = new UsersFutureEventsDbAdapter(this);
		mDbHelperFutureEvents.open();
		
		if (!mDbHelperHistoryEvents.isUserHistoryEmpty())
			mDbHelperHistoryEvents.deleteAllEvents();
		
		for(String[] event : futureEvents){
			String date = convertAmericanDateToLocalDate(event[1]);
			mDbHelperFutureEvents.createUsersFutureEvent(Long.toString(key.getKey()), 
					event[0], date, event[2], "2h");
			 }
		mFutureEventsCursor=mDbHelperFutureEvents.fetchAllUsersFutureEvents();
		startManagingCursor(mFutureEventsCursor);
		showFutureInList();
        
		mDbHelperFutureEvents.close(); 		
	}
    
	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.me, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Intent newIntent = null;
    	
        switch (item.getItemId()) {

        case R.id.menu_map:
        	newIntent = new Intent(this, MapTab.class);
        	newIntent.putExtra("sender", TAG);
        	startActivity(newIntent);
            break;

        case R.id.menu_list:
        	newIntent = new Intent(this, ListTab.class);
        	newIntent.putExtra("sender", TAG);
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
}