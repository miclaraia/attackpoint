package com.michael.attackpoint;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.michael.attackpoint.drawer.NavDrawerUsers;
import com.michael.network.MyCookieStore;
import com.michael.attackpoint.drawer.NavDrawer;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Singleton class managing global objects that can be accessed
 * from any class in the application.
 * @author Michael Laraia
 */
public class Singleton extends Application {
    private static final String TAG = "com.michael.Attackpoint.request.";
    private RequestQueue mRequestQueue;
    private Preferences mPreferences;
    private Context mContext;
    private Activity mActivity;
    private NavDrawer mDrawer;
    private static Singleton mInstance;
    private MyCookieStore mCookieStore;
    private Fragment mFragment;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mActivity = null;

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mPreferences = new Preferences();
        mContext = getApplicationContext();

        initCookies();
    }

    /**
     * returns instance of Singleton initialized at application start
     * @return
     */
    public static synchronized Singleton getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * gets current activity
     * @return Activity
     */
    public Activity getActivity() {
        return mActivity;
    }

    /**
     * sets current activity
     * @param activity
     */
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    /**
     * returns instance of preferences
     * @return
     */
    public Preferences getPreferences() {
        return mPreferences;
    }

    /**
     * returns volley request queue
     * @return
     */
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    /**
     * returns cookie store for network operations
     * @return
     */
    public MyCookieStore getCookieStore() { return mCookieStore; }

    /**
     * initializes cookie store and attaches to cookie handler
     */
    public void initCookies() {
        mCookieStore = new MyCookieStore();
        CookieManager manager = new CookieManager( mCookieStore, CookiePolicy.ACCEPT_ALL );
        CookieHandler.setDefault(manager);
    }

    /**
     * adds request to request queue
     * @param req request
     * @param <T> request type
     */
    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * cancels all requests currently in queue
     */
    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }

    /**
     * sets navigation drawer
     * @param drawer
     */
    public void setDrawer(NavDrawer drawer) {
        this.mDrawer = drawer;
    }

    /**
     * gets navigation drawer
     * @return
     */
    public NavDrawer getDrawer() {
        return mDrawer;
    }

    /**
     * gets NavDrawerGroup controlling users in nav drawer
     * @return
     */
    public NavDrawerUsers getUserGroup() {
        String g = NavDrawerUsers.GROUP_NAME;
        NavDrawerUsers navGroup = (NavDrawerUsers) mDrawer.getGroup(g);
        return navGroup;
    }

    /**
     * Sets fragment currently being used
     * @param fragment
     */
    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    /**
     * gets curreng fragment
     * @return
     */
    public Fragment getFragment() {
        return mFragment;
    }
}
