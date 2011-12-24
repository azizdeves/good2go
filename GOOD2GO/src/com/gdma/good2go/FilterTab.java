package com.gdma.good2go;


import com.gdma.good2go.communication.RestClient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.SeekBar;

public class FilterTab extends Activity implements SeekBar.OnSeekBarChangeListener{
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
//        TextView textview = new TextView(this);
//        textview.setText("This is the Search tab");
//        setContentView(textview);
        
        durationSeekBar = (SeekBar)findViewById(R.id.durationSeek);
        durationSeekBar.setOnSeekBarChangeListener(this);

        radiusSeekBar = (SeekBar)findViewById(R.id.radiusSeek);
        radiusSeekBar.setOnSeekBarChangeListener(this);
         
    	durationTrackingText = (TextView)findViewById(R.id.durationSeekVal);
    	radiusTrackingText = (TextView)findViewById(R.id.radiusSeekVal);
    	
//        mProgressText = (TextView)findViewById(R.id.progress);
//        mTrackingText = (TextView)findViewById(R.id.tracking);        
//       
    }
    
	

    public void getEventsWithFilters(View view){
   	
		final ToggleButton togglebutton_Children = (ToggleButton) findViewById(R.id.childrenFilterButton);
		final ToggleButton togglebutton_Env = (ToggleButton) findViewById(R.id.envFilterButton);
		final ToggleButton togglebutton_Animals = (ToggleButton) findViewById(R.id.animalsFilterButton);
		ToggleButton tbArray[]={togglebutton_Children, togglebutton_Env, togglebutton_Animals};
		
		String debugString = "";
		String js = null; // hold the response from server
		String types[] = {"","",""};
		int i=0;

//		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
//		client.AddParam("action", "getEventsWithFilters");
//		for (ToggleButton tb : tbArray) {
//			if(tb.isChecked()){ 
//				client.AddParam((String)tb.getTextOff(), "true");
//				debugString=debugString.concat((String)tb.getTextOff());
//				types[i]=(String)tb.getTextOff();
//				i++;
//			}
//				
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
		if(togglebutton_Children.isChecked())
			b.putString("animals","1");		
		else
			b.putString("animals","0");			
		if(togglebutton_Children.isChecked())
			b.putString("children","1");
		else
			b.putString("children","0");
		if(togglebutton_Children.isChecked())
			b.putString("disabled","1");
		else
			b.putString("disabled","0");
		if(togglebutton_Children.isChecked())
			b.putString("elderly","1");	
		else
			b.putString("elderly","0");
		if(togglebutton_Children.isChecked())
			b.putString("environment","1");	
		else
			b.putString("environment","0");	
		if(togglebutton_Children.isChecked())
			b.putString("special","1");	
		else
			b.putString("special","0");	
		
		b.putInt("durationInMInutes", duration);
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


}