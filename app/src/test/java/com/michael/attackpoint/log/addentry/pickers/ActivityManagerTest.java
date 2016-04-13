package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogInfoItem;
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
    private LogInfoActivity mItem;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder mSubViewHolder;

    private ActivityManager mManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AndroidFactory.setFactory(mAndroidFactory);
        when(mAndroidFactory.genActivityTable()).thenReturn(mActivityTable);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.activity = mSubViewHolder;

        mManager = new ActivityManager(mActivity, mItem);
    }

    @Test
    public void setItem_test() {
        mManager.mItem = null;

        String testString = "test string";
        when(mItem.toString()).thenReturn(testString);

        mManager.setItem(mItem);
        assertThat((LogInfoActivity) mManager.getItem(), equalTo(mItem));

        verify(mSubViewHolder).setText(testString);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mManager.setItem((LogInfoItem) null);
    }

    @Test
    public void updateLogInfo_test() {
        mManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_ACTIVITY, mItem);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mSubViewHolder).setClickListener(Matchers.<View.OnClickListener>any());
    }
}
