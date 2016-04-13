package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.TrainingDatePicker;
import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Manager;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/5/16.
 */
public class DateManager implements Manager {

    private LogDate mLogDate;
    private Activity mActivity;
    private ViewHolder.SubViewHolder mSubViewHolder;

    public DateManager(Activity activity, LogDate logDate) {
        mLogDate = logDate;
        mActivity = activity;
        mSubViewHolder = activity.getViewHolder().date;
        setClickListener();
    }

    @Override
    public void setView(ViewHolder vh) {
        mSubViewHolder = vh.date;
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (item instanceof LogDate) {
            mLogDate = (LogDate) item;
            mSubViewHolder.setText(mLogDate.toString());
        }
        else throw new InputMismatchException();
    }

    @Override
    public LogInfoItem getItem() {
        return mLogDate;
    }

    @Override
    public LogInfo updateLoginfo(LogInfo li) {
        li.set(LogInfo.KEY_DATE, mLogDate);
        return li;
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
}
