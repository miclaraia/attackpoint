package com.michael.network;

import android.util.Log;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;
import com.michael.database.CookieTable;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * Created by michael on 10/20/15.
 */
public class MyCookieStore implements CookieStore {
    private static final String DEBUG_TAG = "ap.MyCookieStore";
    private Singleton singleton;
    private Preferences prefs;
    CookieTable cookieTable;

    public MyCookieStore() {
        singleton = Singleton.getInstance();
        prefs = singleton.getPreferences();
        cookieTable = new CookieTable();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        String user = prefs.getUser();
        if (user.equals("")) return;

        String name = cookie.getName();
        String value = cookie.getValue();

        cookieTable.addCookie(user, name, value);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getCookies();
    }

    @Override
    public List<HttpCookie> getCookies() {
        String currentUser = prefs.getUser();
        return cookieTable.getCookies(currentUser);
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return cookieTable.removeCookie(cookie.getName(), cookie.getValue());
    }

    @Override
    public boolean removeAll() {
        return cookieTable.removeAll();
    }

    public void removeUser(String user) {
        cookieTable.removeUser(user);
    }

    public String getAllCookies() {
        return cookieTable.getAllCookies();
    }

    public boolean checkValid(String user) {
        Log.d(DEBUG_TAG, "checking if login was valid");
        List<HttpCookie> cookies = cookieTable.getCookies(user);

        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals("login")) return true;
        }
        return false;
    }

    public List<String> getAllUsers() {
        return cookieTable.getAllUsers();
    }
}
