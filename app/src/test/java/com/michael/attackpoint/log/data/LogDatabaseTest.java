package com.michael.attackpoint.log.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.ArrayMap;

import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.AndroidFactory;
import com.michael.attackpoint.util.DatabaseHelper;
import com.michael.attackpoint.log.data.LogDatabase.LogCache;
import com.michael.attackpoint.log.data.LogDatabase.LogCacheUpdate;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 3/17/16.
 */
public class LogDatabaseTest {
    private static final Integer TEST_USER = 199;
    private static final String TEST_TIMESTAMP = "2016-01-01 12:30:00"; // YYYY-MM-DD HH:MI:SS
    private static final Calendar TEST_TIMESTAMP_CAL;
    private static final List<LogInfo> TEST_CACHE;
    static {
        List<LogInfo> cache = new ArrayList<>();
        cache.add(new LogInfo());
        cache.add(new LogInfo());
        cache.add(new LogInfo());
        TEST_CACHE = cache;

        Calendar cal = Calendar.getInstance();
        cal.set(2016,0,01,12,30,0);
        cal.set(Calendar.MILLISECOND, 0);
        TEST_TIMESTAMP_CAL = cal;
    }

    @Mock
    private DatabaseHelper mDBHelper;

    @Mock
    private SQLiteDatabase mDatabaseObject;

    @Mock
    private Cursor mCursor;

    @Mock
    private AndroidFactory mAndroidFactory;

    @Mock
    private ContentValues mContentValues;

    @Mock
    private JSONObject mJSONObject;

    private LogCache mLogCache;
    private LogCacheUpdate mLogCacheUpdate;

    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);

        when(mAndroidFactory.genContentValues()).thenReturn(mContentValues);

        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);

        AndroidFactory.setFactory(mAndroidFactory);

        mLogCache = new LogCache(mDBHelper, mAndroidFactory);
        mLogCacheUpdate = new LogCacheUpdate(mDBHelper, mAndroidFactory);
    }

    @Test
    public void getCachedLog_makesQuery() {
        when(mDatabaseObject.rawQuery(anyString(), any(String[].class))).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn("");

        String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                LogCache.COLUMN_JSON, LogCache.TABLE,
                LogCache.COLUMN_USER, TEST_USER);

        mLogCache.getCachedLog(TEST_USER);

        verify(mDatabaseObject).rawQuery(eq(sql), isNull(String[].class));
    }

    @Test
    public void getCachedEntry_makesQuery() {
        when(mDatabaseObject.rawQuery(anyString(), any(String[].class))).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn(new LogInfo().toJSON().toString());

        int test_id = 999;
        String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d AND %s=%d",
                LogCache.COLUMN_JSON, LogCache.TABLE,
                LogCache.COLUMN_USER, TEST_USER,
                LogCache.COLUMN_AP_ID, test_id);

        mLogCache.getCachedEntry(TEST_USER, test_id);

        verify(mDatabaseObject).rawQuery(eq(sql), isNull(String[].class));
    }

    @Test
    public void addCache_addsListToDatabase() {
        mLogCache.addCache(TEST_USER, TEST_CACHE);

        verify(mContentValues, times(3)).put(LogCache.COLUMN_USER, TEST_USER);
        verify(mContentValues, times(3)).put(eq(LogCache.COLUMN_AP_ID), anyInt());
        verify(mContentValues, times(3)).put(eq(LogCache.COLUMN_JSON), anyString());
        verify(mDatabaseObject, times(3))
                .insert(eq(LogCache.TABLE), isNull(String.class), any(ContentValues.class));
    }

    @Test
    public void addCacheEntry_addsEntryToDatabase() {

        LogInfo entry = TEST_CACHE.get(0);
        String json = entry.toJSON().toString();
        int id = entry.getID();

        mLogCache.addCacheEntry(TEST_USER, TEST_CACHE.get(0));

        verify(mContentValues).put(LogCache.COLUMN_USER, TEST_USER);
        verify(mContentValues).put(LogCache.COLUMN_AP_ID, id);
        verify(mContentValues).put(LogCache.COLUMN_JSON, json);
        verify(mDatabaseObject).insert(eq(LogCache.TABLE), isNull(String.class), any(ContentValues.class));
    }

    @Test
    public void removeCache_makesQuery() {
        String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                LogCache.TABLE, LogCache.COLUMN_USER, TEST_USER);

        mLogCache.removeCache(TEST_USER);

        verify(mDatabaseObject).execSQL(sql);
    }

    @Test
    public void updateUser_makesInsert() {
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);
        when(mAndroidFactory.genContentValues()).thenReturn(mContentValues);

        mLogCacheUpdate.updateUser(TEST_USER);

        verify(mContentValues).put(LogCacheUpdate.COLUMN_USER, TEST_USER);
        verify(mDatabaseObject).insert(eq(LogCacheUpdate.TABLE), isNull(String.class), eq(mContentValues));
    }

    @Test
    public void getTimestamp_makesQuery() {
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);
        when(mDatabaseObject.rawQuery(anyString(), any(String[].class))).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn(TEST_TIMESTAMP);

        String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                LogCacheUpdate.COLUMN_TIMESTAMP, LogCacheUpdate.TABLE,
                LogCacheUpdate.COLUMN_USER, TEST_USER);

        mLogCacheUpdate.getTimestamp(TEST_USER);

        verify(mDatabaseObject).rawQuery(eq(sql), isNull(String[].class));
    }

    @Test
    public void getTimestamp_parses() {
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);
        when(mDatabaseObject.rawQuery(anyString(), any(String[].class))).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn(TEST_TIMESTAMP);

        Calendar timestamp = mLogCacheUpdate.getTimestamp(TEST_USER);
        assertThat(timestamp.getTimeInMillis(), is(TEST_TIMESTAMP_CAL.getTimeInMillis()));
    }

    @Test
    public void timestampIsStale_true() {
        Calendar cal = Calendar.getInstance();
        int minutes = cal.get(Calendar.MINUTE);
        minutes = minutes - LogCacheUpdate.STALE_DURATION - 1;
        cal.set(Calendar.MINUTE, minutes);

        boolean stale = mLogCacheUpdate.timestampIsStale(cal);
        assertThat(stale, is(true));
    }

    @Test
    public void timestampIsStale_false() {
        Calendar cal = Calendar.getInstance();
        int minutes = cal.get(Calendar.MINUTE);
        minutes = minutes - LogCacheUpdate.STALE_DURATION + 1;
        cal.set(Calendar.MINUTE, minutes);

        boolean stale = mLogCacheUpdate.timestampIsStale(cal);
        assertThat(stale, is(false));
    }

    @Test
    public void userIsStale_true() {
        when(mDatabaseObject.rawQuery(anyString(), any(String[].class))).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn(TEST_TIMESTAMP);

        boolean stale = mLogCacheUpdate.userIsStale(TEST_USER);
        assertThat(stale, is(true));
    }

    @Test
    public void removeUser_makesQuery() {
        String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                LogCacheUpdate.TABLE, LogCacheUpdate.COLUMN_USER,
                TEST_USER);

        mLogCacheUpdate.removeUser(TEST_USER);
        verify(mDatabaseObject).execSQL(sql);
    }

}
