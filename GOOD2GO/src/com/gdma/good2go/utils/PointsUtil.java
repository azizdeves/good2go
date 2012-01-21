package com.gdma.good2go.utils;

import android.widget.Toast;

import com.gdma.good2go.communication.RestClient;

public class PointsUtil {
	
	public static final String OPEN_APP = "OPEN_APP";
	public static final String INVITE_A_FRIEND = "INVITE_FRIEND";
	public static final String POST_STATUS= "POST_STATUS"; //TODO TELL DANA TO ADD!!!	
	
	/*NOT IN USE(AUTO IN THE SERVER)*/

	public static final String EVENT_REGISTRATION = "REGISTER_TO_EVENT";	
	public static final String APP_INSTALLATION = "OPEN_ACCOUNT"; 
	public static final String GIVE_FEEDBACK= "RATE_AN_EVENT";
	public static final String GIVE__NO_FEEDBACK= "NO_RATE";

//	SEARCH_EVENT(10), //once a day
//	INVITE_FRIEND(50),
//	FRIEND_REGISTERED(100),
		
	
	public static int remote_addKarma(String userName, String type, RestClient client){
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

	public static int getNumOfPoints(String events){
		if(events==OPEN_APP)
			return 10;
		if(events==INVITE_A_FRIEND)
			return 50;
		if(events==POST_STATUS)
			return 10;
		if(events==EVENT_REGISTRATION)
			return 100;
		if(events==APP_INSTALLATION)
			return 10;
		if(events==GIVE_FEEDBACK)
			return 10;
		if(events==GIVE__NO_FEEDBACK)
			return 0;
		return 0;
		
	}
}
