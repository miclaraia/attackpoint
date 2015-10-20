package com.michael.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 10/20/15.
 */
public class MyCookieStore implements CookieStore {

    private SQLiteDatabase database;
    private UserDbHelper dbHelper;
    private Singleton singleton;
    private Preferences prefs;

    public MyCookieStore() {
        singleton = Singleton.getInstance();
        prefs = singleton.getPreferences();
        dbHelper = new UserDbHelper(singleton.getContext());
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        String name = cookie.getName();
        String value = cookie.getValue();
        String user = prefs.getUser();

        ContentValues sql = new ContentValues();
        sql.put(CookieDBHelper.COLUMN_USER, user);
        sql.put(CookieDBHelper.COLUMN_NAME, name);
        sql.put(CookieDBHelper.COLUMN_COOKIE, value);

        open();
        long row_id = database.insert(UserDbHelper.TABLE, null, sql);
        close();
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getCookies();
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        String select = CookieDBHelper.COLUMN_USER + "LIKE ?";
        String[] selectionArgs = {prefs.getUser()};

        Cursor cursor = database.query(UserDbHelper.TABLE,
                CookieDBHelper.COLUMNS, select, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String user = cursor.getString(0);
            String name = cursor.getString(1);
            String value = cursor.getString(2);
            HttpCookie cookie = new HttpCookie(name, value);
            cookies.add(cookie);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return cookies;
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        String where = CookieDBHelper.COLUMN_NAME + "LIKE ?"
                + CookieDBHelper.COLUMN_COOKIE + "LIKE ?";
        String[] whereArgs = {cookie.getName(), cookie.getValue()};
        open();
        int result = database.delete(CookieDBHelper.TABLE, where, whereArgs);
        close();
        if (result > 0) return true;
        else return false;
    }

    @Override
    public boolean removeAll() {
        open();
        int result = database.delete(CookieDBHelper.TABLE, "1", null);
        close();
        if (result > 0) return true;
        else return false;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
