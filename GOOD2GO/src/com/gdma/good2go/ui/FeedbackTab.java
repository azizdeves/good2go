package com.gdma.good2go.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdma.good2go.Karma;
import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.UsersUtil;

public class FeedbackTab extends ActionBarActivity{
	private Context mContext;	
	private String mUserFirstName = "Dina";
	private String mUserName = "496351";
	private String mEventDesc;
	private String mEventName;
	private Long mEventId;
	private int mPoints;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);

		TextView badgeDetails1 = (TextView) findViewById(R.id.badgesDetails1_confirmationView);
		TextView badgeDetails2= (TextView) findViewById(R.id.badgesDetails2_confirmationView);
		Bundle extras = getIntent().getExtras();
	    mEventName=(extras!=null)?extras.getString("eventname"):null;
	    mEventDesc=(extras!=null)?extras.getString("desc"):null;
	    String EvenIdString=(extras!=null)?extras.getString("event_id"):null;
	    mEventId = Long.valueOf(EvenIdString);
	    mPoints=(extras!=null)?extras.getInt("points"):0;
	  	
	    String currentBadge=Karma.Badge.getMyBadge(mPoints).getName();
	  	long newPoints=UsersUtil.remote_getUsersKarma(mUserName)+mPoints;
	  	String newBadge = Karma.Badge.getMyBadge(newPoints).getName();
	  	if(newBadge.compareTo(currentBadge)!=0){
	  		badgeDetails1.setText("Whooo! You've just earned a new badge!!!");
	  		badgeDetails2.setText(newBadge);
	  	}
	  	else{
	  		badgeDetails1.setText("Your current badge is:");
	  		badgeDetails2.setText(currentBadge);
	  	}
	    
	    
	    TextView eventDetails = (TextView) findViewById(R.id.details_confirmationView);
	    	eventDetails.setText("We got you for "+mEventDesc);
	    TextView title = (TextView) findViewById(R.id.title_confirmationView);
	    	title.setText("Thank you for being awesome "+mUserFirstName +"!");
	    	
	    TextView txpoints= (TextView) findViewById(R.id.points_confirmationView);
	    	txpoints.setText("+"+Integer.toString(mPoints));
	    
			
		Button buttonDone = (Button) findViewById(R.id.DoneConfirmationViewButton);
		buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent();
                i.putExtra("sender", "confirmation");
                i.putExtra(EventsDbAdapter.KEY_EVENTID, Long.valueOf(mEventId));
                setResult(RESULT_OK, i);
    			finish();
             
	            //startActivity(i);
            }
        });
			    	
	    
	}
	

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);	
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
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
