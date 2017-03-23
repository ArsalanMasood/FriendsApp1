package com.friends.friends;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.applidium.shutterbug.cache.ImageCache;
import com.friends.friends.R;
import com.friends.friends.adapters.Friends_list_adapter;
import com.friends.friends.adapters.Friends_pics_list_adapter;
import com.friends.friends.model.FriendInfo;
import com.friends.friends.model.ModelImage;

public class TabFriendActivity extends Activity implements
		RestApiCallListener {





	ListView listFor_Friends;
	ListView listFor_FriendsPics;

	Friends_list_adapter adapter;
	Friends_pics_list_adapter adapterpic;

	ProgressDialog pd;

//	ProgressDialog pd2;
    EditText edtxtFilter;
	ImageView imgFilter;
	//List<String> urls =  new ArrayList<>();
	List<ModelImage> images= new ArrayList<>();
	FriendInfo gs;
	int _position=-1;
	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		_position=-1;
		edtxtFilter= (EditText) findViewById(R.id.edtFilter);
		listFor_Friends = (ListView) findViewById(R.id.tabfriendlist);
		listFor_FriendsPics = (ListView) findViewById(R.id.tabfriendlistpics);
		imgFilter = (ImageView) findViewById(R.id.img_filter);
		//listFor_Friends.setOnItemClickListener(this);
		//listFor_FriendsPics.setOnItemClickListener(this);

	
		ImageCache.getSharedImageCache(TabFriendActivity.this).clear();
		
		if(Global.g_friendList.size() == 0){
			Global.g_friendList = new  ArrayList<>();
			Global.g_curPhotoDownloadIndex = 0;
			
			if (Utils.isNetworkAvailable(getApplicationContext())) {
				RestApiCallPost post = new RestApiCallPost(Global.Get_Users_List,
						this, nvp, 3);
				pd = Utils.show_ProgressDialog(TabFriendActivity.this);
				pd.show();
				post.start();
			} else {
				Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
			}
		}else{
			if (Utils.isNetworkAvailable(getApplicationContext())) {
				RestApiCallPost post = new RestApiCallPost(Global.get_Images,
						this, nvp, 9);
//				pd2 = Utils.show_ProgressDialog(TabFriendActivity.this);
//				pd2.show();
				post.start();
			} else {
				Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
			}

			if(Global.g_downloadServiceIntent == null){
				Global.g_downloadServiceIntent = new Intent(this,DownloadService.class);
				startService(Global.g_downloadServiceIntent);
			}
		}


		imgFilter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//	adapter.filterGender("","Female");
				Intent intent = new Intent(TabFriendActivity.this,ActivityFilter.class);
				startActivityForResult(intent,100);
			}
		});

		adapter = new Friends_list_adapter(
				getApplicationContext(), Global.g_friendList);
		listFor_Friends.setAdapter(adapter);
	//	listFor_Friends.setOnItemClickListener(this);
		listFor_Friends.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FriendInfo friend =(FriendInfo)adapter.getItem(position);

				//		FriendInfo friend = Global.g_friendList.get(arg2);
				Intent in = new Intent(TabFriendActivity.this,
						ConversationActivity.class);
				in.putExtra("name",friend.getName());
				in.putExtra("rec_id", String.valueOf(friend.getUser_id()));
				in.putExtra("url",friend.getImg());
				startActivity(in);

			}
		});
		listFor_Friends.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String user_id = String.valueOf(Global.g_friendList.get(position).getUser_id());
				showDialog(user_id);
				return true;
			}
		});

		adapterpic = new Friends_pics_list_adapter(getApplicationContext(),images);
		listFor_FriendsPics.setAdapter(adapterpic);
		listFor_FriendsPics.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String url = images.get(position).getUrl();
				_position=position;
				if(Global.user_id.equals(images.get(position).getUserId())){
					CharSequence[] items = { "Report","Remove"};

					showReportDialog(url,items);
				}
				else{
					CharSequence[] items = {"Report"};

					showReportDialog(url,items);
				}


				return false;
			}
		});
	//	listFor_FriendsPics.setOnItemClickListener(this);
//		listFor_FriendsPics.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//				String user_id = String.valueOf(Global.g_friendList.get(position).getUser_id());
//				showDialog(user_id);
//				return true;
//			}
//		});

		edtxtFilter.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = edtxtFilter.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);

			}
		});
	}

	String response;

	@Override
	public void onResponse(String response, int pageId) {
		this.response = response;
		if (pageId == 100) {
			handler.sendEmptyMessage(3);
		}
		if (pageId == 150) {
			handler.sendEmptyMessage(6);
		}
		if (pageId == 160) {
			handler.sendEmptyMessage(16);
		}
		if(pageId==9){
			handler.sendEmptyMessage(9);

		}
		else {
			handler.sendEmptyMessage(1);
					if (Utils.isNetworkAvailable(getApplicationContext())) {
			RestApiCallPost post = new RestApiCallPost(Global.get_Images,
					this, nvp, 9);
	//		pd2 = Utils.show_ProgressDialog(TabFriendActivity.this);
	//		pd2.show();
			post.start();
		} else {
			Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
		}
		}
//		if (Utils.isNetworkAvailable(getApplicationContext())) {
//			RestApiCallPost post = new RestApiCallPost(Global.get_Images,
//					this, nvp, 9);
//	//		pd2 = Utils.show_ProgressDialog(TabFriendActivity.this);
//	//		pd2.show();
//			post.start();
//		} else {
//			Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
//		}

	}

	@Override
	public void onError(String error) {
		this.response = error;
		handler.sendEmptyMessage(2);
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			RestApiCallPost post = new RestApiCallPost(Global.get_Images,
					this, nvp, 9);
	//		pd2 = Utils.show_ProgressDialog(TabFriendActivity.this);
	//		pd2.show();
			post.start();
		} else {
			Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100){
			if(resultCode==RESULT_OK){
				String country=data.getStringExtra("country");
				String agemin=data.getStringExtra("agemin");
				String agemax=data.getStringExtra("agemax");
				String gender=data.getStringExtra("gender");
				//String name=data.getStringExtra("name");
				String relation=data.getStringExtra("relation");

				adapter.filterOther(gender,country,agemin,agemax,relation);

			}
		}

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:

				try {
					JSONArray jarray=new JSONArray(response);
					for(int i=0;i<jarray.length();i++){
						JSONObject jsoj=jarray.getJSONObject(i);
						gs = new FriendInfo();
						gs.setUser_id(Integer.parseInt(jsoj.getString("id")));
						gs.setName(jsoj.getString("first_name"));
						gs.setLastName(jsoj.getString("last_name"));
						gs.setImg(jsoj.getString("profile_pic"));
						gs.setThumb_img(jsoj.getString("thumb_pic"));
						gs.setCountry(jsoj.getString("country"));
						gs.setGender(jsoj.getString("sex"));
						gs.setSlogan(jsoj.getString("slogan"));
						gs.setRelation(jsoj.getString("relationship_status"));
						String strdate = jsoj.getString("dob");
						gs.setAge("");
						if(!strdate.equalsIgnoreCase("") && !strdate.equalsIgnoreCase("0000-00-00")){
							Date date = getDateFromString(strdate);
							if(date!=null){
						        gs.setAge(getAge(date)+"");
							}
						}
						gs.setThumb_img(jsoj.getString("thumb_pic"));
						gs.setPhotoDownloaded(false);
						gs.setLatitude(0); 
						gs.setLongitude(0);
						gs.setSlogan(jsoj.getString("slogan"));
						if (!jsoj.getString("lattitude").equals("")){
							gs.setLatitude(Double.valueOf(jsoj.getString("lattitude")));
						}
						if(!jsoj.getString("longitude").equals("")){
							gs.setLongitude(Double.valueOf(jsoj.getString("longitude")));
						}
						Global.g_friendList.add(gs);

					}
					Collections.sort(Global.g_friendList, new Comparator<FriendInfo>(){
						public int compare(FriendInfo friend1, FriendInfo friend2) {
							// ## Ascending order
							//return friend1.getFirstName().compareToIgnoreCase(friend1.getFirstName()); // To compare string values
							 return Integer.valueOf(friend2.getUser_id()).compareTo(friend1.getUser_id()); // To compare integer values

							// ## Descending order
							// return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
							// return Integer.valueOf(emp2.getId()).compareTo(emp1.getId()); // To compare integer values
						}
					});
					adapter.notifyDataSetChanged();
					
					if(Global.g_downloadServiceIntent == null){
						Global.g_downloadServiceIntent = new Intent(TabFriendActivity.this, DownloadService.class);
						startService(Global.g_downloadServiceIntent);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				pd.dismiss();
				break;
			case 2:
				Utils.showAlert(TabFriendActivity.this, response);
				pd.dismiss();
				break;
			case 3:
				try {
					JSONObject jsoj = new JSONObject(response);
					String res = jsoj.optString("response");
					if(res.equalsIgnoreCase("true")) {
						Toast.makeText(TabFriendActivity.this, "User Reported", Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(TabFriendActivity.this, "User coud not be reported", Toast.LENGTH_SHORT).show();
					}
				}catch (Exception e){
					e.getMessage();
					Toast.makeText(TabFriendActivity.this, "User coud not be reported", Toast.LENGTH_SHORT).show();
				}
					pd.dismiss();
					break;
			case 6:
					try {
						JSONObject jsoj = new JSONObject(response);
						String res = jsoj.optString("response");
						if(res.equalsIgnoreCase("true")) {
							Toast.makeText(TabFriendActivity.this, "Image Reported", Toast.LENGTH_SHORT).show();
						}
						else {
							Toast.makeText(TabFriendActivity.this, "Image coud not be reported", Toast.LENGTH_SHORT).show();
						}
					}catch (Exception e){
						e.getMessage();
						Toast.makeText(TabFriendActivity.this, "Image coud not be reported", Toast.LENGTH_SHORT).show();
					}
					pd.dismiss();
					break;
				case 16:
					try {
						JSONObject jsoj = new JSONObject(response);
						String res = jsoj.optString("response");
						if(res.equalsIgnoreCase("true")) {
							Toast.makeText(TabFriendActivity.this, "Image Removed", Toast.LENGTH_SHORT).show();
							images.remove(_position);
							adapterpic.notifyDataSetChanged();
						}
						else {
							Toast.makeText(TabFriendActivity.this, "Image coud not be removed", Toast.LENGTH_SHORT).show();
						}
					}catch (Exception e){
						e.getMessage();
						Toast.makeText(TabFriendActivity.this, "Image coud not be removed", Toast.LENGTH_SHORT).show();
					}
					pd.dismiss();
					break;
				case 9:
					try {
						images.clear();
						JSONArray jarray=new JSONArray(response);
						for(int i=0;i<jarray.length();i++){
							JSONObject jsoj=jarray.getJSONObject(i);
							String url =jsoj.optString("url");
							String user_id =jsoj.optString("user_id");

							ModelImage image = new ModelImage();
							image.setUrl(url);
							image.setUserId(user_id);
							images.add(image);

						}
						adapterpic.notifyDataSetChanged();

					} catch (JSONException e) {
						e.printStackTrace();
					}
	//				pd2.dismiss();
					break;

				default:
				break;
			}

		};
	};

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//	//	if(arg0.getId() == listFor_Friends.getId())
//		{
//			FriendInfo friend =(FriendInfo)adapter.getItem(arg2);
//
//	//		FriendInfo friend = Global.g_friendList.get(arg2);
//			Intent in = new Intent(this,
//					ConversationActivity.class);
//			in.putExtra("name",friend.getName());
//			in.putExtra("rec_id", String.valueOf(friend.getUser_id()));
//			in.putExtra("url",friend.getImg());
//			startActivity(in);
//		}
//	}


	private void showDialog(final String user_id){
		final CharSequence[] items = { "Report"};

		AlertDialog.Builder builder = new AlertDialog.Builder(TabFriendActivity.this);
		builder.setTitle("Options");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (Utils.isNetworkAvailable(getApplicationContext())) {
					nvp.clear();
					nvp.add(new BasicNameValuePair("user_id", user_id));

					RestApiCallPost post = new RestApiCallPost(Global.Report_User,
							TabFriendActivity.this, nvp, 100);
					pd = Utils.show_ProgressDialog(TabFriendActivity.this);
					pd.show();
					post.start();
				} else {
					Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
				}

			}
		});
		builder.show();
	}

	public void showReportDialog(final String url , CharSequence[] items){


		AlertDialog.Builder builder = new AlertDialog.Builder(TabFriendActivity.this);
		builder.setTitle("Options");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {

				if(item==0) {
					if (Utils.isNetworkAvailable(getApplicationContext())) {
						nvp.clear();
						nvp.add(new BasicNameValuePair("url", url));

						RestApiCallPost post = new RestApiCallPost(Global.Report_Image,
								TabFriendActivity.this, nvp, 150);
						pd = Utils.show_ProgressDialog(TabFriendActivity.this);
						pd.show();
						post.start();
					} else {
						Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
					}
				}
				else{
					if (Utils.isNetworkAvailable(getApplicationContext())) {
						nvp.clear();
						nvp.add(new BasicNameValuePair("url", url));

						RestApiCallPost post = new RestApiCallPost(Global.Remove_Image,
								TabFriendActivity.this, nvp, 160);
						pd = Utils.show_ProgressDialog(TabFriendActivity.this);
						pd.show();
						post.start();
					} else {
						Utils.showAlert(TabFriendActivity.this, "No Internet Connection");
					}
				}

			}
		});
		builder.show();
	}
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
