package com.michael.attackpoint.log.addentry;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.michael.attackpoint.R;

import java.util.ArrayList;

/**
 * Created by michael on 8/25/15.
 */
public class TrainingTypePicker extends TrainingPicker {

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
                        mManager.updateDetail(item);
                    }
                });
        return builder.create();
    }
}
