package com.friends.friends;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.GPSTracker;
import utils.Utils;
import utils.customview.CircularImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applidium.shutterbug.cache.LruCache;
import com.friends.friends.model.FriendInfo;
import com.friends.friends.model.InboxUserInfo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import static com.google.android.gms.internal.zzs.TAG;

public class TabMapActivity extends FragmentActivity implements OnMarkerClickListener,
		RestApiCallListener, LocationListener {
	Dialog dialog1;
	String image,val;
	double latitude, longitude;
	// GPSTracker class
    GPSTracker gps;

	LocationManager locationManager;
	String provider;
	ProgressDialog pd;
	private double lat, lng;
	private GoogleMap googleMap;
	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
	
	int rand1;
	
	private int curMarkerIndex;
	private Circle mCircle;
	LruCache<String, BitmapDescriptor> cache;

	private void initCache()
	{
		//Use 1/8 of available memory
		cache = new LruCache<String, BitmapDescriptor>((int)(Runtime.getRuntime().maxMemory() / 1024 / 8));
	}

	public int randNumber(){
		Random r = new Random();
		rand1 = r.nextInt(10) + 1;
		return rand1;
	}

	private BroadcastReceiver mPhotoDownloadReceiver;
	private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

		View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
		ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
		markerImageView.setImageResource(resId);
		customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
		customMarkerView.buildDrawingCache();
		Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
		Drawable drawable = customMarkerView.getBackground();
		if (drawable != null)
			drawable.draw(canvas);
		customMarkerView.draw(canvas);
		return returnedBitmap;
	}
	public void RegisterDownloadBroadcastReceiver(){

		mPhotoDownloadReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				String action = intent.getAction();
				if(action.equals(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_MAP)){
					for(int i = 0; i < Global.g_friendList.size(); i++){
						if(Global.g_friendList.get(i).getMarker() != null && Global.g_friendList.get(i).getThumbBitmap() != null
								&& Global.g_friendList.get(i).isPhotoDownloaded() == true){
							Global.g_friendList.get(i).getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(Global.g_friendList.get(i).getThumbBitmap())));
//							Global.g_friendList.get(i).getMarker();
						}
					}
				}
			}
		};

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_tab);

		try {
			initCache();
			initilizeMap();
//			RegisterDownloadBroadcastReceiver();
//			if(Global.g_friendList.size() > 0){
//				handler.sendEmptyMessage(1);
//			}
//
//			gps = new GPSTracker(TabMapActivity.this);
//			// check if GPS enabled
//	        if(gps.canGetLocation()){
//	        	Random rand = new Random();
//
//	        	if(rand.nextBoolean()){
//	        		lat = gps.getLatitude() + rand.nextFloat()*.002;
//	        	}else{
//	        		lat = gps.getLatitude() - rand.nextFloat()*.002;
//	        	}
//	        	if(rand.nextBoolean()){
//	        		lng = gps.getLongitude() + rand.nextFloat()*.002;
//	        	}else{
//	        		lng = gps.getLongitude() - rand.nextFloat()*.002;
//	        	}
//
//	            try {
//	                val = address(lat, lng);
//	                } catch (IOException e) {
//	                e.printStackTrace();
//	                }
//	        }else{
//	            gps.showSettingsAlert();
//	        }
//
//	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//	        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//	        googleMap.getUiSettings().setZoomGesturesEnabled(true);
//			Criteria criteria = new Criteria();
//			provider = locationManager.getBestProvider(criteria, false);
//			if (provider != null && !provider.equals("")) {
//				// Get the location from the given provider
//				Location location = locationManager.getLastKnownLocation(provider);
//				locationManager.requestLocationUpdates(provider, 100, 10, this);
//				if (location != null)
//					onLocationChanged(location);
//			} else {
//				Toast.makeText(getBaseContext(), "No Provider Found",
//						Toast.LENGTH_SHORT).show();
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

// 	  LatLng latLng = new LatLng(lat, lng);
//	  drawMarkerWithCircle(latLng);
//
//	  googleMap.setOnMarkerClickListener(TabMapActivity.this);
	}

	String response;

	@Override
	public void onResponse(String response, int pageId) {
		this.response = response;
		if (pageId == 4) {	//Get Chat msg list
			handler.sendEmptyMessage(3);
		}
		else {
			handler.sendEmptyMessage(1);
		}
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
				for(int i = 0; i < Global.g_friendList.size(); i++){
					FriendInfo friend = Global.g_friendList.get(i);
					
					LatLng MELBOURNE = new LatLng(friend.getLatitude(), friend.getLongitude());
//					Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//					Bitmap bmp = Bitmap.createBitmap(50, 50, conf);
//					Canvas canvas1 = new Canvas(bmp);
//
//					Paint color = new Paint();
//					color.setTextSize(35);
//					color.setColor(Color.BLACK);
//
//					canvas1.drawBitmap(BitmapFactory.decodeResource(
//							getResources(), R.drawable.pickselect), 0, 0,
//							color);
//					canvas1.drawText("", 30, 40, color);
//
					Marker marker = null;
//					if (friend.getThumb_img().equals("")) {
//						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(bmp)).anchor(0.5f, 1));
//					}else if (friend.getThumbBitmap() != null && friend.isPhotoDownloaded() == true){
//						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(friend.getThumbBitmap()))));
//					}else{
//						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(bmp))));
//					}
					if (friend.getThumb_img().equals("")) {
				//		marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.pickselect))).anchor(0.5f, 1));


						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).anchor(0.5f, 1));
					}else if (friend.getThumbBitmap() != null && friend.isPhotoDownloaded() == true){
						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(friend.getThumbBitmap()))));

						//				marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(friend.getThumbBitmap()))));
					}

					else{
			//			marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.pickselect))));

								marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
					}
					Global.g_friendList.get(i).setMarker(marker);

				}
	            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng) , 16.0f) );
				break;
			case 2:
				Utils.showAlert(TabMapActivity.this, response);
				pd.dismiss();
				break;

				case 3:
					try {
						boolean found=false;
						String id="";
						String name="";
						String imag="";
						//	Toast.makeText(Messages.this,"2",Toast.LENGTH_SHORT).show();
						JSONArray jarray = new JSONArray(response);

						String chhh = jarray.getJSONObject(0).optString("error");
						if (chhh.equals("No Record Found")) {
							Utils.showAlert(TabMapActivity.this, "No Record Found");
						} else {

							FriendInfo friend = Global.g_friendList.get(curMarkerIndex);
							for (int i = 0; i < jarray.length(); i++) {
								JSONObject jobj = jarray.getJSONObject(i);
								InboxUserInfo gs = new InboxUserInfo();
								id= jobj.optString("user_id");
								imag= jobj.optString("profile_pic");
								name= jobj.optString("first_name");
								if(id.equals(String.valueOf(friend.getUser_id()))){

									found=true;
									break;

								}
							}
						}
						//	Toast.makeText(Messages.this,"4",Toast.LENGTH_SHORT).show();
						pd.dismiss();
						if(found){
							found=false;
							Intent in = new Intent(getApplicationContext(),
									ConversationActivity.class);
							in.putExtra("rec_id",id);
							in.putExtra("text","");
							in.putExtra("name",name);
							in.putExtra("url",imag);
							startActivity(in);
							dialog1.dismiss();
						}
						else{
							showDialog();
						}


					} catch (JSONException e) {
						//	Toast.makeText(Messages.this,e.getMessage(),Toast.LENGTH_LONG).show();
						//	Log.e("error1",e.getMessage());
						e.printStackTrace();
					}

					break;

			default:
				break;
			}
		};
	};
	private Marker addMarker(LatLng position, String assetPath)
	{
		Marker marker=null;
		MarkerOptions opts = new MarkerOptions();
		opts.icon(getBitmapDescriptor(assetPath));
		opts.position(position);
		try{
		marker =googleMap.addMarker(opts);
	}
		catch (Exception e){
			e.getMessage();
		}
	return marker;
	}


	private BitmapDescriptor getBitmapDescriptor(String path) {
		BitmapDescriptor result = cache.get(path);
		if (result == null) {
			result = BitmapDescriptorFactory.fromAsset(path);
			cache.put(path, result);
		}

		return result;
	}
	@SuppressLint("NewApi")
	private void initilizeMap() {
		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.tabmap)).getMap();
			 ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
					R.id.tabmap)).getMapAsync(new OnMapReadyCallback() {
				 @Override
				 public void onMapReady(GoogleMap _googleMap) {
					 googleMap=_googleMap;

					 RegisterDownloadBroadcastReceiver();
					 if(Global.g_friendList.size() > 0){
						 handler.sendEmptyMessage(1);
					 }

					 gps = new GPSTracker(TabMapActivity.this);
					 // check if GPS enabled
					 if(gps.canGetLocation()){
						 Random rand = new Random();

						 if(rand.nextBoolean()){
							 lat = gps.getLatitude() + rand.nextFloat()*.002;
						 }else{
							 lat = gps.getLatitude() - rand.nextFloat()*.002;
						 }
						 if(rand.nextBoolean()){
							 lng = gps.getLongitude() + rand.nextFloat()*.002;
						 }else{
							 lng = gps.getLongitude() - rand.nextFloat()*.002;
						 }

						 try {
							 val = address(lat, lng);
						 } catch (IOException e) {
							 e.printStackTrace();
						 }
					 }else{
//						 displayLocationSettingsRequest(TabMapActivity.this);
						 if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
							 displayLocationSettingsRequest(TabMapActivity.this);
						 }
						else{
							 gps.showSettingsAlert();
						 }

					 }

					 locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					 googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					 googleMap.setMyLocationEnabled(true);
					 googleMap.getUiSettings().setZoomControlsEnabled(true);
					 googleMap.getUiSettings().setZoomGesturesEnabled(true);
					 Criteria criteria = new Criteria();
					 provider = locationManager.getBestProvider(criteria, false);
					 if (provider != null && !provider.equals("")) {
						 // Get the location from the given provider
						 Location location = locationManager.getLastKnownLocation(provider);
						 locationManager.requestLocationUpdates(provider, 100, 10, TabMapActivity.this);
						 if (location != null)
							 onLocationChanged(location);
					 } else {
						 Toast.makeText(getBaseContext(), "No Provider Found",
								 Toast.LENGTH_SHORT).show();
					 }
					 LatLng latLng = new LatLng(lat, lng);
					 drawMarkerWithCircle(latLng);

					 googleMap.setOnMarkerClickListener(TabMapActivity.this);

				 }
			 });

			// check if map is created successfully or not
//			if (googleMap == null) {
//				Toast.makeText(getApplicationContext(),
//						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
//						.show();
//			}
			
		}
	}

	//click event
	@Override
	public boolean onMarkerClick(Marker arg0) {



		int position =  -1;
		for(int i = 0; i < Global.g_friendList.size(); i++){
			if(Global.g_friendList.get(i).getMarker() != null){
				if(Global.g_friendList.get(i).getMarker().getId().equals(arg0.getId())){
					position = i;
					break;
				}
			}
		}

		if(position == -1){
			return false;
		}else{
			curMarkerIndex = position;
		}
		final FriendInfo friend = Global.g_friendList.get(curMarkerIndex);
		String n_first = friend.getName();

		String n_last = friend.getLastName();
		String n_slogan = friend.getSlogan();

		String n_age = friend.getAge();
		String n_relation = friend.getRelation();
		String n_gender = friend.getGender();
		String n_country = friend.getCountry();

		dialog1 = new Dialog(TabMapActivity.this);

		dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog1.setCancelable(true);
		dialog1.setCanceledOnTouchOutside(true);

		dialog1.setContentView(R.layout.dialog_send_message);
		TextView fname = (TextView) dialog1.findViewById(R.id.fname);
		TextView country = (TextView) dialog1.findViewById(R.id.country);
		TextView lname = (TextView) dialog1.findViewById(R.id.lname);
		TextView slogan = (TextView) dialog1.findViewById(R.id.slogan);
		TextView age = (TextView) dialog1.findViewById(R.id.age);
		TextView gender = (TextView) dialog1.findViewById(R.id.gender);
		TextView relation = (TextView) dialog1.findViewById(R.id.relation);

		age.setVisibility(View.GONE);
		relation.setVisibility(View.GONE);
		gender.setVisibility(View.GONE);
		country.setVisibility(View.GONE);

		CircularImageView image_profile = (CircularImageView) dialog1.findViewById(R.id.imageView1);
		Button send_message = (Button) dialog1.findViewById(R.id.add2);
		fname.setText(n_first);
		lname.setText(n_last);
		slogan.setText(n_slogan);
		if(!n_age.equals("")){
			age.setText(n_age);
			age.setVisibility(View.VISIBLE);
		}

		if(!n_country.equals("")){
			country.setText(n_country);
			country.setVisibility(View.VISIBLE);
		}
		if(!n_gender.equals("")){
			gender.setText(n_gender);
			gender.setVisibility(View.VISIBLE);
		}

		if(!n_relation.equals("")){
			relation.setText(n_relation);
			relation.setVisibility(View.VISIBLE);
		}

		dialog1.show();

//		if(friend.getThumb_img() != null && friend.isPhotoDownloaded() ==  true){
//			image_profile.setImageBitmap(friend.getThumbBitmap());
//		}else{
//			try {
//		        URL url = new URL(friend.getImg());
//		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		        connection.setDoInput(true);
//		        connection.connect();
//		        InputStream input = connection.getInputStream();
//		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
//		        image_profile.setImageBitmap(myBitmap);
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//		}

		if(!friend.getImg().isEmpty()) {
			Picasso.with(this)
					.load(friend.getImg())
					.placeholder(R.drawable.pickselect)
					.into(image_profile);
		}
		image_profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TabMapActivity.this,PictureDetailActivity.class);
				intent.putExtra("url",friend.getImg());
				startActivity(intent);
			}
		});



		 send_message.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub





				nvp.add(new BasicNameValuePair("user_id", Global.user_id));

				if (Utils.isNetworkAvailable(getApplicationContext())) {
					//	Toast.makeText(Messages.this,"1",Toast.LENGTH_SHORT).show();
					RestApiCallPost post = new RestApiCallPost(
							Global.get_conversation_user, TabMapActivity.this, nvp, 4);
					pd = Utils.show_ProgressDialog(TabMapActivity.this);
					pd.show();
					post.start();
				} else {
					showDialog();
				}





			}
		});
		return true;
	}


	private void showDialog(){

		FriendInfo friend = Global.g_friendList.get(curMarkerIndex);
		Intent in = new Intent(getApplicationContext(),	ComposeMsgActivity.class);
		in.putExtra("flagg", String.valueOf(friend.getUser_id()));
		in.putExtra("flagg1", friend.getName() + " " + friend.getLastName());
		startActivity(in);
	}
	
	private Bitmap getCircleBitmap(Bitmap src)
	{
		Bitmap result = null;
		Bitmap copyBitmap = null;
	    try {
	    	copyBitmap = Bitmap.createScaledBitmap(src, 100, 100, true);
	        result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(result);

	        int color = 0xff424242;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, 200, 200);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawCircle(50, 50, 50, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(copyBitmap, rect, rect, paint);

	    } catch (NullPointerException e) {
	    } catch (OutOfMemoryError o) {
	    }
	    return result;
	}
	
	public void onLocationChanged(Location location) {
//		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();
			        //Move the camera to the user's location and zoom in!

		//	mCircle.setCenter(new LatLng(latitude, longitude));
		//	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16.0f));



	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	
	public String address(double lt,double lg) throws IOException{
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(lt, lg, 1);

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getAddressLine(1);
        String country = addresses.get(0).getAddressLine(2);
        
        return address +"\n"+ city +"\n"+ country;
	}


	private void displayLocationSettingsRequest(Context context) {
		GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
				.addApi(LocationServices.API).build();
		googleApiClient.connect();

		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(10000);
		locationRequest.setFastestInterval(10000 / 2);

		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
		builder.setAlwaysShow(true);

		PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
		result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
			@Override
			public void onResult(LocationSettingsResult result) {
				final Status status = result.getStatus();
				switch (status.getStatusCode()) {
					case LocationSettingsStatusCodes.SUCCESS:
						Log.i(TAG, "All location settings are satisfied.");
						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

			//			gps.showSettingsAlert();
						try {
						//	 Show the dialog by calling startResolutionForResult(), and check the result
						//	 in onActivityResult().
							status.startResolutionForResult(TabMapActivity.this, 4865);
						} catch (IntentSender.SendIntentException e) {
							Log.i(TAG, "PendingIntent unable to execute request.");
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
						break;
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
		switch (requestCode) {
			case 485:
				switch (resultCode) {
					case Activity.RESULT_OK:
						// All required changes were successfully made

						break;
					case Activity.RESULT_CANCELED:
						// The user was asked to change settings, but chose not to

						break;
					default:
						break;
				}
				break;
		}
	}
	private void drawMarkerWithCircle(LatLng position){
	    double radiusInMeters = 10.0;
	    int strokeColor = 0xffff0000; //red outline
	    int shadeColor =0x5500ff00; //opaque red fill
	   
	    CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
	    mCircle = googleMap.addCircle(circleOptions);
	}
	@Override
	public void onResume(){
		super.onResume();
try {
	if(mPhotoDownloadReceiver!=null){
	LocalBroadcastManager.getInstance(this).registerReceiver(mPhotoDownloadReceiver, new IntentFilter(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_MAP));
}}
catch (Exception e){
	e.getMessage();
}
	}
 		
	@Override
	public void onPause(){
		try {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(mPhotoDownloadReceiver);
		}
		catch (Exception e){
			e.getMessage();
		}
		super.onPause();
	}
 }
