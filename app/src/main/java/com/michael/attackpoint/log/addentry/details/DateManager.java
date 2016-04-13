package com.michael.attackpoint.log.addentry.details;

import android.view.View;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.util.Singleton;
import com.michael.attackpoint.log.loginfo.LogDate;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.addentry.TrainingDatePicker;
import com.michael.attackpoint.log.addentry.TrainingPicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class DateManager extends PickerManager<LogDate> {

    public DateManager(ViewHolder.SubViewHolder svh, LogDate detail) {
        super(svh, detail);
    }

    public DateManager(ViewHolder.SubViewHolder svh, LogInfo detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    @Override
    public void setDetail(Object detail) {
        if (detail instanceof Calendar) mDetail.set((Calendar) detail);
    }

    @Override
    protected String getKey() {
        return LogInfo.KEY_DATE;
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
                TrainingPicker newFragment = new TrainingDatePicker();
                newFragment.setManager(mSelf);
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "datePicker");
            }
        };
        return listener;
    }
}
