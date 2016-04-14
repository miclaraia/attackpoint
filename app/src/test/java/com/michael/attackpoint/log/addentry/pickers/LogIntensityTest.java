package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogIntensity;
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
public class LogIntensityTest {
    
    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogIntensity mItem;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder mSubViewHolder;

    private IntensityManager mManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.intensity = mSubViewHolder;

        mManager = new IntensityManager(mActivity, mItem);
    }

    @Test
    public void setItem_test() {
        mManager.mItem = null;

        String testString = "test string";
        when(mItem.toString()).thenReturn(testString);

        mManager.setItem(mItem);
        assertThat(mManager.mItem, equalTo(mItem));
        assertThat((LogIntensity) mManager.getItem(), equalTo(mItem));

        verify(mSubViewHolder).setText(testString);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mManager.setItem(null);
    }

    @Test
    public void updateLogInfo_test() {
        mManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_INTENSITY, mItem);
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mSubViewHolder).setClickListener(Matchers.<View.OnClickListener>any());
    }

    @Test
    public void update_updatesText() {
        String testString = "test string";
        when(mItem.toString()).thenReturn(testString);
        mManager.mItem = mItem;

        mManager.update();
        verify(mSubViewHolder).setText(testString);
    }

    @Test
    public void setView_updatesText() {
        String testString = "test string";
        when(mItem.toString()).thenReturn(testString);
        mManager.mItem = mItem;

        mManager.setView(mViewHolder);
        verify(mSubViewHolder).setText(testString);
    }
}
