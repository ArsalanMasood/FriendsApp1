package com.friends.friends.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import com.friends.friends.TabMapActivity;
import com.friends.friends.model.FriendInfo;
import com.squareup.picasso.Picasso;

public class Friends_list_adapter extends BaseAdapter {

	LayoutInflater inflater;
	MessageUserViewHolder holder = new MessageUserViewHolder();
	FriendInfo gs;
	Context context;
	List<FriendInfo> array_list = new ArrayList<FriendInfo>();
	List<FriendInfo> arraylist;

	public Friends_list_adapter(Context context,
								List<FriendInfo> _arraylist) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.array_list = _arraylist;
		arraylist = new ArrayList<FriendInfo>();
		arraylist = _arraylist;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return array_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		this.array_list = new ArrayList<FriendInfo>();
		if (charText.length() == 0) {
			this.array_list.addAll(arraylist);
		} else {
			for (FriendInfo wp : arraylist) {
				if (wp.getName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
					this.array_list.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

	// Filter Class
//	public void filterOther(String name,String gender,String country,String stragemin,String stragemax,String relation) {
//		gender = gender.toLowerCase(Locale.getDefault());
//		name = name.toLowerCase(Locale.getDefault());
//	//	name = name.toLowerCase(Locale.getDefault());
//		country = country.toLowerCase(Locale.getDefault());
//		relation = relation.toLowerCase(Locale.getDefault());
//		this.array_list = new ArrayList<FriendInfo>();
////		if (gender.length()==0) {
////			this.array_list.addAll(arraylist);
////		} else {
//			for (FriendInfo wp : arraylist) {
//				if (wp.getName().toLowerCase(Locale.getDefault()).startsWith(name) && wp.getGender().toLowerCase(Locale.getDefault()).startsWith(gender) && wp.getCountry().toLowerCase(Locale.getDefault()).startsWith(country) &&
//						wp.getRelation().toLowerCase(Locale.getDefault()).startsWith(relation)) {
//					int age=0;
//					int agemax=0;
//					int agemin=0;
//					if(wp.getAge()!=null && wp.getAge()!="") {
//						age = Integer.parseInt(wp.getAge());
//						agemax=Integer.parseInt(stragemax);
//						agemin=Integer.parseInt(stragemin);
//					}
//					else{
//						age=0;
//					}
//					if(age>=agemin && age<=agemax) {
//						this.array_list.add(wp);
//					}
//				}
//			}
////		}
//		notifyDataSetChanged();
//	}

	public void filterOther(String gender,String country,String stragemin,String stragemax,String relation) {
		gender = gender.toLowerCase(Locale.getDefault());
		//	name = name.toLowerCase(Locale.getDefault());
		country = country.toLowerCase(Locale.getDefault());
		relation = relation.toLowerCase(Locale.getDefault());
		this.array_list = new ArrayList<FriendInfo>();
//		if (gender.length()==0) {
//			this.array_list.addAll(arraylist);
//		} else {
		for (FriendInfo wp : arraylist) {
			if (wp.getGender().toLowerCase(Locale.getDefault()).startsWith(gender) && wp.getCountry().toLowerCase(Locale.getDefault()).startsWith(country) &&
					wp.getRelation().toLowerCase(Locale.getDefault()).startsWith(relation)) {
				int age=0;
				int agemax=0;
				int agemin=0;
				if(wp.getAge()!=null && wp.getAge()!="") {
					age = Integer.parseInt(wp.getAge());
					agemax=Integer.parseInt(stragemax);
					agemin=Integer.parseInt(stragemin);
				}
				else{
					age=0;
				}
				if(age>=agemin && age<=agemax) {
					this.array_list.add(wp);
				}
			}
		}
//		}
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_friend_list, null);
			convertView.setTag(holder);
		} else {
			holder = (MessageUserViewHolder) convertView.getTag();
		}

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

		gs = array_list.get(position);

		holder.textFor_name.setText(gs.getName());
		holder.textFor_age.setText(gs.getAge());
		holder.textFor_gender.setText(gs.getGender());
		holder.textFor_relation.setText(gs.getRelation());
		holder.textFor_distance.setText(gs.getDistance());
		if(!gs.getSlogan().equalsIgnoreCase("")) {
			holder.slogan.setText(gs.getSlogan());
			holder.slogan.setVisibility(View.VISIBLE);
		}
		if (gs.getImg() != null && !gs.getImg().isEmpty()) {
			Picasso.with(context)
					.load(gs.getImg())
					.placeholder(R.drawable.pickselect)
					.into(holder.image);

		}
		if(!gs.getCountry().equalsIgnoreCase("")) {
			holder.txtFor_country.setText(gs.getCountry());
			holder.txtFor_country.setVisibility(View.VISIBLE);
		}

		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PictureDetailActivity.class);
				intent.putExtra("url", array_list.get(position).getImg());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

				//		onClickProfileImageListener.OnClick(v,gs,position);
			}
		});

		//	holder.image.setImage(gs.getImg(), context.getResources().getDrawable(R.drawable.pickselect));
		return convertView;
	}

}

class MessageUserViewHolder {
	FetchableCircularImageView image;
	TextView textFor_name, textFor_age,slogan, textFor_distance, textFor_gender, textFor_relation,txtFor_country;
}





