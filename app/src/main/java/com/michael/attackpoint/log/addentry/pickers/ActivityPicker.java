package com.michael.attackpoint.log.addentry.pickers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.addentry.ActivityTable;
import com.michael.attackpoint.log.loginfo.LogInfoActivity;

import java.util.ArrayList;

/**
 * Created by michael on 4/6/16.
 */
public class ActivityPicker extends DialogFragment implements ManagerContract.Picker {

    private ManagerContract.Manager mManager;

    @Override
    public void setManager(ManagerContract.Manager manager) {
        mManager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        ActivityTable table = new ActivityTable();
        final ArrayList<String> items = table.getAllNames();
        //String[] itemArray = items.toArray(new String[items.size()]);

        builder.setTitle("Select Activity Type");
        builder.setItems(items.toArray(new String[items.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String activity = items.get(which);

                LogInfoActivity logInfoActivity = (LogInfoActivity) mManager.getItem();
                logInfoActivity.set(activity);

                mManager.setItem(logInfoActivity);
            }
        });
        return builder.create();
    }
}
