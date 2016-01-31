package com.michael.attackpoint;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.michael.attackpoint.adapters.DrawerAdapter;

import android.widget.ListView;

import com.michael.attackpoint.drawer.NavDrawer;
import com.michael.attackpoint.drawer.NavDrawerItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "attackpoint.Main";
    private Singleton singleton;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private DrawerAdapter drawerAdapter;
    private NavDrawer drawer;
    private MainActivity self = this;

    private ArrayList<NavDrawerItem> navMenuItems;
    private AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new UsersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawer = new NavDrawer(activity, drawerLayout, drawerList);

        singleton = Singleton.getInstance();
        singleton.setDrawer(drawer);
        singleton.setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Singleton.getInstance().setActivity(this);
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

        if (drawer.checkToggleClick(item)) {
            return true;
        }
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
}
