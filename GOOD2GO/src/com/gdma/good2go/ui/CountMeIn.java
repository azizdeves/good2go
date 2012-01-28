package com.gdma.good2go.ui;

import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.facebook.DialogError;
import com.gdma.good2go.facebook.Facebook;
import com.gdma.good2go.facebook.Facebook.DialogListener;
import com.gdma.good2go.facebook.FacebookError;
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.EventsDbAdapter;

public class CountMeIn extends ActionBarActivity {
	private static final String TAG = "CountMeIn";
	
	private static final int POINTS_PER_HOUR = 1000;
	private static final String POST_STATUS = "POST_STATUS";

	private String mEventName;
    private String mEventDesc;
    private String mFbStatus;
    private String mFacebookToken;
    private Long mEventId;
    private String mOccurenceKey;
    private String mEventDuration;
	Facebook facebook = new Facebook("327638170583802"); //new facebook app instance;
    //RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    private int mAuthAttempts = 0;
    ProgressDialog dialog;
    private String graph_or_fql;
    private String mUsername;
    private Boolean shareToggleBool = true;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.count_me_in);
		
	    /**GET DATA PASSED FROM COUNT-ME-IN-BUTTON in EVENT DETAILS*/
	    Bundle extras = getIntent().getExtras();
	    if(extras!=null){
		    mEventName=extras.getString("eventname");
		    mEventDesc=extras.getString("desc");
		    String EvenIdString=extras.getString("event_id");
		    mEventId = Long.valueOf(EvenIdString);
		    mOccurenceKey = extras.getString("occurence_key");
		    mUsername=extras.getString("userName");
		    mEventDuration = extras.getString(EventsDbAdapter.KEY_EVENT_DURATION);
		}
    
	    
	    
	    
	    /**POPULATE VIEWS FROM EXTRAS*/
	    mFbStatus="This is awesome! I'm going to " + mEventName.toLowerCase() + ".";
	    
	    TextView eventName = (TextView) findViewById(R.id.eventname);
	    TextView fbStatus = (TextView) findViewById(R.id.fbstatus);
	          
	    eventName.setText(mEventName);
	    fbStatus.setText(mFbStatus);
	    
	    
	    final Button buttonFBToggle = (Button) findViewById(R.id.fbposttoggleBtn);
	    buttonFBToggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				shareToggleBool = !shareToggleBool;
				//showToast("share = " + shareToggleBool.toString());
			}
		});
	    
	    
	    
	    
/**************************************************************/	    
/**************NOTICE THE 'addKarma' SECTION*******************/
/**************************************************************/		
		
//	    final Button buttonFBInvite = (Button) findViewById(R.id.fbinvite);
//		   
//	    buttonFBInvite.setOnClickListener(new View.OnClickListener() {
//	        public void onClick(View view) {
//	           // mSoundManager.playSound(3);
//	            /*Intent newIntent = new Intent(view.getContext(), 
//	                            CountMeIn.class);
//	            startActivityForResult(newIntent, 1);*/
//	    		Toast.makeText(view.getContext(), "onButtonClick FB Invite",
//				Toast.LENGTH_LONG).show();
	    		/***Adding point for inviting a friend***/
//	    		mClient = new RestClient("http://good-2-go.appspot.com/good2goserver");
//	    		PointsUtil.remote_addKarma(mUsername, PointsUtil.INVITE_A_FRIEND, client);

	    
//	        }
//	    });
//	    
//	    
	    final Button buttonCountMeIn = (Button) findViewById(R.id.countmeinbtn2);
		   
	    buttonCountMeIn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	//async register to event
	        	new registerUserToEvent().execute(mUsername,mOccurenceKey);
	        	
	        	Bundle extraInfo = new Bundle();
	            extraInfo.putString("eventname", mEventName);
	            extraInfo.putString("desc", mEventDesc);
	            extraInfo.putString("event_id", mEventId.toString());
	            int points = getDeservedPoints(); 
	            extraInfo.putInt("points", points); 
	            
	            mAuthAttempts = 0;
	        	
	        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CountMeIn.this);
	        	mFacebookToken = prefs.getString("FacebookToken", "");
	        	/*if (mFacebookToken.equals("")){
	        		showToast("trying to get FB access token via getAccessToken");
	        		mFacebookToken = facebook.getAccessToken();
	        		showToast(mFacebookToken);
	        	}*/
	        	TextView fbStatus = (TextView) findViewById(R.id.fbstatus);
	    	    mFbStatus=fbStatus.getText().toString();

	    	    if((mFacebookToken.equals("")) && shareToggleBool){
	        		fbAuthAndPost(mFbStatus, view.getContext(), extraInfo);
	        	}
	        	else{
	        		if (!mFacebookToken.equals("") && shareToggleBool){
	        			updateStatus(mFacebookToken);
	        		}
	        		Intent newIntent = new Intent(view.getContext(), Confirmation.class);
		            newIntent.putExtras(extraInfo);
		            startActivityForResult(newIntent, ActivitysCodeUtil.GET_CONFIRMATION);
	        	}
	            
	            
	            	        }
	    });

	}
	
	
	private void saveFBToken(String token, long tokenExpires){
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    prefs.edit().putString("FacebookToken", token).commit();
	}
	
	
	private void fbAuthAndPost(final String message, final Context view, final Bundle extraInfo){
        facebook.authorize(this, new String[] { "email", "offline_access", "publish_checkins", "publish_stream", "read_friendlists" },
        		new DialogListener() {
        				@Override
        				public void onComplete(Bundle values) {
        					Log.d(this.getClass().getName(), "Facebook.authorize Complete: ");
        					saveFBToken(facebook.getAccessToken(), facebook.getAccessExpires());
        					updateStatus(values.getString(facebook.getAccessToken()));
        					
        					/** TODO add karma async*/
        					RemoteFunctions.INSTANCE.addUserKarma(mUsername, POST_STATUS);
        					
        					
        					Intent newIntent = new Intent(view, Confirmation.class);
        		            newIntent.putExtras(extraInfo);
        		            startActivityForResult(newIntent, ActivitysCodeUtil.GET_CONFIRMATION);
        				}

        				@Override
        				public void onFacebookError(FacebookError error) {
        					Log.d(this.getClass().getName(), "Facebook.authorize Error: "+error.toString());
        				}
        				@Override
        				public void onError(DialogError e) {
        					Log.d(this.getClass().getName(),"Facebook.authorize DialogError: "+e.toString());
        				}

        				@Override
        				public void onCancel() {
        					Log.d(this.getClass().getName(),"Facebook authorization canceled");
        				}
                 }
            );
        
        
	}


	

	private void fbAuthAndPost(final String message){
        facebook.authorize(this, new String[] { "email", "offline_access", "publish_checkins", "publish_stream", "read_friendlists" },
        		new DialogListener() {
        				@Override
        				public void onComplete(Bundle values) {
        					Log.d(this.getClass().getName(), "Facebook.authorize Complete: ");
        					saveFBToken(facebook.getAccessToken(), facebook.getAccessExpires());
        					updateStatus(values.getString(facebook.getAccessToken()));
        					/** TODO add karma async*/
        					RemoteFunctions.INSTANCE.addUserKarma(mUsername, POST_STATUS);
        				}

        				@Override
        				public void onFacebookError(FacebookError error) {
        					Log.d(this.getClass().getName(), "Facebook.authorize Error: "+error.toString());
        				}
        				@Override
        				public void onError(DialogError e) {
        					Log.d(this.getClass().getName(),"Facebook.authorize DialogError: "+e.toString());
        				}

        				@Override
        				public void onCancel() {
        					Log.d(this.getClass().getName(),"Facebook authorization canceled");
        				}
                 }
            );
        
        
	}


	
	
	
	
    //added - FB
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);	

    	facebook.authorizeCallback(requestCode, resultCode, data);
		if (resultCode==Activity.RESULT_OK)
		{
	    	if(requestCode==ActivitysCodeUtil.GET_CONFIRMATION){
	    		String sender= data.getStringExtra("sender");
	    		if(sender.compareTo("Confirmation")==0){
	    			Intent i = new Intent();
	                i.putExtra("sender", "Confirmation");
	                i.putExtra(EventsDbAdapter.KEY_EVENTID, Long.valueOf(mEventId));
	                setResult(RESULT_OK, i);
	    			finish();
	             }
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
    
    
    private void updateStatus(String accessToken) {
 	   try{
 		   Bundle bundle = new Bundle();
 		   bundle.putString("message", mFbStatus);
     	   bundle.putString(Facebook.TOKEN, accessToken);
     	   String response = facebook.request("me/feed",bundle,"POST");
     	   Log.d("UPDATE RESPONSE", ""+response);
     	   if(response.indexOf("OAuthException")>-1){
     		   if (mAuthAttempts==0){
     			   mAuthAttempts++;
     			   fbAuthAndPost(mFbStatus);
     		   }
     		   else{
     			   showToast("OAuthException:");
     		   }
     	   }
 	   }
 	   catch(MalformedURLException e){
 		   Log.e("MALFORMED URL",""+e.getMessage());
 		   showToast("MalformedURLException:"+e.getMessage());
 	   }
 	   catch (IOException e){
 		   Log.e("IOEX",""+e.getMessage());
 		   showToast("IOException:"+e.getMessage());
 	   }
 	   
 	   /*
 	   String s = facebook.getAccessToken()+"\n";
 	   s+= String.valueOf(facebook.getAccessExpires())+"\n";
 	   s+="Now:"+String.valueOf(System.currentTimeMillis())+"\n";
 	   tv1.setText(s); */
 	  
 	   setResult(Activity.RESULT_OK);
 	   finish();
    }
    
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    
    
	private int getDeservedPoints() {
		String hour_s=null,min_s=null;
		int hour=0, min=0;
		
		if (mEventDuration.contains("min"))
		{
			String[] time = mEventDuration.split(" ");
			hour_s = time[0].replace("h", "");
			min_s = time [1].replace("min", "");
		}
		else
		{
			if (mEventDuration.contains("h "))
			{
				hour_s = mEventDuration.replace("h ", "");
			}
			
			if (mEventDuration.contains("min"))
			{
				hour_s = mEventDuration.replace("min", "");
			}
		}
		
		if (hour_s!=null)
		{
			hour = Integer.parseInt(hour_s);
		}
		
		if (min_s!=null)
		{
			min = Integer.parseInt(min_s);
		}
		
		float totalHourDur = hour + min / 60;
		
		return (int) totalHourDur * POINTS_PER_HOUR;
	}
    
    
    
    /**THREADS*/		
    private class registerUserToEvent extends AsyncTask<String, Void, Integer> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(CountMeIn.this);
    		dialog.setMessage(getString(R.string.count_me_in_registering));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(Integer execResult) {
    		dialog.dismiss();
    	}
			
		@Override
		protected Integer doInBackground(String... userAndOccDetails) {
    		RemoteFunctions rf = RemoteFunctions.INSTANCE;
    		
    		return rf.registerUserToEvent(userAndOccDetails[0], userAndOccDetails[1]);
			}
	
    }
    

    
}
