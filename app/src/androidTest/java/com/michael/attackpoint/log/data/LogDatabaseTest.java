package com.michael.attackpoint.log.data;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.ArrayMap;

import com.michael.attackpoint.util.DatabaseHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by michael on 3/17/16.
 */
public final class LogDatabaseTest extends AndroidTestCase {
    private static final String TEST_FILE_PREFIX = "test_";
    private static final String TEST_USER = "android_test";
    private static final List<String> TEST_CACHE;
    static {
        List<String> cache = new ArrayList<>();
        cache.add("test_1");
        cache.add("test_2");
        cache.add("test_3");
        TEST_CACHE = cache;
    }
    private static final String TEST_ENTRY = "test_4";

    private LogDatabase mLogDatabase;
    private DatabaseHelper mDBHelper;
    private LogDatabase.LogCache mLogCache;
    private LogDatabase.LogCacheUpdate mLogCacheUpdate;
    RenamingDelegatingContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);
        setContext(mContext);

        mDBHelper = DatabaseHelper.getInstance(mContext);
        mLogDatabase = new LogDatabase(mContext, TEST_USER);
        mLogCache = mLogDatabase.getLogCache();
        mLogCacheUpdate = mLogDatabase.getLogCacheUpdate();
    }

    @Test
    public void subPutCache_addsListToDatabase() {
        mLogCache.putCache(TEST_USER, TEST_CACHE);
        ArrayMap<Integer, String> cache = mLogCache.getCachedLog(TEST_USER);
        assertThat(cache.size(), greaterThan(TEST_CACHE.size()));
        assertThat(cache.values().containsAll(TEST_CACHE), is(Boolean.valueOf(true)));
    }

    @Test
    public void subAddCacheEntry() {
        mLogCache.addCacheEntry(TEST_USER, TEST_ENTRY);
        ArrayMap<Integer, String> cache = mLogCache.getCachedLog(TEST_USER);
        assertThat(cache.values().contains(TEST_ENTRY), is(true));
    }

    @Test
    public void subRemoveCache_emptyTable() {
        if (mLogCache.getCachedLog(TEST_USER).size() <= 0) {
            mLogCache.putCache(TEST_USER, TEST_CACHE);
        }

        mLogCache.removeCache(TEST_USER);

        assertThat(mLogCache.getCachedLog(TEST_USER).size(), is(0));
    }

    @Test
    public void updateUpdateTimestamp() {
        Calendar current = mLogCacheUpdate.getTimestamp(TEST_USER);
        mLogCacheUpdate.updateUser(TEST_USER);
        assertThat(mLogCacheUpdate.getTimestamp(TEST_USER),
                not(current));
    }

    @Test
    public void updatetimestampIsStale_true() {
        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        minute = minute - LogDatabase.LogCacheUpdate.STALE_DURATION - 1;

        cal.set(Calendar.MINUTE, minute);

        assertThat(mLogCacheUpdate.timestampIsStale(cal), is(true));
    }

    @Test
    public void updatetimestampIsStale_false() {
        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        minute = minute - LogDatabase.LogCacheUpdate.STALE_DURATION + 1;

        cal.set(Calendar.MINUTE, minute);

        assertThat(mLogCacheUpdate.timestampIsStale(cal), is(false));
    }

    @Test
    public void updateRemoveUser() {
        mLogCacheUpdate.updateUser(TEST_USER);
        mLogCacheUpdate.removeUser(TEST_USER);

        assertThat(mLogCacheUpdate.userIsStale(TEST_USER), is(true));
    }

}
