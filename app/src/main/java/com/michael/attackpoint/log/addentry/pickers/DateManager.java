package com.michael.attackpoint.log.addentry.pickers;

import android.app.FragmentManager;
import android.view.View;

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
public class DateManager extends GenericPickerManager<LogDate> implements Manager {

    private ViewHolder.DoubleSubViewHolder mDoubleSubViewHolder;

    public DateManager(Activity activity, LogDate logItem) {
        super(activity, logItem);
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (item instanceof LogDate) {
            mItem = (LogDate) item;
            mSubViewHolder.setText(mItem.getDate());
            mDoubleSubViewHolder.setTextSecondary(mItem.getSession());
        }
        else throw new InputMismatchException();
    }

    @Override
    public void setClickListener() {
        final Manager manager = this;
        final FragmentManager fm = mActivity.getFragmentManager();

        mDoubleSubViewHolder.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker dialog = new DatePicker();
                dialog.setManager(manager);
                dialog.show(fm, "datemanager");
            }
        });

        mDoubleSubViewHolder.setSecondaryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSessionPicker dialog = new DateSessionPicker();
                dialog.setManager(manager);
                dialog.show(fm, "datemanager");
            }
        });
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
        mDoubleSubViewHolder = (ViewHolder.DoubleSubViewHolder) vh.date;
        return vh.date;
    }
}
