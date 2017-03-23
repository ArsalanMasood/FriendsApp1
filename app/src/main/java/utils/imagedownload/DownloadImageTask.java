package utils.imagedownload;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import utils.Global;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
 
	private Context context;
	
	public DownloadImageTask(Context context) {
		this.context = context;
	}

	protected Bitmap doInBackground(String... urls) {
//		Log.d("HelloWorld123", "HelloWorld123: Starting " + String.valueOf(Global.g_curPhotoDownloadIndex));
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		if(!urldisplay.equals("")){
			InputStream in = null;
			try {
				in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}finally{
				if(in != null){
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return mIcon11;
	}

	protected void onPostExecute(Bitmap result) {
		if(Global.g_friendList.size() > Global.g_curPhotoDownloadIndex && result != null){
			Global.g_friendList.get(Global.g_curPhotoDownloadIndex).setThumbBitmap(result);
			Global.g_friendList.get(Global.g_curPhotoDownloadIndex).setPhotoDownloaded(true);
		}
		Global.g_curPhotoDownloadIndex++;
		
		Intent intent = new Intent();
		intent.setAction(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
}
