package com.michael.attackpoint.log.addentry.pickers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 4/13/16.
 */
public class DistanceUnitPicker extends DialogFragment implements ManagerContract.Picker {

    private ManagerContract.Manager mManager;

    @Override
    public void setManager(ManagerContract.Manager manager) {
        mManager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);

        final List<Unit> units = Unit.UnitManager.getUnitList();

        builder.setTitle("Select Unit");
        builder.setItems(toStringArray(units), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                Unit selected = units.get(i);
                ((DistanceManager) mManager).setUnit(selected);
            }
        });
        return builder.create();
    }

    private static String[] toStringArray(List<Unit> units) {
        String[] array = new String[units.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = units.get(i).toNickname();
        }

        return array;
    }
}
