package com.friends.friends;

import utils.Global;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.friends.friends.R;
import com.navdrawer.SimpleSideDrawer;
public class Slider {
	Intent in;
	Button homeM, profileM, messegeM, logoutM;
	Context c;
	Activity act;
	SimpleSideDrawer simpleside;

	public Slider(Activity act, Context c) {
		this.c = c;
		this.act = act;
		simpleside = new SimpleSideDrawer(this.act);
		simpleside.setLeftBehindContentView(R.layout.slider_layout);
	}
	
	
	

	public void close_drawer() {
		simpleside.closeLeftSide();
	}

	public Boolean isdrawerclosed() {
		return simpleside.isClosed();
	}

	
	
	public void opendrawer() {
	/*	simpleside.toggleLeftDrawer();
	*/
		simpleside.openLeftSide();
		simpleside.setKeepScreenOn(true);

		homeM = (Button) act.findViewById(R.id.homeM);
		profileM = (Button) act.findViewById(R.id.profileM);
		messegeM = (Button) act.findViewById(R.id.messegeM);
		logoutM = (Button) act.findViewById(R.id.logoutM);

		homeM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				in = new Intent(c, MainActivity.class);
				act.startActivity(in);
				// act.finish();
			}
		});
		profileM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				in = new Intent(c, ProfileInfo.class);
				act.startActivity(in);
				// act.finish();
			}
		});
		messegeM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				in = new Intent(c, Messages.class);
				act.startActivity(in);
				// act.finish();
			}
		});
		logoutM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences pref = c.getSharedPreferences("MyPref", 0); // 0
				Editor editor = pref.edit();
				editor.remove("user_id_sf");
				// will delete key email
				editor.commit();
				Global.user_id = "";
				in = new Intent(c, Login.class);
				act.startActivity(in);
				act.finish();
			
			}
		});
	}
	
	
}
