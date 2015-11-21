package com.michael.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 10/26/15.
 */
public class LoginRequest extends Request<String> {
    private static final String DEBUG_TAG = "attackpoint.Login";
    private static final String URL = "http://www.attackpoint.org/dologin.jsp";
    private static final String RETURL = "http://www.attackpoint.org";

    private Map<String, String> mParams;
    private Response.Listener mListener;
    private String oldUser;
    private Singleton singleton;

    public LoginRequest(String user, String pass,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, errorListener);
        mListener = listener;
        mParams = new HashMap<String, String>();
        mParams.put("username", user);
        mParams.put("password", pass);
        mParams.put("returl", RETURL);

        singleton = Singleton.getInstance();
        Preferences prefs = singleton.getPreferences();
        oldUser = prefs.getUser();
        prefs.setUser(user);

    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String username = mParams.get("username");

        if (singleton.getCookieStore().checkValid(username)) {
            Log.d(DEBUG_TAG, "login successfull");
            return Response.success(username, HttpHeaderParser.parseCacheHeaders(response));
        } else {
            Log.d(DEBUG_TAG, "login unsuccessful");

            singleton.getPreferences().setUser(oldUser);
            singleton.getCookieStore().removeUser(username);
            return Response.error(new AuthFailureError("Login unsuccessfull"));
        }
    }

    @Override
    protected void deliverResponse(String s) {
        mListener.onResponse(s);
    }
}
