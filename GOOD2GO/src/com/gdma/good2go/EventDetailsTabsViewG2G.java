package com.gdma.good2go;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class EventDetailsTabsViewG2G extends TabActivity {
	
    TabHost mTabHost;
  
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.event_details);

	    mTabHost = getTabHost();

	    
	    mTabHost.addTab(mTabHost.newTabSpec("tab_about_event").setIndicator("About").setContent(R.id.textview1));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_map_event").setIndicator("Map").setContent(R.id.textview2));
	    mTabHost.addTab(mTabHost.newTabSpec("tab_npo").setIndicator("Who").setContent(R.id.textview3));
	    
	    mTabHost.setCurrentTab(0);
	    
	    
	    final Button buttonCountMeIn = (Button) findViewById(R.id.countmeinbtn);
	   
	    buttonCountMeIn.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	           // mSoundManager.playSound(3);
	            Intent newIntent = new Intent(view.getContext(), 
	                            CountMeIn.class);
	            startActivityForResult(newIntent, 1);
	    		//Toast.makeText(view.getContext(), "onButtonClick Register",
				//Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
	

}