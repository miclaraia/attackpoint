package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.addentry.TrainingPicker;
import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Manager;
import com.michael.attackpoint.util.AndroidFactory;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/6/16.
 */
public class ActivityManager implements Manager {

    private LogInfoActivity mLogInfoActivity;
    private ManagerContract.Activity mActivity;
    private ViewHolder.SubViewHolder mSubViewHolder;

    public ActivityManager(Activity activity, LogInfoActivity logInfoActivity) {
        mLogInfoActivity = logInfoActivity;
        mActivity = activity;
        mSubViewHolder = activity.getViewHolder().activity;

        mLogInfoActivity.set(getFirstActivity());

        setClickListener();
    }

    @Override
    public void setView(ViewHolder vh) {
        mSubViewHolder = vh.activity;
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (item instanceof LogInfoActivity) {
            mLogInfoActivity = (LogInfoActivity) item;
            mSubViewHolder.setText(mLogInfoActivity.toString());
        }
        else throw new InputMismatchException();
    }

    @Override
    public LogInfoItem getItem() {
        return mLogInfoActivity;
    }

    @Override
    public LogInfo updateLoginfo(LogInfo li) {
        li.set(LogInfo.KEY_ACTIVITY, mLogInfoActivity);
        return li;
    }

    private String getFirstActivity() {
        ActivityTable table = AndroidFactory.getInstance().genActivityTable();
        return table.getFirst();
    }

    @Override
    public void setClickListener() {
        final Manager manager = this;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerGenerator.genActivityPicker(manager);
            }
        };
        mSubViewHolder.setClickListener(listener);
    }
}
