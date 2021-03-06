package com.michael.attackpoint.log.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.VisibleForTesting;
import android.test.AndroidTestCase;
import android.util.ArrayMap;

import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.AndroidFactory;
import com.michael.attackpoint.util.DatabaseHelper;
import com.michael.attackpoint.util.Singleton;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by michael on 3/16/16.
 */
public class LogDatabase implements LogCacheApi.Database{
    public static final String DROP_CACHE = LogCache.TABLE_DROP;
    public static final String DROP_USER = LogCacheUpdate.TABLE_DROP;
    public static final String CREATE_CACHE = LogCache.TABLE_CREATE;
    public static final String CREATE_USER = LogCacheUpdate.TABLE_CREATE;

    private DatabaseHelper mDBHelper;
    private LogCache mLogCache;
    private LogCacheUpdate mLogCacheUpdate;


    public LogDatabase() {
        mLogCache = new LogCache();
        mLogCacheUpdate = new LogCacheUpdate();
    }

    @Override
    public List<LogInfo> getCache(int userID) {
        return mLogCache.getCachedLog(userID);
    }

    @Override
    public LogInfo getCacheEntry(int userID, int id) {
        return mLogCache.getCachedEntry(userID, id);
    }

    @Override
    public void addCache(int userID, List<LogInfo> cache) {
        mLogCache.addCache(userID, cache);
        mLogCacheUpdate.updateUser(userID);
    }

    @Override
    public void addCacheEntry(int userID, LogInfo entry) {
        mLogCache.addCacheEntry(userID, entry);
    }

    @Override
    public void removeCache(int userID) {
        mLogCache.removeCache(userID);
        mLogCacheUpdate.removeUser(userID);
    }

    @Override
    public boolean userInCache(int userID) {
        return mLogCache.userInCache(userID);
    }

    @Override
    public boolean userIsStale(int userID) {
        return mLogCacheUpdate.userIsStale(userID);
    }

    public void emptyCache() {
        mLogCache.empty();
        mLogCacheUpdate.empty();
    }

    public static class LogCache {
        public static final String TABLE = "logcache";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_AP_ID = "ap_id";
        public static final String COLUMN_USER = "userID";
        public static final String COLUMN_JSON = "json";

        public static final String DEBUG_TAG = "LogTable";

        static {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("CREATE TABLE %s (", TABLE));
            builder.append(String.format(" %s INTEGER PRIMARY KEY AUTOINCREMENT,", COLUMN_ID));
            builder.append(String.format(" %s INTEGER,", COLUMN_AP_ID));
            builder.append(String.format(" %s INTEGER,", COLUMN_USER));
            builder.append(String.format(" %s TEXT NOT NULL);", COLUMN_JSON));
            TABLE_CREATE = builder.toString();

        }
        public static final String TABLE_CREATE;
        public static final String TABLE_DROP = String.format("DROP TABLE IF EXISTS %s", TABLE);

        private DatabaseHelper mDBHelper;
        private AndroidFactory mAndroidFactory;

        public LogCache() {
            mAndroidFactory = AndroidFactory.getInstance();
            mDBHelper = mAndroidFactory.genDatabaseHelper();
        }

        public List<LogInfo> getCachedLog(int userID) {
            String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                    COLUMN_JSON, TABLE, COLUMN_USER, userID);
            Cursor cursor = open().rawQuery(sql, null);

            List<LogInfo> cache = new ArrayList<>();
            while(cursor.moveToNext()) {
                String data = cursor.getString(0);
                LogInfo entry = LogInfo.getFromJSON(data);
                cache.add(entry);
            }

            cursor.close();
            return cache;
        }

        public LogInfo getCachedEntry(int userID, int ap_id) {
            String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d AND %s=%d",
                    COLUMN_JSON, TABLE, COLUMN_USER, userID, COLUMN_AP_ID, ap_id);
            Cursor cursor = open().rawQuery(sql, null);

            LogInfo entry;
            if (cursor.moveToFirst()) {
                entry = LogInfo.getFromJSON(cursor.getString(0));
                cursor.close();
                // TODO bad error handling
            } else entry = new LogInfo();

            return entry;
        }

        public void addCache(int userID, List<LogInfo> cache) {
            SQLiteDatabase db = writer();
            for (LogInfo entry: cache) {
                ContentValues params = mAndroidFactory.genContentValues();
                params.put(COLUMN_USER, userID);
                params.put(COLUMN_AP_ID, entry.getID());
                params.put(COLUMN_JSON, entry.toJSON().toString());

                db.insert(TABLE, null, params);
            }
        }

        public void addCacheEntry(int userID, LogInfo entry) {
            ContentValues params = mAndroidFactory.genContentValues();
            params.put(COLUMN_USER, userID);
            params.put(COLUMN_AP_ID, entry.getID());
            params.put(COLUMN_JSON, entry.toJSON().toString());

            writer().insert(TABLE, null, params);
        }

        public void removeCache(int userID) {
            String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                    TABLE, COLUMN_USER, userID);

            writer().execSQL(sql);
        }

        public boolean userInCache(int userId) {
            String sql = String.format(Locale.US, "SELECT COUNT(*) FROM %s WHERE %s=%d",
                    TABLE, COLUMN_USER, userId);

            boolean exists = false;

            Cursor cursor = open().rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(0) > 0) exists = true;
            }

            cursor.close();
            return exists;
        }

        public List<String> dumpTable() {
            String sql = String.format(Locale.US, "SELECT %s,%s FROM %s;", COLUMN_USER, COLUMN_JSON, TABLE);
            Cursor cursor = open().rawQuery(sql, null);

            List<String> dump = new ArrayList<>();

            while (cursor.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format(Locale.US, "user: %s\t", cursor.getString(0)));
                sb.append(String.format(Locale.US, "json: %s\n", cursor.getString(1)));
                dump.add(sb.toString());
            }
            cursor.close();

            return dump;
        }

        public void empty() {
            String sql = String.format(Locale.US, "DELETE FROM %s WHERE 1", TABLE);
            writer().execSQL(sql);
        }

        private SQLiteDatabase open() {
            return mDBHelper.getReadableDatabase();
        }

        private SQLiteDatabase writer() {
            return mDBHelper.getWritableDatabase();
        }
    }

    public static class LogCacheUpdate {
        public static final String TABLE = "logcacheupdate";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final int STALE_DURATION = 15;

        static {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("CREATE TABLE %s (", TABLE));
            builder.append(String.format(" %s INTEGER PRIMARY KEY AUTOINCREMENT,", COLUMN_ID));
            builder.append(String.format(" %s INTEGER,", COLUMN_USER));
            builder.append(String.format(" %s TIMESTAMP DEFAULT CURRENT_TIMESTAMP);", COLUMN_TIMESTAMP));
            TABLE_CREATE = builder.toString();
        }
        public static final String TABLE_CREATE;
        public static final String TABLE_DROP = String.format("DROP TABLE IF EXISTS %s", TABLE);

        private DatabaseHelper mDBHelper;
        private AndroidFactory mAndroidFactory;

        public LogCacheUpdate() {
            mAndroidFactory = AndroidFactory.getInstance();
            mDBHelper = mAndroidFactory.genDatabaseHelper();
        }

        public void updateUser(int userID) {
            removeUser(userID);

            SQLiteDatabase db = writer();
            ContentValues params = mAndroidFactory.genContentValues();
            params.put(COLUMN_USER, userID);

            db.insert(TABLE, null, params);
        }

        public Calendar getTimestamp(int userID) {
            String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                    COLUMN_TIMESTAMP, TABLE, COLUMN_USER, userID);
            Cursor cursor = open().rawQuery(sql, null);
            Calendar timestamp = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            timestamp.set(0,0,0,0,0,0);

            if (cursor.moveToFirst()) {
                String time = cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                try {
                    timestamp.setTime(sdf.parse(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
            cursor.close();
            return timestamp;
        }

        public boolean timestampIsStale(Calendar timestamp) {
            Calendar compare = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            compare.set(Calendar.MINUTE, compare.get(Calendar.MINUTE) - STALE_DURATION);

            int c = timestamp.compareTo(compare);
            if (c > 0) {
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

        public String dumpTable() {
            String sql = String.format(Locale.US, "SELECT %s,%s FROM %s;", COLUMN_USER, COLUMN_TIMESTAMP, TABLE);
            Cursor cursor = open().rawQuery(sql, null);

            StringBuilder sb = new StringBuilder();

            while (cursor.moveToNext()) {
                sb.append(String.format(Locale.US, "user: %s\t", cursor.getString(0)));
                sb.append(String.format(Locale.US, "timestamp: %s\n", cursor.getString(1)));
            }

            return sb.toString();
        }

        public void empty() {
            String sql = String.format(Locale.US, "DELETE FROM %s WHERE 1", TABLE);
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