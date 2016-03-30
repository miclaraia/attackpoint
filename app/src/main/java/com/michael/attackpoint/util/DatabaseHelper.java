package com.michael.attackpoint.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.michael.attackpoint.log.data.LogDatabase;
import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.account.CookieTable;
import com.michael.attackpoint.users.UserTable;

/**
 * Created by michael on 10/20/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ap.database";
    // TODO updating database doesn't reset login status
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper mInstance = null;
    private Context mContext;

    public static DatabaseHelper getInstance(Context context) {

        /**
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CookieTable.TABLE_CREATE);
        database.execSQL(UserTable.TABLE_CREATE);
        database.execSQL(ActivityTable.TABLE_CREATE);
        database.execSQL(LogDatabase.CREATE_CACHE);
        database.execSQL(LogDatabase.CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + CookieTable.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ActivityTable.TABLE);
        db.execSQL(LogDatabase.DROP_CACHE);
        db.execSQL(LogDatabase.DROP_USER);

        onCreate(db);
    }
}
