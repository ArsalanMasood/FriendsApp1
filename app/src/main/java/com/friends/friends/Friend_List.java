package com.friends.friends;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.friends.friends.R;
import com.friends.friends.adapters.Friends_list_adapter;
import com.friends.friends.model.FriendInfo;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Friend_List implements OnItemClickListener {

	ListView listFor_Friends;
	int[] images = { R.drawable.p, R.drawable.pic, R.drawable.pro_pic,
			R.drawable.fb, R.drawable.a };
	String[] name = { "Aman deol", "Rajeev Rana", "Sam", "Devid", "Sane Watson" };
	String[] age = { "24 years old", "24 years old", "28 years old",
			"20 years old", "27 years old" };
	String[] distance = { "6.06 km (6.10 miles) away",
			"24.12 km (5.35 miles) away", "12.06 km (15.50 miles) away",
			"10.06 km (9.90 miles) away", "5.06 km (15.90 miles) away" };
	ArrayList<FriendInfo> array_list = new ArrayList<FriendInfo>();
	FriendInfo gs;
	Context m_context;

	public Friend_List(Context context) {
		// TODO Auto-generated constructor stub
		this.m_context = context;
	}
	public Friend_List(){

	}

	public void view_freind_list(View view) {
		// TODO Auto-generated method stub
		// View view = inflater.inflate(R.layout.activity_favorites, container,
		// false);

		listFor_Friends = (ListView) view.findViewById(R.id.listFor_Friends);
		for (int i = 0; i < images.length; i++) {
			gs = new FriendInfo();
			gs.setImage(images[i]);
			gs.setAge(age[i]);
			gs.setName(name[i]);
			gs.setDistance(distance[i]);
			array_list.add(gs);
		}

		Friends_list_adapter adapter = new Friends_list_adapter(m_context,
				array_list);
		listFor_Friends.setAdapter(adapter);

		listFor_Friends.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// Toast.makeText(m_context, "clicked", 300).show();
		Fragment fragment = new Personal_Info();
		FragmentManager fragmentManager = ((Activity) m_context)
				.getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();

		Slide_Bar.mDrawerList.setItemChecked(position, true);
		Slide_Bar.mDrawerList.setSelection(position);
		setTitle(Slide_Bar.navMenuTitles[position]);
		Slide_Bar.mDrawerLayout.closeDrawer(Slide_Bar.mDrawerList);
		/*
		 * Intent intent = new Intent(m_context, Personal_Info.class);
		 * m_context.startActivity(intent);
		 */}

	public void setTitle(CharSequence title) {
		CharSequence mTitle = title;
		// Toast.makeText(m_context, "clicked"+mTitle, 300).show();
		((Activity) m_context).getActionBar().setTitle(mTitle);
	}

}
