package com.michael.attackpoint.drawer;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.michael.attackpoint.LogFragment;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.account.Login;
import com.michael.attackpoint.dialogs.LoginActivity;
import com.michael.network.MyCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/22/15.
 */
public class NavGroupUsers extends NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGeneral";
    public static final String GROUP_NAME = "Account";
    public static final String ADD_USER = "Add User";
    public static final String USER_PRE = "Logged in as: ";

    private Singleton mSingleton;
    private MyCookieStore mCookieStore;
    private Preferences mPreferences;

    public NavGroupUsers(NavDrawer drawer, AppCompatActivity activity) {
        super(drawer, activity);

        //initialize class variables
        mSingleton = Singleton.getInstance();
        mCookieStore = mSingleton.getCookieStore();
        mPreferences = mSingleton.getPreferences();
    }

    @Override
    public void loadItems() {
        mHeader = new NavItemHeader(GROUP_NAME);

        Login l = mSingleton.getLogin();
        if (l.isLoggedIn()) {
            mNavItems.add(new NavItemReg(USER_PRE + l.getUser(), GROUP_NAME, R.drawable.ic_running, new NavDrawerItem.DrawerListener() {
                @Override
                public void click() {
                   // TODO some action when user is clicked
                }
            }));
            mNavItems.add(new NavItemReg("Logout", GROUP_NAME, R.drawable.ic_logout, new NavDrawerItem.DrawerListener() {
                @Override
                public void click() {
                    mSingleton.getInstance().getLogin().doLogout();
                }
            }));
        } else {
            mNavItems.add(new NavItemReg("Login", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
                @Override
                public void click() {
                    mSingleton.getInstance().getLogin().performLogin();
                }
            }));
        }
    }

    public void reload() {
        mNavItems.removeAll(mNavItems);
        loadItems();
    }
}
