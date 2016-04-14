package com.michael.attackpoint.log.addentry.activity;

import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.addentry.pickers.Managers;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.util.AndroidFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 4/14/16.
 */
public class TrainingPresenterTest {

    @Mock
    private AndroidFactory mAndroidFactory;

    @Mock
    private ActivityTable mActivityTable;

    @Mock
    private TrainingContract.View mView;

    @Mock
    private Managers mManagers;

    @Mock
    private LogInfo mLogInfo;

    private TrainingPresenter mPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        mPresenter = new TrainingPresenter(mView);
    }

    @Test
    public void onSubmit_createRequest() {
        when(mManagers.updateLogInfo(mLogInfo)).thenReturn(mLogInfo);
        mPresenter.mLogInfo = mLogInfo;
        mPresenter.mManagers = mManagers;

        mPresenter.onSubmit();
        verify(mManagers).updateLogInfo(mLogInfo);
        verify(mView).createRequest(mLogInfo);
    }

    @Test
    public void onPause_updatesLogInfo() {
        when(mManagers.updateLogInfo(mLogInfo)).thenReturn(mLogInfo);
        mPresenter.mLogInfo = mLogInfo;
        mPresenter.mManagers = mManagers;

        mPresenter.onPause();
        verify(mManagers).updateLogInfo(mLogInfo);
    }
}
