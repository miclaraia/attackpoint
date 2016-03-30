package com.michael.attackpoint.drawer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.michael.attackpoint.account.Login;
import com.michael.attackpoint.discussion.DiscussionActivity;
import com.michael.attackpoint.log.loglist.LogActivity;
import com.michael.attackpoint.log.loglist.LogFragment;
import com.michael.attackpoint.util.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.util.Singleton;
import com.michael.attackpoint.users.UsersFragment;
import com.michael.attackpoint.training.activity.AddTrainingActivity;
import com.michael.attackpoint.account.CookieTable;
import com.michael.attackpoint.account.MyCookieStore;

import java.util.ArrayList;

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
    }

    @Override
    protected void init() {
        mSingleton = Singleton.getInstance();
        mCookieStore = mSingleton.getCookieStore();
        mPreferences = mSingleton.getPreferences();
    }

    @Override
    public void loadItems() {
        /*String[] navNames = mActivity.getResources().getStringArray(R.array.nav_general_names);

        TypedArray ar = mActivity.getResources().obtainTypedArray(R.array.nav_general_icons);
        int len = ar.length();
        int[] navMenuIcons = new int[len];
        for (int i = 0; i < len; i++)
            navMenuIcons[i] = ar.getResourceId(i, 0);
        ar.recycle();*/

        mNavItems = new ArrayList<>();
        mHeader = new NavItemHeader(GROUP_NAME);

        //Open log of current user
        mNavItems.add(new NavItemReg("Log", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, LogActivity.class);
                intent.putExtra(LogFragment.USER_ID, Login));
                mActivity.startActivity(intent);
            }
        }));

        //Start DiscussionActivity looking at thread 1111416
        mNavItems.add(new NavItemReg("Discussion", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, DiscussionActivity.class);
                intent.putExtra(DiscussionActivity.DISCUSSION_ID, 1111416);
                mActivity.startActivity(intent);
            }
        }));

        //Start TrainingActivity
        mNavItems.add(new NavItemReg("Add Training", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity, AddTrainingActivity.class);
                mActivity.startActivity(intent);
            }
        }));

        mNavItems.add(new NavItemReg("Debug Menu", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                DebugDialog newFragment = new DebugDialog();
                newFragment.show(mActivity.getFragmentManager(), "debug menu");
            }
        }));

        mNavItems.add(new NavItemReg("User Fragment", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, "swapping fragments");
                Fragment fragment = new UsersFragment();

                FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        }));
        /*mNavItems.add(new NavItemReg("Log Fragment", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, "swapping fragments");
                Fragment fragment = new LogFragment();
                Bundle extras = new Bundle();
                extras.putInt(LogFragment.USER_ID, CookieTable.getCurrentID());
                fragment.setArguments(extras);

                FragmentTransaction transaction = mActivity.getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        }));*/
    }
}
