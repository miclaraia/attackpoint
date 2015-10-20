package com.michael.attackpoint;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.michael.network.AuthCookie;
import com.michael.network.Login;
import com.michael.network.MyCookieStore;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

/**
 * Created by michael on 8/18/15.
 */
public class Singleton extends Application {
    private static final String TAG = "com.michael.Attackpoint.request.";
    private RequestQueue mRequestQueue;
    private Login mLogin;
    private Preferences mPreferences;
    private Context mContext;
    private static Singleton mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mPreferences = new Preferences();
        mContext = getApplicationContext();
        mLogin = new Login();

        initCookies();
    }

    public static synchronized Singleton getInstance() {
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context;
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

    public Login getLogin() {
        return mLogin;
    }

    public void initCookies() {
        CookieStore cookieStore = new MyCookieStore();
        CookieManager manager = new CookieManager( cookieStore, CookiePolicy.ACCEPT_ALL );
        CookieHandler.setDefault(manager);
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }
}
