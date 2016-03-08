package com.michael.attackpoint.training;

import android.app.DialogFragment;

import com.michael.attackpoint.training.details.DetailManager;

/**
 * Created by michael on 3/8/16.
 */
public abstract class TrainingPicker extends DialogFragment {

    protected DetailManager mManager;

    public void setManager(DetailManager manager) {
        mManager = manager;
    }
}
