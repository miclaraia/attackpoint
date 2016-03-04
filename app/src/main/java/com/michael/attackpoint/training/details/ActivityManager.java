package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;
import com.michael.attackpoint.training.ActivityTable;
import com.michael.attackpoint.training.TrainingDatePicker;
import com.michael.attackpoint.training.TrainingTypePicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class ActivityManager extends DetailManager<LogInfoActivity> {
    private static final String DATE_FORMAT = "dd MMM, yyyy";

    public ActivityManager(ViewHolder.SubViewHolder svh, LogInfoActivity detail) {
        super(svh, detail);
        ActivityTable table = new ActivityTable();
        String first = table.getFirst();
        mDetail.set(first);
        update();
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    public void updateDetail(String detail) {
        mDetail.set(detail);
        update();
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TrainingTypePicker();
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "typePicker");
            }
        };
        return listener;
    }
}
