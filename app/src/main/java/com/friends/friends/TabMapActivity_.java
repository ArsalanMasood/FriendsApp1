//package com.friends.friends;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//
//import org.apache.http.NameValuePair;
//
//import restetapi.RestApiCallListener;
//import utils.Global;
//import utils.GPSTracker;
//import utils.Utils;
//import utils.customview.CircularImageView;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.PorterDuff.Mode;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.location.Address;
//import android.location.Criteria;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.friends.friends.model.FriendInfo;
//import com.friends.friends.model.FriendMarkerItem;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.model.Circle;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.maps.android.clustering.ClusterManager;
//
//public class TabMapActivity_ extends Activity implements OnMarkerClickListener,
//		RestApiCallListener, LocationListener {
//	Dialog dialog1;
//	String image,val;
//	double latitude, longitude;
//	// GPSTracker class
//    GPSTracker gps;
//
//	LocationManager locationManager;
//	String provider;
//	ProgressDialog pd;
//	private double lat, lng;
//	private GoogleMap googleMap;
//	ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
//	
//	int rand1;
//	
//	private int curMarkerIndex;
//	private Circle mCircle;
//	
//	private ClusterManager<FriendMarkerItem> mClusterManager;
//	
//	public int randNumber(){
//		Random r = new Random();
//		rand1 = r.nextInt(10) + 1;
//		return rand1;
//	}
//
//	private BroadcastReceiver mPhotoDownloadReceiver;
//	
//	public void RegisterDownloadBroadcastReceiver(){
//		mPhotoDownloadReceiver = new BroadcastReceiver(){
//			@Override
//			public void onReceive(Context context, Intent intent){
//				String action = intent.getAction();
//				if(action.equals(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_MAP)){
//					for(int i = 0; i < Global.g_friendList.size(); i++){
//						if(Global.g_friendList.get(i).getMarker() != null && Global.g_friendList.get(i).getThumbBitmap() != null
//								&& Global.g_friendList.get(i).isPhotoDownloaded() == true){
////							Global.g_friendList.get(i).getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(Global.g_friendList.get(i).getThumbBitmap())));
//							Global.g_friendList.get(i).getMarker();
//						}
//					}
//				}
//			}
//		};
//	}
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_map_tab);
//		try {
//			initilizeMap();
////			RegisterDownloadBroadcastReceiver();
////			ImageCache.getSharedImageCache(TabMapActivity_.this).clear();
//			if(Global.g_friendList.size() > 0){
//				handler.sendEmptyMessage(1);
//			}
//			
//			gps = new GPSTracker(TabMapActivity_.this);
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
//	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
// 	  LatLng latLng = new LatLng(lat, lng);
//	  drawMarkerWithCircle(latLng); 
//
//	  googleMap.setOnMarkerClickListener(TabMapActivity_.this);
//	}
//
//	String response;
//
//	@Override
//	public void onResponse(String response, int pageId) {
//		this.response = response;
//		handler.sendEmptyMessage(1);
//	}
//
//	@Override
//	public void onError(String error) {
//		this.response = error;
//		handler.sendEmptyMessage(2);
//	}
//
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 1:
////				setUpClusterer();
//				for(int i = 0; i < Global.g_friendList.size(); i++){
//					FriendInfo friend = Global.g_friendList.get(i);
//					
//					LatLng MELBOURNE = new LatLng(friend.getLatitude(), friend.getLongitude());
////					Bitmap.Config conf = Bitmap.Config.ARGB_8888;
////					Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
////					Canvas canvas1 = new Canvas(bmp);
////					
////					Paint color = new Paint();
////					color.setTextSize(35);
////					color.setColor(Color.BLACK);
////					
////					canvas1.drawBitmap(BitmapFactory.decodeResource(
////							getResources(), R.drawable.pickselect), 0, 0,
////							color);
////					canvas1.drawText("", 30, 40, color);
//					
////					View markerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_friend_photo_marker, null);
////					FetchableCircularImageView image = (FetchableCircularImageView) markerView.findViewById(R.id.imgFriendPhoto);
////					image.setImage(friend.getImg(), getResources().getDrawable(R.drawable.pickselect));
////					image.setBackgroundResource(R.drawable.pickselect);
//					
//					Marker marker = null;
////					if (friend.getImg().equals("")) {
////						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(bmp)).anchor(0.5f, 1));
////					}else if (friend.getThumbBitmap() != null && friend.isPhotoDownloaded() == true){
////						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(friend.getThumbBitmap()))));
////					}else{
////						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(getCircleBitmap(bmp))));
////					}
////					if (friend.getImg().equals("")) {
////						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(bmp)).anchor(0.5f, 1));
////					}else{
////						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(Global.g_currentActivity, markerView))));
////						marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE));
////					}
//					
////					marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(Global.g_currentActivity, markerView))));
//					marker = googleMap.addMarker(new MarkerOptions().position(MELBOURNE));
//					Global.g_friendList.get(i).setMarker(marker);
//				}
//	            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng) , 16.0f) );
//				break;
//			case 2:
//				Utils.showAlert(TabMapActivity_.this, response);
//				pd.dismiss();
//				break;
//			default:
//				break;
//			}
//		};
//	};
//
//	private void setUpClusterer() {
//	    mClusterManager = new ClusterManager<FriendMarkerItem>(this, googleMap);
//	    googleMap.setOnCameraChangeListener(mClusterManager);
//	    googleMap.setOnMarkerClickListener(mClusterManager);
//	}
//	
//	public Bitmap createDrawableFromView(Context context, View view) {
//		DisplayMetrics displayMetrics = new DisplayMetrics();
//		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
//		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
//		view.buildDrawingCache();
//		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//		
//		Canvas canvas = new Canvas(bitmap);
//		view.draw(canvas);
//		return bitmap;
//	}
//	
//	@SuppressLint("NewApi")
//	private void initilizeMap() {
//		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.tabmap)).getMap();
//			// check if map is created successfully or not
//			if (googleMap == null) {
//				Toast.makeText(getApplicationContext(),
//						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
//						.show();
//			}
//			
//		}
//	}
//
//	//click event
//	@Override
//	public boolean onMarkerClick(Marker arg0) {	
//		
//		int position =  -1;
//		for(int i = 0; i < Global.g_friendList.size(); i++){
//			if(Global.g_friendList.get(i).getMarker() != null){
//				if(Global.g_friendList.get(i).getMarker().getId().equals(arg0.getId())){
//					position = i;
//					break;
//				}
//			}
//		}
//		
//		if(position == -1){
//			return false;
//		}else{
//			curMarkerIndex = position;
//		}
//		FriendInfo friend = Global.g_friendList.get(curMarkerIndex);
//		String n_first = friend.getName();
//		String n_last = friend.getLastName();
//		String n_slogan = friend.getSlogan();
//		dialog1 = new Dialog(TabMapActivity_.this);
//		
//		dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog1.setCancelable(true);
//		dialog1.setCanceledOnTouchOutside(true);
//		
//		dialog1.setContentView(R.layout.dialog_send_message);
//		TextView fname = (TextView) dialog1.findViewById(R.id.fname);
//		TextView lname = (TextView) dialog1.findViewById(R.id.lname);
//		TextView slogan = (TextView) dialog1.findViewById(R.id.slogan);
//		CircularImageView image_profile = (CircularImageView) dialog1.findViewById(R.id.imageView1);
//		Button send_message = (Button) dialog1.findViewById(R.id.add2);
//		fname.setText(n_first);
//		lname.setText(n_last);
//		slogan.setText(n_slogan);
//		dialog1.show();
//		
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
//		   
//		 send_message.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				FriendInfo friend = Global.g_friendList.get(curMarkerIndex);
//				Intent in = new Intent(getApplicationContext(),	ComposeMsgActivity.class);
//				in.putExtra("flagg", String.valueOf(friend.getUser_id()));
//				in.putExtra("flagg1", friend.getName() + " " + friend.getLastName());
//				startActivity(in);
//			}
//		});
//		return true;
//	}
//	
//	private Bitmap getCircleBitmap(Bitmap src)
//	{
//		Bitmap result = null;
//		Bitmap copyBitmap = null;
//	    try {
//	    	copyBitmap = Bitmap.createScaledBitmap(src, 100, 100, true);
//	        result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//	        Canvas canvas = new Canvas(result);
//
//	        int color = 0xff424242;
//	        Paint paint = new Paint();
//	        Rect rect = new Rect(0, 0, 200, 200);
//
//	        paint.setAntiAlias(true);
//	        canvas.drawARGB(0, 0, 0, 0);
//	        paint.setColor(color);
//	        canvas.drawCircle(50, 50, 50, paint);
//	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//	        canvas.drawBitmap(copyBitmap, rect, rect, paint);
//
//	    } catch (NullPointerException e) {
//	    } catch (OutOfMemoryError o) {
//	    }
//	    return result;
//	}
//	
//	public void onLocationChanged(Location location) {
//		// TODO Auto-generated method stub
//		latitude = location.getLatitude();
//		longitude = location.getLongitude();
//			        //Move the camera to the user's location and zoom in!
//		mCircle.setCenter(new LatLng(latitude, longitude));
//	    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16.0f));
//	
//	}
//
//	public void onProviderDisabled(String provider) {
//	}
//
//	public void onProviderEnabled(String provider) {
//	}
//
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//	}
//	
//	
//	public String address(double lt,double lg) throws IOException{
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        addresses = geocoder.getFromLocation(lt, lg, 1);
//
//        String address = addresses.get(0).getAddressLine(0);
//        String city = addresses.get(0).getAddressLine(1);
//        String country = addresses.get(0).getAddressLine(2);
//        
//        return address +"\n"+ city +"\n"+ country;
//	}     
// 		  
//	private void drawMarkerWithCircle(LatLng position){
//	    double radiusInMeters = 10.0;
//	    int strokeColor = 0xffff0000; //red outline
//	    int shadeColor =0x5500ff00; //opaque red fill
//	   
//	    CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
//	    mCircle = googleMap.addCircle(circleOptions);
//	}
//	@Override
//	public void onResume(){
//		super.onResume();
////		LocalBroadcastManager.getInstance(this).registerReceiver(mPhotoDownloadReceiver, new IntentFilter(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_MAP));
//	}
// 		
//	@Override
//	public void onPause(){
////		LocalBroadcastManager.getInstance(this).unregisterReceiver(mPhotoDownloadReceiver);
//		super.onPause();
//	}
// }
