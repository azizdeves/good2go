package com.gdma.good2go.utils;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferencesEventsRetrievalDate {
	
     private static final String APP_SHARED_EVENTS_RETRIEVAL_DATE = "event_retrieval_date";
     private static final String DATE_PREF = "date"; 
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
     
     public boolean isFromToday() {
    	 if (isDateExists())
    	 {
    		long oneDay = 24 * 60 * 60 * 1000;
    		long lastWriteDate = getDate() / oneDay;
    		long nowDate = new Date().getTime() / oneDay;
    		return lastWriteDate == nowDate;
    	 }
    	 return false;
     }
}
