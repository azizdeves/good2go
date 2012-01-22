package com.gdma.good2go.utils;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class FiltersUtil {


    public static String[] getArrayOfFilteredTypes(AppPreferencesFilterDetails mFilterPrefs){
    	List<String> types = new ArrayList<String>();
    	
		int i=0;
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
		
		return (String[]) types.toArray(new String[types.size()]);
    }
}
