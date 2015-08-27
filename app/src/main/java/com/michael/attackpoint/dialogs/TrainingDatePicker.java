package com.michael.attackpoint.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.michael.attackpoint.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final String DATE_FORMAT = "dd MMM, yyyy";
    private static final int PICK_TODAY = 1;
    private static final int RESULT_OK = 10;
    private DatePickerDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        dialog =  new DatePickerDialog(getActivity(), DatePickerDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);
        DatePicker picker = dialog.getDatePicker();
        picker.setCalendarViewShown(true);
        picker.setSpinnersShown(false);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        TextView dateView = (TextView) getActivity()
                .findViewById(R.id.training_date).findViewById(R.id.item);
        dateView.setText(sdf.format(date));
        dateView.setTag(date);
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_TODAY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }*/
}
