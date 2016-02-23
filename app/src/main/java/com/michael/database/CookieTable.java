package com.michael.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.michael.attackpoint.Singleton;

import java.net.HttpCookie;
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
    private DatabaseHelper dbHelper;
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
        dbHelper = DatabaseHelper.getInstance(singleton.getContext());
    }

    public static int getCurrentID() {
        String user = Singleton.getInstance().getLogin().getUser();
        CookieTable ct = new CookieTable();
        if (user.equals("")) return -1;

        return ct.getID(user);
    }

    public int getID(String user) {
        String sql = "SELECT " + COLUMN_COOKIE
                + " FROM " + TABLE
                + " WHERE " + COLUMN_USER
                + " LIKE \"" + user + "\""
                + " AND " + COLUMN_NAME
                + " LIKE \"login\"";

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        String cookie = cursor.getString(0);
        String id = cookie.split(":")[0];
        cursor.close();

        return Integer.parseInt(id);
    }

    public void addCookie(String user, String cookieName, String cookieValue) {
        ContentValues sql = new ContentValues();
        sql.put(CookieTable.COLUMN_USER, user);
        sql.put(CookieTable.COLUMN_NAME, cookieName);
        sql.put(CookieTable.COLUMN_COOKIE, cookieValue);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert(CookieTable.TABLE, null, sql);
    }

    public List<HttpCookie> getCookies(String currentUser) {
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();
        String select = COLUMN_USER + " LIKE ?";
        String[] selectionArgs = {currentUser};

        SQLiteDatabase database = dbHelper.getWritableDatabase();
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
        return cookies;
    }

    public boolean removeCookie(String cookieName, String cookieValue) {
        String where = COLUMN_NAME + " LIKE ?"
                + COLUMN_COOKIE + " LIKE ?";
        String[] whereArgs = {cookieName, cookieValue};

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int result = database.delete(TABLE, where, whereArgs);

        if (result > 0) return true;
        else return false;
    }

    public boolean removeAll() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int result = database.delete(CookieTable.TABLE, "1", null);
        if (result > 0) return true;
        else return false;
    }

    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM " + CookieTable.TABLE
                + "GROUP BY " + CookieTable.COLUMN_USER;

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        Log.d(DEBUG_TAG, "found " + count + "users in db");
        return count;
    }

    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();

        String sql = "SELECT " + CookieTable.COLUMN_USER
                + " FROM " + CookieTable.TABLE
                + " GROUP BY " + CookieTable.COLUMN_USER;

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String user = cursor.getString(0);
            users.add(user);
            cursor.moveToNext();
        }

        cursor.close();
        return users;
    }

    public void removeUser(String user) {
        String where = "user LIKE \"" + user + "\"";

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(CookieTable.TABLE, where, null);
    }

    public String getAllCookies() {
        Log.d(DEBUG_TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", CookieTable.TABLE);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
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

        cursor.close();
        return tableString;
    }
}
