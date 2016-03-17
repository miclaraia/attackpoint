package com.michael.attackpoint.log.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import com.michael.attackpoint.util.DatabaseHelper;
import com.michael.attackpoint.util.Singleton;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by michael on 3/16/16.
 */
public class LogDatabase {
    static {
        String drop = String.format("%s; %s;", LogCache.TABLE_DROP, LogCacheUpdate.TABLE_DROP);
        String create = String.format("%s; %s;", LogCache.TABLE_CREATE, LogCacheUpdate.TABLE_CREATE);

        DROP = drop;
        CREATE = create;
    }

    public static final String DROP;
    public static final String CREATE;

    private DatabaseHelper mDBHelper;
    protected LogCache mLogCache;
    protected LogCacheUpdate mLogCacheUpdate;


    public LogDatabase() {
        mDBHelper = DatabaseHelper.getInstance(Singleton.getInstance().getContext());
        mLogCache = new LogCache(mDBHelper);
        mLogCacheUpdate = new LogCacheUpdate(mDBHelper);
    }

    public LogDatabase(Context context) {
        mDBHelper = DatabaseHelper.getInstance(context);
        mLogCache = new LogCache(mDBHelper);
        mLogCacheUpdate = new LogCacheUpdate(mDBHelper);
    }

    protected static class LogCache {
        public static final String TABLE = "logcache";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_AP_ID = "ap_id";
        public static final String COLUMN_USER = "userID";
        public static final String COLUMN_JSON = "json";

        public static final String DEBUG_TAG = "LogTable";

        static {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("CRETAE TABLE %s (", TABLE));
            builder.append(String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT,", COLUMN_ID));
            builder.append(String.format("%s INTEGER", COLUMN_AP_ID));
            builder.append(String.format("%s INTEGER", COLUMN_USER));
            builder.append(String.format("%s TEXT NOT NULL", COLUMN_JSON));
            TABLE_CREATE = builder.toString();

        }
        public static final String TABLE_CREATE;
        public static final String TABLE_DROP = String.format("DROP TABLE IF EXISTS %s", TABLE);

        private DatabaseHelper mDBHelper;

        private LogCache(DatabaseHelper dbHelper) {
            mDBHelper = dbHelper;
        }

        public ArrayMap<Integer, String> getCachedLog(int userID) {
            String sql = String.format(Locale.US, "SELECT %s,%s FROM %s WHERE %s=%d",
                    COLUMN_ID, COLUMN_JSON, TABLE, COLUMN_USER, userID);
            Cursor cursor = open().rawQuery(sql, null);

            ArrayMap<Integer, String> cache = new ArrayMap<>();
            while(cursor.moveToNext()) {
                Integer id = cursor.getInt(0);
                String data = cursor.getString(1);
                cache.put(id, data);
            }

            cursor.close();
            return cache;
        }

        public String getCachedEntry(int userID, int ap_id) {
            String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d AND %s=%d",
                    COLUMN_JSON, TABLE, COLUMN_USER, userID, COLUMN_AP_ID, ap_id);
            Cursor cursor = open().rawQuery(sql, null);

            cursor.moveToFirst();
            String entry = cursor.getString(0);
            cursor.close();

            return entry;
        }

        public void putCache(int userID, ArrayMap<Integer, String> cache) {
            SQLiteDatabase db = writer();
            for (Map.Entry<Integer, String> entry: cache.entrySet()) {
                ContentValues params = new ContentValues();
                params.put(COLUMN_USER, userID);
                params.put(COLUMN_AP_ID, entry.getKey());
                params.put(COLUMN_JSON, entry.getValue());

                db.insert(TABLE, null, params);
            }
        }

        public void removeCache(int userID) {
            String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                    TABLE, COLUMN_USER, userID);

            writer().execSQL(sql);
        }

        public void addCacheEntry(int userID, int ap_id, String entry) {
            ContentValues params = new ContentValues();
            params.put(COLUMN_USER, userID);
            params.put(COLUMN_AP_ID, ap_id);
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

    protected static class LogCacheUpdate {
        public static final String TABLE = "logcacheupdate";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final int STALE_DURATION = 10;

        static {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("CRETAE TABLE %s (", TABLE));
            builder.append(String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT,", COLUMN_ID));
            builder.append(String.format("%s INTEGER", COLUMN_USER));
            builder.append(String.format("%s TIMESTAMP DEFAULT CURRENT_TIMESTAMP", COLUMN_TIMESTAMP));
            TABLE_CREATE = builder.toString();
        }
        public static final String TABLE_CREATE;
        public static final String TABLE_DROP = String.format("DROP TABLE IF EXISTS %s", TABLE);

        private DatabaseHelper mDBHelper;

        private LogCacheUpdate(DatabaseHelper dbHelper) {
            mDBHelper = dbHelper;
        }

        public void updateUser(int userID) {
            SQLiteDatabase db = writer();
            ContentValues params = new ContentValues();
            params.put(COLUMN_USER, userID);

            db.insert(TABLE, null, params);
        }

        public Calendar getTimestamp(int userID) {
            String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                    COLUMN_TIMESTAMP, TABLE, COLUMN_USER, userID);
            Cursor cursor = open().rawQuery(sql, null);
            Calendar timestamp = Calendar.getInstance();
            timestamp.set(0,0,0,0,0,0);

            if (cursor.moveToFirst()) {
                String time = cursor.getString(0);
                timestamp.setTimeInMillis(Timestamp.valueOf(time).getTime());
            }
            cursor.close();
            return timestamp;
        }

        public boolean timestampIsStale(Calendar timestamp) {
            int minutes = timestamp.get(Calendar.MINUTE) + STALE_DURATION;
            timestamp.set(Calendar.MINUTE, minutes);

            Calendar compare = Calendar.getInstance();

            if (timestamp.compareTo(compare) < 0) {
                // timestamp is less than 10 minutes old, not stale yet
                return false;
            }
            // either timestamp is older than 10 minutes (stale)
            // or user does not exist yet
            return true;
        }

        public boolean userIsStale(int userID) {
            Calendar timestamp = getTimestamp(userID);
            return timestampIsStale(timestamp);
        }

        public void removeUser(int userID) {
            String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                    TABLE, COLUMN_USER, userID);
            writer().execSQL(sql);
        }

        private SQLiteDatabase open() {
            return mDBHelper.getReadableDatabase();
        }

        private SQLiteDatabase writer() {
            return mDBHelper.getWritableDatabase();
        }


    }
}