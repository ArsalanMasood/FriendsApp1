package com.friends.friends;
import com.friends.friends.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Home extends Fragment implements OnTabChangeListener {

	TabHost tabhost;
	View view;
	int currentTab;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_home, container, false);
		tabhost = (TabHost) view.findViewById(R.id.tabhost);
		setupTabs();
		
		
		
		
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
		tabhost.setOnTabChangedListener(this);
		tabhost.setCurrentTab(currentTab);
		
		updateTabs("map", R.id.tab_1);
	}
	
	private void updateTabs(String tabId, int placeholder) {
		// TODO Auto-generated method stub
		android.support.v4.app.FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(tabId) == null) {
	//		fm.beginTransaction().replace(placeholder, new Map(), tabId);
//			fm.beginTransaction().replace(placeholder, new Map(tabId), tabId).commit();
		}
	}

	private void setupTabs() {
		// TODO Auto-generated method stub
	
		tabhost.setup();
		tabhost.addTab(newTab("map", 1 , R.id.tab_1));
		tabhost.addTab(newTab("list", 2 , R.id.tab_2));
	}

	private TabSpec newTab(String tag, int labelId, int tabContentId) {
	
		View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tabs, (ViewGroup) view.findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.txtFor_tab)).setText(labelId);
		
		TabSpec tabspec = tabhost.newTabSpec(tag);
		tabspec.setIndicator(indicator);
		tabspec.setContent(tabContentId);
		
		return tabspec;
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		if ("map".equals(tabhost)) {
			
			updateTabs(getTag(), R.id.tab_1);
			currentTab = 0;
			return;
		}
		
		if ("list".equals(tabId)) {
			updateTabs(tabId, R.id.tab_2);
			currentTab = 1;
			return;
		}
	}
	
}
