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

import java.util.ArrayList;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawer {
    private static final String DEBUG_TAG = "NavDrawer";

    private AppCompatActivity mActivity;
    private Adapter mAdapter;
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

        drawer.setDrawerListener(mDrawerToggle);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        mNavGroups = new ArrayList<>();
        mNavItems = new ArrayList<>();

        DrawerClickListener l = new DrawerClickListener();
        drawerList.setOnItemClickListener(l);

        mNavGroups.add(new NavGroupGeneral(this, mActivity));
        mNavGroups.add(new NavDrawerUsers(this, mActivity));
        notifyUpdate();
    }

    public void notifyUpdate() {
        ArrayList<NavDrawerItem> navItems = new ArrayList<>();
        for (NavDrawerGroup group : mNavGroups) {
            navItems.addAll(navItems.size(), group.getAll());
        }
        mNavItems = navItems;

        mAdapter = new Adapter(mActivity, navItems);
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

    /*public void notifyUpdate() {
        boolean bUserFirst = false;

        for (int i = 0; i < navMenuItems.size(); i++) {
            NavDrawerItem item = navMenuItems.get(i);
            String g = item.getGroup();
            if (!bUserFirst && g != null && g.equals("Account")) {
                userFirst = i;
                bUserFirst = true;
            }
        }

        userCount = countUsers();

        if (userCount <= 0) prefs.setUser(null);
        else prefs.setUser(navMenuItems.get(userFirst).getName());

        adapter = new Adapter(activity, navMenuItems);
        drawerList.setAdapter(adapter);


    }*/

    public class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            view.getTag(R.id.drawer_info);
            Log.d(DEBUG_TAG, mNavItems.get(position).getName());

            NavDrawerItem item = mNavItems.get(position);
            String group = item.getGroup();

            if (true || item.getType() == NavDrawerItem.TYPE_REGULAR) {
                mDrawer.closeDrawer(Gravity.LEFT);

                getGroup(group).action(item);
                /*switch (item.getAction()) {
                    case "account":
                        actionAccount(item);
                        break;
                    case "general":
                        actionGeneral(item);
                        break;
                }*/
            } //else if (item.getType() == NavDrawerItem.TYPE_USER) actionUser(item);
        }

        /*private void actionAccount(NavDrawerItem item) {
            switch (item.getName()) {
                case "Add User":
                    Log.i(DEBUG_TAG, "Login pressed");
                    login();
                    break;
                case "Logout":
                    Log.i(DEBUG_TAG, "Logout pressed");
                    //activity.logout();
                    break;
            }
        }

        private void actionUser(NavDrawerItem item) {
            try {
                setUser(item.getName());
            } catch (UserException e) {
                e.printStackTrace();
            }
        }

        private void login() {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }*/
    }

}
