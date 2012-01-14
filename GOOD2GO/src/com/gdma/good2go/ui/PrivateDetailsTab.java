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

public class PrivateDetailsTab extends ActionBarActivity{
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
			setContentView(R.layout.private_details);
		    
				
			Button buttonDone = (Button) findViewById(R.id.DonePrivateDetailsButton);
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
