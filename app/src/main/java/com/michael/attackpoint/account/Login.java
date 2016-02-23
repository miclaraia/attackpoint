package com.michael.attackpoint.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.MainActivity;
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

        String u = mPreferences.getUser();
        if (!(u.equals("") || u == null)) {
            mLogin = true;
            mUser = u;
        } else {
            mLogin = false;
            mUser = null;
        }
    }

    public void loginDialog() {
        Activity activity = mSingleton.getActivity();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public void doLogin(String u, String p) {
        /* set current user beforehand so cookiestore can access
           it when storing login cookies.
          */
        final String oldUser = mUser;
        mUser = u;
        LoginRequest request = new LoginRequest(u, p,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String username) {
                        //update variables
                        mLogin = true;
                        //set user to preferences
                        mPreferences.setUser(username);

                        //reset drawer to load new user
                        restartActivity();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error handling
                        //reset current user to previous state
                        mUser = oldUser;
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
        restartActivity();
    }

    public boolean isLoggedIn() {
        return mLogin;
    }

    public String getUser() {
        return mUser;
    }

    private void restartActivity() {
        /*NavDrawer drawer = mSingleton.getDrawer();
        if (drawer != null) {
            NavGroupUsers drawerGroup = (NavGroupUsers) drawer.getGroup(NavGroupUsers.GROUP_NAME);
            if (drawerGroup != null) drawerGroup.reload();
        }*/

        Activity curActivity = mSingleton.getActivity();
        Context context = mSingleton.getContext();
        Intent intent = new Intent(context, MainActivity.class);

        curActivity.startActivity(intent);
        curActivity.finish();
    }
}
