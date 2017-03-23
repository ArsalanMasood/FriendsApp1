package com.friends.friends.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.friends.friends.R;
import com.friends.friends.model.FriendInfo;

public class New_User_adapter extends BaseAdapter {

	FriendInfo gs;
	Context context;
	ArrayList<FriendInfo> array_list = new ArrayList<FriendInfo>();
	public New_User_adapter(Context context, ArrayList<FriendInfo> arraylist) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.array_list = arraylist;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View_Holder holder = new View_Holder();
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.custom_new_user, null);

			holder.textFor_name = (TextView) convertView.findViewById(R.id.txtFor_name);
			holder.textFor_age = (TextView) convertView.findViewById(R.id.txtFor_age);
			holder.textFor_gender = (TextView) convertView.findViewById(R.id.txtFor_gender);
			holder.image = (ImageView) convertView.findViewById(R.id.imageFor_Friend);

			convertView.setTag(holder);
		}
		else
		{
			holder = (View_Holder) convertView.getTag();
		}

		gs = new FriendInfo();
		gs = array_list.get(position);

		holder.image.setImageResource(gs.getImage());
		holder.textFor_name.setText(gs.getName());
		holder.textFor_age.setText(gs.getAge());
		holder.textFor_gender.setText(gs.getGender());

		return convertView;


	}

}




class View_Holder 
{
	ImageView image;
	TextView textFor_name,textFor_age,textFor_gender;
}
