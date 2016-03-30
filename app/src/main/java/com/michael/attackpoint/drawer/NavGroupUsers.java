package com.michael.attackpoint.drawer;

import android.support.v7.app.AppCompatActivity;

import com.michael.attackpoint.util.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.util.Singleton;
import com.michael.attackpoint.account.Login;
import com.michael.attackpoint.account.MyCookieStore;

/**
 * Created by michael on 11/22/15.
 */
public class NavGroupUsers extends NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGeneral";
    public static final String GROUP_NAME = "Account";
    public static final String ADD_USER = "Add User";
    public static final String USER_PRE = "";

    private Singleton mSingleton;
    private MyCookieStore mCookieStore;
    private Preferences mPreferences;

    public NavGroupUsers(NavDrawer drawer, AppCompatActivity activity) {
        super(drawer, activity);
    }

    @Override
    protected void init() {
        //initialize variables
        mSingleton = Singleton.getInstance();
        mCookieStore = mSingleton.getCookieStore();
        mPreferences = mSingleton.getPreferences();
    }

    @Override
    public void loadItems() {
        mHeader = new NavItemHeader(GROUP_NAME);

        final Login login = Login.getInstance();
        if (login.isLoggedIn()) {
            mNavItems.add(new NavItemReg(USER_PRE + login.getUser(), GROUP_NAME, R.drawable.ic_running, new NavDrawerItem.DrawerListener() {
                @Override
                public void click() {
                   // TODO some action when user is clicked
                }
            }));
            mNavItems.add(new NavItemReg("Logout", GROUP_NAME, R.drawable.ic_logout, new NavDrawerItem.DrawerListener() {
                @Override
                public void click() {
                    login.doLogout();
                }
            }));
        } else {
            mNavItems.add(new NavItemReg("Login", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
                @Override
                public void click() {
                    login.loginDialog();
                }
            }));
        }
    }

    public void reload() {
        mNavItems.removeAll(mNavItems);
        loadItems();
        mNavDrawer.notifyUpdate();
    }
}
