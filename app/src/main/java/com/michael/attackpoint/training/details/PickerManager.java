package com.michael.attackpoint.training.details;

import android.view.View;
import android.widget.TextView;

import com.michael.attackpoint.log.loginfo.LogInfoItem;

/**
 * Created by michael on 3/11/16.
 */
public abstract class PickerManager<T extends LogInfoItem> extends DetailManager<T> {
    final PickerManager mSelf;
    protected TextView mTextView;

    public PickerManager(ViewHolder.SubViewHolder svh, T detail) {
        super(svh, detail, false);
        mTextView = (TextView) svh.item;
        mDetail = detail;
        mSelf = this;

        mSVH.parent.setOnClickListener(createListener());

        update();
    }

    public TextView getTextView() {
        return mTextView;
    }

    protected abstract View.OnClickListener createListener();
}
