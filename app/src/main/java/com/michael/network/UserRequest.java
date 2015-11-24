package com.michael.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.objects.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Created by michael on 11/24/15.
 */
public class UserRequest extends Request<User> {
    private static final String DEBUG_TAG = "ap.userRequest";
    private static final String BASE_URL = "http://www.attackpoint.org/userprofile.jsp/user_";

    private final Response.Listener<User> mListener;
    private int mId;

    public UserRequest(int id, Response.Listener<User> listener,
                                Response.ErrorListener errorListener) {
        super(Method.GET, buildUrl(id), errorListener);
        mListener = listener;
        mId = id;
    }

    @Override
    protected Response<User> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            User user = getUser(Jsoup.parse(data));
            return Response.success(user, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(User user) {
        mListener.onResponse(user);
    }

    private static String buildUrl(int id) {
        return BASE_URL + id;
    }

    //++++++++++++++++++++   JSOUP   ++++++++++++++++++++

    private User getUser(Element html) {
        Element block = html.getElementById("contents");

        String username = block.getElementsByTag("h1").first().text().split(": ")[1];
        User user = new User(username, mId);

        Element element = block.getElementsByTag("p").first();
        String raw = element.html();
        String[] data = raw.split("<br>");
        Log.d(DEBUG_TAG, raw);
        for (int i = 0; i < data.length; i++) {
            Log.d(DEBUG_TAG, data[i]);
            String[] item = data[i].trim().split(": ");
            switch (item[0]) {
                case "Real name":
                    user.setName(item[1]);
                    break;
                case "Birth year":
                    try {
                        int year = Integer.parseInt(item[1]);
                        user.setYear(year);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case "E-mail":
                    user.setEmail(item[1]);
                    break;
                case "Location":
                    user.setLocation(item[1]);
            }
        }
        return user;
    }
}
