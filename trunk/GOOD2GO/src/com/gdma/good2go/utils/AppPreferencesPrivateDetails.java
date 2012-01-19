package com.gdma.good2go.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public  class AppPreferencesPrivateDetails {
	
     private static final String APP_SHARED_PRIVATE_DETAILS = "user_private_details";     
     private static final String USERNAME = "userNameVal";
     private static final String USER_FIRST_NAME = "userFirstName";
     private static final String USER_LAST_NAME = "userLastName";
     private static final String USER_AGE = "userAge";
     private static final String USER_CITY = "userCity";
     private static final String USER_SEX = "userSex";
     private static final String USER_PHONE = "userPhone";
     
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;
        

     public AppPreferencesPrivateDetails(Context context)
     {
         appSharedPrefs = context.getSharedPreferences(APP_SHARED_PRIVATE_DETAILS, Activity.MODE_PRIVATE);
     }

//     public boolean isUsernameExists(){
//    	 return appSharedPrefs.contains(USERNAME);
//     }
     
     public boolean doPrivateDetailsExist(){
    	 
    	 return 
    			 appSharedPrefs.contains(USER_FIRST_NAME)
    			 && appSharedPrefs.contains(USER_LAST_NAME)
    			 && appSharedPrefs.contains(USER_AGE)
    			 && appSharedPrefs.contains(USER_CITY)
    			 && appSharedPrefs.contains(USER_SEX)
    			 && appSharedPrefs.contains(USER_PHONE);
     }
     
     public void setAllPrivatePrefs(String userFirstName, String userLastName, String userAge,
    		 String userCity, String userSex, String userPhone) 
     {
    	 setUserFirstName(userFirstName);
    	 setUserLastName(userLastName);
    	 setUserAge(userAge);
    	 setUserCity(userCity);
    	 setUserSex(userSex);
    	 setUserPhone(userPhone);
     }
     
     public String getUserName() {
    	 return getPref(USERNAME);
     }
    
     public void setUserName(String username) {
    	 setPref(USERNAME, username);
     }
     

	public String getUserFirstName() {
		return getPref(USER_FIRST_NAME);
	}

	public void setUserFirstName(String userFirstName) {
		setPref(USER_FIRST_NAME, userFirstName);
	}

	public String getUserLastName() {
		return getPref(USER_LAST_NAME);
	}

	public void setUserLastName(String userLastName) {
		setPref(USER_LAST_NAME, userLastName);
	}

	public String getUserAge() {
		return getPref(USER_AGE);
	}

	public void setUserAge(String userAge) {
		setPref(USER_AGE, userAge);
	}

	public String getUserCity() {
		return getPref(USER_CITY);
	}

	public void setUserCity(String userCity) {
		setPref(USER_CITY, userCity);
	}

	public String getUserSex() {
		return getPref(USER_SEX);
	}

	public void setUserSex(String userSex) {
		setPref(USER_SEX, userSex);
	}
	
    public String getPref(String prefName){
    	return appSharedPrefs.getString(prefName, null);
    }
    
    public void setPref(String prefName, String prefValue){
    	prefsEditor = appSharedPrefs.edit();
    	prefsEditor.putString(prefName, prefValue);
    	prefsEditor.commit();
    }

	public String getUserPhone() {
		return getPref(USER_PHONE);
	}

	public void setUserPhone(String userPhone) {
		setPref(USER_PHONE, userPhone);
	}
    
     
}
