package com.michael.attackpoint.log.addentry.pickers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.LogDuration;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by michael on 4/13/16.
 */
public class DurationPicker extends DialogFragment implements ManagerContract.Picker {
    private static final String DEBUG_TAG = "durationpicker";
    private ManagerContract.Manager mManager;
    private Dialog mDialog;

    private NumberPicker hour;
    private NumberPicker minute;
    private NumberPicker second;

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
    public void setManager(ManagerContract.Manager manager) {
        mManager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar cal = ((LogDuration) mManager.getItem()).get();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);
        builder.setTitle("Select an Intensity");

        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.picker_time, null);

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

        h.setValue(cal.get(Calendar.HOUR_OF_DAY));
        m.setValue(cal.get(Calendar.MINUTE));
        s.setValue(cal.get(Calendar.SECOND));

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
        mDialog = builder.create();
        return mDialog;
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
                    mDialog.dismiss();
                    break;
                // login
                case R.id.dialog_buttonA:
                    Log.d(DEBUG_TAG, "accept pressed");

                    Calendar cal = Calendar.getInstance();
                    cal.set(0, 0, 0, hour.getValue(), minute.getValue(), second.getValue());

                    LogDuration logDuration = (LogDuration) mManager.getItem();
                    logDuration.set(cal);

                    mManager.setItem(logDuration);
                    mDialog.dismiss();
                    break;
            }
        }
    }
}
