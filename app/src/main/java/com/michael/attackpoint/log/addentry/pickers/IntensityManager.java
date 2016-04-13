package com.michael.attackpoint.log.addentry.pickers;

import android.app.FragmentManager;
import android.view.View;

import com.michael.attackpoint.log.addentry.activity.ViewHolder;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogIntensity;

/**
 * Created by michael on 4/13/16.
 */
public class IntensityManager extends GenericPickerManager<LogIntensity> {

    public IntensityManager(ManagerContract.Activity activity, LogIntensity logItem) {
        super(activity, logItem);
    }

    @Override
    public Class<LogIntensity> getTClass() {
        return LogIntensity.class;
    }

    @Override
    public String getLogInfoKey() {
        return LogInfo.KEY_INTENSITY;
    }

    @Override
    public ViewHolder.SubViewHolder getViewHolder(ViewHolder vh) {
        return vh.intensity;
    }

    @Override
    public void setClickListener() {
        final ManagerContract.Manager manager = this;
        final FragmentManager fm = mActivity.getFragmentManager();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntensityPicker dialog = new IntensityPicker();
                dialog.setManager(manager);
                dialog.show(fm, "durationmanager");
            }
        };
        mSubViewHolder.setClickListener(listener);
    }
}
