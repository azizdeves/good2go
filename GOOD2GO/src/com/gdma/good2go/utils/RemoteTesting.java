package com.gdma.good2go.utils;

import android.content.Context;

import com.gdma.good2go.Karma;
import com.gdma.good2go.communication.RemoteFunctions;

public enum RemoteTesting {
	INSTANCE;
	private AppPreferencesPrivateDetails mUserPrfes;
	private AppPreferencesEventsRetrievalDate mEventsRetrievalDate;
	private Context mC;
	private RemoteFunctions mRf;
	private String mUsername;
	
	
	
	public void testing(Context c)
	{
		mC=c;
		mUserPrfes = new AppPreferencesPrivateDetails(mC);
		mEventsRetrievalDate = new AppPreferencesEventsRetrievalDate(mC);
		mRf = RemoteFunctions.INSTANCE;
		mUsername = getLocalUsername();
		if (mUsername==null) mUsername="test@gmail.com";
		
		
		mRf.addUser(mRf.ADD_USER, getLocalUsername());
		mRf.addUserKarma(mRf.ADD_USER_KARMA, mUsername, Karma.ActionType.OPEN_ACCOUNT.name());
		mRf.addUserKarma(mRf.ADD_USER_KARMA, mUsername, Karma.ActionType.OPEN_ACCOUNT.name());
	}
	
	private String getLocalUserFirstname() {
		return mUserPrfes.getUserFirstName();
	}

	private String getLocalUsername(){
		return mUserPrfes.getUserName();
	}
}
