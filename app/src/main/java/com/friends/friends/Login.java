package com.friends.friends;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.friends.friends.R;
import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements RestApiCallListener {

	ImageView btnFor_signIn,txtFor_signUp;
	EditText un, pass;
	TextView forgot,privacy;
	ProgressDialog pd;
	CheckBox checkBoxLogin;
	ArrayList<NameValuePair> nvp;
	SharedPreferences pref;
	private String filename = "login";

	private boolean writeToFile(String data) {
		try {
			FileOutputStream fOut = openFileOutput(filename, MODE_PRIVATE);
			fOut.write(data.getBytes());
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String readFromFile() {
		String temp="";
		try {
			FileInputStream fin = openFileInput(filename);
			int c;
			while( (c = fin.read()) != -1){
				temp = temp + Character.toString((char)c);
			}
		}
		catch(Exception e){
			return "";
		}
		return temp;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.g_currentActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		btnFor_signIn = (ImageView) findViewById(R.id.btnFor_signIn);
		txtFor_signUp = (ImageView) findViewById(R.id.textForSignUp);
		checkBoxLogin= (CheckBox) findViewById(R.id.checkBox);
		forgot=(TextView)findViewById(R.id.forgot);
		privacy=(TextView)findViewById(R.id.privacy);

		un = (EditText) findViewById(R.id.editForUsername);
		pass = (EditText) findViewById(R.id.editForPassward);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		String ppp = pref.getString("user_id_sf", "uidd");
		boolean remember_login = pref.getBoolean("remember_login", false);
		checkBoxLogin.setChecked(remember_login);



			String creds = readFromFile();
			if(!creds.equals("")) {
				String[] arr = creds.split(" ## ");
				un.setText(arr[0]);
				pass.setText(arr[1]);
			}



		//Utils.ShowToast(getApplicationContext(), ppp);
			if (ppp.equals("uidd") || !remember_login) {
			} else {
				Global.user_id = ppp;
				Intent in = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(in);
				finish();
			}
		btnFor_signIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (un.getText().toString().trim().equals("")) {
					Utils.ShowToast(getApplicationContext(), "Enter email");
				} else if (pass.getText().toString().trim().equals("")) {
					Utils.ShowToast(getApplicationContext(), "enter password");
				} else {
					if (Utils.isNetworkAvailable(getApplicationContext())) {
						startLogin();
					} else {
						Utils.showAlert(Login.this, "No Internet Connection");
					}
				}
			}
		});

		txtFor_signUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(getApplicationContext(), Register.class);
				startActivity(in);
			}
		});
		
		forgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent in = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
			startActivity(in);
			overridePendingTransition(0, 0);
			}
		});
		privacy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getApplicationContext(),PrivacyPolicyActivity.class);
				startActivity(in);
			}
		});

	}

	public void startLogin() {
		nvp = new ArrayList<NameValuePair>();
		nvp.add(new BasicNameValuePair("email", un.getText().toString().trim()));
		nvp.add(new BasicNameValuePair("password", pass.getText().toString()
				.trim()));
		RestApiCallPost post = new RestApiCallPost(Global.User_Login, this,
				nvp, 2);
		pd = Utils.show_ProgressDialog(Login.this);
		pd.show();
		post.start();
	}

	String response;

	@Override
	public void onResponse(String response, int pageId) {
		this.response = response;
		handler.sendEmptyMessage(1);
	}

	@Override
	public void onError(String error) {
		this.response = error;
		handler.sendEmptyMessage(2);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try {
					JSONObject jobj = new JSONObject(response);
					if (jobj.has("success")) {

						// 0 - for
						// private
						// mode
						Editor editor = pref.edit();
						editor.putString("user_id_sf", jobj.getString("id"));
						editor.putString("username", jobj.getString("username"));
						editor.putString("first_name", jobj.getString("first_name"));
						editor.putBoolean("remember_login",checkBoxLogin.isChecked());
						editor.commit();

						if(checkBoxLogin.isChecked()){
							writeToFile(un.getText().toString().trim()+" ## "+pass.getText().toString().trim());
						}


						String ismsgsent = jobj.optString("msg_success");
					    String sender_id =   jobj.optString("sender_id");
						String wow=sender_id;

						String id = jobj.getString("id");
						Global.user_id = id;
						Intent in = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(in);
						finish();
					} else if (jobj.has("error")) {
						String e1 = jobj.getString("error");
						Utils.showAlert(Login.this, e1);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				pd.dismiss();

				break;
			case 2:
				Utils.showAlert(Login.this, response);
				pd.dismiss();
				break;

			default:
				break;
			}

		};
	};




}
