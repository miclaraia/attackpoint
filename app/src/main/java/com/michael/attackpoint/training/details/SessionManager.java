package com.michael.attackpoint.training.details;

import android.view.View;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogSession;
import com.michael.attackpoint.training.SessionPicker;
import com.michael.attackpoint.training.TrainingPicker;

import java.util.Calendar;

/**
 * Created by michael on 3/9/16.
 */
public class SessionManager extends PickerManager<LogSession> {
    public SessionManager(ViewHolder.SubViewHolder svh, LogSession detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(getDetail().toString());
    }

    @Override
    public void setDetail(Object detail) {
        if (detail instanceof Calendar) mDetail.set((Calendar) detail);
    }

    @Override
    protected String getKey() {
        return LogInfo.KEY_SESSION;
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainingPicker newFragment = new SessionPicker();
                newFragment.setManager(mSelf);
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "sessionPicker");
            }
        };
        return listener;
    }
}
