package com.michael.attackpoint;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.michael.network.AuthCookie;
import com.michael.network.Login;
import com.michael.network.MyCookieStore;
import com.michael.network.apLog;
import com.michael.objects.NavDrawer;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 8/18/15.
 */
public class Singleton extends Application {
    private static final String TAG = "com.michael.Attackpoint.request.";
    private RequestQueue mRequestQueue;
    private Login mLogin;
    private Preferences mPreferences;
    private Context mContext;
    private NavDrawer mDrawer;
    private static Singleton mInstance;
    private MyCookieStore mCookieStore;
    private Map<String, Object> mLoginResponse;
    private apLog mLogManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mPreferences = new Preferences();
        mContext = getApplicationContext();
        mLogin = new Login();
        mLoginResponse = new HashMap<>();

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

    public MyCookieStore getCookieStore() { return mCookieStore; }

    public Login getLogin() {
        return mLogin;
    }

    public void initCookies() {
        mCookieStore = new MyCookieStore();
        CookieManager manager = new CookieManager( mCookieStore, CookiePolicy.ACCEPT_ALL );
        CookieHandler.setDefault(manager);
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public Map<String, Object> getLoginResponse() {
        return mLoginResponse;
    }

    public void setLoginResponse(Map<String, Object> loginResponse) {
        this.mLoginResponse = loginResponse;
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }

    public void setDrawer(NavDrawer drawer) {
        this.mDrawer = drawer;
    }

    public NavDrawer getDrawer() {
        return mDrawer;
    }

    public apLog getLogManager() {
        return mLogManager;
    }

    public void setLogManager(apLog logManager) {
        this.mLogManager = logManager;
    }
}
