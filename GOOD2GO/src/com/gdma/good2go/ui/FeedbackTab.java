package com.gdma.good2go.ui;

import java.text.DecimalFormat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;


public class FeedbackTab extends ActionBarActivity  {
	private static final String TAG = "Feedback";
	
	private String mUserName;
    private String mEventName;
    private String mOccurenceKey;
    private String mEventDate;
    private String mPoints;

	private RatingBar mRating;
	private Button mNoThankYouButton;
	private Button mSendFeedBackButton;

    private TextView mFeedbackEvent;
    private Button mFeedbackPoints;
    private TextView mFeedbackTime;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		Bundle extras = getIntent().getExtras();		
		if(extras!=null){		
			mEventName = extras.getString(EventsDbAdapter.KEY_EVENTNAME);
			mOccurenceKey = extras.getString(EventsDbAdapter.KEY_EVENT_OCCURENCE_KEY);
			mEventDate = extras.getString("VOLUNTEER_DATE");
			mPoints = extras.getString("POINTS");
		}
		
		mUserName = getLocalUsername();

		mFeedbackEvent = (TextView) findViewById (R.id.feedback_event);
		mFeedbackPoints = (Button) findViewById (R.id.feedback_pointsvalue);
		mFeedbackTime = (TextView) findViewById (R.id.feedback_time);
		mRating=(RatingBar)findViewById(R.id.ratingBar_FeedbackView);
		mSendFeedBackButton= (Button) findViewById(R.id.sendFeedBackButton_FeedbackView);
		mNoThankYouButton= (Button) findViewById(R.id.noThankYouButton_FeedbackView);
		
		mFeedbackEvent.setText(getResources().getString(R.string.feedback_event) + mEventName);
		mFeedbackPoints.setText(mPoints);
		mFeedbackTime.setText("on " + mEventDate);

		mSendFeedBackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String rating = new DecimalFormat("#").format(
            			mRating.getRating());
            	
            	new sendUserFeedbackTask().execute(
        				 mUserName, mOccurenceKey, rating);
            }
        });		
				
		mNoThankYouButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {           	
            	new sendUserFeedbackTask().execute(
        				 mUserName, mOccurenceKey, "0");
            }
		});
	}

	private String getLocalUsername(){
		AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
		return prefs.getUserName();
	}

    /**THREADS*/		
    private class sendUserFeedbackTask extends AsyncTask<String, Void, Integer> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(FeedbackTab.this);
    		dialog.setMessage(getString(R.string.feedback_sendingFeedback));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(Integer execResult) {
    		dialog.dismiss();
    		
            setResult(RESULT_OK);
			finish();
    	}
			
		@Override
		protected Integer doInBackground(String... feedbackDetails) {
    		RemoteFunctions rf = RemoteFunctions.INSTANCE;
    		return 
    				rf.addFeedback(feedbackDetails[0], 
    						feedbackDetails[1], feedbackDetails[2]);
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
}