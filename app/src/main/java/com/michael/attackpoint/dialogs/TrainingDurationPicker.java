package com.michael.attackpoint.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.michael.attackpoint.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michael on 8/27/15.
 */
public class TrainingDurationPicker extends DialogFragment {
    private static final String DEBUG_TAG = "attackpoint.TP";
    private static final String TIME_FORMAT = "kk:mm:ss";
    private Dialog dialog;
    private HashMap<String, NumberPicker> pickers;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select an Intensity");
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.time_picker, null);

        Activity activity = getActivity();
        view.findViewById(R.id.dialog_buttonA).setOnClickListener(new ClickListener(activity));
        view.findViewById(R.id.dialog_buttonC).setOnClickListener(new ClickListener(activity));

        builder.setView(view);
        np = (NumberPicker) view.findViewById(R.id.np_picker);
        np.setMaxValue(5);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setValue(3);
        dialog = builder.create();
        return dialog;
    }

    private class ClickListener implements View.OnClickListener {
        private TextView intensity;

        public ClickListener(Activity activity) {
            intensity = (TextView) activity
                    .findViewById(R.id.training_intensity).findViewById(R.id.item);
        }

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
                    intensity.setText("" + np.getValue());
                    dialog.dismiss();
                    break;
            }
        }
    }
}
