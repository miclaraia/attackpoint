package com.michael.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by michael on 10/28/15.
 */
public class UserTable {
    public static final String TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_APID = "ap_id";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER
            + " TEXT NOT NULL, " + COLUMN_APID
            + " TEXT NOT NULL);";

    public static final String[] COLUMNS = {
            COLUMN_USER,
            COLUMN_APID,
    };

    public static void addUser(SQLiteDatabase database, String user, String id) {
        if (userExists(database, user)) {
            updateUser(database, user, id);

        }
        ContentValues sql = new ContentValues();
        sql.put(COLUMN_USER, user);
        sql.put(COLUMN_APID, id);
        database.insert(TABLE, null, sql);
    }

    public static boolean userExists(SQLiteDatabase database, String user) {
        String sql = "SELECT * FROM " + TABLE
                + " WHERE " + COLUMN_USER
                + " LIKE " + user;
        Cursor cursor = database.rawQuery(sql, null);
        int count = cursor.getCount();
        return (count > 0) ? true : false;
    }

    public static void updateUser(SQLiteDatabase database, String user, String id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER, user);
        values.put(COLUMN_APID, id);
        String where = COLUMN_USER + " LIKE " + user;
        database.update(TABLE, values, where, null);
    }
}
