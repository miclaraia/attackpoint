package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Manager;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/13/16.
 */
public class DurationManager extends GenericPickerManager<LogDuration> implements ManagerContract.Manager {

    public DurationManager(Activity activity, LogDuration logItem) {
        super(activity, logItem);
    }

    @Override
    public Class<LogDuration> getTClass() {
        return LogDuration.class;
    }

    @Override
    public String getLogInfoKey() {
        return LogInfo.KEY_DURATION;
    }

    @Override
    public ViewHolder.SubViewHolder getViewHolder(ViewHolder vh) {
        return vh.duration;
    }

    @Override
    public void setClickListener() {
        final Manager manager = this;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerGenerator.genDurationPicker(manager);
            }
        };
        mSubViewHolder.setClickListener(listener);
    }
}