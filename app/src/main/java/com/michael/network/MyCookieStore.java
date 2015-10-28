package com.michael.network;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.michael.attackpoint.Preferences;
import com.michael.attackpoint.Singleton;
import com.michael.database.CookieDBHelper;
import com.michael.database.CookieTable;

import org.apache.http.cookie.Cookie;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 10/20/15.
 */
public class MyCookieStore implements CookieStore {
    private static final String DEBUG_TAG = "ap.MyCookieStore";
    private SQLiteDatabase database;
    private CookieDBHelper dbHelper;
    private Singleton singleton;
    private Preferences prefs;

    public MyCookieStore() {
        singleton = Singleton.getInstance();
        prefs = singleton.getPreferences();
        dbHelper = new CookieDBHelper(singleton.getContext());
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        String user = prefs.getUser();
        if (user.equals("")) return;

        String name = cookie.getName();
        String value = cookie.getValue();

        ContentValues sql = new ContentValues();
        sql.put(CookieTable.COLUMN_USER, user);
        sql.put(CookieTable.COLUMN_NAME, name);
        sql.put(CookieTable.COLUMN_COOKIE, value);

        open();
        long row_id = database.insert(CookieTable.TABLE, null, sql);
        close();
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return getCookies();
    }

    public List<HttpCookie> getCookies(String currentUser) {
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        String select = CookieTable.COLUMN_USER + " LIKE ?";
        String[] selectionArgs = {currentUser};

        open();
        Cursor cursor = database.query(CookieTable.TABLE,
                CookieTable.COLUMNS, select, selectionArgs, null, null, null);

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
        close();
        return cookies;
    }

    @Override
    public List<HttpCookie> getCookies() {
        String currentUser = prefs.getUser();
        return getCookies(currentUser);
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        String where = CookieTable.COLUMN_NAME + " LIKE ?"
                + CookieTable.COLUMN_COOKIE + " LIKE ?";
        String[] whereArgs = {cookie.getName(), cookie.getValue()};
        open();
        int result = database.delete(CookieTable.TABLE, where, whereArgs);
        close();
        if (result > 0) return true;
        else return false;
    }

    @Override
    public boolean removeAll() {
        open();
        int result = database.delete(CookieTable.TABLE, "1", null);
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

    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM " + CookieTable.TABLE
                + "GROUP BY " + CookieTable.COLUMN_USER;
        open();
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        close();
        Log.d(DEBUG_TAG, "found " + count + "users in db");
        return count;
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        String sql = "SELECT " + CookieTable.COLUMN_USER
                + " FROM " + CookieTable.TABLE
                + " GROUP BY " + CookieTable.COLUMN_USER;

        open();
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String user = cursor.getString(0);
            users.add(user);
            cursor.moveToNext();
        }

        cursor.close();
        close();
        return users;
    }

    public void removeUser(String user) {
        String where = "user LIKE \"" + user + "\"";
        String[] whereArgs = {CookieTable.COLUMN_USER, user};

        open();
        database.delete(CookieTable.TABLE, where, null);
        close();
    }

    public String getAllCookies() {
        Log.d(DEBUG_TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", CookieTable.TABLE);
        open();
        Cursor cursor  = database.rawQuery("SELECT * FROM " + CookieTable.TABLE, null);
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (cursor.moveToNext());
        }

        return tableString;
    }

    public boolean checkValid(String user) {
        Log.d(DEBUG_TAG, "checking if login was valid");
        List<HttpCookie> cookies = getCookies(user);

        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals("login")) return true;
        }
        return false;
    }
}
