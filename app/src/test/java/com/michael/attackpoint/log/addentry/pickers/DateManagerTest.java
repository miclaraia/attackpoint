package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfo;

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
public class DateManagerTest {

    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogDate mItem;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.DoubleSubViewHolder mSubViewHolder;

    private DateManager mManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.date = mSubViewHolder;

        mManager = new DateManager(mActivity, mItem);
    }

    @Test
    public void setItem_test() {
        mManager.mItem = null;

        String testDate = "test date";
        String testSession = "test session";
        when(mItem.getDate()).thenReturn(testDate);
        when(mItem.getSession()).thenReturn(testSession);

        mManager.setItem(mItem);
        assertThat(mManager.mItem, equalTo(mItem));
        assertThat((LogDate) mManager.getItem(), equalTo(mItem));

        verify(mSubViewHolder).setText(testDate);
        verify(mSubViewHolder).setTextSecondary(testSession);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mManager.setItem(null);
    }

    @Test
    public void updateLogInfo_test() {
        mManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_DATE, mItem);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mSubViewHolder).setItemClickListener(Matchers.<View.OnClickListener>any());
        verify(mSubViewHolder).setSecondaryClickListener(Matchers.<View.OnClickListener>any());
    }

    @Test
    public void update_updatesText() {
        String testDate = "test date";
        String testSession = "test session";
        when(mItem.getDate()).thenReturn(testDate);
        when(mItem.getSession()).thenReturn(testSession);
        mManager.mItem = mItem;

        mManager.update();
        verify(mSubViewHolder).setText(testDate);
        verify(mSubViewHolder).setTextSecondary(testSession);
    }

    @Test
    public void setView_updatesText() {
        String testDate = "test date";
        String testSession = "test session";
        when(mItem.getDate()).thenReturn(testDate);
        when(mItem.getSession()).thenReturn(testSession);
        mManager.mItem = mItem;

        mManager.setView(mViewHolder);
        verify(mSubViewHolder).setText(testDate);
        verify(mSubViewHolder).setTextSecondary(testSession);
    }
}
