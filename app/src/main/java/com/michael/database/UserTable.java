package com.michael.database;

/**
 * Created by michael on 10/28/15.
 */
public class UserTable {
    public static final String TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_NAME = "ap_id";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER
            + " TEXT NOT NULL, " + COLUMN_NAME
            + " TEXT NOT NULL);";

    public static final String[] COLUMNS = {
            COLUMN_USER,
            COLUMN_NAME,
    };
}
