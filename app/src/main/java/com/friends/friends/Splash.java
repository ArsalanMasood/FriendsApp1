package com.friends.friends;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Window;
import utils.Global;

public class Splash extends Activity {
	SharedPreferences mypreff;
	SharedPreferences.Editor prefEditors;
	String UserType;
	
	GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "506561330084";
    
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                Log.d("GCMTAGTAG", "Start");
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.d("GCMTAGTAG", msg);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("GCMTAGTAG", msg);
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Global.g_deviceToken = regid;
                Intent i = new Intent(getApplicationContext(),Login.class);
				startActivity(i);
				finish();
				overridePendingTransition(0, 0);
            }
        }.execute(null, null, null);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		Global.g_currentActivity = this;
		
		mypreff=PreferenceManager.getDefaultSharedPreferences(Splash.this);
		UserType = mypreff.getString("User_type_Login", "");
		
		getRegId();
	}
	
}
