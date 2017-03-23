package com.friends.friends.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.friends.friends.PictureDetailActivity;
import com.friends.friends.R;
import com.friends.friends.TabFriendActivity;
import com.friends.friends.model.FriendInfo;
import com.friends.friends.model.ModelImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import utils.customview.FetchableCircularImageView;

public class Friends_pics_list_adapter extends BaseAdapter {

	LayoutInflater inflater;
	ViewHolderForComposer holder = new ViewHolderForComposer();
	String gs;
	Context context;
	List<ModelImage> array_list = new ArrayList<ModelImage>();

	public Friends_pics_list_adapter(Context context,
									 List<ModelImage> _arraylist) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.array_list = _arraylist;
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_friend_pic, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderForComposer) convertView.getTag();
		}

//		holder.img = (ImageView) convertView
//				.findViewById(R.id.imageView);

		holder.image = (FetchableCircularImageView) convertView
				.findViewById(R.id.my_image);
		holder.rl = (RelativeLayout) convertView
				.findViewById(R.id.llpic);

		ModelImage gs = array_list.get(position);

		if (gs.getUrl() != null && !gs.getUrl().isEmpty()) {
			Picasso.with(context)
					.load(gs.getUrl())
					.placeholder(R.drawable.c7edfc)
					.into(holder.image);

		}

		holder.rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PictureDetailActivity.class);
				intent.putExtra("url", array_list.get(position).getUrl());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

			}
		});
		holder.rl.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				//String url = array_list.get(position);

				return false;
			}
		});

//		holder.img.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, PictureDetailActivity.class);
//				intent.putExtra("url", array_list.get(position));
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(intent);
//
//				//		onClickProfileImageListener.OnClick(v,gs,position);
//			}
//		});

		//	holder.image.setImage(gs.getImg(), context.getResources().getDrawable(R.drawable.pickselect));

		return convertView;
	}
	static class ViewHolderForComposer {
		RelativeLayout rl;
	    ImageView img;
		FetchableCircularImageView image;
	}


}





