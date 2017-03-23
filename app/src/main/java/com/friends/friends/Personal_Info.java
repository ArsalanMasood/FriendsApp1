package com.friends.friends;

import com.friends.friends.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class Personal_Info extends Fragment implements OnClickListener {

	ImageView imgFor_profile;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_personal__info, container, false);
		
		imgFor_profile = (ImageView) view.findViewById(R.id.imgFor_profile);
		
		imgFor_profile.setOnClickListener(this);
		
		return view;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		startActivity(new Intent(getActivity(), Profile_pic.class));
	}

}
