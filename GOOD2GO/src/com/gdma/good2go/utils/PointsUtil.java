package com.gdma.good2go.utils;

import android.widget.Toast;

import com.gdma.good2go.communication.RestClient;

public class PointsUtil {
	
	public static final String OPEN_APP = "OPEN_APP";
	public static final String INVITE_A_FRIEND = "INVITE_FRIEND";

	public static final String POST_STATUS= "POST_STATUS";
	
	public static final int EVENT_REGISTRATION = 100;	
	public static final int APP_INSTALLATION = 100; 
	public static final int GIVE_FEEDBACK= 100;
	
	
	public int remote_addKarma(String userName, String type, RestClient client){
		client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "addKarma");
		client.AddParam("userName", userName);
		client.AddParam("type", type);
		try{
			client.Execute(1); 
			return 1;
		}
		catch (Exception e){
			return -1;   	
		}		
	}
}
