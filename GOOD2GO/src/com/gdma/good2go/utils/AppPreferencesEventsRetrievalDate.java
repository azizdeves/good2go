package com.gdma.good2go.utils;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferencesEventsRetrievalDate {
	
     private static final String APP_SHARED_EVENTS_RETRIEVAL_DATE = "event_retrieval_date";
     private static final String DATE_PREF = "date";
     
     private static final int MEASUREMENT_UNIT = 60; 
     private static final int MILISEC_IN_SEC = 1000;
     private static final int SEC_IN_MIN = MEASUREMENT_UNIT;
     private static final int MIN_IN_HOUR = MEASUREMENT_UNIT;
     private static final int HOUR_IN_DAY = 24;
     
     public static final long HOUR = MIN_IN_HOUR * SEC_IN_MIN * MILISEC_IN_SEC;
     public static final long DAY = HOUR_IN_DAY * HOUR;
     public static final long  HALF_HOUR = HOUR / 2;
     
     
     
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferencesEventsRetrievalDate(Context context)
     {
         this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_EVENTS_RETRIEVAL_DATE, Activity.MODE_PRIVATE);
         this.prefsEditor = appSharedPrefs.edit();
     }

     public Long getDate() {
         return appSharedPrefs.getLong(DATE_PREF, 0);
     }

     public boolean isDateExists(){
    	 return appSharedPrefs.contains(DATE_PREF);
     }
     
     public void removeDate(){
         prefsEditor.remove(DATE_PREF);
         prefsEditor.commit();
     }
     
     public void saveDate(Date date) {
    	 long numOfSec = date.getTime();
         prefsEditor.putLong(DATE_PREF, numOfSec);
         prefsEditor.commit();
     }
     
     public boolean isFromLast(long oneUnitOfTimeMeasurement) {
    	 if (isDateExists())
    	 {
    		long lastWriteDate = getDate() / oneUnitOfTimeMeasurement;
    		long nowDate = new Date().getTime() / oneUnitOfTimeMeasurement;
    		return lastWriteDate == nowDate;
    	 }
    	 return false;
     }
}
