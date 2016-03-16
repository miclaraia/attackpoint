package com.michael.attackpoint.training.details;

import android.view.View;

import com.michael.attackpoint.util.Singleton;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.training.TrainingPicker;
import com.michael.attackpoint.training.TrainingTypePicker;

/**
 * Created by michael on 2/25/16.
 */
public class ActivityManager extends PickerManager<LogInfoActivity> {

    public ActivityManager(ViewHolder.SubViewHolder svh, LogInfoActivity detail) {
        super(svh, detail);
        ActivityTable table = new ActivityTable();
        String first = table.getFirst();
        mDetail.set(first);
        update();
    }

    public ActivityManager(ViewHolder.SubViewHolder svh, LogInfo logInfo) {
        super(svh, logInfo);
        ActivityTable table = new ActivityTable();
        String first = table.getFirst();
        mDetail.set(first);
        update();
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    @Override
    public void setDetail(Object detail) {
        if (detail instanceof String) {
            mDetail.set((String) detail);
        }
    }

    @Override
    protected String getKey() {
        return LogInfo.KEY_ACTIVITY;
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingPicker newFragment = new TrainingTypePicker();
                newFragment.setManager(mSelf);
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "typePicker");
            }
        };
        return listener;
    }
}
