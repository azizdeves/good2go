	package com.gdma.good2go.ui;

	import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;

public class Confirmation extends ActionBarActivity{
	
	    String userFirstName = "Dina";
	    
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.confirmation);
			
			
		    Bundle extras = getIntent().getExtras();
		    String mEventName=(extras!=null)?extras.getString("name"):null;
		    String mEventDesc=(extras!=null)?extras.getString("desc"):null;
		    TextView eventDetails = (TextView) findViewById(R.id.details_confirmationView);
		    	eventDetails.setText("We got you for "+mEventDesc);
		    TextView title = (TextView) findViewById(R.id.title_confirmationView);
		    	title.setText("Thank you for being awesome "+userFirstName +"!");
			

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
