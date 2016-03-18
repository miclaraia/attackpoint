package com.michael.attackpoint.log.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.ArrayMap;

import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.DatabaseHelper;
import com.michael.attackpoint.log.data.LogDatabase.LogCache;
import com.michael.attackpoint.log.data.LogDatabase.LogCacheUpdate;

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
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 3/17/16.
 */
public final class LogDatabaseTest {
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
        cal.set(2016,01,01,12,30,0);
        TEST_TIMESTAMP_CAL = cal;
    }

    @Mock
    private DatabaseHelper mDBHelper;

    @Mock
    private SQLiteDatabase mDatabaseObject;

    @Mock
    private Cursor mCursor;

    private LogDatabase mLogDatabase;
    private LogCache mLogCache;
    private LogCacheUpdate mLogCacheUpdate;

    @Before
    protected void setUp() {
        MockitoAnnotations.initMocks(this);

        mLogCache = new LogDatabase.LogCache(mDBHelper);
    }

    @Test
    public void getCachedLog_makesQuery() {
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);
        String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                LogCache.COLUMN_JSON, LogCache.TABLE,
                LogCache.COLUMN_USER, TEST_USER);

        mLogCache.getCachedLog(TEST_USER);

        verify(mDatabaseObject).rawQuery(sql, null);
    }

    @Test
    public void getCachedEntry_makesQuery() {
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);

        int test_id = 999;
        String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d AND %s=%d",
                LogCache.COLUMN_JSON, LogCache.TABLE,
                LogCache.COLUMN_USER, TEST_USER,
                LogCache.COLUMN_AP_ID, test_id);

        verify(mDatabaseObject).rawQuery(sql, null);
    }

    @Test
    public void addCache_addsListToDatabase() {
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);

        mLogCache.addCache(TEST_USER, TEST_CACHE);

        verify(mDatabaseObject, times(3))
                .insert(LogCache.TABLE, null, any(ContentValues.class));
    }

    @Test
    public void addCacheEntry_addsEntryToDatabase() {
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);

        LogInfo entry = TEST_CACHE.get(0);
        String json = entry.toJSON().toString();
        int id = entry.getID();

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);

        mLogCache.addCacheEntry(TEST_USER, TEST_CACHE.get(0));

        verify(mDatabaseObject).insert(LogCache.TABLE, null, argument.capture());

        ContentValues params = argument.getValue();
        assertThat((Integer) params.get(LogCache.COLUMN_USER), is(TEST_USER));
        assertThat((Integer) params.get(LogCache.COLUMN_AP_ID), is(id));
        assertThat((String) params.get(LogCache.COLUMN_JSON), is(json));
    }

    @Test
    public void removeCache_makesQuery() {
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);

        String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                LogCache.TABLE, LogCache.COLUMN_USER, TEST_USER);

        mLogCache.removeCache(TEST_USER);

        verify(mDatabaseObject).execSQL(sql);
    }

    @Test
    public void updateUser_makesInsert() {
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);

        ArgumentCaptor<ContentValues> argument = ArgumentCaptor.forClass(ContentValues.class);

        mLogCacheUpdate.updateUser(TEST_USER);

        verify(mDatabaseObject).insert(LogCache.TABLE, null, argument.capture());

        ContentValues params = argument.getValue();
        assertThat((Integer) params.get(LogCacheUpdate.COLUMN_USER), is(TEST_USER));
    }

    @Test
    public void getTimestamp_makesQuery() {
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);

        String sql = String.format(Locale.US, "SELECT %s FROM %s WHERE %s=%d",
                LogCacheUpdate.COLUMN_TIMESTAMP, LogCacheUpdate.TABLE,
                LogCacheUpdate.COLUMN_USER, TEST_USER);

        verify(mDatabaseObject).rawQuery(sql, null);
    }

    @Test
    public void getTimestamp_parses() {
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);
        when(mDatabaseObject.rawQuery(anyString(), null)).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn(TEST_TIMESTAMP);

        Calendar timestamp = mLogCacheUpdate.getTimestamp(TEST_USER);
        assertThat(timestamp, is(TEST_TIMESTAMP_CAL));
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
        when(mDBHelper.getReadableDatabase()).thenReturn(mDatabaseObject);
        when(mDatabaseObject.rawQuery(anyString(), null)).thenReturn(mCursor);
        when(mCursor.moveToFirst()).thenReturn(true);
        when(mCursor.getString(0)).thenReturn(TEST_TIMESTAMP);

        boolean stale = mLogCacheUpdate.userIsStale(TEST_USER);
        assertThat(stale, is(true));
    }

    @Test
    public void removeUser_makesQuery() {
        when(mDBHelper.getWritableDatabase()).thenReturn(mDatabaseObject);

        String sql = String.format(Locale.US, "DELETE FROM %s WHERE %s=%d",
                LogCacheUpdate.TABLE, LogCacheUpdate.COLUMN_USER,
                TEST_USER);

        mLogCacheUpdate.removeUser(TEST_USER);
        verify(mDatabaseObject).execSQL(sql);
    }

}
