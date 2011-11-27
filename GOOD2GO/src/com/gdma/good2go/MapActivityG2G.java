package com.gdma.good2go;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MapActivityG2G extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        

       /**OVERLAY*/ 
        //All overlay elements on a map are held by the MapView, so when you want to add some, you have to get a list from the getOverlays() method.
        List<Overlay> mapOverlays = mapView.getOverlays();
        //instantiate the Drawable used for the map marker
        Drawable drawable = this.getResources().getDrawable(R.drawable.mappinlo);
        //The constructor for G2GItemizedOverlay (your custom ItemizedOverlay) takes the Drawable in order to set the default marker for all overlay items
        ItemizedOverlayG2G itemizedoverlay = new ItemizedOverlayG2G(drawable,this);
        
        
        /**ADDING POINTS TO MAP*/
        //GeoPoint defines the map coordinates the overlay item
        GeoPoint point = new GeoPoint(32074938,34775591);

        //Pass the point to a new OverlayItem
        OverlayItem overlayitem = new OverlayItem(point, "Konichiwa!", "TA City!");
        GeoPoint point2 = new GeoPoint(32055555, 34769572);
        OverlayItem overlayitem2 = new OverlayItem(point2, "Shalom, bitchez!", "Adi's house in da house!");
        GeoPoint point3 = new GeoPoint(32063374, 34773080);
        OverlayItem overlayitem3 = new OverlayItem(point3, "Yo!", "Rotschild rocks!");
        
        
        /**SHOW ON MAP*/
        //Add OverlayItems to your collection in the G2GItemizedOverlay instance
        itemizedoverlay.addOverlay(overlayitem);
        itemizedoverlay.addOverlay(overlayitem2);
        itemizedoverlay.addOverlay(overlayitem3);
        //Add the G2GItemizedOverlay to the MapView
        mapOverlays.add(itemizedoverlay);
        
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}