package com.gdma.good2go.ui;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.gdma.good2go.ui.maputils.UserItemizedOverlay;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.FiltersUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;




public class MapTab extends ActionBarMapActivity implements LocationListener {
	
	
	private static final int GET_FILTERED_EVENTS = 10;
	private EventsDbAdapter mDbHelper;
	private Button buttonFilterEvents;
	
	private  LocationManager mLocationManager;
	private  MapView mMap;  
	private  MapController mMapController;
	
	private  GeoPoint mUserGeoLocation = new GeoPoint((int)(32.067228*1E6),(int)(34.777650*1E6));
	private  UserItemizedOverlay mUserLocationOverlay;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        initMapFields();
                             
        Bundle bundleResult = FilterTab.getFilterBundle();
		if (bundleResult!=null)
			showFilteredEventsOnMap(bundleResult);
		else
			onCreateHelper();
		
		setUserLocation();
    }
   
    
    private void setUserLocation() {
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
        
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        if (location != null) 
        { 
        	onLocationChanged(location);          
        }
        else
        {
        	Toast.makeText(mMap.getContext(), "Is your GPS on? We couldn't get accurate location.", Toast.LENGTH_SHORT).show();
        	displayUserLocation(); 
        }		
	}


	@Override
    protected void onResume() {
      super.onResume();
      mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      mLocationManager.removeUpdates(this);
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
//		Bundle bundleResult = data.getExtras();
		Bundle bundleResult = FilterTab.getFilterBundle();
		showFilteredEventsOnMap(bundleResult);
		setUserLocation();
	
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	
    @Override
	public void onLocationChanged(Location location) {
    	
    	int lat = (int)(location.getLatitude() * 1E6);
    	int lon = (int)(location.getLongitude() * 1E6);
    	mUserGeoLocation = new GeoPoint(lat, lon);

    	displayUserLocation();   
	  }

    
	private void displayUserLocation() {
		List<Overlay> mapOverlays = mMap.getOverlays();
		mapOverlays.remove(mUserLocationOverlay);
		
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_maps_current_position);
        
        mUserLocationOverlay = new UserItemizedOverlay(drawable);
        OverlayItem overlayitem = new OverlayItem(mUserGeoLocation, "This is you being awesome.", "");
        mUserLocationOverlay.addOverlay(overlayitem);
         
        mapOverlays.add(mUserLocationOverlay);
        
        mMapController.animateTo(mUserGeoLocation);		
	}
	
	


	@Override
	public void onProviderDisabled(String provider) {
		setUserLocation();
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		setUserLocation();
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	
    private void initMapFields() {
        mMap = (MapView)this.findViewById(R.id.mapview);
        mMap.setBuiltInZoomControls(true);
        
        mMapController = mMap.getController();
        mMapController.setZoom(16);
        
        mLocationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		
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
	        
	        buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
	        buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_off); 
	        buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	getFilterScreen(v);
	            }
	        });
	
	        /**OVERLAY*/ 
	        
	        //All overlay elements on a map are held by the MapView, so when you want to add some, you have to get a list from the getOverlays() method.
	        List<Overlay> mapOverlays = mMap.getOverlays();
	        //instantiate the Drawable used for the map marker
	        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_maps_event_default);
	        //The constructor for G2GItemizedOverlay (your custom ItemizedOverlay) takes the Drawable in order to set the default marker for all overlay items
	        EventsItemizedOverlay itemizedoverlay = new EventsItemizedOverlay(drawable,mMap);
	        
	          
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
	        	/**TODO fix the bug*/
	        }
	        
	        /**SHOW ON MAP*/ 
	        mapOverlays.add(itemizedoverlay);
	    }
	        
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
			types = FiltersUtil.getArrayOfFiltersParams(bundleResult);
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
		//MapView mapView = (MapView) findViewById(R.id.mapview);
        List<Overlay> mapOverlays = mMap.getOverlays();
        mapOverlays.clear();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_maps_event_default);
        EventsItemizedOverlay itemizedoverlay = new EventsItemizedOverlay(drawable,mMap);

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
        
		buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
		buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_on); 
		
		buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Bundle b = null;
            	FilterTab.setFilterBundle(b);
            	onCreateHelper();
            	setUserLocation();
            }
        });
   	
    }




	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map, menu);
        
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
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
}




















