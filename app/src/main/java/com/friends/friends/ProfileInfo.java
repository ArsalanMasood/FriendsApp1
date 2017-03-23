package com.friends.friends;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;
import utils.cameraUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileInfo extends Activity implements RestApiCallListener, OnClickListener {
	
	ProgressDialog pd;
	
	private String response;
	private ArrayList<NameValuePair> nvp;
	
	private Slider sd;
	
	private TextView addessP, birthdayP, phonenoP, emailP;
	private EditText firstnameP, lastnameP, editSlogan,editCountry,editPassword;
	private Spinner genderP,relationP;
	
	ImageView imagev, menu_slide;
	
	String imagesrc,url;

	public final int TAKE_PICTURE=0,ACTIVITY_SELECT_IMAGE=1;
	File finalFile;
	private static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "camera_new";
	cameraUtils camerautils=new cameraUtils();
	Uri selectedImageUri;
	
	private int mYear, mMonth, mDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_profile_info);
		Global.g_currentActivity = this;
		menu_slide = (ImageView) findViewById(R.id.menu_slide);

		sd = new Slider(this, this);

		firstnameP = (EditText) findViewById(R.id.firstnameP);
		editCountry = (EditText) findViewById(R.id.countryP);
		lastnameP = (EditText) findViewById(R.id.lastnameP);
		editSlogan = (EditText)findViewById(R.id.sloganP);
		editPassword = (EditText) findViewById(R.id.edtxtPassword);

		emailP = (TextView)findViewById(R.id.emailP);
		phonenoP=(TextView)findViewById(R.id.phonenoP);
		addessP = (TextView) findViewById(R.id.addressP);
		
		birthdayP = (TextView) findViewById(R.id.birthdayP);
		birthdayP.setOnClickListener(this);
		
		genderP = (Spinner) findViewById(R.id.genderP);
		relationP = (Spinner) findViewById(R.id.relationP);

		((Button)findViewById(R.id.btnSaveProfile)).setOnClickListener(this);
		((Button)findViewById(R.id.btnSaveContact)).setOnClickListener(this);
		((Button)findViewById(R.id.btnPasswordSubmit)).setOnClickListener(this);
		
		imagev = (ImageView) findViewById(R.id.imagev);
		imagev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});
	
		menu_slide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sd.opendrawer();
			}
		});

		GregorianCalendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay= calendar.get(Calendar.DAY_OF_MONTH);
        
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			nvp = new ArrayList<NameValuePair>();
			nvp.add(new BasicNameValuePair("user_id", Global.user_id));
			RestApiCallPost post = new RestApiCallPost(Global.User_Profile,
					this, nvp, 3);
			pd = Utils.show_ProgressDialog(ProfileInfo.this);
			pd.show();
			post.start();
		} else {
			Utils.showAlert(ProfileInfo.this, "No Internet Connection");
		}

	}

	@Override
	public void onResponse(String response, int pageId) {
		this.response = response;
		if(pageId == 3){
			handler.sendEmptyMessage(1);
		}
		if(pageId == 10){
			handler.sendEmptyMessage(3);
		}
		if(pageId == 101){
			handler.sendEmptyMessage(4);
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

				// Utils.showAlert(ProfileInfo.this, response);

				try {
					JSONObject jobj = new JSONObject(response);
					firstnameP.setText(jobj.optString("first_name"));
					lastnameP.setText(jobj.optString("last_name"));
					birthdayP.setText(jobj.optString("dob"));
					String sex = jobj.optString("sex");
					String relation = jobj.getString("relationship_status");
					if(sex.equals("Male")){
						genderP.setSelection(1);
					}else if(sex.equals("Female")){
						genderP.setSelection(2);
					}else{
						genderP.setSelection(0);
					}

					if(relation.equalsIgnoreCase("Single")){
						relationP.setSelection(0);
					}else {
						relationP.setSelection(1);
					}

					birthdayP.setText(jobj.optString("dob"));
					addessP.setText(jobj.optString("address") + " "
							+ jobj.optString("city")+" "+jobj.optString("state")+" "+ jobj.optString("zip")+" "+ jobj.optString("country"));
					editSlogan.setText(jobj.optString("slogan"));
					editCountry.setText(jobj.optString("country"));
					emailP.setText(jobj.optString("email"));
					phonenoP.setText(jobj.optString("phonenumber"));
					
					if (!jobj.optString("profile_pic").equals(""))
					{
						
						try {
						//	URL url= new URL(jobj.optString("profile_pic"));
						String url1=	jobj.optString("profile_pic");
							if(!url1.equalsIgnoreCase("")) {
								Picasso.with(ProfileInfo.this)
										.load(url1)
										.into(imagev);
							}
						//	Bitmap bm= BitmapFactory.decodeStream(url.openConnection().getInputStream());
							
						//	imagev.setImageBitmap(getCircleBitmap(bm));
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
			
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				pd.dismiss();

				break;
			case 2:
				Utils.showAlert(ProfileInfo.this, response);
				pd.dismiss();
				break;
			case 3:
				Utils.show_Toast(ProfileInfo.this, "Update slogan successfully");
				pd.dismiss();
				break;

				case 4:

					// Utils.showAlert(ProfileInfo.this, response);

					try {
						JSONObject jobj = new JSONObject(response);
						String successs=jobj.optString("success");
                        if(successs.equalsIgnoreCase("1")){
							Utils.show_Toast(ProfileInfo.this, "Password changed successfully, please log in again");
							logout();
						}
						else{
							Utils.show_Toast(ProfileInfo.this, "Could not change password");
						}


					} catch (JSONException e) {
						e.printStackTrace();
					}
					pd.dismiss();

					break;

			default:
				break;
			}

		};
	};

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
         AlertDialog.Builder builder = new AlertDialog.Builder(ProfileInfo.this);
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
				 finalFile = new File(cpath);
	             
	             Bitmap yyy = (BitmapFactory.decodeFile(cpath));
	             imagev.setImageBitmap(yyy);
	             new upload_pic().execute();   
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
                  Bitmap yyy = (BitmapFactory.decodeFile(cpath));
  				finalFile = new File(cpath);
  				imagev.setImageBitmap(yyy);
                  new upload_pic().execute();   
            }
        }
    }
	
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	
	}	
	
	
	public Uri getOutputMediaFileUri(int type){
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
		  if (type == MEDIA_TYPE_IMAGE)
		  {
		   mediaFile = new File(mediaStorageDir.getPath() + File.separator
		     + "IMG_" + timeStamp + ".jpg");
		  }
		  else
		  {
		   return null;
		  }

		  return mediaFile;
		 }
	 
		class upload_pic extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					upload();
				} catch (Exception e) {
					e.printStackTrace();
				}			

				return null;
			}

			public void upload() throws Exception {
				//Url of the server
				url="http://fastweightlosscoffee.com/test/james/user/change_pic/?user_id="+Global.user_id;
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
						try {
					MultipartEntity entity = new MultipartEntity();
					entity.addPart("user_pic", new FileBody(finalFile));
					httppost.setEntity(entity);
					HttpResponse response = httpclient.execute(httppost);
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
					String sResponse;
					StringBuilder s = new StringBuilder();
					while ((sResponse = reader.readLine()) != null) {
						s = s.append(sResponse);
						System.out.println("---ssssss---"+s);
					}
				} catch (Exception e) {
					Log.e(e.getClass().getName(), e.getMessage());
				}
			}		

			@Override
			protected void onPostExecute(Void args) {		
				try {			
					
					Intent in = new Intent(getApplicationContext(),ProfileInfo.class);
					startActivity(in);
					overridePendingTransition(0, 0);
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub id=69, slogan=hienjoy!
			if(v.getId() == R.id.btnSaveProfile){
				if (Utils.isNetworkAvailable(getApplicationContext())) {
					nvp = new ArrayList<NameValuePair>();
					nvp.add(new BasicNameValuePair("id", Global.user_id));
					
					String firstName = firstnameP.getText().toString().trim();
					String lastName = lastnameP.getText().toString().trim();
					String birthDay = birthdayP.getText().toString();
					
					String[] genderArr = getResources().getStringArray(R.array.profile_sex_items);
					String gender = "";
					if(genderP.getSelectedItemPosition() > 0) gender = genderArr[genderP.getSelectedItemPosition()];
					String slogan =  editSlogan.getText().toString().trim();
					String country =  editCountry.getText().toString().trim();

					String[] relationArr = getResources().getStringArray(R.array.profile_relation_items);
					String relation = "";
					relation = relationArr[relationP.getSelectedItemPosition()];


					nvp.add(new BasicNameValuePair("fname", firstName));
					nvp.add(new BasicNameValuePair("lname", lastName));
					nvp.add(new BasicNameValuePair("birth", birthDay));
					nvp.add(new BasicNameValuePair("sex", gender));
						nvp.add(new BasicNameValuePair("slogan", slogan));
					nvp.add(new BasicNameValuePair("country", country));
					nvp.add(new BasicNameValuePair("relationship_status", relation));
					
					RestApiCallPost post = new RestApiCallPost(Global.Update_User_Profile,
							this, nvp, 10);
					pd = Utils.show_ProgressDialog(ProfileInfo.this);
					pd.show();
					post.start();
				} else {
					Utils.showAlert(ProfileInfo.this, "No Internet Connection");
				}
			}
			if(v.getId() == R.id.birthdayP){
				new DatePickerDialog(ProfileInfo.this, dateSetListener, mYear, mMonth, mDay).show();
			}

			if(v.getId() == R.id.btnPasswordSubmit){
				if (Utils.isNetworkAvailable(getApplicationContext())) {
					nvp = new ArrayList<NameValuePair>();
					nvp.add(new BasicNameValuePair("user_id", Global.user_id));

					String password = editPassword.getText().toString().trim();
					if(!password.isEmpty()) {
						nvp.add(new BasicNameValuePair("password", password));


						RestApiCallPost post = new RestApiCallPost(Global.Update_User_Password,
								this, nvp, 101);
						pd = Utils.show_ProgressDialog(ProfileInfo.this);
						pd.show();
						post.start();
					}
					else{
						Utils.showAlert(ProfileInfo.this, "Please enter the new password");
					}
				} else {
					Utils.showAlert(ProfileInfo.this, "No Internet Connection");
				}
			}
		}
		
		private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
	          
	           @Override
	          public void onDateSet(DatePicker view, int year, int monthOfYear,
	                     int dayOfMonth) {
	                // TODO Auto-generated method stub
	        	   String dateStr = String.format("%d-%s-%s", year,Utils.leadZero(monthOfYear+1), Utils.leadZero(dayOfMonth));
	        	   birthdayP.setText(dateStr);
	          }
	     };


	private void logout(){
		SharedPreferences pref = ProfileInfo.this.getSharedPreferences("MyPref", 0); // 0
		SharedPreferences.Editor editor = pref.edit();
		editor.remove("user_id_sf");
		// will delete key email
		editor.commit();
		Global.user_id = "";
		Intent intent = new Intent(ProfileInfo.this, Login.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}
}