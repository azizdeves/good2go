package com.gdma.good2go.utils;


import java.util.Date;
import java.util.Iterator;
import java.util.Map;

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
     
     private static final String DATE_PREF_DEFAULT = "default_date";
     private static final String ANIMAL_DEFAULT = "default_animal";
     private static final String CHILDREN_DEFAULT = "default_children";
     private static final String DISABLED_DEFAULT = "default_disabled";
     private static final String ENVIRONMENT_DEFAULT = "default_env";
     private static final String ELDERLY_DEFAULT = "default_elderly";
     private static final String SPECIAL_DEFAULT = "default_special";
     private static final String DURATION_DEFAULT= "default_duration";
     private static final String RADIUS_DEFAULT = "default_radius";
     
//     private static final String WHAT_FILTER_IS_ON = "kind_of_filter"; //default/users
     private static final String IS_USER_FILTERS_EXISTS_FLAG = "user_filters_exists_flag";
     private static final int defaultDurationInMin = 12*60;
     private static final int defaultRadiusInKm = 100;
     
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferencesFilterDetails(Context context)
     {
         this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_EVENTS_FILTER_DATA, Activity.MODE_PRIVATE);
         this.prefsEditor = appSharedPrefs.edit();
	     	prefsEditor.putBoolean(ANIMAL_DEFAULT, true);
	     	prefsEditor.putBoolean(CHILDREN_DEFAULT, true);
	     	prefsEditor.putBoolean(DISABLED_DEFAULT, true);
	     	prefsEditor.putBoolean(ENVIRONMENT_DEFAULT, true);
	     	prefsEditor.putBoolean(ELDERLY_DEFAULT, true);
	     	prefsEditor.putBoolean(SPECIAL_DEFAULT, true);
	     	prefsEditor.putInt(DURATION_DEFAULT, defaultDurationInMin);
	     	prefsEditor.putInt(RADIUS_DEFAULT,defaultRadiusInKm);
	     	prefsEditor.commit();         
         
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
		return appSharedPrefs.getInt(RADIUS, defaultRadiusInKm);
	}
	
    public void saveFilterPrefs(boolean animal,boolean children,boolean disabled, boolean env, boolean elderly, boolean special, int radius, int duration) {
    	if( animal==appSharedPrefs.getBoolean(ANIMAL_DEFAULT, true) &&
    			children==appSharedPrefs.getBoolean(CHILDREN_DEFAULT, true)&&
    					disabled==appSharedPrefs.getBoolean(DISABLED_DEFAULT, true)&&
    							env==appSharedPrefs.getBoolean(ENVIRONMENT_DEFAULT, true)&&
    									elderly==appSharedPrefs.getBoolean(ELDERLY_DEFAULT, true)&&
    											special==appSharedPrefs.getBoolean(SPECIAL_DEFAULT, true)&&
    													radius==appSharedPrefs.getInt(RADIUS_DEFAULT, defaultRadiusInKm)&&
    															duration==appSharedPrefs.getInt(DURATION_DEFAULT, defaultDurationInMin)
			
    		)
    	{
    		prefsEditor.putBoolean(IS_USER_FILTERS_EXISTS_FLAG, false);
    	}
    	else
    	{
    	prefsEditor.putBoolean(ANIMAL, animal);
    	prefsEditor.putBoolean(CHILDREN, children);
    	prefsEditor.putBoolean(DISABLED, disabled);
    	prefsEditor.putBoolean(ENVIRONMENT, env);
    	prefsEditor.putBoolean(ELDERLY, elderly);
    	prefsEditor.putBoolean(SPECIAL, special);
    	prefsEditor.putInt(DURATION, duration);
    	prefsEditor.putInt(RADIUS,radius);
    	prefsEditor.putBoolean(IS_USER_FILTERS_EXISTS_FLAG, true);
    	}
    	prefsEditor.commit();
    }

    public void removeUsersFilters() {
    	
    	prefsEditor.putBoolean(IS_USER_FILTERS_EXISTS_FLAG, false);
    	prefsEditor.commit();
    	
    }
    
    
    public boolean isUserFiltersExist(){
    	return appSharedPrefs.getBoolean(IS_USER_FILTERS_EXISTS_FLAG, false);
    }

    public void setIsUserFiltersExist(boolean b){
    	prefsEditor.putBoolean(IS_USER_FILTERS_EXISTS_FLAG, b);
    	prefsEditor.commit();
    }
	/**
	 * @return the datePrefDefault
	 */
	public static String getDatePrefDefault() {
		return DATE_PREF_DEFAULT;
	}

	/**
	 * @return the animalDefault
	 */
	public  boolean getAnimalDefault() {
		return appSharedPrefs.getBoolean(ANIMAL_DEFAULT, true);
	}

	public  boolean getChildrenDefault() {
		return appSharedPrefs.getBoolean(CHILDREN_DEFAULT, true);
	}
	
	public  boolean getDisabledDefault() {
		return appSharedPrefs.getBoolean(DISABLED_DEFAULT, true);
	}
	
	public  boolean getEnvDefault() {
		return appSharedPrefs.getBoolean(ENVIRONMENT_DEFAULT, true);
	}
	
	public  boolean getElderlyDefault() {
		return appSharedPrefs.getBoolean(ELDERLY_DEFAULT, true);
	}
	
	public  boolean getSpecialDefault() {
		return appSharedPrefs.getBoolean(SPECIAL_DEFAULT, true);
	}

	public  int getDurationDefault() {
		return appSharedPrefs.getInt(DURATION_DEFAULT, defaultDurationInMin);
	}
	
	public  int getRadiusDefault() {
		return appSharedPrefs.getInt(RADIUS_DEFAULT, defaultRadiusInKm);
	}
    
}
