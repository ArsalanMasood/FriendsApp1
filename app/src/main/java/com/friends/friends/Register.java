package com.friends.friends;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.friends.friends.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import utils.Global;
import utils.GPSTracker;
import utils.Utils;
import utils.cameraUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@SuppressWarnings("unused")
public class Register extends Activity implements OnMarkerClickListener,LocationListener{
	public static final int DATE_DIALOG_ID = 1;
	ProgressDialog pd;
	TextView gender1;
	InputStream inputStream;
	ImageView imageView1;
	double latitude, longitude;
	
	
	String address,city,country;
   
	// GPSTracker class
    GPSTracker gps;
    private static final double EARTH_RADIUS = 6378100.0;
    private int offset;

	LocationManager locationManager;
	String provider;
	
	double lat;
	double lng;
	private GoogleMap googleMap;
	private String selectedImagePath = "";
	EditText dob, firstnameR, lastnameR, emailR, passwordR, conirmpasswordR,
			usernameR, cityR, countryR, zipR, stateR, addressR, phonenumberR,dateofbirthR,relationR,genderR;
	ImageView button1;
	static String[] gender = { "Male", "Female" };
	static String[] relation = { "Single", "In a relationship" };
	StringBuilder s;
	public final int TAKE_PICTURE=0,ACTIVITY_SELECT_IMAGE=1;
	File finalFile;
	String img_url="";
	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "camera_new";
	cameraUtils camerautils=new cameraUtils();
	Uri selectedImageUri;
	String image;
	Bitmap bitmap2;
	String dateofbirth="";
	String val;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		gps = new GPSTracker(Register.this);
		// check if GPS enabled
		if(gps.canGetLocation()){
			lat = gps.getLatitude();
			lng = gps.getLongitude();
			try {
				val = address(lat, lng);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			gps.showSettingsAlert();
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (provider != null && !provider.equals("")) {
			// Get the location from the given provider
			Location location = locationManager.getLastKnownLocation(provider);
			locationManager.requestLocationUpdates(provider, 100, 1, this);
			if (location != null)
				onLocationChanged(location);
		} else {
//			Toast.makeText(getBaseContext(), "No Provider Found",
//					Toast.LENGTH_SHORT).show();
		}
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		button1 = (ImageView) findViewById(R.id.button1);
		Global.g_currentActivity = this;
		gender1 = (TextView) findViewById(R.id.gender1);
		dob = (EditText) findViewById(R.id.dobR);
		dateofbirthR = (EditText) findViewById(R.id.dateobirthR);
		genderR = (EditText) findViewById(R.id.genderR);
		relationR = (EditText) findViewById(R.id.relationR);
		firstnameR = (EditText) findViewById(R.id.firstnameR);
		lastnameR = (EditText) findViewById(R.id.lastnameR);
		emailR = (EditText) findViewById(R.id.emailR);
		passwordR = (EditText) findViewById(R.id.passwordR);
		conirmpasswordR = (EditText) findViewById(R.id.conirmpasswordR);
		usernameR = (EditText) findViewById(R.id.usernameR);
		cityR = (EditText) findViewById(R.id.cityR);
		countryR = (EditText) findViewById(R.id.countryR);
		zipR = (EditText) findViewById(R.id.zipR);
		stateR = (EditText) findViewById(R.id.stateR);
		addressR = (EditText) findViewById(R.id.addressR);
		phonenumberR = (EditText) findViewById(R.id.phonenumberR);



		final ArrayAdapter<String> gen_adapter = new ArrayAdapter<String>(
				Register.this, android.R.layout.simple_dropdown_item_1line,
				gender);

		genderR.setFocusable(false);
		genderR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
				builder.setTitle("Select Gender");
				builder.setItems(gender, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						genderR.setText(gender[which]);

					}
				});
				builder.show();

			}
		});

		relationR.setFocusable(false);
		relationR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
				builder.setTitle("Select Relationship Status");
				builder.setItems(relation, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						relationR.setText(relation[which]);

					}
				});
				builder.show();

			}
		});

		dateofbirthR.setFocusable(false);
		dateofbirthR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dateofbirthR.setInputType(InputType.TYPE_NULL);
				showDialog(DATE_DIALOG_ID);
			}
		});



        

//		final ArrayAdapter<String> gen_adapter = new ArrayAdapter<String>(
//				Register.this, android.R.layout.simple_expandable_list_item_1,
//				gender);
		gender1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(Register.this);
				dialog.setContentView(R.layout.list);
				ListView lv = (ListView) dialog.findViewById(R.id.lv);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						gender1.setText(gender[arg2]);
						dialog.dismiss();
					}
				});
				lv.setAdapter(gen_adapter);
				dialog.setCancelable(true);
				dialog.setTitle("Select Gender");
				dialog.show();
			}
		});

		dob.setFocusable(false);
		dob.setOnTouchListener(new View.OnTouchListener() {
			@Override
			@SuppressWarnings("deprecation")
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				dob.setInputType(InputType.TYPE_NULL);
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});

		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onsubmit();
			}
		});

		

		imageView1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				selectImage();		
			}
		});

	}



	private static final int IMAGE_GALLERY = 0;
	private static final int IMAGE_CAMERA = 1;
	private Uri mUri;

	private void changeView(int a) {
		if (a == 0) {
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			mUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "pic_"
							+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 0);
			intent.putExtra("aspectY", 0);
			intent.putExtra("outputX", 250);
			intent.putExtra("outputY", 200);
			try {
				intent.putExtra("return-data", true);
				startActivityForResult(intent, IMAGE_CAMERA);
			} catch (ActivityNotFoundException e) {
			}
		} else if (a == 1) {
			Intent intent = new Intent(); 
			intent.setType("image/*"); 
			intent.setAction(Intent.ACTION_GET_CONTENT); 
			startActivityForResult(Intent.createChooser(intent, "Select Picture"),IMAGE_GALLERY);
		}
	}

	public void onsubmit() {
	 if(firstnameR
			.getText().toString().trim().equals(""))
	{
		
		Utils.ShowToast(getApplicationContext(), "Please enter first name");
	}
	
	else if(lastnameR.getText().toString().trim().equals(""))
	{
		Utils.ShowToast(getApplicationContext(), "Please enter last name");
	}
	else if(emailR.getText().toString().trim().equals(""))
	{
		Utils.ShowToast(getApplicationContext(), "Please enter email");
	}
	 else if(genderR.getText().toString().trim().equals(""))
	 {
		 Utils.ShowToast(getApplicationContext(), "Please select gender");
	 }
	 else if(dateofbirthR.getText().toString().trim().equals(""))
	 {
		 Utils.ShowToast(getApplicationContext(), "Please select date of birth");
	 }
	 else if(relationR.getText().toString().trim().equals(""))
	 {
		 Utils.ShowToast(getApplicationContext(), "Please select relationship status");
	 }
	else if(passwordR.getText().toString().trim().equals(""))
	{
		Utils.ShowToast(getApplicationContext(), "Please enter password");
	}
	else if(conirmpasswordR.getText().toString().trim().equals(""))
	{
		Utils.ShowToast(getApplicationContext(), "Please enter cpnfirm password");
	}
		
	else if (!passwordR.getText().toString().trim()
				.equals(conirmpasswordR.getText().toString().trim())) {
			Utils.ShowToast(getApplicationContext(), "password does not match");
		}
	
	else if(usernameR.getText().toString().trim().equals(""))
	{
		Utils.ShowToast(getApplicationContext(), "Please enter username");
	}
	else if(img_url.equalsIgnoreCase(""))
		{
		Utils.ShowToast(getApplicationContext(), "Please upload user profile");
			img_url="";
		}
	else {
			if (Utils.isNetworkAvailable(getApplicationContext())) {
				startRegister();
			} else {
				Utils.showAlert(Register.this, "No Internet Connection");
			}
		}
	 }



	public void startRegister() {
		new SignupLoader().execute();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dialog = new DatePickerDialog(this,
					mDateSetListener, cyear, cmonth, cday);
			return dialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			String dated = String.valueOf(dayOfMonth);
			int datevalue = Integer.parseInt(dated);
			if (datevalue < 10) {
				dated = "0" + datevalue;
			}
			String monthd = String.valueOf(monthOfYear + 1);
			int monthvalue = Integer.parseInt(monthd);
			if (monthvalue < 10) {
				monthd = "0" + monthvalue;
			}

			String yeard = String.valueOf(year);
			String date_selected = dated + "-" + monthd + "-" + yeard;
			dateofbirthR.setText(date_selected);
			dateofbirth=year + "-" + monthd + "-" + dated;
		}
	};


	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaColumns.DATA };
		android.database.Cursor cursor = managedQuery(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	class SignupLoader extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pd = Utils. show_ProgressDialog_for_register(Register.this);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {




			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Global.User_Register);
			try {
				final File finalFile;
				MultipartEntity entity = new MultipartEntity();

				if (img_url.equalsIgnoreCase("")) {
					img_url="";
				} else {
					finalFile = new File(img_url);
					entity.addPart("profile_pic", new FileBody(finalFile));
				}
				entity.addPart("first_name", new StringBody(firstnameR
						.getText().toString().trim()));
				entity.addPart("last_name", new StringBody(lastnameR.getText()
						.toString().trim()));
				entity.addPart("password", new StringBody(passwordR.getText()
						.toString().trim()));
				entity.addPart("email", new StringBody(emailR.getText()
						.toString().trim()));
				entity.addPart("token", new StringBody(Global.g_deviceToken
						.toString().trim()));
				entity.addPart("address", new StringBody(city));
				entity.addPart("phonenumber", new StringBody(phonenumberR
						.getText().toString().trim()));
				entity.addPart("confirmpassword", new StringBody(
						conirmpasswordR.getText().toString().trim()));
				entity.addPart("username", new StringBody(usernameR.getText()
						.toString().trim()));
				entity.addPart("sex", new StringBody(genderR.getText()
						.toString().trim()));
				entity.addPart("dob", new StringBody(dateofbirth
						.trim()));
				entity.addPart("relationship_status", new StringBody(relationR.getText()
						.toString().trim()));
				entity.addPart("city", new StringBody(city.trim()));
				entity.addPart("country", new StringBody(country.trim()));
				entity.addPart("zip", new StringBody(zipR.getText().toString()
						.trim()));
				entity.addPart("state", new StringBody(city.trim()));

				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;














//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(Global.User_Register);
//			try {
//				final File finalFile;
//				MultipartEntity entity = new MultipartEntity();
//
//				if (img_url.equalsIgnoreCase("")) {
//					img_url="";
//					} else {
//					finalFile = new File(img_url);
//					entity.addPart("profile_pic", new FileBody(finalFile));
//				}
//				entity.addPart("first_name", new StringBody(firstnameR
//						.getText().toString().trim()));
//				entity.addPart("last_name", new StringBody(lastnameR.getText()
//						.toString().trim()));
//				entity.addPart("password", new StringBody(passwordR.getText()
//						.toString().trim()));
//				entity.addPart("email", new StringBody(emailR.getText()
//						.toString().trim()));
//				entity.addPart("address", new StringBody(city));
//				entity.addPart("phonenumber", new StringBody(phonenumberR
//						.getText().toString().trim()));
//				entity.addPart("confirmpassword", new StringBody(
//						conirmpasswordR.getText().toString().trim()));
//				entity.addPart("username", new StringBody(usernameR.getText()
//						.toString().trim()));
//				entity.addPart("sex", new StringBody(gender1.getText()
//						.toString().trim()));
//				entity.addPart("dob", new StringBody(dob.getText().toString()
//						.trim()));
//				entity.addPart("city", new StringBody(city.trim()));
//				entity.addPart("country", new StringBody(city.trim()));
//				entity.addPart("zip", new StringBody(zipR.getText().toString()
//						.trim()));
//				entity.addPart("state", new StringBody(city.trim()));
//
//				httppost.setEntity(entity);
//				HttpResponse response = httpclient.execute(httppost);
//				HttpEntity resEntity = response.getEntity();
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(
//								response.getEntity().getContent(), "UTF-8"));
//				String sResponse;
//				s = new StringBuilder();
//				while ((sResponse = reader.readLine()) != null) {
//					s = s.append(sResponse);
//				}
//			} catch (ClientProtocolException e) {
//
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			System.out.println("==responseeee" + s.toString());
			pd.dismiss();
			
			try {
				JSONObject jobj=new JSONObject(s.toString());
				String success=jobj.optString("success");
				
				if(jobj.has("error"))
				{
					Utils.showAlert(Register.this, jobj.getString("error"));
				}else if(success.equals("1"))
				{
					
					Utils.showAlert(Register.this, "Register Successfully");
					Intent in= new Intent(getApplicationContext(),Login.class);
					startActivity(in);
					overridePendingTransition(0, 0);
					
					
				}
				
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
	
	
	//private 
	
	private Bitmap getCircleBitmap(Bitmap bitmap)
	{
		final Bitmap output= Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
		final Canvas canvas= new Canvas(output);
		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect= new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		final RectF rectF= new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0,0,0,0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return output;
	
	}
	
	
	public void selectImage()
    {
         final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
         AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options,new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    if(options[which].equals("Take Photo"))
                    {
                    
                    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    					selectedImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
    				     intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
    				     // start the image capture Intent
    				     startActivityForResult(intent, TAKE_PICTURE);
    			    
                    }
                    else if(options[which].equals("Choose from Gallery"))
                    {
                        Intent intent = new Intent(
    							Intent.ACTION_PICK,
    							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
                    }
                    else if(options[which].equals("Cancel"))
                    {
                        dialog.dismiss();
                    }

                }
            });
            builder.show();
    }
	
	@Override
	public void onActivityResult(int requestcode,int resultcode,Intent intent)
    {
        super.onActivityResult(requestcode, resultcode, intent);
        if(resultcode==RESULT_OK)
        {
            if(requestcode==TAKE_PICTURE)
            {
                 BitmapFactory.Options options = new BitmapFactory.Options();
			     options.inSampleSize = 8;
			 	 String cpath = camerautils.compressImage(selectedImageUri.getPath(),this);
			 	 
			 	  img_url=cpath;
				 finalFile = new File(cpath);
	             System.out.println("---selectedImagePath---"+cpath);
	             Bitmap yyy = (BitmapFactory.decodeFile(cpath));
	             imageView1.setImageBitmap(getCircleBitmap(yyy));
	        
            }
            else if(requestcode==ACTIVITY_SELECT_IMAGE)
            {
                Uri selectedImage = intent.getData();
                String[] filePath = { MediaColumns.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                String cpath =camerautils.compressImage(picturePath,this);
                img_url=cpath;
                Bitmap yyy = (BitmapFactory.decodeFile(cpath));
				Uri selectedImagePath = getImageUri(getApplicationContext(), yyy);
				finalFile = new File(cpath);
			    imageView1.setImageBitmap(getCircleBitmap(yyy));
              
            
            }
        }
    }
	
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	
	}	
	
	
	public Uri getOutputMediaFileUri(int type)
	{
		
		  return Uri.fromFile(getOutputMediaFile(type));
		  
	}
	
	
	 private static File getOutputMediaFile(int type) {

		  // External sdcard location
		  File mediaStorageDir = new File(
		    Environment
		      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
		    IMAGE_DIRECTORY_NAME);
		  // Create the storage directory if it does not exist
		  if (!mediaStorageDir.exists()) {
		   if (!mediaStorageDir.mkdirs()) {
		    Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
		      + IMAGE_DIRECTORY_NAME + " directory");
		    return null;
		   }
		  }

		  // Create a media file name
		  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
		    Locale.getDefault()).format(new Date());
		  File mediaFile;
		  if (type == MEDIA_TYPE_IMAGE) {
		   mediaFile = new File(mediaStorageDir.getPath() + File.separator
		     + "IMG_" + timeStamp + ".jpg");
		  }  else {
		   return null;
		  }

		  return mediaFile;
		 }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLatitude();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	 public String address(double lt,double lg) throws IOException{
	        Geocoder geocoder;
	        List<Address> addresses;
	        geocoder = new Geocoder(this, Locale.getDefault());
	        addresses = geocoder.getFromLocation(lt, lg, 1);

	        address = addresses.get(0).getAddressLine(0);
	        city = addresses.get(0).getAddressLine(1);
	        country = addresses.get(0).getAddressLine(2);

	        System.out.println("------add ress----"+address);
	        System.out.println("------add city----"+city);
	        System.out.println("------add country----"+country);


	        return address +"\n"+ city +"\n"+ country;
	    }
}


/*package com.friends.friends;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import com.friends.friends.R;

import utils.Comman;
import utils.Utils;
import utils.cameraUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


@SuppressWarnings("unused")
public class Register extends Activity {
	public static final int DATE_DIALOG_ID = 1;
	ProgressDialog pd;
	TextView gender1;
	InputStream inputStream;
	ImageView imageView1;
	private String selectedImagePath = "";
	EditText dob, firstnameR, lastnameR, emailR, passwordR, conirmpasswordR,
			usernameR, cityR, countryR, zipR, stateR, addressR, phonenumberR;
	Button button1;
	static String[] gender = { "Male", "Female" };
	StringBuilder s;
	public final int TAKE_PICTURE=0,ACTIVITY_SELECT_IMAGE=1;
	File finalFile;
	String img_url;
	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "camera_new";
	cameraUtils camerautils=new cameraUtils();
	Uri selectedImageUri;
	String image;
	Bitmap bitmap2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		button1 = (Button) findViewById(R.id.button1);
	
		gender1 = (TextView) findViewById(R.id.gender1);
		dob = (EditText) findViewById(R.id.dobR);
		firstnameR = (EditText) findViewById(R.id.firstnameR);
		lastnameR = (EditText) findViewById(R.id.lastnameR);
		emailR = (EditText) findViewById(R.id.emailR);
		passwordR = (EditText) findViewById(R.id.passwordR);
		conirmpasswordR = (EditText) findViewById(R.id.conirmpasswordR);
		usernameR = (EditText) findViewById(R.id.usernameR);
		cityR = (EditText) findViewById(R.id.cityR);
		countryR = (EditText) findViewById(R.id.countryR);
		zipR = (EditText) findViewById(R.id.zipR);
		stateR = (EditText) findViewById(R.id.stateR);
		addressR = (EditText) findViewById(R.id.addressR);
		phonenumberR = (EditText) findViewById(R.id.phonenumberR);

		final ArrayAdapter<String> gen_adapter = new ArrayAdapter<String>(
				Register.this, android.R.layout.simple_expandable_list_item_1,
				gender);
		gender1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(Register.this);
				dialog.setContentView(R.layout.list);
				ListView lv = (ListView) dialog.findViewById(R.id.lv);
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						gender1.setText(gender[arg2]);
						dialog.dismiss();
					}
				});
				lv.setAdapter(gen_adapter);
				dialog.setCancelable(true);
				dialog.setTitle("Select Gender");
				dialog.show();
			}
		});

		dob.setFocusable(false);
		dob.setOnTouchListener(new View.OnTouchListener() {
			@SuppressWarnings("deprecation")
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				dob.setInputType(InputType.TYPE_NULL);
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});

		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				onsubmit();
			}
		});

		

		imageView1.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				selectImage();
				CharSequence[] items1 = { "Camera", "Gallery" };
				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						Register.this);
				builder1.setTitle("Choose Picture");
				builder1.setItems(items1,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								changeView(item);
							}
						});
				AlertDialog alert1 = builder1.create();
				alert1.show();
			}
		});

	}

	
	 * TextView gender1; EditText
	 * dob,firstnameR,lastnameR,emailR,passwordR,conirmpasswordR
	 * ,usernameR,cityR,countryR,zipR,stateR; Button button1;
	 

	private static final int IMAGE_GALLERY = 0;
	private static final int IMAGE_CAMERA = 1;
	private Uri mUri;

	private void changeView(int a) {
		if (a == 0) {
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			mUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "pic_"
							+ String.valueOf(System.currentTimeMillis()) + ".jpg"));

			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);

			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 0);
			intent.putExtra("aspectY", 0);
			intent.putExtra("outputX", 250);
			intent.putExtra("outputY", 200);

			try {

				intent.putExtra("return-data", true);
				startActivityForResult(intent, IMAGE_CAMERA);

			} catch (ActivityNotFoundException e) {
				// Do nothing for now
			}


			//startActivityForResult(intent, IMAGE_CAMERA);
		} else if (a == 1) {
			Intent intent = new Intent(); 
			intent.setType("image/*"); 
			intent.setAction(Intent.ACTION_GET_CONTENT); 
			startActivityForResult(Intent.createChooser(intent, "Select Picture"),IMAGE_GALLERY);
		}
	}

	public void onsubmit() {
		if (!passwordR.getText().toString().trim()
				.equals(conirmpasswordR.getText().toString().trim())) {
			Utils.ShowToast(getApplicationContext(), "password does not match");
		}

		else {
			if (Utils.isNetworkAvailable(getApplicationContext())) {
				startRegister();
			} else {
				Utils.showAlert(Register.this, "No Internet Connection");
			}
		}
	}

	
	 * EditText dob, firstnameR, lastnameR, emailR, passwordR, conirmpasswordR,
	 * usernameR, cityR, countryR, zipR, stateR,addressR,phonenumberR;
	 

	public void startRegister() {

		System.out.println("====date of birttttttt"+dob.getText().toString());
		
		new SignupLoader().execute();

		
		 * System.out.println("==firstnameR"+firstnameR.getText().toString());
		 * System.out.println("==lastnameR"+lastnameR.getText().toString());
		 * System.out.println("==emailR"+emailR.getText().toString());
		 * System.out.println("==passwordR"+passwordR.getText().toString());
		 * System
		 * .out.println("==conirmpasswordR"+conirmpasswordR.getText().toString
		 * ());
		 * System.out.println("==usernameR"+usernameR.getText().toString());
		 * System.out.println("==cityR"+cityR.getText().toString());
		 * System.out.println("==countryR"+countryR.getText().toString());
		 * System.out.println("==zipR"+zipR.getText().toString());
		 * System.out.println("==stateR"+stateR.getText().toString());
		 * System.out.println("==gender1"+gender1.getText().toString());
		 * System.out.println("==addressR"+addressR.getText().toString());
		 * System
		 * .out.println("==phonenumberR"+phonenumberR.getText().toString());
		 * System.out.println("==dob"+dob.getText().toString());
		 

	}

	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			DatePickerDialog dialog = new DatePickerDialog(this,
					mDateSetListener, cyear, cmonth, cday);
			return dialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			String dated = String.valueOf(dayOfMonth);
			int datevalue = Integer.parseInt(dated);
			if (datevalue < 10) {
				dated = "0" + datevalue;
			}
			String monthd = String.valueOf(monthOfYear + 1);
			int monthvalue = Integer.parseInt(monthd);
			if (monthvalue < 10) {
				monthd = "0" + monthvalue;
			}

			String yeard = String.valueOf(year);
			String date_selected = dated + "-" + monthd + "-" + yeard;
			dob.setText(date_selected);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case IMAGE_GALLERY:
			try{
				if (resultCode == RESULT_OK)
				{
					if (null == data) return;
					Uri selectedImageUri = data.getData();
					
				
					
					selectedImagePath = ImageFilePath.getPath(getApplicationContext(), selectedImageUri);
					try {
						bitmap2 = BitmapFactory.decodeStream(
								getContentResolver().openInputStream(selectedImageUri));
						//imageView1.setImageBitmap(bitmap2);
						imageView1.setImageBitmap(getCircleBitmap(bitmap2));
					} catch (FileNotFoundException e) {
						e.printStackTrace(); 
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			break;


		case IMAGE_CAMERA:
			if (resultCode == RESULT_OK) {
				selectedImagePath = mUri.getPath();
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inTempStorage = new byte[16 * 1024];
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(new FileInputStream(mUri.getPath()), null, options);
					int IMAGE_MAX_SIZE = 640;
					int scale = 1;
					if (options.outHeight > IMAGE_MAX_SIZE|| options.outWidth > IMAGE_MAX_SIZE) {
						scale = (int) Math.pow(2,(int) Math.round(Math.log(IMAGE_MAX_SIZE/ (double) Math.max(options.outHeight,options.outWidth))/ Math.log(0.5)));
					}
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = scale;
					o2.inPurgeable = true;
					o2.outHeight = 480;
					o2.outWidth = 640;
					Bitmap b = BitmapFactory
							.decodeStream(inputStream, null, o2);
					b.recycle();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				ImageFileManipulation objImageFileManipulation = new ImageFileManipulation(Register.this);

				try {
					bitmap2 = ThumbnailUtils.extractThumbnail(
							objImageFileManipulation.getThumbnail(mUri, 90),90, 90);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);

					imageView1.setImageBitmap(bitmap2);
					
					
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

		default:
			break;

		}

	}
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		android.database.Cursor cursor = managedQuery(contentUri, proj, null,
				null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	class SignupLoader extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			pd = Utils.show_ProgressDialog(Register.this);
			pd.show();
			super.onPreExecute();
		}

		protected Void doInBackground(Void... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Comman.User_Register);
			try {
				final File finalFile;
				MultipartEntity entity = new MultipartEntity();

				if (img_url.equalsIgnoreCase("")) {

				} else {
					finalFile = new File(img_url);
					entity.addPart("profile_pic", new FileBody(finalFile));
				}

				entity.addPart("first_name", new StringBody(firstnameR
						.getText().toString().trim()));
				entity.addPart("last_name", new StringBody(lastnameR.getText()
						.toString().trim()));
				entity.addPart("password", new StringBody(passwordR.getText()
						.toString().trim()));
				entity.addPart("email", new StringBody(emailR.getText()
						.toString().trim()));
				entity.addPart("address", new StringBody(addressR.getText()
						.toString().trim()));
				entity.addPart("phonenumber", new StringBody(phonenumberR
						.getText().toString().trim()));
				entity.addPart("confirmpassword", new StringBody(
						conirmpasswordR.getText().toString().trim()));
				entity.addPart("username", new StringBody(usernameR.getText()
						.toString().trim()));
				entity.addPart("sex", new StringBody(gender1.getText()
						.toString().trim()));
				entity.addPart("dob", new StringBody(dob.getText().toString()
						.trim()));
				entity.addPart("city", new StringBody(cityR.getText()
						.toString().trim()));
				entity.addPart("country", new StringBody(countryR.getText()
						.toString().trim()));
				entity.addPart("zip", new StringBody(zipR.getText().toString()
						.trim()));
				entity.addPart("state", new StringBody(stateR.getText()
						.toString().trim()));

				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			System.out.println("==responseeee" + s.toString());
			pd.dismiss();
			
			try {
				JSONObject jobj=new JSONObject(s.toString());
				String success=jobj.optString("success");
				
				if(jobj.has("error"))
				{
					Utils.showAlert(Register.this, jobj.getString("error"));
				}else if(success.equals("1"))
				{
					
					Utils.showAlert(Register.this, "Register Successfully");
					Intent in= new Intent(getApplicationContext(),Login.class);
					startActivity(in);
					overridePendingTransition(0, 0);
					
					
				}
				
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

	}
	
	
	//private 
	
	private Bitmap getCircleBitmap(Bitmap bitmap)
	{
		final Bitmap output= Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
		final Canvas canvas= new Canvas(output);
		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect= new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		final RectF rectF= new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0,0,0,0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		return output;
	
	}
	
	
	public void selectImage()
    {
         final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
         AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options,new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    if(options[which].equals("Take Photo"))
                    {
                    
                    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    					selectedImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
    				     intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
    				     // start the image capture Intent
    				     startActivityForResult(intent, TAKE_PICTURE);
    			    
                    }
                    else if(options[which].equals("Choose from Gallery"))
                    {
                        Intent intent = new Intent(
    							Intent.ACTION_PICK,
    							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
                    }
                    else if(options[which].equals("Cancel"))
                    {
                        dialog.dismiss();
                    }

                }
            });
            builder.show();
    }
	
	public void onActivityResult(int requestcode,int resultcode,Intent intent)
    {
        super.onActivityResult(requestcode, resultcode, intent);
        if(resultcode==RESULT_OK)
        {
            if(requestcode==TAKE_PICTURE)
            {
                 BitmapFactory.Options options = new BitmapFactory.Options();
			     options.inSampleSize = 8;
			 	 String cpath = camerautils.compressImage(selectedImageUri.getPath(),this);
			 	 
			 	  img_url=cpath;
				 finalFile = new File(cpath);
	             System.out.println("---selectedImagePath---"+cpath);
	             Bitmap yyy = (BitmapFactory.decodeFile(cpath));
	             imageView1.setImageBitmap(getCircleBitmap(yyy));
	             
	             try {
						bitmap2 = BitmapFactory.decodeStream(
								getContentResolver().openInputStream(selectedImageUri));
						//imageView1.setImageBitmap(bitmap2);
						imageView1.setImageBitmap(getCircleBitmap(bitmap2));
					} catch (FileNotFoundException e) {
						e.printStackTrace(); 
					}
	             
	             
				 img_pic.setImageBitmap(yyy);
	            
                
            }
            else if(requestcode==ACTIVITY_SELECT_IMAGE)
            {
                Uri selectedImage = intent.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                String cpath =camerautils.compressImage(picturePath,this);
                img_url=cpath;
                Bitmap yyy = (BitmapFactory.decodeFile(cpath));
				Uri selectedImagePath = getImageUri(getApplicationContext(), yyy);
				finalFile = new File(cpath);
			    imageView1.setImageBitmap(getCircleBitmap(yyy));
              
            
            }
        }
    }
	
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	
	}	
	
	
	public Uri getOutputMediaFileUri(int type)
	{
		
		  return Uri.fromFile(getOutputMediaFile(type));
		  
	}
	
	
	 private static File getOutputMediaFile(int type) {

		  // External sdcard location
		  File mediaStorageDir = new File(
		    Environment
		      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
		    IMAGE_DIRECTORY_NAME);
		  // Create the storage directory if it does not exist
		  if (!mediaStorageDir.exists()) {
		   if (!mediaStorageDir.mkdirs()) {
		    Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
		      + IMAGE_DIRECTORY_NAME + " directory");
		    return null;
		   }
		  }

		  // Create a media file name
		  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
		    Locale.getDefault()).format(new Date());
		  File mediaFile;
		  if (type == MEDIA_TYPE_IMAGE) {
		   mediaFile = new File(mediaStorageDir.getPath() + File.separator
		     + "IMG_" + timeStamp + ".jpg");
		  }  else {
		   return null;
		  }

		  return mediaFile;
		 }
	
}*/