package com.gdma.good2go.utils;


import java.util.Date;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.ToggleButton;

public class AppPreferencesFilterDetails {
	
     private static final String APP_SHARED_EVENTS_FILTER_DATA = "events_filter_data";
     private static final String DATE_PREF = "date";
     private static final String ANIMAL = "animal";
     private static final String CHILDREN = "children";
     private static final String DISABLED = "disabled";
     private static final String ENVIRONMENT = "env";
     private static final String ELDERLY = "elderly";
     private static final String SPECIAL = "special";
     private static final String DURATION= "duration";
     private static final String RADIUS = "radius";
     private static final String IS_DEFAULT_FILTERS_FLAG = "default_filter_flag";
     private static final int defaultDurationInMin = 12*60;
     private static final int defaultSadiusInKm = 20;
     
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferencesFilterDetails(Context context)
     {
         this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_EVENTS_FILTER_DATA, Activity.MODE_PRIVATE);
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
    	 Long numOfSec = date.getTime();
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

	public  boolean getAnimal() {
		return appSharedPrefs.getBoolean(ANIMAL, true);
	}

	public  boolean getChildren() {
		return appSharedPrefs.getBoolean(CHILDREN, true);
	}
	
	public  boolean getDisabled() {
		return appSharedPrefs.getBoolean(DISABLED, true);
	}
	
	public  boolean getEnv() {
		return appSharedPrefs.getBoolean(ENVIRONMENT, true);
	}
	
	public  boolean getElderly() {
		return appSharedPrefs.getBoolean(ELDERLY, true);
	}
	
	public  boolean getSpecial() {
		return appSharedPrefs.getBoolean(SPECIAL, true);
	}

	public  int getDuration() {
		return appSharedPrefs.getInt(DURATION, defaultDurationInMin);
	}
	
	public  int getRadius() {
		return appSharedPrefs.getInt(RADIUS, defaultSadiusInKm);
	}
    public void saveFilterPrefs(boolean animal,boolean children,boolean disabled, boolean env, boolean elderly, boolean special, int radius, int duration) {
    	prefsEditor.putBoolean(ANIMAL, animal);
    	prefsEditor.putBoolean(CHILDREN, children);
    	prefsEditor.putBoolean(DISABLED, disabled);
    	prefsEditor.putBoolean(ENVIRONMENT, env);
    	prefsEditor.putBoolean(ELDERLY, elderly);
    	prefsEditor.putBoolean(SPECIAL, special);
    	prefsEditor.putInt(DURATION, duration);
    	prefsEditor.putInt(RADIUS,radius);
    	prefsEditor.putBoolean(IS_DEFAULT_FILTERS_FLAG, false);
    	prefsEditor.commit();
    }
    
    public void saveDefaultFilterPrefs() {
    	prefsEditor.putBoolean(ANIMAL, true);
    	prefsEditor.putBoolean(CHILDREN, true);
    	prefsEditor.putBoolean(DISABLED, true);
    	prefsEditor.putBoolean(ENVIRONMENT, true);
    	prefsEditor.putBoolean(ELDERLY, true);
    	prefsEditor.putBoolean(SPECIAL, true);
    	prefsEditor.putInt(DURATION, defaultDurationInMin);
    	prefsEditor.putInt(RADIUS,defaultSadiusInKm);
    	prefsEditor.putBoolean(IS_DEFAULT_FILTERS_FLAG, true);
    	prefsEditor.commit();
    	
    }
    
    public boolean isDefaultFiltersOn(){
    	return appSharedPrefs.getBoolean(IS_DEFAULT_FILTERS_FLAG, true);
    }
}
