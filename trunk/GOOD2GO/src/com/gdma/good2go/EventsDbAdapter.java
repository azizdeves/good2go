/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gdma.good2go;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
public class EventsDbAdapter {

	public static final String KEY_EVENT_KEY = "eventkey";
	public static final String KEY_EVENTNAME = "name";
	public static final String KEY_EVENT_SHORT_INFO = "info";
	public static final String KEY_EVENT_DETAILS = "details";
	public static final String KEY_EVENT_GP_LONG = "gplong";
	public static final String KEY_EVENT_GP_LAT = "gplat";
	public static final String KEY_EVENT_DISTANCE = "distance";
	public static final String KEY_EVENT_DURATION = "duration";
	public static final String KEY_EVENT_CITY = "city";
	public static final String KEY_EVENT_STREET = "street";
	public static final String KEY_EVENT_STREET_NUMBER = "streetnumber";
	public static final String KEY_EVENT_TYPE_ANIMAL = "type_animal";
	public static final String KEY_EVENT_TYPE_CHILDREN = "type_children";
	public static final String KEY_EVENT_TYPE_DISABLED = "type_disabled";
	public static final String KEY_EVENT_TYPE_ELDERLY = "type_elderly";
	public static final String KEY_EVENT_TYPE_ENVIRONMENT = "type_environment";
	public static final String KEY_EVENT_TYPE_SPECIAL = "type_special";
	public static final String KEY_EVENTID = "_id";
	

    private static final String TAG = "EventsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table events ("
    		+ KEY_EVENTID + " integer primary key autoincrement, "
	        + KEY_EVENTNAME +" text not null, "
	        + KEY_EVENT_SHORT_INFO + " text not null, "
	        + KEY_EVENT_DETAILS + " text not null, "
	        + KEY_EVENT_DISTANCE + " text not null, "
	        + KEY_EVENT_CITY + " text not null, "
	        + KEY_EVENT_STREET + " text not null, "
	        + KEY_EVENT_STREET_NUMBER + " text not null, "
	        + KEY_EVENT_DURATION + " text not null, " 
	        + KEY_EVENT_GP_LONG + " text not null, "
	        + KEY_EVENT_GP_LAT + " text not null, " 
	        + KEY_EVENT_TYPE_ANIMAL + " text not null, "
	        + KEY_EVENT_TYPE_CHILDREN + " text not null, "
	        + KEY_EVENT_TYPE_DISABLED + " text not null, "
	        + KEY_EVENT_TYPE_ELDERLY + " text not null, "
	        + KEY_EVENT_TYPE_ENVIRONMENT + " text not null, "
	        + KEY_EVENT_TYPE_SPECIAL + " text not null, "
	        + KEY_EVENT_KEY + " text not null, "
	        + "UNIQUE ("
	        + KEY_EVENT_KEY
	        + "));";
    
    
    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "events";
    private static final int DATABASE_VERSION = 5;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS events");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public EventsDbAdapter(Context ctx) {
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
    public EventsDbAdapter open() throws SQLException {
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
    public long createEvent(String eventkey, String name, String info, String details, 
    		String gplat, String gplong, String distance, String duration,
    		String city, String street, String streetNumber,
    		String typeAnimal, String typeChildren,String typeDisabled,
    		String typeElderly,String typeEnvironment,String typeSpecial) 
    {
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_EVENTNAME, name);
        initialValues.put(KEY_EVENT_SHORT_INFO, info);
        initialValues.put(KEY_EVENT_DETAILS, details);
        initialValues.put(KEY_EVENT_DISTANCE, distance);
        initialValues.put(KEY_EVENT_CITY, city);
        initialValues.put(KEY_EVENT_STREET, street);
        initialValues.put(KEY_EVENT_STREET_NUMBER, streetNumber);        
        initialValues.put(KEY_EVENT_DURATION, duration);
        initialValues.put(KEY_EVENT_GP_LONG, gplong);
        initialValues.put(KEY_EVENT_GP_LAT, gplat);
        initialValues.put(KEY_EVENT_KEY, eventkey);
        initialValues.put(KEY_EVENT_TYPE_ANIMAL, typeAnimal);
        initialValues.put(KEY_EVENT_TYPE_CHILDREN, typeChildren);
        initialValues.put(KEY_EVENT_TYPE_DISABLED, typeDisabled);
        initialValues.put(KEY_EVENT_TYPE_ELDERLY, typeElderly);
        initialValues.put(KEY_EVENT_TYPE_ENVIRONMENT, typeEnvironment);
        initialValues.put(KEY_EVENT_TYPE_SPECIAL, typeSpecial);
        

        long result=mDb.insert(DATABASE_TABLE, null, initialValues);
        return result;
    }

    /**
     * Delete the event with the given rowId
     * 
     * @param rowId id of event to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteEvent(long eventId) {

        return mDb.delete(DATABASE_TABLE, KEY_EVENTID + "=" + eventId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all events in the database
     * 
     * @return Cursor over all events
     */
    public Cursor fetchAllEvents() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_EVENTID, KEY_EVENTNAME,
        		KEY_EVENT_SHORT_INFO, KEY_EVENT_DETAILS, 
        		KEY_EVENT_GP_LONG, KEY_EVENT_GP_LAT, 
        		KEY_EVENT_DISTANCE, KEY_EVENT_DURATION,
        		KEY_EVENT_CITY, KEY_EVENT_STREET, KEY_EVENT_STREET_NUMBER},
        		null, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the event that matches the given rowId
     * 
     * @param eventId id of event to retrieve
     * @return Cursor positioned to matching event, if found
     * @throws SQLException if event could not be found/retrieved
     */
    public Cursor fetchEvent(long eventId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_EVENTID, KEY_EVENTNAME,
                    KEY_EVENT_SHORT_INFO, KEY_EVENT_DETAILS, 
                    KEY_EVENT_GP_LONG, KEY_EVENT_GP_LAT, 
                    KEY_EVENT_DISTANCE, KEY_EVENT_DURATION,
            		KEY_EVENT_CITY, KEY_EVENT_STREET, KEY_EVENT_STREET_NUMBER,
            		KEY_EVENT_TYPE_ANIMAL, KEY_EVENT_TYPE_CHILDREN, KEY_EVENT_TYPE_DISABLED,
            		KEY_EVENT_TYPE_ELDERLY,KEY_EVENT_TYPE_ENVIRONMENT,KEY_EVENT_TYPE_SPECIAL}, 
                    KEY_EVENTID + "=" + eventId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
   
    @SuppressWarnings("null")
	public Cursor fetchEventByFilters(String[] types, int radius, int timeInMinutes) throws SQLException {

    	int i=0;
    	String q = "SELECT * FROM events WHERE 1=1 ";
    	String arr[] = {"", ""};
    	if(timeInMinutes>0){
    		q=q+" AND "+ KEY_EVENT_DURATION + ">=" + "\' " +Integer.toString(timeInMinutes)+"\'";
    		arr[i]=Integer.toString(timeInMinutes);
    		i++;
    	}
    	if(radius>0){
    		q=q+" AND "+ KEY_EVENT_DISTANCE+" <" +  "\' "+ Integer.toString(radius)+ "\'";
    		arr[i]=Integer.toString(radius);
    		i++;
    	}
    	if(types!=null){
    		int j=0;
    		boolean flag=false;
    		while(j<types.length){
    			if(types[j]=="animals"){
    				if(!flag){
    					q=q+" AND ("+ KEY_EVENT_TYPE_ANIMAL+ " = \'1\' ";
    					flag=true;
    				}
    				else
    					q=q+" OR "+ KEY_EVENT_TYPE_ANIMAL+ " = \'1\' ";
    			}
    			
    			
    			if(types[j]=="children"){
    				if(!flag){
    					q=q+" AND ("+ KEY_EVENT_TYPE_CHILDREN+ " = \'1\'";
    					flag=true;
    				}
    				else
    					q=q+" OR "+ KEY_EVENT_TYPE_CHILDREN+ " = \'1\' ";
    			}  

    			if(types[j]=="disabled"){
    				if(!flag){
    					q=q+ "AND ("+ KEY_EVENT_TYPE_DISABLED+ " = \'1\' ";
    					flag=true;
    				}
    				else
    					q=q+" OR "+ KEY_EVENT_TYPE_DISABLED+ " = \'1\' ";
    			}  
    			if(types[j]=="elderly"){
    				if(!flag){
    					q=q+" AND ("+ KEY_EVENT_TYPE_ELDERLY+ " = \'1\' ";
    					flag=true;
    				}
    				else
    					q=q+" OR "+ KEY_EVENT_TYPE_ELDERLY+ " = \'1\' ";
    			}  
    			if(types[j]=="environment"){
    				if(!flag){
    					q=q+" AND ("+ KEY_EVENT_TYPE_ENVIRONMENT+ " = \'1\' ";
    					flag=true;
    				}
    				else
    					q=q+" OR "+ KEY_EVENT_TYPE_ENVIRONMENT+ " = \'1\' ";
    			}  
    			if(types[j]=="special"){
    				if(!flag){
    					q=q+" AND ("+ KEY_EVENT_TYPE_SPECIAL+ " = \'1\' ";
    					flag=true;
    				}
    				else
    					q=q+" OR "+ KEY_EVENT_TYPE_SPECIAL+ " = \'1\'";
    			}      			

    			j++;
    		}
    		if(flag)
    			q=q+")";
    	}
       	Cursor mCursor = mDb.rawQuery(q, null);
    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }  
    /**
     * Update the event using the details provided. The event to be updated is
     * specified using the eventId, and it is altered to use the details
     * values passed in
     * 
     * @param eventId id of event to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the event was successfully updated, false otherwise
     */
    public boolean updatEvent(long eventId, String name, String info, String details,
    		String gplong, String gplat) 
    {
    	/**TODO: add new columns**/
        ContentValues args = new ContentValues();
        args.put(KEY_EVENTNAME, name);
        args.put(KEY_EVENT_SHORT_INFO, info);
        args.put(KEY_EVENT_DETAILS, details);
        args.put(KEY_EVENT_GP_LONG, gplong);
        args.put(KEY_EVENT_GP_LAT, gplat);

        return mDb.update(DATABASE_TABLE, args, KEY_EVENTID + "=" + eventId, null) > 0;
    }


}
