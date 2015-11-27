package com.michael.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.michael.objects.User;

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
            + COLUMN_NAME + " TEXT NOT NULL ,"
            + COLUMN_APID + " INTEGER, "
            + COLUMN_LOCATION + " TEXT NOT NULL, "
            + COLUMN_YEAR + "INTEGER);";

    public static final String[] COLUMNS = {
            COLUMN_USER,
            COLUMN_APID,
    };

    public static void addUser(SQLiteDatabase database, User user) {
        int id = user.getId();
        ContentValues sql = new ContentValues();
        sql.put(COLUMN_USER, user.getUsername());
        sql.put(COLUMN_NAME, user.getName());
        sql.put(COLUMN_APID, id);
        sql.put(COLUMN_LOCATION, user.getLocation());
        sql.put(COLUMN_YEAR, user.getYear());

        if (userExists(database, user.getId()))
            updateUser(database, id, sql);
        else database.insert(TABLE, null, sql);
    }

    public static boolean userExists(SQLiteDatabase database, int id) {
        String sql = "SELECT * FROM " + TABLE
                + " WHERE " + COLUMN_APID
                + " = " + id;
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        return (count > 0);
    }

    public static void updateUser(SQLiteDatabase database, int id, ContentValues update) {
        String where = COLUMN_APID + " = " + id;
        database.update(TABLE, update, where, null);
    }
}
