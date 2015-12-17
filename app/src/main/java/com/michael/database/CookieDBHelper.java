package com.michael.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by michael on 10/20/15.
 */
public class CookieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cookies.db";
    private static final int DATABASE_VERSION = 1;

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
        database.execSQL(CookieTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CookieDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + CookieTable.TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE);
        onCreate(db);
    }
}
