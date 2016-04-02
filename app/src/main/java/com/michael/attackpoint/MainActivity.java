package com.michael.attackpoint;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.DecorContentParent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    //private DrawerLayout mDrawer;
    private MainActivity self = this;

    private ArrayList<NavDrawerItem> navMenuItems;
    private AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new UsersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.nav_list);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);

        NavDrawer drawer = new NavDrawer(this, drawerLayout, drawerList);

        mSingleton = Singleton.getInstance();
        mSingleton.setDrawer(drawer);
        mSingleton.setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setActivity(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
    public Context getContext() {
        return this;
    }
}
