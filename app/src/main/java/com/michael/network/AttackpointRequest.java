package com.michael.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 8/18/15.
 */
public class AttackpointRequest extends StringRequest {
    public AttackpointRequest(int method, String url,
                              Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        Map headers = response.headers;

        Object cookieObj = headers.get("Set-Cookie");
        if (cookieObj != null) {
            String cookie = cookieObj.toString();
            Singleton.getInstance().getPreferences().saveCookie(cookie);
        }
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        String cookie = Singleton.getInstance().getPreferences().getCookie();
        if(!cookie.equals(""))
            headers.put("Cookie", cookie);
        return headers;
    }
}
