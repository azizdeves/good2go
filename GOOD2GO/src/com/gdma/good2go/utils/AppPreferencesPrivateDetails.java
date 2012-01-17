package com.gdma.good2go.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;  

public class AppPreferencesPrivateDetails {
	
     private static final String APP_SHARED_PRIVATE_DETAILS = "users_private_details"; 
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferencesPrivateDetails(Context context)
     {
         this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PRIVATE_DETAILS, Activity.MODE_PRIVATE);
         this.prefsEditor = appSharedPrefs.edit();
     }

     public String getUserName() {
         return appSharedPrefs.getString("username", "");
     }

     public boolean isUsernameExists(){
    	 return appSharedPrefs.contains("username");
     }
     
     public void saveUserName(String username) {
         prefsEditor.putString("username", username);
         prefsEditor.commit();
     }
}
