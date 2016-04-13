package com.michael.attackpoint.log.addentry.pickers;

import android.app.FragmentManager;
import android.view.View;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;
import com.michael.attackpoint.log.loginfo.Unit;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/6/16.
 */
public class DistanceManager implements ManagerContract.Manager {
    protected LogDistance mItem;
    private ManagerContract.Activity mActivity;
    private ViewHolder.SubViewHolder_Distance mSubViewHolder;

    public DistanceManager(Activity activity, LogDistance logDistance) {
        mItem = logDistance;
        mActivity = activity;
        mSubViewHolder = (ViewHolder.SubViewHolder_Distance) activity.getViewHolder().distance;
        setClickListener();
    }

    @Override
    public void setView(ViewHolder vh) {
        mSubViewHolder = (ViewHolder.SubViewHolder_Distance) vh.distance;
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (item instanceof LogDistance) {
            mItem = (LogDistance) item;

            LogDistance.Distance distance = mItem.get();
            mSubViewHolder.setText(distance.getDistance().toString());
        }
        else throw new InputMismatchException();
    }

    @Override
    public LogInfoItem getItem() {
        updateDistance();
        return mItem;
    }

    @Override
    public LogInfo updateLoginfo(LogInfo li) {
        li.set(LogInfo.KEY_DISTANCE, mItem);
        updateDistance();
        return li;
    }

    @Override
    public void setClickListener() {
        mSubViewHolder.setEditTextListener(mActivity);

        final ManagerContract.Manager manager = this;
        final FragmentManager fm = mActivity.getFragmentManager();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistanceUnitPicker dialog = new DistanceUnitPicker();
                dialog.setManager(manager);
                dialog.show(fm, "durationmanager");
            }
        };
        mSubViewHolder.setUnitClickListener(listener);
    }

    public void setUnit(Unit unit) {
        mItem.setUnit(unit);
        mSubViewHolder.setUnit(unit.toNickname());
    }

    protected void updateDistance() {
        Double distance = Double.parseDouble(mSubViewHolder.getEditTextInput());
        mItem.setDistance(distance);
    }
}
