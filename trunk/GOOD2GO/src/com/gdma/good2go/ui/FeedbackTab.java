package com.gdma.good2go.ui;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RestClient;


public class FeedbackTab extends ActionBarActivity{
	private RatingBar mRating;
	private RestClient client = null;
	private String mOccurrenceKey="0";
	private String mUserName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		mRating=(RatingBar)findViewById(R.id.ratingBar_FeedbackView);// create RatingBar object
		mRating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,	boolean fromUser) {
				remote_setFeedback(mUserName, mOccurrenceKey, rating);
				
			}
		});
	}
	

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);	
    }
    
	/** FOR ACTION BAR MENUS **/
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.empty, menu);

        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Intent newIntent = null;
        switch (item.getItemId()) {
        
        case android.R.id.home:	
        	newIntent = new Intent(this, MainActivity.class);
        	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	startActivity(newIntent);	
        	break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    

    public int remote_setFeedback(String userName, String occurrenceKey,Float rating){
		client = new RestClient("http://good-2-go.appspot.com/good2goserver");
		client.AddParam("action", "addFeedback");
		client.AddParam("userName", userName);
		client.AddParam("occurrenceKey", occurrenceKey);
		client.AddParam("rating",Float.toString(rating));
		
		try{
			client.Execute(1); //1 is HTTP GET
			return 1;
		}
		catch (Exception e){
			Toast debugging=Toast.makeText(this,"Connection to server -remote_getUsersHistory- failed", Toast.LENGTH_LONG);
			debugging.show();
			return -1;   	
		}
    }
}