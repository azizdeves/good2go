package com.gdma.good2go;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CountMeIn extends Activity {

    private String mEventName;
    private String mEventDesc;
    private String mFbStatus;
	
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
	    mFbStatus="Adi Anna is awesome! She's going to " + mEventDesc + ".";
	    
	    TextView eventName = (TextView) findViewById(R.id.eventname);
	    TextView fbStatus = (TextView) findViewById(R.id.fbstatus);
	          
	    eventName.setText(mEventName);
	    fbStatus.setText(mFbStatus);
	    
	    
		
		
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
	


}
