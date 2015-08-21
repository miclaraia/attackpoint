package com.michael.attackpoint;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.michael.network.AuthCookie;

/**
 * Created by michael on 8/18/15.
 */
public class Singleton extends Application {
    private static final String TAG = "com.michael.Attackpoint.request.";
    private RequestQueue mRequestQueue;
    private Preferences mPreferences;
    private AuthCookie mCookie;
    private Context mContext;
    private static Singleton mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mPreferences = new Preferences();
        mContext = getApplicationContext();
        mCookie = new AuthCookie();
    }

    public static synchronized Singleton getInstance() {
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public AuthCookie getCookie() {
        return mCookie;
    }

    public Context getContext() {
        return mContext;
    }

    public Preferences getPreferences() {
        return mPreferences;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }
}
