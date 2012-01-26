package com.gdma.good2go.ui;



import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.gdma.good2go.R;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;



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
		
		Spinner s = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, actsArr); 
		s.setAdapter(adapter);
		s.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
	
	
		final Button buttonGo = (Button) findViewById(R.id.goButton);
		   
	    buttonGo.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View view) {            
	    		//register user on the server
	            new registerUserOnServerTask().execute(selectedAccount);
	            //save user locally
	            saveLocalUsername(selectedAccount);   	
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
		AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
		prefs.setUserName(userName);
	}
	
	
    private Account[] getAccounts(Context cntxt){

		AccountManager am = AccountManager.get(cntxt);
   		Account[] accounts;
		accounts = am.getAccounts();
		Account[] tempAccs, tempAccs2;
		tempAccs = new Account[accounts.length];
		int j = 0;
		
		for (int i = 0; i<accounts.length; i++){
			if (accounts[i].name.contains("@")){
				tempAccs[j++] = accounts[i];
			}
		}
		
		
		boolean tst;
		int finCount=0;
		
		for (int i = 0; i<j; i++){
			tst = true;
			for (int k = 0; k<i; k++){
				if (tempAccs[i].name.equals(tempAccs[k].name)){
					tst = false;
					tempAccs[i] = null;
				}
			}
			if (tst == true){
				finCount++;
			}
		}
		
		tempAccs2 = new Account[finCount];
		j = 0;
		for (int i = 0; i<tempAccs.length; i++){
			if (tempAccs[i] != null){
				tempAccs2[j++] = tempAccs[i];
			}
		}
		
		return tempAccs2;
		
	}	

    /**THREADS*/		
    private class registerUserOnServerTask extends AsyncTask<String, Void, Integer> {
    	ProgressDialog dialog;

    	@Override
    	protected void onPreExecute(){
    		dialog = new ProgressDialog(Login.this);
    		dialog.setMessage(getString(R.string.welcome_register));
    		dialog.setIndeterminate(true);
    		dialog.setCancelable(false);
    		dialog.show();
	    }

    	protected void onPostExecute(Integer execResult) {
    		dialog.dismiss();
    		
	        setResult(RESULT_OK);
			finish();
    	}
			
		@Override
		protected Integer doInBackground(String... userDetails) {
    		RemoteFunctions rf = RemoteFunctions.INSTANCE;
    		
    		return rf.addUser(userDetails[0]);
			}
	
    }	
}




