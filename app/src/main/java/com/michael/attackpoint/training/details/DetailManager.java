package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.michael.attackpoint.log.loginfo.LogInfoItem;

/**
 * Created by michael on 2/25/16.
 */
public abstract class DetailManager<T extends LogInfoItem> {
    TextView mTextView;
    View mParent;
    T mDetail;
    final DetailManager mSelf;

    public DetailManager(ViewHolder.SubViewHolder svh, T detail) {
        mTextView = (TextView) svh.item;
        mParent = svh.parent;
        mDetail = detail;
        mSelf = this;

        mParent.setOnClickListener(createListener());

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

    public TextView getTextView() {
        return mTextView;
    }

    public abstract void update();
    protected abstract void setDetail(Object detail);
    protected abstract View.OnClickListener createListener();
}
