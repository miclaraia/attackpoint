package com.michael.attackpoint.log.logentry;

import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.data.LogRepository;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.AndroidFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 4/16/16.
 */
public class EntryPresenterTest {
    private static final int LOGID = 0;
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
    private EntryContract.View mView;

    @Mock
    private LogInfo mFakeLogInfo;

    @Captor
    ArgumentCaptor<LogRepository.LoadLogEntryCallback> mRepositoryCallback;

    private EntryPresenter mPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        mPresenter = new EntryPresenter(mLogRepository, mView, USER, LOGID);
    }

    @Test
    public void loadEntry_verifyArgsTest() {
        mPresenter.loadEntry();

        verify(mLogRepository).getLogEntry(eq(USER), eq(LOGID),
                Matchers.<LogRepository.LoadLogEntryCallback>any());
    }

    @Test
    public void loadEntry_callbackTest() {
        mPresenter.loadEntry();

        verify(mLogRepository).getLogEntry(anyInt(), anyInt(), mRepositoryCallback.capture());
        mRepositoryCallback.getValue().onLoaded(mFakeLogInfo);

        verify(mView).ShowEntry(mFakeLogInfo);
    }
}
