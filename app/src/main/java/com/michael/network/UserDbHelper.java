package com.michael.network;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by michael on 10/15/15.
 */
public class UserDbHelper extends SQLiteOpenHelper {

    public static final String TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "user";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_EXPIRE = "expire";


    private static final String DATABASE_NAME = "userdata.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
            + " TEXT NOT NULL, " + COLUMN_TOKEN
            + " TEXT NOT NULL, " + COLUMN_EXPIRE
            + " TEXT NOT NULL);";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public static String[] insertColumns() {
        String[] columns = {
            COLUMN_NAME,
            COLUMN_TOKEN,
            COLUMN_EXPIRE
        };
        return columns;
    }

    public static String[] allColumns() {
        String[] columns = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_TOKEN,
                COLUMN_EXPIRE
        };
        return columns;
    }
}
