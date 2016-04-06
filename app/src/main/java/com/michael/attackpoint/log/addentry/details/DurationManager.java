package com.michael.attackpoint.log.addentry.details;

import android.view.View;

import com.michael.attackpoint.util.Singleton;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.addentry.TrainingDurationPicker;
import com.michael.attackpoint.log.addentry.TrainingPicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class DurationManager extends PickerManager<LogDuration> {

    public DurationManager(ViewHolder.SubViewHolder svh, LogDuration detail) {
        super(svh, detail);
    }

    public DurationManager(ViewHolder.SubViewHolder svh, LogInfo detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    @Override
    protected void setDetail(Object detail) {
        if (detail instanceof Calendar) mDetail.set((Calendar) detail);
    }

    @Override
    protected String getKey() {
        return LogInfo.KEY_DURATION;
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
                TrainingPicker newFragment = new TrainingDurationPicker();
                newFragment.setManager(mSelf);
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "durationPicker");
            }
        };
        return listener;
    }
}