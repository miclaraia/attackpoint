package com.michael.attackpoint.log.addentry.pickers;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogInfo;

import org.junit.Before;
import org.junit.Test;
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
public class DescriptionManagerTest {

    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogDescription mItem;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder mSubViewHolder;

    private DescriptionManager mManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.description = mSubViewHolder;

        mManager = new DescriptionManager(mActivity, mItem);
    }

    @Test
    public void setItem_test() {
        mManager.mItem = null;

        String dateTest = "test description";
        when(mItem.toString()).thenReturn(dateTest);

        mManager.setItem(mItem);
        assertThat(mManager.mItem, equalTo(mItem));
        assertThat((LogDescription) mManager.getItem(), equalTo(mItem));

        verify(mSubViewHolder).setText(dateTest);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mManager.setItem(null);
    }

    @Test
    public void updateLogInfo_test() {
        mManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_DESCRIPTION, mItem);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mSubViewHolder).setEditTextListener(mActivity);
    }
}
