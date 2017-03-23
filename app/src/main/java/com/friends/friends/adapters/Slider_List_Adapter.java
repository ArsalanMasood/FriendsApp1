package com.friends.friends.adapters;

import java.util.ArrayList;

import com.friends.friends.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.friends.friends.model.FriendInfo;

public class Slider_List_Adapter extends BaseAdapter {

	ArrayList<FriendInfo> arrayList = new ArrayList<FriendInfo>();
	Context mContext;
	
	public Slider_List_Adapter(Context context, ArrayList<FriendInfo> array_list) {
		// TODO Auto-generated constructor stub
		this.arrayList = array_list;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
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
	//	Log.d("aa4", "444");
		ViewHolder holder = new ViewHolder();
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.slide_list_item, null);
			
			holder.textview = (TextView) convertView.findViewById(R.id.textForItem);

			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		FriendInfo sd = new FriendInfo();
		sd = arrayList.get(position);
	//	Log.d("name", ""+sd.getName());
		holder.textview.setText(sd.getName());
		
		holder.textview.setTag(position);
		
		return convertView;
	}
	
	class ViewHolder 
	{
		TextView textview;
	}

}
