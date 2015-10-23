package com.michael.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by michael on 10/20/15.
 */
public class CookieDBHelper extends SQLiteOpenHelper {

    public static final String TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_NAME = "name2";
    public static final String COLUMN_COOKIE = "cookie";


    private static final String DATABASE_NAME = "userdata.db";
    private static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
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

    public CookieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static String selectWhere(String column, String user) {
        String s = column
                + "LIKE \"" + user + "\"";
        return s;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CookieDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
