package com.michael.attackpoint.log.addentry.details;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
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

    public DetailManager(ViewHolder.SubViewHolder svh, LogInfo logInfo) {
        mSVH = svh;
        mDetail = (T) logInfo.get(getKey());

        update();
    }

    public DetailManager(ViewHolder.SubViewHolder svh, T detail, boolean update) {
        mSVH = svh;
        mDetail = detail;

        if (update) update();
    }

    public DetailManager(ViewHolder.SubViewHolder svh, LogInfo logInfo, boolean update) {
        mSVH = svh;
        mDetail = (T) logInfo.get(getKey());

        if (update) update();
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
        logInfo.set(getKey(), getDetail());
        return logInfo;
    }

    public abstract void update();
    protected abstract void setDetail(Object detail);
    protected abstract String getKey();
}
