	package com.gdma.good2go.ui;

	import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.gdma.good2go.Karma;
import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.utils.EventsDbAdapter;
import com.gdma.good2go.utils.UsersUtil;

public class PersonalDetailsTab extends ActionBarActivity{
		private String mName = "Dina";
		private String mAge = "496351";
		private String mSex;
		private String mCity;
		private String mPhone;
		private String mEmail;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.personal_details);
			
			
		    
				
			Button buttonDone = (Button) findViewById(R.id.DonePrivateDetailsButton);
			buttonDone.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	TextView name = (TextView)findViewById(R.id.nameDetails_personalDetailsView);
	            	TextView age = (TextView)findViewById(R.id.ageDetails_personalDetailsView);
	            	TextView city = (TextView)findViewById(R.id.cityDetails_personalDetailsView);
	            	Spinner sex = (Spinner)findViewById(R.id.sexDetails_personalDetailsView);
	            	TextView phone = (TextView)findViewById(R.id.phoneDetails_personalDetailsView);
	            	TextView email = (TextView)findViewById(R.id.emailDetails_personalDetailsView);
	            	mName=name.getText()!=null ? name.getText().toString() : "";
	            	mAge= age.getText()!=null ?age.getText().toString()  : "";
	            	mCity= city.getText()!=null ? city.getText().toString()  : "";
	            	mSex=String.valueOf(sex.getSelectedItem());
	            	mPhone= phone.getText()!=null ? phone.getText().toString()  : "";
	            	mEmail= email.getText()!=null ? email.getText().toString()  : "";
	            	Intent i = new Intent();
	            	i.putExtra("name", mName);
	            	i.putExtra("age", mAge);
	            	i.putExtra("city", mName);
	            	i.putExtra("sex", mAge);
	            	i.putExtra("phone", mName);
	            	i.putExtra("email", mAge);
	                setResult(RESULT_OK, i);
	    			finish();
	             
		            //startActivity(i);
	            }
	        });
				    	
		    
		}

		

	}
