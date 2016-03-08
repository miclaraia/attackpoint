package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogIntensity;
import com.michael.attackpoint.training.IntensityPicker;
import com.michael.attackpoint.training.TrainingPicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class IntensityManager extends DetailManager<LogIntensity> {

    public IntensityManager(ViewHolder.SubViewHolder svh, LogIntensity detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    @Override
    protected void setDetail(Object detail) {
        if (detail instanceof Integer) mDetail.set((Integer) detail);
    }

    public void updateDetail(Integer intensity) {
        mDetail.set(intensity);
        update();
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingPicker newFragment = new IntensityPicker();
                newFragment.setManager(mSelf);
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "intensityPicker");
            }
        };
        return listener;
    }
}
