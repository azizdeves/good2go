package com.gdma.good2go.ui;
import java.util.ArrayList;

import com.gdma.good2go.Event;
import com.gdma.good2go.R;

import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RestClient;
import com.gdma.good2go.utils.AppPreferencesFilterDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.FiltersUtil;


import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar;



public class FilterTab extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener{
	private  static final String TAG = "Filter";
	private  SeekBar mDurationSeekBar;
	private  SeekBar mRadiusSeekBar;

	private  TextView mDurationTrackingText;
	private  TextView mRadiusTrackingText;
	  
//	private  static Bundle b =null;
	private  EventsDbAdapter mDbHelper;
	private  int mDuration=0;
	private  int mRadius=0;
	private  String caller="";
	private  ToggleButton togglebutton_Animals;
	private  ToggleButton togglebutton_Children;
	private  ToggleButton togglebutton_Disabled;
	private  ToggleButton togglebutton_Env;
	private  ToggleButton togglebutton_Elderly;
	private  ToggleButton togglebutton_Special;
	private AppPreferencesFilterDetails mFilterPrefs;

  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.filter);

      mFilterPrefs = new AppPreferencesFilterDetails(this);
      
      Bundle b = getIntent().getExtras();
	  caller =(b!=null)?b.getString("caller"):null;
	  
	  
	  mDurationSeekBar =		 (SeekBar)findViewById(R.id.durationSeek);
	  mDurationTrackingText =	 (TextView)findViewById(R.id.durationSeekVal);
	  mRadiusTrackingText =  	 (TextView)findViewById(R.id.radiusSeekVal);
      mRadiusSeekBar =			 (SeekBar)findViewById(R.id.radiusSeek);
 	  togglebutton_Animals = 	 (ToggleButton) findViewById(R.id.animalsFilterButton);
 	  togglebutton_Children =	 (ToggleButton) findViewById(R.id.childrenFilterButton);
 	  togglebutton_Disabled = 	 (ToggleButton) findViewById(R.id.disabledFilterButton);
 	  togglebutton_Env = 		 (ToggleButton) findViewById(R.id.envFilterButton);
 	  togglebutton_Elderly = 	 (ToggleButton) findViewById(R.id.elderlyFilterButton);
 	  togglebutton_Special =  	 (ToggleButton) findViewById(R.id.specialFilterButton);


 	  if(mFilterPrefs.isUserFiltersExist()){
	      togglebutton_Animals.setChecked(mFilterPrefs.getAnimal());
	 	  togglebutton_Children.setChecked(mFilterPrefs.getChildren());
	 	  togglebutton_Disabled.setChecked(mFilterPrefs.getDisabled());
	 	  togglebutton_Env.setChecked(mFilterPrefs.getEnv());
	 	  togglebutton_Elderly.setChecked(mFilterPrefs.getElderly());
	 	  togglebutton_Special.setChecked(mFilterPrefs.getSpecial());
	 	  mDurationSeekBar.setProgress(mFilterPrefs.getDuration());
	 	  mRadiusSeekBar.setProgress(mFilterPrefs.getRadius());
	  }
 	  else{
 		  togglebutton_Animals.setChecked(mFilterPrefs.getAnimalDefault());
	 	  togglebutton_Children.setChecked(mFilterPrefs.getChildrenDefault());
	 	  togglebutton_Disabled.setChecked(mFilterPrefs.getDisabledDefault());
	 	  togglebutton_Env.setChecked(mFilterPrefs.getEnvDefault());
	 	  togglebutton_Elderly.setChecked(mFilterPrefs.getElderlyDefault());
	 	  togglebutton_Special.setChecked(mFilterPrefs.getSpecialDefault());
	 	  mDurationSeekBar.setProgress(mFilterPrefs.getDurationDefault());
	 	  mRadiusSeekBar.setProgress(mFilterPrefs.getRadiusDefault());
	  }
	  mDuration = mDurationSeekBar.getProgress();
	  String minutes = mDuration%60==0 ? "00" : Integer.toString(mDuration%60);
	  mDurationTrackingText.setText(Integer.toString(mDuration/60)+":"+minutes+"h");
	  mRadius=mRadiusSeekBar.getProgress();
 	  mRadiusTrackingText.setText(Integer.toString(mRadius)+"km");

	  mDurationSeekBar.setOnSeekBarChangeListener(this);
      mRadiusSeekBar.setOnSeekBarChangeListener(this);
    
  }
  
	

  public void getEventsWithFilters(View view){
 	

//		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
//		client.AddParam("action", "getEvents");

		/*
		
		togglebutton_Animals.value = Event.VolunteeringWith.ANIMALS.name();
		togglebutton_Children.value = Event.VolunteeringWith.CHILDREN.name();
		.
		.
		.
		Etc. (where value is something you need to come up with. the right-hand side of the equations is exactly as it should be.)
		
		boolean isFirst = true;
		String types = new String("");
		
		for (ToggleButton tb : togglebuttons){
			if (tb.isChecked()){
				if (!isFirst)
					types += ",";
				else
					isFirst = false;
				
				types += tb.value; //Again, the value thingie you need to come up with.
			}
		}
		
		if (!isFirst)
			client.addParam("type",types);
		
		/*end of mockup*/
		
		
		
/*		
		for (ToggleButton tb : tbArray) {
			if(tb.isChecked()){ 
				client.AddParam((String)tb.getTextOff(), "true");
				debugString=debugString.concat((String)tb.getTextOff());
				types[i]=(String)tb.getTextOff();
				i++;
			}
	*/			
//		}
//		
//		try{
//		client.Execute(1); //1 for GET
//		}
//		catch (Exception e){
//			setResult(RESULT_CANCELED);
//			finish();
//		}
//
//		js = client.getResponse();
//		js = js.trim();
//		js = js.replaceAll("good2goserver", "good2go");
//		Bundle b = new Bundle();
//		b.putString("events", js);
//		Intent i = new Intent();
//		i.putExtra("events", "to be replaced with bundle b");

//		setResult(RESULT_OK, i);

//		for (ToggleButton tb : tbArray) {
//			if(tb.isChecked()){ 
//				debugString=debugString.concat((String)tb.getTextOff());
//				types[i]=(String)tb.getTextOff();
//				i++;
//			}
//		}

		Bundle tempBundle = new Bundle();
		
		if(togglebutton_Animals.isChecked())
			tempBundle.putBoolean("animals",true);		
		else
			tempBundle.putBoolean("animals",false);			
		if(togglebutton_Children.isChecked())
			tempBundle.putBoolean("children",true);
		else
			tempBundle.putBoolean("children",false);
		if(togglebutton_Env.isChecked())
			tempBundle.putBoolean("environment",true);
		else
			tempBundle.putBoolean("environment",false);
		if(togglebutton_Elderly.isChecked())
			tempBundle.putBoolean("elderly",true);	
		else
			tempBundle.putBoolean("elderly",false);
		if(togglebutton_Disabled.isChecked())
			tempBundle.putBoolean("disabled",true);	
		else
			tempBundle.putBoolean("disabled",false);	
		if(togglebutton_Special.isChecked())
			tempBundle.putBoolean("special",true);	
		else
			tempBundle.putBoolean("special",false);	
		
		
		mFilterPrefs.saveFilterPrefs(tempBundle.getBoolean("animals"), tempBundle.getBoolean("children"),
									tempBundle.getBoolean("disabled"),tempBundle.getBoolean("environment"), 
									tempBundle.getBoolean("elderly"), tempBundle.getBoolean("special"),
									mRadius, mDuration);

		
		mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
		Cursor eventsCursor = mDbHelper.fetchEventByFilters(FiltersUtil.getArrayOfFilteredTypes(mFilterPrefs), mRadius, mDuration);
		if (eventsCursor.getCount()==0){
			Toast noDataAlert=Toast.makeText(this, "There are no search results that match your criteria", Toast.LENGTH_LONG);
			noDataAlert.show();
			return;
		}
		
		
		if (caller!= null && caller.compareTo("MainTab")==0){
			Intent newIntent = new Intent(this, MapTab.class);
		//	tempBundle.putString("action","MainTab");
		//	newIntent.putExtras(tempBundle);
			startActivity(newIntent);
		}
		
		else{
			Intent intent = new Intent();
			intent.putExtras(tempBundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
  

  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
//	        mProgressText.setText(getString(R.string.mDuration));
     

		if (seekBar.getId()==R.id.durationSeek){
			mDuration = progress;
			int hours=mDuration/60;
			int min = mDuration%60;
			mDurationTrackingText.setText(Integer.toString(hours)+":"+Integer.toString(min)+"h");
		}
		if(seekBar.getId()==R.id.radiusSeek){
			mRadius = progress;
			mRadiusTrackingText.setText(Integer.toString(mRadius)+"km");
		}
		else{return;}
	}
	 
  public void onStartTrackingTouch(SeekBar seekBar) {
      //mTrackingText.setText(getString(R.string.seekbar_tracking_on));
  }

  public void onStopTrackingTouch(SeekBar seekBar) {
      //mTrackingText.setText(getString(R.string.seekbar_tracking_off));
  }
    
    
    
    

	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
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
        	
        case R.id.menu_go:	
        	/**TODO add the go button implementation*/
//        	newIntent = new Intent(this, MainActivity.class);
//        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	startActivity(newIntent);	
        	getEventsWithFilters(null);
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
}