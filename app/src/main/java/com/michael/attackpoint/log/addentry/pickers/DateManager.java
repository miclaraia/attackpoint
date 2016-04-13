package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Manager;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 4/5/16.
 */
public class DateManager extends GenericPickerManager<LogDate> implements Manager {

    public DateManager(Activity activity, LogDate logItem) {
        super(activity, logItem);
    }

    @Override
    public void setClickListener() {
        final Manager manager = this;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerGenerator.genDatePicker(manager);
            }
        };
        mSubViewHolder.setClickListener(listener);
    }

    @Override
    public Class<LogDate> getTClass() {
        return LogDate.class;
    }

    @Override
    public String getLogInfoKey() {
        return LogInfo.KEY_DATE;
    }

    @Override
    public ViewHolder.SubViewHolder getViewHolder(ViewHolder vh) {
        return vh.date;
    }
}
