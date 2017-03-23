package com.friends.friends;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import utils.Global;

public class GcmMessageHandler extends IntentService {

    private Handler handler;
    private String sender, msg;
    String id;

    
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        Global.g_notifyTemp++;
       sender = extras.getString("sname");
       msg = extras.getString("message");
        id = extras.getString("sid");
       showNotification();
        Intent _intent = new Intent();
        _intent.setAction("com.friends.friends.new_message");
        _intent.putExtra("id",id);
        _intent.putExtra("name",sender);
        _intent.putExtra("notification_id",String.valueOf(Global.g_notifyTemp));
        sendBroadcast(_intent);
       GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void showNotification(){
        handler.post(new Runnable() {
            public void run() {
                Intent intent = new Intent(Global.g_currentActivity, Messages.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction("com.friends.friends.newmessage");
                intent.putExtra("id",id);
                intent.putExtra("name",sender);
                PendingIntent pendingIntent = PendingIntent.getActivity(Global.g_currentActivity, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Global.g_currentActivity)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(sender)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(Global.g_notifyTemp /* ID of notification */, notificationBuilder.build());

            }
        });
    }
}