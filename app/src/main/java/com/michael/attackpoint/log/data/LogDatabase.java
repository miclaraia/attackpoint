package com.michael.attackpoint.log.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import com.michael.attackpoint.util.DatabaseHelper;
import com.michael.attackpoint.util.Singleton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by michael on 3/16/16.
 */
public class LogDatabase implements LogDatabaseContract.Table{
    static {
        String drop = String.format("%s; %s;", LogCache.TABLE_DROP, LogCacheUpdate.TABLE_DROP);
        String create = String.format("%s; %s;", LogCache.TABLE_CREATE, LogCacheUpdate.TABLE_CREATE);

        DROP = drop;
        CREATE = create;
    }

    public static final String DROP;
    public static final String CREATE;

    private DatabaseHelper mDBHelper;
    private LogCache mLogCache;
    private LogCacheUpdate mLogCacheUpdate;
    private String mUser;


    public LogDatabase(String user) {
        mDBHelper = DatabaseHelper.getInstance(Singleton.getInstance().getContext());
        mLogCache = new LogCache(mDBHelper);
        mLogCacheUpdate = new LogCacheUpdate(mDBHelper);
        mUser = user;
    }

    @Override
    public ArrayMap<Integer, String> getCachedLog(String user) {
        return mLogCache.getCachedLog(user);
    }

    @Override
    public String getCachedEntry(int id) {
        return mLogCache.getCachedEntry(id);
    }

    @Override
    public void putCache(String user, List<String> cache) {
        mLogCache.putCache(user, cache);
    }

    @Override
    public void addCacheEntry(String user, String entry) {
        mLogCache.addCacheEntry(user, entry);
    }

    @Override
    public boolean isStale(String user) {
        boolean stale = mLogCacheUpdate.userIsStale(user);
        if (stale) {
            mLogCache.removeCache(user);
        }

        return stale;
    }

    private static class LogCache {
        public static final String TABLE = "logcache";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_JSON = "json";

        public static final String DEBUG_TAG = "LogTable";

        static {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("CRETAE TABLE %s (", TABLE));
            builder.append(String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT,", COLUMN_ID));
            builder.append(String.format("%s TEXT NOT NULL", COLUMN_USER));
            builder.append(String.format("%s TEXT NOT NULL", COLUMN_JSON));
            TABLE_CREATE = builder.toString();

            String drop = String.format("DROP TABLE IF EXISTS %s", TABLE);
            TABLE_DROP = drop;
        }
        public static final String TABLE_CREATE;
        public static final String TABLE_DROP;

        private DatabaseHelper mDBHelper;

        private LogCache(DatabaseHelper dbHelper) {
            mDBHelper = dbHelper;
        }

        public ArrayMap<Integer, String> getCachedLog(String user) {
            String sql = String.format("SELECT %s,%s FROM %s WHERE %s LIKE %s",
                    COLUMN_ID, COLUMN_JSON, TABLE, COLUMN_USER, user);
            Cursor cursor = open().rawQuery(sql, null);

            ArrayMap<Integer, String> cache = new ArrayMap<>();
            while(cursor.moveToNext()) {
                Integer id = cursor.getInt(0);
                String data = cursor.getString(1);
                cache.put(id, data);
            }

            return cache;
        }

        public String getCachedEntry(int id) {
            String sql = String.format("SELECT %s FROM %s WHERE %s=%d",
                    COLUMN_JSON, TABLE, COLUMN_ID, id);
            Cursor cursor = open().rawQuery(sql, null);

            cursor.moveToFirst();
            String entry = cursor.getString(0);

            return entry;
        }

        public void putCache(String user, List<String> cache) {
            SQLiteDatabase db = writer();
            for (String json : cache) {
                ContentValues params = new ContentValues();
                params.put(COLUMN_USER, user);
                params.put(COLUMN_JSON, json);

                db.insert(TABLE, null, params);
            }
        }

        public void removeCache(String user) {
            String sql = String.format("DELETE FROM %s WHERE %s=%s",
                    TABLE, COLUMN_USER, user);

            writer().execSQL(sql);
        }

        public void addCacheEntry(String user, String entry) {
            ContentValues params = new ContentValues();
            params.put(COLUMN_USER, user);
            params.put(COLUMN_JSON, entry);

            writer().insert(TABLE, null, params);
        }

        private SQLiteDatabase open() {
            return mDBHelper.getReadableDatabase();
        }

        private SQLiteDatabase writer() {
            return mDBHelper.getWritableDatabase();
        }
    }

    private static class LogCacheUpdate {
        public static final String TABLE = "logcacheupdate";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        static {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("CRETAE TABLE %s (", TABLE));
            builder.append(String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT,", COLUMN_ID));
            builder.append(String.format("%s TEXT NOT NULL", COLUMN_USER));
            builder.append(String.format("%s TIMESTAMP DEFAULT CURRENT_TIMESTAMP", COLUMN_TIMESTAMP));
            TABLE_CREATE = builder.toString();

            String drop = String.format("DROP TABLE IF EXISTS %s", TABLE);
            TABLE_DROP = drop;
        }
        public static final String TABLE_CREATE;
        public static final String TABLE_DROP;

        private DatabaseHelper mDBHelper;

        private LogCacheUpdate(DatabaseHelper dbHelper) {
            mDBHelper = dbHelper;
        }

        public void updateUser(String user) {
            SQLiteDatabase db = writer();
            ContentValues params = new ContentValues();
            params.put(COLUMN_USER, user);

            db.insert(TABLE, null, params);
        }

        public boolean userIsStale(String user) {
            String sql = String.format("SELECT %s FROM %s WHERE %s=%s",
                    COLUMN_TIMESTAMP, TABLE, COLUMN_USER, user);
            Cursor cursor = open().rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                Calendar timestamp = Calendar.getInstance();
                timestamp.setTimeInMillis(Timestamp.valueOf(cursor.getString(0)).getTime());
                int minutes = timestamp.get(Calendar.MINUTE) + 10;
                timestamp.set(Calendar.MINUTE, minutes);

                Calendar compare = Calendar.getInstance();

                if (timestamp.compareTo(compare) < 0) {
                    // timestamp is less than 10 minutes old, not stale yet
                    return false;
                } else {
                    // timestamp is older than 10 minutes, stale
                    // also stipulates that user exists in cache
                    removeUser(user);
                    return true;
                }
            }

            return true;
        }

        private void removeUser(String user) {
            String sql = String.format("DELETE FROM %s WHERE %s=%s",
                    TABLE, COLUMN_USER, user);
            writer().execSQL(sql);
        }

        public SQLiteDatabase open() {
            return mDBHelper.getReadableDatabase();
        }

        public SQLiteDatabase writer() {
            return mDBHelper.getWritableDatabase();
        }


    }
}