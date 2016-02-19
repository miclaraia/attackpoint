package com.michael.attackpoint.account;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;
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
        mDrawerGroup = (NavGroupUsers) mSingleton.getDrawer().getGroup(NavGroupUsers.GROUP_NAME);

        String u = mPreferences.getUser();
        if (!(u.equals("") || u == null)) {
            mLogin = true;
            mUser = u;
        }
    }

    public void doLogin(String u, String p) {
        LoginRequest request = new LoginRequest(u, p,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String username) {
                        mLogin = true;
                        // TODO add user to drawer
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
        mSingleton.getCookieStore().removeUser(mUser);
        mPreferences.setUser(null);

        mUser = null;
        mLogin = false;
    }
}
