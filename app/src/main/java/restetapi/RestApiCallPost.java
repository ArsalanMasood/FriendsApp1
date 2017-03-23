package restetapi;

import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import utils.Utils;
import android.os.Looper;
import android.util.Log;

public class RestApiCallPost extends Thread {

	String url;
	ArrayList<NameValuePair> list;
	int pageId;
	RestApiCallListener listener;

	public RestApiCallPost(String url, RestApiCallListener listener,
			ArrayList<NameValuePair> list, int pageID) {

		this.url = url;
		this.listener = listener;
		this.pageId = pageID;
		this.list = list;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Looper.prepare();
//		Log.e("Url", url);
		HttpClient httpclient = null;
		HttpPost httppost = null;
		HttpEntity httpentity = null;
		HttpResponse httpresponse = null;
		try {
			
			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(list,"UTF-8"));
			Log.e("List Data", list.toString());
			httpresponse = httpclient.execute(httppost);

			if (httpresponse != null) {
				httpentity = httpresponse.getEntity();
				String response = EntityUtils.toString(httpentity);
				sendResponse(response, pageId);
			} else {
				sendError("No Data Found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sendError("No Data Found. Try Again later when internet available.");
			Looper.loop();
		}
	}

	public void sendResponse(String response, int pageId) {
//		Utils.ShowLogCat("onResponse", response);
		if (listener != null) {
			listener.onResponse(response, pageId);
		}
	}

	public void sendError(String error) {
//		Utils.ShowLogCat("onError", error);
		if (listener != null) {
			listener.onError(error);
		}
	}
}
