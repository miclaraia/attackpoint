package com.michael.attackpoint.account;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.dialogs.LoginActivity;
import com.michael.attackpoint.drawer.NavDrawer;
import com.michael.attackpoint.drawer.NavDrawerGroup;
import com.michael.attackpoint.drawer.NavGroupUsers;
import com.michael.network.LoginRequest;

/**
 * Created by michael on 2/19/16.
 */
public class Login {
    private static final String DEBUG_TAG = "ap.login";
    private String mUser;
    private Singleton mSingleton;
    private Preferences mPreferences;
    private NavGroupUsers mDrawerGroup;
    private boolean mLogin;

    public Login() {
        mSingleton = Singleton.getInstance();
        mPreferences = mSingleton.getPreferences();

        // TODO NullPointerException waiting to happen
        NavDrawer drawer = mSingleton.getDrawer();
        if (drawer != null) mDrawerGroup = (NavGroupUsers) drawer.getGroup(NavGroupUsers.GROUP_NAME);

        String u = mPreferences.getUser();
        if (!(u.equals("") || u == null)) {
            mLogin = true;
            mUser = u;
        } else {
            mLogin = false;
            mUser = null;
        }
    }

    public void performLogin() {
        Activity activity = mSingleton.getActivity();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public void doLogin(String u, String p) {
        LoginRequest request = new LoginRequest(u, p,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String username) {
                        //update variables
                        mLogin = true;
                        mUser = username;
                        //set user to preferences
                        mPreferences.setUser(username);

                        //reset drawer
                        mDrawerGroup.reload();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        System.out.println("Something went wrong!");
                        error.printStackTrace();
                    }
        });
        mSingleton.add(request);
    }

    public void doLogout() {
        //remove user from preferences and cookies
        mSingleton.getCookieStore().removeUser(mUser);
        mPreferences.setUser(null);

        //update variables
        mUser = null;
        mLogin = false;

        //reset drawer
        mDrawerGroup.reload();
    }

    public boolean isLoggedIn() {
        return mLogin;
    }

    public String getUser() {
        return mUser;
    }
}
