package com.gdma.good2go.utils;
import java.util.Date;

import android.widget.Toast;

import com.gdma.good2go.User;
import com.gdma.good2go.communication.RestClient;


import flexjson.JSONDeserializer;
import flexjson.transformer.DateTransformer;

public class UsersUtil {
    private static RestClient client = null;
   
    public static User remote_getUsersDetails(String username){
    	client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    	client.AddParam("action", "getUserDetails");
		client.AddParam("userName", username);
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
//			Toast debugging=Toast.makeText(this,"Connection to server - remote_getUsersDetails - failed", Toast.LENGTH_LONG);
//			debugging.show();
			return null;
		}
				
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.replaceAll("good2goserver", "good2go");
		JSONResponse = JSONResponse.trim();
		
		//Parse the response from server
		User u=null;
		try{
			 u= new JSONDeserializer<User>().use(Date.class, new DateTransformer("yyyy.MM.dd.HH.aa.mm.ss.SSS")).deserialize(JSONResponse);
		}
		catch(Exception e){
			u=null;
		}
		return u;
    	
    }

    public static long remote_getUsersKarma(String username){
    	client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "getKarma");
		client.AddParam("userName", username);

		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
//			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersKarma- failed", Toast.LENGTH_LONG);
//			debugging.show();
			return -1;
		}
		String JSONResponse = client.getResponse();
		JSONResponse = JSONResponse.trim();
		
		//Parse the response from server
		long p=0;
		if(JSONResponse!=null){
			try{
				p = Long.parseLong(JSONResponse);
			}
			catch(NumberFormatException nfe){
				p=0;
			}
		}
		return p;
	}
    
    private void remote_registerToOccurrence(String username, String occurrenceKey) {
    	client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    	client.AddParam("action", "registerToOccurrence");
		client.AddParam("username", username);
		client.AddParam("occurrenceKey", occurrenceKey);
		client.AddParam("userDate", (new Date()).toString());
		
		try{
			client.Execute(1); //1 is HTTP GET
		}
		catch (Exception e){
//			Toast debugging=Toast.makeText(this,"Connection to server - faild", Toast.LENGTH_LONG);
//			debugging.show();
		}

    }
    
	private int remote_registerNewUser (String userName, String age, String sex, String city, String phone, String email){
		
		client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "??????");
		client.AddParam("userName", userName);
		client.AddParam("age", age);
		client.AddParam("sex", sex);
		client.AddParam("city", city);
		client.AddParam("phone", phone);
		client.AddParam("email", email);
		try{
			client.Execute(1); 
			return 1;
		}
		catch (Exception e){
			return 0;
		}
	}
}
