package com.gdma.good2go.ui;




import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gdma.good2go.R;
import com.gdma.good2go.communication.RestClient;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;



public class LoginNoAccounts extends Activity {

	private String selectedAccount;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_no_accounts);
		
		
		
	
		final EditText mEdit = (EditText) findViewById(R.id.email);
		final Button buttonGo = (Button) findViewById(R.id.goButton);
		   
	    buttonGo.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {
	        	
				
				selectedAccount = mEdit.getText().toString();
				newLocalUser(selectedAccount);
				
				
	        	setResult(Activity.RESULT_OK);
	        	finish();
	        	
	        }
	    });

	
	
	}
	
	
	
	private void saveLocalUsername(String userName){
		AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
		prefs.setUserName(userName);
//		SharedPreferences settings = getSharedPreferences("user_private_details", MODE_PRIVATE);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putString("userNameVal", userName);
//		editor.commit();
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
        
        updateServer(email, firstName, lastName, email, yearOfBirth);
        saveLocalUsername(email);
        
	}
	
	
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

	
}




