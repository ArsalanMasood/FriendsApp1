package com.friends.friends;

import java.util.ArrayList;

import layout.MainLayout;
import utils.Global;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.friends.friends.R;
import com.friends.friends.adapters.NavDrawerListAdapter;
import com.friends.friends.model.FriendInfo;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("NewApi")
@SuppressWarnings("ResourceType")
public class Slide_Bar extends Activity{

	public static DrawerLayout mDrawerLayout;
	public static ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	public static String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<FriendInfo> navDrawerItems;
	private NavDrawerListAdapter adapter;

	//	Slider slider;
	MainLayout mainLayout;
	Fragment fragment = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slide__bar);

		mTitle = mDrawerTitle = getTitle();
		Global.g_currentActivity = this;
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<FriendInfo>();
		
		FriendInfo gs;
		// adding nav drawer items to array
		// Home
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[0]);
		gs.setIcon(navMenuIcons.getResourceId(0, -1));
		navDrawerItems.add(gs);
		// Find People
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[1]);
		gs.setIcon(navMenuIcons.getResourceId(1, -1));
		navDrawerItems.add(gs);
		// Photos
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[2]);
		gs.setIcon(navMenuIcons.getResourceId(2, -1));

		navDrawerItems.add(gs);
		// Communities, Will add a counter here
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[3]);
		gs.setIcon(navMenuIcons.getResourceId(3, -1));
		navDrawerItems.add(gs);
		
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[4]);
		gs.setIcon(navMenuIcons.getResourceId(4, -1));
		navDrawerItems.add(gs);
		
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[5]);
		gs.setIcon(navMenuIcons.getResourceId(5, -1));
		navDrawerItems.add(gs);
		
		gs = new FriendInfo();
		gs.setTitle(navMenuTitles[6]);
		gs.setIcon(navMenuIcons.getResourceId(6, -1));

		navDrawerItems.add(gs);

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

	}

	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
/*	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
*/	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
		
	private void displayView(int position) {
		// update the main content by replacing fragments

		switch (position) {
		case 0:
			fragment = new Map();
			break;
		case 1:
			fragment = new Personal_Info();
			break;
		case 2:
			/*fragment = new Messages() ;
			*/break;
		case 3:
			fragment = new Destination();
			break;
		case 4:
			fragment = new Favorites();
			break;
		case 5:
			fragment = new NewUser();
			break;
		case 6:
			fragment = new Settings();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}



}
