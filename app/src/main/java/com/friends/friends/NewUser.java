package com.friends.friends;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.friends.friends.R;
import com.friends.friends.adapters.New_User_adapter;
import com.friends.friends.model.FriendInfo;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewUser extends Fragment {

	ListView listFor_nUser;
	int[] images = {R.drawable.p,R.drawable.pic,R.drawable.pro_pic,R.drawable.fb,R.drawable.a };
	String[] name = {"Steave","Michel","David hussey","Gillcrist","John"};
	String[] age = {"21 years old","22 years old","20 years old","19 years old","18 years old"};
//	String[] distance = {"6.06 km (6.10 miles) away","24.12 km (5.35 miles) away","12.06 km (15.50 miles) away","10.06 km (9.90 miles) away","5.06 km (15.90 miles) away"};
	String[] gender = {"male","female","male","female","male"};
	ArrayList<FriendInfo> array_list = new ArrayList<FriendInfo>();
	FriendInfo gs;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_new_user, container, false);
	
	listFor_nUser = (ListView) view.findViewById(R.id.listFor_nUser);
		
		for (int i = 0; i < images.length; i++) {
			gs = new FriendInfo();
			gs.setImage(images[i]);
			gs.setAge(age[i]);
			gs.setName(name[i]);
			gs.setGender(gender[i]);
			array_list.add(gs);
		}
		
		listFor_nUser.setAdapter( new New_User_adapter(getActivity(), array_list));

		
		return view;
	
	}
	

}
