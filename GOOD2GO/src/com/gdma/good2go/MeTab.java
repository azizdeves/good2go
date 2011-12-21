package com.gdma.good2go;

import java.util.List;

import android.R.drawable;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gdma.good2go.communication.RestClient;

import flexjson.JSONDeserializer;

public class MeTab extends Activity {
    
    drawable myPic;
    int points;
    String badget;
    String userName="";
    String userFirstName="Mor";
    String userLastName="Yogev";
    List<Event> usersEvents;
    RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);
        //GET USERS INFO FROM THE DB
        points=70;
        badget="mother tereza";
//      User u = getUsersDetails(userName);
//      userFirstName=u.getFirstName();
//      userLastName=u.getLastName();
//      usersEvents=getUsersEvent(userName);
//      points=getUsersPoints(userName);
//      badget=getUsersBadget(userName);        
        userName=userFirstName+" "+userLastName;
        TextView tv = (TextView) findViewById(R.id.userNameMeView);
        tv.setText(userName);
        SeekBar pointsProg = (SeekBar)findViewById(R.id.pointSeekMeView);
        pointsProg.setProgress(points);
        
    
    }
    
    private User getUsersDetails(String username){
		client.AddParam("action", "getUsersDetails");
		client.AddParam("username", username);
		
		String JSONResponse = client.getResponse();
		User u = new JSONDeserializer<User>().deserialize( JSONResponse );
		return u;
	}

    private List<Event> getUsersEvent(String username){
		client.AddParam("action", "getUsersEvent");
		client.AddParam("username", username);
		
		String JSONResponse = client.getResponse();
		List<Event> events = new JSONDeserializer<List<Event>>().deserialize( JSONResponse );
		return events;
	}

//    private ? getUsersKarma(String username){
//		client.AddParam("action", "getUsersKarma");
//		client.AddParam("username", username);
//		
//		String JSONResponse = client.getResponse();
//
//	}
}