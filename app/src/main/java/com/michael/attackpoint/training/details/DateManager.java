package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.training.TrainingDatePicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class DateManager extends DetailManager<LogDate> {
    private static final String DATE_FORMAT = "dd MMM, yyyy";

    public DateManager(ViewHolder.SubViewHolder svh, LogDate detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    public void updateDetail(Calendar cal) {
        mDetail.set(cal);
        update();
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TrainingDatePicker();
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "datePicker");
            }
        };
        return listener;
    }
}
