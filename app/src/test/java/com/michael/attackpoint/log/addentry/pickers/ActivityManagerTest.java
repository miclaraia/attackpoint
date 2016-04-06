package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.util.AndroidFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 4/6/16.
 */
public class ActivityManagerTest {

    @Mock
    AndroidFactory mAndroidFactory;

    @Mock
    ActivityTable mActivityTable;

    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogInfoActivity mLogInfoActivity;

    @Mock
    private LogInfoActivity mLogInfoActivity2;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder mSubViewHolder;

    private ActivityManager mActivityManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.activity = mSubViewHolder;

        mActivityManager = new ActivityManager(mActivity, mLogInfoActivity);
    }

    @Test
    public void setItem_test() {
        mActivityManager.setItem(mLogInfoActivity2);
        assertThat((LogInfoActivity) mActivityManager.getItem(), equalTo(mLogInfoActivity2));

        String dateTest = "test activity";
        when(mLogInfoActivity.toString()).thenReturn(dateTest);

        mActivityManager.setItem(mLogInfoActivity);
        assertThat((LogInfoActivity) mActivityManager.getItem(), equalTo(mLogInfoActivity));

        verify(mActivity).setText(mSubViewHolder, dateTest);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mActivityManager.setItem(null);
    }

    @Test
    public void updateLogInfo_test() {
        mActivityManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_ACTIVITY, mLogInfoActivity);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mActivity).setClickListener(eq(mSubViewHolder), Matchers.<View.OnClickListener>any());
    }
}
