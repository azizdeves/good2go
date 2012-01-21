	package com.gdma.good2go.ui;

	import java.util.Calendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;

public class PersonalDetailsTab extends ActionBarActivity{
	
	private static final String TAG = "PersonalDetails";
	private static final int MIN_YEAR = 1900;
	private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    
	TextView mFirstName;
	TextView mLastName;
	TextView mYOB;
	AutoCompleteTextView mCity;
	Spinner mSex;
	TextView mPhone;
	Button mButtonDone;
    

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.personal_details);
			
        	mFirstName = (TextView)
        			findViewById(R.id.nameData);
        	mLastName = (TextView)
        			findViewById(R.id.lastNameData);
        	mYOB = (TextView)
        			findViewById(R.id.yobData);
        	mCity = (AutoCompleteTextView)
	                 findViewById(R.id.cityData);
        	mSex = (Spinner)
        			findViewById(R.id.sexData);
        	mPhone = (TextView)
        			findViewById(R.id.phoneData);

			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, 
	                 getResources().getStringArray(R.array.cities_array));

			mCity.setAdapter(adapter);	    
		}
		
		private boolean fieldsValid() {
        	boolean fieldsOK = true;
        	
        	if (mFirstName.getText().toString().length()==0){
        		mFirstName.setError( "Please fill your first name" );
        		fieldsOK = false;
        	}
        	
        	if (mLastName.getText().toString().length()==0){
        		mLastName.setError( "Please fill your last name" );
        		fieldsOK = false;
        	}
        	
        	if (mYOB.getText().toString().length()==0){
        		mYOB.setError( "Please fill your year of birth" );
        		fieldsOK = false;
        	}
        	else
        	{
        		int yob = Integer.parseInt(mYOB.getText().toString());
            	if (yob < MIN_YEAR || yob > MAX_YEAR){
            		mYOB.setError( "Year of birth must be between " 
            				+ MIN_YEAR 
            				+" and "
            				+ MAX_YEAR);
            		fieldsOK = false;
            	}
        	}
        	
        	if (mCity.getText().toString().length()==0){
        		mCity.setError( "Please fill your city" );
        		fieldsOK = false;
        	}
        	
        	if (mPhone.getText().toString().length()==0){
        		mPhone.setError( "Please fill your phone" );
        		fieldsOK = false;
        	}
        	return fieldsOK;
		}
		
		
		
	    /**THREADS*/		
	    private class sendUserDetailsToServerTask extends AsyncTask<String, Void, Integer> {
	    	ProgressDialog dialog;

	    	@Override
	    	protected void onPreExecute(){
	    		dialog = new ProgressDialog(PersonalDetailsTab.this);
	    		dialog.setMessage(getString(R.string.sending_user_details));
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
        		
        		return rf.editUser(RemoteFunctions.EDIT_USER, 
        				userDetails[0], userDetails[1], userDetails[2], 
        				userDetails[3], userDetails[4], userDetails[5], 
        				userDetails[6]);
				}
		
	    }
			
		
		/** FOR ACTION BAR MENUS **/
		
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.menu.done, menu);
	        return super.onCreateOptionsMenu(menu);
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) 
	    {
	    	Intent newIntent = null;
	        switch (item.getItemId()) {
	        
	        case android.R.id.home:	
	        	newIntent = new Intent(this, MainActivity.class);
	        	newIntent.putExtra("sender", TAG);
	        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
	        	startActivity(newIntent);	
	        	break;
	        	
	        case R.id.menu_done:	
	        	if (fieldsValid()){
	            	String userFirstName = mFirstName.getText().toString();
	            	String userLastName = mLastName.getText().toString();
	            	String userYOB= mYOB.getText().toString();
	            	String userCity= mCity.getText().toString();
	            	String userSex=String.valueOf(mSex.getSelectedItem());
	            	String userPhone= mPhone.getText().toString();
	            	
	        		//save the details locally
	            	AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
	        		prefs.setAllPrivatePrefs(userFirstName, userLastName, userYOB, userCity, userSex, userPhone);
	        		
	        		//save the details to a server async              
			        new sendUserDetailsToServerTask().execute(
			        		prefs.getUserName(), 
			        		userFirstName, userLastName, userYOB, 
			        		userSex, userCity, userPhone);

	    			break;
	        	}
	        }
	        
	        return super.onOptionsItemSelected(item);
	      }
	  }
	
