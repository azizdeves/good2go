package com.gdma.good2go.utils;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferencesEventsRetrievalDate {
	
     private static final String APP_SHARED_EVENTS_RETRIEVAL_DATE = "event_retrieval_date"; 
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferencesEventsRetrievalDate(Context context)
     {
         this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_EVENTS_RETRIEVAL_DATE, Activity.MODE_PRIVATE);
         this.prefsEditor = appSharedPrefs.edit();
     }

     public Long getDate() {
         return appSharedPrefs.getLong("date", 0);
     }

     public boolean isDateExists(){
    	 return appSharedPrefs.contains("date");
     }
     
     public void saveDate(Date date) {
    	 Long numOfSec = date.getTime();
         prefsEditor.putLong("date", numOfSec);
         prefsEditor.commit();
     }
}
