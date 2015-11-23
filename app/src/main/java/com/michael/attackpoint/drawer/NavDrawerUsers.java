package com.michael.attackpoint.drawer;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.michael.attackpoint.LogFragment;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.dialogs.LoginActivity;
import com.michael.network.MyCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/22/15.
 */
public class NavDrawerUsers extends NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGeneral";
    public static final String GROUP_NAME = "Users";
    public static final String ADD_USER = "Add User";

    private Singleton mSingleton;
    private MyCookieStore mCookieStore;
    private Preferences mPreferences;

    public NavDrawerUsers(NavDrawer drawer, AppCompatActivity activity) {
        super(drawer, activity);
    }

    @Override
    public void action(NavDrawerItem item) {
        String name = item.getName();
        if (name.equals(ADD_USER)) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(intent);
        } else {
            try {
                setUser(name);
            } catch (UserException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadItems() {
        init();

        List<String> users = mCookieStore.getAllUsers();
        int count = users.size();
        if (count > 0) {
            for (String user : users) {
                NavDrawerItem item = new NavDrawerItem(user, NavDrawerItem.TYPE_USER);
                mNavItems.add(mNavItems.size() - 1, item);
            }

            String currentUser = mPreferences.getUser();
            if (currentUser != "") {
                int index = findUser(currentUser);
                NavDrawerItem item = mNavItems.get(index);
                mNavItems.remove(index);
                mNavItems.add(0, item);
            }
        }
    }

    public void init() {
        mSingleton = Singleton.getInstance();
        mCookieStore = mSingleton.getCookieStore();
        mPreferences = mSingleton.getPreferences();

        mNavItems.add(new NavDrawerItem(ADD_USER, GROUP_NAME, R.drawable.ic_adduser));
        mHeader = new NavDrawerItem(GROUP_NAME, NavDrawerItem.TYPE_SEPERATOR);
        mHeader.setGroup(GROUP_NAME);
    }

    public int findUser(String user) {
        for (int i = 0; i < mNavItems.size(); i++) {
            NavDrawerItem item = mNavItems.get(i);
            if (item.getName().equals(user)) return i;
        }
        return -1;
    }

    public int findUser(NavDrawerItem item) {
        for (int i = 0; i < mNavItems.size(); i++) {
            if (mNavItems.get(i).equals(item)) return i;
        }
        return -1;
    }

    public void setUser(String user) throws UserException {
        int index = findUser(user);
        if (index < 0) {
            throw new UserException("Error finding user");
        } else {
            setUser(index);
        }
    }

    public void setUser(int index) {
        NavDrawerItem item = mNavItems.get(index);
        if (index > 0) {
            mNavItems.remove(index);
            mNavItems.add(0, item);
        }

        mNavDrawer.notifyUpdate();
        mPreferences.setUser(item.getName());

        // TODO spawns log if different fragment currently used?
        Fragment fragment = mSingleton.getFragment();
        if (fragment instanceof LogFragment) {
            ((LogFragment) fragment).getLog();
        }
    }

    public void addUser(String name) {
        NavDrawerItem item = new NavDrawerItem(name, NavDrawerItem.TYPE_USER);
        mNavItems.add(0, item);
        mPreferences.setUser(name);
        setUser(0);
    }

    public void removeUser(NavDrawerItem item) throws UserException {
        String user = mPreferences.getUser();
        int index = findUser(item);

        mCookieStore.removeUser(item.getName());
        mNavItems.remove(index);

        if (size() > 1 && user.equals(item.getName())) {
            setUser(0);
        } else {
            mNavDrawer.notifyUpdate();
            mPreferences.setUser(null);
            //todo clear log screen
        }
    }

    public class UserException extends Exception {
        public UserException(String message) {
            super(message);
        }

        public UserException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
