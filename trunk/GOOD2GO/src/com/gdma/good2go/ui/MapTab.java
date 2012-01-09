package com.gdma.good2go.ui;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarMapActivity;
import com.gdma.good2go.ui.maputils.EventOverlayItem;
import com.gdma.good2go.ui.maputils.EventsItemizedOverlay;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;




public class MapTab extends ActionBarMapActivity {
	
	
	private static final int GET_FILTERED_EVENTS = 10;
	private EventsDbAdapter mDbHelper;
	private Button buttonFilterEvents;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Bundle bundleResult = FilterTab.getFilterBundle();
		if (bundleResult!=null)
			showFilteredEventsOnMap(bundleResult);
		else
			onCreateHelper();
       

        
    }

    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
//		Bundle bundleResult = data.getExtras();
		Bundle bundleResult = FilterTab.getFilterBundle();
		showFilteredEventsOnMap(bundleResult);
	
	}
	
    private EventOverlayItem MakeEventPoint(Cursor eventsCursor) {    
    	
    	Long rowID=eventsCursor.getLong
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENTID));
    	int gplat=Integer.parseInt(eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_GP_LAT)));
    	int gplong=Integer.parseInt(eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_GP_LONG)));
    	String name=eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENTNAME));
    	String info=eventsCursor.getString
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_SHORT_INFO));
    	int image = eventsCursor.getInt
    			(eventsCursor.getColumnIndexOrThrow(EventsDbAdapter.KEY_EVENT_IMAGE));

    	
    	GeoPoint gp= new GeoPoint(gplat,gplong); 
    	EventOverlayItem overlayitem = new EventOverlayItem(gp, name, info, rowID, image);

    	return overlayitem;	
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void getFilterScreen(View view){
		//buttonFilterEvents.setBackgroundDrawable(getResources().getDrawable( R.drawable.ic_filter_white));
    	Intent myIntent = new Intent(view.getContext(),FilterTab.class);
    	startActivityForResult(myIntent, GET_FILTERED_EVENTS);		
	}
	
	
	
	private void onCreateHelper(){
		
		Bundle bundleResult = getIntent().getExtras();
	    String action=(bundleResult!=null)?bundleResult.getString("action"):null;
	    
	    if (action!=null && action.compareTo("MainTab")==0){
	    	showFilteredEventsOnMap(bundleResult);
	    }
	    else{
			MapView mapView = (MapView) findViewById(R.id.mapview);
	        mapView.setBuiltInZoomControls(true);
	        
	        buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
	        buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_off); 
	        buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	getFilterScreen(v);
	            }
	        });
	
	        /**OVERLAY*/ 
	        
	        //All overlay elements on a map are held by the MapView, so when you want to add some, you have to get a list from the getOverlays() method.
	        List<Overlay> mapOverlays = mapView.getOverlays();
	        //instantiate the Drawable used for the map marker
	        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
	        //The constructor for G2GItemizedOverlay (your custom ItemizedOverlay) takes the Drawable in order to set the default marker for all overlay items
	        EventsItemizedOverlay itemizedoverlay = new EventsItemizedOverlay(drawable,mapView);
	        
	          
	        /**ADDING DB POINTS TO THE MAP OVERLAY*/
	        
		    mDbHelper = new EventsDbAdapter(this);
		    mDbHelper.open();
		    
	    	Cursor eventsCursor = mDbHelper.fetchAllEvents();
	        startManagingCursor(eventsCursor); //is this needed?
	        
	        int cnt=0;//BUG IN CURSOR
	        if(eventsCursor.moveToFirst()){
	        	do{
	        		EventOverlayItem point=MakeEventPoint(eventsCursor);
	        		itemizedoverlay.addOverlay(point);
	        		cnt++;//BUG IN CURSOR
	        		}
	        	while(eventsCursor.moveToNext()&&cnt!=6/*BUG IN CURSOR*/);
	        }
	//	        if(eventsCursor!=null&&!eventsCursor.isClosed()){
	//	        	mDbHelper.close();
	//	        }
	        
	        //GeoPoint gp=new GeoPoint (32074938,34775591);
	
	        //itemizedoverlay.addOverlay(new EventOverlayItem(gp, "adi", "anna", "230"));
	             
	        /**SHOW ON MAP*/ 
	        //Add the G2GItemizedOverlay to the MapView
	        mapOverlays.add(itemizedoverlay);
	        
			final MapController mc = mapView.getController();
			mc.animateTo(new GeoPoint (32069156,34774003));
			mc.setZoom(16);
	    }
	        
	}
	
	
	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Intent newIntent = null;
        switch (item.getItemId()) {
        
        case R.id.menu_list:
        	newIntent = new Intent(this, ListTab.class);
        	startActivity(newIntent);
        	break;
        	
        case android.R.id.home:	
        	newIntent = new Intent(this, MainActivity.class);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    
    private void showFilteredEventsOnMap(Bundle bundleResult){
		int i=0;
		int arrSize=0;
		int duration=-1;
		int radius=-1;
		String[] types= new String[bundleResult.size()-2];
		for (int j = 0; j < types.length; j++) {
			types[i]="";
		}
		
		if(bundleResult!=null){
			if(bundleResult.getString("animals").compareTo("1")==0){
				types[i]="animals";
				i++;
				arrSize++;
			}	
			if(bundleResult.getString("children").compareTo("1")==0){
				types[i]="children";
				i++;
				arrSize++;
			}
			if(bundleResult.getString("disabled").compareTo("1")==0){
				types[i]="disabled";
				i++;
				arrSize++;
			}
			if(bundleResult.getString("elderly").compareTo("1")==0){
				types[i]="elderly";
				i++;
				arrSize++;
			}
			if(bundleResult.getString("environment").compareTo("1")==0){
				types[i]="environment";
				i++;
				arrSize++;
			}
			if(bundleResult.getString("special").compareTo("1")==0){
				types[i]="special";
				i++;
				arrSize++;
			}
			if( bundleResult.getInt("durationInMinutes")>-1)
				duration= bundleResult.getInt("durationInMinutes");
			if( bundleResult.getInt("radius")>-1)
				radius =  bundleResult.getInt("radius");
		
		}
		/******DEBUGGING AREA******/
//				Toast debugging=Toast.makeText(this, Integer.toString(duration), Toast.LENGTH_SHORT);
//				debugging.show();	
//				debugging=Toast.makeText(this, Integer.toString(radius), Toast.LENGTH_SHORT);
//				debugging.show();	
//				for (int j = 0; j < arrSize; j++) {
//					debugging=Toast.makeText(this, types[j], Toast.LENGTH_SHORT);
//					debugging.show();					
//				}

		/***END OF DEBUGGING AREA***/	
		
		//Cursor eventsCursor = mDbHelper.fetchAllEvents();
		mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
		Cursor eventsCursor = mDbHelper.fetchEventByFilters(types, radius, duration);
//				Toast debugging=Toast.makeText(this, "#of results:"+Integer.toString(eventsCursor.getCount()), Toast.LENGTH_SHORT);
//				debugging.show();
		MapView mapView = (MapView) findViewById(R.id.mapview);
        List<Overlay> mapOverlays = mapView.getOverlays();
        mapOverlays.clear();
        Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        EventsItemizedOverlay itemizedoverlay = new EventsItemizedOverlay(drawable,mapView);

        startManagingCursor(eventsCursor); 
        
        int cnt=0;
        if(eventsCursor.moveToFirst()){
        	do{
        		EventOverlayItem point=MakeEventPoint(eventsCursor);
        		itemizedoverlay.addOverlay(point);
        		cnt++;
        		}
        	while(eventsCursor.moveToNext()&&cnt!=eventsCursor.getCount());
        }

        mapOverlays.add(itemizedoverlay);
        
		final MapController mc = mapView.getController();
		mc.animateTo(new GeoPoint (32069156,34774003));
		mc.setZoom(16);
		buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
		buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_on); 
		
		buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	onCreateHelper();
            }
        });
   	
    }
}




















