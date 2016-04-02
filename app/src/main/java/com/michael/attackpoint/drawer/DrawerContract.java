package com.michael.attackpoint.drawer;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

import com.michael.attackpoint.drawer.items.NavDrawerGroup;
import com.michael.attackpoint.drawer.items.NavDrawerItem;

/**
 * Created by michael on 3/31/16.
 */
public interface DrawerContract {

    interface Activity {
        Context getContext();

        FragmentManager getFragmentManager();

        void startActivity(Intent intent);
    }

    interface Drawer {
        void open();

        void close();

        void toggle();

        NavDrawerGroup getGroup(String name);

        void removeGroup(String name);

        void addItem(NavDrawerItem item);

        void removeItem(NavDrawerItem item);

        void refresh();
    }

}
