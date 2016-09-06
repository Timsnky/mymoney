package com.timsnky.mymoney;

import java.util.ArrayList;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class Home extends ActionBarActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
    private int fragmentPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Expenses and Income
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Debtors & Creditors
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Investment
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// SETTINGS
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// ACCOUNTS
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], true));
		// Bank
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(5, -1)));
		// Cash
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(6, -1)));
		// Mobile
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(7, -1)));
		// Online
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(8, -1)));
		// Other
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(9, -1)));	

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

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
            fragmentPosition = position;
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
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
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

    @Override
    public void onBackPressed() {
        if(fragmentPosition == 0){
            super.onBackPressed();
        }else{
            getFragmentManager().popBackStackImmediate();
            fragmentPosition = 0;
        }
    }

    /**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle classArgs = new Bundle();
		switch (position) {
			case 0:
				Fragment summaryFragment = new Summary();
				transaction.replace(R.id.frame_container,summaryFragment);
				transaction.addToBackStack(null);
				transaction.commit();				
				Log.d("My Money", "Home");
				break;
			case 1:
				Fragment expensesFragment = new Expenses();
                classArgs.putInt("ClassCategory", 1);
                expensesFragment.setArguments(classArgs);
				transaction.replace(R.id.frame_container, expensesFragment);
				transaction.addToBackStack(null);
				transaction.commit();
				Log.d("My Money", "Expenses");
				break;
			case 2:
				Fragment debtorsFragment = new Expenses();
                classArgs.putInt("ClassCategory", 2);
                debtorsFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, debtorsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
				Log.d("My Money", "Debtors");
				break;
			case 3:
				Fragment investmentFragment = new Expenses();
                classArgs.putInt("ClassCategory", 3);
                investmentFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, investmentFragment);
                transaction.addToBackStack(null);
                transaction.commit();
				Log.d("My Money", "Investments");
				break;
			case 4:
                Fragment settingsFragment = new Settings();
                classArgs.putInt("ClassCategory", 4);
                settingsFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, settingsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
				Log.d("My Money", "Settings");
				break;
			case 6:
                Fragment bankFragment = new Expenses();
                classArgs.putInt("ClassCategory", 5);
                bankFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, bankFragment);
                transaction.addToBackStack(null);
                transaction.commit();
				Log.d("My Money", "Bank");
				break;
            case 7:
                Fragment cashFragment = new Expenses();
                classArgs.putInt("ClassCategory", 6);
                cashFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, cashFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.d("My Money", "Cash");
                break;
            case 8:
                Fragment mobileFragment = new Expenses();
                classArgs.putInt("ClassCategory", 7);
                mobileFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, mobileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.d("My Money", "Mobile");
                break;
            case 9:
                Fragment onlineFragment = new Expenses();
                classArgs.putInt("ClassCategory", 8);
                onlineFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, onlineFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.d("My Money", "Online");
                break;
            case 10:
                Fragment otherFragment = new Expenses();
                classArgs.putInt("ClassCategory", 9);
                otherFragment.setArguments(classArgs);
                transaction.replace(R.id.frame_container, otherFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.d("My Money", "Other");
                break;
	
			default:
				break;
		}
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
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
