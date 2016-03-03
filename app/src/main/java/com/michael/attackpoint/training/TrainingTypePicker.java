package com.michael.attackpoint.training;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import com.michael.attackpoint.R;
import com.michael.attackpoint.training.details.ActivityManager;
import com.michael.attackpoint.training.details.DateManager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingTypePicker extends DialogFragment {

    private DatePickerDialog mDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        ActivityTable table = new ActivityTable();
        final ArrayList<String> items = table.getAllNames();
        //String[] itemArray = items.toArray(new String[items.size()]);

        builder.setTitle("Select Activity Type");
        builder.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String item = items.get(which);
                        ActivityManager manager = (ActivityManager) getActivity().findViewById(R.id.training_activity).getTag();
                        manager.updateDetail(item);
                    }
                });
        return builder.create();
    }
}
