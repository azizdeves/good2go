package com.gdma.good2go.utils;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class FiltersUtil {


    public static String[] getArrayOfFilteredTypes(AppPreferencesFilterDetails mFilterPrefs){
    	List<String> types = new ArrayList<String>();
    	
		int i=0;
		if(mFilterPrefs.isUserFiltersExist()){
			
			if(mFilterPrefs.getAnimal()){
				types.add("animals");
				
			}
			if(mFilterPrefs.getChildren()){
				types.add("children");
				
			}
			if(mFilterPrefs.getDisabled()){
				types.add("disabled");
				
			}
			if(mFilterPrefs.getEnv()){
				types.add("environment");
				
			}
			if(mFilterPrefs.getElderly()){
				types.add("elderly");
				
			}
	
			if(mFilterPrefs.getSpecial()){
				types.add("special");
				
			}
		}
		
		else{
			if(mFilterPrefs.getAnimalDefault()){
				types.add("animals");
				
			}
			if(mFilterPrefs.getChildrenDefault()){
				types.add("children");
				
			}
			if(mFilterPrefs.getDisabledDefault()){
				types.add("disabled");
				
			}
			if(mFilterPrefs.getEnvDefault()){
				types.add("environment");
				
			}
			if(mFilterPrefs.getElderlyDefault()){
				types.add("elderly");
				
			}
	
			if(mFilterPrefs.getSpecialDefault()){
				types.add("special");
				
			}			
		}
		return (String[]) types.toArray(new String[types.size()]);
    }
    
    public static int getFilterRadius(AppPreferencesFilterDetails mFilterPrefs){
    	if(mFilterPrefs.isUserFiltersExist())
  	    	return  mFilterPrefs.getRadius();
    	else
    		return  mFilterPrefs.getRadiusDefault();
    }
    
    public static int getFilterDuration(AppPreferencesFilterDetails mFilterPrefs){
    	if(mFilterPrefs.isUserFiltersExist())
  	    	return  mFilterPrefs.getDuration();
    	else
  	   		return  mFilterPrefs.getDurationDefault();
    }
    
 
    
    
}
