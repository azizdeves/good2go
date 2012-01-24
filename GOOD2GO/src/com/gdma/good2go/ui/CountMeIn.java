package com.gdma.good2go.ui;

import com.gdma.good2go.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;


import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.communication.RestClient;
import com.gdma.good2go.facebook.DialogError;
import com.gdma.good2go.facebook.Facebook;
import com.gdma.good2go.facebook.FacebookError;
import com.gdma.good2go.facebook.Facebook.DialogListener;
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.PointsUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CountMeIn extends ActionBarActivity {
	private static final String TAG = "CountMeIn";

	private String mEventName;
    private String mEventDesc;
    private String mFbStatus;
    private String mFacebookToken;
    private Long mEventId;
    private String mOccurenceKey;
	Facebook facebook = new Facebook("327638170583802"); //new facebook app instance;
    RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    private int mAuthAttempts = 0;
    ProgressDialog dialog;
    private String graph_or_fql;
    private String mUsername;
    private Boolean shareToggleBool = true;
 
    private RestClient mClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
	        	
	        	

	           // mSoundManager.playSound(3);
	            /*Intent newIntent = new Intent(view.getContext(), 
	                            CountMeIn.class);
	            startActivityForResult(newIntent, 1);*/
	    		//Toast.makeText(view.getContext(), "Thanks for being awesome! We'll love to see you at: " +mEventName+ ".",
				//Toast.LENGTH_LONG).show();
	    		
	    		////setResult(Activity.RESULT_OK);
	    		////finish();
	        	

//	        	if(!mUsersPrefs.isUsernameExists()){
//	        		remote_registerUserForTheFirstTime(mUsername, mAge, mSex, mCity, mPhone, mEmail);
//	        	}
	        	//remote_registerToOccurrence(mUsername, Long.toString(mEventId));
	        	
	        	/**TODO add async task*/	        	
        		RemoteFunctions rf = RemoteFunctions.INSTANCE;
        		rf.registerUserToEvent(RemoteFunctions.REGISTER_USER_TO_EVENT, mUsername, mOccurenceKey);
	        	
	        	Bundle extraInfo = new Bundle();
	            extraInfo.putString("eventname", mEventName);
	            extraInfo.putString("desc", mEventDesc);
	            extraInfo.putString("event_id", mEventId.toString());
	            extraInfo.putInt("points", 100); 
	            /*MOR - WHY are the point static? If you can't get from dana - use her classes to calculate how much!*/
	            
	            
	            mAuthAttempts = 0;
	        	
	        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CountMeIn.this);
	        	mFacebookToken = prefs.getString("FacebookToken", "");
	        	/*if (mFacebookToken.equals("")){
	        		showToast("trying to get FB access token via getAccessToken");
	        		mFacebookToken = facebook.getAccessToken();
	        		showToast(mFacebookToken);
	        	}*/
	        	
	        	//ADI - added this to get new status in case user changed it
	    
	    	    TextView fbStatus = (TextView) findViewById(R.id.fbstatus);
	    	    mFbStatus=fbStatus.getText().toString();
	    	    
	    	    //ADI - added this to get new status in case user changed it
	        	
	        	
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
        					mClient = new RestClient("http://good-2-go.appspot.com/good2goserver");
        					PointsUtil.remote_addKarma(mUsername, PointsUtil.POST_STATUS, mClient);
        					
        					
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
        					mClient = new RestClient("http://good-2-go.appspot.com/good2goserver");
        					PointsUtil.remote_addKarma(mUsername, PointsUtil.POST_STATUS, mClient);
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
    	
    	if(requestCode==ActivitysCodeUtil.GET_CONFIRMATION){
    		String sender= data.getStringExtra("sender");
    		//String key_eventId=data.getStringExtra(EventsDbAdapter.KEY_EVENTID);
    		if(sender.compareTo("Confirmation")==0){
    			Intent i = new Intent();
                i.putExtra("sender", "Confirmation");
                i.putExtra(EventsDbAdapter.KEY_EVENTID, Long.valueOf(mEventId));
                setResult(RESULT_OK, i);
    			finish();
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
 	   // TODO Auto-generated method stub
 	   try{
 		   Bundle bundle = new Bundle();
 		   bundle.putString("message", mFbStatus);
     	   bundle.putString(Facebook.TOKEN, accessToken);
     	   String response = facebook.request("me/feed",bundle,"POST");
     	   Log.d("UPDATE RESPONSE", ""+response);
     	   //ADI - put the below in comment so it wont show in recording
     	   //showToast("Update process complete. Response:"+response);
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

    
}
