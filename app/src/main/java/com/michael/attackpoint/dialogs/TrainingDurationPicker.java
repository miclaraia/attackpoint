package com.michael.attackpoint.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.michael.attackpoint.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by michael on 8/27/15.
 */
public class TrainingDurationPicker extends DialogFragment {
    private static final String DEBUG_TAG = "attackpoint.TP";
    private static final String TIME_FORMAT_H = "HH:mm:ss";
    private static final String TIME_FORMAT_M = "mm:ss";
    private static final String TIME_FORMAT_S = "ss";
    private Dialog dialog;

    private TextView result;
    private Calendar duration;

    private NumberPicker hour;
    private NumberPicker minute;
    private NumberPicker second;

    private HashMap<String, NumberPicker> pickers;

    public Calendar parseTime(String time) {
        try {
            String parse = "";
            SimpleDateFormat sdf;
            switch (time.split(":").length) {
                case 1:
                    sdf = new SimpleDateFormat(TIME_FORMAT_S);
                    break;
                case 2:
                    sdf = new SimpleDateFormat(TIME_FORMAT_M);
                    break;
                case 3:
                    sdf = new SimpleDateFormat(TIME_FORMAT_H);
                    break;
                default:
                    sdf = new SimpleDateFormat();
            }
            sdf.parse(time);
            return sdf.getCalendar();
        } catch (ParseException e) {
            Log.e(DEBUG_TAG, "Error tring to parse duration time");
            e.printStackTrace();
            return null;
        }
    }

    public void setResultView(TextView result) {
        this.result = result;
    }

    private EditText findInput(ViewGroup np) {
        int count = np.getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = np.getChildAt(i);
            if (child instanceof ViewGroup) {
                findInput((ViewGroup) child);
            } else if (child instanceof EditText) {
                return (EditText) child;
            }
        }
        return null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        String time = (String) bundle.get("time_string");
        duration = parseTime(time);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);
        builder.setTitle("Select an Intensity");

        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.time_picker, null);

        NumberPicker h = (NumberPicker) view.findViewById(R.id.np_picker_h);
        NumberPicker m = (NumberPicker) view.findViewById(R.id.np_picker_m);
        NumberPicker s = (NumberPicker) view.findViewById(R.id.np_picker_s);

        h.setMaxValue(59);
        m.setMaxValue(59);
        s.setMaxValue(59);

        h.setMinValue(0);
        m.setMinValue(0);
        s.setMinValue(0);

        h.setWrapSelectorWheel(false);
        m.setWrapSelectorWheel(false);
        s.setWrapSelectorWheel(false);

        h.setValue(duration.get(Calendar.HOUR));
        m.setValue(duration.get(Calendar.MINUTE));
        s.setValue(duration.get(Calendar.SECOND));

        try {
            EditText hh = findInput(h);
            hh.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            h.setOnFocusChangeListener(new FocusListener(hh));

            EditText mm = findInput(m);
            mm.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            m.setOnFocusChangeListener(new FocusListener(mm));

            EditText ss = findInput(s);
            ss.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            s.setOnFocusChangeListener(new FocusListener(ss));
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Error setting soft keyboard buttons");
            e.printStackTrace();
        }

        hour = h;
        minute = m;
        second = s;

        view.findViewById(R.id.dialog_buttonA).setOnClickListener(new ClickListener());
        view.findViewById(R.id.dialog_buttonC).setOnClickListener(new ClickListener());

        builder.setView(view);
        dialog = builder.create();
        return dialog;
    }

    private class FocusListener implements View.OnFocusChangeListener {
        private EditText next;

        public FocusListener(EditText next) {
            this.next = next;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {

                //TODO somehow prevent keyboard from disappearing
                next.requestFocus();
            }
        }
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // cancel
                case R.id.dialog_buttonC:
                    Log.d(DEBUG_TAG, "cancel pressed");
                    dialog.dismiss();
                    break;
                // login
                case R.id.dialog_buttonA:
                    Log.d(DEBUG_TAG, "accept pressed");
                    if (result == null) {
                        Log.e(DEBUG_TAG, "result view not set for TrainingDurationPicker");
                        result = (TextView) getActivity().findViewById(R.id.training_duration).findViewById(R.id.item);
                    }
                    duration.set(0, 0, 0, hour.getValue(), minute.getValue(), second.getValue());
                    DateFormat sdf = new SimpleDateFormat(TIME_FORMAT_H);
                    result.setText(sdf.format(duration.getTime()));
                    result.setTag(duration);
                    dialog.dismiss();
                    break;
            }
        }
    }
}
