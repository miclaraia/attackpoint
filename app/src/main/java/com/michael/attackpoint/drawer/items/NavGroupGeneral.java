package com.michael.attackpoint.drawer.items;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;

import com.michael.attackpoint.account.Login;
import com.michael.attackpoint.discussion.DiscussionActivity;
import com.michael.attackpoint.drawer.DebugDialog;
import com.michael.attackpoint.drawer.DrawerContract;
import com.michael.attackpoint.log.loglist.LogActivity;
import com.michael.attackpoint.log.loglist.LogFragment;
import com.michael.attackpoint.R;
import com.michael.attackpoint.users.UsersFragment;
import com.michael.attackpoint.log.addentry.activity.AddTrainingActivity;

import java.util.ArrayList;

/**
 * Created by michael on 11/22/15.
 */
public class NavGroupGeneral extends NavDrawerGroup {
    private static final String DEBUG_TAG = "NavGeneral";
    public static final String GROUP_NAME = "General";

    DrawerContract.Activity mActivity;

    public NavGroupGeneral(DrawerContract.Activity activity) {
        super(GROUP_NAME);
        mActivity = activity;
    }

    @Override
    public void loadItems() {

        mNavItems = new ArrayList<>();

        //Open log of current user
        mNavItems.add(new NavItemReg("Log", GROUP_NAME, R.drawable.ic_log) {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity.getContext(), LogActivity.class);
                intent.putExtra(LogFragment.USER_ID, Login.getInstance().getUserId());
                mActivity.startActivity(intent);
            }
        });

        //Start DiscussionActivity looking at thread 1111416
        mNavItems.add(new NavItemReg("Discussion", GROUP_NAME, R.drawable.ic_log) {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity.getContext(), DiscussionActivity.class);
                intent.putExtra(DiscussionActivity.DISCUSSION_ID, 1111416);
                mActivity.startActivity(intent);
            }
        });

        //Start TrainingActivity
        mNavItems.add(new NavItemReg("Add Training", GROUP_NAME, R.drawable.ic_person) {
            @Override
            public void click() {
                Intent intent = new Intent(mActivity.getContext(), AddTrainingActivity.class);
                mActivity.startActivity(intent);
            }
        });

        mNavItems.add(new NavItemReg("Debug Menu", GROUP_NAME, R.drawable.ic_person) {
            @Override
            public void click() {
                DebugDialog newFragment = new DebugDialog();
                newFragment.show(mActivity.getFragmentManager(), "debug menu");
            }
        });

        mNavItems.add(new NavItemReg("User Fragment", GROUP_NAME, R.drawable.ic_log) {
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
        });
        /*mNavItems.add(new NavItemReg("Log Fragment", GROUP_NAME, R.drawable.ic_log) {
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
        });*/
    }
}
