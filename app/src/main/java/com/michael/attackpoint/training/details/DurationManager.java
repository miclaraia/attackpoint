package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogDuration;
import com.michael.attackpoint.training.TrainingDurationPicker;
import com.michael.attackpoint.training.TrainingPicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class DurationManager extends DetailManager<LogDuration> {

    public DurationManager(ViewHolder.SubViewHolder svh, LogDuration detail) {
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
