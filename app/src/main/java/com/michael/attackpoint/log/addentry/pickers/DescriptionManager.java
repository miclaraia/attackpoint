package com.michael.attackpoint.log.addentry.pickers;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/6/16.
 */
public class DescriptionManager implements ManagerContract.Manager {

    protected LogDescription mItem;
    private ManagerContract.Activity mActivity;
    private ViewHolder.SubViewHolder mSubViewHolder;

    public DescriptionManager(Activity activity, LogDescription logDescription) {
        mItem = logDescription;
        mActivity = activity;
        mSubViewHolder = activity.getViewHolder().description;
        setClickListener();
    }

    @Override
    public void setView(ViewHolder vh) {
        mSubViewHolder = vh.description;
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (item instanceof LogDescription) {
            mItem = (LogDescription) item;
            update();
        }
        else throw new InputMismatchException();
    }

    @Override
    public LogInfoItem getItem() {
        return mItem;
    }

    @Override
    public LogInfo updateLoginfo(LogInfo li) {
        li.set(LogInfo.KEY_DESCRIPTION, mItem);
        return li;
    }

    @Override
    public void setClickListener() {
        mSubViewHolder.setEditTextListener(mActivity);
    }

    @Override
    public void update() {
        mSubViewHolder.setText(mItem.toString());
    }
}
