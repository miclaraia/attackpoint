package com.michael.attackpoint.drawer.items;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.michael.attackpoint.drawer.DrawerContract;
import com.michael.attackpoint.drawer.NavDrawer;
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

    private Context mContext;

    public NavGroupUsers(DrawerContract.Activity activity) {
        super(GROUP_NAME);
        mContext = activity.getContext();
    }

    @Override
    public void loadItems() {

        final Login login = Login.getInstance();
        if (login.isLoggedIn()) {
            mNavItems.add(new NavItemReg(USER_PRE + login.getUser(), GROUP_NAME, R.drawable.ic_running) {
                @Override
                public void click() {
                   // TODO some action when user is clicked
                }
            });
            mNavItems.add(new NavItemReg("Logout", GROUP_NAME, R.drawable.ic_logout) {
                @Override
                public void click() {
                    login.doLogout();
                }
            });
        } else {
            mNavItems.add(new NavItemReg("Login", GROUP_NAME, R.drawable.ic_person) {
                @Override
                public void click() {
                    login.loginDialog(mContext);
                }
            });
        }
    }
}
