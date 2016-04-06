package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;
import android.view.View.OnClickListener;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by michael on 4/6/16.
 */
public class DateManagerTest {

    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogDate mLogDate;

    @Mock
    private LogDate mLogDate2;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder mSubViewHolder;

    private DateManager mDateManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.date = mSubViewHolder;

        mDateManager = new DateManager(mActivity, mLogDate);
    }

    @Test
    public void setItem_test() {
        mDateManager.setItem(mLogDate2);
        assertThat((LogDate) mDateManager.getItem(), equalTo(mLogDate2));

        String dateTest = "test date";
        when(mLogDate.toString()).thenReturn(dateTest);

        mDateManager.setItem(mLogDate);
        assertThat((LogDate) mDateManager.getItem(), equalTo(mLogDate));

        verify(mActivity).setText(mSubViewHolder, dateTest);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mDateManager.setItem(null);
    }

    @Test
    public void updateLogInfo_test() {
        mDateManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_DATE, mLogDate);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mActivity).setClickListener(eq(mSubViewHolder), Matchers.<View.OnClickListener>any());
    }


}
