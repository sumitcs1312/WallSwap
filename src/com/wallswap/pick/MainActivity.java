package com.wallswap.pick;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.wallsawp.data.TimeUtils;
import com.wallswap.Callbacks;
import com.wallswap.drawer.HomeFragment;
import com.wallswap.drawer.adapter.NavDrawerListAdapter;
import com.wallswap.drawer.model.NavDrawerItem;

public class MainActivity extends Activity implements Callbacks {

//	@Override
//	protected void onResume() {
//		super.onResume();
//		 if (sharedpreferences.contains(drawerVal))
//	        {
//	        	int val = sharedpreferences.getInt(drawerVal, 0);
//	        	 mDrawerList.setSelection(val);
//	        }
//	}

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
  //  private ActionBarDrawerToggle mDrawerToggle;
    private int wallChangeTime = 60;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    
    // nav drawer title
    //private CharSequence mDrawerTitle;	
 
    // used to store app title
    //private CharSequence mTitle;
 
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
 
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
    
	private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter drawerAdapter;
    
	private String drawerVal="drawerVal";

    
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
	     sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	     final Editor editor = sharedpreferences.edit();
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_drawer_demo);
		//mTitle = mDrawerTitle = getTitle();
		 
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
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
         
 
        // Recycle the typed array
        navMenuIcons.recycle();
 
        // setting the nav drawer list adapter
        drawerAdapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(drawerAdapter);
		 if (sharedpreferences.contains(drawerVal))
        {
        	int val = sharedpreferences.getInt(drawerVal, 0);
//        	 mDrawerList.setSelection(val);
        	 mDrawerList.setItemChecked(val, true);
        	
        }
       
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// update selected item and title, then close the drawer
		         mDrawerList.setItemChecked(arg2, true);
		         mDrawerList.setSelection(arg2);
		         processWallTime(arg2);
		         mDrawerLayout.closeDrawer(Gravity.START);
		         editor.putInt(drawerVal, arg2); 
		         editor.commit();
			}        	
		});
        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
// 
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                R.drawable.ic_drawer,//nav menu toggle icon
//                R.string.app_name, // nav drawer open - description for accessibility
//                R.string.app_name // nav drawer close - description for accessibility
//        ){
//            public void onDrawerClosed(View view) {
//                getActionBar().setTitle("Wallswap");
//                // calling onPrepareOptionsMenu() to show action bar icons
//                invalidateOptionsMenu();
//            }
// 
//            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle("Wallswap");
//                // calling onPrepareOptionsMenu() to hide action bar icons
//                invalidateOptionsMenu();
//            }
//        };
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        displayView(0);
       // mCallbacks=(Callbacks)HomeFragment();
	}

	
	
	 protected void processWallTime(int arg2) {
		// TODO Auto-generated method stub
		switch(arg2){
		case 0:
			Toast.makeText(getApplicationContext(), "Select new Wallpapers to swap after 60 secs ", Toast.LENGTH_SHORT).show();
			TimeUtils.changeTime = 60;
			setWallChangeTime(60);
			break;
		case 1:
			Toast.makeText(getApplicationContext(), "Select new Wallpapers to swap after 5 mins ", Toast.LENGTH_SHORT).show();
			TimeUtils.changeTime = 300;
			setWallChangeTime(300);
			break;
		case 2:
			
			
			Toast.makeText(getApplicationContext(), "Select new Wallpapers to swap after 10 mins ", Toast.LENGTH_SHORT).show();
			TimeUtils.changeTime = 600;
			setWallChangeTime(600);
			break;
		case 3:
			Toast.makeText(getApplicationContext(), "Select new Wallpapers to swap after 30 mins ", Toast.LENGTH_SHORT).show();
			TimeUtils.changeTime = 1800;
			setWallChangeTime(1800);
			break;
		case 4:
			Toast.makeText(getApplicationContext(), "Select new Wallpapers to swap after 1 hour ", Toast.LENGTH_SHORT).show();
			TimeUtils.changeTime = 3600;
			setWallChangeTime(3600);
			break;
		case 5:
			Toast.makeText(getApplicationContext(), "Next Wallpaper swap tommorow", Toast.LENGTH_SHORT).show();
			TimeUtils.changeTime =3600*24;
			setWallChangeTime(3600*24);
			break;
		}
		
	}



	@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // toggle nav drawer on selecting action bar app icon/title
//	        if (mDrawerToggle.onOptionsItemSelected(item)) {
//	            return true;
//	        }
	        // Handle action bar actions click
//	        switch (item.getItemId()) {
//	        case R.id.action_settings:
//	            mDrawerLayout.openDrawer(Gravity.LEFT);
//	            return false;
//	        case R.id.select_all:
//	        	mCallbacks.onItemSelected(true);
//	        	return false;
//	        case R.id.action_deselect_all:
//	        	mCallbacks.onItemSelected(false);
//	        	return false;
//	        default:	
//	            return super.onOptionsItemSelected(item);
//	        }
		return false;
	    }
	 
	    /***
	     * Called when invalidateOptionsMenu() is triggered
	     */
	    @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	        // if nav drawer is opened, hide the action items
	        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	      //  menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	        return super.onPrepareOptionsMenu(menu);
	    }
	    
	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
	    public void setTitle(CharSequence title) {
//	        mTitle = title;
	        getActionBar().setTitle(title);
	    }
	 
	    /**
	     * When using the ActionBarDrawerToggle, you must call it during
	     * onPostCreate() and onConfigurationChanged()...
	     */
	 
	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
//	        mDrawerToggle.syncState();
	    }
	 
	    @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        // Pass any configuration change to the drawer toggls
//	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.drawer_demo, menu);
		return true;
	}
	
	/**
  * Diplaying fragment view for selected nav drawer list item
  * */
 private void displayView(int position) {
     // update the main content by replacing fragments
     Fragment fragment = new HomeFragment();
         
     if (fragment != null) {
         FragmentManager fragmentManager = getFragmentManager();
         fragmentManager.beginTransaction()
                 .replace(R.id.frame_container, fragment).commit();
        //  mCallbacks=(Callbacks)fragment;
         if(fragment.getView()!=null){
         fragment.getView().setFocusableInTouchMode(true);
         
         fragment.getView().setOnKeyListener( new OnKeyListener()
         {
        	 @Override
        	 public boolean onKey( View v, int keyCode, KeyEvent event )
        	 {
        		 if( keyCode == KeyEvent.KEYCODE_BACK )
        		 {
        			 finish();      
        		 }
        		 return false;
        	 }
         } );
         }
     } 
     
 }



	public int getWallChangeTime() {
		return wallChangeTime;
	}



	public void setWallChangeTime(int wallChangeTime) {
		this.wallChangeTime = wallChangeTime;
	}



	@Override
	public void openDrawer(boolean id) {
		// TODO Auto-generated method stub
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

}
