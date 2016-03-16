package com.michael.attackpoint.users;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.michael.attackpoint.util.DatabaseHelper;
import com.michael.attackpoint.util.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by michael on 10/28/15.
 */
public class UserTable {
    public static final String TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_APID = "ap_id";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_NAME = "name";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER + " TEXT NOT NULL, "
            + COLUMN_NAME + " TEXT,"
            + COLUMN_APID + " INTEGER, "
            + COLUMN_LOCATION + " TEXT, "
            + COLUMN_YEAR + " INTEGER);";

    public static final String[] COLUMNS = {
            COLUMN_USER,
            COLUMN_APID,
            COLUMN_NAME,
            COLUMN_LOCATION,
            COLUMN_YEAR
    };

    private DatabaseHelper dbHelper;

    public UserTable() {
        Singleton singleton = Singleton.getInstance();
        dbHelper = DatabaseHelper.getInstance(singleton.getContext());
    }

    public void addUser(User user) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int id = user.getId();
        ContentValues sql = new ContentValues();
        sql.put(COLUMN_USER, user.getUsername());
        sql.put(COLUMN_NAME, user.getName());
        sql.put(COLUMN_APID, id);
        sql.put(COLUMN_LOCATION, user.getLocation());
        sql.put(COLUMN_YEAR, user.getYear());

        if (userExists(user.getId()))
            updateUser(id, sql);
        else database.insert(TABLE, null, sql);
    }

    public User getUser(int id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String where = COLUMN_APID + " = " + id;
        Cursor cursor = database.query(TABLE, null, where, null, null, null, null, "1");
        cursor.moveToFirst();

        Map<String, Integer> columns = new HashMap();
        for (int i = 0; i < COLUMNS.length; i++) {
            columns.put(COLUMNS[i], cursor.getColumnIndex(COLUMNS[i]));
        }

        String username = cursor.getString(columns.get(COLUMN_USER));
        String name = cursor.getString(columns.get(COLUMN_NAME));
        String location = cursor.getString(columns.get(COLUMN_LOCATION));
        int year = cursor.getInt(columns.get(COLUMN_YEAR));

        User user = new User(username, id, name, location, year);
        return user;
    }

    public boolean userExists(int id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE
                + " WHERE " + COLUMN_APID
                + " = " + id;
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();
        return (count > 0);
    }

    public void updateUser(int id, ContentValues update) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String where = COLUMN_APID + " = " + id;
        database.update(TABLE, update, where, null);
    }

    public static String printAllUsers() {
        UserTable userTable = new UserTable();
        SQLiteDatabase database = userTable.dbHelper.getReadableDatabase();

        String[] columns = {COLUMN_USER, COLUMN_APID, COLUMN_YEAR};
        Cursor cursor = database.query(TABLE, columns, null, null, null, null, null);
        String output = "";

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            output += "username: " + cursor.getString(0);
            output += "\nid: " + cursor.getInt(1);
            output += "\nyear: " + cursor.getInt(2);
            output += "\n";
            cursor.moveToNext();
        }
        cursor.close();
        return output;
    }

    public static void clearUsers() {
        UserTable userTable = new UserTable();
        SQLiteDatabase database = userTable.dbHelper.getWritableDatabase();
        database.delete(TABLE, null, null);

    }
}
