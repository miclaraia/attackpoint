package com.michael.objects;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.TrainingActivity;
import com.michael.attackpoint.adapters.DrawerAdapter;
import com.michael.attackpoint.dialogs.LoginActivity;
import com.michael.network.MyCookieStore;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 8/24/15.
 */
public class NavDrawer {
    private static final String DEBUG_TAG = "NavDrawer";
    private Activity activity;
    private DrawerAdapter adapter;
    private ListView drawerList;
    private ArrayList<NavDrawerItem> navMenuItems;
    private DrawerLayout drawer;
    private Singleton singleton;
    private MyCookieStore cookieStore;
    private Preferences prefs;

    private int userFirst;
    private int userCount;

    public NavDrawer(Activity activity, DrawerLayout drawer,
                     ArrayList<NavDrawerItem> navMenuItems,
                     ListView drawerList) {
        this.activity = activity;
        this.navMenuItems = navMenuItems;
        this.drawerList = drawerList;
        this.drawer = drawer;

        this.singleton = Singleton.getInstance();
        this.cookieStore = singleton.getCookieStore();
        this.prefs = singleton.getPreferences();

        this.userFirst = getFirstUser();
        this.userCount = initUsers();

        this.adapter = new DrawerAdapter(activity, navMenuItems);
        this.drawerList.setAdapter(adapter);

        DrawerClickListener l = new DrawerClickListener();
        drawerList.setOnItemClickListener(l);
    }

    private int getFirstUser() {
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

    public void notifyUpdate() {
        boolean bUserFirst = false;

        for (int i = 0; i < navMenuItems.size(); i++) {
            NavDrawerItem item = navMenuItems.get(i);
            String g = item.getGroup();
            if (!bUserFirst && g != null && g.equals("Account")) {
                userFirst = i;
                bUserFirst = true;
            }
        }

        userCount = countUsers();

        if (userCount <= 0) prefs.setUser(null);
        else prefs.setUser(navMenuItems.get(userFirst).getName());

        adapter = new DrawerAdapter(activity, navMenuItems);
        drawerList.setAdapter(adapter);
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
        singleton.getLogManager().getLog();
        notifyUpdate();

        drawer.closeDrawer(Gravity.LEFT);
    }

    public void addUser(String name) {
        NavDrawerItem item = new NavDrawerItem(name, NavDrawerItem.TYPE_USER);
        navMenuItems.add(userFirst, item);
        notifyUpdate();
        singleton.getPreferences().setUser(name);
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
    }

    public class DrawerClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            view.getTag(R.id.drawer_info);
            Log.d(DEBUG_TAG, navMenuItems.get(position).getName());
            NavDrawerItem item = navMenuItems.get(position);
            if (item.getType() == NavDrawerItem.TYPE_REGULAR) {
                drawer.closeDrawer(Gravity.LEFT);
                switch (item.getAction()) {
                    case "account":
                        actionAccount(item);
                        break;
                    case "general":
                        actionGeneral(item);
                        break;
                }
            } else if (item.getType() == NavDrawerItem.TYPE_USER) actionUser(item);
        }

        private void actionAccount(NavDrawerItem item) {
            switch (item.getName()) {
                case "Add User":
                    Log.i(DEBUG_TAG, "Login pressed");
                    login();
                    break;
                case "Logout":
                    Log.i(DEBUG_TAG, "Logout pressed");
                    //activity.logout();
                    break;
            }
        }

        private void actionGeneral(NavDrawerItem item) {
            switch (item.getName()) {
                case "Add Training":
                    Intent intent = new Intent(activity, TrainingActivity.class);
                    activity.startActivity(intent);
                    break;
                case "Check Cookies":
                    Log.d(DEBUG_TAG, cookieStore.getAllCookies());

            }
        }

        private void actionUser(NavDrawerItem item) {
            try {
                setUser(item.getName());
            } catch (UserException e) {
                e.printStackTrace();
            }
        }

        private void login() {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }

}
