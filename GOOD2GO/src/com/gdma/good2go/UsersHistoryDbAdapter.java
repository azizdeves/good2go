package com.gdma.good2go;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the volunteering events, and gives the ability to list all events as well as
 * retrieve  a specific event.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class UsersHistoryDbAdapter {
	
	public static final String KEY_EVENTID = "_id";
	public static final String KEY_EVENTNAME = "name";
	public static final String KEY_EVENT_DURATION = "duration";
	public static final String KEY_EVENTDATE = "date";
	public static final String KEY_EVENPOINTS = "points";
	public static final String KEY_EVENTKEY = "eventkey";
	
    private static final String TAG = "UsersHistoryDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "data2";
    private static final String DATABASE_TABLE = "users_history";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static final String DATABASE_CREATE_USERS_HISTORY_TABLE =
            "create table "+ DATABASE_TABLE + " ("
        		+ KEY_EVENTID + " integer primary key autoincrement, "
    	        + KEY_EVENTNAME +" text not null, "
    	        + KEY_EVENTDATE +" text not null, "
    	        + KEY_EVENPOINTS +" text not null, "
    	        + KEY_EVENT_DURATION + " text not null, "
    	        + KEY_EVENTKEY + " text not null, "
    	        + "UNIQUE ("
    	        + KEY_EVENTKEY
    	        + "));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_USERS_HISTORY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS users_history");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public UsersHistoryDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the events database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public UsersHistoryDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new event using the details provided. If the event is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param name the name of the event
     * @param info the short info of the event
     * @param details the long details of the event
     * @param gplat the geopoint latitude
     * @param gplong the geopoint longtitude
     * @return rowId or -1 if failed
     */
    
    public long createUsersHistory(String key, String name, String date, String points,String duration) 
    {
    	
        ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_EVENTKEY, key);
        initialValues.put(KEY_EVENTNAME, name);
        initialValues.put(KEY_EVENTDATE, date);
        initialValues.put(KEY_EVENPOINTS, points);
        initialValues.put(KEY_EVENT_DURATION, duration);
        long result=mDb.insert(DATABASE_TABLE, null, initialValues);
        return result;
    }

    /**
     * Delete the event with the given rowId
     * 
     * @param rowId id of event to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteUsersHistory(long eventId) {
        return mDb.delete(DATABASE_TABLE, KEY_EVENTID + "=" + eventId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllUsersHistory() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_EVENTID, KEY_EVENTNAME,
        		KEY_EVENTDATE, KEY_EVENPOINTS,KEY_EVENT_DURATION}, null, null, null, null, null, null);
    }


}
