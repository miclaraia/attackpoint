package com.michael.network;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.michael.attackpoint.Singleton;
import com.michael.objects.User;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 11/11/15.
 */
public class FavoriteUsersRequest extends Request<List<User>> {
    private static final String DEBUG_TAG = "ap.fuserrequest";
    private static final String BASE_URL = "http://www.attackpoint.org/training.jsp";

    private ArrayList<User> userList;
    private Singleton singleton;

    private final Response.Listener<List<User>> mListener;

    public FavoriteUsersRequest(Response.Listener<List<User>> listener,
                                Response.ErrorListener errorListener) {
        super(Method.GET, BASE_URL, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<List<User>> parseNetworkResponse(NetworkResponse response) {
        List<User> users;
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            users = getUsers(Jsoup.parse(data));
        } catch (Exception e) {
            e.printStackTrace();
            users = new ArrayList();
        }
        return Response.success(users, HttpHeaderParser.parseCacheHeaders(response));

    }

    @Override
    protected void deliverResponse(List<User> response) {
        mListener.onResponse(response);
    }

    private static String buildURL(String id) {
        String[] pieces = {BASE_URL, id};
        return TextUtils.join("/", pieces);
    }

    //++++++++++++++++++++   JSOUP   ++++++++++++++++++++

    private User getUser(Element element) {
        Element info = element.getElementsByTag("a").first();
        String username = info.text();
        String id = info.attr("href");
        id = id.split("/")[2];
        return new User(username, id);
    }

    private List<User> getUsers(Element element) {
        List<User> users = new ArrayList<User>();

        Elements favorites = element.getElementsByClass("favList").first().getElementsByTag("tr");
        for (Element favorite : favorites) {
            users.add(getUser(favorite));
        }

        return users;
    }
}