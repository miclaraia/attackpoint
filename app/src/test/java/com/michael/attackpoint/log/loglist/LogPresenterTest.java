package com.michael.attackpoint.log.loglist;

import com.michael.attackpoint.log.data.LogRepository;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.util.AndroidFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 3/30/16.
 */
public class LogPresenterTest {

    private static final List<LogInfo> LOGLIST = new ArrayList<>();
    private static final List<LogInfo> EMPTYLIST = new ArrayList<>(0);
    private static final int USER = 0;

    private static LogInfo genLogInfo(String duration, double distance, String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("mmss");
        LogInfo li = new LogInfo();

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(duration));
        } catch (ParseException e) {
            e.printStackTrace();
            fail();
        }
        li.set(LogInfo.KEY_DURATION, new LogDuration(cal));
        li.set(LogInfo.KEY_DISTANCE, new LogDistance(new LogDistance.Distance(distance, "km")));
        li.set(LogInfo.KEY_DESCRIPTION, new LogDescription(text));

        li.setPace();

        return li;
    }

    @Mock
    private AndroidFactory mAndroidFactory;

    @Mock
    private ActivityTable mActivityTable;

    @Mock
    private LogRepository mLogRepository;

    @Mock
    private LogContract.View mLogView;

    @Captor
    private ArgumentCaptor<LogRepository.LoadLogCallback> mLoadCallbackCaptor;

    @Captor
    private ArgumentCaptor<LogRepository.RefreshCallback> mRefreshCallbackCaptor;

    private LogPresenter mPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        LOGLIST.add(genLogInfo("4000", 5.0, "test1"));
        LOGLIST.add(genLogInfo("3000", 3.2, "test2"));
        LOGLIST.add(genLogInfo("4230", 1.6, "test3"));

        mPresenter = new LogPresenter(mLogRepository, mLogView, USER);
    }

    @Test
    public void loadLog_loadFromNetwork() {
        mPresenter.loadLog(true);
        verify(mLogView).setProgressIndicator(true);

        verify(mLogRepository).refreshData(eq(USER), mRefreshCallbackCaptor.capture());
        mRefreshCallbackCaptor.getValue().done();

        verify(mLogRepository).getLog(eq(USER), mLoadCallbackCaptor.capture());
        mLoadCallbackCaptor.getValue().onLoaded(LOGLIST);

        verify(mLogView).showLog(LOGLIST);
        verify(mLogView).setProgressIndicator(false);
    }

    @Test
    public void loadLog_loadFromCache() {
        mPresenter.loadLog(false);
        verify(mLogView).setProgressIndicator(true);

        verify(mLogRepository).getLog(eq(USER), mLoadCallbackCaptor.capture());
        mLoadCallbackCaptor.getValue().onLoaded(LOGLIST);

        verify(mLogView).showLog(LOGLIST);
        verify(mLogView).setProgressIndicator(false);
    }

    @Test
    public void addNewEntry_callbackToView() {
        mPresenter.addNewEntry();
        verify(mLogView).showAddEntry();
    }

    @Test
    public void openEntryDetails_callbackToView() {
        LogInfo li = LOGLIST.get(0);
        mPresenter.openEntryDetails(li);
        String id = "" + li.getID();
        verify(mLogView).showEntryDetail(id);
    }
}
