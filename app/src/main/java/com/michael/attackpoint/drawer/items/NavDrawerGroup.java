package com.michael.attackpoint.drawer.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/22/15.
 */
public abstract class NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGroup";

    protected List<NavDrawerItem> mNavItems;
    protected String mName;

    public NavDrawerGroup(String name) {
        mNavItems = new ArrayList<>();
        mName = name;

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

    public void remove(NavDrawerItem item) {
        mNavItems.remove(item);
    }

    public int size() {
        return mNavItems.size();
    }

    public void check() {
        for (NavDrawerItem item : mNavItems) {
            item.setGroup(mName);
        }
    }

    public String name() {
        return mName;
    }

    public List<NavDrawerItem> getAll() {
        check();
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavItemHeader(mName));
        items.addAll(1, mNavItems);
        return items;
    }

    public abstract void loadItems();
}
