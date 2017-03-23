package com.friends.friends.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import utils.customview.FetchableCircularImageView;

import com.friends.friends.R;
import com.friends.friends.model.FriendInfo;

public class FriendListForComposer_Adapter extends BaseAdapter {
	
	LayoutInflater inflater;
	Context context;
	List<FriendInfo> friendList = new ArrayList<>();
	List<FriendInfo> arraylist;

	ViewHolderForComposer holder = new ViewHolderForComposer();
	
	public FriendListForComposer_Adapter(List<FriendInfo> friendList, Context context) {
		this.friendList = friendList;
		this.arraylist=friendList;
		this.context = context;
		inflater = LayoutInflater.from(this.context);
		this.friendList = friendList;
	}
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		this.friendList= new ArrayList<FriendInfo>();
		if (charText.length() == 0) {
			this.friendList.addAll(arraylist);
		}
		else
		{
			for (FriendInfo wp : arraylist)
			{
				if (wp.getName().toLowerCase(Locale.getDefault()).startsWith(charText)  || wp.getLastName().toLowerCase(Locale.getDefault()).startsWith(charText))
				{
					this.friendList.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return friendList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return friendList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_message_compose, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderForComposer) convertView.getTag();
		}
		
		FriendInfo gs = friendList.get(position);
		
		holder.txtFirstName = (TextView) convertView.findViewById(R.id.fnameC);
		holder.txtFirstName.setText(gs.getName() + " " + gs.getLastName());

		holder.image = (FetchableCircularImageView) convertView.findViewById(R.id.propicC);
		holder.image.setImage(gs.getImg(), context.getResources().getDrawable(R.drawable.pickselect));
		
		return convertView;
	}
}

class ViewHolderForComposer {
	FetchableCircularImageView image;
	TextView txtFirstName, txtLastName;
}
