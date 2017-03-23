package com.friends.friends;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import utils.Global;
import utils.imagedownload.DownloadImageTask;

public class DownloadService extends Service{

	private BroadcastReceiver mPhotoDownloadReceiver;
	
	public void RegisterDownloadBroadcastReceiver(){
		mPhotoDownloadReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				String action = intent.getAction();
				if(action.equals(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO)){
					if(Global.g_curPhotoDownloadIndex < Global.g_friendList.size()){
						DownloadImageTask downloadImageTask = new DownloadImageTask(DownloadService.this);
//						Log.d("FriendApp", "dlink: " + Global.g_curPhotoDownloadIndex + ": " + Global.g_friendList.get(Global.g_curPhotoDownloadIndex).getThumb_img());
						downloadImageTask.execute(Global.g_friendList.get(Global.g_curPhotoDownloadIndex).getThumb_img());
						
						Intent i = new Intent();
						i.setAction(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO_MAP);
						LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(i);
					}else{
						Global.g_downloadServiceIntent = null;
						stopSelf();
					}
				}
			}
		};
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		RegisterDownloadBroadcastReceiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mPhotoDownloadReceiver, new IntentFilter(Global.RECEIVER_FILTER_START_DOWNLOAD_FRIEND_PHOTO));
		
		if(Global.g_curPhotoDownloadIndex < Global.g_friendList.size()){
			DownloadImageTask downloadImageTask = new DownloadImageTask(DownloadService.this);
			
			downloadImageTask.execute(Global.g_friendList.get(Global.g_curPhotoDownloadIndex).getThumb_img());
		}else{
			Global.g_downloadServiceIntent = null;
			stopSelf();
		}
		
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy(){
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mPhotoDownloadReceiver);
		super.onDestroy();
	}
}
