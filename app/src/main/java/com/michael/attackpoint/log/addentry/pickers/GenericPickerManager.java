package com.michael.attackpoint.log.addentry.pickers;

import android.view.View;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.addentry.details.ViewHolder.SubViewHolder;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/13/16.
 */
public abstract class GenericPickerManager<T extends LogInfoItem> implements ManagerContract.Manager {
    protected T mItem;
    protected ManagerContract.Activity mActivity;
    protected SubViewHolder mSubViewHolder;

    public GenericPickerManager(ManagerContract.Activity activity, T logItem) {
        mItem = logItem;
        mActivity = activity;

        setView(mActivity.getViewHolder());
        setClickListener();
    }

    @Override
    public void setView(ViewHolder vh) {
        mSubViewHolder = getViewHolder(vh);
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (getTClass().isInstance(item)) {
            mItem = (T) item;
            mSubViewHolder.setText(mItem.toString());
        }
        else throw new InputMismatchException();
    }

    @Override
    public LogInfoItem getItem() {
        return mItem;
    }

    @Override
    public LogInfo updateLoginfo(LogInfo li) {
        li.set(getLogInfoKey(), mItem);
        return li;
    }

    public abstract Class<T> getTClass();
    public abstract String getLogInfoKey();
    public abstract SubViewHolder getViewHolder(ViewHolder vh);
}
