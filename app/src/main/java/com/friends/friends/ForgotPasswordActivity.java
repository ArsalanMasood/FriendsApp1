package com.friends.friends;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import restetapi.RestApiCallPost;
import utils.Global;
import utils.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity implements OnClickListener,RestApiCallListener, restetapi.RestApiCallListener {
	
	String Mail1;
	EditText Mail;
	Button Submit;
	 private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Global.g_currentActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forgot_password);
		Mail=(EditText)findViewById(R.id.email_txt);
		Submit=(Button)findViewById(R.id.btnFor_signIn);
		
		
		
		Submit.setOnClickListener(new OnClickListener()
		{
			private ProgressDialog dialog;
			@Override
			public void onClick(View arg0) {
				forgot_pass_two();	
			}

		});
		
	}
	//forgot for second
	private void forgot_pass_two() {
		// TODO Auto-generated method stub
		 ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
		  Mail1=Mail.getText().toString().trim();
		  if(Mail1.equalsIgnoreCase(""))
		  {
			  Toast.makeText(getApplicationContext(), "Please Enter The Email First", 1).show();
			 
		  }
		  else if (Validator.isValidEmail(Mail.getText().toString().trim()) == false) {
			  Toast.makeText(getApplicationContext(), "Invalid email format", 1).show();
				
	        
	        }
		  
		  
		  else
		  {
			  nvp.add( new BasicNameValuePair("email", Mail1));
			
			 RestApiCallPost rest = new RestApiCallPost("http://fastweightlosscoffee.com/test/james/user/forgot_password/?",this,nvp,1);
			  rest.start();
			  dialog=Utils.showDialog( "Please wait",  ForgotPasswordActivity.this);
		   }
		  
	}
	
	
	  //=========Hide Keyboard=========//
	  public void hideSoftKeyboard() {
	      if(getCurrentFocus()!=null) {
	          InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	          inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	      }
	  }

@Override
public void onError(String error) {
	// TODO Auto-generated method stub
	
}
//HANDLING  THE RESPONSE 
		String Response = "";
		@Override
		public void onResponse(String response, int pageId) {
			// TODO Auto-generated method stub
			this.Response = response;
			handleResopnse.sendEmptyMessage(pageId);
			Utils.dismissDialog(dialog);
		}
		private String contact1;
		Handler handleResopnse = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:     //THIS RESPONSE IS OF DATA RECEIVED
					try {
                       // JSONArray array= new JSONArray(Response);
                        JSONObject jobj=new JSONObject(Response);

                        System.out.println("------jobj------"+jobj);
                       // {"success":1,"message":"New Password sent to your email address"}

						String error=jobj.optString("success");
						String message=jobj.optString("message");
						if (jobj.has("error")) 
						{
							Utils.show_Toast(getApplicationContext(), ""+error);
						}
						else
						{
							Utils.show_Toast(getApplicationContext(), ""+message+ ", Check spam folder");
							//Utils.show_Toast(getApplicationContext(), "Password has been mailed at your Id");
							Mail.setText("");
							
						}
						} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};

	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
