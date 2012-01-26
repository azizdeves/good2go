package com.gdma.good2go.communication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Pair;

import com.gdma.good2go.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import flexjson.JSONDeserializer;

/**
 * Remote functions provider
 * 
 * USAGE:
 * 			RemoteFunctions rf = RemoteFunctions.INSTANCE;
 * 
 * 			rf.<funcName>(RemoteFunctions.<funcDefConstant>,<funcParams>);
 * 
 * WHERE:
 * 			<funcName> - is one of the defined functions in the class
 * 			<funcDefConstant> - is one of the defined constants in the class 
 * 								it's a Pair that holds 
 * 								> a remote func name as a first param
 * 								> all the needed params for the remote func
 * 			<funcParams> - are the values to pass to the remote func
 * 
 * EXAMPLE:
 *   			rf.addFeedback(RemoteFunctions.ADD_USER_EVENT_FEEDBACK,
 *   							userName, occurrenceKey, rating);
 */

public enum RemoteFunctions {
	
	
	INSTANCE;

	private static final String SERVER_URL = "http://good-2-go.appspot.com/good2goserver";
	
	
	private static final String GET_EVENTS_name = "getEvents";
	private static final String GET_USER_HISTORY_name = "getUserHistory";
	private static final String GET_USER_FUTURE_EVENTS_name = "getRegisteredFutureEvents";	
	private static final String GET_USER_UNFEEDBACKED_EVENTS_name = "getEventsToFeedback";
	private static final String GET_USER_DETAILS_name = "getUserDetails";
	private static final String GET_USER_KARMA_name = "getKarma";
	
	private static final String ADD_USER_name = "addUser";
	private static final String ADD_USER_KARMA_name = "addKarma";
	private static final String ADD_USER_EVENT_FEEDBACK_name = "addFeedback";
	private static final String EDIT_USER_name = "editUser";	
	private static final String REGISTER_USER_TO_EVENT_name = "registerToOccurrence";
	private static final String CANCEL_USER_REGISTRATION_TO_EVENT_name = "cancelRegistration";
	
	
	private static final String[] USER_NAME_params = {"userName"};
	private static final String[] USER_NAME_DATE_params = {"userName", "userDate"};
	private static final String[] USER_NAME_OCC_KEY_params = {"userName", "occurrenceKey"};
	
	private static final String[] GET_EVENTS_params = {"userDate", "lon", "lat"};
	private static final String[] GET_USER_HISTORY_params = USER_NAME_DATE_params;
	private static final String[] GET_USER_FUTURE_EVENTS_params = USER_NAME_DATE_params;
	private static final String[] GET_USER_UNFEEDBACKED_EVENTS_params = USER_NAME_DATE_params;
	private static final String[] GET_USER_DETAILS_params = USER_NAME_params;
	private static final String[] GET_USER_KARMA_params = USER_NAME_params;

	private static final String[] ADD_USER_params = USER_NAME_params;
	private static final String[] ADD_USER_KARMA_params = {"userName", "type"};
	private static final String[] EDIT_USER_params = {"userName", "firstName", "lastName", "birthYear", "sex", "city", "phone"};
	private static final String[] ADD_USER_EVENT_FEEDBACK_params = {"userName", "occurrenceKey", "rating"};
	private static final String[] REGISTER_USER_TO_EVENT_params = USER_NAME_OCC_KEY_params;
	private static final String[] CANCEL_USER_REGISTRATION_TO_EVENT_params = USER_NAME_OCC_KEY_params;
	
	
	private static final Pair<String,String[]> GET_EVENTS =
		    new Pair<String, String[]>(GET_EVENTS_name, GET_EVENTS_params);	
	private static final Pair<String,String[]> GET_USER_HISTORY = 
			new Pair<String, String[]>(GET_USER_HISTORY_name, GET_USER_HISTORY_params);
	private static final Pair<String,String[]> GET_USER_FUTURE_EVENTS = 
			new Pair<String, String[]>(GET_USER_FUTURE_EVENTS_name, GET_USER_FUTURE_EVENTS_params);
	private static final Pair<String,String[]> GET_USER_UNFEEDBACKED_EVENTS = 
			new Pair<String, String[]>(GET_USER_UNFEEDBACKED_EVENTS_name, GET_USER_UNFEEDBACKED_EVENTS_params);
	private static final Pair<String,String[]> GET_USER_DETAILS = 
			new Pair<String, String[]>(GET_USER_DETAILS_name, GET_USER_DETAILS_params);
	private static final Pair<String,String[]> GET_USER_KARMA = 
			new Pair<String, String[]>(GET_USER_KARMA_name, GET_USER_KARMA_params);
	private static final Pair<String,String[]> ADD_USER = 
			new Pair<String, String[]>(ADD_USER_name, ADD_USER_params);
	private static final Pair<String,String[]> ADD_USER_KARMA = 
			new Pair<String, String[]>(ADD_USER_KARMA_name, ADD_USER_KARMA_params);
	private static final Pair<String,String[]> EDIT_USER = 
			new Pair<String, String[]>(EDIT_USER_name, EDIT_USER_params);
	private static final Pair<String,String[]> ADD_USER_EVENT_FEEDBACK = 
			new Pair<String, String[]>(ADD_USER_EVENT_FEEDBACK_name, ADD_USER_EVENT_FEEDBACK_params);
	private static final Pair<String,String[]> REGISTER_USER_TO_EVENT = 
			new Pair<String, String[]>(REGISTER_USER_TO_EVENT_name, REGISTER_USER_TO_EVENT_params);
	private static final Pair<String,String[]> CANCEL_USER_REGISTRATION_TO_EVENT = 
			new Pair<String, String[]>(CANCEL_USER_REGISTRATION_TO_EVENT_name, CANCEL_USER_REGISTRATION_TO_EVENT_params);
	
//SEND	
	private int sendToServer (Pair<String,String[]> action, String... paramsValues){
		
		String actionName = action.first;
		String[] actionParamsNames = action.second;
		
		RestClient client = new RestClient(SERVER_URL);		
		populateParams(actionName, actionParamsNames, paramsValues, client);
		
		try{
			client.Execute(1); //1 is HTTP GET
			return 1;
		}
		catch (Exception e){
			return 0;
		}
	}	
	
	public int addUser (String userName){
		return sendToServer (ADD_USER, new String [] {userName});
	}
	
	public int editUser (String userName, 
			String firstName, String lastName, 
			String birthYear, String sex, 
			String city, String phone){
		return sendToServer (EDIT_USER, new String [] {userName, firstName, lastName, birthYear, sex, city, phone});
	}

	public int addUserKarma (String userName, String type){
		return sendToServer (ADD_USER_KARMA, new String [] {userName, type});
		
	}	
	public int addFeedback (String userName, String occurrenceKey, String rating){
		return sendToServer (ADD_USER_EVENT_FEEDBACK, new String [] {userName, occurrenceKey, rating});
	}
	
	public int registerUserToEvent (String userName, String occurrenceKey){
		return sendToServer (REGISTER_USER_TO_EVENT, new String [] {userName, occurrenceKey});
	}
	
	public int cancelRegistrationToevent (String userName, String occurrenceKey){
		return sendToServer (CANCEL_USER_REGISTRATION_TO_EVENT, new String [] {userName, occurrenceKey});
	}

	//GET
	
	private String getFromServer(Pair<String,String[]> action, String... paramsValues){
		
		String actionName = action.first;
		String[] actionParamsNames = action.second;
		String JSONResponse = null;
		
		RestClient client = new RestClient(SERVER_URL);		
		populateParams(actionName, actionParamsNames, paramsValues, client);
		
		try{
			client.Execute(1); //1 is HTTP GET
			JSONResponse = client.getResponse();
			if (JSONResponse!=null)
				return JSONResponse.trim();
			return null;			
		}
		catch (Exception e){
			return null;
		}
	}
	
	public List<String[]> getUserEvents (Pair<String,String[]> action, String userName, String userDate){
		JsonArray jsonArray = new JsonArray();
		JsonParser parser = new JsonParser();
		List<String[]> eventList= new ArrayList <String[]>();		
		String JSONResponse = null; 
		
		JSONResponse = getFromServer (action, userName, userDate);
		
		if (JSONResponse!=null)
		{
			try{
				jsonArray = parser.parse(JSONResponse).getAsJsonArray();
				for (int i=0;i<jsonArray.size();i++){
					JsonObject jsonKarma = (JsonObject) jsonArray.get(i);
					String eventName = jsonKarma.getAsJsonPrimitive("Event").getAsString();
					String date = jsonKarma.getAsJsonPrimitive("Date").getAsString();
					long points = jsonKarma.getAsJsonPrimitive("Points").getAsLong();
					
					String [] oneEvent = new String[3];
					oneEvent[0] =  eventName;
					oneEvent[1] = date;
					oneEvent[2] = Long.toString(points);			
					eventList.add(oneEvent);
				}
			}
			catch (Exception e){
					return null;
			}
		}
		else
			return null;
		
		return eventList;
	}
	public List<String[]> getUserHistoryEvents (String userName, String userDate){
		return getUserEvents (GET_USER_HISTORY, userName, userDate);
	}
	
	public List<String[]> getUserFutureEvents (String userName, String userDate){
		return getUserEvents (GET_USER_FUTURE_EVENTS, userName, userDate);
	}
	
	public List<Event> getUserUnfeedbackedEvents (String userName, String userDate){
		String JSONResponse = null; 
		JSONResponse = getFromServer(GET_USER_UNFEEDBACKED_EVENTS, userName, userDate);
		if (JSONResponse!=null && JSONResponse!="")
		{
			try{
				JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
				List<Event> events = new JSONDeserializer<List<Event>>().
						use(Date.class, new DateParser()).deserialize(JSONResponse);
				return events;
				}
			catch (Exception e){
				return null;
				}
		}
		return null;
	}
//	
//	public int getUserDetails (Pair<String,String[]> action, String userName){
//		return sendToServer (action, new String [] {userName});
//	}
//	
	public long getUserKarma (String userName){
		String JSONResponse = null; 
		long points = 0;
		JSONResponse = getFromServer (GET_USER_KARMA, userName);
		
		try{
			points = Long.parseLong(JSONResponse);
		}
		catch(Exception e){
		}
		
		return points;
	}
	
	
	private void populateParams(String actionName, String[] actionParamsNames, String[] paramsValues, RestClient client) {
		client.AddParam("action", actionName);		
		for (int i=0; i < actionParamsNames.length; i++){
				client.AddParam(actionParamsNames[i], paramsValues[i]);}		
	}
}
