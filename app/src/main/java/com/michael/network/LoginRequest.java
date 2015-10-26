package com.michael.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 10/26/15.
 */
public class LoginRequest extends StringRequest {
    private static final String DEBUG_TAG = "attackpoint.Login";
    private static final String URL = "http://www.attackpoint.org/dologin.jsp";
    private static final String RETURL = "http://www.attackpoint.org";
    private Map<String, String> mParams;
    private Response.Listener mListener;
    private String user;
    private String oldUser;
    private Singleton singleton;

    public LoginRequest(String user, String pass,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
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
        Map headers = response.headers;
        Log.d(DEBUG_TAG, headers.toString());

        Object obj = headers.get("Location");
        if (obj != null) {
            String location = obj.toString();
            String username = mParams.get("username");
            if (location.equals(RETURL)) {
                Log.d(DEBUG_TAG, "login successfull");
                singleton.getDrawer().addUser(username);
            } else {
                Log.d(DEBUG_TAG, "login unsuccessful");
                singleton.getPreferences().setUser(oldUser);
                singleton.getCookieStore().removeUser(username);
            }
        }
        return super.parseNetworkResponse(response);
    }
}

/**
 * StringRequest postRequest = new StringRequest(Request.Method.POST, url,
 new Response.Listener<String>() {
@Override
public void onResponse(String response) {
try {
JSONObject jsonResponse = new JSONObject(response).getJSONObject("form");
String site = jsonResponse.getString("site"),
network = jsonResponse.getString("network");
System.out.println("Site: "+site+"\nNetwork: "+network);
} catch (JSONException e) {
e.printStackTrace();
}
}
},
 new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
error.printStackTrace();
}
}
 ) {
@Override
protected Map<String, String> getParams()
{
Map<String, String>  params = new HashMap<>();
// the POST parameters:
params.put("site", "code");
params.put("network", "tutsplus");
return params;
}
};
 */
