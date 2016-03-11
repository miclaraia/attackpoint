package com.michael.attackpoint.training.details;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.LogDistance;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.Unit;

/**
 * Created by michael on 3/11/16.
 */
public class DistanceManager extends EditTextManager<LogDistance> {

    public DistanceManager(Context context, ViewHolder.SubViewHolder svh, LogDistance detail) {
        super(context, svh, detail);
    }

    public DistanceManager(Context context, ViewHolder.SubViewHolder svh, LogInfo detail) {
        super(context, svh, detail);
    }

    @Override
    public LogDistance onGetDetail() {
        LogDistance.Distance distance = mDetail.get();

        String text = mEditText.getText().toString();
        if (!text.equals("")) {
            distance.setDistance(Double.parseDouble(text));

            String unit = ((TextView) mSVH.parent.findViewById(R.id.unit)).getText().toString();
            distance.setUnit(Unit.UnitManager.getUnit(unit));

        } else distance.setDistance(0.0);

        mDetail.set(distance);
        return mDetail;
    }

    @Override
    protected void setDetail(Object detail) {
        if (detail instanceof LogDistance.Distance)
            mDetail.set((LogDistance.Distance) detail);
    }

    @Override
    protected String getKey() {
        return LogInfo.KEY_DISTANCE;
    }
}
