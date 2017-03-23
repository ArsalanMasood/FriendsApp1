package com.friends.friends;
import com.friends.friends.R;

import utils.Global;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class First_Activity extends Activity implements OnClickListener {

	Button btnFor_sign, btnFor_register;

	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.g_currentActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first_);

		pref = getApplicationContext().getSharedPreferences("MyPref", 0);

		String ppp = pref.getString("user_id_sf", "uidd");
		// Utils.ShowToast(getApplicationContext(), ppp);
		if (ppp.equals("uidd")) {
		} else {
			Global.user_id = ppp;
			Intent in = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(in);
		}

		btnFor_sign = (Button) findViewById(R.id.btnFor_sign);
		btnFor_register = (Button) findViewById(R.id.btnFor_reg);

		btnFor_sign.setOnClickListener(this);
		btnFor_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnFor_sign:
			startActivity(new Intent(getApplicationContext(), Login.class));
			break;
		case R.id.btnFor_reg:
			startActivity(new Intent(getApplicationContext(), Register.class));
			break;

		default:
			break;
		}
	}

}
