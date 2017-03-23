package com.friends.friends;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import restetapi.RestApiCallListener;
import restetapi.RestApiCallPost;
import utils.GPSTracker;
import utils.Global;
import utils.RealPathUtil;
import utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.friends.friends.R;
import com.friends.friends.adapters.ConversationAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import static com.google.android.gms.internal.zzs.TAG;


public class ConversationActivity extends Activity implements
		RestApiCallListener, OnItemLongClickListener, OnClickListener {
	
	ProgressDialog pd;
	
	ListView conversationC;
	ArrayList<HashMap<String, String>> msgList = new ArrayList<HashMap<String, String>>();
	ConversationAdapter conAdapter;
    private boolean hasRun=false;
	private String response;
	ImageView menu_slide,profile_image;
	private ArrayList<NameValuePair> nvp;
	private String rec_id;
	Slider sd;
	private TextView txtTitle;
	//private EditText chatmsgC;
	private Button chatSendButton;
	private ImageView imgAddPhoto, imgAddVoice, imgAddLocation;
	
	private String attachedFilePath;
	EmojiconEditText emojiconEditText;
	ImageView emojiButton;
	View rootView;
	
	private int REQUEST_CAMERA = 0, SELECT_PHOTO_FILE = 1;
	public  int RECORD_REQUEST = 2;
	String struri="";

	IntentFilter filter = new IntentFilter("com.friends.friends.new_message");
	private BroadcastReceiver myBroadcastReceiver =
			new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {

					if(intent.getAction().equalsIgnoreCase("com.friends.friends.new_message")){
						String id = intent.getStringExtra("id");
						String not_id = intent.getStringExtra("notification_id");
						if(id!=null) {
							if (!id.equalsIgnoreCase("") && id.equalsIgnoreCase(rec_id)) {
								start_get(false);
								cancelNotification(ConversationActivity.this, Integer.parseInt(not_id));
							}
						}
					}

				}
			};
	public void onResume() {
		super.onResume();
		registerReceiver(myBroadcastReceiver, filter);
		if(!hasRun){
		start_get(false);
	   }
	}
	public static void cancelNotification(Context ctx, int notifyId) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
		nMgr.cancel(notifyId);
	}
	public void onPause() {
		super.onPause();
		hasRun=false;
		unregisterReceiver(myBroadcastReceiver);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.g_currentActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_conversation);
		hasRun=true;

		nvp = new ArrayList<NameValuePair>();

		menu_slide = (ImageView) findViewById(R.id.menu_slide);
		sd = new Slider(this, this);

		profile_image= (ImageView) findViewById(R.id.my_image);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
		rootView = findViewById(R.id.root_view);
		emojiButton = (ImageView) findViewById(R.id.emoji_btn);

		conversationC = (ListView) findViewById(R.id.conversationC);
		conversationC.setOnItemLongClickListener(this);
		chatSendButton = (Button) findViewById(R.id.chatSendButton);
		conAdapter = new ConversationAdapter(msgList, this);
		conversationC.setAdapter(conAdapter);
		conAdapter.notifyDataSetChanged();
		
		imgAddPhoto = (ImageView)findViewById(R.id.imgAddPhoto);
		imgAddVoice = (ImageView)findViewById(R.id.imgAddVoice);
		imgAddLocation = (ImageView)findViewById(R.id.imgAddLocation);
		imgAddPhoto.setOnClickListener(this);
		imgAddVoice.setOnClickListener(this);
		imgAddLocation.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if(extras.containsKey("rec_id")){
				rec_id = extras.getString("rec_id");
			}
			if(extras.containsKey("text")){
				String text = extras.getString("text");
				emojiconEditText.setText(text);
			}
			if(extras.containsKey("image")){
				struri = extras.getString("image");

			}
			if(extras.containsKey("name")){
				txtTitle.setText(extras.getString("name"));

			}
			if(extras.containsKey("url")){
				final String url = extras.getString("url");
				if (url!= null && !url.isEmpty()) {
					Picasso.with(ConversationActivity.this)
							.load(url)
							.placeholder(R.drawable.pickselect)
							.into(profile_image);

					profile_image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(ConversationActivity.this, PictureDetailActivity.class);
							intent.putExtra("url",url);
							startActivity(intent);

						}
					});

				}
			}
			// and get whatever type user account id is
		}


		// Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
		final EmojiconsPopup popup = new EmojiconsPopup(rootView, this);

		//Will automatically set size according to the soft keyboard size
		popup.setSizeForSoftKeyboard();

		//If the emoji popup is dismissed, change emojiButton to smiley icon
		popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
			}
		});

		//If the text keyboard closes, also dismiss the emoji popup
		popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

			@Override
			public void onKeyboardOpen(int keyBoardHeight) {

			}

			@Override
			public void onKeyboardClose() {
				if(popup.isShowing())
					popup.dismiss();
			}
		});

		//On emoji clicked, add it to edittext
		popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

			@Override
			public void onEmojiconClicked(Emojicon emojicon) {
				if (emojiconEditText == null || emojicon == null) {
					return;
				}

				int start = emojiconEditText.getSelectionStart();
				int end = emojiconEditText.getSelectionEnd();
				if (start < 0) {
					emojiconEditText.append(emojicon.getEmoji());
				} else {
					emojiconEditText.getText().replace(Math.min(start, end),
							Math.max(start, end), emojicon.getEmoji(), 0,
							emojicon.getEmoji().length());
				}
			}
		});

		//On backspace clicked, emulate the KEYCODE_DEL key event
		popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

			@Override
			public void onEmojiconBackspaceClicked(View v) {
				KeyEvent event = new KeyEvent(
						0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
				emojiconEditText.dispatchKeyEvent(event);
			}
		});

		// To toggle between text keyboard and emoji keyboard keyboard(Popup)
		emojiButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//If popup is not showing => emoji keyboard is not visible, we need to show it
				if(!popup.isShowing()){

					//If keyboard is visible, simply show the emoji popup
					if(popup.isKeyBoardOpen()){
						popup.showAtBottom();
						changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
					}

					//else, open the text keyboard first and immediately after that show the emoji popup
					else{
						emojiconEditText.setFocusableInTouchMode(true);
						emojiconEditText.requestFocus();
						popup.showAtBottomPending();
						final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
						changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
					}
				}

				//If popup is showing, simply dismiss it to show the undelying text keyboard
				else{
					popup.dismiss();
				}
			}
		});

//		Bundle bundle = getIntent().getExtras();
//		rec_id = bundle.getString("rec_id");

		menu_slide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sd.opendrawer();
			}
		});
		start_get(true);



		chatSendButton.setOnClickListener(this);
	}
	private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
		iconToBeChanged.setImageResource(drawableResourceId);
	}
	private void start_get(boolean showprogress){
		nvp.clear();
		nvp.add(new BasicNameValuePair("receiver_id", Global.user_id));
		nvp.add(new BasicNameValuePair("sender_id", rec_id));
		RestApiCallPost post = new RestApiCallPost(Global.get_user_message,
				this, nvp, 7);
		if(showprogress) {
			pd = Utils.show_ProgressDialog(ConversationActivity.this);
			pd.show();
		}
		post.start();
	}

	private String encode(String str){
		String  bytesEncoded = Base64.encodeToString(str.getBytes(),Base64.NO_WRAP | Base64.URL_SAFE);
		return bytesEncoded;
	}
	public void start_send() {
		nvp.clear();
		nvp.add(new BasicNameValuePair("sender_id", Global.user_id));
		nvp.add(new BasicNameValuePair("receiver_id", rec_id));
//		nvp.add(new BasicNameValuePair("message", emojiconEditText.getText().toString()
//				.trim()));
		nvp.add(new BasicNameValuePair("message", encode(emojiconEditText.getText().toString().trim())));
		nvp.add(new BasicNameValuePair("msg_type", "1"));
		RestApiCallPost post = new RestApiCallPost(Global.send_message, this,
				nvp, 6);
		pd = Utils.show_ProgressDialog(ConversationActivity.this);
		pd.show();
		post.start();
	}

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
	@Override
	public void onResponse(String response, int pageId) {
		this.response = response;
		if (pageId == 7) {	//Get Chat msg list
			handler.sendEmptyMessage(3);
		} else if(pageId == 16){	//Delete msg
			handler.sendEmptyMessage(4);
		}
		else if(pageId == 20){	//Delete msg
			handler.sendEmptyMessage(5);
		}
		else if(pageId == 6){		//Send Text Msg;
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
				try {
					JSONObject jobj = new JSONObject(response);
					if (jobj.has("success")) {
//						Utils.ShowToast(getApplicationContext(),
//								jobj.optString("success"));
//
//						Intent im = getIntent();
//						startActivity(im);
//						finish();
						
						HashMap<String, String> hash1 = new HashMap<String, String>();
						hash1.put("id", jobj.getString("id"));
						hash1.put("sender_id", Global.user_id);
						hash1.put("receiver_id", rec_id);
						if(jobj.opt("is_encoded")!=null && jobj.getString("msg_type").equals("1") ){
							if(jobj.getInt("is_encoded")==1 ){
								hash1.put("message",decode(jobj.getString("message").trim()));
							}
							else{
								hash1.put("message",jobj.getString("message").trim());
							}
						}
						else {
							hash1.put("message", jobj.getString("message").trim());
						}
						String datetime = jobj.getString("msg_datetime");
						hash1.put("msg_datetime",convertGmtToLocal(datetime));
						hash1.put("msg_type", jobj.getString("msg_type"));
						hash1.put("read_unread", "1");
						
						emojiconEditText.setText("");
						
						msgList.add(hash1);
						
						conAdapter.notifyDataSetChanged();
						if(conAdapter.getCount() > 0){
							conversationC.setSelection(conAdapter.getCount() - 1);
						}

					} else {
						Utils.showAlert(ConversationActivity.this,
								"try Again !");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(pd!=null){
				pd.dismiss();
			   }

				break;
			case 2:
				Utils.showAlert(ConversationActivity.this, response);
				if(pd!=null){
					pd.dismiss();
				}

				break;
			case 3:
				// Utils.showAlert(ConversationActivity.this, response);
				try {
					JSONObject jobj = new JSONObject(response);
					if (jobj.has("error")) {
						Utils.ShowToast(ConversationActivity.this,
								"No Messaage List...");
					} else {
						msgList.clear();
						JSONArray jarray = jobj.getJSONArray("message");
						for (int i = 0; i < jarray.length(); i++) {
							JSONObject jobj1 = jarray.getJSONObject(i);
							HashMap<String, String> hash1 = new HashMap<String, String>();
							hash1.put("id", jobj1.getString("id"));
							hash1.put("sender_id", jobj1.getString("sender_id"));
							hash1.put("receiver_id",
									jobj1.getString("receiver_id"));
							if(jobj1.opt("is_encoded")!=null && jobj1.getString("msg_type").equals("1") ){
								if(jobj1.getInt("is_encoded")==1){
									hash1.put("message",decode(jobj1.getString("message").trim()));
								}
								else{
									hash1.put("message",jobj1.getString("message").trim());
								}
							}
							else {
								hash1.put("message", jobj1.getString("message").trim());
							}
							String datetime = jobj1.getString("msg_datetime");
							hash1.put("msg_datetime",convertGmtToLocal(datetime));
							hash1.put("date", jobj1.getString("date"));
							hash1.put("read_unread",
									jobj1.getString("read_unread"));
							hash1.put("msg_type", jobj1.getString("msg_type"));
							msgList.add(hash1);
						}
						conAdapter.notifyDataSetChanged();
						if(conAdapter.getCount() > 0){
							conversationC.setSelection(conAdapter.getCount() - 1);
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(pd!=null){
					pd.dismiss();
				}

				if(!struri.equalsIgnoreCase("")){
					Uri uri = Uri.parse(struri);
					sendImageFromURI(uri);
				}
				break;
			case 4:
				try {
					JSONObject jobj = new JSONObject(response);
					Log.d("abcde", "abcde response=" + response);
					int pos = jobj.optInt("position", -1);
					if(pos == -1){
						Utils.ShowToast(ConversationActivity.this,
								"Error, check your internet....");
					}else{
						msgList.remove(pos);
						conAdapter.notifyDataSetChanged();
					}
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(pd!=null){
					pd.dismiss();
				}

				break;
				case 5:
					try {
						JSONObject jobj = new JSONObject(response);
						String res  = jobj.optString("success");
						if(res!=null){
							if(res.equalsIgnoreCase("1")){
								Utils.showAlert(ConversationActivity.this, "image posted successfully");

							}
							else{
								Utils.showAlert(ConversationActivity.this, "could not post image");

							}
						}
						else {
							Utils.showAlert(ConversationActivity.this, "could not post image");
						}
					}
					catch (Exception e){
						e.getMessage();
					}
					if(pd!=null){
						pd.dismiss();
					}

					break;

				default:
				break;
			}
		};
	};

//aGkg8J-YhA==
	private String decode(String str){
		String stringFromBase = new String(Base64.decode(str,Base64.NO_WRAP | Base64.URL_SAFE));
		return stringFromBase;
	}
private String convertGmtToLocal(String gmt){
	if(gmt.equals("0000-00-00 00:00:00")){
		return gmt;
	}
	SimpleDateFormat inputFormat = new SimpleDateFormat(
			// "EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
			"yyyy-MM-dd HH:mm:ss", Locale.US);
	inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

	SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm EEE MMM dd, yyyy");
	Date date=null;
	try {
		date = inputFormat.parse(gmt);
	}
	catch (Exception e){
		date=null;
	}
	String outputText="0000-00-00 00:00:00";
	if(date!=null) {
		outputText = outputFormat.format(date);
	}
	return outputText;
}

	public  void shareText(String subject,String body) {
		Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
		txtIntent .setType("text/plain");
		txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
		startActivity(Intent.createChooser(txtIntent ,"Share"));
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		// TODO Auto-generated method stub
		
		if(parent.getId() == conversationC.getId()){


			final HashMap<String, String> msgItem = (HashMap<String, String>)msgList.get(position);

			if(msgItem.get("msg_type").toString().equals("1")){
				final CharSequence options[] = new CharSequence[] {"Copy","Share/Forward","Delete"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Options");
				builder.setItems(options, new DialogInterface.OnClickListener() {
					@SuppressLint("NewApi")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which==0){

							ClipboardManager cManager = (ClipboardManager) ConversationActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
							ClipData cData = ClipData.newPlainText("text",msgItem.get("message").toString());
							cManager.setPrimaryClip(cData);
							Toast.makeText(ConversationActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
						}
						else if(which==1){
							shareText("Friends' App",msgItem.get("message").toString());
						}
						else{

							showDeleteDialog(msgItem.get("id").toString(),position);

						}
					}
				});
				builder.show();
			}
			else if(msgItem.get("msg_type").toString().equals("2")){
				final CharSequence options[] = new CharSequence[] {"Post image to world picture feed","Delete"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Options");
				builder.setItems(options, new DialogInterface.OnClickListener() {
					@SuppressLint("NewApi")
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which==0){

							nvp.clear();
							nvp.add(new BasicNameValuePair("user_id", Global.user_id));
							nvp.add(new BasicNameValuePair("url",msgItem.get("message").toString()));
							RestApiCallPost post = new RestApiCallPost(Global.Post_Image, ConversationActivity.this, nvp, 20);
							pd = Utils.show_ProgressDialog(ConversationActivity.this);
							pd.show();
							post.start();

						}
						else{

							showDeleteDialog(msgItem.get("id").toString(),position);

						}
					}
				});
				builder.show();

			}
			else{
				showDeleteDialog(msgItem.get("id").toString(),position);
			}




//			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//			    @Override
//			    public void onClick(DialogInterface dialog, int which) {
//			        switch (which){
//			        case DialogInterface.BUTTON_POSITIVE:
//			            //Yes button clicked
//			        	HashMap<String, String> msgItem = (HashMap<String, String>)msgList.get(position);
//						String id = msgItem.get("id").toString();
//
//						nvp.clear();
//						nvp.add(new BasicNameValuePair("id", id));
//						nvp.add(new BasicNameValuePair("pos", String.valueOf(position)));
//						RestApiCallPost post = new RestApiCallPost(Global.delete_user_message, ConversationActivity.this, nvp, 16);
//						pd = Utils.show_ProgressDialog(ConversationActivity.this);
//						pd.show();
//						post.start();
//
//			            break;
//			        case DialogInterface.BUTTON_NEGATIVE:
//			            //No button clicked
//			        	dialog.dismiss();
//			            break;
//			        }
//			    }
//			};
//
//			AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
//			builder.setMessage("Are you sure want to delete message?").setPositiveButton("Yes", dialogClickListener)
//			    .setNegativeButton("No", dialogClickListener).show();
		}
		return false;
	}


	private void showDeleteDialog(final String id, final int position){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked

					//	String id = msgItem.get("id").toString();

						nvp.clear();
						nvp.add(new BasicNameValuePair("id", id));
						nvp.add(new BasicNameValuePair("pos", String.valueOf(position)));
						RestApiCallPost post = new RestApiCallPost(Global.delete_user_message, ConversationActivity.this, nvp, 16);
						pd = Utils.show_ProgressDialog(ConversationActivity.this);
						pd.show();
						post.start();

						break;
					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						dialog.dismiss();
						break;
				}
			}
		};

					AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
			builder.setMessage("Are you sure want to delete message?").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.chatSendButton){
			if (emojiconEditText.getText().toString().trim().equals("")) {
				Utils.ShowToast(getApplicationContext(),"Enter Message First");
			}else {
				if (Utils.isNetworkAvailable(getApplicationContext())) {
					start_send();
				}else {
					Utils.showAlert(ConversationActivity.this, "No Internet Connection");
				}
			}
		}
		if(v.getId() == R.id.imgAddPhoto){
			final CharSequence[] items = { "Take Photo", "Choose from Gallery",
            "Cancel" };

		    AlertDialog.Builder builder = new AlertDialog.Builder(ConversationActivity.this);
		    builder.setTitle("Add Photo!");
		    builder.setItems(items, new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int item) {
		            if (items[item].equals("Take Photo")) {
		                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		                attachedFilePath = Environment.getExternalStorageDirectory() + "/Pictures" + File.separator +
		                        System.currentTimeMillis() + ".jpg";
		                File file = new File(attachedFilePath);
		                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		                startActivityForResult(intent, REQUEST_CAMERA);
		            } else if (items[item].equals("Choose from Gallery")) {
		
		                Intent intent = new Intent();
		                intent.setType("image/*");
		                intent.setAction(Intent.ACTION_GET_CONTENT);
		                startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PHOTO_FILE);
		
		            } else if (items[item].equals("Cancel")) {
		                dialog.dismiss();
		            }
		        }
		    });
		    builder.show();
		}
		if(v.getId() == R.id.imgAddVoice){
			Intent intent = new Intent(
			          MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			      startActivityForResult(intent, RECORD_REQUEST);
		}
		if(v.getId() == R.id.imgAddLocation){
			GPSTracker gps = new GPSTracker(this);
			double lat = 0f; double lng = 0f;
			if(gps.canGetLocation()){
				lat = gps.getLatitude();
				lng = gps.getLongitude();
				String address = Utils.GetAddressFromLatLng(this, lat, lng);
				nvp.clear();
				nvp.add(new BasicNameValuePair("sender_id", Global.user_id));
				nvp.add(new BasicNameValuePair("receiver_id", rec_id));
				nvp.add(new BasicNameValuePair("message", address + ":" + String.valueOf(lat) + ":" + String.valueOf(lng)));
				nvp.add(new BasicNameValuePair("msg_type", "4"));
				RestApiCallPost post = new RestApiCallPost(Global.send_message, this,
						nvp, 6);
				pd = Utils.show_ProgressDialog(ConversationActivity.this);
				pd.show();
				post.start();
	        }else{
				if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
					displayLocationSettingsRequest(ConversationActivity.this);
				}
				else{
					gps.showSettingsAlert();
				}

	        }
		}
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
							status.startResolutionForResult(ConversationActivity.this, 4865);
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
	private void sendImageFromURI(Uri uri){
		// SDK < API11
		if (Build.VERSION.SDK_INT < 11)
			attachedFilePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this,uri);
			// SDK >= 11 && SDK < 19
		else if (Build.VERSION.SDK_INT < 19)
			attachedFilePath = RealPathUtil.getRealPathFromURI_API11to18(this, uri);
			// SDK > 19 (Android 4.4)
		else {
			attachedFilePath = RealPathUtil.getPath19(this, uri);
		}
		if(!attachedFilePath.equals("")) SenFileMessage(2);
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == android.app.Activity.RESULT_OK) {
            if (requestCode == SELECT_PHOTO_FILE){

				sendImageFromURI(data.getData());
//                // SDK < API11
//                if (Build.VERSION.SDK_INT < 11)
//                	attachedFilePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
//                    // SDK >= 11 && SDK < 19
//                else if (Build.VERSION.SDK_INT < 19)
//                	attachedFilePath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
//                    // SDK > 19 (Android 4.4)
//                else {
//                	attachedFilePath = RealPathUtil.getPath19(this, data.getData());
//                }
//                if(!attachedFilePath.equals("")) SenFileMessage(2);
            }else if (requestCode == REQUEST_CAMERA){
            	if(!attachedFilePath.equals("")) SenFileMessage(2);
            }else if (requestCode == RECORD_REQUEST){
            	Uri uri = data.getData();
            	if (Build.VERSION.SDK_INT < 11)
                	attachedFilePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                	attachedFilePath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                    // SDK > 19 (Android 4.4)
                else {
                	attachedFilePath = RealPathUtil.getPath19(this, data.getData());
                }
            	if(!attachedFilePath.equals("")) SenFileMessage(3);
            }
        }
    }
	
	public void SenFileMessage(int msgType){
		new MsgFileUploader(msgType).execute();
	}
	
	class MsgFileUploader extends AsyncTask<Void, String, String> {
		
		private int msgType;
		
		public MsgFileUploader(int msgType) {
			// TODO Auto-generated constructor stub
			this.msgType = msgType;
		}
		
		@Override
		protected void onPreExecute() {
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(Global.send_file_message);
			try {
				File finalFile = new File(attachedFilePath);
				MultipartEntity entity = new MultipartEntity();

				entity.addPart("uploaded_file", new FileBody(finalFile));
				entity.addPart("sender_id", new StringBody(String.valueOf(Global.user_id)));
				entity.addPart("receiver_id", new StringBody(rec_id));
				entity.addPart("msg_type", new StringBody(String.valueOf(msgType)));
//				entity.addPart("first_name", new StringBody(firstnameR
//						.getText().toString().trim()));

				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				return s.toString();
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			pd.dismiss();
			struri="";
			super.onPostExecute(result);
			response = result;
			handler.sendEmptyMessage(1);
		}

	}
}
