package com.michael.attackpoint.log.addentry.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.michael.attackpoint.log.loginfo.LogDate;

import java.util.Calendar;

/**
 * Created by michael on 4/6/16.
 */
public class DatePicker extends DialogFragment implements ManagerContract.Picker,
        DatePickerDialog.OnDateSetListener {

    private ManagerContract.Manager mManager;

    @Override
    public void setManager(ManagerContract.Manager manager) {
        mManager = manager;
    }
    private DatePickerDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        LogDate logDate = (LogDate) mManager.getItem();
        Calendar cal = logDate.get();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        mDialog =  new DatePickerDialog(getActivity(), DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);

        android.widget.DatePicker pickerView = mDialog.getDatePicker();
        pickerView.setCalendarViewShown(true);
        pickerView.setSpinnersShown(false);

        return mDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(0,0,0,0,0,0);
        cal.set(year, monthOfYear, dayOfMonth);

        LogDate logDate = (LogDate) mManager.getItem();
        logDate.set(cal);
        mManager.setItem(logDate);
    }
}
