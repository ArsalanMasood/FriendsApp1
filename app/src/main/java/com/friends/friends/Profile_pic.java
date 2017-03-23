package com.friends.friends;

import com.friends.friends.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import utils.Global;

public class Profile_pic extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_profile_pic);
		Global.g_currentActivity = this;
	}

}
