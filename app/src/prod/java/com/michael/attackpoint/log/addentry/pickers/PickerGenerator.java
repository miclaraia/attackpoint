package com.michael.attackpoint.log.addentry.pickers;

import android.app.DialogFragment;

/**
 * Created by michael on 4/6/16.
 */
public class PickerGenerator {

    static DialogFragment genDatePicker(ManagerContract.Manager manager) {
        DatePicker dialog = new DatePicker();
        dialog.setManager(manager);
        return dialog;
    }

    static DialogFragment genActivityPicker(ManagerContract.Manager manager) {
        ActivityPicker dialog = new ActivityPicker();
        dialog.setManager(manager);
        return dialog;
    }

    static DialogFragment genUnitPicker(ManagerContract.Manager manager) {
        DistanceUnitPicker dialog = new DistanceUnitPicker();
        dialog.setManager(manager);
        return dialog;
    }
}
