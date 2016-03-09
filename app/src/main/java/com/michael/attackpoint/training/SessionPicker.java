package com.michael.attackpoint.training;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import com.michael.attackpoint.R;

import java.util.Calendar;

/**
 * Created by michael on 3/9/16.
 */
public class SessionPicker extends TrainingPicker implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        // Create a new instance of DatePickerDialog and return it
        TimePickerDialog mDialog = new TimePickerDialog(getActivity(), R.style.AppTheme_AlertDialog, this, hour, 0, true);
        return mDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        mManager.updateDetail(cal);
    }
}
