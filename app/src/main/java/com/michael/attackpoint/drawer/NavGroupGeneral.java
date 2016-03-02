package com.michael.attackpoint.drawer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.michael.attackpoint.discussion.DiscussionActivity;
import com.michael.attackpoint.LogActivity;
import com.michael.attackpoint.LogFragment;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.R;
import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.TrainingActivity;
import com.michael.attackpoint.UsersFragment;
import com.michael.attackpoint.discussion.Discussion;
import com.michael.attackpoint.discussion.DiscussionRequest;
import com.michael.attackpoint.log.loginfo.LogClimb;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;
import com.michael.attackpoint.training.AddTrainingRequest;
import com.michael.database.CookieTable;
import com.michael.database.UserTable;
import com.michael.network.FavoriteUsersRequest;
import com.michael.network.MyCookieStore;
import com.michael.network.UserRequest;
import com.michael.objects.User;

import java.util.ArrayList;
import java.util.List;

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
                intent.putExtra(LogFragment.USER_ID, CookieTable.getCurrentID());
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
                Intent intent = new Intent(mActivity, TrainingActivity.class);
                mActivity.startActivity(intent);
            }
        }));

        //test training request
        mNavItems.add(new NavItemReg("Add Training Test", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                LogInfo li = new LogInfo();
                li.set(LogInfo.KEY_DISTANCE, new LogDistance(new LogDistance.Distance(5, "km"));



                li.setDistance(5, "km");
                li.setIntensity(3);
                li.setText("Android app test");
                li.setDuration("00:05:00");
                Request request = new AddTrainingRequest(li, new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean aBoolean) {
                        Log.d(DEBUG_TAG, "success");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        String response = new String(volleyError.networkResponse.data);
                        response = response.replaceAll("(\\r|\\n)", "");
                        VolleyLog.e(response);
                    }
                });
                Singleton.getInstance().add(request);
            }
        }));

        //display all cookies stored in database
        mNavItems.add(new NavItemReg("Check Cookies", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, mCookieStore.getAllCookies());
            }
        }));

        //list all users in database
        mNavItems.add(new NavItemReg("Check Users", GROUP_NAME, R.drawable.ic_person, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, UserTable.printAllUsers());
            }
        }));

        //clear all users from database
        mNavItems.add(new NavItemReg("Clear Users", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                UserTable.clearUsers();
            }
        }));

        //Test favorite user request system
        mNavItems.add(new NavItemReg("Check Favorites", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, "Checking favorites");
                Request request = new FavoriteUsersRequest(
                        new FavoriteUsersRequest.UpdateCallback() {
                            @Override
                            public void go() {

                            }
                        }, new Response.Listener<List<User>>() {
                    @Override
                    public void onResponse(List<User> users) {
                        Log.d(DEBUG_TAG, "Got response");
                        for (User user : users) {
                            Log.d(DEBUG_TAG, user.toString());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("Something went wrong!");
                        volleyError.printStackTrace();
                    }
                }
                );
                mSingleton.add(request);
            }
        }));

        // test discussion request
        mNavItems.add(new NavItemReg("Discussion Test", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, "Testing discussion request");
                Request request = new DiscussionRequest(1132702, new Response.Listener<Discussion>() {
                    @Override
                    public void onResponse(Discussion discussion) {
                        Log.d(DEBUG_TAG, "Got response");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(DEBUG_TAG, "Got error");
                        volleyError.printStackTrace();
                    }
                });
                mSingleton.add(request);
            }
        }));

        //test user request
        mNavItems.add(new NavItemReg("User Test", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
            @Override
            public void click() {
                Log.d(DEBUG_TAG, "Testing user request");
                Request request = new UserRequest(11778, new Response.Listener<User>() {
                    @Override
                    public void onResponse(User user) {
                        Log.d(DEBUG_TAG, "Got response");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.d(DEBUG_TAG, "Got Error");
                        e.printStackTrace();
                    }
                });
                mSingleton.add(request);
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
        mNavItems.add(new NavItemReg("Log Fragment", GROUP_NAME, R.drawable.ic_log, new NavDrawerItem.DrawerListener() {
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
        }));
    }
}
