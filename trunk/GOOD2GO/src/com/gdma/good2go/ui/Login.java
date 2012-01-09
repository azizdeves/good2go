/*
 * Copyright 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gdma.good2go.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarListActivity;
import com.gdma.good2go.communication.RestClient;

public class Login extends ActionBarListActivity {

    private boolean mAlternateTitle = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        
        
        getActionBarHelper().setRefreshActionItemState(true);
        
        //get userName from prefs
        //if userName == NULL then        
        if (getLocalUsername() == null){
        	newLocalUser();	
        }

        
        //go to main menu
        
  
    }



	private void newLocalUser() {
		Account[] accounts;
        String email = null, firstName = null, lastName = null, yearOfBirth = null;
        
        accounts = getAccounts(this);
        
        //list accounts
        // accounts[0].name is email address
        
        
        //get email from accounts list
        //get firstname
        //get lastname
        //get yearOfBirth
        
        if (updateServer(email, firstName, lastName, email, yearOfBirth) == 1){
        	//update local prefs
        	//move to menu page
        }
        else {
        	//go into anonymous mode
        	//move to menu page
        }
	}
    

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Yes. You're awesome.", Toast.LENGTH_SHORT).show();
                break;

//            case R.id.menu_refresh:
//                Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
//                getActionBarHelper().setRefreshActionItemState(true);
//                getWindow().getDecorView().postDelayed(
//                        new Runnable() 
//                        {
//                            @Override
//                            public void run() 
//                            {
//                                getActionBarHelper().setRefreshActionItemState(false);
//                            }
//                         },1000);
//                break;
//
//            case R.id.menu_search:
//                Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
//               
//                /**TESTTESTSTES*/
//         
//	            Intent newIntent = new Intent(this, ListTab.class);
//	            startActivity(newIntent);
//	            
//                /**TESTTESTSTES*/
//                break;
//
//            case R.id.menu_share:
//                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
	
	
    
	
	private void onCreateHelper(){
		
	}
	
	
	
    private void showToast(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
	
	private void saveLocalUsername(String userName){
		SharedPreferences settings = getSharedPreferences("savedUsername", MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userNameVal", userName);
		editor.commit();
	}
    
	private String getLocalUsername(){
		SharedPreferences settings = getSharedPreferences("savedUsername", MODE_PRIVATE);
		return settings.getString("userNameVal", null);
	}
    
}