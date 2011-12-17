package com.gdma.good2go;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListTab extends ListActivity {
	
	private EventsDbAdapter mDbHelper;
	Cursor mEventsCursor;
	
	private String[] mColumns;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	
    	/**GET POINTS FROM LOCAL DB*/
    	mDbHelper = new EventsDbAdapter(this);
		mDbHelper.open();
		mEventsCursor = mDbHelper.fetchAllEvents();
		startManagingCursor(mEventsCursor);
		
		
		/**SHOW THE POINTS IN THE LIST*/
        mColumns = new String[] {EventsDbAdapter.KEY_EVENTNAME, 
        		EventsDbAdapter.KEY_EVENT_SHORT_INFO,EventsDbAdapter.KEY_EVENT_DISTANCE};
        int[] to = new int[] {R.id.eventname_entry, R.id.eventinfo_entry, R.id.eventdistance_entry };

        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, 
        		R.layout.list_item, 
        		mEventsCursor, mColumns, to);

        setListAdapter(mAdapter);
		}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Cursor c = mEventsCursor;
        c.moveToPosition(position);
        
        Intent i = new Intent(this, EventDetails.class);
        i.putExtra("sender", "list");
        i.putExtra(EventsDbAdapter.KEY_EVENTID, id);
        startActivity(i);
    }
}