package com.friends.friends;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.friends.friends.R;
import com.friends.friends.adapters.FriendListForComposer_Adapter;
import com.friends.friends.model.FriendInfo;

import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ComposeMsgActivity extends Activity implements RestApiCallListener {

	String rec_id;
	TextView toS;
	Dialog dialog;
	ListView lv;
	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
	Button button1;
	ProgressDialog pd;
	RestApiCallPost post;
	EditText msgS;

	FriendListForComposer_Adapter adapter;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.g_currentActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_compose_msg);
		toS = (TextView) findViewById(R.id.toS);
		button1 = (Button) findViewById(R.id.button1);
		
		adapter = new FriendListForComposer_Adapter(Global.g_friendList, this);
		msgS = (EditText) findViewById(R.id.msgS);
		
		pd = Utils.show_ProgressDialog(ComposeMsgActivity.this);

//		nvp.add(new BasicNameValuePair("user_id", Comman.user_id));
//		if (Utils.isNetworkAvailable(getApplicationContext())) {
//			post = new RestApiCallPost(Comman.Get_Users_List, this, nvp, 10);
//			post.start();
//		} else {
//			Utils.showAlert(ComposeMsgActivity.this, "No Internet Connection");
//		}
		
		try{
			
			String aaaa=getIntent().getExtras().getString("flagg", "");	
			String bbbb=getIntent().getExtras().getString("flagg1", "");	
			if(aaaa.equals("false") && bbbb.equals("false")){}
			else{
				toS.setText(bbbb);
				rec_id = aaaa;
			}
		}
		catch(Exception e){
		}

		toS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final Dialog dialog = new Dialog(ComposeMsgActivity.this);
				dialog.setContentView(R.layout.list);
				final EditText edtFilter = (EditText) dialog.findViewById(R.id.edtFilter);
				edtFilter.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						String text = edtFilter.getText().toString().toLowerCase(Locale.getDefault());
						adapter.filter(text);

					}
				});
				ListView lv = (ListView) dialog.findViewById(R.id.lv);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						FriendInfo friend =(FriendInfo)adapter.getItem(position);
						toS.setText((friend.getName() + " " + friend.getLastName()));
						rec_id = String.valueOf(Global.g_friendList.get(position).getUser_id());
						dialog.dismiss();
					}
				});
				lv.setAdapter(adapter);
				dialog.setCancelable(true);
				dialog.setTitle("Select Friend");
				dialog.show();

			}
		});

		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (msgS.getText().toString().trim().equals("")) {
					Utils.ShowToast(getApplicationContext(),
							"Enter Message First");
				}

				else {
					if (Utils.isNetworkAvailable(getApplicationContext())) {
						start_send();
					} else {
						Utils.showAlert(ComposeMsgActivity.this,
								"No Internet Connection");
					}
				}
			}
		});
	}

	public void start_send() {
		nvp.clear();
		nvp.add(new BasicNameValuePair("sender_id", Global.user_id));
		nvp.add(new BasicNameValuePair("receiver_id", rec_id));
		nvp.add(new BasicNameValuePair("msg_type", "1"));
		nvp.add(new BasicNameValuePair("message", msgS.getText().toString()
				.trim()));
		RestApiCallPost post = new RestApiCallPost(Global.send_message, this,
				nvp, 11);
		pd.show();
		post.start();
	}

	String response;

	@Override
	public void onResponse(String response, int pageId) {
		this.response = response;
		if (pageId == 11) {
			handler.sendEmptyMessage(2);
		} 
	}

	@Override
	public void onError(String error) {
		this.response = error;
		handler.sendEmptyMessage(3);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				try {
					JSONObject jobj = new JSONObject(response);
					String resppp = jobj.optString("success");
					if (resppp.equals("Message Send Successfully")) {
						Utils.ShowToast(getApplicationContext(), "success");
						toS.setText("");
						msgS.setText("");
					} else {
						Utils.ShowToast(getApplicationContext(), "fail");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Utils.showAlert(ComposeMsgActivity.this, response);
				pd.dismiss();
				break;

			case 3:
				Utils.showAlert(ComposeMsgActivity.this, response);
				break;

			default:
				break;
			}

		};
	};

}