package com.friends.friends.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.graphics.Bitmap;

public class FriendInfo  {

	private int user_id;
	private String name, lastName, age,distance,gender;
	private String title;
	private String country;

	private int icon;
	private String img, thumb_img;
	private String count = "0";
	private boolean isCounterVisible = false;
	private Bitmap thumbBitmap;
	private boolean isPhotoDownloaded;
	private Marker marker;
	private double latitude, longitude;
	private String slogan;
	private String relation;
	private LatLng mPosition;

	public boolean isCounterVisible() {
		return isCounterVisible;
	}

	public void setCounterVisible(boolean isCounterVisible) {
		this.isCounterVisible = isCounterVisible;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}


	public void setPosition(LatLng latlng){
		this.mPosition=latlng;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	int image;

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getThumb_img() {
		return thumb_img;
	}

	public void setThumb_img(String thumb_img) {
		this.thumb_img = thumb_img;
	}

	public Bitmap getThumbBitmap() {
		return thumbBitmap;
	}

	public void setThumbBitmap(Bitmap thumbBitmap) {
		this.thumbBitmap = thumbBitmap;
	}

	public boolean isPhotoDownloaded() {
		return isPhotoDownloaded;
	}

	public void setPhotoDownloaded(boolean isPhotoDownloaded) {
		this.isPhotoDownloaded = isPhotoDownloaded;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
}
