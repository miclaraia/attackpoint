package com.michael.attackpoint.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.michael.attackpoint.drawer.items.NavGroupUsers;
import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.training.request.TrainingTypeRequest;
import com.michael.attackpoint.account.MyCookieStore;
import com.michael.attackpoint.drawer.NavDrawer;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

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

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mActivity = null;

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mPreferences = new Preferences();
        mContext = getApplicationContext();

        initCookies();
        updateActivityTypes();
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
    public NavGroupUsers getUserGroup() {
        String g = NavGroupUsers.GROUP_NAME;
        NavGroupUsers navGroup = (NavGroupUsers) mDrawer.getGroup(g);
        return navGroup;
    }

    public void updateActivityTypes() {
        Request request = new TrainingTypeRequest(new Response.Listener<Map<String, Integer>>() {
            @Override
            public void onResponse(Map<String, Integer> response) {
                ActivityTable table = new ActivityTable();
                table.updateTable(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        add(request);
    }
}
