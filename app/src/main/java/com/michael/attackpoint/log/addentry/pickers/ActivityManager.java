package com.michael.attackpoint.log.addentry.pickers;

import android.app.DialogFragment;
import android.app.FragmentManager;
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
public class ActivityManager extends GenericPickerManager<LogInfoActivity> implements Manager {

    public ActivityManager(Activity activity, LogInfoActivity logItem) {
        super(activity, logItem);
        mItem.set(getFirstActivity());
    }

    @Override
    public Class<LogInfoActivity> getTClass() {
        return LogInfoActivity.class;
    }

    @Override
    public String getLogInfoKey() {
        return LogInfo.KEY_ACTIVITY;
    }

    @Override
    public ViewHolder.SubViewHolder getViewHolder(ViewHolder vh) {
        return vh.activity;
    }

    @Override
    public void setClickListener() {
        final Manager manager = this;
        final FragmentManager fm = mActivity.getFragmentManager();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityPicker dialog = new ActivityPicker();
                dialog.setManager(manager);
                dialog.show(fm, "activitymanager");
            }
        };
        mSubViewHolder.setClickListener(listener);
    }

    private String getFirstActivity() {
        ActivityTable table = AndroidFactory.getInstance().genActivityTable();
        return table.getFirst();
    }
}
