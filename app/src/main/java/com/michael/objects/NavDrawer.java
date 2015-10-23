package com.michael.objects;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.TrainingActivity;
import com.michael.attackpoint.adapters.DrawerAdapter;

import java.util.ArrayList;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawer {
    private static final String DEBUG_TAG = "NavDrawer";
    private Activity activity;
    private DrawerAdapter adapter;
    private ListView drawerList;
    private ArrayList<NavDrawerItem> navMenuItems;
    private DrawerLayout drawer;

    public NavDrawer(Activity activity, DrawerLayout drawer,
                     ArrayList<NavDrawerItem> navMenuItems,
                     ListView drawerList) {
        this.activity = activity;
        this.navMenuItems = navMenuItems;
        this.drawerList = drawerList;
        this.drawer = drawer;

        this.adapter = new DrawerAdapter(activity, navMenuItems);
        this.drawerList.setAdapter(adapter);

        DrawerClickListener l = new DrawerClickListener();
        drawerList.setOnItemClickListener(l);
    }

    public int addUser(String name) {
        NavDrawerItem item = new NavDrawerItem(name, NavDrawerItem.TYPE_USER);
        int i;
        for (i = 0; i < navMenuItems.size(); i++) {
            String g = navMenuItems.get(i).getGroup();
            if (g != null && g.equals("Account")) {
                navMenuItems.add(i, item);
                break;
            }
        }

        adapter = new DrawerAdapter(activity, navMenuItems);
        drawerList.setAdapter(adapter);
        return i;
    }

    public class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            view.getTag(R.id.drawer_info);
            Log.d(DEBUG_TAG, navMenuItems.get(position).getName());
            NavDrawerItem item = (NavDrawerItem) navMenuItems.get(position);
            if (item.getType() == NavDrawerItem.TYPE_REGULAR) {
                drawer.closeDrawer(Gravity.LEFT);
                switch (item.getAction()) {
                    case "account":
                        actionAccount(item);
                        break;
                    case "general":
                        actionGeneral(item);
                        break;
                }
            }
        }

        private void actionAccount(NavDrawerItem item) {
            switch (item.getName()) {
                case "Login":
                    Log.i(DEBUG_TAG, "Login pressed");
                    //activity.login();
                    break;
                case "Logout":
                    Log.i(DEBUG_TAG, "Logout pressed");
                    //activity.logout();
                    break;
            }
        }

        private void actionGeneral(NavDrawerItem item) {
            switch (item.getName()) {
                case "Add Training":
                    Intent intent = new Intent(activity, TrainingActivity.class);
                    activity.startActivity(intent);
                    break;
                case "Test2":
                    addUser("miclaraia");

            }
        }
    }

}
