package com.michael.attackpoint.training.details;

import android.app.DialogFragment;
import android.view.View;

import com.michael.attackpoint.Singleton;
import com.michael.attackpoint.training.NumberPickerDialog;

/**
 * Created by michael on 2/25/16.
 */
public class IntensityManager extends DetailManager<Integer> {

    public IntensityManager(ViewHolder.SubViewHolder svh, Integer detail) {
        super(svh, detail);
    }

    @Override
    public void update() {
        mTextView.setText(mDetail.toString());
    }

    @Override
    protected View.OnClickListener createListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new NumberPickerDialog();
                newFragment.show(Singleton.getInstance().getActivity().getFragmentManager(), "intensityPicker");
            }
        };
        return listener;
    }
}
