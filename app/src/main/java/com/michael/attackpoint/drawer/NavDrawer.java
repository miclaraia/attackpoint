package com.michael.attackpoint.drawer;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.util.Singleton;

import java.util.ArrayList;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawer {
    private static final String DEBUG_TAG = "NavDrawer";

    private AppCompatActivity mActivity;
    private DrawerAdapter mAdapter;
    private ListView mDrawerList;
    private DrawerLayout mDrawer;

    private ArrayList<NavDrawerGroup> mNavGroups;
    private ArrayList<NavDrawerItem> mNavItems;

    private ActionBarDrawerToggle mDrawerToggle;

    public NavDrawer(AppCompatActivity activity, DrawerLayout drawer,
                     ListView drawerList) {
        mActivity = activity;
        mDrawerList = drawerList;
        mDrawer = drawer;

        mDrawerToggle = new ActionBarDrawerToggle(activity, drawer,
                R.string.drawer_open, R.string.drawer_close);

        Singleton.getInstance().setDrawer(this);

        drawer.setDrawerListener(mDrawerToggle);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        mNavGroups = new ArrayList<>();
        mNavItems = new ArrayList<>();

        DrawerClickListener l = new DrawerClickListener();
        drawerList.setOnItemClickListener(l);

        mNavGroups.add(new NavGroupGeneral(this, mActivity));
        mNavGroups.add(new NavGroupUsers(this, mActivity));
        notifyUpdate();
    }

    public void notifyUpdate() {
        ArrayList<NavDrawerItem> navItems = new ArrayList<>();
        for (NavDrawerGroup group : mNavGroups) {
            navItems.addAll(navItems.size(), group.getAll());
        }
        mNavItems = navItems;

        mAdapter = new DrawerAdapter(mActivity, navItems);
        mDrawerList.setAdapter(mAdapter);
    }

    public boolean checkToggleClick(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    public void addGroup(NavDrawerGroup group) {
        mNavGroups.add(group);
    }

    public void addGroup(int position, NavDrawerGroup group) {
        mNavGroups.add(position, group);
    }

    public int findGroup(String name) {
        for (int i = 0; i < mNavGroups.size(); i++) {
            if (name.equals(mNavGroups.get(i).name())) {
                return i;
            }
        }
        return -1;
    }

    public NavDrawerGroup getGroup(String name) {
        int position = findGroup(name);
        if (position < 0) return null;
        return mNavGroups.get(position);
    }


    public void removeGroup(String name) {
        int position = findGroup(name);
        if (position >= 0) {
            mNavGroups.remove(position);
        }
    }

    public void removeGroup(NavDrawerGroup group) {
        mNavGroups.remove(group);
    }

    public class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            view.getTag(R.id.drawer_info);
            Log.d(DEBUG_TAG, mNavItems.get(position).getName());

            NavDrawerItem item = mNavItems.get(position);
            String group = item.getGroup();

            mDrawer.closeDrawer(Gravity.LEFT);

            //implement the items action
            item.action();
        }
    }
}
