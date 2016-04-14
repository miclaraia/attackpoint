package com.michael.attackpoint;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.DecorContentParent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.michael.attackpoint.drawer.DrawerContract;
import com.michael.attackpoint.drawer.NavDrawer;
import com.michael.attackpoint.drawer.items.NavDrawerItem;
import com.michael.attackpoint.users.UsersFragment;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements DrawerContract.Activity {
    private static final String DEBUG_TAG = "attackpoint.Main";
    private Singleton mSingleton;
    private DrawerContract.Drawer mDrawer;

    private ArrayList<NavDrawerItem> navMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new UsersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.nav_list);

        NavDrawer drawer = new NavDrawer(this, drawerLayout, drawerList);
        mDrawer = drawer;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mSingleton = Singleton.getInstance();
        mSingleton.setDrawer(drawer);
        mSingleton.setActivity(this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //getActionBar().setElevation(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*if (drawer.checkToggleClick(item)) {
            return true;
        }*/
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.d("info","menu action: action_search");
                return true;
            case R.id.action_settings:
                Log.d("info","menu action: action_settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ActionBarDrawerToggle getDrawerToggle(DrawerLayout drawerLayout) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle("Attackpoint");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle("Menu");
            }
        };
        return toggle;
    }
}
