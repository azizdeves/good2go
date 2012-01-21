package com.gdma.good2go.utils;

import android.widget.Toast;

import com.gdma.good2go.communication.RestClient;

public class PointsUtil {
	
	public static final String OPEN_APP = "OPEN_APP";
	public static final String INVITE_A_FRIEND = "INVITE_FRIEND";
	public static final String POST_STATUS= "POST_STATUS"; //TODO CHECK THAT DANA ADDED THIS EVENT!!!	
	public static final String SEARCH_EVENT= "SEARCH_EVENT";	
	/*NOT IN USE(AUTO IN THE SERVER)*/

	public static final String EVENT_REGISTRATION = "REGISTER_TO_EVENT";	
	public static final String APP_INSTALLATION = "OPEN_ACCOUNT"; 
	public static final String GIVE_FEEDBACK= "RATE_AN_EVENT";
	public static final String GIVE__NO_FEEDBACK= "NO_RATE";

	

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

	public static int getNumOfPoints(String event){
		if(event==OPEN_APP)
			return 10;
		if(event==INVITE_A_FRIEND)
			return 50;
		if(event==POST_STATUS)
			return 10;
		if(event==EVENT_REGISTRATION)
			return 100;
		if(event==APP_INSTALLATION)
			return 10;
		if(event==GIVE_FEEDBACK)
			return 10;
		if(event==GIVE__NO_FEEDBACK)
			return 0;
		if(event==SEARCH_EVENT)
			return 10;
		return 0;
		
	}
}
