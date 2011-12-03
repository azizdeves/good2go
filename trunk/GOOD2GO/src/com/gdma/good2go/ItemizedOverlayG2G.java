package com.gdma.good2go;

import java.util.ArrayList;

//import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


/*This class can manage a whole set of Overlay (which are the individual items placed on the map)*/
public class ItemizedOverlayG2G extends BalloonItemizedOverlay<OverlayItem> {
	
	/**ranisawesome*/
	
	/*The array in which we'll put each of the OverlayItem objects we want on the map*/
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	/*a reference to the application Context - to handle touch events on the overlay items*/
	private Context mContext;
	
/*	The constructor must define the default marker for each of the OverlayItems. 
	 * In order for the Drawable to actually get drawn, it must have its bounds defined. 
	 * Most commonly, you want the center-point at the bottom of the image to be the point at which it's attached to the map coordinates. 
	 * This is handled for you with the boundCenterBottom() method.
	 * Plus initialize context
	public ItemizedOverlayG2G(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}*/

	public ItemizedOverlayG2G(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		mContext = mapView.getContext();
	}
	/*In order to add new OverlayItems to the ArrayList*/
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    /*Each time you add a new OverlayItem to the ArrayList, you must call populate() for the ItemizedOverlay, 
	     * which will read each of the OverlayItems and prepare them to be drawn*/
	    populate();
	}
	
	/*When the populate() method executes, it will call createItem(int) in the ItemizedOverlay to retrieve each OverlayItem. 
	 * You must override this method to properly read from the ArrayList and return the OverlayItem from the position specified by the given integer*/
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}
	
/*	onTap(int) callback method, which will handle the event when an item is tapped by the user
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  This uses the member android.content.Context to create a new AlertDialog.Builder
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  uses the tapped OverlayItem's title and snippet for the dialog's title and message text
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}*/

	protected boolean onBalloonTap(int index, OverlayItem item) {
		/*Toast.makeText(mContext, "onBalloonTap for overlay index " + index,
				Toast.LENGTH_LONG).show();*/
        
		Intent i = new Intent(mContext, EventDetailsActivity.class);
		mContext.startActivity(i);
		return true;
}
}
