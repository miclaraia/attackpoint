package com.michael.attackpoint.drawer;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.TrainingActivity;
import com.michael.network.FavoriteUsersRequest;
import com.michael.network.MyCookieStore;
import com.michael.objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/22/15.
 */
public class NavGroupGeneral extends NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGeneral";
    private static final String GROUP_NAME = "General";

    private Singleton mSingleton;
    private MyCookieStore mCookieStore;
    private Preferences mPreferences;

    public NavGroupGeneral(AppCompatActivity activity) {
        super(activity);
        mSingleton = Singleton.getInstance();
        mCookieStore = mSingleton.getCookieStore();
        mPreferences = mSingleton.getPreferences();
    }


    @Override
    public void loadItems() {
        String[] navNames = mActivity.getResources().getStringArray(R.array.nav_general_names);

        TypedArray ar = mActivity.getResources().obtainTypedArray(R.array.nav_general_icons);
        int len = ar.length();
        int[] navMenuIcons = new int[len];
        for (int i = 0; i < len; i++)
            navMenuIcons[i] = ar.getResourceId(i, 0);
        ar.recycle();

        mNavItems = new ArrayList<>();

        for (int i = 0; i < navNames.length; i++) {
            NavDrawerItem item = new NavDrawerItem(navNames[i],
                    GROUP_NAME, navMenuIcons[i]);
            mNavItems.add(item);
        }
    }

    @Override
    public void action(NavDrawerItem item) {
        switch (item.getName()) {
            case "Add Training":
                Intent intent = new Intent(mActivity, TrainingActivity.class);
                mActivity.startActivity(intent);
                break;
            case "Check Cookies":
                Log.d(DEBUG_TAG, mCookieStore.getAllCookies());
                break;
            case "Check Favorites":
                Log.d(DEBUG_TAG, "Checking favorites");
                FavoriteUsersRequest request = new FavoriteUsersRequest(
                        new Response.Listener<List<User>>() {
                            @Override
                            public void onResponse(List<User> users) {
                                Log.d(DEBUG_TAG, "God response");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("Something went wrong!");
                        volleyError.printStackTrace();
                    }
                }
                );
                mSingleton.add(request);

        }
    }
}
