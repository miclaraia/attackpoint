package com.michael.attackpoint.log.addentry.details;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoItem;

/**
 * Created by michael on 3/11/16.
 */
public abstract class EditTextManager<T extends LogInfoItem> extends DetailManager<T> {
    protected EditText mEditText;

    public EditTextManager(Context context, ViewHolder.SubViewHolder svh, T detail) {
        super(svh, detail);
        init(context, svh);
    }

    public EditTextManager(Context context, ViewHolder.SubViewHolder svh, LogInfo detail) {
        super(svh, detail);
        init(context, svh);
    }

    private void init(Context context, ViewHolder.SubViewHolder svh) {
        mEditText = (EditText) svh.item;

        final View fItem = svh.item;
        final Context fContext = context;
        svh.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fItem.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) fContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(fItem, 0);
            }
        });
    }

    @Override
    public T getDetail() {
        mDetail = onGetDetail();
        return mDetail;
    }

    @Override
    public void update(){}

    public abstract T onGetDetail();
}
