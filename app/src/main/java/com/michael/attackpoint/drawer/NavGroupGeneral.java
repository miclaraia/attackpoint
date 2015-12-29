package com.michael.attackpoint.drawer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.LogFragment;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.TrainingActivity;
import com.michael.attackpoint.UsersFragment;
import com.michael.database.UserTable;
import com.michael.network.FavoriteUsersRequest;
import com.michael.network.MyCookieStore;
import com.michael.network.UserRequest;
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

    public NavGroupGeneral(NavDrawer drawer, AppCompatActivity activity) {
        super(drawer, activity);
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
        mHeader = new NavDrawerItem(GROUP_NAME, NavDrawerItem.TYPE_SEPERATOR);
        mHeader.setGroup(GROUP_NAME);

        for (int i = 0; i < navNames.length; i++) {
            NavDrawerItem item = new NavDrawerItem(navNames[i],
                    GROUP_NAME, navMenuIcons[i]);
            mNavItems.add(item);
        }
    }

    @Override
    public void action(NavDrawerItem item) {
        Request request;
        Fragment fragment;
        FragmentTransaction transaction;

        switch (item.getName()) {
            case "Add Training":
                Intent intent = new Intent(mActivity, TrainingActivity.class);
                mActivity.startActivity(intent);
                break;
            case "Check Cookies":
                Log.d(DEBUG_TAG, mCookieStore.getAllCookies());
                break;
            case "Check Users":
                Log.d(DEBUG_TAG, UserTable.printAllUsers());
                break;
            case "Clear Users":
                UserTable.clearUsers();
                break;
            case "Check Favorites":
                Log.d(DEBUG_TAG, "Checking favorites");
                request = new FavoriteUsersRequest(
                        new FavoriteUsersRequest.UpdateCallback() {
                            @Override
                            public void go() {

                            }
                        }, new Response.Listener<List<User>>() {
                            @Override
                            public void onResponse(List<User> users) {
                                Log.d(DEBUG_TAG, "Got response");
                                for (User user : users) {
                                    Log.d(DEBUG_TAG, user.toString());
                                }
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
                break;
            case "User Test":
                Log.d(DEBUG_TAG, "Testing user request");
                request = new UserRequest(11778, new Response.Listener<User>() {
                    @Override
                    public void onResponse(User user) {
                        Log.d(DEBUG_TAG, "Got response");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.d(DEBUG_TAG, "Got Error");
                        e.printStackTrace();
                    }
                });
                mSingleton.add(request);
                break;
            case "User Fragment":
                Log.d(DEBUG_TAG, "swapping fragments");
                fragment = new UsersFragment();

                transaction = mActivity.getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                break;
            case "Log Fragment":
                Log.d(DEBUG_TAG, "swapping fragments");
                fragment = new LogFragment();

                transaction = mActivity.getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

        }
    }
}
