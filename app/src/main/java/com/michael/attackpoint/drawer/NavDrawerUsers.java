package com.michael.attackpoint.drawer;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import com.michael.attackpoint.LogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/22/15.
 */
public class NavDrawerUsers extends NavDrawerGroup {
    public NavDrawerUsers(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void action(NavDrawerItem item) {

    }

    @Override
    public void loadItems() {

    }
    /*private int getFirstUser() {
        for (int i = 0; i < navMenuItems.size(); i++) {
            String g = navMenuItems.get(i).getGroup();
            if (g != null && g.equals("Account")) {
                return i;
            }
        }
        return navMenuItems.size();
    }

    private int countUsers() {
        int count = -1;
        for (int i = userFirst; i < navMenuItems.size(); i++) {
            if (navMenuItems.get(i).getGroup().equals("Account")) count++;
        }
        return count;
    }

    private int initUsers() {
        List<String> users = cookieStore.getAllUsers();
        List<NavDrawerItem> userItems = new ArrayList<>();
        int count = users.size();
        if (count > 0) {
            for (String user : users) {
                NavDrawerItem item = new NavDrawerItem(user, NavDrawerItem.TYPE_USER);
                userItems.add(item);
            }
            navMenuItems.addAll(userFirst, userItems);

            String currentUser = prefs.getUser();
            if (currentUser != "") {
                int index = findUser(currentUser);
                NavDrawerItem item = navMenuItems.get(index);
                navMenuItems.remove(index);
                navMenuItems.add(userFirst, item);
            }
        }
        return count;
    }

    public int findUser(String user) {
        for (int i = userFirst; i < navMenuItems.size(); i++) {
            NavDrawerItem item = navMenuItems.get(i);
            if (item.getName().equals(user)) return i;
        }
        return -1;
    }

    public int findUser(NavDrawerItem item) {
        for (int i = userFirst; i < navMenuItems.size(); i++) {
            if (navMenuItems.get(i).equals(item)) return i;
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
        NavDrawerItem item = navMenuItems.get(index);
        navMenuItems.remove(index);
        navMenuItems.add(userFirst, item);

        singleton.getPreferences().setUser(item.getName());

        // TODO spawns log if different fragment currently used?
        Fragment fragment = singleton.getFragment();
        if (fragment instanceof LogFragment) {
            ((LogFragment) fragment).getLog();
        }

        notifyUpdate();
        drawer.closeDrawer(Gravity.LEFT);
    }

    public void addUser(String name) {
        NavDrawerItem item = new NavDrawerItem(name, NavDrawerItem.TYPE_USER);
        navMenuItems.add(userFirst, item);
        singleton.getPreferences().setUser(name);
        setUser(userFirst);
    }

    public void removeUser(NavDrawerItem item) throws UserException {
        String user = singleton.getPreferences().getUser();
        int index = findUser(item);

        cookieStore.removeUser(item.getName());
        navMenuItems.remove(index);
        notifyUpdate();
        drawer.closeDrawer(Gravity.LEFT);

        if (user.equals(item.getName())) {
            setUser(userFirst);
        }
    }

    public class UserException extends Exception {
        public UserException(String message) {
            super(message);
        }

        public UserException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }*/
}
