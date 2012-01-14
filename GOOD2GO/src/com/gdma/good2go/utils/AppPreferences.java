package com.gdma.good2go.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
     private static final String APP_SHARED_PREFS = "com.aydabtu.BroadcastSMS_preferences"; //  Name of the file -.xml
     private SharedPreferences appSharedPrefs;
     private Editor prefsEditor;

     public AppPreferences(Context context)
     {
         this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
         this.prefsEditor = appSharedPrefs.edit();
     }

     public String getSmsBody() {
         return appSharedPrefs.getString("sms_body", "");
     }

     public void saveSmsBody(String text) {
         prefsEditor.putString("sms_body", text);
         prefsEditor.commit();
     }
}
