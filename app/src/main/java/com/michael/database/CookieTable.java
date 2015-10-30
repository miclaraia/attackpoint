package com.michael.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.michael.attackpoint.Singleton;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 10/28/15.
 */
public class CookieTable {
    public static final String TABLE = "cookies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COOKIE = "cookie";

    public static final String DEBUG_TAG = "ap.CookieTable";
    private SQLiteDatabase database;
    private CookieDBHelper dbHelper;
    private Singleton singleton;

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER
            + " TEXT NOT NULL, " + COLUMN_NAME
            + " TEXT NOT NULL, " + COLUMN_COOKIE
            + " TEXT NOT NULL);";

    public static final String[] COLUMNS = {
            COLUMN_USER,
            COLUMN_NAME,
            COLUMN_COOKIE
    };

    public CookieTable() {
        singleton = Singleton.getInstance();
        dbHelper = new CookieDBHelper(singleton.getContext());
    }

    public static String getCurrentID() {
        CookieTable ct = new CookieTable();
        String user = ct.singleton.getPreferences().getUser();
        if (user.equals("")) return null;

        return ct.getID(user);
    }

    public String getID(String user) {
        String sql = "SELECT " + COLUMN_COOKIE
                + " FROM " + TABLE
                + " WHERE " + COLUMN_USER
                + " LIKE \"" + user + "\""
                + " AND " + COLUMN_NAME
                + " LIKE \"login\"";

        open();
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        String cookie = cursor.getString(0);
        String id = cookie.split(":")[0];

        close();
        return id;
    }

    public void addCookie(String user, String cookieName, String cookieValue) {
        ContentValues sql = new ContentValues();
        sql.put(CookieTable.COLUMN_USER, user);
        sql.put(CookieTable.COLUMN_NAME, cookieName);
        sql.put(CookieTable.COLUMN_COOKIE, cookieValue);

        open();
        database.insert(CookieTable.TABLE, null, sql);
        close();
    }

    public List<HttpCookie> getCookies(String currentUser) {
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        String select = COLUMN_USER + " LIKE ?";
        String[] selectionArgs = {currentUser};

        open();
        Cursor cursor = database.query(TABLE,
                COLUMNS, select, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
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

    public boolean removeCookie(String cookieName, String cookieValue) {
        String where = COLUMN_NAME + " LIKE ?"
                + COLUMN_COOKIE + " LIKE ?";
        String[] whereArgs = {cookieName, cookieValue};

        open();
        int result = database.delete(TABLE, where, whereArgs);
        close();
        if (result > 0) return true;
        else return false;
    }

    public boolean removeAll() {
        open();
        int result = database.delete(CookieTable.TABLE, "1", null);
        close();
        if (result > 0) return true;
        else return false;
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

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
