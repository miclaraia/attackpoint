package com.michael.attackpoint.log.addentry;

import android.app.DialogFragment;

import com.michael.attackpoint.log.addentry.details.DetailManager;

/**
 * Created by michael on 3/8/16.
 */
public abstract class TrainingPicker extends DialogFragment {

    protected DetailManager mManager;

    public void setManager(DetailManager manager) {
        mManager = manager;
    }
}
