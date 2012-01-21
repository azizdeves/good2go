package com.gdma.good2go.communication;

import android.util.Pair;

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
	
	
	public static final Pair<String,String[]> GET_EVENTS =
		    new Pair<String, String[]>(GET_EVENTS_name, GET_EVENTS_params);	
	public static final Pair<String,String[]> GET_USER_HISTORY = 
			new Pair<String, String[]>(GET_USER_HISTORY_name, GET_USER_HISTORY_params);
	public static final Pair<String,String[]> GET_USER_FUTURE_EVENTS = 
			new Pair<String, String[]>(GET_USER_FUTURE_EVENTS_name, GET_USER_FUTURE_EVENTS_params);
	public static final Pair<String,String[]> GET_USER_UNFEEDBACKED_EVENTS = 
			new Pair<String, String[]>(GET_USER_UNFEEDBACKED_EVENTS_name, GET_USER_UNFEEDBACKED_EVENTS_params);
	public static final Pair<String,String[]> GET_USER_DETAILSY = 
			new Pair<String, String[]>(GET_USER_DETAILS_name, GET_USER_DETAILS_params);
	public static final Pair<String,String[]> GET_USER_KARMA = 
			new Pair<String, String[]>(GET_USER_KARMA_name, GET_USER_KARMA_params);
	public static final Pair<String,String[]> ADD_USER = 
			new Pair<String, String[]>(ADD_USER_name, ADD_USER_params);
	public static final Pair<String,String[]> ADD_USER_KARMA = 
			new Pair<String, String[]>(ADD_USER_KARMA_name, ADD_USER_KARMA_params);
	public static final Pair<String,String[]> EDIT_USER = 
			new Pair<String, String[]>(EDIT_USER_name, EDIT_USER_params);
	public static final Pair<String,String[]> ADD_USER_EVENT_FEEDBACK = 
			new Pair<String, String[]>(ADD_USER_EVENT_FEEDBACK_name, ADD_USER_EVENT_FEEDBACK_params);
	public static final Pair<String,String[]> REGISTER_USER_TO_EVENT = 
			new Pair<String, String[]>(REGISTER_USER_TO_EVENT_name, REGISTER_USER_TO_EVENT_params);
	public static final Pair<String,String[]> CANCEL_USER_REGISTRATION_TO_EVENT = 
			new Pair<String, String[]>(CANCEL_USER_REGISTRATION_TO_EVENT_name, CANCEL_USER_REGISTRATION_TO_EVENT_params);
	
	
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
	
//	private boolean getFromServer (Pair<String,String[]> action, String... paramsValues){
//		
//		String actionName = action.getKey();
//		String[] actionParamsNames = action.getValue();
//		
//		RestClient client = new RestClient(SERVER_URL);		
//		populateParams(actionName, actionParamsNames, paramsValues, client);
//		
//		try{
//			client.Execute(1); //1 is HTTP GET
//			return 1;
//		}
//		catch (Exception e){
//			return 0;
//		}
//	}
	
	public int addUser (Pair<String,String[]> action, String userName){
		return sendToServer (action, new String [] {userName});
	}
	
	public int editUser (Pair<String,String[]> action, String userName, 
			String firstName, String lastName, 
			String birthYear, String sex, 
			String city, String phone){
		return sendToServer (action, new String [] {userName, firstName, lastName, birthYear, sex, city, phone});
	}

	public int addUserKarma (Pair<String,String[]> action, String userName, String type){
		return sendToServer (action, new String [] {userName, type});
		
	}	
	public int addFeedback (Pair<String,String[]> action, String userName, String occurrenceKey, String rating){
		return sendToServer (action, new String [] {userName, occurrenceKey, rating});
	}
	
	public int registerUserToEvent (Pair<String,String[]> action, String userName, String occurrenceKey){
		return sendToServer (action, new String [] {userName, occurrenceKey});
	}
	
	public int cancelRegistrationToevent (Pair<String,String[]> action, String userName, String occurrenceKey){
		return sendToServer (action, new String [] {userName, occurrenceKey});
	}

	//GETS
	
//	public int getEvents (Pair<String,String[]> action, String userDate, String lon, String lat){
//		return sendToServer (action, new String [] {userDate, lat, lon});
//	}
//	
//	public int getUserHistoryEvents (Pair<String,String[]> action, String userName, String userDate){
//		return sendToServer (action, new String [] {userName, userDate});
//	}
//	
//	public int getUserFutureEvents (Pair<String,String[]> action, String userName, String userDate){
//		return sendToServer (action, new String [] {userName, userDate});
//	}
//	
//	public int getUserUnfeedbackedEvents (Pair<String,String[]> action, String userName, String userDate){
//		return sendToServer (action, new String [] {userName, userDate});
//	}
//	
//	public int getUserDetails (Pair<String,String[]> action, String userName){
//		return sendToServer (action, new String [] {userName});
//	}
//	
//	public int getUserKarma (Pair<String,String[]> action, String userName){
//		return sendToServer (action, new String [] {userName});
//	}
	
	
	private void populateParams(String actionName, String[] actionParamsNames, String[] paramsValues, RestClient client) {
		client.AddParam("action", actionName);		
		for (int i=0; i < actionParamsNames.length; i++){
				client.AddParam(actionParamsNames[i], paramsValues[i]);}		
	}
}
