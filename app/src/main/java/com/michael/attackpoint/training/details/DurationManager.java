package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.Duration;
import com.michael.attackpoint.training.TrainingDurationPicker;

import java.util.Calendar;

/**
 * Created by michael on 2/25/16.
 */
public class DurationManager extends DetailManager<Duration> {

    public DurationManager(ViewHolder.SubViewHolder svh, Duration detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    public void updateDetail(Calendar cal) {
        mDetail.setCalendar(cal);
        update();
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TrainingDurationPicker();
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "durationPicker");
            }
        };
        return listener;
    }
}
