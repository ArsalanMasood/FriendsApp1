package com.friends.friends;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.friends.friends.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;
import utils.imagedownload.DownloadImageTask;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class MainActivity extends TabActivity{

	TabHost tabhost;
	TabHost.TabSpec spec;
	Intent intent;
	Slider sd;
	ImageView menu_slide,img_invite;
	SharedPreferences pref;

	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		Global.g_currentActivity = this;
		sd = new Slider(this, this);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);

		TabHost tabHost = getTabHost();

		menu_slide = (ImageView) findViewById(R.id.menu_slide);
		img_invite = (ImageView) findViewById(R.id.img_invite);

		// Tab for Photos
		TabSpec photospec = tabHost.newTabSpec("Friends");
		// setting Title and Icon for the Tab
		photospec.setIndicator("Friends",
				getResources().getDrawable(R.drawable.ic_launcher));
		Intent photosIntent = new Intent(this, TabFriendActivity.class);
		photospec.setContent(photosIntent);

		TabSpec songspec = tabHost.newTabSpec("Map");
		songspec.setIndicator("Map",
				getResources().getDrawable(R.drawable.ic_launcher));
		Intent songsIntent = new Intent(this, TabMapActivity.class);
		songspec.setContent(songsIntent);

		tabHost.addTab(photospec);
		tabHost.addTab(songspec);

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View v = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(20);
		}

		menu_slide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sd.opendrawer();
				
			}
		});

		img_invite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String first_name = pref.getString("first_name","");
				shareText("Friends App","Hey, join me, "+first_name+" on Friends App so we can chat https://play.google.com/store/apps/details?id=com.friends.friends");
			}
		});
		
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("id", Global.user_id));
			nvp.add(new BasicNameValuePair("token", Global.g_deviceToken));
			RestApiCallPost post = new RestApiCallPost(Global.Update_User_DeviceToken,
					new RestApiCallListener() {
						
						@Override
						public void onResponse(String response, int pageId) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onError(String error) {
							// TODO Auto-generated method stub
							
						}
					}, nvp, 10);
			post.start();
		} else {
			Utils.showAlert(this, "No Internet Connection");
		}
	}
	public  void shareText(String subject,String body) {
		Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
		txtIntent .setType("text/plain");
		txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
		startActivity(Intent.createChooser(txtIntent ,"Share"));
	}

}
