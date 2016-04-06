package com.michael.attackpoint.log.addentry.pickers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.michael.attackpoint.log.addentry.details.ViewHolder;
import com.michael.attackpoint.log.addentry.pickers.ManagerContract.Activity;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

import java.util.InputMismatchException;

/**
 * Created by michael on 4/6/16.
 */
public class DescriptionManager implements ManagerContract.Manager {

    private LogDescription mLogDescription;
    private ManagerContract.Activity mActivity;
    private ViewHolder.SubViewHolder mSubViewHolder;

    public DescriptionManager(Activity activity, LogDescription logDescription) {
        mLogDescription = logDescription;
        mActivity = activity;
        mSubViewHolder = activity.getViewHolder().description;
        setClickListener();
    }

    @Override
    public void setView(ViewHolder vh) {
        mSubViewHolder = vh.date;
    }

    @Override
    public void setItem(LogInfoItem item) {
        if (item instanceof LogDescription) {
            mLogDescription = (LogDescription) item;
            mActivity.setText(mSubViewHolder, mLogDescription.toString());
        }
        else throw new InputMismatchException();
    }

    @Override
    public LogInfoItem getItem() {
        return mLogDescription;
    }

    @Override
    public LogInfo updateLoginfo(LogInfo li) {
        li.set(LogInfo.KEY_DESCRIPTION, mLogDescription);
        return li;
    }

    @Override
    public void setClickListener() {
        mActivity.setClickListener_editText(mSubViewHolder);
    }
}
