package com.michael.attackpoint.log.addentry.pickers;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.LogDate;

import java.util.Calendar;

/**
 * Created by michael on 4/13/16.
 */
public class DateSessionPicker extends DialogFragment implements ManagerContract.Picker,
        TimePickerDialog.OnTimeSetListener {

    private ManagerContract.Manager mManager;

    @Override
    public void setManager(ManagerContract.Manager manager) {
        mManager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int hour = ((Calendar)mManager.getItem().get()).get(Calendar.HOUR_OF_DAY);
        // Create a new instance of DatePickerDialog and return it
        TimePickerDialog mDialog = new TimePickerDialog(getActivity(), R.style.AppTheme_AlertDialog, this, hour, 0, true);
        return mDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        LogDate logDate = (LogDate) mManager.getItem();
        logDate.setSession(hourOfDay);
        mManager.setItem(logDate);
    }
}
