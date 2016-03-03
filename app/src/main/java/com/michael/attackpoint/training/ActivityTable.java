package com.michael.attackpoint.training;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.michael.attackpoint.Singleton;
import com.michael.database.DatabaseHelper;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michael on 10/28/15.
 */
public class ActivityTable {
    public static final String TABLE = "activity_type";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "user";
    public static final String COLUMN_VALUE = "name";

    public static final String DEBUG_TAG = "ap.ActivityTable";
    private DatabaseHelper dbHelper;
    private Singleton singleton;

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_VALUE + " INTEGER" +
            ");";

    public static final String[] COLUMNS = {
            COLUMN_NAME,
            COLUMN_VALUE
    };

    public ActivityTable() {
        singleton = Singleton.getInstance();
        dbHelper = DatabaseHelper.getInstance(singleton.getContext());
    }

    public void updateTable(Map<String, Integer> map) {
        flush();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, entry.getKey());
            values.put(COLUMN_VALUE, entry.getValue());

            db.insert(TABLE, "", values);
        }

    }

    public int getValue(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String format = "SELECT %s FROM %s WHERE %s LIKE %s";
        Formatter f = new Formatter();
        String sql = f.format(format, COLUMN_VALUE, TABLE, COLUMN_NAME, name).toString();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() <= 0) return -1;
        cursor.moveToFirst();
        int value = cursor.getInt(0);
        cursor.close();
        return value;
    }

    public Map<String, Integer> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String format = "SELECT %s,%s FROM %s";
        Formatter f = new Formatter();
        String sql = f.format(format, COLUMN_NAME, COLUMN_VALUE, TABLE).toString();

        //Cursor cursor = db.rawQuery(sql, null);

        Map<String, Integer> map = new HashMap<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                String key = cursor.getString(0);
                Integer value = cursor.getInt(1);

                map.put(key, value);
            }
        }

        return map;
    }

    public ArrayList<String> getAllNames() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String format = "SELECT %s FROM %s";
        Formatter f = new Formatter();
        String sql = f.format(format, COLUMN_NAME, TABLE).toString();

        //Cursor cursor = db.rawQuery(sql, null);

        ArrayList<String> list = new ArrayList<>();
        try (Cursor cursor = db.rawQuery(sql, null)) {
            while (cursor.moveToNext()) {
                String key = cursor.getString(0);
                list.add(key);
            }
        }

        return list;
    }

    public String getFirst() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String format = "SELECT %s FROM %s LIMIT 1";
        Formatter f = new Formatter();
        String sql = f.format(format, COLUMN_NAME, TABLE).toString();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() <= 0) return "";

        cursor.moveToFirst();
        String first = cursor.getString(0);
        cursor.close();
        return first;
    }

    public void flush() {
        String sql = "DELETE FROM " + TABLE + " WHERE 1";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql);
    }
}
