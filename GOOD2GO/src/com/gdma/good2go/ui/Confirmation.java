	package com.gdma.good2go.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdma.good2go.Karma;
import com.gdma.good2go.R;
import com.gdma.good2go.actionbarcompat.ActionBarActivity;
import com.gdma.good2go.communication.RemoteFunctions;
import com.gdma.good2go.utils.AppPreferencesPrivateDetails;
import com.gdma.good2go.utils.EventsDbAdapter;

public class Confirmation extends ActionBarActivity{
	private static final String TAG = "Confirmation";
		
		private String mUserFirstName;
		private String mUserName;
		private String mEventName;
		private Long mEventId;
		private int mPoints;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.confirmation);
			
			mUserName=getLocalUsername();
			mUserFirstName=getLocalUserFirstname();
			
			Bundle extras = getIntent().getExtras();
			if (extras!=null)
			{
				mEventName = extras.getString("eventname");
				mEventId = Long.valueOf(extras.getString("event_id"));
				mPoints = extras.getInt("points");	
			}
			
			TextView thanksUser = (TextView) findViewById(R.id.thanks_confirmationView);
			TextView gotYou = (TextView) findViewById(R.id.gotYou_confirmationView);
			Button pointsNumber = (Button) findViewById(R.id.pointsWinNumber_confirmationView);
			TextView badgeMessage = (TextView) findViewById(R.id.badgesText_confirmationView);
			ImageView badgePic = (ImageView)findViewById(R.id.badgeImage_confirmationView);
			TextView badgeName= (TextView) findViewById(R.id.badgeName_confirmationView);

		  	thanksUser.setText("Thanks for being awesome " + mUserFirstName +"!");		  	
		  	gotYou.setText(getResources().getString(R.string.confirm_got_you) + " " + mEventName);		  	
		  	pointsNumber.setText(" " + Integer.toString(mPoints)+ " ");
			
		  	//badge message
		    String currentBadge = Karma.Badge.getMyBadge(mPoints).getName();
		  	long newPoints = RemoteFunctions.INSTANCE.getUserKarma(mUserName) + mPoints;
		  	String newBadge = Karma.Badge.getMyBadge(newPoints).getName();
		  	if(newBadge.compareTo(currentBadge)!=0){
		  		badgeMessage.setText(R.string.confirm_badge_win);
		  	}
		  	
		  	badgeName.setText(newBadge);
		  	
		  	int BadgeImage = getBadgeImage(newBadge);
		  	badgePic.setImageResource(BadgeImage); 		    
		}
		
		private String getLocalUserFirstname() {
			AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
			return prefs.getUserFirstName();
		}

		private String getLocalUsername(){
			AppPreferencesPrivateDetails prefs = new AppPreferencesPrivateDetails(this);
			return prefs.getUserName();
		}
	    
		private int getBadgeImage(String badge) {
			if (badge.compareTo(Karma.Badge.ANGEL.getName())==0)
				return R.drawable.badge_angel;
			if (badge.compareTo(Karma.Badge.BUDDHIST_MONK.getName())==0
					|| badge.compareTo(Karma.Badge.SAINT.getName())==0)
				return R.drawable.badge_buddhistmonk;
			if (badge.compareTo(Karma.Badge.DALAI_LAMA.getName())==0)
				return R.drawable.badge_dalailama;
			if (badge.compareTo(Karma.Badge.GOD.getName())==0)
				return R.drawable.badge_god;
			if (badge.compareTo(Karma.Badge.MOTHER_TERESA.getName())==0)
				return R.drawable.badge_mothertheresa;
			
			return R.drawable.badge_mrniceguy;			
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
            	Intent i = new Intent();
                i.putExtra("sender", TAG);
                i.putExtra(EventsDbAdapter.KEY_EVENTID, Long.valueOf(mEventId));
                setResult(RESULT_OK, i);
    			finish();
	        	break;
	        	
	        }	        
	        return super.onOptionsItemSelected(item);
	    }
	}
