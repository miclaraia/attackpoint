package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

/**
 * Created by michael on 2/25/16.
 */
public abstract class DetailManager<T extends LogInfoItem> {
    ViewHolder.SubViewHolder mSVH;
    T mDetail;

    public DetailManager(ViewHolder.SubViewHolder svh, T detail) {
        mSVH = svh;
        mDetail = detail;

        update();
    }

    public void updateDetail(Object detail) {
        setDetail(detail);
        update();
    }

    public void updateDetail(T detail) {
        mDetail = detail;
        update();
    }

    public T getDetail() {
        return mDetail;
    }

    public String toString() {
        return mDetail.toString();
    }

    public LogInfo updateLogInfo(LogInfo logInfo) {
        logInfo.set(getKey(), mDetail);
        return logInfo;
    }

    public abstract void update();
    protected abstract void setDetail(Object detail);
    protected abstract String getKey();
}
