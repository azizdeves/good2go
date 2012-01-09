package com.gdma.good2go.utils;

import android.os.Bundle;

public class FiltersUtil {

	public static Bundle getDefaultFiltersBundle(){
    	Bundle b = new Bundle();
		b.putString("animals","1");			
		b.putString("children","1");
	 	b.putString("environment","1");
		b.putString("elderly","1");
		b.putString("disabled","1");	
		b.putString("special","1");	
		
		b.putInt("durationInMinutes", 1000);
		b.putInt("radius", 30);
		
		return b;
	}
	
	public static String[] getArrayOfFiltersParams(Bundle bundleResult){
		
		int i=0;
		String[] types= new String[bundleResult.size()-2];
		for (int j = 0; j < types.length; j++) {
			types[j]="";
		}
		
		if(bundleResult!=null){
			if(bundleResult.getString("animals").compareTo("1")==0){
				types[i]="animals";
				i++;
			}	
			if(bundleResult.getString("children")=="1"){
				types[i]="children";
				i++;

			}
			if(bundleResult.getString("disabled")=="1"){
				types[i]="disabled";
				i++;

			}
			if(bundleResult.getString("elderly")=="1"){
				types[i]="elderly";
				i++;

			}
			if(bundleResult.getString("environment")=="1"){
				types[i]="environment";
				i++;

			}
			if(bundleResult.getString("special")=="1"){
				types[i]="special";
				i++;

			}

		
		}
		return types;
	}
}
