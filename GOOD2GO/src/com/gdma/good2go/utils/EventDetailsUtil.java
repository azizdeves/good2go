package com.gdma.good2go.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import com.gdma.good2go.R;
import com.gdma.good2go.ui.CountMeIn;

public class EventDetailsUtil {
	public static void initTabsAppearance(TabHost tabhost) 
	{
	    for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
	    {
	    	tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.event_details_tab_bg);
	        tabhost.getTabWidget().getChildAt(i).getLayoutParams().height = 50; 
	    }
	}
	
}
