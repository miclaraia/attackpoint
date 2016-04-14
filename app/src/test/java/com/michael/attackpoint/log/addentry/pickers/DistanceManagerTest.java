package com.michael.attackpoint.log.addentry.pickers;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogDistance.Distance;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.Unit;

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
 * Created by michael on 4/13/16.
 */
public class DistanceManagerTest {

    @Mock
    ManagerContract.Activity mActivity;

    @Mock
    private LogDistance mItem;

    @Mock
    private LogInfo mLogInfo;

    @Mock
    private ViewHolder mViewHolder;

    @Mock
    private ViewHolder.SubViewHolder_Distance mSubViewHolder;

    private DistanceManager mManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mActivity.getFragmentManager()).thenReturn(null);
        when(mActivity.getViewHolder()).thenReturn(mViewHolder);
        mViewHolder.distance = mSubViewHolder;

        mManager = new DistanceManager(mActivity, mItem);
    }

    @Test
    public void setItem_test() {
        String testString = "1.6";
        LogDistance.Distance testDistance = new LogDistance.Distance();
        testDistance.setDistance(1.6);

        mManager.mItem = null;

        when(mItem.get()).thenReturn(testDistance);

        mManager.setItem(mItem);
        assertThat(mManager.mItem, equalTo(mItem));
        verify(mSubViewHolder).setText(testString);
    }

    @Test(expected = InputMismatchException.class)
    public void setNull_fail() {
        mManager.setItem(null);
    }

    @Test
    public void getItem_test() {
        String testString = "1.6";
        when(mSubViewHolder.getEditTextInput()).thenReturn(testString);

        mManager.getItem();
        verify(mSubViewHolder).getEditTextInput();
        verify(mItem).setDistance(1.6);
    }

    @Test
    public void updateLogInfo_test() {
        String testString = "1.6";
        when(mSubViewHolder.getEditTextInput()).thenReturn(testString);

        mManager.updateLoginfo(mLogInfo);
        verify(mLogInfo).set(LogInfo.KEY_DISTANCE, mItem);
        verify(mSubViewHolder).getEditTextInput();
    }

    @Test
    public void getItem_getsDistanceFromInput() {
        String testString = "1.6";
        when(mSubViewHolder.getEditTextInput()).thenReturn(testString);

        mManager.getItem();
        verify(mSubViewHolder).getEditTextInput();
    }

    @Test
    public void setClickListener_attachesToActivity() {
        verify(mSubViewHolder).setEditTextListener(mActivity);
    }

    @Test
    public void updateDistance_parsesDouble() {
        String testString = "1.6";
        Double testDouble = 1.6;
        Distance distance = new Distance();

        when(mSubViewHolder.getEditTextInput()).thenReturn(testString);
        when(mItem.get()).thenReturn(distance);

        mManager.setItem(mItem);
        mManager.updateDistance();
        verify(mSubViewHolder).getEditTextInput();
        verify(mItem).setDistance(testDouble);
    }

    @Test
    public void setUnit_updatesTextView() {
        Unit unit = Unit.UnitManager.getDefault();
        mManager.setUnit(unit);
        verify(mSubViewHolder).setUnit(unit.toNickname());
    }

    @Test
    public void setUnit_updatesLogDistance() {
        String testString = "1.6";
        Distance testDistance = new Distance();
        when(mSubViewHolder.getEditTextInput()).thenReturn(testString);
        when(mItem.get()).thenReturn(testDistance);

        Unit testUnit = Unit.UnitManager.getDefault();
        mManager.setItem(mItem);
        mManager.setUnit(testUnit);

        verify(mItem).setUnit(testUnit);
    }

    @Test
    public void update_updatesText() {
        Double testDouble = 1.6;
        String testUnit = "km";
        Distance distance = new Distance(testDouble, testUnit);

        when(mItem.get()).thenReturn(distance);
        mManager.mItem = mItem;

        mManager.update();
        mSubViewHolder.setText(testDouble.toString());
        mSubViewHolder.setUnit(testUnit);
    }

    @Test
    public void setView_updatesText() {
        Double testDouble = 1.6;
        String testUnit = "km";
        Distance distance = new Distance(testDouble, testUnit);

        when(mItem.get()).thenReturn(distance);
        mManager.mItem = mItem;

        mManager.setView(mViewHolder);
        mSubViewHolder.setText(testDouble.toString());
        mSubViewHolder.setUnit(testUnit);
    }
}
