package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 4/13/16.
 */
public class DurationManagerTest {

    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogDuration mItem;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder mSubViewHolder;

    private DurationManager mManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.duration = mSubViewHolder;

        mManager = new DurationManager(mActivity, mItem);
    }

    @Test
    public void setItem_test() {
        mManager.mItem = null;

        String testString = "test string";
        when(mItem.toString()).thenReturn(testString);

        mManager.setItem(mItem);
        assertThat(mManager.mItem, equalTo(mItem));
        assertThat((LogDuration) mManager.getItem(), equalTo(mItem));

        verify(mSubViewHolder).setText(testString);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mManager.setItem(null);
    }

    @Test
    public void updateLogInfo_test() {
        mManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_DURATION, mItem);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mSubViewHolder).setClickListener(Matchers.<View.OnClickListener>any());
    }
}
