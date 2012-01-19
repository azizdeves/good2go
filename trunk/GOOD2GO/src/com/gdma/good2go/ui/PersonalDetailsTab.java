	package com.gdma.good2go.ui;

	import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;

public class PersonalDetailsTab extends ActionBarActivity{
	
    private static final String[] COUNTRIES = new String[] {
        "Belgium", "France", "Italy", "Germany", "Spain"
    };
    
	TextView mFirstName;
	TextView mLastName;
	TextView mAge;
	AutoCompleteTextView mCity;
	Spinner mSex;
	TextView mPhone;
	Button mButtonDone;
    

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.personal_details);
			
        	mFirstName = (TextView)
        			findViewById(R.id.nameDetails_personalDetailsView);
        	mLastName = (TextView)
        			findViewById(R.id.nameDetails_personalDetailsView2);
        	mAge = (TextView)
        			findViewById(R.id.ageDetails_personalDetailsView);
        	mCity = (AutoCompleteTextView)
	                 findViewById(R.id.cityDetails_personalDetailsView);
        	mSex = (Spinner)
        			findViewById(R.id.sexDetails_personalDetailsView);
        	mPhone = (TextView)
        			findViewById(R.id.phoneDetails_personalDetailsView);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	                 android.R.layout.simple_dropdown_item_1line, COUNTRIES);

			mCity.setAdapter(adapter);
					    
				
			mButtonDone = (Button) findViewById(R.id.DonePrivateDetailsButton);
			mButtonDone.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	
	            	if (fieldsValid()){
		            	String userFirstName = mFirstName.getText().toString();
		            	String userLastName = mLastName.getText().toString();
		            	String userAge= mAge.getText().toString();
		            	String userCity= mCity.getText().toString();
		            	String userSex=String.valueOf(mSex.getSelectedItem());
		            	String userPhone= mPhone.getText().toString();
		            	
		        		AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(v.getContext());
		        		prefs.setAllPrivatePrefs(userFirstName, userLastName, userAge, userCity, userSex, userPhone);
		            	
		                setResult(RESULT_OK);
		    			finish();
	            	}
	            }

				private boolean fieldsValid() {
	            	boolean fieldsOK = true;
	            	
	            	if (mFirstName.getText().toString().length()==0){
	            		mFirstName.setError( "First name is required" );
	            		fieldsOK = false;
	            	}
	            	
	            	if (mLastName.getText().toString().length()==0){
	            		mLastName.setError( "Last name is required" );
	            		fieldsOK = false;
	            	}
	            	
	            	if (mAge.getText().toString().length()==0){
	            		mAge.setError( "Age is required" );
	            		fieldsOK = false;
	            	}
	            	
	            	if (mCity.getText().toString().length()==0){
	            		mCity.setError( "City is required" );
	            		fieldsOK = false;
	            	}
	            	
	            	if (mPhone.getText().toString().length()==0){
	            		mPhone.setError( "City is required" );
	            		fieldsOK = false;
	            	}
	            	return fieldsOK;
				}
	        });		    
		}
	}
