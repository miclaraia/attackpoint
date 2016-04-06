package com.michael.attackpoint.log.addentry.pickers;

import android.app.DialogFragment;

/**
 * Created by michael on 4/6/16.
 */
public class PickerGenerator {

    static DialogFragment genDatePicker(ManagerContract.PickerManager manager) {
        DatePicker dialog = new DatePicker();
        dialog.setManager(manager);
        return dialog;
    }

    static DialogFragment genActivityPicker(ManagerContract.PickerManager manager) {
        ActivityPicker dialog = new ActivityPicker();
        dialog.setManager(manager);
        return dialog;
    }
}
