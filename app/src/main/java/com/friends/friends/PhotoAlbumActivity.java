package com.friends.friends;

import com.friends.friends.R;

import android.os.Bundle;
import utils.Global;
import android.app.Activity;

public class PhotoAlbumActivity extends Activity {
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        Global.g_currentActivity = this;
    }
}

/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_album);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_album, menu);
		return true;
	}

}
*/