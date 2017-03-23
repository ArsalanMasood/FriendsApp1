package com.friends.friends;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.friends.friends.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Map extends Fragment implements OnClickListener {

	GoogleMap googleMap;
	LocationManager locatioManager;
	MapView mapView;
	SupportMapFragment fragment;
	MarkerOptions marker;
	String provider;
	double lat,longt;
	String address,token,url;
	Button btnFOr_map,btnFor_fList;
	LinearLayout linear_map,linear_list;
	Friend_List friend_list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.activity_map, container, false);

		btnFOr_map = (Button) view.findViewById(R.id.btnFor_map);
		btnFor_fList = (Button) view.findViewById(R.id.btnFor_fList);

		linear_map = (LinearLayout) view.findViewById(R.id.linear_map);
		linear_list = (LinearLayout) view.findViewById(R.id.linear_list);

		
		mapView = (MapView) view.findViewById(R.id.fragmentForMap);
		mapView.onCreate(savedInstanceState);

		friend_list = new Friend_List(getActivity());
		friend_list.view_freind_list(view);
		
		btnFOr_map.setOnClickListener(this);
		btnFor_fList.setOnClickListener(this);
		/*		locatioManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locatioManager.getBestProvider(criteria, false);
		Location location = locatioManager.getLastKnownLocation(provider);
		Log.d("location", ""+location);
		if(location != null)
		{
			onLocationChanged(location);
		}
		else
		{
			Toast.makeText(getActivity(), "location not availavle", Toast.LENGTH_LONG).show();
		}
		try { 
			initilizeMap();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		googleMap.setOnMarkerDragListener(this);
		googleMap.setInfoWindowAdapter(this);
		googleMap.setOnMapLongClickListener(this);
		 */

		return view;


	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		mapView.onLowMemory();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.btnFor_map:
		linear_map.setVisibility(View.VISIBLE);
		linear_list.setVisibility(View.GONE);
		break;
	case R.id.btnFor_fList:
		linear_map.setVisibility(View.GONE);
		linear_list.setVisibility(View.VISIBLE);
		
		break;

	default:
		break;
	}	
	}


	/*	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lat = (double) location.getLatitude();
		longt = (double) location.getLongitude();
		LatLng latLng = new LatLng(lat, longt);
		//		address = getAddress(lat, longt);
		// Showing the current location in Google Map
		//	googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		//		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	}

	@SuppressLint("NewApi")
	public void initilizeMap()
	{
		if(googleMap == null)
		{
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragmentForMap)).getMap();
		}
		if(googleMap == null)
		{
			Toast.makeText(getActivity(), "sorry unable to create map", Toast.LENGTH_LONG).show();
		}
		if(googleMap != null)
		{
			MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, longt)).title("hello");
			googleMap.setMyLocationEnabled(true);
			marker.draggable(true);
			//	googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			googleMap.addMarker(marker);
		}
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		googleMap.clear();
		Marker marker3 = googleMap.addMarker(new MarkerOptions().position(point));
		marker3.showInfoWindow();
		double nLat = point.latitude;
		double nLongt = point.longitude;
//		address = getAddress(nLat,nLongt);		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		View v = getActivity().getLayoutInflater().inflate(R.layout.custom_window, null);
		TextView textview = (TextView)v.findViewById(R.id.txt);
		ImageView img = (ImageView)v.findViewById(R.id.img);
		img.setImageResource(R.drawable.p);
//		textview.setText(""+address);
		return v;
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	 */

}
