package com.friends.friends;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.applidium.shutterbug.cache.ImageCache;
import com.friends.friends.R;
import com.friends.friends.adapters.Messages_Adapter;
import com.friends.friends.model.InboxUserInfo;

public class Messages extends Activity implements RestApiCallListener {
	ProgressDialog pd;
	ListView messegeC;
	ImageView menu_slide, compose_button;
	Slider sd;
	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
	Messages_Adapter msgadapter;
	ArrayList<InboxUserInfo> msg1 = new ArrayList<InboxUserInfo>();
	Uri imageUri;
	String sharedText;
	SharedPreferences pref;

	void handleSendText(Intent intent) {
		sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (sharedText != null) {
			// Update UI to reflect text being shared
		}
	}

	void handleSendImage(Intent intent) {
		imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_messages);
		Global.g_currentActivity = this;

		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		String ppp = pref.getString("user_id_sf", "");
		Global.user_id=ppp;
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();

		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			} else if (type.startsWith("image/")) {
				handleSendImage(intent); // Handle single image being sent
			}
		} else 	if ("com.friends.friends.newmessage".equals(action)){

			Bundle b = getIntent().getExtras();
			if(b!=null) {
				if (b.containsKey("id")) {
					Intent in = new Intent(getApplicationContext(),
							ConversationActivity.class);
					in.putExtra("rec_id", b.getString("id"));
					in.putExtra("name",b.getString("name"));
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					in.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(in);
					finish();

				}
			}

		}
		else{

		}

		ImageCache.getSharedImageCache(Messages.this).clear();
		
		menu_slide = (ImageView) findViewById(R.id.menu_slide);
		messegeC = (ListView) findViewById(R.id.messegeC);
		compose_button = (ImageView) findViewById(R.id.compose_button);
		sd = new Slider(this, this);

		msgadapter = new Messages_Adapter(msg1, getApplicationContext());
		messegeC.setAdapter(msgadapter);
		msgadapter.notifyDataSetChanged();

		messegeC.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent in = new Intent(getApplicationContext(),
						ConversationActivity.class);
				in.putExtra("rec_id", msg1.get(arg2).getUser_id());

				in.putExtra("name",msg1.get(arg2).getFname());
				in.putExtra("url",msg1.get(arg2).getProfile_pic());
				if(imageUri!=null) {
					in.putExtra("image", imageUri.toString());
					finish();
				}
				else if(sharedText!=null){
					if(!sharedText.isEmpty()) {
						in.putExtra("text", sharedText);
						finish();
					}
				}
				startActivity(in);
			}
		});

		if(!Global.user_id.equals("")) {
			// change to thiss
			nvp.add(new BasicNameValuePair("user_id", Global.user_id));
		}
		else{
			Intent in = new Intent(Messages.this,Login.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			in.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(in);
			finish();
		}
		// nvp.add(new BasicNameValuePair("user_id", "1"));

		if (Utils.isNetworkAvailable(getApplicationContext())) {
		//	Toast.makeText(Messages.this,"1",Toast.LENGTH_SHORT).show();
			RestApiCallPost post = new RestApiCallPost(
					Global.get_conversation_user, this, nvp, 4);
			pd = Utils.show_ProgressDialog(Messages.this);
			pd.show();
			post.start();
		} else {
			Utils.showAlert(Messages.this, "No Internet Connection");
		}

		menu_slide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sd.opendrawer();
			}
		});

		compose_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent in = new Intent(getApplicationContext(),
						ComposeMsgActivity.class);
				in.putExtra("flagg", "false");
				in.putExtra("flagg1", "false");
				
				startActivity(in);
			}
		});




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

				//	Toast.makeText(Messages.this,"2",Toast.LENGTH_SHORT).show();
					JSONArray jarray = new JSONArray(response);

					String chhh = jarray.getJSONObject(0).optString("error");
					if (chhh.equals("No Record Found")) {
						Utils.showAlert(Messages.this, "No Record Found");
					} else {
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject jobj = jarray.getJSONObject(i);
							InboxUserInfo gs = new InboxUserInfo();
							gs.setUser_id(jobj.optString("user_id"));

							gs.setGender(jobj.getString("sex"));
							gs.setCountry(jobj.getString("country"));
							gs.setSlogan(jobj.getString("slogan"));
							gs.setRelation(jobj.getString("relationship_status"));
							String strdate = jobj.getString("dob");
							gs.setAge("");
							if(!strdate.equalsIgnoreCase("") && !strdate.equalsIgnoreCase("0000-00-00")){
								Date date = getDateFromString(strdate);
								if(date!=null){
									gs.setAge(getAge(date)+"");
								}
							}

						//	gs.setUser_id(jobj.optString("user_id"));
						//	gs.setUser_id(jobj.optString("user_id"));
						//	gs.setUser_id(jobj.optString("user_id"));

							gs.setFname(jobj.optString("first_name"));
							gs.setLname(jobj.optString("last_name"));
							gs.setEmail(jobj.optString("email"));
							gs.setProfile_pic(jobj.optString("profile_pic"));
							
							msg1.add(gs);
						}
				//		Toast.makeText(Messages.this,"3",Toast.LENGTH_SHORT).show();
						msgadapter.notifyDataSetChanged();
					}
				//	Toast.makeText(Messages.this,"4",Toast.LENGTH_SHORT).show();
					pd.dismiss();

				} catch (JSONException e) {
				//	Toast.makeText(Messages.this,e.getMessage(),Toast.LENGTH_LONG).show();
				//	Log.e("error1",e.getMessage());
					e.printStackTrace();
				}

				break;
			case 2:
				Utils.showAlert(Messages.this, response);
				pd.dismiss();
				break;

			default:
				break;
			}

		};
	};
	
	@Override
	public void onBackPressed() {
		if(sd.isdrawerclosed())
		{
			super.onBackPressed();
		}
		else
		{
		sd.close_drawer();	
		}
	};
	private int getAge(Date dateOfBirth) {

		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();

		int age = 0;

		birthDate.setTime(dateOfBirth);
		if (birthDate.after(today)) {

			//		throw new IllegalArgumentException("Can't be born in the future");
		}

		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

		// If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
		if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
				(birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
			age--;

			// If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
		}else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
				(birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
			age--;
		}

		return age;
	}

	private Date getDateFromString(String strDate){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(strDate);
			System.out.println(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

}