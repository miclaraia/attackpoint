package com.michael.attackpoint.drawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.TrainingActivity;
import com.michael.network.FavoriteUsersRequest;
import com.michael.objects.User;

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

    public NavDrawerGroup(AppCompatActivity activity) {
        mNavItems = new ArrayList<>();
        mActivity = activity;
        loadItems();
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
        String group = mHeader.getGroup();
        for (NavDrawerItem item : mNavItems) {
            item.setGroup(group);
        }
    }

    public String name() {
        return mHeader.getGroup();
    }

    public List<NavDrawerItem> getAll() {
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(mHeader);
        items.addAll(1, mNavItems);
        return items;
    }

    public abstract void action(NavDrawerItem item);
    public abstract void loadItems();
}
