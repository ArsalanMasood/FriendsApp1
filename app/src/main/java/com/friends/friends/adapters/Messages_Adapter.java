package com.friends.friends.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import utils.customview.FetchableCircularImageView;

import com.friends.friends.PictureDetailActivity;
import com.friends.friends.R;
import com.friends.friends.model.InboxUserInfo;
import com.squareup.picasso.Picasso;

public class Messages_Adapter extends BaseAdapter {
	
	LayoutInflater inflater;
	Context context;
	ArrayList<InboxUserInfo> msg = new ArrayList<InboxUserInfo>();
	ViewHolder holder = new ViewHolder();
	
	public Messages_Adapter(ArrayList<InboxUserInfo> msg, Context context) {
		this.msg = msg;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_message_inbox2, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		InboxUserInfo gs = new InboxUserInfo();
		gs = msg.get(arg0);

		holder.textFor_name = (TextView) convertView
				.findViewById(R.id.txtFor_name);
		holder.textFor_gender = (TextView) convertView
				.findViewById(R.id.txtFor_gender);
		holder.textFor_relation = (TextView) convertView
				.findViewById(R.id.txtFor_relation);
		holder.textFor_age = (TextView) convertView
				.findViewById(R.id.txtFor_age);
		holder.slogan = (TextView) convertView
				.findViewById(R.id.slogan);
		holder.slogan.setVisibility(View.GONE);

		holder.txtFor_country = (TextView) convertView
				.findViewById(R.id.txtFor_country);
		holder.txtFor_country.setVisibility(View.GONE);
//		holder.textFor_age = (TextView) convertView
//				.findViewById(R.id.txtFor_age2);
		holder.textFor_distance = (TextView) convertView
				.findViewById(R.id.txtFor_distance);
		holder.image = (FetchableCircularImageView) convertView.findViewById(R.id.my_image);


		holder.textFor_name.setText(gs.getFname()+" "+gs.getLname());
		holder.textFor_age.setText(gs.getAge());
		holder.textFor_gender.setText(gs.getGender());
		holder.textFor_relation.setText(gs.getRelation());

		if(!gs.getSlogan().equalsIgnoreCase("")) {
			holder.slogan.setText(gs.getSlogan());
			holder.slogan.setVisibility(View.VISIBLE);
		}
		if(!gs.getCountry().equalsIgnoreCase("")) {
			holder.txtFor_country.setText(gs.getCountry());
			holder.txtFor_country.setVisibility(View.VISIBLE);
		}

		if (gs.getProfile_pic()!= null && !gs.getProfile_pic().isEmpty()) {
			Picasso.with(context)
					.load(gs.getProfile_pic())
					.placeholder(R.drawable.pickselect)
					.into(holder.image);

		}

		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PictureDetailActivity.class);
				intent.putExtra("url",msg.get(arg0).getProfile_pic());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

				//		onClickProfileImageListener.OnClick(v,gs,position);
			}
		});
		return convertView;
	}
}

class ViewHolder {
	FetchableCircularImageView image;
	TextView textFor_name, textFor_age,slogan, textFor_distance, textFor_gender, textFor_relation,txtFor_country;
}
