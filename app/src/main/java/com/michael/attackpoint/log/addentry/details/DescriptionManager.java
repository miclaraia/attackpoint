package com.michael.attackpoint.log.addentry.details;

import android.content.Context;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogDescription;
import com.michael.attackpoint.log.loginfo.LogInfo;

/**
 * Created by michael on 3/11/16.
 */
public class DescriptionManager extends EditTextManager<LogDescription> {
    public DescriptionManager(Context context, ViewHolder.SubViewHolder svh, LogDescription detail) {
        super(context, svh, detail);
    }

    public DescriptionManager(Context context, ViewHolder.SubViewHolder svh, LogInfo detail) {
        super(context, svh, detail);
    }

    @Override
    public LogDescription onGetDetail() {
        String text = mEditText.getText().toString();
        mDetail.set(text);
        return mDetail;
    }

    @Override
    protected void setDetail(Object detail) {
        if (detail instanceof String) mDetail.set((String) detail);
    }

    @Override
    protected String getKey() {
        return LogInfo.KEY_DESCRIPTION;
    }
}
