package com.gdma.good2go;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gdma.good2go.Event.VolunteeringWith;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;




public class MapTab extends MapActivity {
	
	
	private static final int GET_FILTERED_EVENTS = 10;
	private EventsDbAdapter mDbHelper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        final Button buttonFilterEvents = (Button) findViewById(R.id.FilterEventsMapViewButton);
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
//        if(eventsCursor!=null&&!eventsCursor.isClosed()){
//        	mDbHelper.close();
//        }
        
        //GeoPoint gp=new GeoPoint (32074938,34775591);

        //itemizedoverlay.addOverlay(new EventOverlayItem(gp, "adi", "anna", "230"));
             
        /**SHOW ON MAP*/ 
        //Add the G2GItemizedOverlay to the MapView
        mapOverlays.add(itemizedoverlay);
        
		final MapController mc = mapView.getController();
		mc.animateTo(new GeoPoint (32069156,34774003));
		mc.setZoom(16);
        
    }

    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		 if (requestCode == GET_FILTERED_EVENTS) {
			if(resultCode==RESULT_OK){
				Bundle bundleResult = data.getExtras();
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
					if(bundleResult.getString("children")=="1"){
						types[i]="children";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("disabled")=="1"){
						types[i]="disabled";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("elderly")=="1"){
						types[i]="elderly";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("environment")=="1"){
						types[i]="environment";
						i++;
						arrSize++;
					}
					if(bundleResult.getString("special")=="1"){
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
//		        if(eventsCursor!=null&&!eventsCursor.isClosed()){
//		        	mDbHelper.close();
//		        }
		        
		        //GeoPoint gp=new GeoPoint (32074938,34775591);

		        //itemizedoverlay.addOverlay(new EventOverlayItem(gp, "adi", "anna", "230"));
		             
		        /**SHOW ON MAP*/ 
		        //Add the G2GItemizedOverlay to the MapView
		        mapOverlays.add(itemizedoverlay);
		        
				final MapController mc = mapView.getController();
				mc.animateTo(new GeoPoint (32069156,34774003));
				mc.setZoom(16);
		        
				
			}
			if(resultCode==RESULT_CANCELED){
//				Toast debugging=Toast.makeText(this, "failed", Toast.LENGTH_LONG);
//				debugging.show();				
			}		
		 }
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

    	
    	GeoPoint gp= new GeoPoint(gplat,gplong); 
    	EventOverlayItem overlayitem = new EventOverlayItem(gp, name, info, rowID);

    	return overlayitem;	
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public void getFilterScreen(View view){
    	Intent myIntent = new Intent(view.getContext(),FilterTab.class);
    	startActivityForResult(myIntent, GET_FILTERED_EVENTS);		
	}
}




















