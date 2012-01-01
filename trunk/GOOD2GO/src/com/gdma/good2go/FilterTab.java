package com.gdma.good2go;
import com.example.android.actionbarcompat.ActionBarActivity;
import com.example.android.actionbarcompat.MainActivity;
import com.example.android.actionbarcompat.R;

import com.gdma.good2go.communication.RestClient;

import android.app.Activity;
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
	  SeekBar durationSeekBar;
	  SeekBar radiusSeekBar;

	  TextView durationTrackingText;
	  TextView radiusTrackingText;
	  
	  int duration=0;
	  int radius=0;
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.filter);
//      TextView textview = new TextView(this);
//      textview.setText("This is the Search tab");
//      setContentView(textview);
      
      durationSeekBar = (SeekBar)findViewById(R.id.durationSeek);
      durationSeekBar.setOnSeekBarChangeListener(this);

      radiusSeekBar = (SeekBar)findViewById(R.id.radiusSeek);
      radiusSeekBar.setOnSeekBarChangeListener(this);
       
  	durationTrackingText = (TextView)findViewById(R.id.durationSeekVal);
  	radiusTrackingText = (TextView)findViewById(R.id.radiusSeekVal);
  	
  	duration=durationSeekBar.getProgress();
  	radius=radiusSeekBar.getProgress();
  	

  	durationTrackingText.setText(Integer.toString(duration)+"h");
  	radiusTrackingText.setText(Integer.toString(radius)+"km");
  }
  
	

  public void getEventsWithFilters(View view){
 	
	  
		final ToggleButton togglebutton_Animals = (ToggleButton) findViewById(R.id.animalsFilterButton);
		final ToggleButton togglebutton_Children = (ToggleButton) findViewById(R.id.childrenFilterButton);
		final ToggleButton togglebutton_Disabled = (ToggleButton) findViewById(R.id.disabledFilterButton);
		final ToggleButton togglebutton_Env = (ToggleButton) findViewById(R.id.envFilterButton);
		final ToggleButton togglebutton_Elderly = (ToggleButton) findViewById(R.id.elderlyFilterButton);
		final ToggleButton togglebutton_Special = (ToggleButton) findViewById(R.id.specialFilterButton);
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getEvents");

		/*mockup FOR MOR/ADI TO USE
		
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


		Bundle b = new Bundle();
		
		if(togglebutton_Animals.isChecked())
			b.putString("animals","1");		
		else
			b.putString("animals","0");			
		if(togglebutton_Children.isChecked())
			b.putString("children","1");
		else
			b.putString("children","0");
		if(togglebutton_Env.isChecked())
			b.putString("environment","1");
		else
			b.putString("environment","0");
		if(togglebutton_Elderly.isChecked())
			b.putString("elderly","1");	
		else
			b.putString("elderly","0");
		if(togglebutton_Disabled.isChecked())
			b.putString("disabled","1");	
		else
			b.putString("disabled","0");	
		if(togglebutton_Special.isChecked())
			b.putString("special","1");	
		else
			b.putString("special","0");	
		
		b.putInt("durationInMinutes", duration);
		b.putInt("radius", radius);

		Intent intent = new Intent();
		intent.putExtras(b);
		setResult(RESULT_OK, intent);
		finish();
	}



  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
//	        mProgressText.setText(getString(R.string.duration));
     

		if (seekBar.getId()==R.id.durationSeek){
			duration = progress;
			durationTrackingText.setText(Integer.toString(duration)+"h");
		}
		if(seekBar.getId()==R.id.radiusSeek){
			radius = progress;
			radiusTrackingText.setText(Integer.toString(radius)+"km");
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
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        	
        case R.id.menu_go:	
        	/**TODO add the go button implementation*/
//        	newIntent = new Intent(this, MainActivity.class);
//        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
}