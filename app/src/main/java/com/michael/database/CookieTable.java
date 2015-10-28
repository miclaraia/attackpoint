package com.michael.database;

/**
 * Created by michael on 10/28/15.
 */
public class CookieTable {
    public static final String TABLE = "cookies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COOKIE = "cookie";

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
}
