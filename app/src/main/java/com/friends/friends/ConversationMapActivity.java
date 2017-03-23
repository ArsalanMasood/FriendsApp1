package com.friends.friends;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class ConversationMapActivity extends Activity {

	private GoogleMap googleMap;
	
	private double friendLat, friendLon;
	private String address;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_conversation_map);
		
		Intent intent = getIntent();
		friendLat = intent.getDoubleExtra("lat", 0d);
		friendLon = intent.getDoubleExtra("lon", 0d);
		address = intent.getStringExtra("addr");
		
		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.tabmap)).getMap();
			((MapFragment) getFragmentManager().findFragmentById(
					R.id.tabmap)).getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap _googleMap) {
				googleMap=_googleMap;
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					googleMap.getUiSettings().setZoomGesturesEnabled(true);

					LatLng loc = new LatLng(friendLat, friendLon);
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
					googleMap.addMarker(new MarkerOptions().position(loc).title(address));
				}
			});
			// check if map is created successfully or not
//			if (googleMap == null) {
//				Toast.makeText(getApplicationContext(),
//						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
//						.show();
//			}
		}
		
//		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.getUiSettings().setZoomGesturesEnabled(true);
//
//        LatLng loc = new LatLng(friendLat, friendLon);
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 14.0f));
//        googleMap.addMarker(new MarkerOptions().position(loc).title(address));
	}
	
	public void onBackPressed(){
		finish();
	}
}
