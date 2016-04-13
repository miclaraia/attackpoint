package com.michael.attackpoint.log.addentry.pickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.michael.attackpoint.R;
import com.michael.attackpoint.log.loginfo.LogInfo;
import com.michael.attackpoint.log.loginfo.LogIntensity;

/**
 * Created by michael on 4/13/16.
 */
public class IntensityPicker extends DialogFragment implements ManagerContract.Picker {
    private static final String DEBUG_TAG = "intensitypicker";
    private ManagerContract.Manager mManager;
    private Dialog mDialog;

    private NumberPicker np;

    @Override
    public void setManager(ManagerContract.Manager manager) {
        mManager = manager;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_AlertDialog);
        builder.setTitle("Select an Intensity");

        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.picker_number, null);

        Activity activity = getActivity();
        view.findViewById(R.id.dialog_buttonA).setOnClickListener(clickListener);
        view.findViewById(R.id.dialog_buttonC).setOnClickListener(clickListener);

        builder.setView(view);
        np = (NumberPicker) view.findViewById(R.id.np_picker);
        np.setMaxValue(5);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setValue(3);
        mDialog = builder.create();
        return mDialog;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // cancel
                case R.id.dialog_buttonC:
                    Log.d(DEBUG_TAG, "cancel pressed");
                    mDialog.dismiss();
                    break;
                // login
                case R.id.dialog_buttonA:
                    Log.d(DEBUG_TAG, "accept pressed");

                    LogIntensity logIntensity = (LogIntensity) mManager.getItem();
                    logIntensity.set(np.getValue());
                    mManager.setItem(logIntensity);

                    mDialog.dismiss();
                    break;
            }
        }
    };
}
