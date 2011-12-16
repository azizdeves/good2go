package com.gdma.good2go;

import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.gdma.good2go.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CountMeIn extends Activity {

    private String mEventName;
    private String mEventDesc;
    private String mFbStatus;
	Facebook facebook = new Facebook("327638170583802"); //new facebook app instance;
	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.count_me_in);
		
	    /**GET DATA PASSED FROM COUNT-ME-IN-BUTTON in EVENT DETAILS*/
	    Bundle extras = getIntent().getExtras();
	    mEventName=(extras!=null)?extras.getString("name"):null;
	    mEventDesc=(extras!=null)?extras.getString("desc"):null;
	    
	    /**POPULATE VIEWS FROM EXTRAS*/
	    mFbStatus="This is awesome! I'm going to " + mEventDesc + ".";
	    
	    TextView eventName = (TextView) findViewById(R.id.eventname);
	    TextView fbStatus = (TextView) findViewById(R.id.fbstatus);
	          
	    eventName.setText(mEventName);
	    fbStatus.setText(mFbStatus);
	    
        
        facebook.authorize(this, new String[] { "email", "offline_access", "publish_checkins", "publish_stream" },
        		
        		new DialogListener() {
  //      	           @Override
        	           public void onComplete(Bundle values) {
        	        	   updateStatus(values.getString(facebook.getAccessToken()));
        	           }

        	           private void updateStatus(String accessToken) {
        	        	   // TODO Auto-generated method stub
        	        	   try{
        	        		   Bundle bundle = new Bundle();
        	        		   bundle.putString("message", mFbStatus);
            	        	   bundle.putString(Facebook.TOKEN, accessToken);
            	        	   String response = facebook.request("me/feed",bundle,"POST");
            	        	   Log.d("UPDATE RESPONSE", ""+response);
        	        	   }
        	        	   catch(MalformedURLException e){
        	        		   Log.e("MALFORMED URL",""+e.getMessage());
        	        	   }
        	        	   catch (IOException e){
        	        		   Log.e("IOEX",""+e.getMessage());
        	        	   }
        	        	   
        	        	   
        	           }

//      	           @Override
        	           public void onFacebookError(FacebookError error) {}

  //      	           @Override
        	           public void onError(DialogError e) {}

  //      	           @Override
        	           public void onCancel() {}
        	      }
        	);
        
        

		
		
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
	    		Toast.makeText(view.getContext(), "Thanks for being awesome! We'll love to see you at: " +mEventName+ ".",
				Toast.LENGTH_LONG).show();
	    		
	    		setResult(Activity.RESULT_OK);
	    		finish();
	        }
	    });

	}
	
    //added - FB
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);	

    	facebook.authorizeCallback(requestCode, resultCode, data);
    }



}
