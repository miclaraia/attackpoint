package com.michael.attackpoint.drawer;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/22/15.
 */
public abstract class NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGroup";

    protected List<NavDrawerItem> mNavItems;
    protected NavDrawerItem mHeader;

    protected AppCompatActivity mActivity;
    protected NavDrawer mNavDrawer;

    public NavDrawerGroup(NavDrawer drawer, AppCompatActivity activity) {
        mNavItems = new ArrayList<>();
        mNavDrawer = drawer;
        mActivity = activity;

        init();
        loadItems();
    }

    //empty void to allow but not require subclass to override
    protected void init() {
    }

    public void add(NavDrawerItem item) {
        mNavItems.add(item);
        check();
    }

    public void addAll(List<NavDrawerItem> items) {
        int end = mNavItems.size();
        mNavItems.addAll(end, items);
        check();
    }

    public void set(List<NavDrawerItem> items) {
        mNavItems = items;
        check();
    }

    public void setHeader(NavDrawerItem header) {
        mHeader = header;
    }

    public int size() {
        return mNavItems.size();
    }

    public void check() {
        String group = mHeader.getName();
        for (NavDrawerItem item : mNavItems) {
            item.setGroup(group);
        }
    }

    public String name() {
        return mHeader.getName();
    }

    public List<NavDrawerItem> getAll() {
        check();
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(mHeader);
        items.addAll(1, mNavItems);
        return items;
    }

    public abstract void loadItems();
}
