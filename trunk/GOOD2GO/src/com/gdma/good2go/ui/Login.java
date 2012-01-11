package com.gdma.good2go.ui;



import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.gdma.good2go.R;
import com.gdma.good2go.communication.RestClient;



public class Login extends Activity {
	private String actsArr[];
	private String selectedAccount;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Account[] accounts = getAccounts(this);
		/*
		AccountManager am = AccountManager.get(this);
		String authTokType = "com.google";
		accounts = am.getAccountsByType(authTokType);
		*/
		actsArr = new String[accounts.length];
		for (int i = 0; i<accounts.length; i++){
			actsArr[i] = accounts[i].name.toString();
		}
		/*
		actsArr[0]=accounts[0].name.toString(); 
		actsArr[1]=accounts[1].name.toString(); 
		actsArr[2]="option 3"; 
		actsArr[3]="option 4"; 
		actsArr[4]="option 5"; 
*/
		
		Spinner s = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, actsArr); 
		s.setAdapter(adapter);
		s.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
	
	
		final Button buttonGo = (Button) findViewById(R.id.goButton);
		   
	    buttonGo.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	
				showToast("The account you selected is: " + selectedAccount);
				newLocalUser(selectedAccount);
				
				
	        	setResult(Activity.RESULT_OK);
	        	finish();
	        	
	        }
	    });

	
	
	}
	
	
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {    
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {      
			selectedAccount = parent.getItemAtPosition(pos).toString();		
		}    
		public void onNothingSelected(AdapterView<?> parent) {
			// Do nothing.    
		}
	}
	
	
	private void saveLocalUsername(String userName){
		SharedPreferences settings = getSharedPreferences("savedUsername", MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userNameVal", userName);
		editor.commit();
	}
	
	
    private Account[] getAccounts(Context cntxt){

		AccountManager am = AccountManager.get(cntxt);
   		Account[] accounts;
		String authTokType = "com.google";
		accounts = am.getAccountsByType(authTokType);
		return accounts;
		
	}
	
	private int updateServer (String userName, String email, String firstName, String lastName, String yearOfBirth){
		
		RestClient client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "addUser");
		client.AddParam("userName", userName);
		client.AddParam("firstName", firstName);
		client.AddParam("lastName", lastName);
		client.AddParam("email", email);
		client.AddParam("birthYear", yearOfBirth);
		
		

		try{
			client.Execute(1); //1 is HTTP GET
			return 1;
		}
		catch (Exception e){
			//Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersHistory- failed", Toast.LENGTH_LONG);
			//debugging.show();
			return 0;
		}
	}

	private void newLocalUser(String email) {
        String firstName = "", lastName = "", yearOfBirth = "";
        
        if (updateServer(email, firstName, lastName, email, yearOfBirth) == 1){
        	saveLocalUsername(email);
        	//move to menu page
        }
        else {
        	saveLocalUsername("Anonymous");
        	//move to menu page
        }
	}
	
	
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

	
}




