package com.friends.friends.adapters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import utils.Global;
import utils.Utils;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.friends.friends.ConversationMapActivity;
import com.friends.friends.PictureDetailActivity;
import com.friends.friends.R;
import com.squareup.picasso.Picasso;

public class ConversationAdapter extends BaseAdapter {
	LayoutInflater inflator;
	Context context;
	ArrayList msg = new ArrayList();
	int rand1;
	
	private boolean isPLAYING;
	private MediaPlayer mp;
	
	public int ranNumber(){
		Random r = new Random();
		rand1 = r.nextInt(10) + 1;
		return rand1;
	}

	public ConversationAdapter(ArrayList msg, Context context) {
		this.msg = msg;
		this.context = context;
		inflator = LayoutInflater.from(this.context);
		mp = new MediaPlayer();
	}

	@Override
	public int getCount() {
		return msg.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		
		final HashMap<String, String> hash1 = (HashMap<String, String>) msg.get(arg0);
		
		View v = null;
		if(hash1.get("msg_type").equals("2")){
			v = inflator.inflate(R.layout.custom_photo_message_conversation, null);
			final ImageView imgView = (ImageView)v.findViewById(R.id.imgPhoto);
			final TextView imgDatetime = (TextView)v.findViewById(R.id.datetime);
			if(hash1.get("sender_id").trim().equals(Global.user_id)){
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				imgView.setLayoutParams(params);

				imgDatetime.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					imgDatetime.setText(hash1.get("msg_datetime").trim());
					RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)imgDatetime.getLayoutParams();
					params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

					params2.addRule(RelativeLayout.BELOW,R.id.imgPhoto);
					imgDatetime.setLayoutParams(params2);
					imgDatetime.setVisibility(View.VISIBLE);

				}

				}else{
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)imgView.getLayoutParams();
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				imgView.setLayoutParams(params);
				imgDatetime.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					imgDatetime.setText(hash1.get("msg_datetime").trim());
					RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams)imgDatetime.getLayoutParams();
					params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);


					params2.addRule(RelativeLayout.BELOW,R.id.imgPhoto);
					imgDatetime.setLayoutParams(params2);
					imgDatetime.setVisibility(View.VISIBLE);

				}
			}
			final String url = hash1.get("message");
			
			imgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,PictureDetailActivity.class);
					intent.putExtra("url",url);
					context.startActivity(intent);
				//	Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
				}
			});
			

			if(!url.equals("")){
				Picasso.with(context)
						.load(url)
						.into(imgView);

				//imgView.setImage(url);
			}
		}else if(hash1.get("msg_type").equals("3")){
			v = inflator.inflate(R.layout.custom_voice_message_conversation, null);
			
			LinearLayout sendLay = (LinearLayout) v.findViewById(R.id.sendLay);
			LinearLayout recLay = (LinearLayout) v.findViewById(R.id.recLay);
	
			ImageView sendC = (ImageView) v.findViewById(R.id.imgPlaySend);
			ImageView recC = (ImageView) v.findViewById(R.id.imgPlayRec);

			TextView sendT = (TextView) v.findViewById(R.id.sendT);
			TextView recT = (TextView) v.findViewById(R.id.recT);


			if(hash1.get("sender_id").trim().equals(Global.user_id)){
				sendT.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					sendT.setText(hash1.get("msg_datetime").trim());
					sendT.setVisibility(View.VISIBLE);
				}
				recLay.setVisibility(View.GONE);
			}else{
				recT.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					recT.setText(hash1.get("msg_datetime").trim());
					recT.setVisibility(View.VISIBLE);
				}
				sendLay.setVisibility(View.GONE);
			}
			sendC.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					playAudio(hash1.get("message"));
				}
			});
			recC.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					playAudio(hash1.get("message"));
				}
			});
		}else if(hash1.get("msg_type").equals("4")){
			v = inflator.inflate(R.layout.custom_location_message_conversation, null);
			
			LinearLayout sendLay = (LinearLayout) v.findViewById(R.id.sendLay);
			LinearLayout recLay = (LinearLayout) v.findViewById(R.id.recLay);
	
			ImageView imgSendC = (ImageView) v.findViewById(R.id.imgMapSend);
			ImageView imgRecC = (ImageView) v.findViewById(R.id.imgMapRec);
			
			TextView txtSendC = (TextView) v.findViewById(R.id.txtAddressSend);
			TextView txtRecC = (TextView) v.findViewById(R.id.txtAddressRec);

			TextView sendT = (TextView) v.findViewById(R.id.sendT);
			TextView recT = (TextView) v.findViewById(R.id.recT);


			double lat = 0, lon = 0; 
			String address = "";
			String[]locations = hash1.get("message").split(":");
			if(locations.length == 3){
				address = locations[0];
				lat = Double.parseDouble(locations[1]);
				lon = Double.parseDouble(locations[2]);
			}
			
			if(hash1.get("sender_id").trim().equals(Global.user_id)){
				sendT.setVisibility(View.GONE);
				recT.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					sendT.setText(hash1.get("msg_datetime").trim());
					sendT.setVisibility(View.VISIBLE);
				}
				recLay.setVisibility(View.GONE);
			}else{
				recT.setVisibility(View.GONE);
				sendT.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					recT.setText(hash1.get("msg_datetime").trim());
					recT.setVisibility(View.VISIBLE);
				}
				sendLay.setVisibility(View.GONE);
			}
			txtSendC.setText(address);
			txtRecC.setText(address);
			imgSendC.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, ConversationMapActivity.class);
					HashMap<String, String> hash1 = (HashMap<String, String>) msg.get(arg0);
					String[]locations = hash1.get("message").split(":");
					if(locations.length == 3){
						String address = locations[0];
						double lat = Double.parseDouble(locations[1]);
						double lon = Double.parseDouble(locations[2]);
						
						intent.putExtra("lat", lat);
						intent.putExtra("lon", lon);
						intent.putExtra("addr", address);
					}
					context.startActivity(intent);
				}
			});
			imgRecC.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, ConversationMapActivity.class);
					HashMap<String, String> hash1 = (HashMap<String, String>) msg.get(arg0);
					String[]locations = hash1.get("message").split(":");
					if(locations.length == 3){
						String address = locations[0];
						double lat = Double.parseDouble(locations[1]);
						double lon = Double.parseDouble(locations[2]);
						
						intent.putExtra("lat", lat);
						intent.putExtra("lon", lon);
						intent.putExtra("addr", address);
					}
					context.startActivity(intent);
				}
			});
		}else{
			v = inflator.inflate(R.layout.custom_message_conversation, null);
	
			LinearLayout sendLay = (LinearLayout) v.findViewById(R.id.sendLay);
			LinearLayout recLay = (LinearLayout) v.findViewById(R.id.recLay);
	
			TextView sendC = (TextView) v.findViewById(R.id.sendC);
			TextView recC = (TextView) v.findViewById(R.id.recC);
			TextView sendT = (TextView) v.findViewById(R.id.sendT);
			TextView recT = (TextView) v.findViewById(R.id.recT);

			if(hash1.get("sender_id").trim().equals(Global.user_id)){
				sendT.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					sendT.setText(hash1.get("msg_datetime").trim());
					sendT.setVisibility(View.VISIBLE);
				}
				sendC.setText(hash1.get("message"));
				recLay.setVisibility(View.GONE);
			}else{

				recT.setVisibility(View.GONE);
				if(!hash1.get("msg_datetime").trim().equals("0000-00-00 00:00:00")){
					recT.setText(hash1.get("msg_datetime").trim());
					recT.setVisibility(View.VISIBLE);
				}
				recC.setText(hash1.get("message"));
				sendLay.setVisibility(View.GONE);
			}
		}
		
		return v;
	}
	
	private void playAudio(String url){
		mp.stop();
		mp.release();
		mp = new MediaPlayer();
		try {
            mp.setDataSource(url);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
				}
			});
        } catch (IOException e) {
        }
	}
}
