package com.gdma.good2go.ui;

import java.util.ArrayList;
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
import com.gdma.good2go.utils.ActivitysCodeUtil;
import com.gdma.good2go.utils.AppPreferencesFilterDetails;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.FiltersUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;




public class MapTab extends ActionBarMapActivity implements LocationListener {
	private static final String TAG = "Map";
	
	private EventsDbAdapter mDbHelper;
	private Button buttonFilterEvents;
	
	private  LocationManager mLocationManager;
	private  MapView mMap;  
	private  MapController mMapController;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	private  GeoPoint mUserGeoLocation = new GeoPoint((int)(32.067228*1E6),(int)(34.777650*1E6));
	private  UserItemizedOverlay mUserLocationOverlay;
	private Location mUserLocation;
	private AppPreferencesFilterDetails mFilterPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        initMapFields();
        

	    
	    
	    mFilterPrefs = new AppPreferencesFilterDetails(this);
	    showEventsOnMap();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
				
		setUserLocation();
    }
    
   
    private void initMapFields() {
        mMap = (MapView)this.findViewById(R.id.mapview);
        mMap.setBuiltInZoomControls(true);
        
        mMapController = mMap.getController();
        mMapController.setZoom(12);
        
        mLocationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		
	}
    
    
    private void setUserLocation() {
        
        Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNETWORK = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        
        mUserLocation = (locationGPS!=null) ? locationGPS : 
        	(locationNETWORK!=null) ? locationNETWORK : null;
        
        if (mUserLocation != null) 
        { 
        	onLocationChanged(mUserLocation);          
        }
        else
        {
         	Toast.makeText(mMap.getContext(), "Couldn't get accurate location.", Toast.LENGTH_SHORT).show();
        	displayUserLocation(); 
        }		
	}

    

    /** Determines whether one Location reading is better than the current Location fix
      * @param location  The new Location that you want to evaluate
      * @param currentBestLocation  The current Location fix, to which you want to compare the new one
      */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
             return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }

	@Override
    protected void onResume() {
      super.onResume();
      
      mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
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
		if (requestCode == ActivitysCodeUtil.GET_FILTERED_EVENTS) {
			showEventsOnMap();
			setUserLocation();
		
		}
	
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	
    @Override
	public void onLocationChanged(Location location) {
    	
    	if (isBetterLocation(location, mUserLocation) ||
    			(location.getLatitude()==mUserLocation.getLatitude() &&
    			location.getLongitude()==mUserLocation.getLongitude()))
    	{
    		mUserLocation=location;
    		int lat = (int)(mUserLocation.getLatitude() * 1E6);
    		int lon = (int)(mUserLocation.getLongitude() * 1E6);
    		
    		mUserGeoLocation = new GeoPoint(lat, lon);
    		
    		displayUserLocation();
    	}
	  }

    
	private void displayUserLocation() {
		List<Overlay> mapOverlays = mMap.getOverlays();
		mapOverlays.remove(mUserLocationOverlay);
		
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_home);
        
        mUserLocationOverlay = new UserItemizedOverlay(drawable);
        OverlayItem overlayitem = new OverlayItem(mUserGeoLocation, "This is you being awesome.", "");
        mUserLocationOverlay.addOverlay(overlayitem);
         
        mapOverlays.add(mUserLocationOverlay);
        
        mMapController.animateTo(mUserGeoLocation);		
	}
	
	


	@Override
	public void onProviderDisabled(String provider) {
		//setUserLocation();
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		setUserLocation();
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
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

    
    private void showEventsOnMap(){
		mDbHelper = new EventsDbAdapter(this);
	    mDbHelper.open();
		//Cursor eventsCursor = mDbHelper.fetchEventByFilters(FiltersUtil.getArrayOfFilteredTypes(mFilterPrefs), FiltersUtil.getFilterRadius(mFilterPrefs), FiltersUtil.getFilterDuration(mFilterPrefs));
	    Cursor eventsCursor = mDbHelper.fetchAllEvents();
	    if(mFilterPrefs.isUserFiltersExist())
			setRemoveFilterButton();
		else
			setFilterButton();
		
        List<Overlay> mapOverlays = mMap.getOverlays();
        mapOverlays.clear();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_map_event);
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

        mDbHelper.close();
   	
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
        	newIntent.putExtra("sender", "map");
        	newIntent.putExtra("lat", mUserGeoLocation.getLatitudeE6());
        	newIntent.putExtra("lon", mUserGeoLocation.getLongitudeE6());
        	
        	startActivity(newIntent);
        	break;
        	
        case android.R.id.home:	
        	newIntent = new Intent(this, MainActivity.class);
        	newIntent.putExtra("sender", TAG);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void setRemoveFilterButton(){
		buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
		buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_on2); 
		buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mFilterPrefs.setIsUserFiltersExist(false);
            	showEventsOnMap();
            	setUserLocation();
            }
        });
    }
    
    private void setFilterButton(){
        buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
        buttonFilterEvents.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_filter_off2); 
        buttonFilterEvents.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getFilterScreen(v);
            }
        });  	
    }
    
	public void getFilterScreen(View view){
    	Intent myIntent = new Intent(view.getContext(),FilterTab.class);
    	startActivityForResult(myIntent, ActivitysCodeUtil.GET_FILTERED_EVENTS);		
	}

    
}




















